package co.edu.udistrital.core.common.util.resource;

import java.util.MissingResourceException;
import java.util.Properties;

public class ManageProperties {

	private Properties bundle;
	private Properties properties;

	public ManageProperties() {
		try {
			getResourseBundle();
			getResourseProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** @author MTorres */
	private void getResourseBundle() throws Exception {
		try {
			if (this.bundle == null) {
				this.bundle = new Properties();
				this.bundle.load(ManageProperties.class.getResourceAsStream("webMessages.properties"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	private void getResourseProperties() throws Exception {
		try {
			if (this.properties == null) {
				this.properties = new Properties();
				this.properties.load(ManageProperties.class.getResourceAsStream("properties.properties"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	public String getWebMessage(String key) {
		String message = null;
		try {
			if (key != null && !key.trim().isEmpty()) {
				message = bundle.getProperty(key);
				if (message == null) {
					message = "Could not find resource: " + key + "...";
				}

			} else
				message = "key bundle is Empty";
			return message;
		} catch (MissingResourceException e) {
			System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	public String getProperty(String key) {
		try {
			String message = null;
			if (key != null && !key.trim().isEmpty()) {
				message = properties.getProperty(key);
				if (message == null) {
					message = "Could not find resource: " + key + "...";
				}
			} else {
				message = "key property is Empty";
			}
			return message;
		} catch (MissingResourceException e) {
			System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
			e.printStackTrace();
			throw e;
		}
	}

}
