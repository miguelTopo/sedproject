package co.edu.udistrital.core.login.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.api.IEmailTemplate;
import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.controller.ErrorNotificacion;
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

	// Primitives
	private boolean exitUserEmail = true, invalidEmail = false;

	// Java Object
	private String userEmail;

	// Controller Object
	private PasswordRecoverController controller;

	public PasswordRecoverBean() {
		try {
			this.controller = new PasswordRecoverController();
		} catch (Exception e) {
			ErrorNotificacion.handleErrorMailNotification(e, this);
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
			ErrorNotificacion.handleErrorMailNotification(e, this);
		}

	}

	public void recoverPassword() {
		try {
			if (isExitUserEmail() && !isInvalidEmail()) {
				SedUser user = this.controller.loadSedUser(this.userEmail);
				if (user != null) {
					String password = RandomPassword.getPassword(7);
					if (this.controller.updateSedUserPassword(user.getId(), password)) {
						threadEmailRecoverPassword(user, password);
						cleanVar();
						BackingBean.addInfoMessage(BackingBean.getMessage("page.password.resetPassword"),
							BackingBean.getMessage("page.password.labelSuccessResetPassword"));
					}


				} else {
					BackingBean.addWarnMessage(BackingBean.getMessage("page.password.resetPassword"),
						BackingBean.getMessage("page.password.warnUserNoFound"));
					return;
				}
			}
		} catch (Exception e) {
			ErrorNotificacion.handleErrorMailNotification(e, this);
		}
	}

	private void sendEmailRecoverPassword(final SedUser sedUser, final String password, final String userMail) throws Exception {
		try {
			EmailTemplate t = MailGeneratorFunction.getEmailTemplate(IEmailTemplate.PASSWORD_RECOVER);
			SMTPEmail e = new SMTPEmail();
			e.sendProcessMail(null, t.getSubject(), MailGeneratorFunction.createGenericMessage(t.getBody(), t.getAnalyticsCode(), sedUser.getName()
				+ " " + sedUser.getLastName(), sedUser.getIdentification(), password), userMail);
		} catch (Exception e) {
			throw e;
		}

	}

	/** @author MTorres 19/11/2014 17:20:49 */
	private void threadEmailRecoverPassword(final SedUser su, final String pw) throws Exception {
		try {
			final String userMail = this.userEmail.trim().toLowerCase();
			final SedUser sedUser = su;
			final String password = pw;

			new Thread(new Runnable() {

				public void run() {
					try {
						sendEmailRecoverPassword(sedUser, password, userMail);
					} catch (Exception e) {
						ErrorNotificacion.handleErrorMailNotification(e, this);
					}
				}
			}).start();

		} catch (Exception e) {
			throw e;
		}
	}

	private void cleanVar() throws Exception {
		try {
			this.userEmail = null;
			setExitUserEmail(true);
			setInvalidEmail(false);
		} catch (Exception e) {
			throw e;
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
