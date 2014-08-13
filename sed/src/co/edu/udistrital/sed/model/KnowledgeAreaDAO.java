package co.edu.udistrital.sed.model;

import java.util.List;

import org.hibernate.Query;

import co.edu.udistrital.core.connection.HibernateDAO;

public class KnowledgeAreaDAO extends HibernateDAO {

	/** @author MTorres 10/08/2014 4:55:22 p. m. */
	public List<KnowledgeArea> findAll() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" FROM KnowledgeArea ka WHERE ka.state > 0 ORDER BY ka.id ");
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
