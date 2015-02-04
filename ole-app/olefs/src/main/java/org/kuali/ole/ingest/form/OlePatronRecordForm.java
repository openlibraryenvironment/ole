package org.kuali.ole.ingest.form;

import org.kuali.ole.deliver.bo.OleAddressSourceBo;
import org.kuali.ole.deliver.bo.OleSourceBo;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * OlePatronRecordForm is the Form class for Patron Record Document
 */
public class OlePatronRecordForm extends UifFormBase {

    private MultipartFile patronFile;
    private String message;
    private boolean addUnmatchedPatron;
    private boolean deletePatron;
    private String olePatronSummaryId;
    private String patronAddressSource;
    private OleSourceBo oleSourceBo=new OleSourceBo();


    /**
     * Gets the olePatronSummaryId attribute.
     * @return Returns the olePatronSummaryId.
     */
    public String getOlePatronSummaryId() {
        return olePatronSummaryId;
    }
    /**
     * Sets the olePatronSummaryId attribute value.
     * @param olePatronSummaryId The olePatronSummaryId to set.
     */
    public void setOlePatronSummaryId(String olePatronSummaryId) {
        this.olePatronSummaryId = olePatronSummaryId;
    }
    /**
     * Gets the addUnmatchedPatron attribute.
     * @return Returns the addUnmatchedPatron.
     */
    public boolean isAddUnmatchedPatron() {
        return addUnmatchedPatron;
    }
    /**
     * Sets the addUnmatchedPatron attribute value.
     * @param addUnmatchedPatron The addUnmatchedPatron to set.
     */
    public void setAddUnmatchedPatron(boolean addUnmatchedPatron) {
        this.addUnmatchedPatron = addUnmatchedPatron;
    }
    /**
     * Gets the patronFile attribute.
     * @return Returns the patronFile.
     */
    public MultipartFile getPatronFile() {
        return patronFile;
    }
    /**
     * Sets the patronFile attribute value.
     * @param patronFile The patronFile to set.
     */
    public void setPatronFile(MultipartFile patronFile) {
        this.patronFile = patronFile;
    }
    /**
     * Gets the message attribute.
     * @return Returns the message.
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
     * Gets the deletePatron attribute.
     * @return Returns the deletePatron.
     */
    public boolean isDeletePatron() {

        return deletePatron;
    }
    /**
     * Sets the deletePatron attribute value.
     * @param deletePatron The deletePatron to set.
     */
    public void setDeletePatron(boolean deletePatron) {
        this.deletePatron = deletePatron;
    }

    public String getPatronAddressSource() {
        return patronAddressSource;
    }

    public void setPatronAddressSource(String patronAddressSource) {
        this.patronAddressSource = patronAddressSource;
    }

    public OleSourceBo getOleSourceBo() {
        return oleSourceBo;
    }

    public void setOleSourceBo(OleSourceBo oleSourceBo) {
        this.oleSourceBo = oleSourceBo;
    }


}
