package co.edu.udistrital.sed.tracing.controller;

import java.util.List;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.sed.model.QualificationDAO;
import co.edu.udistrital.sed.model.Student;

public class TracingController extends Controller {

	/** @author MTorres 4/9/2014 23:19:51 */
	public List<Student> loadStudentList(Long idPeriod, Long idGrade, Long idCourse, List<Long> idCourseList) throws Exception {
		QualificationDAO dao = new QualificationDAO();
		try {
			return dao.loadStudentList(idPeriod, idGrade, idCourse, idCourseList);
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}
	}

}
