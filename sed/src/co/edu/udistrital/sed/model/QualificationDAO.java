package co.edu.udistrital.sed.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.connection.HibernateDAO;

public class QualificationDAO extends HibernateDAO {

	/** @author MTorres 7/06/2014 */
	public List<Qualification> loadQualificationListByStudent(Long idStudent, int idPeriod) throws Exception {
		StringBuilder hql = null;
		Query qo = null;

		try {
			hql = new StringBuilder();
			hql.append(" SELECT q.id AS id, ");
			hql.append(" q.idSubject AS idSubject, ");
			hql.append(" q.value AS value, ");
			hql.append(" q.idQualificationType AS idQualificationType, ");
			hql.append(" std.id AS idStudent, ");
			hql.append(" std.name AS studentName, ");
			hql.append(" s.id AS idSubject, ");
			hql.append(" s.name AS subjectName, ");
			hql.append(" ka.id AS idKnowledgeArea, ");
			hql.append(" ka.name AS knowledgeAreaName ");
			hql.append(" FROM Qualification q, ");
			hql.append(" StudentCourse sc, ");
			hql.append(" Student std, ");
			hql.append(" Subject s, ");
			hql.append(" KnowledgeArea ka ");
			hql.append(" WHERE sc.id = q.idStudentCourse ");
			hql.append(" AND std.id = sc.idStudent ");
			hql.append(" AND s.id = q.idSubject ");
			hql.append(" AND ka.id = s.idKnowledgeArea ");
			hql.append(" AND std.id = :idStudent ");
			hql.append(idPeriod > 1000 ? " AND sc.idPeriod = :idPeriod " : "");
			hql.append(" AND q.state = :state ");
			hql.append(" ORDER BY ka.id, s.id, q.idQualificationType ");

			qo = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(Qualification.class));
			qo.setParameter("idStudent", idStudent);
			qo.setParameter("state", IState.ACTIVE);
			if(idPeriod>1000)
				qo.setParameter("idPeriod", Long.valueOf(idPeriod));
			
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
