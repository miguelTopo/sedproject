package co.edu.udistrital.core.login.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.Entity;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "treesedrole", schema = "lifemena")
public class TreeSedRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5632789006245355970L;

	private Long id;
	private Long idSedRole;
	private Long idTree;
	private Long state;
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

	@Column(name = "idSedRole", nullable = false)
	public Long getIdSedRole() {
		return idSedRole;
	}

	public void setIdSedRole(Long idSedRole) {
		this.idSedRole = idSedRole;
	}

	@Column(name = "idTree", nullable = false)
	public Long getIdTree() {
		return idTree;
	}

	public void setIdTree(Long idTree) {
		this.idTree = idTree;
	}
}
