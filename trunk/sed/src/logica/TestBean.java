package logica;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.edu.udistrital.core.common.controller.BackingBean;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@ManagedBean
@ViewScoped
@URLMapping(id = "testBean", pattern = "/portal/test", viewId = "/pages/test/test.jspx")
public class TestBean extends BackingBean {

	private boolean showList = false, showAdd = false, showDetail = false;

	public TestBean() {
		try {
			setShowList(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** @author MTorres */
	public boolean getValidateSedUserRole(Long idSedRole) throws Exception {
		try {
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void goDetail() {
		try {
			hideAll();
			setShowDetail(true);
			setPanelView("detailTest", "Detallar test", "TestBean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goAdd() {
		try {
			hideAll();
			setShowAdd(true);
			setPanelView("addTest", "Detallar test", "TestBean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hideAll() {
		try {
			this.showAdd = this.showDetail = this.showList = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isShowList() {
		return showList;
	}

	public void setShowList(boolean showList) {
		this.showList = showList;
	}

	public boolean isShowAdd() {
		return showAdd;
	}

	public void setShowAdd(boolean showAdd) {
		this.showAdd = showAdd;
	}

	public boolean isShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}


}
