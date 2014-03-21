package co.edu.udistrital.sed.student.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.controller.BackingBean;

@ManagedBean
@ViewScoped
@URLMapping(id = "studentBean", pattern = "/portal/estudiante", viewId = "/pages/student/student.jspx")
public class StudentBean extends BackingBean{

	
	private static final long serialVersionUID = -8160892651775048022L;
	private boolean showList = false, showAdd = false, showDetail = false;  
	private Long miDato;
	
	public StudentBean() {
		try {
			setShowList(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void cargarFuncion(){
		try {
			System.out.println("Ingresando....."+this.miDato);
			if(!this.miDato.equals(0L)){
				addInfoMessage("Agregar", "Usted eligio  "+this.miDato);
			}else{
				addErrorMessage("Agregar", "Por favor seleccioneun valor");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Long getMiDato() {
		return miDato;
	}

	public void setMiDato(Long miDato) {
		this.miDato = miDato;
	}
	public boolean isShowList() {
		return showList;
	}
	public void setShowList(boolean showList) {
		this.showList = showList;
	}
	public boolean isShowAdd() {
		return showAdd;
	}
	public void setShowAdd(boolean showAdd) {
		this.showAdd = showAdd;
	}
	public boolean isShowDetail() {
		return showDetail;
	}
	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}
	
	
	
}
