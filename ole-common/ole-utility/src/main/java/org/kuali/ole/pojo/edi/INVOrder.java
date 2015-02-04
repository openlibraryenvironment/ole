package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/26/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class INVOrder {
    private MessageHeader messageHeader;
    private InvoiceMessage message;
    private List<LineItemOrder> lineItemOrder = new ArrayList<LineItemOrder>();
    private Summary summary;
    private Trailer trailer;

    public void addLineItemOrder(LineItemOrder lineItemOrder) {
        if (!this.lineItemOrder.contains(lineItemOrder)) {
            this.lineItemOrder.add(lineItemOrder);
        }
    }


    public Trailer getTrailer() {

        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public InvoiceMessage getMessage() {
        return message;
    }

    public void setMessage(InvoiceMessage message) {
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
