package co.edu.udistrital.sed.qualification.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.poi.ss.usermodel.BorderFormatting;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.controller.ErrorNotificacion;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.model.StudentQualification;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "stQualification", pattern = "/portal/calificaciones", viewId = "/pages/student/qualification/qualification.jspx")
public class StudentQualificationBean extends BackingBean {

	// Primitives
	private int activeIndex;

	private Long idStudent;

	// User List
	List<StudentQualification> studentQualificationList;
	List<StudentQualification> studentQualificationFilterList;
	List<Student> studentResponsibleList;

	// User Object
	private Student student;
	private Student studentSelected;
	// Controller Object
	private StudentQualificationController controller;

	public StudentQualificationBean() throws Exception {
		try {
			this.controller = new StudentQualificationController();
			if (getUserSession().getIdSedRole().equals(ISedRole.STUDENT)) {
				this.student = this.controller.loadStudent(getUserSession().getId());
				this.activeIndex = 0;
				this.idStudent = this.student.getId();
				handleTabChange();
			} else if (getUserSession().getIdSedRole().equals(ISedRole.STUDENT_RESPONSIBLE)) {
				this.studentResponsibleList = this.controller.loadStudentResponsibleList(getUserSession().getIdSedUser());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 7/8/2014 22:31:47 */
	public void loadQualificationList() {
		try {
			this.student = null;
			if (this.idStudent != null) {
				Long idSedUser = null;
				for (Student s : this.studentResponsibleList) {
					if (s.getId().equals(this.idStudent)) {
						idSedUser = s.getIdSedUser();
						break;
					}

				}
				this.student = this.controller.loadStudent(idSedUser);
				handleTabChange();
			}

		} catch (Exception e) {
			ErrorNotificacion.handleErrorMailNotification(e, this);
		}
	}

	/** @author MTorres 7/06/2014 */
	public void handleTabChange() {
		try {
			List<Qualification> qualificationStudentList = null;
			this.studentQualificationList = null;
			this.studentQualificationFilterList = null;
			switch (this.activeIndex) {
				case 0:
					Calendar c = Calendar.getInstance();
					qualificationStudentList = this.controller.loadQualificationListByStudent(this.idStudent, c.get(Calendar.YEAR));
				break;
				case 1:
					qualificationStudentList = this.controller.loadQualificationHistoricalList(this.idStudent);
				break;
				default:
				break;
			}
			if (qualificationStudentList != null && !qualificationStudentList.isEmpty())
				orderQualificationList(qualificationStudentList);
			else
				addInfoMessage(getMessage("page.qualification.labelQualification"), getMessage("page.qualification.sq.labelEmptyQualification"));
		} catch (Exception e) {
			ErrorNotificacion.handleErrorMailNotification(e, this);
		}
	}

	/** @author MTorres 7/06/2014 */
	private void orderQualificationList(List<Qualification> qualificationList) throws Exception {
		try {
			if (qualificationList != null && !qualificationList.isEmpty()) {

				Long idKa = Long.valueOf(0);
				Long idSubject = Long.valueOf(0);
				String kaName = null;
				String subjectName = null;
				List<Qualification> qualificationStdList = new ArrayList<Qualification>(getQualificationTypeList().size());
				this.studentQualificationList = new ArrayList<StudentQualification>();

				Long idPeriod = 0L;

				for (Qualification q : qualificationList) {

					Qualification qs = new Qualification();
					qs.setValue(q.getValue());
					qs.setIdQualificationType(q.getIdQualificationType());
					idPeriod =
						q.getIdPeriod() != null && !q.getIdPeriod().equals(0L) ? q.getIdPeriod() : Long.valueOf(Calendar.getInstance().get(
							Calendar.YEAR));
					qs.setIdPeriod(idPeriod);


					if (idSubject.equals(0L)) {
						idSubject = q.getIdSubject();
					}

					if (idSubject.equals(q.getIdSubject())) {
						qualificationStdList.add(qs);
					} else if (!idSubject.equals(q.getIdSubject())) {
						this.studentQualificationList.add(new StudentQualification(kaName, subjectName, qualificationStdList, qs.getIdPeriod()));
						idSubject = q.getIdSubject();
						qualificationStdList = null;
						qualificationStdList = new ArrayList<Qualification>(getQualificationTypeList().size());
						qualificationStdList.add(qs);
					}

					kaName = q.getKnowledgeAreaName();
					subjectName = q.getSubjectName();


					if (idKa.equals(0L)) {
						idKa = q.getIdKnowledgeArea();
					} else if (!idKa.equals(q.getIdKnowledgeArea())) {
						idKa = q.getIdKnowledgeArea();
						idSubject = Long.valueOf(0L);
					}

				}

				if (qualificationStdList != null && !qualificationStdList.isEmpty())
					this.studentQualificationList.add(new StudentQualification(kaName, subjectName, qualificationStdList, idPeriod));

				for (StudentQualification sq : this.studentQualificationList) {
					int countMissing = 0;
					if (sq.getQualificationList().size() != getQualificationTypeList().size())
						countMissing = getQualificationTypeList().size() - sq.getQualificationList().size();

					for (int i = 0; i < countMissing; i++) {
						Qualification mq = new Qualification();
						sq.getQualificationList().add(mq);
					}
				}
				this.studentQualificationFilterList = this.studentQualificationList;
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public void handleDataExporter(Object o) {
		try {
			Workbook wb = (Workbook) o;
			wb.setSheetName(0, "Reporte");
			CellStyle bs = wb.createCellStyle();
			bs.setBorderBottom(BorderFormatting.BORDER_THIN);
			bs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			bs.setBorderTop(BorderFormatting.BORDER_THIN);
			bs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			bs.setBorderLeft(BorderFormatting.BORDER_THIN);
			bs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			bs.setBorderRight(BorderFormatting.BORDER_THIN);
			bs.setRightBorderColor(IndexedColors.BLACK.getIndex());
			CellStyle hs = wb.createCellStyle();
			hs.cloneStyleFrom(bs);
			hs.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			hs.setFillPattern(CellStyle.SOLID_FOREGROUND);

			for (Row r : wb.getSheetAt(0)) {
				for (Cell c : r) {
					if (c.getRowIndex() == 0)
						c.setCellStyle(hs);
					else
						c.setCellStyle(bs);
				}
			}

			int maxIndex = getQualificationTypeList().size() + 2;

			for (int i = 0; i < maxIndex; i++) {
				wb.getSheetAt(0).autoSizeColumn(i);
			}

		} catch (Exception e) {
			ErrorNotificacion.handleErrorMailNotification(e, this);
		}

	}

	/** @author MTorres 9/8/2014 12:12:39 */
	public boolean getValidateSedUserRole() throws Exception {
		if (getUserSession() != null) {
			if (getUserSession().getIdSedRole().equals(ISedRole.STUDENT) || getUserSession().getIdSedRole().equals(ISedRole.STUDENT_RESPONSIBLE))
				return true;
			else
				return false;
		}
		return false;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<StudentQualification> getStudentQualificationList() {
		return studentQualificationList;
	}

	public void setStudentQualificationList(List<StudentQualification> studentQualificationList) {
		this.studentQualificationList = studentQualificationList;
	}

	public List<StudentQualification> getStudentQualificationFilterList() {
		return studentQualificationFilterList;
	}

	public void setStudentQualificationFilterList(List<StudentQualification> studentQualificationFilterList) {
		this.studentQualificationFilterList = studentQualificationFilterList;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public List<Student> getStudentResponsibleList() {
		return studentResponsibleList;
	}

	public void setStudentResponsibleList(List<Student> studentResponsibleList) {
		this.studentResponsibleList = studentResponsibleList;
	}

	public Student getStudentSelected() {
		return studentSelected;
	}

	public void setStudentSelected(Student studentSelected) {
		this.studentSelected = studentSelected;
	}

	public Long getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(Long idStudent) {
		this.idStudent = idStudent;
	}

}
