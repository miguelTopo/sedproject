package co.edu.udistrital.core.mail.io.mn.aws;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import co.edu.udistrital.core.common.list.BeanList;
import co.edu.udistrital.core.common.util.FieldValidator;

public class SMTPEmail {

	private static final String SMTP_HOST_NAME = BeanList.getProperties().getProperty("mail.smtp.host");
	private static final String SMTP_AUTH_USER = BeanList.getProperties().getProperty("mail.smtp.user");
	private static final String SMTP_AUTH_PWD = BeanList.getProperties().getProperty("mail.password");

	private Session mailSession;

	public SMTPEmail() {
		try {
			System.out.println("ingresando al metodo....");
			Properties properties = new Properties();
			properties.put("mail.transport.protocol", BeanList.getProperties().getProperty("mail.transport.protocol"));
			properties.put("mail.smtp.host", SMTP_HOST_NAME);
			properties.put("mail.smtp.port", BeanList.getProperties().getProperty("mail.smtp.port"));
			properties.put("mail.smtp.auth", BeanList.getProperties().getProperty("mail.smtp.auth"));
			properties.put("mail.smtp.starttls.enable", BeanList.getProperties().getProperty("mail.smtp.starttls.enable"));

			Authenticator auth = new SMTPAuthenticator();

			this.mailSession = Session.getDefaultInstance(properties, auth);

		} catch (Exception e) {
			throw e;
		}
	}

	private class SMTPAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}

	public void sendProcessMail(String from, String subject, String HTMLBody, String... to) {
		try {
			if (from == null)
				from = SMTP_AUTH_USER;
			if (to != null && from != null && subject != null) {
				Transport transport = mailSession.getTransport();
				MimeMessage message = new MimeMessage(mailSession);
				Multipart multipart = new MimeMultipart("alternative");

				BodyPart bodyPart = new MimeBodyPart();
				bodyPart.setContent(HTMLBody, "text/html");

				multipart.addBodyPart(bodyPart);

				message.setContent(multipart);
				message.setFrom(new InternetAddress(from));
				message.setSubject(subject);

				for (String s : to) {
					if (s != null && !s.isEmpty()) {
						if (FieldValidator.isValidEmail(s))
							message.addRecipient(Message.RecipientType.BCC, new InternetAddress(s));
					}
				}

				transport.connect();
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			} else
				System.out.println("SMTPEmail email list is empty");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
