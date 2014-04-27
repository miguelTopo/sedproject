package co.edu.udistrital.core.common.controller;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import co.edu.udistrital.core.common.list.BeanList;
import co.edu.udistrital.core.login.controller.PanelStackBean;
import co.edu.udistrital.core.login.model.SedRole;
import co.edu.udistrital.core.login.model.Tree;
import co.edu.udistrital.core.login.model.TreeSedRole;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Grade;
import co.edu.udistrital.sed.model.IdentificationType;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.session.common.SedSession;

public abstract class BackingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8403730495105725673L;
	private PanelStackBean panelStackBean;
	private SedSession sedSession;

	public BackingBean() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract boolean getValidateSedUserRole(Long idSedRole) throws Exception;

	/** @author MTorres */
	public static void addInfoMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void addWarnMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void addErrorMessage(String summary, String detail) {
		try {
			getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void addFatalMessage(String summary, String detail) {
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

	public static void redirect(String facesPattern) {
		try {
			String url = null;
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			String scheme = getRequest().getScheme();
			String serverName = getRequest().getServerName();
			String contextPath = getRequest().getContextPath();
			int serverPort = getRequest().getServerPort();

			url = scheme + "://" + serverName + ":" + serverPort + contextPath;

			context.redirect(url + facesPattern);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HttpServletRequest getRequest() {
		try {
			return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
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

	/** @author MTorres */
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

	/** @author MTorres */
	public List<Subject> loadSubjectListByGrade(Long idGrade) {
		try {
			List<Subject> subjectGradeList = new ArrayList<>();

			for (Subject s : getSubjectList()) {
				if (s.getIdGrade().equals(idGrade))
					subjectGradeList.add(s);
			}
			return subjectGradeList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres */
	public static String getProperty(String key) {
		try {
			return BeanList.getProperties().getProperty(key);
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres */
	public static String getMessage(String key) {
		try {
			return BeanList.getProperties().getWebMessage(key);
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres */
	public List<Tree> loadTreeListByRole(Long idRole) {
		try {
			List<Tree> treeRoleList = new ArrayList<Tree>();
			for (TreeSedRole tr : getTreeSedRoleList()) {

				if (tr.getIdSedRole().equals(idRole)) {


					for (Tree t : BeanList.getTreeList()) {
						if (tr.getIdTree().equals(t.getId()) && t.isRoot()) {

							for (Tree tl : BeanList.getTreeList()) {
								if (!tl.isRoot() && tl.getIdTreeRoot().equals(t.getId()))
									t.getLeafTreeList().add(tl);
							}
							treeRoleList.add(t);
						}

					}
				}
			}

			return treeRoleList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// ////////----------getters and setters----------//////////

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

	public List<Grade> getGradeList() {
		try {
			return BeanList.getGradeList();
		} catch (Exception e) {
			throw e;
		}
	}


	public List<TreeSedRole> getTreeSedRoleList() {
		try {
			return BeanList.getTreeSedRoleList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Tree> getTreeList() {
		try {
			return BeanList.getTreeList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<IdentificationType> getIdentificationTypeList() {
		try {
			return BeanList.getIdentificationTypeList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<SedRole> getSedRoleList() {
		try {
			return BeanList.getSedRoleList();
		} catch (Exception e) {
			throw e;
		}
	}

	public SedSession getSedSession() {
		return sedSession;
	}

	public void setSedSession(SedSession sedSession) {
		this.sedSession = sedSession;
	}


}
