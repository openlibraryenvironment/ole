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
package org.kuali.rice.kim.api.type;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.common.attribute.KimAttribute;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

/**
 * An immutable representation of a {@link KimTypeAttributeContract}.
 *
 * <p>To construct an instance of a KimTypeAttribute, use the {@link KimTypeAttribute.Builder} class.<p/>
 *
 * @see KimTypeAttributeContract
 */
@XmlRootElement(name = KimTypeAttribute.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KimTypeAttribute.Constants.TYPE_NAME, propOrder = {
        KimTypeAttribute.Elements.ID,
        KimTypeAttribute.Elements.SORT_CODE,
        KimTypeAttribute.Elements.KIM_ATTRIBUTE,
        KimTypeAttribute.Elements.KIM_TYPE_ID,
        KimTypeAttribute.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KimTypeAttribute extends AbstractDataTransferObject implements KimTypeAttributeContract {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = KimTypeAttribute.Elements.ID, required = false)
    private final String id;

    @XmlElement(name = KimTypeAttribute.Elements.SORT_CODE, required = false)
    private final String sortCode;

    @XmlElement(name = KimTypeAttribute.Elements.KIM_ATTRIBUTE, required = false)
    private final KimAttribute kimAttribute;

    @XmlElement(name = KimTypeAttribute.Elements.KIM_TYPE_ID, required = false)
    private final String kimTypeId;

    @XmlElement(name = KimTypeAttribute.Elements.ACTIVE, required = false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * This constructor should never be called except during JAXB unmarshalling.
     */
    private KimTypeAttribute() {
        this.id = null;
        this.sortCode = null;
        this.kimAttribute = null;
        this.kimTypeId = null;
        this.active = false;
        this.versionNumber = Long.valueOf(1L);
        this.objectId = null;
    }

    private KimTypeAttribute(Builder builder) {
        this.id = builder.getId();
        this.sortCode = builder.getSortCode();

        this.kimAttribute = builder.getKimAttribute() != null ? builder.getKimAttribute().build() : null;
        this.kimTypeId = builder.getKimTypeId();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSortCode() {
        return sortCode;
    }

    @Override
    public KimAttribute getKimAttribute() {
        return kimAttribute;
    }

    @Override
    public String getKimTypeId() {
        return kimTypeId;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    /**
     * This builder constructs an KimTypeAttribute enforcing the constraints of the {@link KimTypeAttributeContract}.
     */
    public static final class Builder implements KimTypeAttributeContract, ModelBuilder, Serializable {
        private String id;
        private String sortCode;
        private KimAttribute.Builder kimAttribute;
        private String kimTypeId;
        private boolean active;
        private Long versionNumber = 1L;
        private String objectId;

        private Builder() {
        }

        /**
         * creates a KimTypeAttribute with the required fields.
         */
        public static Builder create() {
            return new Builder();
        }

        /**
         * creates a KimTypeAttribute from an existing {@link KimTypeAttributeContract}.
         */
        public static Builder create(KimTypeAttributeContract contract) {
            Builder builder = new Builder();
            builder.setId(contract.getId());
            builder.setSortCode(contract.getSortCode());
            if (contract.getKimAttribute() != null) {
                builder.setKimAttribute(KimAttribute.Builder.create(contract.getKimAttribute()));
            }
            builder.setKimTypeId(contract.getKimTypeId());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        @Override
        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        @Override
        public String getSortCode() {
            return sortCode;
        }

        public void setSortCode(final String sortCode) {
            this.sortCode = sortCode;
        }

        @Override
        public KimAttribute.Builder getKimAttribute() {
            return kimAttribute;
        }

        public void setKimAttribute(final KimAttribute.Builder kimAttribute) {
            this.kimAttribute = kimAttribute;
        }

        @Override
        public String getKimTypeId() {
            return kimTypeId;
        }

        public void setKimTypeId(final String kimTypeId) {
            this.kimTypeId = kimTypeId;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        public void setActive(final boolean active) {
            this.active = active;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(final Long versionNumber) {
            if (versionNumber != null && versionNumber <= 0) {
                throw new IllegalArgumentException("versionNumber is invalid");
            }

            this.versionNumber = versionNumber;
        }

        @Override
        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(final String objectId) {
            this.objectId = objectId;
        }

        @Override
        public KimTypeAttribute build() {
            return new KimTypeAttribute(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        static final String ROOT_ELEMENT_NAME = "kimTypeAttribute";
        static final String TYPE_NAME = "KimTypeAttributeType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        static final String ID = "id";
        static final String SORT_CODE = "sortCode";
        static final String KIM_ATTRIBUTE = "kimAttribute";
        static final String KIM_TYPE_ID = "kimTypeId";
        static final String ACTIVE = "active";
    }
}
