package co.edu.udistrital.sed.assignment.controller;

import java.util.Date;
import java.util.List;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserDAO;
import co.edu.udistrital.sed.model.Assignment;
import co.edu.udistrital.sed.model.AssignmentDAO;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.model.SubjectDAO;

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
	public boolean saveTeacherAssignment(Assignment assignment) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			dao.getSession().saveOrUpdate(assignment);
			tx.commit();
			return true;
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
	public List<Assignment> loadAssignmentListByTeacher(Long idSedUser) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		try {
			return dao.loadAssignmentListByTeacher(idSedUser);
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
	public boolean deleteTeacherAssignment(Long idAssignment, String user) throws Exception{
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

}
