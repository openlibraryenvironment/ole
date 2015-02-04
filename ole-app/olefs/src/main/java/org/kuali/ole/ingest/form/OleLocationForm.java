package org.kuali.ole.ingest.form;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * OleLocationForm is the Form class for Location Document
 */
public class OleLocationForm extends UifFormBase {

    private MultipartFile locationFile;
    private String message;
    private String oleLocationSummaryId;

    /**
     * Gets the locationFile attribute.
     * @return returns the locationFile.
     */
    public MultipartFile getLocationFile() {
        return locationFile;
    }

    /**
     * Sets the locationFile attribute value.
     * @param locationFile The locationFile to set.
     */
    public void setLocationFile(MultipartFile locationFile) {
        this.locationFile = locationFile;
    }

    /**
     * Gets the locationFile attribute.
     * @return returns the locationFile.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message attribute value.
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the oleLocationSummaryId attribute.
     * @return returns the oleLocationSummaryId.
     */
    public String getOleLocationSummaryId() {
        return oleLocationSummaryId;
    }

    /**
     * Sets the oleLocationSummaryId attribute value.
     * @param oleLocationSummaryId The oleLocationSummaryId to set.
     */
    public void setOleLocationSummaryId(String oleLocationSummaryId) {
        this.oleLocationSummaryId = oleLocationSummaryId;
    }
}
