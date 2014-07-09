package co.edu.udistrital.core.login.controller;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.model.SedUserDAO;

public class PasswordController extends Controller {

	private static final long serialVersionUID = -2401144292039751003L;


	public boolean updateSedUserPassword(Long idSedUser, String password) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		boolean success = false;
		try {
			if (dao.validateOldPassword(idSedUser, password)) {
				tx = dao.getSession().beginTransaction();
				success = dao.updateSedUserPassword(idSedUser, password);
				tx.commit();
				success = true;
			}
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
		return success;
	}

	/**
	 * @author MTorres
	 */
	public boolean validateOldUserPassword(Long idSedUser, String md5OldPw) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.validateOldUserPassword(idSedUser, md5OldPw);
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

	/** @author MTorres */
	public boolean updatePassword(Long idSedUser, String pwMD5, String user) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			boolean success = dao.updatePassword(idSedUser, pwMD5, user);
			tx.commit();
			return success;
		} catch (Exception e) {
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
