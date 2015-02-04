package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/2/12
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageHeader {

    private String interchangeControlReference;
    private MesssageTypeInformation messsageTypeInformation;

    public MesssageTypeInformation getMesssageTypeInformation() {
        return messsageTypeInformation;
    }

    public void setMesssageTypeInformation(MesssageTypeInformation messsageTypeInformation) {
        this.messsageTypeInformation = messsageTypeInformation;
    }

    public String getInterchangeControlReference() {
        return interchangeControlReference;
    }

    public void setInterchangeControlReference(String interchangeControlReference) {
        this.interchangeControlReference = interchangeControlReference;
    }


}
