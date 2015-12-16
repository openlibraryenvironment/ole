package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by SheikS on 11/25/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileMatchPoint {

    private long matchPointId;

    @JsonProperty("matchPointDocType")
    private String dataType;

    @JsonProperty("matchPointValue")
    private String matchPoint;

    public long getMatchPointId() {
        return matchPointId;
    }

    public void setMatchPointId(long matchPointId) {
        this.matchPointId = matchPointId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMatchPoint() {
        return matchPoint;
    }

    public void setMatchPoint(String matchPoint) {
        this.matchPoint = matchPoint;
    }
}
