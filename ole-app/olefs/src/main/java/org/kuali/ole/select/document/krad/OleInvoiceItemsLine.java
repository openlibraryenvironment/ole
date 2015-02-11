package org.kuali.ole.select.document.krad;

import java.util.List;

import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.widget.QuickFinder;

public class OleInvoiceItemsLine {

	private int lineNumber;
	private String lineId;
	private String bindPath;
	private OleInvoiceItem item;
	private List<Action> actions;
	private QuickFinder relinkQuickfinder;
	private Component rowDetails;

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

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

	public OleInvoiceItem getItem() {
		return item;
	}

	public void setItem(OleInvoiceItem item) {
		this.item = item;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public QuickFinder getRelinkQuickfinder() {
		return relinkQuickfinder;
	}

	public void setRelinkQuickfinder(QuickFinder relinkQuickfinder) {
		this.relinkQuickfinder = relinkQuickfinder;
	}

	public Component getRowDetails() {
		return rowDetails;
	}

	public void setRowDetails(Component rowDetails) {
		this.rowDetails = rowDetails;
	}

	@Override
	public String toString() {
		return "OleInvoiceItemsLine [lineId=" + lineId + ", bindPath="
				+ bindPath + ", item=" + item + "]";
	}

}

