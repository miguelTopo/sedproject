package co.edu.udistrital.sed.user.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.api.IEmailTemplate;
import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.model.EmailTemplate;
import co.edu.udistrital.core.common.util.FieldValidator;
import co.edu.udistrital.core.common.util.RandomPassword;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.mail.io.mn.aws.MailGeneratorFunction;
import co.edu.udistrital.core.mail.io.mn.aws.SMTPEmail;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Student;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "sedUserBean", pattern = "/portal/usuarios", viewId = "/pages/sedUser/sedUser.jspx")
public class SedUserBean extends BackingBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6302926772676488811L;

	// Primitives
	private boolean showList = false, showAdd = false, showEdit = false, showDetail = false;
	private boolean existIdentification = false, existEmail = false;
	private boolean randomPassword;

	// Basic Java Data Object
	private String userPassword;
	private String confirmPassword;
	private Long idFilterSedRole;
	private Long idFilterIdentificationType;
	private Long idStudentResponsible;

	// User List
	private List<SedUser> sedUserList;
	private List<SedUser> sedUserFilteredList;
	private List<Course> courseTmpList;
	private List<Student> studentResponsibleList;
	private List<Student> studentList;

	// User Object
	private SedUser sedUser;
	private SedUser sedUserCopy;
	private SedUser selectedSedUser;
	private Student student;
	private Student studentSelected;

	// Controller Object
	private SedUserController controller;

	/** @author MTorres */
	public SedUserBean() {
		try {
			this.controller = new SedUserController();
			this.sedUserList = this.sedUserFilteredList = this.controller.loadSedUserList();
			setShowList(true);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/** @author MTorres 18/06/2014 20:10:12 */
	public void deleteSedUser() {
		try {
			if (this.selectedSedUser != null) {
				if (this.controller.deleteSedUser(this.selectedSedUser, getUserSession() != null ? getUserSession().getIdentification() : "admin")) {
					this.sedUserList.remove(this.selectedSedUser);
					this.sedUserFilteredList.remove(this.selectedSedUser);
					addInfoMessage("Eliminar Usuario", "El Usuario fue eliminado correctamente.");
				}
			}
		} catch (Exception e) {
			addErrorMessage(getMessage("page.core.labelHeaderError"), getMessage("page.core.labelSummaryError"));
			e.printStackTrace();
		}
	}

	/** @author MTorres 22/06/2014 13:15:19 */
	private void sendMailUpdateSedLogin(final SedUser su, final String password) {
		try {
			EmailTemplate t =
				MailGeneratorFunction.getEmailTemplate(password != null ? IEmailTemplate.SED_PASSWORD_USER_CHANGE : IEmailTemplate.SED_USER_CHANGE);
			SMTPEmail e = new SMTPEmail();

			String[] paramList = new String[password != null ? 3 : 2];
			paramList[0] = su.getName() + " " + su.getLastName();
			paramList[1] = su.getIdentification();

			if (password != null)
				paramList[2] = password;

			e.sendProcessMail(null, t.getSubject(), MailGeneratorFunction.createGenericMessage(t.getBody(), t.getAnalyticsCode(), paramList),
				su.getEmail());


		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 18/06/2014 22:17:20 */
	private void threadUpdateSedLogin(String userPassword) {
		try {
			final String password = userPassword;
			final SedUser su = this.sedUser;
			new Thread(new Runnable() {

				public void run() {
					try {
						sendMailUpdateSedLogin(su, password);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 18/06/2014 20:10:12 */
	public void updateSedUser() {
		try {
			if (!validateSedUser())
				return;

			this.sedUser.setEmail(this.sedUser.getEmail().trim().toLowerCase());

			boolean updSedLogin = false;
			boolean updSedRoleUser = false;

			if (isRandomPassword() || !this.userPassword.trim().isEmpty()
				|| !this.sedUserCopy.getIdentification().equals(this.sedUser.getIdentification())) {
				updSedLogin = true;
			}
			if (!this.sedUserCopy.getIdSedRole().equals(this.sedUser.getIdSedRole()))
				updSedRoleUser = true;

			String password = null;
			if (isRandomPassword())
				password = RandomPassword.getPassword(7);

			else if (!this.userPassword.trim().isEmpty())
				password = this.userPassword.trim();

			if (this.controller.updateSedUser(this.sedUser, updSedLogin, updSedRoleUser, password, getUserSession() != null ? getUserSession()
				.getIdentification() : "admin")) {
				addInfoMessage("Actualizar Usuario", "El Usuario se ha actualizado  correctamente.");

				if (updSedLogin)
					threadUpdateSedLogin(password);

				this.sedUserCopy = null;
				cleanVar();
				goBack();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveSedUser() {
		try {
			if (!validateSedUser())
				return;

			this.sedUser.setEmail(this.sedUser.getEmail().trim().toLowerCase());

			if (this.controller.saveSedUser(this.sedUser, this.userPassword, getUserSession() != null ? getUserSession().getIdentification()
				: "admin")) {
				this.sedUserList.add(this.sedUser);
				this.sedUserFilteredList = this.sedUserList;
				threadSaveSedUser();

				cleanVar();
				goBack();
				addInfoMessage("Guardar Usuario", "El Usuario se guard� exitosamente.");
			}

		} catch (Exception e) {
			addFatalMessage("Guardar Usuario",
				"Ocurri� un error inesperado y no fu� posible agregar el usuario. Por favor consulte al administrador del sistema.");
			e.printStackTrace();
		}
	}

	/** @author MTorres 17/06/2014 23:31:01 */
	private boolean validateEditPassword() {
		try {
			if (!isRandomPassword() && this.userPassword.trim().isEmpty() && this.confirmPassword.trim().isEmpty())
				return true;
			else if (isRandomPassword())
				return true;
			else if (!this.userPassword.equals(this.confirmPassword)) {
				addWarnMessage("Crear Usuario", "Las contrase�as no coinciden, por favor verifique.");
				return false;
			} else
				return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres */
	private boolean validatePassword() {
		try {
			if (!isRandomPassword()) {
				if (this.userPassword == null || this.userPassword.trim().isEmpty()) {
					addWarnMessage("Crear Usuario", "Por favor diligencie la contrase�a.");
					return false;
				} else if (this.confirmPassword == null || this.confirmPassword.trim().isEmpty()) {
					addWarnMessage("Crear Usuario", "Por favor repita la contrase�a.");
					return false;
				} else if (!this.userPassword.equals(this.confirmPassword)) {
					addWarnMessage("Crear Usuario", "Las contrase�as no coinciden, por favor verifique.");
					return false;
				} else
					return true;
			} else {
				this.userPassword = RandomPassword.getPassword(7);
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 17/06/2014 19:59:32 */
	private void threadSaveSedUser() {
		try {
			final SedUser su = this.sedUser;
			final String pw = this.userPassword;
			new Thread(new Runnable() {

				public void run() {
					try {
						sendNewSedUserAccount(su, pw);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres */
	private void sendNewSedUserAccount(final SedUser su, final String userPassword) {
		try {
			EmailTemplate t = MailGeneratorFunction.getEmailTemplate(IEmailTemplate.NEW_SEDUSER_ACCOUNT);
			SMTPEmail e = new SMTPEmail();
			e.sendProcessMail(null, t.getSubject(), MailGeneratorFunction.createGenericMessage(t.getBody(), t.getAnalyticsCode(),
				su.getName() + su.getLastName(), su.getIdentification(), userPassword), su.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 25/06/2014 21:38:50 */
	public void validateStudentList() {
		try {
			if (this.studentResponsibleList != null && !this.studentResponsibleList.isEmpty()) {
				getRequestContext().execute("PF('dlgStudentSelectWV').hide();");
				addInfoMessage("Agregar Estudiante(s)", "Estudiantes seleccionados correctamente.");
			} else {
				addWarnMessage("Agregar Estudiante(s)", "No ha seleccionado ning�n estudiante.");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void validateIdentification() {
		try {
			setExistIdentification(false);
			if (this.sedUser != null) {
				if (this.sedUser.getIdentification() != null && !this.sedUser.getIdentification().trim().isEmpty()) {
					setExistIdentification(this.controller.validateExistField(SedUser.class.getSimpleName(), "identification",
						this.sedUser.getIdentification()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void validateEmail() {
		try {
			setExistEmail(false);
			if (isShowEdit()) {
				if (!this.sedUser.getEmail().trim().toLowerCase().equals(this.sedUserCopy.getEmail().trim().toLowerCase()))
					setExistEmail(this.controller.validateExistField(SedUser.class.getSimpleName(), "email", this.sedUser.getEmail().trim()
						.toLowerCase()));
			} else if (this.sedUser != null) {
				if (this.sedUser.getEmail() != null && !this.sedUser.getEmail().trim().isEmpty()) {
					setExistEmail(this.controller.validateExistField(SedUser.class.getSimpleName(), "email", this.sedUser.getEmail().trim()
						.toLowerCase()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	private boolean validateSedUser() {
		try {
			if (this.sedUser == null) {
				addWarnMessage("Crear Usuario", "Por favor diligencie todos los valores para crear el usuario.");
				return false;
			} else if (this.sedUser.getLastName() == null || this.sedUser.getLastName().trim().isEmpty()) {
				addWarnMessage("Crear Usuario", "Por favor diligencie los apellidos.");
				return false;
			} else if (this.sedUser.getName() == null || this.sedUser.getName().trim().isEmpty()) {
				addWarnMessage("Crear Usuario", "Por favor diligencie los nombres.");
				return false;
			} else if (this.sedUser.getIdIdentificationType() == null || this.sedUser.getIdIdentificationType().equals(0L)) {
				addWarnMessage("Crear Usuario", "Por favor seleccione el tipo de identificaci�n.");
				return false;
			} else if (this.sedUser.getIdentification() == null || this.sedUser.getIdentification().trim().isEmpty()) {
				addWarnMessage("Crear Usuario", "Por favor diligencie el n�mero de identificaci�n.");
				return false;
			} else if (this.sedUser.getEmail() == null || this.sedUser.getEmail().trim().isEmpty()) {
				addWarnMessage("Crear Usuario", "Por favor diligencie el correo electr�nico.");
				return false;
			} else if (!FieldValidator.isValidEmail(this.sedUser.getEmail().trim())) {
				addWarnMessage("Crear Usuario", "El correo electr�nico ingresado no es v�lido.");
				return false;
			} else if (this.sedUser.getIdSedRole() == null || this.sedUser.getIdSedRole().equals(0L)) {
				addWarnMessage("Crear Usuario", "Por favor seleccione el tipo de usuario.");
				return false;
			} else if (isExistIdentification()) {
				addWarnMessage("Crear Usuario", "El n�mero de identificaci�n ya se encuentra registrado. Debe modificarlo para continuar.");
				return false;
			} else if (isExistEmail()) {
				addWarnMessage("Crear Usuario", "El correo seleccionado ya se encuentra registrado. Debe modificarlo para continuar.");
				return false;
			} else if (isShowAdd())
				return validatePassword();
			else if (isShowEdit())
				return validateEditPassword();

			return true;

		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres */
	public void activeRandomPassword() {
		try {
			setRandomPassword(!isRandomPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 22/06/2014 18:59:52 */
	public void loadStudentByCourse() {
		try {
			this.studentList = null;
			if (this.student != null && this.student.getIdGrade() != null && !this.student.getIdGrade().equals(0L)
				&& this.student.getIdCourse() != null && !this.student.getIdCourse().equals(0L))
				this.studentList = this.controller.loadStudentList(this.student.getIdCourse());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeStudent() {
		try {
			if (this.studentSelected != null && this.studentResponsibleList != null) {
				this.studentResponsibleList.remove(this.studentSelected);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelStudentList() {
		try {
			this.studentResponsibleList = null;
			this.studentList = null;
			this.studentList = new ArrayList<Student>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addStudent() {
		try {
			if (this.studentSelected != null) {
				if (this.studentResponsibleList != null) {
					if (this.studentResponsibleList.contains(this.studentSelected))
						addWarnMessage("Seleccionar Estudiante", "El estudiante no se puede elegir m�s de una vez.");
					else
						this.studentResponsibleList.add(this.studentSelected);
				} else
					getStudentResponsibleList().add(this.studentSelected);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleGradeChange() {
		try {
			this.courseTmpList = null;
			if (this.student != null) {
				if (this.student.getIdGrade() != null && !this.student.getIdGrade().equals(0L))
					this.courseTmpList = loadCourseListByGrade(this.student.getIdGrade());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleRoleChange() {
		try {
			if (this.sedUser != null) {
				this.student = null;
				this.studentResponsibleList = null;
				if (this.sedUser.getIdSedRole().equals(ISedRole.STUDENT) || this.sedUser.getIdSedRole().equals(ISedRole.STUDENT_RESPONSIBLE))
					this.student = new Student();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goSelectStudent() {
		try {
			this.courseTmpList = null;
			this.idStudentResponsible = null;
			this.idStudentResponsible = 0L;
			this.student = null;
			this.student = new Student();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void goAddUser() {
		try {
			hideAll();
			setShowAdd(true);
			this.sedUser = new SedUser();
			this.student = new Student();
			setPanelView("addSedUser", "Agregar Usuario", "SedUserBean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cleanVar() {
		try {
			this.sedUser = null;
			this.sedUser = new SedUser();
			this.userPassword = null;
			this.confirmPassword = null;
			setRandomPassword(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void goDetailUser() {
		try {
			if (this.selectedSedUser != null) {
				hideAll();
				setShowDetail(true);
				setPanelView("detailSedUser", "Detallar Usuario", "SedUserBean");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void goBack() {
		try {
			hideAll();
			this.sedUserList = this.sedUserFilteredList = this.controller.loadSedUserList();
			setShowList(true);
			setPanelView("sedUserList", "Lista de Usuarios", "SedUserBean");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void goEditUser() {
		try {
			hideAll();
			this.sedUserCopy = this.sedUser.clone();
			setExistEmail(false);
			setExistIdentification(false);
			setShowEdit(true);
			setPanelView("addSedUser", "Editar Usuario", "SedUserBean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	private void hideAll() {
		try {
			setShowAdd(false);
			setShowDetail(false);
			setShowEdit(false);
			setShowList(false);
			this.sedUserCopy = null;
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

	// ////////----------getters and setters ----------//////////

	public SedUser getSedUser() {
		return sedUser;
	}

	public void setSedUser(SedUser sedUser) {
		this.sedUser = sedUser;
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

	public boolean isShowEdit() {
		return showEdit;
	}

	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	public boolean isShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	public List<SedUser> getSedUserList() {
		return sedUserList;
	}

	public void setSedUserList(List<SedUser> sedUserList) {
		this.sedUserList = sedUserList;
	}

	public SedUser getSelectedSedUser() {
		return selectedSedUser;
	}

	public void setSelectedSedUser(SedUser selectedSedUser) {
		this.selectedSedUser = selectedSedUser;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}



	public boolean isExistIdentification() {
		return existIdentification;
	}

	public void setExistIdentification(boolean existIdentification) {
		this.existIdentification = existIdentification;
	}

	public boolean isExistEmail() {
		return existEmail;
	}

	public void setExistEmail(boolean existEmail) {
		this.existEmail = existEmail;
	}

	public boolean isRandomPassword() {
		return randomPassword;
	}

	public void setRandomPassword(boolean randomPassword) {
		this.randomPassword = randomPassword;
	}

	public List<SedUser> getSedUserFilteredList() {
		return sedUserFilteredList;
	}

	public void setSedUserFilteredList(List<SedUser> sedUserFilteredList) {
		this.sedUserFilteredList = sedUserFilteredList;
	}

	public Long getIdFilterSedRole() {
		return idFilterSedRole;
	}

	public void setIdFilterSedRole(Long idFilterSedRole) {
		this.idFilterSedRole = idFilterSedRole;
	}

	public Long getIdFilterIdentificationType() {
		return idFilterIdentificationType;
	}

	public void setIdFilterIdentificationType(Long idFilterIdentificationType) {
		this.idFilterIdentificationType = idFilterIdentificationType;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public List<Course> getCourseTmpList() {
		return courseTmpList;
	}

	public void setCourseTmpList(List<Course> courseTmpList) {
		this.courseTmpList = courseTmpList;
	}

	public Long getIdStudentResponsible() {
		return idStudentResponsible;
	}

	public void setIdStudentResponsible(Long idStudentResponsible) {
		this.idStudentResponsible = idStudentResponsible;
	}

	public List<Student> getStudentResponsibleList() {
		if (studentResponsibleList == null)
			studentResponsibleList = new ArrayList<Student>();
		return studentResponsibleList;
	}

	public void setStudentResponsibleList(List<Student> studentResponsibleList) {
		this.studentResponsibleList = studentResponsibleList;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public Student getStudentSelected() {
		return studentSelected;
	}

	public void setStudentSelected(Student studentSelected) {
		this.studentSelected = studentSelected;
	}


}
