package org.kuali.ole;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/7/12
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */

import org.kuali.ole.pojo.edi.ReceiverInformation;
import org.kuali.ole.pojo.edi.Schedule;
import org.kuali.ole.pojo.edi.SenderInformation;
import org.kuali.ole.pojo.edi.SendersAndReceiversConstant;

public enum SendersAndReceiverFixture {


    SendersAndReceiversConstant("UNOC",
            "3", null, null, null, null, null, null, null
    ),
    SenderInformation(null, null, "DUL-WCS",
            "ZZ", null, null, null, null, null
    ),
    ReceiverInformation(null, null, null, null, "HAR2",
            "ZZ", null, null, null
    ),
    Schedule(null, null, null, null, null, null, "111026",
            "1215", null),
    InterchangeControlReference(null, null, null, null, null, null, null, null, "34"),;

    private String code;
    private String value;
    private String senderId;
    private String senderIdType;
    private String receiverId;
    private String receiverIdType;
    private String preparationDate;
    private String preparationTime;
    private String interChangeControlReference;

    public String getInterChangeControlReference() {
        return interChangeControlReference;
    }

    public void setInterChangeControlReference(String interChangeControlReference) {
        this.interChangeControlReference = interChangeControlReference;
    }

    private SendersAndReceiverFixture(String code, String value, String senderId, String senderIdType,
                                      String receiverId, String receiverIdType, String preparationDate,
                                      String preparationTime, String interChangeControlReference) {
        this.code = code;
        this.value = value;
        this.senderId = senderId;
        this.senderIdType = senderIdType;
        this.receiverId = receiverId;
        this.receiverIdType = receiverIdType;
        this.preparationDate = preparationDate;
        this.preparationTime = preparationTime;
        this.interChangeControlReference = interChangeControlReference;
    }

    public SendersAndReceiversConstant createSenderAndReceiverConstant(Class clazz) {
        // SenderAndReceiver senderAndReceiver= null;
        SendersAndReceiversConstant sendersAndReceiversConstant = null;
        try {
            sendersAndReceiversConstant = (SendersAndReceiversConstant) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("SendersAndReceiversConstant creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SendersAndReceiversConstant creation failed. class = " + clazz);
        }
        sendersAndReceiversConstant.setCode(code);
        sendersAndReceiversConstant.setValue(value);

        return sendersAndReceiversConstant;
    }

    public SenderInformation createSenderInformation(Class clazz) {
        SenderInformation senderInformation = null;
        try {
            senderInformation = (SenderInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("SenderInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SenderInformation creation failed. class = " + clazz);
        }
        senderInformation.setSenderId(senderId);
        senderInformation.setSenderIdType(senderIdType);

        return senderInformation;
    }

    public ReceiverInformation createReceiverInformation(Class clazz) {
        ReceiverInformation receiverInformation = null;
        try {
            receiverInformation = (ReceiverInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("ReceiverInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("ReceiverInformation creation failed. class = " + clazz);
        }
        receiverInformation.setReceiverId(receiverId);
        receiverInformation.setReceiverIdType(receiverIdType);

        return receiverInformation;
    }

    public Schedule createSchedule(Class clazz) {
        Schedule schedule = null;
        try {
            schedule = (Schedule) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Schedule creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Schedule creation failed. class = " + clazz);
        }
        schedule.setPreparationDate(preparationDate);
        schedule.setPreparationTime(preparationTime);
        return schedule;
    }

    public String createInterchangeControlReference() {
        return getInterChangeControlReference();
    }


}
