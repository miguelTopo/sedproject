package co.edu.udistrital.sed.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name = "Assignment", schema = "lifemena")
public class Assignment implements Serializable, Cloneable {

	private Long id;
	private Long idSedUser;
	private Long idPeriod;
	private Long idCourse;
	private Long idSubject;
	private Long idDay;
	private Long state;
	private String startHour;
	private String endHour;
	private String userCreation;
	private String dateCreation;
	private String userChange;
	private String dateChange;

	// Transient
	private Long idGrade;
	private Long idTeacher;
	private String teacherFullName;
	private String subjectName;
	private String courseName;
	private String subjectStyleClass;

	public Assignment() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** @author MTorres 29/7/2014 22:01:11 */
	public Assignment clone() {
		try {
			return (Assignment) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", nullable = false, unique = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "idSedUser", nullable = false)
	public Long getIdSedUser() {
		return idSedUser;
	}

	public void setIdSedUser(Long idSedUser) {
		this.idSedUser = idSedUser;
	}

	@Column(name = "idPeriod", nullable = false)
	public Long getIdPeriod() {
		return idPeriod;
	}

	public void setIdPeriod(Long idPeriod) {
		this.idPeriod = idPeriod;
	}

	@Column(name = "idCourse", nullable = false)
	public Long getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(Long idCourse) {
		this.idCourse = idCourse;
	}

	@Column(name = "idSubject", nullable = false)
	public Long getIdSubject() {
		return idSubject;
	}

	public void setIdSubject(Long idSubject) {
		this.idSubject = idSubject;
	}

	@Column(name = "idDay", nullable = false)
	public Long getIdDay() {
		return idDay;
	}

	public void setIdDay(Long idDay) {
		this.idDay = idDay;
	}

	@Column(name = "startHour", nullable = false, length = 10)
	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	@Column(name = "endHour", nullable = false, length = 10)
	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	@Column(name = "dateCreation", nullable = false, length = 15)
	public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	@Column(name = "userCreation", nullable = false, length = 50)
	public String getUserCreation() {
		return userCreation;
	}

	public void setUserCreation(String userCreation) {
		this.userCreation = userCreation;
	}

	@Column(name = "userChange", nullable = false, length = 50)
	public String getUserChange() {
		return userChange;
	}

	public void setUserChange(String userChange) {
		this.userChange = userChange;
	}

	@Column(name = "dateChange", nullable = false, length = 15)
	public String getDateChange() {
		return dateChange;
	}

	public void setDateChange(String dateChange) {
		this.dateChange = dateChange;
	}

	@Column(name = "state", nullable = false)
	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	@Transient
	public Long getIdGrade() {
		return idGrade;
	}

	public void setIdGrade(Long idGrade) {
		this.idGrade = idGrade;
	}

	@Transient
	public Long getIdTeacher() {
		return idTeacher;
	}

	public void setIdTeacher(Long idTeacher) {
		this.idTeacher = idTeacher;
	}

	@Transient
	public String getTeacherFullName() {
		return teacherFullName;
	}

	public void setTeacherFullName(String teacherFullName) {
		this.teacherFullName = teacherFullName;
	}

	@Transient
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	@Transient
	public String getSubjectStyleClass() {
		return subjectStyleClass;
	}

	public void setSubjectStyleClass(String subjectStyleClass) {
		this.subjectStyleClass = subjectStyleClass;
	}

	@Transient
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

}
