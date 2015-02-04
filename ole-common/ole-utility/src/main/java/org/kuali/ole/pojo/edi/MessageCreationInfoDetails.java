package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageCreationInfoDetails {
    private String messageCreationInfoConstant;
    private String messageCreationInfoDate;
    private String messageCreationInfoDateFormat;

    public String getMessageCreationInfoConstant() {
        return messageCreationInfoConstant;
    }

    public void setMessageCreationInfoConstant(String messageCreationInfoConstant) {
        this.messageCreationInfoConstant = messageCreationInfoConstant;
    }

    public String getMessageCreationInfoDate() {
        return messageCreationInfoDate;
    }

    public void setMessageCreationInfoDate(String messageCreationInfoDate) {
        this.messageCreationInfoDate = messageCreationInfoDate;
    }

    public String getMessageCreationInfoDateFormat() {
        return messageCreationInfoDateFormat;
    }

    public void setMessageCreationInfoDateFormat(String messageCreationInfoDateFormat) {
        this.messageCreationInfoDateFormat = messageCreationInfoDateFormat;
    }

}
