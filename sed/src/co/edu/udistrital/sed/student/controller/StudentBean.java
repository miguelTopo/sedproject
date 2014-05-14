package co.edu.udistrital.sed.student.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Student;

@ManagedBean
@ViewScoped
@URLMapping(id = "studentBean", pattern = "/portal/estudiante", viewId = "/pages/student/student.jspx")
public class StudentBean extends BackingBean {

	// static
	private static final long serialVersionUID = -8160892651775048022L;

	// Primitives
	private boolean showList = false, showAdd = false, showDetail = false, showEdit = false;

	// Basic Java Object
	private Long grade;
	private Long course;
	private Long miDato;

	// User List
	private List<Student> studentList;
	private List<Student> studentFilteredList;
	private List<Course> courseTmpList;

	// User Object
	private Student studentSelected;

	// Controller
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
			if (!validateLoadStudentList())
				return;

			this.studentList = this.studentFilteredList = this.controller.loadStudentList(this.course);
			if (this.studentList == null || this.studentList.isEmpty()) {
				addWarnMessage("Cargar Estudiantes", "No existen estudiantes con los parámetros ingresados.");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteStudent() {
		try {
			if (this.studentSelected != null) {
				if (this.controller.deleteStudent(this.studentSelected.getId(), getUserSession() != null ? getUserSession().getIdentification()
					: "admin")) {
					this.studentList.remove(this.studentSelected);
					this.studentFilteredList = this.studentList;
					this.studentSelected = null;
					this.studentSelected = new Student();
					addInfoMessage("Eliminar Estudiante", "El estudiante se eliminó correctamente.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public boolean getValidateSedUserRole() throws Exception {
		try {
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public boolean validateLoadStudentList() {
		try {
			if (this.grade == null || this.grade.equals(0L)) {
				addWarnMessage("Listar estudiantes", "Por favor indique el grado.");
				return false;
			} else if (this.course == null || this.course.equals(0L)) {
				addWarnMessage("Listar estudiantes", "Por favor indique el curso.");
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
				this.courseTmpList = loadCourseListByGrade(this.grade);
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

	public void goBack() {
		try {
			hideAll();
			clearVar();
			setShowList(true);
			setPanelView("studentList", "Listar Estudiantes", "studentBean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goDetail() {
		try {
			hideAll();
			setShowDetail(true);
			setPanelView("detailStudent", "Detallar Estudiante", "studentBean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goEdit() {
		try {
			hideAll();
			setShowEdit(true);
			setPanelView("addStudent", "Editar Estudiante", "studentBean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goAdd() {
		try {
			hideAll();
			setShowAdd(true);
			setPanelView("addStudent", "Crear Estudiante", "studentBean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearVar() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hideAll() {
		try {
			setShowAdd(false);
			setShowDetail(false);
			setShowList(false);
			setShowEdit(false);
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

	public List<Student> getStudentFilteredList() {
		return studentFilteredList;
	}

	public void setStudentFilteredList(List<Student> studentFilteredList) {
		this.studentFilteredList = studentFilteredList;
	}

	public boolean isShowEdit() {
		return showEdit;
	}

	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	public Student getStudentSelected() {
		return studentSelected;
	}

	public void setStudentSelected(Student studentSelected) {
		this.studentSelected = studentSelected;
	}



}
