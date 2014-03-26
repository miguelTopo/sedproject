package co.edu.udistrital.sed.student.controller;

import java.util.List;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.student.model.StudentDAO;

public class StudentController extends Controller{

	// ////Maneja transacciones,

	public Student loadStudent(Long idStudent) throws Exception {
		StudentDAO dao = new StudentDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			// dao.getSession().save(new Student());
			// tx.commit();
			return dao.loadStudent(idStudent);
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

	/** @author PPaez */
	public List<Student> loadStudentList(Long grade, Long course)
			throws Exception {
		StudentDAO dao = new StudentDAO();
		Transaction tx = null;
		try {
			System.out.print("Controller");
			tx = dao.getSession().beginTransaction();
			return dao.loadStudentList(grade, course);
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
