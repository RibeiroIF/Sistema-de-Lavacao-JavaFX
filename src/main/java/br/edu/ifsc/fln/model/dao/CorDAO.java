package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class CorDAO extends GenericDAO {

    //INSERT
    public void inserir(Cor cor) throws DAOException {
        execute(session -> {
            session.persist(cor);
            return null;
        });
    }

    //UPDATE
    public void alterar(Cor cor) throws DAOException{
        execute(session -> {
            session.merge(cor);
            return null;
        });
    }

    //DELETE por objeto
    public void remover(Cor cor) throws DAOException{
        execute(session -> {
            Cor corPersistida = session.find(Cor.class, cor.getId());
            if (corPersistida == null) {
                throw new DAOException("Cor não encontrada para remoção.");
            }
            session.remove(corPersistida);
            return null;
        });
    }

    //LISTAR TODOS
    public List<Cor> listar() throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Cor", Cor.class)
                    .list();
        } catch (Exception e) {
            throw new DAOException("Não foi possível realizar a pesquisa no banco de dados", e);
        }
    }

}