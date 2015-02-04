package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.rice.krad.web.form.DocumentFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/17/12
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemBillForm extends DocumentFormBase {

    private String itemBarcode;
    private FeeType feeType;
    private List<FeeType> feeTypes = new ArrayList<FeeType>();

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public List<FeeType> getFeeTypes() {
        return feeTypes;
    }

    public void setFeeTypes(List<FeeType> feeTypes) {
        this.feeTypes = feeTypes;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }
}
