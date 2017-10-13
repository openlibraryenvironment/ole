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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

@XmlRootElement(name = ProcessDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ProcessDefinition.Constants.TYPE_NAME, propOrder = {
        ProcessDefinition.Elements.ID,
        ProcessDefinition.Elements.NAME,
        ProcessDefinition.Elements.DOCUMENT_TYPE_ID,
        ProcessDefinition.Elements.INITIAL_ROUTE_NODE,
        ProcessDefinition.Elements.INITIAL,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ProcessDefinition extends AbstractDataTransferObject implements ProcessDefinitionContract {

    private static final long serialVersionUID = 1076976038764944504L;

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.NAME, required = true)
    private final String name;

    @XmlElement(name = Elements.DOCUMENT_TYPE_ID, required = false)
    private final String documentTypeId;

    @XmlElement(name = Elements.INITIAL_ROUTE_NODE, required = false)
    private final RouteNode initialRouteNode;

    @XmlElement(name = Elements.INITIAL, required = true)
    private final boolean initial;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private ProcessDefinition() {
        this.id = null;
        this.name = null;
        this.documentTypeId = null;
        this.initialRouteNode = null;
        this.initial = false;
        this.versionNumber = null;
    }

    private ProcessDefinition(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.documentTypeId = builder.getDocumentTypeId();
        if (builder.getInitialRouteNode() != null) {
            this.initialRouteNode = builder.getInitialRouteNode().build();
        } else {
            this.initialRouteNode = null;
        }
        this.initial = builder.isInitial();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDocumentTypeId() {
        return this.documentTypeId;
    }

    @Override
    public RouteNodeContract getInitialRouteNode() {
        return this.initialRouteNode;
    }

    @Override
    public boolean isInitial() {
        return this.initial;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * A builder which can be used to construct {@link ProcessDefinition} instances. Enforces the constraints
     * of the {@link ProcessDefinitionContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, ProcessDefinitionContract {

        private static final long serialVersionUID = 5897271312287857808L;

        private String id;
        private String name;
        private String documentTypeId;
        private RouteNode.Builder initialRouteNode;
        private boolean initial;
        private Long versionNumber;

        private Builder(String name, RouteNode.Builder initialRouteNode, boolean initial) {
            setName(name);
            setInitialRouteNode(initialRouteNode);
            setInitial(initial);
        }

        /**
         * Create a new ProcessDefinition.Builder
         * @param name The name for the process.
         * @param initialRouteNode The first route node for the process.  May be null.
         * @param initial
         * @return
         */
        public static Builder create(String name, RouteNode.Builder initialRouteNode, boolean initial) {
            return new Builder(name, initialRouteNode, initial);
        }

        public static Builder create(ProcessDefinitionContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            RouteNode.Builder initialRouteNode = null;
            if (contract.getInitialRouteNode() != null) {
                initialRouteNode = RouteNode.Builder.create(contract.getInitialRouteNode());
            }

            Builder builder = create(contract.getName(), initialRouteNode, contract.isInitial());
            builder.setDocumentTypeId(contract.getDocumentTypeId());
            builder.setId(contract.getId());
            return builder;
        }

        public ProcessDefinition build() {
            return new ProcessDefinition(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getDocumentTypeId() {
            return this.documentTypeId;
        }

        @Override
        public RouteNode.Builder getInitialRouteNode() {
            return this.initialRouteNode;
        }

        @Override
        public boolean isInitial() {
            return this.initial;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name was null or blank");
            }
            this.name = name;
        }

        public void setDocumentTypeId(String documentTypeId) {
            this.documentTypeId = documentTypeId;
        }

        public void setInitialRouteNode(RouteNode.Builder initialRouteNode) {
            this.initialRouteNode = initialRouteNode;
        }

        public void setInitial(boolean initial) {
            this.initial = initial;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "process";
        final static String TYPE_NAME = "ProcessType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this
     * object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String NAME = "name";
        final static String DOCUMENT_TYPE_ID = "documentTypeId";
        final static String INITIAL_ROUTE_NODE = "initialRouteNode";
        final static String INITIAL = "initial";
    }

}
