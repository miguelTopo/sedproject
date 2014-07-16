package co.edu.udistrital.sed.assignment.controller;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Subject;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "assignmentBean", pattern = "/portal/cargaAcademica", viewId = "/pages/assignment/assignment.jspx")
public class AssignmentBean extends BackingBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7388806594985147701L;

	// Primivites
	private boolean showAssigment;

	// Basic Java Object
	private Date assigInitialDate;
	private Date assigEndDate;
	private Long sheduleOption;
	private Long idGrade;
	private Long idCourse;

	// API Object
	private ScheduleModel model;
	private ScheduleEvent event;

	// User List
	private List<SedUser> teacherList;
	private List<Subject> subjectList;
	private List<Course> courseTmpList;

	private AssignmentController controller;

	public AssignmentBean() throws Exception {
		try {
			this.model = new DefaultScheduleModel();
			loadAssignmentView();
			setShowAssigment(true);
		} catch (Exception e) {
			throw e;
		}
	}

	private void loadAssignmentView() {
		try {
			this.controller = new AssignmentController();
			this.teacherList = this.controller.loadSedUserByRole(ISedRole.TEACHER);
			this.subjectList = this.controller.loadSubjectList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDateSelect(SelectEvent selectEvent) {
		try {
			this.event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleGradeChange() {
		try {
			this.courseTmpList = null;
			if (this.idGrade != null && !this.idGrade.equals(0L))
				this.courseTmpList = loadCourseListByGrade(this.idGrade);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean getValidateSedUserRole() throws Exception {

		return false;
	}

	// ////////----------getters && setters ----------//////////

	public boolean isShowAssigment() {
		return showAssigment;
	}

	public void setShowAssigment(boolean showAssigment) {
		this.showAssigment = showAssigment;
	}

	public ScheduleModel getModel() {
		return model;
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}

	public Date getAssigInitialDate() {
		return assigInitialDate;
	}

	public void setAssigInitialDate(Date assigInitialDate) {
		this.assigInitialDate = assigInitialDate;
	}

	public Date getAssigEndDate() {
		return assigEndDate;
	}

	public void setAssigEndDate(Date assigEndDate) {
		this.assigEndDate = assigEndDate;
	}

	public Long getSheduleOption() {
		return sheduleOption;
	}

	public void setSheduleOption(Long sheduleOption) {
		this.sheduleOption = sheduleOption;
	}

	public List<SedUser> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(List<SedUser> teacherList) {
		this.teacherList = teacherList;
	}

	public List<Subject> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<Subject> subjectList) {
		this.subjectList = subjectList;
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

	public List<Course> getCourseTmpList() {
		return courseTmpList;
	}

	public void setCourseTmpList(List<Course> courseTmpList) {
		this.courseTmpList = courseTmpList;
	}

}
