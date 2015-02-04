package org.kuali.ole.pojo;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/11/12
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleEditorResponse {
    private OleBibRecord oleBibRecord;
    private String tokenId;

    public OleBibRecord getOleBibRecord() {
        return oleBibRecord;
    }

    public void setOleBibRecord(OleBibRecord oleBibRecord) {
        this.oleBibRecord = oleBibRecord;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
