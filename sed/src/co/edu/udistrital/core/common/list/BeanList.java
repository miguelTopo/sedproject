package co.edu.udistrital.core.common.list;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import co.edu.udistrital.core.common.list.beanlist.controller.ControllerList;
import co.edu.udistrital.core.common.model.EmailTemplate;
import co.edu.udistrital.core.common.util.resource.ManageProperties;
import co.edu.udistrital.core.login.model.SedRole;
import co.edu.udistrital.core.login.model.Tree;
import co.edu.udistrital.core.login.model.TreeSedRole;
import co.edu.udistrital.sed.api.IPeriodClose;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Grade;
import co.edu.udistrital.sed.model.IdentificationType;
import co.edu.udistrital.sed.model.KnowledgeArea;
import co.edu.udistrital.sed.model.Period;
import co.edu.udistrital.sed.model.QualificationType;
import co.edu.udistrital.sed.model.Subject;

@ManagedBean(eager = true)
@ApplicationScoped
public class BeanList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6631727569215723693L;

	private static ManageProperties properties;

	private static ControllerList controller;

	private static List<Subject> subjectList;
	private static List<Course> courseList;
	private static List<Grade> gradeList;
	private static List<Tree> treeList;
	private static List<KnowledgeArea> knowledgeAreaList;
	private static List<TreeSedRole> treeSedRoleList;
	private static List<IdentificationType> identificationTypeList;
	private static List<SedRole> sedRoleList;
	private static List<QualificationType> qualificationTypeList;
	private static List<Period> periodList;

	private static List<EmailTemplate> emailTemplateList;

	static {
		initializeList();
	}

	/** @author MTorres */
	public static void initializeList() {
		try {
			controller = new ControllerList();
			properties = new ManageProperties();
			loadSubjectList();
			loadGradeList();
			loadCourseList();
			loadTreeList();
			loadTreeSedRoleList();
			loadIdentificationTypeList();
			loadSedRoleList();
			loadEmailTemplateList();
			loadQualificationTypeList();
			loadKnowledgeAreaList();
			loadPeriodList();
			updatePeriodTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 20/9/2014 15:23:55 */
	private static void updatePeriodTask() throws Exception {
		try {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				public void run() {
					Calendar today = Calendar.getInstance();
					if (today.get(Calendar.MONTH) == IPeriodClose.MONTH && today.get(Calendar.DAY_OF_MONTH) == IPeriodClose.DAY) {
						try {
							controller.updatePeriodTask(Long.valueOf(today.get(Calendar.YEAR) + 1));
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						System.out.println("El dia " + today.getTime() + " aun no es dia de cierre de periodo.");

					}
				}
			}, 0, 86400000);
		} catch (Exception e) {
			throw e;
		}

	}

	public static void loadPeriodList() {
		try {
			if (periodList == null)
				periodList = controller.loadPeriodList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadKnowledgeAreaList() {
		try {
			if (knowledgeAreaList == null)
				knowledgeAreaList = controller.loadKnowledgeAreaList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadQualificationTypeList() {
		try {
			if (qualificationTypeList == null)
				qualificationTypeList = controller.loadQualificationTypeList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadEmailTemplateList() {
		try {
			if (emailTemplateList == null)
				emailTemplateList = controller.loadEmailTemplateList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void loadSedRoleList() {
		try {
			if (sedRoleList == null)
				sedRoleList = controller.loadSedRoleList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void loadIdentificationTypeList() {
		try {
			if (identificationTypeList == null)
				identificationTypeList = controller.loadIdentificationTypeList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void loadTreeSedRoleList() {
		try {
			if (treeSedRoleList == null)
				treeSedRoleList = controller.loadTreeSedRoleList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadTreeList() {
		try {
			if (treeList == null) {
				treeList = controller.loadTreeList();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void loadGradeList() {
		try {
			if (gradeList == null)
				gradeList = controller.loadGradeList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void loadCourseList() {
		try {
			if (courseList == null)
				courseList = controller.loadCourseList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void loadSubjectList() {
		try {
			if (subjectList == null)
				subjectList = controller.loadSubjectList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Subject> getSubjectList() {
		return subjectList;
	}

	public static List<Course> getCourseList() {
		return courseList;
	}

	public static List<Grade> getGradeList() {
		return gradeList;
	}

	public static List<Tree> getTreeList() {
		return treeList;
	}

	public static List<TreeSedRole> getTreeSedRoleList() {
		return treeSedRoleList;
	}

	public static List<IdentificationType> getIdentificationTypeList() {
		return identificationTypeList;
	}

	public static List<SedRole> getSedRoleList() {
		return sedRoleList;
	}

	public static ManageProperties getProperties() {
		return properties;
	}

	public static List<EmailTemplate> getEmailTemplateList() {
		return emailTemplateList;
	}

	public static List<QualificationType> getQualificationTypeList() {
		return qualificationTypeList;
	}

	public static List<KnowledgeArea> getKnowledgeAreaList() {
		return knowledgeAreaList;
	}

	public static List<Period> getPeriodList() {
		return periodList;
	}

}
