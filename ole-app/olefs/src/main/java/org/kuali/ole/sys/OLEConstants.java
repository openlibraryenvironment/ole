/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.sys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.gl.businessobject.OriginEntryFull;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * This class is used to define global constants.
 */
public class OLEConstants {
    private static final long serialVersionUID = 2882277719647128949L;
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEConstants.class);
    public static final String APPLICATION_NAMESPACE_CODE = "OLE";

    public static final String KFS_CORE_SERVICE_NAMESPACE = "http://kfs.kuali.org/core/v5_0";

    public static final String KFS_CORE_DISTRIBUTED_CACHE_MANAGER = "kfs.core.DistributedCacheManager";
    public static Map<String, Boolean>     PO_NOTE_MAP                             = new HashMap<String, Boolean>();
    public static final String MULTIPLE_ITEM_LOC = "Multiple";
    public static final int BIG_DECIMAL_SCALE = 2;
    @Deprecated
    public static class ParameterNamespaces {
        public static final String OLE = "OLE-SYS";
        public static final String CHART = "OLE-COA";
        public static final String FINANCIAL = "OLE-FP";
        public static final String GL = "OLE-GL";
        public static final String PDP = "OLE-PDP";
        public static final String KNS = KRADConstants.KNS_NAMESPACE;
    }

    public static class CoreModuleNamespaces {
        public static final String OLE = "OLE-SYS";
        public static final String CHART = "OLE-COA";
        public static final String FINANCIAL = "OLE-FP";
        public static final String GL = "OLE-GL";
        public static final String VENDOR = "OLE-VND";
        public static final String PDP = "OLE-PDP";
        public static final String KNS = KRADConstants.KNS_NAMESPACE;
        public static final String ACCESS_SECURITY = "OLE-SEC";
        public static final String SELECT = "OLE-SELECT";
    }

    // for ease of reference, a list of the optional modules delivered with and supported by the KFS project
    public static final class OptionalModuleNamespaces {
        public static final String PURCHASING_ACCOUNTS_PAYABLE = "OLE-PURAP";
    }

    public static class DocumentTypeAttributes {
        public static final String INDICATOR_ATTRIBUTE_TRUE_VALUE = "Y";

        public static final String TRANSACTION_SCRUBBER_OFFSET_INDICATOR_ATTRIBUTE_KEY = "TRANSACTION_SCRUBBER_OFFSET_GENERATION";
        public static final String ACCOUNTING_DOCUMENT_TYPE_NAME = "OLE_ACCT";
    }

    // special user used in the post-processor
    //TODO: eventually this should be removed and the system user should be pulled from a parameter
    // right now, you HAVE to make sure that a 'ole' user exists in your system, either in the DB
    // or in an external system such as LDAP if implemented
    public static final String SYSTEM_USER = "SYSTEM_USER";

    public static final String BATCH_FAILURE_ATCH_NAME_BIB="Failure Bib File";
    public static final String BATCH_FAILURE_ATCH_NAME_EDI="Failure Edi File";
    public static final String BATCH_FAILURE_EXTRA_ATCH_NAME_BIB="Extra Bib Records File";
    public static final String BATCH_FAILURE_EXTRA_ATCH_NAME_EDI="Extra Edi Records File";
    public static final String BATCH_FAILURE_BIB_FILE_ETN="_bib_err.xml";
    public static final String BATCH_FAILURE_EDI_FILE_ETN="_edi_err.xml";
    public static final String BATCH_FAILURE_FILE_MRK="_err.mrk";
    public static final String BATCH_REQ_ID_FILE="_reqId.txt";
    public static final String BATCH_FAILURE_CONTENT_TYPE="text/xml";
    public static final String ACCOUNT_MAINTENANCE_DOCUMENT_TYPE_DD_KEY = "AccountMaintenanceDocument";
    public static final String ENVIRONMENT_KEY = "environment";
    public static final String VERSION_KEY = "version";
    public static final String LOG4J_SETTINGS_FILE_KEY = "log4j.settings.file";
    public static final String LOGS_DIRECTORY_KEY = "logs.directory";
    public static final String LOG4J_RELOAD_MINUTES_KEY = "log4j.reload.minutes";
    public static final String LOG4J_OVERRIDE_KEY = "ole.fs.log4j.override";
    public static final String APPLICATION_URL_KEY = "application.url";
    public static final String ATTACHMENTS_DIRECTORY_KEY = "attachments.directory";
    public static final String ATTACHMENTS_PENDING_DIRECTORY_KEY = "attachments.pending.directory";
    public static final String STAGING_DIRECTORY_KEY = "staging.directory";
    public static final String TEMP_DIRECTORY_KEY = "temp.directory";
    public static final String EXTERNALIZABLE_HELP_URL_KEY = "externalizable.help.url";
    public static final String EXTERNALIZABLE_IMAGES_URL_KEY = "externalizable.images.url";
    public static final String EXTERNALIZABLE_XML_URL_KEY = "externalizable.xml.url";
    public static final String RICE_EXTERNALIZABLE_IMAGES_URL_KEY = "kr.externalizable.images.url";
    public static final String REPORTS_DIRECTORY_KEY = "reports.directory";
    public static final String WORKFLOW_URL_KEY = "workflow.url";
    public static final String PROD_ENVIRONMENT_CODE_KEY = "production.environment.code";
    public static final String USE_STANDALONE_WORKFLOW = "rice.use.standalone.workflow";
    public static final String BATCH_FILE_LOOKUP_ROOT_DIRECTORIES = "batch.file.lookup.root.directories";
    public static final String BATCH_UPLOAD_HELP_SYS_PARAM_NAME = "BATCH_UPLOAD_HELP_URL";
    public static final String BATCH_ISBN_DUPLICATE_FOUND="ISBN_DUP_FOUND";
    public static final String BATCH_VNO_DUPLICATE_FOUND="VEN_NO_DUP_FOUND";
    public static final String BATCH_LOAD_FAILD_FOUND="LOAD_FAILD_FOUND";
    public static final String BATCH_TITLE_FAILD_FOUND="TITLE_NOT_FOUND";
    public static final String BATCH_VNO_NOT_FOUND="VEN_NO_NOT_FOUND";
    public static final String BAD_BFN_NO_FOUND="BAD_BFN_NUMBER_FOUND";
    public static final String BAD_CONTROLL_LINE_FOUND="BAD_CONTROL_LINE_FOUND";
    public static final String APO_RULE_FAILED="APO_RULE_FAILED";
    public static final String BATCH_FAILURE_ATCH_NAME="Failure Mrk File";
    public static final String DOWNLOAD_CUSTM_BO_ATTACHMENT_METHOD="downloadCustomBOAttachment";
    //public static final String LICENSE_WEB_SERVICE_URL="license.web.service.url";
    public static final String   EXCEPTION="EXCEPTION";
    public static final String   FINAL="FINAL";
    public static final String   REQ_IDENTIFIER="requisitionIdentifier";
    public static final String   PUR_DOC_IDENTIFIER="purapDocumentIdentifier";
    public static final String   LOCATION_WEB_SERVICE_URL                                                       = "location.web.service.url";
    public static final String   PATRON_WEB_SERVICE_URL                                                         = "patron.web.service.url";
    public static final String   PATRON_SERVICE_CLASSNAME                                                       = "org.kuali.ole.select.document.service.OlePatronWebService";
    public static final String   PATRON_SERVICE_NAME                                                            = "olePatronWebService";

    public static final String DATABASE_REPOSITORY_FILES_LIST_NAME = "databaseRepositoryFilePaths";
    public static final String JOB_NAMES_LIST_NAME = "jobNames";
    public static final String TRIGGER_NAMES_LIST_NAME = "triggerNames";

    public static final String LOOKUP_RESULTS_LIMIT_URL_KEY = "RESULTS_LIMIT";
    public static final String DOCHANDLER_DO_URL = "/DocHandler.do?docId=";
    public static final String DOCHANDLER_URL_CHUNK = "&command=displayDocSearchView";

    public static final String ACCOUNT_NUMBER_PROPERTY_NAME = "accountNumber";
    public static final String MODULE_ID_PROPERTY_NAME = "moduleId";
    public static final String MODULE_CODE_PROPERTY_NAME = "moduleCode";
    public static final String ACCOUNT_STATUS_CLOSED = "Y";
    public static final String ACCOUNTING_PERIOD_ACTIVE_INDICATOR_FIELD = "active";
    public static final String ACTION_FORM_UTIL_MAP_METHOD_PARM_DELIMITER = "~";
    public static final String ADD_LINE_METHOD = "addLine";
    public static final String ADD_PREFIX = "add";
    public static final String ACTIVE_INDICATOR = "Y";
    public static final String AGGREGATE_ENCUMBRANCE_BALANCE_TYPE_CODE = "EN";
    public static final String AMOUNT_PROPERTY_NAME = "amount";
    public static final String APPROVE_METHOD = "approve";
    public static final String NON_ACTIVE_INDICATOR = "N";
    public static final String BLANK_SPACE = " ";
    public static final String BACK_LOCATION = "backLocation";
    public static final String BACKDOOR_PARAMETER = "backdoorId";
    public static final String BALANCE_INQUIRY_REPORT_MENU_ACTION = "balanceInquiryReportMenu.do";
    public static final String ASSET_INQUIRY_MENU_ACTION = "camsAssetPayment.do";
    public static final String BALANCE_TYPE_PROPERTY_NAME = "balanceTypeCode";
    public static final String BALANCE_TYPE_CURRENT_BUDGET = "CB";
    public static final String BALANCE_TYPE_BASE_BUDGET = "BB";
    public static final String BALANCE_TYPE_MONTHLY_BUDGET = "MB";
    public static final String BALANCE_TYPE_EXTERNAL_ENCUMBRANCE = "EX";
    public static final String BALANCE_TYPE_INTERNAL_ENCUMBRANCE = "IE";
    public static final String BALANCE_TYPE_COST_SHARE_ENCUMBRANCE = "CE";
    public static final String BALANCE_TYPE_ACTUAL = "AC";
    public static final String BALANCE_TYPE_AUDIT_TRAIL = "NB";
    public static final String BALANCE_TYPE_PRE_ENCUMBRANCE = "PE";
    public static final String BLANKET_APPROVE_METHOD = "blanketApprove";
    public static final String BUSINESS_OBJECT_CLASS_ATTRIBUTE = "businessObjectClassName";
    public static final String CALLING_METHOD = "caller";
    public static final String CASH_MANAGEMENT_DOCUMENT_ACTION = "financialCashManagement.do";
    public static final String CHANGE_JOURNAL_VOUCHER_BALANCE_TYPE_METHOD = "changeBalanceType";
    public static final String CHART_OF_ACCOUNTS_CODE_PROPERTY_NAME = "chartOfAccountsCode";
    public static final String CONFIRMATION_QUESTION = "confirmationQuestion";
    public static final String CONSOLIDATED_SUBACCOUNT = "*ALL*";
    public static final String CONVERSION_FIELDS_PARAMETER = "conversionFields";
    public static final String LOOKUP_READ_ONLY_FIELDS = "readOnlyFields";
    public static final String LOOKUP_AUTO_SEARCH = "autoSearch";
    public static final String OBJECT_SUB_TYPES_DIFFERENT_QUESTION = "Object Subtypes different Question";

    public static final String CREDIT_AMOUNT_PROPERTY_NAME = "newSourceLineCredit";
    public static final String DEBIT_AMOUNT_PROPERTY_NAME = "newSourceLineDebit";
    public static final String DELETE_LINE_METHOD = "deleteLine";
    public static final String DICTIONARY_BO_NAME = "dictionaryBusinessObjectName";
    public static final String DISBURSEMENT_VOUCHER_PDP_EXTRACT_FILE_NAME = "extr_fr_disb_voucher";
    public static final String DISENCUMBRANCE = "Disencumbrance";
    public static final String DISPATCH_REQUEST_PARAMETER = "methodToCall";
    public static final String DOC_FORM_KEY = "docFormKey";
    public static final String FORM_KEY = "formKey";
    public static final String BALANCE_INQUIRY_REPORT_MENU_CALLER_DOC_FORM_KEY = "balanceInquiryReportMenuCallerDocFormKey";
    public static final String ASSET_INQUIRY_CALLER_DOC_FORM_KEY = "assetInquiryCallerDocFormKey";
    public static final String DOCUMENT_CANCEL_QUESTION = "DocCancel";
    public static final String DOCUMENT_DELETE_QUESTION = "DocDelete";
    public static final String DOCUMENT_DISAPPROVE_QUESTION = "DocDisapprove";
    public static final String DOCUMENT_HEADER_ID = "documentHeaderId";
    public static final String DOCUMENT_HEADER_DOCUMENT_STATUS_CODE_PROPERTY_NAME = "financialDocumentStatusCode";
    public static final String NOTE_TEXT_PROPERTY_NAME = "noteText";
    public static final String DOCUMENT_HEADER_PROPERTY_NAME = "documentHeader";
    public static final String DOCUMENT_SAVE_BEFORE_CLOSE_QUESTION = "DocSaveBeforeClose";
    public static final String EMPLOYEE_ACTIVE_STATUS = "A";
    public static final String EXISTING_SOURCE_ACCT_LINE_PROPERTY_NAME = "sourceAccountingLine";
    public static final String EXISTING_TARGET_ACCT_LINE_PROPERTY_NAME = "targetAccountingLine";
    public static final String SOURCE_ACCT_LINE_TYPE_CODE = "F"; // F = From, the label for this on most documents
    public static final String TARGET_ACCT_LINE_TYPE_CODE = "T"; // T = To, the label for this on most documents
    public static final String EXTRA_BUTTON_SOURCE = "extraButtonSource";
    public static final String EXTRA_BUTTON_PARAMS = "extraButtonParams";
    public static final String NEW_DOCUMENT_NOTE_PROPERTY_NAME = "newDocumentNote";
    public static final String NEW_AD_HOC_ROUTE_PERSON_PROPERTY_NAME = "newAdHocRoutePerson";
    public static final String NEW_AD_HOC_ROUTE_WORKGROUP_PROPERTY_NAME = "newAdHocRouteWorkgroup";
    public static final String EXISTING_AD_HOC_ROUTE_PERSON_PROPERTY_NAME = "adHocRoutePerson";
    public static final String EXISTING_AD_HOC_ROUTE_WORKGROUP_PROPERTY_NAME = "adHocRouteWorkgroup";
    public static final String NEW_SOURCE_ACCT_LINE_PROPERTY_NAME = "newSourceLine";
    public static final String NEW_TARGET_ACCT_LINES_PROPERTY_NAME = "newTargetLines";
    public static final String NEW_TARGET_ACCT_LINE_PROPERTY_NAME = "newTargetLine";
    public static final String DOCUMENT_PROPERTY_NAME = "document";
    public static final String DOCUMENT_TYPE_NAME = "docTypeName";
    public static final String EDIT_PREFIX = "edit";
    public static final String DASH = "-";
    public static final String EMPTY_STRING = "";
    public static final String ENCUMBRANCE = "Encumbrance";
    public static final String EXPENSE = "Expense";
    public static final String FIELD_CONVERSION_PAIR_SEPERATOR = ":";
    public static final String FIELD_CONVERSIONS_SEPERATOR = ",";
    public static final String REFERENCES_TO_REFRESH_SEPARATOR = ",";
    public static final String FIELD_CONVERSION_PREFIX_PARAMETER = "fieldConversionPrefix";
    public static final String FINANCIAL_OBJECT_CODE_PROPERTY_NAME = "financialObjectCode";
    public static final String FINANCIAL_OBJECT_LEVEL_CODE_PROPERTY_NAME = "financialObjectLevelCode";
    public static final String FINANCIAL_SUB_OBJECT_CODE_PROPERTY_NAME = "financialSubObjectCode";
    public static final String FISCAL_CHART_NAME = "fiscalChartOfAccountsCode";
    public static final String FISCAL_ORG_NAME = "fiscalOrganizationCode";
    public static final String FROM = "From";
    public static final String GENERIC_FIELD_NAME = "Field";
    public static final String GENERIC_CODE_PROPERTY_NAME = "code";
    public static final String GL_BALANCE_INQUIRY_FLAG = "inquiryFlag";
    public static final String GL_ACCOUNT_BALANCE_BY_CONSOLIDATION_LOOKUP_ACTION = "glAccountBalanceByConsolidationLookup.do";
    public static final String AR_CUSTOMER_AGING_REPORT_LOOKUP_ACTION = "arCustomerAgingReportLookup.do";
    public static final String GL_BALANCE_INQUIRY_ACTION = "glBalanceInquiry.do";
    public static final String GL_MODIFIED_INQUIRY_ACTION = "glModifiedInquiry.do";
    public static final String GL_PE_OFFSET_STRING = "TP Generated Offset";
    public static final String SUB_OBJECT_CODE_PROPERTY_NAME = "subObjectCode";
    public static final String SUB_ACCOUNT_PROPERTY_NAME = "subAccount";
    public static final String ACCOUNT_PROPERTY_NAME = "account";
    public static final String CHART_PROPERTY_NAME = "chart";
    public static final String PROJECT_PROPERTY_NAME = "project";
    public static final String REF_ORIGIN_PROPERTY_NAME = "referenceOrigin";
    public static final String UNIVERSITY_FISCAL_YEAR_PROPERTY_NAME = "universityFiscalYear";
    public static final String UNIVERSITY_FISCAL_PERIOD_CODE_PROPERTY_NAME = "universityFiscalPeriodCode";
    public static final String FINANCIAL_BALANCE_TYPE_CODE_PROPERTY_NAME = "financialBalanceTypeCode";
    public static final String ACCOUNT_SUFFICIENT_FUNDS_CODE_PROPERTY_NAME = "accountSufficientFundsCode";
    public static final String CURRENT_BUDGET_BALANCE_AMOUNT_PROPERTY_NAME = "currentBudgetBalanceAmount";
    public static final String ACCOUNT_ENCUMBRANCE_AMOUNT_PROPERTY_NAME = "accountEncumbranceAmount";
    public static final String TRANSACTION_DEBIT_CREDIT_CODE = "transactionDebitCreditCode";
    public static final String TRANSACTION_LEDGER_ENTRY_AMOUNT = "transactionLedgerEntryAmount";
    public static final String ACCOUNT_SUFFICIENT_FUNDS_FINANCIAL_OBJECT_CODE_PROPERTY_NAME = "acctSufficientFundsFinObjCd";
    public static final String FINANCIAL_OBJECT_TYPE_CODE = "financialObjectTypeCode";
    public static final String FINANCIAL_DOCUMENT_TYPE_CODE = "financialDocumentTypeCode";
    public static final String ORGANIZATION_CODE_PROPERTY_NAME = "organizationCode";
    public static final String ORIGIN_CODE_KUALI = "01";
    public static final String TRANSFER_FUNDS = "TF";
    public static final String[] ENCUMBRANCE_BALANCE_TYPE = new String[] { BALANCE_TYPE_EXTERNAL_ENCUMBRANCE, BALANCE_TYPE_INTERNAL_ENCUMBRANCE, BALANCE_TYPE_PRE_ENCUMBRANCE };
    public static final String STAND_IN_BUSINESS_OBJECT_FOR_ATTRIBUTES = "GenericAttributes";
    public static final String EMPLOYEE_FUNDING_INQUIRY_ACTION = "employeeFundingInquiry.do";
    public static final String OVERRIDE_KEYS = "overrideKeys";
    public static final String PERCENTAGE_SIGN = "%";
    public static final String RICE_PATH_PREFIX = "kr/";
    public static final String USE_CONTINUATION_BANK_QUESTION = "UseContinuationBankQuestion";
    public static final String NEW_WINDOW_URL_TARGET = "_blank";
    public static final String AND = "and";
    public static final String COMMA = ",";
    public static final String PERFORMANCE_BALANCE_INQUIRY_FOR_METHOD = "performBalanceInquiryFor";
    public static final String INSERT_METHOD = "insert";
    public static final String SOURCE_ACCOUNTING_LINES_GROUP_NAME = "source";
    public static final String TARGET_ACCOUNTING_LINES_GROUP_NAME = "target";
    public static final String SUB_ACCOUNT_EDIT_CG_ICR_SECTION_ID = "Edit CG ICR";
    public static final String INITIATE_LICENSE_REQUEST = "ILR";
    public static final String OLE_WEB_SERVICE_PROVIDER = "oleWebServiceProvider";
    public static final String OLE_NMSPC="OLE-SYS";
    public static final String OLE_CMPNT="OLE";
    public static final String APPL_ID="KUALI";

    public static final String SCRIPT_CONFIGURATION_FILES_LIST_NAME = "scriptConfigurationFilePaths";
    /**
     * This value denotes that a max length has not been defined for a given lookup results field
     */
    public static final int LOOKUP_RESULT_FIELD_MAX_LENGTH_NOT_DEFINED = -1;

    /**
     * The number of levels BusinessObjectDictionaryServiceImpl will recurse. If this number is high, it may lead to serious
     * performance problems
     */
    public static final int BUSINESS_OBJECT_DICTIONARY_SERVICE_PERFORM_FORCE_UPPERCASE_RECURSION_MAX_DEPTH = 3;


    /**
     * When checkboxes are rendered on the form, a hidden field will also be rendered corresponding to each checkbox with the
     * checkbox's name suffixed with the value of this constant. No real fields should have names that contain this suffix, since
     * this may lead to undesired results.
     */
    public static final String CHECKBOX_PRESENT_ON_FORM_ANNOTATION = "{CheckboxPresentOnFormAnnotation}";

    public static class OrgReversion {
        public static final String VALID_PREFIX = "EXTENDED_DEFINITIONS_INCLUDE_";
        public static final String INVALID_PREFIX = "EXTENDED_DEFINITIONS_EXCLUDE_";
        public static final String OBJECT_CONSOL_PARAM_SUFFIX = "OBJECT_CONSOLIDATIONS_BY_ORGANIZATION_REVERSION_CATEGORY";
        public static final String OBJECT_LEVEL_PARAM_SUFFIX = "OBJECT_LEVELS_BY_ORGANIZATION_REVERSION_CATEGORY";
        public static final String OBJECT_TYPE_PARAM_SUFFIX = "OBJECT_TYPES_BY_ORGANIZATION_REVERSION_CATEGORY";
        public static final String OBJECT_SUB_TYPE_PARAM_SUFFIX = "OBJECT_SUB_TYPES_BY_ORGANIZATION_REVERSION_CATEGORY";
        public static final String IS_EXPENSE_PARAM = "EXTENDED_DEFINITIONS_EXPENSE_CATEGORIES";
    }

    // CR doc properties
    public static final String NEW_CHECK_PROPERTY_NAME = "newCheck";
    public static final String EXISTING_CHECK_PROPERTY_NAME = "check";

    public static final int DOCUMENT_ANNOTATION_MAX_LENGTH = 2000;

    // TRN_LDGR_DEBIT_CRDT_CD valid values
    public static final String GL_DEBIT_CODE = "D";
    public static final String GL_CREDIT_CODE = "C";
    public static final String GL_BUDGET_CODE = " ";

    // TRN_ENCUM_UPDT_CD value values
    public static final String ENCUMB_UPDT_DOCUMENT_CD = "D";
    public static final String ENCUMB_UPDT_REFERENCE_DOCUMENT_CD = "R";
    public static final String ENCUMB_UPDT_NO_ENCUMBRANCE_CD = "N";

    // GL Reversal Generated Entry Description Prefix
    public static final String GL_REVERSAL_DESCRIPTION_PREFIX = "AUTO REVERSAL-";

    // Misc GL text.
    public static final String PLANT_INDEBTEDNESS_ENTRY_DESCRIPTION = "GENERATED TRANSFER TO NET PLANT";

    // Sufficient Funds Type Codes
    public static final String SF_TYPE_NO_CHECKING = "N";
    public static final String SF_TYPE_OBJECT = "O";
    public static final String SF_TYPE_LEVEL = "L";
    public static final String SF_TYPE_CONSOLIDATION = "C";
    public static final String SF_TYPE_CASH_AT_ACCOUNT = "H";
    public static final String SF_TYPE_ACCOUNT = "A";
    public static final String NOTIFICATION_APPROVER = "ole-butt";

    public static final String GRANT = "Grant";
    public static final String HIDE_LOOKUP_RETURN_LINK = "hideReturnLink";
    public static final String SUPPRESS_ACTIONS = "suppressActions";
    public static final String REFERENCES_TO_REFRESH = "referencesToRefresh";
    public static final String CHECK_IN_OPERATION                                      = "checkIn";
    public static final String INCOME = "Income";
    public static final String INITIAL_KUALI_DOCUMENT_STATUS_CD = "?";
    public static final String INSERT_SOURCE_LINE_METHOD = "insertSourceLine";
    public static final String INSERT_TARGET_LINE_METHOD = "insertTargetLine";
    public static final String ICR = "Receipt";
    public static final String PROJECT_CODE_PROPERTY_NAME = "projectCode";

    public static final String INQUIRY_ACTION = "kr/inquiry.do";
    public static final String JOURNAL_VOUCHER_CHANGE_BALANCE_TYPE_QUESTION = "JournalVoucherChangeBalanceTypeQuestion";
    public static final String JOURNAL_VOUCHER_ROUTE_OUT_OF_BALANCE_DOCUMENT_QUESTION = "JournalVoucherRouteOutOfBalanceDocumentQuestion";
    public static final String JOURNAL_LINE_HELPER_PROPERTY_NAME = "journalLineHelper";
    public static final String AUXILIARY_LINE_HELPER_PROPERTY_NAME = "auxiliaryLineHelper";
    public static final String VOUCHER_LINE_HELPER_CREDIT_PROPERTY_NAME = ".credit";
    public static final String VOUCHER_LINE_HELPER_DEBIT_PROPERTY_NAME = ".debit";
    public static final String LOOKUP_ACTION = "kr/lookup.do";
    public static final String LOOKUP_RESULTS_SEQUENCE_NUMBER = "lookupResultsSequenceNumber";
    public static final String LOOKUP_RESULTS_BO_CLASS_NAME = "lookupResultsBOClassName";
    public static final String LOOKED_UP_COLLECTION_NAME = "lookedUpCollectionName";
    public static final String MULTIPLE_VALUE_LOOKUP_PREVIOUSLY_SELECTED_OBJ_IDS_PARAM = "previouslySelectedObjectIds";
    public static final String MULTIPLE_VALUE_LOOKUP_DISPLAYED_OBJ_ID_PARAM_PREFIX = "displayedObjId-";
    public static final String MULTIPLE_VALUE_LOOKUP_SELECTED_OBJ_ID_PARAM_PREFIX = "selectedObjId-";
    public static final String LOOKUP_ANCHOR = "lookupAnchor";
    public static final String LOOKUPABLE_IMPL_ATTRIBUTE_NAME = "lookupableImplServiceName";
    public static final String LOOKUPABLE_SUFFIX = "Lookupable";
    public static final String KUALI_LOOKUPABLE_IMPL = "kualiLookupable";
    public static final String KUALI_DISBURSEMENT_PAYEE_LOOKUPABLE_IMPL = "disbursementPayeeLookupable";
    public static final String KUALI_VENDOR_ADDRESS_LOOKUPABLE_IMPL = "vendorAddressLookupable";
    public static final String DOC_HANDLER_ACTION = "DocHandler.do";
    public static final String DOC_HANDLER_METHOD = "docHandler";
    public static final String DOC_HANDLER_URL ="/kew/DocHandler.do?command=displayDocSearchView&docId";
    public static final String PARAMETER_DOC_ID = "docId";
    public static final String PARAMETER_COMMAND = "command";
    public static final String METHOD_DISPLAY_DOC_SEARCH_VIEW = "displayDocSearchView";
    public static final String MAINTENANCE_ACTION = "maintenance.do";
    public static final String MAINTENANCE_ADD_PREFIX = "add.";
    public static final String MAINTENANCE_COPY_ACTION = "Copy";
    public static final String MAINTENANCE_EDIT_ACTION = "Edit";
    public static final String MAINTENANCE_NEW_ACTION = "New";
    public static final String MAINTENANCE_COPY_METHOD_TO_CALL = "copy";
    public static final String MAINTENANCE_EDIT_METHOD_TO_CALL = "edit";
    public static final String MAINTENANCE_NEW_METHOD_TO_CALL = "start";
    public static final String MAINTENANCE_NEWWITHEXISTING_ACTION = "newWithExisting";
    public static final String MAINTENANCE_NEW_MAINTAINABLE = "document.newMaintainableObject.";
    public static final String MAINTENANCE_OLD_MAINTAINABLE = "document.oldMaintainableObject.";
    public static final String MAPPING_BASIC = "basic";
    public static final String MAPPING_VIEW = "view";
    public static final String MAPPING_CANCEL = "cancel";
    public static final String MAPPING_CLOSE = "close";
    public static final String MAPPING_ERROR = "error";
    public static final String MAPPING_PORTAL = "portal";
    public static final String MAPPING_BALANCE_INQUIRY_REPORT_MENU = "balanceInquiryReportMenu";
    public static final String MAPPING_DV_PER_DIEM_LINKS = "dvPerDiemLinks";
    public static final String METHOD_TO_CALL_ATTRIBUTE = "methodToCallAttribute";
    public static final String METHOD_TO_CALL_PATH = "methodToCallPath";
    public static final String METHOD_TO_CALL_BOPARM_LEFT_DEL = "(!!";
    public static final String METHOD_TO_CALL_BOPARM_RIGHT_DEL = "!!)";
    public static final String METHOD_TO_CALL_PARM1_LEFT_DEL = "(((";
    public static final String METHOD_TO_CALL_PARM1_RIGHT_DEL = ")))";
    public static final String METHOD_TO_CALL_PARM2_LEFT_DEL = "((#";
    public static final String METHOD_TO_CALL_PARM2_RIGHT_DEL = "#))";
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
    public static final String METHOD_TO_CALL_PARM13_LEFT_DEL = "(:::;";
    public static final String METHOD_TO_CALL_PARM13_RIGHT_DEL = ";:::)";
    // if more strings needed, then add more colons to the PARM11 strings above, e.g. (::; (:::;, etc.

    public static final String ACTION_EXTENSION_DOT_DO = ".do";
    public static final String ANCHOR = "anchor";
    public static final String ANCHOR_TOP_OF_FORM = "topOfForm";
    public static final String NOT_AVAILABLE_STRING = "N/A";
    public static final int NEGATIVE_ONE = -1;
    @Deprecated
    public static final String OBJECT_TYPE_CODE_PROPERTY_NAME = OLEPropertyConstants.OBJECT_TYPE_CODE;
    public static final String QUESTION_CLICKED_BUTTON = "buttonClicked";
    public static final String QUESTION_INST_ATTRIBUTE_NAME = "questionIndex";
    public static final String QUESTION_REFRESH = "QuestionRefresh";
    public static final String QUESTION_CONTEXT = "context";
    public static final String QUESTION_REASON_ATTRIBUTE_NAME = "reason";
    public static final String RELOAD_METHOD_TO_CALL = "reload";
    public static final String REFRESH_CALLER = "refreshCaller";
    public static final String REQUIRED_FIELD_SYMBOL = "*";
    public static final String RETURN_LOCATION_PARAMETER = "returnLocation";
    public static final String RETURN_METHOD_TO_CALL = "refresh";
    public static final String ROUTE_METHOD = "route";
    public static final String SAVE_METHOD = "save";
    public static final String START_METHOD = "start";
    public static final String SEARCH_METHOD = "search";
    public static final String COPY_METHOD = "copy";
    public static final String ERRORCORRECT_METHOD = "correct";
    public static final String SOURCE = "Source";
    public static final String SQUARE_BRACKET_LEFT = "[";
    public static final String SQUARE_BRACKET_RIGHT = "]";
    @Deprecated
    public static final String SUB_ACCOUNT_NUMBER_PROPERTY_NAME = OLEPropertyConstants.SUB_ACCOUNT_NUMBER;
    public static final String TARGET = "Target";
    public static final String TO = "To";
    public static final String USER_SESSION_KEY = "UserSession";
    public static final String VERSION_NUMBER = "versionNumber";

    public static final String SEARCH_LIST_REQUEST_KEY = "searchResultKey";

    public static final int CORRECTION_RECENT_GROUPS_DAY = 10;

    public static final String GLOBAL_ERRORS = "GLOBAL_ERRORS";
    public static final String GLOBAL_MESSAGES = "GlobalMessages";
    public static final String DOCUMENT_DOCUMENT_ERRORS = "document.document*";
    public static final String DOCUMENT_EXPLANATION_ERRORS = "document.explanation*";
    public static final String DOCUMENT_REVERSAL_ERRORS = "document.reversal*";
    public static final String DOCUMENT_SELECTED_ERRORS = "document.selected*";
    public static final String DOCUMENT_HEADER_ERRORS = "document.header*";
    public static final String DOCUMENT_ERRORS_LESS_DOCUMENT = DOCUMENT_EXPLANATION_ERRORS + "," + DOCUMENT_REVERSAL_ERRORS + "," + DOCUMENT_SELECTED_ERRORS + "," + DOCUMENT_HEADER_ERRORS;
    public static final String DOCUMENT_ERRORS = DOCUMENT_DOCUMENT_ERRORS + "," + DOCUMENT_EXPLANATION_ERRORS + "," + DOCUMENT_REVERSAL_ERRORS + "," + DOCUMENT_SELECTED_ERRORS + "," + DOCUMENT_HEADER_ERRORS;
    public static final String DOCUMENT_NOTES_ERRORS = "newDocumentNote*";

    public enum NoteTypeEnum {
        BUSINESS_OBJECT_NOTE_TYPE("BO", "documentBusinessObject"), DOCUMENT_HEADER_NOTE_TYPE("DH", "documentHeader");
        private String noteTypeCode;
        private String noteTypePath;

        private NoteTypeEnum(String noteTypeCode, String noteTypePath) {
            this.noteTypeCode = noteTypeCode;
            this.noteTypePath = noteTypePath;
        }

        public String getCode() {
            return this.noteTypeCode;
        }

        public String getPath() {
            return this.noteTypePath;
        }

        public String getFullPath() {
            return OLEConstants.DOCUMENT_PROPERTY_NAME + "." + getPath();
        }
    }

    public static final String EDIT_JOURNAL_VOUCHER_ERRORS = "EditJournalVoucherErrors";
    public static final String EDIT_AUXILIARY_VOUCHER_ERRORS = "EditAuxiliaryVoucherErrors";
    public static final String EDIT_PRE_ENCUMBRANCE_ERRORS = "EditPreEncumbranceErrors";

    public static final String ACCOUNTING_LINE_ERRORS = "document.accountingLines";
    public static final String OHQ_REQUISITION_STATUS                    = "OLE_REQS";
    //cannot use SOURCE_ACCOUNTING_LINE_ERROR_PATTERN due to doubled error displayed in checking already added source accounting line
    public static final String NEW_SOURCE_LINE_ERRORS = "newSourceLine*";
    public static final String SOURCE_ACCOUNTING_LINE_ERROR_PATTERN = "document.sourceAccounting*,sourceAccountingLines,newSourceLine*,journalLineHelper*,auxiliaryLineHelper*";
    public static final String TARGET_ACCOUNTING_LINE_ERROR_PATTERN = "document.targetAccounting*,targetAccountingLines,newTargetLine*";
    public static final String ACCOUNTING_LINE_GROUP_SUFFIX = "s";
    public static final String SOURCE_ACCOUNTING_LINE_ERRORS = EXISTING_SOURCE_ACCT_LINE_PROPERTY_NAME + ACCOUNTING_LINE_GROUP_SUFFIX;
    public static final String TARGET_ACCOUNTING_LINE_ERRORS = EXISTING_TARGET_ACCT_LINE_PROPERTY_NAME + ACCOUNTING_LINE_GROUP_SUFFIX;
    public static final String ITEM_LINE_ERRORS = "newItem*,document.item*";

    public static final String CREDIT_CARD_RECEIPTS_LINE_ERRORS = "newCreditCardReceipt*,document.creditCardReceipt*";
    public static final String ADVANCE_DEPOSITS_LINE_ERRORS = "newAdvanceDeposit*,document.advanceDeposit*";
    public static final String GENERAL_LEDGER_PENDING_ENTRIES_TAB_ERRORS = "document.generalLedgerPendingEntr*";

    public static final String WILDCARD_CHARACTER = "*";
    public static final String WILDCARD_NOT_ALLOWED_ON_FIELD = "error.fieldDoNotAllowWildcard";

    // disbursement voucher error fields
    public static final String DV_PAYEE_TAB_ERRORS = "DVPayeeErrors,document.dvPayeeDetail.disbVchrPayeeIdNumber,document.dvPayeeDetail.disbVchrPayeeCityName,document.dvPayeeDetail.disbVchrPayeePersonName," + "document.dvPayeeDetail.disbVchrPayeeStateCode,document.dvPayeeDetail.disbVchrPayeeLine1Addr,document.dvPayeeDetail.disbVchrPayeeZipCode,document.dvPayeeDetail.disbVchrPayeeLine2Addr,document.dvPayeeDetail.disbVchrPayeeCountryCode,document.dvPayeeDetail.disbursementVoucherPayeeTypeCode,";
    public static final String DV_PAYEE_INIT_TAB_ERRORS = "DVPayeeErrors,payeeIdNumber,payeePersonName,payeeTypeCode";
    public static final String DV_PAYMENT_TAB_ERRORS = "DVPaymentErrors,document.dvPayeeDetail.disbVchrPaymentReasonCode,document.disbVchrCheckTotalAmount,document.disbursementVoucherDueDate,document.dvPayeeDetail.disbVchrAlienPaymentCode," + "document.dvPayeeDetail.disbVchrPayeeEmployeeCode,document.disbVchrAttachmentCode,document.disbVchrSpecialHandlingCode,document.disbVchrPayeeW9CompleteCode" + "document.disbVchrPaymentMethodCode,document.disbursementVoucherDocumentationLocationCode,document.disbVchrCheckStubText";
    public static final String DV_NRATAX_TAB_ERRORS = "DVNRATaxErrors,document.dvNonResidentAlienTax.incomeClassCode,document.dvNonResidentAlienTax.incomeTaxTreatyExemptCode,document.dvNonResidentAlienTax.federalIncomeTaxPercent," + "document.dvNonResidentAlienTax.foreignSourceIncomeCode,document.dvNonResidentAlienTax.stateIncomeTaxPercent,document.dvNonResidentAlienTax.incomeTaxGrossUpCode,document.dvNonResidentAlienTax.postalCountryCode," + "document.dvNonResidentAlienTax.referenceFinancialDocumentNumber";
    public static final String DV_FOREIGNDRAFTS_TAB_ERRORS = "DVForeignDraftErrors,document.dvWireTransfer.disbursementVoucherForeignCurrencyTypeCode,document.dvWireTransfer.disbursementVoucherForeignCurrencyTypeName";
    public static final String DV_CONTACT_TAB_ERRORS = "DVContactErrors,document.disbVchrContact*";
    public static final String DV_SPECHAND_TAB_ERRORS = "DVSpecialHandlingErrors,document.dvPayeeDetail.disbVchrSpecialHandlingPersonName,document.dvPayeeDetail.disbVchrSpecialHandlingCityName,document.dvPayeeDetail.disbVchrSpecialHandlingLine1Addr,document.dvPayeeDetail.disbVchrSpecialHandlingStateCode," + "document.dvPayeeDetail.disbVchrSpecialHandlingLine2Addr,document.dvPayeeDetail.disbVchrSpecialHandlingZipCode,document.dvPayeeDetail.disbVchrSpecialHandlingCountryName";
    public static final String DV_WIRETRANSFER_TAB_ERRORS = "DVWireTransfersErrors,document.dvWireTransfer.disbursementVoucherBankName,document.dvWireTransfer.disbVchrBankRoutingNumber,document.dvWireTransfer.disbVchrBankCityName,document.dvWireTransfer.disbVchrBankStateCode," + "document.dvWireTransfer.disbVchrBankCountryCode,document.dvWireTransfer.disbVchrAttentionLineText,document.dvWireTransfer.disbVchrAdditionalWireText,document.dvWireTransfer.disbVchrPayeeAccountNumber,document.dvWireTransfer.disbVchrCurrencyTypeName,document.dvWireTransfer.disbVchrCurrencyTypeCode," + "document.dvWireTransfer.disbursementVoucherWireTransferFeeWaiverIndicator,document.dvWireTransfer.disbursementVoucherPayeeAccountName,document.dvWireTransfer.disbursementVoucherPayeeAccountTypeCode,document.dvWireTransfer.disbursementVoucherAutomatedClearingHouseProfileNumber";
    public static final String DV_NON_EMPL_TRAVEL_TAB_ERRORS = "DVNonEmployeeTravelErrors,newPrePaidNonEmployeeExpenseLine.*,newNonEmployeeExpenseLine.*,document.dvNonEmployeeTravel.*";
    public static final String DV_PREPAID_TAB_ERRORS = "DVPrePaidTravelErrors,newPreConferenceRegistrantLine.*,document.dvPreConferenceDetail.*";
    public static final String GENERAL_PAYMENT_TAB_ERRORS = "DVPaymentErrors";
    public static final String GENERAL_NRATAX_TAB_ERRORS = "DVNRATaxErrors";
    public static final String GENERAL_SPECHAND_TAB_ERRORS = "DVSpecialHandlingErrors";
    public static final String GENERAL_PREPAID_TAB_ERRORS = "DVPrePaidTravelErrors";
    public static final String GENERAL_NONEMPLOYEE_TAB_ERRORS = "DVNonEmployeeTravelErrors,document.dvNonEmployeeTravel.totalTravelAmount";
    public static final String DV_CHECK_TRAVEL_TOTAL_ERROR = "document.dvNonEmployeeTravel.totalTravelAmount";

    // country
    public static final String COUNTRY_CODE_UNITED_STATES = "US";

    // CashManagement tab errors
    public static final String CASH_MANAGEMENT_DEPOSIT_ERRORS = "document.deposit*";

    // Coin and Currency Amounts
    public static class CoinTypeAmounts {
        public static final KualiDecimal HUNDRED_CENT_AMOUNT = new KualiDecimal(1.0);
        public static final KualiDecimal FIFTY_CENT_AMOUNT = new KualiDecimal(0.5);
        public static final KualiDecimal TWENTY_FIVE_CENT_AMOUNT = new KualiDecimal(0.25);
        public static final KualiDecimal TEN_CENT_AMOUNT = new KualiDecimal(0.1);
        public static final KualiDecimal FIVE_CENT_AMOUNT = new KualiDecimal(0.05);
        public static final KualiDecimal ONE_CENT_AMOUNT = new KualiDecimal(0.01);
    }

    public static class CurrencyTypeAmounts {
        public static final KualiDecimal HUNDRED_DOLLAR_AMOUNT = new KualiDecimal(100.0);
        public static final KualiDecimal FIFTY_DOLLAR_AMOUNT = new KualiDecimal(50.0);
        public static final KualiDecimal TWENTY_DOLLAR_AMOUNT = new KualiDecimal(20.0);
        public static final KualiDecimal TEN_DOLLAR_AMOUNT = new KualiDecimal(10.0);
        public static final KualiDecimal FIVE_DOLLAR_AMOUNT = new KualiDecimal(5.0);
        public static final KualiDecimal TWO_DOLLAR_AMOUNT = new KualiDecimal(2.0);
        public static final KualiDecimal ONE_DOLLAR_AMOUNT = new KualiDecimal(1.0);
    }

    // Cashiering source constants
    public static class CurrencyCoinSources {
        public static final String CASH_MANAGEMENT_IN = "R"; // money coming in through cashiering activity
        public static final String DEPOSITS = "D"; // money going out through deposits
        public static final String CASH_RECEIPTS = "C"; // money coming in through cash receipts
        public static final String CASH_MANAGEMENT_OUT = "O"; // money going out through cashiering activity
        public static final String CASH_MANAGEMENT_MASTER = "M"; // an amalgamation of a cashiering transaction
        public static final String CASH_CHANGE_REQUEST = "Q"; // requesting some change money back
        public static final String CASH_CHANGE_GRANTED = "G"; // verified change request
    }

    // Constants for check sources
    // Why are these constants different from the Currency/Coin constants?
    // Why, I ask you in return, is the sky blue? That's right, because of
    // the effect of Rayleigh scattering on atmospheric particles. That's why.
    public static class CheckSources {
        public static final String CASH_RECEIPTS = "C";
        public static final String CASH_MANAGEMENT = "R";
    }

    public static final String CASHIERING_TRANSACTION_OPEN_ITEM_IN_PROCESS_PROPERTY = "document.currentTransaction.openItemInProcess";

    // Tab error patterns must be at the top level; JSPs do not have access to the nested classes.
    public static final String EDIT_CASH_RECEIPT_CASH_RECONCILIATION_ERRORS = "document.totalCashAmount,document.totalCheckAmount,document.totalCoinAmount,document.sumTotalAmount";
    public static final String EDIT_CASH_RECEIPT_CHECK_DETAIL_ERRORS = "newCheck*,document.check*";
    public static final String EDIT_CASH_RECEIPT_CURRENCY_COIN_ERRORS = "document.currencyDetail.*,document.coinDetail.*";
    public static final String EDIT_CASH_MANAGEMENT_CASHIERING_TRANSACTION_ERRORS = "document.currentTransaction.*";
    public static final String EDIT_CAPITAL_ASSET_INFORMATION_ERRORS = "document.capitalAssetInformation*";
    public static final String EDIT_CAPITAL_ASSET_MODIFY_ERRORS = "document.capitalAssetModify*";
    public static final String EDIT_ACCOUNTING_LINES_FOR_CAPITALIZATION_ERRORS = "document.capitalAccountingLines*";

    public static final String MULTIPLE_VALUE = "multipleValues";

    // special chars that I don't know how to put into string literals in JSP expression language
    public static final String NEWLINE = "\n";

    // Workflow constants

    public static final String IS_FAILURE_RECORDS = "IsFailureRecords";

    @Deprecated
    public static final String WORKFLOW_FYI_REQUEST = KewApiConstants.ACTION_REQUEST_FYI_REQ;

    public static class DocumentStatusCodes {
        public static final String INITIATED = "?";
        public static final String CANCELLED = "X";
        public static final String ENROUTE = "R";
        public static final String DISAPPROVED = "D";
        public static final String APPROVED = "A";

        public static class CashReceipt {
            // once a CashReceipt gets approved, its financialDocumentStatus goes to "verified"
            public static final String VERIFIED = "V";

            // when a CashReceipt associated with a Deposit, its financialDocumentStatus changes to "interim" or "final"
            public static final String INTERIM = "I";
            public static final String FINAL = "F";

            // when the CMDoc is finalized, the CRs of its Deposits change to status "approved"
        }
    }

    public static class AuxiliaryVoucher {
        public static final String ADJUSTMENT_DOC_TYPE = "OLE_AVAD";
        public static final String ADJUSTMENT_DOC_TYPE_NAME = "Adjustment";
        public static final String RECODE_DOC_TYPE = "OLE_AVRC";
        public static final String RECODE_DOC_TYPE_NAME = "Recode";
        public static final String ACCRUAL_DOC_TYPE = "OLE_AVAE";
        public static final String ACCRUAL_DOC_TYPE_NAME = "Accrual";
        public static final int ACCRUAL_DOC_DAY_OF_MONTH = 15;
        public static final String ERROR_DOCUMENT_RECODE_DISTRIBUTION_OF_INCOME_AND_EXPENSE_UNSUCCESSFUL = "Unable to auto-generate Distribution of Income and Expense for document with number \"%s.\" Please contact your System Administrator for a Distribution of Income and Expense to be created manually.";
        public static final String ERROR_DOCUMENT_HAS_TARGET_LINES = "AV document doesn't have target accounting lines. This method should have never been entered";
        public static final String RECODE_DISTRIBUTION_OF_INCOME_AND_EXPENSE_DESCRIPTION = "Auto-generated for Auxiliary Voucher";
        public static final String RECODE_DISTRIBUTION_OF_INCOME_AND_EXPENSE_EXPLANATION = "Auxiliary Voucher recode document type was chosen. A Distribution of Income And Expense needs to be routed to FINAL along with it. This Document is routed by Auxiliary Voucher \"%s\".";
        public static final String CHANGE_VOUCHER_TYPE = "changeVoucherType";
    }

    public static class CashDrawerConstants {
        public static final String STATUS_CLOSED = "C";
        public static final String STATUS_OPEN = "O";
        public static final String STATUS_LOCKED = "L";
    }

    public static class CashReceiptConstants {
        public static final String DEFAULT_CASH_RECEIPT_CAMPUS_LOCATION_CODE = "??";

        public static final String CASH_RECEIPT_CAMPUS_LOCATION_CODE_PROPERTY_NAME = "campusLocationCode";
        public static final String CASH_RECEIPT_DOC_HEADER_STATUS_CODE_PROPERTY_NAME = OLEConstants.DOCUMENT_HEADER_PROPERTY_NAME + "." + OLEConstants.DOCUMENT_HEADER_DOCUMENT_STATUS_CODE_PROPERTY_NAME;
    }

    public static class DepositConstants {
        public static final String DEPOSIT_TYPE_VERIFIED = "V";
        public static final String DEPOSIT_TYPE_INTERIM = "I";
        public static final String DEPOSIT_TYPE_FINAL = "F";

        public static final String DEPOSIT_WIZARD_CASHRECEIPT_ERROR = "cashReceiptErrors";
        public static final String DEPOSIT_WIZARD_DEPOSITHEADER_ERROR = "depositHeaderErrors";
    }

    public static class BudgetAdjustmentDocumentConstants {
        public static final String SOURCE_BA = "From/Decrease";
        public static final String TARGET_BA = "To/Increase";
        public static final String GENERATE_BENEFITS_QUESTION_ID = "GenerateBenefitsQuestion";
        public static final String ADJUSTMENT_RESTRICTION_LEVEL_FUND = "F";
        public static final String ADJUSTMENT_RESTRICTION_LEVEL_CHART = "C";
        public static final String ADJUSTMENT_RESTRICTION_LEVEL_ORGANIZATION = "O";
        public static final String ADJUSTMENT_RESTRICTION_LEVEL_ACCOUNT = "A";
        public static final String ADJUSTMENT_RESTRICTION_LEVEL_SUBFUND = "S";
        public static final String ADJUSTMENT_RESTRICTION_LEVEL_NONE = "N";
        public static final String CROSS_INCOME_STREAM_GLPE_TRANSFER_GENERATING_FUND_GROUPS = "CROSS_INCOME_STREAM_GLPE_TRANSFER_GENERATING_FUND_GROUPS";
        public static final String CROSS_INCOME_STREAM_GLPE_TRANSFER_GENERATING_SUB_FUND_GROUPS = "CROSS_INCOME_STREAM_GLPE_TRANSFER_GENERATING_SUB_FUND_GROUPS";
    }

    public static class DisbursementVoucherDocumentConstants {
        public static final String CLEAR_NON_EMPLOYEE_TAB_QUESTION_ID = "ClearNonEmplTravTabQuestion";
        public static final String CLEAR_WIRE_TRANSFER_TAB_QUESTION_ID = "ClearWireTransferTabQuestion";
        public static final String CLEAR_FOREIGN_DRAFT_TAB_QUESTION_ID = "ClearForeignDraftTabQuestion";
    }

    public static final String ACCOUNTING_LINE_IMPORT_MAX_FILE_SIZE_PARM_NM = "MAX_FILE_SIZE_ACCOUNTING_LINE_IMPORT";
    public static final String ORIGIN_ENTRY_IMPORT_MAX_FILE_SIZE_PARM_NM = "MAX_FILE_SIZE_ORIGIN_ENTRY_IMPORT";

    public static class ChartApcParms {

        public static final String FISCAL_YEAR_MAKER_REPLACE_MODE = "OVERRIDE_TARGET_YEAR_DATA_IND";
        public static final String FISCAL_YEAR_MAKER_SOURCE_FISCAL_YEAR = "SOURCE_FISCAL_YEAR";

        // added from parameter refactoring.
        public static final String APC_HRMS_ACTIVE_KEY = "USE_HRMS_ORGANIZATION_ATTRIBUTES_IND";
        public final static String OBJECT_CODE_ILLEGAL_VALUES = "OBJECT_CODES";
        public static final String DOCTYPE_AND_OBJ_CODE_ACTIVE = "DOCUMENT_TYPES_REQUIRING_ACTIVE_OBJECT_CODES";
        public static final String CG_ALLOWED_SUBACCOUNT_TYPE_CODES = "SUB_ACCOUNT_TYPES";

        // Account parms
        public static final String INCOME_STREAM_ACCOUNT_REQUIRING_FUND_GROUPS = "INCOME_STREAM_ACCOUNT_REQUIRING_FUND_GROUPS";
        public static final String INCOME_STREAM_ACCOUNT_REQUIRING_SUB_FUND_GROUPS = "INCOME_STREAM_ACCOUNT_REQUIRING_SUB_FUND_GROUPS";

        // Org parms
        public static final String DEFAULT_ACCOUNT_NOT_REQUIRED_ORG_TYPES = "ORGANIZATION_TYPES_NOT_REQUIRING_DEFAULT_ACCOUNT";
        public static final String ORG_MUST_REPORT_TO_SELF_ORG_TYPES = "ORGANIZATION_TYPES_THAT_MUST_REPORT_TO_SELF";

        public static final String ACCOUNT_FUND_GROUP_DENOTES_CG = "FUND_GROUP_DENOTES_CG_IND";
        public static final String ACCOUNT_CG_DENOTING_VALUE = "CG_DENOTING_VALUE";

        // newly created prior year accounts to be added
        public static final String PRIOR_YEAR_ACCOUNTS_TO_BE_ADDED = "PRIOR_YEAR_ACCOUNTS_TO_BE_ADDED";
    }

    public static class FinancialApcParms {
        public static final String ACCOUNTING_LINE_IMPORT_HELP = "ACCOUNTING_LINE_IMPORT";
    }

    public static class SystemGroupParameterNames {

        public static final String FLEXIBLE_OFFSET_ENABLED_FLAG = "USE_FLEXIBLE_OFFSET_IND";
        public static final String PURGE_GL_ACCT_BALANCES_T_BEFORE_YEAR = "PRIOR_TO_YEAR";
        public static final String PURGE_GL_ENCUMBRANCE_T_BEFORE_YEAR = "PRIOR_TO_YEAR";
        public static final String PURGE_GL_SF_BALANCES_T_BEFORE_YEAR = "PRIOR_TO_YEAR";
        public static final String PURGE_GL_BALANCE_T_BEFORE_YEAR = "PRIOR_TO_YEAR";
        public static final String PURGE_GL_ENTRY_T_BEFORE_YEAR = "PRIOR_TO_YEAR";
        public static final String PURGE_GL_ID_BILL_T_BEFORE_YEAR = "PRIOR_TO_YEAR";

        public static final String GL_ANNUAL_CLOSING_DOC_TYPE = "ANNUAL_CLOSING_DOCUMENT_TYPE";
        public static final String GL_INDIRECT_COST_RECOVERY = "INDIRECT_COST_RECOVERY_DOCUMENT_TYPE";
        public static final String GL_ORIGINATION_CODE = "MANUAL_FEED_ORIGINATION";
        public static final String GL_SCRUBBER_VALIDATION_DAYS_OFFSET = "CG_ACCOUNT_EXPIRATION_EXTENSION_DAYS";

        public static final String MULTIPLE_VALUE_LOOKUP_RESULTS_PER_PAGE = "MULTIPLE_VALUE_RESULTS_PER_PAGE";
        public static final String MULTIPLE_VALUE_LOOKUP_RESULTS_EXPIRATION_AGE = "MULTIPLE_VALUE_RESULTS_EXPIRATION_SECONDS";

        public static final String ACTIVE_INPUT_TYPES_PARAMETER_NAME = "ACTIVE_FILE_TYPES";

        public static final String COLLECTOR_VALIDATOR_EMAIL_SUBJECT_PARAMETER_NAME = "VALIDATION_EMAIL_SUBJECT_LINE";
        public static final String COLLECTOR_VALIDATOR_ERROR_EMAIL_SUBJECT_PARAMETER_NAME = "VALIDATION_ERROR_EMAIL_SUBJECT_LINE";
        public static final String COLLECTOR_DEMERGER_EMAIL_SUBJECT_PARAMETER_NAME = "ERROR_EMAIL_SUBJECT_LINE";
        public static final String COLLECTOR_EQUAL_DC_TOTAL_DOCUMENT_TYPES = "EQUAL_DEBIT_CREDIT_TOTAL_DOCUMENT_TYPES";
        public static final String COLLECTOR_PERFORM_DUPLICATE_HEADER_CHECK = "PERFORM_DUPLICATE_HEADER_CHECK_IND";

        public static final String BATCH_SCHEDULE_CUTOFF_TIME = "CUTOFF_TIME";
        public static final String BATCH_SCHEDULE_CUTOFF_TIME_IS_NEXT_DAY = "CUTOFF_TIME_NEXT_DAY_IND";
        public static final String BATCH_SCHEDULE_STATUS_CHECK_INTERVAL = "STATUS_CHECK_INTERVAL";

        /**
         * Used by PurgePendingAttachmentsJob to compute the maximum amount of time a pending attachment is allowed to persist on
         * the file system before being deleted.
         */
        public static final String PURGE_PENDING_ATTACHMENTS_STEP_MAX_AGE = "MAX_AGE";

        public static final String NUMBER_OF_DAYS_SINCE_LAST_UPDATE  = "NUMBER_OF_DAYS_SINCE_LAST_UPDATE";

        public static final String ACCOUNTS_CAN_CROSS_CHARTS_IND = "ACCOUNTS_CAN_CROSS_CHARTS_IND";

        public static final String BATCH_CONTAINER_SEMAPHORE_PROCESSING_INTERVAL = "SEMAPHORE_PROCESSING_INTERVAL";
    }

    public static class GeneralLedgerApplicationParameterKeys {
        public static final String INCOME_OBJECT_TYPE_CODES = "INCOME_OBJECT_TYPE_CODES";
        public static final String INCOME_TRANSFER_OBJECT_TYPE_CODES = "INCOME_TRANSFER_OBJECT_TYPE_CODES";
        public static final String EXPENSE_OBJECT_TYPE_CODES = "EXPENSE_OBJECT_TYPE_CODES";
        public static final String EXPENSE_TRANSFER_OBJECT_TYPE_CODES = "EXPENSE_TRANSFER_OBJECT_TYPE_CODES";
    }

    public static class GeneralLedgerCorrectionProcessApplicationParameterKeys {
        public static final String RECORD_COUNT_FUNCTIONALITY_LIMIT = "RECORD_COUNT_FUNCTIONALITY_LIMIT";
        public static final String RECORDS_PER_PAGE = "RECORDS_PER_PAGE";
    }

    public static class EnterpriseFeederApplicationParameterKeys {
        public static final String TO_ADDRESS = "INVALID_FILE_TO_EMAIL_ADDRESSES";
    }

    public static class ParameterValues {
        public static final String YES = "Y";
        public static final String NO = "N";
    }

    public static class Maintenance {
        public static final String AFTER_CLASS_DELIM = "!!";
        public static final String AFTER_FIELDNAME_DELIM = "^^";
        public static final String AFTER_VALUE_DELIM = "::";
    }

    public static class ObjectCodeConstants {
        public static final String INACTIVE_OBJECT_LEVEL_QUESTION_ID = "InactiveObjectLevelQuestion";
        public static final String SECTION_ID_RESEARCH_ADMIN_ATTRIBUTES = "researchAdminAttributes";
    }

    public static final String MONTH1 = "01";
    public static final String MONTH2 = "02";
    public static final String MONTH3 = "03";
    public static final String MONTH4 = "04";
    public static final String MONTH5 = "05";
    public static final String MONTH6 = "06";
    public static final String MONTH7 = "07";
    public static final String MONTH8 = "08";
    public static final String MONTH9 = "09";
    public static final String MONTH10 = "10";
    public static final String MONTH11 = "11";
    public static final String MONTH12 = "12";
    public static final String MONTH13 = "13";
    public static final String PERIOD_CODE_ANNUAL_BALANCE = "AB";
    public static final String PERIOD_CODE_BEGINNING_BALANCE = "BB";
    public static final String PERIOD_CODE_CG_BEGINNING_BALANCE = "CB";

    public static final String REQUEST_SEARCH_RESULTS = "reqSearchResults";
    public static final String REQUEST_SEARCH_RESULTS_SIZE = "reqSearchResultsSize";
    public static final String GL_COLLECTOR_STAGING_DIRECTORY = "collector.staging.directory";

    public static final String DISBURSEMENT_VOUCHER_DOCUMENTATION_LOCATION_CODE_PROPERTY_NAME = "disbursementVoucherDocumentationLocationCode";
    public static final String FUND_GROUP_CODE_PROPERTY_NAME = "code";
    public static final String SUB_FUND_GROUP_CODE_PROPERTY_NAME = "subFundGroupCode";

    public static final String RULE_CODE_R1 = "R1";
    public static final String RULE_CODE_R2 = "R2";
    public static final String RULE_CODE_N1 = "N1";
    public static final String RULE_CODE_N2 = "N2";
    public static final String RULE_CODE_C1 = "C1";
    public static final String RULE_CODE_C2 = "C2";
    public static final String RULE_CODE_A = "A";
    public static final String TRANSACTION_DT = "TRANSACTION_DT";
    public static final String UNALLOC_OBJECT_CD = "UNALLOC_OBJECT_CD";
    public static final String BEG_BUD_CASH_OBJECT_CD = "BEG_BUD_CASH_OBJECT_CD";
    public static final String FUND_BAL_OBJECT_CD = "FUND_BAL_OBJECT_CD";
    public static final String UNIV_FISCAL_YR = "UNIV_FISCAL_YR";

    public static final String EMPLOYEE_LOOKUP_ERRORS = "document.employeeLookups,document.emplid,universityFiscalYear";

    public static class BudgetConstructionConstants {

        /* OLEConstants for the budget construction flag names */
        public final static String BUDGET_ADMINSTRATION_ACTIVE = "BAACTV";
        public final static String BASE_BUDGET_UPDATES_OK = "BASEAD";
        public final static String BUDGET_BATCH_SYNCHRONIZATION_OK = "BSSYNC";
        public final static String CSF_UPDATES_OK = "CSFUPD";
        public final static String BUDGET_CONSTRUCTION_ACTIVE = "BCACTV";
        public final static String BUDGET_CONSTRUCTION_GENESIS_RUNNING = "BCGENE";
        public final static String BUDGET_CONSTRUCTION_UPDATES_OK = "BCUPDT";
        public final static String BUDGET_ON_LINE_SYNCHRONIZATION_OK = "PSSYNC";

        /*
         * object code which stores amounts by which pending general ledger rows in budget construction are out of balance
         */
        @Deprecated
        public final static String OBJECT_CODE_2PLG = "2PLG";

    }

    public static class OperationType {
        public static final String READ = "read";
        public static final String REPORT_ERROR = "with error";
        public static final String INSERT = "insert";
        public static final String UPDATE = "update";
        public static final String DELETE = "delete";
        public static final String SELECT = "select";
        public static final String BYPASS = "bypassed";
    }

    public static class PENDING_ENTRY_APPROVED_STATUS_CODE {
        public static final String APPROVED = "A";
        public static final String PROCESSED = "X";
    }

    public static class TableRenderConstants {
        public static final String SWITCH_TO_PAGE_METHOD = "switchToPage";
        public static final String SORT_METHOD = "sort";

        public static final String PREVIOUSLY_SORTED_COLUMN_INDEX_PARAM = "previouslySortedColumnIndex";
        public static final String VIEWED_PAGE_NUMBER = "viewedPageNumber";
    }

    public static final String PCDO_FILE_TYPE_INDENTIFIER = "procurementCardInputFileType";
    public static final String COLLECTOR_XML_FILE_TYPE_INDENTIFIER = "collectorXmlInputFileType";
    public static final String COLLECTOR_FLAT_FILE_TYPE_INDENTIFIER = "collectorFlatFileInputFileType";
    public static final String ENTERPRISE_FEEDER_FILE_SET_TYPE_INDENTIFIER = "enterpriseFeederFileSetType";

    //variables for upload of vendor information relating to OLE Select batch upload
    public static final String REQUISITION_FILE_TYPE_INDENTIFIER = "requisitionInputFileType";
    public static final String MARC_FILE_TYPE_INDENTIFIER = "marcInputFileType";
    public static final String ORD_FILE_TYPE_INDENTIFIER = "ordInputFileType";
    // next 2 variables for the enterprise feeder batch upload
    public static final String DATA_FILE_TYPE = "DATA";
    public static final String RECON_FILE_TYPE = "RECON";

    // next variable used by the batch upload framework
    public static final String DONE_FILE_TYPE = "DONE_FILE";

    // variables for batch upload inquiry
    public static final String BATCH_UPLOAD_ACTION_PATH = "acqBatchUpload.do";

    /**
     * The base implementation of {@link org.kuali.ole.gl.batch.service.impl.EnterpriseFeederStatusBase} uses strings contained within
     * ApplicationResources.properties to store the human-readable descriptions of each status object. The fully qualified class
     * name is appended to the end of this key to generate the true key. For example,
     * gl.EnterpriseFeeder.StatusDescriptionPrefix.org.kuali.ole.gl.batch.service.impl.FileReconBadLoadAbortedStatus
     */
    public static final String ENTERPRISE_FEEDER_STATUS_DESCRIPTION_PREFIX = "gl.EnterpriseFeeder.StatusDescription.";

    public static final String BATCH_STEP_RUNNER_JOB_NAME = "stepRunByBatchStepRunner";

    // Some static method calls below that could be done in static variables instead but isn't safe to do during class loading
    // w/SpringContext.
    private static String DASH_FINANCIAL_OBJECT_CODE = null;

    public static String getDashFinancialObjectCode() {
        if (DASH_FINANCIAL_OBJECT_CODE == null) {
            DASH_FINANCIAL_OBJECT_CODE = StringUtils.rightPad("", SpringContext.getBean(DataDictionaryService.class).getAttributeMaxLength(OriginEntryFull.class, OLEPropertyConstants.FINANCIAL_OBJECT_CODE), '-');
        }
        return DASH_FINANCIAL_OBJECT_CODE;
    }

    private static String DASH_FINANCIAL_SUB_OBJECT_CODE = null;

    public static String getDashFinancialSubObjectCode() {
        if (DASH_FINANCIAL_SUB_OBJECT_CODE == null) {
            DASH_FINANCIAL_SUB_OBJECT_CODE = StringUtils.rightPad("", SpringContext.getBean(DataDictionaryService.class).getAttributeMaxLength(OriginEntryFull.class, OLEPropertyConstants.FINANCIAL_SUB_OBJECT_CODE), '-');
        }
        return DASH_FINANCIAL_SUB_OBJECT_CODE;
    }

    private static String DASH_SUB_ACCOUNT_NUMBER = null;

    public static String getDashSubAccountNumber() {
        if (DASH_SUB_ACCOUNT_NUMBER == null) {
            DASH_SUB_ACCOUNT_NUMBER = StringUtils.rightPad("", SpringContext.getBean(DataDictionaryService.class).getAttributeMaxLength(OriginEntryFull.class, OLEPropertyConstants.SUB_ACCOUNT_NUMBER), '-');
        }
        return DASH_SUB_ACCOUNT_NUMBER;
    }

    private static String SPACE_SUB_ACCOUNT_NUMBER = null;

    public static String getSpaceSubAccountNumber() {
        if (SPACE_SUB_ACCOUNT_NUMBER == null) {
            SPACE_SUB_ACCOUNT_NUMBER = StringUtils.rightPad("", SpringContext.getBean(DataDictionaryService.class).getAttributeMaxLength(OriginEntryFull.class, OLEPropertyConstants.SUB_ACCOUNT_NUMBER), ' ');
        }
        return SPACE_SUB_ACCOUNT_NUMBER;
    }

    private static String DASH_PROJECT_CODE = null;

    public static String getDashProjectCode() {
        if (DASH_PROJECT_CODE == null) {
            DASH_PROJECT_CODE = StringUtils.rightPad("", SpringContext.getBean(DataDictionaryService.class).getAttributeMaxLength(OriginEntryFull.class, OLEPropertyConstants.PROJECT_CODE), '-');
        }
        return DASH_PROJECT_CODE;
    }

    public static final class ReportGeneration{
        public final static String PARAMETER_NAME_SUBREPORT_DIR = "SUBREPORT_DIR";
        public final static String PARAMETER_NAME_SUBREPORT_TEMPLATE_NAME = "SUBREPORT_TEMPLATE_NAMES";
        public final static String DESIGN_FILE_EXTENSION = ".jrxml";
        public final static String JASPER_REPORT_EXTENSION = ".jasper";
        public final static String PDF_FILE_EXTENSION = ".pdf";
        public final static String PDF_MIME_TYPE = "application/pdf";
        public final static String TEXT_MIME_TYPE = "text/plain";
        public final static String ACCOUNT_EXPORT_FILE_NAME = "account_export.txt";
        public final static String MONTHLY_EXPORT_FILE_NAME = "monthly_export.txt";
        public final static String FUNDING_EXPORT_FILE_NAME = "funding_export.txt";
    }

    public final static KualiInteger ONE_HUNDRED = new KualiInteger(100);
    public final static KualiInteger ONE = new KualiInteger(1);

    // effort certification period status codes
    public static final class PeriodStatusCodes {
        public static final String CLOSED = "C";
        public static final String NOT_OPEN = "N";
        public static final String OPEN = "O";
    }

    public static final class CustomerParameter {
        public static final String TAX_NUMBER_REQUIRED_IND = "TAX_NUMBER_REQUIRED_IND";
    }

    // financial document type codes
    public static final class FinancialDocumentTypeCodes {
        public static final String ALL = "ALL";

        // financial processing
        public static final String GENERAL_ERROR_CORRECTION = "OLE_GEC";
        public static final String YEAR_END_GENERAL_ERROR_CORRECTION = "OLE_YEGE";
        public static final String DISTRIBUTION_OF_INCOME_AND_EXPENSE = "OLE_DI";
        public static final String YEAR_END_DISTRIBUTION_OF_INCOME_AND_EXPENSE = "OLE_YEDI";
        public static final String SERVICE_BILLING = "OLE_SB";
        public static final String INTERNAL_BILLING = "OLE_IB";
        public static final String CASH_RECEIPT = "OLE_CR";
        public static final String PROCUREMENT_CARD = "OLE_PCDO";
        public static final String ADVANCE_DEPOSIT = "OLE_AD";
        public static final String CASH_MANAGEMENT = "OLE_CMD";
        public static final String CREDIT_CARD_RECEIPT = "OLE_CCR";
        public static final String NON_CHECK_DISBURSEMENT = "OLE_ND";

        // purap
        public static final String REQUISITION = "OLE_REQS";
        public static final String REQUESTOR = "OLE_REQSTR";
        public static final String PAYMENT_REQUEST = "OLE_PREQ";
        public static final String INVOICE = "OLE_PRQS";
        public static final String PURCHASE_ORDER = "OLE_PO";
        public static final String PURCHASE_ORDER_AMENDMENT = "OLE_POA";
        public static final String PURCHASE_ORDER_CLOSE = "OLE_POC";
        public static final String PURCHASE_ORDER_SPLIT = "OLE_POSP";
        public static final String PURCHASE_ORDER_REOPEN = "OLE_POR";
        public static final String PURCHASE_ORDER_PAYMENT_HOLD = "OLE_POPH";
        public static final String PURCHASE_ORDER_REMOVE_HOLD = "OLE_PORH";
        public static final String PURCHASE_ORDER_RETRANSMIT = "OLE_PORT";
        public static final String PURCHASE_ORDER_VOID = "OLE_POV";
        public static final String LINE_ITEM_RECEIVING = "OLE_RCVL";
        public static final String CORRECTION_RECEIVING = "OLE_RCVC";
        public static final String VENDOR_CREDIT_MEMO = "OLE_CM";
        public static final String BULK_RECEIVING = "OLE_RCVB";
        public static final String ELECTRONIC_INVOICE_REJECT = "OLE_EIRT";
        public static final String ACQ_BATCH_UPLOAD = "OLE_ACQBTHUPLOAD";
    }

    // financial document type names
    @Deprecated
    public static final class FinancialDocumentTypeNames {
        public static final String GENERAL_ERROR_CORRECTION = "General Error Correction";
        public static final String YEAR_END_GENERAL_ERROR_CORRECTION = "Year End General Error Correction";
        public static final String DISTRIBUTION_OF_INCOME_AND_EXPENSE = "Distribution of Income and Expense";
        public static final String YEAR_END_DISTRIBUTION_OF_INCOME_AND_EXPENSE = "Year End Distribution of Income and Expense";
        public static final String SERVICE_BILLING = "Service Billing";
        public static final String INTERNAL_BILLING = "Internal Billing";
        public static final String CASH_RECEIPT = "Cash Receipt";
        public static final String PROCUREMENT_CARD = "Procurement Card";
    }

    public static final class AccountingLineViewStandardBlockNames {
        public static final String ACTION_BLOCK = "actions";
        public static final String SEQUENCE_NUMBER_BLOCK = "sequenceNumber";
        public static final String SALES_TAX_BLOCK = "salesTaxInformation";
    }

    public static final class TaxRegionConstants {
        public static final String TAX_REGION_RATES = "taxRegionRates";
        public static final String TAX_REGION_COUNTIES = "taxRegionCounties";
        public static final String TAX_REGION_STATES = "taxRegionStates";
        public static final String TAX_REGION_POSTAL_CODES = "taxRegionPostalCodes";
        public static final String TAX_REGION_STATE_CODE = "stateCode";
        public static final String TAX_REGION_COUNTY_CODE = "countyCode";
        public static final String TAX_REGION_POSTAL_CODE = "postalCode";
        public static final String TAX_REGION_TAX_RATE = "taxRate";
        public static final String TAX_REGION_EFFECTIVE_DATE = "effectiveDate";
        public static final String TAX_REGION_TYPE_CODE = "taxRegionTypeCode";
        public static final String TAX_REGION_COUNTIES_SECTION_ID = "TaxRegionCounties";
        public static final String TAX_REGION_STATES_SECTION_ID = "TaxRegionStates";
        public static final String TAX_REGION_POSTAL_CODES_SECTION_ID = "TaxRegionPostalCodes";
        public static final String TAX_REGION_RATES_SECTION_ID = "TaxRegionRates";
        public static final String TAX_REGION_CREATE_SECTION_ID = "CreateTaxRegion";

        public static final String TAX_REGION_TYPE_CODE_COUNTY = "CNTY";
        public static final String TAX_REGION_TYPE_CODE_POSTAL_CODE = "POST";
        public static final String TAX_REGION_TYPE_CODE_STATE = "ST";
    }

    public static final class SubAccountType {
        public static final String COST_SHARE = "CS";
        public static final String EXPENSE = "EX";

        public static final List<String> ELIGIBLE_SUB_ACCOUNT_TYPE_CODES = getEligibleSubAccountTypeCodes();

        private static final List<String> getEligibleSubAccountTypeCodes(){
            List<String> subAccountTypeCodesList = new ArrayList<String>();
            subAccountTypeCodesList.add(OLEConstants.SubAccountType.COST_SHARE);
            subAccountTypeCodesList.add(OLEConstants.SubAccountType.EXPENSE);
            return subAccountTypeCodesList;
        }
    }

    @Deprecated // move to PDP
    public static final class PdpConstants {
        public static final String PAYMENT_OPEN_STATUS_CODE = "OPEN";
    }

    // define a set of indicators related to payments, including payee types, tax review requirements and others
    public static class AdHocPaymentIndicator{
        public static final String EMPLOYEE_VENDOR = "E";
        public static final String ALIEN_VENDOR = "A";
        public static final String EMPLOYEE_PAYEE = "E";
        public static final String ALIEN_PAYEE = "A";
        public static final String TAX_CONTROL_REQUIRING_TAX_REVIEW = "T";
        public static final String PAYMENT_REASON_REQUIRING_TAX_REVIEW = "P";
        public static final String OTHER = "N";
    }

    public static class SysKimApiConstants{
        public static final String ACCOUNT_SUPERVISOR_KIM_ROLE_NAME = "Account Supervisor";
        public static final String CONTRACTS_AND_GRANTS_PROJECT_DIRECTOR = "Contracts & Grants Project Director";
        public static final String FISCAL_OFFICER_KIM_ROLE_NAME = "Fiscal Officer";
        public static final String FISCAL_OFFICER_PRIMARY_DELEGATE_KIM_ROLE_NAME = "Fiscal Officer Primary Delegate";
        public static final String FISCAL_OFFICER_SECONDARY_DELEGATE_KIM_ROLE_NAME = "Fiscal Officer Secondary Delegate";
        public static final String AWARD_SECONDARY_DIRECTOR_KIM_ROLE_NAME = "Award Project Director";
        public static final String ACTIVE_FACULTY_OR_STAFF_KIM_ROLE_NAME = "Active Faculty or Staff";
        public static final String ACTIVE_PROFESSIONAL_EMPLOYEE_KIM_ROLE_NAME = "Active Professional Employee";
        public static final String ACTIVE_EMPLOYEE_AND_KFS_USER_KIM_ROLE_NAME = "Active Employee & Financial System User";
        public static final String ACTIVE_PROFESSIONAL_EMPLOYEE_AND_KFS_USER_KIM_ROLE_NAME = "Active Professional Employee & Financial System User";
        public static final String CHART_MANAGER_KIM_ROLE_NAME = "Chart Manager";
        public static final String ORGANIZATION_REVIEWER_ROLE_NAMESPACECODE = CoreModuleNamespaces.OLE;
        public static final String ACCOUNTING_REVIEWER_ROLE_NAMESPACECODE = CoreModuleNamespaces.OLE;
        public static final String ACCOUNTING_REVIEWER_ROLE_NAME = "Accounting Reviewer";
        public static final String ORGANIZATION_REVIEWER_ROLE_NAME = "Organization Reviewer";
        public static final String KFS_USER_ROLE_NAME = "User";
    }

    public enum PermissionAttributeValue {
        SOURCE_ACCOUNTING_LINES("sourceAccountingLines"), TARGET_ACCOUNTING_LINES("targetAccountingLines");

        public final String value;

        private PermissionAttributeValue(String value) {
            this.value = value;
        }
    }

    public enum PermissionTemplate {
        DEFAULT( KRADConstants.DEFAULT_NAMESPACE, "Default"),
        ERROR_CORRECT_DOCUMENT( OLEConstants.CoreModuleNamespaces.OLE, "Error Correct Document"),
        MODIFY_ACCOUNTING_LINES(OLEConstants.CoreModuleNamespaces.OLE, "Modify Accounting Lines"),
        CLAIM_ELECTRONIC_PAYMENT(OLEConstants.CoreModuleNamespaces.OLE, "Claim Electronic Payment"),
        MODIFY_BATCH_JOB(OLEConstants.CoreModuleNamespaces.OLE, "Modify Batch Job"),
        EDIT_BANK_CODE(OLEConstants.CoreModuleNamespaces.OLE, "Edit Bank Code"),
        ADMINISTER_ROUTING_FOR_DOCUMENT(KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE, "Administer Routing for Document"),
        VIEW_BATCH_FILES(OLEConstants.CoreModuleNamespaces.OLE, "Administer Batch File"),
        UPLOAD_BATCH_INPUT_FILES(OLEConstants.CoreModuleNamespaces.OLE, "Upload Batch Input File(s)");

        public final String name;
        public final String namespace;
        private PermissionTemplate(String namespace, String name) {
            this.namespace = namespace;
            this.name = name;
        }
    }

    public enum PermissionNames {
        EDIT_INACTIVE_ACCOUNT( OLEConstants.CoreModuleNamespaces.CHART, "Edit Inactive Account" ),
        SERVE_AS_ACCOUNT_MANAGER( OLEConstants.CoreModuleNamespaces.CHART, "Serve As Account Manager" ),
        SERVE_AS_ACCOUNT_SUPERVISOR( OLEConstants.CoreModuleNamespaces.CHART, "Serve As Account Supervisor" ),
        SERVE_AS_FISCAL_OFFICER( OLEConstants.CoreModuleNamespaces.CHART, "Serve As Fiscal Officer" ),
        SERVE_AS_FISCAL_OFFICER_DELEGATE( OLEConstants.CoreModuleNamespaces.CHART, "Serve As Fiscal Officer Delegate" );

        public final String name;
        public final String namespace;
        private PermissionNames(String namespace, String name) {
            this.namespace = namespace;
            this.name = name;
        }
    }

    public static final String KFS_ACTION_CAN_ERROR_CORRECT = "canErrorCorrect";
    public static final String KFS_ACTION_CAN_EDIT_BANK = "canEditBank";


    public static final String AMOUNT_TOTALING_EDITING_MODE = "amountTotaling";
    public static final String BANK_ENTRY_VIEWABLE_EDITING_MODE = "bankEntryViewable";
    public static final String BANK_ENTRY_EDITABLE_EDITING_MODE = "bankEntry";

    public class RouteLevelNames {
        public static final String ACCOUNT = "Account";
        public static final String ACCOUNTING_ORGANIZATION_HIERARCHY = "AccountingOrganizationHierarchy";
        public static final String ACCOUNT_REVIEW_FULL_EDIT = "AccountFullEdit";
        public static final String PROJECT_MANAGEMENT = "ProjectManagement";
        public static final String ORGANIZATION_HIERARCHY = "OrganizationHierarchy";
    }

    public static final String ROOT_DOCUMENT_TYPE = "OLE";
    public static final String FINANCIAL_SYSTEM_LEDGER_ONLY_ROOT_DOCUMENT_TYPE = "OpenLibraryEnvironmentLedgerOnlyDocument";

    public static class COAConstants{
        public static final String FINANCIAL_SYSTEM_DOCUMENT = ROOT_DOCUMENT_TYPE;
        public static final String FINANCIAL_SYSTEM_TRANSACTIONAL_DOCUMENT = "OpenLibraryEnvironmentTransactionalDocument";
        public static final String FINANCIAL_SYSTEM_COMPLEX_MAINTENANCE_DOCUMENT = "OpenLibraryEnvironmentComplexMaintenanceDocument";
        public static final String FINANCIAL_SYSTEM_SIMPLE_MAINTENANCE_DOCUMENT = "OpenLibraryEnvironmentSimpleMaintenanceDocument";

        public static final String NODE_NAME_ORGANIZATION_HIERARCHY = "OrganizationHierarchy";
        public static final String NODE_NAME_ACCOUNTING_ORGANIZATION_HIERARCHY = "AccountingOrganizationHierarchy";

        public static final String ORG_REVIEW_ROLE_ORG_ACC_ONLY_CODE = "A";
        public static final String ORG_REVIEW_ROLE_ORG_ACC_ONLY_TEXT = "Organization Accounting Only";
        public static final String ORG_REVIEW_ROLE_ORG_ONLY_CODE = "O";
        public static final String ORG_REVIEW_ROLE_ORG_ONLY_TEXT = "Organization Only";
        public static final String ORG_REVIEW_ROLE_ORG_ACC_BOTH_CODE = "B";
        public static final String ORG_REVIEW_ROLE_ORG_ACC_BOTH_TEXT = "Both";
        public static final String ORG_REVIEW_ROLE_DOCUMENT_TYPE_NAME = "OLE_ORR";
        public static final String ORG_REVIEW_ROLE_CREATE_DELEGATION_DISPLAY_TEXT = "create delegation";

    }

    public static class ReportConstants{
        public static final String EMPTY_CELL_ENTRY_KEY_PREFIX = "EMPTY_CELL";

        public static final String TABLE_HEADER_LINE_KEY = "tableHeaderLine";
        public static final String SEPARATOR_LINE_KEY = "separatorLine";
        public static final String TABLE_CELL_FORMAT_KEY = "tableCellFormat";
        public static final String[] FORMAT_ESCAPE_CHARACTERS = new String[]{"%", "\\"};
    }

    public static final String DOCUMENT_LOCKOUT_PARM_NM = "LOCKOUT_IND";
    public static final String DOCUMENT_LOCKOUT_PARM_DESC = "This parameter is to lock document during the lockout time.";
    public static final String DOCUMENT_LOCKOUT_DEFAULT_MESSAGE = "DOCUMENT_LOCKOUT_DEFAULT_MESSAGE";
    public static final String FROM_EMAIL_ADDRESS_PARM_NM = "FROM_EMAIL_ADDRESS";


    // System Parameters
    public static final String RESULT_SUMMARY_TO_EMAIL_ADDRESSES = "RESULT_SUMMARY_TO_EMAIL_ADDRESSES";
    public static final String SOURCE_URL_PARAMETER = "SOURCE_URL";

    public static final String REPORT_WRITER_SERVICE_PAGE_NUMBER_PLACEHOLDER = "${pageNumber}";

    public static class SchemaBuilder {
        public static final String SCHEMA_FILE_DD_VALIDATION_PLACEHOLDER_BEGIN = "${";
        public static final String SCHEMA_FILE_DD_VALIDATION_PLACEHOLDER_END = "}";
        public static final String XSD_VALIDATION_PREFIX = "xsd:";
        public static final String DD_VALIDATION_PREFIX = "dd:";
    }

    public static class CapitalAssets {
        //C ==> create asset action, M ==> modify asset action
        public static final String CAPITAL_ASSET_CREATE_ACTION_INDICATOR = "C";
        public static final String CAPITAL_ASSET_MODIFY_ACTION_INDICATOR = "M";
        public static final String CAPITAL_ASSET_TAB_STATE_OPEN = "OPEN";
        public static final String CAPITAL_ASSET_TAB_STATE_CLOSE = "CLOSE";
        public static final String CAPITAL_ASSET_PROCESSED_IND = "N";

        public static final String ACCOUNTING_LINES_FOR_CAPITALIZATION_TAB_TITLE = "Accounting Lines for Capitalization";
        public static final String CREATE_CAPITAL_ASSETS_TAB_TITLE = "Create Capital Assets";
        public static final String MODIFY_CAPITAL_ASSETS_TAB_TITLE = "Modify Capital Assets";

        public static final String DISTRIBUTE_COST_EQUALLY_CODE = "2";
        public static final String DISTRIBUTE_COST_EQUALLY_DESCRIPTION = "Distribute cost evenly";
        public static final String DISTRIBUTE_COST_BY_INDIVIDUAL_ASSET_AMOUNT_CODE = "1";
        public static final String DISTRIBUTE_COST_BY_INDIVIDUAL_ASSET_AMOUNT_DESCRIPTION = "Distribute cost by amount";

        public static final Integer PERCENT_SCALE = new Integer(2);
        public static final Integer CAPITAL_ACCOUNT_LINE_PERCENT_SCALE = new Integer(20);
    }

    public static final String CREATE_TAX_REGION_FROM_LOOKUP_PARM = "createTaxRegionFromLookup";

    public static class OleRequisitionItem{
        public static final String ORDER_HOLD_QUEUE_ROLE = "OLE_ORDQU-User";
        public static final String ORDER_HOLD_QUEUE_ROLE_NAMESPACE          = CoreModuleNamespaces.SELECT;
        public static final String ORDER_HOLD_QUEUE_ASSIGN_PERMISSION = "Assign To Others";
        public static final String ORDER_HOLD_QUEUE_FROM_DATE_LAST_MODIFIED = "fromDateLastModified";
        public static final String ORDER_HOLD_QUEUE_TO_DATE_LAST_MODIFIED = "toDateLastModified";
        public static final String ORDER_HOLD_QUEUE_SELECTOR = "selector";
        public static final String ORDER_HOLD_QUEUE_EXTERNAL_REQUESTOR = "externalRequestorName";
        public static final String ORDER_HOLD_QUEUE_INTERNAL_REQUESTOR = "internalRequestorName";
        public static final String ORDER_HOLD_QUEUE_EXTERNAL_REQUESTORID = "requestorId";
        public static final String ORDER_HOLD_QUEUE_INTERNAL_REQUESTORID = "internalRequestorId";
        public static final String ORDER_HOLD_QUEUE_REQUESTOR_FNAME = "requestorFirstName";
        public static final String ORDER_HOLD_QUEUE_ASSIGN_OWN_REQ = "Assign own Requisition";
        public static final String ORDER_HOLD_QUEUE_APPROVE_REQ = "Approve REQ";

        public static final String FIRM_FIXED_ITEM_LOCATION = "UC/UCX/Order";
        public static final String FIRM_FIXED_ITEM_STATUS = "ONORDER";
        public static final String APPROVAL_ITEM_LOCATION = "UC/UCX/InProc";
        public static final String APPROVAL_ITEM_STATUS = "INPROCESS";
        public static final String RECURRING_PAYMENT_TYPE = "VARV";
        public static final String DEFAULT_CHART_OF_ACCOUNTS_CODE = "DEFAULT_ACCOUNTINGLINE_CHART_CODE";
        public static final String DEFAULT_FINANCIAL_OBJECT_CODE = "DEFAULT_ACCOUNTINGLINE_OBJECT_CODE";
        public static final String DEFAULT_COPY_NUMBER = "1";
    }

    public static class OleLineItemReceiving{
        public static final String ADD_NEW_LINE_ITEM = "Add New Line Item";
        public static final String LINE_ITEM_RECEIVING_NAMESPACE = CoreModuleNamespaces.SELECT;
        public static final String RECEIVED_STATUS = "Received";
        public static final String SEE_COPIES_SECTION = "See Copies Section";
        public static final String NOT_APPLICABLE = "N/A";
        public static final String NOT_RECEIVED_STATUS="Not Received";

    }

    public static class OlePaymentRequest{
        public static final String CAN_CLOSE_PO = "Close Purchase Order";
        public static final String PAYMENT_REQUEST_NAMESPACE = CoreModuleNamespaces.SELECT;
        public static final String HAS_INVOICE_TYPE = "HasInvoiceType";
        public static final String HAS_PREPAID_INVOICE_TYPE = "HasPrepaidInvoiceType";
        public static final String HAS_PAYMENT_METHOD = "HasPaymentMethod";
    }

    public static class OleInvoice{
        public static final String INVOICE_SAVED = "Saved";
        public static final String CAN_CLOSE_PO = "Close Purchase Order";
        public static final String PAYMENT_REQUEST_NAMESPACE = CoreModuleNamespaces.SELECT;
        public static final String HAS_INVOICE_TYPE = "HasInvoiceType";
        public static final String HAS_PREPAID_INVOICE_TYPE = "HasPrepaidInvoiceType";
        public static final String HAS_PAYMENT_METHOD = "HasPaymentMethod";
    }

    public static class OleRequisition{
        public static final String FIRM_TYPE_ORDERS = "Firm Type Requisition Edit";
        public static final String OTHER_TYPE_ORDERS = "Other Type Requisition Edit";
        public static final String REQUISITION_NAMESPACE      = CoreModuleNamespaces.SELECT;
        public static final String REQUISITION = "OLE_REQS";
        public static final String EDIT_OWN_DOCUMENT = "Edit Own Requisition Document";
        public static final String EDIT_OWN_ASSIGNED_DOCUMENT = "Edit Own Assigned Requisition Document";
        public static final String CREATE_BIBEDITOR = "Create BibEditor";
        public static final String EDIT_BIBEDITOR = "Edit BibEditor";

    }

    public static class OleLoadSummary{
        public static final String CAN_VIEW_LOAD_SUMMARY = "View Load Summary";
        public static final String CAN_SEARCH_LOAD_SUMMARY = "Search Load Summary";
        public static final String LOAD_SUMMARY_NAMESPACE  = CoreModuleNamespaces.SELECT;
        public static final String LOAD_SUMMARY = "OLE_LOADSUM";


    }

    public static class Vendor{
        public static final String DOCUMENT_TYPE = "OLE_PVEN";
        public static final String VENDOR_NAMESPACE        = CoreModuleNamespaces.SELECT;
        public static final String BLANKET_APPROVE         = "Blanket Approve Document";
        public static final String VENDOR_REVIEW = "Review";
        public static final String VENDOR_MANAGEMENT="Management";
        public static final String BLANKET_APPROVE_VENDOR_DOCUMENT = "Blanket Approve Vendor Document";
        public static final String VENDOR_REVIEW_DOCUMENT= "Review PVEN Management";
        public static final String DEACTIVATE_VENDOR = "Deactivate Vendor";
        public static final String CREATE_VENDOR_DIVISION = "Create Vendor Division";
        public static final String EDIT_VENDOR_LINKING_NUM = "Edit Vendor Linking Number";
    }

    public static class OlePurchaseOrder{
        public static final String PRINT_PURCHASE_ORDER = "Print Purchase Order";
        public static final String PO_NAMESPACE         = CoreModuleNamespaces.SELECT;
        public static final String PO_DOCUMENT_TYPE = "OLE_PO";
        public static final String POR_DOCUMENT_TYPE = "OLE_POR";
        public static final String POSP_DOCUMENT_TYPE = "OLE_POSP";
        public static final String POV_DOCUMENT_TYPE = "OLE_POV";
        public static final String EDIT_OWN_DOCUMENT = "Edit Own Requisition Document";
        public static final String EDIT_VOID_DOCUMENT = "Edit Void Document";
        public static final String EDIT_SPLIT_DOCUMENT = "Edit Split Document";
        public static final String EDIT_REOPEN_DOCUMENT = "Edit Reopen Document";
        public static final String CREATE_APO = "Create APO";

    }




    public static class BibInfoBean{
        public static final String ITEM_AUTHOR = "author";
        public static final String ITEM_TITLE = "title";
    }
    public static class OrderQueue{
        public static final String PRINCIPAL_NAME = "document.principalName";
        public static final String TOTAL_PRICE = "TOTAL_PRICE";
        public static final String DOCUMENT_TYPE = "OLE_ORDQU";
        public static final String SUBMIT_ANNOTATION = "Completed from Order Hold Queue";
        public static final String CANCEL_ANNOTATION = "Cancelled from Order Hold Queue";
        public static final String APPROVE_ANNOTATION = " approved for ";
        public static final String CONSTANTS = "OrderQueueConstants";
        public static final String OBJECT_ID = "objectId";
        public static final String REQUISITIONS = "requisitions";
        public static final String CHART_CODE = "Chart Code";
        public static final String OBJECT_CODE = "Object Code";


        // Added for OLE-1976 Order Queue Search Enhancements Starts
        public static final String ORDQ_REQ_DOC_NUMBER = "requisitionDocNumber";
        public static final String REQ_ITM_REQ_DOC_NUMBER = "requisition.documentNumber";
        public static final String ORDQ_REQ_STATUS = "requisitionStatusCode";
        public static final String REQ_ITM_REQ_STATUS     = "requisition.documentHeader.workflowDocument.applicationDocumentStatus";
        public static final String ORDQ_VND_NAME = "vendorName";
        public static final String REQ_ITM_VND_NAME = "requisition.vendorName";
        public static final String ORDQ_INT_REQID = "internalRequestorId";
        public static final String REQ_ITM_INT_REQID = "internalRequestorId";
        public static final String ORDQ_EXT_REQID = "externalRequestorId";
        public static final String REQ_ITM_EXT_REQID = "requestorId";
        public static final String ORDQ_FMT_TYP_ID = "formatTypeId";
        public static final String REQ_ITM_FMT_TYP_ID = "formatTypeId";
        public static final String ORDQ_TITLE = "title";
        public static final String REQ_ITM_TITLE = "docData.title";
        public static final String ORDQ_AUTHOR = "author";
        public static final String REQ_ITM_AUTHOR = "docData.author";
        public static final String ORDQ_PUBLISHER = "publisher";
        public static final String REQ_ITM_PUBLISHER = "docData.publisher";
        public static final String ORDQ_ISBN = "isbn";
        public static final String REQ_ITM_ISBN = "docData.isbn";
        public static final String OLE_SUPER_SELECTOR = "OLE_Super-Selectors";
        public static final String REQ_ACCT_NUM = "sourceAccountingLines.accountNumber";
        public static final String ORDQ_ACCT_NUM = "accountNumber";
        public static final String REQ_CHART_CODE = "sourceAccountingLines.chartOfAccountsCode";
        public static final String ORDQ_CHART_CODE = "chartOfAccountsCode";
        public static final String REQ_OBJ_CODE = "sourceAccountingLines.financialObjectCode";
        public static final String ORDQ_OBJ_CODE = "objectCode";

        public static final Map<String, String> getRequisitionFieldNames() {
            Map<String, String> requisitionFields = new HashMap<String, String>();
            requisitionFields.put(ORDQ_REQ_DOC_NUMBER, REQ_ITM_REQ_DOC_NUMBER);
            // requisitionFields.put(ORDQ_REQ_STATUS, REQ_ITM_REQ_STATUS);
            requisitionFields.put(ORDQ_VND_NAME, REQ_ITM_VND_NAME);
            requisitionFields.put(ORDQ_INT_REQID, REQ_ITM_INT_REQID);
            requisitionFields.put(ORDQ_EXT_REQID, REQ_ITM_EXT_REQID);
            requisitionFields.put(ORDQ_FMT_TYP_ID, REQ_ITM_FMT_TYP_ID);
            requisitionFields.put(ORDQ_TITLE, REQ_ITM_TITLE);
            requisitionFields.put(ORDQ_AUTHOR, REQ_ITM_AUTHOR);
            requisitionFields.put(ORDQ_ACCT_NUM, REQ_ACCT_NUM);
            requisitionFields.put(ORDQ_CHART_CODE, REQ_CHART_CODE);
            requisitionFields.put(ORDQ_OBJ_CODE, REQ_OBJ_CODE);
            requisitionFields.put(ORDQ_PUBLISHER, REQ_ITM_PUBLISHER);
            requisitionFields.put(ORDQ_ISBN, REQ_ITM_ISBN);
            return Collections.unmodifiableMap(requisitionFields);
        }

        public static final Map<String, String> REQUISITION_FIELDS = getRequisitionFieldNames();
        public static final String selectorField = "selectorUserId";
        public static final String workflowStatusChangeDateFrom = "workflowStatusChangeDateFrom";
        public static final String workflowStatusChangeDateTo = "workflowStatusChangeDateTo";
        // Added for OLE-1976 Order Queue Search Enhancements Ends
    }

    public static final String DOCSTORE_URL_KEY = "ole.docstore.url";
    public static final String DOCSTORE_URL_KEY_FOR_POS = "docstore.url";
    public static final String DOCSTORE_APP_URL_KEY = "ole.docstoreapp.url";
    public static final String DOCSTORE_VIEW_URL_KEY = "ole.docstoreviewapp.url";
    public static final String BIBEDITOR_URL_KEY = "ole.bibeditor.url";
    public static final String DUBLINEDITOR_URL_KEY = "ole.dublineditor.url";
    public static final String INSTANCEEDITOR_URL_KEY                          = "ole.instanceEditor.url";
    public static final String BIBEDITOR_CREATE_URL_KEY = "ole.bibeditor.create.url";
    public static final String BIBEDITOR_SEARCH_URL_KEY = "ole.bibsearch.url";
    public static final String DOCSEARCH_URL_KEY = "ole.docsearch.url";
    public static final String DOCSEARCH_APP_URL_KEY = "ole.docsearch.app.url";
    public static final String DOCSTORE_APP_CONTENT_TYPE_KEY = "ole.docstoreapp.contenttype";
    public static final String DOCSTORE_APP_CHARSET_CONTENT_TYPE_KEY = "ole.docstoreapp.charset.contenttyp";
    public static final String DOCSTORE_APP_POST_DATA_KEY = "ole.docstoreapp.postdata";
    public static final String DOCSTORE_APP_POST_DATA_EDIT_KEY = "ole.docstoreapp.edit.postdata";
    public static final String DOCSTORE_APP_POST_DATA_DELETE_KEY = "ole.docstoreapp.delete.postdata";
    public static final String DOCSTORE_FILE_KEY = "ole.docstore.file";
    public static final String DOCSEARCH_ORDERQUEUE_LIMIT_KEY = "orderqueue.docsearch.maxLimit";
    public static final String SPECIAL_CONDITIONS_NOTE = "Special Conditions Note";
    public static final String SPECIAL_PROCESSING_INSTRUCTION_NOTE = "Special Processing Instruction Note";
    public static final String PR_LOOKUP_ACTION = "prlookup.do";
    public static final String BIB_LOOKUP_ACTION = "bibLookUp.do";
    public static final String MANUAL_INGEST_DOCUMENT_DESCRIPTION = "YBP_Firm_Ingest";
    public static final String DOCUMENT_DESCRIPTION = "YBP";
    public static final String ORDER_TYPE="Firm";

    public static final String DOCSEARCH_LIMIT_KEY = "docsearch.noOfRows";

    public static final String INSTANCE_MARC_XML_STRING= "<instanceCollection>\n" +
            "  <instance>\n" +
            "   <instanceIdentifier></instanceIdentifier>\n" +
            "    <oleHoldings primary=\"true\">\n" +
            "       <holdingsIdentifier></holdingsIdentifier>\n" +
            "       <receiptStatus></receiptStatus>\n" +
            "       <uri></uri>\n" +
            "       <note type=\"public\"></note>\n" +
            "       <location primary=\"true\" status=\"permanent\">\n" +
            "        <locationLevel>\n" +
            "          <name></name>\n" +
            "          <level></level>\n" +
            "          <locationLevel>\n" +
            "              <name></name>\n" +
            "              <level></level>\n" +
            "              <locationLevel>\n" +
            "                 <name></name>\n" +
            "                 <level></level>\n" +
            "                 <locationLevel>\n" +
            "                     <name></name>\n" +
            "                     <level></level>\n" +
            "                 </locationLevel>\n" +
            "              </locationLevel>\n" +
            "           </locationLevel>\n" +
            "        </locationLevel>\n" +
            "      </location>\n" +
            "      <extension>\n" +
            "        <additionalAttributes>\n" +
            "          <createdBy></createdBy>\n" +
            "          <dateEntered></dateEntered>\n" +
            "        </additionalAttributes>\n" +
            "      </extension>\n" +
            "      <callNumber>\n" +
            "        <type></type>\n" +
            "        <prefix></prefix>\n" +
            "        <number></number>\n" +
            "        <shelvingScheme>\n" +
            "          <codeValue></codeValue>\n" +
            "        </shelvingScheme>\n" +
            "        <shelvingOrder>\n" +
            "          <codeValue></codeValue>\n" +
            "        </shelvingOrder>\n" +
            "      </callNumber>\n" +
            "    </oleHoldings>\n" +
            "    <items>\n" +
            "      <item>\n" +
            "        <staffOnlyFlag>false</staffOnlyFlag>\n" +
            "        <fastAddFlag>false</fastAddFlag>\n" +
            "        <extension reference=\"../../../oleHoldings/extension\"/>\n" +
            "      </item>\n" +
            "    </items>\n" +
            "  </instance>\n" +
            "</instanceCollection>"  ;


    public static final String BIB_CATEGORY_WORK = "work";
    public static final String BIB_TYPE_BIBLIOGRAPHY = "bibliographic";
    public static final String BIB_TYPE_INSTANCE = "instance";
    public static final String BIB_FORMAT_MARC = "marc";
    public static final String BIB_FORMAT_OLEML = "oleml";
    public static final String ITEM_DOC_TYPE                                   = "item";
    public static final String HOLDING_DOC_TYPE                                = "holdings";
    public static final String NEW_ITEM_ID                                     = "NEW_ITEM";
    public static final String INGEST_OPERATION                                = "ingest";

    public static final String PRORATE_BY_QTY = "QTY";
    public static final String PRORATE_BY_DOLLAR = "DOLLAR";
    public static final String MANUAL_PRORATE = "MANUAL";
    public static final String NO_PRORATE = "NO_PRORATE";
    public static final String DEFAULT_PRORATE_BY_INVOICE = "DOLLAR";
    public static final String RCV_LN_ITM_IDN = "receivingLineItemIdentifier";
    public static final String LN_ITM_IDN= "itemIdentifier";

    public static final String YEAR_END_ACCOUNTING_PERIOD_EDIT_PERMISSION = "Edit Accounting Period";
    public static final String YEAR_END_ACCOUNTING_PERIOD_VIEW_PERMISSION = "View Accounting Period";
    public static final String YEAR_END_ACCOUNTING_PERIOD_EDIT_DOCUMENT_ACTION = "AccountingPeriodEditAction";
    public static final String YEAR_END_ACCOUNTING_PERIOD_VIEW_DOCUMENT_ACTION = "AccountingPeriodViewAction";

    public static class LicenseRequest{
        public static final String CANCEL_ANNOTATION = "Cancelled from License Request";
        public static final String APPROVE_ANNOTATION = " approved for ";
        public static final String REQUISITIONS = "requisitions";
        public static final String REQUISITIONS_DOC_NUMBER = "documentNumber";

    }

    public static final String REQ_SRC_CD = "STAN";
    public static final String OLE_FS_KEYSTORE_FILE_PROPERTY = "keystore.file";
    public static final String OLE_FS_DEFAULT_KEYSTORE_FILE_LOCATION_PROPERTY = "keystore.file.default";
    public static final String ORD_TYPE_FIRM_FIX = "Firm, Fixed";
    public static final String ITEM = "ITEM";
    public static final String APPROVAL = "Approval";
    public static final String FIRM_MUL_PART = "Firm (multi part)";

    public static final String OVER = "Over";
    public static final String UNDER = "Under";
    public static final String NONE = "None";
    public static final String PERCENTAGE = "%";
    public static final String HASH = "#";

    public static final String NOTES_TAB_ERROR= "document.oleFinancialNotes";

    public static final String HAS_VENDOR_DEPOSIT_ACCOUNT = "HasVendorDepositAccount";
    public static final String CLEARING_ACCOUNT_CODE = "CLRREV";
    public static final String OLE_PREPAYMENT = "OLE_Prepayment";
    public static final String REQUIRES_SEPARATION_OF_DUTIES= "RequiresSeparationOfDutiesReview";
    public static final String OLE_FUND_LOOKUP = "oleFundLookup.do";
    public static final int START_INDEX = 24;
    public static final int END_INDEX = 28;
    public static final String BAL_TYP_CD = "CB";
    public static final String FIN_DOC_STS_CD="R";
    public static final String FIN_DOC_STS_APP_CD                             = "A";
    public static final String ERROR_MSG_FOR_INSUFF_FUND                      = "insuff.fund.preq";
    public static final String INSUFF_FUND                                    = "Insufficient fund to continue PREQ for this account number ";

    public static class Account {
        public static final String ACCOUNT_NAMESPACE            = CoreModuleNamespaces.SELECT;
        public static final String ACCOUNT_RESTRICTIONS= "Edit Restriction";
        public static final String ACCOUNT_SUFFICIENT_FUND = "Edit Sufficient fund check";
        public static final String ACCOUNT_RESTRICTED_STATUS_CD = "accountRestrictedStatusCode";
        public static final String ACCOUNT_SUFFICIENT_FUND_CODE = "accountSufficientFundsCode";
        public static final String UPLOAD_BUDGET = "Upload Budget";
        public static final String LABOR_BENEFIT_RATE_CATEGORY_CODE = "--";

    }

    public static final String ERROR_AMOUNT          = "error.amount";
    public static final String ACCOUNT_NEW_SRC_LINE  = "newSourceLine";
    public static final String ACCOUNT_NEW_TRGT_LINE = "newTargetLine";


    // Added for Jira OLE-1900 Starts

    public static final String ITEM_LOCATION_REQUIRED = "error.itemLocation.required";

    public static final String ITEM_COPIESANDPARTS_SHOULDNOT_BE_GREATERTHAN_ONE_EINSTANCE = "item.Copiesandparts.shouldnot.be.greatethanone.einstance";

    public static final String PART_ENUMERATION_COPY   = "ole.partEnumeration.copy";

    public static final String PART_ENUMERATION_VOLUME = "ole.partEnumeration.volume";

    public static final String ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED = "item.copies.itemCopies.greatethan.itemCopiesOrdered";

    public static final String TOTAL_OF_ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED = "total.of.item.copies.itemCopies.greatethan.itemCopiesOrdered";

    public static final String ITEM_STARTINGCOPYNUMBER_SHOULDNOT_BE_GREATERTHAN_ITEMCOPIESORDERED                            = "item.copies.startingCopyNumber.shouldnot.be.greatethan.itemCopiesOrdered";

    public static final String ADDITION_OF_ITEM_STARTINGCOPYNUMBER_AND_ITEMCOPIES_SHOULDNOT_BE_GREATERTHAN_ITEMCOPIESORDERED = "addition.of.item.startingCopyNumber.and.item.itemcopies.shouldnot.be.greatethan.itemCopiesOrdered";

    public static final String ITEM_ITEMCOPIES_OR_LOCATIONCOPIES_SHOULDNOT_BE_NULL                                           = "item.itemCopies.or.locationCopies.shouldnot.be.null";

    public static final String ADD_COPIES_AT_LINE_ITEM_WHEN_COPIESORDERED_AND_PARTSORDERED_GREATERTHAN_1                     = "add.copies.at.lineItem.when.copiesOrdered.and.partsOrdered.greaterThan.1";

    public static final String TOTAL_OF_ITEMCOPIES_SHOULDNOT_BE_LESSTHAN_ITEMQUANTITY                                        = "total.of.itemcopies.shouldnot.be.lessthan.itemQuantity";

    public static final String LOCATION_LEVEL_CODE_INSTITUTION                                                               = "Institution";

    public static final String LOCATION_LEVEL_CODE_LIBRARY                                                                   = "Library";

    public static final String LOCATION_LEVEL_CODE_CAMPUS                                                                    = "Campus";

    public static final String LOCATION_PRIMARY                                                                              = "true";

    public static final String LOCATION_STATUS                                                                               = "temporary";

    public static final String OLE_DOCSTORE_RESTFUL_URL                                                                      = "ole.docstore.restful.url";

    public static final String IDENTIFIER_TYPE                                                                               = "identifierType";

    public static final String UUID                                                                                          = "UUID";

    public static final String OPERATION                                                                                     = "operation";

    public static final String DELETE                                                                                        = "delete";

    public static final String DOC_CATEGORY                                                                                  = "docCategory";

    public static final String DOC_TYPE                                                                                      = "docType";

    public static final String DOC_FORMAT                                                                                    = "docFormat";

    public static final String PO_RECEIPT_STATUS_PARTIALLY_RECEIVED                                                          = "PR";

    public static final String PO_RECEIPT_STATUS_FULLY_RECEIVED                                                              = "FR";

    public static final String PO_RECEIPT_STATUS_NOT_RECEIVED                                                                = "NR";

    public static final String DOC_NUMBER                                                                                    = "documentNumber";

    public static final String RCPT_STATUS_CD                                                                                = "receiptStatusCd";

    public static final String PO_IDNTFR                                                                                     = "purchaseOrderIdentifier";

    public static final String RCPT_STS_DOC_TYP                                                                              = "receiptStatusDocType";

    public static final String RCV_RCPT_STS_DOC_TYP                                                                          = "RCV";

    public static final String RCV_RECEIPT_STATUS_RCVD                                                                       = "RCVD";

    public static final String RCV_RECEIPT_STATUS_EXPTD                                                                      = "EXP";

    public static final String RCV_RECEIPT_STATUS_NONE                                                                       = "NON";

    public static final String COMMA_TO_SEPARATE_ENUMERATION                                                                 = ", ";

    public static final String DOT_TO_SEPARATE_COPIES_PARTS                                                                  = ".";

    // Added for Jira OLE-1900 Ends

    public static final String VOLUME_NUMBER_VALIDATIONS                                                                     = "error.volumeNumber.validations";

    public static final String VOLUME_NUMBER_REGEX_VALIDATIONS                                                               = "error.volumeNumber.regex.validations";

    public static final String VOLUME_NUMBER_PATTERN                                                                         = "^([0-9]+,?)+$";

    public static final String USER_ID                                                                                       = "userId";

    public static class OlePersonRequestorLookupable {
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME  = "lastName";
        public static final String EMAIL      = "email";
        public static final String EMAIL_ADDRESS     = "emailAddress";
        public static final String PHONE_NUMBER      = "phoneNumber";
        public static final String ID           = "id";
        public static final String REQUESTOR_TYPE_ID = "requestorTypeId";
        public static final String REF_KRIM_ID       = "refKrimId";
        public static final String PRINCIPAL_ID      = "principalId";
        public static final String REQUESTOR_FIRST_NAME = "requestorFirstName";
        public static final String REQUESTOR_LAST_NAME  = "requestorLastName";
        public static final String REQUESTOR_EMAIL      = "requestorEmail";
        public static final String REQUESTOR_PHONE_NUMBER = "requestorPhoneNumber";
        public static final String REQUESTOR_ID           = "requestorId";
        public static final String NULLSTRING = "null";
        public static final String EMPTY = "";
    }

    public static final String ACCOUNT_FUND_CODE = "A";

    public static final String OBJECT_FUND_CODE  = "O";

    public static final int    DATE_START_INDEX  = 6;

    public static final int    DATE_END_INDEX    = 10;

    public static final String BLOCK_USR_KEY     = "block_user";
    public static final String BLOCK_USR_VAL     = "Block User";
    public static final String WAR_USR_KEY       = "warning";
    public static final String WAR_USR_VAL       = "Warning";
    public static final String NOT_USR_KEY       = "notification";
    public static final String NOT_USR_VAL       = "Notification";
    public static final String ROU_USR_KEY       = "routing";
    public static final String ROU_USR_VAL       = "Routing";
    public static final String ACC_NUM           = "Account number ";
    public static final String EXC_BUD_AMT       = " exceeds budget amount";
    public static final String ITM_TYP_CD        = "ITEM";
    public static final String ITM_TYP_CD_KEY    = "itemTypeCode";
    public static final String PUR_AP_IDEN       = "purapDocumentIdentifier";

    public static class SufficientFundCheck {
        public static String       REQUISITION_SFC_CHECKING        = "message.requisition.sfc.checking";
        public static String       INVOICE_SFC_CHECKING             = "message.invoice.sfc.checking";
        public static String       REQUISITION_SFC_CHECKING_STRING = "Sufficient Fund Check";
        public static final String ERROR_MSG_FOR_INSUFF_FUND       = "insuff.fund.req";
        public static final String INSUFF_FUND_REQ                 = "Insufficient fund to continue Requisition for this account number ";
        public static final String INSUFF_FUND_INV                 = "Insufficient fund to continue Invoice for this account number ";
        public static final String DATE_FORMAT                     = "MM/dd/yyyy";
        public static final String REQ_NOTE                        = "Routed this document to budget approval due to insufficient fund";
        public static final String PO_NOTE                         = "Routed this document to budget approval due to insufficient fund";
        public static final String POA_NOTE                        = "Routed this document to budget approval due to insufficient fund";
        public static final String PREQ_NOTE                       = "Routed this document to budget approval due to insufficient fund";
        public static final String INV_NOTE                        = "Routed this document to budget approval due to insufficient fund";
        public static final String INSUFF_FUND_POA                 = "Insufficient fund to continue POA for this account number ";
        public static final String FYI_NOTE                        = "Sent FYI to budget reviewer due to insufficient fund";

    }

    public static class OleCopy {
        public static final String BIB_ID = "bibId";
        public static final String LOC = "location";
        public static final String INSTANCE_ID = "instanceId";
        public static final String REQ_ITM_ID = "reqItemId";
        public static final String PO_DOC_NUM = "poDocNum";
        public static final String PO_ITM_ID = "poItemId";
        public static final String COPY_NUM = "copyNumber";
        public static final String COPY_ID = "copyId";


    }
    public static class InvoiceDocument {
        public static final String VENDOR_HEADER_IDENTIFIER="vendorHeaderGeneratedIdentifier";
        public static final String VENDOR_DETAIL_IDENTIFIER="vendorDetailAssignedIdentifier";
        public static final String VENDOR_ALIAS_NAME="vendorAliasName";
        public static final String VENDOR_NUMBER="vendorNumber";
        public static final String TITLE="title";
        public static final String AUTHOR="author";
        public static final String ISBN="isbn";
        public static final String VENDOR_NAME="vendorName";
        public static final String INVOICE_DOCUMENT_NUMBER = "documentNumber";
        public static final String INVOICE_PURAP_DOCUMENT_IDENTIFIER = "purapDocumentIdentifier";
        public static final String FDOC_NBR = "FDOC_NBR";
        public static final String PO_ID = "PO_ID";
        public static final String CMPNT_CD = "Invoice";
        public static final String VENDOR_NOT_FOUND = "Vendor not selected";
        public static final String ERROR_SELECT_INVOICE_ITEM = "error.select.invoiceItem";
        public static final String ERROR_DUPLICATE_INVOICE_DATE_NUMBER_VND = "error.duplicate.invoice.date.number";
        public static final String INVOICE_NUMBER = "invoiceNumber";
        public static final String INVOICE_DATE = "invoiceDate";
        public static final String VENDOR_GENERATED_IDENTIFIER = "vendorHeaderGeneratedIdentifier";
        public static final String VENDOR_DETAIL_ASSIGNED_GENERATED_IDENTIFIER = "vendorDetailAssignedIdentifier";
        public static final String INVOICE_DOCUMENT_INITIATED="Initiated";
        public static final String INVOICE_DOCUMENT_STATUS_IND="purchaseOrderCurrentIndicator";
        public static final String INVOICE_IDENTIFIER = "invoiceIdentifier";
        public static final List<String> getItemtypeCodes() {
            List<String> itemTypeCodes = new ArrayList<String>();
            itemTypeCodes.add(PurapConstants.ItemTypeCodes.ITEM_TYPE_FREIGHT_CODE);
            itemTypeCodes.add(PurapConstants.ItemTypeCodes.ITEM_TYPE_SHIP_AND_HAND_CODE);
            itemTypeCodes.add(PurapConstants.ItemTypeCodes.ITEM_TYPE_MIN_ORDER_CODE);
            itemTypeCodes.add(PurapConstants.ItemTypeCodes.ITEM_TYPE_MISC_CODE);
            return Collections.unmodifiableList(itemTypeCodes);
        }
    }

    public static final String VENDOR_NOT_FOUND = "error.vendor.notFound";
    public static final String NO_VENDOR = "error.no.vendor";
    public static final String NO_ACC_LINE = "error.no.acc.line";
    public static final String NO_ADD_ACC_LINE = "error.no.add.acc.line";
    public static final String NO_RECEIVING_ADDR = "error.no.recv.addr";
    public static final String NO_DELIVERY_ADDR = "error.no.delv.addr";
    public static final String VENDOR_NOT_SAME = "error.vendor.notSame";
    public static final String VENDOR_SELECT ="error.vendor.select";
    public static final String REFRESH_VENDOR_CALLER = "vendorLookupable";
    public static final String VENDOR_HEADER_IDENTIFIER="vendorHeaderGeneratedIdentifier";
    public static final String VENDOR_DETAIL_IDENTIFIER="vendorDetailAssignedIdentifier";
    public static final String VENDOR_ALIAS_NAME="vendorAliasName";
    public static final String VENDOR_DETAILS="vendorDetail";
    public static final String VENDOR_CONTRACT="vendorContract";
    public static final String REFRESH_DV_VENDOR_CALLER="disbursementPayeeLookupable";
    public static final String SFC_INSUFF_WARN = "Insufficient Fund to continue invoice for this account number ";
    public static final String ERROR_RECEIVING_EXIST="error.receiving.exist";
    public static final String ERROR_SELECT_PO_ITM="error.select.purchaseOrderItem";
    public static final String ERROR_SELECT_PO_ITM_FORMAT="error.select.purchaseOrderItem.format";
    public static final String SUFF_FUND_CHECK = "oleSufficientFundCheck";
    public static final String ERROR_SELECT_EMCUM_METHOD = "error.document.encum.required";
    public static final String ERROR_SELECT_EMCUM_CON_TYP = "error.document.con.typ.required";
    public static final String ERROR_SELECT_EMCUM_AMT = "error.document.enc.amt.required";
    public static final String ERROR_SELECT_EXP_AMT = "error.document.exp.amt.required";
    public static final String ERROR_SELECT_NOT_TYP = "error.document.not.typ.required";
    public static final String ERROR_SELECT_INVALID_DATE = "error.document.invalid.date";

    public static final String PATRON_ID = "olePatronId";
    public static final String PO_ITEM_ID = "itemIdentifier";
    public static final String PO_LINE_ITEM_URL = "/" + "DocHandler.do?command=displayDocSearchView&amp;docId=";
    public static final String INV_INSUFF_FUND = "Insufficient fund to continue invoice for this account number ";

    public static final String TITLE_SORT = "Title_sort";
    public static final String TITLE_DISPLAY = "Title_display";
    public static final String UUID_FOR_TITLE_SORT = "uuid";
    public static final String ERR_MSG_FOR_ACC_LINE =" is Restricted Account";
    public static final String DOC_TYP_CD = "OLE_BA";
    public static final String FDOC_APPR_CD = "A";
    public static final String DOCFORMAT                                                                                    = "DocFormat";

    public static final String INVOICE_COLLAPSE_SECTIONS_ON_PO_ADD = "COLLAPSE_SECTIONS_ON_PO_ADD";
    public static final String INITIAL_COLLAPSE_SECTIONS = "INITIAL_COLLAPSE_SECTIONS";
    public static final String OVERVIEW_SECTION= "Document Overview";
    public static final String VENDOR_INFO_SECTION = "Vendor Info";
    public static final String INVOICE_INFO_SECTION = "Invoice Info";
    public static final String PROCESS_ITEMS_SECTION = "Process Items";
    public static final String PROCESS_TITLES_SECTION = "Process Titles";
    public static final String CURRENT_ITEM_SECTION= "Current Items";
    public static final String ADDITIONAL_CHARGES_SECTION = "Additional Charges";
    public static final String ACCOUNT_SUMMARY_SECTION = "Account Summary";
    public static final String NOTES_AND_ATTACH_SECTION = "Notes and Attachments";
    public static final String ADHOC_RECIPIENT_SECTION = "Ad Hoc Recipients";
    public static final String ROUTE_LOG_SECTION = "Route Log";
    public static final String DELIVERY_SECTION= "Delivery";
    public static final String VENDOR_SECTION= "Vendor";
    public static final String TITLES_SECTION= "Titles";
    public static final String PAYMENT_INFO_SECTION= "Payment Info";
    public static final String ADDITIONAL_INSTUT_SECTION= "Additional Institutional Info";
    public static final String RELATED_DOCUMENT_SECTION= "View Related Documents";
    public static final String PAYMENT_HISTORY_SECTION= "View Payment History";
    public static final String INVOICE_SECTION= "Invoice Info";
    public static final String PROCESS_ITEM_SECTION= "Process Items";
    public static final String GENERAL_ENTRY_SECTION= "General Ledger Pending Entries";
    public static final String CREDIT_MEMO_INFO_SECTION= "Credit Memo Info";
    public static final String ITEMS_SECTION= "Items";


    public static final String DONOR_CODE = "donorCode";
    public static final String OLEInvoiceView_ProcessItems_AccountingLines = "OLEInvoiceView-processItems-accountingLines_line0_line0";
    public static final String ERROR_CHART_CODE_REQ = "error.required.field";
    public static final String ERROR_ACC_NUMB_REQ = "error.required.field";
    public static final String ERROR_OBJECT_CODE_REQ = "error.required.field";
    public static final String ERROR_DONOR_CODE = "error.donor.code.doesnt.exist";
    public static final String DONOR_CODE_EXISTS = "error.donor.code.exist";
    public static final String DEPOSIT = "Deposit";


    public static final String ERROR_FUND_CODE = "error.fund.code.doesnot.exist";
    public static final String EMPTY_FUND_CODE = "error.fund.code.empty";

    public static final String ERROR_REASON = "error.reason";
    public static final String ERROR_CANCELLATION_REASON_REQUIRED = "error.cancellation.reason.required";
    public static final String QUESTION_ACTION = "/oleReqPOAskQuestion.do";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String NON_PUBLIC = "nonPublic";
    public static final String NULL = "null";
    public static final String REQUISITION_CANCEL_NOTE_PREFIX = "Note entered while cancelling a Requisition :";
    public static final String CANCEL_TEXT = "document.question.cancel.text";
    public static final String DEFAULT_ORDER_TYPE_VALUE = "1";
    public static final String ASK_LOCATION_CHANGE = "document.ask.location.change";
    public static final String ITEM_LOCATION_CHANGE = "Item Location Change";

    public static final String ITEM_CATEGORY = "work";
    public static final String ITEM_TYPE = "item";
    public static final String ITEM_FORMAT = "oleml";
    public static final String INVALID_ACQUISITION_NUMBER = "error.invalid.acquisition.number";
    public static final String ITEM_WITHOUT_PO = "error.invalid.item.without.po";
    public static final String VENDOR_CUSTOMER_NUMBER = "vendorCustomerNumber";
    public static final String DOCSTORE_NODE= "-";

    public static final int ZERO = 0;
    public static final String PRINT = "print";
    public static final String ELECTRONIC = "electronic";
    public static final String NB_PRINT = "NB_PRINT";
    public static final String NB_ELECTRONIC = "NB_ELECTRONIC";
    public static final String EB_PRINT = "EB_PRINT";
    public static final String EB_ELECTRONIC = "EB_ELECTRONIC";

    public static final String OLE_MAILER = "oleMailer";
    public static final String OLE_VENDOR_EMAIL_OPTION = "Email";
    public static final String OLE_VENDOR_PDF_OPTION = "Pdf";
    public static final String MAIL_SUBJECT = " Purchase Order Document";
    public static final String MAIL_MESSAGE_BODY = "Hi,"+"\n\n"+" Kindly find the attached Purchase Order Document.";
    public static final String VENDOR_TRANS_FORMAT_ID = "vendorTransmissionFormatId";
    public static final String VENDOR_TRANS_TYPE_ID = "vendorTransmissionTypeId";
    public static final String OLE_VENDOR_EDI_OPTION = "Edi";
    public static final String PO_ID = "poItemId";
    public static final String VENDOR_HDR_GEN_ID = "vendorHeaderGeneratedIdentifier";
    public static final String ALIAS_TYP_ID = "aliasTypeId";
    public static final String EXTERNAL_VNDR_CD = "External Vendor Code";
    public static final String PRINCIPAL_ID="principalId";
    public static final String   FINAL_STATUS="F";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final String CHART_CODE = "chartOfAccountsCode";
    public static final String FISCAL_YEAR = "universityFiscalYear";
    public static final String OBJECT_CODE = "objectCode";
    public static final String CHART_CODE_NOT_FOUND = "The requested Chart Code does not exist";
    public static final String CHART_CODE_WILDCARD_SEARCH = "Wildcard search not applicable to chart code";
    public static final String ACC_NO_NOT_FOUND = "The requested Account Number does not exist";
    public static final String OBJ_CODE_NOT_FOUND = "The requested Object Code does not exist";
    public static final String UNIV_FIS_YR_FOUND = "No result found for requested fiscal year";

    public static final String REQ_DESC="REQ_DESC";
    public static final String OPERATOR_INITIALS="{OPERATOR_INITIALS}";
    public static final String CURRENT_DATE_TIME="{CURRENT_DATE_TIME}";
    public static final String INV_DESC = "INV_DESC";
    public static final String LINE_ITEM_RCV_DESC = "LINE_ITEM_RCV_DESC";
    public static final String PREQ_DESC = "PREQ_DESC";
    public static final String PO_DOC_ID = "{PO_DOC_ID}";
    public static final String VENDOR_NAME = "{VENDOR_NAME}";
    public static final String ORDER_TYP = "{ORDER_TYPE}";
    public static final String VND_ITM_ID = "{VND_ITM_ID}";
    public static final String ORDER_IMPORT_REQ_DESC = "ORDER_IMPORT_REQ_DESC";
    public static final String BILL_PHN_NBR = "BILL_PHN_NBR";
    public static final String DELIVERY_TO_NAME = "DELIVERY_TO_NAME";
    public static final String UOM = "UOM";
    public static final String REQUESTOR_PERSON_NAME = "REQUESTOR_PERSON_NAME";
    public static final String REQUESTOR_PERSON_PHONE_NUMBER = "REQUESTOR_PERSON_PHONE_NUMBER";
    public static final String REQUESTOR_PERSON_EMAIL_ADDRESS = "REQUESTOR_PERSON_EMAIL_ADDRESS";
    public static final String VENDOR_CONTRACT_DEFAULT_APO_LIMIT = "VENDOR_CONTRACT_DEFAULT_APO_LIMIT";
    public static final String PURCHASE_ORDER_AUTOMATIC_INDICATIOR = "PURCHASE_ORDER_AUTOMATIC_INDICATIOR";
    public static final String FIN_YEAR = "FIN_YEAR";
    public static final String CHART_OF_ACC_CD = "CHART_OF_ACC_CD";
    public static final String ORG_CODE = "ORG_CODE";
    public static final String FUND_SRC_CD = "FUND_SRC_CD";
    public static final String USE_TAX_IND = "USE_TAX_IND";
    public static final String DLVR_CMPS_CD = "DLVR_CMPS_CD";
    public static final String DLVR_BLDNG_OTHR_IND = "DLVR_BLDNG_OTHR_IND";
    public static final String DLVR_BLDNG_CD = "DLVR_BLDNG_CD";
    public static final String DLVR_BLDNG_LN_ADDR = "DLVR_BLDNG_LN_ADDR";
    public static final String DLVR_BLDNG_ROOM_NBR = "DLVR_BLDNG_ROOM_NBR";
    public static final String DLVR_CITY_NM = "DLVR_CITY_NM";
    public static final String DLVR_STATE_CD = "DLVR_STATE_CD";
    public static final String DLVR_POSTAL_CD = "DLVR_POSTAL_CD";
    public static final String DLVR_CNTRY_CD = "DLVR_CNTRY_CD";
    public static final String ITEM_TYPE_CD = "ITEM_TYPE_CD";
    public static final String LIST_PRICE = "LIST_PRICE";
    public static final String QTY = "QTY";
    public static final String PO_TRNS_MTH_CD = "PO_TRNS_MTH_CD";
    public static final String PO_CST_SRC_CD = "PO_CST_SRC_CD";
    public static final String LOCATION = "LOCATION";
    public static final String ORG_PO_LMT = "ORG_PO_LMT";
    public static final String RCV_REQ_INT = "RCV_REQ_INT";
    public static final String PREQ_APPRL_INT = "PREQ_APPRL_INT";
    public static final String BILL_NM = "BILL_NM";
    public static final String BILL_CITY_NM = "BILL_CITY_NM";
    public static final String BILL_CNTRY_CD = "BILL_CNTRY_CD";
    public static final String BILL_LIN_ADDR = "BILL_LIN_ADDR";
    public static final String BILL_POSTAL_CD = "BILL_POSTAL_CD";
    public static final String BILL_STATE_CD = "BILL_STATE_CD";

    public static final String ANGLE_BRACKET_LESS_THAN = "<";
    public static final String ANGLE_BRACKET_GREATER_THAN = ">";
    public static final String ALL_OBJ_CD = "*ALL*";
    public static final String ALL = "*";
    public static class OleFundLookupDocument {
        public static final String ACC_NAME = "accountName";
        public static final String ORG_CODE = "organizationCode";
        public static final String CHART_CODE = "chartOfAccountsCode";
        public static final String ACC_NO = "accountNumber";
    }

    public static final String VENDOR_LINK = "/kr/inquiry.do?methodToCall=start&amp;businessObjectClassName=org.kuali.ole.vnd.businessobject.VendorDetail&amp;vendorHeaderGeneratedIdentifier=";
    public static final String VENDOR_TYPE = "Vendor Instructions Note";
    public static final String NOTE_ID = "noteTypeId";

    public static final String CURRENCY_OVERRIDE = "error.currency.override";

    public static final String SOURCE_FOLDER = "SOURCE_FOLDER";
    public static final String LOG_FOLDER = "LOG_FOLDER";
    public static final String DESTINATION_FOLDER = "DESTINATION_FOLDER";
    public static final String BACKUP_FOLDER = "BACKUP_FOLDER";
    public static final String VENDOR_TRANSMISSION_FILE = "VENDOR_TRANSMISSION_FILE";
    public static final String VENDOR_DIRECTORY = "VENDOR_DIRECTORY";
    public static final String PARENT_FOLDER = "PARENT_FOLDER";
    public static final String LIQUIBASE_GL_ACCT_BAL_LOC = "/ole-app/ole-db/ole-liquibase/ole-liquibase-changeset/src/main/resources/ole-demo/general-ledger/GL_ACCT_BALANCES_T.csv";
    public static final String USER_DIR = "user.dir";
    public static final String SOURCE_FISCAL_YR = "sourceFiscalYear";
    public static final String DEST_FISCAL_YR = "destFiscalYear";
    public static final String RUN = "run";
    public static final String CARRY_FORWARD = "carryForward";
    public static final String SUB_ACCT_NO = "-----";
    public static final String SUB_OBJECT_CD = "---";
    public static final String XLS_FORMAT = ".xls";
    public static final String ROLLOVER_INGEST_SUCCESS = "ingest.successfull";
    public static final String FISCAL_YR_CONTROLLER = "OLEFiscalYearRolloverController.class";
    public static final String OLE_APP = "ole-app";
    public static final String SAMPLE_FILE = "/Sample.xls";
    public static final String PENDING_ENTRY_OPTION = "dummyBusinessObject.pendingEntryOption";
    public static final String CONSOLIDATION_OPTION = "dummyBusinessObject.consolidationOption";
    public static final String SUB_ACCT_NUMBER = "subAccountNumber";
    public static final String BACK_LOC= "backLocation";
    public static final String DOC_FORM_VALUE = "88888888";
    public static final String  SUB_OBJ_CD = "subObjectCode";
    public static final String  CONSOLIDATION = "Consolidation";
    public static final String  ALL_VALUE = "All";
    public static final String  PORTAL = "portal.do";
    public static final String OLE_COPY_CSV = "OleCopyFiscalYearCSVData.groovy";
    public static final String OLE_COPY_SCHEMA = "OleCopyFiscalYearData.groovy";
    public static final String INVALID_FILE = "invalid.file";
    public static final String REENCUM_RECURR = "ReEncumberRecuring";
    public static final String BIBIMPORT_FILE_PATTERN = "Job";
    public static final String ROLLOVER_DIRECTORY = "/rollover";
    public static final String PO_BULK_AMEND_OUT_FILE_NM = "POBA-Report.txt";
    public static final String POBA_DIRECTORY = "/poba";
    public static final String POBA_FILE = "PurchaseOrderBulkAmendmentFile";
    public static final String POBA_LOG_FILE = "PurchaseOrderBulkAmendmentLog";
    public static final String PO_DOC_CURR_IND = "purchaseOrderCurrentIndicator";
    public static final String PO_DOC_STAT_CLOSED = "Closed";
    public static final String SEQ_NBR = "sequenceObject";
    public static final String INDEX_NBR = "indexNo";
    public static final String RECURRING_PAY_TYP = "RECURRING_PAY_TYP_CD";
    public static final String ITEM_LOCATION_FIRM_FIXD = "ITEM_LOCATION_FIRM_FIXD";
    public static final String ITEM_LOCATION_APPROVAL = "ITEM_LOCATION_APPROVAL";
    public static final String COPY_NUMBER = "COPY_NO";
    public static final String ITEM_STATUS_FIRM_FIXD = "ITEM_STATUS_FIRM_FIXD";
    public static final String ITEM_STATUS_APPROVAL = "ITEM_STATUS_APPROVAL";
    public static final String ERROR_REPORT = "ErrorReport";
    public static final String DATE_FORM_PO_BEGN_DT                = "MM/dd/yyyy";
    public static final String DATE_FORM_PO_END_DT = "12/31/9999";
    public static final String THREAD_POOL_SIZE = "AUTO_CLOSE_PO_THREAD_POOL_SIZE";
    public static final String ERESOURCE = "E-Resource";
    public static final String CANCEL_PDF_CREATION = "POBA_CANCEL_PDF_CREATION";
    public static final String POA_BATCH_NOTE = "Amended in batch by PO document";
}