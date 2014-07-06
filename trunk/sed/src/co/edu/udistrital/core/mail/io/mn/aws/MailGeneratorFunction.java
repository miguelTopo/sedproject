package co.edu.udistrital.core.mail.io.mn.aws;

import co.edu.udistrital.core.common.list.BeanList;
import co.edu.udistrital.core.common.model.EmailTemplate;

public class MailGeneratorFunction {

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public static EmailTemplate getEmailTemplate(Long idEmailTemplate)
			throws Exception {
		try {
			for (EmailTemplate et : BeanList.getEmailTemplateList()) {
				if (et.getId().equals(idEmailTemplate))
					return et;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static String createGenericMessage(String body,
			String googleAnalytics, Object... params) throws Exception {
		try {
			String b = body;
			b = addGoogleAnalytics(googleAnalytics, b);
			b = replaceParameterWithFunction(b, params);

			// b = replaceMessageWithParameters(b, params);
			return b;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String replaceParameterWithFunction(String b,
			Object... params) throws Exception {
		try {
			int i = 0;
			for (Object object : params) {
				if (object != null && !object.toString().isEmpty())
					b = replaceMessage(b, "{" + i + "}", object.toString());
				i++;
			}
			return b;
		} catch (Exception e) {
			throw e;
		}

	}

	public static String addGoogleAnalytics(String googleAnalytics, String b)
			throws Exception {
		try {
			if (googleAnalytics != null && !googleAnalytics.isEmpty())
				b = replaceMessage(b, "<body>",
						getAnalyticsHTMLCode(googleAnalytics));
			return b;
		} catch (Exception e) {
			throw e;
		}
	}

	private static String getAnalyticsHTMLCode(String analyticsCode) {
		// TODO implement
		return "";
	}

	public static String replaceMessage(String message, String token,
			String value) throws Exception {
		try {
			StringBuilder sb = new StringBuilder();
			int index = message.indexOf(token);
			if (index < 0) {
				return message;
			} else {
				sb.append(message.substring(0, index));
				sb.append(value);
				sb.append(message.substring(token.length() + index));
				return replaceMessage(sb.toString(), token, value);
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
