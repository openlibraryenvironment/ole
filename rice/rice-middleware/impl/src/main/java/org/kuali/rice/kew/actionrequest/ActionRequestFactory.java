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
package org.kuali.rice.kew.actionrequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.user.UserId;
import org.kuali.rice.kew.api.util.CodeTranslator;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.identity.service.IdentityHelperService;
import org.kuali.rice.kew.role.KimRoleRecipient;
import org.kuali.rice.kew.role.KimRoleResponsibilityRecipient;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.user.RoleRecipient;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kew.workgroup.GroupId;
import org.kuali.rice.kim.api.common.delegate.DelegateMember;
import org.kuali.rice.kim.api.common.delegate.DelegateType;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.responsibility.ResponsibilityAction;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A factory to aid in creating the ever-so-gnarly ActionRequestValue object.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionRequestFactory {

	private static final Logger LOG = Logger.getLogger(ActionRequestFactory.class);

	private static RoleService roleService;
	private static IdentityHelperService identityHelperService;
	private static IdentityService identityService;
    private static GroupService groupService;
	private static ActionRequestService actionRequestService;
	
	private DocumentRouteHeaderValue document;
	private RouteNodeInstance routeNode;
	private List<ActionRequestValue> requestGraphs = new ArrayList<ActionRequestValue>();

	public ActionRequestFactory() {
	}

	public ActionRequestFactory(DocumentRouteHeaderValue document) {
		this.document = document;
	}

	public ActionRequestFactory(DocumentRouteHeaderValue document, RouteNodeInstance routeNode) {
		this.document = document;
		this.routeNode = routeNode;
	}

    public ActionRequestFactory(RouteContext routeContext) {
        this(routeContext.getDocument(), routeContext.getNodeInstance());
    }

	/**
	 * Constructs ActionRequestValue using default priority and 0 as responsibility
	 *
	 * @param actionRequested
	 * @param recipient
	 * @param description
	 * @param forceAction
	 * @param annotation
     * @return ActionRequestValue
	 */
	public ActionRequestValue createActionRequest(String actionRequested, Recipient recipient, String description, Boolean forceAction, String annotation) {
		return createActionRequest(actionRequested, new Integer(0), recipient, description, KewApiConstants.MACHINE_GENERATED_RESPONSIBILITY_ID, forceAction, annotation);
	}

	public ActionRequestValue createActionRequest(String actionRequested, Integer priority, Recipient recipient, String description, String responsibilityId, Boolean forceAction, String annotation) {
    	return createActionRequest(actionRequested, priority, recipient, description, responsibilityId, forceAction, null, null, annotation);
    }

	public ActionRequestValue createActionRequest(String actionRequested, Integer priority, Recipient recipient, String description, String responsibilityId, Boolean forceAction, String approvePolicy, String ruleId, String annotation) {
		return createActionRequest(actionRequested, priority, recipient, description, responsibilityId, forceAction, approvePolicy, ruleId, annotation, null);
	}

	public ActionRequestValue createActionRequest(String actionRequested, Integer priority, Recipient recipient, String description, String responsibilityId, Boolean forceAction, String approvePolicy, String ruleId, String annotation, String requestLabel) {
		ActionRequestValue actionRequest = new ActionRequestValue();
        actionRequest.setActionRequested(actionRequested);
        actionRequest.setDocVersion(document.getDocVersion());
        actionRequest.setPriority(priority);
        actionRequest.setRouteHeader(document);
        actionRequest.setDocumentId(document.getDocumentId());
        actionRequest.setRouteLevel(document.getDocRouteLevel());
    	actionRequest.setNodeInstance(routeNode);
    	actionRequest.setResponsibilityId(responsibilityId);
    	actionRequest.setResponsibilityDesc(description);
    	actionRequest.setApprovePolicy(approvePolicy);
    	actionRequest.setForceAction(forceAction);
    	actionRequest.setRuleBaseValuesId(ruleId);
    	actionRequest.setAnnotation(annotation);
    	actionRequest.setRequestLabel(requestLabel);
    	setDefaultProperties(actionRequest);
    	resolveRecipient(actionRequest, recipient);

        return actionRequest;
	}

	public ActionRequestValue createBlankActionRequest() {
		ActionRequestValue request = new ActionRequestValue();
		request.setRouteHeader(document);
		if (document != null) {
			request.setDocumentId(document.getDocumentId());
		}
		request.setNodeInstance(routeNode);
		return request;
	}


    public ActionRequestValue createNotificationRequest(String actionRequestCode, PrincipalContract principal, String reasonActionCode, PrincipalContract reasonActionUser, String responsibilityDesc) {
    	ActionRequestValue request = createActionRequest(actionRequestCode, new KimPrincipalRecipient(principal), responsibilityDesc, Boolean.TRUE, null);
    	String annotation = generateNotificationAnnotation(reasonActionUser, actionRequestCode, reasonActionCode, request);
    	request.setAnnotation(annotation);
    	return request;
    }

    //unify these 2 methods if possible
    public List<ActionRequestValue> generateNotifications(List requests, PrincipalContract principal, Recipient delegator,
            String notificationRequestCode, String actionTakenCode)
    {
        String groupName =  CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE,
                KRADConstants.DetailTypes.WORKGROUP_DETAIL_TYPE,
                KewApiConstants.NOTIFICATION_EXCLUDED_USERS_WORKGROUP_NAME_IND);
        
        
        Group notifyExclusionWorkgroup = null;
        if(!StringUtils.isBlank(groupName)){
        	notifyExclusionWorkgroup = getGroupService().getGroupByNamespaceCodeAndName(
                    Utilities.parseGroupNamespaceCode(groupName), Utilities.parseGroupName(groupName));
        }
        
 
        
        return generateNotifications(null, getActionRequestService().getRootRequests(requests), principal, delegator, notificationRequestCode, actionTakenCode, notifyExclusionWorkgroup);
    }

    /**
     * Generates a notification request for each action request specified, filtering out the specified principal
     * and delegator, and exclusion workgroup members from notification list
     * @param parentRequest if non-null, attaches generated notification requests to this parent action request
     * @param requests list of ActionRequestValues for which to generate corresponding notification requests
     * @param principal principal to exclude from notifications
     * @param delegator delegator to exclude from notifications
     * @param notificationRequestCode the actionrequest code of generated notifications
     * @param actionTakenCode the actiontaken code to display as the cause of the notification generation
     * @param notifyExclusionWorkgroup workgroup whose members should not be sent notifications
     * @return a list of generated notification requests
     */
    private List<ActionRequestValue> generateNotifications(ActionRequestValue parentRequest,
            List requests, PrincipalContract principal, Recipient delegator, String notificationRequestCode,
            String actionTakenCode, Group notifyExclusionWorkgroup)
    {
        List<ActionRequestValue> notificationRequests = new ArrayList<ActionRequestValue>();
        for (Iterator iter = requests.iterator(); iter.hasNext();) 
        {
            ActionRequestValue actionRequest = (ActionRequestValue) iter.next();
            if (!(actionRequest.isRecipientRoutedRequest(principal.getPrincipalId()) || actionRequest.isRecipientRoutedRequest(delegator)))
            {
                // skip user requests to system users
                if ((notifyExclusionWorkgroup != null) &&
                        (isRecipientInGroup(notifyExclusionWorkgroup, actionRequest.getRecipient())))
                {
                    continue;
                }
                ActionRequestValue notificationRequest = createNotificationRequest(actionRequest, principal, notificationRequestCode, actionTakenCode);
                notificationRequests.add(notificationRequest);
                if (parentRequest != null)
                {
                    notificationRequest.setParentActionRequest(parentRequest);
                    parentRequest.getChildrenRequests().add(notificationRequest);
                }
                notificationRequests.addAll(generateNotifications(notificationRequest, actionRequest.getChildrenRequests(), principal, delegator, notificationRequestCode, actionTakenCode, notifyExclusionWorkgroup));
            }
        }
        return notificationRequests;
    }

    private boolean isRecipientInGroup(Group group, Recipient recipient)
    {
        boolean isMember = false;

        if(recipient instanceof KimPrincipalRecipient)
        {
            String principalId = ((KimPrincipalRecipient) recipient).getPrincipalId();
            String groupId = group.getId();
            isMember = getGroupService().isMemberOfGroup(principalId, groupId);
        }
        else if (recipient instanceof KimGroupRecipient)
        {
            String kimRecipientId = ((KimGroupRecipient) recipient).getGroup().getId();
            isMember = getGroupService().isGroupMemberOfGroup(kimRecipientId, group.getId() );
        }
        return isMember;
    }

    private ActionRequestValue createNotificationRequest(ActionRequestValue actionRequest, PrincipalContract reasonPrincipal, String notificationRequestCode, String actionTakenCode) {

    	String annotation = generateNotificationAnnotation(reasonPrincipal, notificationRequestCode, actionTakenCode, actionRequest);
        ActionRequestValue request = createActionRequest(notificationRequestCode, actionRequest.getPriority(), actionRequest.getRecipient(), actionRequest.getResponsibilityDesc(), KewApiConstants.MACHINE_GENERATED_RESPONSIBILITY_ID, Boolean.TRUE, annotation);

        request.setDocVersion(actionRequest.getDocVersion());
        request.setApprovePolicy(actionRequest.getApprovePolicy());
        request.setRoleName(actionRequest.getRoleName());
        request.setQualifiedRoleName(actionRequest.getQualifiedRoleName());
        request.setQualifiedRoleNameLabel(actionRequest.getQualifiedRoleNameLabel());
        request.setDelegationType(actionRequest.getDelegationType());
        return request;
    }

    private void setDefaultProperties(ActionRequestValue actionRequest) {
    	if (actionRequest.getApprovePolicy() == null) {
    		actionRequest.setApprovePolicy(ActionRequestPolicy.FIRST.getCode());
    	}
        actionRequest.setCreateDate(new Timestamp(System.currentTimeMillis()));
        actionRequest.setCurrentIndicator(Boolean.TRUE);
        if (actionRequest.getForceAction() == null) {
        	actionRequest.setForceAction(Boolean.FALSE);
        }
        if (routeNode != null) {
        	actionRequest.setNodeInstance(routeNode);
        }
        actionRequest.setJrfVerNbr(new Integer(0));
        actionRequest.setStatus(ActionRequestStatus.INITIALIZED.getCode());
        actionRequest.setRouteHeader(document);
        actionRequest.setDocumentId(document.getDocumentId());
    }

    private static void resolveRecipient(ActionRequestValue actionRequest, Recipient recipient) {
    	if (recipient instanceof KimPrincipalRecipient) {
    		actionRequest.setRecipientTypeCd(RecipientType.PRINCIPAL.getCode());
    		actionRequest.setPrincipalId(((KimPrincipalRecipient)recipient).getPrincipal().getPrincipalId());
    	}  else if (recipient instanceof KimGroupRecipient) {
    		KimGroupRecipient kimGroupRecipient = (KimGroupRecipient)recipient;
    		actionRequest.setRecipientTypeCd(RecipientType.GROUP.getCode());
    		actionRequest.setGroupId(kimGroupRecipient.getGroup().getId());
    	} else if (recipient instanceof RoleRecipient){
    		RoleRecipient role = (RoleRecipient)recipient;
    		actionRequest.setRecipientTypeCd(RecipientType.ROLE.getCode());
    		actionRequest.setRoleName(role.getRoleName());
    		actionRequest.setQualifiedRoleName(role.getQualifiedRoleName());
    		ResolvedQualifiedRole qualifiedRole = role.getResolvedQualifiedRole();
    		if (qualifiedRole != null) {
    			actionRequest.setAnnotation(qualifiedRole.getAnnotation() == null ? "" : qualifiedRole.getAnnotation());
    			actionRequest.setQualifiedRoleNameLabel(qualifiedRole.getQualifiedRoleLabel());
    		}
    		Recipient targetRecipient = role.getTarget();
    		if (role.getTarget() != null) {
    			if (targetRecipient instanceof RoleRecipient) {
    				throw new WorkflowRuntimeException("Role Cannot Target a role problem activating request for document " + actionRequest.getDocumentId());
    			}
    			resolveRecipient(actionRequest, role.getTarget());
    		}
    	} else if (recipient instanceof KimRoleResponsibilityRecipient) {
    		KimRoleResponsibilityRecipient roleResponsibilityRecipient = (KimRoleResponsibilityRecipient)recipient;
    		actionRequest.setRecipientTypeCd(RecipientType.ROLE.getCode());
    		actionRequest.setRoleName(roleResponsibilityRecipient.getResponsibilities().get(0).getRoleId());
    		actionRequest.setQualifiedRoleName(
                    roleResponsibilityRecipient.getResponsibilities().get(0).getResponsibilityName());
    		// what about qualified role name label?
//    		actionRequest.setAnnotation(roleRecipient.getResponsibilities().get(0).getResponsibilityName());
    		Recipient targetRecipient = roleResponsibilityRecipient.getTarget();
    		if (targetRecipient != null) {
    			if (targetRecipient instanceof RoleRecipient) {
    				throw new WorkflowRuntimeException("Role Cannot Target a role problem activating request for document " + actionRequest.getDocumentId());
    			}
    			resolveRecipient(actionRequest, roleResponsibilityRecipient.getTarget());
    		}
    	} else if (recipient instanceof KimRoleRecipient) {
            KimRoleRecipient roleRecipient = (KimRoleRecipient)recipient;
            actionRequest.setRecipientTypeCd(RecipientType.ROLE.getCode());
            Role role = roleRecipient.getRole();
            actionRequest.setRoleName(role.getId());
            actionRequest.setQualifiedRoleNameLabel(role.getName());
            Recipient targetRecipient = roleRecipient.getTarget();
    		if (targetRecipient != null) {
    			if (targetRecipient instanceof RoleRecipient) {
    				throw new WorkflowRuntimeException("Role Cannot Target a role problem activating request for document " + actionRequest.getDocumentId());
    			}
    			resolveRecipient(actionRequest, targetRecipient);
    		}
        }
    }

    /**
     * Creates a root Role Request
     * @param role
     * @param actionRequested
     * @param approvePolicy
     * @param priority
     * @param responsibilityId
     * @param forceAction
     * @param description
     * @param ruleId
     * @return the created root role request
     */
    public ActionRequestValue addRoleRequest(RoleRecipient role, String actionRequested, String approvePolicy, Integer priority, String responsibilityId, Boolean forceAction, String description, String ruleId) {

    	ActionRequestValue requestGraph = createActionRequest(actionRequested, priority, role, description, responsibilityId, forceAction, approvePolicy, ruleId, null);
    	if (role != null && role.getResolvedQualifiedRole() != null && role.getResolvedQualifiedRole().getRecipients() != null) {
    	    int legitimateTargets = 0;
            for (Iterator<Id> iter = role.getResolvedQualifiedRole().getRecipients().iterator(); iter.hasNext();) {
                Id recipientId = (Id) iter.next();
                if (recipientId.isEmpty())
                {
                    throw new WorkflowRuntimeException("Failed to resolve id of type " + recipientId.getClass().getName() + " returned from role '" + role.getRoleName() + "'.  Id returned contained a null or empty value.");
                }
                if (recipientId instanceof UserId)
                {
                    Principal principal = getIdentityHelperService().getPrincipal((UserId) recipientId);
                    if(ObjectUtils.isNotNull(principal)) {
                        role.setTarget(new KimPrincipalRecipient(principal));
                    }
                } else if (recipientId instanceof GroupId)
                {
                    role.setTarget(new KimGroupRecipient(getIdentityHelperService().getGroup((GroupId) recipientId)));
                } else
                {
                    throw new WorkflowRuntimeException("Could not process the given type of id: " + recipientId.getClass());
                }
                if (role.getTarget() != null)
                {
                    legitimateTargets++;
                    ActionRequestValue request = createActionRequest(actionRequested, priority, role, description, responsibilityId, forceAction, null, ruleId, null);
                    request.setParentActionRequest(requestGraph);
                    requestGraph.getChildrenRequests().add(request);
                }
            }
    	if (legitimateTargets == 0) {
            LOG.warn("Role did not yield any legitimate recipients");
        }
    	} else {
    		LOG.warn("Didn't create action requests for action request description '" + description + "' because of null role or null part of role object graph.");
    	}
    	requestGraphs.add(requestGraph);
    	return requestGraph;
    }

    /**
     * Generates an ActionRequest graph for the given KIM Responsibilities.  This graph includes any associated delegations.
     * @param responsibilities
     * @param approvePolicy
     */
    public void addRoleResponsibilityRequest(List<ResponsibilityAction> responsibilities, String approvePolicy) {
    	if (responsibilities == null || responsibilities.isEmpty()) {
    		LOG.warn("Didn't create action requests for action request description because no responsibilities were defined.");
    		return;
    	}
    	// it's assumed the that all in the list have the same action type code, priority number, etc.
    	String actionTypeCode = responsibilities.get(0).getActionTypeCode();
    	Integer priority = responsibilities.get(0).getPriorityNumber();
    	boolean forceAction = responsibilities.get(0).isForceAction();
    	KimRoleResponsibilityRecipient roleResponsibilityRecipient = new KimRoleResponsibilityRecipient(responsibilities);

    	// Creation of a parent graph entry for ????
    	ActionRequestValue requestGraph = null;
    	StringBuffer parentAnnotation = null;
    	// set to allow for suppression of duplicate annotations on the parent action request
    	Set<String> uniqueChildAnnotations = null;
    	if ( responsibilities.size() > 1 ) {
	    	requestGraph = createActionRequest(
	    	        actionTypeCode, 
	    	        priority, roleResponsibilityRecipient,
	    	        "", // description 
	    	        KewApiConstants.MACHINE_GENERATED_RESPONSIBILITY_ID,
	    	        forceAction, 
	    	        approvePolicy, 
	    	        null, // ruleId
	    	        null );// annotation
	    	requestGraphs.add(requestGraph);
	    	parentAnnotation = new StringBuffer();
	    	uniqueChildAnnotations = new HashSet<String>( responsibilities.size() );
    	}
    	StringBuffer annotation = new StringBuffer();
    	for (ResponsibilityAction responsibility : responsibilities) {
    		if ( LOG.isDebugEnabled() ) {
    			LOG.debug( "Processing Responsibility for action request: " + responsibility );
    		}
        	// KFSMI-2381 - pull information from KIM to populate annotation
    		annotation.setLength( 0 );
    		Role role = getRoleService().getRole(responsibility.getRoleId());
    		annotation.append( role.getNamespaceCode() ).append( ' ' ).append( role.getName() ).append( ' ' );
    		Map<String, String> qualifier = responsibility.getQualifier();
    		if ( qualifier != null ) {
	    		for ( String key : qualifier.keySet() ) {
	        		annotation.append( qualifier.get( key ) ).append( ' ' );
	    		}
    		}
			if (responsibility.getPrincipalId() != null) {
				roleResponsibilityRecipient.setTarget(new KimPrincipalRecipient(responsibility.getPrincipalId()));
			} else if (responsibility.getGroupId() != null) {
				roleResponsibilityRecipient.setTarget(new KimGroupRecipient(responsibility.getGroupId()));
			} else {
				throw new RiceRuntimeException("Failed to identify a group or principal on the given ResponsibilityResolutionInfo:" + responsibility);
			}
			String annotationStr = annotation.toString();
			ActionRequestValue request = createActionRequest(
			        responsibility.getActionTypeCode(), 
			        responsibility.getPriorityNumber(), roleResponsibilityRecipient,
			        responsibility.getParallelRoutingGroupingCode(), // description
			        responsibility.getResponsibilityId(), 
			        responsibility.isForceAction(), 
			        // If not nested in a parent action request, ensure that the request
			        // is first approve so delegations of this request do not require 
			        // ALL_APPROVE as well
			        (responsibilities.size() == 1)?ActionRequestPolicy.FIRST.getCode():approvePolicy, 
			        null, // ruleId
			        annotationStr);
			// if there is only a single request, don't create the nesting structure
			if ( responsibilities.size() > 1 ) {
				request.setParentActionRequest(requestGraph);
				requestGraph.getChildrenRequests().add(request);
				if ( !uniqueChildAnnotations.contains(annotationStr) ) {
					parentAnnotation.append( annotationStr ).append( " -- " );
					uniqueChildAnnotations.add(annotationStr);
				}
			} else {
				requestGraphs.add(request);
			}
            generateKimRoleDelegationRequests(responsibility.getDelegates(), request);

	    }
    	if ( responsibilities.size() > 1 ) {
	    	requestGraph.setAnnotation( StringUtils.chomp( parentAnnotation.toString(), " -- " ) );
    	}
    }

    private String generateRoleResponsibilityDelegateAnnotation(DelegateMember member, boolean isPrincipal, boolean isGroup, ActionRequestValue parentRequest) {
    	StringBuffer annotation = new StringBuffer( "Delegation of: " );
    	annotation.append( parentRequest.getAnnotation() );
    	annotation.append( " to " );
    	if (isPrincipal) {
    		annotation.append( "principal " );
    		Principal principal = getIdentityService().getPrincipal(member.getMemberId());
    		if ( principal != null ) {
    			annotation.append( principal.getPrincipalName() );
    		} else {
    			annotation.append( member.getMemberId() );
    		}
    	} else if (isGroup) {
    		annotation.append( "group " );
    		Group group = getGroupService().getGroup( member.getMemberId() );
    		if ( group != null ) {
    			annotation.append( group.getNamespaceCode() ).append( '/' ).append( group.getName() );
    		} else {
    			annotation.append( member.getMemberId() );
    		}
    	} else {
    		annotation.append( "?????? '" );
			annotation.append( member.getMemberId() );
    		annotation.append( "'" );
    	}
    	return annotation.toString();
    }

    public ActionRequestValue addDelegationRoleRequest(ActionRequestValue parentRequest, String approvePolicy, RoleRecipient role, String responsibilityId, Boolean forceAction, DelegationType delegationType, String description, String ruleId) {
    	Recipient parentRecipient = parentRequest.getRecipient();
    	if (parentRecipient instanceof RoleRecipient) {
    		throw new WorkflowRuntimeException("Cannot delegate on Role Request.  It must be a request to a person or workgroup, although that request may be in a role");
    	}
    	if (! relatedToRoot(parentRequest)) {
    		throw new WorkflowRuntimeException("The parent request is not related to any request managed by this factory");
    	}
    	ActionRequestValue delegationRoleRequest = createActionRequest(parentRequest.getActionRequested(), parentRequest.getPriority(), role, description, responsibilityId, forceAction, approvePolicy, ruleId, null);
    	delegationRoleRequest.setDelegationType(delegationType);
    	int count = 0;
    	for (Iterator<Id> iter = role.getResolvedQualifiedRole().getRecipients().iterator(); iter.hasNext(); count++) {
    		//repeat of createRoleRequest code
    		Id recipientId = iter.next();
    		if (recipientId.isEmpty()) {
				throw new WorkflowRuntimeException("Failed to resolve id of type " + recipientId.getClass().getName() + " returned from role '" + role.getRoleName() + "'.  Id returned contained a null or empty value.");
			}
			if (recipientId instanceof UserId) {
				role.setTarget(new KimPrincipalRecipient(getIdentityHelperService().getPrincipal((UserId) recipientId)));
			} else if (recipientId instanceof GroupId) {
			    role.setTarget(new KimGroupRecipient(getIdentityHelperService().getGroup((GroupId) recipientId)));
			} else {
				throw new WorkflowRuntimeException("Could not process the given type of id: " + recipientId.getClass());
			}
			ActionRequestValue request = createActionRequest(parentRequest.getActionRequested(), parentRequest.getPriority(), role, description, responsibilityId, forceAction, null, ruleId, null);
			request.setDelegationType(delegationType);
			//end repeat
			request.setParentActionRequest(delegationRoleRequest);
			delegationRoleRequest.getChildrenRequests().add(request);
    	}

    	//put this mini graph in the larger graph
    	if (count > 0) {
    		parentRequest.getChildrenRequests().add(delegationRoleRequest);
    		delegationRoleRequest.setParentActionRequest(parentRequest);
    	}

    	return delegationRoleRequest;
    }

    /**
     * Add a delegation request to the given parent action request.
     *
     * @param parentRequest the parent request to add it to
     * @param recipient the recipient to send the delegation request to
     * @param responsibilityId
     * @param forceAction
     * @param delegationType primary or secondary?
     * @param actionRequestPolicyCode the action request policy code specifying when the action requests are considered to be completed
     * @param annotation the annotation to put on the delegation request
     * @param ruleId
     * @return the delegation request that was added
     */
    public ActionRequestValue addDelegationRequest(ActionRequestValue parentRequest, Recipient recipient, String responsibilityId, Boolean forceAction, DelegationType delegationType, String actionRequestPolicyCode, String annotation, String ruleId) {
        if (! relatedToRoot(parentRequest)) {
            throw new WorkflowRuntimeException("The parent request is not related to any request managed by this factory");
        }
        ActionRequestValue delegationRequest = createActionRequest(parentRequest.getActionRequested(), parentRequest.getPriority(), recipient, parentRequest.getResponsibilityDesc(), responsibilityId, forceAction, actionRequestPolicyCode, ruleId, annotation);
        delegationRequest.setDelegationType(delegationType);

        parentRequest.getChildrenRequests().add(delegationRequest);
        delegationRequest.setParentActionRequest(parentRequest);

        return delegationRequest;
    }

    /**
     * Add a delegation request to the given parent action request.
     *
     * <p>no action type policy code will be specified.</p>
     *
     * @param parentRequest the parent request to add it to
     * @param recipient the recipient to send the delegation request to
     * @param responsibilityId
     * @param forceAction
     * @param delegationType primary or secondary?
     * @param annotation the annotation to put on the delegation request
     * @param ruleId
     * @return the delegation request that was added
     */
    public ActionRequestValue addDelegationRequest(ActionRequestValue parentRequest, Recipient recipient, String responsibilityId, Boolean forceAction, DelegationType delegationType, String annotation, String ruleId) {
    	return addDelegationRequest(parentRequest, recipient, responsibilityId, forceAction, delegationType, null, annotation, ruleId);
    }

    //could probably base behavior off of recipient type
    public ActionRequestValue addRootActionRequest(String actionRequested, Integer priority, Recipient recipient, String description, String responsibilityId, Boolean forceAction, String approvePolicy, String ruleId) {
    	ActionRequestValue requestGraph = createActionRequest(actionRequested, priority, recipient, description, responsibilityId, forceAction, approvePolicy, ruleId, null);
    	requestGraphs.add(requestGraph);
    	return requestGraph;
    }

    /**
     * Generates an ActionRequest graph for the given KIM Responsibilities.  This graph includes any associated delegations.
     *
     * @param actionRequestedCode the type of action requested
     * @param priority
     * @param role the role that the members belong to
     * @param memberships the role members to generate child requests to
     * @param description
     * @param responsibilityId
     * @param forceAction
     * @param actionRequestPolicyCode the action request policy code specifying when the action requests are considered to be completed
     * @param requestLabel
     * @return the request generated for the role members
     */
    public ActionRequestValue addKimRoleRequest(String actionRequestedCode, Integer priority, Role role,
            List<RoleMembership> memberships, String description, String responsibilityId, boolean forceAction,
            String actionRequestPolicyCode, String requestLabel) {
        return addKimRoleRequest(actionRequestedCode, priority, role, memberships, description, responsibilityId,
                forceAction, actionRequestPolicyCode, requestLabel, /* ignoreKimDelegates = */ false);
    }

    /**
     * Generates an ActionRequest graph for the given KIM Responsibilities.  This graph includes any associated delegations.
     *
     * @param actionRequestedCode the type of action requested
     * @param priority
     * @param role the role that the members belong to
     * @param memberships the role members to generate child requests to
     * @param description
     * @param responsibilityId
     * @param forceAction
     * @param actionRequestPolicyCode the action request policy code specifying when the action requests are considered to be completed
     * @param requestLabel
     * @param ignoreKimDelegates should kim delegates be ignored when generating requests
     * @return the request generated for the role members     */
    public ActionRequestValue addKimRoleRequest(String actionRequestedCode, Integer priority, Role role,
            List<RoleMembership> memberships, String description, String responsibilityId, boolean forceAction,
            String actionRequestPolicyCode, String requestLabel, boolean ignoreKimDelegates) {

        ActionRequestValue roleMemberRequest = null;

    	if (CollectionUtils.isEmpty(memberships)) {
    		LOG.warn("Didn't create action requests for action request description because no role members were defined for role id " + role.getId());
    		return roleMemberRequest;
    	}

        KimRoleRecipient roleRecipient = new KimRoleRecipient(role);

    	// Creation of a parent graph entry for ????
    	ActionRequestValue requestGraph = null;
    	if ( memberships.size() > 1 ) {
	    	requestGraph = createActionRequest(
	    	        actionRequestedCode,
	    	        priority,
                    roleRecipient,
	    	        "", // description
	    	        responsibilityId,
	    	        forceAction,
	    	        actionRequestPolicyCode,
	    	        null, // ruleId
	    	        null );// annotation
	    	requestGraphs.add(requestGraph);
    	}

    	for (RoleMembership membership : memberships) {
    		if ( LOG.isDebugEnabled() ) {
    			LOG.debug( "Processing RoleMembership for action request: " + membership );
    		}

			if (MemberType.PRINCIPAL.equals(membership.getType())) {
				roleRecipient.setTarget(new KimPrincipalRecipient(membership.getMemberId()));
			} else if (MemberType.GROUP.equals(membership.getType())) {
				roleRecipient.setTarget(new KimGroupRecipient(membership.getMemberId()));
			} else {
				throw new RiceRuntimeException("Failed to identify a group or principal on the given RoleMembership:" + membership);
			}

			ActionRequestValue request = createActionRequest(
			        actionRequestedCode,
			        priority,
                    roleRecipient,
                    "", // description
			        responsibilityId,
			        forceAction,
			        // If not nested in a parent action request, ensure that the request
			        // is first approve so delegations of this request do not require
			        // ALL_APPROVE as well
			        (memberships.size() == 1) ? ActionRequestPolicy.FIRST.getCode() : actionRequestPolicyCode,
			        null, // ruleId
			        null); // annotation

			// if there is only a single request, don't create the nesting structure
			if ( memberships.size() > 1 ) {
				request.setParentActionRequest(requestGraph);
				requestGraph.getChildrenRequests().add(request);

                if (roleMemberRequest == null) {
                    roleMemberRequest = requestGraph;
                }
			} else {
                roleMemberRequest = request;
				requestGraphs.add(request);
			}

            if (!ignoreKimDelegates) {
                generateKimRoleDelegationRequests(membership.getDelegates(), request);
            }
	    }

        return roleMemberRequest;
    }

    /**
     * Generates a delegate request to a KIM role.
     *
     * <p>In other words, the Role is the delegate.  Since delegates in KEW are limited to 1 level, this will ignore
     * any KIM delegations on the given role.</p>
     *
     * @param parentRequest the parent request that the delegate request will be added to
     * @param actionRequestedCode the type of action requested
     * @param priority
     * @param role the role that is being delegated to
     * @param memberships the role members to generate child requests to
     * @param description
     * @param responsibilityId
     * @param forceAction
     * @param actionRequestPolicyCode the action request policy code specifying when the action requests are considered to be completed
     * @param requestLabel
     * @return the delegate request generated for the role members
     */
    public ActionRequestValue addDelegateKimRoleRequest(ActionRequestValue parentRequest, DelegationType delegationType,
            String actionRequestedCode, Integer priority, Role role, List<RoleMembership> memberships,
            String description, String responsibilityId, boolean forceAction, String actionRequestPolicyCode,
            String requestLabel) {

        // This is a modified version of addKimRoleRequest.  The methods could probably be combined,
        // but the signature would be even more out of hand and the usage even more confusing.

        ActionRequestValue delegateRoleRequest = null;

        if (CollectionUtils.isEmpty(memberships)) {
            LOG.warn("Didn't create action requests for action request description because no role members were defined for role id " + role.getId());
            return delegateRoleRequest;
        }

        KimRoleRecipient roleRecipient = new KimRoleRecipient(role);

        // Creation of a parent graph entry for ????
        ActionRequestValue requestGraph = null;
        if ( memberships.size() > 1 ) {
            requestGraph = createActionRequest(
                    actionRequestedCode,
                    priority,
                    roleRecipient,
                    "", // description
                    responsibilityId,
                    forceAction,
                    actionRequestPolicyCode,
                    null, // ruleId
                    null );// annotation
            requestGraphs.add(requestGraph);
        }

        for (RoleMembership membership : memberships) {
            if ( LOG.isDebugEnabled() ) {
                LOG.debug( "Processing RoleMembership for action request: " + membership );
            }

            if (MemberType.PRINCIPAL.equals(membership.getType())) {
                roleRecipient.setTarget(new KimPrincipalRecipient(membership.getMemberId()));
            } else if (MemberType.GROUP.equals(membership.getType())) {
                roleRecipient.setTarget(new KimGroupRecipient(membership.getMemberId()));
            } else {
                throw new RiceRuntimeException("Failed to identify a group or principal on the given RoleMembership:" + membership);
            }

            ActionRequestValue request = createActionRequest(
                    actionRequestedCode,
                    priority,
                    roleRecipient,
                    "", // description
                    responsibilityId,
                    forceAction,
                    // If not nested in a parent action request, ensure that the request
                    // is first approve so delegations of this request do not require
                    // ALL_APPROVE as well
                    (memberships.size() == 1) ? ActionRequestPolicy.FIRST.getCode() : actionRequestPolicyCode,
                    null, // ruleId
                    null); // annotation

            // if there is only a single request, don't create the nesting structure
            if ( memberships.size() > 1 ) {
                request.setParentActionRequest(requestGraph);
                requestGraph.getChildrenRequests().add(request);

                if (delegateRoleRequest == null) {
                    delegateRoleRequest = requestGraph;
                }
            } else {
                delegateRoleRequest = request;
            }
        }

        delegateRoleRequest.setDelegationType(delegationType);
        delegateRoleRequest.setParentActionRequest(parentRequest);
        parentRequest.getChildrenRequests().add(delegateRoleRequest);

        return delegateRoleRequest;
    }

     private void generateKimRoleDelegationRequests(List<DelegateType> delegates, ActionRequestValue parentRequest) {
    	for (DelegateType delegate : delegates) {
            for (DelegateMember member : delegate.getMembers()) {
    		    Recipient recipient;
    		    boolean isPrincipal = MemberType.PRINCIPAL.equals(member.getType());
                boolean isGroup = MemberType.GROUP.equals(member.getType());
    		    if (isPrincipal) {
    			    recipient = new KimPrincipalRecipient(member.getMemberId());
    		    } else if (isGroup) {
    			    recipient = new KimGroupRecipient(member.getMemberId());
    		    } else {
    			    throw new RiceRuntimeException("Invalid DelegateInfo memberTypeCode encountered, was '" + member.getType() + "'");
    		    }
    		    String delegationAnnotation = generateRoleResponsibilityDelegateAnnotation(member, isPrincipal, isGroup, parentRequest);
    		    addDelegationRequest(parentRequest, recipient, delegate.getDelegationId(), parentRequest.getForceAction(), delegate.getDelegationType(), delegationAnnotation, null);
    	    }
        }
    }

    //return true if requestGraph (root) is in this requests' parents
    public boolean relatedToRoot(ActionRequestValue request) {
    	int i = 0;
    	while(i < 3) {
    		if (requestGraphs.contains(request)) {
    			return true;
    		} else if (request == null) {
    			return false;
    		}
    		i++;
    		request = request.getParentActionRequest();
    	}
    	return false;
    }

	public List<ActionRequestValue> getRequestGraphs() {
		//clean up all the trailing role requests with no children -
		requestGraphs.removeAll(cleanUpChildren(requestGraphs));
		return requestGraphs;
	}

	private Collection<ActionRequestValue> cleanUpChildren(Collection<ActionRequestValue> children) {
		Collection<ActionRequestValue> requestsToRemove = new ArrayList<ActionRequestValue>();
        for (ActionRequestValue aChildren : children)
        {

            if (aChildren.isRoleRequest())
            {
                if (aChildren.getChildrenRequests().isEmpty())
                {
                    requestsToRemove.add(aChildren);
                } else
                {
                    Collection<ActionRequestValue> childRequestsToRemove = cleanUpChildren(aChildren.getChildrenRequests());
                    aChildren.getChildrenRequests().removeAll(childRequestsToRemove);
				}
			}                                                                                 
		}
		return requestsToRemove;
	}

    private String generateNotificationAnnotation(PrincipalContract principal, String notificationRequestCode, String actionTakenCode, ActionRequestValue request) {
    	String notification = "Action " + CodeTranslator.getActionRequestLabel(notificationRequestCode) + " generated by Workflow because " + principal.getPrincipalName() + " took action "
				+ CodeTranslator.getActionTakenLabel(actionTakenCode);
    	// FIXME: KULRICE-5201 switched rsp_id to a varchar, so the comparison below is no longer valid
//    	if (request.getResponsibilityId() != null && request.getResponsibilityId() != 0) {
    	// TODO: KULRICE-5329 Verify that this code below makes sense and is sufficient
    	if (request.getResponsibilityId() != null && !KewApiConstants.MACHINE_GENERATED_RESPONSIBILITY_ID.equals(request.getResponsibilityId())) {
    		notification += " Responsibility " + request.getResponsibilityId();
    	}
    	if (request.getRuleBaseValuesId() != null) {
    		notification += " Rule Id " + request.getRuleBaseValuesId();
    	}
    	if (request.getAnnotation() != null && request.getAnnotation().length()!=0){
    		notification += " " + request.getAnnotation();
    	}
    	return notification;
	}

    protected static ActionRequestService getActionRequestService() {
		if ( actionRequestService == null ) {
			actionRequestService = KEWServiceLocator.getActionRequestService();
		}
		return actionRequestService;
    }

	/**
	 * @return the roleService
	 */
    protected static RoleService getRoleService() {
		if ( roleService == null ) {
			roleService = KimApiServiceLocator.getRoleService();
		}
		return roleService;
	}

	/**
	 * @return the identityHelperService
	 */
    protected static IdentityHelperService getIdentityHelperService() {
		if ( identityHelperService == null ) {
			identityHelperService = KEWServiceLocator.getIdentityHelperService();
		}
		return identityHelperService;
	}

	/**
	 * @return the identityService
	 */
    protected static IdentityService getIdentityService() {
		if ( identityService == null ) {
			identityService = KimApiServiceLocator.getIdentityService();
		}
		return identityService;
	}

    protected static GroupService getGroupService() {
		if ( groupService == null ) {
			groupService = KimApiServiceLocator.getGroupService();
		}
		return groupService;
	}
}
