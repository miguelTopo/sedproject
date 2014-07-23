package co.edu.udistrital.core.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ManageDate {

	public static final String YYYY_MM_DD = "yyyy/MM/dd";
	public static final String HH_MM_SS = "hh:mm:ss";

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

	public static String formatDate(Date date, String format) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public static Date stringToDate(String stringDate, String format) throws Exception {
		try {
			Calendar c = Calendar.getInstance();
			if (format.equals(ManageDate.YYYY_MM_DD)) {
				// System.out.println(stringDate.substring(0, 4));
				// System.out.println(stringDate.substring(5, 7));
				// System.out.println(stringDate.substring(8, 10));
				// System.out.println(Integer.parseInt(stringDate.substring(5, 7)));

				c.set(Calendar.YEAR, Integer.parseInt(stringDate.substring(0, 4)));
				c.set(Calendar.MONTH, Integer.parseInt(stringDate.substring(5, 7)) - 1);
				c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(stringDate.substring(8, 10)));
			}
			return c.getTime();
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 17/7/2014 23:12:51 */
	public static Date parseTimeZone(Date date, TimeZone zone) throws Exception {
		try {
			Calendar c = new GregorianCalendar();
			c.setTimeZone(zone);
			c.setTime(date);
			return c.getTime();
		} catch (Exception e) {
			throw e;
		}
	}


}
