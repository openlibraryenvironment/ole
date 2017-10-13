/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.ken.api.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.w3c.dom.Element;

@XmlRootElement(name = Notification.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Notification.Constants.TYPE_NAME, propOrder = {
        Notification.Elements.PRIORITY,
        Notification.Elements.CONTENT,
        Notification.Elements.CHANNEL,
        Notification.Elements.CONTENT_TYPE,
        Notification.Elements.CREATION_DATE_TIME,
        Notification.Elements.RECIPIENTS,
        Notification.Elements.SENDERS,
        Notification.Elements.AUTO_REMOVE_DATE_TIME,
        Notification.Elements.DELIVERY_TYPE,
        Notification.Elements.PRODUCER,
        Notification.Elements.SEND_DATE_TIME,
        Notification.Elements.PROCESSING_FLAG,
        Notification.Elements.LOCKED_DATE,
        Notification.Elements.TITLE,
        Notification.Elements.CONTENT_MESSAGE,
        Notification.Elements.ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        Notification.Elements.DOC_TYPE_NAME,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Notification
        extends AbstractDataTransferObject
        implements NotificationContract
{

    @XmlElement(name = Elements.PRIORITY, required = false)
    private final NotificationPriority priority;
    @XmlElement(name = Elements.CONTENT, required = false)
    private final String content;
    @XmlElement(name = Elements.CHANNEL, required = false)
    private final NotificationChannel channel;
    @XmlElement(name = Elements.CONTENT_TYPE, required = false)
    private final NotificationContentType contentType;
    @XmlElement(name = Elements.CREATION_DATE_TIME, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime creationDateTime;
    @XmlElementWrapper(name = Elements.RECIPIENTS, required = false)
    @XmlElement(name = Elements.RECIPIENT, required = false)
    private final List<NotificationRecipient> recipients;
    @XmlElementWrapper(name = Elements.SENDERS, required = false)
    @XmlElement(name = Elements.SENDER, required = false)
    private final List<NotificationSender> senders;
    @XmlElement(name = Elements.AUTO_REMOVE_DATE_TIME, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime autoRemoveDateTime;
    @XmlElement(name = Elements.DELIVERY_TYPE, required = false)
    private final String deliveryType;
    @XmlElement(name = Elements.PRODUCER, required = false)
    private final NotificationProducer producer;
    @XmlElement(name = Elements.SEND_DATE_TIME, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime sendDateTime;
    @XmlElement(name = Elements.PROCESSING_FLAG, required = false)
    private final String processingFlag;
    @XmlElement(name = Elements.LOCKED_DATE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime lockedDate;
    @XmlElement(name = Elements.TITLE, required = false)
    private final String title;
    @XmlElement(name = Elements.CONTENT_MESSAGE, required = false)
    private final String contentMessage;
    @XmlElement(name = Elements.ID, required = false)
    private final Long id;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    /**
     * @since 2.3.1
     */
    @XmlElement(name = Elements.DOC_TYPE_NAME, required = false)
    private final String docTypeName;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     *
     */
    private Notification() {
        this.priority = null;
        this.content = null;
        this.channel = null;
        this.contentType = null;
        this.creationDateTime = null;
        this.recipients = null;
        this.senders = null;
        this.autoRemoveDateTime = null;
        this.deliveryType = null;
        this.producer = null;
        this.sendDateTime = null;
        this.processingFlag = null;
        this.lockedDate = null;
        this.title = null;
        this.contentMessage = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
        this.docTypeName = null;
    }

    private Notification(Builder builder) {
        this.priority = builder.getPriority() == null ? null : builder.getPriority().build();
        this.content = builder.getContent();
        this.channel = builder.getChannel() == null ? null : builder.getChannel().build();
        this.contentType = builder.getContentType() == null ? null : builder.getContentType().build();
        this.creationDateTime = builder.getCreationDateTime();
        this.recipients = new ArrayList<NotificationRecipient>();
        if (CollectionUtils.isNotEmpty(builder.getRecipients())) {
            for (NotificationRecipient.Builder recipient : builder.getRecipients()) {
                this.recipients.add(recipient.build());
            }
        }
        this.senders = new ArrayList<NotificationSender>();
        if (CollectionUtils.isNotEmpty(builder.getSenders())) {
            for (NotificationSender.Builder sender : builder.getSenders()) {
                this.senders.add(sender.build());
            }
        }
        this.autoRemoveDateTime = builder.getAutoRemoveDateTime();
        this.deliveryType = builder.getDeliveryType();
        this.producer = builder.getProducer() == null ? null : builder.getProducer().build();
        this.sendDateTime = builder.getSendDateTime();
        this.processingFlag = builder.getProcessingFlag();
        this.lockedDate = builder.getLockedDate();
        this.title = builder.getTitle();
        this.contentMessage = builder.getContentMessage();
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.docTypeName = builder.getDocTypeName();
    }

    @Override
    public NotificationPriority getPriority() {
        return this.priority;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public NotificationChannel getChannel() {
        return this.channel;
    }

    @Override
    public NotificationContentType getContentType() {
        return this.contentType;
    }

    @Override
    public DateTime getCreationDateTime() {
        return this.creationDateTime;
    }

    @Override
    public List<NotificationRecipient> getRecipients() {
        return this.recipients;
    }

    @Override
    public List<NotificationSender> getSenders() {
        return this.senders;
    }

    @Override
    public DateTime getAutoRemoveDateTime() {
        return this.autoRemoveDateTime;
    }

    @Override
    public String getDeliveryType() {
        return this.deliveryType;
    }

    @Override
    public NotificationProducer getProducer() {
        return this.producer;
    }

    @Override
    public DateTime getSendDateTime() {
        return this.sendDateTime;
    }

    @Override
    public String getProcessingFlag() {
        return this.processingFlag;
    }

    @Override
    public DateTime getLockedDate() {
        return this.lockedDate;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getContentMessage() {
        return this.contentMessage;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    @Override
    public String getDocTypeName() {
        return this.docTypeName;
    }


    /**
     * A builder which can be used to construct {@link Notification} instances.  Enforces the constraints of the {@link NotificationContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, NotificationContract
    {

        private NotificationPriority.Builder priority;
        private String content;
        private NotificationChannel.Builder channel;
        private NotificationContentType.Builder contentType;
        private DateTime creationDateTime;
        private List<NotificationRecipient.Builder> recipients;
        private List<NotificationSender.Builder> senders;
        private DateTime autoRemoveDateTime;
        private String deliveryType;
        private NotificationProducer.Builder producer;
        private DateTime sendDateTime;
        private String processingFlag;
        private DateTime lockedDate;
        private String title;
        private String contentMessage;
        private Long id;
        private Long versionNumber;
        private String objectId;
        private String docTypeName;

        private Builder() {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
        }

        public static Builder create() {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder();
        }

        public static Builder create(NotificationContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create();
            builder.setPriority(contract.getPriority() == null ? null : NotificationPriority.Builder.create(contract.getPriority()));
            builder.setContent(contract.getContent());
            builder.setChannel(contract.getChannel() == null ? null : NotificationChannel.Builder.create(contract.getChannel()));
            builder.setContentType(contract.getContentType() == null ? null : NotificationContentType.Builder.create(contract.getContentType()));
            builder.setCreationDateTime(contract.getCreationDateTime());
            if (contract.getRecipients() != null) {
                List<NotificationRecipient.Builder> tempRecipients = new ArrayList<NotificationRecipient.Builder>();
                for (NotificationRecipientContract recipient : contract.getRecipients()) {
                    tempRecipients.add(NotificationRecipient.Builder.create(recipient));
                }
                builder.setRecipients(tempRecipients);
            }
            if (contract.getSenders() != null) {
                List<NotificationSender.Builder> tempSenders = new ArrayList<NotificationSender.Builder>();
                for (NotificationSenderContract sender : contract.getSenders()) {
                    tempSenders.add(NotificationSender.Builder.create(sender));
                }
                builder.setSenders(tempSenders);
            }
            builder.setAutoRemoveDateTime(contract.getAutoRemoveDateTime());
            builder.setDeliveryType(contract.getDeliveryType());
            builder.setProducer(contract.getProducer() == null ? null : NotificationProducer.Builder.create(contract.getProducer()));
            builder.setSendDateTime(contract.getSendDateTime());
            builder.setProcessingFlag(contract.getProcessingFlag());
            builder.setLockedDate(contract.getLockedDate());
            builder.setTitle(contract.getTitle());
            builder.setContentMessage(contract.getContentMessage());
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setDocTypeName(contract.getDocTypeName());
            return builder;
        }

        public Notification build() {
            return new Notification(this);
        }

        @Override
        public NotificationPriority.Builder getPriority() {
            return this.priority;
        }

        @Override
        public String getContent() {
            return this.content;
        }

        @Override
        public NotificationChannel.Builder getChannel() {
            return this.channel;
        }

        @Override
        public NotificationContentType.Builder getContentType() {
            return this.contentType;
        }

        @Override
        public DateTime getCreationDateTime() {
            return this.creationDateTime;
        }

        @Override
        public List<NotificationRecipient.Builder> getRecipients() {
            return this.recipients;
        }

        @Override
        public List<NotificationSender.Builder> getSenders() {
            return this.senders;
        }

        @Override
        public DateTime getAutoRemoveDateTime() {
            return this.autoRemoveDateTime;
        }

        @Override
        public String getDeliveryType() {
            return this.deliveryType;
        }

        @Override
        public NotificationProducer.Builder getProducer() {
            return this.producer;
        }

        @Override
        public DateTime getSendDateTime() {
            return this.sendDateTime;
        }

        @Override
        public String getProcessingFlag() {
            return this.processingFlag;
        }

        @Override
        public DateTime getLockedDate() {
            return this.lockedDate;
        }

        @Override
        public String getTitle() {
            return this.title;
        }

        @Override
        public String getContentMessage() {
            return this.contentMessage;
        }

        @Override
        public Long getId() {
            return this.id;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        @Override
        public String getDocTypeName() {
            return this.docTypeName;
        }

        public void setPriority(NotificationPriority.Builder priority) {
            this.priority = priority;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setChannel(NotificationChannel.Builder channel) {
            this.channel = channel;
        }

        public void setContentType(NotificationContentType.Builder contentType) {
            this.contentType = contentType;
        }

        public void setCreationDateTime(DateTime creationDateTime) {
            this.creationDateTime = creationDateTime;
        }

        public void setRecipients(List<NotificationRecipient.Builder> recipients) {
            this.recipients = recipients;
        }

        public void setSenders(List<NotificationSender.Builder> senders) {
            this.senders = senders;
        }

        public void setAutoRemoveDateTime(DateTime autoRemoveDateTime) {
            this.autoRemoveDateTime = autoRemoveDateTime;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }

        public void setProducer(NotificationProducer.Builder producer) {
            this.producer = producer;
        }

        public void setSendDateTime(DateTime sendDateTime) {
            this.sendDateTime = sendDateTime;
        }

        public void setProcessingFlag(String processingFlag) {
            this.processingFlag = processingFlag;
        }

        public void setLockedDate(DateTime lockedDate) {
            this.lockedDate = lockedDate;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContentMessage(String contentMessage) {
            this.contentMessage = contentMessage;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        /**
         * Sets the custom document type name.
         *
         * <p>
         * If null, the system will use the default {@code KualiNotification} document type when routing the notification.
         * If the document type does not match any document type name in the system, the system behavior is undefined.
         * </p>
         *
         * @param docTypeName document type name of this notification
         * @since 2.3.1
         */
        public void setDocTypeName(String docTypeName) {
            this.docTypeName = docTypeName;
        }
    }


    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "notification";
        final static String TYPE_NAME = "NotificationType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String PRIORITY = "priority";
        final static String CONTENT = "content";
        final static String CHANNEL = "channel";
        final static String CONTENT_TYPE = "contentType";
        final static String CREATION_DATE_TIME = "creationDateTime";
        final static String LOCK_VER_NBR = "lockVerNbr";
        final static String RECIPIENTS = "recipients";
        final static String RECIPIENT = "recipient";
        final static String SENDERS = "senders";
        final static String SENDER = "sender";
        final static String AUTO_REMOVE_DATE_TIME = "autoRemoveDateTime";
        final static String DELIVERY_TYPE = "deliveryType";
        final static String PRODUCER = "producer";
        final static String SEND_DATE_TIME = "sendDateTime";
        final static String PROCESSING_FLAG = "processingFlag";
        final static String LOCKED_DATE = "lockedDate";
        final static String TITLE = "title";
        final static String CONTENT_MESSAGE = "contentMessage";
        final static String ID = "id";
        final static String DOC_TYPE_NAME = "docTypeName";

    }

}