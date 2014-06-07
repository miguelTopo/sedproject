package co.edu.udistrital.sed.model;

import java.util.List;

import org.hibernate.Query;

import co.edu.udistrital.core.connection.HibernateDAO;

public class QualificationTypeDAO extends HibernateDAO {

	/**@author MTorres 7/06/2014*/
	public List<QualificationType> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM QualificationType qt WHERE qt.state > 0  ");
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
