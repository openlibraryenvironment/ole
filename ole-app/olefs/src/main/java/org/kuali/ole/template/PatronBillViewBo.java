package org.kuali.ole.template;

import java.util.List;

/**
 * Created by gopalp on 2/18/16.
 */
public class PatronBillViewBo {

    private String patronId;
    private String patronName;
    private String patronAddress;
    private List<PatronBillItemView> patronBillItemViewList;


    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getPatronName() {
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }

    public String getPatronAddress() {
        return patronAddress;
    }

    public void setPatronAddress(String patronAddress) {
        this.patronAddress = patronAddress;
    }

    public List<PatronBillItemView> getPatronBillItemViewList() {
        return patronBillItemViewList;
    }

    public void setPatronBillItemViewList(List<PatronBillItemView> patronBillItemViewList) {
        this.patronBillItemViewList = patronBillItemViewList;
    }
}
