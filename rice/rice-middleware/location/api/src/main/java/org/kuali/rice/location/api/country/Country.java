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
package org.kuali.rice.location.api.country;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.location.api.LocationConstants;
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
 * POJO implementation of CountryContract that is immutable. Instances of Country can be (un)marshalled to and from XML.
 *
 * @see CountryContract
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = Country.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Country.Constants.TYPE_NAME, propOrder = {
        Country.Elements.CODE,
        Country.Elements.ALTERNATE_CODE,
        Country.Elements.NAME,
        Country.Elements.RESTRICTED,
        Country.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Country extends AbstractDataTransferObject implements CountryContract {
    private static final long serialVersionUID = -8975392777320033940L;

    @XmlElement(name = Elements.CODE, required = true)
    private final String code;

    @XmlElement(name = Elements.ALTERNATE_CODE, required = false)
    private final String alternateCode;

    @XmlElement(name = Elements.NAME, required = false)
    private final String name;

    @XmlElement(name = Elements.RESTRICTED, required = true)
    private final boolean restricted;

    @XmlElement(name = Elements.ACTIVE, required = true)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * This constructor should never be called except during JAXB unmarshalling.
     */
    @SuppressWarnings("unused")
    private Country() {
        this.code = null;
        this.alternateCode = null;
        this.name = null;
        this.restricted = false;
        this.active = false;
        this.versionNumber = null;
    }

    private Country(Builder builder) {
        this.code = builder.getCode();
        this.alternateCode = builder.getAlternateCode();
        this.name = builder.getName();
        this.restricted = builder.isRestricted();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
    }

    /** {@inheritDoc} */
    @Override
    public String getCode() {
        return this.code;
    }

    /** {@inheritDoc} */
    @Override
    public String getAlternateCode() {
        return this.alternateCode;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRestricted() {
        return this.restricted;
    }

    /** {@inheritDoc} */
    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * Builder for immutable Country objects.
     */
    public static class Builder implements CountryContract, ModelBuilder, Serializable {
        private static final long serialVersionUID = -4786917485397379322L;

        private String code;
        private String alternateCode;
        private String name;
        private boolean restricted;
        private boolean active;
        private Long versionNumber;

        private Builder(String code, String alternateCode, String name,
                        boolean restricted, boolean active) {
            this.setCode(code);
            this.setAlternateCode(alternateCode);
            this.setName(name);
            this.setRestricted(restricted);
            this.setActive(active);
        }

        public static Builder create(String code, String name) {
            return new Builder(code, null, name, false, true);
        }

        public static Builder create(String code, String alternatePostalCode, String name,
                                     boolean restricted, boolean active) {
            return new Builder(code, alternatePostalCode, name, restricted, active);
        }

        public static Builder create(CountryContract cc) {
            Builder builder = new Builder(cc.getCode(), cc.getAlternateCode(),
                    cc.getName(), cc.isRestricted(), cc.isActive());
            builder.setVersionNumber(cc.getVersionNumber());
            return builder;
        }

        @Override
        public Country build() {
            return new Country(this);
        }

        /**
         * Sets code property.
         *
         * @param code required to be not null and not empty.
         */
        public void setCode(String code) {
            if (StringUtils.isBlank(code)) {
                throw new IllegalArgumentException("code cannot be blank or null");
            }
            this.code = code;
        }

        @Override
        public String getCode() {
            return this.code;
        }

        /**
         * Sets the optional alternatePostalCode property
         *
         * @param alternatePostalCode
         */
        public void setAlternateCode(String alternatePostalCode) {
            this.alternateCode = alternatePostalCode;
        }

        @Override
        public String getAlternateCode() {
            return this.alternateCode;
        }

        /**
         * Sets the optional name property.
         *
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        /**
         * Sets the active property.
         *
         * @param active
         */
        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        /**
         * Sets the versionNumber property.
         *
         * @param versionNumber
         */
        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        /**
         * Sets the restrictedProperty
         *
         * @param restricted
         */
        public void setRestricted(boolean restricted) {
            this.restricted = restricted;
        }

        @Override
        public boolean isRestricted() {
            return this.restricted;
        }
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String CODE = "code";
        final static String ALTERNATE_CODE = "alternateCode";
        final static String NAME = "name";
        final static String RESTRICTED = "restricted";
        final static String ACTIVE = "active";
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "country";
        final static String TYPE_NAME = "CountryType";
    }

    public static class Cache {
        public static final String NAME = LocationConstants.Namespaces.LOCATION_NAMESPACE_2_0 + "/" + Country.Constants.TYPE_NAME;
    }
}
