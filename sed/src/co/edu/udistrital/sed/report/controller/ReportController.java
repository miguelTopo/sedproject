package co.edu.udistrital.sed.report.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.student.model.StudentDAO;

public class ReportController extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8981805231681844213L;

	/** @author MTorres */
	public List<Student> loadStudentListByGrade(List<Course> courseStudentList) throws Exception {
		StudentDAO dao = new StudentDAO();
		Transaction tx = null;
		List<Long> idCourseList = new ArrayList<>(courseStudentList.size());
		try {
			for (Course c : courseStudentList) {
				idCourseList.add(c.getId());
			}
			tx = dao.getSession().beginTransaction();
			return dao.loadStudentListByGrade(idCourseList);
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
