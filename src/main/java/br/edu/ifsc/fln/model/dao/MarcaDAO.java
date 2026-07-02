package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class MarcaDAO extends GenericDAO {

    //INSERT
    public void inserir(Marca marca) throws DAOException {
        execute(session -> {
            session.persist(marca);
            return null;
        });
    }

    //UPDATE
    public void alterar(Marca marca) throws DAOException{
        execute(session -> {
            session.merge(marca);
            return null;
        });
    }

    //DELETE por objeto
    public void remover(Marca marca) throws DAOException{
        execute(session -> {
            Marca marcaPersistida = session.find(Marca.class, marca.getId());
            if (marcaPersistida == null) {
                throw new DAOException("Marca não encontrada para remoção.");
            }
            session.remove(marcaPersistida);
            return null;
        });
    }

    //LISTAR TODOS
    public List<Marca> listar() throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Marca ORDER BY nome", Marca.class)
                    .list();
        } catch (Exception e) {
            throw new DAOException("Não foi possível realizar a pesquisa no banco de dados", e);
        }
    }

}