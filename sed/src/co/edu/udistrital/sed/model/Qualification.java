package co.edu.udistrital.sed.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import co.edu.udistrital.core.common.model.AParameter;
import co.edu.udistrital.core.common.model.ASEDModel;

@Entity
@Table(name = "qualification")
public class Qualification extends ASEDModel implements Serializable {

	private Long idStudentCourse;
	private Long idQualificationType;
	private Long idSubject;
	private Double value;

	// Transient
	private transient Long idStudent;
	private transient Long idKnowledgeArea;
	private transient String studentName;
	private transient String subjectName;
	private transient String knowledgeAreaName;

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", nullable = false, unique = true)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "state", nullable = false)
	public Long getState() {
		return this.state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	@Column(name = "userCreation", nullable = false, length = 50)
	public String getUserCreation() {
		return this.userCreation;
	}

	public void setUserCreation(String userCreation) {
		this.userCreation = userCreation;
	}

	@Column(name = "dateCreation", nullable = false, length = 15)
	public String getDateCreation() {
		return this.dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getUserChange() {
		return this.userChange;
	}

	@Column(name = "userChange", length = 50)
	public void setUserChange(String userChange) {
		this.userChange = userChange;
	}

	public String getDateChange() {
		return this.dateChange;
	}

	public void setDateChange(String dateChange) {
		this.dateChange = dateChange;
	}

	@Column(name = "idStudentCourse", nullable = false)
	public Long getIdStudentCourse() {
		return idStudentCourse;
	}

	public void setIdStudentCourse(Long idStudentCourse) {
		this.idStudentCourse = idStudentCourse;
	}

	@Column(name = "idQualificationType", nullable = false)
	public Long getIdQualificationType() {
		return idQualificationType;
	}

	public void setIdQualificationType(Long idQualificationType) {
		this.idQualificationType = idQualificationType;
	}

	@Column(name = "idSubject", nullable = false)
	public Long getIdSubject() {
		return idSubject;
	}

	public void setIdSubject(Long idSubject) {
		this.idSubject = idSubject;
	}

	@Column(name = "value")
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Transient
	public Long getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(Long idStudent) {
		this.idStudent = idStudent;
	}

	@Transient
	public Long getIdKnowledgeArea() {
		return idKnowledgeArea;
	}

	public void setIdKnowledgeArea(Long idKnowledgeArea) {
		this.idKnowledgeArea = idKnowledgeArea;
	}

	@Transient
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@Transient
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	@Transient
	public String getKnowledgeAreaName() {
		return knowledgeAreaName;
	}

	public void setKnowledgeAreaName(String knowledgeAreaName) {
		this.knowledgeAreaName = knowledgeAreaName;
	}

}
