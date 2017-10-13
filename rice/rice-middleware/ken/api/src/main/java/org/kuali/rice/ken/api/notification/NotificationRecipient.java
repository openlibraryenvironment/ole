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

@XmlRootElement(name = NotificationRecipient.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NotificationRecipient.Constants.TYPE_NAME, propOrder = {
        NotificationRecipient.Elements.NOTIFICATION_ID,
        NotificationRecipient.Elements.RECIPIENT_TYPE,
        NotificationRecipient.Elements.RECIPIENT_ID,
        NotificationRecipient.Elements.ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NotificationRecipient
        extends AbstractDataTransferObject
        implements NotificationRecipientContract
{

    @XmlElement(name = Elements.NOTIFICATION_ID, required = false)
    private final Long notificationId;
    @XmlElement(name = Elements.RECIPIENT_TYPE, required = false)
    private final String recipientType;
    @XmlElement(name = Elements.RECIPIENT_ID, required = false)
    private final String recipientId;
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
    private NotificationRecipient() {
        this.notificationId = null;
        this.recipientType = null;
        this.recipientId = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private NotificationRecipient(Builder builder) {
        this.notificationId = builder.getNotificationId();
        this.recipientType = builder.getRecipientType();
        this.recipientId = builder.getRecipientId();
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public Long getNotificationId() {
        return this.notificationId;
    }

    @Override
    public String getRecipientType() {
        return this.recipientType;
    }

    @Override
    public String getRecipientId() {
        return this.recipientId;
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
     * A builder which can be used to construct {@link NotificationRecipient} instances.  Enforces the constraints of the {@link NotificationRecipientContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, NotificationRecipientContract
    {

        private Long notificationId;
        private String recipientType;
        private String recipientId;
        private Long id;
        private Long versionNumber;
        private String objectId;

        private Builder() {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
        }

        public static Builder create() {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder();
        }

        public static Builder create(NotificationRecipientContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create();
            builder.setNotificationId(contract.getNotificationId());
            builder.setRecipientType(contract.getRecipientType());
            builder.setRecipientId(contract.getRecipientId());
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public NotificationRecipient build() {
            return new NotificationRecipient(this);
        }

        @Override
        public Long getNotificationId() {
            return this.notificationId;
        }

        @Override
        public String getRecipientType() {
            return this.recipientType;
        }

        @Override
        public String getRecipientId() {
            return this.recipientId;
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
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.notificationId = notificationId;
        }

        public void setRecipientType(String recipientType) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.recipientType = recipientType;
        }

        public void setRecipientId(String recipientId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.recipientId = recipientId;
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

        final static String ROOT_ELEMENT_NAME = "notificationRecipient";
        final static String TYPE_NAME = "NotificationRecipientType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String NOTIFICATION_ID = "notificationId";
        final static String RECIPIENT_TYPE = "recipientType";
        final static String RECIPIENT_ID = "recipientId";
        final static String ID = "id";

    }

}
