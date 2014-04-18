package co.edu.udistrital.core.login.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.Entity;

import org.hibernate.annotations.GenericGenerator;

import co.edu.udistrital.core.common.model.AParameter;

import java.io.Serializable;

@Entity
@Table(name = "tree")
public class Tree extends AParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8144263378384284865L;
	
	private boolean isRoot;
	private Long idTreeRoot;
	private String ulrPattern;


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

	@Column(name = "isRoot", nullable = false)
	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	@Column(name = "idTreeRoot")
	public Long getIdTreeRoot() {
		return idTreeRoot;
	}

	public void setIdTreeRoot(Long idTreeRoot) {
		this.idTreeRoot = idTreeRoot;
	}

	@Column(name = "ulrPattern", length = 100, nullable = false)
	public String getUlrPattern() {
		return ulrPattern;
	}

	public void setUlrPattern(String ulrPattern) {
		this.ulrPattern = ulrPattern;
	}



}
