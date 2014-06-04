package co.edu.udistrital.sed.teacher.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.controller.BackingBean;
import co.edu.udistrital.sed.model.Student;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "tQualification", pattern = "/portal/calificar", viewId = "/pages/teacherqualification/teacherqualification.jspx")
public class TeacherQualificationBean extends BackingBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3837887624361885564L;

	// Primitives
	private boolean showP1 = false;
	private boolean showP2 = false;

	private List<Student> studentList;

	public TeacherQualificationBean() {
		try {
			setShowP1(true);
			this.studentList = new ArrayList<Student>(10);
			
			studentList.add(new Student("1014212275", "Torres Cardona", "Leidy Caterine"));
			studentList.add(new Student("1010456741", "Andrade Peralta", "Lina"));
			studentList.add(new Student("1012452369", "Alvarado Espinoza", "Viviana Andrea"));
			studentList.add(new Student("1015894522", "Paez Cardenas", "Patricia"));
			studentList.add(new Student("1013478951", "Pardo Guerrero", "Maby"));
			studentList.add(new Student("1013847963", "Roncancio Rincon", "Yuly Marcela"));
			studentList.add(new Student("1023964753", "Giraldo Cruz", "Luz Dary"));
			studentList.add(new Student("1010963357", "Galarza Leon", "Andrea Carolina"));
			studentList.add(new Student("1015226745", "Palacios Reyes", "Liliana"));
			studentList.add(new Student("1013778421", "Bermudez Rios", "Milena"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goProp2(int prop) {
		try {
			hideAll();
			if (prop == 1) {
				setShowP1(true);
				setPanelView("prop1", "Propuesta 1", "TeacherQualificationBean");
			}
			else {
				setShowP2(true);
				setPanelView("prop2", "Propuesta 2", "TeacherQualificationBean");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void hideAll() {
		try {
			setShowP1(false);
			setShowP2(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean getValidateSedUserRole() throws Exception {

		return false;
	}

	public boolean isShowP1() {
		return showP1;
	}

	public void setShowP1(boolean showP1) {
		this.showP1 = showP1;
	}

	public boolean isShowP2() {
		return showP2;
	}

	public void setShowP2(boolean showP2) {
		this.showP2 = showP2;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}


}
