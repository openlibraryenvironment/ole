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
package org.kuali.rice.location.api.county;

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
 * An immutable representation of a {@link CountyContract}.
 *
 * <p>To construct an instance of a County, use the {@link County.Builder} class.
 *
 * @see CountyContract
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = County.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = County.Constants.TYPE_NAME, propOrder = {
        County.Elements.CODE,
        County.Elements.NAME,
        County.Elements.COUNTRY_CODE,
        County.Elements.STATE_CODE,
        County.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class County extends AbstractDataTransferObject implements CountyContract {

    private static final long serialVersionUID = 6097498602725305353L;

    @XmlElement(name = Elements.CODE, required = true)
    private final String code;

    @XmlElement(name = Elements.NAME, required = true)
    private final String name;

    @XmlElement(name = Elements.COUNTRY_CODE, required = true)
    private final String countryCode;

    @XmlElement(name = Elements.STATE_CODE, required = true)
    private final String stateCode;

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
    private County() {
        this.code = null;
        this.name = null;
        this.countryCode = null;
        this.stateCode = null;
        this.active = false;
        this.versionNumber = null;
    }

    private County(Builder builder) {
        code = builder.getCode();
        name = builder.getName();
        countryCode = builder.getCountryCode();
        stateCode = builder.getStateCode();
        active = builder.isActive();
        versionNumber = builder.getVersionNumber();
    }

    /** {@inheritDoc} */
    @Override
    public String getCode() {
        return code;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public String getCountryCode() {
        return countryCode;
    }

    /** {@inheritDoc} */
    @Override
    public String getStateCode() {
        return stateCode;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isActive() {
        return active;
    }

    /** {@inheritDoc} */
    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    /**
     * This builder constructs an County enforcing the constraints of the {@link CountyContract}.
     */
    public static class Builder implements CountyContract, ModelBuilder, Serializable {

        private static final long serialVersionUID = 7077484401017765844L;

        private String code;
        private String name;
        private String countryCode;
        private String stateCode;
        private boolean active;
        private Long versionNumber;

        private Builder(String code, String name, String countryCode, String stateCode) {
            setCode(code);
            setName(name);
            setCountryCode(countryCode);
            setStateCode(stateCode);
            setVersionNumber(versionNumber);
        }

        /**
         * creates a County Builder with the required fields.
         */
        public static Builder create(String code, String name, String countryCode, String stateCode) {
            final Builder builder = new Builder(code, name, countryCode, stateCode);
            builder.setActive(true);
            return builder;
        }

        /**
         * creates a County Builder from an existing {@link CountyContract}.
         */
        public static Builder create(CountyContract contract) {
            final Builder builder = new Builder(contract.getCode(), contract.getName(), contract.getCountryCode(), contract.getStateCode());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        @Override
        public String getCode() {
            return code;
        }

        /**
         * Sets the code to be used for the County created from this Builder.
         * @param code String code for a County
         * @throws IllegalArgumentException if the passed in code is null or a blank String.
         */
        public void setCode(String code) {
            if (StringUtils.isBlank(code)) {
                throw new IllegalArgumentException("code is blank");
            }

            this.code = code;
        }

        @Override
        public String getName() {
            return name;
        }

        /**
         * Sets the full name of the County created from this Builder.
         * @param name String representing the full name for the County
         * @throws IllegalArgumentException if the passed in name is null or a blank String.
         */
        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }

            this.name = name;
        }

        @Override
        public String getCountryCode() {
            return countryCode;
        }

        /**
         * Sets the Country code to be associated with the County created from this Builder.
         * @param countryCode String representing the Country Code
         * @throws IllegalArgumentException if the passed in countryCode is null or a blank String.
         * @see org.kuali.rice.location.api.country.CountryContract
         */
        public void setCountryCode(String countryCode) {
            if (StringUtils.isBlank(countryCode)) {
                throw new IllegalArgumentException("countryCode is blank");
            }

            this.countryCode = countryCode;
        }

        @Override
        public String getStateCode() {
            return stateCode;
        }

        /**
         * Sets the State code to be associated with the County created from this Builder.
         * @param stateCode String representing the State code
         * @throws  IllegalArgumentException if the passed in statecode is null or a blank String.
         * @see org.kuali.rice.location.api.state.StateContract
         */
        public void setStateCode(String stateCode) {
            if (StringUtils.isBlank(stateCode)) {
                throw new IllegalArgumentException("stateCode is blank");
            }

            this.stateCode = stateCode;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        /**
         * Sets the active flag for the County created from this Builder.
         * @param active
         */
        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        @Override
        public County build() {
            return new County(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "county";
        final static String TYPE_NAME = "CountyType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String CODE = "code";
        final static String NAME = "name";
        final static String COUNTRY_CODE = "countryCode";
        final static String STATE_CODE = "stateCode";
        final static String ACTIVE = "active";
    }

    public static class Cache {
        public static final String NAME = LocationConstants.Namespaces.LOCATION_NAMESPACE_2_0 + "/" + County.Constants.TYPE_NAME;
    }
}
