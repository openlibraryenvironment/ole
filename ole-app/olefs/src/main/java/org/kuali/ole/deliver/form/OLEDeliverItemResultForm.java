package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OLESingleItemResultDisplayRow;
import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * Created by chenchulakshmig on 1/28/15.
 */
public class OLEDeliverItemResultForm extends UifFormBase {

    private boolean singleItemFlag;

    private OLESingleItemResultDisplayRow oleSingleItemResultDisplayRow;

    public boolean isSingleItemFlag() {
        return singleItemFlag;
    }

    public void setSingleItemFlag(boolean singleItemFlag) {
        this.singleItemFlag = singleItemFlag;
    }

    public OLESingleItemResultDisplayRow getOleSingleItemResultDisplayRow() {
        return oleSingleItemResultDisplayRow;
    }

    public void setOleSingleItemResultDisplayRow(OLESingleItemResultDisplayRow oleSingleItemResultDisplayRow) {
        this.oleSingleItemResultDisplayRow = oleSingleItemResultDisplayRow;
    }
}
