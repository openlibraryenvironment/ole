package org.kuali.ole.deliver.form;

import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

public class OlePatronMaintenanceDocumentForm extends MaintenanceDocumentForm {

	private static final long serialVersionUID = -4515599420718903399L;

	private boolean filterAffiliation = true;
	private boolean filterLibraryPolicies = true;
	private boolean filterNotesSection = true;
	private boolean filterLoanedRecords = true;
	private boolean filterRequestedRecords = true;
	private boolean filterTemporaryCirculationHistoryRecords = true;
	private boolean filterProxySection = true;
	private boolean filterProxyForSection = true;
	private boolean filterPatronLocalIdSection = true;
	private boolean filterInvalidOrLostBarcodeSection = true;

	public boolean isFilterAffiliation() {
		return filterAffiliation;
	}

	public void setFilterAffiliation(boolean filterAffiliation) {
		this.filterAffiliation = filterAffiliation;
	}

	public boolean isFilterLibraryPolicies() {
		return filterLibraryPolicies;
	}

	public void setFilterLibraryPolicies(boolean filterLibraryPolicies) {
		this.filterLibraryPolicies = filterLibraryPolicies;
	}

	public boolean isFilterNotesSection() {
		return filterNotesSection;
	}

	public void setFilterNotesSection(boolean filterNotesSection) {
		this.filterNotesSection = filterNotesSection;
	}

	public boolean isFilterLoanedRecords() {
		return filterLoanedRecords;
	}

	public void setFilterLoanedRecords(boolean filterLoanedRecords) {
		this.filterLoanedRecords = filterLoanedRecords;
	}

	public boolean isFilterRequestedRecords() {
		return filterRequestedRecords;
	}

	public void setFilterRequestedRecords(boolean filterRequestedRecords) {
		this.filterRequestedRecords = filterRequestedRecords;
	}

	public boolean isFilterTemporaryCirculationHistoryRecords() {
		return filterTemporaryCirculationHistoryRecords;
	}

	public void setFilterTemporaryCirculationHistoryRecords(
			boolean filterTemporaryCirculationHistoryRecords) {
		this.filterTemporaryCirculationHistoryRecords = filterTemporaryCirculationHistoryRecords;
	}

	public boolean isFilterProxySection() {
		return filterProxySection;
	}

	public void setFilterProxySection(boolean filterProxySection) {
		this.filterProxySection = filterProxySection;
	}

	public boolean isFilterProxyForSection() {
		return filterProxyForSection;
	}

	public void setFilterProxyForSection(boolean filterProxyForSection) {
		this.filterProxyForSection = filterProxyForSection;
	}

	public boolean isFilterPatronLocalIdSection() {
		return filterPatronLocalIdSection;
	}

	public void setFilterPatronLocalIdSection(boolean filterPatronLocalIdSection) {
		this.filterPatronLocalIdSection = filterPatronLocalIdSection;
	}

	public boolean isFilterInvalidOrLostBarcodeSection() {
		return filterInvalidOrLostBarcodeSection;
	}

	public void setFilterInvalidOrLostBarcodeSection(
			boolean filterInvalidOrLostBarcodeSection) {
		this.filterInvalidOrLostBarcodeSection = filterInvalidOrLostBarcodeSection;
	}

}

