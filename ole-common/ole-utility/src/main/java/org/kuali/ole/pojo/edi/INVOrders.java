package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/26/13
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class INVOrders {
    private String seperators;
    private SenderAndReceiver senderAndReceiver;
    private List<INVOrder> invOrder = new ArrayList<>();

    public void addInvOrder(INVOrder invOrder) {
        if (!this.invOrder.contains(invOrder)) {
            this.invOrder.add(invOrder);
        }
    }

    public String getSeperators() {
        return seperators;
    }

    public void setSeperators(String seperators) {
        this.seperators = seperators;
    }

    public SenderAndReceiver getSenderAndReceiver() {
        return senderAndReceiver;
    }

    public void setSenderAndReceiver(SenderAndReceiver senderAndReceiver) {
        this.senderAndReceiver = senderAndReceiver;
    }

    public List<INVOrder> getInvOrder() {
        return invOrder;
    }

    public void setInvOrder(List<INVOrder> invOrder) {
        this.invOrder = invOrder;
    }
}
