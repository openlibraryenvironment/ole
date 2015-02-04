package org.kuali.ole;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/8/12
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */

import org.kuali.ole.pojo.edi.MesssageTypeInformation;

public enum MessageHeaderFixture {
    MesssageTypeInformation("ORDERS",
            "D",
            "96A",
            "UN",
            "EAN008", null
    ),
    InterChangeControlRef(null, null, null, null, null,
            "34"
    ),;

    private String messageTypeId;
    private String constant1;
    private String constant2;
    private String constant3;
    private String constant4;
    private String interchangeControlReference;

    public String getInterchangeControlReference() {
        return interchangeControlReference;
    }

    public void setInterchangeControlReference(String interchangeControlReference) {
        this.interchangeControlReference = interchangeControlReference;
    }


    private MessageHeaderFixture(String messageTypeId, String constant1, String constant2, String constant3,
                                 String constant4, String interchangeControlReference) {
        this.messageTypeId = messageTypeId;
        this.constant1 = constant1;
        this.constant2 = constant2;
        this.constant3 = constant3;
        this.constant4 = constant4;
        this.interchangeControlReference = interchangeControlReference;
    }

    public MesssageTypeInformation createMesssageTypeInformation(Class clazz) {
        MesssageTypeInformation messsageTypeInformation = null;
        try {
            messsageTypeInformation = (MesssageTypeInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("MesssageTypeInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("MesssageTypeInformation creation failed. class = " + clazz);
        }
        messsageTypeInformation.setMessageTypeId(messageTypeId);
        messsageTypeInformation.setConstant1(constant1);
        messsageTypeInformation.setConstant2(constant2);
        messsageTypeInformation.setConstant3(constant3);
        messsageTypeInformation.setConstant4(constant4);
        return messsageTypeInformation;
    }

    public String createInterchangeControlReference() {
        return interchangeControlReference;
    }

}
