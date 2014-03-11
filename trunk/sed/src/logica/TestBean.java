package logica;

import java.io.File;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class TestBean {
	
	public TestBean() {
		
	}
	
	public void handleFileUpload(FileUploadEvent event){
		try {
			System.out.println("aqui paso algo");
			UploadedFile file = event.getFile();
			Workbook wb = null;
			if(file.getFileName().endsWith(".xlsx"))
			 wb = WorkbookFactory.create(file.getInputstream());
			else if(file.getFileName().endsWith(".xls"))
				wb = new XSSFWorkbook(file.getInputstream()); 
			if(wb!=null)
			System.out.println("Workbook dej{o de ser nulo ");
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
