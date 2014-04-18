package co.edu.udistrital.core.common.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ManageMD5 {

	/**@author MTorres*/
	public static String parseMD5(String inputString) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(inputString.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashText = number.toString(16);

			while (hashText.length() < 32) {
				hashText = "0" + hashText;
			}
			return hashText;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
}
