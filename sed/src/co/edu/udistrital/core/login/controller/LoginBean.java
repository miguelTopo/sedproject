package co.edu.udistrital.core.login.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import co.edu.udistrital.core.common.controller.BackingBean;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@SessionScoped
@URLMapping(id = "login", pattern = "/portal/login", viewId = "/pages/sedLogin.jspx")
public class LoginBean extends BackingBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7559214049695375492L;
	
	
	private String userName;
	private String userPassword;


	public void validateSedUser() {
		try {
			if (!validateLoginData())
				return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean validateLoginData() {
		try {
			if (this.userName == null || this.userName.trim().isEmpty()) {
				addWarnMessage("Ingresar", "Por favor ingrese el nombre de usuario.");
				return false;
			} else if (this.userPassword == null || this.userPassword.trim().isEmpty()) {
				addWarnMessage("Ingresar", "Por favor ingrese la contraseña.");
				return false;
			} else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// ////////----------getters and setter ----------//////////
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}



}
