package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchDeliverNotices {

    @JsonProperty("deliverNoticeName")
    private String deliverNoticeName;

    public String getDeliverNoticeName() {
        return deliverNoticeName;
    }

    public void setDeliverNoticeName(String deliverNoticeName) {
        this.deliverNoticeName = deliverNoticeName;
    }
}
