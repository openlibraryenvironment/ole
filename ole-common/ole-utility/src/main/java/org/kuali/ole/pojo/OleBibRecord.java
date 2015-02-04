package org.kuali.ole.pojo;

import org.kuali.ole.docstore.common.document.Bib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/7/12
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleBibRecord {
    //TODO: No need for this unless this map is populated witht the SOlr Query resonse object.
    //TODO: essentially a map of all the indexed fields and its values
    private Map<String, ?> bibAssociatedFieldsValueMap = new HashMap();
    private Bib bib;
    private String bibUUID;
    private String linkedInstanceId;
    private String eInstance;
    private String staffOnlyFlag;

    public String getStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    public void setStaffOnlyFlag(String staffOnlyFlag) {
        this.staffOnlyFlag = staffOnlyFlag;
    }

    public String getBibUUID() {
        return bibUUID;
    }

    public void setBibUUID(String bibUUID) {
        this.bibUUID = bibUUID;
    }

    public String getLinkedInstanceId() {
        return linkedInstanceId;
    }

    public void setLinkedInstanceId(String linkedInstanceId) {
        this.linkedInstanceId = linkedInstanceId;
    }

    public Map<String, ?> getBibAssociatedFieldsValueMap() {
        return bibAssociatedFieldsValueMap;
    }

    public void setBibAssociatedFieldsValueMap(Map<String, ?> bibAssociatedFieldsValueMap) {
        this.bibAssociatedFieldsValueMap = bibAssociatedFieldsValueMap;
    }

    public String geteInstance() {
        return eInstance;
    }

    public void seteInstance(String eInstance) {
        this.eInstance = eInstance;
    }

    public Bib getBib() {
        return bib;
    }

    public void setBib(Bib bib) {
        this.bib = bib;
    }

    @Override
    public String toString() {
        return "OleBibRecord{" +
                "bibAssociatedFieldsValueMap=" + bibAssociatedFieldsValueMap +
                ", bibUUID='" + bibUUID + '\'' +
                ", linkedInstanceId='" + linkedInstanceId + '\'' +
                '}';
    }
}
