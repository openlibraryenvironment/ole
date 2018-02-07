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
import org.kuali.rice.ken.api.KenApiConstants;
import org.w3c.dom.Element;

@XmlRootElement(name = NotificationResponse.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NotificationResponse.Constants.TYPE_NAME, propOrder = {
        NotificationResponse.Elements.MESSAGE,
        NotificationResponse.Elements.STATUS,
        NotificationResponse.Elements.NOTIFICATION_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NotificationResponse
        extends AbstractDataTransferObject
        implements NotificationResponseContract
{

    @XmlElement(name = Elements.MESSAGE, required = false)
    private final String message;
    @XmlElement(name = Elements.STATUS, required = false)
    private final String status;
    @XmlElement(name = Elements.NOTIFICATION_ID, required = false)
    private final Long notificationId;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     *
     */
    private NotificationResponse() {
        this.message = null;
        this.status = null;
        this.notificationId = null;
    }

    private NotificationResponse(Builder builder) {
        this.message = builder.getMessage();
        this.status = builder.getStatus();
        this.notificationId = builder.getNotificationId();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public Long getNotificationId() {
        return this.notificationId;
    }


    /**
     * A builder which can be used to construct {@link NotificationResponse} instances.  Enforces the constraints of the {@link NotificationResponseContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, NotificationResponseContract
    {

        private String message;
        private String status;
        private Long notificationId;

        private Builder() {
            this.status = KenApiConstants.RESPONSE_STATUSES.SUCCESS;
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(NotificationResponseContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setMessage(contract.getMessage());
            builder.setStatus(contract.getStatus());
            builder.setNotificationId(contract.getNotificationId());
            return builder;
        }

        public NotificationResponse build() {
            return new NotificationResponse(this);
        }

        @Override
        public String getMessage() {
            return this.message;
        }

        @Override
        public String getStatus() {
            return this.status;
        }

        @Override
        public Long getNotificationId() {
            return this.notificationId;
        }

        public void setMessage(String message) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.message = message;
        }

        public void setStatus(String status) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.status = status;
        }

        public void setNotificationId(Long notificationId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.notificationId = notificationId;
        }

    }


    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "notificationResponse";
        final static String TYPE_NAME = "NotificationResponseType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String MESSAGE = "message";
        final static String STATUS = "status";
        final static String NOTIFICATION_ID = "notificationId";

    }

}