package co.edu.udistrital.core.common.util;

public class RandomPassword {

	public static final String NUMBERS = "0123456789";
	public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LOWER = "abcdefghijklmnopqrstuvwxyz";

	public static String getPassword(int passwordLength) {
		try {
			return getPassword(NUMBERS + UPPER + LOWER, passwordLength);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private static String getPassword(String key, int length) {
		try {
			StringBuilder pswd = new StringBuilder();
			for (int i = 0; i < length; i++) {
				pswd.append(key.charAt((int) (Math.random() * key.length())));
			}
			return pswd.toString();
		} catch (Exception e) {
			throw e;
		}

	}
}
