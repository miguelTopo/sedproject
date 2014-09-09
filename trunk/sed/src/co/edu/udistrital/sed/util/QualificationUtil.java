package co.edu.udistrital.sed.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Array;

import co.edu.udistrital.sed.model.Qualification;

public class QualificationUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4020453093835253475L;

	private Long idSubject;
	private List<Long> idQualficationTypeList;
	private List<Qualification>qualificationList;

	public QualificationUtil() {
		
	}

	public QualificationUtil(Long idSubject, List<Long> idQualficationTypeList) {
		try {
			this.idSubject = idSubject;
			this.idQualficationTypeList = idQualficationTypeList;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Long getIdSubject() {
		return idSubject;
	}

	public void setIdSubject(Long idSubject) {
		this.idSubject = idSubject;
	}

	public List<Long> getIdQualficationTypeList() {
		if (idQualficationTypeList == null)
			idQualficationTypeList = new ArrayList<Long>();
		return idQualficationTypeList;
	}

	public void setIdQualficationTypeList(List<Long> idQualficationTypeList) {
		this.idQualficationTypeList = idQualficationTypeList;
	}

	public List<Qualification> getQualificationList() {
		return qualificationList;
	}

	public void setQualificationList(List<Qualification> qualificationList) {
		this.qualificationList = qualificationList;
	}
	
}
