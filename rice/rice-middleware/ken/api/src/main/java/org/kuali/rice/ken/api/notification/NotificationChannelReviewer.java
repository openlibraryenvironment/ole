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

@XmlRootElement(name = NotificationChannelReviewer.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NotificationChannelReviewer.Constants.TYPE_NAME, propOrder = {
        NotificationChannelReviewer.Elements.CHANNEL,
        NotificationChannelReviewer.Elements.REVIEWER_TYPE,
        NotificationChannelReviewer.Elements.REVIEWER_ID,
        NotificationChannelReviewer.Elements.ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NotificationChannelReviewer
        extends AbstractDataTransferObject
        implements NotificationChannelReviewerContract
{

    @XmlElement(name = Elements.CHANNEL, required = false)
    private final NotificationChannel channel;
    @XmlElement(name = Elements.REVIEWER_TYPE, required = false)
    private final String reviewerType;
    @XmlElement(name = Elements.REVIEWER_ID, required = false)
    private final String reviewerId;
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
    private NotificationChannelReviewer() {
        this.channel = null;
        this.reviewerType = null;
        this.reviewerId = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private NotificationChannelReviewer(Builder builder) {
        this.channel = builder.getChannel() != null ? builder.getChannel().build() : null;
        this.reviewerType = builder.getReviewerType();
        this.reviewerId = builder.getReviewerId();
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public NotificationChannel getChannel() {
        return this.channel;
    }

    @Override
    public String getReviewerType() {
        return this.reviewerType;
    }

    @Override
    public String getReviewerId() {
        return this.reviewerId;
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
     * A builder which can be used to construct {@link NotificationChannelReviewer} instances.  Enforces the constraints of the {@link NotificationChannelReviewerContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, NotificationChannelReviewerContract
    {

        private NotificationChannel.Builder channel;
        private String reviewerType;
        private String reviewerId;
        private Long id;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(NotificationChannelReviewerContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setChannel(contract.getChannel() != null ? NotificationChannel.Builder.create(contract.getChannel()) : null);
            builder.setReviewerType(contract.getReviewerType());
            builder.setReviewerId(contract.getReviewerId());
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public NotificationChannelReviewer build() {
            return new NotificationChannelReviewer(this);
        }

        @Override
        public NotificationChannel.Builder getChannel() {
            return this.channel;
        }

        @Override
        public String getReviewerType() {
            return this.reviewerType;
        }

        @Override
        public String getReviewerId() {
            return this.reviewerId;
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

        public void setReviewerType(String reviewerType) {
            this.reviewerType = reviewerType;
        }

        public void setReviewerId(String reviewerId) {
            this.reviewerId = reviewerId;
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

        final static String ROOT_ELEMENT_NAME = "notificationChannelReviewer";
        final static String TYPE_NAME = "NotificationChannelReviewerType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String CHANNEL = "channel";
        final static String REVIEWER_TYPE = "reviewerType";
        final static String REVIEWER_ID = "reviewerId";
        final static String ID = "id";

    }

}