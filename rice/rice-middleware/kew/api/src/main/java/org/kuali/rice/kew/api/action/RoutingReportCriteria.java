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
package org.kuali.rice.kew.api.action;

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

@XmlRootElement(name = RoutingReportCriteria.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RoutingReportCriteria.Constants.TYPE_NAME, propOrder = {
        RoutingReportCriteria.Elements.DOCUMENT_ID,
        RoutingReportCriteria.Elements.TARGET_NODE_NAME,
        RoutingReportCriteria.Elements.TARGET_PRINCIPAL_IDS,
        RoutingReportCriteria.Elements.ROUTING_PRINCIPAL_ID,
        RoutingReportCriteria.Elements.DOCUMENT_TYPE_NAME,
        RoutingReportCriteria.Elements.XML_CONTENT,
        RoutingReportCriteria.Elements.RULE_TEMPLATE_NAMES,
        RoutingReportCriteria.Elements.NODE_NAMES,
        RoutingReportCriteria.Elements.ACTIONS_TO_TAKE,
        RoutingReportCriteria.Elements.ACTIVATE_REQUESTS,
        RoutingReportCriteria.Elements.FLATTEN_NODES,
        CoreConstants.CommonElements.FUTURE_ELEMENTS})
public final class RoutingReportCriteria extends AbstractDataTransferObject implements RoutingReportCriteriaContract {

    @XmlElement(name = Elements.DOCUMENT_ID, required = false)
    private final String documentId;

    @XmlElement(name = Elements.TARGET_NODE_NAME, required = false)
    private final String targetNodeName;

    @XmlElementWrapper(name = Elements.TARGET_PRINCIPAL_IDS, required = false)
    @XmlElement(name = Elements.TARGET_PRINCIPAL_ID, required = false)
    private final List<String> targetPrincipalIds;

    @XmlElement(name = Elements.ROUTING_PRINCIPAL_ID, required = false)
    private final String routingPrincipalId;

    @XmlElement(name = Elements.DOCUMENT_TYPE_NAME, required = false)
    private final String documentTypeName;

    @XmlElement(name = Elements.XML_CONTENT, required = false)
    private final String xmlContent;

    @XmlElementWrapper(name = Elements.RULE_TEMPLATE_NAMES, required = false)
    @XmlElement(name = Elements.RULE_TEMPLATE_NAME, required = false)
    private final List<String> ruleTemplateNames;

    @XmlElementWrapper(name = Elements.NODE_NAMES, required = false)
    @XmlElement(name = Elements.NODE_NAME, required = false)
    private final List<String> nodeNames;

    @XmlElementWrapper(name = Elements.ACTIONS_TO_TAKE, required = false)
    @XmlElement(name = Elements.ACTION_TO_TAKE, required = false)
    private final List<RoutingReportActionToTake> actionsToTake;

    @XmlElement(name = Elements.ACTIVATE_REQUESTS, required = false)
    private final boolean activateRequests;

    @XmlElement(name = Elements.FLATTEN_NODES, required = false)
    private final boolean flattenNodes;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private RoutingReportCriteria() {
        this.documentId = null;
        this.targetNodeName = null;
        this.targetPrincipalIds = null;
        this.routingPrincipalId = null;
        this.documentTypeName = null;
        this.xmlContent = null;
        this.ruleTemplateNames = null;
        this.nodeNames = null;
        this.actionsToTake = null;
        this.flattenNodes = false;
        this.activateRequests = false;
    }

