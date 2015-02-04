package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/5/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReceiverInformation {

    private String receiverId;
    private String receiverIdType;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverIdType() {
        return receiverIdType;
    }

    public void setReceiverIdType(String receiverIdType) {
        this.receiverIdType = receiverIdType;
    }
}
