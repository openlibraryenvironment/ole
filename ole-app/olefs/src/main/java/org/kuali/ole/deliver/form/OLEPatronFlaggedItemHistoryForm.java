package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OLEFlaggedItems;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 5/6/14
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPatronFlaggedItemHistoryForm extends UifFormBase {
    private String itemBarcode;
    private List<OLEFlaggedItems> itemsList=new ArrayList<OLEFlaggedItems>();

    public List<OLEFlaggedItems> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<OLEFlaggedItems> itemsList) {
        this.itemsList = itemsList;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
}
