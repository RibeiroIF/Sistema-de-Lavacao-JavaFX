package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class ClienteDAO extends GenericDAO {

    public void inserir(Cliente cliente) throws DAOException {
        execute(session -> {
            session.persist(cliente);
            return null;
        });
    }

    public void alterar(Cliente cliente) throws DAOException {
        execute(session -> {
            session.merge(cliente);
            return null;
        });
    }

    public void remover(int id) throws DAOException {
        execute(session -> {
            Cliente cliente = session.find(Cliente.class, id);
            if (cliente != null) {
                session.remove(cliente);
            }
            return null;
        });
    }

    public void remover(Cliente cliente) throws DAOException {
        remover(cliente.getId());
    }

    public Cliente buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Cliente.class, id);
        }
    }

    public PessoaFisica buscarPorCpf(String cpf) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM PessoaFisica WHERE cpf = :cpf", PessoaFisica.class)
                    .setParameter("cpf", cpf)
                    .uniqueResult();
        }
    }

    public PessoaJuridica buscarPorCnpj(String cnpj) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM PessoaJuridica WHERE cnpj = :cnpj", PessoaJuridica.class)
                    .setParameter("cnpj", cnpj)
                    .uniqueResult();
        }
    }

    public Cliente buscarVeiculos(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT c FROM Cliente c " +
                                    "LEFT JOIN FETCH c.ListaDeVeiculos " +
                                    "WHERE c.id = :id",
                            Cliente.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public List<Cliente> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Cliente", Cliente.class).list();
        }
    }

}