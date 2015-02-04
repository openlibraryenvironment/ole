package org.kuali.ole.select.businessobject;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 7/26/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePurchaseOrderDetailsForInvoice {

    private Date poEndDate;
    private boolean closePO;
    private String poNotes;
    private String purapDocumentIdentifier;

    public Date getPoEndDate() {
        return poEndDate;
    }

    public void setPoEndDate(Date poEndDate) {
        this.poEndDate = poEndDate;
    }

    public String getPurapDocumentIdentifier() {
        return purapDocumentIdentifier;
    }

    public void setPurapDocumentIdentifier(String purapDocumentIdentifier) {
        this.purapDocumentIdentifier = purapDocumentIdentifier;
    }

    public String getPoNotes() {
        return poNotes;
    }

    public void setPoNotes(String poNotes) {
        this.poNotes = poNotes;
    }

    public boolean isClosePO() {
        return closePO;
    }

    public void setClosePO(boolean closePO) {
        this.closePO = closePO;
    }
}
