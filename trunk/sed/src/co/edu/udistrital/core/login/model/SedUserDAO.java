package co.edu.udistrital.core.login.model;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.connection.HibernateDAO;
import co.edu.udistrital.session.common.SedSession;

public class SedUserDAO extends HibernateDAO {

	/** @author MTorres */
	public SedSession validateSedUser(String userName, String password) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT sul.idSedUser ");
			hql.append(" FROM SedUserLogin sul ");
			hql.append(" WHERE sul.userName = :userName ");
			hql.append(" AND sul.md5Password = :md5Password ");
			hql.append(" AND sul.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("userName", userName);
			qo.setParameter("md5Password", ManageMD5.parseMD5(password));
			qo.setParameter("state", IState.ACTIVE);
			qo.setMaxResults(1);

			Long idSedUser = (Long) qo.uniqueResult();

			if (idSedUser != null) {
				hql = new StringBuilder();
				qo = null;

				hql.append(" SELECT su.id AS id, ");
				hql.append(" su.idIdentificationType AS idIdentificationType, ");
				hql.append(" su.name AS name, ");
				hql.append(" su.lastName AS lastName, ");
				hql.append(" su.identification AS identification, ");
				hql.append(" su.email AS email, ");
				hql.append(" sru.id AS idSedRoleUser ");
				hql.append(" FROM SedUser su, SedRoleUser sru ");
				hql.append(" WHERE sru.idSedUser = su.id ");
				hql.append(" AND su.id = :idSedUser ");
				hql.append(" AND su.state = :state ");
				hql.append(" AND sru.state = :state ");

				qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(SedSession.class));
				qo.setParameter("idSedUser", idSedUser);
				qo.setParameter("state", IState.ACTIVE);
				qo.setMaxResults(1);

				return (SedSession) qo.uniqueResult();

			} else
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
