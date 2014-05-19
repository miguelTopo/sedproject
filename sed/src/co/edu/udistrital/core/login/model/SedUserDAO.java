package co.edu.udistrital.core.login.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.postgresql.util.MD5Digest;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.connection.HibernateDAO;
import co.edu.udistrital.session.common.User;

public class SedUserDAO extends HibernateDAO {

	/** @author MTorres */
	public User validateSedUser(String userName, String password) throws Exception {
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
				hql.append(" su.id AS idSedUser, ");
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

				qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(User.class));
				qo.setParameter("idSedUser", idSedUser);
				qo.setParameter("state", IState.ACTIVE);
				qo.setMaxResults(1);

				return (User) qo.uniqueResult();

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

	/** @author MTorres */
	public List<SedUser> loadSedUserList() throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" ");
			hql.append(" SELECT su.id AS id, ");
			hql.append(" su.idIdentificationType AS idIdentificationType, ");
			hql.append(" su.name AS name, ");
			hql.append(" su.lastName AS lastName, ");
			hql.append(" su.identification AS identification, ");
			hql.append(" su.email AS email, ");
			hql.append(" it.name AS nameIdentificationType ");
			hql.append(" FROM SedUser su, ");
			hql.append(" IdentificationType it ");
			hql.append(" WHERE su.idIdentificationType = it.id ");
			hql.append(" AND su.state = :state ");
			hql.append(" AND it.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(SedUser.class));

			qo.setParameter("state", IState.ACTIVE);

			return qo.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	public boolean validateExistField(String className, String field, String fieldCompare) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT COUNT(c.id) ");
			hql.append(" FROM ");
			hql.append(className);
			hql.append(" c ");
			hql.append(" WHERE c.");
			hql.append(field);
			hql.append(" = :fieldCompare ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("fieldCompare", fieldCompare);
			qo.setMaxResults(1);
			Object o = qo.uniqueResult();

			return (o != null) ? Integer.parseInt(o.toString()) > 0 : true;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}

	}

	/** @author MTorres */
	public SedUser loadSedUser(String email) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT su.id AS id, ");
			hql.append(" su.name AS name, ");
			hql.append(" su.lastName AS lastName, ");
			hql.append(" sul.userName AS userName ");
			hql.append(" FROM SedUser su, ");
			hql.append(" SedUserLogin sul ");
			hql.append(" WHERE sul.idSedUser = su.id ");
			hql.append(" AND su.email = :sedUserEmail ");
			hql.append(" AND su.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(SedUser.class));
			qo.setParameter("sedUserEmail", email);
			qo.setParameter("state", IState.ACTIVE);
			qo.setMaxResults(1);

			return (SedUser) qo.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}

	}

	public boolean updateSedUserPassword(Long idSedUser, String password) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" UPDATE SedUserLogin sul ");
			hql.append(" SET sul.md5Password = :md5Password ");
			hql.append(" WHERE sul.idSedUser = :idSedUser ");
			hql.append(" AND sul.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("md5Password", ManageMD5.parseMD5(password));
			qo.setParameter("idSedUser", idSedUser);
			qo.setParameter("state", IState.ACTIVE);

			return qo.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}
}
