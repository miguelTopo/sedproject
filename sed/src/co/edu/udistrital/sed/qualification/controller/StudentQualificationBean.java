package co.edu.udistrital.sed.qualification.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.sed.model.Qualification;

@ManagedBean
@ViewScoped
@URLMapping(id = "stQualification", pattern = "/portal/calificaciones", viewId = "/pages/student/qualification/qualification.jspx")
public class StudentQualificationBean extends BackingBean {
	
	//Primitives
	private int activeIndex;
	
	//User List
	private List<Qualification>studentQualificationList;

	// Controller Object
	private StudentQualificationController controller;

	public StudentQualificationBean() throws Exception {
		try {
			this.controller = new StudentQualificationController();
				this.studentQualificationList = this.controller.loadStudentQualificationList(getUserSession().getId(),getUserSession().getIdSedRole());
				
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

	public List<Qualification> getStudentQualificationList() {
		return studentQualificationList;
	}

	public void setStudentQualificationList(List<Qualification> studentQualificationList) {
		this.studentQualificationList = studentQualificationList;
	}
		

}
