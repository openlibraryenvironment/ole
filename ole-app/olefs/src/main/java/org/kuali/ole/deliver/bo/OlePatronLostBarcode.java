package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OlePatronLostBarcodeContract;
import org.kuali.ole.deliver.api.OlePatronLostBarcodeDefinition;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;

/**
 * OlePatronDocument provides OlePatronDocument information through getter and setter.
 */
public class OlePatronLostBarcode extends PersistableBusinessObjectBase implements OlePatronLostBarcodeContract {

    private String olePatronLostBarcodeId;
    private String olePatronId;
    private Date invalidOrLostBarcodeEffDate;
    private String invalidOrLostBarcodeNumber;
    private OlePatronDocument olePatronDocument;
    private boolean revertBarcode;
    private String status;
    private String description;
    private boolean active;
    public String getOlePatronLostBarcodeId() {
        return olePatronLostBarcodeId;
    }

    public void setOlePatronLostBarcodeId(String olePatronLostBarcodeId) {
        this.olePatronLostBarcodeId = olePatronLostBarcodeId;
    }

    public String getOlePatronId() {
        return olePatronId;
    }

    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    public Date getInvalidOrLostBarcodeEffDate() {
        return invalidOrLostBarcodeEffDate;
    }

    public void setInvalidOrLostBarcodeEffDate(Date invalidOrLostBarcodeEffDate) {
        this.invalidOrLostBarcodeEffDate = invalidOrLostBarcodeEffDate;
    }

    public String getInvalidOrLostBarcodeNumber() {
        return invalidOrLostBarcodeNumber;
    }

    public void setInvalidOrLostBarcodeNumber(String invalidOrLostBarcodeNumber) {
        this.invalidOrLostBarcodeNumber = invalidOrLostBarcodeNumber;
    }

    public OlePatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OlePatronNotes into immutable object OlePatronNotesDefinition
     *
     * @param bo
     * @return OlePatronNotesDefinition
     */
    public static OlePatronLostBarcodeDefinition to(org.kuali.ole.deliver.bo.OlePatronLostBarcode bo) {
        if (bo == null) {
            return null;
        }
        return OlePatronLostBarcodeDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OlePatronNotesDefinition into PersistableBusinessObjectBase OlePatronNotes
     *
     * @param im
     * @return bo
     */
    public static org.kuali.ole.deliver.bo.OlePatronLostBarcode from(OlePatronLostBarcodeDefinition im) {
        if (im == null) {
            return null;
        }

        org.kuali.ole.deliver.bo.OlePatronLostBarcode bo = new org.kuali.ole.deliver.bo.OlePatronLostBarcode();
        bo.olePatronLostBarcodeId = im.getOlePatronLostBarcodeId();
        bo.olePatronId = im.getOlePatronId();
        //bo.olePatron = OlePatronDocument.from(im.getOlePatron());
        bo.invalidOrLostBarcodeEffDate = im.getInvalidOrLostBarcodeEffDate();
        bo.invalidOrLostBarcodeNumber = im.getInvalidOrLostBarcodeNumber();
        bo.versionNumber = im.getVersionNumber();

        return bo;
    }

    public boolean isRevertBarcode() {
        return revertBarcode;
    }

    public void setRevertBarcode(boolean revertBarcode) {
        this.revertBarcode = revertBarcode;
    }

    @Override
    public String getId() {
        return this.olePatronLostBarcodeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}