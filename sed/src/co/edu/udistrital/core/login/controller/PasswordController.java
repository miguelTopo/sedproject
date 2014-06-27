package co.edu.udistrital.core.login.controller;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.model.SedUserDAO;

public class PasswordController extends Controller{
	
	private static final long serialVersionUID = -2401144292039751003L;
	
	public boolean   rightOldPass(Long idUser,String password) {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		boolean success= false;
		
		try {
			
	tx = dao.getSession().beginTransaction();
	success= dao.validateOldPassword(idUser, password);
	tx.commit();
	
	
	
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return success;
	}
	
	
	public boolean updateSedUserPassword(Long idSedUser, String password) throws Exception {
		SedUserDAO dao = new SedUserDAO();
		Transaction tx = null;
		boolean success = false;
		try {
			if	(dao.validateOldPassword(idSedUser, password))
			{
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
}
