package co.edu.udistrital.sed.student.model;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.connection.HibernateDAO;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.sed.model.Student;

public class StudentDAO extends HibernateDAO {

	public Student loadStudent(Long idStudent, Long idCourse) throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		try {
			hql = new StringBuilder();
			hql.append(" SELECT s.id AS id, ");
			hql.append(" s.name AS name, ");
			hql.append(" s.lastName AS lastName, ");
			hql.append(" s.identification AS identification, ");
			hql.append(" s.userCreation AS userCreation, ");
			hql.append(" s.dateCreation AS dateCreation, ");
			hql.append(" s.state AS state, ");
			hql.append(" s.idIdentificationType AS idIdentificationType, ");
			hql.append(" s.birthday AS birthday, ");
			hql.append(" su.email AS email, ");
			hql.append(" sc.idCourse AS idCourse, ");
			hql.append(" c.idGrade AS idGrade, ");
			hql.append(" c.name AS courseName, ");
			hql.append(" c.id AS idCourse ");
			hql.append(" FROM Student s, ");
			hql.append(" StudentCourse sc, ");
			hql.append(" SedUser su, ");
			hql.append(" Course c ");
			hql.append(" WHERE sc.idStudent = s.id ");
			hql.append(" AND su.id = s.id ");
			hql.append(" AND c.id = sc.idCourse ");
			hql.append(" AND sc.idCourse = :idCourse ");
			hql.append(" AND s.state = :state ");
			hql.append(" and s.id = :idStudent ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("idStudent", idStudent);
			qo.setParameter("idCourse", idCourse);
			qo.setParameter("state", IState.ACTIVE);
			qo.setMaxResults(1);

			return (Student) qo.uniqueResult();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres */
	public List<Student> loadStudentList(Long idCourse) throws Exception {
		StringBuilder hql = null;
		Query qo = null;

		try {
			hql = new StringBuilder();
			hql.append(" SELECT s.id AS id, ");
			hql.append(" s.name AS name, ");
			hql.append(" s.lastName AS lastName, ");
			hql.append(" s.identification AS identification, ");
			hql.append(" s.userCreation AS userCreation, ");
			hql.append(" s.dateCreation AS dateCreation, ");
			hql.append(" s.state AS state, ");
			hql.append(" s.idIdentificationType AS idIdentificationType, ");
			hql.append(" s.birthday AS birthday, ");
			hql.append(" su.email AS email, ");
			hql.append(" sc.idCourse AS idCourse, ");
			hql.append(" c.idGrade AS idGrade, ");
			hql.append(" c.name AS courseName, ");
			hql.append(" c.id AS idCourse, ");
			hql.append(" it.name AS identificationTypeName, ");
			hql.append(" g.name AS gradeName ");
			hql.append(" FROM Student s, ");
			hql.append(" StudentCourse sc, ");
			hql.append(" SedUser su, ");
			hql.append(" Course c, ");
			hql.append(" Grade g, ");
			hql.append(" IdentificationType it ");
			hql.append(" WHERE sc.idStudent = s.id ");
			hql.append(" AND it.id = s.idIdentificationType ");
			hql.append(" AND c.idGrade = g.id ");
			hql.append(" AND su.id = s.id ");
			hql.append(" AND c.id = sc.idCourse ");
			hql.append(" AND sc.idCourse = :idCourse ");
			hql.append(" AND s.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
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

	/** @author MTorres */
	public boolean deleteStudent(Long idStudent, Long idSedUser, String user) throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		int rowUpdate = 0;
		try {
			hql = new StringBuilder();
			hql.append(" UPDATE Student s ");
			hql.append(" SET s.state = :inactiveState, ");
			hql.append(" s.dateChange = :dateChange, ");
			hql.append(" s.userChange = :userChange ");
			hql.append(" WHERE s.id = :idStudent ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("inactiveState", IState.INACTIVE);
			qo.setParameter("dateChange", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameter("userChange", user);
			qo.setParameter("idStudent", idStudent);

			rowUpdate += qo.executeUpdate();
			hql = null;
			qo = null;

			hql = new StringBuilder();
			hql.append(" UPDATE SedUser su ");
			hql.append(" SET su.state = :inactiveState, ");
			hql.append(" su.dateChange = :dateChange, ");
			hql.append(" su.userChange = :userChange ");
			hql.append(" WHERE su.id = :idSedUser ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("inactiveState", IState.INACTIVE);
			qo.setParameter("dateChange", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameter("userChange", user);
			qo.setParameter("idSedUser", idSedUser);

			rowUpdate += qo.executeUpdate();

			return rowUpdate == 2;
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres */
	public List<Student> loadStudentListByGrade(List<Long> idCourseList) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT s.id AS id, ");
			hql.append(" s.identification AS identification, ");
			hql.append(" sc.id AS idStudentCourse ");
			hql.append(" FROM Student s, StudentCourse sc ");
			hql.append(" WHERE s.id = sc.idStudent ");
			hql.append(" AND sc.idCourse IN:idCourseList ");
			hql.append(" AND sc.idPeriod = :year ");
			hql.append(" AND s.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			Calendar c = Calendar.getInstance();
			qo.setParameter("year", Long.valueOf(c.get(Calendar.YEAR)));
			qo.setParameter("state", IState.ACTIVE);
			qo.setParameterList("idCourseList", idCourseList);

			return qo.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 7/06/2014 */
	public Student loadStudent(Long idSedUser) throws Exception {
		StringBuilder hql = null;
		Query qo = null;

		try {
			hql = new StringBuilder();
			hql.append(" SELECT s.id AS id, ");
			hql.append(" s.name AS name, ");
			hql.append(" s.lastName AS lastName, ");
			hql.append(" s.identification AS identification, ");
			hql.append(" s.idIdentificationType AS idIdentificationType, ");
			hql.append(" su.email AS email, ");
			hql.append(" it.name AS identificationTypeName ");
			hql.append(" FROM Student s, ");
			hql.append(" SedUser su, ");
			hql.append(" IdentificationType it ");
			hql.append(" WHERE su.id = s.idSedUser ");
			hql.append(" AND it.id = su.idIdentificationType ");
			hql.append(" AND it.id = su.idIdentificationType ");
			hql.append(" AND su.id = :idSedUser ");
			hql.append(" AND s.state = :state ");
			hql.append(" AND su.state = :state ");
			hql.append(" AND it.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));

			qo.setParameter("idSedUser", idSedUser);
			qo.setParameter("state", IState.ACTIVE);
			qo.setMaxResults(1);

			return (Student) qo.uniqueResult();

		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 12/7/2014 18:13:59 */
	public Student loadStudentGradeCourse(Long idSedUser) throws Exception{
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT c.id AS idCourse, ");
			hql.append(" c.name AS courseName, ");
			hql.append(" g.id AS idGrade, ");
			hql.append(" g.name  AS gradeName ");
			hql.append(" FROM Course c, ");
			hql.append(" StudentCourse sc, ");
			hql.append(" Grade g, ");
			hql.append(" Student s ");
			hql.append(" WHERE c.id = sc.idCourse ");
			hql.append(" AND c.idGrade = g.id ");
			hql.append(" AND s.id = sc.idStudent ");
			hql.append(" AND s.idSedUser = :idSedUser ");
			hql.append(" AND sc.idPeriod = :period ");
			hql.append(" AND c.state = :state ");
			hql.append(" AND g.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("idSedUser", idSedUser);
			qo.setParameter("state", IState.ACTIVE);
			qo.setParameter("period", Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			qo.setMaxResults(1);
			
			return (Student) qo.uniqueResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}
}
