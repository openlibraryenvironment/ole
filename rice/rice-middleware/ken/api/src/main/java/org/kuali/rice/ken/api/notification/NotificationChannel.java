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

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

@XmlRootElement(name = NotificationChannel.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NotificationChannel.Constants.TYPE_NAME, propOrder = {
        NotificationChannel.Elements.NAME,
        NotificationChannel.Elements.DESCRIPTION,
        NotificationChannel.Elements.SUBSCRIBABLE,
        NotificationChannel.Elements.RECIPIENT_LISTS,
        NotificationChannel.Elements.PRODUCERS,
        NotificationChannel.Elements.REVIEWERS,
        NotificationChannel.Elements.SUBSCRIPTIONS,
        NotificationChannel.Elements.ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NotificationChannel
        extends AbstractDataTransferObject
        implements NotificationChannelContract
{

    @XmlElement(name = Elements.NAME, required = false)
    private final String name;
    @XmlElement(name = Elements.DESCRIPTION, required = false)
    private final String description;
    @XmlElement(name = Elements.SUBSCRIBABLE, required = false)
    private final boolean subscribable;
    @XmlElementWrapper(name = Elements.RECIPIENT_LISTS, required = false)
    @XmlElement(name = Elements.RECIPIENT_LIST, required = false)
    private final List<NotificationListRecipient> recipientLists;
    @XmlElementWrapper(name = Elements.PRODUCERS, required = false)
    @XmlElement(name = Elements.PRODUCER, required = false)
    private final List<NotificationProducer> producers;
    @XmlElementWrapper(name = Elements.REVIEWERS, required = false)
    @XmlElement(name = Elements.REVIEWER, required = false)
    private final List<NotificationChannelReviewer> reviewers;
    @XmlElementWrapper(name = Elements.SUBSCRIPTIONS, required = false)
    @XmlElement(name = Elements.SUBSCRIPTION, required = false)
    private final List<UserChannelSubscription> subscriptions;
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
    private NotificationChannel() {
        this.name = null;
        this.description = null;
        this.subscribable = false;
        this.recipientLists = null;
        this.producers = null;
        this.reviewers = null;
        this.subscriptions = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private NotificationChannel(Builder builder) {
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.subscribable = builder.isSubscribable();

        this.recipientLists = new ArrayList<NotificationListRecipient>();
        if (CollectionUtils.isNotEmpty(builder.getRecipientLists())) {
            for (NotificationListRecipient.Builder listRecipient : builder.getRecipientLists()) {
                this.recipientLists.add(listRecipient.build());
            }
        }

        this.producers = new ArrayList<NotificationProducer>();
        if (CollectionUtils.isNotEmpty(builder.getProducers())) {
            for (NotificationProducer.Builder producer : builder.getProducers()) {
                this.producers.add(producer.build());
            }
        }

        this.reviewers = new ArrayList<NotificationChannelReviewer>();
        if (CollectionUtils.isNotEmpty(builder.getReviewers())) {
            for (NotificationChannelReviewer.Builder reviewer : builder.getReviewers()) {
                this.reviewers.add(reviewer.build());
            }
        }

        this.subscriptions = new ArrayList<UserChannelSubscription>();
        if (CollectionUtils.isNotEmpty(builder.getSubscriptions())) {
            for (UserChannelSubscription.Builder subscription : builder.getSubscriptions()) {
                this.subscriptions.add(subscription.build());
            }
        }

        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean isSubscribable() {
        return this.subscribable;
    }

    @Override
    public List<NotificationListRecipient> getRecipientLists() {
        return this.recipientLists;
    }

    @Override
    public List<NotificationProducer> getProducers() {
        return this.producers;
    }

    @Override
    public List<NotificationChannelReviewer> getReviewers() {
        return this.reviewers;
    }

    @Override
    public List<UserChannelSubscription> getSubscriptions() {
        return this.subscriptions;
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
     * A builder which can be used to construct {@link NotificationChannel} instances.  Enforces the constraints of the {@link NotificationChannelContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, NotificationChannelContract
    {

        private String name;
        private String description;
        private boolean subscribable;
        private List<NotificationListRecipient.Builder> recipientLists;
        private List<NotificationProducer.Builder> producers;
        private List<NotificationChannelReviewer.Builder> reviewers;
        private List<UserChannelSubscription.Builder> subscriptions;
        private Long id;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(NotificationChannelContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setName(contract.getName());
            builder.setDescription(contract.getDescription());
            builder.setSubscribable(contract.isSubscribable());
            if (contract.getRecipientLists() != null) {
                List<NotificationListRecipient.Builder> tempListRecipient = new ArrayList<NotificationListRecipient.Builder>();
                for (NotificationListRecipientContract listRecipient : contract.getRecipientLists()) {
                    tempListRecipient.add(NotificationListRecipient.Builder.create(listRecipient));
                }
                builder.setRecipientLists(tempListRecipient);
            }
            if (contract.getProducers() != null) {
                List<NotificationProducer.Builder> tempProducers = new ArrayList<NotificationProducer.Builder>();
                for (NotificationProducerContract producer : contract.getProducers()) {
                    tempProducers.add(NotificationProducer.Builder.create(producer));
                }
                builder.setProducers(tempProducers);
            }
            if (contract.getReviewers() != null) {
                List<NotificationChannelReviewer.Builder> tempReviewers = new ArrayList<NotificationChannelReviewer.Builder>();
                for (NotificationChannelReviewerContract reviewer : contract.getReviewers()) {
                    tempReviewers.add(NotificationChannelReviewer.Builder.create(reviewer));
                }
                builder.setReviewers(tempReviewers);
            }
            if (contract.getSubscriptions() != null) {
                List<UserChannelSubscription.Builder> tempSubscriptions = new ArrayList<UserChannelSubscription.Builder>();
                for (UserChannelSubscriptionContract subscription : contract.getSubscriptions()) {
                    tempSubscriptions.add(UserChannelSubscription.Builder.create(subscription));
                }
                builder.setSubscriptions(tempSubscriptions);
            }
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public NotificationChannel build() {
            return new NotificationChannel(this);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public boolean isSubscribable() {
            return this.subscribable;
        }

        @Override
        public List<NotificationListRecipient.Builder> getRecipientLists() {
            return this.recipientLists;
        }

        @Override
        public List<NotificationProducer.Builder> getProducers() {
            return this.producers;
        }

        @Override
        public List<NotificationChannelReviewer.Builder> getReviewers() {
            return this.reviewers;
        }

        @Override
        public List<UserChannelSubscription.Builder> getSubscriptions() {
            return this.subscriptions;
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

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setSubscribable(boolean subscribable) {
            this.subscribable = subscribable;
        }

        public void setRecipientLists(List<NotificationListRecipient.Builder> recipientLists) {
            this.recipientLists = recipientLists;
        }

        public void setProducers(List<NotificationProducer.Builder> producers) {
            this.producers = producers;
        }

        public void setReviewers(List<NotificationChannelReviewer.Builder> reviewers) {
            this.reviewers = reviewers;
        }

        public void setSubscriptions(List<UserChannelSubscription.Builder> subscriptions) {
            this.subscriptions = subscriptions;
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

        final static String ROOT_ELEMENT_NAME = "notificationChannel";
        final static String TYPE_NAME = "NotificationChannelType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String NAME = "name";
        final static String DESCRIPTION = "description";
        final static String SUBSCRIBABLE = "subscribable";
        final static String RECIPIENT_LISTS = "recipientLists";
        final static String RECIPIENT_LIST = "recipientList";
        final static String PRODUCERS = "producers";
        final static String PRODUCER = "producer";
        final static String REVIEWERS = "reviewers";
        final static String REVIEWER = "reviewer";
        final static String SUBSCRIPTIONS = "subscriptions";
        final static String SUBSCRIPTION = "subscription";
        final static String ID = "id";

    }

}