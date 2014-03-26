package co.edu.udistrital.sed.student.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Student;

@ManagedBean
@ViewScoped
@URLMapping(id = "studentBean", pattern = "/portal/estudiante", viewId = "/pages/student/student.jspx")
public class StudentBean extends BackingBean {

	private static final long serialVersionUID = -8160892651775048022L;
	private boolean showList = false, showAdd = false, showDetail = false;

	private Long grade;
	private Long course;
	private Long miDato;

	private List<Student> studentList;
	private List<Course> courseTmpList;

	private StudentController controller;

	public StudentBean() {
		try {
			this.controller = new StudentController();
			setShowList(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadStudentList() {
		try {
			System.out.print("Bean");
			if (validateLoadList()) {
				// TODO mirar el funcionamiento de datatable de primefaces
				// tu lista = controllwer.tufuncion en el controller -> DAO
				this.studentList = this.controller.loadStudentList(this.grade,
						this.course);
			} else {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean validateLoadList() {
		try {
			if (this.grade == null || this.grade.equals(0L)) {
				addWarnMessage("Listar estudiantes",
						"Por favor indique el grado.");
				return false;
			} else if (this.course == null || this.course.equals(0L)) {
				addWarnMessage("Listar estudiantes",
						"Por favor indique el curso.");
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void handleGradeChange() {
		try {
			if (this.grade != null && !this.grade.equals(0L)) {
				this.courseTmpList =loadCourseListByGrade(this.grade);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cargarFuncion() {
		try {
			System.out.println("Ingresando....." + this.miDato);
			if (!this.miDato.equals(0L)) {
				addInfoMessage("Agregar", "Usted eligio  " + this.miDato);
			} else {
				addErrorMessage("Agregar", "Por favor seleccioneun valor");
				return;
			}
			Student s = new Student();
			s = this.controller.loadStudent(2L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Long getMiDato() {
		return miDato;
	}

	public void setMiDato(Long miDato) {
		this.miDato = miDato;
	}

	public boolean isShowList() {
		return showList;
	}

	public void setShowList(boolean showList) {
		this.showList = showList;
	}

	public boolean isShowAdd() {
		return showAdd;
	}

	public void setShowAdd(boolean showAdd) {
		this.showAdd = showAdd;
	}

	public boolean isShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	public Long getGrade() {
		return grade;
	}

	public void setGrade(Long grade) {
		this.grade = grade;
	}

	public Long getCourse() {
		return course;
	}

	public void setCourse(Long course) {
		this.course = course;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public List<Course> getCourseTmpList() {
		return courseTmpList;
	}

	public void setCourseTmpList(List<Course> courseTmpList) {
		this.courseTmpList = courseTmpList;
	}

}
