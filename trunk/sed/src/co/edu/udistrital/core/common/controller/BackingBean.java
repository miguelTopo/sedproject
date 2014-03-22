package co.edu.udistrital.core.common.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.hibernate.id.IdentityGenerator.GetGeneratedKeysDelegate;
import org.primefaces.context.RequestContext;

import co.edu.udistrital.core.common.list.BeanList;
import co.edu.udistrital.core.login.controller.PanelStackBean;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Grade;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.model.TimeZone;

public abstract class BackingBean implements Serializable {

	private PanelStackBean panelStackBean;

	public BackingBean() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void addInfoMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void addWarnMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void addErrorMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public void addFatalMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static FacesContext getFacesContext() {
		try {
			return FacesContext.getCurrentInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	public void setPanelView(String page, String title, String beanName) {
		try {
			String prefix = null;
			if (page.startsWith("/"))
				prefix = "/WEB-INF/includes";
			else
				prefix = "/WEB-INF/includes/";

			getPanelStackBean().setSelectedPanelAndTitle(prefix + page, title);

			if (getRequestContext() != null)
				getRequestContext().update("panelStack");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static RequestContext getRequestContext() {
		try {
			return RequestContext.getCurrentInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	public PanelStackBean getPanelStackBean() {
		try {
			if (panelStackBean == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				panelStackBean =
					(PanelStackBean) facesContext.getApplication().getExpressionFactory()
						.createValueExpression(facesContext.getELContext(), "#{panelStack}", PanelStackBean.class)
						.getValue(facesContext.getELContext());
			}
			return panelStackBean;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void setPanelStackBean(PanelStackBean panelStackBean) {
		this.panelStackBean = panelStackBean;
	}

	public List<Course> loadCourseListByGrade(Long idGrade) {
		try {
			List<Course> courseList = new ArrayList<Course>();
			for (Course c : getCourseList()) {
				if (c.getIdGrade().equals(idGrade))
					courseList.add(c);
			}
			return courseList;
		} catch (Exception e) {
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

	public List<Course> getCourseList() {
		try {
			return BeanList.getCourseList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<TimeZone> getTimeZoneList() {
		try {
			return BeanList.getTimeZoneList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Grade> getGradeList() {
		try {
			return BeanList.getGradeList();
		} catch (Exception e) {
			throw e;
		}
	}

}
