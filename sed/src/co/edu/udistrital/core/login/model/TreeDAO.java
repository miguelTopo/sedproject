package co.edu.udistrital.core.login.model;

import java.util.List;

import org.hibernate.Query;

import co.edu.udistrital.core.connection.HibernateDAO;

public class TreeDAO extends HibernateDAO {

	/** @author MTorres */
	public List<Tree> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM Tree t WHERE t.state > 0 ");
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
