package org.kuali.ole.vnd.businessobject;

import org.kuali.ole.select.bo.OLEEResPltfrmEventType;
import org.kuali.ole.select.bo.OLELogType;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by srirams on 20/11/14.
 */
public class VendorEventLog extends PersistableBusinessObjectBase {


    private String vendorEventLogId;

    private String logTypeId;

    private String logTypeName;

    private OLELogType logType;

    private String eventTypeId;

    private String eventTypeName;

    private OLEEResPltfrmEventType eventType;

    private String note;

    private Timestamp date;

    private String userId;

    private Integer vendorHeaderGeneratedIdentifier;

    private Integer vendorDetailAssignedIdentifier;

    /*private VendorDetail vendorDetail;*/

    public String getVendorEventLogId() {
        return vendorEventLogId;
    }

    public void setVendorEventLogId(String vendorEventLogId) {
        this.vendorEventLogId = vendorEventLogId;
    }

    public String getLogTypeId() {
        return logTypeId;
    }

    public void setLogTypeId(String logTypeId) {
        this.logTypeId = logTypeId;
    }

    public String getLogTypeName() {
        if (this.logType != null) {
            return logType.getLogTypeName();
        } else if (this.logTypeId != null) {
            Map logTypeMap = new HashMap();
            logTypeMap.put("logTypeId", this.logTypeId);
            OLELogType oleLogType = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLELogType.class, logTypeMap);
            if (oleLogType != null) {
                return oleLogType.getLogTypeName();
            }
        }
        return logTypeName;
    }

    public void setLogTypeName(String logTypeName) {
        this.logTypeName = logTypeName;
    }

    public OLELogType getLogType() {
        return logType;
    }

    public void setLogType(OLELogType logType) {
        this.logType = logType;
    }

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventTypeName() {
        if (this.eventType != null) {
            return eventType.geteResPltfrmEventTypeName();
        } else if (this.eventTypeId != null) {
            Map eventTypeMap = new HashMap();
            eventTypeMap.put("eResPltfrmEventTypeId", this.eventTypeId);
            OLEEResPltfrmEventType oleEventType = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEEResPltfrmEventType.class, eventTypeMap);
            if (oleEventType != null) {
                return oleEventType.geteResPltfrmEventTypeName();
            }
        }
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public OLEEResPltfrmEventType getEventType() {
        return eventType;
    }

    public void setEventType(OLEEResPltfrmEventType eventType) {
        this.eventType = eventType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getVendorHeaderGeneratedIdentifier() {
        return vendorHeaderGeneratedIdentifier;
    }

    public void setVendorHeaderGeneratedIdentifier(Integer vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }

    public Integer getVendorDetailAssignedIdentifier() {
        return vendorDetailAssignedIdentifier;
    }

    public void setVendorDetailAssignedIdentifier(Integer vendorDetailAssignedIdentifier) {
        this.vendorDetailAssignedIdentifier = vendorDetailAssignedIdentifier;
    }

}
