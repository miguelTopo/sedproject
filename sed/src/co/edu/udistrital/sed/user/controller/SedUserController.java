package co.edu.udistrital.sed.user.controller;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserDAO;
import co.edu.udistrital.core.login.model.SedUserLogin;

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
	public boolean saveSedUser(SedUser sedUser, String userPassword, String string) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {

			sedUser.initialize(true);
			sedUser.setState(IState.ACTIVE);
			tx = dao.getSession().beginTransaction();

			dao.getSession().save(sedUser);

			SedUserLogin sl = new SedUserLogin();
			sl.setUserCreation("admin");
			sl.setState(IState.ACTIVE);
			sl.setIdSedUser(sedUser.getId());
			sl.setUserName(sedUser.getUserName());
			sl.setMd5Password(ManageMD5.parseMD5(userPassword));
			sl.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));

			dao.getSession().save(sl);

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



}
