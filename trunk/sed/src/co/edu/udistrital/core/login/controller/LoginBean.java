package co.edu.udistrital.core.login.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.controller.ManageCookie;
import co.edu.udistrital.core.login.model.Tree;
import co.edu.udistrital.session.common.SedSession;
import co.edu.udistrital.session.common.User;

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
	private boolean uniqueEnter;

	// Simple Java Data Object
	private String userName;
	private String userPassword;

	// UserList
	List<Tree> treeList;
	List<User> userSessionList;

	// User
	private LoginController controller;

	public LoginBean() {
		try {
			this.controller = new LoginController();
			if (getUserSession() == null) {
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

			if (getUserSession() == null) {
				String sessionId = getSession(false).getId();
				setUserSession(this.controller.validateSedUser(this.userName, this.userPassword));
				User u = getUserSession();
				System.out.println(u.getEmail());

				if (getUserSession() != null) { 
					addUserCookieList();
					this.treeList = null;
					this.treeList = loadTreeListByRole(getUserSession().getIdSedRoleUser());

					setUniqueEnter(true);
					getUserSession().setIdSession(sessionId);
					userSessionList = SedSession.getLoginUser(getUserSession().getId().toString());

					redirect("/portal/menu");
				} else {
					addWarnMessage("Ingresar", "Los datos de usuario y contraseña no coinciden, por favor verifique e intente nuevamente.");
					return;
				}
			}else 
				redirect("/portal/menu");
			
				

		} catch (Exception e) {
			e.printStackTrace();
			setUserSession(null);
		}finally{
			setUserSession(null);
		}
	}

	private void addUserCookieList() {
		try {
			ManageCookie.addCookie("uID", getUserSession().getId().toString(), 30, "user", true);
			ManageCookie.addCookie("locate", "es_co", 30, "user", true);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
	public boolean getValidateSedUserRole() throws Exception {
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

	public boolean isUniqueEnter() {
		return uniqueEnter;
	}


	public void setUniqueEnter(boolean uniqueEnter) {
		this.uniqueEnter = uniqueEnter;
	}


	public List<User> getUserSessionList() {
		return userSessionList;
	}


	public void setUserSessionList(List<User> userSessionList) {
		this.userSessionList = userSessionList;
	}

}
