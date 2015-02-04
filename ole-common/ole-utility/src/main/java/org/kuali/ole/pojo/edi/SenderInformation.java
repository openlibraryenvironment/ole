package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/5/12
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class SenderInformation {
    private String senderId;
    private String senderIdType;


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderIdType() {
        return senderIdType;
    }

    public void setSenderIdType(String senderIdType) {
        this.senderIdType = senderIdType;
    }
}
