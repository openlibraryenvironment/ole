package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/11/12
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePaymentStatus extends PersistableBusinessObjectBase {

    private String paymentStatusId;
    private String paymentStatusCode;
    private String paymentStatusName;


    public String getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(String paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public String getPaymentStatusName() {
        return paymentStatusName;
    }

    public void setPaymentStatusName(String paymentStatusName) {
        this.paymentStatusName = paymentStatusName;
    }

    public String getPaymentStatusCode() {
        return paymentStatusCode;
    }

    public void setPaymentStatusCode(String paymentStatusCode) {
        this.paymentStatusCode = paymentStatusCode;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("paymentStatusId", paymentStatusId);
        return toStringMap;
    }
}
