package co.edu.udistrital.sed.user.controller;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserDAO;

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



}
