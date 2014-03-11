package co.edu.udistrital.sed.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import co.edu.udistrital.core.common.model.AParameter;

@Entity
@Table(name = "qualification")
public class Qualification extends AParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1184189519737803513L;

	private Long idStudent;
	private Long idSubject;
	private Long idCourse;
	private Double p1;
	private Double p2;
	private Double p3;
	private Double p4;
	private Double p5;
	private Double p6;
	private Double pf;
	private Double pm;
	private Double au;

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

	@Column(name = "dateCreation", length = 15)
	public String getDateChange() {
		return this.dateChange;
	}

	public void setDateChange(String dateChange) {
		this.dateChange = dateChange;
	}

	@Column(name = "idStudent", nullable = false)
	public Long getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(Long idStudent) {
		this.idStudent = idStudent;
	}

	@Column(name = "idSubject", nullable = false)
	public Long getIdSubject() {
		return idSubject;
	}

	public void setIdSubject(Long idSubject) {
		this.idSubject = idSubject;
	}

	@Column(name = "idCourse", nullable = false)
	public Long getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(Long idCourse) {
		this.idCourse = idCourse;
	}

	@Column(name = "p1")
	public Double getP1() {
		return p1;
	}

	public void setP1(Double p1) {
		this.p1 = p1;
	}

	@Column(name = "p2")
	public Double getP2() {
		return p2;
	}

	public void setP2(Double p2) {
		this.p2 = p2;
	}

	@Column(name = "p3")
	public Double getP3() {
		return p3;
	}

	public void setP3(Double p3) {
		this.p3 = p3;
	}

	@Column(name = "p4")
	public Double getP4() {
		return p4;
	}

	public void setP4(Double p4) {
		this.p4 = p4;
	}

	@Column(name = "p5")
	public Double getP5() {
		return p5;
	}

	public void setP5(Double p5) {
		this.p5 = p5;
	}

	@Column(name = "p6")
	public Double getP6() {
		return p6;
	}

	public void setP6(Double p6) {
		this.p6 = p6;
	}

	@Column(name = "pf")
	public Double getPf() {
		return pf;
	}

	public void setPf(Double pf) {
		this.pf = pf;
	}

	@Column(name = "pm")
	public Double getPm() {
		return pm;
	}

	public void setPm(Double pm) {
		this.pm = pm;
	}

	@Column(name = "au")
	public Double getAu() {
		return au;
	}

	public void setAu(Double au) {
		this.au = au;
	}

}
