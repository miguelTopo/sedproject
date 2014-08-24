package co.edu.udistrital.sed.util;

import java.io.Serializable;
import java.util.List;

public class QualificationUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4020453093835253475L;
	
	private Long idSubject;
	private List<Long> idQualficationTypeList;


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
		return idQualficationTypeList;
	}

	public void setIdQualficationTypeList(List<Long> idQualficationTypeList) {
		this.idQualficationTypeList = idQualficationTypeList;
	}

}
