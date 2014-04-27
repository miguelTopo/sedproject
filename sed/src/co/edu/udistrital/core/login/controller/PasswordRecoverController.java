package co.edu.udistrital.core.login.controller;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserDAO;

public class PasswordRecoverController extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4451523140750334210L;

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
	public SedUser loadSedUser(String email) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.loadSedUser(email);
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
	public boolean updateSedUserPassword(Long idSedUser, String password) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		boolean success = false;
		try {
			tx = dao.getSession().beginTransaction();
			success = dao.updateSedUserPassword(idSedUser, password);
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

}
