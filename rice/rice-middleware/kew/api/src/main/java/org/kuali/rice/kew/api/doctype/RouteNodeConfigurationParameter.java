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
package org.kuali.rice.kew.api.doctype;

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
import org.w3c.dom.Element;

@XmlRootElement(name = RouteNodeConfigurationParameter.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RouteNodeConfigurationParameter.Constants.TYPE_NAME, propOrder = {
        RouteNodeConfigurationParameter.Elements.ID,
        RouteNodeConfigurationParameter.Elements.ROUTE_NODE_ID,
        RouteNodeConfigurationParameter.Elements.KEY,
        RouteNodeConfigurationParameter.Elements.VALUE,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RouteNodeConfigurationParameter extends AbstractDataTransferObject
        implements RouteNodeConfigurationParameterContract {

    private static final long serialVersionUID = 3494982096398369148L;

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.ROUTE_NODE_ID, required = false)
    private final String routeNodeId;

    @XmlElement(name = Elements.KEY, required = true)
    private final String key;

    @XmlElement(name = Elements.VALUE, required = false)
    private final String value;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private RouteNodeConfigurationParameter() {
        this.id = null;
        this.routeNodeId = null;
        this.key = null;
        this.value = null;
    }

    private RouteNodeConfigurationParameter(Builder builder) {
        this.id = builder.getId();
        this.routeNodeId = builder.getRouteNodeId();
        this.key = builder.getKey();
        this.value = builder.getValue();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getRouteNodeId() {
        return this.routeNodeId;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * A builder which can be used to construct {@link RouteNodeConfigurationParameter} instances.
     * Enforces the constraints of the {@link RouteNodeConfigurationParameterContract}.
     * 
     */
    public final static class Builder
            implements Serializable, ModelBuilder, RouteNodeConfigurationParameterContract {

        private static final long serialVersionUID = -7040162478345153231L;
        
        private String id;
        private String routeNodeId;
        private String key;
        private String value;

        private Builder(String key) {
            setKey(key);
        }

        public static Builder create(String key) {
            return new Builder(key);
        }

        public static Builder create(RouteNodeConfigurationParameterContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getKey());
            builder.setId(contract.getId());
            builder.setRouteNodeId(contract.getRouteNodeId());
            builder.setValue(contract.getValue());
            return builder;
        }

        public RouteNodeConfigurationParameter build() {
            return new RouteNodeConfigurationParameter(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getRouteNodeId() {
            return this.routeNodeId;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getValue() {
            return this.value;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setRouteNodeId(String routeNodeId) {
            this.routeNodeId = routeNodeId;
        }

        public void setKey(String key) {
            if (StringUtils.isBlank(key)) {
                throw new IllegalArgumentException("key was null or blank");
            }
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "routeNodeConfigurationParameter";
        final static String TYPE_NAME = "RouteNodeConfigurationParameterType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this
     * object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String ROUTE_NODE_ID = "routeNodeId";
        final static String KEY = "key";
        final static String VALUE = "value";
    }

}
