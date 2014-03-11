package co.edu.udistrital.core.connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	private static ServiceRegistry serviceRegistry;

	public static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new AnnotationConfiguration();
			configuration.configure();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			return configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Inicio de sesión falló por error: " + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session getSession() throws Exception {
		try {
			return sessionFactory.openSession();
		} catch (Exception e) {
			throw e;
		}
	}

	public static void close() {
		try {
			getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
