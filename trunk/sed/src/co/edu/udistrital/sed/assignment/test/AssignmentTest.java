package co.edu.udistrital.sed.assignment.test;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.Object;

import co.edu.udistrital.core.common.util.ManageDate;
import co.edu.udistrital.sed.assignment.controller.AssignmentBean;
import co.edu.udistrital.sed.model.Assignment;



public class AssignmentTest {

	AssignmentBean ab;

	@Before
	public void init() {
		try {
			this.ab = new AssignmentBean();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void saveAssignment() {
		Assignment a = new Assignment();
		a.setIdSedUser(2L);
		a.setIdPeriod(Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		a.setIdCourse(7L);
		a.setIdSubject(5L);
		a.setIdGrade(1L);
		a.setStartHour("06:00:00");
		a.setEndHour("08:00:00");
		a.setUserCreation("admin");
		a.setDateCreation(ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD));
		ab.setIdDay(2L);
		ab.setAssignStartDate(new Date());
		ab.setAssignEndDate(new Date());
		ab.setAssignment(a);
		ab.saveTeacherAssignment();
		Assert.assertNull(null);
		
	}


}
