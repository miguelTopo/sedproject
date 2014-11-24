package co.edu.udistrital.core.login.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.api.IEmailTemplate;
import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.common.model.EmailTemplate;
import co.edu.udistrital.core.common.util.FieldValidator;
import co.edu.udistrital.core.mail.io.mn.aws.MailGeneratorFunction;
import co.edu.udistrital.core.mail.io.mn.aws.SMTPEmail;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "password", pattern = "/portal/clave", viewId = "/pages/password/password.jspx")
public class PasswordBean extends BackingBean {

	private static final long serialVersionUID = 9075256738266226834L;

	private String passwordOld;
	private String password;
	private String retryPassword;

	private PasswordController controller;

	public PasswordBean() throws Exception {
		try {
			this.controller = new PasswordController();
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 8/7/2014 21:44:26 */
	private boolean validateSuccessPassword() throws Exception {
		try {
			if (!this.password.equals(this.retryPassword)) {
				addWarnMessage(getMessage("page.login.labelPasswordUpdate"), getMessage("page.login.warnPasswordNoMatch"));
				return false;
			} else {
				String pwMd5 = ManageMD5.parseMD5(this.passwordOld);
				this.passwordOld = null;
				if (!this.controller.validateOldUserPassword(getUserSession().getIdSedUser(), pwMd5)) {
					addWarnMessage(getMessage("page.login.labelPasswordUpdate"), getMessage("page.login.warnOldPassword"));
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 8/7/2014 21:44:26 */
	private boolean validateUpdatePassword() throws Exception {
		try {
			if (this.passwordOld == null || this.passwordOld.trim().isEmpty()) {
				addWarnMessage(getMessage("page.login.labelPasswordUpdate"), getMessage("page.password.warnOldPassword"));
				return false;
			} else if (this.password == null || this.password.trim().isEmpty()) {
				addWarnMessage(getMessage("page.login.labelPasswordUpdate"), getMessage("page.password.warnNewPassword"));
				return false;
			} else if (this.retryPassword == null || this.retryPassword.trim().isEmpty()) {
				addWarnMessage(getMessage("page.login.labelPasswordUpdate"), getMessage("page.password.warnRetryPassword"));
				return false;
			} else
				return validateSuccessPassword();
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 8/7/2014 21:59:29 */
	private void sendMailPasswordUpdate(final String userName, final String userPassword, final String email) throws Exception {
		try {
			if (FieldValidator.isValidEmail(email)) {
				EmailTemplate t = MailGeneratorFunction.getEmailTemplate(IEmailTemplate.PASSWORD_UPDATE);
				SMTPEmail e = new SMTPEmail();

				e.sendProcessMail(null, t.getSubject(),
					MailGeneratorFunction.createGenericMessage(t.getBody(), t.getAnalyticsCode(), userName, userPassword), email);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 8/7/2014 21:58:05 */
	private void threadPasswordUpdate() throws Exception {
		try {
			final String passUser = this.password;
			final String userEmail = getUserSession().getEmail();
			final String userName = getUserSession().getName() + " " + getUserSession().getLastName();
			new Thread(new Runnable() {

				public void run() {
					try {
						sendMailPasswordUpdate(userName, passUser, userEmail);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 8/7/2014 21:44:26 */
	public void updatePassword() {
		try {
			if (!validateUpdatePassword())
				return;
			if (this.controller.updatePassword(getUserSession().getIdSedUser(), ManageMD5.parseMD5(this.password), getUserSession()
				.getIdentification())) {
				threadPasswordUpdate();
				clearVar();
				addInfoMessage(getMessage("page.login.labelPasswordUpdate"), getMessage("page.password.labelSuccessUpdate"));
			} else {
				addErrorMessage(getMessage("page.login.labelPasswordUpdate"), getMessage("page.password.errorUpdate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 8/7/2014 21:44:26 */
	private void clearVar() throws Exception {
		try {
			this.passwordOld = this.password = this.retryPassword = null;
			this.passwordOld = this.password = this.retryPassword = "";
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean getValidateSedUserRole() throws Exception {
		return false;
	}

	public String getPasswordOld() {
		return passwordOld;
	}

	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRetryPassword() {
		return retryPassword;
	}

	public void setRetryPassword(String retryPassword) {
		this.retryPassword = retryPassword;
	}

}
