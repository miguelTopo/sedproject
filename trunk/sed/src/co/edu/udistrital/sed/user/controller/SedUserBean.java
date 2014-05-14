package co.edu.udistrital.sed.user.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.api.IEmailTemplate;
import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.model.EmailTemplate;
import co.edu.udistrital.core.common.util.FieldValidator;
import co.edu.udistrital.core.common.util.RandomPassword;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserLogin;
import co.edu.udistrital.core.mail.io.mn.aws.MailGeneratorFunction;
import co.edu.udistrital.core.mail.io.mn.aws.SMTPEmail;

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
	private boolean existIdentification = false, existEmail = false, existUserName = false;
	private boolean randomPassword;



	// Basic Java Data Object
	private String userPassword;
	private String confirmPassword;

	// User List
	private List<SedUser> sedUserList;
	private List<SedUser> sedUserFilteredList;

	// User Object
	private SedUser sedUser;
	private SedUser selectedSedUser;

	// Controller Object
	private SedUserController controller;

	/** @author MTorres */
	public SedUserBean() {
		try {
			this.controller = new SedUserController();
			this.sedUserList = this.sedUserFilteredList= this.controller.loadSedUserList();
			setShowList(true);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void saveSedUser() {
		try {
			if (!validateSedUser())
				return;
			else {
				if (this.controller.saveSedUser(this.sedUser, this.userPassword, "admin")) {
					sendNewSedUserAccount();
					cleanVar();
					goBack();
					addInfoMessage("Guardar Usuario", "El Usuario se guardó exitosamente.");
				}
			}
		} catch (Exception e) {
			addFatalMessage("Guardar Usuario",
				"Ocurrió un error inesperado y no fué posible agregar el usuario. Por favor consulte al administrador del sistema.");
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	private boolean validatePassword() {
		try {
			if (!isRandomPassword()) {
				if (this.userPassword == null || this.userPassword.trim().isEmpty()) {
					addWarnMessage("Crear Usuario", "Por favor diligencie la contraseña.");
					return false;
				} else if (this.confirmPassword == null || this.confirmPassword.trim().isEmpty()) {
					addWarnMessage("Crear Usuario", "Por favor repita la contraseña.");
					return false;
				} else if (!this.userPassword.equals(this.confirmPassword)) {
					addWarnMessage("Crear Usuario", "Las contraseñas no coinciden, por favor verifique.");
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

	/** @author MTorres */
	public void sendNewSedUserAccount() {
		try {
			EmailTemplate t = MailGeneratorFunction.getEmailTemplate(IEmailTemplate.NEW_SEDUSER_ACCOUNT);
			SMTPEmail e = new SMTPEmail();
			e.sendProcessMail(null, t.getSubject(), MailGeneratorFunction.createGenericMessage(t.getBody(), t.getAnalyticsCode(),
				this.sedUser.getName() + this.sedUser.getLastName(), this.sedUser.getUserName(), this.userPassword), "migueltrock@gmail.com");
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

	public void validateSedUserName() {
		try {
			setExistUserName(false);
			if (this.sedUser != null) {
				if (this.sedUser.getUserName() != null && !this.sedUser.getUserName().trim().isEmpty()) {
					setExistUserName(this.controller.validateExistField(SedUserLogin.class.getSimpleName(), "userName", this.sedUser.getUserName()));
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
			if (this.sedUser != null) {
				if (this.sedUser.getEmail() != null && !this.sedUser.getEmail().trim().isEmpty()) {
					setExistEmail(this.controller.validateExistField(SedUser.class.getSimpleName(), "email", this.sedUser.getEmail()));
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
				addWarnMessage("Crear Usuario", "Por favor seleccione el tipo de identificación.");
				return false;
			} else if (this.sedUser.getIdentification() == null || this.sedUser.getIdentification().trim().isEmpty()) {
				addWarnMessage("Crear Usuario", "Por favor diligencie el número de identificación.");
				return false;
			} else if (this.sedUser.getEmail() == null || this.sedUser.getEmail().trim().isEmpty()) {
				addWarnMessage("Crear Usuario", "Por favor diligencie el correo electrónico.");
				return false;
			} else if (!FieldValidator.isValidEmail(this.sedUser.getEmail().trim())) {
				addWarnMessage("Crear Usuario", "El correo electrónico ingresado no es válido.");
				return false;
			} else if (this.sedUser.getUserName() == null || this.sedUser.getUserName().trim().isEmpty()) {
				addWarnMessage("Crear Usuario", "Por favor diligencie el nombre de usuario.");
				return false;
			} else if (this.sedUser.getIdSedRole() == null || this.sedUser.getIdSedRole().equals(0L)) {
				addWarnMessage("Crear Usuario", "Por favor seleccione el tipo de usuario.");
				return false;
			} else if (isExistIdentification()) {
				addWarnMessage("Crear Usuario", "El número de identificación ya se encuentra registrado. Debe modificarlo para continuar.");
				return false;
			} else if (isExistEmail()) {
				addWarnMessage("Crear Usuario", "El correo seleccionado ya se encuentra registrado. Debe modificarlo para continuar.");
				return false;
			} else if (isExistUserName()) {
				addWarnMessage("Crear Usuario", "El nombre de usaurio ya se encuentra registrado. Debe modificarlo para continuar.");
				return false;
			} else
				return validatePassword();
		} catch (Exception e) {
			throw e;
		}
	}

	/**@author MTorres*/
	public void activeRandomPassword() {
		try {
			setRandomPassword(!isRandomPassword());
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
			this.sedUserList = this.sedUserFilteredList=this.controller.loadSedUserList();
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

	public boolean isExistUserName() {
		return existUserName;
	}

	public void setExistUserName(boolean existUserName) {
		this.userPassword = this.confirmPassword = null;
		this.existUserName = existUserName;
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
	
}
