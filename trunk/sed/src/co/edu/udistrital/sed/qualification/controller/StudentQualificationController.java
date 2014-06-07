package co.edu.udistrital.sed.qualification.controller;

import java.util.List;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.QualificationDAO;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.student.model.StudentDAO;

public class StudentQualificationController extends Controller {

	public List<Qualification> loadQualificationListByStudent(Long idStudent, int idPeriod) throws Exception {
		QualificationDAO dao = null;
		Transaction tx = null;
		try {
			dao = new QualificationDAO();
			tx = dao.getSession().beginTransaction();

			return dao.loadQualificationListByStudent(idStudent, idPeriod);
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			dao.getSession().cancelQuery();
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres 7/06/2014 */
	public Student loadStudent(Long idSedUser) throws Exception {
		StudentDAO dao = null;
		Transaction tx = null;
		try {
			dao = new StudentDAO();
			tx = dao.getSession().beginTransaction();
			return dao.loadStudent(idSedUser);
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			dao.getSession().cancelQuery();
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

}
