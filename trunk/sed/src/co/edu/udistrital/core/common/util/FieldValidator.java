package co.edu.udistrital.core.common.util;

public class FieldValidator {

	public static boolean isNumeric(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
