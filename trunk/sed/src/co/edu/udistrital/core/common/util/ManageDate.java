package co.edu.udistrital.core.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ManageDate {

	public static final String YYYY_MM_DD = "yyyy/MM/dd";

	public ManageDate() {

	}

	public static String getCurrentDate(String format) {
		try {
			Date currentDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getTimeZone("EST"));
			return sdf.format(currentDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
