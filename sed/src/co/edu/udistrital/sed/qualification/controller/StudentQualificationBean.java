package co.edu.udistrital.sed.qualification.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.controller.BackingBean;

@ManagedBean
@ViewScoped
@URLMapping(id = "stQualification", pattern = "/portal/calificaciones", viewId = "/pages/student/qualification/qualification.jspx")
public class StudentQualificationBean extends BackingBean {
	
	private int activeIndex;

	// Controller Object
	private StudentQualificationController controller;

	public StudentQualificationBean() {
		try {
			this.controller = new StudentQualificationController();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void handleTabChange(){
		try {
			
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
	

}
