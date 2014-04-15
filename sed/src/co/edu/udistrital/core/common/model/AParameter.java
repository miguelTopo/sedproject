package co.edu.udistrital.core.common.model;

import java.io.Serializable;

public abstract class AParameter extends ASEDModel implements Serializable{

	protected String name;


	public abstract String getName();

	public abstract void setName(String name);

	public abstract Long getId();

	public abstract void setId(Long id);

	public abstract Long getState();

	public abstract void setState(Long state);

	public abstract String getUserCreation();

	public abstract void setUserCreation(String userCreation);

	public abstract String getDateCreation();

	public abstract void setDateCreation(String dateCreation);

	public abstract String getUserChange();

	public abstract void setUserChange(String userChange);

	public abstract String getDateChange();

	public abstract void setDateChange(String dateChange);

}
