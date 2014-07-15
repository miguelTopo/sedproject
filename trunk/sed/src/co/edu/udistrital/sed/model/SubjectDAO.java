package co.edu.udistrital.sed.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.connection.HibernateDAO;

public class SubjectDAO extends HibernateDAO {

	public List<Subject> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM Subject s WHERE s.state > 0 ORDER BY s.orderSheet ");
			qo = getSession().createQuery(hql.toString());
			return qo.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}

	}

	/** @author MTorres 14/7/2014 23:43:46 */
	public List<Subject> loadSubjectList() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT s.id AS id, ");
			hql.append(" s.name AS name, ");
			hql.append(" s.idKnowledgeArea AS idKnowledgeArea ");
			hql.append(" FROM Subject s ");
			hql.append(" WHERE s.state = :state ");
			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Subject.class));
			qo.setParameter("state", IState.ACTIVE);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}


}
