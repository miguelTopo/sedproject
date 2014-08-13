package co.edu.udistrital.sed.report.controller;

import java.util.List;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.sed.model.KnowledgeArea;
import co.edu.udistrital.sed.model.KnowledgeAreaDAO;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.QualificationDAO;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.student.model.StudentDAO;

public class ReportController extends Controller {

	/** @author MTorres 10/08/2014 4:16:34 p. m. */
	public List<Student> loadStudentListByGrade(List<Long> idCourseList) throws Exception {
		StudentDAO dao = new StudentDAO();
		try {
			return dao.loadStudentListByGrade(idCourseList);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

	/** @author MTorres 10/08/2014 4:21:21 p. m. */
	public List<Qualification> loadQualificationList(List<Long> idStudentCourseList) throws Exception {
		QualificationDAO dao = new QualificationDAO();
		try {
			return dao.loadQualificationList(idStudentCourseList);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

}
