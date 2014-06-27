package co.edu.udistrital.core.login.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Transaction;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.common.util.FieldValidator;
import co.edu.udistrital.core.common.util.RandomPassword;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.SedUserDAO;

@ManagedBean
@ViewScoped
@URLMapping(id = "password", pattern = "/portal/clave", viewId = "/pages/password/password.jspx")
public class PasswordBean extends BackingBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9075256738266226834L;
	private PasswordController controller;
	private String newPass;
	private String password;
	private String newPass2;

	public PasswordBean() {
		try {
			this.controller = new PasswordController();
			getUserSession().getIdSedUser();
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean isRightNewPass() {
		try {
			if (this.newPass == null || this.newPass.trim().isEmpty()) {
				addWarnMessage("Valida",
						"Por favor ingrese la nueva contraseña");
				return false;
			} else if (this.newPass2 == null || this.newPass2.trim().isEmpty()) {
				addWarnMessage("Valida", "Por favor ingrese la ");
				return false;
			} else if (!this.newPass.equals(this.newPass2)) {
				addWarnMessage("Valida", "Por favor ingrese la ");
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void changePassword() {
		try {
			if (!isRightNewPass())
				return;
			if (this.controller.rightOldPass(getUserSession().getIdSedUser(),
					this.password)) {
				this.controller.updateSedUserPassword(getUserSession()
						.getIdSedUser(), this.newPass);
				BackingBean.addInfoMessage("Restablecer contraseña",
						"Cambio de contraseña exitoso.");

			} else {

				BackingBean.addInfoMessage("Clave Antigua Incorrecta",
						"Por favor ingrese la clave correcta.");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean getValidateSedUserRole() throws Exception {
		String md5Pass = ManageMD5.parseMD5("123456");

		return true;
	}

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPass2() {
		return newPass2;
	}

	public void setNewPass2(String newPass2) {
		this.newPass2 = newPass2;
	}
	

}
