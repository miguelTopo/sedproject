package co.edu.udistrital.core.common.list;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import co.edu.udistrital.core.common.list.beanlist.controller.ControllerList;
import co.edu.udistrital.sed.model.Subject;

@ManagedBean(eager = true)
@ApplicationScoped
public class BeanList implements Serializable{
	
	private static ControllerList controller;
	
	private static List<Subject>subjectList;
	
	static{
		initializeList();
	}
	
	public static void initializeList(){
		try {
			controller = new ControllerList();
			loadSubjectList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	public static void loadSubjectList(){
		try {
			if(subjectList == null)
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
	
	

}
