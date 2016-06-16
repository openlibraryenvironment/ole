package org.kuali.ole.docstore.common.response;

/**
 * Created by rajeshbabuk on 4/5/16.
 */
public class BatchDeleteSuccessResponse {

    private String matchPoint;
    private String matchPointValue;
    private String bibId;
    private String message;

    public String getMatchPoint() {
        return matchPoint;
    }

    public void setMatchPoint(String matchPoint) {
        this.matchPoint = matchPoint;
    }

    public String getMatchPointValue() {
        return matchPointValue;
    }

    public void setMatchPointValue(String matchPointValue) {
        this.matchPointValue = matchPointValue;
    }

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
