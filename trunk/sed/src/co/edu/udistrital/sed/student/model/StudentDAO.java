package co.edu.udistrital.sed.student.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.connection.HibernateDAO;
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

	public List<Student> loadListStudentByCourse(Long idCourse) throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		try {

			hql.append("select s.id AS id,");
			hql.append("s.identification AS identificacion,");
			hql.append("s.name AS name");
			hql.append("s.idCourse AS idCourse");
			hql.append("FROM student s");
			hql.append("WHERE s.idCourse=:idCourse");
			hql.append("AND s.state=:paramState");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("paramState", IState.ACTIVE);
			qo.setParameter("idCourse", idCourse);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

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

	public boolean deleteStudent(Long idStudent, String user) throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		try {
			hql= new StringBuilder(); 
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

			return qo.executeUpdate() == 1;
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/*
	 * public boolean createStudent() throws Exception { StringBuilder hql = null; Query qo = null;
	 * try { hql.append(" UPDATE Student"); hql.append(" SET state=0");
	 * hql.append(" WHERE id:paramId "); hql.append(" AND s.state = :paramState ");
	 * 
	 * qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers
	 * .aliasToBean(Student.class)); qo.setParameter("state", IState.ACTIVE); //
	 * qo.setParameter("paramId", idStudent);
	 * 
	 * qo.executeUpdate(); return true;
	 * 
	 * } catch (Exception e) { throw e; }finally{ hql = null; qo = null; } }
	 */
	public boolean updateStudent(Long idStudent, String identificacionStudent, String nameStudent, Long idCourse) throws Exception {
		StringBuilder hql = null;
		Query qo = null;
		try {

			hql.append(" UPDATE Student s");
			hql.append(" SET s.identification:identificacionStudent,");
			hql.append("s.name:nameStudent, ");
			hql.append("s.idCourse:idCourse");
			hql.append(" WHERE s.id:idStudent");
			hql.append(" AND s.state:state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("idStudent", idStudent);
			qo.setParameter("identificacionStudent", identificacionStudent);
			qo.setParameter("nameStudent", nameStudent);
			qo.setParameter("idCourse", idCourse);
			qo.setParameter("state", IState.ACTIVE);

			qo.executeUpdate();
			return true;

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

}
