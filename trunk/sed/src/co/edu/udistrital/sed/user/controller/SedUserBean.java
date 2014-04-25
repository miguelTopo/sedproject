package co.edu.udistrital.sed.user.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.util.FieldValidator;
import co.edu.udistrital.core.login.model.SedUser;

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

	// Basic Java Data Object
	private String userPassword;
	private String confirmPassword;

	// User List
	private List<SedUser> sedUserList;

	// User Object
	private SedUser sedUser;
	private SedUser selectedSedUser;

	// Controller Object
	private SedUserController controller;

	/** @author MTorres */
	public SedUserBean() {
		try {
			this.controller = new SedUserController();
			this.sedUserList = this.controller.loadSedUserList();
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

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	private boolean validatePassword() {
		try {
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
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**@author MTorres*/
	public void validateIdentification(){
		try {
			setExistIdentification(false);
			if(this.sedUser!=null){
				if(this.sedUser.getIdentification()!=null && !this.sedUser.getIdentification().trim().isEmpty()){
					setExistIdentification(this.controller.validateExistField(SedUser.class.getSimpleName(),"identification",this.sedUser.getIdentification()));
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
			}else if(!FieldValidator.isValidEmail(this.sedUser.getEmail().trim())){
				addWarnMessage("Crear Usuario", "El correo electr�nico ingresado no es v�lido.");
				return false;
			}			
			else if (this.sedUser.getUserName() == null || this.sedUser.getUserName().trim().isEmpty()) {
				addWarnMessage("Crear Usuario", "Por favor diligencie el nombre de usuario.");
				return false;
			} else if (this.sedUser.getIdSedRole() == null || this.sedUser.getIdSedRole().equals(0L)) {
				addWarnMessage("Crear Usuario", "Por favor seleccione el tipo de usuario.");
				return false;
			} else
				return validatePassword();
		} catch (Exception e) {
			throw e;
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
			this.sedUserList = this.controller.loadSedUserList();
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
	public boolean getValidateSedUserRole(Long idSedRole) throws Exception {
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
		this.existUserName = existUserName;
	}
	
}
