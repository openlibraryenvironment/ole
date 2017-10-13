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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = ActionRequest.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ActionRequest.Constants.TYPE_NAME, propOrder = {
		ActionRequest.Elements.ID,
	    ActionRequest.Elements.ACTION_REQUESTED_CODE,
	    ActionRequest.Elements.STATUS_CODE,
	    ActionRequest.Elements.CURRENT,
	    ActionRequest.Elements.DATE_CREATED,
	    ActionRequest.Elements.RESPONSIBILITY_ID,
	    ActionRequest.Elements.DOCUMENT_ID,
		ActionRequest.Elements.PRIORITY,
        ActionRequest.Elements.ROUTE_LEVEL,
		ActionRequest.Elements.ANNOTATION,
		ActionRequest.Elements.RECIPIENT_TYPE_CODE,
		ActionRequest.Elements.PRINCIPAL_ID,
		ActionRequest.Elements.GROUP_ID,
		ActionRequest.Elements.REQUEST_POLICY_CODE,
		ActionRequest.Elements.RESPONSIBILITY_DESCRIPTION,
		ActionRequest.Elements.FORCE_ACTION,
		ActionRequest.Elements.DELEGATION_TYPE_CODE,
		ActionRequest.Elements.ROLE_NAME,
		ActionRequest.Elements.QUALIFIED_ROLE_NAME,
		ActionRequest.Elements.QUALIFIED_ROLE_NAME_LABEL,
		ActionRequest.Elements.ROUTE_NODE_INSTANCE_ID,
		ActionRequest.Elements.NODE_NAME,
		ActionRequest.Elements.REQUEST_LABEL,
		ActionRequest.Elements.PARENT_ACTION_REQUEST_ID,
		ActionRequest.Elements.ACTION_TAKEN,
		ActionRequest.Elements.CHILD_REQUESTS,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ActionRequest extends AbstractDataTransferObject implements ActionRequestContract {

	private static final long serialVersionUID = -7581089059447119201L;

	@XmlElement(name = Elements.ID, required = true)
    private final String id;
    
    @XmlElement(name = Elements.ACTION_REQUESTED_CODE, required = true)
    private final String actionRequestedCode;
    
    @XmlElement(name = Elements.STATUS_CODE, required = true)
    private final String statusCode;
    
    @XmlElement(name = Elements.CURRENT, required = true)
    private final boolean current;
    
    @XmlElement(name = Elements.DATE_CREATED, required = true)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateCreated;
    
    @XmlElement(name = Elements.RESPONSIBILITY_ID, required = true)
    private final String responsibilityId;
    
    @XmlElement(name = Elements.DOCUMENT_ID, required = true)
    private final String documentId;
        
    @XmlElement(name = Elements.PRIORITY, required = true)
    private final int priority;

    @XmlElement(name = Elements.ROUTE_LEVEL, required = true)
    private final int routeLevel;

    @XmlElement(name = Elements.ANNOTATION, required = false)
    private final String annotation;
    
    @XmlElement(name = Elements.RECIPIENT_TYPE_CODE, required = true)
    private final String recipientTypeCode;
    
    @XmlElement(name = Elements.PRINCIPAL_ID, required = false)
    private final String principalId;
    
    @XmlElement(name = Elements.GROUP_ID, required = false)
    private final String groupId;
    
    @XmlElement(name = Elements.REQUEST_POLICY_CODE, required = false)
    private final String requestPolicyCode;
    
    @XmlElement(name = Elements.RESPONSIBILITY_DESCRIPTION, required = false)
    private final String responsibilityDescription;
    
    @XmlElement(name = Elements.FORCE_ACTION, required = true)
    private final boolean forceAction;
    
    @XmlElement(name = Elements.DELEGATION_TYPE_CODE, required = false)
    private final String delegationTypeCode;
    
    @XmlElement(name = Elements.ROLE_NAME, required = false)
    private final String roleName;
    
    @XmlElement(name = Elements.QUALIFIED_ROLE_NAME, required = false)
    private final String qualifiedRoleName;
    
    @XmlElement(name = Elements.QUALIFIED_ROLE_NAME_LABEL, required = false)
    private final String qualifiedRoleNameLabel;
    
    @XmlElement(name = Elements.ROUTE_NODE_INSTANCE_ID, required = false)
    private final String routeNodeInstanceId;
    
    @XmlElement(name = Elements.NODE_NAME, required = false)
    private final String nodeName;
    
    @XmlElement(name = Elements.REQUEST_LABEL, required = false)
    private final String requestLabel;
    
    @XmlElement(name = Elements.PARENT_ACTION_REQUEST_ID, required = false)
    private final String parentActionRequestId;
    
    @XmlElement(name = Elements.ACTION_TAKEN, required = false)
    private final ActionTaken actionTaken;
    
    @XmlElementWrapper(name = Elements.CHILD_REQUESTS, required = false)
    @XmlElement(name = Elements.CHILD_REQUEST, required = false)
    private final List<ActionRequest> childRequests;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private ActionRequest() {
        this.id = null;
        this.actionRequestedCode = null;
        this.statusCode = null;
        this.current = false;
        this.dateCreated = null;
        this.responsibilityId = null;
        this.documentId = null;
        this.priority = 0;
        this.routeLevel = 0;
    	this.annotation = null;
        this.recipientTypeCode = null;
        this.principalId = null;
        this.groupId = null;
        this.requestPolicyCode = null;
        this.responsibilityDescription = null;
        this.forceAction = false;
        this.delegationTypeCode = null;
        this.roleName = null;
        this.qualifiedRoleName = null;
        this.qualifiedRoleNameLabel = null;
        this.routeNodeInstanceId = null;
        this.nodeName = null;
        this.requestLabel = null;
        this.parentActionRequestId = null;
        this.actionTaken = null;
        this.childRequests = null;
    }

    private ActionRequest(Builder builder) {
        this.id = builder.getId();
        this.actionRequestedCode = builder.getActionRequested().getCode();
        this.statusCode = builder.getStatus().getCode();
        this.current = builder.isCurrent();
        this.dateCreated = builder.getDateCreated();
        this.responsibilityId = builder.getResponsibilityId();
        this.documentId = builder.getDocumentId();
        this.priority = builder.getPriority();
        this.routeLevel = builder.getRouteLevel();
    	this.annotation = builder.getAnnotation();
        this.recipientTypeCode = builder.getRecipientType().getCode();
        this.principalId = builder.getPrincipalId();
        this.groupId = builder.getGroupId();
        if (builder.getRequestPolicy() == null) {
        	this.requestPolicyCode = null;
        } else {
        	this.requestPolicyCode = builder.getRequestPolicy().getCode();
        }
        this.responsibilityDescription = builder.getResponsibilityDescription();
        this.forceAction = builder.isForceAction();
        if (builder.getDelegationType() == null) {
        	this.delegationTypeCode = null;
        } else {
        	this.delegationTypeCode = builder.getDelegationType().getCode();
        }
        this.roleName = builder.getRoleName();
        this.qualifiedRoleName = builder.getQualifiedRoleName();
        this.qualifiedRoleNameLabel = builder.getQualifiedRoleNameLabel();
        this.routeNodeInstanceId = builder.getRouteNodeInstanceId();
        this.nodeName = builder.getNodeName();
        this.requestLabel = builder.getRequestLabel();
        this.parentActionRequestId = builder.getParentActionRequestId();
        ActionTaken.Builder actionTakenBuilder = builder.getActionTaken();
        if (actionTakenBuilder == null) {
        	this.actionTaken = null;
        } else {
        	this.actionTaken = actionTakenBuilder.build();
        }
        this.childRequests = new ArrayList<ActionRequest>();
        List<ActionRequest.Builder> childRequestBuilders = builder.getChildRequests();
        if (childRequestBuilders != null) {
        	for (ActionRequest.Builder childRequestBuilder : childRequestBuilders) {
        		this.childRequests.add(childRequestBuilder.build());
        	}
        }
    }

    @Override
    public String getAnnotation() {
        return this.annotation;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public int getRouteLevel() {
        return this.routeLevel;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public ActionRequestType getActionRequested() {
        return ActionRequestType.fromCode(this.actionRequestedCode);
    }

    @Override
    public ActionRequestStatus getStatus() {
        return ActionRequestStatus.fromCode(this.statusCode);
    }

    @Override
    public boolean isCurrent() {
        return this.current;
    }

    @Override
    public DateTime getDateCreated() {
        return this.dateCreated;
    }

    @Override
    public String getResponsibilityId() {
        return this.responsibilityId;
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public RecipientType getRecipientType() {
        return RecipientType.fromCode(this.recipientTypeCode);
    }

    @Override
    public String getPrincipalId() {
        return this.principalId;
    }

    @Override
    public String getGroupId() {
        return this.groupId;
    }

    @Override
    public ActionRequestPolicy getRequestPolicy() {
    	if (this.requestPolicyCode == null) {
    		return null;
    	}
        return ActionRequestPolicy.fromCode(this.requestPolicyCode);
    }

    @Override
    public String getResponsibilityDescription() {
        return this.responsibilityDescription;
    }

    @Override
    public boolean isForceAction() {
        return this.forceAction;
    }

    @Override
    public DelegationType getDelegationType() {
    	if (this.delegationTypeCode == null) {
    		return null;
    	}
        return DelegationType.fromCode(this.delegationTypeCode);
    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public String getQualifiedRoleName() {
        return this.qualifiedRoleName;
    }

    @Override
    public String getQualifiedRoleNameLabel() {
        return this.qualifiedRoleNameLabel;
    }

    @Override
    public String getRouteNodeInstanceId() {
        return this.routeNodeInstanceId;
    }
    
    @Override
    public String getNodeName() {
        return this.nodeName;
    }

    @Override
    public String getRequestLabel() {
        return this.requestLabel;
    }

    @Override
    public String getParentActionRequestId() {
        return this.parentActionRequestId;
    }

    @Override
    public ActionTaken getActionTaken() {
        return this.actionTaken;
    }
    
    @Override
    public List<ActionRequest> getChildRequests() {
    	if (this.childRequests == null) {
    		return Collections.emptyList();
    	} else {
    		return Collections.unmodifiableList(this.childRequests);
    	}
    }

    public boolean isAdHocRequest() {
    	return KewApiConstants.ADHOC_REQUEST_RESPONSIBILITY_ID.equals(getResponsibilityId());
    }

    public boolean isGeneratedRequest() {
    	return KewApiConstants.MACHINE_GENERATED_RESPONSIBILITY_ID.equals(getResponsibilityId());
    }

    public boolean isExceptionRequest() {
    	return KewApiConstants.EXCEPTION_REQUEST_RESPONSIBILITY_ID.equals(getResponsibilityId());
    }

    public boolean isRouteModuleRequest() {
    	return getResponsibilityId() != null && Long.parseLong(getResponsibilityId()) > 0;
    }
    
    public boolean isNotificationRequest() {
        return isAcknowledgeRequest() || isFyiRequest();
    }

    public boolean isApprovalRequest() {
        return ActionRequestType.APPROVE == getActionRequested() || ActionRequestType.COMPLETE == getActionRequested();
    }

    public boolean isAcknowledgeRequest() {
        return ActionRequestType.ACKNOWLEDGE == getActionRequested();
    }

    public boolean isFyiRequest() {
        return ActionRequestType.FYI == getActionRequested();
    }

    public boolean isPending() {
        return isInitialized() || isActivated();
    }

    public boolean isCompleteRequest() {
        return ActionRequestType.COMPLETE == getActionRequested();
    }

    public boolean isInitialized() {
        return ActionRequestStatus.INITIALIZED == getStatus();
    }

    public boolean isActivated() {
        return ActionRequestStatus.ACTIVATED == getStatus();
    }

    public boolean isDone() {
        return ActionRequestStatus.DONE == getStatus();
    }

    public boolean isUserRequest() {
        return RecipientType.PRINCIPAL == getRecipientType();
    }

    public boolean isGroupRequest() {
    	return RecipientType.GROUP == getRecipientType();
    }

    public boolean isRoleRequest() {
    	return RecipientType.ROLE == getRecipientType();
    }
    
    public List<ActionRequest> flatten() {
    	List<ActionRequest> flattenedRequests = new ArrayList<ActionRequest>();
    	flattenedRequests.add(this);
    	for (ActionRequest childRequest : getChildRequests()) {
    		flattenedRequests.addAll(childRequest.flatten());
    	}
    	return Collections.unmodifiableList(flattenedRequests);
    }

    /**
     * A builder which can be used to construct {@link ActionRequest} instances.  Enforces the constraints of the {@link ActionRequestContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, ActionRequestContract {

		private static final long serialVersionUID = -1354211147778354091L;

		private String id;
        private ActionRequestType actionRequested;
        private ActionRequestStatus status;
        private boolean current;
        private DateTime dateCreated;
        private String responsibilityId;
        private String documentId;
        private int priority;
        private int routeLevel;
        private String annotation;
        private RecipientType recipientType;
        private String principalId;
        private String groupId;
        private ActionRequestPolicy requestPolicy;
        private String responsibilityDescription;
        private boolean forceAction;
        private DelegationType delegationType;
        private String roleName;
        private String qualifiedRoleName;
        private String qualifiedRoleNameLabel;
        private String routeNodeInstanceId;
        private String nodeName;
        private String requestLabel;
        private String parentActionRequestId;
        private ActionTaken.Builder actionTaken;
        private List<ActionRequest.Builder> childRequests;

        private Builder(String id, ActionRequestType actionRequested, ActionRequestStatus status, String responsibilityId, String documentId, RecipientType recipientType) {
            setId(id);
            setActionRequested(actionRequested);
            setStatus(status);
            setResponsibilityId(responsibilityId);
            setDocumentId(documentId);
            setRecipientType(recipientType);
            setCurrent(true);
            setDateCreated(new DateTime());
        }

        public static Builder create(String id, ActionRequestType actionRequested, ActionRequestStatus status, String responsibilityId, String documentId, RecipientType recipientType) {
            return new Builder(id, actionRequested, status, responsibilityId, documentId, recipientType);
        }

        public static Builder create(ActionRequestContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create(contract.getId(), contract.getActionRequested(), contract.getStatus(), contract.getResponsibilityId(), contract.getDocumentId(), contract.getRecipientType());
            builder.setCurrent(contract.isCurrent());
            builder.setDateCreated(contract.getDateCreated());
            builder.setPriority(contract.getPriority());
            builder.setRouteLevel(contract.getRouteLevel());
            builder.setAnnotation(contract.getAnnotation());
            builder.setPrincipalId(contract.getPrincipalId());
            builder.setGroupId(contract.getGroupId());
            builder.setRequestPolicy(contract.getRequestPolicy());
            builder.setResponsibilityDescription(contract.getResponsibilityDescription());
            builder.setForceAction(contract.isForceAction());
            builder.setDelegationType(contract.getDelegationType());
            builder.setRoleName(contract.getRoleName());
            builder.setQualifiedRoleName(contract.getQualifiedRoleName());
            builder.setQualifiedRoleNameLabel(contract.getQualifiedRoleNameLabel());
            builder.setNodeName(contract.getNodeName());
            builder.setRequestLabel(contract.getRequestLabel());
            builder.setParentActionRequestId(contract.getParentActionRequestId());
            if (contract.getActionTaken() != null) {
            	builder.setActionTaken(ActionTaken.Builder.create(contract.getActionTaken()));
            }
            List<ActionRequest.Builder> actionRequestBuilders = new ArrayList<ActionRequest.Builder>();
            for (ActionRequestContract actionRequest : contract.getChildRequests()) {
            	actionRequestBuilders.add(ActionRequest.Builder.create(actionRequest));
            }
            builder.setChildRequests(actionRequestBuilders);
            return builder;
        }

        public ActionRequest build() {
            return new ActionRequest(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public ActionRequestType getActionRequested() {
            return this.actionRequested;
        }

        @Override
        public ActionRequestStatus getStatus() {
            return this.status;
        }

        @Override
        public boolean isCurrent() {
            return this.current;
        }

        @Override
        public DateTime getDateCreated() {
            return this.dateCreated;
        }

        @Override
        public String getResponsibilityId() {
            return this.responsibilityId;
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public int getPriority() {
            return this.priority;
        }

        @Override
        public int getRouteLevel() {
            return this.routeLevel;
        }

        @Override
        public String getAnnotation() {
            return this.annotation;
        }
        
        @Override
        public RecipientType getRecipientType() {
            return this.recipientType;
        }

        @Override
        public String getPrincipalId() {
            return this.principalId;
        }

        @Override
        public String getGroupId() {
            return this.groupId;
        }

        @Override
        public ActionRequestPolicy getRequestPolicy() {
            return this.requestPolicy;
        }

        @Override
        public String getResponsibilityDescription() {
            return this.responsibilityDescription;
        }

        @Override
        public boolean isForceAction() {
            return this.forceAction;
        }

        @Override
        public DelegationType getDelegationType() {
            return this.delegationType;
        }

        @Override
        public String getRoleName() {
            return this.roleName;
        }

        @Override
        public String getQualifiedRoleName() {
            return this.qualifiedRoleName;
        }

        @Override
        public String getQualifiedRoleNameLabel() {
            return this.qualifiedRoleNameLabel;
        }

        @Override
        public String getRouteNodeInstanceId() {
        	return this.routeNodeInstanceId;
        }
        
        @Override
        public String getNodeName() {
            return this.nodeName;
        }

        @Override
        public String getRequestLabel() {
            return this.requestLabel;
        }

        @Override
        public String getParentActionRequestId() {
            return this.parentActionRequestId;
        }

        @Override
        public ActionTaken.Builder getActionTaken() {
            return this.actionTaken;
        }

        @Override
        public List<ActionRequest.Builder> getChildRequests() {
            return this.childRequests;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
            	throw new IllegalArgumentException("id was null or blank");
            }
            this.id = id;
        }

        public void setActionRequested(ActionRequestType actionRequested) {
            if (actionRequested == null) {
            	throw new IllegalArgumentException("actionRequested was null");
            }
            this.actionRequested = actionRequested;
        }

        public void setStatus(ActionRequestStatus status) {
            if (status == null) {
            	throw new IllegalArgumentException("status was null");
            }
            this.status = status;
        }

        public void setCurrent(boolean current) {
            this.current = current;
        }

        public void setDateCreated(DateTime dateCreated) {
        	if (dateCreated == null) {
        		throw new IllegalArgumentException("dateCreated was null");
        	}
            this.dateCreated = dateCreated;
        }

        public void setResponsibilityId(String responsibilityId) {
        	if (StringUtils.isBlank(responsibilityId)) {
        		throw new IllegalArgumentException("responsibilityId was null or blank");
        	}
            this.responsibilityId = responsibilityId;
        }

        public void setDocumentId(String documentId) {
            if (StringUtils.isBlank(documentId)) {
            	throw new IllegalArgumentException("documentId was null or blank");
            }
            this.documentId = documentId;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public void setRouteLevel(int routeLevel) {
            this.routeLevel = routeLevel;
        }

        public void setAnnotation(String annotation) {
            this.annotation = annotation;
        }

        public void setRecipientType(RecipientType recipientType) {
        	if (recipientType == null) {
        		throw new IllegalArgumentException("recipientType was null");
        	}
            this.recipientType = recipientType;
        }

        public void setPrincipalId(String principalId) {
            this.principalId = principalId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setRequestPolicy(ActionRequestPolicy requestPolicy) {
            this.requestPolicy = requestPolicy;
        }

        public void setResponsibilityDescription(String responsibilityDescription) {
            this.responsibilityDescription = responsibilityDescription;
        }

        public void setForceAction(boolean forceAction) {
            this.forceAction = forceAction;
        }

        public void setDelegationType(DelegationType delegationType) {
            this.delegationType = delegationType;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public void setQualifiedRoleName(String qualifiedRoleName) {
            this.qualifiedRoleName = qualifiedRoleName;
        }

        public void setQualifiedRoleNameLabel(String qualifiedRoleNameLabel) {
            this.qualifiedRoleNameLabel = qualifiedRoleNameLabel;
        }
        
        public void setRouteNodeInstanceId(String routeNodeInstanceId) {
        	this.routeNodeInstanceId = routeNodeInstanceId;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public void setRequestLabel(String requestLabel) {
            this.requestLabel = requestLabel;
        }

        public void setParentActionRequestId(String parentActionRequestId) {
            this.parentActionRequestId = parentActionRequestId;
        }

        public void setActionTaken(ActionTaken.Builder actionTaken) {
            this.actionTaken = actionTaken;
        }

        public void setChildRequests(List<ActionRequest.Builder> childRequests) {
            this.childRequests = childRequests;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "actionRequest";
        final static String TYPE_NAME = "ActionRequestType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {
        final static String ANNOTATION = "annotation";
        final static String PRIORITY = "priority";
        final static String ID = "id";
        final static String ACTION_REQUESTED_CODE = "actionRequestedCode";
        final static String STATUS_CODE = "statusCode";
        final static String CURRENT = "current";
        final static String ROUTE_LEVEL = "routeLevel";
        final static String DATE_CREATED = "dateCreated";
        final static String RESPONSIBILITY_ID = "responsibilityId";
        final static String DOCUMENT_ID = "documentId";
        final static String RECIPIENT_TYPE_CODE = "recipientTypeCode";
        final static String PRINCIPAL_ID = "principalId";
        final static String GROUP_ID = "groupId";
        final static String REQUEST_POLICY_CODE = "requestPolicyCode";
        final static String RESPONSIBILITY_DESCRIPTION = "responsibilityDescription";
        final static String FORCE_ACTION = "forceAction";
        final static String DELEGATION_TYPE_CODE = "delegationTypeCode";
        final static String ROLE_NAME = "roleName";
        final static String QUALIFIED_ROLE_NAME = "qualifiedRoleName";
        final static String QUALIFIED_ROLE_NAME_LABEL = "qualifiedRoleNameLabel";
        final static String ROUTE_NODE_INSTANCE_ID = "routeNodeInstanceId";
        final static String NODE_NAME = "nodeName";
        final static String REQUEST_LABEL = "requestLabel";
        final static String PARENT_ACTION_REQUEST_ID = "parentActionRequestId";
        final static String ACTION_TAKEN = "actionTaken";
        final static String CHILD_REQUESTS = "childRequests";
        final static String CHILD_REQUEST = "childRequest";
    }

}

