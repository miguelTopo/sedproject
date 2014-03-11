package co.edu.udistrital.sed.report.api;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.FileUploadEvent;

public interface IReport {

	public static List<String> CELL_REFERENCE_LIST = new ArrayList<String>() {
		{
			add("D3");
			add("D4");
			add("O3");
			add("O4");
			add("AB3");
			add("AB4");
		}
	};
	
	/**@author MTorres Este método permite interactuar con pagina JSF y capturar el archivo XLS */
	public abstract void handleFileUpload(FileUploadEvent event);
	
	/**@author MTorres Este método permite capturar los datos básicos del archivo XLS como Sede, director de grupo y otros*/
	public abstract void getDataBasicDegreeFile();
	
	/**@author MTorres Este método permite procesar el archivo XLS*/
	public abstract void processDegreeFile();
	
}
