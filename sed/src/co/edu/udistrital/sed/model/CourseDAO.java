package co.edu.udistrital.sed.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.api.IState;
import co.edu.udistrital.core.connection.HibernateDAO;
import co.edu.udistrital.core.login.api.ISedRole;

public class CourseDAO extends HibernateDAO {

	public List<Course> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM Course c WHERE c.state > 0 ");
			qo = getSession().createQuery(hql.toString());
			return qo.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres 31/7/2014 22:38:22 */
	public List<Course> loadCourseListByTeacher(Long idSedUser) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT DISTINCT (a.idCourse) AS id, ");
			hql.append(" c.name AS name, ");
			hql.append(" c.idGrade AS idGrade ");
			hql.append(" FROM Assignment a, ");
			hql.append(" Course c ");
			hql.append(" WHERE c.id = a.idCourse ");
			hql.append(" AND a.idPeriod = :idPeriod ");
			hql.append(" AND a.idSedUser = :idSedUser ");
			hql.append(" AND a.state = :state ");
			hql.append(" AND c.state = :state ");
			hql.append(" ORDER BY c.name ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Course.class));
			qo.setParameter("idPeriod", Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			qo.setParameter("idSedUser", idSedUser);
			qo.setParameter("state", IState.ACTIVE);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/**
	 * @author Miguel 17/11/2014 16:58:18
	 */
	public List<Long> loadStudentCourse(Long idSedUser, List<Long> idStudentList, Long idSedRole) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo;
		try {
			hql.append(" SELECT DISTINCT (sc.idCourse) ");
			hql.append(" FROM StudentCourse sc ");
			hql.append(" WHERE sc.idStudent IN(:idStudentList) ");
			hql.append(" AND sc.idPeriod = :idPeriod ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("idPeriod", Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			qo.setParameterList("idStudentList", idStudentList);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
		}
	}

	/**
	 * @author Miguel 17/11/2014 17:07:11
	 */
	public List<Long> loadStudentByResponsible(Long idSedUser) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo;
		try {
			hql.append(" SELECT s.id ");
			hql.append(" FROM Student s ");
			hql.append(" WHERE s.idSedUserResponsible = :idSedUser ");
			hql.append(" AND s.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("idSedUser", idSedUser);
			qo.setParameter("state", IState.ACTIVE);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
		}
	}
}
