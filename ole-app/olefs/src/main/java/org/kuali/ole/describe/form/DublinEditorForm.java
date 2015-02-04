package org.kuali.ole.describe.form;

import org.kuali.ole.describe.bo.DublinEditorField;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DublinEditorForm is the Form class for Dublin Editor
 */
public class DublinEditorForm extends UifFormBase {

    private List<DublinEditorField> dublinFieldList = new ArrayList<DublinEditorField>();
    private List<DublinEditorField> existingDublinFieldList = new ArrayList<DublinEditorField>();
    private String message;
    private String uuid;
    private String bibStatus;
    private String updatedBy;
    private String createdBy;
    private Date createdDate;

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        //createdBy = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    private Date updatedDate;

    public String getBibStatus() {
        return bibStatus;
    }

    public void setBibStatus(String bibStatus) {
        this.bibStatus = bibStatus;
    }

    /**
     * Default Constructor.
     * The default behaviour of this object.
     */
    public DublinEditorForm() {
        super();
        dublinFieldList.add(new DublinEditorField());
    }

    /**
     * Gets the dublinFieldList attribute.
     *
     * @return Returns the dublinFieldList.
     */
    public List<DublinEditorField> getDublinFieldList() {
        return dublinFieldList;
    }

    /**
     * Sets the dublinFieldList attribute value.
     *
     * @param dublinFieldList The dublinFieldList to set.
     */
    public void setDublinFieldList(List<DublinEditorField> dublinFieldList) {
        this.dublinFieldList = dublinFieldList;
    }

    /**
     * Gets the existingDublinFieldList attribute.
     *
     * @return Returns the existingDublinFieldList.
     */
    public List<DublinEditorField> getExistingDublinFieldList() {
        return existingDublinFieldList;
    }

    /**
     * Sets the existingDublinFieldList attribute value.
     *
     * @param existingDublinFieldList The existingDublinFieldList.
     */
    public void setExistingDublinFieldList(List<DublinEditorField> existingDublinFieldList) {
        this.existingDublinFieldList = existingDublinFieldList;
    }

    /**
     * Gets the message attribute.
     *
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message attribute value.
     *
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the uuid attribute.
     *
     * @return Returns the uuid.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the uuid attribute value.
     *
     * @param uuid The uuid to set.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
