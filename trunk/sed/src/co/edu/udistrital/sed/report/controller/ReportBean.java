package co.edu.udistrital.sed.report.controller;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
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

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.core.common.excel.ManageExcel;
import co.edu.udistrital.sed.model.Student;
import co.edu.udistrital.sed.report.api.IGrade;
import co.edu.udistrital.sed.report.api.IReport;

@ManagedBean
@ViewScoped
@URLMapping(id = "addReport", pattern = "/portal/reporte", viewId = "/pages/report/report.jspx")
public class ReportBean extends BackingBean implements IReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6964596552007842254L;

	private boolean showAdd = false, showDownloadFile = false;

	private Long idGrade;

	private List<String> basicInformation;

	// POI
	private Workbook wbDegree;

	// Primefaces
	private StreamedContent file;

	// User
	private Student student;

	public ReportBean() {
		try {
			setShowAdd(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			System.out.println("ingresando ");
			if (event != null) {
				UploadedFile inputFile = event.getFile();
				if (inputFile != null) {
					if (inputFile.getFileName().endsWith(".xls"))
						this.wbDegree = ManageExcel.parseXLSFile(inputFile.getInputstream());
					else if (inputFile.getFileName().endsWith(".xlsx"))
						this.wbDegree = ManageExcel.parseXLSXFile(inputFile.getInputstream());

					if (this.wbDegree != null) {
						getDataBasicDegreeFile();
						processDegreeFile();
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

	public void getDataBasicDegreeFile() {
		try {
			Sheet sheet = this.wbDegree.getSheetAt(0);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processDegreeFile() {
		try {
			// for (int j = 0; j < this.wbDegree.getNumberOfSheets(); j++) {

			Iterator<Row> ri = this.wbDegree.getSheetAt(0).rowIterator();

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
			while (ri != null && ri.hasNext()) {
				Row r = (Row) ri.next();

				Iterator<Cell> ci = r.cellIterator();

				int i = 0;
				this.student = new Student();

				int iteration = 0;

				for (Iterator<Cell> iterator = ci; iterator.hasNext(); i++) {
					Cell c = iterator.next();

					String value = null;
					Double doubleValue = null;
					System.out.println("iteracion" + i);
					if (i >= 13)
						System.out.println("aqui vamos");



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
					}



				}
			}

			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void validateInsertRow(String stringValue, Double numericValue, int column) {
		try {
			boolean validCell = false;
			if (column == 2) {
				validCell = (stringValue != null && !stringValue.trim().isEmpty()) ? true : false;
			} else {
				validCell = (numericValue != null) ? true : false;
			}
			if (validCell) {
				switch (column) {
					case 0:
						System.out.println("columna 0");
					// if (!FieldValidator.isNumeric(numericValue.toString())) {
					// this.student.getInvalidColumn().add(new Integer(column));
					// }
					break;
					case 1:
						System.out.println("columna 1");
						String identification = String.valueOf(numericValue.intValue());
						this.student.setIdentification(identification);

					break;
					case 13:
						this.student.setName(stringValue);
						System.out.println("columna 2 puesto");
					break;
					case 26:
					case 30:
					case 34:
					case 38:
					case 42:
						System.out.println("Notas correspondientes a corte 1");
						if (validateNote(numericValue))
							this.student.getQualification().setP1(numericValue);
					break;
					case 27:
					case 31:
					case 35:
					case 39:
					case 43:
						System.out.println("Notas correspondientes a corte 2");
					break;

					case 28:
					case 32:
					case 36:
					case 40:
					case 44:
						System.out.println("Notas correspondientes a corte 3");
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

	public void showDownloadFilePanel() {
		try {
			setShowDownloadFile(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean validateNote(Double numericValue) {
		try {
			return (numericValue.doubleValue() < 0.0 || numericValue.doubleValue() > 5.0) ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void test() {
		try {
			System.out.println("INgresando carajo");
		} catch (Exception e) {
			e.printStackTrace();
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
}