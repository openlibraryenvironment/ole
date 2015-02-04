package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


/**
 * The OleLoanStatus is a BO class that defines the loan status fields with getters and setters which
 * is used for interacting the loan data with the persistence layer in OLE.
 */
public class OleLoanStatus extends PersistableBusinessObjectBase {

    private String loanStatusId;
    private String loanStatusCode;
    private String loanStatusName;

    /**
     * Gets the loanStatusId attribute.
     *
     * @return Returns the loanStatusId
     */
    public String getLoanStatusId() {
        return loanStatusId;
    }

    /**
     * Sets the loanStatusId attribute value.
     *
     * @param loanStatusId The loanStatusId to set.
     */
    public void setLoanStatusId(String loanStatusId) {
        this.loanStatusId = loanStatusId;
    }

    /**
     * Gets the loanStatusCode attribute.
     *
     * @return Returns the loanStatusCode
     */
    public String getLoanStatusCode() {
        return loanStatusCode;
    }

    /**
     * Sets the loanStatusCode attribute value.
     *
     * @param loanStatusCode The loanStatusCode to set.
     */
    public void setLoanStatusCode(String loanStatusCode) {
        this.loanStatusCode = loanStatusCode;
    }

    /**
     * Gets the loanStatusName attribute.
     *
     * @return Returns the loanStatusName
     */
    public String getLoanStatusName() {
        return loanStatusName;
    }

    /**
     * Sets the loanStatusName attribute value.
     *
     * @param loanStatusName The loanStatusName to set.
     */
    public void setLoanStatusName(String loanStatusName) {
        this.loanStatusName = loanStatusName;
    }
}
