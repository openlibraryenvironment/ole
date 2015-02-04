package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/2/12
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class EDIOrder {
    private String seperators;
    private SenderAndReceiver senderAndReceiver;
    private MessageHeader messageHeader;
    private Message message;
    private List<LineItemOrder> lineItemOrder = new ArrayList<LineItemOrder>();
    private Summary summary;
    private Trailer trailer;

    public void addLineItemOrder(LineItemOrder lineItemOrder) {
        if (!this.lineItemOrder.contains(lineItemOrder)) {
            this.lineItemOrder.add(lineItemOrder);
        }
    }


    public String getSeperators() {
        return seperators;
    }

    public void setSeperators(String seperators) {
        this.seperators = seperators;
    }

    public Trailer getTrailer() {

        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public SenderAndReceiver getSenderAndReceiver() {
        return senderAndReceiver;
    }

    public void setSenderAndReceiver(SenderAndReceiver senderAndReceiver) {
        this.senderAndReceiver = senderAndReceiver;
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<LineItemOrder> getLineItemOrder() {
        return lineItemOrder;
    }

    public void setLineItemOrder(List<LineItemOrder> lineItemOrder) {
        this.lineItemOrder = lineItemOrder;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }
}
