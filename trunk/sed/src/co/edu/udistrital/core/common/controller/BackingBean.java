package co.edu.udistrital.core.common.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import co.edu.udistrital.core.common.list.BeanList;
import co.edu.udistrital.core.login.controller.PanelStackBean;
import co.edu.udistrital.core.login.model.SedRole;
import co.edu.udistrital.core.login.model.Tree;
import co.edu.udistrital.core.login.model.TreeSedRole;
import co.edu.udistrital.sed.model.AssignmentType;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Grade;
import co.edu.udistrital.sed.model.IdentificationType;
import co.edu.udistrital.sed.model.KnowledgeArea;
import co.edu.udistrital.sed.model.Period;
import co.edu.udistrital.sed.model.QualificationType;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.session.common.User;

public abstract class BackingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8403730495105725673L;

	// Primitives
	protected boolean validLogin;


	// User Object
	private PanelStackBean panelStackBean;
	private User userSession;



	public BackingBean() throws Exception {
		try {
			if (getSession(false) != null) {
				this.userSession = (User) getSession(false).getAttribute("user");
				if (this.userSession != null) {
					this.validLogin = true;
				} else {
					this.validLogin = false;
				}
			} else
				this.validLogin = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void redirectToLogin() throws Exception {
		try {
			// String requestUrl = PrettyContext.getCurrentInstance().getRequestURL().toURL();
			// if (requestUrl != null && !requestUrl.trim().isEmpty() &&
			// !requestUrl.endsWith("login")) {
			// // redirect to login
			redirect("/portal/login");
			// getSession(false).setAttribute("requestPath", requestUrl);
			// }

		} catch (Exception e) {
			throw e;
		}
	}

	public abstract boolean getValidateSedUserRole() throws Exception;

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

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public static FacesContext getFacesContext() throws Exception {
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

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public static RequestContext getRequestContext() throws Exception {
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

	public static HttpServletRequest getRequest() throws Exception {
		try {
			return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static HttpSession getSession(boolean isNew) throws Exception {
		try {
			if (FacesContext.getCurrentInstance().getExternalContext() != null)
				return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(isNew);
			return null;
		} catch (Exception e) {
			throw e;

		}
	}

	public static HttpServletResponse getResponse() {
		try {
			return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public PanelStackBean getPanelStackBean() throws Exception {
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


	/** @author MTorres 17/11/2014 18:07:11 */
	public List<Course> loadCourseListByIdCourse(Long idGrade, List<Long> idCourseList) throws Exception {
		try {
			List<Course> courseList = new ArrayList<Course>();
			if (idCourseList != null) {
				for (Course c : getCourseList()) {
					if (c.getIdGrade().equals(idGrade) && idCourseList.contains(c.getId()))
						courseList.add(c);
				}
				return courseList;
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public List<Course> loadCourseListByGrade(Long idGrade) throws Exception {
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

	/** @author MTorres 11/08/2014 11:04:54 p. m. */
	protected QualificationType loadQualificationTypeById(Long idQualiticationType) throws Exception {
		try {
			if (idQualiticationType != null && !idQualiticationType.equals(0L)) {
				for (QualificationType qt : getQualificationTypeList()) {
					if (qt.getId().equals(idQualiticationType))
						return qt;
				}
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public List<Subject> loadSubjectListByGrade(Long idGrade) throws Exception {
		try {
			List<Subject> subjectGradeList = new ArrayList<Subject>();

			for (Subject s : BeanList.getSubjectList()) {
				if (s.getIdGrade().equals(idGrade))
					subjectGradeList.add(s);
			}
			return subjectGradeList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** @author MTorres 28/7/2014 23:18:35 */
	public Course loadCourseById(Long idCourse) throws Exception {
		try {
			if (idCourse == null || idCourse.equals(0L))
				return null;
			for (Course c : getCourseList()) {
				if (c.getId().equals(idCourse))
					return c;
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	/** @author MTorres 28/7/2014 23:16:29 */
	public Subject loadSubjectById(Long idSubject) throws Exception {
		try {
			if (idSubject == null || idSubject.equals(0L))
				return null;
			for (Subject s : getSubjectList()) {
				if (s.getId().equals(idSubject))
					return s;
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public static String getProperty(String key) throws Exception {
		try {
			return BeanList.getProperties().getProperty(key);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public static String getMessage(String key) throws Exception {
		try {
			return BeanList.getProperties().getWebMessage(key);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @author MTorres
	 * @throws Exception
	 */
	public List<Tree> loadTreeListByRole(Long idRole) throws Exception {
		try {
			List<Tree> treeRoleList = new ArrayList<Tree>();

			List<Long> idTreeList = new ArrayList<Long>();

			// Verificar id de todos los Tree
			for (TreeSedRole tsr : getTreeSedRoleList()) {
				if (tsr.getIdSedRole().equals(idRole) && !idTreeList.contains(tsr.getIdTree()))
					idTreeList.add(tsr.getIdTree());
			}


			for (Tree t : BeanList.getTreeList()) {

				if (t.isRoot() && idTreeList.contains(t.getId())) {
					Tree tree = new Tree();
					t.setLeafTreeList(null);
					tree = t;

					for (Tree leaf : BeanList.getTreeList()) {
						if (!leaf.getId().equals(tree.getId()) && leaf.getIdTreeRoot() != null && leaf.getIdTreeRoot().equals(tree.getId())
							&& idTreeList.contains(leaf.getId()))
							tree.getLeafTreeList().add(leaf);
					}
					treeRoleList.add(tree);
				}
			}
			return treeRoleList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<Subject> getSubjectList() throws Exception {
		try {
			return BeanList.getSubjectList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Course> getCourseList() throws Exception {
		try {
			return BeanList.getCourseList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Grade> getGradeList() throws Exception {
		try {
			return BeanList.getGradeList();
		} catch (Exception e) {
			throw e;
		}
	}


	public List<TreeSedRole> getTreeSedRoleList() throws Exception {
		try {
			return BeanList.getTreeSedRoleList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Tree> getTreeList() throws Exception {
		try {
			return BeanList.getTreeList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Period> getPeriodList() throws Exception {
		try {
			return BeanList.getPeriodList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<QualificationType> getQualificationTypeList() throws Exception {
		try {
			return BeanList.getQualificationTypeList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<IdentificationType> getIdentificationTypeList() throws Exception {
		try {
			return BeanList.getIdentificationTypeList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<SedRole> getSedRoleList() throws Exception {
		try {
			return BeanList.getSedRoleList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<KnowledgeArea> getKnowledgeAreaList() throws Exception {
		try {
			return BeanList.getKnowledgeAreaList();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<AssignmentType> getAssignmentTypeList() throws Exception {
		try {
			return BeanList.getAssignmentTypeList();
		} catch (Exception e) {
			throw e;
		}
	}

	public User getUserSession() {
		return userSession;
	}

	public void setUserSession(User userSession) {
		this.userSession = userSession;
	}

	public boolean isValidLogin() {
		return validLogin;
	}

	public void setValidLogin(boolean validLogin) {
		this.validLogin = validLogin;
	}

}
