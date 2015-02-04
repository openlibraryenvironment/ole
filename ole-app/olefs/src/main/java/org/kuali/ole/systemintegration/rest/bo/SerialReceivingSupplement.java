package org.kuali.ole.systemintegration.rest.bo;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 3/10/14
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SerialReceivingSupplement {
    private String enumerationCaption;
    private String chronologyCaption;
    private String publicReceiptNote;

    public String getEnumerationCaption() {
        return enumerationCaption;
    }

    public void setEnumerationCaption(String enumerationCaption) {
        this.enumerationCaption = enumerationCaption;
    }

    public String getChronologyCaption() {
        return chronologyCaption;
    }

    public void setChronologyCaption(String chronologyCaption) {
        this.chronologyCaption = chronologyCaption;
    }

    public String getPublicReceiptNote() {
        return publicReceiptNote;
    }

    public void setPublicReceiptNote(String publicReceiptNote) {
        this.publicReceiptNote = publicReceiptNote;
    }
}
