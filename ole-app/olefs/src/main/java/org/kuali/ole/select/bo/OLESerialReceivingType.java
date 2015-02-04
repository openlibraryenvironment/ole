package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 10/3/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingType extends PersistableBusinessObjectBase {
    private String serialReceivingTypeId;
    private String serialReceivingRecordId;
    private String receivingRecordType;
    private Date actionDate;
    private String actionInterval;
    private String chronologyCaptionLevel1;
    private String chronologyCaptionLevel2;
    private String chronologyCaptionLevel3;
    private String chronologyCaptionLevel4;
    private String enumerationCaptionLevel1;
    private String enumerationCaptionLevel2;
    private String enumerationCaptionLevel3;
    private String enumerationCaptionLevel4;
    private String enumerationCaptionLevel5;
    private String enumerationCaptionLevel6;
    private OLESerialReceivingDocument oleSerialReceivingDocument;
    private boolean validRecordType = true;

    private boolean documentExist = false;

    public String getReceivingRecordType() {
        return receivingRecordType;
    }

    public void setReceivingRecordType(String receivingRecordType) {
        this.receivingRecordType = receivingRecordType;
    }

    public OLESerialReceivingDocument getOleSerialReceivingDocument() {
        return oleSerialReceivingDocument;
    }

    public void setOleSerialReceivingDocument(OLESerialReceivingDocument oleSerialReceivingDocument) {
        this.oleSerialReceivingDocument = oleSerialReceivingDocument;
    }

    public String getSerialReceivingTypeId() {
        return serialReceivingTypeId;
    }

    public void setSerialReceivingTypeId(String serialReceivingTypeId) {
        this.serialReceivingTypeId = serialReceivingTypeId;
    }

    public String getSerialReceivingRecordId() {
        return serialReceivingRecordId;
    }

    public void setSerialReceivingRecordId(String serialReceivingRecordId) {
        this.serialReceivingRecordId = serialReceivingRecordId;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionInterval() {
        return actionInterval;
    }

    public void setActionInterval(String actionInterval) {
        this.actionInterval = actionInterval;
    }

    public String getChronologyCaptionLevel1() {
        return chronologyCaptionLevel1;
    }

    public void setChronologyCaptionLevel1(String chronologyCaptionLevel1) {
        this.chronologyCaptionLevel1 = chronologyCaptionLevel1;
    }

    public String getChronologyCaptionLevel2() {
        return chronologyCaptionLevel2;
    }

    public void setChronologyCaptionLevel2(String chronologyCaptionLevel2) {
        this.chronologyCaptionLevel2 = chronologyCaptionLevel2;
    }

    public String getChronologyCaptionLevel3() {
        return chronologyCaptionLevel3;
    }

    public void setChronologyCaptionLevel3(String chronologyCaptionLevel3) {
        this.chronologyCaptionLevel3 = chronologyCaptionLevel3;
    }

    public String getChronologyCaptionLevel4() {
        return chronologyCaptionLevel4;
    }

    public void setChronologyCaptionLevel4(String chronologyCaptionLevel4) {
        this.chronologyCaptionLevel4 = chronologyCaptionLevel4;
    }

    public String getEnumerationCaptionLevel1() {
        return enumerationCaptionLevel1;
    }

    public void setEnumerationCaptionLevel1(String enumerationCaptionLevel1) {
        this.enumerationCaptionLevel1 = enumerationCaptionLevel1;
    }

    public String getEnumerationCaptionLevel2() {
        return enumerationCaptionLevel2;
    }

    public void setEnumerationCaptionLevel2(String enumerationCaptionLevel2) {
        this.enumerationCaptionLevel2 = enumerationCaptionLevel2;
    }

    public String getEnumerationCaptionLevel3() {
        return enumerationCaptionLevel3;
    }

    public void setEnumerationCaptionLevel3(String enumerationCaptionLevel3) {
        this.enumerationCaptionLevel3 = enumerationCaptionLevel3;
    }

    public String getEnumerationCaptionLevel4() {
        return enumerationCaptionLevel4;
    }

    public void setEnumerationCaptionLevel4(String enumerationCaptionLevel4) {
        this.enumerationCaptionLevel4 = enumerationCaptionLevel4;
    }

    public String getEnumerationCaptionLevel5() {
        return enumerationCaptionLevel5;
    }

    public void setEnumerationCaptionLevel5(String enumerationCaptionLevel5) {
        this.enumerationCaptionLevel5 = enumerationCaptionLevel5;
    }

    public String getEnumerationCaptionLevel6() {
        return enumerationCaptionLevel6;
    }

    public void setEnumerationCaptionLevel6(String enumerationCaptionLevel6) {
        this.enumerationCaptionLevel6 = enumerationCaptionLevel6;
    }

    public boolean isDocumentExist() {
        return documentExist;
    }

    public void setDocumentExist(boolean documentExist) {
        this.documentExist = documentExist;
    }

    public boolean isValidRecordType() {
        return validRecordType;
    }

    public void setValidRecordType(boolean validRecordType) {
        this.validRecordType = validRecordType;
    }
}
