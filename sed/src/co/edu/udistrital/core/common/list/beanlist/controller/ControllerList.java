package co.edu.udistrital.core.common.list.beanlist.controller;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.common.list.BeanList;
import co.edu.udistrital.core.common.model.EmailTemplate;
import co.edu.udistrital.core.common.model.EmailTemplateDAO;
import co.edu.udistrital.core.login.model.SedRole;
import co.edu.udistrital.core.login.model.SedRoleDAO;
import co.edu.udistrital.core.login.model.Tree;
import co.edu.udistrital.core.login.model.TreeDAO;
import co.edu.udistrital.core.login.model.TreeSedRole;
import co.edu.udistrital.core.login.model.TreeSedRoleDAO;
import co.edu.udistrital.sed.api.IGrade;
import co.edu.udistrital.sed.api.IQualificationType;
import co.edu.udistrital.sed.model.AssignmentType;
import co.edu.udistrital.sed.model.AssignmentTypeDAO;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.CourseDAO;
import co.edu.udistrital.sed.model.Grade;
import co.edu.udistrital.sed.model.GradeDAO;
import co.edu.udistrital.sed.model.IdentificationType;
import co.edu.udistrital.sed.model.IdentificationTypeDAO;
import co.edu.udistrital.sed.model.KnowledgeArea;
import co.edu.udistrital.sed.model.KnowledgeAreaDAO;
import co.edu.udistrital.sed.model.Period;
import co.edu.udistrital.sed.model.PeriodDAO;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.QualificationHistory;
import co.edu.udistrital.sed.model.QualificationHistoryDAO;
import co.edu.udistrital.sed.model.QualificationType;
import co.edu.udistrital.sed.model.QualificationTypeDAO;
import co.edu.udistrital.sed.model.StudentCourse;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.model.SubjectDAO;


public class ControllerList extends Controller {

	/** @author MTorres */
	public List<Subject> loadSubjectList() throws Exception {
		SubjectDAO dao = new SubjectDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres */
	public List<Course> loadCourseList() throws Exception {
		CourseDAO dao = new CourseDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}

	}

	/** @author MTorres */
	public List<Grade> loadGradeList() throws Exception {
		GradeDAO dao = new GradeDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres */
	public List<Tree> loadTreeList() throws Exception {
		TreeDAO dao = new TreeDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres */
	public List<TreeSedRole> loadTreeSedRoleList() throws Exception {
		TreeSedRoleDAO dao = new TreeSedRoleDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres */
	public List<IdentificationType> loadIdentificationTypeList() throws Exception {
		IdentificationTypeDAO dao = new IdentificationTypeDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres */
	public List<SedRole> loadSedRoleList() throws Exception {
		SedRoleDAO dao = new SedRoleDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres 7/06/2014 */
	public List<EmailTemplate> loadEmailTemplateList() throws Exception {
		EmailTemplateDAO dao = new EmailTemplateDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres 7/06/2014 */
	public List<QualificationType> loadQualificationTypeList() throws Exception {
		QualificationTypeDAO dao = null;
		Transaction tx = null;
		try {
			dao = new QualificationTypeDAO();
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			dao.getSession().cancelQuery();
			dao.getSession().close();
			dao = null;
			tx = null;
		}

	}

	/** @author MTorres 10/08/2014 4:53:02 p. m. */
	public List<KnowledgeArea> loadKnowledgeAreaList() throws Exception {
		KnowledgeAreaDAO dao = new KnowledgeAreaDAO();
		try {
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}

	}

	/** @author MTorres 4/9/2014 22:57:44 */
	public List<Period> loadPeriodList() throws Exception {
		PeriodDAO dao = new PeriodDAO();
		try {
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
		}

	}

	/** @author MTorres 20/9/2014 15:42:46 */
	public void updatePeriodTask(Long periodNew) throws Exception {
		QualificationHistoryDAO dao = new QualificationHistoryDAO();
		Transaction tx = null;
		try {
			List<QualificationHistory> totalQualificationList = dao.loadTotalQualificationList();

			tx = dao.getSession().beginTransaction();

			Period p = new Period();
			p.initialize(true, "admin");
			p.setId(periodNew);
			p.setName(String.valueOf(periodNew));
			dao.getSession().save(p);
			tx.commit();

			tx = null;
			tx = dao.getSession().beginTransaction();


			int count = 0;
			StudentCourse sc = null;

			if (totalQualificationList != null && !totalQualificationList.isEmpty()) {

				for (QualificationHistory qh : totalQualificationList) {
					qh.initialize(true, "admin");

					dao.getSession().save(qh);
					if (count++ % 20 == 0)
						dao.getSession().flush();

					if (qh.getIdQualificationType().equals(IQualificationType.CF)) {

						sc = new StudentCourse();
						sc.setIdPeriod(periodNew);
						System.out.println("este es el id del estudiante" + qh.getIdStudent());
						sc.setIdStudent(qh.getIdStudent());
						// El estudiante no paso por ende se deja en el mismo curso
						if (qh.getValue() < 65.0)
							sc.setIdCourse(qh.getIdStudentCourse());
						else {
							if (!qh.getIdGrade().equals(IGrade.ELEVENTH_GRADE))
								sc.setIdCourse(getNextCourse(qh.getIdCourse()));
							else
								sc.setIdCourse(qh.getIdStudentCourse());

						}

						if (qh.getIdGrade().equals(IGrade.ELEVENTH_GRADE))
							sc = null;

					}
					if (sc != null) {
						dao.getSession().save(sc);
						sc = null;
						if (count++ % 20 == 0)
							dao.getSession().flush();
					}


				}
			}
			// Eliminar Calificaciones de periodo anterior
			dao.deleteQualificationPeriod();
			// Crear Nuevo Periodo
			tx.commit();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}

	}

	/** @author MTorres 20/9/2014 16:49:32 */
	private Long getNextCourse(Long idCourse) throws Exception {
		try {
			for (Course c : BeanList.getCourseList()) {
				if (c.getId().equals(idCourse))
					return c.getIdNextCourse();
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @author Miguel 17/11/2014 10:14:12
	 */
	public List<AssignmentType> loadAssignmentTypeList() throws Exception{
		AssignmentTypeDAO dao = new AssignmentTypeDAO();
		try {
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			throw e;
		}finally{
			dao.getSession().close();
			dao = null;
		}
	}
}
