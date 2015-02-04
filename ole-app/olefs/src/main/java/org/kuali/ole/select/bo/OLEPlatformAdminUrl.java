
package org.kuali.ole.select.bo;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;

/**
 * Created by chenchulakshmig on 9/16/14.
 * OLEPlatformAdminUrl provides platform url information through getter and setter.
 */
public class OLEPlatformAdminUrl extends PersistableBusinessObjectBase {

    private String platformAdminUrlId;

    private String olePlatformId;

    private String url;

    private String typeId;

    private String userName;

    private String password;

    private String note;

    private String platfomDocumentNumber;

    private OLEPlatformAdminUrlType olePlatformAdminUrlType;

    private OLEPlatformRecordDocument olePlatformRecordDocument;

    private boolean saveFlag = false;

    public String getPlatformAdminUrlId() {
        return platformAdminUrlId;
    }

    public void setPlatformAdminUrlId(String platformAdminUrlId) {
        this.platformAdminUrlId = platformAdminUrlId;
    }

    public String getOlePlatformId() {
        return olePlatformId;
    }

    public void setOlePlatformId(String olePlatformId) {
        this.olePlatformId = olePlatformId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public OLEPlatformAdminUrlType getOlePlatformAdminUrlType() {
        return olePlatformAdminUrlType;
    }

    public void setOlePlatformAdminUrlType(OLEPlatformAdminUrlType olePlatformAdminUrlType) {
        this.olePlatformAdminUrlType = olePlatformAdminUrlType;
    }

    public OLEPlatformRecordDocument getOlePlatformRecordDocument() {
        return olePlatformRecordDocument;
    }

    public void setOlePlatformRecordDocument(OLEPlatformRecordDocument olePlatformRecordDocument) {
        this.olePlatformRecordDocument = olePlatformRecordDocument;
    }

    public boolean isSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(boolean saveFlag) {
        this.saveFlag = saveFlag;
    }


    public String getPlatfomDocumentNumber() {
        return platfomDocumentNumber;
    }

    public void setPlatfomDocumentNumber(String platfomDocumentNumber) {
        this.platfomDocumentNumber = platfomDocumentNumber;
    }
}
