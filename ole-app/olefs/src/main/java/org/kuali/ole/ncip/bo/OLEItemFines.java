package org.kuali.ole.ncip.bo;


import java.util.List;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/19/13
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEItemFines {
    private String code;
    private String message;
    private List<OLEItemFine> oleItemFineList=new ArrayList<OLEItemFine>();

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

    public List<OLEItemFine> getOleItemFineList() {
        return oleItemFineList;
    }

    public void setOleItemFineList(List<OLEItemFine> oleItemFineList) {
        this.oleItemFineList = oleItemFineList;
    }
}
