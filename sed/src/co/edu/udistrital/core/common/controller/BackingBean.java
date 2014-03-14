package co.edu.udistrital.core.common.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import co.edu.udistrital.core.common.list.BeanList;
import co.edu.udistrital.sed.model.Subject;

public abstract class BackingBean implements Serializable {

	public BackingBean() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addInfoMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, summary,
							detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addWarnMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, summary,
							detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addErrorMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,
							detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addFatalMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, summary,
							detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static FacesContext getFacesContext() {
		try {
			return FacesContext.getCurrentInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<Subject> getSubjectList() {
		try {
			return BeanList.getSubjectList();
		} catch (Exception e) {
			throw e;
		}
	}

}
