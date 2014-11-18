package co.edu.udistrital.sed.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.connection.HibernateDAO;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.sed.api.IAssignmentType;
import co.edu.udistrital.session.common.User;

public class AssignmentDAO extends HibernateDAO {

	/** @author MTorres 23/7/2014 23:01:25 */
	public List<Assignment> loadAssignmentListByPeriod(Long idPeriod) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT a.id AS id, ");
			hql.append(" a.idSedUser AS idSedUser, ");
			hql.append(" a.idPeriod AS idPeriod, ");
			hql.append(" a.idCourse AS idCourse, ");
			hql.append(" a.idSubject AS idSubject, ");
			hql.append(" a.idDay AS idDay, ");
			hql.append(" a.startHour AS startHour, ");
			hql.append(" a.endHour AS endHour, ");
			hql.append(" a.userCreation AS userCreation, ");
			hql.append(" a.dateCreation AS dateCreation, ");
			hql.append(" a.state AS state, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" s.styleClass AS subjectStyleClass, ");
			hql.append(" su.id AS idTeacher, ");
			hql.append(" su.name ||' '|| su.lastName AS teacherFullName, ");
			hql.append(" c.name AS courseName, ");
			hql.append(" g.id AS idGrade ");
			hql.append(" FROM Assignment a, ");
			hql.append(" Subject s, ");
			hql.append(" Grade g, ");
			hql.append(" SedUser su, ");
			hql.append(" Course c ");
			hql.append(" WHERE s.id = a.idSubject ");
			hql.append(" AND a.idSedUser = su.id ");
			hql.append(" AND g.id = c.idGrade ");
			hql.append(" AND s.idGrade = g.id ");
			hql.append(" AND c.id = a.idCourse ");
			hql.append(" AND a.idPeriod = :idPeriod ");
			hql.append(" AND a.state = :state ");
			hql.append(" ORDER BY a.idDay ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Assignment.class));
			qo.setParameter("idPeriod", idPeriod);
			qo.setParameter("state", IState.ACTIVE);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 25/7/2014 23:03:23 */
	public boolean validAvailability(Long idAssignment, Long idCourse, Long idDay, Date startDate, Date endDate, Long idSedUser) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			Calendar startDateAux = Calendar.getInstance();
			startDateAux.setTime(startDate);

			// startDateAux.set(Calendar.HOUR,
			// startDateAux.get(Calendar.MINUTE) == 0 ? startDateAux.get(Calendar.HOUR) - 1 :
			// startDateAux.get(Calendar.HOUR));
			startDateAux.set(Calendar.MINUTE, startDateAux.get(Calendar.MINUTE) + 1);
			startDateAux.set(Calendar.SECOND, 0);

			Calendar endDateAux = Calendar.getInstance();
			endDateAux.setTime(endDate);

			endDateAux.set(Calendar.HOUR, endDateAux.get(Calendar.MINUTE) == 0 ? endDateAux.get(Calendar.HOUR) - 1 : endDateAux.get(Calendar.HOUR));
			endDateAux.set(Calendar.MINUTE, endDateAux.get(Calendar.SECOND) == 0 ? 59 : endDateAux.get(Calendar.MINUTE) - 1);
			endDateAux.set(Calendar.SECOND, endDateAux.get(Calendar.SECOND) == 0 ? 59 : endDateAux.get(Calendar.SECOND) - 1);

			hql.append(" SELECT COUNT(a.id) ");
			hql.append(" FROM Assignment a ");
			hql.append(" WHERE idDay = :idDay ");
			hql.append(" AND a.idPeriod = :idPeriod ");
			hql.append(idSedUser != null ? "" : " AND a.idCourse = :idCourse ");
			hql.append(idAssignment != null ? " AND a.id <> :idAssignment " : "");
			hql.append(idSedUser != null ? " AND a.idSedUser = :idSedUser " : "");
			hql.append(" AND ( :startHour BETWEEN a.startHour AND a.endHour ");
			hql.append(" OR  :endHour BETWEEN a.startHour AND a.endHour ");
			hql.append(" OR  a.startHour BETWEEN :startHour AND :endHour ");
			hql.append(" OR  a.endHour BETWEEN :startHour AND :endHour) ");
			hql.append(" AND a.state = :state ");

			qo = getSession().createQuery(hql.toString());
			if (idSedUser == null)
				qo.setParameter("idCourse", idCourse);

			qo.setParameter("idDay", idDay);
			qo.setParameter("idPeriod", Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			qo.setParameter("startHour", ManageDate.formatDate(startDateAux.getTime(), ManageDate.HH_MM_SS_24));
			qo.setParameter("endHour", ManageDate.formatDate(endDateAux.getTime(), ManageDate.HH_MM_SS_24));
			qo.setParameter("state", IState.ACTIVE);
			if (idSedUser != null)
				qo.setParameter("idSedUser", idSedUser);
			if (idAssignment != null)
				qo.setParameter("idAssignment", idAssignment);
			qo.setMaxResults(1);

			Object o = qo.uniqueResult();

			return o != null ? Integer.parseInt(o.toString()) == 0 : false;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}

	}

	/** @author MTorres 28/7/2014 23:56:47 */
	public List<Assignment> loadAssignmentListByTeacher(Long idSedUser, Long idSedRole) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT a.id AS id, ");
			hql.append(" a.idSedUser AS idSedUser, ");
			hql.append(" a.idPeriod AS idPeriod, ");
			hql.append(" a.idCourse AS idCourse, ");
			hql.append(" a.idSubject AS idSubject, ");
			hql.append(" a.idDay AS idDay, ");
			hql.append(" a.startHour AS startHour, ");
			hql.append(" a.endHour AS endHour, ");
			hql.append(" a.userCreation AS userCreation, ");
			hql.append(" a.dateCreation AS dateCreation, ");
			hql.append(" a.state AS state, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" s.styleClass AS subjectStyleClass, ");
			hql.append(" su.id AS idTeacher, ");
			hql.append(" su.name ||' '|| su.lastName AS teacherFullName, ");
			hql.append(" c.name AS courseName, ");
			hql.append(" g.id AS idGrade ");
			hql.append(" FROM Assignment a, ");
			hql.append(" Subject s, ");
			hql.append(" SedUser su, ");
			hql.append(" Grade g, ");
			hql.append(" Course c ");
			hql.append(" WHERE s.id = a.idSubject ");
			hql.append(" AND a.idSedUser = su.id ");
			hql.append(" AND c.id = a.idCourse ");
			hql.append(" AND g.id = c.idGrade ");
			hql.append(" AND s.idGrade = g.id ");
			hql.append(" AND a.idPeriod = :idPeriod ");
			hql.append(" AND a.state = :state ");
			hql.append(" and a.idSedUser = :idSedUser ");
			hql.append(" ORDER BY a.idDay ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Assignment.class));
			qo.setParameter("idPeriod", Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			qo.setParameter("state", IState.ACTIVE);
			qo.setParameter("idSedUser", idSedUser);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 28/7/2014 23:58:43 */
	public List<Assignment> loadAssignmentListByCourse(Long idCourse) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT a.id AS id, ");
			hql.append(" a.idSedUser AS idSedUser, ");
			hql.append(" a.idPeriod AS idPeriod, ");
			hql.append(" a.idCourse AS idCourse, ");
			hql.append(" a.idAssignmentType AS idAssignmentType, ");
			hql.append(" a.idSubject AS idSubject, ");
			hql.append(" a.idDay AS idDay, ");
			hql.append(" a.startHour AS startHour, ");
			hql.append(" a.endHour AS endHour, ");
			hql.append(" a.userCreation AS userCreation, ");
			hql.append(" a.dateCreation AS dateCreation, ");
			hql.append(" a.state AS state, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" s.styleClass AS subjectStyleClass, ");
			hql.append(" su.id AS idTeacher, ");
			hql.append(" su.name ||' '|| su.lastName AS teacherFullName, ");
			hql.append(" c.name AS courseName, ");
			hql.append(" g.id AS idGrade ");
			hql.append(" FROM Assignment a, ");
			hql.append(" Subject s, ");
			hql.append(" SedUser su, ");
			hql.append(" Grade g, ");
			hql.append(" Course c ");
			hql.append(" WHERE s.id = a.idSubject ");
			hql.append(" AND a.idSedUser = su.id ");
			hql.append(" AND g.id = c.idGrade ");
			hql.append(" AND s.idGrade = g.id ");
			hql.append(" AND c.id = a.idCourse ");
			hql.append(" AND a.idPeriod = :idPeriod ");
			hql.append(" AND a.state = :state ");
			hql.append(" and a.idCourse = :idCourse ");
			hql.append(" ORDER BY a.idDay ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Assignment.class));
			qo.setParameter("idPeriod", Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			qo.setParameter("state", IState.ACTIVE);
			qo.setParameter("idCourse", idCourse);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 30/7/2014 23:28:22 */
	public boolean deleteTeacherAssignment(Long idAssignment, String user) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" UPDATE Assignment a ");
			hql.append(" SET a.state = :inactiveState, ");
			hql.append(" a.dateChange = :dateChange, ");
			hql.append(" a.userChange = :userChange ");
			hql.append(" WHERE a.id = :idAssignment ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("inactiveState", IState.INACTIVE);
			qo.setParameter("dateChange", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameter("userChange", user);
			qo.setParameter("idAssignment", idAssignment);

			return qo.executeUpdate() == 1;
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 27/9/2014 17:16:18 */
	public Assignment loadTeacherManager(Long idCourse, Long idWorkDay) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT a.id AS id, ");
			hql.append(" a.idPeriod AS idPeriod, ");
			hql.append(" a.idCourse AS idCourse, ");
			hql.append(" a.idSedUser AS idSedUser, ");
			hql.append(" c.name AS courseName, ");
			hql.append(" su.name ||' '||su.lastName AS teacherFullName ");
			hql.append(" FROM Assignment a, ");
			hql.append(" Course c, ");
			hql.append(" SedUser su ");
			hql.append(" WHERE c.id = a.idCourse ");
			hql.append(" AND su.id = a.idSedUser ");
			hql.append(" AND c.id = :idCourse ");
			hql.append(" AND a.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Assignment.class));
			qo.setParameter("idCourse", idCourse);
			qo.setParameter("state", IState.ACTIVE);
			qo.setMaxResults(1);

			return (Assignment) qo.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/**
	 * @author Miguel 17/11/2014 13:03:21
	 */
	public List<Assignment> loadAssignmentDefault(User sedUser) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo;
		try {
			hql.append(" SELECT a.id AS id, ");
			hql.append(" a.idSedUser AS idSedUser, ");
			hql.append(" a.idPeriod AS idPeriod, ");
			hql.append(" a.idCourse AS idCourse, ");
			hql.append(" a.idAssignmentType AS idAssignmentType, ");
			hql.append(" a.idSubject AS idSubject, ");
			hql.append(" a.idDay AS idDay, ");
			hql.append(" a.startHour AS startHour, ");
			hql.append(" a.endHour AS endHour, ");
			hql.append(" a.userCreation AS userCreation, ");
			hql.append(" a.dateCreation AS dateCreation, ");
			hql.append(" a.state AS state, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" CASE WHEN s.styleClass IS NULL THEN 'ui-attention' ELSE s.styleClass END AS subjectStyleClass, ");
			hql.append(" su.id AS idTeacher, ");
			hql.append(" su.name ||' '|| su.lastName AS teacherFullName, ");
			hql.append(" c.name AS courseName, ");
			hql.append(" g.id AS idGrade ");
			hql.append(" FROM lifemena.Assignment a ");
			hql.append(" LEFT JOIN lifemena.Subject s ");
			hql.append(" ON s.id = a.idSubject ");
			hql.append(" LEFT JOIN lifemena.SedUser su ");
			hql.append(" ON su.id = a.idSedUser ");
			hql.append(" LEFT JOIN lifemena.Course c ");
			hql.append(" ON c.id = a.idCourse ");
			hql.append(" LEFT JOIN lifemena.Grade g ");
			hql.append(" ON g.id = c.idGrade ");
			hql.append(" WHERE a.state = :state ");
			hql.append(" AND su.id = :idSedUser ");

			qo =
				getSession().createSQLQuery(hql.toString()).addScalar("id", LongType.INSTANCE).addScalar("idSedUser", LongType.INSTANCE)
					.addScalar("idPeriod", LongType.INSTANCE).addScalar("idCourse", LongType.INSTANCE)
					.addScalar("idAssignmentType", LongType.INSTANCE).addScalar("idSubject", LongType.INSTANCE).addScalar("idDay", LongType.INSTANCE)
					.addScalar("startHour", StringType.INSTANCE).addScalar("endHour", StringType.INSTANCE)
					.addScalar("userCreation", StringType.INSTANCE).addScalar("dateCreation", StringType.INSTANCE)
					.addScalar("state", LongType.INSTANCE).addScalar("subjectName", StringType.INSTANCE)
					.addScalar("subjectStyleClass", StringType.INSTANCE).addScalar("teacherFullName", StringType.INSTANCE)
					.addScalar("courseName", StringType.INSTANCE).addScalar("idGrade", LongType.INSTANCE)
					.setResultTransformer(Transformers.aliasToBean(Assignment.class));

			qo.setParameter("state", IState.ACTIVE);
			qo.setParameter("idSedUser", sedUser.getIdSedUser());

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
		}
	}

	/**
	 * @author Miguel 17/11/2014 15:08:18
	 */
	public List<Assignment> loadAssignmentBySedUser(Long idSedUser, Long idSedRole, Long idAssignmentType) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo;
		try {
			hql.append(" SELECT a.id AS id, ");
			hql.append(" a.idSedUser AS idSedUser, ");
			hql.append(" a.idPeriod AS idPeriod, ");
			hql.append(" a.idCourse AS idCourse, ");
			hql.append(" a.idAssignmentType AS idAssignmentType, ");
			hql.append(" a.idSubject AS idSubject, ");
			hql.append(" a.idDay AS idDay, ");
			hql.append(" a.startHour AS startHour, ");
			hql.append(" a.endHour AS endHour, ");
			hql.append(" a.userCreation AS userCreation, ");
			hql.append(" a.dateCreation AS dateCreation, ");
			hql.append(" a.state AS state, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" CASE WHEN s.styleClass IS NULL THEN 'ui-attention' ELSE s.styleClass END AS subjectStyleClass, ");
			hql.append(" su.id AS idTeacher, ");
			hql.append(" su.name ||' '|| su.lastName AS teacherFullName, ");
			hql.append(" c.name AS courseName, ");
			hql.append(" g.id AS idGrade ");
			hql.append(" FROM lifemena.Assignment a ");
			hql.append(" LEFT JOIN lifemena.Subject s ");
			hql.append(" ON s.id = a.idSubject ");
			hql.append(" LEFT JOIN lifemena.SedUser su ");
			hql.append(" ON su.id = a.idSedUser ");
			hql.append(" LEFT JOIN lifemena.Course c ");
			hql.append(" ON c.id = a.idCourse ");
			hql.append(" LEFT JOIN lifemena.Grade g ");
			hql.append(" ON g.id = c.idGrade ");
			hql.append(" WHERE a.state = :state ");
			hql.append(idAssignmentType != null && !idAssignmentType.equals(IAssignmentType.ALL) ? " AND a.idAssignmentType = :idAssignmentType "
				: "");
			hql.append(" AND su.id = :idSedUser ");

			qo =
				getSession().createSQLQuery(hql.toString()).addScalar("id", LongType.INSTANCE).addScalar("idSedUser", LongType.INSTANCE)
					.addScalar("idPeriod", LongType.INSTANCE).addScalar("idCourse", LongType.INSTANCE)
					.addScalar("idAssignmentType", LongType.INSTANCE).addScalar("idSubject", LongType.INSTANCE).addScalar("idDay", LongType.INSTANCE)
					.addScalar("startHour", StringType.INSTANCE).addScalar("endHour", StringType.INSTANCE)
					.addScalar("userCreation", StringType.INSTANCE).addScalar("dateCreation", StringType.INSTANCE)
					.addScalar("state", LongType.INSTANCE).addScalar("subjectName", StringType.INSTANCE)
					.addScalar("subjectStyleClass", StringType.INSTANCE).addScalar("teacherFullName", StringType.INSTANCE)
					.addScalar("courseName", StringType.INSTANCE).addScalar("idGrade", LongType.INSTANCE)
					.setResultTransformer(Transformers.aliasToBean(Assignment.class));

			qo.setParameter("state", IState.ACTIVE);
			qo.setParameter("idSedUser", idSedUser);
			if (idAssignmentType != null && !idAssignmentType.equals(IAssignmentType.ALL))
				qo.setParameter("idAssignmentType", idAssignmentType);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
		}
	}
}
