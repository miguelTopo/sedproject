package co.edu.udistrital.sed.tracing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.QualificationType;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.util.QualificationUtil;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "tracing", pattern = "/portal/seguimiento", viewId = "/pages/tracing/tracing.jspx")
public class TracingBean extends BackingBean {

	// Primitives
	private boolean showStudentList;
	private boolean showStudentDetail;

	// Basic Java Object
	private Long idPeriod;
	private Long idGrade;
	private Long idCourse;

	// User List
	private List<Course> courseTmpList;
	private List<Student> studentList;
	private List<Student> studentFilteringList;
	private List<Qualification> studentQualificationList;
	private List<Qualification> studentQualificationFilterList;
	private List<QualificationUtil> qualificationUtilList;

	// User Object
	private Student studentSelected;

	// Controller
	private TracingController controller;

	public TracingBean() throws Exception {
		try {
			this.controller = new TracingController();
			this.showStudentList = true;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 4/9/2014 23:42:06 */
	private List<Long> loadCourseList() throws Exception {
		try {
			List<Course> courseTmpList = loadCourseListByGrade(this.idGrade);
			List<Long> idCourseList = new ArrayList<Long>(courseTmpList.size());

			for (Course c : courseTmpList)
				idCourseList.add(c.getId());

			return idCourseList;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 4/9/2014 23:18:12 */
	public void loadStudentList() {
		try {
			if (!validateLoadStudentList())
				return;
			this.studentList = null;
			List<Long> idCourseList = null;


			if (this.idCourse == null || this.idCourse.equals(0L) && this.idGrade != null && !this.idGrade.equals(0L))
				idCourseList = loadCourseList();

			this.studentList = this.controller.loadStudentList(this.idPeriod, this.idGrade, this.idCourse, idCourseList);
			this.studentFilteringList = this.studentList;
			if (this.studentList == null || this.studentList.isEmpty())
				addInfoMessage("Buscar", "No se encontró ningun estudiante con los criterios seleccionados.");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 4/9/2014 23:12:12 */
	private boolean validateLoadStudentList() throws Exception {
		try {
			if (this.idPeriod == null || this.idPeriod.equals(0L)) {
				addWarnMessage("Buscar", "El criterio minimo de búsqueda es el periodo, por favor seleccione uno.");
				return false;
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 8/9/2014 20:30:46 */
	public void goStudentDetail() {
		try {
			if (this.studentSelected != null) {
				hideAll();
				this.showStudentDetail = true;
				List<Qualification> qualificationList =
					this.controller.loadStudentQualificationTrace(this.studentSelected.getIdStudentCourse(), this.idPeriod);
				buildQualificationStudentList(qualificationList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 15/9/2014 23:35:19 */
	public void goBack() {
		try {
			hideAll();
			this.showStudentList = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 11/9/2014 22:01:52 */
	public void handleGradeChange() {
		try {
			this.courseTmpList = null;
			if (this.idGrade != null && !this.idGrade.equals(0L))
				this.courseTmpList = loadCourseListByGrade(this.idGrade);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 8/9/2014 22:20:51 */
	private void buildQualificationStudentList(List<Qualification> qualificationList) throws Exception {
		try {
			if (qualificationList != null && !qualificationList.isEmpty()) {

				this.qualificationUtilList = new ArrayList<QualificationUtil>();
				Long idSubject = qualificationList.get(0).getIdSubject();
				String subjectName = null;
				String knowledgeAreaName = null;

				List<Long> idQualificationTypeList = new ArrayList<Long>();
				for (Qualification q : qualificationList) {


					if (!idSubject.equals(q.getIdSubject())) {
						QualificationUtil qu = new QualificationUtil(idSubject, idQualificationTypeList);

						qu.setSubjectName(subjectName);
						qu.setKnowledgeAreaName(knowledgeAreaName);
						this.qualificationUtilList.add(qu);

						idQualificationTypeList = null;
						idQualificationTypeList = new ArrayList<Long>();
						idSubject = q.getIdSubject();
						subjectName = q.getSubjectName();
						knowledgeAreaName = q.getKnowledgeAreaName();
						qu.setKnowledgeAreaName(knowledgeAreaName);
						idQualificationTypeList.add(q.getIdQualificationType());
					} else
						idQualificationTypeList.add(q.getIdQualificationType());

					subjectName = q.getSubjectName();
					knowledgeAreaName = q.getKnowledgeAreaName();
				}

				if (idQualificationTypeList != null && !idQualificationTypeList.isEmpty() && idSubject != null) {
					QualificationUtil qu = new QualificationUtil(idSubject, idQualificationTypeList);
					qu.setSubjectName(subjectName);
					qu.setKnowledgeAreaName(knowledgeAreaName);
					this.qualificationUtilList.add(qu);
					idQualificationTypeList = null;
					idSubject = null;
				}

				for (QualificationUtil qu : this.qualificationUtilList) {

					for (QualificationType qt : getQualificationTypeList()) {
						if (qu.getIdQualficationTypeList().contains(qt.getId())) {

							for (Qualification q : qualificationList) {
								if (q.getIdSubject().equals(qu.getIdSubject()) && q.getIdQualificationType().equals(qt.getId())) {
									qu.getQualificationList().add(q);
									break;
								}
							}

						} else {
							Qualification qualification = new Qualification();
							qualification.setValue(0D);
							qualification.setIdQualificationType(qt.getId());
							qualification.setIdSubject(qu.getIdSubject());
							qu.getQualificationList().add(qualification);

						}
					}

				}
			}

		} catch (Exception e) {
			throw e;
		}
	}

	private void hideAll() throws Exception {
		try {
			this.showStudentList = false;
			this.showStudentDetail = false;
		} catch (Exception e) {
			throw e;
		}
	}


	public boolean getValidateSedUserRole() throws Exception {
		try {
			if (getUserSession().getIdSedRole().equals(ISedRole.ADMINISTRATOR))
				return true;
			else
				return false;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Course> getCourseTmpList() {
		return courseTmpList;
	}

	public void setCourseTmpList(List<Course> courseTmpList) {
		this.courseTmpList = courseTmpList;
	}

	public Long getIdPeriod() {
		return idPeriod;
	}

	public void setIdPeriod(Long idPeriod) {
		this.idPeriod = idPeriod;
	}

	public Long getIdGrade() {
		return idGrade;
	}

	public void setIdGrade(Long idGrade) {
		this.idGrade = idGrade;
	}

	public Long getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(Long idCourse) {
		this.idCourse = idCourse;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public List<Student> getStudentFilteringList() {
		return studentFilteringList;
	}

	public void setStudentFilteringList(List<Student> studentFilteringList) {
		this.studentFilteringList = studentFilteringList;
	}

	public Student getStudentSelected() {
		return studentSelected;
	}

	public void setStudentSelected(Student studentSelected) {
		this.studentSelected = studentSelected;
	}

	public boolean isShowStudentList() {
		return showStudentList;
	}

	public void setShowStudentList(boolean showStudentList) {
		this.showStudentList = showStudentList;
	}

	public boolean isShowStudentDetail() {
		return showStudentDetail;
	}

	public void setShowStudentDetail(boolean showStudentDetail) {
		this.showStudentDetail = showStudentDetail;
	}

	public List<Qualification> getStudentQualificationList() {
		return studentQualificationList;
	}

	public void setStudentQualificationList(List<Qualification> studentQualificationList) {
		this.studentQualificationList = studentQualificationList;
	}

	public List<Qualification> getStudentQualificationFilterList() {
		return studentQualificationFilterList;
	}

	public void setStudentQualificationFilterList(List<Qualification> studentQualificationFilterList) {
		this.studentQualificationFilterList = studentQualificationFilterList;
	}

	public List<QualificationUtil> getQualificationUtilList() {
		return qualificationUtilList;
	}

	public void setQualificationUtilList(List<QualificationUtil> qualificationUtilList) {
		this.qualificationUtilList = qualificationUtilList;
	}

}
