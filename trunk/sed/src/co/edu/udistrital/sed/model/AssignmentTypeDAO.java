/**
 * @author Miguel 17/11/2014 10:14:30
 */
package co.edu.udistrital.sed.model;

import java.util.List;

import org.hibernate.Query;

import co.edu.udistrital.core.connection.HibernateDAO;

/**
 * AssignmentTypeDAO.java
 * 
 * @author Miguel 17/11/2014 10:14:30
 */
/**
 * @author Miguel 17/11/2014 10:14:30
 * 
 */
public class AssignmentTypeDAO extends HibernateDAO {

	/**
	 * @author Miguel 17/11/2014 10:15:58
	 */
	public List<AssignmentType> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM AssignmentType a WHERE a.state > 0 ORDER BY a.name ");
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
