package co.edu.udistrital.core.login.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.login.model.Tree;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

@ManagedBean
@SessionScoped
@URLMappings(mappings = {@URLMapping(id = "login", pattern = "/portal/login", viewId = "/pages/sedLogin.jspx"),
	@URLMapping(id = "menu", pattern = "/portal/menu", viewId = "/pages/sed.jspx")})
public class LoginBean extends BackingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7559214049695375492L;

	// Primitives

	// Simple Java Data Object
	private String userName;
	private String userPassword;

	// UserList
	List<Tree> treeList;

	// User
	private LoginController controller;

	public LoginBean() {
		try {
			this.controller = new LoginController();
			if (getSedSession() == null) {
				redirect("/portal/login");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/** @author MTorres */
	public void validateSedUser() {
		try {
			if (!validateLoginData())
				return;
			else {
				setSedSession(this.controller.validateSedUser(this.userName, this.userPassword));
				if (getSedSession() != null) {
					this.treeList = loadTreeListByRole(getSedSession().getIdSedRoleUser());
					redirect("/portal/menu");
				} else {
					addWarnMessage("Ingresar", "Los datos de usuario y contraseña no coinciden, por favor verifique e intente nuevamente.");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
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

	/** @author MTorres */
	public boolean getValidateSedUserRole(Long idSedRole) throws Exception {
		try {
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

	public List<Tree> getTreeList() {
		return treeList;
	}


	public void setTreeList(List<Tree> treeList) {
		this.treeList = treeList;
	}

}
