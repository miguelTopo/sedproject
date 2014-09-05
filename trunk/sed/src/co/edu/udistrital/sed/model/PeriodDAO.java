package co.edu.udistrital.sed.model;

import java.util.List;

import org.hibernate.Query;

import co.edu.udistrital.core.connection.HibernateDAO;

public class PeriodDAO extends HibernateDAO {

	/** @author MTorres 10/08/2014 4:55:22 p. m. */
	public List<Period> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM Period p WHERE p.state > 0 ORDER BY p.id DESC ");
			qo = getSession().createQuery(hql.toString());
			return qo.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

}
