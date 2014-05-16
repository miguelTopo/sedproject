package co.edu.udistrital.sed.student.controller;

import java.util.List;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.core.login.model.SedRole;
import co.edu.udistrital.core.login.model.SedRoleUser;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserLogin;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.model.StudentCourse;
import co.edu.udistrital.sed.student.model.StudentDAO;

public class StudentController extends Controller {

	// ////Maneja transacciones,

	public Student loadStudent(Long idStudent) throws Exception {
		StudentDAO dao = new StudentDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			// dao.getSession().save(new Student());
			// tx.commit();
			return dao.loadStudent(idStudent);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres */
	public List<Student> loadStudentList(Long idCourse) throws Exception {
		StudentDAO dao = new StudentDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.loadStudentList(idCourse);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}

	}

	public boolean deleteStudent(Long idStudent, String user) throws Exception {
		StudentDAO dao = new StudentDAO();
		Transaction tx = null;
		boolean success = false;
		try {
			tx = dao.getSession().beginTransaction();
			success = dao.deleteStudent(idStudent, user);
			tx.commit();
			return success;
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	public boolean saveStudent(Student student, String user, String password) throws Exception {
		StudentDAO dao = new StudentDAO();
		Transaction tx = null;
		boolean isNew;
		try {
			isNew = student.getId() != null ? false : true;
			student.initialize(isNew);
			tx = dao.getSession().beginTransaction();
			// save Student
			dao.getSession().saveOrUpdate(student);

			if (isNew) {
				/** TODO mtorres realizar mejora para que se vea el periodo al que se quiere asignar */
				// save StudentCourse
				StudentCourse sc = new StudentCourse();
				sc.setIdStudent(student.getId());
				sc.setIdCourse(student.getIdCourse());
				sc.setIdPeriod(Long.valueOf(2014));
				sc.initialize(true);

				dao.getSession().save(sc);

				// Save SedUser
				SedUser su = new SedUser(student);
				su.initialize(true);
				dao.getSession().save(su);

				// Save SedUserRole
				SedRoleUser sru = new SedRoleUser();
				sru.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
				sru.setUserCreation(user);
				sru.setIdSedRole(ISedRole.STUDENT);
				sru.setIdSedUser(su.getId());
				sru.setState(IState.ACTIVE);

				dao.getSession().save(sru);


				// Save SedUserLogin
				SedUserLogin sul = new SedUserLogin();
				sul.setIdSedUser(su.getId());
				sul.setUserName(student.getIdentification());
				sul.setMd5Password(ManageMD5.parseMD5(password));
				sul.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
				sul.setUserCreation(user);
				sul.setState(IState.ACTIVE);

				dao.getSession().save(sul);
			}

			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

}
