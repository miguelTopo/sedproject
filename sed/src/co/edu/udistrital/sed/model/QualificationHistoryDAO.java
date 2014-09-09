package co.edu.udistrital.sed.model;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.connection.HibernateDAO;

public class QualificationHistoryDAO extends HibernateDAO {

	/** @author MTorres 10/08/2014 2:50:50 p. m. */
	public List<Qualification> loadQualificationHistoricalList(Long idStudent) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT qh.id AS id, ");
			hql.append(" qh.idSubject AS idSubject, ");
			hql.append(" qh.value AS value, ");
			hql.append(" qh.idQualificationType AS idQualificationType, ");
			hql.append(" std.id AS idStudent, ");
			hql.append(" std.name AS studentName, ");
			hql.append(" s.id AS idSubject, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" ka.id AS idKnowledgeArea, ");
			hql.append(" ka.name AS knowledgeAreaName, ");
			hql.append(" p.id AS idPeriod ");
			hql.append(" FROM QualificationHistory qh, ");
			hql.append(" StudentCourse sc, ");
			hql.append(" Student std, ");
			hql.append(" Subject s, ");
			hql.append(" KnowledgeArea ka, ");
			hql.append(" Period p ");
			hql.append(" WHERE sc.id = qh.idStudentCourse ");
			hql.append(" AND std.id = sc.idStudent ");
			hql.append(" AND s.id = qh.idSubject ");
			hql.append(" AND sc.idPeriod = p.id ");
			hql.append(" AND ka.id = s.idKnowledgeArea ");
			hql.append(" AND std.id = :idStudent ");
			hql.append(" AND qh.state = :state ");
			hql.append(" ORDER BY p.id, ka.id, s.id, qh.idQualificationType ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Qualification.class));
			qo.setParameter("idStudent", idStudent);
			qo.setParameter("state", IState.ACTIVE);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 8/9/2014 22:12:36 */
	public List<Qualification> loadStudentQualificationTrace(Long idStudentCourse, Long idPeriod) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" ");
			hql.append(" SELECT q.id AS id, ");
			hql.append(" q.value AS value, ");
			hql.append(" q.idSubject AS idSubject, ");
			hql.append(" q.idQualificationType AS idQualificationType, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" ka.id AS idKnowledgeArea, ");
			hql.append(" ka.name AS knowledgeAreaName ");
			hql.append(" FROM  ");
			hql.append(Calendar.getInstance().get(Calendar.YEAR) == idPeriod.intValue() ? " Qualification q, " : " QualificationHistory q, ");

			hql.append(" QualificationType qt , ");
			hql.append(" Subject s, ");
			hql.append(" KnowledgeArea ka ");
			hql.append(" WHERE s.id = q.idSubject ");
			hql.append(" AND ka.id = s.idKnowledgeArea ");
			hql.append(" AND qt.id = q.idQualificationType ");
			hql.append(" AND q.idStudentCourse = :idStudentCourse ");
			hql.append(" AND q.state = :state ");
			hql.append(" AND qt.state = :state ");
			hql.append(" AND s.state = :state ");
			hql.append(" AND ka.state = :state ");
			hql.append(" ORDER BY ka.id, s.id, qt.id ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Qualification.class));
			qo.setParameter("state", IState.ACTIVE);
			qo.setParameter("idStudentCourse", idStudentCourse);

			return qo.list();

		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}


	}
}
