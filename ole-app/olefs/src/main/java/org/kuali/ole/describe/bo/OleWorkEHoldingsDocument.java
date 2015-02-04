package org.kuali.ole.describe.bo;

import org.kuali.ole.docstore.common.document.EHoldingsOleml;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 8/1/13
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleWorkEHoldingsDocument extends EHoldingsOleml {
    private boolean select;
    private String bibIdentifier;
    private String holdingsIdentifier;
    private String instanceIdentifier;
    private String staffOnly;

    public String getStaffOnly() {
        return staffOnly;
    }

    public void setStaffOnly(String staffOnly) {
        this.staffOnly = staffOnly;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public String getHoldingsIdentifier() {
        return holdingsIdentifier;
    }

    public void setHoldingsIdentifier(String holdingsIdentifier) {
        this.holdingsIdentifier = holdingsIdentifier;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
