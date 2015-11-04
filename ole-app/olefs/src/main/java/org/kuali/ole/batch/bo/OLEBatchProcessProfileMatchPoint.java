package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/1/14
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileMatchPoint extends PersistableBusinessObjectBase {

    private String matchPointId;
    private String matchPoint;
    private String matchPointType;
    private String batchProcessProfileId;
    private String cascadingMatchPoint;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public String getMatchPointId() {
        return matchPointId;
    }

    public void setMatchPointId(String matchPointId) {
        this.matchPointId = matchPointId;
    }

    public String getMatchPoint() {
        return matchPoint;
    }

    public void setMatchPoint(String matchPoint) {
        this.matchPoint = matchPoint;
    }

    public String getMatchPointType() {
        return matchPointType;
    }

    public void setMatchPointType(String matchPointType) {
        this.matchPointType = matchPointType;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public String getCascadingMatchPoint() {
        return cascadingMatchPoint;
    }

    public void setCascadingMatchPoint(String cascadingMatchPoint) {
        this.cascadingMatchPoint = cascadingMatchPoint;
    }
}
