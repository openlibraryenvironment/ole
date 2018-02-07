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
package org.kuali.rice.kew.actions;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.api.document.DocumentProcessingOptions;
import org.kuali.rice.kew.api.document.DocumentProcessingQueue;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeIndexingQueue;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.PostProcessor;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Super class containing mostly often used methods by all actions. Holds common
 * state as well, {@link DocumentRouteHeaderValue} document,
 * {@link ActionTakenValue} action taken (once saved), {@link PrincipalContract} principal
 * that has taken the action
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class ActionTakenEvent {

    /**
     * Default value for queueing document after the action is recorded
     */
    protected static final boolean DEFAULT_QUEUE_DOCUMENT_AFTER_ACTION = true;
    /**
     * Default value for running postprocessor logic after the action is recorded.
     * Inspected when queueing document processing and notifying postprocessors of action taken and doc status change
     */
    protected static final boolean DEFAULT_RUN_POSTPROCESSOR_LOGIC = true;
    /**
     * Default annotation - none.
     */
    protected static final String DEFAULT_ANNOTATION = null;

    private static final Logger LOG = Logger.getLogger(ActionTakenEvent.class);


	/**
	 * Used when saving an ActionTakenValue, and for validation in validateActionRules
     * TODO: Clarify the intent of actionTakenCode vs getActionPerformed() - which is the perceived incoming
     * value, and what is getActionPerformed, the "value-we-are-going-to-save-to-the-db"? if so, make
     * sure that is reflected consistently in all respective usages.
     * See: SuperUserActionRequestApproveEvent polymorphism
	 */
	private String actionTakenCode;

	protected final String annotation;

    /**
     * This is in spirit immutable, however for expediency it is mutable as at least one action
     * (ReturnToPreviousNodeAction) attempts to reload the route header after every pp notification.
     */
	protected DocumentRouteHeaderValue routeHeader;

	private final PrincipalContract principal;

    private final boolean runPostProcessorLogic;

    private final boolean queueDocumentAfterAction;

    /**
     * This is essentially just a cache to avoid an expensive lookup in getGroupIdsForPrincipal
     */
    private transient List<String> groupIdsForPrincipal;

	public ActionTakenEvent(String actionTakenCode, DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
		this(actionTakenCode, routeHeader, principal, DEFAULT_ANNOTATION, DEFAULT_RUN_POSTPROCESSOR_LOGIC, DEFAULT_QUEUE_DOCUMENT_AFTER_ACTION);
	}

    public ActionTakenEvent(String actionTakenCode, DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation) {
        this(actionTakenCode, routeHeader, principal, annotation, DEFAULT_RUN_POSTPROCESSOR_LOGIC, DEFAULT_QUEUE_DOCUMENT_AFTER_ACTION);
    }

	public ActionTakenEvent(String actionTakenCode, DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean runPostProcessorLogic) {
        this(actionTakenCode, routeHeader, principal, annotation, runPostProcessorLogic, DEFAULT_QUEUE_DOCUMENT_AFTER_ACTION);
	}

    public ActionTakenEvent(String actionTakenCode, DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean runPostProcessorLogic, boolean queueDocumentAfterAction) {
        this.actionTakenCode = actionTakenCode;
        this.routeHeader = routeHeader;
        this.principal = principal;
        this.annotation = annotation == null ? "" : annotation;
        this.runPostProcessorLogic = runPostProcessorLogic;
        this.queueDocumentAfterAction = queueDocumentAfterAction;
    }

	public ActionRequestService getActionRequestService() {
		return (ActionRequestService) KEWServiceLocator.getService(KEWServiceLocator.ACTION_REQUEST_SRV);
	}

	protected DocumentRouteHeaderValue getRouteHeader() {
		return routeHeader;
	}

	protected void setRouteHeader(DocumentRouteHeaderValue routeHeader) {
		this.routeHeader = routeHeader;
	}

    protected PrincipalContract getPrincipal() {
		return principal;
	}

	/**
	 * Code of the action performed by the user
	 *
	 * Method may be overriden is action performed will be different than action
	 * taken
     * @return
     */
	protected String getActionPerformedCode() {
		return getActionTakenCode();
	}

	/**
	 * Validates whether or not this action is valid for the given principal
	 * and DocumentRouteHeaderValue.
	 */
	protected boolean isActionValid() {
		return org.apache.commons.lang.StringUtils.isEmpty(validateActionRules());
	}


    /**
     * Determines whether a specific policy is set on the document type.
     * @param docType the document type
     * @param policy the DocumentTypePolicy
     * @param deflt the default value if the policy is not present
     * @return the policy value or deflt if missing
     */
    protected static boolean isPolicySet(DocumentType docType, DocumentTypePolicy policy, boolean deflt) {
        return docType.getPolicyByName(policy.name(), Boolean.valueOf(deflt)).getPolicyValue().booleanValue();
    }

    /**
     * Determines whether a specific policy is set on the document type.
     * @param docType the document type
     * @param policy the DocumentTypePolicy
     * @return the policy value or false if missing
     */
    protected static boolean isPolicySet(DocumentType docType, DocumentTypePolicy policy) {
        return isPolicySet(docType, policy, false);
    }

	/**
	 * Placeholder for validation rules for each action
	 *
	 * @return error message string of specific error message
	 */
	public abstract String validateActionRules();
	protected abstract String validateActionRules(List<ActionRequestValue> actionRequests);
	
	/**
	 * Filters action requests based on if they occur after the given requestCode, and if they relate to this
	 * event's principal
	 * @param actionRequests the List of ActionRequestValues to filter
	 * @param requestCode the request code for all ActionRequestValues to be after
	 * @return the filtered List of ActionRequestValues
	 */
	protected List<ActionRequestValue> filterActionRequestsByCode(List<ActionRequestValue> actionRequests, String requestCode) {
		return getActionRequestService().filterActionRequestsByCode(actionRequests, getPrincipal().getPrincipalId(), getGroupIdsForPrincipal(), requestCode);
	}

	protected boolean isActionCompatibleRequest(List<ActionRequestValue> requests) {
		LOG.debug("isActionCompatibleRequest() Default method = returning true");
		return true;
	}

    // TODO: determine why some code invokes performAction, and some code invokes record action
    // notably, WorkflowDocumentServiceImpl. Shouldn't all invocations go through a single public entry point?
    // are some paths implicitly trying to avoid error handling or document queueing?
	public void performAction() throws InvalidActionTakenException {
	    try{
	        recordAction();
        }catch(InvalidActionTakenException e){
            if(routeHeader.getDocumentType().getEnrouteErrorSuppression().getPolicyValue()){
                LOG.error("Invalid Action Taken Exception was thrown, but swallowed due to ENROUTE_ERROR_SUPPRESSION document type policy!");
                return;
            }else{
                throw e;
            }
        }
        if (queueDocumentAfterAction) {
	    	queueDocumentProcessing();
	    }

	}

	protected abstract void recordAction() throws InvalidActionTakenException;

	protected void updateSearchableAttributesIfPossible() {
		// queue the document up so that it can be indexed for searching if it
		// has searchable attributes
		RouteContext routeContext = RouteContext.getCurrentRouteContext();
		if (routeHeader.getDocumentType().hasSearchableAttributes() && !routeContext.isSearchIndexingRequestedForContext()) {
			routeContext.requestSearchIndexingForContext();
            DocumentAttributeIndexingQueue queue = KewApiServiceLocator.getDocumentAttributeIndexingQueue(routeHeader.getDocumentType().getApplicationId());
            queue.indexDocument(getDocumentId());
		}
	}

    /**
     * Wraps PostProcessor invocation with error handling
     * @param message log message
     * @param invocation the callable that invokes the postprocessor
     */
    protected void invokePostProcessor(String message, Callable<ProcessDocReport> invocation) {
        if (!isRunPostProcessorLogic()) {
            return;
        }
        LOG.debug(message);
        try {
            ProcessDocReport report = invocation.call();
            if (!report.isSuccess()) {
                LOG.warn(report.getMessage(), report.getProcessException());
                throw new InvalidActionTakenException(report.getMessage());
            }
        } catch (Exception ex) {
            processPostProcessorException(ex);
        }
    }

	protected void notifyActionTaken(final ActionTakenValue actionTaken) {
        invokePostProcessor("Notifying post processor of action taken", new Callable<ProcessDocReport>() {
            public ProcessDocReport call() throws Exception {
                PostProcessor postProcessor = routeHeader.getDocumentType().getPostProcessor();
                return postProcessor.doActionTaken(new org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent(routeHeader.getDocumentId(), routeHeader.getAppDocId(), ActionTakenValue.to(actionTaken)));
            }
        });
	}

    protected void notifyAfterActionTaken(final ActionTakenValue actionTaken) {
        invokePostProcessor("Notifying post processor after action taken", new Callable<ProcessDocReport>() {
            public ProcessDocReport call() throws Exception {
                PostProcessor postProcessor = routeHeader.getDocumentType().getPostProcessor();
                return postProcessor.afterActionTaken(ActionType.fromCode(getActionPerformedCode()), new org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent(routeHeader.getDocumentId(), routeHeader.getAppDocId(), ActionTakenValue.to(actionTaken)));
            }
        });
    }

	protected void notifyStatusChange(final String newStatusCode, final String oldStatusCode) throws InvalidActionTakenException {
        invokePostProcessor("Notifying post processor of status change " + oldStatusCode + "->" + newStatusCode, new Callable<ProcessDocReport>() {
            public ProcessDocReport call() throws Exception {
                DocumentRouteStatusChange statusChangeEvent = new DocumentRouteStatusChange(routeHeader.getDocumentId(), routeHeader.getAppDocId(), oldStatusCode, newStatusCode);
                PostProcessor postProcessor = routeHeader.getDocumentType().getPostProcessor();
                return postProcessor.doRouteStatusChange(statusChangeEvent);
            }
        });
	}

	/**
	 * Asynchronously queues the documented to be processed by the workflow engine.
	 */
	protected void queueDocumentProcessing() {
    	DocumentRouteHeaderValue document = getRouteHeader();
        String applicationId = document.getDocumentType().getApplicationId();
        DocumentProcessingQueue documentProcessingQueue = (DocumentProcessingQueue) KewApiServiceLocator.getDocumentProcessingQueue(
            document.getDocumentId(), applicationId);
        DocumentProcessingOptions options = DocumentProcessingOptions.create(isRunPostProcessorLogic(), RouteContext.getCurrentRouteContext().isSearchIndexingRequestedForContext());
        documentProcessingQueue.processWithOptions(getDocumentId(), options);
	}

	protected ActionTakenValue saveActionTaken() {
	    return saveActionTaken(Boolean.TRUE);
	}

	protected ActionTakenValue saveActionTaken(Boolean currentInd) {
		return saveActionTaken(currentInd, null);
	}

	protected ActionTakenValue saveActionTaken(Recipient delegator) {
	    return saveActionTaken(Boolean.TRUE, delegator);
	}

	protected ActionTakenValue saveActionTaken(Boolean currentInd, Recipient delegator) {
		ActionTakenValue val = new ActionTakenValue();
		val.setActionTaken(getActionTakenCode());
		val.setAnnotation(annotation);
		val.setDocVersion(routeHeader.getDocVersion());
		val.setDocumentId(routeHeader.getDocumentId());
		val.setPrincipalId(principal.getPrincipalId());
		if (delegator instanceof KimPrincipalRecipient) {
			val.setDelegatorPrincipalId(((KimPrincipalRecipient)delegator).getPrincipalId());
		} else if (delegator instanceof KimGroupRecipient) {
			val.setDelegatorGroupId(((KimGroupRecipient) delegator).getGroupId());
		}
		//val.setRouteHeader(routeHeader);
		val.setCurrentIndicator(currentInd);
		KEWServiceLocator.getActionTakenService().saveActionTaken(val);
		return val;
	}

	/**
	 * Returns the highest priority delegator in the list of action requests.
	 */
	protected Recipient findDelegatorForActionRequests(List actionRequests) {
		return getActionRequestService().findDelegator(actionRequests);
	}

	public String getActionTakenCode() {
		return actionTakenCode;
	}

	protected void setActionTakenCode(String string) {
		actionTakenCode = string;
	}

	protected String getDocumentId() {
		return this.routeHeader.getDocumentId();
	}

	/*protected void delete() {
	    KEWServiceLocator.getActionTakenService().delete(actionTaken);
	}*/

	protected boolean isRunPostProcessorLogic() {
        return this.runPostProcessorLogic;
    }
	
	protected List<String> getGroupIdsForPrincipal() {
		if (groupIdsForPrincipal == null) {
			groupIdsForPrincipal = KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(
                    getPrincipal().getPrincipalId());
		}
		return groupIdsForPrincipal;
	}

	private void processPostProcessorException(Exception e) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException)e;
        }
        throw new WorkflowRuntimeException(e);
	}

    /**
     * Utility for generating Acknowledgements to previous document action takers.  Note that in constrast with other
     * notification-generation methods (such as those in ActionRequestFactory) this method determines its recipient list
     * from ActionTakenValues, not from outstanding ActionRequests.
     * @see ActionRequestFactory#generateNotifications(java.util.List, org.kuali.rice.kim.api.identity.principal.PrincipalContract, org.kuali.rice.kew.actionrequest.Recipient, String, String)
     * @see ActionRequestFactory#generateNotifications(org.kuali.rice.kew.actionrequest.ActionRequestValue, java.util.List, org.kuali.rice.kim.api.identity.principal.PrincipalContract, org.kuali.rice.kew.actionrequest.Recipient, String, String, org.kuali.rice.kim.api.group.Group)
     * @param notificationNodeInstance the node instance with which generated actionrequests will be associated
     */
    protected void generateAcknowledgementsToPreviousActionTakers(RouteNodeInstance notificationNodeInstance)
    {
        String groupName = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(
                KewApiConstants.KEW_NAMESPACE,
                KRADConstants.DetailTypes.WORKGROUP_DETAIL_TYPE,
                KewApiConstants.NOTIFICATION_EXCLUDED_USERS_WORKGROUP_NAME_IND);

        Set<String> systemPrincipalIds = new HashSet<String>();

        if( !StringUtils.isBlank(groupName))
        {
            Group systemUserWorkgroup = KimApiServiceLocator.getGroupService().
                    getGroupByNamespaceCodeAndName(Utilities.parseGroupNamespaceCode(groupName),
                            Utilities.parseGroupName(groupName));

            List<String> principalIds = KimApiServiceLocator.
                    getGroupService().getMemberPrincipalIds( systemUserWorkgroup.getId());

            if (systemUserWorkgroup != null)
            {
                for( String id : principalIds)
                {
                    systemPrincipalIds.add(id);
                }
            }
        }
        ActionRequestFactory arFactory = new ActionRequestFactory(getRouteHeader(), notificationNodeInstance);
        Collection<ActionTakenValue> actions = KEWServiceLocator.getActionTakenService().findByDocumentId(getDocumentId());
        //one notification per person
        Set<String> usersNotified = new HashSet<String>();
        for (ActionTakenValue action : actions)
        {
            if ((action.isApproval() || action.isCompletion()) && !usersNotified.contains(action.getPrincipalId()))
            {
                if (!systemPrincipalIds.contains(action.getPrincipalId()))
                {
                    ActionRequestValue request = arFactory.createNotificationRequest(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, action.getPrincipal(), getActionTakenCode(), getPrincipal(), getActionTakenCode());
                    KEWServiceLocator.getActionRequestService().activateRequest(request);
                    usersNotified.add(request.getPrincipalId());
                }
            }
        }
    }
}
