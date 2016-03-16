package org.kuali.ole.pojo;


import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.pojo.edi.EDIOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/4/12
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleOrderRecord {
    private String agendaName;
    private String description;
    private String originalEDIFileName;
    private String oleOriginalBibRecordFileName;
    private EDIOrder originalEdi;
    private OleBibRecord oleBibRecord;
    private OleTxRecord oleTxRecord;
    private BibMarcRecord originalRecord;
    private String bibId;
    private BibId bibTree;
    private String linkToOrderOption;
    private String bibImportProfileName;
    private Integer recordIndex;

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    private Map<String, Object> messageMap = new HashMap();

    public OleBibRecord getOleBibRecord() {
        return oleBibRecord;
    }

    public void setOleBibRecord(OleBibRecord oleBibRecord) {
        this.oleBibRecord = oleBibRecord;
    }

    public OleTxRecord getOleTxRecord() {
        return oleTxRecord;
    }

    public void setOleTxRecord(OleTxRecord oleTxRecord) {
        this.oleTxRecord = oleTxRecord;
    }

    public BibMarcRecord getOriginalRecord() {
        return originalRecord;
    }

    public void setOriginalRecord(BibMarcRecord originalRecord) {
        this.originalRecord = originalRecord;
    }

    public EDIOrder getOriginalEdi() {
        return originalEdi;
    }

    public void setOriginalEdi(EDIOrder originalEdi) {
        this.originalEdi = originalEdi;
    }

    public void addMessageToMap(String key, Object value) {
        messageMap.put(key, value);
    }

    public Map<String, Object> getMessageMap() {
        return messageMap;
    }

    public String getAgendaName() {
        return agendaName;
    }

    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }

    public String getOriginalEDIFileName() {
        return originalEDIFileName;
    }

    public void setOriginalEDIFileName(String originalEDIFileName) {
        this.originalEDIFileName = originalEDIFileName;
    }

    public String getOleOriginalBibRecordFileName() {
        return oleOriginalBibRecordFileName;
    }

    public void setOleOriginalBibRecordFileName(String oleOriginalBibRecordFileName) {
        this.oleOriginalBibRecordFileName = oleOriginalBibRecordFileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BibId getBibTree() {
        return bibTree;
    }

    public void setBibTree(BibId bibTree) {
        this.bibTree = bibTree;
    }

    public String getLinkToOrderOption() {
        return linkToOrderOption;
    }

    public void setLinkToOrderOption(String linkToOrderOption) {
        this.linkToOrderOption = linkToOrderOption;
    }

    @Override
    public String toString() {
        return "OleOrderRecord{" +
                "originalRecord=" + originalRecord +
                ", originalEdi=" + originalEdi +
                ", oleBibRecord=" + oleBibRecord +
                ", oleTxRecord=" + oleTxRecord +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OleOrderRecord that = (OleOrderRecord) o;

        if (!agendaName.equals(that.agendaName)) return false;
        if (!description.equals(that.description)) return false;
        if (!messageMap.equals(that.messageMap)) return false;
        if (!oleBibRecord.equals(that.oleBibRecord)) return false;
        if (!oleOriginalBibRecordFileName.equals(that.oleOriginalBibRecordFileName)) return false;
        if (!oleTxRecord.equals(that.oleTxRecord)) return false;
        if (!originalEDIFileName.equals(that.originalEDIFileName)) return false;
        if (!originalEdi.equals(that.originalEdi)) return false;
        if (!originalRecord.equals(that.originalRecord)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = agendaName.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + originalEDIFileName.hashCode();
        result = 31 * result + oleOriginalBibRecordFileName.hashCode();
        result = 31 * result + originalEdi.hashCode();
        result = 31 * result + oleBibRecord.hashCode();
        result = 31 * result + oleTxRecord.hashCode();
        result = 31 * result + originalRecord.hashCode();
        result = 31 * result + messageMap.hashCode();
        return result;
    }

    public String getBibImportProfileName() {
        return bibImportProfileName;
    }

    public void setBibImportProfileName(String bibImportProfileName) {
        this.bibImportProfileName = bibImportProfileName;
    }

    public Integer getRecordIndex() {
        return recordIndex;
    }

    public void setRecordIndex(Integer recordIndex) {
        this.recordIndex = recordIndex;
    }
}

