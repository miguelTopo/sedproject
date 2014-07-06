package co.edu.udistrital.sed.assignment.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import co.edu.udistrital.core.common.controller.BackingBean;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "assignmentBean", pattern = "/portal/cargaAcademica", viewId = "/pages/assignment/assignment.jspx")
public class AssignmentBean extends BackingBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7388806594985147701L;

	private boolean showAssigment;

	private ScheduleModel model;

	public AssignmentBean() throws Exception{
		try {
			this.model= new DefaultScheduleModel();
			
		
			setShowAssigment(true);
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean getValidateSedUserRole() throws Exception {

		return false;
	}

	// ////////----------getters && setters ----------//////////

	public boolean isShowAssigment() {
		return showAssigment;
	}

	public void setShowAssigment(boolean showAssigment) {
		this.showAssigment = showAssigment;
	}

	public ScheduleModel getModel() {
		return model;
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}
}
