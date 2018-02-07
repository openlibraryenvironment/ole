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
package org.kuali.rice.kew.impl.document;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.doctype.RouteNode;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.api.document.DocumentLink;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.dto.DTOConverter;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValueContent;
import org.kuali.rice.kew.routeheader.DocumentStatusTransition;
import org.kuali.rice.kew.service.KEWServiceLocator;

import javax.jws.WebParam;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class WorkflowDocumentServiceImpl implements WorkflowDocumentService {

	private static final Logger LOG = Logger.getLogger(WorkflowDocumentServiceImpl.class);
	
	@Override
	public Document getDocument(String documentId) {
		if (StringUtils.isBlank(documentId)) {
			throw new RiceIllegalArgumentException("documentId was blank or null");
		}
		DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
		return DocumentRouteHeaderValue.to(documentBo);
	}

	@Override
	public boolean doesDocumentExist(String documentId) {
	    if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }
	    DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
	    return documentBo != null;
	}

    @Override
    public DocumentDetail getDocumentDetailByAppId(String documentTypeName, String appId) {
        if (StringUtils.isEmpty(documentTypeName)) {
            throw new RiceIllegalArgumentException("documentTypeName was blank or null");
        }
        if (StringUtils.isEmpty(appId)) {
            throw new RiceIllegalArgumentException("appId was blank or null");
        }

        Collection documentIds = KEWServiceLocator.getRouteHeaderService().findByDocTypeAndAppId(documentTypeName, appId);
        if(documentIds==null||documentIds.isEmpty()){
            throw new RiceIllegalStateException("No RouteHeader Ids found for documentTypName: " + documentTypeName + ", appId: " + appId);
        }
        if(documentIds.size()>1){
            throw new RiceIllegalStateException("Multiple RouteHeader Ids found for documentTypName: " + documentTypeName + ", appId: " + appId);
		}

        return getDocumentDetail((String)documentIds.iterator().next());
	}

    public RouteNodeInstance getRouteNodeInstance(String nodeInstanceId)  {
        if (StringUtils.isEmpty(nodeInstanceId)) {
            throw new RiceIllegalArgumentException("nodeInstanceId was blank or null");
        }
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Fetching RouteNodeInstanceVO [id="+nodeInstanceId+"]");
        }
        org.kuali.rice.kew.engine.node.RouteNodeInstance nodeInstance = KEWServiceLocator.getRouteNodeService().findRouteNodeInstanceById(nodeInstanceId);
        return org.kuali.rice.kew.engine.node.RouteNodeInstance.to(nodeInstance);
    }

    @Override
    public DocumentStatus getDocumentStatus(String documentId) {
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }
        String documentStatus = KEWServiceLocator.getRouteHeaderService().getDocumentStatus(documentId);
        if (StringUtils.isEmpty(documentStatus)) {
            throw new RiceIllegalStateException("DocumentStatus not found for documentId: " + documentId);
        }
        return DocumentStatus.fromCode(documentStatus);
    }

    @Override
    public String getApplicationDocumentId(String documentId) {
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }
 	 	return KEWServiceLocator.getRouteHeaderService().getAppDocId(documentId);
 	}

    @Override
    public String getApplicationDocumentStatus(String documentId)
            throws RiceIllegalArgumentException {
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }
        return KEWServiceLocator.getRouteHeaderService().getAppDocStatus(documentId);
    }

    @Override
    public DocumentSearchResults documentSearch(String principalId, DocumentSearchCriteria criteria) {
        if (criteria == null) {
            throw new RiceIllegalArgumentException("criteria was null");
        }
        return KEWServiceLocator.getDocumentSearchService().lookupDocuments(principalId, criteria);
    }

    @Override
    public List<String> getSearchableAttributeStringValuesByKey(String documentId, String key) {
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }
        if (StringUtils.isEmpty(key)) {
            throw new RiceIllegalArgumentException("key was blank or null");
        }
		return KEWServiceLocator.getRouteHeaderService().getSearchableAttributeStringValuesByKey(documentId, key);
	}

    @Override
	public List<DateTime> getSearchableAttributeDateTimeValuesByKey(String documentId, String key) {
		if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }
        if (StringUtils.isEmpty(key)) {
            throw new RiceIllegalArgumentException("key was blank or null");
        }

        List<Timestamp> results = KEWServiceLocator.getRouteHeaderService().getSearchableAttributeDateTimeValuesByKey(documentId, key);
        if (results == null) {
            return null;
        }
        List<DateTime> dateTimes = new ArrayList<DateTime>();

		for(Timestamp time : results) {
            dateTimes.add(new DateTime(time.getTime()));
        }
        return dateTimes;
	}

    @Override
	public List<BigDecimal> getSearchableAttributeFloatValuesByKey(String documentId, String key) {
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }
        if (StringUtils.isEmpty(key)) {
            throw new RiceIllegalArgumentException("key was blank or null");
        }
		return KEWServiceLocator.getRouteHeaderService().getSearchableAttributeFloatValuesByKey(documentId, key);
	}

    @Override
    public List<Long> getSearchableAttributeLongValuesByKey(String documentId, String key) {
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }
        if (StringUtils.isEmpty(key)) {
            throw new RiceIllegalArgumentException("key was blank or null");
        }
		return KEWServiceLocator.getRouteHeaderService().getSearchableAttributeLongValuesByKey(documentId, key);
	}
	
	@Override
	public DocumentContent getDocumentContent(String documentId) {
		if (StringUtils.isBlank(documentId)) {
			throw new RiceIllegalArgumentException("documentId was blank or null");
		}
		DocumentRouteHeaderValueContent content = KEWServiceLocator.getRouteHeaderService().getContent(documentId);
		return DocumentRouteHeaderValueContent.to(content);
	}

	@Override
	public List<ActionRequest> getRootActionRequests(String documentId) {
        if (StringUtils.isBlank(documentId)) {
			throw new RiceIllegalArgumentException("documentId was blank or null");
		}
		List<ActionRequest> actionRequests = new ArrayList<ActionRequest>();
		List<ActionRequestValue> actionRequestBos = KEWServiceLocator.getActionRequestService().findAllRootActionRequestsByDocumentId(documentId);
		for (ActionRequestValue actionRequestBo : actionRequestBos) {
			actionRequests.add(ActionRequestValue.to(actionRequestBo));
		}
		return Collections.unmodifiableList(actionRequests);
	}
	
	@Override
	public List<ActionRequest> getPendingActionRequests(String documentId) {
		if (StringUtils.isBlank(documentId)) {
			throw new RiceIllegalArgumentException("documentId was blank or null");
		}
		List<ActionRequest> actionRequests = new ArrayList<ActionRequest>();
		List<ActionRequestValue> actionRequestBos = KEWServiceLocator.getActionRequestService().findAllPendingRequests(documentId);
		for (ActionRequestValue actionRequestBo : actionRequestBos) {
			actionRequests.add(ActionRequestValue.to(actionRequestBo));
		}
		return Collections.unmodifiableList(actionRequests);
	}
	
	@Override
	public List<ActionRequest> getActionRequestsForPrincipalAtNode(String documentId, String nodeName,
            String principalId) {
        if (StringUtils.isBlank(documentId)) {
        	throw new RiceIllegalArgumentException("documentId was null or blank");
        }
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Fetching ActionRequests [docId="+documentId+", nodeName="+nodeName+", principalId="+principalId+"]");
        }
        List<ActionRequestValue> actionRequestBos = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(documentId);
        List<ActionRequestValue> matchingActionRequests = new ArrayList<ActionRequestValue>();
        for (ActionRequestValue actionRequestValue : actionRequestBos) {
            if (actionRequestMatches(actionRequestValue, nodeName, principalId)) {
                matchingActionRequests.add(actionRequestValue);
            }
        }
        List<ActionRequest> actionRequests = new ArrayList<ActionRequest>(matchingActionRequests.size());
        for (ActionRequestValue matchingActionRequest : matchingActionRequests) {
        	actionRequests.add(ActionRequestValue.to(matchingActionRequest));
        }
        return actionRequests;
    }
	
    protected boolean actionRequestMatches(ActionRequestValue actionRequest, String nodeName, String principalId) {
        boolean matchesUserId = true;  // assume a match in case user is empty
        boolean matchesNodeName = true;  // assume a match in case node name is empty
        if (StringUtils.isNotBlank(nodeName)) {
            matchesNodeName = nodeName.equals(actionRequest.getPotentialNodeName());
        }
        if (principalId != null) {
            matchesUserId = actionRequest.isRecipientRoutedRequest(principalId);
        }
        return matchesNodeName && matchesUserId;
    }


	@Override
	public List<ActionTaken> getActionsTaken(String documentId) {
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId is null or empty.");
        }
		List<ActionTaken> actionTakens = new ArrayList<ActionTaken>();
		Collection<ActionTakenValue> actionTakenBos = KEWServiceLocator.getActionTakenService().findByDocumentId(documentId);
		for (ActionTakenValue actionTakenBo : actionTakenBos) {
			actionTakens.add(ActionTakenValue.to(actionTakenBo));
		}
		return actionTakens;
	}

    @Override
    public List<ActionTaken> _getActionsTaken(String documentId) {
        return getActionsTaken(documentId);
    }

    @Override
    public List<ActionTaken> getAllActionsTaken(String documentId){
        if(StringUtils.isEmpty(documentId)){
            throw new RiceIllegalArgumentException("documentId is null or empty.");
        }

		List<ActionTaken> actionsTaken = new ArrayList<ActionTaken>();
        Collection<ActionTakenValue> actionTakenBos = KEWServiceLocator.getActionTakenService().findByDocumentIdIgnoreCurrentInd(documentId);
		for (ActionTakenValue actionTakenBo : actionTakenBos) {
			actionsTaken.add(ActionTakenValue.to(actionTakenBo));
		}
       return actionsTaken;
    }
	
	@Override
	public DocumentDetail getDocumentDetail(@WebParam(name = "documentId") String documentId) {
		if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Fetching DocumentDetail [id="+documentId+"]");
        }
        DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        if (document == null) {
        	return null;
        }
        DocumentDetail documentDetailVO = DTOConverter.convertDocumentDetailNew(document);
        if ( LOG.isDebugEnabled() ) {
        	LOG.debug("Returning DocumentDetailVO [id=" + documentId + "]");
        }
        return documentDetailVO;
	}

    @Override
    public List<org.kuali.rice.kew.api.document.DocumentStatusTransition> getDocumentStatusTransitionHistory(String documentId) {
		if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }
        if ( LOG.isDebugEnabled() ) {
            LOG.debug("Fetching document status transition history [id="+documentId+"]");
        }
        DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);;

        List<DocumentStatusTransition> list = document.getAppDocStatusHistory();

        List<org.kuali.rice.kew.api.document.DocumentStatusTransition> transitionHistory = new ArrayList<org.kuali.rice.kew.api.document.DocumentStatusTransition>(list.size());

        for (DocumentStatusTransition transition : list) {
            transitionHistory.add(DocumentStatusTransition.to(transition));
        }
        return transitionHistory;
    }
	
	@Override
	public List<RouteNodeInstance> getRouteNodeInstances(String documentId) {
    	if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }

        if ( LOG.isDebugEnabled() ) {
    		LOG.debug("Fetching RouteNodeInstances [documentId=" + documentId + "]");
    	}
    	DocumentRouteHeaderValue documentBo = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
    	if (documentBo == null) {
    		return Collections.emptyList();
    	}
    	return convertRouteNodeInstances(KEWServiceLocator.getRouteNodeService().getFlattenedNodeInstances(documentBo, true));
    }	
	
	@Override
	public List<RouteNodeInstance> getActiveRouteNodeInstances(String documentId) {
		if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }

        if ( LOG.isDebugEnabled() ) {
    		LOG.debug("Fetching active RouteNodeInstances [documentId=" + documentId + "]");
    	}
        return convertRouteNodeInstances(KEWServiceLocator.getRouteNodeService().getActiveNodeInstances(documentId));
	}

    @Override
    public List<RouteNodeInstance> getTerminalRouteNodeInstances(String documentId) {
    	if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }

        if ( LOG.isDebugEnabled() ) {
    		LOG.debug("Fetching terminal RouteNodeInstanceVOs [docId=" + documentId + "]");
    	}
        return convertRouteNodeInstances(KEWServiceLocator.getRouteNodeService().getTerminalNodeInstances(documentId));
    }

    public List<RouteNodeInstance> getCurrentRouteNodeInstances(String documentId) {
    	if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }

        if ( LOG.isDebugEnabled() ) {
    		LOG.debug("Fetching current RouteNodeInstanceVOs [docId=" + documentId + "]");
    	}
    	return convertRouteNodeInstances(KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(documentId));
    }
    
    public List<String> getActiveRouteNodeNames(String documentId) {
    	if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }
    	
    	final List<String> nodes = KEWServiceLocator.getRouteNodeService().getActiveRouteNodeNames(documentId);
    	return nodes != null ? Collections.unmodifiableList(nodes) : Collections.<String>emptyList();
    }
    
    public List<String> getTerminalRouteNodeNames(String documentId) {
    	if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }
    	
    	final List<String> nodes = KEWServiceLocator.getRouteNodeService().getTerminalRouteNodeNames(documentId);
    	return nodes != null ? Collections.unmodifiableList(nodes) : Collections.<String>emptyList();
    }

    public List<String> getCurrentRouteNodeNames(String documentId) {
    	if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }
    	
    	final List<String> nodes = KEWServiceLocator.getRouteNodeService().getCurrentRouteNodeNames(documentId);
    	return nodes != null ? Collections.unmodifiableList(nodes) : Collections.<String>emptyList();
    }

	private List<RouteNodeInstance> convertRouteNodeInstances(List<org.kuali.rice.kew.engine.node.RouteNodeInstance> routeNodeInstanceBos) {
		List<RouteNodeInstance> routeNodeInstances = new ArrayList<RouteNodeInstance>();
        for (org.kuali.rice.kew.engine.node.RouteNodeInstance routeNodeInstanceBo : routeNodeInstanceBos) {
        	routeNodeInstances.add(org.kuali.rice.kew.engine.node.RouteNodeInstance.to(routeNodeInstanceBo));
        }
        return Collections.unmodifiableList(routeNodeInstances);
	}
	
	@Override
	public List<String> getPreviousRouteNodeNames(String documentId) {

		if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }
        if ( LOG.isDebugEnabled() ) {
			LOG.debug("Fetching previous node names [documentId=" + documentId + "]");
		}
        return new ArrayList<String>(KEWServiceLocator.getRouteNodeService().findPreviousNodeNames(documentId));
	}

    @Override
    public List<String> getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId(String actionRequestedCd, String documentId){
    	if (StringUtils.isEmpty(actionRequestedCd)) {
            throw new RiceIllegalArgumentException("actionRequestCd was blank or null");
        }
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }
        return KEWServiceLocator.getActionRequestService().
    				getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId(actionRequestedCd, documentId);
    }

    @Override
    public String getDocumentInitiatorPrincipalId(String documentId) {
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }

        DocumentRouteHeaderValue header = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId, false);
        if ( header == null) {
        	return null;
        }
    	return header.getInitiatorWorkflowId();
    }

    @Override
    public String getRoutedByPrincipalIdByDocumentId(String documentId) {
        if (StringUtils.isEmpty(documentId)) {
            throw new RiceIllegalArgumentException("documentId was blank or null");
        }

        DocumentRouteHeaderValue header = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId, false);
        if ( header == null) {
        	return null;
        }
    	return header.getRoutedByUserWorkflowId();
    }

	@Override
	public DocumentLink addDocumentLink(DocumentLink documentLink) throws RiceIllegalArgumentException {
		if (documentLink == null) {
			throw new RiceIllegalArgumentException("documentLink was null");
		}
		if (documentLink.getId() != null) {
			throw new RiceIllegalArgumentException("the given documentLink already has an id, cannot add a document link with an existing id");
		}
		org.kuali.rice.kew.documentlink.DocumentLink documentLinkBo = org.kuali.rice.kew.documentlink.DocumentLink.from(documentLink);
		KEWServiceLocator.getDocumentLinkService().saveDocumentLink(documentLinkBo);
		return org.kuali.rice.kew.documentlink.DocumentLink.to(documentLinkBo);
	}

	@Override
	public DocumentLink deleteDocumentLink(String documentLinkId) throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(documentLinkId)) {
			throw new RiceIllegalArgumentException("documentLinkId was null or blank");
		}
		org.kuali.rice.kew.documentlink.DocumentLink documentLinkBo = KEWServiceLocator.getDocumentLinkService().getDocumentLink(Long.valueOf(documentLinkId));
		if (documentLinkBo == null) {
			throw new RiceIllegalStateException("Failed to locate document link with the given documentLinkId: " + documentLinkId);
		}
		KEWServiceLocator.getDocumentLinkService().deleteDocumentLink(documentLinkBo);
		return org.kuali.rice.kew.documentlink.DocumentLink.to(documentLinkBo);
	}
	    
	@Override
	public List<DocumentLink> deleteDocumentLinksByDocumentId(String originatingDocumentId) throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(originatingDocumentId)) {
			throw new RiceIllegalArgumentException("originatingDocumentId was null or blank");
		}
		List<org.kuali.rice.kew.documentlink.DocumentLink> documentLinkBos = KEWServiceLocator.getDocumentLinkService().getLinkedDocumentsByDocId(originatingDocumentId);
		if (documentLinkBos == null || documentLinkBos.isEmpty()) {
			return Collections.emptyList();
		}
		List<DocumentLink> deletedDocumentLinks = new ArrayList<DocumentLink>();
		for (org.kuali.rice.kew.documentlink.DocumentLink documentLinkBo : documentLinkBos) {
			deletedDocumentLinks.add(org.kuali.rice.kew.documentlink.DocumentLink.to(documentLinkBo));
			KEWServiceLocator.getDocumentLinkService().deleteDocumentLink(documentLinkBo);
		}
		return Collections.unmodifiableList(deletedDocumentLinks);
    }
	    
	@Override
	public List<DocumentLink> getOutgoingDocumentLinks(String originatingDocumentId) throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(originatingDocumentId)) {
			throw new RiceIllegalArgumentException("originatingDocumentId was null or blank");
		}
		List<org.kuali.rice.kew.documentlink.DocumentLink> outgoingDocumentLinkBos = KEWServiceLocator.getDocumentLinkService().getLinkedDocumentsByDocId(originatingDocumentId);
		List<DocumentLink> outgoingDocumentLinks = new ArrayList<DocumentLink>();
		for (org.kuali.rice.kew.documentlink.DocumentLink outgoingDocumentLinkBo : outgoingDocumentLinkBos) {
			outgoingDocumentLinks.add(org.kuali.rice.kew.documentlink.DocumentLink.to(outgoingDocumentLinkBo));
		}
		return Collections.unmodifiableList(outgoingDocumentLinks);
    }
	
	@Override
	public List<DocumentLink> getIncomingDocumentLinks(String destinationDocumentId) throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(destinationDocumentId)) {
			throw new RiceIllegalArgumentException("destinationDocumentId was null or blank");
		}
		List<org.kuali.rice.kew.documentlink.DocumentLink> incomingDocumentLinkBos = KEWServiceLocator.getDocumentLinkService().getOutgoingLinkedDocumentsByDocId(destinationDocumentId);
		List<DocumentLink> incomingDocumentLinks = new ArrayList<DocumentLink>();
		for (org.kuali.rice.kew.documentlink.DocumentLink incomingDocumentLinkBo : incomingDocumentLinkBos) {
			incomingDocumentLinks.add(org.kuali.rice.kew.documentlink.DocumentLink.to(incomingDocumentLinkBo));
		}
		return Collections.unmodifiableList(incomingDocumentLinks);
    }
	    
	@Override
	public DocumentLink getDocumentLink(String documentLinkId) throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(documentLinkId)) {
			throw new RiceIllegalArgumentException("documentLinkId was null or blank");
		}
		org.kuali.rice.kew.documentlink.DocumentLink documentLinkBo = KEWServiceLocator.getDocumentLinkService().getDocumentLink(Long.valueOf(documentLinkId));
		return org.kuali.rice.kew.documentlink.DocumentLink.to(documentLinkBo);
    }
	
}
