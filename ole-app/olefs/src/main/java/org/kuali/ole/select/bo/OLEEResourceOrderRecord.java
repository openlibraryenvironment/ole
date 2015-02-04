package org.kuali.ole.select.bo;

import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.pojo.OleBibRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenchulakshmig on 12/4/14.
 */
public class OLEEResourceOrderRecord {

    private OLEEResourceTxnRecord oleEResourceTxnRecord;
    private OleBibRecord oleBibRecord;
    private String oleERSIdentifier;
    private Map<String, Object> messageMap = new HashMap();
    private BibId bibTree;
    private String linkToOrderOption;
    private String bibId;

    public void addMessageToMap(String key, Object value) {
        messageMap.put(key, value);
    }

    public OLEEResourceTxnRecord getOleEResourceTxnRecord() {
        return oleEResourceTxnRecord;
    }

    public void setOleEResourceTxnRecord(OLEEResourceTxnRecord oleEResourceTxnRecord) {
        this.oleEResourceTxnRecord = oleEResourceTxnRecord;
    }

    public OleBibRecord getOleBibRecord() {
        return oleBibRecord;
    }

    public void setOleBibRecord(OleBibRecord oleBibRecord) {
        this.oleBibRecord = oleBibRecord;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public Map<String, Object> getMessageMap() {
        return messageMap;
    }

    public void setMessageMap(Map<String, Object> messageMap) {
        this.messageMap = messageMap;
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

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }
}
