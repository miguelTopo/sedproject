package co.edu.udistrital.core.login.model;

import javax.persistence.Column;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.persistence.Entity;

import org.hibernate.annotations.GenericGenerator;

import co.edu.udistrital.core.common.model.AParameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tree")
public class Tree extends AParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8144263378384284865L;
	// Primitives
	private boolean isRoot;

	// Simple Java Data Object
	private Long idTreeRoot;
	private String urlPattern;
	private String icon;
	private String iconClass;

	// User Transient List
	private List<Tree> leafTreeList;



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

	@Column(name = "icon")
	public String getIcon() {
		return icon;
	}

	@Column(name = "urlPattern", length = 100)
	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}
	// ////////----------Transient----------//////////

	@Transient
	public List<Tree> getLeafTreeList() {
		if (leafTreeList == null)
			leafTreeList = new ArrayList<Tree>();
		return leafTreeList;
	}

	public void setLeafTreeList(List<Tree> leafTreeList) {
		this.leafTreeList = leafTreeList;
	}



}
