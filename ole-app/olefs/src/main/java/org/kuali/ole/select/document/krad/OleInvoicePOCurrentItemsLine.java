package org.kuali.ole.select.document.krad;

import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.rice.krad.uif.component.Component;

public class OleInvoicePOCurrentItemsLine {

	private String lineId;
	private String bindPath;
	private OlePurchaseOrderItem poItem;
	private Component rowDetails;

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getBindPath() {
		return bindPath;
	}

	public void setBindPath(String bindPath) {
		this.bindPath = bindPath;
	}

	public OlePurchaseOrderItem getPoItem() {
		return poItem;
	}

	public void setPoItem(OlePurchaseOrderItem poItem) {
		this.poItem = poItem;
	}

	public Component getRowDetails() {
		return rowDetails;
	}

	public void setRowDetails(Component rowDetails) {
		this.rowDetails = rowDetails;
	}

	@Override
	public String toString() {
		return "OleInvoicePOCurrentItemsLine [lineId=" + lineId + ", bindPath="
				+ bindPath + ", poItem=" + poItem + "]";
	}

}

