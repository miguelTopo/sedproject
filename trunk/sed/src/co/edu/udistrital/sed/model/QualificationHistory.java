package co.edu.udistrital.sed.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import co.edu.udistrital.core.common.model.ASEDModel;

@Entity
@Table(name = "QualificationHistory", schema = "lifemena")
public class QualificationHistory extends ASEDModel {

	private Long idStudentCourse;
	private Long idQualificationType;
	private Long idSubject;
	private Double value;

	// Transient
	private Long idStudent;
	private Long idKnowledgeArea;
	private String studentName;
	private String studentLastName;
	private String studentIdentification;
	private String subjectName;
	private String knowledgeAreaName;
	private String qualificationTypeName;
	private Long idPeriod;
	private Long idGrade;
	private Long idCourse;

	public QualificationHistory() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


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

	@Transient
	public String getStudentIdentification() {
		return studentIdentification;
	}

	public void setStudentIdentification(String studentIdentification) {
		this.studentIdentification = studentIdentification;
	}

	@Transient
	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	@Transient
	public String getQualificationTypeName() {
		return qualificationTypeName;
	}

	public void setQualificationTypeName(String qualificationTypeName) {
		this.qualificationTypeName = qualificationTypeName;
	}

	@Transient
	public Long getIdPeriod() {
		return idPeriod;
	}

	public void setIdPeriod(Long idPeriod) {
		this.idPeriod = idPeriod;
	}

	@Transient
	public Long getIdGrade() {
		return idGrade;
	}

	public void setIdGrade(Long idGrade) {
		this.idGrade = idGrade;
	}

	@Transient
	public Long getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(Long idCourse) {
		this.idCourse = idCourse;
	}


}
