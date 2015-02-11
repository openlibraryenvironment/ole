package org.kuali.ole.select.document.krad;

import java.math.BigDecimal;
import java.util.List;

import org.kuali.ole.module.purap.businessobject.InvoiceAccount;
import org.kuali.ole.select.businessobject.OlePurchaseOrderAccount;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.widget.QuickFinder;

public class OleInvoiceAccountingLine {

	private String lineId;
	private String bindPath;

	private String chartOfAccountsCode;
	private String accountNumber;
	private String subAccountNumber;
	private String financialObjectCode;
	private String financialSubObjectCode;
	private String projectCode;
	private String orgRefId;
	private BigDecimal amount;
	private BigDecimal percent;

	private QuickFinder accountNumberQuickfinder;
	private QuickFinder subAccountNumberQuickfinder;
	private QuickFinder financialObjectCodeQuickfinder;
	private QuickFinder financialSubObjectCodeQuickfinder;
	private QuickFinder projectCodeQuickfinder;
	private List<Action> actions;

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

	public void setPoAccount(OlePurchaseOrderAccount poAccount) {
		chartOfAccountsCode = poAccount.getChartOfAccountsCode();
		accountNumber = poAccount.getAccountNumber();
		subAccountNumber = poAccount.getSubAccountNumber();
		financialObjectCode = poAccount.getFinancialObjectCode();
		financialSubObjectCode = poAccount.getFinancialSubObjectCode();
		projectCode = poAccount.getProjectCode();
		orgRefId = poAccount.getOrganizationReferenceId();
		amount = poAccount.getAmount() == null ? null : poAccount.getAmount()
				.bigDecimalValue();
		percent = poAccount.getAccountLinePercent();
	}

	public void setInvoiceAccount(InvoiceAccount invAccount) {
		chartOfAccountsCode = invAccount.getChartOfAccountsCode();
		accountNumber = invAccount.getAccountNumber();
		subAccountNumber = invAccount.getSubAccountNumber();
		financialObjectCode = invAccount.getFinancialObjectCode();
		financialSubObjectCode = invAccount.getFinancialSubObjectCode();
		projectCode = invAccount.getProjectCode();
		orgRefId = invAccount.getOrganizationReferenceId();
		amount = invAccount.getAmount() == null ? null : invAccount.getAmount()
				.bigDecimalValue();
		percent = invAccount.getAccountLinePercent();
	}

	public String getChartOfAccountsCode() {
		return chartOfAccountsCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getSubAccountNumber() {
		return subAccountNumber;
	}

	public String getFinancialObjectCode() {
		return financialObjectCode;
	}

	public String getFinancialSubObjectCode() {
		return financialSubObjectCode;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public String getOrgRefId() {
		return orgRefId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public QuickFinder getAccountNumberQuickfinder() {
		return accountNumberQuickfinder;
	}

	public void setAccountNumberQuickfinder(QuickFinder accountNumberQuickfinder) {
		this.accountNumberQuickfinder = accountNumberQuickfinder;
	}

	public QuickFinder getSubAccountNumberQuickfinder() {
		return subAccountNumberQuickfinder;
	}

	public void setSubAccountNumberQuickfinder(
			QuickFinder subAccountNumberQuickfinder) {
		this.subAccountNumberQuickfinder = subAccountNumberQuickfinder;
	}

	public QuickFinder getFinancialObjectCodeQuickfinder() {
		return financialObjectCodeQuickfinder;
	}

	public void setFinancialObjectCodeQuickfinder(
			QuickFinder financialObjectCodeQuickfinder) {
		this.financialObjectCodeQuickfinder = financialObjectCodeQuickfinder;
	}

	public QuickFinder getFinancialSubObjectCodeQuickfinder() {
		return financialSubObjectCodeQuickfinder;
	}

	public void setFinancialSubObjectCodeQuickfinder(
			QuickFinder financialSubObjectCodeQuickfinder) {
		this.financialSubObjectCodeQuickfinder = financialSubObjectCodeQuickfinder;
	}

	public QuickFinder getProjectCodeQuickfinder() {
		return projectCodeQuickfinder;
	}

	public void setProjectCodeQuickfinder(QuickFinder projectCodeQuickfinder) {
		this.projectCodeQuickfinder = projectCodeQuickfinder;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	@Override
	public String toString() {
		return "OleInvoicePOAccountingLine [lineId=" + lineId + ", bindPath="
				+ bindPath + ", chartOfAccountsCode=" + chartOfAccountsCode
				+ ", accountNumber=" + accountNumber + ", subAccountNumber="
				+ subAccountNumber + ", financialObjectCode="
				+ financialObjectCode + ", financialSubObjectCode="
				+ financialSubObjectCode + ", projectCode=" + projectCode
				+ ", orgRefId=" + orgRefId + ", amount=" + amount
				+ ", percent=" + percent + "]";
	}

}

