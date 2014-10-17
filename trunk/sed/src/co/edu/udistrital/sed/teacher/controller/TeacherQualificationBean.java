package co.edu.udistrital.sed.teacher.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.sed.api.IQualificationType;
import co.edu.udistrital.sed.api.IQualificationValue;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.model.Subject;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "tQualification", pattern = "/portal/calificar", viewId = "/pages/teacherqualification/teacherqualification.jspx")
public class TeacherQualificationBean extends BackingBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3837887624361885564L;

	private boolean validC1;
	private boolean validC2;
	private boolean validC3;
	private boolean validC4;

	private Long idCourse;
	private Long idFinalQualificationType;
	private Long idSubject;

	// User List
	private List<Student> studentList;
	private List<Student> studentFilterList;
	private List<Course> courseTeacherList;
	private List<Subject> subjectTeacherList;

	// Controller
	private TeacherQualificationController controller;


	public TeacherQualificationBean() throws Exception {
		try {
			this.controller = new TeacherQualificationController();
			this.courseTeacherList = this.controller.loadCourseListByTeacher(getUserSession().getIdSedUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 10/08/2014 11:17:42 a. m. */
	public void cleanQualificationView() {
		try {
			this.studentList = null;
			this.studentFilterList = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 31/7/2014 23:16:07 */
	public void handleCourseChange() {
		try {
			cleanQualificationView();
			this.subjectTeacherList = null;
			if (this.idCourse != null && !this.idCourse.equals(0L))
				this.subjectTeacherList = this.controller.loadSubjectListByTeacherCourse(getUserSession().getIdSedUser(), this.idCourse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 10/08/2014 10:59:01 a. m. */
	private boolean validQualificationListType() throws Exception {
		try {
			this.validC1 = false;
			this.validC2 = false;
			this.validC3 = false;
			this.validC4 = false;
			int totalC1 = 0;
			int totalC2 = 0;
			int totalC3 = 0;
			int totalC4 = 0;
			int totalValidC3 = 0;


			for (Student s : this.studentList) {
				for (Qualification q : s.getQualificationList()) {
					if (q.getIdQualificationType().equals(IQualificationType.C1) && q.getValue() > 0)
						totalC1++;
					if (q.getIdQualificationType().equals(IQualificationType.C2) && q.getValue() > 0)
						totalC2++;
					if (q.getIdQualificationType().equals(IQualificationType.C3) && q.getValue() != null)
						totalC3++;
					if (q.getIdQualificationType().equals(IQualificationType.C3) && q.getValue() > 0)
						totalValidC3++;
					if (q.getIdQualificationType().equals(IQualificationType.C4) && q.getValue() > 0)
						totalC4++;
				}
			}

			if (totalC1 != 0 && totalC1 != this.studentList.size()) {
				addWarnMessage(getMessage("page.qualification.tq.labelSave"), getMessage("page.qualification.tq.warnMissingC1"));
				return false;
			} else if (totalC2 != 0 && totalC2 != this.studentList.size()) {
				addWarnMessage(getMessage("page.qualification.tq.labelSave"), getMessage("page.qualification.tq.warnMissingC2"));
				return false;
			} else if (totalC3 != 0 && totalC3 != this.studentList.size()) {
				addWarnMessage(getMessage("page.qualification.tq.labelSave"), getMessage("page.qualification.tq.warnMissingC3"));
				return false;
			} else if (totalC4 != 0 && totalC4 != this.studentList.size()) {
				addWarnMessage(getMessage("page.qualification.tq.labelSave"), getMessage("page.qualification.tq.warnMissingC4"));
				return false;
			}
			this.idFinalQualificationType = 0L;
			// Verificar cual tipo de nota es la final
			if (totalC1 == this.studentList.size()) {
				this.validC1 = true;
				this.idFinalQualificationType = IQualificationType.C1;
			}

			if (totalC2 == this.studentList.size()) {
				this.validC2 = true;
				this.idFinalQualificationType = IQualificationType.C2;
			}
			if (totalC3 == this.studentList.size())
				this.validC3 = true;

			if (totalC4 != this.studentList.size())
				this.validC4 = true;

			if (totalValidC3 == this.studentList.size())
				this.idFinalQualificationType = IQualificationType.C3;

			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 7/8/2014 19:15:05 */
	private boolean validQualificationList() throws Exception {
		try {
			for (Student s : this.studentList) {

				for (Qualification q : s.getQualificationList()) {

					if (q.getIdQualificationType().equals(IQualificationType.C1)) {
						if ((q.getValue() > IQualificationValue.C1_MAX_VALUE || q.getValue() < IQualificationValue.C1_MIN_VALUE) && this.validC1) {
							addWarnMessage(getMessage("page.qualification.tq.labelSave"), getMessage("page.qualification.tq.warnRememberC1")
								+ IQualificationValue.C1_MAX_VALUE + " y " + IQualificationValue.C1_MIN_VALUE);
							return false;
						}
					} else if (q.getIdQualificationType().equals(IQualificationType.C2)) {
						if ((q.getValue() > IQualificationValue.C2_MAX_VALUE || q.getValue() < IQualificationValue.C2_MIN_VALUE) && this.validC2) {
							addWarnMessage(getMessage("page.qualification.tq.labelSave"), getMessage("page.qualification.tq.warnRememberC2")
								+ IQualificationValue.C2_MAX_VALUE + " y " + IQualificationValue.C2_MIN_VALUE);
							return false;
						}
					} else if (q.getIdQualificationType().equals(IQualificationType.C3)) {
						if ((q.getValue() > IQualificationValue.C3_MAX_VALUE || q.getValue() < IQualificationValue.C3_MIN_VALUE) && this.validC3) {
							addWarnMessage(getMessage("page.qualification.tq.labelSave"), getMessage("page.qualification.tq.warnRememberC3")
								+ IQualificationValue.C3_MAX_VALUE + " y " + IQualificationValue.C3_MIN_VALUE);
							return false;
						}
					} else if (q.getIdQualificationType().equals(IQualificationType.C4)) {
						if (q.getValue() > IQualificationValue.C4_MAX_VALUE || q.getValue() < IQualificationValue.C4_MIN_VALUE && this.validC4) {
							addWarnMessage(getMessage("page.qualification.tq.labelSave"), getMessage("page.qualification.tq.warnRememberC4")
								+ IQualificationValue.C4_MAX_VALUE + " y " + IQualificationValue.C4_MIN_VALUE);
							return false;
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 4/8/2014 22:45:11 */
	private boolean validLoadStudentSubjectList() throws Exception {
		try {
			if (this.idCourse == null || this.idCourse.equals(0L)) {
				addWarnMessage(getMessage("page.qualification.tc.labelView"), getMessage("page.assignment.warnCourse"));
				return false;
			} else if (this.idSubject == null || this.idSubject.equals(0L)) {
				addWarnMessage(getMessage("page.qualification.tc.labelView"), getMessage("page.assignment.warnSubject"));
				return false;
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 7/8/2014 21:18:48 */
	public void saveQualificationList() {
		try {
			if (!validQualificationListType())
				return;

			if (this.studentList != null && validQualificationList()) {
				loadFinalStudentQualification();

				if (this.controller.saveQualificationList(this.studentList, getUserSession().getIdSedUser(), this.idSubject, getUserSession()
					.getIdentification())) {
					this.validC1 = this.validC2 = this.validC3 = false;
					loadStudentSubjectList();
					addInfoMessage(getMessage("page.qualification.tq.labelSave"), getMessage("page.qualification.tq.labelSuccessSave"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 7/8/2014 16:08:25 */
	private void buildCalificationListStudent(List<Qualification> qualificationList) throws Exception {
		try {
			// Si la lista de calificaciones llega vacia se rellena con datos vacios
			if (qualificationList == null || qualificationList.isEmpty()) {
				for (Student s : this.studentList) {
					if (s.getQualificationList() == null)
						s.setQualificationList(new ArrayList<Qualification>(getQualificationTypeList().size()));
					for (int i = 0; i < getQualificationTypeList().size(); i++) {
						Qualification q = new Qualification(0.0);
						if (i == 0)
							q.setIdQualificationType(IQualificationType.C1);
						else if (i == 1)
							q.setIdQualificationType(IQualificationType.C2);
						else if (i == 2)
							q.setIdQualificationType(IQualificationType.C3);
						else if (i == 3)
							q.setIdQualificationType(IQualificationType.C4);
						else if (i == 4)
							q.setIdQualificationType(IQualificationType.CF);
						s.getQualificationList().add(q);
					}
				}
			} else {
				for (Student s : this.studentList) {
					if (s.getQualificationList() == null)
						s.setQualificationList(new ArrayList<Qualification>(getQualificationTypeList().size()));
					for (Qualification q : qualificationList) {
						if (q.getIdStudent().equals(s.getId()))
							s.getQualificationList().add(q);
						else if (q.getIdStudent() > s.getId())
							break;
					}
				}
			}


		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 10/08/2014 1:12:41 p.m. */
	private void loadFinalStudentQualification() throws Exception {
		try {
			if (this.studentList != null && !this.studentList.isEmpty()) {
				for (Student s : this.studentList) {
					for (Qualification q : s.getQualificationList()) {
						if (q.getIdQualificationType().equals(this.idFinalQualificationType)) {
							double fQualification = q.getValue();
							for (Qualification sq : s.getQualificationList()) {
								if (sq.getIdQualificationType().equals(IQualificationType.CF)) {
									sq.setValue(fQualification);
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 4/8/2014 22:35:44 */
	public void loadStudentSubjectList() {
		try {
			if (!validLoadStudentSubjectList())
				return;
			this.studentList = this.controller.loadStudentListByCourse(this.idCourse);
			if (this.studentList != null && !this.studentList.isEmpty()) {
				List<Long> idStudentList = new ArrayList<Long>(this.studentList.size());

				for (Student s : this.studentList) {
					idStudentList.add(s.getId());
				}

				List<Qualification> qualificationList = this.controller.loadQualificationByCourseSubject(idStudentList, this.idSubject);
				buildCalificationListStudent(qualificationList);
				this.studentFilterList = this.studentList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 9/8/2014 16:08:27 */
	public boolean getValidateSedUserRole() throws Exception {
		try {
			if (getUserSession() != null) {
				if (getUserSession().getIdSedRole().equals(ISedRole.ADMINISTRATOR) || getUserSession().getIdSedRole().equals(ISedRole.TEACHER))
					return true;
				else
					return false;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public List<Course> getCourseTeacherList() {
		return courseTeacherList;
	}

	public void setCourseTeacherList(List<Course> courseTeacherList) {
		this.courseTeacherList = courseTeacherList;
	}

	public Long getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(Long idCourse) {
		this.idCourse = idCourse;
	}

	public Long getIdSubject() {
		return idSubject;
	}

	public void setIdSubject(Long idSubject) {
		this.idSubject = idSubject;
	}

	public List<Subject> getSubjectTeacherList() {
		return subjectTeacherList;
	}

	public void setSubjectTeacherList(List<Subject> subjectTeacherList) {
		this.subjectTeacherList = subjectTeacherList;
	}

	public List<Student> getStudentFilterList() {
		return studentFilterList;
	}

	public void setStudentFilterList(List<Student> studentFilterList) {
		this.studentFilterList = studentFilterList;
	}

}
