package org.kuali.ole.bo;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/20/13
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEHolds {
    private String code;
    private String message;
    private List<OLEHold> oleHoldList;


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

    public List<OLEHold> getOleHoldList() {
        return oleHoldList;
    }

    public void setOleHoldList(List<OLEHold> oleHoldList) {
        this.oleHoldList = oleHoldList;
    }
}
