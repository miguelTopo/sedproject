package co.edu.udistrital.sed.model;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.connection.HibernateDAO;

public class QualificationDAO extends HibernateDAO {

	/** @author MTorres 7/06/2014 */
	public List<Qualification> loadQualificationListByStudent(Long idStudent, int idPeriod) throws Exception {
		StringBuilder hql = null;
		Query qo = null;

		try {
			hql = new StringBuilder();
			hql.append(" SELECT q.id AS id, ");
			hql.append(" q.idSubject AS idSubject, ");
			hql.append(" q.value AS value, ");
			hql.append(" q.idQualificationType AS idQualificationType, ");
			hql.append(" std.id AS idStudent, ");
			hql.append(" std.name AS studentName, ");
			hql.append(" s.id AS idSubject, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" ka.id AS idKnowledgeArea, ");
			hql.append(" ka.name AS knowledgeAreaName ");
			hql.append(" FROM Qualification q, ");
			hql.append(" StudentCourse sc, ");
			hql.append(" Student std, ");
			hql.append(" Subject s, ");
			hql.append(" KnowledgeArea ka ");
			hql.append(" WHERE sc.id = q.idStudentCourse ");
			hql.append(" AND std.id = sc.idStudent ");
			hql.append(" AND s.id = q.idSubject ");
			hql.append(" AND ka.id = s.idKnowledgeArea ");
			hql.append(" AND std.id = :idStudent ");
			hql.append(idPeriod > 1000 ? " AND sc.idPeriod = :idPeriod " : "");
			hql.append(" AND q.state = :state ");
			hql.append(" ORDER BY ka.id, s.id, q.idQualificationType ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Qualification.class));
			qo.setParameter("idStudent", idStudent);
			qo.setParameter("state", IState.ACTIVE);
			if (idPeriod > 1000)
				qo.setParameter("idPeriod", Long.valueOf(idPeriod));

			return qo.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 4/8/2014 22:47:16 */
	public List<Student> loadStudentListByCourse(Long idCourse) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT std.id AS id, ");
			hql.append(" std.identification AS identification, ");
			hql.append(" std.name AS name, ");
			hql.append(" std.lastName AS lastName, ");
			hql.append(" sc.id AS idStudentCourse ");
			hql.append(" FROM StudentCourse sc, ");
			hql.append(" Student std ");
			hql.append(" WHERE std.id = sc.idStudent ");
			hql.append(" AND sc.idPeriod = :idPeriod ");
			hql.append(" AND sc.idCourse = :idCourse ");
			hql.append(" AND std.state = :state ");
			hql.append(" ORDER BY std.id ");



			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("idPeriod", Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			qo.setParameter("idCourse", idCourse);
			qo.setParameter("state", IState.ACTIVE);

			return qo.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 7/8/2014 15:30:58 */
	public List<Qualification> loadQualificationByCourseSubject(List<Long> idStudentList, Long idSubject) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT sc.id AS idStudentCourse, ");
			hql.append(" sc.idStudent AS idStudent, ");
			hql.append(" q.id AS id, ");
			hql.append(" q.value AS value, ");
			hql.append(" q.idQualificationType AS idQualificationType, ");
			hql.append(" q.idStudentCourse AS idStudentCourse, ");
			hql.append(" q.idSubject AS idSubject, ");
			hql.append(" q.dateCreation AS dateCreation, ");
			hql.append(" q.userCreation AS userCreation, ");
			hql.append(" qt.name AS qualificationTypeName ");
			hql.append(" FROM StudentCourse sc, ");
			hql.append(" Qualification q, ");
			hql.append(" QualificationType qt ");
			hql.append(" WHERE q.idStudentCourse = sc.id ");
			hql.append(" AND qt.id = q.idQualificationType ");
			hql.append(" AND sc.idPeriod = :idPeriod ");
			hql.append(" AND q.idSubject = :idSubject ");
			hql.append(" AND sc.idStudent IN(:idStudentList) ");
			hql.append(" AND q.state = :state ");
			hql.append(" ORDER BY sc.idStudent, q.idQualificationType ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Qualification.class));
			qo.setParameter("idPeriod", Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			qo.setParameter("idSubject", idSubject);
			qo.setParameter("state", IState.ACTIVE);
			qo.setParameterList("idStudentList", idStudentList);

			return qo.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}

	}

	/** @author MTorres 10/08/2014 4:22:04 p. m. */
	public List<Qualification> loadQualificationList(List<Long> idStudentCourseList) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {

			hql.append(" SELECT q.id AS id, ");
			hql.append(" q.value AS value, ");
			hql.append(" q.idSubject AS idSubject, ");
			hql.append(" q.idStudentCourse AS idStudentCourse, ");
			hql.append(" qt.id AS idQualificationType, ");
			hql.append(" qt.name AS qualificationTypeName, ");
			hql.append(" ka.id AS idKnowledgeArea ");
			hql.append(" FROM Qualification q, ");
			hql.append(" StudentCourse sc, ");
			hql.append(" QualificationType qt, ");
			hql.append(" Subject s, ");
			hql.append(" KnowledgeArea ka ");
			hql.append(" WHERE sc.id = q.idStudentCourse ");
			hql.append(" AND s.id = q.idSubject ");
			hql.append(" AND ka.id = s.idKnowledgeArea ");
			hql.append(" AND qt.id = q.idQualificationType ");
			hql.append(" AND q.idStudentCourse IN(:idStudentCourseList) ");
			hql.append(" AND q.state = :state ");
			hql.append(" AND qt.state = :state ");
			hql.append(" AND s.state = :state ");
			hql.append(" AND ka.state = :state ");
			hql.append(" ORDER BY sc.id, ka.id, s.id, qt.id ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Qualification.class));
			qo.setParameter("state", IState.ACTIVE);
			qo.setParameterList("idStudentCourseList", idStudentCourseList);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 5/9/2014 0:00:53 */
	public List<Student> loadStudentList(Long idPeriod, Long idGrade, Long idCourse, List<Long> idCourseList) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" ");
			hql.append(" SELECT su.name || ' ' || su.lastName AS sedUserResponsibleFullName, ");
			hql.append(" s.name AS name, ");
			hql.append(" s.lastName AS lastName, ");
			hql.append(" s.identification AS identification, ");
			hql.append(" s.idIdentificationType AS idIdentificationType, ");
			hql.append(" s.idSedUserResponsible AS idSedUserResponsible, ");
			hql.append(" it.name AS identificationTypeName ");
			hql.append(" FROM Student s ");
			hql.append(" LEFT JOIN SedUser su ");
			hql.append(" ON s.idSedUserResponsible = su.id ");
			hql.append(" INNER JOIN StudentCourse sc ");
			hql.append(" ON sc.idStudent = s.id ");
			hql.append(" INNER JOIN identificationType it ");
			hql.append(" ON it.id = s.idIdentificationType ");
			hql.append(" WHERE sc.idPeriod = :idPeriod ");
			// AND sc.idCourse = 1
			// --AND sc.idcourse IN()
			qo =
				getSession().createSQLQuery(hql.toString()).addScalar("sedUserResponsibleFullName", StringType.INSTANCE)
					.addScalar("name", StringType.INSTANCE).addScalar("lastName", StringType.INSTANCE)
					.addScalar("identification", StringType.INSTANCE).addScalar("idIdentificationType", LongType.INSTANCE)
					.addScalar("idSedUserResponsible", LongType.INSTANCE).addScalar("identificationTypeName", StringType.INSTANCE)
					.setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("idPeriod", idPeriod);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}
}
