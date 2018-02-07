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

@XmlRootElement(name = NotificationProducer.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NotificationProducer.Constants.TYPE_NAME, propOrder = {
        NotificationProducer.Elements.NAME,
        NotificationProducer.Elements.DESCRIPTION,
        NotificationProducer.Elements.CONTACT_INFO,
        NotificationProducer.Elements.ID,
        NotificationProducer.Elements.CHANNEL_IDS,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NotificationProducer
        extends AbstractDataTransferObject
        implements NotificationProducerContract
{

    @XmlElement(name = Elements.NAME, required = false)
    private final String name;
    @XmlElement(name = Elements.DESCRIPTION, required = false)
    private final String description;
    @XmlElement(name = Elements.CONTACT_INFO, required = false)
    private final String contactInfo;
    @XmlElement(name = Elements.ID, required = false)
    private final Long id;
    @XmlElementWrapper(name = Elements.CHANNEL_IDS, required = false)
    @XmlElement(name = Elements.CHANNEL_ID, required = false)
    private final List<Long> channelIds;
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
    private NotificationProducer() {
        this.name = null;
        this.description = null;
        this.contactInfo = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
        this.channelIds = null;
    }

    private NotificationProducer(Builder builder) {
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.contactInfo = builder.getContactInfo();
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.channelIds = builder.getChannelIds();
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
    public String getContactInfo() {
        return this.contactInfo;
    }

    @Override
    public Long getId() {
        return this.id;
    }
    
    @Override
    public List<Long> getChannelIds() {
        return this.channelIds;
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
     * A builder which can be used to construct {@link NotificationProducer} instances.  Enforces the constraints of the {@link NotificationProducerContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, NotificationProducerContract
    {

        private String name;
        private String description;
        private String contactInfo;
        private Long id;
        private List<Long> channelIds;
        private Long versionNumber;
        private String objectId;

        private Builder() {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
        }

        public static Builder create() {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder();
        }

        public static Builder create(NotificationProducerContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create();
            builder.setName(contract.getName());
            builder.setDescription(contract.getDescription());
            builder.setContactInfo(contract.getContactInfo());
            builder.setId(contract.getId());
            builder.setChannelIds(contract.getChannelIds());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public NotificationProducer build() {
            return new NotificationProducer(this);
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
        public String getContactInfo() {
            return this.contactInfo;
        }

        @Override
        public Long getId() {
            return this.id;
        }
        
        @Override
        public List<Long> getChannelIds() {
            return this.channelIds;
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

        public void setContactInfo(String contactInfo) {
            this.contactInfo = contactInfo;
        }

        public void setId(Long id) {
            this.id = id;
        }
        
        public void setChannelIds(List<Long> channelIds) {
            this.channelIds = channelIds;
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

        final static String ROOT_ELEMENT_NAME = "notificationProducer";
        final static String TYPE_NAME = "NotificationProducerType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String NAME = "name";
        final static String DESCRIPTION = "description";
        final static String CONTACT_INFO = "contactInfo";
        final static String ID = "id";
        final static String CHANNEL_IDS = "channelIds";
        final static String CHANNEL_ID = "channelId";

    }

}