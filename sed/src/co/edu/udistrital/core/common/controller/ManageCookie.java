package co.edu.udistrital.core.common.controller;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ManageCookie {
	
	public ManageCookie() {
	
	}

	public static void addCookie(String name, String value, int expire, String comment, boolean secure) {
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

	public static String getCookieByName(String cookieName) {
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
