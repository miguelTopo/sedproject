package co.edu.udistrital.sed.report.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.engine.spi.LoadQueryInfluencers;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.KnowledgeArea;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.QualificationType;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.report.api.IReport;

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

						System.out.println("id area conocimiento:" + ka.getId());
						System.out.println("id area conocimiento en subject:" + s.getIdKnowledgeArea());
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
			buildStudentQualificationList(qualificationList);
			buildReport();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 10/08/2014 4:41:07 p. m. */
	private void buildStudentQualificationList(List<Qualification> qualificationList) throws Exception {
		try {
			// Recorrido de estudiantes vs calificaciones para asignar las calificaciones
			// pertinentes
			for (Student s : this.studentList) {
				s.setQualificationList(null);

				List<Long> idQtStudentList = new ArrayList<Long>(getQualificationTypeList().size());

				for (Qualification q : qualificationList) {
					if (s.getIdStudentCourse().equals(q.getIdStudentCourse())) {
						if (s.getQualificationList() == null)
							s.setQualificationList(new ArrayList<Qualification>(getQualificationTypeList().size()));

						s.getQualificationList().add(q);
						idQtStudentList.add(q.getIdQualificationType());
					}

					if (s.getIdStudentCourse() > q.getIdStudentCourse())
						break;
				}

				// Verificar si tiene tipo de calificacion, si no agregarla
				for (QualificationType qt : getQualificationTypeList()) {
					if (!idQtStudentList.contains(qt.getId())) {
						if (s.getQualificationList() == null)
							s.setQualificationList(new ArrayList<Qualification>(getQualificationTypeList().size()));

						Qualification sq = new Qualification(0.0);
						sq.setIdQualificationType(qt.getId());
						sq.setQualificationTypeName(loadQualificationTypeById(qt.getId()).getName());
						s.getQualificationList().add(sq);
					}
				}

			}
			System.out.println("Finalizando construccion de notas insertadas.");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres 11/08/2014 10:54:56 p. m. */
	private List<Qualification> buildTotalQualificationList() throws Exception {
		try {
			List<Qualification> qList = new ArrayList<Qualification>(getQualificationTypeList().size());
			for (QualificationType qt : getQualificationTypeList()) {
				Qualification q = new Qualification(0.0);
				q.setIdQualificationType(qt.getId());
				q.setQualificationTypeName(loadQualificationTypeById(qt.getId()).getName());
				qList.add(q);
			}
			return qList;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 11/08/2014 10:43:33 p. m. */
	private void buildReport() throws Exception {
		try {
			for (Student s : this.studentList) {

				// Verificar las materias para los cuales el estudiante tiene calificaciones
				List<Long> idPresenceSubjectList = new ArrayList<Long>();

				for (Qualification q : s.getQualificationList()) {
					if (!idPresenceSubjectList.contains(q.getIdSubject()))
						idPresenceSubjectList.add(q.getIdSubject());
				}

				// Cargar notas faltantes del estudiante a 0
				for (KnowledgeArea ka : this.knowledgeAreaGradeList) {
					for (Subject sj : ka.getSubjectList()) {
						if (!idPresenceSubjectList.contains(sj.getId())) {
							s.setQualificationList(buildTotalQualificationList());
						}
					}
				}
			}
			System.out.println("finalizando la construccion");
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
