package co.edu.udistrital.sed.report.controller;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.excel.ManageExcel;
import co.edu.udistrital.core.common.util.NumberFormatter;
import co.edu.udistrital.sed.api.ICourt;
import co.edu.udistrital.sed.api.IGrade;
import co.edu.udistrital.sed.api.IQualificationType;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Qualification;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.report.api.IReport;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@SessionScoped
@URLMapping(id = "addReport", pattern = "/portal/reporte", viewId = "/pages/report/report.jspx")
public class ReportBean extends BackingBean implements IReport, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6964596552007842254L;

	// Primitives
	private boolean showAdd = false, showDownloadFile = false;
	private boolean fileError = false;
	private int invalidStudent;

	// Java Object
	private Long idGrade;
	private Long idSelectedGrade;
	private String studentName;
	private String studentIdentification;

	// Java Object List
	private List<String> basicInformation;
	private List<Subject> subjectListByGrade;

	// POI
	private Workbook wbDegree;

	// Primefaces
	private StreamedContent file;

	// UserList
	private List<Student> properStudentList;
	private List<Student> invalidStudentList;
	private List<Student> totalStudentList;
	private List<Course> tmpCourseList;

	// User Object
	private Student student;
	private Qualification qualification;

	// Controller
	private ReportController controller;

	public ReportBean() {
		try {
			this.controller = new ReportController();
			setShowAdd(true);
			setShowDownloadFile(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public boolean getValidateSedUserRole() throws Exception {
		try {
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void downloadErrorFile() {
		try {
			System.out.println("vamos a descargar archivo de errores....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void handleFileUpload(FileUploadEvent event) {
		try {
			System.out.println("ingresando ");
			if (this.idSelectedGrade == null || this.idSelectedGrade.equals(0L)) {
				addWarnMessage("Agregar Archivo",
					"Para poder subir el archivo es necesario que indique el grado al cual quiere subir la información.");
				return;
			} else {
				this.tmpCourseList = loadCourseListByGrade(this.idSelectedGrade);
			}

			if (event != null) {
				loadSubjectList(this.idSelectedGrade);

				UploadedFile inputFile = event.getFile();
				if (inputFile != null) {

					// Verificacion de tipo de Excel xls o xlsx

					if (inputFile.getFileName().endsWith(".xls"))
						this.wbDegree = ManageExcel.parseXLSFile(inputFile.getInputstream());
					else if (inputFile.getFileName().endsWith(".xlsx"))
						this.wbDegree = ManageExcel.parseXLSXFile(inputFile.getInputstream());

					// Procesar Archivo Excel en busca de errores de inserción
					if (this.wbDegree != null) {
						processDegreeFile();

						if (this.totalStudentList != null && this.properStudentList != null
							&& (this.totalStudentList.size() == this.properStudentList.size())) {

							this.totalStudentList = null;
							List<Course> courseStudentList = loadCourseListByGrade(this.idSelectedGrade);
							List<Student> studentGradeList = this.controller.loadStudentListByGrade(courseStudentList);

							// reeemplazar numero de documento de estudiantes con id en la BD
							replaceIdentificationId(studentGradeList);

							if (this.controller.saveCalificationList(this.properStudentList)) {
								addInfoMessage("Cargar archivo", "El archivo fué leido y almacenado en la base de datos correctamente.");
							}

						} else {
							setFileError(true);
							addWarnMessage("Cargar archivo",
								"Se analizó el archivo y se encontraron algunas fallas, por favor verifique el archivo e intente nuevamente.");
							return;
						}
						cleanVarList();
					} else {
						addFatalMessage("Subir Archivo",
							"Se ha producido un error al tratar de subir el archivo. Por favor consulte al administrador del sistema");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void replaceIdentificationId(List<Student> studentGradeList) {
		try {
			for (Student s : this.properStudentList) {
				for (Student std : studentGradeList) {
					System.out.println(std.getIdentification());
					System.out.println(s.getIdentification());
					if (std.getIdentification().trim().equals(s.getIdentification().trim())) {
						s.setId(std.getId());
						for (Qualification q : s.getQualificationList()) {
							q.setIdStudentCourse(std.getIdStudentCourse());
						}
						break;
						// studentGradeList.remove(std);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** @author MTorres */
	private void loadSubjectList(Long idGrade) {
		try {
			this.subjectListByGrade = loadSubjectListByGrade(idGrade);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** @author MTorres */
	public boolean getDataBasicDegreeFile(int indexSheet) {
		try {
			Sheet sheet = this.wbDegree.getSheetAt(indexSheet);
			this.basicInformation = new ArrayList<String>(IReport.CELL_REFERENCE_LIST.size());
			for (String s : IReport.CELL_REFERENCE_LIST) {
				CellReference cr = new CellReference(s);
				Row row = sheet.getRow(cr.getRow());
				Cell cell = row.getCell(cr.getCol());

				if (cell != null) {
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							if (!cell.getRichStringCellValue().getString().isEmpty())
								basicInformation.add(cell.getRichStringCellValue().getString());
							else
								return false;
						break;
						case Cell.CELL_TYPE_NUMERIC:
							basicInformation.add(String.valueOf(cell.getNumericCellValue()));
						break;
						case Cell.CELL_TYPE_FORMULA:
							basicInformation.add(cell.getStringCellValue());
						break;
						default:
						break;
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	private void readXLSFile(int indexSheet) {
		try {
			this.student = new Student();
			Iterator<Row> ri = this.wbDegree.getSheetAt(indexSheet).rowIterator();

			System.out.println("start process file " + System.currentTimeMillis());
			ri.next();
			ri.next();
			ri.next();
			ri.next();
			ri.next();
			ri.next();
			ri.next();
			ri.next();
			ri.next();
			int count = 0;
			// Bucle de control de filas
			while (ri != null && ri.hasNext()) {


				if (this.student != null && count > 0)
					if (this.student.getInvalidColumn() != null && this.student.getInvalidColumn().isEmpty())
						addStudent(indexSheet);
					else {
						getInvalidStudentList().add(this.student);
						break;
					}

				else if (this.student == null && count > 0) {
					break;
				}

				Row r = (Row) ri.next();

				Iterator<Cell> ci = r.cellIterator();

				int i = 0;
				this.student = new Student();

				int countSave = 0;
				int indexSubject = 0;
				this.qualification = new Qualification();
				// Bucle de control de celdas/columnas
				for (Iterator<Cell> iterator = ci; iterator.hasNext(); i++) {
					Cell c = iterator.next();
					if (i == 1 || i == 13 || i >= 27) {

						if (i == 53)
							System.out.println("aqui vamos a revisar muy bien");

						// if (i == 27 || i == 31 || i == 35 || i == 39 || i == 43 || i == 47 || i
						// == 51 || i == 55 || i == 59 || i == 63 || i == 67
						// || i == 71 || i == 47 || i == 75)


						if (i > 27 && this.student != null) {
							if (countSave++ == 4) {
								indexSubject++;
								countSave = 0;
							}
							addStudentQualification(indexSubject);
						}
						this.qualification = new Qualification();
						String value = null;
						Double doubleValue = null;

						switch (c.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:
								doubleValue = c.getNumericCellValue();
								System.out.println(doubleValue);
							break;
							case Cell.CELL_TYPE_STRING:
								value = c.getStringCellValue();
								System.out.println(value);
							break;
							case Cell.CELL_TYPE_BOOLEAN:
								System.out.println("estamos en boolean ");
							break;
							case Cell.CELL_TYPE_FORMULA:
								System.out.println("estamos en formula");
							break;
							default:
							break;
						}
						if (value != null || doubleValue != null) {
							validateInsertRow(value, doubleValue, i);
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void processDegreeFile() {
		try {
			for (int j = 0; j < this.wbDegree.getNumberOfSheets(); j++) {
				if (getDataBasicDegreeFile(j)) {
					readXLSFile(j);
				} else {
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	private void addStudentQualification(int indexSubject) {
		try {
			if (this.qualification != null && this.student != null)
				if (this.qualification.getValue() != null) {
					this.qualification.setIdSubject(subjectListByGrade.get(indexSubject).getId());
					this.student.getQualificationList().add(this.qualification);
				}


			this.qualification = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	private void addStudent(int indexSheet) {
		try {
			if (this.student.getIdentification() != null && !this.student.getIdentification().trim().isEmpty()) {
				getTotalStudentList().add(this.student);
				if (this.student.getInvalidColumn() != null && this.student.getInvalidColumn().isEmpty())
					getProperStudentList().add(this.student);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	/** @author MTorres */
	public void validateInsertRow(String stringValue, Double numericValue, int column) {
		try {
			boolean validCell = false;
			if (column == 2 || column == 13) {
				validCell = (stringValue != null && !stringValue.trim().isEmpty()) ? true : false;
			} else {
				validCell = (numericValue != null) ? true : false;
			}
			if (validCell) {
				switch (column) {
					case 1:
						System.out.println("columna 1");
						this.studentIdentification = NumberFormatter.parseDoubleToString(numericValue, NumberFormatter.FORMAT_0);
						System.out.println(this.studentIdentification);
						this.student.setIdentification(this.studentIdentification);
					break;
					case 13:
						this.studentName = stringValue;
						this.student.setName(this.studentName);

						System.out.println("columna 2 puesto");
					break;
					case 27:
					case 31:
					case 35:
					case 39:
					case 43:
					case 47:
					case 51:
					case 55:
					case 59:
					case 63:
					case 67:
					case 71:
					case 75:
						System.out.println("Notas correspondientes a corte 1");
						if (validateNote(numericValue, ICourt.FIRST_COURT)) {
							this.qualification.setIdQualificationType(IQualificationType.C1);
							this.qualification.setValue(numericValue);
						} else
							this.student.getInvalidColumn().add(new Integer(column));
					break;
					case 28:
					case 32:
					case 36:
					case 40:
					case 44:
					case 48:
					case 52:
					case 56:
					case 60:
					case 64:
					case 68:
					case 72:
					case 76:
						System.out.println("Notas correspondientes a corte 2");
						if (validateNote(numericValue, ICourt.SECOND_COURT)) {
							this.qualification.setIdQualificationType(IQualificationType.C2);
							this.qualification.setValue(numericValue);
						} else
							this.student.getInvalidColumn().add(new Integer(column));
					break;
					case 29:
					case 33:
					case 37:
					case 41:
					case 45:
					case 49:
					case 53:
					case 57:
					case 61:
					case 65:
					case 69:
					case 73:
					case 77:
						System.out.println("Notas correspondientes a corte 3");
						if (validateNote(numericValue, ICourt.THIRD_COURT)) {
							this.qualification.setIdQualificationType(IQualificationType.C3);
							this.qualification.setValue(numericValue);
						} else
							this.student.getInvalidColumn().add(new Integer(column));
					break;
					case 30:
					case 34:
					case 38:
					case 42:
					case 46:
					case 50:
					case 54:
					case 58:
					case 62:
					case 66:
					case 70:
					case 74:
					case 78:
						if (validateNote(numericValue, ICourt.THIRD_COURT))
							// this.qualification.setCf(numericValue);
							// else
							this.student.getInvalidColumn().add(new Integer(column));
					break;

					default:
					break;
				}
			} else {
				this.student.getInvalidColumn().add(new Integer(column));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** @author MTorres */
	public void showDownloadFilePanel() {
		try {
			setShowDownloadFile(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public boolean validateNote(Double numericValue, Long idCourt) {
		try {
			if (numericValue != null) {
				if (idCourt.equals(ICourt.FIRST_COURT)) {
					return (numericValue >= 80 && numericValue <= 100) ? true : false;
				} else if (idCourt.equals(ICourt.SECOND_COURT)) {
					return (numericValue >= 60 && numericValue <= 100) ? true : false;
				} else if (idCourt.equals(ICourt.THIRD_COURT)) {
					return (numericValue >= 30 && numericValue <= 100) ? true : false;
				} else if (idCourt.equals(ICourt.FINAL_COURT)) {
					return (numericValue >= 0 && numericValue <= 100) ? true : false;
				} else
					return false;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	private void cleanVarList() {
		try {
			this.properStudentList = this.totalStudentList = null;
			this.idGrade = null;
			this.idSelectedGrade = null;
			this.student = null;
			this.studentIdentification = null;
			this.studentName = null;
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean isShowAdd() {
		return showAdd;
	}

	public void setShowAdd(boolean showAdd) {
		this.showAdd = showAdd;
	}

	public boolean isShowDownloadFile() {
		return showDownloadFile;
	}

	public void setShowDownloadFile(boolean showDownloadFile) {
		this.showDownloadFile = showDownloadFile;
	}

	public StreamedContent getFile() {
		try {
			String fileName = "";
			String fileOutputName = "";
			if (this.idGrade != null) {
				if (this.idGrade.equals(0L)) {
					addWarnMessage("Descagar archivo guia", "Por favor seleccione un curso para descargar el archivo correspondiente");
					return null;
				} else if (this.idGrade.equals(IGrade.SIXTH_GRADE)) {
					fileName = "frmSixth.xlsx";
					fileOutputName = "plantilla-grado-sexto.xlsx";
				}

			}
			file = new DefaultStreamedContent(this.getClass().getResourceAsStream(fileName), "application/xls", fileOutputName);
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public Long getIdGrade() {
		return idGrade;
	}

	public void setIdGrade(Long idGrade) {
		this.idGrade = idGrade;
	}

	public List<Student> getProperStudentList() {
		if (this.properStudentList == null)
			this.properStudentList = new ArrayList<Student>();
		return this.properStudentList;
	}

	public void setProperStudentList(List<Student> properStudentList) {
		this.properStudentList = properStudentList;
	}

	public List<Student> getTotalStudentList() {
		if (this.totalStudentList == null)
			this.totalStudentList = new ArrayList<Student>();
		return this.totalStudentList;
	}

	public void setTotalStudentList(List<Student> totalStudentList) {
		this.totalStudentList = totalStudentList;
	}

	public Long getIdSelectedGrade() {
		return idSelectedGrade;
	}

	public void setIdSelectedGrade(Long idSelectedGrade) {
		this.idSelectedGrade = idSelectedGrade;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

	public List<Student> getInvalidStudentList() {
		if (this.invalidStudentList == null)
			this.invalidStudentList = new ArrayList<Student>();
		return invalidStudentList;
	}

	public void setInvalidStudentList(List<Student> invalidStudentList) {
		this.invalidStudentList = invalidStudentList;
	}



}
