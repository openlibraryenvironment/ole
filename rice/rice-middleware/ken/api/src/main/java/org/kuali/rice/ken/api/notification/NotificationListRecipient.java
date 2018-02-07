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
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

@XmlRootElement(name = NotificationListRecipient.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NotificationListRecipient.Constants.TYPE_NAME, propOrder = {
        NotificationListRecipient.Elements.CHANNEL,
        NotificationListRecipient.Elements.RECIPIENT_ID,
        NotificationListRecipient.Elements.RECIPIENT_TYPE,
        NotificationListRecipient.Elements.ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NotificationListRecipient
        extends AbstractDataTransferObject
        implements NotificationListRecipientContract
{

    @XmlElement(name = Elements.CHANNEL, required = false)
    private final NotificationChannel channel;
    @XmlElement(name = Elements.RECIPIENT_ID, required = false)
    private final String recipientId;
    @XmlElement(name = Elements.RECIPIENT_TYPE, required = false)
    private final String recipientType;
    @XmlElement(name = Elements.ID, required = false)
    private final Long id;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     *
     */
    private NotificationListRecipient() {
        this.channel = null;
        this.recipientId = null;
        this.recipientType = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private NotificationListRecipient(Builder builder) {
        this.channel = builder.getChannel() == null ? null : builder.getChannel().build();
        this.recipientId = builder.getRecipientId();
        this.recipientType = builder.getRecipientType();
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public NotificationChannel getChannel() {
        return this.channel;
    }

    @Override
    public String getRecipientId() {
        return this.recipientId;
    }

    @Override
    public String getRecipientType() {
        return this.recipientType;
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


    /**
     * A builder which can be used to construct {@link NotificationListRecipient} instances.  Enforces the constraints of the {@link NotificationListRecipientContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, NotificationListRecipientContract
    {

        private NotificationChannel.Builder channel;
        private String recipientId;
        private String recipientType;
        private Long id;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(NotificationListRecipientContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create();
            builder.setChannel(contract.getChannel() != null ? NotificationChannel.Builder.create(contract.getChannel()) : null);
            builder.setRecipientId(contract.getRecipientId());
            builder.setRecipientType(contract.getRecipientType());
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public NotificationListRecipient build() {
            return new NotificationListRecipient(this);
        }

        @Override
        public NotificationChannel.Builder getChannel() {
            return this.channel;
        }

        @Override
        public String getRecipientId() {
            return this.recipientId;
        }

        @Override
        public String getRecipientType() {
            return this.recipientType;
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

        public void setChannel(NotificationChannel.Builder channel) {
            this.channel = channel;
        }

        public void setRecipientId(String recipientId) {
            this.recipientId = recipientId;
        }

        public void setRecipientType(String recipientType) {
            this.recipientType = recipientType;
        }

        public void setId(Long id) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.id = id;
        }

        public void setVersionNumber(Long versionNumber) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.objectId = objectId;
        }

    }


    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "notificationListRecipient";
        final static String TYPE_NAME = "NotificationListRecipientType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String CHANNEL = "channel";
        final static String RECIPIENT_ID = "recipientId";
        final static String RECIPIENT_TYPE = "recipientType";
        final static String ID = "id";

    }

}