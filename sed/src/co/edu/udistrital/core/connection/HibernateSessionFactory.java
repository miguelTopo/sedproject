package co.edu.udistrital.core.connection;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateSessionFactory {

	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private static SessionFactory sessionFactory;
	private static Configuration configuration = new AnnotationConfiguration();
	private static ServiceRegistry serviceRegistry;

	public HibernateSessionFactory() {

	}

	static {
		try {
			String fileConfiguration="/hibernate.cfg.xml";
			configuration.configure(fileConfiguration);
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Exception e) {
			System.err.println("Error to create SessionFactory");
			e.printStackTrace();
		}
	}

	public static Session getSession() throws HibernateException {
		try {
			Session session = (Session) threadLocal.get();
			if (session == null || !session.isOpen()) {
				if (sessionFactory == null) {
					rebuildSessionFactory();
				}
				session = (sessionFactory != null) ? sessionFactory
						.openSession() : null;
				threadLocal.set(session);
			}

			return session;
		} catch (HibernateException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void rebuildSessionFactory() {
		try {
			configuration.configure();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Exception e) {
			System.err
					.println("Error to create SessionFactory in RebuildSessionFactory");
			e.printStackTrace();
		}
	}

	public static void closeSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		threadLocal.set(null);

		if (session != null) {
			session.close();
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

}
