package org.kuali.ole.select.document;

import org.kuali.ole.select.bo.OLEEResPltfrmEventType;
import org.kuali.ole.select.bo.OLELogType;
import org.kuali.ole.select.bo.OLEProblemType;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arjuns
 * Date: 6/26/13
 * Time: 5:50 PM
 * To change this template use File | Settings | File Templates.
 */


public class OLEEResourceEventLog extends PersistableBusinessObjectBase {
    private String oleEResEventLogID;
    private String oleERSIdentifier;
    private String eventTypeId;
    private String eventTypeName;
    private String eventType;
    private Timestamp eventDate;
    private String eventUser;
    private String eventNote;
    private String logTypeId;
    private String logTypeName;
    private OLEProblemType problemType;
    private String problemTypeId;
    private String problemTypeName;
    private String eventStatus;
    private String eventResolution;
    private Timestamp eventResolvedDate;
    private OLELogType logType;
    private OLEEResourceRecordDocument oleERSDocument;
    private boolean saveFlag;
    private MultipartFile attachmentFile1;
    private String attachmentFileName1;
    private String attachmentMimeType1;
    private byte[] attachmentContent1;
    private MultipartFile attachmentFile2;
    private String attachmentFileName2;
    private String attachmentMimeType2;
    private byte[] attachmentContent2;

    public OLEEResourceEventLog() {
        logTypeId= "1";
    }

    public String getOleEResEventLogID() {
        return oleEResEventLogID;
    }

    public void setOleEResEventLogID(String oleEResEventLogID) {
        this.oleEResEventLogID = oleEResEventLogID;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventUser() {
        if (eventUser == null){
            eventUser= GlobalVariables.getUserSession().getPrincipalName();
        }
        return eventUser;
    }

    public void setEventUser(String eventUser) {
        this.eventUser = eventUser;
    }

    public String getEventNote() {
        return eventNote;
    }

    public void setEventNote(String eventNote) {
        this.eventNote = eventNote;
    }

    public OLEEResourceRecordDocument getOleERSDocument() {
        return oleERSDocument;
    }

    public void setOleERSDocument(OLEEResourceRecordDocument oleERSDocument) {
        this.oleERSDocument = oleERSDocument;
    }

    /**
     * set the timestamp attribute value.
     */
    public void setCurrentTimeStamp() {
        final Timestamp now = CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp();
        this.setEventDate(now);
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

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventResolution() {
        return eventResolution;
    }

    public void setEventResolution(String eventResolution) {
        this.eventResolution = eventResolution;
    }

    public Timestamp getEventResolvedDate() {
        return eventResolvedDate;
    }

    public void setEventResolvedDate(Timestamp eventResolvedDate) {
        this.eventResolvedDate = eventResolvedDate;
    }

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventTypeName() {
        if (this.eventType != null) {
            //return eventType.getEresPltfrmEventTypeName();
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

    public boolean isSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(boolean saveFlag) {
        this.saveFlag = saveFlag;
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
