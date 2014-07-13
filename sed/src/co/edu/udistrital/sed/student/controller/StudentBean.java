package co.edu.udistrital.sed.student.controller;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.api.IEmailTemplate;
import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.model.EmailTemplate;
import co.edu.udistrital.core.common.util.FieldValidator;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.common.util.RandomPassword;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.mail.io.mn.aws.MailGeneratorFunction;
import co.edu.udistrital.core.mail.io.mn.aws.SMTPEmail;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Student;

@ManagedBean
@ViewScoped
@URLMapping(id = "studentBean", pattern = "/portal/estudiantes", viewId = "/pages/student/student/student.jspx")
public class StudentBean extends BackingBean {


	// Primitives
	private boolean showList = false, showAdd = false, showDetail = false, showEdit = false;
	private boolean existDocument = false;
	private boolean existEmail = false;

	// Basic Java Object
	private Long grade;
	private Long course;
	private Long miDato;
	private Date studentBirthday;

	// User List
	private List<Student> studentList;
	private List<Student> studentFilteredList;
	private List<Course> courseTmpList;

	// User Object
	private Student studentSelected;
	private Student student;

	// Controller
	private StudentController controller;

	/** @author MTorres */
	public StudentBean() throws Exception{
		try {
			if (getUserSession() != null && getUserSession().getIdSedRoleUser().equals(ISedRole.ADMINISTRATOR)) {
				this.controller = new StudentController();
				setShowList(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void loadStudentList() {
		try {
			if (!validateLoadStudentList())
				return;

			this.studentList = this.studentFilteredList = this.controller.loadStudentList(this.course);
			if (this.studentList == null || this.studentList.isEmpty()) {
				addWarnMessage("Cargar Estudiantes", "No existen estudiantes con los par�metros ingresados.");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** @author MTorres 
	 * @throws Exception */
	private void threadSaveStudent(String password) throws Exception {
		try {
			final String pass = password;
			final Student s = this.student;

			if (FieldValidator.isValidEmail(s.getEmail())) {
				new Thread(new Runnable() {

					public void run() {
						try {
							sendAddStudentEmail(pass, s);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

		} catch (Exception e) {
			throw e;
		}

	}

	/** @author MTorres 
	 * @throws Exception */
	private void sendAddStudentEmail(final String userPassword, final Student s) throws Exception {
		try {
			EmailTemplate t = MailGeneratorFunction.getEmailTemplate(IEmailTemplate.NEW_STUDENT);
			SMTPEmail e = new SMTPEmail();
			e.sendProcessMail(
				null,
				t.getSubject(),
				MailGeneratorFunction.createGenericMessage(t.getBody(), t.getAnalyticsCode(), s.getName() + " " + s.getLastName(),
					s.getIdentification(), userPassword), s.getEmail());
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres */
	public void saveStudent() {
		try {
			if (!validateSaveStudent())
				return;

			String password = RandomPassword.getPassword(7);
			System.out.println("************************************LA CONTRASE�A ES: " + password + "****************************");
//			this.student.setBirthday(ManageDate.formatDate(this.studentBirthday, ManageDate.YYYY_MM_DD));

			if (this.controller.saveStudent(this.student, getUserSession() != null ? getUserSession().getIdentification() : "admin", password)) {
				threadSaveStudent(password);
				password = null;
				addInfoMessage("Guardar Estudiante", "El estudiante fu� almacenado correctamente.");
				goBack();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void deleteStudent() {
		try {
			if (this.studentSelected != null) {
				if (this.controller.deleteStudent(this.studentSelected.getId(), this.studentSelected.getIdSedUser(),
					getUserSession() != null ? getUserSession().getIdentification() : "admin")) {
					this.studentList.remove(this.studentSelected);
					this.studentFilteredList = this.studentList;
					this.studentSelected = null;
					this.studentSelected = new Student();
					addInfoMessage("Eliminar Estudiante", "El estudiante se elimin� correctamente.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public boolean getValidateSedUserRole() throws Exception {
		try {
			if (getUserSession() != null) {
				if (getUserSession().getIdSedRoleUser().equals(ISedRole.ADMINISTRATOR))
					return true;
				else
					return false;
			} else
				return false;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	private boolean validateSaveStudent() throws Exception {
		try {
			if (this.student == null)
				return false;

			if (isExistDocument()) {
				addWarnMessage("Guardar Estudiante", "El documento ya existe por favor verifique.");
				return false;
			} else if (isExistEmail()) {
				addWarnMessage("Guardar Estudiante", "El coreo electr�nico ya existe por favor verifique.");
				return false;
			} else if (this.student.getName() == null || this.student.getName().trim().isEmpty()) {
				addWarnMessage("Guardar Estudiante", "Por favor digite los nombres del estudiante.");
				return false;
			} else if (this.student.getLastName() == null || this.student.getLastName().trim().isEmpty()) {
				addWarnMessage("Guardar Estudiante", "Por favor digite los apellidos del estudiante.");
				return false;
			} else if (this.student.getEmail() == null || this.student.getEmail().trim().isEmpty()) {
				addWarnMessage("Guardar Estudiante", "Por favor digite el correo del estudiante.");
				return false;
			} else if (!FieldValidator.isValidEmail(this.student.getEmail())) {
				addWarnMessage("Guardar Estudiante", "El correo ingresado no es un correo v�lido. Por favor verifique.");
				return false;
			} else if (this.studentBirthday == null) {
				addWarnMessage("Guardar Estudiante", "Por favor ingrese la fecha de nacimiento.");
				return false;
			} else if (this.student.getIdIdentificationType() == null || this.student.getIdIdentificationType().equals(0L)) {
				addWarnMessage("Guardar Estudiante", "Por favor seleccione el tipo de identificaci�n.");
				return false;
			} else if (this.student.getIdentification() == null || this.student.getIdentification().trim().isEmpty()) {
				addWarnMessage("Guardar Estudiante", "Por favor digite la identificaci�n del estudiante.");
				return false;
			} else if (this.student.getIdGrade() == null || this.student.getIdGrade().equals(0L)) {
				addWarnMessage("Guardar Estudiante", "Por favor seleccione el grado del estudiante.");
				return false;
			} else if (this.student.getIdCourse() == null || this.student.getIdCourse().equals(0L)) {
				addWarnMessage("Guardar Estudiante", "Por favor seleccione el curso del estudiante.");
				return false;
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres */
	public void validateIdentification() {
		try {
			if (this.student != null && this.student.getIdentification() != null && !this.student.getIdentification().trim().isEmpty())
				setExistDocument(this.controller.validateExistField(Student.class.getSimpleName(), "identification", this.student.getIdentification()
					.trim())
					&& this.controller.validateExistField(SedUser.class.getSimpleName(), "identification", this.student.getIdentification().trim()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void validateEmail() {
		try {
			if (this.student != null && this.student.getEmail() != null && !this.student.getEmail().trim().isEmpty())
				setExistEmail(this.controller.validateExistField(SedUser.class.getSimpleName(), "email", this.student.getEmail().trim()));

		} catch (Exception e) {
			e.printStackTrace();
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
			if (isShowAdd()) {
				if (this.student.getIdGrade() != null && !this.student.getIdGrade().equals(0L)) {
					this.courseTmpList = loadCourseListByGrade(this.student.getIdGrade());
				}
			} else {
				if (this.grade != null && !this.grade.equals(0L)) {
					this.courseTmpList = loadCourseListByGrade(this.grade);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
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

	/** @author MTorres */
	public void goDetail() {
		try {
			if (this.studentSelected != null) {
				hideAll();
				setShowDetail(true);
				setPanelView("detailStudent", "Detallar Estudiante", "studentBean");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	private Student loadStudent() throws Exception {
		try {
//			return this.controller.loadStudent(this.idStudentSelected, this.course);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	public void goEdit() {
		try {
//			if (this.idStudentSelected != null) {
//				hideAll();
//				setShowEdit(true);
//				this.student = loadStudent();
//				this.studentBirthday = ManageDate.stringToDate(this.student.getBirthday(), ManageDate.YYYY_MM_DD);
//				System.out.println(ManageDate.formatDate(this.studentBirthday, ManageDate.YYYY_MM_DD));
//				setPanelView("addStudent", "Editar Estudiante", "studentBean");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void goAdd() {
		try {
			hideAll();
			this.student = new Student();
			this.student.setIdGrade(0L);
			setShowAdd(true);
			setPanelView("addStudent", "Crear Estudiante", "studentBean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void clearVar() {
		try {
			this.student = null;
			this.student = new Student();
			this.studentBirthday = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
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

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public boolean isExistDocument() {
		return existDocument;
	}

	public void setExistDocument(boolean existDocument) {
		this.existDocument = existDocument;
	}

	public boolean isExistEmail() {
		return existEmail;
	}

	public void setExistEmail(boolean existEmail) {
		this.existEmail = existEmail;
	}

	public Date getStudentBirthday() {
		return studentBirthday;
	}

	public void setStudentBirthday(Date studentBirthday) {
		this.studentBirthday = studentBirthday;
	}

}
