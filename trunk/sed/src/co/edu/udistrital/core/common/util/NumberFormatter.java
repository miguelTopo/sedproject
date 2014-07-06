package co.edu.udistrital.core.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberFormatter {

	public static final String FORMAT_0 = "#0";

	/** @author MTorres 
	 * @throws Exception */
	public static String parseDoubleToString(Double number, String format) throws Exception {
		try {
			NumberFormat formatter = new DecimalFormat(format);
			return formatter.format(number);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
