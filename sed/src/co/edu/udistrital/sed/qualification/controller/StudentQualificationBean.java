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

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.login.api.ISedRole;
import co.edu.udistrital.core.login.model.SedUser;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.model.StudentQualification;

@ManagedBean
@ViewScoped
@URLMapping(id = "stQualification", pattern = "/portal/calificaciones", viewId = "/pages/student/qualification/qualification.jspx")
public class StudentQualificationBean extends BackingBean {

	// Primitives
	private int activeIndex;

	// User List
	List<StudentQualification> studentQualificationList;
	List<StudentQualification> studentQualificationFilterList;

	// User Object
	private Student student;
	// Controller Object
	private StudentQualificationController controller;

	public StudentQualificationBean() throws Exception {
		try {
			if (getUserSession().getIdSedRole().equals(ISedRole.STUDENT)) {
				this.controller = new StudentQualificationController();
				this.student = this.controller.loadStudent(getUserSession().getId());
				this.activeIndex = 0;
				handleTabChange();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 7/06/2014 */
	public void handleTabChange() {
		try {
			List<Qualification> qualificationStudentList = null;
			switch (this.activeIndex) {
				case 0:
					Calendar c = Calendar.getInstance();
					qualificationStudentList = this.controller.loadQualificationListByStudent(getUserSession().getIdStudent(), c.get(Calendar.YEAR));

				break;
				case 1:
					qualificationStudentList = this.controller.loadQualificationListByStudent(getUserSession().getIdStudent(), -1);
				break;
				default:
				break;
			}
			orderQualificationList(qualificationStudentList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 7/06/2014 */
	private void orderQualificationList(List<Qualification> qualificationList) {
		try {
			if (qualificationList != null && !qualificationList.isEmpty()) {

				Long idKa = Long.valueOf(0);
				Long idSubject = Long.valueOf(0);
				String kaName = null;
				String subjectName = null;
				List<Qualification> qualificationStdList = new ArrayList<Qualification>(getQualificationTypeList().size());
				this.studentQualificationList = new ArrayList<StudentQualification>();

				for (Qualification q : qualificationList) {

					Qualification qs = new Qualification();
					qs.setValue(q.getValue());
					qs.setIdQualificationType(q.getIdQualificationType());


					if (idSubject.equals(0L)) {
						idSubject = q.getIdSubject();
					}

					if (idSubject.equals(q.getIdSubject())) {
						qualificationStdList.add(qs);
					} else if (!idSubject.equals(q.getIdSubject())) {
						this.studentQualificationList.add(new StudentQualification(kaName, subjectName, qualificationStdList));
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
					this.studentQualificationList.add(new StudentQualification(kaName, subjectName, qualificationStdList));

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
			e.printStackTrace();
		}
	}

	public void handleDataExporter(Object o) {
		try {
			Workbook wb = (Workbook) o;
			wb.setSheetName(0, "Datos Educacional");
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
			e.printStackTrace();
		}

	}

	public boolean getValidateSedUserRole() throws Exception {
		return true;
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

}
