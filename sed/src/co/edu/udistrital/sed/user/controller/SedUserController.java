package co.edu.udistrital.sed.user.controller;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.login.model.SedRoleUser;
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
			sl.setUserName(sedUser.getIdentification());
			sl.setMd5Password(ManageMD5.parseMD5(userPassword));
			sl.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));


			dao.getSession().save(sl);

			SedRoleUser sru = new SedRoleUser();
			sru.setIdSedRole(sedUser.getIdSedRole());
			sru.setIdSedUser(sedUser.getId());
			sru.setUserCreation("admin");
			sru.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			sru.setState(IState.ACTIVE);
			dao.getSession().save(sru);

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

	/** @author MTorres 17/06/2014 23:59:50 */
	public boolean updateSedUser(SedUser sedUser, boolean updSedLogin, boolean updSedRoleUser, String password) throws HibernateException, Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			sedUser.initialize(false);
			dao.getSession().update(sedUser);

			if (updSedLogin)
				dao.updateSedUserLogin(sedUser, password);

			if (updSedRoleUser)
				dao.updateSedRoleUser(sedUser);

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
