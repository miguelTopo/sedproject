package co.edu.udistrital.core.login.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.api.IState;
import co.edu.udistrital.core.common.encryption.ManageMD5;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.connection.HibernateDAO;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.sed.model.Student;
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
			qo.setParameter("md5Password", password);
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

	public Boolean validateOldPassword(Long idUser, String password) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT sul.idSedUser ");
			hql.append(" FROM SedUserLogin sul ");
			hql.append(" WHERE sul.idSedUser = :idUser ");
			hql.append(" AND sul.md5Password = :md5Password ");
			hql.append(" AND sul.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("idUser", idUser);
			qo.setParameter("md5Password", ManageMD5.parseMD5(password));
			qo.setParameter("state", IState.ACTIVE);
			qo.setMaxResults(1);

			Long idSedUser = (Long) qo.uniqueResult();

			if (idSedUser != null) {
				hql = new StringBuilder();
				qo = null;
				return true;

			} else
				return false;
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
			hql.append(" su.userCreation AS userCreation, ");
			hql.append(" su.dateCreation AS dateCreation, ");
			hql.append(" su.email AS email, ");
			hql.append(" su.birthday AS birthday, ");
			hql.append(" su.state AS state, ");
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
			hql.append(" ORDER BY su.lastName ");

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
			hql.append(" su.identification AS identification ");
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
	public void updateSedRoleUser(SedUser sedUser) throws Exception {
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

	/** @author MTorres 18/06/2014 20:08:48 */
	public boolean deleteSedUser(SedUser sedUser, String user) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		int affectedRow = 0;
		try {
			hql.append(" UPDATE SedUser su ");
			hql.append(" SET su.state = :inactiveState, ");
			hql.append(" su.userChange = :userChange, ");
			hql.append(" su.dateChange = :dateChange ");
			hql.append(" WHERE su.id = :idSedUser ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("inactiveState", IState.INACTIVE);
			qo.setParameter("userChange", user);
			qo.setParameter("dateChange", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameter("idSedUser", sedUser.getId());
			affectedRow += qo.executeUpdate();


			hql = null;
			qo = null;
			hql = new StringBuilder();
			hql.append(" UPDATE SedUserLogin sul ");
			hql.append(" SET sul.state = :inactiveState, ");
			hql.append(" sul.userChange = :userChange, ");
			hql.append(" sul.dateChange = :dateChange ");
			hql.append(" WHERE sul.idSedUser = :idSedUser ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("inactiveState", IState.INACTIVE);
			qo.setParameter("userChange", user);
			qo.setParameter("dateChange", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameter("idSedUser", sedUser.getId());

			affectedRow += qo.executeUpdate();

			if (sedUser.getIdSedRole().equals(ISedRole.STUDENT)) {
				hql = null;
				qo = null;
				hql = new StringBuilder();
				hql.append(" UPDATE Student s ");
				hql.append(" SET s.state = :inactiveState, ");
				hql.append(" s.userChange = :userChange, ");
				hql.append(" s.dateChange = :dateChange ");
				hql.append(" WHERE s.idSedUser = :idSedUser ");
				qo = getSession().createQuery(hql.toString());
				qo.setParameter("inactiveState", IState.INACTIVE);
				qo.setParameter("userChange", user);
				qo.setParameter("dateChange", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
				qo.setParameter("idSedUser", sedUser.getId());
				affectedRow += qo.executeUpdate();
			}
			return affectedRow > 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public boolean validateOldUserPassword(Long idSedUser, String md5OldPw) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT sul.md5Password ");
			hql.append(" FROM SedUserLogin sul ");
			hql.append(" WHERE sul.idSedUser = :idSedUser ");
			hql.append(" AND sul.md5Password = :md5Pass ");
			hql.append(" AND sul.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("idSedUser", idSedUser);
			qo.setParameter("md5Pass", md5OldPw);
			qo.setParameter("state", IState.ACTIVE);
			qo.setMaxResults(1);

			Object pw = qo.uniqueResult();

			return (pw == null) ? false : pw.toString().equals(md5OldPw);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres */
	public boolean updatePassword(Long idSedUser, String pwMD5, String user) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" UPDATE SedUserLogin sul ");
			hql.append(" SET sul.md5Password = :md5Password, ");
			hql.append(" sul.dateChange = :dateChange, ");
			hql.append(" sul.userChange = :userChange ");
			hql.append(" WHERE sul.idSedUser = :idSedUser ");
			hql.append(" AND sul.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("md5Password", pwMD5);
			qo.setParameter("dateChange", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameter("userChange", user);
			qo.setParameter("idSedUser", idSedUser);
			qo.setParameter("state", IState.ACTIVE);

			return qo.executeUpdate() == 1;
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 12/7/2014 12:50:36 */
	public void updateResponsibleList(List<Long> idStudentList, Long idResponsible, String userChange) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" UPDATE Student s ");
			hql.append(" SET s.idSedUserResponsible = :idResponsible, ");
			hql.append(" s.userChange = :userChange, ");
			hql.append(" s.dateChange = :dateChange ");
			hql.append(" WHERE s.id IN(:idStudentList) ");
			hql.append(" AND s.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("idResponsible", idResponsible);
			qo.setParameter("userChange", userChange);
			qo.setParameter("dateChange", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameterList("idStudentList", idStudentList);
			qo.setParameter("state", IState.ACTIVE);

			qo.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 12/7/2014 18:04:14 */
	public List<Student> loadStudentResponsibleListByUser(Long idSedUser) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT s.id AS id, ");
			hql.append(" s.name AS name, ");
			hql.append(" s.lastName AS lastName, ");
			hql.append(" c.id AS idCourse, ");
			hql.append(" c.name AS courseName ");
			hql.append(" FROM Student s, ");
			hql.append(" StudentCourse sc, ");
			hql.append(" Course c ");
			hql.append(" WHERE sc.idStudent = s.id ");
			hql.append(" AND c.id = sc.idCourse ");
			hql.append(" AND s.idSedUserResponsible = :idSedUserResponsible ");
			hql.append(" AND s.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Student.class));
			qo.setParameter("idSedUserResponsible", idSedUser);
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

	/** @author MTorres 12/7/2014 23:21:50 */
	public void deleteResponsibleList(List<Long> idStudentResponsibleDropList, String userChange) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" UPDATE Student s ");
			hql.append(" SET s.userChange = :userChange, ");
			hql.append(" s.dateChange = :dateChange, ");
			hql.append(" s.idSedUserResponsible = :idSedUserNull ");
			hql.append(" WHERE s.id IN(:idStudentList) ");
			hql.append(" AND s.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("userChange", userChange);
			qo.setParameter("dateChange", ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			qo.setParameter("idSedUserNull", null);
			qo.setParameter("state", IState.ACTIVE);
			qo.setParameterList("idStudentList", idStudentResponsibleDropList);

			qo.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 12/7/2014 23:39:22 */
	public boolean updateStudentCourse(SedUser sedUser) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT s.id ");
			hql.append(" FROM Student s ");
			hql.append(" WHERE s.idSedUser = :idSedUser ");
			hql.append(" AND s.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("idSedUser", sedUser.getId());
			qo.setParameter("state", IState.ACTIVE);
			qo.setMaxResults(1);

			Object o = qo.uniqueResult();

			if (o == null)
				return false;

			else if (o != null) {
				Long idStudent = Long.valueOf(o.toString());
				hql = null;
				qo = null;
				hql = new StringBuilder();

				hql.append(" UPDATE StudentCourse sc ");
				hql.append(" SET sc.idCourse = :idCourse, ");
				hql.append(" sc.idPeriod = :idPeriod ");
				hql.append(" WHERE sc.idStudent = :idStudent ");

				qo = getSession().createQuery(hql.toString());
				qo.setParameter("idCourse", sedUser.getIdStudentCourse());
				qo.setParameter("idStudent", idStudent);
				qo.setParameter("idPeriod", Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

			}
			return qo.executeUpdate() == 1 ? true : false;
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}

	}

	/** @author MTorres 14/7/2014 23:28:48 */
	public List<SedUser> loadSedUserByRole(Long idSedRole) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT su.id AS id, ");
			hql.append(" su.name ||' '|| su.lastName AS sedUserFullName, ");
			hql.append(" su.identification AS identification ");
			hql.append(" FROM SedUser su, ");
			hql.append(" SedRoleUser sru ");
			hql.append(" WHERE su.id = sru.idSedUser ");
			hql.append(" AND sru.idSedRole = :idSedRole ");
			hql.append(" AND su.state = :state ");
			hql.append(" AND sru.state = :state ");
			hql.append(" ORDER BY su.name ");
			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(SedUser.class));
			qo.setParameter("idSedRole", idSedRole);
			qo.setParameter("state", IState.ACTIVE);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/** @author MTorres 9/8/2014 18:43:42 */
	public SedUser loadSedUserById(Long idSedUser) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo = null;
		try {
			hql.append(" SELECT su.id AS id, ");
			hql.append(" su.name AS name, ");
			hql.append(" su.lastName AS lastName, ");
			hql.append(" su.birthday AS birthday, ");
			hql.append(" su.identification AS identification, ");
			hql.append(" su.email AS email, ");
			hql.append(" su.idIdentificationType AS idIdentificationType, ");
			hql.append(" it.name AS nameIdentificationType ");
			hql.append(" FROM SedUser su, ");
			hql.append(" IdentificationType it ");
			hql.append(" WHERE it.id = su.idIdentificationType ");
			hql.append(" AND su.id = :idSedUser ");
			hql.append(" AND su.state = :state ");
			hql.append(" AND it.state = :state ");

			qo = getSession().createQuery(hql.toString());
			qo.setParameter("idSedUser", idSedUser);
			qo.setParameter("state", IState.ACTIVE);
			qo.setMaxResults(1);
			return (SedUser) qo.uniqueResult();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
			qo = null;
		}
	}

	/**
	 * @author Miguel 17/11/2014 16:10:49
	 */
	public List<SedUser> loadTeacherListByCourseList(List<Long> idCourseList) throws Exception {
		StringBuilder hql = new StringBuilder();
		Query qo;
		try {
			hql.append(" SELECT DISTINCT (su.id) AS id, ");
			hql.append(" su.name ||' '||lastName AS sedUserFullName ");
			hql.append(" FROM SedUser su, ");
			hql.append(" Assignment a ");
			hql.append(" WHERE a.idSedUser = su.id ");
			hql.append(idCourseList != null ? " AND a.idCourse IN(:idCourseList) " : "");
			hql.append(" AND su.state = :state ");
			hql.append(" AND a.state = :state ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(SedUser.class));
			qo.setParameter("state", IState.ACTIVE);
			if (idCourseList != null)
				qo.setParameterList("idCourseList", idCourseList);

			return qo.list();
		} catch (Exception e) {
			throw e;
		} finally {
			hql = null;
		}
	}
}
