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
import org.kuali.rice.core.api.util.collect.CollectionUtils;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name = RouteNode.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RouteNode.Constants.TYPE_NAME, propOrder = {
        RouteNode.Elements.ID,
        RouteNode.Elements.DOCUMENT_TYPE_ID,
        RouteNode.Elements.NAME,
        RouteNode.Elements.ROUTE_METHOD_NAME,
        RouteNode.Elements.ROUTE_METHOD_CODE,
        RouteNode.Elements.FINAL_APPROVAL,
        RouteNode.Elements.MANDATORY,
        RouteNode.Elements.ACTIVATION_TYPE,
        RouteNode.Elements.EXCEPTION_GROUP_ID,
        RouteNode.Elements.TYPE,
        RouteNode.Elements.BRANCH_NAME,
        RouteNode.Elements.NEXT_DOCUMENT_STATUS,
        RouteNode.Elements.CONFIGURATION_PARAMETERS,
        RouteNode.Elements.PREVIOUS_NODE_IDS,
        RouteNode.Elements.NEXT_NODE_IDS,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RouteNode extends AbstractDataTransferObject implements RouteNodeContract {

    private static final long serialVersionUID = -1756380702013287285L;

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.DOCUMENT_TYPE_ID, required = false)
    private final String documentTypeId;

    @XmlElement(name = Elements.NAME, required = true)
    private final String name;

    @XmlElement(name = Elements.ROUTE_METHOD_NAME, required = false)
    private final String routeMethodName;

    @XmlElement(name = Elements.ROUTE_METHOD_CODE, required = false)
    private final String routeMethodCode;

    @XmlElement(name = Elements.FINAL_APPROVAL, required = false)
    private final boolean finalApproval;

    @XmlElement(name = Elements.MANDATORY, required = false)
    private final boolean mandatory;

    @XmlElement(name = Elements.ACTIVATION_TYPE, required = false)
    private final String activationType;

    @XmlElement(name = Elements.EXCEPTION_GROUP_ID, required = false)
    private final String exceptionGroupId;

    @XmlElement(name = Elements.TYPE, required = true)
    private final String type;

    @XmlElement(name = Elements.BRANCH_NAME, required = false)
    private final String branchName;
    
    @XmlElement(name = Elements.NEXT_DOCUMENT_STATUS, required = false)
    private final String nextDocumentStatus;
    
    @XmlElementWrapper(name = Elements.CONFIGURATION_PARAMETERS, required = false)
    @XmlElement(name = Elements.CONFIGURATION_PARAMETER, required = false)
    private final List<RouteNodeConfigurationParameter> configurationParameters;

    @XmlElementWrapper(name = Elements.PREVIOUS_NODE_IDS, required = false)
    @XmlElement(name = Elements.PREVIOUS_NODE_ID, required = false)
    private final List<String> previousNodeIds;

    @XmlElementWrapper(name = Elements.NEXT_NODE_IDS, required = false)
    @XmlElement(name = Elements.NEXT_NODE_ID, required = false)
    private final List<String> nextNodeIds;
    
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private RouteNode() {
        this.id = null;
        this.documentTypeId = null;
        this.name = null;
        this.routeMethodName = null;
        this.routeMethodCode = null;
        this.finalApproval = false;
        this.mandatory = false;
        this.activationType = null;
        this.exceptionGroupId = null;
        this.type = null;
        this.branchName = null;
        this.nextDocumentStatus = null;
        this.configurationParameters = null;
        this.previousNodeIds = null;
        this.nextNodeIds = null;
        this.versionNumber = null;
    }

    private RouteNode(Builder builder) {
        this.id = builder.getId();
        this.documentTypeId = builder.getDocumentTypeId();
        this.name = builder.getName();
        this.routeMethodName = builder.getRouteMethodName();
        this.routeMethodCode = builder.getRouteMethodCode();
        this.finalApproval = builder.isFinalApproval();
        this.mandatory = builder.isMandatory();
        this.activationType = builder.getActivationType();
        this.exceptionGroupId = builder.getExceptionGroupId();
        this.type = builder.getType();
        this.branchName = builder.getBranchName();
        this.nextDocumentStatus = builder.getNextDocumentStatus();
        this.configurationParameters = new ArrayList<RouteNodeConfigurationParameter>();
        if (builder.getConfigurationParameters() != null) {
            for (RouteNodeConfigurationParameter.Builder configurationParameter : builder.getConfigurationParameters()) {
                this.configurationParameters.add(configurationParameter.build());
            }
        }
        this.previousNodeIds = new ArrayList<String>(builder.getPreviousNodeIds());
        this.nextNodeIds = new ArrayList<String>(builder.getNextNodeIds());
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
    public String getRouteMethodName() {
        return this.routeMethodName;
    }

    @Override
    public String getRouteMethodCode() {
        return this.routeMethodCode;
    }

    @Override
    public boolean isFinalApproval() {
        return this.finalApproval;
    }

    @Override
    public boolean isMandatory() {
        return this.mandatory;
    }

    @Override
    public String getActivationType() {
        return this.activationType;
    }

    @Override
    public String getExceptionGroupId() {
        return this.exceptionGroupId;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getBranchName() {
        return this.branchName;
    }

    @Override
    public String getNextDocumentStatus() {
        return this.nextDocumentStatus;
    }

    @Override
    public List<RouteNodeConfigurationParameter> getConfigurationParameters() {
        return CollectionUtils.unmodifiableListNullSafe(this.configurationParameters);
    }

    @Override
    public List<String> getPreviousNodeIds() {
        return CollectionUtils.unmodifiableListNullSafe(this.previousNodeIds);
    }

    @Override
    public List<String> getNextNodeIds() {
        return CollectionUtils.unmodifiableListNullSafe(this.nextNodeIds);
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * A builder which can be used to construct {@link RouteNode} instances. Enforces the
     * constraints of the {@link RouteNodeContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, RouteNodeContract {

        private static final long serialVersionUID = 7076065049417371344L;
        private String id;
        private String documentTypeId;
        private String name;
        private String routeMethodName;
        private String routeMethodCode;
        private boolean finalApproval;
        private boolean mandatory;
        private String activationType;
        private String exceptionGroupId;
        private String type;
        private String branchName;
        private String nextDocumentStatus;
        private List<RouteNodeConfigurationParameter.Builder> configurationParameters;
        private List<String> previousNodeIds;
        private List<String> nextNodeIds;
        private Long versionNumber;

        private Builder(String name, String type) {
            setName(name);
            setType(type);
            setConfigurationParameters(new ArrayList<RouteNodeConfigurationParameter.Builder>());
            setPreviousNodeIds(new ArrayList<String>());
            setNextNodeIds(new ArrayList<String>());
        }

        public static Builder create(String name, String type) {
            return new Builder(name, type);
        }

        public static Builder create(RouteNodeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getName(), contract.getType());
            builder.setId(contract.getId());
            builder.setDocumentTypeId(contract.getDocumentTypeId());
            builder.setName(contract.getName());
            builder.setRouteMethodName(contract.getRouteMethodName());
            builder.setRouteMethodCode(contract.getRouteMethodCode());
            builder.setFinalApproval(contract.isFinalApproval());
            builder.setMandatory(contract.isMandatory());
            builder.setActivationType(contract.getActivationType());
            builder.setExceptionGroupId(contract.getExceptionGroupId());
            builder.setType(contract.getType());
            builder.setBranchName(contract.getBranchName());
            builder.setNextDocumentStatus(contract.getNextDocumentStatus());
            List<RouteNodeConfigurationParameter.Builder> parameterBuilders = new ArrayList<RouteNodeConfigurationParameter.Builder>();
            for (RouteNodeConfigurationParameterContract configurationParameter : contract.getConfigurationParameters()) {
                parameterBuilders.add(RouteNodeConfigurationParameter.Builder.create(configurationParameter));
            }
            builder.setConfigurationParameters(parameterBuilders);
            builder.setPreviousNodeIds(contract.getPreviousNodeIds());
            builder.setNextNodeIds(contract.getNextNodeIds());
            builder.setVersionNumber(contract.getVersionNumber());
            
            return builder;
        }

        public RouteNode build() {
            return new RouteNode(this);
        }
        
        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getDocumentTypeId() {
            return this.documentTypeId;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getRouteMethodName() {
            return this.routeMethodName;
        }

        @Override
        public String getRouteMethodCode() {
            return this.routeMethodCode;
        }

        @Override
        public boolean isFinalApproval() {
            return this.finalApproval;
        }

        @Override
        public boolean isMandatory() {
            return this.mandatory;
        }

        @Override
        public String getActivationType() {
            return this.activationType;
        }

        @Override
        public String getExceptionGroupId() {
            return this.exceptionGroupId;
        }

        @Override
        public String getType() {
            return this.type;
        }

        @Override
        public String getBranchName() {
            return this.branchName;
        }

        @Override
        public String getNextDocumentStatus() {
            return this.nextDocumentStatus;
        }

        @Override
        public List<RouteNodeConfigurationParameter.Builder> getConfigurationParameters() {
            return this.configurationParameters;
        }

        @Override
        public List<String> getPreviousNodeIds() {
            return this.previousNodeIds;
        }

        @Override
        public List<String> getNextNodeIds() {
            return this.nextNodeIds;
        }
        
        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }
        
        public void setId(String id) {
            this.id = id;
        }

        public void setDocumentTypeId(String documentTypeId) {
            this.documentTypeId = documentTypeId;
        }
        
        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name was null or blank");
            }
            this.name = name;
        }

        public void setRouteMethodName(String routeMethodName) {
            this.routeMethodName = routeMethodName;
        }

        public void setRouteMethodCode(String routeMethodCode) {
            this.routeMethodCode = routeMethodCode;
        }

        public void setFinalApproval(boolean finalApproval) {
            this.finalApproval = finalApproval;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
        }

        public void setActivationType(String activationType) {
            this.activationType = activationType;
        }

        public void setExceptionGroupId(String exceptionGroupId) {
            this.exceptionGroupId = exceptionGroupId;
        }
        
        public void setType(String type) {
            if (StringUtils.isBlank(type)) {
                throw new IllegalArgumentException("type was null or blank");
            }
            this.type = type;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }
        
        public void setNextDocumentStatus(String nextDocumentStatus) {
            this.nextDocumentStatus = nextDocumentStatus;
        }
        
        public void setConfigurationParameters(List<RouteNodeConfigurationParameter.Builder> configurationParameters) {
            if (configurationParameters == null) {
                throw new IllegalArgumentException("configurationParameters was null");
            }
            this.configurationParameters = configurationParameters;
        }

        public void setPreviousNodeIds(List<String> previousNodeIds) {
            if (previousNodeIds == null) {
                throw new IllegalArgumentException("previousNodeIds was null");
            }
            this.previousNodeIds = previousNodeIds;
        }

        public void setNextNodeIds(List<String> nextNodeIds) {
            if (nextNodeIds == null) {
                throw new IllegalArgumentException("nextNodeIds was null");
            }
            this.nextNodeIds = nextNodeIds;
        }
        
        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "routeNode";
        final static String TYPE_NAME = "RouteNodeType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this
     * object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String DOCUMENT_TYPE_ID = "documentTypeId";
        final static String NAME = "name";
        final static String ROUTE_METHOD_NAME = "routeMethodName";
        final static String ROUTE_METHOD_CODE = "routeMethodCode";
        final static String FINAL_APPROVAL = "finalApproval";
        final static String MANDATORY = "mandatory";
        final static String ACTIVATION_TYPE = "activationType";
        final static String EXCEPTION_GROUP_ID = "exceptionGroupId";
        final static String TYPE = "type";
        final static String BRANCH_NAME = "branchName";
        final static String NEXT_DOCUMENT_STATUS = "nextDocumentStatus";
        final static String CONFIGURATION_PARAMETERS = "configurationParameters";
        final static String CONFIGURATION_PARAMETER = "configurationParameter";
        final static String PREVIOUS_NODE_IDS = "previousNodeIds";
        final static String PREVIOUS_NODE_ID = "previousNodeId";
        final static String NEXT_NODE_IDS = "nextNodeIds";
        final static String NEXT_NODE_ID = "nextNodeId";
    }

}
