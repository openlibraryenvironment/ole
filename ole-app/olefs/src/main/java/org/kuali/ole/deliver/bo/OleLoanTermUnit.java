package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * The OleLoanTermUnit is a BO class that defines the loan term unit fields with getters and setters which
 * is used for interacting the loan data with the persistence layer in OLE.
 */
public class OleLoanTermUnit extends PersistableBusinessObjectBase {

    private String loanTermUnitId;
    private String loanTermUnitCode;
    private String loanTermUnitName;

    /**
     * Gets the loanTermUnitId attribute.
     *
     * @return Returns the loanTermUnitId
     */
    public String getLoanTermUnitId() {
        return loanTermUnitId;
    }

    /**
     * Sets the loanTermUnitId attribute value.
     *
     * @param loanTermUnitId The loanTermUnitId to set.
     */
    public void setLoanTermUnitId(String loanTermUnitId) {
        this.loanTermUnitId = loanTermUnitId;
    }

    /**
     * Gets the loanTermUnitCode attribute.
     *
     * @return Returns the loanTermUnitCode
     */
    public String getLoanTermUnitCode() {
        return loanTermUnitCode;
    }

    /**
     * Sets the loanTermUnitCode attribute value.
     *
     * @param loanTermUnitCode The loanTermUnitCode to set.
     */
    public void setLoanTermUnitCode(String loanTermUnitCode) {
        this.loanTermUnitCode = loanTermUnitCode;
    }

    /**
     * Gets the loanTermUnitName attribute.
     *
     * @return Returns the loanTermUnitName
     */
    public String getLoanTermUnitName() {
        return loanTermUnitName;
    }

    /**
     * Sets the loanTermUnitName attribute value.
     *
     * @param loanTermUnitName The loanTermUnitName to set.
     */
    public void setLoanTermUnitName(String loanTermUnitName) {
        this.loanTermUnitName = loanTermUnitName;
    }
}
