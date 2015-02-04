package org.kuali.ole.select.bo;

/**
 * Created by srirams on 18/9/14.
 */
public class OLEEResourceChangeDashBoard {

    private String date;
    private String eResource;
    private String title;
    private String type;
    private String details;
    private String origin;
    private boolean approve;
    private boolean clear;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String geteResource() {
        return eResource;
    }

    public void seteResource(String eResource) {
        this.eResource = eResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }
}
