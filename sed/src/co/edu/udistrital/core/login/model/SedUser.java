package co.edu.udistrital.core.login.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import co.edu.udistrital.core.common.model.AParameter;
import co.edu.udistrital.sed.model.Student;

@Entity
@Table(name = "seduser")
public class SedUser extends AParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3851722277162781870L;

	private Long idIdentificationType;
	private String lastName;
	private String identification;
	private String email;

	// Transient
	private transient Long idSedRole;
	private transient String nameSedRole;
	private transient String nameIdentificationType;
	private transient String userName;

	public SedUser() {

	}

	public SedUser(Student student) {
		try {
			this.idIdentificationType = student.getIdIdentificationType();
			this.name = student.getName();
			this.lastName=student.getLastName();
			this.identification = student.getIdentification();
			this.email = student.getEmail();
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

	@Column(name = "idIdentificationType", nullable = false)
	public Long getIdIdentificationType() {
		return idIdentificationType;
	}

	public void setIdIdentificationType(Long idIdentificationType) {
		this.idIdentificationType = idIdentificationType;
	}

	@Column(name = "lastName", length = 60)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "identification", length = 20)
	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// ////////----------Transient----------//////////

	@Transient
	public Long getIdSedRole() {
		return idSedRole;
	}

	public void setIdSedRole(Long idSedRole) {
		this.idSedRole = idSedRole;
	}

	@Transient
	public String getNameSedRole() {
		return nameSedRole;
	}

	public void setNameSedRole(String nameSedRole) {
		this.nameSedRole = nameSedRole;
	}

	@Transient
	public String getNameIdentificationType() {
		return nameIdentificationType;
	}

	public void setNameIdentificationType(String nameIdentificationType) {
		this.nameIdentificationType = nameIdentificationType;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
