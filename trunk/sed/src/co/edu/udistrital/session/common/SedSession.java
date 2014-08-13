package co.edu.udistrital.session.common;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSInput;

public class SedSession {

	private static List<User> listUserSession;

	public SedSession() {

	}

	public static synchronized void addLoginUser(User user) throws Exception {
		try {
			if (user != null)
				getListUserSession().add(user);
		} catch (Exception e) {
			throw e;
		}

	}

	public static synchronized List<User> getLoginUser(String idUser) throws Exception {
		try {
			List<User> loginList = new ArrayList<User>();
			if (listUserSession != null) {
				for (User u : listUserSession) {
					if (u.getId().toString().equals(idUser)) {
						loginList.add(u);
					}
				}

				return (loginList != null && !loginList.isEmpty()) ? loginList : null;
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	public static synchronized User getUserBySession(String idSession) throws Exception {
		try {
			if (idSession != null && !idSession.trim().isEmpty()) {
				if (listUserSession == null)
					return null;
				for (User u : listUserSession) {
					if (u.getIdSession().equals(idSession))
						return u;
				}
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	public static void deleteLoginSessionId(String idSession, String idUser) throws Exception {
		try {
			int index = -1;
			if ((idSession != null || idUser != null) && listUserSession != null) {
				for (int i = 0; i < listUserSession.size(); i++) {
					User u = listUserSession.get(i);
					if (u.getId().toString().equals(idUser) || u.getIdSession().equals(idSession)) {
						index = i;
						break;
					}
				}
			}

			if (index != -1 && index > -1 && listUserSession != null)
				listUserSession.remove(index);

		} catch (Exception e) {
			throw e;
		}
	}

	public static List<User> getListUserSession() {
		if (listUserSession != null)
			listUserSession = new ArrayList<User>();
		return listUserSession;
	}

	public static void setListUserSession(List<User> listUserSession) {
		SedSession.listUserSession = listUserSession;
	}
}
