package org.kuali.ole.select.bo;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;

/**
 * OleEventLogBo is the business object class for License Request Maintenance Document.
 */
public class OleEventLogBo extends PersistableBusinessObjectBase {

    private String oleEventLogId;
    private String eventType;
    private Timestamp createdDate;
    private String createdBy;
    private String eventDescription;
    private String oleLicenseRequestId;

    private OleLicenseRequestBo oleLicenseRequestBo = new OleLicenseRequestBo();

    /**
     * Gets the oleEventLogId attribute.
     *
     * @return Returns the oleEventLogId
     */
    public String getOleEventLogId() {
        return oleEventLogId;
    }

    /**
     * Sets the oleEventLogId attribute value.
     *
     * @param oleEventLogId The oleEventLogId to set.
     */
    public void setOleEventLogId(String oleEventLogId) {
        this.oleEventLogId = oleEventLogId;
    }

    /**
     * Gets the eventType attribute.
     *
     * @return Returns the eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the eventType attribute value.
     *
     * @param eventType The eventType to set.
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Gets the createdDate attribute.
     *
     * @return Returns the createdDate
     */
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the createdDate attribute value.
     *
     * @param createdDate The createdDate to set.
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Gets the createdBy attribute.
     *
     * @return Returns the createdBy
     */
    public String getCreatedBy() {
        if (createdBy == null) {
            createdBy = GlobalVariables.getUserSession().getPrincipalName();
        }
        return createdBy;
    }

    /**
     * Sets the createdBy attribute value.
     *
     * @param createdBy The createdBy to set.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the eventDescription attribute.
     *
     * @return Returns the eventDescription
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the eventDescription attribute value.
     *
     * @param eventDescription The eventDescription to set.
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Gets the oleLicenseRequestId attribute.
     *
     * @return Returns the oleLicenseRequestId
     */
    public String getOleLicenseRequestId() {
        return oleLicenseRequestId;
    }

    /**
     * Sets the oleLicenseRequestId attribute value.
     *
     * @param oleLicenseRequestId The oleLicenseRequestId to set.
     */
    public void setOleLicenseRequestId(String oleLicenseRequestId) {
        this.oleLicenseRequestId = oleLicenseRequestId;
    }

    /**
     * Gets the oleLicenseRequestBo attribute.
     *
     * @return Returns the oleLicenseRequestBo
     */
    public OleLicenseRequestBo getOleLicenseRequestBo() {
        return oleLicenseRequestBo;
    }

    /**
     * Sets the oleLicenseRequestBo attribute value.
     *
     * @param oleLicenseRequestBo The oleLicenseRequestBo to set.
     */
    public void setOleLicenseRequestBo(OleLicenseRequestBo oleLicenseRequestBo) {
        this.oleLicenseRequestBo = oleLicenseRequestBo;
    }

    /**
     * set the timestamp attribute value.
     */
    public void setCurrentTimeStamp() {
        final Timestamp now = CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp();
        this.setCreatedDate(now);
    }
}
