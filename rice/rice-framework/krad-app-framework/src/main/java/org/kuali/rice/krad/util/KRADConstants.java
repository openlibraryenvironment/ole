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
package org.kuali.rice.krad.util;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.core.api.CoreConstants;

/**
 * Defines Global Constants for the KRAD Module
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class KRADConstants {

    private KRADConstants() {
        throw new UnsupportedOperationException("do not call");
    }

    public static final String MODULE_NAME = "krad";

    public static final String KR_MODULE_NAME = "kr";

    public static final String DEFAULT_ENCODING = "UTF-8";

    // special user used in the post-processor
    public static final String SYSTEM_USER = "kr";

    public static final String KRAD_URL_KEY = "krad.url";
    public static final String KRAD_INQUIRY_URL_KEY = "krad.inquiry.url";
    public static final String KRAD_LOOKUP_URL_KEY = "krad.lookup.url";
    public static final String KRAD_SERVER_LOOKUP_URL_KEY = "rice.server.krad.lookup.url";
    public static final String KRAD_INITIATED_DOCUMENT_URL_KEY="initiated.document.url";
    public static final String KRAD_INITIATED_DOCUMENT_VIEW_NAME="InitiatedDocumentView";

    public static final String KRAD_DICTIONARY_INDEX_POOL_SIZE = "krad.dictionary.indexPoolSize";

    public static final String PARAM_MAINTENANCE_VIEW_MODE = "maintenanceViewMode";
    public static final String PARAM_MAINTENANCE_VIEW_MODE_MAINTENANCE = "maintenance";
    public static final String PARAM_MAINTENANCE_VIEW_MODE_LOOKUP = "lookup";
    public static final String PARAM_MAINTENANCE_VIEW_MODE_INQUIRY = "inquiry";

    public static final String KNS_NAMESPACE = "KR-NS";
    public static final String KRAD_NAMESPACE = "KR-KRAD";
    public static final String KUALI_RICE_SYSTEM_NAMESPACE = "KR-SYS";
    public static final String KUALI_RICE_WORKFLOW_NAMESPACE = "KR-WKFLW";
    public static final String KUALI_RICE_SERVICE_BUS_NAMESPACE = "KR-BUS";

    public static final String KUALI_ACTION_CAN_EDIT = "canEdit";
    public static final String KUALI_ACTION_CAN_ANNOTATE = "canAnnotate";
    public static final String KUALI_ACTION_CAN_CLOSE = "canClose";
    public static final String KUALI_ACTION_CAN_SAVE = "canSave";
    public static final String KUALI_ACTION_CAN_ROUTE = "canRoute";
    public static final String KUALI_ACTION_CAN_CANCEL = "canCancel";
    public static final String KUALI_ACTION_CAN_RECALL = "canRecall";
    public static final String KUALI_ACTION_CAN_RELOAD = "canReload";
    public static final String KUALI_ACTION_CAN_COPY = "canCopy";
    public static final String KUALI_ACTION_PERFORM_ROUTE_REPORT = "canPerformRouteReport";
    public static final String KUALI_ACTION_CAN_AD_HOC_ROUTE = "canAdHocRoute";
    public static final String KUALI_ACTION_CAN_BLANKET_APPROVE = "canBlanketApprove";
    public static final String KUALI_ACTION_CAN_ACKNOWLEDGE = "canAcknowledge";
    public static final String KUALI_ACTION_CAN_FYI = "canFYI";
    public static final String KUALI_ACTION_CAN_APPROVE = "canApprove";
    public static final String KUALI_ACTION_CAN_DISAPPROVE = "canDisapprove";
    public static final String KUALI_ACTION_CAN_CREATE = "canCreate";
    public static final String KUALI_ACTION_CAN_SEND_ADHOC_REQUESTS = "canSendAdHocRequests";
    public static final String KUALI_ACTION_CAN_ADD_ADHOC_REQUESTS = "canAddAdHocRequests";
    public static final String KUALI_ACTION_CAN_SEND_NOTE_FYI = "canSendNoteFyi";
    public static final String KUALI_ACTION_CAN_EDIT_DOCUMENT_OVERVIEW = "canEditDocumentOverview";
    public static final String KUALI_ACTION_CAN_EXPORT = "canExport";
    public static final String KUALI_DEFAULT_TRUE_VALUE = "true";
    public static final String USE_CACHE_ADMINISTRATION_SCREEN = "Use Cache Adminstration Screen";
    public static final String KUALI_ACTION_CAN_COMPLETE = "canComplete";

    public static final class DetailTypes {
        public static final String NA_PARM_DETAIL_TYPE = "N/A";
        public static final String ALL_DETAIL_TYPE = "All";
        public static final String LOOKUP_PARM_DETAIL_TYPE = "Lookup";
        public static final String UNIVERSAL_USER_DETAIL_TYPE = "Person";
        public static final String KUALI_MODULE_USER_DETAIL_TYPE = "KualiModuleUser";
        public static final String DOCUMENT_DETAIL_TYPE = "Document";
        public static final String DOCUMENT_TYPE_DETAIL_TYPE = "DocumentType";
        public static final String RULE_DETAIL_TYPE = "Rule";
        public static final String ACTION_LIST_DETAIL_TYPE = "ActionList";
        public static final String BACKDOOR_DETAIL_TYPE = "Backdoor";
        public static final String DOCUMENT_SEARCH_DETAIL_TYPE = "DocumentSearch";
        public static final String EDOC_LITE_DETAIL_TYPE = "EDocLite";
        public static final String FEATURE_DETAIL_TYPE = "Feature";
        public static final String GLOBAL_REVIEWER_DETAIL_TYPE = "GlobalReviewer";
        public static final String MAILER_DETAIL_TYPE = "Mailer";
        public static final String NOTE_DETAIL_TYPE = "Note";
        public static final String QUICK_LINK_DETAIL_TYPE = "QuickLink";
        public static final String ROUTE_QUEUE_DETAIL_TYPE = "RouteQueue";
        public static final String ROUTE_DETAIL_TYPE = "Route";
        public static final String RULE_SERVICE_DETAIL_TYPE = "RuleBaseValues";
        public static final String RULE_TEMPLATE_DETAIL_TYPE = "RuleTemplate";
        public static final String WORKGROUP_DETAIL_TYPE = "Notification";

        private DetailTypes() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    public static final class ParameterNames {
        public static final String DEFAULT_LOCALE_CODE = "DEFAULT_LOCALE_CODE";
    }

    public static final class DocumentFormHeaderFieldIds {
        public static final String DOCUMENT_NUMBER = "documentNumber";
        public static final String DOCUMENT_WORKFLOW_STATUS = "documentStatus";
        public static final String DOCUMENT_INITIATOR = "documentInitiator";
        public static final String DOCUMENT_CREATE_DATE = "documentCreateDate";
        public static final String DOCUMENT_TEMPLATE_NUMBER = "documentTemplateNumber";

        private DocumentFormHeaderFieldIds() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    public static final String MAX_UPLOAD_SIZE_PARM_NM = "MAX_FILE_SIZE_DEFAULT_UPLOAD";

    public static final String UPLOADED_FILE_REQUEST_ATTRIBUTE_KEY = "org.kuali.rice.krad.util.WebUtils.uploadedFiles";

    public static final String NOTE_WORKFLOW_NOTIFICATION_REQUEST_LABEL = "READ NOTES";

    public static final String SEND_NOTE_WORKFLOW_NOTIFICATION_ACTIONS_PARM_NM =
            "SEND_NOTE_WORKFLOW_NOTIFICATION_ACTIONS";
    public static final String ATTACHMENT_MAX_FILE_SIZE_PARM_NM = "MAX_FILE_SIZE_ATTACHMENT";
    public static final String DOCUMENT_HTTP_SESSION_KEY = "documentHttpSessionKey";

    //    public static final String CONFIGURATION_FILE_NAME = "configuration";
    public static final String ENVIRONMENT_KEY = org.kuali.rice.core.api.config.property.Config.ENVIRONMENT;
    public static final String MESSAGE_RESOURCES = "rice.struts.message.resources";
    //    public static final String VERSION_KEY = "version";
    //    public static final String LOG4J_SETTINGS_FILE_KEY = "log4j.settings.file";
    //    public static final String LOGS_DIRECTORY_KEY = "logs.directory";
    //    public static final String LOG4J_RELOAD_MINUTES_KEY = "log4j.reload.minutes";
    //    public static final String STARTUP_STATS_MAILING_LIST_KEY = "startup.stats.mailing.list";
    public static final String APPLICATION_URL_KEY = "application.url";
    public static final String ATTACHMENTS_DIRECTORY_KEY = "attachments.directory";
    public static final String ATTACHMENTS_PENDING_DIRECTORY_KEY = "attachments.pending.directory";
    //    public static final String HTDOCS_LOGS_URL_KEY = "htdocs.logs.url";
    //    public static final String HTDOCS_STAGING_URL_KEY = "htdocs.staging.url";
    public static final String EXTERNALIZABLE_HELP_URL_KEY = "externalizable.help.url";
    public static final String APPLICATION_EXTERNALIZABLE_IMAGES_URL_KEY = "externalizable.images.url";
    public static final String EXTERNALIZABLE_IMAGES_URL_KEY = "kr.externalizable.images.url";
    public static final String ATTACHMENT_IMAGE_PREFIX = "attach.img.";
    public static final String ATTACHMENT_IMAGE_DEFAULT = "attach.img.default";
    //    public static final String REPORTS_DIRECTORY_KEY = "reports.directory";
    public static final String WORKFLOW_URL_KEY = "workflow.url";
    public static final String KUALI_RICE_URL_KEY = "kr.url";
    public static final String PROD_ENVIRONMENT_CODE_KEY =
            org.kuali.rice.core.api.config.property.Config.PROD_ENVIRONMENT_CODE;
    public static final String DOC_HANDLER_ACTION = "DocHandler.do";
    public static final String DOCHANDLER_DO_URL = "/" + DOC_HANDLER_ACTION + "?docId=";
    public static final String DOCHANDLER_URL_CHUNK = "&command=displayDocSearchView";
    public static final String SUPERUSER_ACTION = "SuperUser.do";

    //    public static final String DATABASE_REPOSITORY_FILES_LIST_NAME = "databaseRepositoryFilePaths";
    public static final String SCRIPT_CONFIGURATION_FILES_LIST_NAME = "scriptConfigurationFilePaths";
    //    public static final String JOB_NAMES_LIST_NAME = "jobNames";
    //    public static final String TRIGGER_NAMES_LIST_NAME = "triggerNames";

    public static final String IMAGE_URL_EXPRESSION = "@{#ConfigProperties['krad.externalizable.images.url']}";
    public static final String DETAILS_IMAGE = "details_open.png";

    public static final String ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER = "~";
    public static final String ADD_LINE_METHOD = "addLine";
    public static final String ADD_PREFIX = "add";
    public static final String YES_INDICATOR_VALUE = "Y";
    public static final String NO_INDICATOR_VALUE = "N";
    //    public static final String AMOUNT_PROPERTY_NAME = "amount";
    //    public static final String APPROVE_METHOD = "approve";
    //    public static final String NON_ACTIVE_INDICATOR = "N";
    public static final String BLANK_SPACE = " ";
    public static final String BACK_LOCATION = "backLocation";
    public static final String BACKDOOR_PARAMETER = "backdoorId";
    public static final String LOGOFF_REDIRECT_URL_PARAMETER = "LOGOFF_REDIRECT_URL";
    public static final String LOGOFF_REDIRECT_URL_PROPERTY = "rice.portal.logout.redirectUrl";
    public static final String PORTAL_ALLOWED_REGEX = "rice.portal.allowed.regex";
    //    public static final String BLANKET_APPROVE_METHOD = "blanketApprove";
    public static final String BUSINESS_OBJECT_CLASS_ATTRIBUTE = "businessObjectClassName";
    public static final String DATA_OBJECT_CLASS_ATTRIBUTE = "dataObjectClassName";
    public static final String CALLING_METHOD = "caller";
    /**
     * The {@link org.kuali.rice.kns.question.ConfirmationQuestion} bean
     */
    public static final String CONFIRMATION_QUESTION = "confirmationQuestion";
    /**
     * The {@link org.kuali.rice.kns.question.RecallQuestion} bean
     */
    public static final String RECALL_QUESTION = "recallQuestion";
    public static final String CONVERSION_FIELDS_PARAMETER = "conversionFields";
    public static final String FIELDS_CONVERSION_PARAMETER = "fieldConversions";
    public static final String LOOKUP_READ_ONLY_FIELDS = "readOnlyFields";
    public static final String LOOKUP_AUTO_SEARCH = "autoSearch";
    public static final String SEARCH_METHOD = "search";
    //    public static final String DEFAULT_RETURN_LOCATION = "lookup.do";
    public static final String DELETE_LINE_METHOD = "deleteLine";
    public static final String TOGGLE_INACTIVE_METHOD = "toggleInactiveRecordDisplay";
    public static final String DICTIONARY_BO_NAME = "dictionaryBusinessObjectName";
    public static final String DISPATCH_REQUEST_PARAMETER = "methodToCall";

    /**
     * Constant defined to match with method call in module-locked.jsp which is
     * set to a message that is displayed when the module is locked.
     */
    public static final String MODULE_LOCKED_MESSAGE_REQUEST_PARAMETER = "moduleLockedMessage";

    public static final String CUSTOM_ACTION = "customAction";
    public static final String DOC_FORM_KEY = "docFormKey";
    public static final String FORM_KEY = "formKey";
    public static final String NEW_NOTE_NOTE_TYPE_CODE = "newNote.noteTypeCode";
    public static final String POST_TEXT_AREA_TO_PARENT = "postTextAreaToParent";
    public static final String DOCUMENT_CANCEL_QUESTION = "DocCancel";
    //    public static final String DOCUMENT_DELETE_QUESTION = "DocDelete";
    public static final String DOCUMENT_DISAPPROVE_QUESTION = "DocDisapprove";
    public static final String DOCUMENT_RECALL_QUESTION = "DocRecall";
    public static final String DOCUMENT_SENSITIVE_DATA_QUESTION = "DocSensitiveDataQuestion";
    //    public static final String DOCUMENT_HEADER_ID = "documentHeaderId";
    public static final String DOCUMENT_HEADER_PROPERTY_NAME = "documentHeader";
    public static final String DOCUMENT_SAVE_BEFORE_CLOSE_QUESTION = "DocSaveBeforeClose";
    public static final String EXTRA_BUTTON_SOURCE = "extraButtonSource";
    public static final String EXTRA_BUTTON_PARAMS = "extraButtonParams";
    public static final String ADVANCED_SEARCH_FIELD = "isAdvancedSearch";
    public static final String NEW_AD_HOC_ROUTE_PERSON_PROPERTY_NAME = "newAdHocRoutePerson";
    public static final String NEW_AD_HOC_ROUTE_WORKGROUP_PROPERTY_NAME = "newAdHocRouteWorkgroup";
    public static final String EXISTING_AD_HOC_ROUTE_PERSON_PROPERTY_NAME = "adHocRoutePerson";
    public static final String EXISTING_AD_HOC_ROUTE_WORKGROUP_PROPERTY_NAME = "adHocRouteWorkgroup";
    public static final String DOCUMENT_PROPERTY_NAME = "document";
    public static final String DOCUMENT_TYPE_NAME = "docTypeName";
    public static final String EDIT_PREFIX = "edit";
    public static final String EMPTY_STRING = CoreConstants.EMPTY_STRING;
    public static final String FIELD_CONVERSION_PAIR_SEPARATOR = ":";
    public static final String FIELD_CONVERSIONS_SEPARATOR = ",";
    public static final String REFERENCES_TO_REFRESH_SEPARATOR = ",";
    public static final String RESTRICTED_DATA_MASK = "xxxxxx";
    //    public static final String GENERIC_FIELD_NAME = "Field";
    //    public static final String GENERIC_CODE_PROPERTY_NAME = "code";
    //    public static final String STAND_IN_BUSINESS_OBJECT_FOR_ATTRIBUTES = "AttributeReferenceDummy";
    public static final String OVERRIDE_KEYS = "overrideKeys";
    public static final String COPY_KEYS = "cpKys";

    public static final String KRAD_APPLICATION_DATASOURCE = "kradApplication.datasource";
    public static final String KRAD_APPLICATION_DATASOURCE_JNDI = "kradApplication.datasource.jndi.location";

    // **************** Begin Note & Attachments defines
    /**
     * Property name for notes collection
     */
    public static final String NOTES_PROPERTY_NAME = "notes";
    /**
     * Property name for new document - Value is "newNote"
     */
    public static final String NEW_DOCUMENT_NOTE_PROPERTY_NAME = "newNote";
    /**
     * Property name for note text - Value is "noteText"
     */
    public static final String NOTE_TEXT_PROPERTY_NAME = "noteText";
    /**
     * Property name for note topic text - Value is "noteTopicText"
     */
    public static final String NOTE_TOPIC_TEXT_PROPERTY_NAME = "noteTopicText";
    /**
     * Property name for note attachment - Value is "attachmentFile"
     */
    public static final String NOTE_ATTACHMENT_FILE_PROPERTY_NAME = "attachmentFile";

    /**
     * Property name for enabling attachments to note, overriding the defaults defined by the document template - Value
     * is
     * "enableNoteAttachments"
     * <p>
     * Example: The following line defined in the application properties file
     * <p>
     * enableNoteAttachments=false
     * <p>
     * disables the attachment option in Notes & Attachment of a document
     */
    public static final String NOTE_ATTACHMENT_ENABLED = "enableNoteAttachments";
    // **************** End Note & Attachments defines

    /**
     * This value is the name of the param for the default max column length of a lookup result field if the field
     * maxLength
     * has not been defined in the DD
     */
    public static final String RESULTS_DEFAULT_MAX_COLUMN_LENGTH = "RESULTS_DEFAULT_MAX_COLUMN_LENGTH";

    //    /**
    //     * The number of levels BusinessObjectDictionaryServiceImpl will recurse. If this number is high, it may lead to serious
    //     * performance problems
    //     */
    //    public static final int BUSINESS_OBJECT_DICTIONARY_SERVICE_PERFORM_FORCE_UPPERCASE_RECURSION_MAX_DEPTH = 3;

    /**
     * When checkboxes are rendered on the form, a hidden field will also be rendered corresponding to each checkbox
     * with the
     * checkbox's name suffixed with the value of this constant. No real fields should have names that contain this
     * suffix,
     * since this may lead to undesired results.
     */
    public static final String CHECKBOX_PRESENT_ON_FORM_ANNOTATION = "{CheckboxPresentOnFormAnnotation}";

    public static final int DOCUMENT_ANNOTATION_MAX_LENGTH = 2000;

    public static final String HIDE_LOOKUP_RETURN_LINK = "hideReturnLink";
    public static final String SUPPRESS_ACTIONS = "suppressActions";
    public static final String REFERENCES_TO_REFRESH = "referencesToRefresh";

    public static final String INQUIRABLE_ATTRIBUTE_NAME = "kualiInquirable";
    public static final String INQUIRY_ACTION = "inquiry.do";
    public static final String PORTAL_ACTION = "portal.do";
    public static final String DIRECT_INQUIRY_ACTION = "directInquiry.do";
    public static final String CONTINUE_WITH_INQUIRY_METHOD_TO_CALL = "continueWithInquiry";
    public static final String INQUIRY_PK_VALUE_PASSED_FROM_PREVIOUS_REQUEST_PREFIX = "previousPkValue_";
    public static final String INACTIVE_RECORD_DISPLAY_PARAM_PREFIX = "inactiveRecordDisplay_";

    public static final String FIELD_NAME_TO_FOCUS_ON_AFTER_SUBMIT = "fieldNameToFocusOnAfterSubmit";

    public static final String DEFAULT_PARAMETER_APPLICATION_ID = "KUALI";
    public static final String DEFAULT_NAMESPACE = "KUALI";
    public static final String LOOKUP_ACTION = "lookup.do";
    public static final String MULTIPLE_VALUE_LOOKUP_ACTION = "multipleValueLookup.do";
    public static final String LOOKUP_RESULTS_SEQUENCE_NUMBER = "lookupResultsSequenceNumber";
    public static final String LOOKUP_RESULTS_BO_CLASS_NAME = "lookupResultsBOClassName";
    public static final String LOOKED_UP_COLLECTION_NAME = "lookedUpCollectionName";
    public static final String MULTIPLE_VALUE_LOOKUP_PREVIOUSLY_SELECTED_OBJ_IDS_PARAM = "previouslySelectedObjectIds";
    public static final String MULTIPLE_VALUE_LOOKUP_OBJ_IDS_SEPARATOR = "||";
    public static final String MULTIPLE_VALUE_LOOKUP_DISPLAYED_OBJ_ID_PARAM_PREFIX = "displayedObjId-";
    public static final String MULTIPLE_VALUE_LOOKUP_SELECTED_OBJ_ID_PARAM_PREFIX = "selectedObjId-";
    public static final String LOOKUP_ANCHOR = "lookupAnchor";
    public static final String LOOKUPABLE_IMPL_ATTRIBUTE_NAME = "lookupableImplServiceName";
    public static final String LOOKUP_RESULTS_SEQUENCE = "KRNS_LOOKUP_RSLT_S";
    public static final String KUALI_LOOKUPABLE_IMPL = "kualiLookupable";
    public static final String PARAMETER_DOC_ID = "docId";
    public static final String PARAMETER_COMMAND = "command";
    public static final String ACTION_CLASS = "actionClass";
    public static final String NAMESPACE_CODE = "namespaceCode";
    public static final String COMPONENT_NAME = "componentName";
    //    public static final String LOOKUP_METHOD = "performLookup";
    public static final String LOOKUP_DEFAULT_RANGE_SEARCH_LOWER_BOUND_LABEL = "From";
    public static final String LOOKUP_DEFAULT_RANGE_SEARCH_UPPER_BOUND_LABEL = "To";
    public static final String LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX = "rangeLowerBoundKeyPrefix_";
    public static final String LOOKUP_RANGE_UPPER_BOUND_PROPERTY_PREFIX = "rangeUpperBoundKeyPrefix_";
    public static final String LOOKUP_PARAMETER_LITERAL_PREFIX = "literal";
    public static final String LOOKUP_PARAMETER_LITERAL_DELIMITER = "=";

    public static final String METHOD_DISPLAY_DOC_SEARCH_VIEW = "displayDocSearchView";
    public static final String MAINTENANCE_ACTION = "maintenance.do";
    public static final String MAINTENANCE_ADD_PREFIX = "add.";
    public static final String MAINTENANCE_COPY_ACTION = "Copy";
    public static final String MAINTENANCE_EDIT_ACTION = "Edit";
    public static final String MAINTENANCE_DELETE_ACTION = "Delete";
    public static final String MAINTENANCE_NEW_ACTION = "New";
    public static final String MAINTENANCE_COPY_METHOD_TO_CALL = "copy";
    public static final String MAINTENANCE_EDIT_METHOD_TO_CALL = "edit";
    public static final String MAINTENANCE_DELETE_METHOD_TO_CALL = "delete";
    public static final String MAINTENANCE_NEW_METHOD_TO_CALL = "start";
    public static final String MAINTENANCE_NEWWITHEXISTING_ACTION = "newWithExisting";
    public static final String MAINTENANCE_ACTN = "maintenanceAction";
    public static final String MAINTENANCE_NEW_MAINTAINABLE = "document.newMaintainableObject.";
    public static final String MAINTENANCE_OLD_MAINTAINABLE = "document.oldMaintainableObject.";
    public static final String MAPPING_CANCEL = "cancel";
    public static final String MAPPING_RECALL = "recall";
    public static final String MAPPING_CLOSE = "close";
    public static final String MAPPING_DISAPPROVE = "disapprove";
    //    public static final String MAPPING_DELETE = "delete";
    // Activate the MAPPING_ERROR define for use in Exception incident and handling
    public static final String MAPPING_ERROR = "error";
    public static final String MAPPING_PORTAL = "portal";
    //    public static final String MAPPING_MULTIPLE_VALUE_LOOKUP = "multipleValueLookup";
    public static final String MAPPING_ROUTE_REPORT = "route_report";
    //    public static final String MAXLENGTH_SUFFIX = ".maxLength";
    public static final String METHOD_TO_CALL_ATTRIBUTE = "methodToCallAttribute";
    public static final String METHOD_TO_CALL_PATH = "methodToCallPath";
    public static final String METHOD_TO_CALL_BOPARM_LEFT_DEL = "(!!";
    public static final String METHOD_TO_CALL_BOPARM_RIGHT_DEL = "!!)";
    public static final String METHOD_TO_CALL_PARM1_LEFT_DEL = "(((";
    public static final String METHOD_TO_CALL_PARM1_RIGHT_DEL = ")))";
    public static final String METHOD_TO_CALL_PARM2_LEFT_DEL = "((`";
    public static final String METHOD_TO_CALL_PARM2_RIGHT_DEL = "`))";
    public static final String METHOD_TO_CALL_PARM3_LEFT_DEL = "((<";
    public static final String METHOD_TO_CALL_PARM3_RIGHT_DEL = ">))";
    public static final String METHOD_TO_CALL_PARM4_LEFT_DEL = "(([";
    public static final String METHOD_TO_CALL_PARM4_RIGHT_DEL = "]))";
    public static final String METHOD_TO_CALL_PARM5_LEFT_DEL = "((*";
    public static final String METHOD_TO_CALL_PARM5_RIGHT_DEL = "*))";
    public static final String METHOD_TO_CALL_PARM6_LEFT_DEL = "((%";
    public static final String METHOD_TO_CALL_PARM6_RIGHT_DEL = "%))";
    public static final String METHOD_TO_CALL_PARM7_LEFT_DEL = "((^";
    public static final String METHOD_TO_CALL_PARM7_RIGHT_DEL = "^))";
    public static final String METHOD_TO_CALL_PARM8_LEFT_DEL = "((&";
    public static final String METHOD_TO_CALL_PARM8_RIGHT_DEL = "&))";
    public static final String METHOD_TO_CALL_PARM9_LEFT_DEL = "((~";
    public static final String METHOD_TO_CALL_PARM9_RIGHT_DEL = "~))";
    public static final String METHOD_TO_CALL_PARM10_LEFT_DEL = "((/";
    public static final String METHOD_TO_CALL_PARM10_RIGHT_DEL = "/))";
    public static final String METHOD_TO_CALL_PARM11_LEFT_DEL = "(:;";
    public static final String METHOD_TO_CALL_PARM11_RIGHT_DEL = ";:)";
    public static final String METHOD_TO_CALL_PARM12_LEFT_DEL = "(::;";
    public static final String METHOD_TO_CALL_PARM12_RIGHT_DEL = ";::)";
    // below 2 fields used in rowDisplay.tag file
    public static final String METHOD_TO_CALL_PARM13_LEFT_DEL = "(:::;";
    public static final String METHOD_TO_CALL_PARM13_RIGHT_DEL = ";:::)";
    // used for baseLookupURL
    public static final String METHOD_TO_CALL_PARM14_LEFT_DEL = "(::::;";
    public static final String METHOD_TO_CALL_PARM14_RIGHT_DEL = ";::::)";
    // if more strings needed, then add more colons to the PARM11 strings above, e.g. (::; (:::;, etc.

    // Pessimistic Locking Constants
    public static final String SESSION_TIMEOUT_WARNING_MESSAGE_TIME_PARM_NM = "SESSION_TIMEOUT_WARNING_MESSAGE_TIME";
    public static final String SESSION_TIMEOUT_WARNING_MILLISECONDS = "SESSION_TIMEOUT_WARNING_MILLISECONDS";
    public static final String SESSION_TIMEOUT_WARNING_MINUTES = "SESSION_TIMEOUT_WARNING_MINUTES";

    public static final String ANCHOR = "anchor";
    public static final String ANCHOR_TOP_OF_FORM = "topOfForm";
    public static final String QUESTION_ANCHOR = "questionAnchor";
    public static final String NOT_AVAILABLE_STRING = "N/A";
    public static final String QUESTION_ACTION = "questionPrompt.do";
    public static final String QUESTION_CLICKED_BUTTON = "buttonClicked";
    public static final String QUESTION_ERROR_KEY = "questionErrorKey";
    public static final String QUESTION_ERROR_PROPERTY_NAME = "questionErrorPropertyName";
    public static final String QUESTION_ERROR_PARAMETER = "questionErrorParameter";
    public static final String QUESTION_IMPL_ATTRIBUTE_NAME = "questionType";
    public static final String QUESTION_INST_ATTRIBUTE_NAME = "questionIndex";
    public static final String QUESTION_PAGE_TITLE = "Question Dialog Page";
    public static final String QUESTION_REFRESH = "QuestionRefresh";
    public static final String QUESTION_CONTEXT = "context";
    public static final String QUESTION_TEXT_ATTRIBUTE_NAME = "questionText";
    public static final String QUESTION_REASON_ATTRIBUTE_NAME = "reason";
    public static final String QUESTION_SHOW_REASON_FIELD = "showReasonField";

    public static final String REFRESH_CALLER = "refreshCaller";
    public static final String REFRESH_CALLER_TYPE = "refreshCallerType";
    public static final String REFRESH_DATA_OBJECT_CLASS = "refreshDataObjectClass";
    public static final String REFRESH_MAPPING_PREFIX = "/Refresh";

    // below field used in tag files
    public static final String REQUIRED_FIELD_SYMBOL = "*";
    public static final String RETURN_LOCATION_PARAMETER = "returnLocation";
    public static final String RETURN_METHOD_TO_CALL = "refresh";
    // Another possible value for the "refreshCaller" request parameter, as a fix for KULRICE-2903.
    public static final String TEXT_AREA_REFRESH = "TextAreaRefresh";
    // below field used in tag files

    // KualiDocumentActionBase Method Names
    public static final String ROUTE_METHOD = "route";
    public static final String SAVE_METHOD = "save";
    public static final String APPROVE_METHOD = "approve";
    public static final String BLANKET_APPROVE_METHOD = "blanketApprove";
    public static final String DOC_HANDLER_METHOD = "docHandler";
    public static final String CLOSE_METHOD = "close";
    public static final String CANCEL_METHOD = "cancel";
    public static final String LOAD_DOCUMENT_METHOD = "loadDocument";
    public static final String CREATE_DOCUMENT_METHOD = "createDocument";
    public static final String DISAPPROVE_METHOD = "disapprove";
    public static final String FYI_METHOD = "fyi";
    public static final String ACKNOWLEDGE_METHOD = "acknowledge";

    public static final String DOWNLOAD_BO_ATTACHMENT_METHOD = "downloadBOAttachment";
    public static final String DOWNLOAD_CUSTOM_BO_ATTACHMENT_METHOD = "downloadCustomBOAttachment";
    public static final String NOTE_IDENTIFIER = "noteIdentifier";
    public static final String BO_ATTACHMENT_FILE_NAME = "fileName";
    public static final String BO_ATTACHMENT_FILE_CONTENT_TYPE = "contentType";
    public static final String BO_ATTACHMENT_FILE_CONTENT_FIELD = "fileContentBOField";

    // specialized method 'delete' used only for PessimisticLocks currently
    public static final String DELETE_METHOD = "delete";
    public static final String START_METHOD = "start";
    public static final String USER_SESSION_KEY = "UserSession";
    public static final String KUALI_SESSION_ID = "kualiSessionId";
    public static final String EXITING_DOCUMENT = "exitingDocument";
    public static final String DOCUMENT_DOCUMENT_NUMBER = "document.documentNumber";
    public static final String DOC_NUM = "docNum";

    public static final String SEARCH_LIST_KEY_PREFIX = "searchResults";
    public static final String SEARCH_LIST_REQUEST_KEY = "searchResultKey";

    public static final String METHOD_DISPLAY_ALL_INACTIVATION_BLOCKERS = "displayAllInactivationBlockers";
    public static final String DISPLAY_ALL_INACTIVATION_BLOCKERS_ACTION = "inactivationBlockers.do";

    public static final String GLOBAL_ERRORS = "GLOBAL_ERRORS";
    // TODO: fix this constant to be GLOBAL_MESSAGES once KNS is removed
    public static final String GLOBAL_MESSAGES = "GlobalMessages";
    public static final String GLOBAL_INFO = "GLOBAL_INFO";
    public static final String PESSIMISTIC_LOCK_MESSAGES = "DocumentPessimisticLockMessages";
    public static final String AD_HOC_ROUTE_PERSON_ERRORS = "newAdHocRoutePerson*,adHocRoutePerson*";
    public static final String AD_HOC_ROUTE_WORKGROUP_ERRORS = "newAdHocRouteWorkgroup*,adHocRouteWorkgroup*";
    public static final String AD_HOC_ROUTE_ERRORS = AD_HOC_ROUTE_PERSON_ERRORS + "," + AD_HOC_ROUTE_WORKGROUP_ERRORS;
    public static final String DOCUMENT_DOCUMENT_ERRORS = "document.document*";
    public static final String DOCUMENT_EXPLANATION_ERRORS = "document.explanation*";
    public static final String DOCUMENT_REVERSAL_ERRORS = "document.reversal*";
    public static final String DOCUMENT_SELECTED_ERRORS = "document.selected*";
    public static final String DOCUMENT_HEADER_ERRORS = "document.header*";
    //    public static final String DOCUMENT_ERRORS_LESS_DOCUMENT = DOCUMENT_EXPLANATION_ERRORS + "," + DOCUMENT_REVERSAL_ERRORS + "," + DOCUMENT_SELECTED_ERRORS + "," + DOCUMENT_HEADER_ERRORS;
    public static final String DOCUMENT_ERRORS = DOCUMENT_DOCUMENT_ERRORS +
            "," +
            DOCUMENT_EXPLANATION_ERRORS +
            "," +
            DOCUMENT_REVERSAL_ERRORS +
            "," +
            DOCUMENT_SELECTED_ERRORS +
            "," +
            DOCUMENT_HEADER_ERRORS;
    // below field used in tag files
    public static final String DOCUMENT_NOTES_ERRORS = NEW_DOCUMENT_NOTE_PROPERTY_NAME + "*";

    // export formats

    public static final String XML_FORMAT = "xml";
    public static final String EXCEL_FORMAT = "xls";
    public static final String CSV_FORMAT = "csv";

    public static final String XML_MIME_TYPE = "application/xml";

    // Header Tab navigation constant values
    public static final String NAVIGATE_TO = "navigateTo.";
    public static final String HEADER_DISPATCH = "headerDispatch.";

    public static final String MULTIPLE_VALUE = "multipleValues";

    // Agency type codes
    //    public static final String AGENCY_TYPE_CODE_FEDERAL = "F";

    // special chars that I don't know how to put into string literals in JSP expression language
    // below field used in tag files
    public static final String NEWLINE = "\n";

    // websession
    public static final String DOCUMENT_WEB_SCOPE = "documentWebScope";
    public static final String SESSION_SCOPE = "session";

    public static final class SystemGroupParameterNames {
        public static final String CHECK_ENCRYPTION_SERVICE_OVERRIDE_IND = "CHECK_ENCRYPTION_SERVICE_OVERRIDE_IND";

        public static final String LOOKUP_RESULTS_LIMIT = "RESULTS_LIMIT";
        public static final String MULTIPLE_VALUE_LOOKUP_RESULTS_PER_PAGE = "MULTIPLE_VALUE_RESULTS_PER_PAGE";
        //        public static final String MULTIPLE_VALUE_LOOKUP_RESULTS_EXPIRATION_AGE = "MULTIPLE_VALUE_RESULTS_EXPIRATION_SECONDS";

        public static final String DEFAULT_CAN_PERFORM_ROUTE_REPORT_IND = "DEFAULT_CAN_PERFORM_ROUTE_REPORT_IND";
        public static final String ALLOW_ENROUTE_BLANKET_APPROVE_WITHOUT_APPROVAL_REQUEST_IND =
                "ALLOW_ENROUTE_BLANKET_APPROVE_WITHOUT_APPROVAL_REQUEST_IND";
        /**
         * Used to indicate whether field level help is enabled. Depending on the namespace this parameter is in, it
         * may
         * affect either lookups (i.e. the search criteria fields) or maintenance documents.
         */
        public static final String ENABLE_FIELD_LEVEL_HELP_IND = "ENABLE_FIELD_LEVEL_HELP_IND";

        //        /**
        //         * Used by PurgePendingAttachmentsJob to compute the maximum amount of time a pending attachment is allowed to
        //         * persist on the file system before being deleted.
        //         */
        //        public static final String PURGE_PENDING_ATTACHMENTS_STEP_MAX_AGE = "purgePendingAttachmentsStepMaxAge";

        public static final String ENABLE_DIRECT_INQUIRIES_IND = "ENABLE_DIRECT_INQUIRIES_IND";
        public static final String DEFAULT_COUNTRY = "DEFAULT_COUNTRY";

        public static final String SENSITIVE_DATA_PATTERNS = "SENSITIVE_DATA_PATTERNS";
        public static final String SENSITIVE_DATA_PATTERNS_WARNING_IND = "SENSITIVE_DATA_PATTERNS_WARNING_IND";

        public static final String OLTP_LOCKOUT_ACTIVE_IND = "OLTP_LOCKOUT_ACTIVE_IND";
        public static final String OLTP_LOCKOUT_MESSAGE_PARM = "OLTP_LOCKOUT_MESSAGE";
        public static final String OLTP_LOCKOUT_DEFAULT_MESSAGE = "OLTP_LOCKOUT_DEFAULT_MESSAGE";

        private SystemGroupParameterNames() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    public static final int DEFAULT_NUM_OF_COLUMNS = 1;

    public static final class TableRenderConstants {
        public static final String SWITCH_TO_PAGE_METHOD = "switchToPage";
        public static final String SORT_METHOD = "sort";
        public static final String SELECT_ALL_METHOD = "selectAll";
        public static final String UNSELECT_ALL_METHOD = "unselectAll";

        // below field used on displayMultipleValueLookupResults.tag
        public static final String PREVIOUSLY_SORTED_COLUMN_INDEX_PARAM = "previouslySortedColumnIndex";
        public static final String VIEWED_PAGE_NUMBER = "viewedPageNumber";

        private TableRenderConstants() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    public static final String TAB_STATES = "tabStates";

    public static final List<String> ALWAYS_VALID_PARAMETER_PREFIXES = new ArrayList<String>();

    static {
        ALWAYS_VALID_PARAMETER_PREFIXES.add(TAB_STATES);
        ALWAYS_VALID_PARAMETER_PREFIXES.add(DISPATCH_REQUEST_PARAMETER + ".hideAllTabs");
        ALWAYS_VALID_PARAMETER_PREFIXES.add(DISPATCH_REQUEST_PARAMETER + ".showAllTabs");
        ALWAYS_VALID_PARAMETER_PREFIXES.add(DISPATCH_REQUEST_PARAMETER + ".toggleTab");
        ALWAYS_VALID_PARAMETER_PREFIXES.add(
                DISPATCH_REQUEST_PARAMETER + "." + TableRenderConstants.SWITCH_TO_PAGE_METHOD);
        ALWAYS_VALID_PARAMETER_PREFIXES.add(DISPATCH_REQUEST_PARAMETER + "." + TableRenderConstants.SORT_METHOD);
    }

    public static final String GLOBAL_VARIABLES_MESSAGES_LIST_ACTION_MESSAGES = "GlobalVariablesMessagesList";

    public static final class Config {
        public static final String APPLY_ILLEGAL_BUSINESS_OBJECT_FOR_SAVE_CHECK =
                "rice.krad.illegalBusinessObjectsForSave.applyCheck";
        public static final String ILLEGAL_BUSINESS_OBJECTS_FOR_SAVE = "rice.krad.illegalBusinessObjectsForSave";
        public static final String COMPONENT_PUBLISHING_ENABLED = "rice.krad.componentPublishing.enabled";
        public static final String COMPONENT_PUBLISHING_DELAY = "rice.krad.componentPublishing.delay";
        public static final String IGNORE_MISSIONG_FIELDS_ON_DESERIALIZE = "rice.krad.bos.ignoreMissingFieldsOnDeserialize";

        private Config() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    public static final String ENABLE_NONPRODUCTION_UNMASKING = "enable.nonproduction.data.unmasking";

    public static final String SINGLE_QUOTE = "'";

    public static final String SENSITIVE_DATA_QUESTION_SESSION_TICKET = "SENSITIVE_DATA_QUESTION_SESSION_TICKET";
    public static final String EDITABLE_PROPERTIES_HISTORY_HOLDER_ATTR_NAME = "EditablePropertiesHistoryHolder";

    // TODO: new krad constants, previous needs to be cleaned up
    public static final String DATA_TYPE_STRING = CoreConstants.DATA_TYPE_STRING;
    public static final String DATA_TYPE_DATE = CoreConstants.DATA_TYPE_DATE;
    public static final String DATA_TYPE_LONG = CoreConstants.DATA_TYPE_LONG;
    public static final String DATA_TYPE_FLOAT = CoreConstants.DATA_TYPE_FLOAT;
    public static final String DATA_TYPE_BOOLEAN = CoreConstants.DATA_TYPE_BOOLEAN;

    public static final String ACTIONS_COLUMN_TITLE = "Actions";

    public static final class Maintenance {
        public static final String REQUEST_MAPPING_MAINTENANCE = "maintenance";
        public static final String METHOD_TO_CALL_NEW = "start";
        public static final String METHOD_TO_CALL_NEW_WITH_EXISTING = "maintenanceNewWithExisting";
        public static final String METHOD_TO_CALL_EDIT = "maintenanceEdit";
        public static final String METHOD_TO_CALL_COPY = "maintenanceCopy";
        public static final String METHOD_TO_CALL_DELETE = "maintenanceDelete";
        public static final String LOCK_AFTER_CLASS_DELIM = "!!";
        public static final String LOCK_AFTER_FIELDNAME_DELIM = "^^";
        public static final String LOCK_AFTER_VALUE_DELIM = "::";
    }

    public static class Lookup {
        public static final String TITLE_RETURN_URL_PREPENDTEXT_PROPERTY = "title.return.url.value.prependtext";
        public static final String TITLE_ACTION_URL_PREPENDTEXT_PROPERTY = "title.action.url.value.prependtext";
    }

    public static final class MessageParsing {
        public static final String LEFT_TOKEN = "[";
        public static final String RIGHT_TOKEN = "]";
        public static final String RIGHT_TOKEN_MARKER = "$@$";
        public static final String RIGHT_TOKEN_PLACEHOLDER = RIGHT_TOKEN_MARKER + RIGHT_TOKEN;
        public static final String LEFT_BRACKET = "&#91;";
        public static final String RIGHT_BRACKET = "&#93;";
        public static final String INLINE_COMP_CLASS = "inlineBlock";
        public static final String COMPONENT_BY_ID = "id";
        public static final String COLOR = "color";
        public static final String CSS_CLASSES = "css";
        public static final String ACTION_LINK = "action";
        public static final String ACTION_DATA = "data";
        public static final String LINK = "link";
        public static final String[] UNALLOWED_HTML =
                {"script", "link", "iframe", "html", "head", "body", "object", "form", "frame", "frameset", "!DOCTYPE"};
    }

    public static final String DICTIONARY_BEAN_PARENT_SUFFIX = "-parentBean";
    public static final String EXPRESSION_MESSAGE_PLACEHOLDER_PREFIX = "#msg(";
    public static final String EXPRESSION_MESSAGE_PLACEHOLDER_SUFFIX = ")";
    public static final String MESSAGE_KEY_PLACEHOLDER_PREFIX = "@msg{";
    public static final String MESSAGE_KEY_PLACEHOLDER_SUFFIX = "}";
    public static final String MESSAGE_KEY_PATH_INDICATOR = "#";

    public static final String QUOTE_PLACEHOLDER = "@quot@";

    public static final String REQUEST_MAPPING_SESSION_TIMEOUT = "sessionTimout";
    public static final String SESSION_TIMEOUT_VIEW_ID = "Uif-SessionTimeoutView";

    public static final class ConfigParameters {
        public static final String APPLICATION_VERSION = "application.version";
        public static final String APPLICATION_URL = "application.url";
        public static final String KRAD_DEV_MODE = "rice.krad.dev.mode";
    }
}
