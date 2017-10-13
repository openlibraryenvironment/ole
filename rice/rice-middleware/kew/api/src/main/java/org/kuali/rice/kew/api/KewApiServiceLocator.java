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
package org.kuali.rice.kew.api;

import javax.xml.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.action.ActionInvocationQueue;
import org.kuali.rice.kew.api.action.RolePokerQueue;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.actionlist.ActionListService;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.kew.api.document.DocumentOrchestrationQueue;
import org.kuali.rice.kew.api.document.DocumentProcessingQueue;
import org.kuali.rice.kew.api.document.DocumentRefreshQueue;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeIndexingQueue;
import org.kuali.rice.kew.api.extension.ExtensionRepositoryService;
import org.kuali.rice.kew.api.group.GroupMembershipChangeQueue;
import org.kuali.rice.kew.api.mail.ImmediateEmailReminderQueue;
import org.kuali.rice.kew.api.note.NoteService;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowService;
import org.kuali.rice.kew.api.preferences.PreferencesService;
import org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService;
import org.kuali.rice.kew.api.responsibility.ResponsibilityChangeQueue;
import org.kuali.rice.kew.api.rule.RuleService;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;

/**
 * A static service locator which aids in locating the various services that
 * form the Kuali Service Bus API.
 */
public class KewApiServiceLocator {

