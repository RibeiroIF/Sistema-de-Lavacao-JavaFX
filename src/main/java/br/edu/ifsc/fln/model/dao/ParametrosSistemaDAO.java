package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ParametrosSistema;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ParametrosSistemaDAO {

    public ParametrosSistema buscarPrimeiro() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "FROM ParametrosSistema",
                            ParametrosSistema.class
                    )
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    public void atualizar(ParametrosSistema config) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.merge(config);

            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}