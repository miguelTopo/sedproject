package co.edu.udistrital.core.login.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.api.IEmailTemplate;
import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.model.EmailTemplate;
import co.edu.udistrital.core.common.util.FieldValidator;
import co.edu.udistrital.core.common.util.RandomPassword;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.core.mail.io.mn.aws.MailGeneratorFunction;
import co.edu.udistrital.core.mail.io.mn.aws.SMTPEmail;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "passrecover", pattern = "/portal/recuperar", viewId = "/pages/forgetPassword.jspx")
public class PasswordRecoverBean implements Serializable {

	// Primitices
	private boolean exitUserEmail = true, invalidEmail = false;

	// Java Object
	private String userEmail;

	// Controller Object
	private PasswordRecoverController controller;

	public PasswordRecoverBean() {
		try {
			this.controller = new PasswordRecoverController();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void validEmail() {
		try {
			setExitUserEmail(true);
			setInvalidEmail(false);
			if (this.userEmail != null && !this.userEmail.trim().isEmpty()) {
				if (FieldValidator.isValidEmail(this.userEmail.trim()))
					setExitUserEmail(this.controller.validateExistField(SedUser.class.getSimpleName(), "email", this.userEmail));
				else
					setInvalidEmail(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void recoverPassword() {
		try {
			if (isExitUserEmail() && !isInvalidEmail()) {
				SedUser user = this.controller.loadSedUser(this.userEmail);
				if (user != null) {
					String password = RandomPassword.getPassword(7);
					if (this.controller.updateSedUserPassword(user.getId(), password)) {
						sendEmailRecoverPassword(user, password);
						cleanVar();
						BackingBean.addInfoMessage("Restablecer contraseña",
							"Se ha enviado un correo electrónico al correo con los datos necesarios para que ingreses nuevamente.");
					}


				} else {
					BackingBean.addWarnMessage("Recuperar contraseña",
						"Su usuario no se encuentra activo en el sistema. Por favor comuníquese con el administrador del sistema.");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendEmailRecoverPassword(SedUser sedUser, String password) {
		try {
			EmailTemplate t = MailGeneratorFunction.getEmailTemplate(IEmailTemplate.PASSWORD_RECOVER);
			SMTPEmail e = new SMTPEmail();
			e.sendProcessMail(null, t.getSubject(), MailGeneratorFunction.createGenericMessage(t.getBody(), t.getAnalyticsCode(), sedUser.getName()
				+ " " + sedUser.getLastName(), sedUser.getUserName(), password), this.userEmail.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void cleanVar() {
		try {
			this.userEmail = null;
			setExitUserEmail(true);
			setInvalidEmail(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public boolean isExitUserEmail() {
		return exitUserEmail;
	}

	public void setExitUserEmail(boolean exitUserEmail) {
		this.exitUserEmail = exitUserEmail;
	}

	public boolean isInvalidEmail() {
		return invalidEmail;
	}

	public void setInvalidEmail(boolean invalidEmail) {
		this.invalidEmail = invalidEmail;
	}



}
