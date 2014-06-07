package co.edu.udistrital.session.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

	private Long id;
	private Long idSedUser;
	private Long idIdentificationType;
	private Long idStudent;
	private String name;
	private String lastName;
	private String identification;
	private String email;
	private String sedRoleName;
	private String idSession;
	private Long idSedRoleUser;
	private Long idSedRole;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdIdentificationType() {
		return idIdentificationType;
	}

	public void setIdIdentificationType(Long idIdentificationType) {
		this.idIdentificationType = idIdentificationType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getIdSedRoleUser() {
		return idSedRoleUser;
	}

	public void setIdSedRoleUser(Long idSedRoleUser) {
		this.idSedRoleUser = idSedRoleUser;
	}

	public Long getIdSedUser() {
		return idSedUser;
	}

	public void setIdSedUser(Long idSedUser) {
		this.idSedUser = idSedUser;
	}

	public String getIdSession() {
		return idSession;
	}

	public void setIdSession(String idSession) {
		this.idSession = idSession;
	}

	public String getSedRoleName() {
		return sedRoleName;
	}

	public void setSedRoleName(String sedRoleName) {
		this.sedRoleName = sedRoleName;
	}

	public Long getIdSedRole() {
		return idSedRole;
	}

	public void setIdSedRole(Long idSedRole) {
		this.idSedRole = idSedRole;
	}

	public Long getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(Long idStudent) {
		this.idStudent = idStudent;
	}
					
}
