package co.edu.udistrital.core.common.controller;

import org.hibernate.HibernateException;

import co.edu.udistrital.core.connection.HibernateSessionFactory;
import co.edu.udistrital.core.mail.io.mn.aws.SMTPEmail;



public class ErrorNotificacion {

	/**
	 * Send Mail Error
	 */
	public static void handleErrorMailNotification(Exception e, Object classObject) {
		try {
			String className =
				classObject instanceof BackingBean ? (((BackingBean) classObject).getUserSession() == null ? "external user"
					: ((BackingBean) classObject).getUserSession().getIdentification()) : (classObject instanceof String ? classObject.toString()
					: classObject.getClass().getName());
			System.out.println("HANDLE ERROR: exception for: " + className);
			e.printStackTrace();
			if (e instanceof HibernateException) {
				HibernateSessionFactory.getSession().clear();
				HibernateSessionFactory.getSession().flush();
				HibernateSessionFactory.getSession().cancelQuery();
				HibernateSessionFactory.getSession().close();
			}


			SMTPEmail smtp = new SMTPEmail();
			StringBuilder m = new StringBuilder();
			int c = 0;
			do {
				m.append("<div>");
				m.append(e.getClass().getName() + " :" + e.getMessage());
				m.append("</div>");
				for (StackTraceElement st : e.getStackTrace()) {
					m.append("<div>");
					m.append(st.toString());
					m.append("</div>");
				}
				e = e.getCause() == null ? null : (Exception) e.getCause();
				c++;
				m.append("Cause :");
			} while (e != null && c <= 5);

			smtp.sendProcessMail(null, "Error al realizar operación SED", className + " " + m.toString(), "mtorres@zyos.co", "migueltrock@gmail.com",
				"duck1girl@gmail.com");
		} catch (Exception e2) {
			e2.printStackTrace();
			System.out.println("ERROR: FAIL send error mail ");
		}
	}

}
