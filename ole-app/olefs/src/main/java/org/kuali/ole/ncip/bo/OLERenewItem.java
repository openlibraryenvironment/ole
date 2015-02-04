package org.kuali.ole.ncip.bo;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/3/13
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERenewItem {
    private String code;
    private String message;
    private String pastDueDate;
    private String newDueDate;
    private String renewalCount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPastDueDate() {
        return pastDueDate;
    }

    public void setPastDueDate(String pastDueDate) {
        this.pastDueDate = pastDueDate;
    }

    public String getNewDueDate() {
        return newDueDate;
    }

    public void setNewDueDate(String newDueDate) {
        this.newDueDate = newDueDate;
    }

    public String getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(String renewalCount) {
        this.renewalCount = renewalCount;
    }
}
