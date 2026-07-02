package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.*;
import br.edu.ifsc.fln.model.service.ParametrosService;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdemServicoDAO extends GenericDAO {

    public boolean inserir(OrdemServico ordemServico) throws DAOException {
        return execute(session -> {

            Veiculo veiculo = session.find(Veiculo.class, ordemServico.getVeiculo().getId());
            ordemServico.setVeiculo(veiculo);

            for (ItemOS item : ordemServico.getItens()) {
                Servico servico = session.find(Servico.class, item.getServico().getId());
                double valorAlterado = item.getValorServico();
                item.setServico(servico);
                item.setOrdemServico(ordemServico);
                item.setValorServico(valorAlterado);
            }

            ordemServico.calcularTotal();

            Cliente cliente = veiculo.getCliente();

            if (cliente != null && cliente.getPontuacao() != null) {
                int pontosPorServico = ParametrosService.getPontos();
                int quantidadeServicos = ordemServico.getItens().size();
                int pontosASomar = quantidadeServicos * pontosPorServico;
                cliente.getPontuacao().somarPontos(pontosASomar);
                session.merge(cliente);
            }

            session.persist(ordemServico);
            return true;
        });
    }

    public boolean alterar(OrdemServico ordemNova) throws DAOException {
        return execute(session -> {
            OrdemServico ordemServicoDB = session.createQuery(
                            "SELECT DISTINCT v FROM OrdemServico v " +
                                    "LEFT JOIN FETCH v.itens iv " +
                                    "LEFT JOIN FETCH iv.servico " +
                                    "WHERE v.id = :id",
                            OrdemServico.class).setParameter("id", ordemNova.getId()).uniqueResult();

            if (ordemServicoDB == null) {
                throw new DAOException("Venda não encontrada.");
            }
            if ((ordemServicoDB.getStatus() == EStatus.FECHADA || ordemServicoDB.getStatus() == EStatus.CANCELADA) && ordemNova.getStatus() != EStatus.ABERTA) {
                throw new RuntimeException("Ordem de serviço não aceita alterações");
            }

            Veiculo veiculo = session.find(Veiculo.class, ordemNova.getVeiculo().getId());
            ordemServicoDB.setVeiculo(veiculo);
            ordemServicoDB.setAgenda(ordemNova.getAgenda());
            ordemServicoDB.setDesconto(ordemNova.getDesconto());
            ordemServicoDB.setStatus(ordemNova.getStatus());

            ordemServicoDB.getItens().removeIf(itemAntigo -> ordemNova.getItens().stream().noneMatch(itemNovo ->
            itemNovo.getId() == itemAntigo.getId()));

            for (ItemOS novoItem : ordemNova.getItens()) {
                Servico servico = session.find(Servico.class, novoItem.getServico().getId());
                novoItem.setServico(servico);
                novoItem.setValorServico(novoItem.getValorServico());
                novoItem.setOrdemServico(ordemServicoDB);

                if (novoItem.getId() == 0) {
                    ordemServicoDB.getItens().add(novoItem);
                } else {
                    for (ItemOS itemDB : ordemServicoDB.getItens()) {
                        if (itemDB.getId() == novoItem.getId()) {
                            itemDB.setValorServico(novoItem.getValorServico());
                            itemDB.setObservacoes(novoItem.getObservacoes());
                            itemDB.setServico(servico);
                        }
                    }
                }
            }

            ordemServicoDB.calcularTotal();
            session.merge(ordemServicoDB);
            return true;
        });
    }

    // LISTAR TODAS
    public List<OrdemServico> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT DISTINCT v FROM OrdemServico v " +
                            "LEFT JOIN FETCH v.itens " +
                            "ORDER BY v.agenda DESC",
                    OrdemServico.class
            ).getResultList();
        }
    }

    // REMOVER
    public boolean remover(OrdemServico ordemServico) throws DAOException {
        return execute(session -> {
            OrdemServico ordemServicoDB = session.createQuery(
                            "SELECT DISTINCT v FROM OrdemServico v " +
                                    "LEFT JOIN FETCH v.itens iv " +
                                    "LEFT JOIN FETCH iv.servico " +
                                    "WHERE v.id = :id",
                            OrdemServico.class).setParameter("id", ordemServico.getId()).uniqueResult();

            if (ordemServicoDB == null) {
                throw new DAOException("Ordem de Serviço não encontrada");
            }
            session.remove(ordemServicoDB);
            return true;
        });
    }

    //LISTAR ORDENS MENSAIS
    public Map<Integer, ArrayList<Integer>> listarQuantidadeOrdensMensais() {
        Map<Integer, ArrayList<Integer>> retorno = new HashMap<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Object[]> resultados = session.createQuery(
                    "SELECT COUNT(v.id), YEAR(v.agenda), MONTH(v.agenda) " +
                            "FROM OrdemServico v " +
                            "GROUP BY YEAR(v.agenda), MONTH(v.agenda) " +
                            "ORDER BY YEAR(v.agenda), MONTH(v.agenda)",
                    Object[].class
            ).getResultList();

            for (Object[] row : resultados) {

                Long count = (Long) row[0];
                Integer ano = (Integer) row[1];
                Integer mes = (Integer) row[2];

                if (!retorno.containsKey(ano)) {
                    ArrayList<Integer> linha = new ArrayList<>();
                    linha.add(mes);
                    linha.add(count.intValue());
                    retorno.put(ano, linha);
                } else {
                    ArrayList<Integer> linha = retorno.get(ano);
                    linha.add(mes);
                    linha.add(count.intValue());
                }
            }

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retorno;
    }

    //LISTAR LUCRO MENSAL BASEADO NO VALOR DAS ORDENS
    public Map<Integer, ArrayList<Double>> listarLucroOrdensMensais() {
        Map<Integer, ArrayList<Double>> retorno = new HashMap<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Object[]> resultados = session.createQuery(
                    "SELECT SUM(v.total), YEAR(v.agenda), MONTH(v.agenda) " +
                            "FROM OrdemServico v " +
                            "GROUP BY YEAR(v.agenda), MONTH(v.agenda) " +
                            "ORDER BY YEAR(v.agenda), MONTH(v.agenda)",
                    Object[].class
            ).getResultList();

            for (Object[] row : resultados) {

                Double somaTotal = (Double) row[0];
                Integer ano = (Integer) row[1];
                Integer mes = (Integer) row[2];

                if (somaTotal == null) {
                    somaTotal = 0.0;
                }

                if (!retorno.containsKey(ano)) {
                    ArrayList<Double> linha = new ArrayList<>();
                    linha.add(mes.doubleValue());
                    linha.add(somaTotal);
                    retorno.put(ano, linha);
                } else {
                    ArrayList<Double> linha = retorno.get(ano);
                    linha.add(mes.doubleValue());
                    linha.add(somaTotal);
                }
            }

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retorno;
    }

    public OrdemServico buscarServicosPorOrdem(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT s FROM OrdemServico s " +
                                    "LEFT JOIN FETCH s.itens " +
                                    "WHERE s.id = :id",
                            OrdemServico.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

}