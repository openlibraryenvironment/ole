package org.kuali.ole.docstore.common.response;

/**
 * Created by rajeshbabuk on 4/21/16.
 */
public class BatchResponse {

    private String bibId;
    private String message;

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
