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
package org.kuali.rice.kew.api.document.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.w3c.dom.Element;

@XmlRootElement(name = RouteNodeInstance.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RouteNodeInstance.Constants.TYPE_NAME, propOrder = {
    RouteNodeInstance.Elements.ID,
    RouteNodeInstance.Elements.NAME,
    RouteNodeInstance.Elements.STATE,
    RouteNodeInstance.Elements.DOCUMENT_ID,
    RouteNodeInstance.Elements.BRANCH_ID,
    RouteNodeInstance.Elements.ROUTE_NODE_ID,
    RouteNodeInstance.Elements.PROCESS_ID,
    RouteNodeInstance.Elements.ACTIVE,
    RouteNodeInstance.Elements.COMPLETE,
    RouteNodeInstance.Elements.INITIAL,
    RouteNodeInstance.Elements.NEXT_NODE_INSTANCES,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RouteNodeInstance extends AbstractDataTransferObject
    implements RouteNodeInstanceContract
{

    @XmlElement(name = Elements.NAME, required = false)
    private final String name;

    @XmlElementWrapper(name = Elements.STATE, required = false)
    @XmlElement(name = Elements.ROUTE_NODE_INSTANCE_STATE, required = false)
    private final List<RouteNodeInstanceState> state;

    @XmlElement(name = Elements.DOCUMENT_ID, required = false)
    private final String documentId;

    @XmlElement(name = Elements.BRANCH_ID, required = false)
    private final String branchId;

    @XmlElement(name = Elements.ROUTE_NODE_ID, required = false)
    private final String routeNodeId;

    @XmlElement(name = Elements.PROCESS_ID, required = false)
    private final String processId;

    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;

    @XmlElement(name = Elements.COMPLETE, required = false)
    private final boolean complete;

    @XmlElement(name = Elements.INITIAL, required = false)
    private final boolean initial;

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElementWrapper(name = Elements.NEXT_NODE_INSTANCES, required = false)
    @XmlElement(name = Elements.NEXT_NODE_INSTANCE, required = false)
    private final List<RouteNodeInstance> nextNodeInstances;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private RouteNodeInstance() {
        this.name = null;
        this.state = null;
        this.documentId = null;
        this.branchId = null;
        this.routeNodeId = null;
        this.processId = null;
        this.active = false;
        this.complete = false;
        this.initial = false;
        this.id = null;
        this.nextNodeInstances = null;
    }

    private RouteNodeInstance(Builder builder) {
        this.name = builder.getName();
        if (builder.getState() != null) {
            List<RouteNodeInstanceState> states = new ArrayList<RouteNodeInstanceState>();
            for(RouteNodeInstanceState.Builder stateBuilder : builder.getState()) {
                states.add(stateBuilder.build());
            }
            this.state = states;
        } else {
            this.state = Collections.emptyList();
        }

        if (builder.getNextNodeInstances() != null) {
            List<RouteNodeInstance> nextInstances = new ArrayList<RouteNodeInstance>();
            for (RouteNodeInstance.Builder instance : builder.getNextNodeInstances()) {
                nextInstances.add(instance.build());
            }
            this.nextNodeInstances = nextInstances;
        } else {
            this.nextNodeInstances = Collections.emptyList();
        }


        this.documentId = builder.getDocumentId();
        this.branchId = builder.getBranchId();
        this.routeNodeId = builder.getRouteNodeId();
        this.processId = builder.getProcessId();
        this.active = builder.isActive();
        this.complete = builder.isComplete();
        this.initial = builder.isInitial();
        this.id = builder.getId();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<RouteNodeInstanceState> getState() {
        return this.state;
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public String getBranchId() {
        return this.branchId;
    }

    @Override
    public String getRouteNodeId() {
        return this.routeNodeId;
    }

    @Override
    public String getProcessId() {
        return this.processId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean isComplete() {
        return this.complete;
    }

    @Override
    public boolean isInitial() {
        return this.initial;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public List<RouteNodeInstance> getNextNodeInstances() {
        return this.nextNodeInstances;
    }


    /**
     * A builder which can be used to construct {@link RouteNodeInstance} instances.  Enforces the constraints of the {@link RouteNodeInstanceContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RouteNodeInstanceContract
    {

        private String name;
        private List<RouteNodeInstanceState.Builder> state;
        private String documentId;
        private String branchId;
        private String routeNodeId;
        private String processId;
        private boolean active;
        private boolean complete;
        private boolean initial;
        private String id;
        private List<RouteNodeInstance.Builder> nextNodeInstances;

        private Builder() {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
        }

        public static Builder create() {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder();
        }

        public static Builder create(RouteNodeInstanceContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create();
            builder.setName(contract.getName());
            if (contract.getState() != null) {
                List<RouteNodeInstanceState.Builder> stateBuilders = new ArrayList<RouteNodeInstanceState.Builder>();
                for (RouteNodeInstanceStateContract stateContract : contract.getState()) {
                    stateBuilders.add(RouteNodeInstanceState.Builder.create(stateContract));
                }
                builder.setState(stateBuilders);
            }

            builder.setDocumentId(contract.getDocumentId());
            builder.setBranchId(contract.getBranchId());
            builder.setRouteNodeId(contract.getRouteNodeId());
            builder.setProcessId(contract.getProcessId());
            builder.setActive(contract.isActive());
            builder.setComplete(contract.isComplete());
            builder.setInitial(contract.isInitial());
            builder.setId(contract.getId());
            if (contract.getNextNodeInstances() != null) {
                List<RouteNodeInstance.Builder> instanceBuilders = new ArrayList<RouteNodeInstance.Builder>();
                for(RouteNodeInstanceContract instanceContract : contract.getNextNodeInstances()) {
                    instanceBuilders.add(RouteNodeInstance.Builder.create(instanceContract));
                }
                builder.setNextNodeInstances(instanceBuilders);
            }
            return builder;
        }

        public RouteNodeInstance build() {
            return new RouteNodeInstance(this);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public List<RouteNodeInstanceState.Builder> getState() {
            return this.state;
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public String getBranchId() {
            return this.branchId;
        }

        @Override
        public String getRouteNodeId() {
            return this.routeNodeId;
        }

        @Override
        public String getProcessId() {
            return this.processId;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public boolean isComplete() {
            return this.complete;
        }

        @Override
        public boolean isInitial() {
            return this.initial;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public List<RouteNodeInstance.Builder> getNextNodeInstances() {
            return this.nextNodeInstances;
        }

        public void setNextNodeInstances(List<RouteNodeInstance.Builder> nextNodeInstances) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.nextNodeInstances = Collections.unmodifiableList(nextNodeInstances);
        }

        public void setName(String name) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.name = name;
        }

        public void setState(List<RouteNodeInstanceState.Builder> state) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.state = state;
        }

        public void setDocumentId(String documentId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.documentId = documentId;
        }

        public void setBranchId(String branchId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.branchId = branchId;
        }

        public void setRouteNodeId(String routeNodeId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.routeNodeId = routeNodeId;
        }

        public void setProcessId(String processId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.processId = processId;
        }

        public void setActive(boolean active) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.active = active;
        }

        public void setComplete(boolean complete) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.complete = complete;
        }

        public void setInitial(boolean initial) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.initial = initial;
        }

        public void setId(String id) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.id = id;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "routeNodeInstance";
        final static String TYPE_NAME = "RouteNodeInstanceType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String NAME = "name";
        final static String STATE = "state";
        final static String ROUTE_NODE_INSTANCE_STATE = "routeNodeInstanceState";
        final static String DOCUMENT_ID = "documentId";
        final static String BRANCH_ID = "branchId";
        final static String ROUTE_NODE_ID = "routeNodeId";
        final static String PROCESS_ID = "processId";
        final static String ACTIVE = "active";
        final static String COMPLETE = "complete";
        final static String INITIAL = "initial";
        final static String ID = "id";
        final static String NEXT_NODE_INSTANCES = "nextNodeInstances";
        final static String NEXT_NODE_INSTANCE = "nextNodeInstance";
    }

}
