package org.kuali.ole.oleng.batch.profile.model;

/**
 * Created by SheikS on 11/25/2015.
 */
public class BatchProfileMatchPoint {

    private long matchPointId;
    private String dataType;
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
