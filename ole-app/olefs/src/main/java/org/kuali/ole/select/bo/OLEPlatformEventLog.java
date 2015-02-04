package org.kuali.ole.select.bo;

import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.rice.krad.bo.BusinessObjectBase;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenchulakshmig on 9/17/14.
 * OLEPlatformEventLog provides platform event log information through getter and setter.
 */
public class OLEPlatformEventLog extends PersistableBusinessObjectBase {

    private String platformEventLogId;

    private String olePlatformId;

    private String logTypeId;

    private String logTypeName;

    private String eventTypeId;

    private String eventTypeName;

    private String problemTypeId;

    private String problemTypeName;

    private String eventNote;

    private String eventUserId;

    private Date eventReportedDate;

    private String eventStatus;

    private Date eventResolvedDate;

    private String eventResolution;

    private Integer eventTempId;

    private boolean saveFlag;

    private OLEPlatformRecordDocument olePlatformRecordDocument;

    private OLEEResPltfrmEventType eventType;

    private OLEProblemType problemType;

    private OLELogType logType;

    private MultipartFile attachmentFile1;

    private String attachmentFileName1;

    private String attachmentMimeType1;

    private byte[] attachmentContent1;

    private MultipartFile attachmentFile2;

    private String attachmentFileName2;

    private String attachmentMimeType2;

    private byte[] attachmentContent2;

    public OLEPlatformEventLog() {
        this.setLogTypeId("1");
        this.setEventReportedDate(new Date(System.currentTimeMillis()));
        if (GlobalVariables.getUserSession() != null) {
            this.setEventUserId(GlobalVariables.getUserSession().getPrincipalId());
        }
    }

    public String getPlatformEventLogId() {
        return platformEventLogId;
    }

    public void setPlatformEventLogId(String platformEventLogId) {
        this.platformEventLogId = platformEventLogId;
    }

    public String getOlePlatformId() {
        return olePlatformId;
    }

    public void setOlePlatformId(String olePlatformId) {
        this.olePlatformId = olePlatformId;
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

    public String getEventNote() {
        return eventNote;
    }

    public void setEventNote(String eventNote) {
        this.eventNote = eventNote;
    }

    public String getEventUserId() {
        return eventUserId;
    }

    public void setEventUserId(String eventUserId) {
        this.eventUserId = eventUserId;
    }

    public Date getEventReportedDate() {
        return eventReportedDate;
    }

    public void setEventReportedDate(Date eventReportedDate) {
        this.eventReportedDate = eventReportedDate;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Date getEventResolvedDate() {
        return eventResolvedDate;
    }

    public void setEventResolvedDate(Date eventResolvedDate) {
        this.eventResolvedDate = eventResolvedDate;
    }

    public String getEventResolution() {
        return eventResolution;
    }

    public void setEventResolution(String eventResolution) {
        this.eventResolution = eventResolution;
    }

    public Integer getEventTempId() {
        return eventTempId;
    }

    public void setEventTempId(Integer eventTempId) {
        this.eventTempId = eventTempId;
    }

    public boolean isSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(boolean saveFlag) {
        this.saveFlag = saveFlag;
    }

    public OLEPlatformRecordDocument getOlePlatformRecordDocument() {
        return olePlatformRecordDocument;
    }

    public void setOlePlatformRecordDocument(OLEPlatformRecordDocument olePlatformRecordDocument) {
        this.olePlatformRecordDocument = olePlatformRecordDocument;
    }

    public String getProblemTypeId() {
        return problemTypeId;
    }

    public void setProblemTypeId(String problemTypeId) {
        this.problemTypeId = problemTypeId;
    }

    public String getProblemTypeName() {
        if (this.problemType != null) {
            return problemType.getProblemTypeName();
        } else if (this.problemTypeId != null) {
            Map problemTypeMap = new HashMap();
            problemTypeMap.put("problemTypeId", this.problemTypeId);
            OLEProblemType oleProblemType = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEProblemType.class, problemTypeMap);
            if (oleProblemType != null) {
                return oleProblemType.getProblemTypeName();
            }
        }
        return problemTypeName;
    }

    public void setProblemTypeName(String problemTypeName) {
        this.problemTypeName = problemTypeName;
    }

    public OLEEResPltfrmEventType getEventType() {
        return eventType;
    }

    public void setEventType(OLEEResPltfrmEventType eventType) {
        this.eventType = eventType;
    }

    public OLEProblemType getProblemType() {
        return problemType;
    }

    public void setProblemType(OLEProblemType problemType) {
        this.problemType = problemType;
    }

    public OLELogType getLogType() {
        return logType;
    }

    public void setLogType(OLELogType logType) {
        this.logType = logType;
    }

    public MultipartFile getAttachmentFile1() {
        return attachmentFile1;
    }

    public void setAttachmentFile1(MultipartFile attachmentFile1) {
        this.attachmentFile1 = attachmentFile1;
    }

    public String getAttachmentFileName1() {
        return attachmentFileName1;
    }

    public void setAttachmentFileName1(String attachmentFileName1) {
        this.attachmentFileName1 = attachmentFileName1;
    }

    public String getAttachmentMimeType1() {
        return attachmentMimeType1;
    }

    public void setAttachmentMimeType1(String attachmentMimeType1) {
        this.attachmentMimeType1 = attachmentMimeType1;
    }

    public byte[] getAttachmentContent1() {
        return attachmentContent1;
    }

    public void setAttachmentContent1(byte[] attachmentContent1) {
        this.attachmentContent1 = attachmentContent1;
    }

    public MultipartFile getAttachmentFile2() {
        return attachmentFile2;
    }

    public void setAttachmentFile2(MultipartFile attachmentFile2) {
        this.attachmentFile2 = attachmentFile2;
    }

    public String getAttachmentFileName2() {
        return attachmentFileName2;
    }

    public void setAttachmentFileName2(String attachmentFileName2) {
        this.attachmentFileName2 = attachmentFileName2;
    }

    public String getAttachmentMimeType2() {
        return attachmentMimeType2;
    }

    public void setAttachmentMimeType2(String attachmentMimeType2) {
        this.attachmentMimeType2 = attachmentMimeType2;
    }

    public byte[] getAttachmentContent2() {
        return attachmentContent2;
    }

    public void setAttachmentContent2(byte[] attachmentContent2) {
        this.attachmentContent2 = attachmentContent2;
    }
}
