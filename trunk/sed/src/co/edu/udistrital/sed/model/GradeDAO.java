package co.edu.udistrital.sed.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.api.IState;
import co.edu.udistrital.core.connection.HibernateDAO;

public class GradeDAO extends HibernateDAO {

	public List<Grade> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM Grade g WHERE g.state > 0 ");
			qo = getSession().createQuery(hql.toString());
			return qo.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @author Miguel 17/11/2014 17:42:19
	 */
	public List<Grade> loadGradeListByCourseList(List<Long> idCourseList) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo;
		try {
			hql.append(" SELECT DISTINCT (g.id) AS id, ");
			hql.append(" g.name AS name, ");
			hql.append(" g.userCreation AS userCreation, ");
			hql.append(" g.dateCreation AS dateCreation, ");
			hql.append(" g.userChange AS userChange, ");
			hql.append(" g.dateChange AS dateChange, ");
			hql.append(" g.state AS state ");
			hql.append(" FROM Grade g, ");
			hql.append(" Course c ");
			hql.append(" WHERE c.idGrade = g.id ");
			hql.append(" AND c.id IN(:idCourseList) ");
			hql.append(" AND g.state = :state ");
			hql.append(" AND c.state = :state ");
			hql.append(" ORDER BY g.id ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Grade.class));
			qo.setParameter("state", IState.ACTIVE);
			qo.setParameterList("idCourseList", idCourseList);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
		}
	}
}
