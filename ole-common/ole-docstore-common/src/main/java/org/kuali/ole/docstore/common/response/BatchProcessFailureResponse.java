package org.kuali.ole.docstore.common.response;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by angelind on 2/11/16.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class BatchProcessFailureResponse {

    @JsonProperty("batchProcessProfileName")
    private String batchProcessProfileName;

    @JsonProperty("responseData")
    private String responseData;

    @JsonProperty("failureReason")
    private String failureReason;

    @JsonIgnore
    private String failedRawMarcContent;

    public String getBatchProcessProfileName() {
        return batchProcessProfileName;
    }

    public void setBatchProcessProfileName(String batchProcessProfileName) {
        this.batchProcessProfileName = batchProcessProfileName;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getFailedRawMarcContent() {
        return failedRawMarcContent;
    }

    public void setFailedRawMarcContent(String failedRawMarcContent) {
        this.failedRawMarcContent = failedRawMarcContent;
    }
}
