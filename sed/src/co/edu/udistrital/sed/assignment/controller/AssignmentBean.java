package co.edu.udistrital.sed.assignment.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.sed.model.Assignment;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Grade;
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
	private boolean validAssign;

	// Basic Java Object
	private Date assignStartDate;
	private Date assignEndDate;
	private Long sheduleOption;
	private Long idGrade;
	private Long idCourse;
	private Calendar startDate;
	private Calendar endDate;

	// API Object
	private ScheduleModel model;
	private ScheduleEvent event;

	// User List
	private List<SedUser> teacherList;
	private List<Subject> subjectList;
	private List<Course> courseTmpList;
	private List<Grade> gradeTmpList;

	// User Object
	private Assignment assignment;

	// Controller
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

	@PostConstruct
	public void init() {
		try {
			Calendar sc = new GregorianCalendar();
			Calendar ec = new GregorianCalendar();

			sc.set(Calendar.HOUR_OF_DAY, 9);
			sc.set(Calendar.MINUTE, 30);



			this.model = new DefaultScheduleModel();
//			Date date1 = new Date();
//			Date date2 = new Date();
//			date1.setHours(7);
//			date1.setMinutes(0);
//			date2.setHours(9);
//			date2.setMinutes(0);
//			this.model.addEvent(new DefaultScheduleEvent("Evento verficador", date1, date2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean validSaveTeacherAssignment() throws Exception {
		try {
			if (this.assignment == null) {
				addWarnMessage("Programar Docente", "Por favor diligencie completamente el formulario.");
				return false;
			} else if (this.assignment.getIdSedUser() == null || this.assignment.getIdSedUser().equals(0L)) {
				addWarnMessage("Programar Docente", "Por favor seleccione el docente.");
				return false;
			} else if (this.assignment.getIdGrade() == null || this.assignment.getIdGrade().equals(0L)) {
				addWarnMessage("Programar Docente", "Por favor seleccione el grado.");
				return false;
			} else if (this.assignment.getIdCourse() == null || this.assignment.getIdCourse().equals(0L)) {
				addWarnMessage("Programar Docente", "Por favor seleccione el curso.");
				return false;
			} else if (this.assignStartDate == null) {
				addWarnMessage("Programar Docente", "Por favor seleccione la hora inicial.");
				return false;
			} else if (this.assignEndDate == null) {
				addWarnMessage("Programar Docente", "Por favor seleccione la hora final.");
				return false;
			} else if (this.assignment.getIdSubject() == null || this.assignment.getIdSubject().equals(0L)) {
				addWarnMessage("Programar Docente", "Por favor seleccione la materia.");
				return false;
			} else
				return this.validAssign;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 22/7/2014 20:46:42 */
	public void saveTeacherAssignment() {
		try {
			if (!validSaveTeacherAssignment())
				return;

			this.assignment.setIdPeriod(Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			this.assignment.setIdDay(Long.valueOf(this.startDate.get(Calendar.DAY_OF_WEEK)));
			this.assignment.setStartHour(ManageDate.formatDate(this.startDate.getTime(), ManageDate.HH_MM_SS));
			this.assignment.setEndHour(ManageDate.formatDate(this.endDate.getTime(), ManageDate.HH_MM_SS));
			this.assignment.setUserCreation(getUserSession() != null ? getUserSession().getIdentification() : "admin");
			this.assignment.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			this.assignment.setState(IState.ACTIVE);

			if (this.controller.saveTeacherAssignment(this.assignment)) {
				this.model.addEvent(new DefaultScheduleEvent(this.assignment.getIdSedUser() + "" + this.assignment.getIdSubject(), this.startDate
					.getTime(), this.endDate.getTime()));
				clearSelectDateDialog();
				getRequestContext().execute("PF('dlgSelectDateWV').hide();");
				getRequestContext().update(":calendarAssigmentForm:assignmentSchedule");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadAssignmentView() {
		try {
			this.controller = new AssignmentController();
			this.teacherList = this.controller.loadSedUserByRole(ISedRole.TEACHER);
			this.gradeTmpList = getGradeList();
			this.assignment = new Assignment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		try {
			System.out.println(event.getDayDelta());
			System.out.println(event.getMinuteDelta());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		try {
			System.out.println("mmoviendo evento");
			System.out.println(event.getDayDelta());
			System.out.println(event.getMinuteDelta());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEventSelect(SelectEvent selectEvent) {
		try {
			System.out.println("ingresando a seleccionar evento");
			this.event = (ScheduleEvent) selectEvent.getObject();
			System.out.println(this.event.getStartDate());
			System.out.println(this.event.getEndDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDateSelect(SelectEvent selectEvent) {
		try {
			this.event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
			if (this.event != null) {

				this.startDate = Calendar.getInstance();
				this.startDate.setTime(this.event.getStartDate());

				this.endDate = Calendar.getInstance();
				this.endDate.setTime(this.event.getStartDate());
				this.endDate.set(Calendar.HOUR_OF_DAY, this.startDate.get(Calendar.HOUR_OF_DAY) + 2);

				this.assignStartDate = this.startDate.getTime();
				this.assignEndDate = this.endDate.getTime();

				System.out.println("Hora inicial: " + this.assignStartDate);
				System.out.println("Hora final: " + this.assignEndDate);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 22/7/2014 22:10:48 */
	public void handleAssignDateChange() {
		try {
			if (this.assignStartDate != null && this.assignEndDate != null) {
				this.validAssign = true;

				Calendar calStartDate = Calendar.getInstance();
				Calendar calEndDate = Calendar.getInstance();
				calStartDate.setTime(this.assignStartDate);
				calEndDate.setTime(this.assignEndDate);

				calStartDate.set(Calendar.YEAR, 0);
				calStartDate.set(Calendar.MONTH, 0);
				calStartDate.set(Calendar.DATE, 0);
				calStartDate.set(Calendar.MILLISECOND, 0);
				calEndDate.set(Calendar.YEAR, 0);
				calEndDate.set(Calendar.MONTH, 0);
				calEndDate.set(Calendar.DATE, 0);
				calEndDate.set(Calendar.MILLISECOND, 0);

				this.startDate.set(Calendar.MINUTE, calStartDate.get(Calendar.MINUTE));
				this.startDate.set(Calendar.HOUR_OF_DAY, calStartDate.get(Calendar.HOUR_OF_DAY));
				this.startDate.set(Calendar.SECOND, 0);
				this.endDate.set(Calendar.MINUTE, calEndDate.get(Calendar.MINUTE));
				this.endDate.set(Calendar.HOUR_OF_DAY, calEndDate.get(Calendar.HOUR_OF_DAY));
				this.endDate.set(Calendar.SECOND, 0);

				if (calStartDate.equals(calEndDate)) {
					addWarnMessage("Programar Docente", "Las horas de inicio y fin no pueden ser identicas, por favor verifique.");
					this.validAssign = false;
					return;
				} else if (calStartDate.after(calEndDate)) {
					addWarnMessage("Programar Docente", "Error en las horas seleccionadas por favor verifique.");
					this.validAssign = false;
					return;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 22/7/2014 19:59:16 */
	public void handleGradeChange(boolean dialogChange) {
		try {
			this.courseTmpList = null;
			this.subjectList = null;
			if (dialogChange) {
				if (this.assignment != null && this.assignment.getIdGrade() != null && !this.assignment.getIdGrade().equals(0L)) {
					this.courseTmpList = loadCourseListByGrade(this.assignment.getIdGrade());
					this.subjectList = loadSubjectListByGrade(this.assignment.getIdGrade());
				}
			} else {
				if (this.idGrade != null && !this.idGrade.equals(0L))
					this.courseTmpList = loadCourseListByGrade(this.idGrade);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** @author MTorres 22/7/2014 23:16:45 */
	private void clearSelectDateDialog() throws Exception {
		try {
			this.assignment = null;
			this.startDate = null;
			this.endDate = null;
			this.subjectList = null;
			this.courseTmpList = null;
		} catch (Exception e) {
			throw e;
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

	public Date getAssignStartDate() {
		return assignStartDate;
	}

	public void setAssignStartDate(Date assignStartDate) {
		this.assignStartDate = assignStartDate;
	}

	public Date getAssignEndDate() {
		return assignEndDate;
	}

	public void setAssignEndDate(Date assignEndDate) {
		this.assignEndDate = assignEndDate;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public List<Grade> getGradeTmpList() {
		return gradeTmpList;
	}

	public void setGradeTmpList(List<Grade> gradeTmpList) {
		this.gradeTmpList = gradeTmpList;
	}


}
