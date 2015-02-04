package org.kuali.ole.ingest.pojo;

import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.util.List;

/**
 * OlePatronAffiliations is a business object class for Ole Patron Affiliation Document
 */
public class OlePatronEmployments {

    private String employeeId;
    private boolean primary;
    private String employeeStatusCode;
    private String employeeTypeCode;
    private KualiDecimal baseSalaryAmount;
    private String primaryDepartmentCode;
    private boolean active;

    /**
     * Gets the employeeId attribute.
     * @return  Returns the employeeId.
     */
    public String getEmployeeId() {
        return employeeId;
    }
    /**
     * Sets the employeeId attribute value.
     * @param employeeId The employeeId to set.
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the primary attribute.
     * @return  Returns the primary.
     */
    public boolean getPrimary() {
        return primary;
    }
    /**
     * Sets the primary attribute value.
     * @param primary The primary to set.
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
    /**
     * Gets the employeeStatusCode attribute.
     * @return  Returns the employeeStatusCode.
     */
    public String getEmployeeStatusCode() {
        return employeeStatusCode;
    }
    /**
     * Sets the employeeStatusCode attribute value.
     * @param employeeStatusCode The employeeStatusCode to set.
     */
    public void setEmployeeStatusCode(String employeeStatusCode) {
        this.employeeStatusCode = employeeStatusCode;
    }
    /**
     * Gets the employeeTypeCode attribute.
     * @return  Returns the employeeTypeCode.
     */
    public String getEmployeeTypeCode() {
        return employeeTypeCode;
    }
    /**
     * Sets the employeeTypeCode attribute value.
     * @param employeeTypeCode The employeeTypeCode to set.
     */
    public void setEmployeeTypeCode(String employeeTypeCode) {
        this.employeeTypeCode = employeeTypeCode;
    }
    /**
     * Gets the baseSalaryAmount attribute.
     * @return  Returns the baseSalaryAmount.
     */
    public KualiDecimal getBaseSalaryAmount() {
        return baseSalaryAmount;
    }

    /**
     * Sets the baseSalaryAmount attribute value.
     * @param baseSalaryAmount The baseSalaryAmount to set.
     */
    public void setBaseSalaryAmount(KualiDecimal baseSalaryAmount) {
        this.baseSalaryAmount = baseSalaryAmount;
    }

    /**
     * Gets the primaryDepartmentCode attribute.
     * @return  Returns the primaryDepartmentCode.
     */
    public String getPrimaryDepartmentCode() {
        return primaryDepartmentCode;
    }
    /**
     * Sets the primaryDepartmentCode attribute value.
     * @param primaryDepartmentCode The primaryDepartmentCode to set.
     */
    public void setPrimaryDepartmentCode(String primaryDepartmentCode) {
        this.primaryDepartmentCode = primaryDepartmentCode;
    }

    /**
     * Gets the active attribute.
     * @return  Returns the active.
     */
    public boolean isActive() {
        return active;
    }
    /**
     * Sets the active attribute value.
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
