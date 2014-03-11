package co.edu.udistrital.core.connection;

import org.hibernate.Session;

public class HibernateDAO implements IHibernateDAO{

	public Session getSession() throws Exception {
		return HibernateSessionFactory.getSession();
	}

}
