package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ModeloDAO extends GenericDAO {

    //INSERIR
    public void inserir(Modelo modelo) throws DAOException {
        execute(session -> {
            session.persist(modelo);
            return null;
        });
    }

    //ATUALIZAR
    public void alterar(Modelo modelo) throws DAOException {
        execute(session -> {
            session.merge(modelo);
            return null;
        });
    }

    //REMOVER
    public void remover(Modelo modelo) throws DAOException {
        execute(session -> {
            Modelo m = session.contains(modelo)
                    ? modelo
                    : session.merge(modelo);
            session.remove(m);

            return null;
        });
    }

    //LISTAR TODOS
    public List<Modelo> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Modelo ORDER BY descricao",
                    Modelo.class
            ).list();
        }
    }

    //BUSCAR POR ID
    public Modelo buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Modelo.class, id);
        }
    }

    //LISTAR POR NOME
    public List<Modelo> listarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Modelo WHERE lower(descricao) LIKE :nome ORDER BY descricao",
                            Modelo.class
                    ).setParameter("nome", "%" + nome.toLowerCase() + "%")
                    .list();
        }
    }

    //PESQUISA (nome + descrição)
    public List<Modelo> pesquisar(String termo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Modelo " +
                                    "WHERE lower(descricao) LIKE :termo " +
                                    "ORDER BY descricao",
                            Modelo.class
                    ).setParameter("termo", "%" + termo.toLowerCase() + "%")
                    .list();
        }
    }

}