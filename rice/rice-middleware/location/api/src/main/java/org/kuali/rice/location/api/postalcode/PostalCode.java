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
package org.kuali.rice.location.api.postalcode;


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
 * An immutable representation of a {@link PostalCodeContract}.
 *
 * <p>To construct an instance of a PostalCode, use the {@link PostalCode.Builder} class.
 *
 * @see PostalCodeContract
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = PostalCode.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = PostalCode.Constants.TYPE_NAME, propOrder = {
        PostalCode.Elements.CODE,
        PostalCode.Elements.CITY_NAME,
        PostalCode.Elements.COUNTRY_CODE,
        PostalCode.Elements.STATE_CODE,
        PostalCode.Elements.ACTIVE,
        PostalCode.Elements.COUNTY_CODE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class PostalCode extends AbstractDataTransferObject implements PostalCodeContract {

    private static final long serialVersionUID = 6097498602725305353L;

    @XmlElement(name = Elements.CODE, required = true)
    private final String code;

    @XmlElement(name = Elements.CITY_NAME, required = false)
    private final String cityName;

    @XmlElement(name = Elements.COUNTRY_CODE, required = true)
    private final String countryCode;

    @XmlElement(name = Elements.STATE_CODE, required = false)
    private final String stateCode;

    @XmlElement(name = Elements.COUNTY_CODE, required = false)
    private final String countyCode;

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
    private PostalCode() {
        this.code = null;
        this.cityName = null;
        this.countryCode = null;
        this.stateCode = null;
        this.countyCode = null;
        this.active = false;
        this.versionNumber = null;
    }

    private PostalCode(Builder builder) {
        code = builder.getCode();
        cityName = builder.getCityName();
        countryCode = builder.getCountryCode();
        stateCode = builder.getStateCode();
        countyCode = builder.getCountyCode();
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
    public String getCityName() {
        return cityName;
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
    public String getCountyCode() {
        return countyCode;
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
     * This builder constructs a PostalCode enforcing the constraints of the {@link PostalCodeContract}.
     */
    public static class Builder implements PostalCodeContract, ModelBuilder, Serializable {

        private static final long serialVersionUID = 7077484401017765844L;

        private String code;
        private String cityName;
        private String countryCode;
        private String stateCode;
        private String countyCode;
        private boolean active;
        private Long versionNumber;

        private Builder(String code, String countryCode) {
            setCode(code);
            setCountryCode(countryCode);
        }

        /**
         * creates a PostalCode builder with the required fields.
         */
        public static Builder create(String code, String countryCode) {
            final Builder builder = new Builder(code, countryCode);
            builder.setActive(true);
            return builder;
        }

        /**
         * creates a PostalCode builder from an existing {@link PostalCodeContract}.
         */
        public static Builder create(PostalCodeContract contract) {
            final Builder builder = new Builder(contract.getCode(), contract.getCountryCode());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            if (StringUtils.isNotBlank(contract.getCountyCode())) {
                builder.setCountyCode(contract.getCountyCode());
            }

            if (StringUtils.isNotBlank(contract.getCityName())) {
                builder.setCityName(contract.getCityName());
            }

            if (StringUtils.isNotBlank(contract.getStateCode())) {
                builder.setStateCode(contract.getStateCode());
            }
            return builder;
        }

        @Override
        public String getCode() {
            return code;
        }

        /**
         * Sets the code for the PostalCode created from this Builder.
         *
         * @param code String code for the PostalCode
         * @throws IllegalArgumentException if the passed in code is null or a blank String.
         */
        public void setCode(String code) {
            if (StringUtils.isBlank(code)) {
                throw new IllegalArgumentException("code is blank");
            }

            this.code = code;
        }

        @Override
        public String getCityName() {
            return cityName;
        }

        /**
         * Sets the name of the city associated with the PostalCode to be created from this Builder.
         *
         * @param cityName String representing the name of the City
         * @throws IllegalArgumentException if the passed in cityname is null or a blank String.
         */
        public void setCityName(String cityName) {
            if (StringUtils.isBlank(cityName)) {
                throw new IllegalArgumentException("cityName is blank");
            }

            this.cityName = cityName;
        }

        @Override
        public String getCountryCode() {
            return countryCode;
        }

        /**
         * Sets the Country code to be associated with the PostalCode created from this Builder.
         *
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
         * Sets the State code to be associated with the PostalCode created from this Builder.
         *
         * @param stateCode String representing the State code
         * @throws IllegalArgumentException if the passed in stateCode is null or a blank String.
         * @see org.kuali.rice.location.api.state.StateContract
         */
        public void setStateCode(String stateCode) {
            if (StringUtils.isBlank(stateCode)) {
                throw new IllegalArgumentException("stateCode is blank");
            }

            this.stateCode = stateCode;
        }

        @Override
        public String getCountyCode() {
            return countyCode;
        }

        /**
         * Sets the County code to be associated with the PostalCode created from this Builder.
         *
         * @param countyCode String representing the County code
         * @throws IllegalArgumentException if the passed in countyCode is null or a blank String.
         * @see org.kuali.rice.location.api.county.CountyContract
         */
        public void setCountyCode(String countyCode) {
            if (StringUtils.isBlank(countyCode)) {
                throw new IllegalArgumentException("countyCode is blank");
            }

            this.countyCode = countyCode;
        }

        @Override
        public boolean isActive() {
            return active;
        }

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
        public PostalCode build() {
            return new PostalCode(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "postalCode";
        final static String TYPE_NAME = "PostalCodeType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String CODE = "code";
        final static String CITY_NAME = "cityName";
        final static String COUNTRY_CODE = "countryCode";
        final static String STATE_CODE = "stateCode";
        final static String COUNTY_CODE = "countyCode";
        final static String ACTIVE = "active";
    }

    public static class Cache {
        public static final String NAME = LocationConstants.Namespaces.LOCATION_NAMESPACE_2_0 + "/" + PostalCode.Constants.TYPE_NAME;
    }
}
