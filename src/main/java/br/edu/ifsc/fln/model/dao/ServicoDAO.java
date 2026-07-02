package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class ServicoDAO extends GenericDAO {

    //INSERT
    public void inserir(Servico servico) throws DAOException {
        execute(session -> {
            session.persist(servico);
            return null;
        });
    }

    //UPDATE
    public void alterar(Servico servico) throws DAOException{
        execute(session -> {
            session.merge(servico);
            return null;
        });
    }

    //DELETE por objeto
    public void remover(Servico servico) throws DAOException{
        execute(session -> {
            Servico servicoPersistido = session.find(Servico.class, servico.getId());
            if (servicoPersistido== null) {
                throw new DAOException("Serviço não encontrado para remoção.");
            }
            session.remove(servicoPersistido);
            return null;
        });
    }

    //LISTAR TODOS
    public List<Servico> listar() throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Servico ORDER BY descricao", Servico.class)
                    .list();
        } catch (Exception e) {
            throw new DAOException("Não foi possível realizar a pesquisa no banco de dados", e);
        }
    }

}