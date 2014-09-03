package co.edu.udistrital.core.login.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

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

	private boolean validLogin;


	// Simple Java Data Object
	private String userName;
	private String userPassword;

	// UserList
	List<Tree> treeList;
	List<User> userSessionList;

	// User
	private LoginController controller;

	public LoginBean() throws Exception {
		try {
			this.controller = new LoginController();
			validLogin = false;
			if (getUserSession() == null) {
				redirect("/portal/login");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void login(ActionEvent actionEvent) {
		try {
			this.treeList = null;
			if (!validateLoginData())
				return;

			String idSession = getSession(false).getId();
			setUserSession(this.controller.validateSedUser(this.userName, this.userPassword));


			if (getUserSession() != null) {
				addUserCookieList();
				this.validLogin = true;
				
				this.treeList = loadTreeListByRole(getUserSession().getIdSedRole());
				getUserSession().setIdSession(idSession);

				this.userName = null;
				this.userPassword = null;

				addInfoMessage("Bienvenid@", this.userName);
			} else {
				this.validLogin = false;
				addWarnMessage("Iniciar Sesión", "Las Credenciales usadas son inválidas.");
			}
			getSession(false).setAttribute("user", getUserSession());
			getRequestContext().addCallbackParam("isLogin", this.validLogin);
			if (validLogin)
				getRequestContext().addCallbackParam("view", "menu");



		} catch (Exception e) {
			e.printStackTrace();
			setUserSession(null);
		}
	}

	public void sedUserLogout() {
		try {
			this.treeList = null;
			getSession(false).invalidate();
			ManageCookie.removeCookieByName("uID");
			ManageCookie.removeCookieByName("locate");
			this.validLogin = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	private void addUserCookieList() throws Exception {
		try {
			ManageCookie.addCookie("uID", getUserSession().getId().toString(), 30, "user", true);
			ManageCookie.addCookie("locate", "es_co", 30, "user", true);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	private boolean validateLoginData() throws Exception {
		try {
			if (this.userName == null || this.userName.trim().isEmpty()) {
				addWarnMessage("Ingresar", "Por favor ingrese el nombre de usuario.");
				return false;
			} else if (this.userPassword == null || this.userPassword.trim().isEmpty()) {
				addWarnMessage("Ingresar", "Por favor ingrese la contrase�a.");
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

	public List<User> getUserSessionList() {
		return userSessionList;
	}


	public void setUserSessionList(List<User> userSessionList) {
		this.userSessionList = userSessionList;
	}


	public boolean isValidLogin() {
		return validLogin;
	}


	public void setValidLogin(boolean validLogin) {
		this.validLogin = validLogin;
	}


}
