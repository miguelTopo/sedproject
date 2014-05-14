package co.edu.udistrital.session.common;

import java.io.Serializable;

public class SedSession implements Serializable {

	private Long id;
	private Long idSedUser;
	private Long idIdentificationType;
	private String name;
	private String lastName;
	private String identification;
	private String email;
	private Long idSedRoleUser;

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
	
}
