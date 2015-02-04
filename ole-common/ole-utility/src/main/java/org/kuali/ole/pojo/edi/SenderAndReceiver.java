package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/2/12
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class SenderAndReceiver {
    private SendersAndReceiversConstant sendersAndReceiversConstant;
    private SenderInformation senderInformation;
    private ReceiverInformation receiverInformation;
    private Schedule schedule;
    private String interChangeControlReference;
    private String field6;
    private String field7;
    private String field8;
    private String field9;
    private String field10;
    private String field11;


    public SendersAndReceiversConstant getSendersAndReceiversConstant() {
        return sendersAndReceiversConstant;
    }

    public void setSendersAndReceiversConstant(SendersAndReceiversConstant sendersAndReceiversConstant) {
        this.sendersAndReceiversConstant = sendersAndReceiversConstant;
    }

    public SenderInformation getSenderInformation() {
        return senderInformation;
    }

    public void setSenderInformation(SenderInformation senderInformation) {
        this.senderInformation = senderInformation;
    }

    public ReceiverInformation getReceiverInformation() {
        return receiverInformation;
    }

    public void setReceiverInformation(ReceiverInformation receiverInformation) {
        this.receiverInformation = receiverInformation;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getInterChangeControlReference() {
        return interChangeControlReference;
    }

    public void setInterChangeControlReference(String interChangeControlReference) {
        this.interChangeControlReference = interChangeControlReference;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getField10() {
        return field10;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }

    public String getField11() {
        return field11;
    }

    public void setField11(String field11) {
        this.field11 = field11;
    }
}
