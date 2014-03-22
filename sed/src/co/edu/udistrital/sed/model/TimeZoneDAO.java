package co.edu.udistrital.sed.model;

import java.util.List;
import org.hibernate.Query;

import co.edu.udistrital.core.connection.HibernateDAO;

public class TimeZoneDAO extends HibernateDAO {

	public List<TimeZone> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM TimeZone tz WHERE tz.state > 0 ");
			qo = getSession().createQuery(hql.toString());
			return qo.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
