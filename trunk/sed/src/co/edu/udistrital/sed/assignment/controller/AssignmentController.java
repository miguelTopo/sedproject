package co.edu.udistrital.sed.assignment.controller;

import java.util.List;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserDAO;
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

}
