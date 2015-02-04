package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/3/12
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestReOrderForm extends UifFormBase {

    private String itemId;
    private String message;
    private String itemUuid;

    private List<OleDeliverRequestBo> deliverRequestBos = new ArrayList<OleDeliverRequestBo>();

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public List<OleDeliverRequestBo> getDeliverRequestBos() {
        return deliverRequestBos;
    }

    public void setDeliverRequestBos(List<OleDeliverRequestBo> deliverRequestBos) {
        this.deliverRequestBos = deliverRequestBos;

    }
}
