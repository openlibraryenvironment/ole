package org.kuali.ole.bo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 8/16/13
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECheckedOutItems {
    private String code;
    private String message;
    private List<OLECheckedOutItem> checkedOutItems;


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

    public List<OLECheckedOutItem> getCheckedOutItems() {
        return checkedOutItems;
    }

    public void setCheckedOutItems(List<OLECheckedOutItem> checkedOutItems) {
        this.checkedOutItems = checkedOutItems;
    }
}
