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
package org.kuali.rice.kim.api.identity.privacy;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

@XmlRootElement(name = EntityPrivacyPreferences.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityPrivacyPreferences.Constants.TYPE_NAME, propOrder = {
    EntityPrivacyPreferences.Elements.ENTITY_ID,
    EntityPrivacyPreferences.Elements.SUPPRESS_NAME,
    EntityPrivacyPreferences.Elements.SUPPRESS_ADDRESS,
    EntityPrivacyPreferences.Elements.SUPPRESS_EMAIL,
    EntityPrivacyPreferences.Elements.SUPPRESS_PHONE,
    EntityPrivacyPreferences.Elements.SUPPRESS_PERSONAL,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class EntityPrivacyPreferences extends AbstractDataTransferObject
    implements EntityPrivacyPreferencesContract
{

    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;
    @XmlElement(name = Elements.SUPPRESS_NAME, required = false)
    private final boolean suppressName;
    @XmlElement(name = Elements.SUPPRESS_ADDRESS, required = false)
    private final boolean suppressAddress;
    @XmlElement(name = Elements.SUPPRESS_EMAIL, required = false)
    private final boolean suppressEmail;
    @XmlElement(name = Elements.SUPPRESS_PHONE, required = false)
    private final boolean suppressPhone;
    @XmlElement(name = Elements.SUPPRESS_PERSONAL, required = false)
    private final boolean suppressPersonal;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private EntityPrivacyPreferences() {
        this.entityId = null;
        this.suppressName = false;
        this.suppressAddress = false;
        this.suppressEmail = false;
        this.suppressPhone = false;
        this.suppressPersonal = false;
        this.versionNumber = null;
        this.objectId = null;
    }

    private EntityPrivacyPreferences(Builder builder) {
        this.entityId = builder.getEntityId();
        this.suppressName = builder.isSuppressName();
        this.suppressAddress = builder.isSuppressAddress();
        this.suppressEmail = builder.isSuppressEmail();
        this.suppressPhone = builder.isSuppressPhone();
        this.suppressPersonal = builder.isSuppressPersonal();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public String getEntityId() {
        return this.entityId;
    }

    @Override
    public boolean isSuppressName() {
        return this.suppressName;
    }

    @Override
    public boolean isSuppressAddress() {
        return this.suppressAddress;
    }

    @Override
    public boolean isSuppressEmail() {
        return this.suppressEmail;
    }

    @Override
    public boolean isSuppressPhone() {
        return this.suppressPhone;
    }

    @Override
    public boolean isSuppressPersonal() {
        return this.suppressPersonal;
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
     * A builder which can be used to construct {@link EntityPrivacyPreferences} instances.  Enforces the constraints of the {@link EntityPrivacyPreferencesContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityPrivacyPreferencesContract
    {

        private String entityId;
        private boolean suppressName;
        private boolean suppressAddress;
        private boolean suppressEmail;
        private boolean suppressPhone;
        private boolean suppressPersonal;
        private Long versionNumber;
        private String objectId;

        private Builder(String entityId) {
            setEntityId(entityId);
        }

        public static Builder create(String entityId) {
            return new Builder(entityId);
        }

        public static Builder create(EntityPrivacyPreferencesContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getEntityId());
            builder.setSuppressName(contract.isSuppressName());
            builder.setSuppressAddress(contract.isSuppressAddress());
            builder.setSuppressEmail(contract.isSuppressEmail());
            builder.setSuppressPhone(contract.isSuppressPhone());
            builder.setSuppressPersonal(contract.isSuppressPersonal());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public EntityPrivacyPreferences build() {
            return new EntityPrivacyPreferences(this);
        }

        @Override
        public String getEntityId() {
            return this.entityId;
        }

        @Override
        public boolean isSuppressName() {
            return this.suppressName;
        }

        @Override
        public boolean isSuppressAddress() {
            return this.suppressAddress;
        }

        @Override
        public boolean isSuppressEmail() {
            return this.suppressEmail;
        }

        @Override
        public boolean isSuppressPhone() {
            return this.suppressPhone;
        }

        @Override
        public boolean isSuppressPersonal() {
            return this.suppressPersonal;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        public void setEntityId(String entityId) {
            if (StringUtils.isEmpty(entityId)) {
                throw new IllegalArgumentException("entityId is empty");
            }
            this.entityId = entityId;
        }

        public void setSuppressName(boolean suppressName) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.suppressName = suppressName;
        }

        public void setSuppressAddress(boolean suppressAddress) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.suppressAddress = suppressAddress;
        }

        public void setSuppressEmail(boolean suppressEmail) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.suppressEmail = suppressEmail;
        }

        public void setSuppressPhone(boolean suppressPhone) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.suppressPhone = suppressPhone;
        }

        public void setSuppressPersonal(boolean suppressPersonal) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.suppressPersonal = suppressPersonal;
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

        final static String ROOT_ELEMENT_NAME = "entityPrivacyPreferences";
        final static String TYPE_NAME = "EntityPrivacyPreferencesType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ENTITY_ID = "entityId";
        final static String SUPPRESS_NAME = "suppressName";
        final static String SUPPRESS_ADDRESS = "suppressAddress";
        final static String SUPPRESS_EMAIL = "suppressEmail";
        final static String SUPPRESS_PHONE = "suppressPhone";
        final static String SUPPRESS_PERSONAL = "suppressPersonal";

    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + EntityPrivacyPreferences.Constants.TYPE_NAME;
    }

}