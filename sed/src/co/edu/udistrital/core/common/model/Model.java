package co.edu.udistrital.core.common.model;

import java.io.Serializable;

public abstract class Model implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8223886977122588596L;
	
	protected Long id;
	protected String dateCreation;
	protected String userCreation;
	protected String dateChange;
	protected String userChange;
	protected Long state;

}
