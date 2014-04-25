package co.edu.udistrital.core.login.model;

import java.util.List;

import org.hibernate.Query;

import co.edu.udistrital.core.connection.HibernateDAO;

public class SedRoleDAO extends HibernateDAO{

	/**@author MTorres*/
	public List<SedRole> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM SedRole sr WHERE sr.state > 0 ");
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
