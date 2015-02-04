package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageBeginning {
    private String messageBeginningOrder;
    private String messageBeginningInterchangeControlReference;
    private String messageBeginningCodeListAgency;

    public String getMessageBeginningOrder() {
        return messageBeginningOrder;
    }

    public void setMessageBeginningOrder(String messageBeginningOrder) {
        this.messageBeginningOrder = messageBeginningOrder;
    }

    public String getMessageBeginningInterchangeControlReference() {
        return messageBeginningInterchangeControlReference;
    }

    public void setMessageBeginningInterchangeControlReference(String messageBeginningInterchangeControlReference) {
        this.messageBeginningInterchangeControlReference = messageBeginningInterchangeControlReference;
    }

    public String getMessageBeginningCodeListAgency() {
        return messageBeginningCodeListAgency;
    }

    public void setMessageBeginningCodeListAgency(String messageBeginningCodeListAgency) {
        this.messageBeginningCodeListAgency = messageBeginningCodeListAgency;
    }
}
