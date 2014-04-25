package co.edu.udistrital.core.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldValidator {


	/** @author MTorres */
	public static boolean isNumeric(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/** @author MTorres */
	public static boolean isValidEmail(String email) {
		try {
			Pattern p = Pattern.compile("[-\\w\\.]+@[\\.\\w]+\\.\\w+");
			Matcher m = p.matcher(email);
			return m.matches();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
