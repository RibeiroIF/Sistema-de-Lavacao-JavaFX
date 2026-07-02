package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class GenericDAO {

    @FunctionalInterface
    protected interface SessionAction<T> {
        T execute(Session session) throws Exception;
    }

    protected <T> T execute(SessionAction<T> action) throws DAOException {
        Transaction tx = null;
        Session session = null; // Removido do try-with-resources

        try {
            // Abre a sessão manualmente
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            T result = action.execute(session);

            tx.commit();
            return result;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            if (e instanceof DAOException daoEx) {
                throw daoEx;
            }
            if (e instanceof RuntimeException runtimeEx) {
                throw runtimeEx;
            }
            throw new DAOException("Erro na persistência.", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}