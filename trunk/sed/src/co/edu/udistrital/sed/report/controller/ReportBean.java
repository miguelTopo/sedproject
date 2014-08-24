package co.edu.udistrital.sed.report.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.engine.spi.LoadQueryInfluencers;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.sed.api.IQualificationType;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.KnowledgeArea;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.QualificationType;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.report.api.IReport;
import co.edu.udistrital.sed.util.QualificationUtil;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "addReport", pattern = "/portal/reporte", viewId = "/pages/report/report.jspx")
public class ReportBean extends BackingBean implements IReport {

	// Basic Java Object
	private Long idGrade;

	// User List
	private List<Student> studentList;
	private List<Subject> subjectGradeList;
	private List<KnowledgeArea> knowledgeAreaGradeList;

	// Controller
	private ReportController controller;

	/** @author MTorres 10/08/2014 3:52:21 p. m. */
	public ReportBean() throws Exception {
		try {
			this.controller = new ReportController();
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 10/08/2014 5:09:33 p. m. */
	private void loadHeaderReport(List<Long> idKnowledgeAreaList) throws Exception {
		try {
			if (idKnowledgeAreaList != null && !idKnowledgeAreaList.isEmpty()) {
				this.knowledgeAreaGradeList = new ArrayList<KnowledgeArea>();
				for (KnowledgeArea ka : getKnowledgeAreaList()) {
					ka.setSubjectList(null);
					if (idKnowledgeAreaList.contains(ka.getId()) && !this.knowledgeAreaGradeList.contains(ka)) {
						this.knowledgeAreaGradeList.add(ka);
					}

				}
				for (KnowledgeArea ka : this.knowledgeAreaGradeList) {
					for (Subject s : this.subjectGradeList) {
						if (s.getIdKnowledgeArea().equals(ka.getId())) {

							if (ka.getSubjectList() == null)
								ka.setSubjectList(new ArrayList<Subject>());
							ka.getSubjectList().add(s);
						}
						if (s.getIdKnowledgeArea() > ka.getId())
							break;
					}
				}

			}
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 10/08/2014 3:46:08 p. m. */
	public void loadReportQualificationList() {
		try {
			this.studentList = null;
			this.subjectGradeList = null;
			this.knowledgeAreaGradeList = null;

			if (!validateLoadReportQualificationList())
				return;

			// Cargar materias del grado
			this.subjectGradeList = loadSubjectListByGrade(this.idGrade);
			List<Long> idKnowledgeAreaList = new ArrayList<Long>();

			for (Subject s : this.subjectGradeList) {
				if (!idKnowledgeAreaList.contains(s.getIdKnowledgeArea()))
					idKnowledgeAreaList.add(s.getIdKnowledgeArea());
			}

			loadHeaderReport(idKnowledgeAreaList);

			// Cargar los cursos asociados al grado seleccionado
			List<Course> courseList = loadCourseListByGrade(this.idGrade);
			List<Long> idCourseList = new ArrayList<Long>(courseList.size());
			for (Course c : courseList)
				idCourseList.add(c.getId());

			// Cargar estudiantes de los cursos respectivos
			this.studentList = this.controller.loadStudentListByGrade(idCourseList);
			List<Long> idStudentCourseList = new ArrayList<Long>(this.studentList.size());

			for (Student s : this.studentList)
				idStudentCourseList.add(s.getIdStudentCourse());

			// Cargar calificaciones de estudiantes en cursos
			List<Qualification> qualificationList = this.controller.loadQualificationList(idStudentCourseList);

			buildReport(qualificationList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**@author MTorres*/
	public void handleReportDataExporter(Object o) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/** @author MTorres 11/08/2014 10:54:56 p. m. */
	private List<Qualification> buildTotalQualificationList(Subject subject, Student student) throws Exception {
		try {
			List<Qualification> qList = new ArrayList<Qualification>(getQualificationTypeList().size());
			for (QualificationType qt : getQualificationTypeList()) {
				Qualification q = new Qualification(0.0);
				q.setIdQualificationType(qt.getId());
				q.setQualificationTypeName(loadQualificationTypeById(qt.getId()).getName());
				q.setIdKnowledgeArea(subject.getIdKnowledgeArea());
				q.setIdStudentCourse(student.getIdStudentCourse());
				q.setIdStudent(student.getId());
				q.setIdSubject(subject.getId());
				qList.add(q);
			}
			return qList;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 11/08/2014 10:43:33 p. m. */
	private void buildReport(List<Qualification> qualificationList) throws Exception {
		try {
			for (Student s : this.studentList) {
				s.setQualificationList(null);
				for (Qualification q : qualificationList) {

					if (s.getIdStudentCourse().equals(q.getIdStudentCourse())) {
						s.getQualificationTmpList().add(q);
					} else if (q.getIdStudentCourse().intValue() > s.getIdStudentCourse().intValue())
						break;
				}
			}

			List<Long> idQualificationTypeList = null;
			Long idSubject = 0L;

			// Aca se verifica las notas y tipos de notas que tiene cada estudiante.

			for (Student s : this.studentList) {
				idQualificationTypeList = null;
				idQualificationTypeList = new ArrayList<Long>(getQualificationTypeList().size());

				int count = 0;

				for (Qualification q : s.getQualificationTmpList()) {
					count++;

					if (!idSubject.equals(0L) && (q.getIdSubject().intValue() > idSubject.intValue())) {
						QualificationUtil qu = new QualificationUtil(idSubject, idQualificationTypeList);
						s.getQualificationUtilList().add(qu);
						s.getIdSubjectList().add(idSubject);
						idQualificationTypeList = new ArrayList<Long>(getQualificationTypeList().size());
					}

					idSubject = q.getIdSubject();

					if (q.getIdStudentCourse().equals(s.getIdStudentCourse()))
						idQualificationTypeList.add(q.getIdQualificationType());

					if (count == s.getQualificationTmpList().size() && idQualificationTypeList != null && !idQualificationTypeList.isEmpty()) {
						QualificationUtil qu = new QualificationUtil(idSubject, idQualificationTypeList);
						s.getQualificationUtilList().add(qu);
						s.getIdSubjectList().add(idSubject);
					}



				}
			}

			// Agregar Calificaciones pendientes y calificaciones presentes.

			for (Subject sb : this.subjectGradeList) {

				for (Student s : this.studentList) {

					if (s.getIdSubjectList().contains(sb.getId())) {

						for (QualificationUtil qu : s.getQualificationUtilList()) {

							if (sb.getId().equals(qu.getIdSubject())) {

								for (QualificationType qt : getQualificationTypeList()) {

									// Validando cual es la calificacion final
									if (qt.getId().equals(IQualificationType.CF))
										s.getQualificationList().add(validateFinalQualification(s.getQualificationList(), sb, s));

									else if (!qu.getIdQualficationTypeList().contains(qt.getId())) {
										Qualification qualification = new Qualification();
										qualification.setValue(0D);
										qualification.setIdSubject(sb.getId());
										qualification.setIdStudentCourse(s.getIdStudentCourse());
										qualification.setIdStudent(s.getId());
										qualification.setIdKnowledgeArea(sb.getIdKnowledgeArea());
										qualification.setIdQualificationType(qt.getId());
										s.getQualificationList().add(qualification);
									} else {

										for (Qualification q : s.getQualificationTmpList()) {
											if (q.getIdQualificationType().equals(qt.getId()) && q.getIdSubject().equals(sb.getId())) {
												s.getQualificationList().add(q);
												break;
											}

										}
									}

								}

							}
						}

					} else
						s.getQualificationList().addAll(buildTotalQualificationList(sb, s));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author */
	private Qualification validateFinalQualification(List<Qualification> qualificationList, Subject sb, Student s) throws Exception {
		try {
			Qualification qualification = new Qualification();
			qualification.setValue(0D);
			qualification.setIdSubject(sb.getId());
			qualification.setIdStudentCourse(s.getIdStudentCourse());
			qualification.setIdStudent(s.getId());
			qualification.setIdKnowledgeArea(sb.getIdKnowledgeArea());
			qualification.setIdQualificationType(IQualificationType.CF);

			for (Qualification q : qualificationList) {
				if (q.getIdSubject().intValue() > sb.getId().intValue())
					break;
				if (q.getValue() != null && !q.getValue().equals(0D) && q.getIdSubject().equals(sb.getId()))
					qualification.setValue(q.getValue());
			}
			return qualification;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 10/08/2014 3:48:14 p. m. */
	private boolean validateLoadReportQualificationList() throws Exception {
		try {
			if (this.idGrade == null || this.idGrade.equals(0L)) {
				addWarnMessage("Ver Notas", "Por favor seleccione el grado.");
				return false;
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 10/08/2014 3:34:30 p. m. */
	public boolean getValidateSedUserRole() throws Exception {
		try {
			if (getUserSession() != null) {
				if (getUserSession().getIdSedRole().equals(ISedRole.ADMINISTRATOR))
					return true;
				else
					return false;
			} else
				return false;
		} catch (Exception e) {
			throw e;
		}
	}

	public Long getIdGrade() {
		return idGrade;
	}

	public void setIdGrade(Long idGrade) {
		this.idGrade = idGrade;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public List<Subject> getSubjectGradeList() {
		return subjectGradeList;
	}

	public void setSubjectGradeList(List<Subject> subjectGradeList) {
		this.subjectGradeList = subjectGradeList;
	}

	public List<KnowledgeArea> getKnowledgeAreaGradeList() {
		return knowledgeAreaGradeList;
	}

	public void setKnowledgeAreaGradeList(List<KnowledgeArea> knowledgeAreaGradeList) {
		this.knowledgeAreaGradeList = knowledgeAreaGradeList;
	}

}
