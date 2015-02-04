package org.kuali.ole.coa.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;

/**
 * Created by chenchulakshmig on 12/3/14.
 */
public class OLECretePOAccountingLine extends PersistableBusinessObjectBase {

    private String createPOAccountingLineId;

    private String createPOId;

    private String chartOfAccountsCode;

    private String accountNumber;

    private String subAccountNumber;

    private String financialObjectCode;

    private String financialSubObjectCode;

    private String projectCode;

    private String organizationReferenceId;

    private BigDecimal accountLinePercent;

    public String getCreatePOAccountingLineId() {
        return createPOAccountingLineId;
    }

    public void setCreatePOAccountingLineId(String createPOAccountingLineId) {
        this.createPOAccountingLineId = createPOAccountingLineId;
    }

    public String getCreatePOId() {
        return createPOId;
    }

    public void setCreatePOId(String createPOId) {
        this.createPOId = createPOId;
    }

    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSubAccountNumber() {
        return subAccountNumber;
    }

    public void setSubAccountNumber(String subAccountNumber) {
        this.subAccountNumber = subAccountNumber;
    }

    public String getFinancialObjectCode() {
        return financialObjectCode;
    }

    public void setFinancialObjectCode(String financialObjectCode) {
        this.financialObjectCode = financialObjectCode;
    }

    public String getFinancialSubObjectCode() {
        return financialSubObjectCode;
    }

    public void setFinancialSubObjectCode(String financialSubObjectCode) {
        this.financialSubObjectCode = financialSubObjectCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getOrganizationReferenceId() {
        return organizationReferenceId;
    }

    public void setOrganizationReferenceId(String organizationReferenceId) {
        this.organizationReferenceId = organizationReferenceId;
    }

    public BigDecimal getAccountLinePercent() {
        return accountLinePercent;
    }

    public void setAccountLinePercent(BigDecimal accountLinePercent) {
        this.accountLinePercent = accountLinePercent;
    }
}
