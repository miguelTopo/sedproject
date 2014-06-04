package co.edu.udistrital.sed.model;

import java.util.List;

import org.hibernate.Query;

import co.edu.udistrital.core.connection.HibernateDAO;

public class QualificationDAO extends HibernateDAO{

	public List<Qualification> loadStudentQualificationList(Long idSedUser, Long idSedRole) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append("");

			qo = getSession().createQuery(hql.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}
	
	

}
