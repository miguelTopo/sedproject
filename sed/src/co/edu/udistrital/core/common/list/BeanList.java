package co.edu.udistrital.core.common.list;

import java.io.Serializable;


import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import co.edu.udistrital.core.common.list.beanlist.controller.ControllerList;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Grade;
import co.edu.udistrital.sed.model.Subject;

@ManagedBean(eager = true)
@ApplicationScoped
public class BeanList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6631727569215723693L;

	private static ControllerList controller;

	private static List<Subject> subjectList;
	private static List<Course> courseList;
	private static List<Grade> gradeList;

	static {
		initializeList();
	}

	/** @author MTorres */
	public static void initializeList() {
		try {
			controller = new ControllerList();
			loadSubjectList();
			loadGradeList();
			loadCourseList();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void loadGradeList() {
		try {
			if (gradeList == null)
				gradeList = controller.loadGradeList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void loadCourseList() {
		try {
			if (courseList == null)
				courseList = controller.loadCourseList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres */
	public static void loadSubjectList() {
		try {
			if (subjectList == null)
				subjectList = controller.loadSubjectList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Subject> getSubjectList() {
		return subjectList;
	}

	public static List<Course> getCourseList() {
		return courseList;
	}

	public static List<Grade> getGradeList() {
		return gradeList;
	}

}
