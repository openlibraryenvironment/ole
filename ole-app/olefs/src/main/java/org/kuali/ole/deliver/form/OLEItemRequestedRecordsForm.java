package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * Created by chenchulakshmig on 1/23/15.
 */
public class OLEItemRequestedRecordsForm extends UifFormBase {

    private List<OleDeliverRequestBo> requestBos;

    public List<OleDeliverRequestBo> getRequestBos() {
        return requestBos;
    }

    public void setRequestBos(List<OleDeliverRequestBo> requestBos) {
        this.requestBos = requestBos;
    }
}
