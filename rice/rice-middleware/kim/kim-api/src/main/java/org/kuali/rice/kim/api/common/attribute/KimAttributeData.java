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
package org.kuali.rice.kim.api.common.attribute;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.type.KimType;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;


@XmlRootElement(name = KimAttributeData.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KimAttributeData.Constants.TYPE_NAME, propOrder = {
        KimAttributeData.Elements.ID,
        KimAttributeData.Elements.ASSIGNED_TO_ID,
        KimAttributeData.Elements.KIM_TYPE_ID,
        KimAttributeData.Elements.KIM_TYPE,
        KimAttributeData.Elements.KIM_ATTRIBUTE,
        KimAttributeData.Elements.ATTRIBUTE_VALUE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KimAttributeData extends AbstractDataTransferObject implements KimAttributeDataContract {
    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.ASSIGNED_TO_ID, required = false)
    private final String assignedToId;

    @XmlElement(name = Elements.KIM_TYPE_ID, required = true)
    private final String kimTypeId;

    @XmlElement(name = Elements.KIM_TYPE, required = false)
    private final KimType kimType;

    @XmlElement(name = Elements.KIM_ATTRIBUTE, required = false)
    private final KimAttribute kimAttribute;

    @XmlElement(name = Elements.ATTRIBUTE_VALUE, required = false)
    private final String attributeValue;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    @SuppressWarnings("unused")
    private KimAttributeData() {
        this.id = null;
        this.assignedToId = null;
        this.kimTypeId = null;
        this.kimType = null;
        this.kimAttribute = null;
        this.attributeValue = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private KimAttributeData(Builder builder) {
        this.id = builder.getId();
        this.assignedToId = builder.getAssignedToId();
        this.kimTypeId = builder.getKimTypeId();
        this.kimType =
                builder.getKimType() != null ? builder.getKimType().build() : null;
        this.kimAttribute =
                builder.getKimAttribute() != null ? builder.getKimAttribute().build() : null;
        this.attributeValue = builder.getAttributeValue();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getAssignedToId() {
        return assignedToId;
    }

    @Override
    public String getKimTypeId() {
        return kimTypeId;
    }

    @Override
    public KimType getKimType() {
        return kimType;
    }

    @Override
    public KimAttribute getKimAttribute() {
        return kimAttribute;
    }

    @Override
    public String getAttributeValue() {
        return attributeValue;
    }

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    public static final class Builder implements KimAttributeDataContract, ModelBuilder, Serializable {
        private String id;
        private String assignedToId;
        private String kimTypeId;
        private KimType.Builder kimType;
        private KimAttribute.Builder kimAttribute;
        private String attributeValue;
        private Long versionNumber;
        private String objectId;

        private Builder(String kimTypeId) {
            setKimTypeId(kimTypeId);
        }

        /**
         * creates a Parameter with the required fields.
         */
        public static Builder create(String kimTypeId) {
            return new Builder(kimTypeId);
        }

        /**
         * creates a KimAttributeData from an existing {@link org.kuali.rice.kim.api.common.attribute.KimAttributeContract}
         */
        public static Builder create(KimAttributeDataContract contract) {
            Builder builder = new Builder(contract.getKimTypeId());
            builder.setAssignedToId(contract.getAssignedToId());

            builder.setId(contract.getId());
            if (contract.getKimAttribute() != null) {
                builder.setKimAttribute(KimAttribute.Builder.create(contract.getKimAttribute()));
            }
            if (contract.getKimType() != null) {
                builder.setKimType(KimType.Builder.create(contract.getKimType()));
            }
            builder.setValue(contract.getAttributeValue());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        @Override
        public String getId() {
            return id;
        }

        public void setId(final String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is whitespace");
            }
            this.id = id;
        }

        @Override
        public String getAssignedToId() {
            return assignedToId;
        }

        public void setAssignedToId(final String assignedToId) {
            this.assignedToId = assignedToId;
        }

        @Override
        public String getKimTypeId(){
            return kimTypeId;
        }

        public void setKimTypeId(String kimTypeId) {
            this.kimTypeId = kimTypeId;
        }

        @Override
        public KimType.Builder getKimType() {
            return kimType;
        }

        public void setKimType(final KimType.Builder kimType) {
            this.kimType = kimType;
        }

        @Override
        public KimAttribute.Builder getKimAttribute() {
            return kimAttribute;
        }

        public void setKimAttribute(final KimAttribute.Builder kimAttribute) {
            this.kimAttribute = kimAttribute;
        }

        @Override
        public String getAttributeValue() {
            return attributeValue;
        }

        public void setValue(final String attributeValue) {
            this.attributeValue = attributeValue;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
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
        public KimAttributeData build() {
            return new KimAttributeData(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "kimAttributeData";
        final static String TYPE_NAME = "KimAttributeDataType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String ASSIGNED_TO_ID = "assignedToId";
        final static String KIM_TYPE_ID = "kimTypeId";
        final static String KIM_TYPE = "kimType";
        final static String KIM_ATTRIBUTE = "kimAttribute";
        final static String ATTRIBUTE_VALUE = "attributeValue";
    }
}
