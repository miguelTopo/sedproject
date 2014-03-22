package co.edu.udistrital.core.common.list;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import co.edu.udistrital.core.common.list.beanlist.controller.ControllerList;
import co.edu.udistrital.sed.model.Course;
import co.edu.udistrital.sed.model.Grade;
import co.edu.udistrital.sed.model.Subject;
import co.edu.udistrital.sed.model.TimeZone;

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
	private static List<TimeZone> timeZoneList;
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
			loadTimeZoneList();

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
	public static void loadTimeZoneList() {
		try {
			if (timeZoneList == null)
				timeZoneList = controller.loadTimeZoneList();
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

	public static void setSubjectList(List<Subject> subjectList) {
		BeanList.subjectList = subjectList;
	}

	public static List<Course> getCourseList() {
		return courseList;
	}

	public static void setCourseList(List<Course> courseList) {
		BeanList.courseList = courseList;
	}

	public static List<TimeZone> getTimeZoneList() {
		return timeZoneList;
	}

	public static void setTimeZoneList(List<TimeZone> timeZoneList) {
		BeanList.timeZoneList = timeZoneList;
	}

	public static List<Grade> getGradeList() {
		return gradeList;
	}

	public static void setGradeList(List<Grade> gradeList) {
		BeanList.gradeList = gradeList;
	}



}
