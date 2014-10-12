package co.edu.udistrital.core.login.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.controller.ManageCookie;
import co.edu.udistrital.core.login.model.Tree;
import co.edu.udistrital.session.common.User;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

@ManagedBean
@SessionScoped
@URLMappings(mappings = {@URLMapping(id = "login", pattern = "/portal/login", viewId = "/pages/login/sedLogin.jspx"),
	@URLMapping(id = "menu", pattern = "/portal/menu", viewId = "/pages/sed.jspx")})
public class LoginBean extends BackingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7559214049695375492L;

	// Simple Java Data Object
	private String userName;
	private String imageEffect;
	private String userPassword;

	// UserList
	List<Tree> treeList;
	List<User> userSessionList;
	List<String> imageList;

	// User
	private LoginController controller;

	public LoginBean() throws Exception {
		try {
			this.controller = new LoginController();
			this.validLogin = false;
			this.imageList = new ArrayList<String>() {
				{
					add("basketball.jpg");
					add("building.jpg");
					add("home.jpg");
					add("basketball2.jpg");
					add("church.jpg");
					add("court.jpg");
					add("sculpture.jpg");
					add("wall.jpg");
					add("wallGirl.png");
				}
			};
			loadEffectSwitch();
			if (getUserSession() == null) {
				redirect("/portal/login");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** @author MTorres 11/10/2014 16:43:12 */
	private void loadEffectSwitch() throws Exception {
		try {
			Random random = new Random();
			int i = random.nextInt(4 - 1 + 1);
			switch (i) {
				case 0:
					this.imageEffect = "turnDown";
				break;
				case 1:
					this.imageEffect = "wipe";
				break;
				case 2:
					this.imageEffect = "fade";
				break;
				case 3:
					this.imageEffect = "zoom";
				break;

				default:
					this.imageEffect = "fade";
				break;
			}
		} catch (Exception e) {
			throw e;
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
				System.out.println("INGRESO CON USUARIO " + getUserSession().getName() + " ROL " + getUserSession().getIdSedRole());
				addUserCookieList();
				this.validLogin = true;

				this.treeList = loadTreeListByRole(getUserSession().getIdSedRole());
				getUserSession().setIdSession(idSession);

				this.userName = null;
				this.userPassword = null;

				addInfoMessage("Bienvenid@", getUserSession().getName() + " " + getUserSession().getLastName());
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

	/** @author MTorres 2/9/2014 20:21:48 */
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

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public String getImageEffect() {
		return imageEffect;
	}

	public void setImageEffect(String imageEffect) {
		this.imageEffect = imageEffect;
	}


}
