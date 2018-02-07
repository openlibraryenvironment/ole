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
package org.kuali.rice.kew.api.rule;

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
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

@XmlRootElement(name = RoleName.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RoleName.Constants.TYPE_NAME, propOrder = {
    RoleName.Elements.NAME,
    RoleName.Elements.BASE_NAME,
    RoleName.Elements.RETURN_URL,
    RoleName.Elements.LABEL,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RoleName
    extends AbstractDataTransferObject
    implements RoleNameContract
{

    @XmlElement(name = Elements.NAME, required = true)
    private final String name;
    @XmlElement(name = Elements.BASE_NAME, required = true)
    private final String baseName;
    @XmlElement(name = Elements.RETURN_URL, required = false)
    private final String returnUrl;
    @XmlElement(name = Elements.LABEL, required = true)
    private final String label;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     *
     */
    private RoleName() {
        this.name = null;
        this.baseName = null;
        this.returnUrl = null;
        this.label = null;
    }

    private RoleName(Builder builder) {
        this.name = builder.getName();
        this.baseName = builder.getBaseName();
        this.returnUrl = builder.getReturnUrl();
        this.label = builder.getLabel();
    }

    public RoleName(String attributeClassName, String baseName, String label) {
        this(RoleName.Builder.createWithClassName(attributeClassName, baseName, label));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getBaseName() {
        return this.baseName;
    }

    @Override
    public String getReturnUrl() {
        return this.returnUrl;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public static String constructRoleValue(String attributeClassName, String roleName) {
    	return attributeClassName + "!" + roleName;
    }


    /**
     * A builder which can be used to construct {@link RoleName} instances.  Enforces the constraints of the {@link RoleNameContract}.
     *
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RoleNameContract
    {

        private String name;
        private String baseName;
        private String returnUrl;
        private String label;

        private Builder(String name, String baseName, String label) {
            setName(name);
            setBaseName(baseName);
            setLabel(label);
        }

        public static Builder createWithClassName(String attributeClassName, String baseName, String label) {
            return new Builder(constructRoleValue(attributeClassName, baseName), baseName, label);
        }

        public static Builder create(String name, String baseName, String label) {
            return new Builder(name, baseName, label);
        }

        public static Builder create(RoleNameContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getName(), contract.getBaseName(), contract.getLabel());
            builder.setReturnUrl(contract.getReturnUrl());
            return builder;
        }

        public RoleName build() {
            return new RoleName(this);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getBaseName() {
            return this.baseName;
        }

        @Override
        public String getReturnUrl() {
            return this.returnUrl;
        }

        @Override
        public String getLabel() {
            return this.label;
        }

        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new RiceIllegalArgumentException("name is blank");
            }
            this.name = name;
        }

        public void setBaseName(String baseName) {
            if (StringUtils.isBlank(baseName)) {
                throw new RiceIllegalArgumentException("baseName is blank");
            }
            this.baseName = baseName;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }

        public void setLabel(String label) {
            if (StringUtils.isBlank(label)) {
                throw new RiceIllegalArgumentException("label is blank");
            }
            this.label = label;
        }

    }


    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "roleName";
        final static String TYPE_NAME = "RoleNameType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String NAME = "name";
        final static String BASE_NAME = "baseName";
        final static String RETURN_URL = "returnUrl";
        final static String LABEL = "label";

    }

}
