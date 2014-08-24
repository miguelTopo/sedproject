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
			hql.append(" FROM Subject s WHERE s.state > 0 ORDER BY s.id ");
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

	/** @author MTorres 31/7/2014 23:13:25 */
	public List<Subject> loadSubjectListByTeacherCourse(Long idSedUser, Long idCourse) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT s.id AS id, ");
			hql.append(" s.name AS name ");
			hql.append(" FROM Subject s, ");
			hql.append(" Assignment a ");
			hql.append(" WHERE a.idSubject = s.id ");
			hql.append(" AND a.idSedUser = :idSedUser ");
			hql.append(" AND a.idCourse = :idCourse ");
			hql.append(" AND a.state = :state ");
			hql.append(" AND s.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Subject.class));
			qo.setParameter("idSedUser", idSedUser);
			qo.setParameter("idCourse", idCourse);
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
