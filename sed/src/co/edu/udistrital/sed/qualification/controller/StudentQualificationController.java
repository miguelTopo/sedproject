package co.edu.udistrital.sed.qualification.controller;

import java.util.List;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.QualificationDAO;

public class StudentQualificationController extends Controller {

	/** @author MTorres */
	public List<Qualification> loadStudentQualificationList(Long idSedUser, Long idSedRole) throws Exception {
		QualificationDAO dao = new QualificationDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.loadStudentQualificationList(idSedUser, idSedRole);
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
