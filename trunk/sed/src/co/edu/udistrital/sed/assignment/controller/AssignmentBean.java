package co.edu.udistrital.sed.assignment.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	private boolean eventAdd;
	private boolean eventDelete;
	private int warnResizeEvent = -1;

	// Basic Java Object
	private Date assignStartDate;
	private Date assignEndDate;
	private Long sheduleOption;
	private Long idGrade;
	private Long idCourse;
	private Long idDay;
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
	private List<Assignment> assignmentList;

	// User Object
	private Assignment assignment;
	private Assignment assignmentFilter;

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
			this.model = new DefaultScheduleModel();
			Calendar today = Calendar.getInstance();
			// this.assignmentList =
			// this.controller.loadAssignmentListByPeriod(Long.valueOf(today.get(Calendar.YEAR)));
			// loadScheduleData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 29/7/2014 21:03:14 */
	private void loadScheduleData() {
		try {

			this.model = new DefaultScheduleModel();
			for (Assignment a : this.assignmentList) {

				Calendar cStartHour = Calendar.getInstance();
				Date startHour = ManageDate.stringToDate(a.getStartHour(), ManageDate.HH_MM_SS_24);
				Calendar auxCal = Calendar.getInstance();
				auxCal.setTime(startHour);

				cStartHour.set(Calendar.DAY_OF_WEEK, Integer.parseInt(a.getIdDay().toString()));
				cStartHour.set(Calendar.HOUR_OF_DAY, auxCal.get(Calendar.HOUR_OF_DAY));
				cStartHour.set(Calendar.MINUTE, auxCal.get(Calendar.MINUTE));
				cStartHour.set(Calendar.SECOND, 0);

				Calendar cEndHour = Calendar.getInstance();
				Date endHour = ManageDate.stringToDate(a.getEndHour(), ManageDate.HH_MM_SS_24);
				auxCal = Calendar.getInstance();
				auxCal.setTime(endHour);

				cEndHour.set(Calendar.DAY_OF_WEEK, Integer.parseInt(a.getIdDay().toString()));
				cEndHour.set(Calendar.HOUR_OF_DAY, auxCal.get(Calendar.HOUR_OF_DAY));
				cEndHour.set(Calendar.MINUTE, auxCal.get(Calendar.MINUTE));
				cEndHour.set(Calendar.SECOND, 0);

				DefaultScheduleEvent dse =
					new DefaultScheduleEvent(a.getTeacherFullName() + " " + a.getSubjectName() + " " + a.getCourseName(), cStartHour.getTime(),
						cEndHour.getTime(), a.getSubjectStyleClass());
				dse.setData(a);

				this.model.addEvent(dse);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/** @author MTorres 28/7/2014 22:54:41* */
	private boolean validAvailability() throws Exception {
		try {
			boolean valid = false;
			valid =
				this.controller.validAvailability(this.assignment.getId(), this.assignment.getIdCourse(), this.idDay, this.assignStartDate,
					this.assignEndDate, null);
			if (!valid) {
				addWarnMessage("Programar Docente",
					"Ya existe un docente asignado para el curso y horas especificadas, o hay crude de horario, por favor verifique.");
				return valid;
			} else
				valid =
					this.controller.validAvailability(this.assignment.getId(), this.assignment.getIdCourse(), this.idDay, this.assignStartDate,
						this.assignEndDate, this.assignment.getIdSedUser());
			if (!valid) {
				addWarnMessage("Programar Docente", "El docente seleccionado ya posee asignación para la fecha y horas indicadas.");
				return valid;
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}


	/** @author MTorres 25/7/2014 21:07:01 */
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
			} else if (this.idDay == null || this.idDay.equals(0L)) {
				addWarnMessage("Programar Docente", "Por favor seleccione el día.");
				return false;
			} else if (this.assignment.getIdSubject() == null || this.assignment.getIdSubject().equals(0L)) {
				addWarnMessage("Programar Docente", "Por favor seleccione la materia.");
				return false;
			} else if (!handleAssignDateChange()) {
				return false;
			} else
				return validAvailability();
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
			this.assignment.setIdDay(this.idDay);
			this.startDate.set(Calendar.SECOND, 0);
			this.endDate.set(Calendar.SECOND, 0);
			this.assignment.setStartHour(ManageDate.formatDate(this.startDate.getTime(), ManageDate.HH_MM_SS_24));
			this.assignment.setEndHour(ManageDate.formatDate(this.endDate.getTime(), ManageDate.HH_MM_SS_24));
			this.assignment.setUserCreation(getUserSession() != null ? getUserSession().getIdentification() : "admin");
			this.assignment.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
			this.assignment.setState(IState.ACTIVE);

			if (this.controller.saveTeacherAssignment(this.assignment)) {

				String teacher = loadTeacherFullNameById(this.assignment.getIdSedUser());
				String subjectName = loadSubjectById(this.assignment.getIdSubject()).getName();
				String courseName = loadCourseById(this.assignment.getIdCourse()).getName();
				String eventStyle = loadSubjectById(this.assignment.getIdSubject()).getStyleClass();

				if (!this.eventAdd) {
					deleteObjectScheduleModel(this.assignment);
					this.startDate.set(Calendar.DAY_OF_WEEK, Integer.parseInt(this.idDay.toString()));
					this.endDate.set(Calendar.DAY_OF_WEEK, Integer.parseInt(this.idDay.toString()));
				}
				this.startDate.set(Calendar.DAY_OF_WEEK, Integer.parseInt(this.idDay.toString()));
				this.endDate.set(Calendar.DAY_OF_WEEK, Integer.parseInt(this.idDay.toString()));

				this.model.addEvent(new DefaultScheduleEvent(teacher + " " + subjectName + " " + courseName, this.startDate.getTime(), this.endDate
					.getTime(), eventStyle));


				clearSelectDateDialog();
				getRequestContext().execute("PF('dlgSelectDateWV').hide();");
				getRequestContext().update(":calendarAssigmentForm:assignmentSchedule");
				getRequestContext().execute("PF('assignmentScheduleWV').update();");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 29/7/2014 23:35:07 */
	private void deleteObjectScheduleModel(Assignment assignmentDelete) throws Exception {
		try {
			if (assignmentDelete != null) {
				for (ScheduleEvent e : this.model.getEvents()) {
					Assignment a = (Assignment) e.getData();
					if (a.getId().equals(assignmentDelete.getId())) {
						this.model.deleteEvent(e);
						break;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}

	}

	/** @author MTorres 28/7/2014 23:21:46 */
	private String loadTeacherFullNameById(Long idSedUser) throws Exception {
		try {
			if (idSedUser == null || idSedUser.equals(0L))
				return null;

			for (SedUser su : this.teacherList) {
				if (su.getId().equals(idSedUser))
					return su.getSedUserFullName();
			}
			return null;
		} catch (Exception e) {
			throw e;
		}

	}

	private void loadAssignmentView() {
		try {
			this.controller = new AssignmentController();
			this.teacherList = this.controller.loadSedUserByRole(ISedRole.TEACHER);
			this.gradeTmpList = getGradeList();
			this.assignment = new Assignment();
			this.assignmentFilter = new Assignment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 31/7/2014 21:38:01 */
	public void loadDeleteMessage() {
		try {
			if (this.eventDelete)
				addInfoMessage("Eliminar Asignación", "La asignación se eliminó exitosamente.");
			else
				addErrorMessage("Eliminar Asignación",
					"Ocurrió un error y no fue posible eliminar la asignación. Consulte al administrador del sistema.");

			this.eventDelete = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 31/7/2014 21:36:19 */
	public void deleteAssignment() {
		try {
			this.eventDelete = false;
			if (this.assignment != null && this.assignment.getId() != null) {
				if (this.controller.deleteTeacherAssignment(this.assignment.getId(), getUserSession() != null ? getUserSession().getIdentification()
					: "admin")) {
					this.eventDelete = true;
					for (ScheduleEvent se : this.model.getEvents()) {
						Assignment a = (Assignment) se.getData();
						if (a.getId().equals(this.assignment.getId())) {
							this.model.deleteEvent(se);
							break;
						}

					}
					this.assignment = null;
					this.assignment = new Assignment();
				} else
					this.eventDelete = false;


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 30/7/2014 23:16:57 */
	private void validateResizeMove() throws Exception {
		try {
			this.warnResizeEvent = -1;
			Calendar day = Calendar.getInstance();
			day.setTime(this.event.getStartDate());
			this.idDay = Long.valueOf(day.get(Calendar.DAY_OF_WEEK));

			Calendar calStartHour = Calendar.getInstance();
			calStartHour.setTime(this.event.getStartDate());
			calStartHour.set(Calendar.SECOND, 0);

			Calendar calEndHour = Calendar.getInstance();
			calEndHour.setTime(this.event.getEndDate());
			calEndHour.set(Calendar.SECOND, 0);

			this.assignStartDate = calStartHour.getTime();
			this.assignEndDate = calEndHour.getTime();


			this.assignment = (Assignment) this.event.getData();

			for (ScheduleEvent se : this.model.getEvents()) {
				Assignment a = (Assignment) se.getData();
				if (a.getId().equals(this.assignment.getId())) {
					this.model.deleteEvent(se);
					break;
				}
			}
			Assignment assignmentCopy = this.assignment.clone();

			if (validAvailability()) {
				this.assignment.setIdDay(Long.valueOf(this.idDay));


				this.assignment.setStartHour(ManageDate.formatDate(this.event.getStartDate(), ManageDate.HH_MM_SS_24));
				this.assignment.setEndHour(ManageDate.formatDate(this.event.getEndDate(), ManageDate.HH_MM_SS_24));
				this.assignment.setDateChange(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
				this.assignment.setUserChange(getUserSession().getIdentification());

				this.controller.saveTeacherAssignment(this.assignment);
				this.warnResizeEvent = 1;

				DefaultScheduleEvent dse =
					new DefaultScheduleEvent(this.assignment.getTeacherFullName() + " " + this.assignment.getSubjectName() + " "
						+ this.assignment.getCourseName(), this.assignStartDate, this.assignEndDate, this.assignment.getSubjectStyleClass());
				dse.setData(this.assignment);
				this.model.addEvent(dse);

			} else {

				Date startHour = ManageDate.stringToDate(assignmentCopy.getStartHour(), ManageDate.HH_MM_SS_24);
				Date endHour = ManageDate.stringToDate(assignmentCopy.getEndHour(), ManageDate.HH_MM_SS_24);

				Calendar startCal = Calendar.getInstance();
				startCal.setTime(startHour);
				startCal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(assignmentCopy.getIdDay().toString()));

				Calendar endCal = Calendar.getInstance();
				endCal.setTime(endHour);
				endCal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(assignmentCopy.getIdDay().toString()));

				DefaultScheduleEvent dse =
					new DefaultScheduleEvent(assignmentCopy.getTeacherFullName() + " " + assignmentCopy.getSubjectName() + " "
						+ assignmentCopy.getCourseName(), startCal.getTime(), endCal.getTime(), assignmentCopy.getSubjectStyleClass());
				dse.setData(assignmentCopy);
				this.model.addEvent(dse);
				this.warnResizeEvent = 0;
			}
		} catch (Exception e) {
			throw e;
		}

	}

	/** @author MTorres 30/7/2014 23:16:46 */
	public void onEventMove(ScheduleEntryMoveEvent event) {
		try {
			this.warnResizeEvent = -1;
			if (event == null) {
				addErrorMessage("Mover Asignación",
					"Ocurrió un problema y no fue posible moverl la asignación, por favor consulte con el administrador del sistema.");
				return;
			}
			this.event = event.getScheduleEvent();
			validateResizeMove();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 30/7/2014 23:16:39 */
	public void onEventResize(ScheduleEntryResizeEvent event) {
		try {
			this.warnResizeEvent = -1;
			if (event == null) {
				addErrorMessage("Mover Asignación",
					"Ocurrió un problema y no fue posible moverl la asignación, por favor consulte con el administrador del sistema.");
				return;
			}
			this.event = event.getScheduleEvent();
			validateResizeMove();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 30/7/2014 21:19:42 */
	public void loadEventResizeWarn() throws Exception {
		try {
			switch (this.warnResizeEvent) {
				case 0:
					addWarnMessage("Mover Asignación",
						"No fue posible mover la asignación por cruce de horario o disponibilidad docente. Por favor verifique.");
				break;
				case 1:
					addInfoMessage("Mover Asignación", "La asignación se ha movido exitosamente.");
				break;
				default:
				break;
			}
			this.warnResizeEvent = -1;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 30/7/2014 23:16:19 */
	public void onEventSelect(SelectEvent selectEvent) {
		try {
			this.eventAdd = false;
			this.event = (ScheduleEvent) selectEvent.getObject();

			if (this.event == null)
				return;

			this.assignment = (Assignment) this.event.getData();

			if (this.assignment != null) {

				this.assignStartDate = this.event.getStartDate();
				this.assignEndDate = this.event.getEndDate();

				Calendar selectDate = Calendar.getInstance();
				selectDate.setTime(this.assignStartDate);

				this.idDay = Long.valueOf(selectDate.get(Calendar.DAY_OF_WEEK));
				handleGradeChange(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goAddAssignment() {
		try {
			this.eventAdd = true;
			this.startDate = Calendar.getInstance();
			this.startDate.set(Calendar.MINUTE, 0);
			this.idDay = Long.valueOf(this.startDate.get(Calendar.DAY_OF_WEEK));
			this.endDate = Calendar.getInstance();
			this.endDate.set(Calendar.MINUTE, 0);
			this.endDate.set(Calendar.HOUR_OF_DAY, this.startDate.get(Calendar.HOUR_OF_DAY) + 2);

			this.assignStartDate = this.startDate.getTime();
			this.assignEndDate = this.endDate.getTime();

			this.assignment = null;
			this.assignment = new Assignment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 28/7/2014 22:06:23 */
	public void onDateSelect(SelectEvent selectEvent) {
		try {
			this.eventAdd = true;

			this.event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
			if (this.event != null) {

				this.startDate = Calendar.getInstance();
				this.startDate.setTime(this.event.getStartDate());

				this.idDay = Long.valueOf(this.startDate.get(Calendar.DAY_OF_WEEK));

				this.endDate = Calendar.getInstance();
				this.endDate.setTime(this.event.getStartDate());
				this.endDate.set(Calendar.HOUR_OF_DAY, this.startDate.get(Calendar.HOUR_OF_DAY) + 2);

				this.assignStartDate = this.startDate.getTime();
				this.assignEndDate = this.endDate.getTime();

				this.assignment = null;
				this.assignment = new Assignment();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 22/7/2014 22:10:48 */
	public boolean handleAssignDateChange() throws Exception {
		try {
			if (this.assignStartDate != null && this.assignEndDate != null) {

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

				if (this.startDate == null)
					this.startDate = Calendar.getInstance();
				if (this.endDate == null)
					this.endDate = Calendar.getInstance();

				this.startDate.set(Calendar.MINUTE, calStartDate.get(Calendar.MINUTE));
				this.startDate.set(Calendar.HOUR_OF_DAY, calStartDate.get(Calendar.HOUR_OF_DAY));
				this.startDate.set(Calendar.SECOND, 0);
				this.endDate.set(Calendar.MINUTE, calEndDate.get(Calendar.MINUTE));
				this.endDate.set(Calendar.HOUR_OF_DAY, calEndDate.get(Calendar.HOUR_OF_DAY));
				this.endDate.set(Calendar.SECOND, 0);

				if (calStartDate.equals(calEndDate)) {
					addWarnMessage("Programar Docente", "Las horas de inicio y fin no pueden ser identicas, por favor verifique.");
					return false;
				} else if (calStartDate.after(calEndDate)) {
					addWarnMessage("Programar Docente", "Error en las horas seleccionadas por favor verifique.");
					return false;
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres 28/7/2014 23:36:42 */
	public void filterScheduleOption() {
		try {
			if (this.sheduleOption != null && !this.sheduleOption.equals(0L) && this.assignmentFilter != null) {
				this.model = null;

				switch (this.sheduleOption.intValue()) {
					case 1:
						if (this.assignmentFilter.getIdSedUser() != null && !this.assignmentFilter.getIdSedUser().equals(0L)) {
							this.assignmentList = this.controller.loadAssignmentListByTeacher(this.assignmentFilter.getIdSedUser());
							if (this.assignmentList == null || this.assignmentList.isEmpty())
								addWarnMessage("Calendario académico", "El docente seleccionado no tiene asignación hasta el momento.");
							else
								loadScheduleData();
						}
					break;
					case 2:
						if (this.assignmentFilter.getIdGrade() != null && !this.assignmentFilter.getIdGrade().equals(0L)
							&& this.assignmentFilter.getIdCourse() != null && !this.assignmentFilter.getIdCourse().equals(0L)) {
							this.assignmentList = this.controller.loadAssignmentListByCourse(this.assignmentFilter.getIdCourse());
							if (this.assignmentList == null || this.assignmentList.isEmpty())
								addWarnMessage("Calendario académico", "El curso seleccionado no posee aún materias ni docentes asignados.");
							else
								loadScheduleData();
						}
					break;

					default:
					break;
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
				if (this.assignmentFilter.getIdGrade() != null && !this.assignmentFilter.getIdGrade().equals(0L))
					this.courseTmpList = loadCourseListByGrade(this.assignmentFilter.getIdGrade());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** @author MTorres 28/7/2014 23:33:55 */
	public void cleanFilterParameter() {
		try {
			this.assignmentFilter = null;
			this.assignmentFilter = new Assignment();
		} catch (Exception e) {

		}
	}

	/** @author MTorres 22/7/2014 23:16:45 */
	private void clearSelectDateDialog() throws Exception {
		try {
			this.assignment = null;
			this.startDate = null;
			this.endDate = null;
			this.subjectList = null;
			this.idDay = null;
			this.courseTmpList = null;
			this.eventAdd = false;
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean getValidateSedUserRole() throws Exception {
		try {
			if (getUserSession() != null) {
				if (getUserSession().getIdSedRoleUser().equals(ISedRole.ADMINISTRATOR)
					|| getUserSession().getIdSedRoleUser().equals(ISedRole.STUDENT)
					|| getUserSession().getIdSedRoleUser().equals(ISedRole.STUDENT_RESPONSIBLE)
					|| getUserSession().getIdSedRoleUser().equals(ISedRole.TEACHER))
					return true;
				else
					return false;
			} else
				return false;
		} catch (Exception e) {
			throw e;
		}
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

	public Long getIdDay() {
		return idDay;
	}

	public void setIdDay(Long idDay) {
		this.idDay = idDay;
	}

	public Assignment getAssignmentFilter() {
		return assignmentFilter;
	}

	public void setAssignmentFilter(Assignment assignmentFilter) {
		this.assignmentFilter = assignmentFilter;
	}

	public boolean isEventAdd() {
		return eventAdd;
	}

	public void setEventAdd(boolean eventAdd) {
		this.eventAdd = eventAdd;
	}
}
