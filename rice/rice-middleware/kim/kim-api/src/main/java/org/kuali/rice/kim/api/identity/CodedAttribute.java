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
package org.kuali.rice.kim.api.identity;

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

@XmlRootElement(name = CodedAttribute.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = CodedAttribute.Constants.TYPE_NAME, propOrder = {
    CodedAttribute.Elements.CODE,
    CodedAttribute.Elements.NAME,
    CodedAttribute.Elements.SORT_CODE,
    CodedAttribute.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class CodedAttribute extends AbstractDataTransferObject
    implements CodedAttributeContract
{
    @XmlElement(name = Elements.CODE, required = true)
    private final String code;
    @XmlElement(name = Elements.NAME, required = false)
    private final String name;
    @XmlElement(name = Elements.SORT_CODE, required = false)
    private final String sortCode;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private CodedAttribute() {
        this.name = null;
        this.code = null;
        this.sortCode = null;
        this.versionNumber = null;
        this.objectId = null;
        this.active = false;
    }

    private CodedAttribute(Builder builder) {
        this.name = builder.getName();
        this.code = builder.getCode();
        this.sortCode = builder.getSortCode();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.active = builder.isActive();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getSortCode() {
        return this.sortCode;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }


    /**
     * A builder which can be used to construct {@link CodedAttribute} instances.  Enforces the constraints of the {@link CodedAttributeContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, CodedAttributeContract
    {

        private String name;
        private String code;
        private String sortCode;
        private Long versionNumber;
        private String objectId;
        private boolean active;

        private Builder(String code) {
            setCode(code);
        }

        public static Builder create(String code) {
            return new Builder(code);
        }

        public static Builder create(CodedAttributeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create(contract.getCode());
            builder.setName(contract.getName());
            builder.setSortCode(contract.getSortCode());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());
            return builder;
        }

        public CodedAttribute build() {
            return new CodedAttribute(this);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getCode() {
            return this.code;
        }

        @Override
        public String getSortCode() {
            return this.sortCode;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            if (StringUtils.isWhitespace(code)) {
                throw new IllegalArgumentException("code is empty");
            }
            this.code = code;
        }

        public void setSortCode(String sortCode) {
            this.sortCode = sortCode;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "codedAttribute";
        final static String TYPE_NAME = "CodedAttributeType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String NAME = "name";
        final static String CODE = "code";
        final static String SORT_CODE = "sortCode";
        final static String ACTIVE = "active";

    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + CodedAttribute.Constants.TYPE_NAME;
    }

}
