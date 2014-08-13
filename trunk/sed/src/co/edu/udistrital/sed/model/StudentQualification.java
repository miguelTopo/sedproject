package co.edu.udistrital.sed.model;

import java.io.Serializable;
import java.util.List;


public class StudentQualification implements Serializable {

	private String knowledgeAreaName;
	private String subjectName;
	private List<Qualification> qualificationList;

	private Long idPeriod;

	public StudentQualification() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public StudentQualification(String knowledgeAreaName, String subjectName, List<Qualification> qualificationList, Long idPeriod) {
		try {
			this.knowledgeAreaName = knowledgeAreaName;
			this.subjectName = subjectName;
			this.qualificationList = qualificationList;
			this.idPeriod = idPeriod;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public String getKnowledgeAreaName() {
		return knowledgeAreaName;
	}

	public void setKnowledgeAreaName(String knowledgeAreaName) {
		this.knowledgeAreaName = knowledgeAreaName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<Qualification> getQualificationList() {
		return qualificationList;
	}

	public void setQualificationList(List<Qualification> qualificationList) {
		this.qualificationList = qualificationList;
	}

	public Long getIdPeriod() {
		return idPeriod;
	}

	public void setIdPeriod(Long idPeriod) {
		this.idPeriod = idPeriod;
	}
}
