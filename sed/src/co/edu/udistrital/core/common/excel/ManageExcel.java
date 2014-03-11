package co.edu.udistrital.core.common.excel;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ManageExcel {
	
	public static Workbook parseXLSFile(InputStream stream) throws Exception{
		try {
			Workbook wb = new XSSFWorkbook(stream);
			return wb;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static Workbook parseXLSXFile(InputStream stream) throws Exception{
		try {
			Workbook wb = WorkbookFactory.create(stream);
			return wb;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
