package org.kuali.ole.response;

import java.util.Date;

/**
 * Created by sheiks on 27/03/17.
 */
public class PartialIndexStatus extends IndexStatus {

    private String type;
    private Integer fromBibId;
    private Integer toBibId;
    private Date fromDate;
    private Date toDate;

    private static PartialIndexStatus ourInstance = new PartialIndexStatus();

    public static PartialIndexStatus getInstance() {
        return ourInstance;
    }

    private PartialIndexStatus() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFromBibId() {
        return fromBibId;
    }

    public void setFromBibId(Integer fromBibId) {
        this.fromBibId = fromBibId;
    }

    public Integer getToBibId() {
        return toBibId;
    }

    public void setToBibId(Integer toBibId) {
        this.toBibId = toBibId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
