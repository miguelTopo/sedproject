package co.edu.udistrital.sed.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.connection.HibernateDAO;

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
			hql.append(" a.idSubject AS idSubject, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" s.styleClass AS subjectStyleClass, ");
			hql.append(" su.id AS idTeacher, ");
			hql.append(" su.name ||' '|| su.lastName AS teacherFullName, ");
			hql.append(" c.name AS courseName ");
			hql.append(" FROM Assignment a, ");
			hql.append(" Subject s, ");
			hql.append(" SedUser su, ");
			hql.append(" Course c ");
			hql.append(" WHERE s.id = a.idSubject ");
			hql.append(" AND a.idSedUser = su.id ");
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
	public boolean validSpaceAvailability(Long idCourse, Long idDay, Date startDate, Date endDate) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			Calendar startDateAux = new GregorianCalendar();
			startDateAux.setTime(startDate);

			startDateAux.set(Calendar.HOUR,
				startDateAux.get(Calendar.MINUTE) == 0 ? startDateAux.get(Calendar.HOUR) - 1 : startDateAux.get(Calendar.HOUR));
			startDateAux.set(Calendar.MINUTE, startDateAux.get(Calendar.SECOND) == 0 ? 59 : startDateAux.get(Calendar.MINUTE) - 1);
			startDateAux.set(Calendar.SECOND, startDateAux.get(Calendar.SECOND) == 0 ? 59 : startDateAux.get(Calendar.SECOND) - 1);

			Calendar endDateAux = new GregorianCalendar();
			endDateAux.setTime(endDate);

			endDateAux.set(Calendar.HOUR, endDateAux.get(Calendar.MINUTE) == 0 ? endDateAux.get(Calendar.HOUR) - 1 : endDateAux.get(Calendar.HOUR));
			endDateAux.set(Calendar.MINUTE, endDateAux.get(Calendar.SECOND) == 0 ? 59 : endDateAux.get(Calendar.MINUTE) - 1);
			endDateAux.set(Calendar.SECOND, endDateAux.get(Calendar.SECOND) == 0 ? 59 : endDateAux.get(Calendar.SECOND) - 1);

			hql.append(" SELECT COUNT(a.id) ");
			hql.append(" FROM Assignment a ");
			hql.append(" WHERE idDay = :idDay ");
			hql.append(" AND a.idPeriod = :idPeriod ");
			hql.append(" AND ( :startHour BETWEEN a.startHour AND a.endHour ");
			hql.append(" OR  :endHour BETWEEN a.starthour AND a.endHour ");
			hql.append(" OR  a.startHour BETWEEN :startHour AND :endHour ");
			hql.append(" OR  a.endHour BETWEEN :startHour AND :endHour) ");
			hql.append(" AND a.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("idDay", idDay);
			qo.setParameter("idPeriod", Calendar.getInstance().get(Calendar.YEAR));
			qo.setParameter("startHour", ManageDate.formatDate(startDateAux.getTime(), ManageDate.HH_MM_SS));
			qo.setParameter("endHour", ManageDate.formatDate(endDateAux.getTime(), ManageDate.HH_MM_SS));
			qo.setParameter("state", IState.ACTIVE);

			qo.setMaxResults(1);

			Object o = qo.uniqueResult();

			return o != null ? Integer.parseInt(o.toString()) == 0 : false;
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}

	}
}
