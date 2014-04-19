package co.edu.udistrital.core.common.list.beanlist.controller;

import java.util.List;


import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import co.edu.udistrital.core.common.controller.Controller;
import co.edu.udistrital.core.login.model.Tree;
import co.edu.udistrital.core.login.model.TreeDAO;
import co.edu.udistrital.core.login.model.TreeSedRole;
import co.edu.udistrital.core.login.model.TreeSedRoleDAO;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.CourseDAO;
import co.edu.udistrital.sed.model.Grade;
import co.edu.udistrital.sed.model.GradeDAO;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.model.SubjectDAO;


public class ControllerList extends Controller {

	/** @author MTorres */
	public List<Subject> loadSubjectList() throws Exception {
		SubjectDAO dao = new SubjectDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	/** @author MTorres */
	public List<Course> loadCourseList() throws Exception {
		CourseDAO dao = new CourseDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}

	}

	/** @author MTorres */
	public List<Grade> loadGradeList() throws Exception {
		GradeDAO dao = new GradeDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	public List<Tree> loadTreeList()throws Exception {
		TreeDAO dao = new TreeDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}

	public List<TreeSedRole> loadTreeSedRoleList() throws Exception{
		TreeSedRoleDAO dao = new TreeSedRoleDAO();
		Transaction tx = null;
		try {
			tx = dao.getSession().beginTransaction();
			return dao.findAll();
		} catch (Exception e) {
			dao.getSession().cancelQuery();
			tx.rollback();
			throw e;
		} finally {
			dao.getSession().close();
			dao = null;
			tx = null;
		}
	}
}
