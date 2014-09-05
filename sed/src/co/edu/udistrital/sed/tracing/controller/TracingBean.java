package co.edu.udistrital.sed.tracing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Student;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "tracing", pattern = "/portal/seguimiento", viewId = "/pages/tracing/tracing.jspx")
public class TracingBean extends BackingBean {

	// Basic Java Object
	private Long idPeriod;
	private Long idGrade;
	private Long idCourse;

	// User List
	private List<Course> courseTmpList;
	private List<Student> studentList;

	// Controller
	private TracingController controller;

	public TracingBean() throws Exception {
		try {
			this.controller = new TracingController();
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 4/9/2014 23:42:06 */
	private List<Long> loadCourseList() throws Exception {
		try {
			List<Course> courseTmpList = loadCourseListByGrade(this.idGrade);
			List<Long> idCourseList = new ArrayList<Long>(courseTmpList.size());

			for (Course c : courseTmpList)
				idCourseList.add(c.getId());

			return idCourseList;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 4/9/2014 23:18:12 */
	public void loadStudentList() {
		try {
			if (!validateLoadStudentList())
				return;
			this.studentList = null;
			List<Long> idCourseList = null;


			if ((this.idGrade != null && !this.idGrade.equals(0L)) && (this.idCourse == null || this.idCourse.equals(0L)))
				idCourseList = loadCourseList();



			this.studentList = this.controller.loadStudentList(this.idPeriod, this.idGrade, this.idCourse, idCourseList);
			if (this.studentList == null || this.studentList.isEmpty())
				addInfoMessage("Buscar", "No se encontró ningun estudiante con los criterios seleccionados.");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 4/9/2014 23:12:12 */
	private boolean validateLoadStudentList() throws Exception {
		try {
			if (this.idPeriod == null || this.idPeriod.equals(0L)) {
				addWarnMessage("Buscar", "El criterio minimo de búsqueda es el periodo, por favor seleccione uno.");
				return false;
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}


	public boolean getValidateSedUserRole() throws Exception {
		try {
			if (getUserSession().getIdSedRole().equals(ISedRole.ADMINISTRATOR))
				return true;
			else
				return false;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Course> getCourseTmpList() {
		return courseTmpList;
	}

	public void setCourseTmpList(List<Course> courseTmpList) {
		this.courseTmpList = courseTmpList;
	}

	public Long getIdPeriod() {
		return idPeriod;
	}

	public void setIdPeriod(Long idPeriod) {
		this.idPeriod = idPeriod;
	}

	public Long getIdGrade() {
		return idGrade;
	}

	public void setIdGrade(Long idGrade) {
		this.idGrade = idGrade;
	}

	public Long getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(Long idCourse) {
		this.idCourse = idCourse;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

}
