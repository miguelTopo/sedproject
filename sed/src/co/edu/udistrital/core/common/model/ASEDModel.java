package co.edu.udistrital.core.common.model;

import java.io.Serializable;

import co.edu.udistrital.core.common.controller.IState;
import co.edu.udistrital.core.common.util.ManageDate;

public abstract class ASEDModel implements Serializable {

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

	public void initialize(boolean isNew, String user) {
		try {
			if (isNew) {
				this.dateCreation = ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD);
				this.userCreation = user;
				this.state = IState.ACTIVE;
			} else {
				this.dateChange = ManageDate.getCurrentDate(ManageDate.YYYY_MM_DD);
				this.userChange = user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
