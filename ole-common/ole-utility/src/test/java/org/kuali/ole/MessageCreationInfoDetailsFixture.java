package org.kuali.ole;

import org.kuali.ole.pojo.edi.MessageCreationInfoDetails;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/8/12
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public enum MessageCreationInfoDetailsFixture {
    MessageCreationInformation("137",
            "20111026",
            "102"),;

    private String messageCreationInfoConstant;
    private String messageCreationInfoDate;
    private String messageCreationInfoDateFormat;

    private MessageCreationInfoDetailsFixture(String messageCreationInfoConstant, String messageCreationInfoDate,
                                              String messageCreationInfoDateFormat) {
        this.messageCreationInfoConstant = messageCreationInfoConstant;
        this.messageCreationInfoDate = messageCreationInfoDate;
        this.messageCreationInfoDateFormat = messageCreationInfoDateFormat;
    }

    public MessageCreationInfoDetails createMessageCreationInfoDetails(Class clazz) {
        MessageCreationInfoDetails messageCreationInfoDetails = null;
        try {
            messageCreationInfoDetails = (MessageCreationInfoDetails) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("MessageCreationInfoDetails creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("MessageCreationInfoDetails creation failed. class = " + clazz);
        }
        messageCreationInfoDetails.setMessageCreationInfoConstant(messageCreationInfoConstant);
        messageCreationInfoDetails.setMessageCreationInfoDate(messageCreationInfoDate);
        messageCreationInfoDetails.setMessageCreationInfoDateFormat(messageCreationInfoDateFormat);

        return messageCreationInfoDetails;
    }
}
