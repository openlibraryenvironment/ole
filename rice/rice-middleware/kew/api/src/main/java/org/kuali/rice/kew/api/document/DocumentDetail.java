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
package org.kuali.rice.kew.api.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.w3c.dom.Element;

@XmlRootElement(name = DocumentDetail.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentDetail.Constants.TYPE_NAME, propOrder = {
    DocumentDetail.Elements.DOCUMENT,
    DocumentDetail.Elements.ACTION_REQUESTS,
    DocumentDetail.Elements.ACTIONS_TAKEN,
    DocumentDetail.Elements.ROUTE_NODE_INSTANCES,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentDetail extends AbstractDataTransferObject implements DocumentDetailContract {

	private static final long serialVersionUID = -8569515693892958719L;

	@XmlElement(name = Elements.DOCUMENT, required = true)
    private final Document document;
    
    @XmlElementWrapper(name = Elements.ACTION_REQUESTS, required = true)
    @XmlElement(name = Elements.ACTION_REQUEST, required = false)
    private final List<ActionRequest> actionRequests;
    
    @XmlElementWrapper(name = Elements.ACTIONS_TAKEN, required = true)
    @XmlElement(name = Elements.ACTION_TAKEN, required = false)
    private final List<ActionTaken> actionsTaken;
    
    @XmlElementWrapper(name = Elements.ROUTE_NODE_INSTANCES, required = true)
    @XmlElement(name = Elements.ROUTE_NODE_INSTANCES, required = true)
    private final List<RouteNodeInstance> routeNodeInstances;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private DocumentDetail() {
        this.document = null;
        this.actionRequests = null;
        this.actionsTaken = null;
        this.routeNodeInstances = null;
    }

    private DocumentDetail(Builder builder) {
        this.document = builder.getDocument();
        this.actionRequests = builder.getActionRequests();
        this.actionsTaken = builder.getActionsTaken();
        this.routeNodeInstances = builder.getRouteNodeInstances();
    }

    @Override
    public Document getDocument() {
        return this.document;
    }

    @Override
    public List<ActionRequest> getActionRequests() {
        return this.actionRequests;
    }

    @Override
    public List<ActionTaken> getActionsTaken() {
        return this.actionsTaken;
    }

    @Override
    public List<RouteNodeInstance> getRouteNodeInstances() {
        return this.routeNodeInstances;
    }


    /**
     * A builder which can be used to construct {@link DocumentDetail} instances.  Enforces the constraints of the {@link DocumentDetailContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DocumentDetailContract {

		private static final long serialVersionUID = 3108177943363491329L;

		private Document document;
        private List<ActionRequest> actionRequests;
        private List<ActionTaken> actionsTaken;
        private List<RouteNodeInstance> routeNodeInstances;

        private Builder() {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
        }

        public static Builder create(Document document) {
            Builder builder = new Builder();
            builder.setDocument(document);
            builder.setActionRequests(new ArrayList<ActionRequest>());
            builder.setActionsTaken(new ArrayList<ActionTaken>());
            builder.setRouteNodeInstances(new ArrayList<RouteNodeInstance>());
            return builder;
        }

        public static Builder create(DocumentDetailContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getDocument());
            builder.setActionRequests(contract.getActionRequests());
            builder.setActionsTaken(contract.getActionsTaken());
            builder.setRouteNodeInstances(contract.getRouteNodeInstances());
            return builder;
        }

        public DocumentDetail build() {
            return new DocumentDetail(this);
        }

        @Override
        public Document getDocument() {
            return this.document;
        }

        @Override
        public List<ActionRequest> getActionRequests() {
            return this.actionRequests;
        }

        @Override
        public List<ActionTaken> getActionsTaken() {
            return this.actionsTaken;
        }

        @Override
        public List<RouteNodeInstance> getRouteNodeInstances() {
            return this.routeNodeInstances;
        }

        public void setDocument(Document document) {
            if (document == null) {
            	throw new IllegalArgumentException("document was null");
            }
            this.document = document;
        }

        public void setActionRequests(List<ActionRequest> actionRequests) {
            if (actionRequests == null) {
            	throw new IllegalArgumentException("actionRequests was null");
            }
            this.actionRequests = actionRequests;
        }

        public void setActionsTaken(List<ActionTaken> actionsTaken) {
        	if (actionsTaken == null) {
            	throw new IllegalArgumentException("actionsTaken was null");
            }
            this.actionsTaken = actionsTaken;
        }

        public void setRouteNodeInstances(List<RouteNodeInstance> routeNodeInstances) {
        	if (routeNodeInstances == null) {
            	throw new IllegalArgumentException("routeNodeInstances was null");
            }
            this.routeNodeInstances = routeNodeInstances;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentDetail";
        final static String TYPE_NAME = "DocumentDetailType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String DOCUMENT = "document";
        final static String ACTION_REQUESTS = "actionRequests";
        final static String ACTION_REQUEST = "actionRequest";
        final static String ACTIONS_TAKEN = "actionsTaken";
        final static String ACTION_TAKEN = "actionTaken";
        final static String ROUTE_NODE_INSTANCES = "routeNodeInstances";
        final static String ROUTE_NODE_INSTANCE = "routeNodeInstance";
    }

}

