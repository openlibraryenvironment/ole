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

@XmlRootElement(name = NotificationSender.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NotificationSender.Constants.TYPE_NAME, propOrder = {
        NotificationSender.Elements.NOTIFICATION_ID,
        NotificationSender.Elements.SENDER_NAME,
        NotificationSender.Elements.ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NotificationSender
        extends AbstractDataTransferObject
        implements NotificationSenderContract
{

    @XmlElement(name = Elements.NOTIFICATION_ID, required = false)
    private final Long notificationId;
    @XmlElement(name = Elements.SENDER_NAME, required = false)
    private final String senderName;
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
    private NotificationSender() {
        this.notificationId = null;
        this.senderName = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private NotificationSender(Builder builder) {
        this.notificationId = builder.getNotificationId();
        this.senderName = builder.getSenderName();
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public Long getNotificationId() {
        return this.notificationId;
    }

    @Override
    public String getSenderName() {
        return this.senderName;
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
     * A builder which can be used to construct {@link NotificationSender} instances.  Enforces the constraints of the {@link NotificationSenderContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, NotificationSenderContract
    {

        private Long notificationId;
        private String senderName;
        private Long id;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(NotificationSenderContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setNotificationId(contract.getNotificationId());
            builder.setSenderName(contract.getSenderName());
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public NotificationSender build() {
            return new NotificationSender(this);
        }

        @Override
        public Long getNotificationId() {
            return this.notificationId;
        }

        @Override
        public String getSenderName() {
            return this.senderName;
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

        public void setNotificationId(Long notificationId) {
            this.notificationId = notificationId;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
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

    }


    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "notificationSender";
        final static String TYPE_NAME = "NotificationSenderType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String NOTIFICATION_ID = "notificationId";
        final static String SENDER_NAME = "senderName";
        final static String ID = "id";

    }

}