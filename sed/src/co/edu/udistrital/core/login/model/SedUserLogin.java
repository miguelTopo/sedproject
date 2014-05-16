package co.edu.udistrital.core.login.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name = "seduserlogin")
public class SedUserLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1733551404130798405L;

	private Long id;
	private Long state;
	private Long idSedUser;
	private String userName;
	private String md5Password;
	private String userCreation;
	private String dateCreation;
	private String userChange;
	private String dateChange;


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

	@Column(name = "idSedUser", nullable = false)
	public Long getIdSedUser() {
		return idSedUser;
	}

	public void setIdSedUser(Long idSedUser) {
		this.idSedUser = idSedUser;
	}

	@Column(name = "userName", length = 100, nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "md5Password", length = 500, nullable = false)
	public String getMd5Password() {
		return md5Password;
	}

	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}


}
