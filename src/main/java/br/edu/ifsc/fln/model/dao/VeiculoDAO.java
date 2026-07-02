package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class VeiculoDAO extends GenericDAO {

    public void inserir(Veiculo veiculo) throws DAOException {
        execute(session -> {
            session.persist(veiculo);
            return null;
        });
    }

    public void alterar(Veiculo veiculo) throws DAOException {
        execute(session -> {
            session.merge(veiculo);
            return null;
        });
    }

    public void remover(int id) throws DAOException {
        execute(session -> {

            Veiculo veiculo = session.find(Veiculo.class, id);
            if (veiculo != null) {
                session.remove(veiculo);
            }
            return null;
        });
    }

    public void remover(Veiculo veiculo) throws DAOException {
        remover(veiculo.getId());
    }

    public Veiculo buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Veiculo.class, id);
        }
    }

    public Veiculo buscarPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Veiculo WHERE upper(placa) = :placa", Veiculo.class)
                    .setParameter("placa", placa.trim().toUpperCase())
                    .uniqueResult(); // Retorna o veículo ou null se não encontrar
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Veiculo> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Veiculo", Veiculo.class).list();
        }
    }

    public List<Veiculo> listarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Veiculo WHERE lower(placa) LIKE :placa",
                            Veiculo.class
                    ).setParameter("placa", "%" + nome.toLowerCase() + "%")
                    .list();
        }
    }

    public List listarVeiculosPorClienteId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT v FROM Veiculo v " +
                                    "LEFT JOIN FETCH v.modelo " +
                                    "LEFT JOIN FETCH v.cor " +
                                    "LEFT JOIN FETCH v.cliente c " +
                                    "WHERE v.id = :id",
                            Veiculo.class
                    )
                    .setParameter("id", id)
                    .list();
        }
    }
}