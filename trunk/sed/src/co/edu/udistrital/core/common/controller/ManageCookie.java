package co.edu.udistrital.core.common.controller;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ManageCookie {

	public ManageCookie() {

	}

	public static void addCookie(String name, String value, int expire, String comment, boolean secure) throws Exception {
		try {
			Cookie cookie = null;
			if (cookie == null) {
				cookie = new Cookie(name, value);
				cookie.setMaxAge(expire * 60 * 60 * 24);

				((HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext()).getResponse()).addCookie(cookie);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static boolean removeCookieByName(String cookieName) throws Exception {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			Cookie cookieList[] = request.getCookies();
			if (cookieList != null) {
				for (Cookie c : cookieList) {
					if (c.getName().equals(cookieName)) {
						c.setMaxAge(0);
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getCookieByName(String cookieName) throws Exception {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			Cookie cookieList[] = request.getCookies();
			if (cookieList != null) {
				for (Cookie c : cookieList) {
					if (c.getName().equals(cookieName))
						return c.getValue();
				}
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

}
