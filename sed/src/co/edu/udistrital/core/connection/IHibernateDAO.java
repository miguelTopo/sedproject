package co.edu.udistrital.core.connection;

import org.hibernate.Session;

public interface IHibernateDAO {

	public Session getSession() throws Exception;

}
