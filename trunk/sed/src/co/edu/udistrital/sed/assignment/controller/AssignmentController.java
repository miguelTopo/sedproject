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
			dao.getSession().save(assignment);
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
	public boolean validSpaceAvailability(Long idCourse, Long idDay, Date startDate, Date endDate) throws Exception {
		AssignmentDAO dao = new AssignmentDAO();
		Transaction tx = null;
		try {
			return dao.validSpaceAvailability(idCourse, idDay, startDate, endDate);
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