    private RoutingReportCriteria(Builder builder) {
        this.documentId = builder.getDocumentId();
        this.targetNodeName = builder.getTargetNodeName();
        this.targetPrincipalIds = builder.getTargetPrincipalIds();
        this.routingPrincipalId = builder.getRoutingPrincipalId();
        this.documentTypeName = builder.getDocumentTypeName();
        this.xmlContent = builder.getXmlContent();
        this.ruleTemplateNames = builder.getRuleTemplateNames();
        this.nodeNames = builder.getNodeNames();
        if (builder.getActionsToTake() != null) {
            List<RoutingReportActionToTake> actions = new ArrayList<RoutingReportActionToTake>();
            for (RoutingReportActionToTake.Builder actionBuilder : builder.getActionsToTake()) {
                actions.add(actionBuilder.build());
            }
            this.actionsToTake = actions;
        } else {
            this.actionsToTake = Collections.emptyList();
        }
        this.flattenNodes = builder.isFlattenNodes();
        this.activateRequests = builder.isActivateRequests();
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public String getTargetNodeName() {
        return this.targetNodeName;
    }

    @Override
    public List<String> getTargetPrincipalIds() {
        return this.targetPrincipalIds;
    }

    @Override
    public String getRoutingPrincipalId() {
        return this.routingPrincipalId;
    }

    @Override
    public String getDocumentTypeName() {
        return this.documentTypeName;
    }

    @Override
    public String getXmlContent() {
        return this.xmlContent;
    }

    @Override
    public List<String> getRuleTemplateNames() {
        return this.ruleTemplateNames;
    }

    @Override
    public List<String> getNodeNames() {
        return this.nodeNames;
    }

    @Override
    public List<RoutingReportActionToTake> getActionsToTake() {
        return this.actionsToTake;
    }

    @Override
    public boolean isActivateRequests() {
        return this.activateRequests;
    }

    @Override
    public boolean isFlattenNodes() {
        return this.flattenNodes;
    }

    /**
     * A builder which can be used to construct {@link RoutingReportCriteria} instances.  Enforces the constraints of
     * the {@link RoutingReportCriteriaContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, RoutingReportCriteriaContract {

        private String documentId;
        private String targetNodeName;
        private List<String> targetPrincipalIds;
        private String routingPrincipalId;
        private String documentTypeName;
        private String xmlContent;
        private List<String> ruleTemplateNames;
        private List<String> nodeNames;
        private List<RoutingReportActionToTake.Builder> actionsToTake;
        private boolean activateRequests;
        private boolean flattenNodes;

        private Builder(String documentId, String targetNodeName) {
            this.setDocumentId(documentId);
            this.setTargetNodeName(targetNodeName);
        }

        private Builder(String documentTypeName) {
            this.setDocumentTypeName(documentTypeName);
        }

        public static Builder createByDocumentTypeName(String documentTypeName) {
            return new Builder(documentTypeName);
        }

        public static Builder createByDocumentId(String documentId) {
            return new Builder(documentId, null);
        }

        public static Builder createByDocumentIdAndTargetNodeName(String documentId, String targetNodeName) {
            return new Builder(documentId, targetNodeName);
        }

        public static Builder create(RoutingReportCriteriaContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder =
                    createByDocumentIdAndTargetNodeName(contract.getDocumentId(), contract.getTargetNodeName());
            builder.setDocumentId(contract.getDocumentId());
            builder.setTargetNodeName(contract.getTargetNodeName());
            builder.setTargetPrincipalIds(contract.getTargetPrincipalIds());
            builder.setRoutingPrincipalId(contract.getRoutingPrincipalId());
            builder.setDocumentTypeName(contract.getDocumentTypeName());
            builder.setXmlContent(contract.getXmlContent());
            builder.setRuleTemplateNames(contract.getRuleTemplateNames());
            builder.setNodeNames(contract.getNodeNames());
            if (contract.getActionsToTake() != null) {
                List<RoutingReportActionToTake.Builder> actionsToTake = new ArrayList<RoutingReportActionToTake.Builder>();
                for (RoutingReportActionToTakeContract action : contract.getActionsToTake()) {
                    actionsToTake.add(RoutingReportActionToTake.Builder.create(action));
                }
                builder.setActionsToTake(actionsToTake);
            }
            builder.setActivateRequests(contract.isActivateRequests());
            builder.setFlattenNodes(contract.isFlattenNodes());

            return builder;
        }

        public RoutingReportCriteria build() {
            return new RoutingReportCriteria(this);
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public String getTargetNodeName() {
            return this.targetNodeName;
        }

        @Override
        public List<String> getTargetPrincipalIds() {
            return this.targetPrincipalIds;
        }

        @Override
        public String getRoutingPrincipalId() {
            return this.routingPrincipalId;
        }

        @Override
        public String getDocumentTypeName() {
            return this.documentTypeName;
        }

        @Override
        public String getXmlContent() {
            return this.xmlContent;
        }

        @Override
        public List<String> getRuleTemplateNames() {
            return this.ruleTemplateNames;
        }

        @Override
        public List<String> getNodeNames() {
            return this.nodeNames;
        }

        @Override
        public List<RoutingReportActionToTake.Builder> getActionsToTake() {
            return this.actionsToTake;
        }

        @Override
        public boolean isActivateRequests() {
            return this.activateRequests;
        }

        @Override
        public boolean isFlattenNodes() {
            return this.flattenNodes;
        }

        public void setDocumentId(String documentId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.documentId = documentId;
        }

        public void setTargetNodeName(String targetNodeName) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.targetNodeName = targetNodeName;
        }

        public void setTargetPrincipalIds(List<String> targetPrincipalIds) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.targetPrincipalIds = Collections.unmodifiableList(targetPrincipalIds);
        }

        public void setRoutingPrincipalId(String routingPrincipalId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.routingPrincipalId = routingPrincipalId;
        }

        public void setDocumentTypeName(String documentTypeName) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.documentTypeName = documentTypeName;
        }

        public void setXmlContent(String xmlContent) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.xmlContent = xmlContent;
        }

        public void setRuleTemplateNames(List<String> ruleTemplateNames) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.ruleTemplateNames = Collections.unmodifiableList(ruleTemplateNames);
        }

        public void setNodeNames(List<String> nodeNames) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.nodeNames = Collections.unmodifiableList(nodeNames);
        }

        public void setActionsToTake(List<RoutingReportActionToTake.Builder> actionsToTake) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.actionsToTake = Collections.unmodifiableList(actionsToTake);
        }

        public void setActivateRequests(boolean activateRequests) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.activateRequests = activateRequests;
        }

        public void setFlattenNodes(boolean flattenNodes) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.flattenNodes = flattenNodes;
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "routingReportCriteria";
        final static String TYPE_NAME = "RoutingReportCriteriaType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled
     * to XML.
     */
    static class Elements {

        final static String DOCUMENT_ID = "documentId";
        final static String TARGET_NODE_NAME = "targetNodeName";
        final static String TARGET_PRINCIPAL_IDS = "targetPrincipalIds";
        final static String TARGET_PRINCIPAL_ID = "targetPrincipalId";
        final static String ROUTING_PRINCIPAL_ID = "routingPrincipalId";
        final static String DOCUMENT_TYPE_NAME = "documentTypeName";
        final static String XML_CONTENT = "xmlContent";
        final static String RULE_TEMPLATE_NAMES = "ruleTemplateNames";
        final static String RULE_TEMPLATE_NAME = "ruleTemplateName";
        final static String NODE_NAMES = "nodeNames";
        final static String NODE_NAME = "nodeName";
        final static String ACTIONS_TO_TAKE = "actionsToTake";
        final static String ACTION_TO_TAKE = "actionToTake";
        final static String ACTIVATE_REQUESTS = "activateRequests";
        final static String FLATTEN_NODES = "flattenNodes";
    }
}
