package co.edu.udistrital.sed.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import co.edu.udistrital.core.common.model.AParameter;
import co.edu.udistrital.sed.util.QualificationUtil;

@Entity
@Table(name = "student")
public class Student extends AParameter implements Serializable {


	private String identification;
	private String lastName;
	private Long idIdentificationType;
	private Long idSedUser;
	private Long idSedUserResponsible;

	// Transient
	private String courseName;
	private String gradeName;
	private String identificationTypeName;
	private String email;
	private String studentFullName;
	private String sedUserResponsibleFullName;
	private Long idGrade;
	private Long idCourse;
	private Long idStudentCourse;


	private List<Integer> invalidColumn;
	private List<Long> idSubjectList;
	private List<Qualification> qualificationList;
	private List<Qualification> qualificationTmpList;
	private List<KnowledgeArea> knowledgeAreaList;
	private List<QualificationUtil> qualificationUtilList;

	public Student() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public Student(String identification, String lastName, String name) {
		this.identification = identification;
		this.lastName = lastName;
		this.name = name;
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

	@Column(name = "identification", nullable = false, length = 50)
	public String getIdentification() {
		return this.identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	@Column(name = "name", nullable = false, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Column(name = "userChange", length = 50)
	public String getUserChange() {
		return this.userChange;
	}

	public void setUserChange(String userChange) {
		this.userChange = userChange;
	}

	@Column(name = "dateChange", length = 15)
	public String getDateChange() {
		return this.dateChange;
	}

	public void setDateChange(String dateChange) {
		this.dateChange = dateChange;
	}

	@Column(name = "lastName", length = 60)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "idIdentificationType", nullable = false)
	public Long getIdIdentificationType() {
		return idIdentificationType;
	}

	public void setIdIdentificationType(Long idIdentificationType) {
		this.idIdentificationType = idIdentificationType;
	}

	@Column(name = "idSedUser", nullable = false)
	public Long getIdSedUser() {
		return idSedUser;
	}

	public void setIdSedUser(Long idSedUser) {
		this.idSedUser = idSedUser;
	}

	@Column(name = "idSedUserResponsible", nullable = false)
	public Long getIdSedUserResponsible() {
		return idSedUserResponsible;
	}

	public void setIdSedUserResponsible(Long idSedUserResponsible) {
		this.idSedUserResponsible = idSedUserResponsible;
	}

	@Transient
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Transient
	public List<Integer> getInvalidColumn() {
		if (invalidColumn == null)
			invalidColumn = new ArrayList<Integer>();
		return invalidColumn;
	}

	public void setInvalidColumn(List<Integer> invalidColumn) {
		this.invalidColumn = invalidColumn;
	}

	@Transient
	public Long getIdGrade() {
		return idGrade;
	}

	public void setIdGrade(Long idGrade) {
		this.idGrade = idGrade;
	}

	@Transient
	public List<Qualification> getQualificationList() {
		if (this.qualificationList == null)
			this.qualificationList = new ArrayList<Qualification>();
		return this.qualificationList;
	}

	public void setQualificationList(List<Qualification> qualificationList) {
		this.qualificationList = qualificationList;
	}

	@Transient
	public Long getIdStudentCourse() {
		return idStudentCourse;
	}

	public void setIdStudentCourse(Long idStudentCourse) {
		this.idStudentCourse = idStudentCourse;
	}

	@Transient
	public Long getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(Long idCourse) {
		this.idCourse = idCourse;
	}

	@Transient
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Transient
	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	@Transient
	public String getIdentificationTypeName() {
		return identificationTypeName;
	}

	public void setIdentificationTypeName(String identificationTypeName) {
		this.identificationTypeName = identificationTypeName;
	}

	@Transient
	public String getStudentFullName() {
		return studentFullName;
	}

	public void setStudentFullName(String studentFullName) {
		this.studentFullName = studentFullName;
	}

	@Transient
	public List<KnowledgeArea> getKnowledgeAreaList() {
		return knowledgeAreaList;
	}

	public void setKnowledgeAreaList(List<KnowledgeArea> knowledgeAreaList) {
		this.knowledgeAreaList = knowledgeAreaList;
	}

	@Transient
	public List<QualificationUtil> getQualificationUtilList() {
		if (qualificationUtilList == null)
			qualificationUtilList = new ArrayList<QualificationUtil>();
		return qualificationUtilList;
	}

	public void setQualificationUtilList(List<QualificationUtil> qualificationUtilList) {
		this.qualificationUtilList = qualificationUtilList;
	}

	@Transient
	public List<Long> getIdSubjectList() {
		if (idSubjectList == null)
			idSubjectList = new ArrayList<Long>();
		return idSubjectList;
	}

	public void setIdSubjectList(List<Long> idSubjectList) {
		this.idSubjectList = idSubjectList;
	}

	@Transient
	public List<Qualification> getQualificationTmpList() {
		if (qualificationTmpList == null)
			qualificationTmpList = new ArrayList<Qualification>();
		return qualificationTmpList;
	}

	public void setQualificationTmpList(List<Qualification> qualificationTmpList) {
		this.qualificationTmpList = qualificationTmpList;
	}

	@Transient
	public String getSedUserResponsibleFullName() {
		return sedUserResponsibleFullName;
	}

	public void setSedUserResponsibleFullName(String sedUserResponsibleFullName) {
		this.sedUserResponsibleFullName = sedUserResponsibleFullName;
	}
}
