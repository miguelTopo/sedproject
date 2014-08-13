package co.edu.udistrital.core.login.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "panelStack")
@SessionScoped
public class PanelStackBean implements Serializable {

	private String selectedPanel;
	private String selectedTitle;

	public void clear() {
		selectedPanel = "";
		selectedTitle = "";
	}

	public void setSelectedPanelAndTitle(String selectedPanel, String title) {
		setSelectedPanel(selectedPanel);
		setSelectedTitle(title);
	}

	public String getSelectedPanel() {
		return selectedPanel;
	}

	public void setSelectedPanel(String selectedPanel) {
		this.selectedPanel = selectedPanel;
	}

	public String getSelectedTitle() {
		return selectedTitle;
	}

	public void setSelectedTitle(String selectedTitle) {
		if (selectedTitle != null)
			this.selectedTitle = selectedTitle;
	}



}
