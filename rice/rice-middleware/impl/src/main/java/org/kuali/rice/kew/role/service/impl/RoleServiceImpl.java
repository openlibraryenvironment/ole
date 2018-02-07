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
package org.kuali.rice.kew.role.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.RolePokerQueue;
import org.kuali.rice.kew.api.document.DocumentProcessingQueue;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.role.service.RoleService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.FlexRM;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.service.KEWServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoleServiceImpl implements RoleService {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RoleServiceImpl.class);

    public void reResolveRole(DocumentType documentType, String roleName) {
    	String infoString = "documentType="+(documentType == null ? null : documentType.getName())+", role="+roleName;
        if (documentType == null ||
                org.apache.commons.lang.StringUtils.isEmpty(roleName)) {
            throw new IllegalArgumentException("Cannot pass null or empty arguments to reResolveQualifiedRole: "+infoString);
        }
        LOG.debug("Re-resolving role asynchronously for "+infoString);
    	Set documentIds = new HashSet();
    	findAffectedDocuments(documentType, roleName, null, documentIds);
    	LOG.debug(documentIds.size()+" documents were affected by this re-resolution, requeueing with the RolePokerQueue");
    	for (Iterator iterator = documentIds.iterator(); iterator.hasNext();) {
    		String documentId = (String) iterator.next();
            String applicationId = KEWServiceLocator.getRouteHeaderService().getApplicationIdByDocumentId(documentId);
            RolePokerQueue rolePokerQueue = KewApiServiceLocator.getRolePokerQueue(documentId, applicationId);
    		rolePokerQueue.reResolveRole(documentId, roleName);
		}
    }

    public void reResolveQualifiedRole(DocumentType documentType, String roleName, String qualifiedRoleNameLabel) {
    	String infoString = "documentType="+(documentType == null ? null : documentType.getName())+", role="+roleName+", qualifiedRole="+qualifiedRoleNameLabel;
        if (documentType == null ||
                org.apache.commons.lang.StringUtils.isEmpty(roleName) ||
                org.apache.commons.lang.StringUtils.isEmpty(qualifiedRoleNameLabel)) {
            throw new IllegalArgumentException("Cannot pass null or empty arguments to reResolveQualifiedRole: "+infoString);
        }
        LOG.debug("Re-resolving qualified role asynchronously for "+infoString);
    	Set documentIds = new HashSet();
    	findAffectedDocuments(documentType, roleName, qualifiedRoleNameLabel, documentIds);
    	LOG.debug(documentIds.size()+" documents were affected by this re-resolution, requeueing with the RolePokerQueue");
    	for (Iterator iterator = documentIds.iterator(); iterator.hasNext();) {
    		String documentId = (String) iterator.next();
            String applicationId = KEWServiceLocator.getRouteHeaderService().getApplicationIdByDocumentId(documentId);
            RolePokerQueue rolePokerQueue = KewApiServiceLocator.getRolePokerQueue(documentId, applicationId);
    		rolePokerQueue.reResolveQualifiedRole(documentId, roleName, qualifiedRoleNameLabel);
		}
    }

    /**
     *
     * route level and then filters in the approriate ones.
     */
    public void reResolveQualifiedRole(DocumentRouteHeaderValue routeHeader, String roleName, String qualifiedRoleNameLabel) {
        String infoString = "routeHeader="+(routeHeader == null ? null : routeHeader.getDocumentId())+", role="+roleName+", qualifiedRole="+qualifiedRoleNameLabel;
        if (routeHeader == null ||
                org.apache.commons.lang.StringUtils.isEmpty(roleName) ||
                org.apache.commons.lang.StringUtils.isEmpty(qualifiedRoleNameLabel)) {
            throw new IllegalArgumentException("Cannot pass null arguments to reResolveQualifiedRole: "+infoString);
        }
        LOG.debug("Re-resolving qualified role synchronously for "+infoString);
        List nodeInstances = findNodeInstances(routeHeader, roleName);
        int requestsGenerated = 0;
        if (!nodeInstances.isEmpty()) {
            deletePendingRoleRequests(routeHeader.getDocumentId(), roleName, qualifiedRoleNameLabel);
            for (Iterator nodeIt = nodeInstances.iterator(); nodeIt.hasNext();) {
                RouteNodeInstance nodeInstance = (RouteNodeInstance)nodeIt.next();
                RuleTemplateBo ruleTemplate = nodeInstance.getRouteNode().getRuleTemplate();
                FlexRM flexRM = new FlexRM();
        		RouteContext context = RouteContext.getCurrentRouteContext();
        		context.setDocument(routeHeader);
        		context.setNodeInstance(nodeInstance);
        		try {
        			List actionRequests = flexRM.getActionRequests(routeHeader, nodeInstance, ruleTemplate.getName());
        			for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
        				ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
        				if (roleName.equals(actionRequest.getRoleName()) && qualifiedRoleNameLabel.equals(actionRequest.getQualifiedRoleNameLabel())) {
        					actionRequest = KEWServiceLocator.getActionRequestService().initializeActionRequestGraph(actionRequest, routeHeader, nodeInstance);
        					KEWServiceLocator.getActionRequestService().saveActionRequest(actionRequest);
        					requestsGenerated++;
        				}
        			}
        		} catch (Exception e) {
        			RouteContext.clearCurrentRouteContext();
        		}

            }
        }
        LOG.debug("Generated "+requestsGenerated+" action requests after re-resolve: "+infoString);
        requeueDocument(routeHeader);
    }

    public void reResolveRole(DocumentRouteHeaderValue routeHeader, String roleName) {
    	String infoString = "routeHeader="+(routeHeader == null ? null : routeHeader.getDocumentId())+", role="+roleName;
        if (routeHeader == null ||
                org.apache.commons.lang.StringUtils.isEmpty(roleName)) {
            throw new RiceIllegalArgumentException("Cannot pass null arguments to reResolveQualifiedRole: "+infoString);
        }
        LOG.debug("Re-resolving role synchronously for "+infoString);
        List nodeInstances = findNodeInstances(routeHeader, roleName);
        int requestsGenerated = 0;
        if (!nodeInstances.isEmpty()) {
            deletePendingRoleRequests(routeHeader.getDocumentId(), roleName, null);
            for (Iterator nodeIt = nodeInstances.iterator(); nodeIt.hasNext();) {
                RouteNodeInstance nodeInstance = (RouteNodeInstance)nodeIt.next();
                RuleTemplateBo ruleTemplate = nodeInstance.getRouteNode().getRuleTemplate();
                FlexRM flexRM = new FlexRM();
        		RouteContext context = RouteContext.getCurrentRouteContext();
        		context.setDocument(routeHeader);
        		context.setNodeInstance(nodeInstance);
        		try {
        			List actionRequests = flexRM.getActionRequests(routeHeader, nodeInstance, ruleTemplate.getName());
        			for (Iterator iterator = actionRequests.iterator(); iterator.hasNext();) {
        				ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
        				if (roleName.equals(actionRequest.getRoleName())) {
        					actionRequest = KEWServiceLocator.getActionRequestService().initializeActionRequestGraph(actionRequest, routeHeader, nodeInstance);
        					KEWServiceLocator.getActionRequestService().saveActionRequest(actionRequest);
        					requestsGenerated++;
        				}
        			}
        		} finally {
        			RouteContext.clearCurrentRouteContext();
        		}
            }
        }
        LOG.debug("Generated "+requestsGenerated+" action requests after re-resolve: "+infoString);
        requeueDocument(routeHeader);
    }

    // search the document type and all its children
    private void findAffectedDocuments(DocumentType documentType, String roleName, String qualifiedRoleNameLabel, Set documentIds) {
    	List pendingRequests = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocumentType(documentType.getDocumentTypeId());
    	for (Iterator iterator = pendingRequests.iterator(); iterator.hasNext();) {
			ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
			if (roleName.equals(actionRequest.getRoleName()) &&
					(qualifiedRoleNameLabel == null || qualifiedRoleNameLabel.equals(actionRequest.getQualifiedRoleNameLabel()))) {
				documentIds.add(actionRequest.getDocumentId());
			}
		}
    	for (Iterator iterator = documentType.getChildrenDocTypes().iterator(); iterator.hasNext();) {
			DocumentType childDocumentType = (DocumentType) iterator.next();
			findAffectedDocuments(childDocumentType, roleName, qualifiedRoleNameLabel, documentIds);
		}
    }

    private void deletePendingRoleRequests(String documentId, String roleName, String qualifiedRoleNameLabel) {
        List pendingRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(documentId);
        pendingRequests = KEWServiceLocator.getActionRequestService().getRootRequests(pendingRequests);
        List requestsToDelete = new ArrayList();
        for (Iterator iterator = pendingRequests.iterator(); iterator.hasNext();) {
            ActionRequestValue actionRequest = (ActionRequestValue) iterator.next();
            if (roleName.equals(actionRequest.getRoleName()) &&
            		(qualifiedRoleNameLabel == null || qualifiedRoleNameLabel.equals(actionRequest.getQualifiedRoleNameLabel()))) {
                requestsToDelete.add(actionRequest);
            }
        }
        LOG.debug("Deleting "+requestsToDelete.size()+" action requests for roleName="+roleName+", qualifiedRoleNameLabel="+qualifiedRoleNameLabel);
        for (Iterator iterator = requestsToDelete.iterator(); iterator.hasNext();) {
            KEWServiceLocator.getActionRequestService().deleteActionRequestGraph((ActionRequestValue)iterator.next());
        }
    }

    private List findNodeInstances(DocumentRouteHeaderValue routeHeader, String roleName) {
        List nodeInstances = new ArrayList();
        Collection activeNodeInstances = KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(routeHeader.getDocumentId());
        if (CollectionUtils.isEmpty(activeNodeInstances)) {
            throw new IllegalStateException("Document does not currently have any active nodes so re-resolving is not legal.");
        }
        for (Iterator iterator = activeNodeInstances.iterator(); iterator.hasNext();) {
            RouteNodeInstance activeNodeInstance = (RouteNodeInstance) iterator.next();
            RuleTemplateBo template = activeNodeInstance.getRouteNode().getRuleTemplate();
            if (templateHasRole(template, roleName)) {
                nodeInstances.add(activeNodeInstance);
            }
        }
        if (nodeInstances.isEmpty()) {
            throw new IllegalStateException("Could not locate given role to re-resolve: " + roleName);
        }
        return nodeInstances;
    }

    private boolean templateHasRole(RuleTemplateBo template, String roleName) {
        List<RuleTemplateAttributeBo> templateAttributes = template.getRuleTemplateAttributes();
        for (RuleTemplateAttributeBo templateAttribute : templateAttributes) {
            List<RoleName> roleNames = KEWServiceLocator.getWorkflowRuleAttributeMediator().getRoleNames(templateAttribute);
            for (RoleName role : roleNames) {
                if (role.getLabel().equals(roleName)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void requeueDocument(DocumentRouteHeaderValue document) {
        String applicationId = document.getDocumentType().getApplicationId();
        DocumentProcessingQueue documentProcessingQueue = KewApiServiceLocator.getDocumentProcessingQueue(document.getDocumentId(), applicationId);
        documentProcessingQueue.process(document.getDocumentId());
    }

}
