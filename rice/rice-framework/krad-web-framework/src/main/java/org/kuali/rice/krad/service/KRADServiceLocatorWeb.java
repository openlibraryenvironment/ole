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
package org.kuali.rice.krad.service;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.lookup.Lookupable;
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.uif.service.AttributeQueryService;
import org.kuali.rice.krad.uif.service.ViewDictionaryService;
import org.kuali.rice.krad.uif.service.ViewService;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;

/**
 * Service locator for the KRAD Web module
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KRADServiceLocatorWeb {

    public static final class Namespaces {
        public static final String MODULE_NAME = "krad";
        public static final String KRAD_NAMESPACE_PREFIX =
                CoreConstants.Namespaces.ROOT_NAMESPACE_PREFIX + "/" + MODULE_NAME;

        /**
         * Namespace for the krad module which is compatible with Kuali Rice 2.0.x.
         */
        public static final String KRAD_NAMESPACE_2_0 =
                KRAD_NAMESPACE_PREFIX + "/" + CoreConstants.Versions.VERSION_2_0;

    }

    public static final String DATA_DICTIONARY_REMOTE_FIELD_SERVICE = "dataDictionaryRemoteFieldService";
    public static final String DATA_DICTIONARY_COMPONENT_PUBLISHER_SERVICE = "dataDictionaryComponentPublisherService";
    public static final String DOCUMENT_DICTIONARY_SERVICE = "documentDictionaryService";
    public static final String SESSION_DOCUMENT_SERVICE = "sessionDocumentService";
    public static final String DATA_OBJECT_AUTHORIZATION_SERVICE = "dataObjectAuthorizationService";
    public static final String MAINTENANCE_DOCUMENT_SERVICE = "maintenanceDocumentService";
    public static final String WORKFLOW_DOCUMENT_SERVICE = "workflowDocumentService";
    public static final String EXCEPTION_INCIDENT_REPORT_SERVICE = "kradExceptionIncidentService";
    public static final String FEEDBACK_REPORT_SERVICE = "kradFeedbackService";
    public static final String DATA_DICTIONARY_SERVICE = "dataDictionaryService";
    public static final String DOCUMENT_HEADER_SERVICE = "documentHeaderService";
    public static final String PESSIMISTIC_LOCK_SERVICE = "pessimisticLockService";
    public static final String PERSISTENCE_SERVICE_OJB = "persistenceServiceOjb";
    public static final String KUALI_MODULE_SERVICE = "kualiModuleService";
    public static final String KUALI_RULE_SERVICE = "kualiRuleService";
    public static final String DOCUMENT_SERVICE = "documentService";
    public static final String DOCUMENT_SERIALIZER_SERVICE = "documentSerializerService";
    public static final String LOOKUP_SERVICE = "lookupService";
    public static final String DICTIONARY_VALIDATION_SERVICE = "dictionaryValidationService";
    public static final String DEFAULT_INACTIVATION_BLOCKING_DETECTION_SERVICE = "inactivationBlockingDetectionService";
    public static final String DATA_OBJECT_METADATA_SERVICE = "dataObjectMetaDataService";
    public static final String VIEW_SERVICE = "viewService";
    public static final String VIEW_DICTIONARY_SERVICE = "viewDictionaryService";
    public static final String VIEW_VALIDATION_SERVICE = "viewValidationService";
    public static final String ATTRIBUTE_QUERY_SERVICE = "attributeQueryService";
    public static final String MESSAGE_SERVICE = "messageService";
    public static final String POST_PROCESSOR_SERVICE = "postProcessorService";
    public static final String INACTIVATION_BLOCKING_DISPLAY_SERVICE = "inactivationBlockingDisplayService";

    public static <T extends Object> T getService(String serviceName) {
        return GlobalResourceLoader.<T>getService(serviceName);
    }

    public static DocumentDictionaryService getDocumentDictionaryService() {
        return getService(DOCUMENT_DICTIONARY_SERVICE);
    }

    public static SessionDocumentService getSessionDocumentService() {
        return getService(SESSION_DOCUMENT_SERVICE);
    }

    public static DataObjectAuthorizationService getDataObjectAuthorizationService() {
        return getService(DATA_OBJECT_AUTHORIZATION_SERVICE);
    }

    public static MaintenanceDocumentService getMaintenanceDocumentService() {
        return getService(MAINTENANCE_DOCUMENT_SERVICE);
    }

    public static WorkflowDocumentService getWorkflowDocumentService() {
        return getService(WORKFLOW_DOCUMENT_SERVICE);
    }

    public static final KualiExceptionIncidentService getKualiExceptionIncidentService() {
        return getService(EXCEPTION_INCIDENT_REPORT_SERVICE);
    }

    public static final KualiFeedbackService getKualiFeedbackService() {
        return getService(FEEDBACK_REPORT_SERVICE);
    }

    public static DataDictionaryService getDataDictionaryService() {
        return getService(DATA_DICTIONARY_SERVICE);
    }

    public static DocumentHeaderService getDocumentHeaderService() {
        return getService(DOCUMENT_HEADER_SERVICE);
    }

    public static PessimisticLockService getPessimisticLockService() {
        return getService(PESSIMISTIC_LOCK_SERVICE);
    }

    public static Lookupable getLookupable(String lookupableName) {
        return getService(lookupableName);
    }

    public static PersistenceService getPersistenceServiceOjb() {
        return getService(PERSISTENCE_SERVICE_OJB);
    }

    public static KualiModuleService getKualiModuleService() {
        return getService(KUALI_MODULE_SERVICE);
    }

    public static KualiRuleService getKualiRuleService() {
        return getService(KUALI_RULE_SERVICE);
    }

    public static DocumentService getDocumentService() {
        return getService(DOCUMENT_SERVICE);
    }

    public static DocumentSerializerService getDocumentSerializerService() {
        return (DocumentSerializerService) getService(DOCUMENT_SERIALIZER_SERVICE);
    }

    public static LookupService getLookupService() {
        return (LookupService) getService(LOOKUP_SERVICE);
    }

    public static DictionaryValidationService getDictionaryValidationService() {
        return (DictionaryValidationService) getService(DICTIONARY_VALIDATION_SERVICE);
    }

    public static InactivationBlockingDetectionService getInactivationBlockingDetectionService(String serviceName) {
        return (InactivationBlockingDetectionService) getService(serviceName);
    }

    public static DataObjectMetaDataService getDataObjectMetaDataService() {
        return (DataObjectMetaDataService) getService(DATA_OBJECT_METADATA_SERVICE);
    }

    public static ViewService getViewService() {
        return (ViewService) getService(VIEW_SERVICE);
    }

    public static ViewDictionaryService getViewDictionaryService() {
        return (ViewDictionaryService) getService(VIEW_DICTIONARY_SERVICE);
    }

    public static ViewValidationService getViewValidationService() {
        return (ViewValidationService) getService(VIEW_VALIDATION_SERVICE);
    }

    public static AttributeQueryService getAttributeQueryService() {
        return (AttributeQueryService) getService(ATTRIBUTE_QUERY_SERVICE);
    }

    public static DataDictionaryRemoteFieldService getDataDictionaryRemoteFieldService() {
        return (DataDictionaryRemoteFieldService) getService(DATA_DICTIONARY_REMOTE_FIELD_SERVICE);
    }

    public static MessageService getMessageService() {
        return getService(MESSAGE_SERVICE);
    }

    public static DataDictionaryComponentPublisherService getDataDictionaryComponentPublisherService() {
        return getService(DATA_DICTIONARY_COMPONENT_PUBLISHER_SERVICE);
    }

    public static PostProcessorService getPostProcessorService() {
        return (PostProcessorService) getService(POST_PROCESSOR_SERVICE);
    }

    public static InactivationBlockingDisplayService getInactivationBlockingDisplayService() {
    	return (InactivationBlockingDisplayService) getService(INACTIVATION_BLOCKING_DISPLAY_SERVICE);
    }
}
