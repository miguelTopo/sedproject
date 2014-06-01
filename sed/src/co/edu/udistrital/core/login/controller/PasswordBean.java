package co.edu.udistrital.core.login.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.controller.BackingBean;

@ManagedBean
@ViewScoped
@URLMapping(id = "password", pattern = "/portal/clave", viewId = "/pages/password/password.jspx")
public class PasswordBean extends BackingBean {

	private PasswordController controller;

	public PasswordBean() {
		try {
			this.controller = new PasswordController();
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean getValidateSedUserRole() throws Exception {

		return true;
	}


}
