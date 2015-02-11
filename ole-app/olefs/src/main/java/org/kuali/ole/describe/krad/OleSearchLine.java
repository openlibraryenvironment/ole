package org.kuali.ole.describe.krad;

import org.kuali.ole.describe.bo.SearchResultDisplayRow;

public class OleSearchLine {

	private int lineNumber;
	private String lineId;
	private String bindPath;
	private SearchResultDisplayRow row;

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

	public SearchResultDisplayRow getRow() {
		return row;
	}

	public void setRow(SearchResultDisplayRow row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return "OleSearchLine [row=" + row + "]";
	}

}