    // Rice 2.0 - TODO - should these be using the QName versions instead?  How else will they be fetched in "remote" mode?
	public static final String WORKFLOW_DOCUMENT_ACTIONS_SERVICE = "rice.kew.workflowDocumentActionsService";
	public static final String WORKFLOW_DOCUMENT_SERVICE = "rice.kew.workflowDocumentService";
    public static final String ACTION_LIST_SERVICE = "rice.kew.actionListService";
	public static final String DOCUMENT_TYPE_SERVICE = "rice.kew.documentTypeService";
	public static final String NOTE_SERVICE = "rice.kew.noteService";
    public static final String EXTENSION_REPOSITORY_SERVICE = "rice.kew.extensionRepositoryService";
    public static final String RULE_SERVICE = "rice.kew.ruleService";
    public static final String KEW_TYPE_REPOSITORY_SERVICE = "rice.kew.kewTypeRepositoryService";
    public static final String PEOPLE_FLOW_SERVICE = "rice.kew.peopleFlowService";
    public static final String PREFERENCES_SERVICE = "rice.kew.preferencesService";
    public static final String KEW_RUN_MODE_PROPERTY = "kew.mode";
    public static final String STANDALONE_APPLICATION_ID = "standalone.application.id";
    public static final QName DOCUMENT_ATTRIBUTE_INDEXING_QUEUE_NAME = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "documentAttributeIndexingQueue");
    public static final QName GROUP_MEMBERSHIP_CHANGE_QUEUE_NAME = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "groupMembershipChangeQueue");
    public static final QName IMMEDIATE_EMAIL_REMINDER_QUEUE = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "immediateEmailReminderQueue");
    public static final QName RESPONSIBILITY_CHANGE_QUEUE = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "responsibilityChangeQueue");
    public static final QName DOCUMENT_REFRESH_QUEUE = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "documentRefreshQueue");
    public static final QName ROLE_POKER_QUEUE = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "rolePokerQueue");
    public static final QName DOCUMENT_PROCESSING_QUEUE = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "documentProcessingQueue");
    public static final QName DOCUMENT_ORCHESTRATION_QUEUE = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "documentOrchestrationQueue");
    public static final QName ACTION_INVOCATION_QUEUE = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "actionInvocationQueue");

    static <T> T getService(String serviceName) {
        return GlobalResourceLoader.<T>getService(serviceName);
    }

    public static WorkflowDocumentActionsService getWorkflowDocumentActionsService() {
        RunMode kewRunMode = RunMode.valueOf(ConfigContext.getCurrentContextConfig().getProperty(KEW_RUN_MODE_PROPERTY));
        if (kewRunMode == RunMode.REMOTE || kewRunMode == RunMode.THIN) {
            String standaloneApplicationId = ConfigContext.getCurrentContextConfig().getProperty(STANDALONE_APPLICATION_ID);
            return getWorkflowDocumentActionsService(standaloneApplicationId);
        } else { 
            return getService(WORKFLOW_DOCUMENT_ACTIONS_SERVICE);
        }
    }
    
    public static WorkflowDocumentActionsService getWorkflowDocumentActionsService(String applicationId){
        if(!StringUtils.isEmpty(applicationId)){//Need to find out proper remote endpoint in this case
            QName qN = new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, KewApiConstants.ServiceNames.WORKFLOW_DOCUMENT_ACTIONS_SERVICE_SOAP);
            //http://rice.kuali.org/kew/v2_0}workflowDocumentActionsService
            return (WorkflowDocumentActionsService)KsbApiServiceLocator.getServiceBus().getService(qN, applicationId);
        }else{//we can use the default internal service here since we dont have an applicationId
            return getWorkflowDocumentActionsService();
        }
    }
    
    public static WorkflowDocumentService getWorkflowDocumentService() {
        return getService(WORKFLOW_DOCUMENT_SERVICE);
    }

    public static ActionListService getActionListService() {
        return getService(ACTION_LIST_SERVICE);
    }
    
    public static DocumentTypeService getDocumentTypeService() {
        return getService(DOCUMENT_TYPE_SERVICE);
    }
    
    public static NoteService getNoteService() {
    	return getService(NOTE_SERVICE);
    }

    public static RuleService getRuleService() {
    	return getService(RULE_SERVICE);
    }

    public static ExtensionRepositoryService getExtensionRepositoryService() {
        return getService(EXTENSION_REPOSITORY_SERVICE);
    }

    public static KewTypeRepositoryService getKewTypeRepositoryService() {
        return getService(KEW_TYPE_REPOSITORY_SERVICE);
    }

    public static PeopleFlowService getPeopleFlowService() {
        return getService(PEOPLE_FLOW_SERVICE);
    }

    public static DocumentAttributeIndexingQueue getDocumentAttributeIndexingQueue() {
        return getDocumentAttributeIndexingQueue(null);
    }

    /**
     * For accessing common asynchronous services.
     */

    public static DocumentAttributeIndexingQueue getDocumentAttributeIndexingQueue(String applicationId) {
        return (DocumentAttributeIndexingQueue)KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(DOCUMENT_ATTRIBUTE_INDEXING_QUEUE_NAME, applicationId);
    }
    
    public static GroupMembershipChangeQueue getGroupMembershipChangeQueue() {
        return (GroupMembershipChangeQueue)KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(GROUP_MEMBERSHIP_CHANGE_QUEUE_NAME);
    }

    public static ResponsibilityChangeQueue getResponsibilityChangeQueue() {
        return (ResponsibilityChangeQueue)KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(
                RESPONSIBILITY_CHANGE_QUEUE);
    }
    
    public static ImmediateEmailReminderQueue getImmediateEmailReminderQueue() {
        return KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(IMMEDIATE_EMAIL_REMINDER_QUEUE);
    }

    public static PreferencesService getPreferencesService() {
        return getService(PREFERENCES_SERVICE);
    }

    public static DocumentProcessingQueue getDocumentProcessingQueue(String documentId, String applicationId) {
        return (DocumentProcessingQueue)getServiceAsynchronously(DOCUMENT_PROCESSING_QUEUE, documentId, applicationId);
    }

    public static ActionInvocationQueue getActionInvocationProcessorService(String documentId, String applicationId) {
        return (ActionInvocationQueue) getServiceAsynchronously(ACTION_INVOCATION_QUEUE, documentId, applicationId);
    }

    public static DocumentOrchestrationQueue getDocumentOrchestrationQueue(String documentId, String applicationId) {
        return (DocumentOrchestrationQueue) getServiceAsynchronously(DOCUMENT_ORCHESTRATION_QUEUE, documentId, applicationId);
    }

    public static RolePokerQueue getRolePokerQueue(String documentId, String applicationId) {
        return (RolePokerQueue) getServiceAsynchronously(ROLE_POKER_QUEUE, documentId, applicationId);
    }

    public static DocumentRefreshQueue getDocumentRequeuerService(String applicationId, String documentId, long waitTime) {
        if (waitTime > 0) {
            return (DocumentRefreshQueue) getDelayedServiceAsynchronously(DOCUMENT_REFRESH_QUEUE, documentId, waitTime, applicationId);
        }
        return (DocumentRefreshQueue) getServiceAsynchronously(DOCUMENT_REFRESH_QUEUE, documentId, applicationId);
    }

    private static Object getDelayedServiceAsynchronously(QName serviceName, String documentId, long waitTime, String applicationId) {
        return KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName, applicationId, null, (documentId == null ? null : documentId.toString()), null, waitTime);
    }

    private static Object getServiceAsynchronously(QName serviceName, String documentId, String applicationId) {
        return KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName, applicationId, null, null, (documentId == null ? null : documentId.toString()), null);
    }
}
