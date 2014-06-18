package co.edu.udistrital.core.login.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.connection.HibernateDAO;
import co.edu.udistrital.core.login.api.ISedRole;
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
				hql.append(" sru.id AS idSedRoleUser, ");
				hql.append(" sr.id AS idSedRole, ");
				hql.append(" sr.name AS sedRoleName ");
				hql.append(" FROM SedUser su, SedRoleUser sru, SedRole sr ");
				hql.append(" WHERE sru.idSedUser = su.id ");
				hql.append(" AND sr.id = sru.idSedRole ");
				hql.append(" AND su.id = :idSedUser ");
				hql.append(" AND su.state = :state ");
				hql.append(" AND sru.state = :state ");

				qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(User.class));
				qo.setParameter("idSedUser", idSedUser);
				qo.setParameter("state", IState.ACTIVE);
				qo.setMaxResults(1);


				User u = (User) qo.uniqueResult();
				if (u.getIdSedRole().equals(ISedRole.STUDENT)) {
					hql = null;
					qo = null;
					hql = new StringBuilder();
					hql.append(" SELECT s.id ");
					hql.append(" FROM Student s ");
					hql.append(" WHERE s.idSedUser = :idSedUser ");
					hql.append(" AND s.state = :state ");

					qo = getSession().createQuery(hql.toString());
					qo.setParameter("state", IState.ACTIVE);
					qo.setParameter("idSedUser", u.getIdSedUser());
					qo.setMaxResults(1);

					u.setIdStudent((Long) qo.uniqueResult());

				}
				return u;

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
			hql.append(" SELECT su.id AS id, ");
			hql.append(" su.idIdentificationType AS idIdentificationType, ");
			hql.append(" su.name AS name, ");
			hql.append(" su.lastName AS lastName, ");
			hql.append(" su.identification AS identification, ");
			hql.append(" su.email AS email, ");
			hql.append(" it.id AS idIdentificationType, ");
			hql.append(" it.name AS nameIdentificationType, ");
			hql.append(" sr.id AS idSedRole, ");
			hql.append(" sr.name AS nameSedRole ");
			hql.append(" FROM SedUser su, ");
			hql.append(" IdentificationType it, ");
			hql.append(" SedRoleUser sru, ");
			hql.append(" SedRole sr ");
			hql.append(" WHERE su.idIdentificationType = it.id ");
			hql.append(" AND sru.idSedUser = su.id ");
			hql.append(" AND sr.id = sru.idSedRole ");
			hql.append(" AND su.state = :state ");
			hql.append(" AND it.state = :state ");
			hql.append(" AND sru.state = :state ");
			hql.append(" AND sr.state = :state ");

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

	/** @author MTorres 17/06/2014 23:54:10 * */
	public void updateSedUserLogin(SedUser sedUser, String password) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" UPDATE SedUserLogin sul ");
			hql.append(" SET sul.userName = :userName, ");
			hql.append(" sul.userCreation = :userCreation, ");
			hql.append(" sul.dateCreation = :dateCreation ");
			hql.append(password != null ? " , sul.md5Password = :md5Password" : "");
			hql.append(" WHERE sul.idSedUser = :idSedUser ");
			hql.append(" AND sul.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("userName", sedUser.getIdentification());
			qo.setParameter("userCreation", "admin");
			qo.setParameter("dateCreation", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameter("idSedUser", sedUser.getId());
			qo.setParameter("state", IState.ACTIVE);

			if (password != null)
				qo.setParameter("md5Password", ManageMD5.parseMD5(password));

			qo.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}

	}

	/** @author MTorres 17/06/2014 23:54:10 * */
	public void updateSedRoleUser(SedUser sedUser) throws Exception{
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" UPDATE SedRoleUser sru ");
			hql.append(" SET sru.idSedRole = :idSedRole, ");
			hql.append(" sru.userCreation = :userCreation, ");
			hql.append(" sru.dateCreation = :dateCreation ");
			hql.append(" WHERE sru.idSedUser = :idSedUser ");
			hql.append(" AND sru.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("idSedRole", sedUser.getIdSedRole());
			qo.setParameter("userCreation", "admin");
			qo.setParameter("dateCreation", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameter("idSedUser", sedUser.getId());
			qo.setParameter("state", IState.ACTIVE);
			
			qo.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
		
	}
}
