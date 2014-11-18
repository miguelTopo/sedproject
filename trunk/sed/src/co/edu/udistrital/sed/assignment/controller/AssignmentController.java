package co.edu.udistrital.sed.assignment.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserDAO;
import co.edu.udistrital.sed.api.IAssignmentType;
import co.edu.udistrital.sed.model.Assignment;
import co.edu.udistrital.sed.model.AssignmentDAO;
import co.edu.udistrital.sed.model.CourseDAO;
import co.edu.udistrital.sed.model.Grade;
import co.edu.udistrital.sed.model.GradeDAO;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.model.SubjectDAO;
import co.edu.udistrital.sed.student.model.StudentDAO;
import co.edu.udistrital.session.common.User;

public class AssignmentController extends Controller {

	/** @author MTorres 14/7/2014 23:27:32 */
	public List<SedUser> loadSedUserByRole(Long idSedRole) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		try {
			return dao.loadSedUserByRole(idSedRole);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 14/7/2014 23:45:08 */
	public List<Subject> loadSubjectList() throws Exception {
		SubjectDAO dao = new SubjectDAO();
		try {
			return dao.loadSubjectList();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 22/7/2014 23:09:53 */
	public Long saveTeacherAssignment(Assignment assignment) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			dao.getSession().saveOrUpdate(assignment);
			tx.commit();
			return assignment.getId();
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

	/** @author MTorres 23/7/2014 23:01:13 */
	public List<Assignment> loadAssignmentListByPeriod(Long idPeriod) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		try {
			return dao.loadAssignmentListByPeriod(idPeriod);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 25/7/2014 23:02:51 */
	public boolean validAvailability(Long idAssignment, Long idCourse, Long idDay, Date startDate, Date endDate, Long idSedUser) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		try {
			return dao.validAvailability(idAssignment, idCourse, idDay, startDate, endDate, idSedUser);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}

	}

	/** @author MTorres 28/7/2014 23:49:28 */
	public List<Assignment> loadAssignmentListByTeacher(Long idSedUser, Long idSedRole) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		try {
			return dao.loadAssignmentListByTeacher(idSedUser, idSedRole);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 28/7/2014 23:59:26 */
	public List<Assignment> loadAssignmentListByCourse(Long idCourse) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		try {
			return dao.loadAssignmentListByCourse(idCourse);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 30/7/2014 23:23:06 */
	public boolean deleteTeacherAssignment(Long idAssignment, String user) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		Transaction tx = null;
		boolean success = false;
		try {
			tx = dao.getSession().beginTransaction();
			success = dao.deleteTeacherAssignment(idAssignment, user);
			tx.commit();
			return success;
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

	/**
	 * @author Miguel 17/11/2014 13:02:33
	 */
	public List<Assignment> loadAssignmentDefault(User sedUser) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		try {
			return dao.loadAssignmentDefault(sedUser);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/**
	 * @author Miguel 17/11/2014 13:54:13
	 */
	public List<Assignment> loadAssignmentBySedUser(Long idSedUser, Long idSedRole, Long idAssignmentType) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		try {
			if(idSedRole.equals(ISedRole.STUDENT_RESPONSIBLE))
				idAssignmentType= IAssignmentType.ATTENTION;
			return dao.loadAssignmentBySedUser(idSedUser, idSedRole, idAssignmentType);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/**
	 * @author Miguel 17/11/2014 16:09:54
	 */
	public List<SedUser> loadTeacherListByCourseList(List<Long> idCourseList) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		try {
			return dao.loadTeacherListByCourseList(idCourseList);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/**
	 * @author Miguel 17/11/2014 16:55:48
	 * @return
	 */
	public List<Long> loadStudentCourse(Long idSedUser, final Long idStudent, Long idSedRole) throws Exception {
		CourseDAO dao = new CourseDAO();
		try {
			List<Long> idStudentList = null;

			if (idSedRole.equals(ISedRole.STUDENT))
				idStudentList = new ArrayList<Long>() {
					{
						add(idStudent);
					}
				};
			else if (idSedRole.equals(ISedRole.STUDENT_RESPONSIBLE))
				idStudentList = dao.loadStudentByResponsible(idSedUser);

			return dao.loadStudentCourse(idSedUser, idStudentList, idSedRole);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/**
	 * @author Miguel 17/11/2014 17:41:29
	 */
	public List<Grade> loadGradeListByCourseList(List<Long> idCourseList) throws Exception {
		GradeDAO dao = new GradeDAO();
		try {
			return dao.loadGradeListByCourseList(idCourseList);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/**
	 * @author Miguel 18/11/2014 8:06:19
	 */
	public Long loadStudentCourse(Long idStudent) throws Exception{
		StudentDAO dao = new StudentDAO();
		try {
			return dao.loadStudentCourse(idStudent);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		}finally{
			dao.getSession().close();
			dao = null;
		}
	}

}
