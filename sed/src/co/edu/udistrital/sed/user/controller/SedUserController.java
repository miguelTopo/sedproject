package co.edu.udistrital.sed.user.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import co.edu.udistrital.core.common.api.IState;
import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.core.login.model.SedRoleUser;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserDAO;
import co.edu.udistrital.core.login.model.SedUserLogin;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.model.StudentCourse;
import co.edu.udistrital.sed.student.model.StudentDAO;

public class SedUserController extends Controller {

	public List<SedUser> loadSedUserList() throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.loadSedUserList();
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
	public boolean validateExistField(String className, String field, String fieldCompare) throws HibernateException, Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.validateExistField(className, field, fieldCompare);
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
	public Long saveSedUser(SedUser sedUser, String userPassword, String user, List<Student> studentResponsibleList) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {

			sedUser.initialize(true, user);
			tx = dao.getSession().beginTransaction();

			dao.getSession().save(sedUser);

			SedUserLogin sl = new SedUserLogin();
			sl.setUserCreation(user);
			sl.setState(IState.ACTIVE);
			sl.setIdSedUser(sedUser.getId());
			sl.setUserName(sedUser.getIdentification());
			sl.setMd5Password(ManageMD5.parseMD5(userPassword));
			sl.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));


			dao.getSession().save(sl);

			SedRoleUser sru = new SedRoleUser();
			sru.setIdSedRole(sedUser.getIdSedRole());
			sru.setIdSedUser(sedUser.getId());
			sru.setUserCreation(user);
			sru.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			sru.setState(IState.ACTIVE);
			dao.getSession().save(sru);

			// Crear instancias necesarias para Estudiante
			if (sedUser.getIdSedRole().equals(ISedRole.STUDENT)) {
				// Crear estudiante
				Student s = new Student();
				s.setName(sedUser.getName());
				s.setLastName(sedUser.getLastName());
				s.setIdentification(sedUser.getIdentification());
				s.setIdIdentificationType(sedUser.getIdIdentificationType());
				s.setIdSedUser(sedUser.getId());

				s.initialize(true, user);
				dao.getSession().save(s);

				Calendar c = Calendar.getInstance();
				StudentCourse sc = new StudentCourse();
				sc.setIdStudent(s.getId());
				sc.setIdCourse(sedUser.getIdStudentCourse());
				sc.setIdPeriod(Long.valueOf(c.get(Calendar.YEAR)));
				dao.getSession().save(sc);

			}

			tx.commit();
			return sedUser.getId();
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

	
	public void updateResponsibleList(List<Long> idStudentList, Long idResponsible, String user) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			dao.updateResponsibleList(idStudentList, idResponsible, user);
			tx.commit();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres 17/06/2014 23:59:50 */
	public boolean updateSedUser(SedUser sedUser, boolean updSedLogin, boolean updSedRoleUser, String password, String user,
		List<Student> studentResponsibleList, List<Long> idStudentResponsibleDropList) throws HibernateException, Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			sedUser.initialize(false, user);
			dao.getSession().update(sedUser);

			if (updSedLogin)
				dao.updateSedUserLogin(sedUser, password);

			if (updSedRoleUser)
				dao.updateSedRoleUser(sedUser);

			if (sedUser.getIdSedRole().equals(ISedRole.STUDENT_RESPONSIBLE)) {
				// Verificar si existen estudiantes por actualizar responsable
				if (studentResponsibleList != null && !studentResponsibleList.isEmpty()) {
					List<Long> idStudentList = new ArrayList<Long>(studentResponsibleList.size());
					for (Student s : studentResponsibleList) {
						idStudentList.add(s.getId());
					}
					dao.updateResponsibleList(idStudentList, sedUser.getId(), user);
				}
				// Verificar si se debe eliminar responsable
				if (idStudentResponsibleDropList != null && !idStudentResponsibleDropList.isEmpty())
					dao.deleteResponsibleList(idStudentResponsibleDropList, user);

			} else if (sedUser.getIdSedRole().equals(ISedRole.STUDENT)) {
				if (!dao.updateStudentCourse(sedUser))
					return false;
			}
			tx.commit();
			return true;
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

	/** @author MTorres 18/06/2014 20:09:16 */
	public boolean deleteSedUser(SedUser sedUser, String user) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			boolean success = dao.deleteSedUser(sedUser, user);
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

	/** @author MTorres 22/06/2014 18:57:30 */
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

	/** @author MTorres 12/7/2014 18:04:54 * */
	public List<Student> loadStudentResponsibleListByUser(Long idSedUser) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		try {
			return dao.loadStudentResponsibleListByUser(idSedUser);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	public SedUser loadStudentGradeCourse(SedUser sedUser) throws Exception {
		StudentDAO dao = new StudentDAO();
		try {
			Student gradeCourse = dao.loadStudentGradeCourse(sedUser.getId());
			sedUser.setStudentGradeName(gradeCourse.getGradeName());
			sedUser.setIdStudentGrade(gradeCourse.getIdGrade());
			sedUser.setStudentCourseName(gradeCourse.getCourseName());
			sedUser.setIdStudentCourse(gradeCourse.getIdCourse());
			return sedUser;
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}


}
