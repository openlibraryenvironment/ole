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
package org.kuali.rice.location.api.state;

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
 * POJO implementation of StateContract that is immutable. Instances of State can be (un)marshalled to and from XML.
 *
 * @see StateContract
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = State.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = State.Constants.TYPE_NAME, propOrder = {
        State.Elements.CODE,
        State.Elements.NAME,
        State.Elements.COUNTRY_CODE,
        State.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class State extends AbstractDataTransferObject implements StateContract {

    private static final long serialVersionUID = 6097498602725305353L;

    @XmlElement(name = Elements.CODE, required = true)
    private final String code;

    @XmlElement(name = Elements.NAME, required = true)
    private final String name;

    @XmlElement(name = Elements.COUNTRY_CODE, required = true)
    private final String countryCode;

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
    private State() {
        this.code = null;
        this.name = null;
        this.countryCode = null;
        this.active = false;
        this.versionNumber = null;
    }

    private State(Builder builder) {
        code = builder.getCode();
        name = builder.getName();
        countryCode = builder.getCountryCode();
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
    public boolean isActive() {
        return active;
    }

    /** {@inheritDoc} */
    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    /**
     * This builder constructs a State enforcing the constraints of the {@link StateContract}.
     */
    public static class Builder implements StateContract, ModelBuilder, Serializable {

        private static final long serialVersionUID = 7077484401017765844L;

        private String code;

        private String name;

        private String countryCode;

        private boolean active;

        private Long versionNumber;

        private Builder(String code, String name, String countryCode) {
            setCode(code);
            setName(name);
            setCountryCode(countryCode);
        }

        /**
         * creates a State with the required fields.
         * @param code represents code for the State being built
         * @param name represents full name for the State being built
         * @param countryCode code for the Country this State is associated with
         * @return a bootstrapped Builder defaulted with the passed in code, name, and countryCode.
         */
        public static Builder create(String code, String name, String countryCode) {
            final Builder builder = new Builder(code, name, countryCode);
            builder.setActive(true);
            return builder;
        }

        /**
         * creates a Parameter from an existing {@link StateContract}.
         */
        public static Builder create(StateContract contract) {
            final Builder builder = new Builder(contract.getCode(), contract.getName(), contract.getCountryCode());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        @Override
        public String getCode() {
            return code;
        }

        /**
         * Sets the code to be used for the State created from this Builder.
         * @param code String code for a State.
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
         * Sets the full name of the State created from this Builder.
         * @param name String representing the full name for the State
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
         * Sets the Country code to be associated with the State created from this Builder.
         * @param countryCode String representing the Country Code
         * @throws IllegalArgumentException if the passed in countryCode is null or a blank String.
         */
        public void setCountryCode(String countryCode) {
            if (StringUtils.isBlank(countryCode)) {
                throw new IllegalArgumentException("countryCode is blank");
            }

            this.countryCode = countryCode;
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
        public State build() {
            return new State(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "state";
        final static String TYPE_NAME = "StateType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String CODE = "code";
        final static String NAME = "name";
        final static String COUNTRY_CODE = "countryCode";
        final static String ACTIVE = "active";
    }

    public static class Cache {
        public static final String NAME = LocationConstants.Namespaces.LOCATION_NAMESPACE_2_0 + "/" + State.Constants.TYPE_NAME;
    }
}
