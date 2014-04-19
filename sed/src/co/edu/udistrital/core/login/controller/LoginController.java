package co.edu.udistrital.core.login.controller;


import java.util.List;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserDAO;
import co.edu.udistrital.core.login.model.Tree;
import co.edu.udistrital.core.login.model.TreeDAO;
import co.edu.udistrital.session.common.SedSession;

public class LoginController extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4365975570263539163L;

	/** @author MTorres */
	public SedSession validateSedUser(String userName, String password) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.validateSedUser(userName, password);
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

}
