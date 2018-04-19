package org.kuali.ole;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OleTemporaryCirculationHistory;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/7/12
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEConstants {

    public static final String EXCEED_MAX_COUNT = "exceed.max.count";
    public static final String LICENSE_ID = "licenseId";
	public static final String EMPTY_ITEM_BARCODE ="empty.item.barcode";
    public static final String ERES_ACCESS_ACTIVATION_CONFIG_ACTION_LINK = "oleAccessActivationConfiguration";
    public static final String OLE_ACCESS_ACTIVATION = "OLEAccessActivationConfigurationMaintenanceDocument-AccessActivationWorkflow";
    public static final String OLE_ACCESS_ACTIVATION_NOTIFIER = "OLEAccessActivationConfigurationMaintenanceDocument-mailConfiguration";
    public static final String OLE_ALERT_SECTION = "OLE-AlertSection";
    public static final String OLEERESOURCE_ACCESS_ADHOC_RECIPIENTS = "OLEEResourceAccess-AdHocRecipients";
    public static final String ACCESS_ACTIVATION_CONFIGURATION_CONTROLLER = "oleAccessActivationConfiguration";
    public static final String NO_WORKFLOW_SELECTED = "error.workflow.not.selected";
    public static final String ACCESS_ROLE_ID="id";
    public static final String ACCESS_ROLE_NAME="name";
    public static final String ALERT_GROUP_NAME="name";
    public static final String ALERT_ROLE_NAME="name";
    public static final String ACCESS_ROLE_NAME_ID_FIELD="dataobject.name";
    public static final String ACCESS_ROLE_NAME_FIELD="dataobject.name";
    public static final String ACCESS_ROLE_ID_FIELD="dataobject.name";
    public static final String ACCESS_STATUS="status";
    public static final String ACCESS_NAME="workflowName";
    public static final String ACCESS_STATUS_FIELD="dataobject.status";
    public static final String ACCESS_NAME_FIELD="dataobject.workflowName";
    public static final String ORDER_NO_FIELD="dataobject.orderNo";
    public static final String ORDER_NO="orderNo";
    public static final String SELECTOR_ROLE = "Role";
    public static final String SELECTOR_PERSON = "Person";
    public static final String SELECTOR_GROUP = "Group";
    public static final String ALERT_ROLE_URL = "/" + "identityManagementRoleInquiry.do?methodToCall=inquiry&amp;id=";
    public static final String ALERT_GROUP_URL = "/" + "identityManagementGroupInquiry.do?methodToCall=inquiry&amp;id=";
    public static final String ALERT_PERSON_URL = "/" + "identityManagementPersonInquiry.do?principalId=";
    public static final String PATRON_ID_POLICY = "patronId";
    public static final String ITEM_ID_POLICY = "itemId";
    public static final String DUE_DATE_INFO = " Due date may be different than usual because of requests on this item. No renewals allowed.";
    public static final String INVOICE_ITEM_LIMIT = "INVOICE_ITEM_LIMIT";
    public static final String PURAP_DOC_IDENTIFIER = "purapDocumentIdentifier";
    public static final String ISBN = "isbn";
    public static final String ITEM_TITLE_ID = "itemTitleId";
    public static final String POSTING_YEAR = "postingYear";
    public static final String TOTAL_DOLLAR_AMOUNT = "financialDocumentTotalAmount";
    public static final String SUBSCRIPTION_RENEWAL = "Subscription_Renewal";

    public static final String FAST_ADD_LINK = "/ole-kr-krad/circFastAddController?viewId=CircFastAddItemView&methodToCall=start";
    public static final String ITEM_ALREADY_LOANED = "Item is already loaned.";
    public static final String ITEM_ALREADY_LOANED_REDIRECT_URL = "?viewId=PatronItemView&methodToCall=start&formKey=";

    public static final String SERIAL_SINGLE_SEC_LIMIT = "SERIAL_SINGLE_SEC_LIMIT";
    public static final String SERIAL_MULTI_SEC_LIMIT = "SERIAL_MULTI_SEC_LIMIT";
    public static final String SERIAL_SEARCH_LIMIT = "SERIAL_SEARCH_LIMIT";
    public static final String VIEW_ALL_REQUESTS= "View all requests";
    //calender
    public static final String CALENDAR_BEGIN_END_DATE = "error.begin.end.date";
    public static final String CALENDAR_GENERAL = "GeneralInfo";
    public static final String CALENDAR_GENERAL_ECH_DAY_WEEK = "error.general.each.day.of.week";
    public static final String CALENDAR_WEEKDAYS_OVERLAP = "error.week.days.overlap";
    public static final String CALENDAR_EXCEPTION = "ExceptionDay";
    public static final String CALENDAR_PERIOD = "ExceptionPeriod";
    public static final String CALENDAR_PERIOD_RANGE_OVERLAP_ERROR = "error.exception.perd.date.overlap";
    public static final String CALENDAR_DATE_RANGE_OVERLAP_ERROR = "error.exception.date.overlap";
    public static final String CALENDAR_PERIOD_DATE_RANGE_OVERLAP_ERROR = "error.exception.date.or.perd.date.overlap";
    /*public static final String CALENDAR_PERIOD_DATE_RANGE_OVERLAP_ERROR="error.exception.date.or.perd.date.overlap";
    public static final String CALENDAR_DATE_RANGE_OVERLAP_ERROR="error.exception.date.overlap";*/
    public static final String CALENDAR_OPEN_CLOSE_TIME_CHECK = "error.gen.open.close.time";
    public static final String CALENDAR_OPEN_TIME = "error.gen.open.time.empty";
    public static final String CALENDAR_CLOSE_TIME = "error.gen.close.time.empty";
    public static final String CALENDAR_WEEK_DAYS_SAME = "error.week.days.same";
    public static final String CALENDAR_GENERAL_LIST = "error.gen.list";
    public static final String CALENDAR_EXCEPTION_VALIDATE = "error.exp.day";
    public static final String CALENDAR_EXCEPTION_DUPLICATE = "error.exp.day.twice";
    public static final String CALENDAR_EXCEPTION_DATE_EMPTY = "error.exp.date";
    public static final String CALENDAR_EXCEPTION_TYPE_EMPTY = "error.exp.type";
    public static final String CALENDAR_EXCEPTION_OPEN_TIME_EMPTY = "error.exp.open.time";
    public static final String CALENDAR_EXCEPTION_CLOSE_TIME_EMPTY = "error.exp.close.time";
    public static final String CALENDAR_EXCEPTION_OPEN_TIME = "error.exp.par.open.time";
    public static final String CALENDAR_EXCEPTION_CLOSE_TIME = "error.exp.par.close.time";
    public static final String CALENDAR_PERIOD_BEGIN = "error.exp.period.begin.day";
    public static final String CALENDAR_PERIOD_END = "error.exp.period.end.day";
    public static final String CALENDAR_PERIOD_BEGIN_END = "error.exp.period.begin.end.day";
    public static final String CALENDAR_PERIOD_BEGIN_DATE_EMPTY = "error.beg.date";
    public static final String CALENDAR_PERIOD_END_DATE_EMPTY = "error.end.date";
    public static final String CALENDAR_PERIOD_EXP_PRD_TYP = "error.exp.prd.typ";
    public static final String CALENDAR_PERIOD_LIST_EMPTY = "error.per.list";
    public static final String CALENDAR_EXCEPTION_TYPE = "Holiday";
    public static final String CALENDAR_GET_OPEN_TIME = "openTime";
    public static final String CALENDAR_GET_CLOSE_TIME = "closeTime";
    public static final String CALENDER_ID = "calendarGroupId";
    public static final String CALENDER_SUN = "Sunday";
    public static final String CALENDER_MON = "Monday";
    public static final String CALENDER_TUE = "Tuesday";
    public static final String CALENDER_WED = "Wednesday";
    public static final String CALENDER_THU = "Thursday";
    public static final String CALENDER_FRI = "Friday";
    public static final String CALENDER_SAT = "Saturday";
    public static final String CALENDER_STARTDATE = "temp.strt.end.date.new.cal";
    public static final String CALENDER_FLAG = "INCLUDE_NON_WORKING_HRS";
    public static final String GRACE_PERIOD_FOR_NON_WORKING_HOURS = "GRACE_PERIOD_FOR_NON_WORKING_HOURS";
    public static final String FINE_FLAG = "INCLUDE_NON_WORKING_HRS_FOR_FINE_CAL";
    public static final String CLAIMS_CHECKED_IN_FLAG = "CLAIMS_CHECKED_IN_NOTE";
    public static final String CLAIMS_CHECKED_OUT_FLAG = "CLAIMS_CHECKED_OUT_NOTE";
    public static final String MISSING_PIECE_ITEM_CHECKED_IN_FLAG = "MISSING_PIECE_CHECKED_IN_NOTE";
    public static final String MISSING_PIECE_CHECK_IN_NOTE_ADD = " with a missing piece check-in note : ";
    public static final String DAMAGED_ITEM_CHECKED_IN_FLAG = "DAMAGED_PIECE_CHECKED_IN_NOTE";
    public static final String DAMAGED_ITEM_CHECKED_OUT_FLAG = "DAMAGED_PIECE_CHECKED_OUT_NOTE";
    public static final String MISSING_PIECE_ITEM_CHECKED_OUT_FLAG = "MISSING_PIECE_CHECKED_OUT_NOTE";
    public static final String MISSING_PIECE_ITEM_REPLACE_MESSAGE = "Missing Piece";

    public static final String CLAIM_ERROR_MESSAGE = "No Records Found";
    public static final String CALENDAR_MAINTENANCE_ACTION_LINK = "OleCalendarController";
    public static final String OLE_CALENDAR_DOC_SERVICE = "oleCalendarDocumentService";
    public static final String GROUP_ID = "groupId";
    public static final String EXCLUDE_ITEM_STATUS = "EXCLUDE_ITEM_STATUS";
    public static final String MAIN_RCV_REC_TYP = "Main";
    public static final String SUPPLEMENT_RCV_REC_TYP = "Supplementary";
    public static final String INDEX_RCV_REC_TYP = "Index";
    public static final String SERIAL_SEARCH = "serial_search";
    public static final String PO_SEARCH = "po_search";
    public static final String JOURNAL_TITLE_SEARCH = "JournalTitle_search";
    public static final String TITLE_SEARCH = "Title_search";
    public static final String ISSN_SEARCH = "ISSN_search";
    public static final String BIB_SEARCH = "bibIdentifier";
    public static final String LOCALID_SEARCH = "LocalId_display";
    public static final String LOCAL_IDENTIFIER = "localIdentifier";
    public static final String ISSN = "issn";
    public static final String INSTANCE_SEARCH = "instanceIdentifier";
    public static final String BIB_ID = "bibId";
    public static final String DOC_NUM = "documentNumber";
    public static final String CONFIRM_MSG_RETURN_TO_SEARCH = "Form has unsaved data. Do you want to leave anyway?";
    public static final String SERIAL_REC_DESC = "New Serial Receiving";
    public static final String SRR_ROUTE_NOTES = "This document is routed for deletion";
    public static final String CAPTION_LEVEL_SEPARATOR = "-";
    public static final String ENUM_CAPTION1 = "ENUM_CAPTION1";
    public static final String ENUM_CAPTION2 = "ENUM_CAPTION2";
    public static final String ENUM_CAPTION3 = "ENUM_CAPTION3";
    public static final String ENUM_CAPTION4 = "ENUM_CAPTION4";
    public static final String ENUM_CAPTION5 = "ENUM_CAPTION5";
    public static final String ENUM_CAPTION6 = "ENUM_CAPTION6";
    public static final String ENUMERATION_CAPTION = "ENUMERATION_CAPTION";

    public static final String CHRON_CAPTION1 = "CHRON_CAPTION1";
    public static final String CHRON_CAPTION2 = "CHRON_CAPTION2";
    public static final String CHRON_CAPTION3 = "CHRON_CAPTION3";
    public static final String CHRON_CAPTION4 = "CHRON_CAPTION4";
    public static final String CHRONOLOGY_CAPTION = "CHRONOLOGY_CAPTION";

    public static final String SERIAL_SEARCH_CREATE_NEW = "Create";
    public static final String SERIAL_SEARCH_SHOW_RECORD = "Show";
    public static final String INSTANCE_ID = "instanceId";
    public static final String HOLDINGS_ID = "holdingsId";
    public static final String DOC_FORMAT_ALL = "ALL";
    public static final String KUALI_RULE_SERVICE = "kualiRuleService";
    public static final String BILL_WISE = "billwise";
    public static final String ITEM_WISE = "itemwise";
    public static final String PAY_OUTSTANDING = "PAY_OUTSTN";
    public static final String PAY_PARTIALLY = "PAY_PAR";
    public static final String REQUEST_EXISTS = "Request exists for this item ";
    public static final String DAMAGED_CHECK_IN_HEADER = "<h1>Damaged Check-in</h1></br>";
    public static final String CHECK_IN_NOTE_HEADER = "<h1>Check-in Note</h1></br>";
    public static final String CAN_REMOVE_NOTE = "Can Remove Note";
    public static final String CIRC_DESK_REQUIRED = "Circulation Desk Name is required";
    public static final String CIRC_DESK_INVALID = "Circulation Desk Name is invalid";
    public static final String AUDIO_OPTION = "AUDIO_OPTION";
    public static final String INVAL_ITEM_STATUS = "Invalid Item Status";
    public static final String ITEM_STATUS_CODE = "itemAvailableStatusCode";
    public static final String OPTR_ID = "operatorId";
    public static final String VALID_FILE_FORMAT = "VALID_FILE_FORMAT";
    public static final String APPL_ID = "KUALI";
    public static final String APPL_ID_OLE = "OLE";
    public static final String SELECT_NMSPC = "OLE-SELECT";
    public static final String SELECT_CMPNT = "Select";
    public static final String ERESOURCE_CMPNT = "E-Resource";
    public static final String OVERLAY_OBJECT_CODE = "OVERLAY_OBJECT_CODE";
    public static final String OVERLAY_CHART_CODE = "OVERLAY_CHART_CODE";
    public static final String OVERLAY_ACCOUNT_NUMBER = "OVERLAY_ACCOUNT_NUMBER";
    public static final String PROFILE_NM = "profileName";
    public static final String OBJECT_CODE = "objectCode";
    public static final String CHART_CODE = "chartCode";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final String SUB_ACCOUNT_NUMBER = "subAccountNumber";
    public static final String FINANCIAL_SUB_OBJECT_CODE = "financialSubObjectCode";
    public static final String ACCOUNT_LINE_PERCENT = "percentage";
    public static final String SUB_ACCOUNT = "subAccount";
    public static final String SUB_OBJECT = "subObject";
    public static final String PROJECT = "project";
    public static final String ORG_REF_ID = "orgRefId";
    public static final String BIB_OVERLAY_DIRECTORY = "bibOverlay.directory";
    public static final String ACTION_START_TAG = "<actions>\n";
    public static final String ACTION_CLOSE_TAG = "\n</actions>";
    public static final String EDI = "edi";
    public static final String MRC = "mrc";
    public static final String NO_CIRC_POLICY_FOUND = "No Circulation Policy Set Found";
    public static final String RECALL_DUE_DATE = "recallDueDate";
    public static final String LOANED_DATE = "loanedDate";
    public static final String MINIMUM_LOAN_PERIOD = "minimumLoanPeriod";
    public static final String RECALL_LOAN_PERIOD = "recallLoanPeriod";
    public static final String MAX_NO_OF_DAYS_ONHOLD = "maxNumberOfDaysOnHold";
    public static final String NO_OF_DAYS_ON_HOLD = "numberOfDaysOnHold";
    public static final String DUE_DATE_DIFF_INTERVAL = "dueDateDiffInterval";
    public static final String REQ_EXPIRATION_DAY_LIMIT = "requestExpirationDay";
    public static final String REQ_EXPIRATION_DATE = "requestExpirationDate";
    public static final String DATA_OBJECT = "//newMaintainableObject/dataObject/";
    public static final String PATRON_TYPE_NAME = "olePatron/oleBorrowerType/borrowerTypeCode";
    public static final String ITEM_TYPE_NM = "itemType";
    public static final String SHELVING_LOCATION = "shelvingLocation";
    public static final String MAX_NO_OF_PAGE_REQUEST = "maxNumberOfPageRequest";
    public static final String MAX_NO_OF_ASR_REQUEST = "maxNumberOfASRRequest";
    public static final String MAX_NO_OF_HOLD_REQUEST = "maxNumberOfHoldRequest";
    public static final String MAX_NO_OF_RECALL_REQUEST = "maxNumberOfRecallRequest";
    public static final String NOT_CHECK_OUT_STATUS = "AVAILABLE";
    public static final String RECENTLY_RETURNED = "RECENTLY-RETURNED";
    public static final String ITEM_STATUS_RECENTLY_RETURNED = "Recently Returned";
    public static final String ITM_CHECKIN_MSG = "Item is not loaned.";
    public static final String MARC_XML = "marc_xml";
    public static final String FINE_AMOUNT="fineAmount";
    public static final String FINE_AMOUNT_FROM="fineAmountFrom";
    public static final String FINE_AMOUNT_TO="fineAmountTo";
    public static final String FINE_ITEM_DUE_DATE = "Fine Item Due Date";
    public static final String CURR_CIR_DESK="CurrentCirculationDesk";
    public static final String ALL_CIR_DESK="AllCirculationDesk";
    public static final String PTRN_RQST_MSG_CURR_CIR_DESK="This patron has hold(s) waiting for pickup at this location.  Do you want to proceed?";
    public static final String PTRN_RQST_MSG_ALL_CIR_DESK="This patron has hold(s) waiting for pickup at another location.  Do you want to proceed?";
    //public static final String FIELD_035 = "035";
    public static final String OLE_BIB_RECORD = "oleBibRecord";
    public static final String ID = "id";
    public static final String UNAUTHORIZED_LOAN_USER = "is not authorized to perform this function.";
    public static final String ITEM_TITLE = "Notice";
    public static final String ITEM_ID = "Item_Id";
    public static final String INVALID_CRON = "dataObject.jobCronExpression";
    public static final String OLEML_FORMAT = "oleml";
    public static final String WORK_CATEGORY = "work";
    public static final String BIB_DOC_TYPE = "bibliographic";
    public static final String NO_LOC_CIR_DESK = "Circulation desk contains no location.Please add a location to it.";
    public static final String INSTANCE_DOC_TYPE = "instance";
    public static final String MARC_FORMAT = "marc";
    public static final String DUBLIN_FORMAT = "dublin";
    public static final String UNQUALIFIED_DUBLIN_FORMAT = "dublinunq";
    public static final String UPDATE_ITEM_FLAG = "updateItem";
    public static final String BIB_CREATION_FLAG = "bibliographicRecordCreated";
    public static final String HOLDING_DOC_TYPE = "holdings";
    public static final String OVERLAY = "overlay";
    public static final String OVERLAY_DELETE_REPLACE = "deletReplaceBibInstance";
    public static final String UPDATE_BIB_EXCLUDING_GPF = "updateBibExcludingGPF";
    public static final String UPDATE_BIB_INCLUDING_GPF = "updateBibIncludingGPF";
    public static final String LICENSE_DOC_TYPE = "license";

    public static final String EXCEPTION_CREATION_FLAG = "exceptionRouted";
    public static final String ISBN_TERM = "ISBN Term";
    public static final String ISBN_FUNCTION_DEF_NAME = "isbnFunction";
    public static final String ISSN_FUNCTION_DEF_NAME = "issnFunction";
    public static final String OCLC_FUNCTION_DEF_NAME = "oclcFunction";
    public static final String LOCATION_FUNCTION_DEF_NAME = "locationFunction";
    public static final String VENDOR_LINEITEM_REF_NUM_FUNCTION_DEF_NAME = "vendorLineItemReferenceFunction";
    public static final String ITEM_BARCODE_FUNCTION_DEF_NAME = "itemBarcodeFunction";
    public static final String EXISTING_FIELD = "existingfield";
    public static final String INCOMING_FIELD = "incomingfield";
    public static final String VENDOR_PROFILE_CODE = "vendorProfileCode";
    public static final String OLE_ORDER_RECORD = "oleOrderRecord";
    public static final String OLE_TX_RECORD = "oleTxRecord";
    public static final String PROFILE_ATTRIBUTE_LIST = "profileAttributesList";
    public static final String OVERLAY_OPTION_LIST = "overlayOptionList";
    public static final String BATCH_PROFILE_BO = "batchProcessProfileBo";
    public static final String OVERLAY_LOOKUP_ACTION_LIST = "overlayLookupActionList";
    public static final String DATA_CARRIER_SERVICE = "dataCarrierService";
    public static final String REQUEST_BIB_RECORD = "requestBibRecord";
    public static final String REQUEST_LINE_ITEM_ORDER_RECORD = "requestLineItemOrderRecord";

    public static final String DEFAULT_LOCATION = "defaultLocation";
    public static final String HIGHLEVEL_OVERLAY_OPTION = "highlevelOverlayOption";
    public static final String OVERLAY_DOCSTORE_OUTPUT_TARGET_OBJECT = "docstore";
    public static final String OVERLAY_REQUISITION_OUTPUT_TARGET_OBJECT = "requisition";
    public static final String RECEIPT_STATUS = "receiptStatus";
    public static final String OVERLAY_HELPER_SERVICE = "overlayHelperService";
    public static final String OVERLAY_RETRIVAL_SERVICE = "overlayRetrivalService";
    public static final String OVERLAY_DATAFIELD_SERVICE = "overlayDataFieldService";
    public static final String OVERLAY_LOOKUPTABLE_SERVICE = "overlayLookupTableService";
    public static final String OVERLAY_FILE_READER_FACTORY = "overlayFileReaderFactory";
    public static final String OVERLAY_OUTPUTSERVICE_FACTORY = "overlayOutputServiceFactory";
    public static final String OVERLAY_TABLELOOKUP_SERVICE = "overlayTableLookupService";
    public static final String OVERLAY_DOCSTORE_OUTPUT_SERVICE = "overlayDocstoreOutputService";
    public static final String OVERLAY_PROFILEMANAGER_SERVICE = "overlayProfileManagerService";
    public static final String OVERLAY_ACTION_SERVICE = "oleOverlayActionService";

    public static final String COMMON_IDENTIFIER_SEARCH = "common_identifier_search";
    public static final String OCLC_SEARCH = "035a";
    public static final String OVERLAY_CALLNUMBER = "callnumber";
    public static final String OVERLAY_ITEM_CALLNUMBER = "item.callNumber";
    public static final String OVERLAY_CALLNUMBER_TABLE = "callNumberTable";
    public static final String OVERLAY_CODES_TABLE = "codeTable";
    public static final String OVERLAY_BUDGET_CODE_TABLE = "budgetCodeTable";
    public static final String OVERLAY_ITEM_ITEMTYPE = "itemType";
    public static final String OVERLAY_ITEM_BARCODE = "barcodeARSL";
    public static final String OVERLAY_ITEM_STATISTICALSEARCHINGCODES = "statisticalSearchingCode";
    public static final String OVERLAY_ITEM_VENDOR_LINEITEM_IDENTIFIER = "vendorLineItemIdentifier";
    public static final String OVERLAY_ITEM_STAFF_ONLY_FLAG = "staffOnlyFlag";
    public static final String OVERLAY_ITEM_FAST_ADD_FLAG = "fastAddFlag";
    public static final String OVERLAY_ITEM_STATUS = "itemStatus";
    public static final String OVERLAY_ITEM_LOCATION = "location";
    public static final String OVERLAY_INSTANCE = "instance";
    public static final String OVERLAY_OLE_HOLDINGS = "oleHoldings";
    public static final String OVERLAY_ITEM = "item";
    public static final String OVERLAY_ORDERRECORD = "orderRecord";
    public static final String OVERLAY_TX_RECORD = "oleTxRecord";
    public static final String OVERLAY_CALLNUMBER_PREF_ONE = "callNumberPreferenceOne";
    public static final String OVERLAY_CALLNUMBER_PREF_TWO = "callNumberPreferenceTwo";
    public static final String OVERLAY_CALLNUMBER_PREF_THREE = "callNumberPreferenceThree";
    public static final String BIBlIOGRAPHICUUID = "bibUUID";
    public static final String OVERLAY_INSTANCEUUID = "instanceUUID";
    public static final String OVERLAY_HOLDINGUUID = "holdingUUID";
    public static final String OVERLAY_ITEMUUID = "itemUUID";
    public static final String OVERLAY_OPTION_ADD = "add";
    public static final String OVERLAY_OPTION_DELETE = "delete";
    public static final String OVERLAY_OPTION_UPDATE = "update";
    public static final String OVERLAY_OPTION_DONTADD = "dontAdd";
    public static final String OVERLAY_OPTION_ALL_NEW_RECORDS = "ALL_NEW_RECORDS";
    public static final String OVERLAY_TABLENAME = "tableName";
    public static final String OVERLAY_NEXTACTION = "nextAction";
    public static final String OVERLAY_FIELDTOUPDATE = "fieldToUpdate";
    public static final String OVERLAY_lOOKUPFIELD_FOR_TABLELOOKUP = "lookupFieldForTableLookup";
    public static final String OVERLAY_USEVALUE = "usevalue";
    public static final String OVERLAY_LOOKUPVALUE = "lookupvalue";
    public static final String OVERLAY_FINALVALUE = "finalValue";
    public static final String OVERLAY_INPUTVALUE = "inputValue";
    public static final String OVERLAY_PROFILEID = "profileId";
    public static final String EDI_ORDER = "ediOrder";
    public static final String OVERLAY_NOTE = "note";
    public static final String OVERLAY_HOLDINGS_IDENTIFIER = "holdingsIdentifier";
    public static final String OVERLAY_FILE_FORMAT_ERROR = "error.overlay.file.format";
    public static final String OVERLAY_NEXT_ACTION_ERROR = "error.overlay.next.action";
    public static final String OVERLAY_TABLE_NAME = "error.overlay.table.name";
    public static final String OVERLAY_TABLE_FIELD_NAME = "error.overlay.table.field.name";
    public static final String OVERLAY_SOURCE_FIELD_NAME = "error.overlay.source.field.name";
    public static final String DELETE_PURCHASE_ORDER_FAIL_MESSAGE = "record.delete.po.message";
    public static final String DELETE_LOANED_FAIL_MESSAGE = "record.delete.loan.message";
    public static final String DELETE_REQUEST_FAIL_MESSAGE = "record.delete.request.message";
    public static final String RECORD_DELETE_MESSAGE = "record.delete.message";

    public static final String DATA_FIELD_985 = "985";
    public static final String SUB_FIELD_A = "a";
    public static final String DEFAULT_LOCATION_LEVEL_INSTITUTION = "UC";
    /*public static final String OVERLAY_OPTION_EXCLUDE_GPF = "EXCLUDE_GPF";
    public static final String OVERLAY_OPTION_INCLUDE_GPF = "INCLUDE_GPF";*/
    public static final String OVERLAY_OPTION_DONT_IGNORE_GPF = "DONT_IGNORE_GPF";
    public static final String OVERLAY_OPTION_IGNORE_GPF = "IGNORE_GPF";
    public static final String OVERLAY_OPTION_REPLACE_MATCHING_RECORDS_PROTECTEDFIELD = "REPLACE_MATCHING_RECORDS_PROTECTEDFIELD";
    public static final String OVERLAY_OPTION_REPLACE_MATCHING_RECORDS_KEEP_PROTECTEDFIELD = "MATCHING_RECORDS_KEEP_PROTECTEDFIELD";
    public static final String DELIMITER_DASH = "-";
    public static final String DELIMITER_HASH = "#";
    public static final String DELIMITER_DOLLAR = "$";

    public static final String FIXED_DUE_DATE_CONTROLLER = "oleFixedDueDate";

    public static final String OLE_CHART_CODE = "chartCode";
    public static final String OLE_ITEM_CHART_CODE = "itemChartCode";
    public static final String ORG_CODE = "orgCode";
    public static final String RECV_REQUIRED = "receivingRequired";
    public static final String CONTRACT_MANAGER = "contractMgr";
    public static final String ASSIGN_TO_USER = "assignToUser";
    public static final String USE_TAXIND = "useTaxInd";
    public static final String ORDER_TYPE = "orderType";
    public static final String FUNDING_SOURCE = "fundingSource";
    public static final String PAYREQ_POSITIVE_APPROVAL = "payReqPositiveApprovalReq";
    public static final String PURCHASE_CONFIRMATION_INDICATOR = "purchaseorderconfirmationindicator";
    public static final String REQUISITION_SOURCE = "requisitionSource";
    public static final String DELIVERY_CAMPUS = "deliveryCampus";
    public static final String BUILDING = "building";
    public static final String VENDOR_CHOICE = "vendorchoice";
    public static final String ROUTE_RQUESTER = "routeRequester";
    public static final String ITEM_TYPE = "itemType";
    public static final String PUBLIC_VIEW = "publicView";
    public static final String PO_TRAMISSION_METHOD = "poTransmissionMethod";
    public static final String INTERNAL_PURCHASING_LIMIT = "internalPurchasingLimit";
    public static final String COST_SOURCE = "costSource";
    public static final String PERCENT = "percent";
    public static final String ORDER_TYPE_VALUE = "Firm, Fixed";

    public static final String ERROR_MESSAGE_UPLOAD = "Please select either both raw marc,edi or marc,edi xml.";
    public static final String ERROR_AGENDA_NAME = "Please choose a valid agenda name";
    public static final String STAFF_UPLOAD_SUCCESS = "Staff Upload executed successfully. Please click Load Reports to view load summary or the OLE tab to return to the OLE Main Menu.";
    public static final String STAFF_UPLOAD_FAILURE = "Failed to perform Staff Upload.";
    public static final String STAFF_UPLOAD_UPDATE_SUCCESS = "Staff Upload updated successfully. Please click Load Reports to view load summary or the OLE tab to return to the OLE Main Menu.";
    public static final String PROFILE_BUILDER_SELECT_FILE = "Please select file to upload.";
    public static final String PROFILE_BUILDER_SUCCESS = "Profile File uploaded successfully.";
    public static final String PROFILE_BUILDER_FAILURE = "Failed to upload Profile File.";
    public static final String PROFILE_BUILDER_INVALID_SCHEMA = "Invalid Schema File Uploaded.";
    public static final String INVOICE_UPLOAD_SUCCESS = "Invoice Ingest executed successfully.";
    public static final String BATCH_ORDER_IMPORT_SUCCESS = "Staff Upload executed successfully.";
    public static final String BATCH_ORDER_IMPORT_FAILURE = "Failed to perform Staff Upload.";

    /*public static final String KRMS_BUILDER_SUCCESS = "info.krms.file.success";
    public static final String KRMS_BUILDER_FAILURE = "error.krms.file.failure";
    public static final String KRMS_BUILDER_SELECT_FILE = "error.krms.file.upload";
    public static final String KRMS_BUILDER_INVALID_SCHEMA = "error.krms.invalid.schema";*/
    public static final String OLE_CURRENT_DATE_FUNCTION = "currentDateFunction";
    public static final String CHECK_DIGIT_ROUTINE = "CheckDigitRoutine";
    public static final String OLE_CONTAINS_FUNCTION = "containsFunction";
    public static final String OLE_CIRC_POLICY_FOUND_FUNCTION = "circulationPolicyFoundFunction";
    public static final String OLE_RENEWAL_DATE_FUNCTION = "renewalDateFunction";

    //MARC EDITOR messages
    public static final String MARC_EDITOR_SUCCESS = "Record saved successfully. Please close the window to return to OLE.";
    public static final String MARC_EDITOR_FAILURE = "Failed to save record.";
    public static final String MARC_EDITOR_REQUIRED_MSG = "Minimum one leader field, Control Field-008 and one Data Field are required. ";
    public static final String OLE_DOCSTORE_RESPONSE_STATUS = "success";
    public static final String MARC_EDITOR_TITLE_245 = "245";
    public static final String MARC_EDITOR_TITLE_LETTER = "|a";
    public static final String MARC_EDITOR_INVALID_TITLE = "Minimum title field 245 and |a is required. ";
    public static final String MARC_EDITOR_TITLE_100 = "100";
    public static final String MARC_EDITOR_BIB_COPY_MESSAGE = "record.bib.copy.message";
    public static final String MARC_EDITOR_HOLDINGS_COPY_MESSAGE = "record.holdings.copy.message";
    public static final String MARC_EDITOR_ITEM_COPY_MESSAGE = "record.item.copy.message";
    public static final String MARC_EDITOR_EDITION_250 = "250";
    public static final String MARC_EDITOR_SERIES_490 = "490";
    public static final String MARC_EDITOR_PUBLISHER_260 = "260";
    public static final String MARC_EDITOR_POP_260 = "260";
    public static final String MARC_EDITOR_YOP_260 = "260";
    public static final String MARC_EDITOR_024 = "024";
    public static final String MARC_EDITOR_AUTHOR_100 = "100";
    public static final String MARC_EDITOR_022 = "022";
    public static final String MARC_EDITOR_020 = "020";
    public static final String MARC_EDITOR_IND1_8 = "8";
    public static final String MARC_EDITOR_IND1 = "";

    public static final String DOCSTORE_HELPER_SERVICE = "docstoreHelperService";
    public static final String DISCOVERY_HELPER_SERVICE = "discoveryHelperService";
    public static final String OLE_WEB_SERVICE_PROVIDER = "oleWebServiceProvider";
    public static final String OLE_NAMESPACE = "OLE";
    public static final String BIB_INFO_LIST_FROM_SOLR_RESPONSE = "bibRecordFromSOLRResponse";
    public static final String ITEM_DOC_TYPE = "item";
    public static final String SOURCEHOLDINGS_DOC_TYPE = "sourceHoldings";

    public static final String PROFILE_AGENDA_NM = "YBP";
    public static final String PROFILE_ATTRIBUTE_NM = "orderPerFile";
    public static final String PROFILE_ATTRIBUTE_VALUE = "SINGLE-ORDER";

    public static final String BIB_UNIQUE_ID = "uniqueId";
    public static final String BIB_INSTANCE_ID = "instanceIdentifier";
    public static final String BIB_ITEM_ID = "itemIdentifier";
    public static final String NOTE_TYPE = "public";
    public static final String LOCATION_LOCATIONLEVEL_NAME = "location.locationLevel.name";
    public static final String LOCATION_LOCATIONLEVEL_LEVEL = "location.locationLevel.level";
    public static final String PERMANENT = "permanent";
    public static final String TRUE = "true";
    public static final String NAMESPACE_CODE = "namespaceCode";
    public static final String COMPONENT_CODE = "componentCode";
    public static final String FEE_TYPE_NAME = "feeTypeName";
    public static final String DESK_LOCATION = "deskLocation";
    public static final String NEW_REQUEST_DOC = "New Request Document";
    public static final String EDIT_REQUEST_DOC = "Edited Request Document";
    public static final String COPY_REQUEST_DOC = "Copied Request Document";
    public static final String BIB = "bib";
    public static final String ITEM = "item";
    public static final String HOLDING = "holding";
    public static final String DOCUMENT_ITEM = "documentItem";
    public static final String GREGORIAN_PATTERN = "yyyy-mm-dd hh:mm:ss";
    public static final String BUILDING_ROOM_NUMBER = "buildingRoomNumber";
    public static final String LOAD_008_METHOD_TO_CALL = "loadControlField008";
    public static final String FORMAT_008_BOOKS = "books";
    public static final String FORMAT_008_COMPUTER = "computer";
    public static final String FORMAT_008_MAP = "map";
    public static final String FORMAT_008_MUSIC = "music";
    public static final String FORMAT_008_COUNTRES = "countRes";
    public static final String FORMAT_008_VISMAT = "visMat";
    public static final String FORMAT_008_MIXMAT = "mixMat";
    public static final String A = "a";
    public static final String T = "t";
    public static final String C = "c";
    public static final String D = "d";
    public static final String M = "m";
    public static final String E = "e";
    public static final String F = "f";
    public static final String I = "i";
    public static final String J = "j";
    public static final String K = "k";
    public static final String P = "p";
    public static final String R = "r";
    public static final String B = "b";
    public static final String S = "s";
    public static final String G = "g";
    public static final String O = "o";
    public static final String EDITOR_VIEW = "EditorView";
    public static final String WORK_BIB_EDITOR_VIEW_PAGE = "WorkBibEditorViewPage";
    public static final String WORK_HOLDINGS_VIEW_PAGE = "WorkHoldingsViewPage";
    public static final String WORK_ITEM_VIEW_PAGE = "WorkItemViewPage";
    public static final String WORK_EINSTANCE_VIEW_PAGE = "WorkEInstanceViewPage";
    public static final String EDITOR_WORKFORM_VIEW = "EditorWorkformView";
    public static final String EDITOR_WORKFORM_VIEW_PAGE = "EditorWorkformViewPage";
    public static final String LEADER_DEFAULT = "#####nam#a22######a#4500";
    public static final String DEFAULT_008 = "######s########xxu###########000#0#eng#d";
    public static final String DATAFIELD_020 = "020";
    public static final String DATAFIELD_022 = "022";
    public static final String DATAFIELD_100 = "100";
    public static final String DATAFIELD_245 = "245";
    public static final String DATAFIELD_250 = "250";
    public static final String DATAFIELD_260 = "260";
    public static final String DATAFIELD_300 = "300";
    public static final String DATAFIELD_490 = "490";
    public static final String INDICATOR_0 = "0";
    public static final String INDICATOR_1 = "1";
    public static final String PIPE = "|";
    public static final String DEFAULT_BULK_PO_CHANGE_THREAD_POOL_SIZE = "";


    public static final class OLEBatchProcess {
        public static final String SECTION_ID ="OLEBatchProcessProfileBo-MaintenanceView-MatchPointSection";
        public static final String OLE_BATCH_BIB_MATCH_POINT="error.batch.bib.match.point";
        public static final String OLE_BATCH_HOLDINGS_MATCH_POINT="error.batch.holdings.match.point";
        public static final String OLE_BATCH_EHOLDINGS_MATCH_POINT="error.batch.eholdings.match.point";
        public static final String OLE_BATCH_ITEM_MATCH_POINT="error.batch.item.match.point";

        public static final String ERROR_SELECT_FIELD = "errot.batch.fill.atleast.onefield";
        public static final String ERROR_DATE_FORMAT_FIELD_VALUE = "error.batch.filtercritria.dateFormat.date";
        public static final String ERROR_DATE_FORMAT_FIELD_RANGE = "error.batch.filtercritria.dateFormat.daterange";

        public static final String ERROR_NUMBER_FORMAT_FIELD_VALUE = "error.batch.filtercritria.numberFormat.number";
        public static final String ERROR_NUMBER_FORMAT_FIELD_RANGE = "error.batch.filtercritria.numberFormat.numberrange";

        public static final String SELECT_VALUE_FOR_FIELD_NAME = "error.batch.select.fieldName";
        public static final String ERROR_STRING_FORMAT_FIELDVALUE = "error.batch.filtercriteria.stringformat.field";
        public static final String ERROR_BATCH_FIELD_VIOLATION = "error.batch.field.violation";
        public static final String OLE_BATCH_FILTER_CRITERIA_MNT_ID = "OLEBatchProcessProfileBo-MaintenanceView-filterCriteriaSection";
        public static final String OLE_BATCH_FLTR_FIELD_VALUE = "FieldValue";
        public static final String OLE_BATCH_FLTR_FIELD_VALUE_RANGE_FROM = "FieldValueRangeFrom";
        public static final String OLE_BATCH_FLTR_FIELD_VALUE_RANGE_TO = "FieldValueRangeTo";
        public static final String OLE_BATCH_FLTR_DATE = "Date";
        public static final String OLE_BATCH_FLTR_DATE_RANGE = "Date Range";
        public static final String OLE_BATCH_FLTR_NUMBER = "Number";
        public static final String OLE_BATCH_FLTR_NUMBER_RANGE = "Number Range";
        public static final String OLE_BATCH_FLTR_STRING = "String";
        public static final String OLE_BATCH_FLTR_CRITERIA_LOAD_FROM_FILE = "Bib Local Id From File";
        public static final String OLE_BATCH_FLTR_CRITERIA_BIB_STATUS = "Bib Status";
        public static final String OLE_BATCH_SCHEDULE = "error.format.required";
        public static final String OLE_BATCH_SCHEDULE_TIME = "error.format.time";
        public static final String OLE_BATCH_SCHEDULE_ERR = "error.scheduler.time";
        public static final String OLE_BATCH_REQUIRED = "error.required.field";
        public static final String OLE_BATCH_DUPLICATE = "error.duplicate.field";
        public static final String OLE_BATCH_DATA_MAPPING_DATA_TYPE = "data.mapping.data.type";
        public static final String OLE_BATCH_DATA_MAPPING_SOURCE_FIELD = "data.mapping.source.field";
        public static final String OLE_BATCH_DATA_MAPPING_DESTINATION_FIELD = "data.mapping.destination.field";
        public static final String OLE_BATCH_DATA_MAPPING_DESTINATION_FIELD_VALUE = "data.mapping.destination.field.value";
        public static final String OLE_BATCH_DATA_MAPPING_DESTINATION_DATA_TYPE = "data.mapping.destination.data.type";
        public static final String OLE_BATCH_PROFILE_BIB_MATCH_POINT_SECTION_ID = "OLEBatchProcessProfileBo-MaintenanceView-bibMatchPointSection";
        public static final String OLE_BATCH_PROFILE_DATA_MAPPING_SECTION_ID = "OLEBatchProcessProfileBo-MaintenanceView-optionSection_line0";
        public static final String OLE_BATCH_PROFILE_FILTER_CRITERIA_SECTION_ID = "OLEBatchProcessProfileBo-MaintenanceView-filterCriteriaSection";
        public static final String OLE_BATCH_PROFILE_CONSTANT_SECTION_ID = "OLEBatchProcessProfileBo-MaintenanceView-profileConstantsSection";
        public static final String OLE_BATCH_BIB_MATCH_POINT_ERR = "error.bib.match.point";
        public static final String OLE_BATCH_BIB_DATA_MAPPING_FIELD_ERR = "error.bib.data.mapping.field";
        public static final String OLE_BATCH_BIB_DATA_MAPPING_IMPORT_DESTINATION_FIELD_ERR = "error.bib.data.mapping.import.destination.field";
        public static final String OLE_BATCH_BIB_DATA_MAPPING_IMPORT_DESTINATION_FIELD_ERESOURCE_ERR = "error.bib.data.mapping.import.destination.field.eresource";
        public static final String OLE_BATCH_BIB_DATA_MAPPING_IMPORT_SOURCE_FIELD_ERR = "error.bib.data.mapping.import.source.field";
        public static final String OLE_BATCH_BIB_DATA_MAPPING_EXPORT_DESTINATION_FIELD_ERR = "error.bib.data.mapping.export.destination.field";
        public static final String OLE_BATCH_BIB_DATA_MAPPING_EXPORT_SOURCE_FIELD_ERR = "error.bib.data.mapping.export.source.field";
        public static final String OLE_BATCH_BIB_FILTER_CRITERIA_FIELD_ERR = "error.bib.filter.criteria.field";
        public static final String BATCH_PROCESS_PROFILE_CONTROLLER = "oleBatchProcessProfile";

        public static final String BATCH_PROCESS_SCHEDULE_SECTION_ID = "BatchProcessDefinition-schedule";
        public static final String BATCH_PROCESS_SCHEDULED = "Scheduled";
        public static final String SCHEDULE_ONETIME = "onetime";
        public static final String SCHEDULE_RECURRING = "recurring";
        public static final String SCHEDULE_TYPE_DAILY = "Daily";
        public static final String SCHEDULE_TYPE_WEEKLY = "Weekly";
        public static final String SCHEDULE_TYPE_MONTHLY = "Monthly";
        public static final String NEW_BATCH_PRCS_DOCUMENT = "New Batch Process Document";
        public static final String TIME_AM = "AM";
        public static final String TIME_PM = "PM";
        public static final String PROFILE_JOB = "_Job";
        public static final String PROFILE_SCHEDULE = "_Scheduled";
        public static final String JOB_STATUS_SCHEDULED = "SCHEDULED";
        public static final String JOB_STATUS_RUNNING = "RUNNING";
        public static final String JOB_STATUS_CANCELLED = "CANCELLED";
        public static final String JOB_STATUS_STOPPED = "STOPPED";
        //public static final String JOB_STATUS_CANCELLED = "CANCELLED";
        public static final String JOB_STATUS_COMPLETED = "COMPLETED";
        public static final String JOB_STATUS_PAUSED = "PAUSED";
        public static final String ORDER_RECORD_IMPORT = "Order Record Import";
        public static final String BATCH_EXPORT = "Batch Export";
        public static final String UPLOAD_FILE = "error.upload.file";
        public static final String UPLOAD_MARC_FILE = "error.upload.marc.file";
        public static final String UPLOAD_EDI_FILE = "error.upload.edi.file";
        public static final String OUTPUT_FILE = "error.output.file";
        public static final String ONE_TIME_OR_RECUR_ERROR = "error.one.time.or.recur";
        public static final String DAILY_WEEKLY_MONTHLY = "error.daily.weekly.monthly";
        public static final String CRON_OR_SCHEDULE = "error.cron.or.schedule";
        public static final String ERROR_CRON_EXPRESSION = "error.cron.expression";
        public static final String JOB_STATUS_RESTART = "RESTART";
        public static final String JOB_STATUS_PAUSE = "PAUSE";
        public static final String JOB_STATUS_RESUME = "RESUME";
        public static final String BATCH_JOB = "BATCH_JOB_";
        public static final String PROVIDED_CRON = "ProvidedCron";
        public static final String BATCH_DELETE = "Batch Delete";
        public static final String BATCH_DELETE_INGEST_FILE_FORMAT = "error.batch.delete.ingest.file";
        public static final String BATCH_BIB_IMPORT_INGEST_FILE_FORMAT = "error.batch.bib.import.ingest.file";
        public static final String BATCH_EXPORT_INGEST_FILE_FORMAT = "error.batch.export.ingest.file";
        public static final String BATCH_DELETE_MATCH_POINT = "error.batch.delete.match.point";
        public static final String LOCATION_IMPORT_INGEST_FILE_FORMAT = "error.location.import.ingest.file";
        public static final String ERROR_EMAIL_ID = "error.email.id";
        public static final String PATRON_IMPORT_INGEST_FILE_FORMAT = "error.location.import.ingest.file";
        public static final String SERIAL_RECEIVING_IMPORT_INGEST_FILE_FORMAT = "error.serial.receiving.import.ingest.file";
        public static final String REQUIRED_VALUES_FOR_ORDER_RECORD_IMPORT = "required.order.record.import";
        public static final String PROFILE_NAME_REQUIRED = "required.profile.name";
        public static final String ERROR_AUTHORIZATION = "error.authorization";
        public static final String BATCH_BIB_IMPORT = "Bib Import";
        public static final String BATCH_INVOICE = "Invoice Import";
        public static final String PATRON_IMPORT = "Patron Import";
        public static final String CLAIM_REPORT = "Claim Report";
        public static final String GOKB_IMPORT = "GOKB Import";
        public static final String SERIAL_RECORD_IMPORT = "Serial Record Import";
        public static final String LOCATION_IMPORT = "Location Import";
        public static final String FUND_RECORD_IMPORT = "Fund Record Import";
        public static final String FUND_RECORD_SUCCESS = "Fund Record File uploaded successfully.";
        public static final String BATCH_PROCESS_PROFILE_ID = "batchProcessProfileId";
        public static final String CHECKIN = "checkIn";
        public static final String INSTANCE_INGEST = "instanceIngest";
        public static final String INGEST = "ingest";
        public static final String DATA_TO_IMPORT_BIB_INSTANCE = "Bibliographic, Holdings, and Item Data";
        public static final String DATA_TO_IMPORT_BIB_EINSTANCE = "Bibliographic and EHoldings Data";
        public static final String DATA_TO_IMPORT_BIB_INSTANCE_EINSTANCE = "Bibliographic,Holdings,Item and EHoldings Data";
        public static final String BIB_OVERLAY = "overlay";
        public static final String BIB_ADD = "add";
        public static final String BIB_NONE = "none";
        public static final String BATCH_PROCESS_PROFILE_DATATYPE_HOLDINGS = "Holdings";
        public static final String BATCH_PROCESS_PROFILE_DATATYPE_EHOLDINGS = "EHoldings";
        public static final String BATCH_PROCESS_PROFILE_DATATYPE_ITEM = "Item";
        public static final String BATCH_PROCESS_PROFILE_DATATYPE_BIBMARC = "Bibmarc";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER = "Holdings Call Number";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE = "Holdings Call Number Type";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER = "Holdings Copy Number";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_PREFIX = "Holdings Call Number Prefix";
        public static final String DESTINATION_FIELD_CALL_NUMBER = "Call Number";
        public static final String DESTINATION_FIELD_COPY_NUMBER = "Copy Number";
        public static final String DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY = "Donor Public Display";
        public static final String DESTINATION_FIELD_DONOR_NOTE = "Donor Note";
        public static final String DESTINATION_FIELD_DONOR_CODE = "Donor Code";
        public static final String DESTINATION_FIELD_DATE_CREATED = "Date Created";
        public static final String DESTINATION_FIELD_DATE_UPDATED = "Date Updated";
        public static final String PUBLIC_NOTE = "Public Note";
        public static final String SOURCE_FIELD_DATE_CREATED = "Date Created";
        public static final String SOURCE_FIELD_DATE_UPDATED = "Date Updated";
        public static final String LCC = "LCC";
        public static final String LOCAL_IDENTIFIER = "Local Identifier";
        public static final String DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE = "Call Number Type";
        public static final String DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX = "Call Number Prefix";
        public static final String DESTINATION_FIELD_HOLDING_LOCATION = "Location";
        public static final String DESTINATION_FIELD_LOCATION_LEVEL_1 = "Location Level1";
        public static final String DESTINATION_FIELD_LOCATION_LEVEL_2 = "Location Level2";
        public static final String DESTINATION_FIELD_LOCATION_LEVEL_3 = "Location Level3";
        public static final String DESTINATION_FIELD_LOCATION_LEVEL_4 = "Location Level4";
        public static final String DESTINATION_FIELD_LOCATION_LEVEL_5 = "Location Level5";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_1 = "Holdings Location Level1";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_2 = "Holdings Location Level2";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_3 = "Holdings Location Level3";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_4 = "Holdings Location Level4";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_5 = "Holdings Location Level5";
        public static final String DESTINATION_FIELD_ITEM_STATUS = "Item Status";
        public static final String DESTINATION_FIELD_LINK_TEXT = "Link Text";
        public static final String DESTINATION_FIELD_LINK_URL = "URL";
        public static final String DESTINATION_FIELD_PERSISTENTLINK = "Persistent Link";
        public static final String DESTINATION_FIELD_PUBLIC_DISPLAY_NOTE = "Public Display Note";
        public static final String DESTINATION_FIELD_PUBLIC_NOTE = "Public Note";
        public static final String DESTINATION_FIELD_COVERAGE_START_DATE = "Coverage Start Date";
        public static final String DESTINATION_FIELD_COVERAGE_START_ISSUE = "Coverage Start Issue";
        public static final String DESTINATION_FIELD_COVERAGE_START_VOLUME = "Coverage Start Volume";
        public static final String DESTINATION_FIELD_COVERAGE_END_DATE = "Coverage End Date";
        public static final String DESTINATION_FIELD_COVERAGE_END_ISSUE = "Coverage End Issue";
        public static final String DESTINATION_FIELD_COVERAGE_END_VOLUME = "Coverage End Volume";
        public static final String DESTINATION_FIELD_STATISTICAL_CODE = "Statistical Code";
        public static final String DESTINATION_FIELD_ACCESS_STATUS_CODE = "Access Status";
        public static final String DESTINATION_FIELD_MATIRIAL_SPECIFIED = "Materials Specified";
        public static final String DESTINATION_FIELD_FIRST_INDICATOR = "First Indicator";
        public static final String DESTINATION_FIELD_SECOND_INICATOR = "Second Indicator";
        public static final String DESTINATION_FIELD_PLATFORM = "Platform";
        public static final String DESTINATION_FIELD_ERESOURCE_NAME = "EResource Name";
        public static final String DESTINATION_FIELD_ERESOURCE_ID = "EResource Id";
        public static final String DESTINATION_FIELD_GOKB_ID = "Gokb Id";
        public static final String DESTINATION_FIELD_IMPRINT = "Imprint";
        public static final String DESTINATION_FIELD_PUBLISHER = "Publisher";
        public static final String DATE_ENTERED = "Date Entered";
        public static final String DESTINATION_FIELD_ITEM_ITEM_BARCODE = "Item Barcode";
        public static final String DESTINATION_FIELD_ITEM_ENUMERATION = "Enumeration";
        public static final String DESTINATION_FIELD_ITEM_CHRONOLOGY = "Chronology";
        public static final String DESTINATION_FIELD_VENDOR_LINE_ITEM_IDENTIFIER = "Vendor Line Item Identifier";
        public static final String CONTROL_FIELD_NAME_001 = "controlfield_001";
        public static final String CONTROL_FIELD_001 = "001";
        public static final String CONTROL_FIELD_003 = "003";
        public static final String DATA_FIELD_035 = "035";
        public static final String OLE = "OLE";
        public static final String DELETE_001 = "delete001";
        public static final String PREPEND_001_TO_035 = "prepend003To035";
        public static final String PREPEND_VALUE_TO_035 = "prependvalueto035";
        public static final String PROFILE_CONSTANT_DEFAULT = "default";
        public static final String LOCATION_LEVEL_SHELVING = "SHELVING";
        public static final String LOCATION_LEVEL_LIBRARY = "LIBRARY";
        public static final String LOCATION_LEVEL_INSTITUTION = "INSTITUTION";
        public static final String LOCATION_LEVEL_CAMPUS = "CAMPUS";
        public static final String LOCATION_LEVEL_COLLECTION = "COLLECTION";
        public static final String CHANGE_TAG_035 = "changeTo035";
        public static final String DONOT_CHANGE = "doNotChange";
        public static final String BIB_IMPORT_SUCCESS = "Bib Records Uploaded Succesfully";
        public static final String BIB_IMPORT_FAILURE = "Failed to Upload Bib Records";
        public static final String LOCALID_SEARCH = "LocalId_search";
        public static final String DELETE_ALL_ADD_NEW = "deleteAllAddNew";
        public static final String KEEP_ALL_ADD_NEW = "keepOldAddNew";
        public static final String RECORDS_CREATED_WITHOUT_LINK = "_BibFileWithoutLinkElement_";
        public static final String RECORDS_CREATED_WITH_MORE_THAN_ONE_LINK = "_BibFileWithMoreThanOneLinkElement_";
        public static final String HOLDINGS_MATCHED_MORE_THAN_ONE = "_MoreThanOneHoldingsMatched_";
        public static final String ITEMS_MATCHED_MORE_THAN_ONE = "_MoreThanOneItemMatched_";
        public static final String BIBS_MATCHED = "_BibMatched_";
        public static final String HOLDINGS_MATCHED = "_HoldingsMatched_";
        public static final String ITEMS_MATCHED = "_ItemsMatched_";
        public static final String BIBS_NO_MATCHED = "_No_BibMatched_";
        public static final String HOLDINGS_NO_MATCHED = "_No_HoldingsMatched_";
        public static final String ITEMS_NO_MATCHED = "_No_ItemsMatched_";
        public static final String MATCHED_BIB_IDS_FILE_NAME = "_Matched_Bibs.txt";
        public static final String MATCHED_HOLDINGS_IDS_FILE_NAME = "_Matched_Holdings.txt";
        public static final String MATCHED_ITEM_IDS_FILE_NAME = "_Matched_Holdingss.txt";
        public static final String  NO_MATCHED_BIB_FILE_NAME = "_NO_Matched_Bibs.txt";
        public static final String  NO_MATCHED_HOLDINGS_FILE_NAME = "_NO_Matched_Holdings.txt";
        public static final String  NO_MATCHED_ITEM_FILE_NAME = "_NO_Matched_Items.txt";

        public static final String OPERATION_INGEST = "ingest";
        public static final String LEVEL_CODE = "levelCode";
        public static final String LEVEL_NAME = "levelName";
        public static final String ITEM_TYPE = "Item Type";
        public static final String RESPONSE_STATUS_FAILED = "Failed";
        public static final String RESPONSE_STATUS_SUCCESS = "Success";
        public static final String ADDITIONAL_ATTRIBUTE_STATUS = "status";
        public static final String HOME = ConfigContext.getCurrentContextConfig().getProperty("project.home");
        public static final String DEFAULT = "default";
        public static final String CONSTANT = "constant";
        public static final String CHANGE = "change";
        public static final String OLE_BATCH_PRIORITY = "error.same.priority";

        public static final String MORE_THAN_ONE_MATCHING_FOUND_FROM_EXISTING_RECORD = "More than one matching found from existing record";
        public static final String NO_MATCHING_RECORD = "No matching record";
        public static final String MATCHING_POINT_BIB = "Matching point in incoming file does not have any value to perform matching";
        public static final String PROCESS_FAILURE = "Failed to process bib trees";
        public static final String NO_MATCH_DISCARD_BIB = "Bib discarded";
        // batch directories

        public static final String SERIAL_XML_CSV_FORMAT = "error.serial.input.both.format";
        public static final String SERIAL_REC_CSV = "error.serial.record.csv";
        public static final String SERIAL_TYP_CSV = "error.serial.type.csv";
        public static final String SERIAL_HIS_CSV = "error.serial.history.csv";
        public static final String RECORD_NAME_MISMATCH = "error.serial.record.name.mismatch";
        public static final String RECORD_TYPE_NAME_MISMATCH = "error.serial.type.name.mismatch";
        public static final String RECORD_HISTORY_NAME_MISMATCH = "error.serial.history.name.mismatch";
        public static final String RECORD_UPLOAD_CSV = "error.record.upload.scv";

        public static final String SERIAL_RECORD_NAME = "SERIAL_RECORD_CSV_NAME";
        public static final String SERIAL_TYPE_NAME = "SERIAL_TYPE_CSV_NAME";
        public static final String SERIAL_HISTORY_NAME = "SERIAL_HISTORY_CSV_NAME";
        public static final String SERIAL_RECORD_DATE_FORMAT = "SERIAL_RECORD_DATE_FORMAT";
        public static final String FUND_RECORD_NAME = "FUND_RECORD_CSV_NAME";
        public static final String FUND_ACCOUNTING_LINE_RECORD_NAME = "FUND_ACCOUNTING_LINE_RECORD_CSV_NAME";
        public static final String FUND_RECORD_UPLOAD_CSV = "error.fund.record.upload.csv";
        public static final String FUND_ACCOUNT_RECORD_UPLOAD_CSV = "error.fund.account.upload.csv";
        public static final String FUND_RECORD_TYPE_CSV = "error.fund.record.csv";
        public static final String FUND_ACCOUNT_RECORD_TYPE_CSV = "error.fund.account.record.csv";
        public static final String FUND_RECORD_NAME_MISMATCH = "error.fund.record.name.mismatch";
        public static final String FUND_ACCOUNT_RECORD_NAME_MISMATCH = "error.fund.account.name.mismatch";
        public static final String FUND_CODE_IMPORT_DIR_PATH = "batch.fund.import.directory";

        public static final String BATCH_DELETE_DIR_PATH = "batch.delete.directory";
        public static final String BATCH_BIB_IMPORT_DIR_PATH = "batch.bibImport.directory";
        public static final String BATCH_GOKB_IMPORT_DIR_PATH = "batch.gokbImport.directory";
        public static final String BATCH_PATRON_IMPORT_DIR_PATH = "batch.patron.directory";
        public static final String BATCH_LOCATION_IMPORT_DIR_PATH = "batch.location.directory";
        public static final String BATCH_ORDER_RECORD_IMPORT_DIR_PATH = "batch.orderRecord.directory";
        public static final String BATCH_INVOICE_DIR_PATH = "batch.invoice.directory";
        public static final String BATCH_EXPORT_DIR_PATH = "batch.export.directory";
        public static final String SERIAL_IMPORT_DIR_PATH = "batch.serial.import.directory";
        public static final String BATCH_EXPORT_PATH_APP_URL = "application.url";
        public static final String DIRECTORY_LIST_LOCATION = "/ole-kr-krad/oleDirectoryListController?viewId=OLEListDirectoryView&methodToCall=start&filePath=";
        public static final String BATCH_CHANNEL_STRING = "/oleportal.do?channelTitle=List Directory&channelUrl=";
        public static final String NO_FILES = "error.no.files";
        public static final String INVALID_FILE_PATH = "error.invalid.file.path";


        public static final String ADHOC_BATCH_JOB = "ADHOC_BATCH_JOB_";

        public static final String ERR_BIB = "Error BIB:: ";
        public static final String TIME_STAMP = " ::TIME STAMP:: ";
        public static final String ERR_CAUSE = " ::Error Caused:: ";
        public static final String lineSeparator = System.getProperty("line.separator");
        public static final String ERR_INSTANCE = "Err Holdings ID:: ";
        public static final String ERR_HOLDING = "Err Holdings ID:: ";
        public static final String ERR_ITEM = "Err Item ID:: ";
        public static final String COMMA = ",";

        //batch export
        public static final String DATE = "date";
        public static final String DATE_UPDATED = "dateUpdated";
        public static final String STATUS_UPDATED_ON = "statusUpdatedOn";
        public static final String STATUS_SEARCH = "Status_search";
        public static final String LOCAL_ID_SEARCH = "LocalId_search";
        public static final String LOCAL_ID_DISPLAY = "LocalId_display";
        public static final String STAFF_ONLY_FLAG = "staffOnlyFlag";
        public static final String DYNAMIC_FIELD_PREFIX = "mdf_";
        public static final String DELETED_BIB_IDS_FILE_NAME = "_Deleted_Bibs.txt";

        // batch delete
        public static final String FAILURE_INFO = "failureInfo";
        public static final String BIB_REC_NOT_FOUND = "Bib record not found";
        public static final String LINK_REQ_PO = "linked with requisition/po";
        public static final String ITEM_LOANED = "Item is Loaned";
        public static final String ITEM_ONHOLD = "Item is OnHold";
        public static final String MORE_BIB_RECORDS = "Contains more than one bib record";
        public static final String MORE_HOLDINGS = "bib record contains more than one holdings";
        public static final String MORE_EHOLDINGS ="bib record contains more than one eHoldings";
        public static final String BIB_BOUNDS_WITH = "bib record bounds with other record";
        public static final String BATCH_DELETION_JOB = "Batch deletion job ";
        public static final String FAILED = " has FAILED";
        public static final String COMPLETE = " is COMPLETE";
        public static final String PART = "part";

        // data mapping and profile constants for order record import
        public static final String BATCH_ORDER_IMPORT =  "Order Record Import";
        public static final String ORDER_IMPORT = "OrderImport";
        public static final String CHART_CODE = "chartCode";
        public static final String ORG_CODE = "orgCode";
        public static final String CONTRACT_MANAGER = "contractManager";
        public static final String ASSIGN_TO_USER = "assignToUser";
        public static final String ORDER_TYPE = "orderType";
        public static final String FUNDING_SOURCE = "fundingSource";
        public static final String DELIVERY_CAMPUS_CODE = "deliveryCampusCode";
        public static final String BUILDING_CODE = "buildingCode";
        public static final String VENDOR_CHOICE = "vendorChoice";
        public static final String ITEM_TYPES = "itemType";
        public static final String METHOD_OF_PO_TRANSMISSION = "methodOfPOTransmission";
        public static final String COST_SOURCE = "costSource";
        public static final String PERCENT = "percent";
        public static final String DEFAULT_LOCATION = "defaultLocation";
        public static final String LIST_PRICE = "listPrice";
        public static final String SINGLE_COPY_NUMBER = "singleCopyNumber";
        public static final String VENDOR_NUMBER = "vendorNumber";
        public static final String VENDOR_ALIAS_NAME = "vendorAliasName";
        public static final String QUANTITY = "quantity";
        public static final String VENDOR_REFERENCE_NUMBER = "vendorReferenceNumber";
        public static final String RECEIVING_REQUIRED = "receivingRequired";
        public static final String USE_TAX_INDICATOR = "useTaxIndicator";
        public static final String PREQ_POSITIVE_APPROVAL_REQ = "payReqPositiveApprovalReq";
        public static final String PO_CONFIRMATION_INDICATOR = "purchaseOrderConfirmationIndicator";
        public static final String ROUTE_TO_REQUESTOR = "routeToRequestor";
        public static final String REQUESTOR_NAME = "requestorName";
        public static final String ITEM_STATUS = "itemStatus";
        public static final String DISCOUNT = "discount";
        public static final String DISCOUNT_TYPE = "discountType";
        public static final String ACCOUNT_NUMBER = "accountNumber";
        public static final String OBJECT_CODE = "financialObjectCode";
        public static final String ITEM_NO_OF_PARTS = "itemNoOfParts";
        public static final String VENDOR_PROFILE_CODE = "vendorProfileCode";
        public static final String MISC_NOTE = "Miscellaneous/Other Note";
        public static final String RCPT_NOTE = "Receipt Note";
        public static final String RQST_NOTE = "Requestor Note";
        public static final String SELECTOR_NOTE = "Selector Note";
        public static final String SPL_PROCESS_NOTE = "Special Processing Instruction Note";
        public static final String VNDR_INSTR_NOTE = "Vendor Instructions Note";
        public static final String CAPTION = "caption";
        public static final String VOLUME_NUMBER = "volumeNumber";
        public static final String VENDOR_CUST_NBR = "vendorCustomerNumber";
        public static final String FORMAT_TYP_NM = "formatTypeName";
        public static final String FORMAT="Format";
        public static final String RECURRING_PAYMENT_TYP = "RecurringPaymentType";
        public static final String RECURRING_PAYMENT_BEGIN_DT = "RecurringPaymentBeginDate";
        public static final String RECURRING_PAYMENT_END_DT = "RecurringPaymentEndDate";
        public static final String RECURRING_PAYMENT_TYP_CODE = "recurringPaymentTypeCode";
        public static final String REQUEST_SRC = "requestSourceType";
        public static final String REC_POSITION = "record # ";
        public static final String DELIVERY_BUILDING_ROOM_NUMBER = "deliveryBuildingRoomNumber";
        public static final String BUILDING_ROOM_NUMBER = "Building Room Number";

        public static final String CHART_OF_ACCOUNTS_CODE = "chartOfAccountsCode";
        public static final String ORGANIZATION_CODE = "organizationCode";
        public static final String CONTRACT_MANAGER_NAME = "contractManagerName";
        public static final String PURCHASE_ORDER_TYPE = "purchaseOrderType";
        public static final String FUNDING_SOURCE_CODE = "fundingSourceCode";
        public static final String CAMPUS_CODE = "campusCode";
        public static final String PO_VENDOR_CHOICE_CODE = "purchaseOrderVendorChoiceCode";
        public static final String ITEM_TYPE_CODE = "itemTypeCode";
        public static final String PO_TRANSMISSION_METHOD_CODE = "purchaseOrderTransmissionMethodCode";
        public static final String PO_TRANSMISSION_METHOD_DESC = "purchaseOrderTransmissionMethodDescription";
        public static final String PO_COST_SOURCE_CODE = "purchaseOrderCostSourceCode";
        public static final String VENDOR_REF_NUMBER = "vendorRefNumber";
        public static final String TRUE = "true";
        public static final String FALSE = "false";
        public static final String RECURR_PAY_END_DATE = "RecurringPaymentEndDate";
        public static final String RECURR_PAY_BEGIN_DATE = "RecurringPaymentBeginDate";

        //Validation message for order record import
        public static final String INVALID_DONOR_CODE = "error.invalid.donorCode";
        public static final String INVALID_CHART_CODE = "error.invalid.chartCode";
        public static final String INVALID_ORGANIZATION_CODE = "error.invalid.organizationCode";
        public static final String INVALID_ACCOUNT_NUMBER = "error.invalid.accountNumber";
        public static final String INVALID_OBJECT_CODE = "error.invalid.objectCode";
        public static final String INVALID_ITEM_CHART_CD = "error.invalid.itemChartCode";
        public static final String INVALID_CONTRACT_MANAGER_NAME = "error.invalid.contractManagerName";
        public static final String INVALID_ORDER_TYPE = "error.invalid.orderType";
        public static final String INVALID_FUNDING_SOURCE_CODE = "error.invalid.fundingSourceCode";
        public static final String INVALID_CAMPUS_CODE = "error.invalid.campusCode";
        public static final String INVALID_BUILDING_CODE = "error.invalid.buildingCode";
        public static final String INVALID_PO_VENDOR_CHOICE_CODE = "error.invalid.vendorChoiceCode";
        public static final String INVALID_ITEM_TYPE_CODE = "error.invalid.itemTypeCode";
        public static final String INVALID_PO_TRANSMISSION_METHOD_CODE = "error.invalid.transmissionMethodCode";
        public static final String INVALID_PO_COST_SOURCE_CODE = "error.invalid.costSourceCode";
        public static final String INVALID_RECEIVING_REQUIRED = "error.invalid.receivingRequired";
        public static final String INVALID_USE_TAX_INDICATOR = "error.invalid.useTaxIndicator";
        public static final String INVALID_PREQ_POSITIVE_APPROVAL_REQ = "error.invalid.positiveApprovalRequired";
        public static final String INVALID_PO_CONFIRMATION_INDICATOR = "error.invalid.confirmationIndicator";
        public static final String INVALID_ROUTE_TO_REQUESTOR = "error.invalid.route.to.requestor";
        public static final String INVALID_DISCOUNT_TYPE = "error.invalid.discountType";
        public static final String INVALID_DEFAULT_LOCATION = "error.invalid.defaultLocation";
        public static final String INVALID_VENDOR_NUMBER = "error.invalid.vendorNumber";
        public static final String INVALID_PERCENT = "error.invalid.percent";
        public static final String INVALID_LIST_PRICE = "error.invalid.listPrice";
        public static final String INVALID_DISCOUNT = "error.discount";
        public static final String INVALID_ASSIGN_TO_USER = "error.invalid.assignToUser";
        public static final String INVALID_QUANTITY = "error.invalid.quantity";
        public static final String INVALID_NO_OF_PARTS = "error.invalid.noOfParts";
        public static final String INVALID_REQUESTOR_NAME = "error.invalid.requestorName";
        public static final String INVALID_ITEM_STATUS = "error.invalid.itemStatus";
        public static final String DESC_MAX_LENG = "Document Description should not be more than 40 characters.";
        public static final String INVALID_RECURRING_PAYMENT_DATE = "error.invalid.recurringPayment.date";

        //Validation message for invoice import

        public static final String INVALID_INVOICE_NUMBER = "error.invalid.invoice.number";
        public static final String INVALID_VENDOR_INVOICE_AMOUNT = "error.invalid.vendor.invoice.amount";
        public static final String INVALID_INVOICED_PRICE = "error.invalid.invoicedPrice";
        public static final String INVALID_INVOICED_FOREIGN_PRICE = "error.invalid.invoicedForeignPrice";
        public static final String INVALID_INVOICE_DATE = "error.invalid.invoice.date";
        public static final String INVALID_EXCHANGE_RATE = "error.invalid.exchangeRate";
        public static final String INVALID_EXCHANGE_RATE_NUMBER = "error.invalid.exchangeRate.number";
        public static final String BIB_IMPORT_PROFILE = "bibImportProfileForOrderRecord";
        public static final String CLOSED_ACC_NO = " Account Number is closed, please give valid account no with non closed status to proceed further.";
        public static final String INVALID_ACNO_OBJCD_COMBINATION = "Please enter a right combination of Account Number,Object Code and Item Chart Code.";
        public static final String INVALID_ORGCD_CHARTCD_COMBINATION = "Please enter a right combination of Org Code and Chart Code.";
        public static final String INVALID_BLDGCD_CMPCD_ROOM_COMBINATION = "Please enter a right combination of Building Code, Delivery Campus Code and Building Room Number.";
        public static final String INVALID_VNDRNBR_CUSTNBR_COMBINATION = "Please enter a right combination of Vendor Number and Acquisition Unit's Vendor account / Vendor Info Customer #";
        public static final String BIB_IMP_FAILED = "Bib import failed ";
        public static final String REASON = " Reason :  ";

        // data mapping and profile constants for invoice import

        public static final String INVOICE_IMPORT = "InvoiceImport";
        public static final String INVOICE_RECORD_IMPORT = "Invoice Import";
        public static final String VENDOR_ITEM_IDENTIFIER = "vendorItemIdentifier";
        public static final String VENDOR_NAME = "vendorName";
        public static final String VENDOR_NON_BILLABLE = "nonBillable";
        public static final String REQUESTOR = "requestor";
        public static final String EBOOK = "ebook";
        public static final String BOOK_PLATE = "bookPlate";
        public static final String INVOICE_NUMBER = "invoiceNumber";
        public static final String INVOICE_DATE = "invoiceDate";
        public static final String VENDOR_INVOICE_AMOUNT = "vendorInvoiceAmount";
        public static final String ITEM_CHART_CODE = "itemChartCode";
        public static final String ITEM_DESCRIPTION = "itemDescription";
        public static final String CURRENCY_TYPE = "currencyType";
        public static final String CURRENCY_TYPE_ID = "currencyTypeId";
        public static final String EXCHANGE_RATE = "exchangeRate";
        public static final String FOREIGN_LIST_PRICE = "foreignListPrice";

        //SERIAL RECEIVING IMPORT
        public static final String FDOC_NBR = "FDOC_NBR";
        public static final String SER_RCV_REC_ID = "SER_RCV_REC_ID";
        public static final String BIB_ID = "BIB_ID";
        public static final String BOUND_LOC = "BOUND_LOC";
        public static final String CALL_NUM = "CALL_NUM";
        public static final String RCV_REC_TYP = "RCV_REC_TYP";
        public static final String CLAIM = "CLAIM";
        public static final String CLAIM_INTRVL_INFO = "CLAIM_INTRVL_INFO";
        public static final String COPY_NUM = "COPY_NUM";
        public static final String CORPORATE_AUTH = "CORPORATE_AUTH";
        public static final String CREATE_ITEM = "CREATE_ITEM";
        public static final String GEN_RCV_NOTE = "GEN_RCV_NOTE";
        public static final String INSTANCE_ID = "INSTANCE_ID";
        public static final String ISSN = "ISSN";
        public static final String PO_ID = "PO_ID";
        public static final String PRINT_LBL = "PRINT_LBL";
        public static final String PUBLIC_DISPLAY = "PUBLIC_DISPLAY";
        public static final String PUBLISHER = "PUBLISHER";
        public static final String SER_RCPT_LOC = "SER_RCPT_LOC";
        public static final String SER_RCV_REC = "SER_RCV_REC";
        public static final String SUBSCR_STAT = "SUBSCR_STAT";
        public static final String TREATMENT_INSTR_NOTE = "TREATMENT_INSTR_NOTE";
        public static final String UNBOUND_LOC = "UNBOUND_LOC";
        public static final String URGENT_NOTE = "URGENT_NOTE";
        public static final String VENDOR = "VENDOR";
        public static final String CREATE_DATE = "CREATE_DATE";
        public static final String OPTR_ID = "OPTR_ID";
        public static final String MACH_ID = "MACH_ID";
        public static final String SUBSCR_STAT_DT = "SUBSCR_STAT_DT";
        public static final String SR_TITLE = "SR_TITLE";
        public static final String OBJ_ID = "OBJ_ID";
        public static final String VER_NBR = "VER_NBR";
        public static final String ACTIVE = "ACTIVE";
        public static final String SER_RCPT_HIS_REC_ID = "SER_RCPT_HIS_REC_ID";
        public static final String CHRON_LVL_1 = "CHRON_LVL_1";
        public static final String CHRON_LVL_2 = "CHRON_LVL_2";
        public static final String CHRON_LVL_3 = "CHRON_LVL_3";
        public static final String CHRON_LVL_4 = "CHRON_LVL_4";
        public static final String CLAIM_COUNT = "CLAIM_COUNT";
        public static final String CLAIM_DATE = "CLAIM_DATE";
        public static final String CLAIM_NOTE = "CLAIM_NOTE";
        public static final String CLAIM_TYPE = "'CLAIM_TYPE";
        public static final String CLAIM_RESP = "CLAIM_RESP";
        public static final String ENUM_LVL_1 = "ENUM_LVL_1";
        public static final String ENUM_LVL_2 = "ENUM_LVL_2";
        public static final String ENUM_LVL_3 = "ENUM_LVL_3";
        public static final String ENUM_LVL_4 = "ENUM_LVL_4";
        public static final String ENUM_LVL_5 = "ENUM_LVL_5";
        public static final String ENUM_LVL_6 = "ENUM_LVL_6";
        public static final String PUB_DISPLAY = "PUB_DISPLAY";
        public static final String SER_RCPT_NOTE = "SER_RCPT_NOTE";
        public static final String RCPT_STAT = "RCPT_STAT";
        public static final String RCPT_DATE = "RCPT_DATE";
        public static final String PUB_RCPT = "PUB_RCPT";
        public static final String STAFF_ONLY_RCPT = "STAFF_ONLY_RCPT";

        public static final String SER_RCV_REC_TYP_ID = "SER_RCV_REC_TYP_ID";
        public static final String ACTN_DATE = "ACTN_DATE";
        public static final String ACTN_INTRVL = "ACTN_INTRVL";
        public static final String CHRON_CAPTN_LVL1 = "CHRON_CAPTN_LVL1";
        public static final String CHRON_CAPTN_LVL2 = "CHRON_CAPTN_LVL2";
        public static final String CHRON_CAPTN_LVL3 = "CHRON_CAPTN_LVL3";
        public static final String CHRON_CAPTN_LVL4 = "CHRON_CAPTN_LVL4";
        public static final String ENUM_CAPTN_LVL1 = "ENUM_CAPTN_LVL1";
        public static final String ENUM_CAPTN_LVL2 = "ENUM_CAPTN_LVL2";
        public static final String ENUM_CAPTN_LVL3 = "ENUM_CAPTN_LVL3";
        public static final String ENUM_CAPTN_LVL4 = "ENUM_CAPTN_LVL4";
        public static final String ENUM_CAPTN_LVL5 = "ENUM_CAPTN_LVL5";
        public static final String ENUM_CAPTN_LVL6 = "ENUM_CAPTN_LVL6";

        public static final String CONSTANT_DATAMAPPING_FOR_EHOLDINGS = "956";
        public static final String E_HOLDINGS_URL_MAPPING = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $u";
        public static final String E_HOLDINGS_START_DATE_MAPPING = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $a";
        public static final String E_HOLDINGS_START_VOLUME_MAPPING = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $b";
        public static final String E_HOLDINGS_START_ISSUE_MAPPING = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $c";
        public static final String E_HOLDINGS_END_DATE_MAPPING = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $d";
        public static final String E_HOLDINGS_END_VOLUME_MAPPING = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $e";
        public static final String E_HOLDINGS_END_ISSUE_MAPPING = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $f";
        public static final String E_HOLDINGS_PLATFORM = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $g";
        public static final String E_HOLDINGS_E_RESOURCE_ID = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $h";
        public static final String E_HOLDINGS_GOKB_ID = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $i";
        public static final String E_HOLDINGS_PUBLISHER = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $j";
        public static final String E_HOLDINGS_IMPRINT = CONSTANT_DATAMAPPING_FOR_EHOLDINGS + " $k";

        public static final String REQUISITION_SOURCE = "requisitionSource";
        public static final String ITEM_PRICE_SOURCE = "itemPriceSource";
    }

    public static final class OlePatron {
        public static final String PATRON_ENTITY_CONVERSION_SUFFIX = "OLEPTRN";
        public static final String PATRON_ID = "olePatronId";
        public static final String PATRON_BATCH_UPDATE = "patronBatchUpdate";
        public static final String PROXY_PATRON_ID = "proxyPatronId";
        public static final String BARCODE = "barcode";
        public static final String BORROWER_TYPE = "borrowerType";
        public static final String PROXY_BARCODE = "proxyPatronBarcode";
        public static final String PATRON_FIRST_NAME = "firstName";
        public static final String PATRON_LAST_NAME = "lastName";
        public static final String PATRON_MIDDLE_NAME = "middleName";
        public static final String PATRON_EMAIL_ADDRESS = "emailAddress";
        public static final String PATRON_BILL = "Patron Bill";
        public static final String PATRON_PHONE_NUMNER = "phoneNumber";
        public static final String PATRON_VIEW_BILL_URL = "patronbill?viewId=BillView&amp;methodToCall=start&amp;patronId=";
        public static final String PATRON_CREATE_BILL_URL = "patronBillMaintenance?viewTypeName=MAINTENANCE&returnLocation=%2Fportal.do&methodToCall=start&dataObjectClassName=org.kuali.ole.deliver.bo.PatronBillPayment&patronId=";
        public static final String PATRON_ACTIVE_IND = "activeIndicator";
        public static final String OLE_ADDRESS_ID = "oleAddressId";
        public static final String ENTITY_ADDRESS_ID = "id";
        public static final String ENTITY_ID = "entityId";
        public static final String ENTITY_BO_ID = "id";
        public static final String ENTITY_NM_TYP_CD = "PRFR";
        public static final String PATRON_MAINTENANCE_ACTION_LINK = "patronMaintenance";
        public static final String PATRON_MAINTENANCE_DATE_FORMAT = "yyyyMMdd";
        public static final String ERROR_PATRON_GENERAL_BLOCK_NOTES = "error.patron.general.block.notes";
        public static final String ERROR_PATRON_NEW_PAST_DATE = "The patron document has new past Activation Date.";
        public static final String ERROR_PATRON_ACTIVATION_DATE = "error.patron.activation.date";
        public static final String ERROR_PATRON_EXPIRATION_DATE = "error.patron.expiration.date";
        public static final String ERROR_PROXY_PATRON_ACTIVATION_DATE = "error.proxy.patron.activation.date";
        public static final String ERROR_REAL_PATRON_ACTIVATION_DATE = "error.real.patron.activation.date";
        public static final String ERROR_PROXY_PATRON_EXPIRATION_DATE = "error.proxy.patron.expiration.date";
        public static final String ERROR_REAL_PATRON_EXPIRATION_DATE = "error.real.patron.expiration.date";
        public static final String ERROR_PATRON_VALID_ADDRESS_TO_DATE = "error.patron.valid.addressTo.date";
        public static final String ERROR_PATRON_REQUIRED_ADDRESS_FROM_DATE = "error.patron.required.address.from.date";
        public static final String ERROR_PATRON_ADDRESS_FROM_TO_DATE_OVERLAP = "error.patron.address.from.to.date.overlap";
        public static final String INVALID_BARCODE = "error.deliver.patron.invalidBarcode";
        public static final String ERROR_PROXY_PATRON_ID = "error.proxy.patron.id";
        public static final String ERROR_DUPLICATE_PROXY_PATRON_BARCODE = "error.proxy.barcode.duplicate";
        public static final String ERROR_PROXY_PATRON_BARCODE = "error.proxy.barcode.required";
        public static final String PATRON_GENERAL_BLOCK_NOTES = "General Block Notes";
        public static final String ERROR_PATRON_NOT_FOUND = "error.patron.not.found";
        public static final String ERROR_PATRON_HAS_LOAN = "error.patron.cannot.delete";
        public static final String SAVE_SUCCESSFUL_MSG = "Successfully saved";
        public static final String ERROR_DEFAULT_MESSAGE = "Either of the address or email or phone must have a default";
        public static final String ERROR_ADDRESS_SOURCE_REQUIRED = "Address Source is required";
        public static final String ERROR_PHONE_SOURCE_REQUIRED = "Phone Source is required";
        public static final String ERROR_EMAIL_SOURCE_REQUIRED = "Email Source is required";
        public static final String OLE_PATRON_SERVICE = "olePatronService";
        public static final String OLE_PATRON_MAINTENANCE_DOC_SERVICE = "olePatronMaintenanceDocumentService";
        public static final String CRITERIA_LOOKUP_SERVICE = "criteriaLookupService";
        public static final String OLE_PATRON_DELETE = "Delete";
        public static final String PATRON_NOTE_ID = "patronNoteId";
        public static final String ERROR_PATRON_PHOTOGRAPH_SIZE = "error.patron.photograph.size";
        public static final String ERROR_PATRON_PHOTOGRAPH_FORMAT = "error.patron.photograph.format";
        public static final String ERROR_PATRON_PHOTOGRAPH_WITHOUT_FILE = "error.patron.photograph.without.file";
        public static final String ENTER_PATRON_BARCODE = "enter.patron.barcode";
        public static final String ERROR_PATRON_BARCODE_INVALID = "error.patron.barcode.invalid";
        public static final String ERROR_PATRON_BORROWER_TYPE_INACTIVE = "error.patron.borrowerType.inactive";
        public static final String ERROR_PATRON_ADDRESS_DEFAULT_DATE = "error.patron.address.noDateForPreferred";
        public static final String ERROR_PATRON_ADDRESS_SINGLE_DATE = "error.patron.address.singleDate";
        public static final String ERROR_PATRON_MULIT_PREFERRED_EMAIL = "error.patron.multi.defaultEmail";
        public static final String ERROR_PATRON_MULIT_PREFERRED_ADDRESS = "error.patron.multi.defaultAddress";
        public static final String ERROR_PATRON_MULIT_DELIVER_ADDRESS = "error.patron.multi.deliverAddress";
        public static final String ERROR_PATRON_REQUIRED_DEFAULT_FIELD_CONTACT = "error.patron.required.default.field.contact";
        public static final String ERROR_PATRON_REQUIRED_ADDRESS = "error.patron.required.address";
        public static final String ERROR_PATRON_REQUIRED_AFFILIATION = "error.patron.required.affiliation";
        public static final String ERROR_PATRON_REQUIRED_EMPLOYEE = "error.patron.required.employee";
        public static final String ERROR_PATRON_FIRST_NAME = "error.patron.first.name";
        public static final String ERROR_PATRON_MIDDLE_NAME = "error.patron.middle.name";
        public static final String ERROR_PATRON_LAST_NAME = "error.patron.last.name";
        public static final String PATRON_NAME_VALIDATION = "PATRON_NAME_VALIDATION";
        public static final String PATRON_BILL_REVIEW_PRINT = "PATRON_BILL_REVIEW_PRINT";
        public static final String PATRON_SUCCESS_MAIL = "error.message.mail.patron.sent";
        public static final String ERROR_PATRON_MAIL = "error.message.mail.patron";
        public static final String FIRST_NAME = "dataObject.name.firstName";
        public static final String MIDDLE_NAME = "dataObject.name.middleName";
        public static final String LAST_NAME = "dataObject.name.lastName";
        public static final String INV_ITEM_BAR = "error.invalid.item";
        public static final String ITEM_REQ = "Item is required";
        public static final String OLE_SOURCE_ID = "oleSourceId";
        public static final String KRIM_ENTITY_ID_S = "KRIM_ENTITY_ID_S";
        public static final String CREATED_RECORD = ", Created record :";
        public static final String UPDATED_RECORD = ", Updated record : ";
        public static final String FAILED_RECORD = ", Failed record : ";
        public static final String REJECTED_RECORD = ", Rejected record : ";
        public static final String TOTAL_RECORD = " Total record : ";
        public static final String OLE_ADD_SRC_CD = "oleAddressSourceCode";
        public static final String OLE_DLVR_ADD_S = "OLE_DLVR_ADD_S";
        public static final String OLE_DLVR_PHONE_S = "OLE_DLVR_PHONE_S";
        public static final String OLE_DLVR_EMAIL_S = "OLE_DLVR_EMAIL_S";
        public static final String PATRON_VIEW_BILLS = "View Bills";
        //patronBill
        public static final String PAY_BILL_PATRON_ID = "patronId";
        public static final String OUTSTANDING_BILL = "error.bill.selection.outstanding";
        public static final String BILL_PAYMENT_ID = "billNumber";
        public static final String ITEMBARCODE_SEPARATOR = ", ";
        public static final String BILL_PAYMENT_STATUS_OUTSTANDING = "Outstanding";
        public static final String DEFAULT = "default";
        public static final String BILL_VIEW_PAGE = "BillViewPage";
        public static final String OLE_PATRON_BILL_DELETE = "Delete";
        public static final String FILE_NAME = "fileName";
        public static final String OLE_PTRN_BILL_MAIN_DOC_SER = "olePatronBillMaintenanceDocumentService";
        public static final String NOT_ELIGIBLE_FOR_CREDIT = "error.not.eligible.for.credit";
        public static final String POSITIVE_VAL_NOT_ALLOWED = "error.positive.value.not.allowed";
        public static final String PATRON_BILL_OLE_KR_KRAD = "/ole-kr-krad/";
        public static final String PATRON_BILL_URL = "/portal.do?channelTitle=PatronBill&amp;channelUrl=";
        //patronMerge
        public static final String PATRON_MERGE_SURVIVOR_SELECT = "error.select.survivor";
        public static final String PATRON_MERGE_DUPLICATE_PATRON_SELECT = "error.select.dyingPatron";
        public static final String PATRON_MERGE_VIEW_PAGE = "PatronMergeViewPage";
        public static final String PATRON_MERGE_CONFIG_XML_FILE = "patronConfig.xml";
        public static final String PATRON_DUPLICATE_BARCODE = "error.barcode.duplicate";
        public static final String PATRON_BARCODE_BLOCK_MANUALLY = "error.patron.barcode.block";
        /*        public static final String PATRON_NAMESPACE = "OLE-PTRN";
                public static final String PATRON_CMPNT = "OLELSCMPNT";*/
        public static final String PATRON_NAMESPACE = "OLE-PTRN";
        public static final String PATRON = "Patron";
        public static final String ERROR_REQUIRED = "error.required";
        public static final String ERROR_SELECTION_PREFERRED_ADDRESS = "error.selection.preferred.address";
        public static final String ERROR_SELECTION_PREFERRED_DELIVER_ADDRESS = "error.selection.preferred.deliver.address";
        public static final String ERROR_SELECTION_PREFERRED_EMAIL = "error.selection.preferred.email";
        public static final String ERROR_SELECTION_PREFERRED_PHONE = "error.selection.preferred.phone";
        public static final String ERROR_SELECTION_PREFERRED_ADDRESS_ACTIVE = "error.selection.preferred.address.active";
        public static final String ERROR_SELECTION_PREFERRED_EMAIL_ACTIVE = "error.selection.preferred.email.active";
        public static final String ERROR_SELECTION_PREFERRED_PHONE_ACTIVE = "error.selection.preferred.phone.active";
        public static final String ADDRESS_SECTION_ID = "OlePatronDocument-Address";
        public static final String EMAIL_SECTION_ID = "OlePatronDocument-Email";
        public static final String PHONE_SECTION_ID = "OlePatronDocument-Phone";
        public static final String ERROR_BARCODE_EXIST_LOST_SECTION = "error.patron.barcode.contains.lost.section";
        public static final String REINSTATE = "REINSTATE";
        public static final String PATRON_LOST_BARCODE_FLD = "invalidOrLostBarcodeNumber";
        public static final String NEWBARCODE_STATUS = "INVALID";
        public static final String NEWBARCODE_DESCRIPTION = "Previous barcode reinstated.";
        public static final String PATRON_LOANED_ITEM_SECTION = "OlePatronDocument-LoanedItems";
        public static final String ERROR_PATRON_ITEM_INFO = "error.patron.item.noOfPieces";
        public static final String ERROR_PATRON_MISSING_PIECE_ITEM_INFO = "error.patron.item.noOfMissingPieces";
        public static final String ERROR_PATRON_MISSING_PIECE_ITEM_COUNT_GREATER = "error.exclusiveMin";
        public static final String ERROR_PATRON_MISSING_PIECE_ITEM_COUNT_LESSER = "error.inclusiveMax";
        public static final String PATRON_BARCODE_DOES_NOT_EXIST_REINSTATE = "error.patron.reinstate.remove.barcode";
        public static final String PATRON_XML_ISVALID = "isValid";
        public static final String PATRON_POLLERSERVICE_ERROR_MESSAGE = "errorMessage";



        public static final Map<String, Object> getPatronConfigObject() {
            Map<String, Object> patronConfigDocuments = new HashMap<String, Object>();
            patronConfigDocuments.put("loanDocument", OleLoanDocument.class);
            patronConfigDocuments.put("temporaryCirculationHistoryRecord", OleTemporaryCirculationHistory.class);
            patronConfigDocuments.put("requestRecord", OleDeliverRequestBo.class);
            patronConfigDocuments.put("patronBillPayment", PatronBillPayment.class);
            return Collections.unmodifiableMap(patronConfigDocuments);
        }
    }

    public static final String PATRON_ENTITY_ACTIVE = "activeIndicator";


    // patron ingest message
    public static final String PATRON_RECORD_SELECT_FILE = "Please select file to upload.";
    public static final String PATRON_RECORD_SUCCESS = "Patron File uploaded successfully.";
    public static final String PATRON_RECORD_FAILURE = "Failed to upload Patron File.";
    public static final String PATRON_RECORD_INVALID_SCHEMA = "Invalid Schema File Uploaded.";
    public static final String DOCUMENT_HEADER_SERVICE = "documentService";
    public static final String PATRON_CONVERTER_SERVICE = "olePatronConverterService";
    public static final String FAILED_PATRON_ATTACHMENT_DOWNLOAD_URL = "patronrecordcontroller?viewId=OlePatronRecordView&methodToCall=downloadAttachment&olePatronSummaryId=";
    public static final String FAILED_PATRON_RECORD_NAME = "_patron_err.xml";
    public static final String PATRON_SUMMARY_REPORT_ID = "olePatronSummaryId";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String PREFERRED = "Preferred";
    public static final String PATRON_NOTE_TYPE_NAME = "patronNoteTypeName";
    public static final String PATRON_NOTE_TYPE_CODE = "patronNoteTypeCode";
    public static final String BORROWER_TYPE_NAME = "borrowerTypeName";
    public static final String BORROWER_TYPE = "borrowerType";
    public static final String BORROWER_TYPE_ID = "borrowerTypeId";
    public static final String BORROWER_TYPE_CODE = "borrowerTypeCode";
    public static final String SOURCE_CODE = "oleSourceCode";
    public static final String STATISTICAL_CATEGORY_CODE = "oleStatisticalCategoryCode";
    public static final String STAGING_DIRECTORY = "staging.directory";
    //public static final String PATRON_FILE_DIRECTORY = "patron.directory";
    public static final String PATRON_FILE_DIRECTORY = "/patron/";
    public static final String USER_HOME_DIRECTORY = "user.home";
    // Location Ingest message
    public static final String LOCATION_RECORD_SELECT_FILE = "Please select file to upload.";
    public static final String LOCATION_RECORD_INVALID_SCHEMA = "Invalid Schema File Uploaded.";
    public static final String LOCATION_RECORD_SUCCESS = "Location File Uploaded successfully";
    public static final String LOCATION_RECORD_FAILURE = "Failed to upload location file.";
    public static final String OLE_LOCATION_DOCUMENT_TYPE = "OLE_LMD";
    //public static final String OLE_LOCATION_DESCRIPTION = "Ole Location";
    public static final String FAILED_LOCATION_ATTACHMENT_DOWNLOAD_URL = "locationcontroller?viewId=OleLocationView&methodToCall=downloadAttachment&oleLocationSummaryId=";
    public static final String FAILED_LOCATION_RECORD_NAME = "_location_err.xml";
    public static final String LOCATION_SUMMARY_REPORT_ID = "oleLocationSummaryId";
    //public static final String LOCATION_ERROR_FILE_PATH = "/location/";
    public static final String LOCATION_ERROR_FILE_PATH = "location.directory";
    public static final String VENDOR_TRANSMISSION_FILE_PATH = "vendor.transmission.directory";
    //deliver
/*    public static final String DLVR_NMSPC = "OLE-DLVR";
    public static final String DLVR_CMPNT = "OLELSCMPNT";*/
    public static final String DLVR_NMSPC = "OLE-DLVR";
    public static final String DLVR_CMPNT = "Deliver";
    public static final String SYS_NMSPC = "OLE-SYS";
    public static final String BATCH_CMPNT = "Batch";
    public static final String BARCODE_LOST_OR_STOLEN = "Patron Barcode Lost/Stolen.";
    //public static final String COURTESY_NOTICE_INTER = "COURTESY_NOTICE_INTER";
    public static final String OVERDUE_NOTICE_INTER = "OVERDUE_NOTICE_INTER";
    public static final String PATRON_DIGIT_ROUTINE = "PATRON_DIGIT_ROUTINE";
    public static final String PATRON_DIGIT_ROUTINE_PATTERN = "PATRON_DIGIT_ROUTINE_PATTERN";
    public static final String PATRON_STATUS_LABEL = "Barcode Status  :";
    public static final String PATRON_DESCRIPTION_LABEL = "Note :";
    public static final String PATRON_INVALID_BARCODE_MESSAGE = "This barcode is invalid. Please ask the patron if they have a different barcode.";
    //public static final String ITEM_DIGIT_ROUTINE = "ITEM_DIGIT_ROUTINE";
    //public static final String ITEM_DIGIT_ROUTINE_PATTERN = "ITEM_DIGIT_ROUTINE_PATTERN";
    public static final String PRINT_DUE_DATE_PER_TRANSACTION = "PRINT_DUE_DATE_PER_TRANSACTION";
    public static final String MAX_TIME_CHECK_IN = "MAX_TIME_CHECK_IN";
    public static final String MAX_TIME_LOAN = "MAX_TIME_LOAN";
    public static final String CHECK_IN_AGENDA_NM = "Check-in Validation";
    public static final String CHECK_OUT_GEN_AGENDA_NM = "General Checks";
    public static final String CHECK_OUT_AGENDA_NM = "CheckOut Validation";
    public static final String RENEWAL_AGENDA_NM = "Renewal Validation";
    public static final String REQUEST_AGENDA_NM = "Request Validation";
    public static final String NOTICE_AGENDA_NM = "Notice Validation";
    public static final String BATCH_PROGRAM_AGENDA = "Batch Program Agenda";
    public static final String MAX_NO_OF_DAYS_ON_HOLD = "MAX_NO_OF_DAYS_ON_HOLD";
    public static final String SHELVING_LAG_TIME = "SHELVING_LAG_TIME";
    public static final String NO_RECORD_FOUND = "error.not.found.record";
    public static final String DONOR_NOT_FOUND = "error.donor.not.found.record";
    public static final String NO_CURENCYTYPE_FOUND = "error.not.found.currency";

    public static final String OLE_PATRON_BILL_FEE_TYPE_SECTION_ID = "PatronBillMaintenanceDocument-FeeTypeSection";
    public static final String FEE_AMOUNT_REQUIRED = "error.required.field";
    public static final String REQUIRED_FIELD = "error.required.field";
    public static final String FEE_AMOUNT_ZERO_NOT_ALLOWED = "error.fee.amount.zero";

    //item search
    public static final String ITM_BLANK_SEARCH_ERROR_MSG = "error.invalid.item.blank.search";
    //instance search
    public static final String INSTANCE_BLANK_SEARCH_ERROR_MSG = "error.invalid.instance.blank.search";
    //public static final String ITM_SEARCH_DOCSTORE_ROW_SIZE_VALUE = "ITM_SEARCH_DOCSTORE_ROW_SIZE_VALUE";
    //Loan
    public static final String ITM_ALRDY_CHECKED_OUT = "Item already checked out by another patron.";
    public static final String ITEM_STATUS_CHECKEDOUT = "LOANED";
    public static final String RETURN_PROCESS_FAILURE = "Return process for this Item failed.";
    public static final String ITEM_STATUS_LOANED_ANOTHER_PATRON_PERMISSION = "Item currently loaned to another patron";
    public static final String ITEM_STATUS_RETURNED_DAMAGED = "RETURNED-DAMAGED";
    public static final String ITEM_STATUS_RETURNED_MISSING = "RETURNED-WITH-MISSING-ITEMS";
    public static final String ITEM_STATUS_LOST_AND_PAID = "LOST-AND-PAID";
    public static final String REPLACED = "PAY_REPLACED";
    public static final String CHANGE_LOC_MESS = "Are you sure you want to change the Circulation Location?You will lose your information.";
    public static final String CIR_LOC_NULL = "Please select your circulation location.";
    public static final String OVERDUE_FINE = "Overdue Fine";
    public static final String REPLACEMENT_FEE = getParameter("LOST_ITEM_FEE");
    public static final String LOST_ITEM_PROCESSING_FEE = "Lost Item Processing Fee";
    public static final String SERVICE_FEE = "Service Fee";
    public static final String ADMIN_USER = "admin";
    public static final String CLAIMS_RETURNED_MESSAGE = "Claims Returned item has been found.";
    //Instance Record Meta Data constants
    public static final String STATUS = "status";
    public static final String FAST_ADD_FLAG = "fastAddFlag";
    public static final String SUPRESS_FROM_PUBLIC = "supressFromPublic";
    public static final String CREATED_BY = "createdBy";
    public static final String DATE_ENTERED = "dateEntered";
    public static final String LAST_UPDATED = "lastUpdated";
    public static final String LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String DATE_ENTERED_FORMAT = "MMM dd, yyyy";
    public static final String LAST_UPDATED_FORMAT = "MMM dd, yyyy hh:mm:ss a";

    //Bib Editor Messages
    public static final String BIB_EDITOR_CREATE_SUCCESS = "record.create.message";
    public static final String BIB_EDITOR_UPDATE_SUCCESS = "record.update.message";


    //Instance Editor Messages
    public static final String INSTANCE_EDITOR_SUCCESS = "Record Submitted Successfully";
    public static final String INSTANCE_EDITOR_FAILURE = "Failed to Submit Record";
    public static final String INSTANCE_EDITOR_LOAD_SUCCESS = "Record Loaded Successfully";
    public static final String INSTANCE_EDITOR_DELETE_SUCCESS = "Record Deleted Successfully";
    public static final String INSTANCE_EDITOR_DELETE_FAILURE = "Failed to Delete Record";
    public static final String INSTANCE_EDITOR_LOAD_FAILURE = "Failed to Load Record";

    //Instance Editor constants
    public static final String SHELVING_LOCATION_LEVEL_ID = "4";
    public static final String NON_SERIAL_HOLDINGS_TEXT = "Holdings";
    public static final String ROOT_NODE = "Root";
    public static final String NON_ELECTRONIC_PREFIX_TEXT = "c.";
    public static final String ITEM_LEVEL_TEXT = "Item";
    public static final String ITEM_ELECTRONIC = "electronic";
    public static final String NO_LOCATION_ITEM = "No Location Item";
    public static final String ITEM_PAGE = "ItemTabs";
    public static final String HOLDINGS_PAGE = "HoldingTabs";
    public static final String DOC_TYPE = "DocType";
    public static final String DOCTYPE="docType";
    public static final String CLASSIFICATION_SCHEME="classificationScheme";
    public static final String CALL_NUMBER_BROWSE_TXT="callNumberBrowseText";
    public static final String LOC="location";
    public static final String SEARCH_TYPE="searchType";
    public static final String BROWSE_FIELD="browseField";
    public static final String CLS_BTN_SHOWN_FLG="closeBtnShowFlag";
    public static final String NEW_ITEM_ID = "NEW_ITEM";
    public static final String ITEM_STATUS_ERRORMESSAGE = "Please enter item status";
    public static final String ITEM_STATUS_INVALID = "Invalid item status";
    public static final String ITEM_TYPE_ERROR_MESSAGE = "You are not authorized to change item status";
    public static final String ITEM_TYPE_ERRORMESSAGE = "Please enter item type";

    //request constants
    public static final String RECALL_DELIVERY_ITEM_STATUS = "RECALL_DELIVERY_ITEM_STATUS";
    public static final String RECALL_HOLD_ITEM_STATUS = "RECALL_HOLD_ITEM_STATUS";
    public static final String HOLD_DELIVERY_ITEM_STATUS = "HOLD_DELIVERY_ITEM_STATUS";
    public static final String HOLD_HOLD_ITEM_STATUS = "HOLD_HOLD_ITEM_STATUS";
    public static final String PAGE_DELIVERY_ITEM_STATUS = "PAGE_DELIVERY_ITEM_STATUS";
    public static final String PAGE_HOLD_ITEM_STATUS = "PAGE_HOLD_ITEM_STATUS";
    public static final String COPY_REQUEST_ITEM_STATUS = "COPY_REQUEST_ITEM_STATUS";
    public static final String ASR_REQUEST_ITEM_STATUS = "ASR_REQUEST_ITEM_STATUS";
    public static final String REQUEST_DOC_TYPE = "OLE_DLVR_REQS";
    public static final String DEFAULT_REQUEST_TYPE = "DEFAULT_REQUEST_TYPE";
    public static final String DEFAULT_PICK_UP_LOCATION = "DEFAULT_PICK_UP_LOCATION";


    //Docstore operations
    public static final String INGEST_OPERATION = "ingest";
    public static final String CHECK_IN_OPERATION = "checkIn";
    public static final String CHECK_IN_DATE = "Check-in Date is not a current date";
    public static final String VERIFY_PIECES = "Verify all the pieces (";
    public static final String PIECES_RETURNED = ") are returned.";
    public static final String CHECK_IN_DATE_TIME_FORMAT = "yyyy-MM-dd";
    public static final String CHECK_IN_TIME_MS = ":00";
    public static final String CHECK_OUT_DUE_TIME_MS = ":59";


    //Notice Related
    public static final String EMAIL = "email";
    public static final String SMS = "sms";
    public static final String MAIL = "mail";
    public static final String KUALI_MAIL = "kuali.mail";
    public static final String NOTICE_MAIL = "Notice Mail";
    public static final String CANCELLATION_NOTICE = "Cancellation Notice";
    //public static final String NOTICE_FROM_MAIL = "DELIVER_NOTICE_FROM_ADDRESS";

    public static final class OleAccessMethod {
        public static final String ACCESS_METHOD_CODE = "dataObject.accessMethodCode";
        public static final String ACCESS_METHOD_CD = "accessMethodCode";
    }

    public static final class OleAcquisitionMethod {
        public static final String ACQUISITION_METHOD_CODE = "dataObject.acquisitionMethodCode";
        public static final String ACQUISITION_METHOD_CD = "acquisitionMethodCode";
    }

    public static final class OleAction {
        public static final String ACTION_CODE = "dataObject.actionCode";
        public static final String ACTION_CD = "actionCode";
    }

    public static final class OleCountryCodes {
        public static final String COUNTRY_CODE = "dataObject.countryCode";
        public static final String COUNTRY_CD = "countryCode";
    }

    public static final class OleStatisticalSearchingCodes {
        public static final String STATISTICAL_SEARCHING_CODE = "dataObject.statisticalSearchingCode";
        public static final String STATISTICAL_SEARCHING_CD = "statisticalSearchingCode";
    }

    public static final class OleTypeOfOwnership {
        public static final String TYPE_OF_OWNERSHIP_CODE = "dataObject.typeOfOwnershipCode";
        public static final String TYPE_OF_OWNERSHIP_CD = "typeOfOwnershipCode";
    }


    public static final class OleInstanceItemType {
        public static final String INSTANCE_ITEM_TYPE_CODE = "dataObject.instanceItemTypeCode";
        public static final String INSTANCE_ITEM_TYPE_CD = "instanceItemTypeCode";
    }

    public static final class OleSpecificRetentionPolicyTypeUnit {
        public static final String SPECIFIC_POLICY_UNIT_TYPE_CODE = "dataObject.specificPolicyUnitTypeCode";
        public static final String SPECIFIC_POLICY_UNIT_TYPE_CD = "specificPolicyUnitTypeCode";
    }

    public static final class OleSourceOfTerm {
        public static final String SOURCE_OF_TERM_CODE = "dataObject.sourceOfTermCode";
        public static final String SOURCE_OF_TERM_CD = "sourceOfTermCode";
    }

    public static final class OleReceiptStatus {
        public static final String RECEIPT_STATUS_CODE = "dataObject.receiptStatusCode";
        public static final String RECEIPT_STATUS_CD = "receiptStatusCode";
    }

    public static final class OlePrivacy {
        public static final String PRIVACY_CODE = "dataObject.privacyCode";
        public static final String PRIVACY_CD = "privacyCode";
    }

    public static final class OleNotationType {
        public static final String NOTATION_TYPE_CODE = "dataObject.notationTypeCode";
        public static final String NOTATION_TYPE_CD = "notationTypeCode";
    }

    public static final class OleRecordType {
        public static final String RECORD_TYPE_CODE = "dataObject.recordTypeCode";
        public static final String RECORD_TYPE_CD = "recordTypeCode";
    }

    public static final class OleReproductionPolicy {
        public static final String REPRODUCTION_POLICY_CODE = "dataObject.reproductionPolicyCode";
        public static final String REPRODUCTION_POLICY_CD = "reproductionPolicyCode";
    }

    public static final class OleSeperateOrCompositeReport {
        public static final String SEPERATE_OR_COMPOSITE_REPORT_CODE = "dataObject.seperateOrCompositeReportCode";
        public static final String SEPERATE_OR_COMPOSITE_REPORT_CD = "seperateOrCompositeReportCode";
    }

    public static final class OleShelvingOrder {
        public static final String SHELVING_ORDER_CODE = "dataObject.shelvingOrderCode";
        public static final String SHELVING_ORDER_CD = "shelvingOrderCode";
    }

    public static final class OleShelvingScheme {
        public static final String SHELVING_SCHEME_CODE = "dataObject.shelvingSchemeCode";
        public static final String SHELVING_SCHEME_CD = "shelvingSchemeCode";
    }

    public static final class OleCompleteness {
        public static final String COMPLETENESS_CODE = "dataObject.completenessCode";
        public static final String COMPLETENESS_CD = "completenessCode";
    }

    public static final class OleElectronicLocationAndAccessRelationship {
        public static final String ELECTRONIC_LOCATION_AND_ACCESS_RELATIONSHIP_CODE = "dataObject.elaRelationshipCode";
        public static final String ELECTRONIC_LOCATION_AND_ACCESS_RELATIONSHIP_CD = "elaRelationshipCode";
    }

    public static final class OleSpecificRetentionPolicyType {
        public static final String SPECIFIC_RETENTION_POLICY_TYPE_CODE = "dataObject.specificRetentionPolicyTypeCode";
        public static final String SPECIFIC_RETENTION_POLICY_TYPE_CD = "specificRetentionPolicyTypeCode";
    }

    public static final class OleEncodingLevel {
        public static final String ENCODING_LEVEL_CODE = "dataObject.encodingLevelCode";
        public static final String ENCODING_LEVEL_CD = "encodingLevelCode";
    }

    public static final class OleFieldEncodingLevel {
        public static final String FIELD_ENCODING_LEVEL_CODE = "dataObject.fieldEncodingLevelCode";
        public static final String FIELD_ENCODING_LEVEL_CD = "fieldEncodingLevelCode";
    }

    public static final class OleGeneralRetentionPolicy {
        public static final String GENERAL_RETENTION_POLICY_CODE = "dataObject.generalRetentionPolicyCode";
        public static final String GENERAL_RETENTION_POLICY_CD = "generalRetentionPolicyCode";
    }

    public static final class OleLendingPolicy {
        public static final String LENDING_POLICY_CODE = "dataObject.lendingPolicyCode";
        public static final String LENDING_POLICY_CD = "lendingPolicyCode";

    }


    //OleCirculationDesk Constants
    public static final class OleCirculationDesk {
        public static final String OLE_CIRCULATION_DESK_MAINTENANCE_ACTION_LINK = "oleCirculationDeskMaintenance";
        public static final String OLE_CIRCULATION_DESK_CODE = "dataObject.circulationDeskCode";
        public static final String OLE_CIRCULATION_DESK_LOCATION = "circulationDeskLocation";
        public static final String OLE_CIRCULATION_DESK_LOCATION_ERROR = "error.circulationDesk.location";
        public static final String OLE_CIRCULATION_DESK_LOCATION_DUPLICATE_ERROR = "error.circulationDesk.locationDuplicate";
        public static final String OLE_CIRCULATION_DESK_LOCATION_MAPPED_ERROR = "error.circulationDesk.locationMapped";
        public static final String OLE_CIRCULATION_DESK_CD_EXIST = "error.duplicate.circulationDeskCode";
        public static final String OLE_CIRCULATION_DESK_CD = "circulationDeskCode";
        public static final String OLE_CIRCULATION_DESK_MAP = "dataObject.active";
        public static final String OLE_CIRCULATION_DESK_MAP_ERROR = "error.record.mapped";
        public static final String OLE_CIRCULATION_DESK_ID = "circulationDeskId";
        public static final String OLE_CIRCULATION_DESK_VALIDATIONS = "has not been assigned to work at any Circulation Desk.";
        public static final String OLE_CIRCULATION_DESK_LOCATION_VALID_ERROR = "error.valid.circulationDesk.location";
        public static final String COPY = "copy";
        public static final String EDIT = "edit";
        public static final String OLE_INVALID_CIRCULATION_DESK_LOCATION = "error.valid.circulationDesk.invalid.location";
        public static final String OLE_INVALID_FEE_TYPE = "error.valid.feeType";
        public static final String OLE_COPY_FORMAT_CODE = "code";
        public static final String OLE_COPY_FORMAT_CODE_ERROR = "error.copyformat.code";
        public static final String SOLR_MAX_PAGE_SIZE = "solr.max.page.size.for.update.item.status.job";
    }

    public static final class OleLocationLevel {
        public static final String EDIT_LOCATION_LEVEL_PERM = "Edit Location Level Name Field";
        public static final String EDIT_LOCATION_LEVEL_NMSPC_CODE = "OLE-KRAD";
        /*        public static final String DESCRIPTION = "Description";
                public static final String EXPLANATION = "Explanation";
                public static final String DOCUMENT_NUMBER = "Organization Document Number";*/
        public static final String CODE = "Level Code";
        public static final String PARENT_ID = "Parent Level Id";
    }

    public static final class OleLocationIngestSummaryRecord {
        public static final String DATE_FORMAT = "MM/dd/yyyy";
        public static final String DATE = "date";
    }

    public static final class EntityAddressTypeBo {
        public static final String ADDRESS_TYPE_NAME = "dataObject.name";
    }

    public static final class EntityPhoneTypeBo {
        public static final String PHONE_TYPE_NAME = "dataObject.name";
    }

    public static final class EntityEmailTypeBo {
        public static final String EMAIL_TYPE_NAME = "dataObject.name";
    }

    public static final class OleBorrowerType {
        public static final String BORROWER_TYPE_CODE = "dataObject.borrowerTypeCode";
        public static final String BORROWER_TYPE_CD = "borrowerTypeCode";
        public static final String BORROWER_TYPE_ACTIVE = "dataObject.active";
        public static final String BORROWER_TYPE_ACTIVE_ERROR = "error.borrower.used";
    }

    public static final class OleCheckList {
        public static final String CHECK_LIST_MAINTENANCE_ACTION_LINK = "oleCheckListMaintenance";
        public static final String CHECK_LIST_LINK = "oleCheckListMaintenance?viewTypeName=MAINTENANCE&returnLocation=";
        //public static final String METHOD_TO_CALL = "&methodToCall=downloadAttachment&dataObjectClassName=org.kuali.ole.license.bo.OleCheckListBo&oleCheckListId=";
        public static final String METHOD_TO_CALL = "&methodToCall=downloadCheckListAttachment&dataObjectClassName=org.kuali.ole.license.bo.OleCheckListBo&oleCheckListId=";
    }

    public static final String DOC_TYP_ID = "documentTypeId";
    public static final String DOC_ID = "documentId";
    public static final String DAMAGED_NOTE ="<b>The item has damage note: </b>";
    public static final String CLAIMS_NOTE ="<b>The item has claims return note: </b>";
    public static final String MISSING_PIECE_NOTE ="<b>The item has missing piece note: </b>";


    public static final class OleLicenseRequest {
        public static final String LICENSE_REQUEST_DOC_TYPE = "LicenseRequestDocument";
        public static final String LICENSE_DOCUMENT_INITIATOR = "license.document.initiator";
        public static final String LICENSE_REQ_DOCUMENT_STATUS = "license.document.status";
        public static final String LICENSE_DESC = "LicenseNeeded-REQS_";
        //public static final String AGREEMENT_LOC = "/tmp/agreement";
        public static final String AGREEMENT_LOCATION = File.separator + "agreement" + File.separator + "agreement_upload";
        public static final String AGREEMENT_TMP_LOCATION = File.separator + "agreement" + File.separator + "agreement_tmp";
        public static final String PLATFORM_EVENT_TMP_LOCATION = File.separator + "platform" + File.separator + "platform_tmp";
        public static final String ERESOURCE_EVENT_TMP_LOCATION = File.separator + "eResource" + File.separator + "eResource_tmp";
        public static final String AGREEMENT_DELETE = File.separator + "agreement" + File.separator + "agreement_delete";
        public static final String LICENSE_RULE_VAILDATIONS = "license.rule.validations";
        public static final String LICENSE_DISAPPROVE_VALIDATIONS = "license.disapprove.validations";
        public static final String LICENSE_EVENT_LOG_CHECK = "license.event.log.check";
        public static final String LICENSE_AGENDA_NM = "LICENSE_POLICY";
        public static final String SIGNATORY_ROLE_NM = "OLE_Signatory";
        public static final String LICENSE_MNGR_ROLE_NM = "OLE_LicenseManager";
        public static final String REVIEWER_ROLE_NM = "OLE_LicenseReviewer";
        public static final String APPROVER_ROLE_NM = "OLE_Licensing_Approver";
        public static final String SIGNATORY_ONLY = "SIGO";
        public static final String REVIEW_ONLY = "RVWO";
        public static final String APPROVE_ONLY = "APPO";
        public static final String FULL_APPROVAL = "FAPP";
        public static final String UNIVERSITY_COMPLETE = "UNIC";
        public static final String PENDING_SIGNATURE = "PSIG";
        public static final String PENDING_REVIEW = "PREV";
        public static final String PENDING_APPROVAL = "PAPP";
        public static final String PENDING_UNIVERSITY = "PUNI";
        public static final String SIGNATORY_COMPLETE = "SIGC";
        public static final String REVIEW_COMPLETE = "RVWC";
        public static final String LICENSE_COMPLETE = "LC";
        public static final String NEGOTIATION_FAILED = "LNF";
        public static final String WORKFLOW_INITIAL_FILTER = "License Request workflow initial filter";
        public static final String FULL_WORKFLOW = "Full License Request workflow";
        public static final String WORKFLOW_SEC_FILTER = "License Request secondary filter";
        /*public static final String LIC_NAMESPACE="OLE";*/
        public static final Map<String, String> INITIAL_FILTER_WORKFLOW_CODE = getInitialFilterWprkflowCode();
        public static final Map<String, String> SEC_FILTER_WORKFLOW_CODE = getSecondFilterWprkflowCode();
        public static final String START_TAG = "</oldMaintainableObject>";
        public static final String END_TAG = "<fieldsClearedOnCopy>";
        public static final String ASSIGNEE = "assignee";
        public static final String LOCATION_ID = "locationId";
        public static final String STATUS_CODE = "licenseRequestStatusCode";
        public static final String BIB_TITLE = "bibliographicTitle";
        public static final String MSG_DELETE_DOC = "error.delete.document";
        public static final String DOCUMENT_NOT_DELETED ="error.delete.no.document";
        public static final String ERROR_FILE_NOT_FOUND = "error.file.not.found";
        public static final String ERROR_CHECKLIST_NOT_FOUND = "error.checklist.not.found";

        public static final String LICENSE_COMPLETE_RETURN = "LRC";
        public static final String LICENSE_NEGOTIATION_FAILED = "NF";
        public static final String AGREEMENT_ERROR = "error.agreement.detail";
        public static final String LICENSE_REQ_INTIAL_EVENT_LOG = " License Request initiated by E-Resource.";
        //public static final String LICENSE_NMSPACE = "OLE-LIC";
        public static final String LICENSE_NMSPACE = "OLE-LIC";
        public static final String LICENSE_ASSIGNEE_ROLE = "OLE_LicenseAssignee";
        public static final String LICENSE_INITIAL_WORKFLOW = "LND";
        public static final String LICENSE_INITIAL_LOCATON = "1";
        public static final String LICENSE_REQUEST_TYPE_ID = "licenseRequestTypeId";
        public static final String E_RES_NAME = "eResourceName";
        public static final String LICENSE_NEEDED = "LN";
        public static final String REQUISITION_DOC_NUM = "requisitionDocNumber";
        /*        public static final String KEW_DOC_HANDLER = "kew/DocHandler.do?command=displayDocSearchView&docId=";*/
        public static final String LICENSE_RECEIVED = "LIRC";
        public static final String LICENSE_REQUESTED = "LRTD";
        public static final String LICENSE_REQUESTED_VALUE = "LRQ";
        public static final String LICENSE_IN_PROCESS = "IPS";
        public static final String LICENSE_IN_PROCESS_VALUE = "IP";
        public static final String LICENSE_IN_NEGO = "INN";
        public static final String LICENSE_IN_NEGO_VALUE = "IN";
        public static final String ADDENDUM = "ADDUM";
        public static final String EVENT_TYPE_NM = "eventTypeName";
        public static final String EVENT_TYPE_NAME = "dataObject.eventTypeName";
        public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
        public static final String DO_ASSIGNEE = "Owner";
        public static final String INITIATE_LR_WORKFLOW = "Initiate Licensing Workflow";
        public static final String DO_LICENSE_REQUEST_TYPE = "License Request Type";
        public static final String DO_AGR_MTHD = "Agreement Method";
        public static final String ERROR_REQUIRED = "error.required";
        public static final String ERROR_DATE_FROM_EXCEEDS_DATE_TO = "error.date.from.exceeds.date.to";
        public static final String ERROR_NOT_FOUND = "error.not.found";
        public static final String CREATED_FROM_DATE = "createdDateFrom";
        public static final String CREATED_TO_DATE = "createdDateTo";
        public static final String LAST_MOD_FROM_DATE = "lastModifiedDateFrom";
        public static final String LAST_MOD_TO_DATE = "lastModifiedDateTo";
        public static final String LAST_MOD_SEARCH_TYPE = "lastModifiedDateSearchType";
        public static final String USER = "user";
        public static final String HELPER_SERVICE = "oleLicenseRequestWebService";
        public static final String LICENSE_REQUEST_SERVICE = "oleLicenseRequestService";


        public static final Map<String, String> getInitialFilterWprkflowCode() {
            Map<String, String> initalWorkflowCodes = new HashMap<String, String>();
            initalWorkflowCodes.put("ADDUM", "ADDUM");
            initalWorkflowCodes.put("APPO", "APPO");
            initalWorkflowCodes.put("FAPP", "FAPP");
            initalWorkflowCodes.put("REWAL", "REWAL");
            initalWorkflowCodes.put("RVWO", "RVWO");
            initalWorkflowCodes.put("SIGO", "SIGO");
            return Collections.unmodifiableMap(initalWorkflowCodes);
        }

        public static final Map<String, String> getSecondFilterWprkflowCode() {
            Map<String, String> secondWorkflowCodes = new HashMap<String, String>();
            secondWorkflowCodes.put("INN", "INN");
            secondWorkflowCodes.put("IPS", "IPS");
            secondWorkflowCodes.put("LIRC", "LIRC");
            secondWorkflowCodes.put("LND", "LND");
            secondWorkflowCodes.put("LRTD", "LRTD");
            return Collections.unmodifiableMap(secondWorkflowCodes);
        }
    }

    public static final String NAMESPACE_CODE_SELECTOR = "namespaceCode";
    public static final String NAME_SELECTOR = "name";

    //Loan Constants
    public static final String PTRN_START_LINK = "<a id=\"u20\" href=\"patronMaintenance?viewTypeName=MAINTENANCE&amp;returnLocation=";
    public static final String PTRN_END_LINK = "/portal.do&amp;methodToCall=start&amp;dataObjectClassName=org.kuali.ole.deliver.bo.OlePatronDocument\" target=\"_blank\" title=\"Create New Patron with\" class=\"uif-field uif-link uif-createNewLink\">Create New Patron</a>";
    public static final String PTRN_BARCD_NOT_EXT = "Patron barcode does not exist.";
    public static final String PTRN_LOST_BARCODE = "Patron barcode is reported lost/stolen. Do Not Loan";
    public static final String ERROR_PTRN_BARCD_NOT_EXT = "error.patron.barcode.not.exist";
    public static final String ITM_BARCD_NT_AVAL_DOC = "Item barcode does not exist.";
    public static final String TIT_NT_EXT = "Title does not exist.";
    public static final String PAR_EXP = "Parser Exception-item xml to item pojo.";
    public static final String ITM_STS_NT_AVAL = "Item status is unavailable.";
    public static final String KRMS_EXP_MSG = "Please ingest the deliver xml in KRMS Builder.";
    public static final String ITM_STS_TO_DOC_FAIL = "Error while updating item information";
    public static final String DAT_FORM = "MMM dd, yyyy hh:mm:ss a";
    public static final String DAT_FORMAT_EFFECTIVE = "MM/dd/yyyy hh:mm:ss a";
    public static final String DAT_FORMAT_EFFECTIVE_PRINT = "MM/dd/yyyy hh:mm a";
    public static final String DAT_FORMAT_EFFECTIVE_NOTICE = "MM/dd/yyyy hh:mm:ss";
    public static final String INVAL_LOC = "error.invalid.location";
    public static final String ITEM_NOT_AVAILABLE = "item.not.available";
    public static final String INVAL_ITEM = "Either item type or item status or both are empty";
    public static final String GENERAL_BLOCK = "generalBlock";
    public static final String LOST_BARCODE = "lostBarcode";
    public static final String ADDR_VERIFIED = "isAddressVerified";
    public static final String EXPIR_DATE = "expirationDate";
    public static final String PROXY_EXPIR_DATE = "proxyExpirationDate";
    public static final String PATRON_BAR = "patronBarcode";
    public static final String IS_PROXY_PATRON = "isProxyPatron";
    public static final String IS_ACTIVE_PATRON = "activePatron";
    public static final String IS_ACTIVE_PROXY_PATRON = "activeProxyPatron";
    public static final String PATRON_ACTIVATION_DATE = "activationDate";
    public static final String EXPIRATION_DATE_STRING = "expirationDateString";
    public static final String ACTIVATION_DATE_STRING = "activationDateString";
    public static final String PROXY_PATRON_ACTIVATION_DATE = "proxyActivationDate";
    public static final String PROXY_PATRON_ACTIVATION_DATE_STRING = "proxyActivationDateString";
    public static final String PROXY_PATRON_EXPIRATION_DATE_STRING = "proxyExpirationDateString";
    public static final String DIGIT_ROUTINE = "digitRoutine";
    public static final String PATTERN = "pattern";
    public static final String ERROR_ACTION = "errorMessage";
    public static final String ERRORS_AND_PERMISSION = "errorsAndPermission";
    public static final String PERMISSION_NAME = "permissionName";
    public static final String PERMISSION_ACTION = "roleName";
    public static final String LOST_STOLEN = "Lost/Stolen";
    public static final String FALSE = "false";
    public static final String H4 = "<h4>";
    public static final String H5 = "<h5>";
    public static final String TEXT_BOLD_TAG_START = "<b>";
    public static final String TEXT_BOLD_TAG_CLOSE = "</b>";
    public static final String H4_CLOSE = "</h4><br/>";
    public static final String H5_CLOSE = "</h5><br/>";
    public static final String BREAK = "<br/>";
    public static final String SEMI_COLON = ":";
    public static final String GENERAL_BLOCK_MESSAGE = "This patron has a general block";
    public static final String ITEMSTATUSLOST = "please return the Item to proceed Loan.";
    public static final String COMMA = ",";
    public static final String SPACE = " ";
    public static final String HM = "HM";
    public static final String USER = "USER";
    public static final String ENTITY_ID = "entityId";
    public static final String HUNDRED_A = "100a";
    public static final String TWOFORTY_A = "245a";
    public static final String ID_COLAN = "id:";
    public static final String NO_AUTHOR = "No Author";
    public static final String OLE_LOAN_DOCUMENT_LIST = "oleLoanDocumentList";
    public static final String LIST_OVERDUE_DAYS = "listOfOverDueDays";
    public static final String HOURS_DIFF = "hoursDiff";
    public static final String LIST_RECALLED_OVERDUE_DAYS = "listOfRecalledOverdueDays";
    public static final String DDMMYYYYHHMMSS = "dd-MM-yyyy hh:mm:ss";
    public static final String LOCATION = "location";
    public static final String NUM_ITEMS_CHECKED_OUT = "numberOfItemsCheckedOut";
    public static final String NUM_OVERDUE_ITEMS_CHECKED_OUT = "numberOfOverDueItemsCheckedOut";
    public static final String NUM_OVERDUE_RECALLED_ITEMS_CHECKED_OUT = "numberOfOverDueRecalledItemsCheckedOut";
    public static final String NUM_CLAIMS_RETURNED = "numberOfClaimsReturned";
    public static final String OVERDUE_FINE_AMT = "overdueFineAmt";
    public static final String REPLACEMENT_FEE_AMT = "replacementFeeAmt";
    public static final String ALL_CHARGES = "allCharges";
    public static final String IS_RENEWAL = "isRenewal";
    public static final String NUM_RENEWALS = "numberOfRenewals";
    public static final String ITEMS_DUE_DATE = "itemDueDate";
    public static final String MAX_PAGE_SIZE_FOR_LOAN = "25000";
    public static final String ITEMS_DUE_DATE_STRING = "itemDueDateString";
    public static final String ITEM_BARCODE = "itemBarcode";
    public static final String FEE_TYPE_FIELD = "feeType";
    public static final String FEE_TYPE_CODE = "feeTypeCode";
    public static final String FEE_TYPE_CODE_OVERDUE = "OVR_DUE";
    public static final String FEE_TYPE_CODE_REPL_FEE = "REPL_FEE";
    public static final String LOST_ITEM_PRCS_FEE = "LOST_ITEM_PRCS_FEE";
    public static final String LOANED_ITEM_COUNT = "loanedItemCount";
    public static final String FEE_TYPE_PATRON_ID = "patronBillPayment.patronId";
    public static final String ITEM_LOCATION = "itemLocation";
    public static final String IS_ITEM_PRICE = "isItemPrice";
    public static final String DELIVERY_PRIVILEGES = "deliveryPrivileges";
    public static final String ITEM_PICKUP_LOCATION = "itemPickUpLocation";
    public static final String DESTINATION_LOCATION = "destinationLocation";
    public static final String CIRCULATION_LOCATION = "operatorsCirculationLocation";
    public static final String IS_PATRON_POSITION_ONE = "isPatronInPositionOne";
    public static final String REQUEST_TYPE = "requestType";
    public static final String ITEM_STATUS = "itemStatus";
    public static final String REPLACEMENT_FEE_EXIST = "replacementFeeExist";
    public static final String OVERDUE_FINE_EXIST = "overdueFineExist";
    public static final String DIFF_PATRON_FLD = "differentPatron";
    public static final String ITEM_DAMAGED_STATUS_FLD = "itemDamagedStatus";
    public static final String ITEM_MISING_PICS_FLAG_FLD = "missingPieceFlag";
    public static final String ITEM_SHELVING = "itemShelving";
    public static final String ITEM_COLLECTION = "itemCollection";
    public static final String ITEM_LIBRARY = "itemLibrary";
    public static final String ITEM_INSTITUTION = "itemInstitution";
    public static final String ITEM_CAMPUS = "itemCampus";
    public static final String DELETE_TEMP_HISTORY_REC = "deleteTemporaryHistoryRecord";
    public static final String DATE_CHECK_IN = "checkInDate";
    public static final String DELETE = "delete";
    public static final String OVERDUE_DAY_LIMIT_ERROR = "OverdueDayLimit exceeds for an item, Block the Patron";
    public static final String RECALL_OVERDUE_DAY_LIMIT_ERROR = "RecallOverdueDayLimit exceeds for an item, Block the Patron";
    public static final String OVERDUE_DAY = "overdueDay";
    public static final String OVER_DUE_FINE = "overdueFine";
    public static final String NOTICE_TYPE = "noticeType";
    public static final String UPDATE_ITEM_STATUS = "updateItemStatus";
    public static final String LOAN_PERIOD = "loanPeriod";
    public static final String FIXED_DUE_DATE = "fixedDueDate";
    public static final String CIRCULATION_POLICY_SET_ID = "circulationPolicySetId";
    public static final String COUNT = "count";
    public static final String RECALL_COUNT = "recallCount";
    public static final String OVERDUE_COUNT = "overdueCount";
    public static final String LIST_OF_OVERDUE_DAYS = "listOfOverdueDays";
    public static final String DUE_DATE = "dueDate";
    public static final String FINE_RATE = "fineRate";
    public static final String MAX_FINE = "maxFine";
    public static final String CHECKOUT = "checkOut";
    public static final String REPLACEMENT_BILL = "replacementBill";
    public static final String GENERATE_NOTICE = "generateNotice";
    public static final String CIRC_POLICY_FOUND = "circulationPolicyFound";
    public static final String NOTICE = "notice";
    public static final String OR = " (OR) ";
    public static final String ZERO = "0";
    public static final String LOCATION_ID = "locationId";
    public static final String SLASH = "/";
    public static final String NAME_NM = "name";
    public static final String AGENDA_NAME = "agendaName";
    public static final String LEVEL_NAME = "levelName";
    public static final String LEVEL_ID = "levelId";
    public static final String CAN_OVERRIDE_LOAN = "Can Override";
    public static final String CAN_DISPLAY_BILL = "Display Bill";
    public static final String CAN_CREATEORUPDATE_BILL = "Create/update Bill";
    public static final String EDIT_PATRON_DOCUMENT = "Edit Patron Maintenance Document";
    public static final String CAN_UPDATE_IEM_STATUS = "Can update item status";
    public static final String CAN_CHECKIN = "Can Checkin";
    public static final String DISPLAY_BIB = "Display Bib";
    public static final String DISPLAY_ITEM = "Display Item";
    public static final String DISPLAY_BIB_ERRORMSG = "You are not authorized to view Bib Information";
    public static final String DISPLAY_ITEM_ERRORMSG = "You are not authorized to view Item Information";
    public static final String EDIT_PATRON = "Initiate Patron Document";
    public static final String ASSIGN_EDIT_PATRON_ID = "patronMaintenance?olePatronId=";
    public static final String ASSIGN_PATRON_MAINTENANCE_EDIT = "&amp;methodToCall=maintenanceEdit&amp;dataObjectClassName=org.kuali.ole.deliver.bo.OlePatronDocument&amp;viewTypeName=MAINTENANCE&amp;dialogMode=true&amp;showHome=false&amp;showHistory=false";
    public static final String ASSIGN_INQUIRY_PATRON_ID = "inquiry?olePatronId=";
    public static final String ASSIGN_PATRON_INQUIRY = "&amp;methodToCall=start&amp;dataObjectClassName=org.kuali.ole.deliver.bo.OlePatronDocument&amp;dialogMode=true&amp;showHome=false&amp;showHistory=false";
    public static final String PATRON_NOTE_TYPE_ID = "patronNoteTypeId";
    public static final String SHEL_LAG_TIME = "shelvingLagTime";
    public static final String REQ_FIELD = "Please enter the required fields.";
    public static final String BORROWER_NAME = "borrowerName";

    public static final String CLAIMS_ITM_ERR_INFO = "Select an item(s).";
    public static final String SELECT_SINGLE_ITEM = "Select single Item";
    public static final String OVERRIDE_LOGIN_ERR_INFO = "has no permission to override the following block(s) :";
    public static final String ALTER_DUE_DATE_ERR_INFO = "No selected items.";
    public static final String FAST_ADD_ITM_ERR_INFO = "Record failed to save.";
    public static final String FAST_ADD_ITM_SUCCESS_INFO = "Record saved successfully.";
    public static final String FAST_ADD_ITM_VALIDATION_FAIL = "Barcode already exist.";
    public static final String RENEWAL_ITM_ERR_INFO = "Select an item from Currently Checked Out item(s).";
    public static final String RENEWAL_ITM_SUCCESS_INFO = "Item Renewal is done successfully.";
    public static final String RENEWAL_INDEFINITE_INFO = "Items on indefinite loan do not need to be renewed.";
    public static final String RENEWAL_DUEDATE_SAME_INFO =  "The item was not renewed because the new due date/time would not change.";
    public static final String RENEWAL_ITM_POPUP = "Do you want to renew the item?";
    public static final String PENDING_RQST_RENEWAL_ITM_INFO = "Item contains the pending request";
    public static final String RENEWAL_ITM_AFTER_FIXED_DUEDATE = "Please renew on or after fixed due date.";
    //public static final String FAST_ADD_ITEM_DEFAULT_STATUS = "FAST_ADD_ITEM_DEFAULT_STATUS";
    public static final String LOC_CD = "locationCode";
    public static final String LOC_CODE = "dataObject.locationCode";
    public static final String LOC_LEVEl_ID = "dataObject.levelId";
    public static final String LOC_ADMIN = "Location Administrator";
    public static final String LOC_LEVEL_ERROR = "error.locationLevel.unauthorized";
    public static final String LOC_LEVEL_SHELVING = "SHELVING";
    public static final String INDEFINITE = "Indefinite";

    public static final String LOCATION_LEVEL_SHELVING = "Shelving Location";
    public static final String LOCATION_LEVEL_COLLECTION = "Collection";
    public static final String LOCATION_LEVEL_LIBRARY = "Library";
    public static final String LOCATION_LEVEL_INSTITUTION = "Institution";
    public static final String LOCATION_LEVEL_CAMPUS = "Campus";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String BIB_UUID = "bibUuid";
    public static final String INSTANCE_UUID = "instanceUuid";
    public static final String ITEM_UUID = "itemUuid";
    public static final String ITEM_BARCODE_DISPLAY = "ItemBarcode_display:";
    public static final String INSTANCE_IDENTIFIER = "instanceIdentifier";
    public static final String ITEM_IDENTIFIER_DISPLAY = "ItemIdentifier_display";
    public static final String BIB_IDENTIFIER = "bibIdentifier";
    public static final String COPY_REQUEST = "Copy Request";
    public static final String COPY_REQUEST_FULFILL = "Verify whether copy request has been fulfilled";
    public static final String PAYMENT_STATUS_OUTSTANDING = "Outstanding";
    public static final String ITEM_STATUS_ON_HOLD = "ONHOLD";
    public static final String ITEM_STATUS_IN_TRANSIT = "INTRANSIT";
    public static final String ITEM_STATUS_IN_TRANSIT_HOLD = "INTRANSIT-FOR-HOLD";
    public static final String ITEM_STATUS_IN_TRANSIT_LOAN = "INTRANSIT-FOR-LOAN";
    public static final String ITEM_STATUS_IN_TRANSIT_STAFF = "INTRANSIT-PER-STAFF-REQUEST";
    public static final String DEFAULT_CALL_NUMBER_TYPE = "Other";
    public static final String DEFAULT_CALL_NUMBER = "X";
    public static final String FEE_TYPE_PAY_STATUS = "feeType.paymentStatus";
    public static final String PAY_STATUS = "paymentStatus";
    public static final String PAY_STATUS_CODE = "paymentStatusCode";
    public static final String PAY_STATUS_ID = "paymentStatusId";
    public static final String PAY_STATUS_OUTSTANDING_CODE = "PAY_OUTSTN";
    public static final String PAY_STATUS_PART_CODE = "PAY_PAR";
    public static final String PAY_STATUS_ERROR_CODE = "PAY_ERR";
    public static final String PAY_STATUS_FORGIVEN_CODE = "PAY_FORGIVEN";
    public static final String PAY_STATUS_FULL_CODE = "PAY_FULL";
    public static final String PAY_STATUS_CANCEL_CODE = "PAY_CANCELLED";
    public static final String FLAG_TYP_ITM_MISSING = "MISSING";
    public static final String FLAG_TYP_ITM_CLAIMS_RETURNED = "CLAIMS_RETURNED";
    public static final String FLAG_TYP_ITM_DAMAGED = "DAMAGED";
    public static final String ERROR_MISSING_ITEM_NO_GREATER= "Item no of pieces should no be less than 1";
    public static final String ERROR_MISSING_COUNT_NO_LESS= "No of missing piece count should no be less than 1";
    public static final String ERROR_MISSING_ITEM_GREATER_COUNT= "Missing piece count should not be greater than no of pieces";
    public static final String ERROR_MISSING_ITEM_COUNT_REQUIRED= "Provide missing  piece count  information";

    public static final String CHECKIN_TIME_FORMAT_MESSAGE = "Check-In time must be a valid 24 hour (0-23) time in HH:mm format, seconds are optional";
    public static final String ALTER_DUE_DATE_TIME_FORMAT_MESSAGE = "Cannot alter due date, Entered time must be a valid 24 hour (0-23) time in HH:mm format, seconds are optional";
    public static final String TIME_24_HR_PATTERN = "([01]?[0-9]|2[0-3]):(([0-5][0-9])|([0-5][0-9]:[0-5][0-9]))";
    public static final String DUE_DATE_TIME_FORMAT_MESSAGE = "Due date time must be a valid 24 hour (0-23) time in HH:mm format, seconds are optional";

    public static final class OleItemAvailableStatus {
        public static final String ITEM_AVAILABLE_STATUS_CODE = "dataObject.itemAvailableStatusCode";
        public static final String ITEM_AVAILABLE_STATUS_CD = "itemAvailableStatusCode";
        public static final String ITEM_ACTIVE_INDICATOR = "dataObject.active";
    }

    public static final class OleGloballyProtectedField {
        public static final String GLOBALLY_PROTECTED_ACTION_LINK = "oleGloballyProtectedFieldMaintenance";
    }

    //Agrreement download constants
    public static final class OleAgreementDownloadConstants {
        public static final String AGREEMENT_CHECKOUT_LOCATION = File.separator + "agreement" + File.separator + "agreement_checkout/request.xml";
        public static final String AGREEMENT_CHECKOUT_LOCATION_ROOT = File.separator + "agreement" + File.separator + "agreement_checkout/";

    }

    public static final class OleCirculationDeskDetail {
        public static final String OPERATOR_ROLE_NAME = "Operator";
        //public static final String OPERATOR_ROLE_NAMESPACE = "OLE-PTRN";
        public static final String OPERATOR_ROLE_NAMESPACE = "OLE-PTRN";
        public static final String OLE_CIRCULATION_UNAUTHORISED_USER_MSG = " The current user does not have sufficient permissions to access these screens";
    }


    public static final String CRCL_DSK_NO_LOC_ERR = "Preferred desk should be selected";
    public static final String CRCL_DSK_LOC_ERR = " Operator and desk should be selected for mapping";
    public static final String CRCL_DSK_INVALID_OPR = "Not a valid Operator";
    public static final String CRCL_DSK_SUCCESS = "Circulation desk mapping done successfully";
    public static final String RECIEVED = "Received";
    public static final String RECEIVED_PAID = "Recieved & Paid";
    public static final String UN_PAID = "Un Paid";
    public static final String PAID = "Paid";
    public static final String FULL_PAID = "PAY_FULL";
    public static final String PAR_PAID = "PAY_PAR";
    public static final String IN_ERROR = "PAY_ERR";
    public static final String ERROR = "Error";
    public static final String FORGIVEN = "PAY_FORGIVEN";
    public static final String FORGIVE = getParameter("PAYMENT_MODE_FORGIVE");
    public static final String CREDITS_ISSUED = "Credit Issued";
    public static final String FEE_CANCELLED = "Cancelled";
    public static final String FEE_UNSUSPENDED = "Fee Unsuspended";
    public static final String CANCELLED = "PAY_CANCELLED";
    public static final String SUSPENDED = "PAY_SUSPENDED";
    public static final String CANCEL = "Cancel";
    public static final String CANCEL_MESSAGE = "This Bill has been Cancelled ";
    public static final String CANCEL_MESSAGE_AMT = "This bill has been Cancelled with an amount ";
    public static final String FORGIVE_MESSAGE = getParameter("PAYMENT_MODE_FORGIVE_MESSAGE");
    public static final String ERROR_MESSAGE = " has made an error ";
    public static final String FEE_TYPE_NONE = "None";
    public static final String ITEM_STATUS_LOST = "LOST";
    public static final BigDecimal BIGDECIMAL_DEF_VALUE = new BigDecimal(0.00);
    public static final KualiDecimal KUALI_BIGDECIMAL_DEF_VALUE = new KualiDecimal(BIGDECIMAL_DEF_VALUE);
    public static final String PAID_AMT_EXC = "error.paid.amount.exceeds";
    public static final String PAY_AMT_EMPTY = "error.pay.amount.empty";
    public static final String PAID_AMT_EMPTY = "error.paid.amount.empty";
    public static final String PAY_METHOD_REQUIRED = "error.pay.method.required";
    public static final String ITM_BAR_NOT_AVAL = "error.item.barcode.not.available";
    public static final String ENTR_ITM_BAR = "error.enter.item.barcode";
    public static final String BILL_NOT_AVAI_BAR = "error.bill.not.avai.barcode";
    public static final String BILL_DT = "Date";
    public static final String BILL_NO = "Bill No";
    public static final String FIRST_NAME = "First Name";
    public static final String LAST_NAME = "Last Name";
    public static final String FEE_TYPE = "Fee Type";
    public static final String FEE_AMT = "Fee Amount";
    public static final String TOT_AMT = "Total Amount Due";
    public static final String TOT_AMT_PAID = "Total Amount Paid";
    public static final String ITM_BARCODE = "Item Barcode";
    public static final String ITM_TYP = "Item Type";
    public static final String ITM_TIT = "Item Title";
    public static final String ITM_AUT = "Item Author";
    public static final String ITM_CALL = "Call Number";
    public static final String ITM_COPY = "Copy Number";
    public static final String BILL_TEMP_NORMAL = "Normal";
    public static final String BILL_TEMP_TABLE = "Table";
    public static final String BILL_TITLE = "PATRON BILL";
    public static final String SELECT = "error.select";
    public static final String ERROR_TRANSACTION_SELECT = "error.no.transaction";
    public static final String ADD_FEE_TYPE = "error.add.fee.type";
    public static final String BAL_AMT = "Balance Amount : ";
    public static final String PTRN_ID = "patronId";
    public static final String PTRN_FN = "firstName";
    public static final String PTRN_LN = "lastName";
    public static final String PTRN_TYPE_ID = "patronTypeId";
    public static final String ITM_BAR_DISP = "ItemBarcode_display:";
    public static final String NO_BILLS = "error.no.bills";
    public static final String NOT_REVIEWED = "error.not.reviewed";
    public static final String NEGATIVE_NUM = "error.negative.number";
    public static final String ERROR_SELECTED_ITEM_FULLY_PAID = "error.selected.item.fully.paid";
    public static final String ERROR_OVER_PAYMENT = "error.bill.over.payment";
    public static final String FIXED_DUE_DATE_FIELD = "fixedDueDate";
    public static final String FIXED_DUE_DATE_MANDATORY = "error.deliver.fixedDueDate.field";
    public static final String FROM_DUE_DATE_FIELD = "fromDueDate";
    public static final String FROM_DUE_DATE_MANDATORY = "error.deliver.fromDueDate.field";
    public static final String TO_DUE_DATE_FIELD = "toDueDate";
    public static final String TO_DUE_DATE_MANDATORY = "error.deliver.toDueDate.field";
    public static final String TIME_SPAN_MANDATORY = "error.deliver.timespan.section";


    public static final class OleDeliverRequest {
        public static final String DELIVER_REQUEST_MAINTENANCE_ACTION_LINK = "deliverRequestMaintenance";

        public static final String PICKUP_LOCATION = "error.deliver.pickupLocation";
        public static final String PATRON_RECORD_EXPIRE = "error.deliver.patron.expire";
        public static final String PATRON_RECORD_FUTURE = "error.deliver.patron.future";
        public static final String INVALID_PROXY = "error.deliver.invalid.proxy";
        public static final String NO_DELIVERY_PRIVILEGE = "error.deliver.no.delivery.privilege";
        public static final String NO_PAGE_PRIVILEGE = "error.deliver.no.paging.privilege";
        public static final String ALREADY_RAISED = "error.deliver.request.raised";
        public static final String TRANSIT_ERROR = "error.deliver.request.transit";
        public static final String QUEUE_DUPLICATE = "Duplicate Queue Position found";
        public static final String BORROWER_ID = "borrowerId";
        public static final String PROXY_BORROWER_ID = "proxyBorrowerId";
        public static final String PROXY_PATRON_ID = "proxyPatronId";
        public static final String TRANSIT_REQUEST_RAISED = "error.deliver.request.transit.raised";
        public static final String REQUEST_TYPE_ID = "requestTypeId";
        public static final String ITEM_IN_LOAN = "error.deliver.request.item.loan";
        public static final String REQUEST_ID_INVALID = "error.deliver.request.item.invalidrequest";
        public static final String ITEM_ID = "itemId";
        public static final String LOAN_TRANS_ID = "loanTransactionId";
        public static final String FLAG = "flag";
        public static final String ITEM_UUID = "itemUUID";
        public static final String REQUEST_ID = "requestId";
        public static final String PATRON_ID = "olePatronId";
        public static final String PATRON_BARCODE = "barcode";
        public static final String LOAN_PATRON_ID = "patronId";
        public static final String REQUESTER_PATRON = "Patron";
        public static final String REQUESTER_PROXY_PATRON = "Proxy Patron";
        public static final String REQUESTER_OPERATOR = "Operator";
        /*        public static final String REQUEST_TYPE_ID_1 = "1";
                public static final String REQUEST_TYPE_ID_2 = "2";
                public static final String REQUEST_TYPE_ID_3 = "3";
                public static final String REQUEST_TYPE_ID_4 = "4";
                public static final String REQUEST_TYPE_ID_5 = "5";
                public static final String REQUEST_TYPE_ID_6 = "6";
                public static final String REQUEST_TYPE_ID_7 = "7";
                public static final String REQUEST_TYPE_ID_8 = "8";*/
        public static final String SHELVING = "Shelving";
        public static final String QUEUE_POSITION = "borrowerQueuePosition";
        public static final String REQUEST_QUEUE = "REQUEST_QUEUE";
        public static final String ITEM_BARCODE = "itemBarCode";
        public static final String ITEM_TYPE_CODE = "instanceItemTypeCode";
        public static final String DATE_FORMAT = "yyyyMMdd";
        public static final String EFF_DATE_FORMAT = "yyyy-MM-dd";
        public static final String INTRANSIT_STATUS = "INTRANSIT-PER-STAFF-REQUEST";
        public static final String ITEM_AVAILABLE = "error.deliver.request.item.available";
        public static final String ITEM_NOT_ELIGIBLE = "error.deliver.request.item.eligible";
        public static final String ITEM_ALREADY_LOANED = "error.deliver.request.item.already.loan";
        public static final String MAX_REQUEST_REACHED = "error.deliver.request.maximum.reached";
        public static final String NO_RECALL_REQUEST = "error.deliver.request.recall";
        public static final String NO_REQUEST = "error.deliver.request.all";
        public static final String NO_PAGE_REQUEST = "error.deliver.request.page";
        public static final String REQUEST_TYPE_CD = "requestTypeCode";
        //public static final String ITEM_NOT_AVAILABLE_IN_DOCSTORE = "error.deliver.item.notInDocstore";
        public static final String INVALID_LOCATION = "error.deliver.inValidPickupLocation";
        public static final String REORDER_SUCCESS = "Queue Position changed successfully";
        public static final String POSITIVE_QUEUE_POSITION = "Queue position should be a positive non zero value";
        public static final String NO_PENDING_REQUEST = "No Pending Request for this item";
        public static final String RECALL_DELIVERY = "Recall/Delivery";
        public static final String HOLD_DELIVERY = "Hold/Delivery";
        public static final String PAGE_DELIVERY = "Page/Delivery";
        public static final String RECALL_HOLD = "Recall/Hold";
        public static final String HOLD_HOLD = "Hold/Hold";
        public static final String PAGE_HOLD = "Page/Hold";
        public static final String ASR_REQUEST = "ASR Request";
        public static final String RECALL = "Recall";
        public static final String HOLD = "Hold";
        public static final String PAGE = "Page";
        public static final String COPY = "Copy";
        public static final String INTRANSIT = "In Transit";
        public static final String RECALL_BODY = "RECALL_BODY";
        public static final String RECALL_NOTICE_TYPE = "RECALL_NOTICE_TYPE";
        public static final String ONHOLD_BODY = "ONHOLD_BODY";
        public static final String ONHOLD_NOTICE_TYPE = "ONHOLD_NOTICE_TYPE";
        public static final String EXPIRED_REQUEST = "ExpiredRequestNotice";
        public static final String CLAIMS_RETURNED_FLAG = "claimsReturnedFlag";
        //public static final String EXPIRED_BODY = "EXPIRED_BODY";
        public static final String RQST_EXPR_NOTICE_TYPE = "RQST_EXPR_NOTICE_TYPE";
        public static final String NOTICE_VALIDATION = "Notice Validation";
        public static final String LOSTBILLREPLACEMENT = "lost/billed for replacement";
        public static final String MISSINGFINALOVERDUE = "missing/final overdue notice sent";
        public static final String WITHDRAWNCLIAMSRETURN = "withdrawn or claims returned";
        public static final String CLAIM_RETURNED = "claimsReturned";
        public static final String REPLACEMENT_FEE_PATRON_BILL = "replacementFeePatronBill";
        public static final String NUBER_OF_OVER_DUE_SENT = "numberOfOverdueSent";
        public static final String DUE_DATE_SUM_INTERVAL = "dueDateSumInterval";
        public static final String CONFIGURABLE_INTERVAL = "configurableInterval";
        public static final String INTERVAL_WITH_NOTICE_COUNT = "intervalWithNoticeCount";
        public static final String IS_TEMPORARY_HISTORY_RECORD = "isTemporaryHistoryRecord";
        public static final String EXP_HOLD_NOTICE_CONTENT = "EXP_HOLD_NOTICE_CONTENT";
        public static final String ONHOLD_COURTESY_NOTICE_CONTENT= "ONHOLD_COURTESY_NOTICE_CONTENT";
        public static final String ONHOLD_COURTESY_NOTICE_INTERVAL= "ONHOLD_COURTESY_NOTICE_INTERVAL";
        public static final String COURTESY_NOTICE_CONTENT = "COURTESY_NOTICE_CONTENT";
        public static final String OVERDUE_NOTICE_CONTENT = "OVERDUE_NOTICE_CONTENT";
        public static final String CLAIMS_RETURNED_NOTICE_CONTENT = "CLAIMS_RETURNED_NOTICE_CONTENT";
        public static final String CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE_CONTENT = "CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE_CONTENT";
        public static final String CLAIMS_RETURNED_NOT_FOUND_NOTICE_CONTENT = "CLAIMS_RETURNED_NOT_FOUND_NOTICE_CONTENT";
        public static final String CLAIMS_RETURNED_NOT_FOUND_FINES_OWED_NOTICE_CONTENT = "CLAIMS_RETURNED_NOT_FOUND_FINES_OWED_NOTICE_CONTENT";
        public static final String CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE_CONTENT = "CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE_CONTENT";
        public static final String CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE_CONTENT = "CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE_CONTENT";
        public static final String PAYMENT_STATUS_NAME = "paymentStatusName";
        public static final String MISSING = "Missing";
        public static final String ONHOLD = "OnHold";
        public static final String INVALID_PATRON = "error.deliver.request.invalidPatron";
        public static final String INVALID_PROXY_PATRON = "error.deliver.request.invalidProxyPatron";
        public static final String INVALID_REQUEST_TYPE = "error.deliver.request.invalidRequestType";
        public static final String INVALID_OPERATOR = "error.deliver.request.invalidOperator";
        public static final String BORROWER_CODE = "borrowerCode";
        public static final String BORROWER_BARCODE = "borrowerBarcode";
        public static final String INVALID_PATRON_BARCODE = "error.deliver.request.invalidPatronBarcode";
        public static final String REQUEST_OUTCOME_STATUS = "requestOutComeStatus";
    }

    public static final class OlePatronBill {
        public static final String LABEL_BILL_NUMBER="Bill No";
        public static final String LABEL_PATRON_RECEIPT_NUMBER="Trans Id";
        public static final String LABEL_TRANSACTION_DATE="Transaction Date";
        public static final String LABEL_OPERATOR_ID="Operator Id";
        public static final String LABEL_ITEM_BARCODE="Item Barcode";
        public static final String LABEL_ITEM_TITLE="Title";
        public static final String LABEL_ITEM_AUTHOR="Author";
        public static final String LABEL_ITEM_CALL_NUMBER="Call Number";
        public static final String LABEL_ITEM_COPY_NUMBER="Copy Number";
        public static final String LABEL_TOTAL_AMOUNT="Total Amount";
        public static final String LABEL_PAID_AMOUNT="Amount paid by user";
        public static final String LABEL_TRANSACTION_NUMBER="Check/CreditCard/Journal-Ledger Number";
        public static final String LABEL_TRANSACTION_NOTE="Transaction Note";
        public static final String LABEL_PAYMENT_MODE="Payment Mode";
        public static final String LABEL_FEE_TYPE="Fee Type";
        public static final String HEADER_PATRON_RECEIPT="PATRON RECEIPT";
        public static final String LABEL_NOTE="Note";
        public static final String NEW_PATRON_BILL_DESC="New Patron Bill";

    }

    public static final String VOL_NUM_DISPLAY = "VolumeNumber_display";
    public static final String CALL_NUM_DISPLAY = "CallNumber_display";
    public static final String COPY_NUM_DISPLAY = "CopyNumber_display";
    public static final String ENUMERATION_DISPLAY = "Enumeration_display";
    public static final String CHRONOLOGY_DISPLAY = "Chronology_display";
    public static final String ITEM_STATUS_DISPLAY = "ItemStatus_display";
    public static final String ITEM_IDENTIFIER_SEARCH = "ItemIdentifier_search";
    public static final String CALL_NUM_PREFIX_DISPLAY = "CallNumberPrefix_display";
    public static final String LOACTION_LEVEL_DISPLAY = "Location_display";
    public static final String IMPRINT_DISPLAY = "Imprint_display";
    public static final String PURCHASE_ORDER_LINE_ID_SEARCH = "PurchaseOrderLineItemIdentifier_search";
    public static final String BIB_AUTHOR = "Author";
    public static final String CALL_NUM = "callNumber";
    public static final String VOL_NUM = "volumeNumber";
    public static final String COPY_NUM = "copyNumber";
    public static final String BIBUUID = "bibUuid";
    public static final String EFF_DATE_FORMAT = "MMM dd, yyyy HH:MM:SS a";
    public static final String COURTESY_NOTICE = "Courtesy Notice";
    public static final String HOLD_EXP_COURTESY_NOTICE = "Expired Hold  Notice";
    public static final String OVERDUE_NOTICE = "Overdue Notice";
    public static final String CLAIMS_RETURNED_NOTICE = "Claims Returned Notice";
    public static final String CHECKOUT_RECEIPT_NOTICE = "Checkout Receipt";
    public static final String CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE = "Claims Returned Found No Fees Notice";
    public static final String CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE = "Claims Returned Found Fines Owed Notice";
    public static final String CLAIMS_RETURNED_NOT_FOUND_NOTICE = "Claims Returned Not Found Notice";
    public static final String CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE = "Claims Returned Not Found No Fees Notice";
    public static final String CLAIMS_RETURNED_NOT_FOUND_FINES_OWED_NOTICE_TITLE = "Claims Rtrnd Not Found Fines Owed Notice";
    public static final String RETURN_MISSING_PIECE_NOTICE = "Return With Missing Piece Notice";
    public static final String LOST_NOTICE = "Lost Notice";
    public static final String LOST_ITEM_PROCESSING_FEE_NOTICE = "Lost Item Processing Fee Notice";
    public static final String OVERDUE_FINE_NOTICE = "Overdue Fine Notice";
    public static final String NOTICE_HOLD_COURTESY = "HoldCourtesyNotice";
    public static final String NOTICE_RECALL = "RecallNotice";
    public static final String PICKUP_NOTICE = "Pickup Notice";
    public static final String NOTICE_ONHOLD = "OnHoldNotice";
    public static final String MY_ACCOUNT_URL = "MY_ACCOUNT_URL";
    public static final String OLE_MY_ACCOUNT_URL_CHANNEL = "/portal.do?channelTitle=MyAccount&channelUrl=";
    public static final String OLE_MY_ACCOUNT_URL = "/ole-kr-krad/myaccountcontroller?viewId=RenewalItemView&methodToCall=start";
    public static final String NOTICE_LOST = "Lost";
    public static final String PICKUP_NOTICE_START_CONTENT = "The item you requested is available for pickup from the ";
    public static final String PICKUP_NOTICE_MIDDLE_CONTENT = ". It will be held until ";
    public static final String PICKUP_NOTICE_FINAL_CONTENT = ". Please pick it up at your earliest convenience.";
    public static final String COURTESY_NOTICE_CONTENT_CONFIG_NAME = "Courtesy Notice Content Config Name";
    public static final String OVERDUE_NOTICE_CONTENT_CONFIG_NAME = "Overdue Notice Content Config Name";
    public static final String LOST_NOTICE_CONTENT_CONFIG_NAME = "Lost Notice Content Config Name";
    public static final String RECALL_NOTICE_CONTENT_CONFIG_NAME = "Recall Notice Content Config Name";
    public static final String REQUEST_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME = "Request Expiration Notice Content Config Name";
    public static final String ON_HOLD_NOTICE_CONTENT_CONFIG_NAME = "On Hold Notice Content Config Name";
    public static final String ON_HOLD_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME = "On Hold Expiration Notice Content Config Name";
    public static final String ON_HOLD_COURTESY_NOTICE_CONTENT_CONFIG_NAME = "On Hold Courtesy Notice Content Config Name";

    public static final int DEFAULT_NOTICE_THREAD_POOL_SIZE = 20;


    public static final class OleInvoiceImport {
        public static final String QTY_TYP = "QTY";
        public static final String ITM_CHART_CODE = "BL";
        public static final String PAY_METHOD = "4";
    }

    public static final class OverlayMatchingServiceImpl {
        public static final String LOCATION_LEVEL_SEARCH = "Location_search";
        public static final String ITEM_BARCODE_DISPLAY = "ItemBarcode_display";
        public static final String VENDOR_LINEITEM_IDENTIFIER = "VendorLineItemIdentifier_search";
    }

    public static final class OleExternalDataSourceConfig {
        public static final String DATA_SOURCE_NAME = "dataObject.name";
        public static final String DATA_SOURCE_NM = "name";
        public static final String DATA_SOURCE_MAINTENANCE_ACTION_LINK = "externalDataSourceMaintenance";
        public static final String DATA_SOURCE_DELETE = "Delete";
        public static final String DATA_SOURCE_MAINTENANCE_DOC_SERVICE = "externalDSConfigMaintenanceDocumentService";
    }

    public static final class OleUserPreferences {
        public static final String USER_PREF_NAME = "dataObject.PrefName";
        public static final String USER_PREF_CALL_NUMBER = "dataObject.callNumberSource1";
        public static final String USER_PREF_TAGS = "dataObject.removalTags";
        public static final String USER_PREF_NM = "prefName";
        public static final String USER_PREF_MAINTENANCE_ACTION_LINK = "userPreferences";
        public static final String USER_PREF_MAINTENANCE_DOC_SERVICE = "externalDSConfigMaintenanceDocumentService";
    }

    public static final class OleBibliographicRecordStatus {
        public static final String BIBLIOGRAPHICRECORD_STATUS_CODE = "dataObject.bibliographicRecordStatusCode";
        public static final String BIBLIOGRAPHICRECORD_STATUS_CD = "bibliographicRecordStatusCode";
    }

    public static final class OleDiscoveryExportProfile {
        public static final String OLE_EXP_MARC_FIELD = "dataObject.marcField";
        public static final String OLE_EXP_ITEM_FIELD = "dataObject.itemField";
        public static final String OLE_EXP_MARC_DUPLICATE_ERROR = "error.discoveryExportProfile.duplicateMARCField";
        public static final String OLE_EXP_ITEM_DUPLICATE_ERROR = "error.discoveryExportProfile.duplicateItemField";
        public static final String OLE_EXP_ADD_ITEM = "dataObject.marcField";
        public static final String OLE_EXP_ADD_ITEM_ERROR = "error.discoveryExportProfile.addItem";
    }

    public static final class OlePatronIngestLoadReport {
        public static final String CREATED_DATE_FORMAT = "MM/dd/yyyy";
        public static final String CREATED_DATE = "createdDate";
    }

    public static final String ERROR_DUPLICATE_CODE = "error.duplicate.code";
    public static final String ERROR_DUPLICATE_NAME = "error.duplicate.name";
    public static final String ERROR_DUPLICATE_ORDER_NO="error.order.no";
    public static final String ERROR_DUPLICATE_ORDER_NO_FOUND="error.duplicate.order.no";
    public static final String ERROR_EMPTY_ORDER_NO="empty.order.no";
    public static final String ERROR_EMPTY_STATUS="empty.status";
    public static final String ERROR_EMPTY_ROLE_NAME="empty.role.name";
    public static final String ERROR_EMPTY_PERSON_NAME="empty.person.name";
    public static final String ERROR_EMPTY_GROUP_NAME="empty.group.name";
    public static final String ERROR_EMPTY_ROLE="empty.role";
    public static final String ERROR_EMPTY_PERSON="empty.person";
    public static final String ERROR_EMPTY_GROUP="empty.group";
    public static final String ERROR_INVALID_ORDER_NO="invalid.order.no";
    public static final String ERROR_DUPLICATE_WORKFLOW_NAME="error.workflow.name";
    public static final String ERROR_ATLEAST_ONE_WORKFLOW = "error.atleast.one.workflow";
    public static final String ERROR_DUPLICATE_STATUS="error.status";
    public static final String ERROR_DUPLICATE_STATUS_FOUND="error.duplicate.status";
    public static final String ERROR_INVALID_ID_NAME="error.id.name";
    public static final String ERROR_INVALID_PERSON_ID_NAME="error.person.id.name";
    public static final String ERROR_INVALID_GROUP_ID_NAME="error.group.id.name";
    public static final String ERROR_INVALID_PERSON_ID="error.person.id";
    public static final String ERROR_INVALID_GROUP_ID="error.group.id";
    public static final String ERROR_INVALID_ID="error.id";
    public static final String ERROR_INVALID_NAME="error.name";
    public static final String ERROR_INVALID_PERSON_NAME="error.person.name";
    public static final String ERROR_INVALID_GROUP_NAME="error.group.name";
    public static final String ERROR_DUPLICATE_ROLE = "error.role";
    public static final String NAME_FIELD = "dataObject.name";
    public static final String ERROR_EMPTY_ROLE_PERSON_GROUP = "error.empty.role.person.group";
    public static final String NO_USER_FOR_GROUP = "error.no.user.group";
    public static final String NO_USER_FOR_ROLE = "error.no.user.role";
    public static final String EMPTY_MAIL_CONTENT = "error.empty.mail.content";
    public static final String EMPTY_USER_FOR_MAIL = "error.empty.mail.user";

    public static final class OleNoticeContentConfigurationRule {
        public static final String NOTICE_NAME_FIELD = "dataObject.noticeName";
        public static final String NOTICE_NAME = "noticeName";
    }

    public static final class OleAgreementDocTypeRule {
        public static final String AGR_DOC_TYPE_NAME_FIELD = "dataObject.agreementDocTypeName";
        public static final String AGR_DOC_TYPE_NAME = "agreementDocTypeName";
    }

    public static final class OleAgreementMethodRule {
        public static final String AGR_MTHD_NAME_FIELD = "dataObject.agreementMethodName";
        public static final String AGR_MTHD_NAME = "agreementMethodName";
    }

    public static final class OleAgreementStatusRule {
        public static final String AGR_STATUS_NAME_FIELD = "dataObject.agreementStatusName";
        public static final String AGR_STATUS_NAME = "agreementStatusName";
    }

    public static final class OLECancellationReasonRule {
        public static final String CNCL_RSN_NAME_FIELD = "dataObject.cancelReasonName";
        public static final String CNCL_RSN_NAME = "cancelReasonName";
    }

    public static final class OleAgreementTypeRule {
        public static final String AGR_TYPE_NAME_FIELD = "dataObject.agreementTypeName";
        public static final String AGR_TYPE_NAME = "agreementTypeName";
    }

    public static final String LOGIN_USR = "user";
    public static final String USER_ID = "userId";

    public static final String DESC_NMSPC = "OLE-DESC";
    public static final String DESC_CMPNT = "OLELSCMPNT";

    public static final String PRINT_NMSPC = "OLE-PRNT";
    //public static final String PRINT_CMPNT = "OLELSCMPNT";

    //Serial Receiving
    public static final String RECEIVED = "Received";
    public static final String CLAIMED = "Claimed";
    public static final String RCV_CANCELLED = "Cancelled";
    public static final String DISPLAY_YES = "Y";
    public static final String DISPLAY_NO = "N";
    public static final String SERIAL_RECEIVING_MAIN_PAGE = "OLESerialReceiving-MainPage";

    //E-res main docs jira OLE-2758
    public static final class OleMaterialTypeCode {
        public static final String MATERIAL_TYPE_NAME = "oleMaterialTypeName";
        public static final String MATERIAL_TYPE_FIELD = "dataObject.oleMaterialTypeName";
        public static final String ERROR_MATERIAL_TYPE_NAME = "error.duplicate.material.name";
    }

    public static final class OlePaymentType {
        public static final String PAYMENT_TYPE_NAME = "olePaymentTypeName";
        public static final String PAYMENT_TYPE_FIELD = "dataObject.olePaymentTypeName";
        public static final String ERROR_PAYMENT_TYPE_NAME = "error.duplicate.paymenttype.name";
    }

    public static final class OleContentType {
        public static final String CONTENT_TYPE_NAME = "oleContentTypeName";
        public static final String CONTENT_TYPE_FIELD = "dataObject.oleContentTypeName";
        public static final String ERROR_CONTENT_TYPE_NAME = "error.duplicate.contenttype.name";
    }

    public static final class OleAccessType {
        public static final String ACCESS_TYPE_FIELD = "dataObject.oleAccessTypeName";
        public static final String ACCESS_TYPE_NAME = "oleAccessTypeName";
        public static final String ERROR_ACCESS_TYPE_NAME = "error.duplicate.accesstype.name";
    }

    public static final class OleRequestPriority {
        public static final String REQUEST_PRIORITY_NAME = "oleRequestPriorityName";
        public static final String REQUEST_PRIORITY_FIELD = "dataObject.oleRequestPriorityName";
        public static final String ERROR_REQUEST_PRIORITY_NAME = "error.duplicate.requestpriority.name";
    }

    public static final class OleAuthenticationType {
        public static final String AUTH_TYPE_NAME_FIELD = "dataObject.oleAuthenticationTypeName";
        public static final String AUTH_TYPE_NAME = "oleAuthenticationTypeName";
    }

    public static final class OleAccessLocation {
        public static final String ACCESS_TYPE_NAME_FIELD = "dataObject.oleAccessLocationName";
        public static final String ACCESS_TYPE_NAME = "oleAccessLocationName";
    }

    public static final class OleDepartment {
        public static final String DEP_TYPE_NAME_FIELD = "dataObject.depCode";
        public static final String DEP_TYPE_CODE = "depCode";
    }

    public static final class OleEmployee {
        public static final String EMP_TYPE_CODE_FIELD = "dataObject.empName";
        public static final String EMP_TYPE_CODE = "empName";
    }

    public static final class OleSubject {
        public static final String SUB_TYPE_CODE_FIELD = "dataObject.subjectCode";
        public static final String SUB_TYPE_CODE = "subjectCode";
    }

    public static final class OleEResourceStatus {
        public static final String E_RES_STAT_TYPE_NAME_FIELD = "dataObject.oleEResourceStatusName";
        public static final String E_RES_STAT_TYPE_NAME = "oleEResourceStatusName";
    }

    public static final class OlePackageType {
        public static final String PACKG_TYPE_NAME_FIELD = "dataObject.olePackageTypeName";
        public static final String PACKG_TYPE_NAME = "olePackageTypeName";
    }

    public static final class OlePackageScope {
        public static final String PACKG_SCOPE_NAME_FIELD = "dataObject.olePackageScopeName";
        public static final String PACKG_SCOPE_NAME = "olePackageScopeName";
    }

    public static final class OLEEResPltfrmEventType {
        public static final String ERES_PLTFRM_EVNT_TYP_NAME_FIELD = "dataObject.eResPltfrmEventTypeName";
        public static final String ERES_PLTFRM_EVNT_TYP_NAME = "eResPltfrmEventTypeName";
    }

    public static final class OLEMarcRecordSourceType {
        public static final String MARC_REC_SRC_TYP_NAME_FIELD = "dataObject.marcRecordSourceTypeName";
        public static final String MARC_REC_SRC_TYP_NAME = "marcRecordSourceTypeName";
    }

    public static final class OLEMobileAccess {
        public static final String MOB_ACC_NAME_FIELD = "dataObject.mobileAccessName";
        public static final String MOB_ACC_NAME = "mobileAccessName";
    }

    public static final class OLEProblemType {
        public static final String PRBLM_TYPE_NAME_FIELD = "dataObject.problemTypeName";
        public static final String PRBLM_TYPE_NAME = "problemTypeName";

        public static final String PRBLM_TYPE_DEFAULT_INDICATOR_FIELD = "dataObject.defaultIndicator";
        public static final String PRBLM_TYPE_DEFAULT_INDICATOR = "defaultIndicator";
    }

    public static final class OLEAddTitlesToInvoice {
        public static final String ERROR_SELECT_ATLEAST_ONE_TITLE = "Select atleast one Title";
        public static final String ERROR_SELECT_TITLE_SAME_VENDOR = "Select Titles Having Same Vendor for Invoice processing";
        public static final String ERROR_SELECT_RECEIVE_SAME_PO = "Select Titles Belongs to same PO for Receiving";
        public static final String ERROR_DATE = "Enter Date";
        public static final String ERROR_VND_AMT = "Enter Vendor Amount";
        public static final String ERROR_INVOICE_NUMBER = "Enter Invoice Number";
        public static final String ERROR_SELECT_PAYMENT_METHOD = "Select Payment Method";
        public static final String SUCCESS_CREATE_INVOICE = "Invoice Created Successfully";
        public static final String ERROR_SELECT_POITEM_INVOICE_SAME_VND = "Select Titles and Invoice having same Vendor";
        public static final String UPDATE_INVOICE = "Invoice Updated Successfully";
        public static final String SUCCESS_RECEIVE = "Receiving created Successfully";
        public static final String ERROR_CREATE_RCV_PO_ITM_EXIST = "Failed to create receiving:receiving already exist for this purchase order";
        public static final String ERROR_PO_CLOSED = "Document status is closed for PurchaseOrders : ";
        public static final String LINK_START_TAG_HREF = "<a href=";
        public static final String ANCHOR_END = "/>";
        public static final String LINK_END_TAG = "</a>";
        public static final String LINK_DOC_HANDLER = "../kew/DocHandler.do?command=displayDocSearchView&amp;docId=";
        public static final String DOC_ID = " DOC_ID ";
        public static final String INV_OR_NOT_EXIST_DOC_NUM = "Invoice document does not Exist";
        public static final String INVALID_FORMAT_DOC = "Document number should be Number";
        public static final String ERROR_PO_ITEM_NULL = "Error Occured while loading page";
    }

    public static final class OLEEResourceRecord {
        public static final String NEW_E_RESOURCE_REC = "E-Resource";
        public static final String HELPER_SERVICE = "oleERSHelperService";
        public static final String DOCUMENT_MATERIAL_TYPES = "document.oleMaterialTypes*";
        public static final String DOCUMENT_CONTENT_TYPES = "document.oleContentTypes*";
        public static final String DOCUMENT_FORMAT_TYPES = "document.oleFormatTypes*";
        public static final String MATERIAL_TYPE_REQUIRED = "error.material.type.required";
        public static final String CONTENT_TYPE_REQUIRED = "error.content.type.required";
        public static final String FORMAT_TYPE_REQUIRED = "error.format.type.required";
        public static final String INVALID_VENDOR = "invalid.vendor";
        public static final String INVALID_PUBLISHER = "invalid.publisher";
        public static final String OLEERMAINTAB_OVERVIEW = "OLEEResourceMainTab-OverviewSection";
        public static final String ERESOURCE_TITLE = "title";
        public static final String ERESOURCE_ISBN = "ISBN";
        public static final String ERESOURCE_ISSN = "ISSN";
        public static final String ERESOURCE_ISBN_ISSN_VALUE = "ISBN/ISSN";
        public static final String ERESOURCE_OCLC = "oclc";
        public static final String ERESOURCE_PUBLISHER = "publisher";
        public static final String ERESOURCE_PLATFORM_PROVIDER = "platformProvider";
        public static final String ERESOURCE_IDENTIFIER = "oleERSIdentifier";
        public static final String ERESOURCE_NAME = "title";
        public static final String GOKB_IDENTIFIER = "gokbIdentifier";
        public static final String ERESOURCE_LICENSE_REQ_STATUS = "licenseReqStatus";
        public static final String ERESOURCE_PO_ITEM_ID = "olePOItemId";
        public static final String ERESOURCE_SUB_WORKFLOW_STATUS = "Sub-Workflow Status";
        public static final String ERESOURCE_PO_ID = "poItemId";
        public static final String ERESOURCE_INVOICE_NO = "invoiceNumber";
        public static final String ERESOURCE_PO_NUMBER = "PO Number";
        public static final String ERESOURCE_INVOICE_NUMBER = "Invoice Number";
        public static final String ERESOURCE_DOC_NUMBER = "eResourceDocNumber";
        public static final String ERESOURCE_INSTANCE_ID = "instanceId";
        public static final String ERESOURCE_DOC_NUMB = "documentNumber";
        public static final String ERESOURCE_DOC_ID = "DocId";
        public static final String ERESOURCE_STATUS_DATE = "statusDate";
        public static final String AND = "AND";
        public static final String ON = "on";
        public static final String OFF = "off";
        public static final String OR = "OR";
        public static final String NOT = "NOT";
        public static final String CREATED_DATE_FORMAT = "MM/dd/yyyy";
        public static final String ERESOURSE_SEARCH_SERVICE = "oleEResourceSearchService";
        public static final String STATUS_ID = "statusId";
        public static final String ERESOURCE_RECORD_NAME = "E-Resource Record Name";
        public static final String ERESOURCE_OCLC_VALUE = "OCLC Number";
        public static final String ERESOURCE_PUBLISHER_VALUE = "Publisher";
        public static final String ERESOURCE_PLATFORM_PROV_NAME = "Platform Provider Name";
        public static final String ERESOURCE_RECORD_ID = "E-Resource Record Id";
        public static final String PO_ITEM_ID = "itemIdentifier";
        public static final String INV_PO_ITEM_ID = "poItemIdentifier";
        public static final String PO_ID = "purchaseOrderId";
        public static final String CANCELED = "Canceled";
        public static final String STATUS_NAME = "oleEResourceStatusName";
        public static final String E_RES_INSTANCE_TAB = "OLEEResourceRecordView-E-ResourceInstanceTab";
        public static final String LINK_EXIST_INSTANCE = "linkExistingInstance";
        public static final String CREATE_NEW_INSTANCE = "createNewInstance";
        public static final String VENDOR_HEADER_GEN_ID = "vendorHeaderGeneratedIdentifier";
        public static final String VENDOR_DETAILED_ASSIGNED_ID = "vendorDetailAssignedIdentifier";
        public static final String ACCESS_LOCATION_SEPARATOR = ",";
        public static final String COMMA_SEPARATOR = "COMMA_SEPARATOR";
        public static final String COVERAGE_DATE_SEPARATOR = "COVERAGE_DATE_SEPARATOR";
        public static final String PERPETUAL_ACCESS_DATE_SEPARATOR = "PERPETUAL_ACCESS_DATE_SEPARATOR";
        public static final String DEFAULT_DATE_VOL = "Volume ";
        public static final String DEFAULT_DATE_ISSUE = "Issue ";
        public static final String SPACE = " ";
        public static final String SHOULD_NOT_BLANK = "error.shouldNotBlank";
        public static final String REQUESTOR_SECTION_ID = "OLEEResourceMainTab-RequestorSection";
        public static final String SELECTOR_SECTION_ID = "OLEEResourceMainTab-SelectorSection";
        public static final String REQUESTOR_SELECTOR_COMMENT_SECTION_ID = "OLEEResourceMainTab-RequestorSelectorCommentSection";
        public static final String VARIANT_TITLE_SECTION_ID = "OLEEResourceMainTab-VariantTitle";
        public static final String STATISTICAL_SEARCH_CD_ID = "statisticalSearchingCodeId";
        public static final String CALENDAR_OR_YEAR_AGO = "CALENDAR_OR_YEAR_AGO";
        public static final String MASK_PASSWORD = "MASK_PASSWORD";
        public static final String CALENDAR_OR_YEARS_AGO = "CALENDAR_OR_YEARS_AGO";
        public static final String MONTH_AGO = "MONTH_AGO";
        public static final String MONTHS_AGO = "MONTHS_AGO";
        public static final String WEEK_AGO = "WEEK_AGO";
        public static final String WEEKS_AGO = "WEEKS_AGO";
        public static final String DAY_AGO = "DAY_AGO";
        public static final String DAYS_AGO = "DAYS_AGO";
        public static final String FIRST_DAY_OF_YEAR = "FIRST_DAY_OF_YEAR";
        public static final String LAST_DAY_OF_YEAR = "LAST_DAY_OF_YEAR";
        public static final String DATE_FORMAT = "MM/dd/yyyy";
        public static final String COV_START_DATE_FORMAT_INV = "Coverage start date format is invalid";
        public static final String COV_END_DATE_FORMAT_INV = "Coverage end date format is invalid";
        public static final String PER_ACC_START_DATE_FORMAT_INV = "Perpetual Access start date format is invalid";
        public static final String PER_ACC_END_DATE_FORMAT_INV = "Perpetual Access end date format is invalid";
        public static final String ONE = "1";
        public static final String DATE_FORMAT_REGEX = "(^(10|12|0?[13578])([/])(3[01]|[12][0-9]|0?[1-9])([/])((1[8-9]" +
                "\\d{2})|([2-9]\\d{3}))$)|(^(11|0?[469])([/])(30|[12][0-9]|0?[1-9])([/])((1[8-9]\\d{2})|([2-9]\\d{3}))$)" +
                "|(^(0?2)([/])(2[0-8]|1[0-9]|0?[1-9])([/])((1[8-9]\\d{2})|([2-9]\\d{3}))$)" +
                "|(^(0?2)([/])(29)([/])([2468][048]00)$)|(^(0?2)([/])(29)([/])([3579][26]00)$)" +
                "|(^(0?2)([/])(29)([/])([1][89][0][48])$)|(^(0?2)([/])(29)([/])([2-9][0-9][0][48])$)" +
                "|(^(0?2)([/])(29)([/])([1][89][2468][048])$)|(^(0?2)([/])(29)([/])([2-9][0-9][2468][048])$)" +
                "|(^(0?2)([/])(29)([/])([1][89][13579][26])$)|(^(0?2)([/])(29)([/])([2-9][0-9][13579][26])$)";
        public static final List<String> ERESOURCE_RESULT_FIELDS = getSearchEresourcefield();
        public static final String FUND_CODE = "fundCode";
        public static final String ERROR_FUND_CODE = "error.fund.code.doesnt.exist";
        public static final String ERROR_LINKED_RECORD = "error.linked.record";
        public static final String RECIPIENT_ID = "recipientId";
        public static final String ERROR_RECIPIENT_ID = "error.recipient.id.required";
        public static final String ERROR_PERCENTAGE_UNEQUAL_HUNDRED = "fund.code.percentage.unequal.hundred";
        public static final String ERROR_PERCENTAGE_LESS_THAN_HUNDRED = "fund.code.percentage.less.than.hundred";
        public static final String ERROR_PERCENTAGE_MORE_THAN_HUNDRED = "fund.code.percentage.more.than.hundred";
        public static final String ERROR_ATLEAST_ONE_ACCOUNTING_LINE = "error.atleast.one.accounting.line";
        public static final String ERROR_INVALID_ACCOUNT_NUM = "error.invalid.account.no";
        public static final String ERROR_INVALID_COMBINATION_ACCOUNT_NUM = "error.invalid.combination.account.no";
        public static final String ERROR_INVALID_SUB_ACCOUNT_NUM = "error.invalid.sub.account.no";
        public static final String ERROR_INVALID_COMBINATION_SUB_ACCOUNT_NUM = "error.invalid.combination.sub.account.no";
        public static final String ERROR_INVALID_OBJECT_CODE = "error.invalid.object.code";
        public static final String ERROR_INVALID_SUB_OBJECT_CODE = "error.invalid.sub.object.code";
        public static final String ERROR_INVALID_COMBINATION_SUB_OBJECT_CODE = "error.invalid.combination.sub.object.code";
        public static final String ERROR_INVALID_PROJECT_CODE = "error.invalid.project.code";
        public static final String ERROR_INVALID_CHART_CODE = "error.invalid.chart.code";
        public static final String ERROR_DUPLICATE_FUND_CODE = "error.duplicate.fund.code";
        public static final String ERROR_MANDATORY_FIELDS = "error.mandatory.fields";
        public static final String ERROR_INVALID_PERCENTAGE = "error.invalid.percentage";
        public static final String ERROR_INVALID_PRICE = "error.invalid.price";
        public static final String ERROR_INVALID_UNIVERSAL_FISCAL_YEAR = "error.invalid.fiscalYear";
        public static final String FUND_ACCOUNTING_LINE_MAINTENANCE_ACTION_LINK = "addAccountingLineValidator";
        public static final List<String> getSearchEresourcefield() {
            List<String> resultFields = new ArrayList<String>();
            resultFields.add(ERESOURCE_TITLE);
            resultFields.add(ERESOURCE_IDENTIFIER);
            resultFields.add(ERESOURCE_ISBN);
            resultFields.add(ERESOURCE_PUBLISHER);
            resultFields.add(ERESOURCE_PLATFORM_PROVIDER);
            resultFields.add(ERESOURCE_STATUS_DATE);
            resultFields.add(STATUS_ID);
            resultFields.add(ERESOURCE_DOC_NUMB);
            resultFields.add(GOKB_IDENTIFIER);
            return Collections.unmodifiableList(resultFields);
        }

        public static final List<String> getEresourceClosedStatuses() {
            List<String> documentStatuses = new ArrayList<String>();
            documentStatuses.add("Cancelled");
            documentStatuses.add("Final");
            documentStatuses.add("Exception");
            documentStatuses.add("Processed");
            documentStatuses.add("Negotiation Failed");
            documentStatuses.add("Complete");
            return Collections.unmodifiableList(documentStatuses);
        }

        public static final List<String> ERESOURCE_STATUSES = getEresourceClosedStatuses();
        public static final String LICENSE_FINAL_STATUS = "Complete";
        public static final String BLANK_SELECTED_INDEX = "Selected collection was not set for delete line action, cannot delete line";
        public static final String INSTANCE_ID_REMOVE_NOTE = " (Holdings Id :";
        public static final String REMOVE_NOTE = ") was removed";
        public static final String SYSTEM = "System";
        public static final String STATUS_IS = "status is ";
        public static final String STATUS_FROM = "status changed from ";
        public static final String STATUS_TO = " to ";
        public static final String OLE_ERS_DOC = "OLE_ERS_DOC";

        public static final String ERESOURCE_EMAIL_APPROVAL_TEXT = "Approval is required for the price increase to the ";
        public static final String ERESOURCE_EMAIL_LAST_YEAR_COST = ". Last year cost was $";
        public static final String ERESOURCE_EMAIL_THIS_YEAR_QUOTE = ". This years price quote is $";
        public static final String ERESOURCE_EMAIL_PRICE_INCREASE = ". This is a price increase of $";
        public static final String ERESOURCE_EMAIL_CONFIRM = ". Please confirm if you would like to renew this E-resource";
        public static final String ERESOURCE_EMAIL_OR = " or ";
        public static final String ERESOURCE_EMAIL_PERCENT = "%";
        public static final String ONE_PO_PER_TITLE = "One PO Per Title";
        public static final String ONE_PO_WITH_ALL_TITLES = "One PO With All Titles";
        public static final String ERESOURCE_CAN_NOT_BE_DELETED = "error.eResource.not.deleted";
        public static final String ERESOURCE_HAS_BEEN_DELETED ="eResource.has.been.deleted";

        public static final String QUANTITY = "QUANTITY";
        public static final String NO_OF_PARTS = "NO_OF_PARTS";
        public static final String LOCATION = "LOCATION";
        public static final String COST_SOURCE = "COST_SOURCE";
        public static final String METHOD_OF_PO_TRANSMISSION = "METHOD_OF_PO_TRANSMISSION";
        public static final String BUILDING_CODE = "BUILDING_CODE";
        public static final String DELIVERY_CAMPUS_CODE = "DELIVERY_CAMPUS_CODE";
        public static final String BUILDING_ROOM_NUMBER = "BUILDING_ROOM_NUMBER";
        public static final String ORG_CODE = "ORG_CODE";
        public static final String CHART_CODE = "CHART_CODE";
        public static final String FUNDING_SOURCE = "FUNDING_SOURCE";
        public static final String REQ_DESC = "REQ_DESC";
        public static final String PURPOSE_TYPE = "PURPOSE_TYPE";
        public static final String ERROR_FILL_MANDATORY_FIELDS = "error.fill.mandatory.fields";
        public static final String ERROR_INVALID_FUND_CODE = "error.invali.fund.code";
        public static final String ERROR_INVALID_WORKFLOW="error.invalid.workflow";
        public static final String ID_FOR_LOG_TYPE_SYSTEM="3";

    }

    public static final class OleHoldings {
        public static final String ERROR_MSG_COV_START_DATE = "error.eholdings.cov.start.date.message";
        public static final String ERROR_MSG_COV_END_DATE = "error.eholdings.cov.end.date.message";
        public static final String ERROR_MSG_PER_ACC_START_DATE = "error.eholdings.per.acc.start.date.message";
        public static final String ERROR_MSG_PER_ACC_END_DATE = "error.eholdings.per.acc.end.date.message";
        public static final String ELECTRONIC = "electronic";
        public static final String ACTIVE = "active";
    }

    public static final String BIB_SELECT = "error.select.bib";
    public static final String LINK_SUCCESS_MESSAGE = "Selected bib is linked successfully";
    public static final String BIB_ERROR_MESSAGE = "Select a Bib record";
    public static final String HOLDINGS_ERROR_MESSAGE = "Select a Holdings record";
    public static final String EHOLDINGS_ERROR_MESSAGE = "Select a EHoldings record";
    public static final String POPUP_MESSAGE = "Selected bib record already linked to requisition(s) : ";
    public static final String PROCEED_MESSAGE = " Do you want to continue or cancel";
    public static final String OLE_EXPOSED_WEB_SERVICE = "oleExposedWebService";
    public static final String OLE_EXPOSED_WEB_SERVICE_url = "oleExposedWebService.url";
    public static final String CIRC_DESK_ATTENDANT_ONE = "Circ Desk Attendant I";
    public static final String CIRC_DESK_ATTENDANT_TWO = "Circ Desk Attendant II";

    public static final String VIEWBILL = "viewPatronBillLink";
    public static final String CREATEBILL = "createPatronBillLink";

    public static final String PATRON = "org.kuali.ole.deliver.bo.OlePatronDocument";

    public static final String SEARCH_BIB = "Search Bib";
    public static final String SEARCH_BIB_ERROR = "You are not authorized to search bib information";
    public static final String SEARCH_ITEM = "Search Item";
    public static final String SEARCH_ITEM_ERROR = "You are not authorized to search item information";
    public static final String NO_PATRON_INFO = "error.no.patron.information";
    public static final String ITEM_BARCODE_REQUIRED ="item.barcode.required";
    public static final String CIRCULATION_DESK_NOT_MAPPED_OPERATOR = "error.no.circulation.desk.mapped";
    public static final String SUCCESSFULLEY_LOANED = "success.loan.message";
    public static final String OPERATION_FAILED = "Operation Failed";
    public static final String ITEM_BARCODE_DOESNOT_EXISTS = "error.no.item.barcode.exist";
    public static final String SUCCESSFULLEY_CHECKED_IN = "success.checkin.message";
    public static final String CHECK_IN_FAILED = "error.checkin.failed";
    public static final String REQUEST_SUCCESSFULLEY_CANCELLED = "request.successfully.cancelled";
    public static final String NO_REQUEST_FOUND_REQUEST_ID = "error.no.request.id.found";
    public static final String NO_REQUEST_FOUND = "error.no.request";
    public static final String ITEM_NOT_CREATED = "item.not.created";
    public static final String ITEM_EXIST = "item.barcode.exist";
    public static final String INVALID_OPERATOR = "Operator Id received does not belong to an Operator";
    public static final String EXPORT_FILE_DIRECTORY = "/upload/export/";
    public static final String ITM_TYP_CODE = "ITEM";
    public static final String ITEM_CREATED = "Item Created Successfully";
    public static final String AGENCY_ID = "LEHI";
    public static final String HOME_LIBRARY = "LEHIGH";
    public static final String AUTH_BARCODE = "Barcode Id";
    public static final String AUTH_UID = "Username";
    public static final String PRIV_STAT_DESCRIPTION = "User Status";
    public static final String PRIV_TYPE_STATUS = "STATUS";
    public static final String PRIV_USER_PROFILE_DESCRIPTION = "LEHI";
    public static final String PRIV_TYPE_PROFILE = "User Profile";
    public static final String PRIV_LIB_DESCRIPTION = "PROFILE";
    public static final String PRIV_TYPE_LIBRARY = "User Library";
    public static final String PRIV_CAT_DESCRIPTION = "User Category One";
    public static final String PRIV_TYPE_CAT = "CAT1";
    public static final String daysUntilHoldExpires = "7";

    public static final String CHECKIN_CONVRTR_SRVC = "oleCheckInItemConverter";
    public static final String CHECKOUT_CONVRTR_SRVC = "oleCheckOutItemConverter";
    public static final String OLE_SERIALS_RECEIVING_NOT_AUTHORIZED = "You are not authorized to search records";
    public static final String OLE_SERIALS_RECEIVING_NO_RECORD = "No Records Found";

    public static final String CAT_NAMESPACE = "OLE-CAT";
    public static final String DESC_WORKBENCH_SEARCH = "DESC_WORKBENCH_SEARCH";
    public static final String SEARCH_AUTHORIZATION_ERROR = "Current user is not authorized to perform this action.";
    public static final String INSTANCE_EDITOR_DELETE_ITEM = "INSTANCE_EDITOR_DELETE_ITEM";
    public static final String INSTANCE_EDITOR_DELETE_INSTANCE = "INSTANCE_EDITOR_DELETE_INSTANCE";
    public static final String INSTANCE_EDITOR_ADD_ITEM = "INSTANCE_EDITOR_ADD_ITEM";
    public static final String MARC_EDITOR_ADD_BIB = "MARC_EDITOR_ADD_BIB";
    public static final String MARC_EDITOR_EDIT_BIB = "MARC_EDITOR_EDIT_BIB";
    public static final String CALL_NUMBER_BROWSE = "CALL_NUMBER_BROWSE";
    public static final String TRANSFER_SEARCH = "TRANSFER_SEARCH";
    public static final String BOUND_WITH_SEARCH = "BOUNDWITH_SEARCH";
    public static final String BATCH_PROCESS_IMPORT = "BATCH_PROCESS_IMPORT";
    public static final String BATCH_PROCESS_EXPORT = "BATCH_PROCESS_EXPORT";
    public static final String ERROR_BIB_CREATE_AUTHORIZATION = "error.bib.create.authorization";
    public static final String ERROR_BIB_EDIT_AUTHORIZATION = "error.bib.edit.authorization";
    public static final String INGEST_BIB_LOCAL = "INGEST_BIB_LOCAL";
    public static final String INGEST_BIB_EXTERNAL = "INGEST_BIB_EXTERNAL";
    public static final String ERROR_INFIELD = "error.search.infield";
    public static final String ERROR_AUTHORIZATION = "error.permission.authorization";
    public static final String DESC_WORKBENCH_EXPORT_XML = "DESC_WORKBENCH_EXPORT_XML";
    public static final String TRANSFER_HOLDING_OR_ITEM = "TRANSFER_HOLDING_OR_ITEM";
    public static final String BOUND_WITH = "BOUND_WITH";
    public static final String ERROR_CREATE_INSTANCE = "error.create.instance";
    public static final String ERROR_CREATE_ITEM = "error.create.item";
    public static final String INSTANCE_EDITOR_ADD_INSTANCE = "INSTANCE_EDITOR_ADD_INSTANCE";
    public static final String INSTANCE_EDITOR_EDIT_INSTANCE = "INSTANCE_EDITOR_EDIT_INSTANCE";
    public static final String INSTANCE_EDITOR_EDIT_ITEM = "INSTANCE_EDITOR_EDIT_ITEM";
    public static final String ERROR_EDIT_INSTANCE = "error.edit.instance";
    public static final String ERROR_EDIT_INSTANCE_HOLDINGS_UPDATED = "error.edit.update.holdingsitem";
    public static final String ERROR_EDIT_ITEM = "error.edit.item";
    public static final String BATCH_PROCESS_DELETE = "BATCH_PROCESS_DELETE";
    public static final String DESCRIBE_SEARCH_MESSAGE = "error.results.not.found";
    public static final String DESCRIBE_ENTER_SEARCH_TEXT = "error.enter.search.field";
    public static final String DESCRIBE_SEARCH_FILE_NOT_FOUND = "error.search.file.not.found";
    //Added for SOAS DB Validation
    public static final String SOURCE_NOT_SELECTED = "error.source.not.selected";
    public static final String EXTERNAL_DATA_IMPORT_ERROR = "error.import.external.db";

    public static final String DOCTYPE_COMBINATION_ERROR = "error.doctype.combination";
    public static final String DOCTYPE_HOLDING_COMBINATION = "Bib-Holdings or Holdings-Item are allowed here";
    public static final String DOCTYPE_ITEM_COMBINATION = "Bib-Item or Holdings-Item are allowed here";
    public static final String DOCTYPE_BIB_COMBINATION = "Bib-Holdings or Bib-EHoldings are allowed here";
    public static final String TRANSFER_LEFT_TREE_SECTION = "TransferTreeSection1";
    public static final String TRANSFER_RIGHT_TREE_SECTION = "TransferTreeSection2";
    public static final String TRANSFER_FAIL_MESSAGE_ITEM_ATTACHED_OLE = "error.transfer.failure";

    // E-Instance
    public static final String E_HOLDINGS_DOC_TYPE = "eHoldings";
    public static final String E_INSTANCE = "eInstance";
    public static final String LINK_EXISTING_INSTANCE = "linkExistingInstance";
    public static final String E_RESOURCE_ID = "eResourceId";
    public static final String TOKEN_ID = "tokenId";
    public static final String LINK_TO_ORDER_OPTION = "linkToOrderOption";
    public static final String HOLDINGS_DOC_TYPE = "holdings";
    public static final String VENDOR_NAME = "vendorName";
    public static final String PUBLISHER_ID = "publisherId" ;
    public static final String VENDOR_HEADER_GENERATED_ID = "vendorHeaderGeneratedIdentifier";
    public static final String VENDOR_DETAILED_ASSIGNED_ID = "vendorDetailAssignedIdentifier";

    public static final String ASSIGN_REQUESTOR_INQUIRY = "inquiry.do?methodToCall=start&businessObjectClassName=org.kuali.ole.deliver.bo.OLEPatronEntityViewBo&patronId=";

    public static final String DESCRIBE_COMPONENT = "Describe";
    public static final String DELIVER_COMPONENT = "Deliver";
    public static final String DESCRIBE_EFFECTIVE_DATE = " 00:00:00";

    public static final String INSTANCE_EDITOR_DELETE_EINSTANCE = "INSTANCE_EDITOR_DELETE_EINSTANCE";
    public static final String EHOLDINGS_MESSAGE = "error.eholdings.message";
    public static final String WEN = "wen";
    public static final String SERACH_BY_REQUIRED = "error.serach.by.required";
    public static final String SERACH_CRITERIA_REQUIRED = "error.serach.criteria.required";

    public static final String LINK_EXISTING_BIB = "Link Existing Bib";

    public static final String LOAN_AUTHORIZATION_MESSAGE = "Current user does not have permission to checkout an item";

    public static final String PATRON_OVERDUE_DAY = "Patron has at least one item overdue for more than n days";
    public static final String PATRON_RECALLED_OVERDUE_DAY = "Patron has at least one recalled item overdue for more than n days";
    public static final String PUR_ORDER_IDENTIFIER = "purchaseOrderIdentifier";
    public static final String DONOR_CODE = "donorCode";
    public static final String DONOR_CODE_FIELD = "dataObject.donorCode";
    public static final String ERROR_DONOR_CODE = "error.duplicate.donorcode";
    public static final String DONOR_CD = "Donor Code";
    public static final String DONOR_CODE_DISPLAY = "DonorCode_display";
    public static final String DONOR_CODE_SEARCH = "DonorCode_search";
    public static final String ITEM_CHART_CODE_VALUE = "BL";

    public static final String CLAIM_NOTICE = "Claim_Notice";

    public static final String PURCHASE_ORDER_NUM = "Purchase Order #";

    public static final String INVALID_PHONE_NUMBER_FORMAT = "error.invalid.phoneNumber.format";

    public static final String MARC_EDITOR_COPY_BIB = "MARC_EDITOR_COPY_BIB";

    public static List<String> getProtocolList() {
        List<String> protocolsList = new ArrayList<String>();
        protocolsList.add("http://");
        protocolsList.add("https://");
        protocolsList.add("ftp://");
        protocolsList.add("mailto:");
        return protocolsList;
    }

    public static final String INVALID_OPRTR_ID = "error.invalid.operator";
    public static final String RENEW_SUCCESS = "renew.success.message";
    public static final String RQST_PNDNG = "error.request.pending";
    public static final String NO_RENEW = "error.not.authorized.to.renew";
    public static final String ITM_NT_LOAN = "error.item.not.by.patron";
    public static final String RTRVD_SUCCESS = "success.retrieve.message";
    public static final String NO_LOAN = "error.no.loan";
    public static final String NO_FINE = "error.no.fine";
    public static final String NO_HOLD = "error.no.hold";
    public static final String INVALID_RQST_TYP = "error.invalid.request.type";
    public static final String INVALID_ITM_TYP = "error.invalid.item.type";
    public static final String INVALID_PK_UP_LOCN = "error.invalid.pickup.location";
    public static final String INVALID_PICKUP_LOCN = "error.invalid.pick.up.location";
    public static final String BARCODE = "barcode";
    public static final String PATRON_ID = "patronId";
    public static final String CIRCULATION_DESK_ID = "circulationDeskId";
    public static final String ITEM_LEVEL="Item Level";

    public static final String PTRN_RCRD_EXPRD = "Patron record expired";
    public static final String RQST_ALRDY_RAISD = "error.request.already.raised";
    public static final String ITM_NOT_LOAN = "error.item.not.eligible.loan";
    public static final String ITM_LOAN_BY_PTRN = "error.item.in.loan";
    public static final String RQST_CONDITION = " cannot be raised for an item with status :";
    public static final String RQST_SUCCESS = "Request raised succesfully";
    public static final String NTCE_PRBLM = " .Problem occured while sending notice.";
    public static final String NO_ITM = "Item barcode does not exist.";
    public static final String RQST_FAIL = "error.request.fail";
    public static final String RQST_CREATION_FAIL = "error.create.request";
    public static final String SUCCESS = "Success";
    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_AUTH = "mail.smtps.auth";
    public static final String SMTP_STARTTLS = "mail.smtp.starttls.enable";
    public static final String TRANSPORT_PROTOCOL = "mail.transport.protocol";
    public static final String RQST_PRBLM = " .Problem occured while retrieving item information";

    //Boundwith
    public static final String LEFT_TREE_SECTION = "LeftTreeSection";
    public static final String ERROR_BOUNDWITH_SELECT_BIBS = "error.boundwith.select.bibs";
    public static final String BOUNDWITH_SELECTION_SECTION = "BoundwithSelectionSection";
    public static final String ERROR_BOUNDWITH_SELECT_BIBS_TREE2 = "error.boundwith.select.bibs.tree2";
    public static final String ERROR_BOUNDWITH_SELECT_BIBS_TREE1 = "error.boundwith.select.bibs.tree1";

    //Analytics
    public static final String ANALYTICS_VIEW = "AnalyticsView";
    public static final String ANALYTICS_SUMMARY_VIEW = "AnalyticsSummaryView";
    public static final String ANALYTICS_SUMMARY_VIEW_PAGE = "AnalyticsSummaryViewPage";
    public static final String CREATE_ANALYTICS = "CREATE";
    public static final String BREAK_ANALYTICS = "BREAK";
    public static final String ANALYTICS_SELECTION_SECTION = "AnalyticsSelectionSection";
    public static final String TREE_ID = "treeId";
    public static final String LEFT_LIST = "LeftList";
    public static final String RIGHT_LIST = "RightList";
    public static final String LEFT_TREE = "leftTree";
    public static final String RIGHT_TREE = "rightTree";

    public static final String ERROR_SELECT_SERIES_AND_ANALYTICS = "error.select.series.analytics";
    public static final String ERROR_SELECT_ANALYTICS = "error.select.analytics";
    public static final String ERROR_SELECT_SERIES = "error.select.series";
    public static final String ERROR_SELECT_ONLY_HOLDINGS = "error.select.holdings.series";
    public static final String ERROR_SELECT_ONE_HOLDINGS = "error.select.one.holdings.series";
    public static final String ERROR_SELECT_ONLY_ITEMS = "error.select.item.analytics";
    public static final String ERROR_SELECT_ONE_ITEM = "error.select.one.item.analytics";
    public static final String ERROR_SELECT_ITEM_NOT_ANALYTIC = "error.analytics.select.item.not.analytic";
    public static final String ERROR_SELECT_ITEM_IN_ANALYTIC_RELATION = "error.analytics.select.item.analytic.relation";
    public static final String ERROR_SELECT_ONLY_BIBS = "error.analytics.select.bib";
    public static final String ERROR_SELECT_ONLY_ONE_BIB_SERIES = "error.analytics.select.one.bib.series";
    public static final String ERROR_SELECT_BIB_IS_SERIES = "error.analytics.select.analytics.bib.selected";
    public static final String ERROR_SELECT_BIB_IS_ANALYTIC = "error.analytics.select.series.bib.selected";
    public static final String ERROR_ANALYTICS_CREATE_FAILED = "error.analytics.create.failed";
    public static final String ERROR_ANALYTICS_BREAK_FAILED = "error.analytics.break.failed";
    public static final String INFO_ANALYTICS_CREATE_SUCCESS = "info.analytics.success";
    public static final String INFO_ANALYTICS_BREAK_SUCCESS = "info.analytics.break.success";


    public static final String CLAIM_MAIL_SUBJECT = "Claim Document";
    public static final String CLAIM_MAIL_MSSG_BODY = "Hi " + "\n\n" + " Kindly find the attached Claim Document.";

    public static final String CALL_NUMBER = "CallNumber_display";
    public static final String CALL_NUMBER_PREFIX = "CallNumberPrefix_display";
    public static final String CALL_NUMBER_TYPE_CODE = "ShelvingSchemeCode_display";
    public static final String CALL_NUMBER_TYPE_VALUE = "ShelvingSchemeValue_display";
    public static final String SHELVING_ORDER = "ShelvingOrder_display";
    public static final String SHELVING_ORDER_CODE = "ShelvingOrderCode_display";
    public static final String LOCATION_LEVEL = "Location_display";
    public static final String LOCATION_LEVEL_CODE = "LocationLevelName_display";
    public static final String HOLDINGS_NOTE = "HoldingsNote_display";
    public static final String URI = "Uri_display";
    public static final String RECEIPT_STATUS_CODE = "ReceiptStatus_display";
    public static final String COPY_NUMBER_LABEL = "CopyNumberLabel_display";
    public static final String COPY_NUMBER = "CopyNumber_display";
    public static final String EXTENTOFOWNERSHIP_NOTE_VALUE_DISPLAY = "ExtentOfOwnership_Note_Value_display";
    public static final String EXTENTOFOWNERSHIP_NOTE_TYPE_DISPLAY = "ExtentOfOwnership_Note_Type_display";
    public static final String EXTENTOFOWNERSHIP_Type_display = "ExtentOfOwnership_Type_display";
    public static final String EXTENTOFOWNERSHIP_ALREADY_EXISTS = "error.extentOfOwnership.already.exists";

    public static final String URI_SEARCH = "Uri_search";
    public static final String URI_DISPLAY = "Uri_display";

    public static final String EHOLDING_ACCESS_STATUS = "AccessStatus_display";
    public static final String EHOLDING_PLATFORM_DISPLAY = "Platform_display";
    public static final String EHOLDING_IMPRINT = "Imprint_display";
    public static final String EHOLDING_E_PUBLISHER = "E_Publisher_display";
    public static final String EHOLDING_STATISTICAL_CODE = "StatisticalSearchingCodeValue_display";
    public static final String EHOLDING_SUBSCRIPTION_STATUS = "Subscription_display";
    public static final String EHOLDING_LINK = "Url_display";
    public static final String EHOLDING_SIMULTANEOUS_USER = "NumberOfSimultaneousUses_display";
    public static final String EHOLDING_PERSIST_LINK = "Persist_Link_display";
    public static final String EHOLDING_ACCESS_LOCATION = "AccessLocation_display";
    public static final String EHOLDING_LINK_TEXT = "Link_Text_display";
    public static final String EHOLDING_ADMIN_USER = "Admin_UserName_display";
    public static final String EHOLDING_ADMIN_PWSD = "Admin_Password_display";
    public static final String EHOLDING_ACCESS_USR_NAME = "Access_UserName_display";
    public static final String EHOLDING_ACCESS_PSWD = "Access_Password_display";
    public static final String EHOLDING_ADMIN_URL = "Admin_url_display";
    public static final String EHOLDING_AUTHENTICATION_TYPE = "Authentication_display";
    public static final String EHOLDING_PROXIED = "Proxied_display";
    public static final String EHOLDING_ILL_IND = "ILL_display";
    public static final String EHOLDING_COVERAGE = "CoverageDate_display";
    public static final String EHOLDING_PERPETUAL = "PerpetualAccess_display";
    public static final String EHOLDING_DONOR_PUBLIC_DISPLAY = "DonorPublic_display";
    public static final String EHOLDING_DONOR_NOTE = "DonorNote_display";

    public static final String ITEM_STATUS_CODE_VALUE = "ItemStatus_display";
    public static final String ITEM_BAR_CODE = "ItemBarcode_display";
    public static final String ITEM_URI = "ItemUri_display";
    public static final String PO_LINE_ITEM_IDENTIFIER = "PurchaseOrderLineItemIdentifier_display";
    public static final String VENDOR_LINE_ITEM_IDENTIFIER = "VendorLineItemIdentifier_display";
    public static final String BAR_CODE_ARSL = "BarcodeARSL_display";
    public static final String STATISTICAL_SEARCH_CODE = "StatisticalSearchingCodeValue_display";
    public static final String ITEM_TYPE_FULL_VALUE = "ItemTypeFullValue_display";
    public static final String ITEM_TYPE_CODE_VALUE = "ItemTypeCodeValue_display";
    public static final String VOLUME_NUMBER = "volumeNumber_display";
    public static final String VOLUME_NUMBER_LABEL = "volumeNumberLabel_display";
    public static final String ENUMARATION = "Enumeration_display";
    public static final String CHRONOLOGY = "Chronology_display";
    public static final String DONORPUBLIC_DISPLAY = "DonorPublic_display";
    public static final String DONORNOTE_DISPLAY = "DonorNote_display";
    public static final String PUBLIC_NOTE_DISPLAY = "Public_Note_display";
    public static final String HIGHDENSITYSTORAGE_ROW_DISPLAY = "HighDensityStorage_Row_display";
    public static final String HIGHDENSITYSTORAGE_SHELF_DISPLAY = "HighDensityStorage_Shelf_display";

    public static final String ITEMNOTE_VALUE_DISPLAY = "ItemNote_Value_display";
    public static final String ITEMNOTE_TYPE_DISPLAY = "ItemNote_Type_display";
    public static final String ITEMBARCODE_DISPLAY = "ItemBarcode_display";
    public static final String ITEMBARCODE_SEARCH = "ItemBarcode_search";

    public static final String TEMPITEMTYPE_DISPLAY = "TempItemType_display";
    public static final String DONORCODE_DISPLAY = "DonorCode_display";
    public static final String FORMERIDENTIFIER_DISPLAY = "FormerIdentifier_display";
    public static final String FASTADD_DISPLAY = "FastAdd_display";
    public static final String PIECES_DISPLAY = "Pieces_display";
    public static final String ITEMSTATUSDATE_DISPLAY = "ItemStatusDate_display";
    public static final String CHECKINNOTE_DISPLAY = "CheckinNote_display";
    public static final String DUEDATETIME_DISPLAY = "DueDateTime_display";
    public static final String CLAIMSRETURNFLAG_DISPLAY = "ClaimsReturnFlag_display";
    public static final String MISSINGPIECEFLAG_DISPLAY = "MissingPieceFlag_display";
    public static final String ITEMDAMAGEDSTATUS_DISPLAY = "ItemDamagedStatus_display";


    public static final String GLOBAL_EDIT_ADD_RECORDS_MESSAGE = "info.globalEdit.edit.selected.records";
    public static final String GLOBAL_EDIT_SELECTED_RECORDS_ADD_MESSAGE = "info.globalEdit.edit.selected.message";
    public static final String GLOBAL_EDIT_VIEW_RECORDS_MESSAGE = "info.globalEdit.view.selected.records";
    public static final String GLOBAL_EDIT_HOLDINGS_HEADER_MESSAGE = "Global Holdings Editor  - OLEML Format";
    public static final String GLOBAL_EDIT_ITEM_HEADER_MESSAGE = "Global Item Editor - OLEML Format";
    public static final String GLOBAL_EDIT_EHOLDINGS_HEADER_MESSAGE = "Global EHoldings Editor  - OLEML Format";


    public static final String NO_RCVNG_RCRD_TYP = "Receiving Record Type is not available";
    public static final String NO_SRL_RCPT_LOCN = "Serial Receipt Location is not available";
    public static final String INVLD_SUBS_STAT = "Invalid Subscription Status";
    public static final String INVLD_PO = "No matching purchase order found for the given combination";
    public static final String INVLD_BIB_INS = "Holdings is not linked to the corresponding bib";
    public static final String LINK_DOC_FAILURE = "Linked Serial Receiving Document failed ";
    public static final String LINK_DOC_MISS = "Linked Serial Receiving Document is missing in the current uploaded files";
    public static final String SERIAL_DOC_ALRDY_EXIST = "Serial Receiving Record for the corresponding instance and bib already exist";

    public static final String RULE_EVALUATED = "Rule Evaluated";
    public static final String RENEWAL_LIMIT = "RenewalLimit";
    public static final String DOC_TYPES_SAVED = "info.doc.type.saved";
    public static final String DOC_FORMAT_SAVED = "info.doc.format.saved";
    public static final String DOC_FIELDS_SAVED = "info.doc.fields.saved";
    public static final String DOC_SEARCH_RESULT_SAVED = "info.doc.results.saved";
    public static final String DOC_SEARCH_RESULT_DUPLICATE = "info.doc.results.duplicate";
    public static final String DOC_FACET_PAGE_SAVED = "info.doc.facet.saved";

    public static final String DESCRIBE_GLOBAL_SEARCH_MESSAGE = "error.global.results.not.found";


    public static final String GLOBAL_EDIT_PERMISSION = "Global Edit of Holdings/Items/EHoldings";
    public static final String ANALYTICS_PERMISSION = "Create Analytics";
    public static final String SERIAL_RCVNG_DOC_TYP = "OLE_SER_RECV_REC";

    public static final String INVALID_RECORD_TYPE = "Invalid Record Type";
    public static final String LINKED_TYP_FAILED = "Linked Serial Receiving Type Record Failed";
    public static final String LINKED_HSTRY_FAILED = "Linked Serial Receiving History Record Failed";

    public static final String RECORD_TYP_MAIN = "Main";
    public static final String RECORD_TYP_SUPPLEMENTARY = "Supplement";
    public static final String RECORD_TYP_INDEX = "Index";
    public static final String SERIAL_LOADER_DELIMITER = "SERIAL_RECEIVING_LOADER_DELIMETER";
    public static final String YES = "YES";
    public static final String NO = "NO";
    public static final String DELIVERY = "Delivery";
    public static final String PAGING = "Paging";
    public static final String PROFILE = "Profile";
    public static final String BLOCKED = "BLOCKED";
    public static final String LOCATION_CODE = "locationCode";
    public static final String AVAILABLE = "AVAILABLE";
    public static final String LCC = "LCC";
    public static final String DUEDATE = "DUEDATE";
    public static final String CHECKINDATE = "CHECKINDATE";
    public static final String EXCLUDE_TIME = "EXCLUDE_TIME";
    public static final String NONCIRC = "NONCIRC";

    public static final String DELIVERY_DESCRIPTION = "Eligible to receive item";
    public static final String PAGING_DESCRIPTION = "Eligible to Page item";
    public static final String PROFILE_DESCRIPTION = "User Profile";
    public static final String COURTESY_DESCRIPTION = "Eligible to receive the courtesy Notices";
    public static final String STATUS_DESCRIPTION = "Allowed to Check out an item or not";
    public static final String OK = "OK";
    public static final String ITEM_IDENTIFIER = "itemIdentifier";
    public static final String LOAN_ID = "loanId";

    public static final class OleSerialReceivingDocumentLoader {
        public static final String FDOC_NBR = "FDOC_NBR";
        public static final String SER_RCV_REC_ID = "SER_RCV_REC_ID";
        public static final String BIB_ID = "BIB_ID";
        public static final String BOUND_LOC = "BOUND_LOC";
        public static final String CALL_NUM = "CALL_NUM";
        public static final String RCV_REC_TYP = "RCV_REC_TYP";
        public static final String CLAIM = "CLAIM";
        public static final String CLAIM_INTRVL_INFO = "CLAIM_INTRVL_INFO";
        public static final String COPY_NUM = "COPY_NUM";
        public static final String CORPORATE_AUTH = "CORPORATE_AUTH";
        public static final String CREATE_ITEM = "CREATE_ITEM";
        public static final String GEN_RCV_NOTE = "GEN_RCV_NOTE";
        public static final String INSTANCE_ID = "INSTANCE_ID";
        public static final String ISSN = "ISSN";
        public static final String PO_ID = "PO_ID";
        public static final String PRINT_LBL = "PRINT_LBL";
        public static final String PUBLIC_DISPLAY = "PUBLIC_DISPLAY";
        public static final String PUBLISHER = "PUBLISHER";
        public static final String SER_RCPT_LOC = "SER_RCPT_LOC";
        public static final String SER_RCV_REC = "SER_RCV_REC";
        public static final String SUBSCR_STAT = "SUBSCR_STAT";
        public static final String TREATMENT_INSTR_NOTE = "TREATMENT_INSTR_NOTE";
        public static final String UNBOUND_LOC = "UNBOUND_LOC";
        public static final String URGENT_NOTE = "URGENT_NOTE";
        public static final String VENDOR = "VENDOR";
        public static final String CREATE_DATE = "CREATE_DATE";
        public static final String OPTR_ID = "OPTR_ID";
        public static final String MACH_ID = "MACH_ID";
        public static final String SUBSCR_STAT_DT = "SUBSCR_STAT_DT";
        public static final String SR_TITLE = "SR_TITLE";
        public static final String OBJ_ID = "OBJ_ID";
        public static final String VER_NBR = "VER_NBR";
        public static final String ACTIVE = "ACTIVE";

    }
    public static final class EDIBatchProfile {
        public static final String DEFAULT_EDI_FORMAT="Default_EDI_Format";
        public static final String LINE_ITEM = "Line Item";
        public static final String PURAP_DOCUMENT_IDENTIFIER = "Purap Document Identifier";
        public static final String UNIQUE_REF_NUMBER = "Unique Ref Number";
        public static final String DATE_TIME_FORMAT_YYMMDDHHMM = "yyMMdd:HHmm";
        public static final String INTERCHANGE_DETAILS = "Interchange Details";
        public static final String DEFAULT_SEGMENT = "Default Segment";
        public static final String ORDER_DATE = "Order Date";
        public static final String VENDOR_USERNAME = "Vendor Username";
        public static final String VENDOR_SAN = "Vendor SAN";
        public static final String CURRENCY_CODE = "Currency Code";
        public static final String PURCHASE_LINE_ITEM = "Purchase Line Item";
        public static final String ITEM_PRICE = "Item Price";
        public static final String ITEM_PUBLISHER = "Item Publisher";
        public static final String ITEM_PUBLISHER_DATE = "Item Publisher Date";
        public static final String ITEM_QUANTITY = "Item Quantity";
        public static final String REFERENCE_QUALIFIER_1 ="Reference Qualifier 1";
        public static final String REFERENCE_QUALIFIER_2 = "Reference Qualifier 2";
        public static final String ITEM_LOCATION = "Item Location";
        public static final String DELIVERY_ADDRESS = "Delivery Address";
        public static final String OTHER_ADDRESS = "Other Address";
        public static final String SECTION_IDENTIFICATION = "Section Identification";
        public static final String CONTROL_QUALIFIER_1 = "Control Qualifier 1";
        public static final String CONTROL_QUALIFIER_2 = "Control Qualifier 2";
        public static final String NUMBER_OF_SEGMENTS = "Number of Segments";
        public static final String INTERCHANGE_CONTROL_COUNT = "Interchange Control count";
        public static final String NUM_0001 = "0001";
        public static final String SYMBOL_ASTERISK = "*";
        public static final String DOC_LOOKUP_SERVICE = "docLookupService";
    }
    public static final class OleSerialReceivingTypeLoader {
        public static final String SER_RCPT_HIS_REC_ID = "SER_RCPT_HIS_REC_ID";
        public static final String SER_RCV_REC_ID = "SER_RCV_REC_ID";
        public static final String RCV_REC_TYP = "RCV_REC_TYP";
        public static final String CHRON_LVL_1 = "CHRON_LVL_1";
        public static final String CHRON_LVL_2 = "CHRON_LVL_2";
        public static final String CHRON_LVL_3 = "CHRON_LVL_3";
        public static final String CHRON_LVL_4 = "CHRON_LVL_4";
        public static final String CLAIM_COUNT = "CLAIM_COUNT";
        public static final String CLAIM_DATE = "CLAIM_DATE";
        public static final String CLAIM_NOTE = "CLAIM_NOTE";
        public static final String CLAIM_TYPE = "CLAIM_TYPE";
        public static final String CLAIM_RESP = "CLAIM_RESP";
        public static final String ENUM_LVL_1 = "ENUM_LVL_1";
        public static final String ENUM_LVL_2 = "ENUM_LVL_2";
        public static final String ENUM_LVL_3 = "ENUM_LVL_3";
        public static final String ENUM_LVL_4 = "ENUM_LVL_4";
        public static final String ENUM_LVL_5 = "ENUM_LVL_5";
        public static final String ENUM_LVL_6 = "ENUM_LVL_6";
        public static final String PUB_DISPLAY = "PUB_DISPLAY";
        public static final String SER_RCPT_NOTE = "SER_RCPT_NOTE";
        public static final String OPTR_ID = "OPTR_ID";
        public static final String MACH_ID = "MACH_ID";
        public static final String RCPT_STAT = "RCPT_STAT";
        public static final String RCPT_DATE = "RCPT_DATE";
        public static final String PUB_RCPT = "PUB_RCPT";
        public static final String STAFF_ONLY_RCPT = "STAFF_ONLY_RCPT";
        public static final String OBJ_ID = "OBJ_ID";
        public static final String VER_NBR = "VER_NBR";
    }

    public static final class OleSerialReceivingHistoryLoader {
        public static final String SER_RCV_REC_TYP_ID = "SER_RCV_REC_TYP_ID";
        public static final String SER_RCV_REC_ID = "SER_RCV_REC_ID";
        public static final String RCV_REC_TYP = "RCV_REC_TYP";
        public static final String ACTN_DATE = "ACTN_DATE";
        public static final String ACTN_INTRVL = "ACTN_INTRVL";
        public static final String CHRON_CAPTN_LVL1 = "CHRON_CAPTN_LVL1";
        public static final String CHRON_CAPTN_LVL2 = "CHRON_CAPTN_LVL2";
        public static final String CHRON_CAPTN_LVL3 = "CHRON_CAPTN_LVL3";
        public static final String CHRON_CAPTN_LVL4 = "CHRON_CAPTN_LVL4";
        public static final String ENUM_CAPTN_LVL1 = "ENUM_CAPTN_LVL1";
        public static final String ENUM_CAPTN_LVL2 = "ENUM_CAPTN_LVL2";
        public static final String ENUM_CAPTN_LVL3 = "ENUM_CAPTN_LVL3";
        public static final String ENUM_CAPTN_LVL4 = "ENUM_CAPTN_LVL4";
        public static final String ENUM_CAPTN_LVL5 = "ENUM_CAPTN_LVL5";
        public static final String ENUM_CAPTN_LVL6 = "ENUM_CAPTN_LVL6";
        public static final String OBJ_ID = "OBJ_ID";
        public static final String VER_NBR = "VER_NBR";
    }

    public static final class OLEPatronEntityViewBo {
        public static final String PATRON_ID = "olePatronEntityViewBo.patronId";
        public static final String BARCODE = "olePatronEntityViewBo.patronBarcode";
        public static final String PATRON_TYPE = "olePatronEntityViewBo.patronType";
        public static final String PATRON_TYPE_ID = "olePatronEntityViewBo.patronTypeId";
        public static final String PATRON_FIRST_NAME = "olePatronEntityViewBo.firstName";
        public static final String PATRON_LAST_NAME = "olePatronEntityViewBo.lastName";
        public static final String PATRON_MIDDLE_NAME = "olePatronEntityViewBo.middleName";
        public static final String PATRON_EMAIL_ADDRESS = "olePatronEntityViewBo.emailAddress";
        public static final String PATRON_PHONE_NUMBER = "olePatronEntityViewBo.phoneNumber";
        public static final String PATRON_ACTIVE_IND = "olePatronEntityViewBo.active";
        public static final String DATE_FORMAT = "yyyy-MM-dd";
    }
    public static final class OleAddressSourceBo {
        public static final String ADDRESS_SOURCE_CODE_FIELD = "dataObject.oleAddressSourceCode";
        public static final String ADDRESS_SOURCE_CODE = "oleAddressSourceCode";
        public static final String ERROR_ADDRESS_SOURCE_CODE = "error.duplicate.address.source.code";
    }
    public static final class OleFeeType {
        public static final String FEE_TYPE_CODE_FIELD = "dataObject.feeTypeCode";
        public static final String FEE_TYPE_CODE = "feeTypeCode";
        public static final String FEE_TYPE_ID = "feeTypeId";
        public static final String ERROR_FEE_TYPE_CODE = "error.duplicate.fee.type.code";
    }
    public static final class OleSourceBo {
        public static final String SOURCE_CODE_FIELD = "dataObject.oleSourceCode";
        public static final String SOURCE_CODE = "oleSourceCode";
        public static final String ERROR_SOURCE_CODE = "error.duplicate.source.code";
    }
    public static final class OleCalendarExceptionType {
        public static final String EXCEPTION_TYPE_CODE_FIELD = "dataObject.exceptionTypeCode";
        public static final String EXCEPTION_TYPE_CODE = "exceptionTypeCode";
        public static final String ERROR_EXCEPTION_TYPE_CODE = "error.duplicate.exception.type.code";
    }
    public static final class OleCalendarGroup {
        public static final String CALENDER_GROUP_CODE_FIELD = "dataObject.calendarGroupCode";
        public static final String CALENDER_GROUP_CODE = "calendarGroupCode";
        public static final String ERROR_CALENDER_GROUP_CODE = "error.duplicate.calendar.group.code";
    }
    public static final class OlePaymentStatus {
        public static final String PAYMENT_STATUS_CODE_FIELD = "dataObject.paymentStatusCode";
        public static final String PAYMENT_STATUS_CODE = "paymentStatusCode";
        public static final String ERROR_PAYMENT_STATUS_CODE = "error.duplicate.payment.status.code";
    }
    public static final class OleStatisticalCategoryBo {
        public static final String STATISTICAL_CATEGORY_CODE_FIELD = "dataObject.oleStatisticalCategoryCode";
        public static final String STATISTICAL_CATEGORY_CODE = "oleStatisticalCategoryCode";
        public static final String ERROR_STATISTICAL_CATEGORY_CODE = "error.duplicate.statistical.category.code";
    }
    public static final class BarcodeStatus {
        public static final String BARCODE_STATUS_CODE_FIELD = "dataObject.barcodeStatusCode";
        public static final String BARCODE_STATUS_CODE = "barcodeStatusCode";
        public static final String ERROR_BARCODE_STATUS_CODE = "error.duplicate.barcode.status.code";
    }

    public static final class OlePurchaseOrderPurpose {
        public static final String PURCHASE_ORDER_PURPOSE_CODE_FIELD = "dataObject.purchaseOrderPurposeCode";
        public static final String PURCHASE_ORDER_PURPOSE_CODE = "purchaseOrderPurposeCode";
        public static final String PURCHASE_ORDER_PURPOSE_ID = "purchaseOrderPurposeId";
        public static final String ERROR_PURCHASE_ORDER_PURPOSE_CODE = "error.duplicate.purchase.order.purpose.code";
    }

    //Order Import and Invoice Ingest Constants

    public static final String ONE_REQUISITION_PER_TITLE = "One Requisition Per Title";
    public static final String ONE_REQUISITION_WITH_ALL_TITLES = "One Requisition With All Titles";
    public static final String IS_VALID_RECORD = "isValidRecord";
    public static final String IS_VALID_BFN = "isValidBFN";
    public static final String IS_BAD_CTRL_FLD = "isBadControlField";
    public static final String IS_APO_RULE = "isApoRule";
    public static final String PO_TYPE = "purchaseOrderType";
    public static final String ORDER_IMPORT_SUCCESS_COUNT = "orderImportSuccessCount";
    public static final String ORDER_IMPORT_FAILURE_COUNT = "orderImportFailureCount";
    public static final String NOTE_TYPE_CD = "BO";
    public static final String RQST_TYPE = "requestorType";
    public static final String REQUESTOR_FIRST_NM = "requestorFirstName";
    public static final String REQUESTOR_LAST_NM = "requestorLastName";
    public static final String NOTE_TYP = "noteType";
    public static final String DEFAULT_ORDER_TYPE_VALUE = "1";
    public static final String PRINT = "print";
    public static final String GL_DEBIT_CODE = "D";
    public static final String FIRST_NM = "firstName";
    public static final String LAST_NM = "lastName";
    public static final String PERCENTAGE = "%";
    public static final String DOCUMENT_DESCRIPTION = "YBP";
    public static final String UPDATE_BIB_CNT = "updateBibCount";
    public static final String CREATE_HLD_CNT = "createHoldingsCount";
    public static final String RULES_EVAL = "rulesEvaluated";
    public static final String FAILURE_REASON = "reasonForFailure";
    public static final String EDI_XML_CONTENT = "ediXMLContent";
    public static final String BATCH_PROFILE_NM = "batchProcessProfileName";
    public static final String AGENDA_NM = "agenda_name";
    public static final String INVALID_CHART_CD = "Invalid Chart Of Accounts Code -";
    public static final String INVALID_ORG_CD = "Invalid Org Code -";
    public static final String INVALID_CONTRACT_MANAGER_NM = "Invalid Contract Manager Name -";
    public static final String INVALID_ASSIGN_TO_USER = "Invalid Assign to user -";
    public static final String INVALID_ORDER_TYPE = "Invalid Order Type -";
    public static final String INVALID_FUNDING_SOURCE_CD = "Invalid Funding Source Code -";
    public static final String INVALID_CAMPUS_CODE = "Invalid Campus Code -";
    public static final String INVALID_BUILDING_CD = "Invalid Building Code -";
    public static final String INVALID_DELIVERY_BUILDING_ROOM_NUMBER = "Invalid Building Room Number -";
    public static final String INVALID_VENDOR_CHOICE_CD = "Invalid Vendor Choice Code -";
    public static final String INVALID_VENDOR_CUST_NBR = "Invalid Acquisition Unit's Vendor account / Vendor Info Customer # -";
    public static final String INVALID_ITEM_TYPE_CD = "Invalid Item Type Code -";
    public static final String INVALID_METHOD_OF_PO_TRANSMISSION_CD = "Invalid PO Transmission Method Code -";
    public static final String INVALID_RECURRING_PAYMENT_TYP_CD = "Invalid Recurring Payment Type Code -";
    public static final String INVALID_COST_SOURCE_CD = "Invalid Cost Source Code -";
    public static final String INVALID_PERCENT = "Invalid Percentage -";
    public static final String INVALID_RECURRING_BEGIN_DT = "Invalid Recurring Begin Date -";
    public static final String RECURRING_BEGIN_DT = "Recurring Begin Date -";
    public static final String INVALID_RECURRING_END_DT = "Invalid Recurring End Date -";
    public static final String RECURRING_END_DT = "Recurring End Date -";
    public static final String INVALID_LOCN_NM = "Invalid Location Name -";
    public static final String INVALID_LIST_PRICE = "Invalid List Price -";
    public static final String INVALID_INVOICED_PRICE = "Invalid Invoiced Price -";
    public static final String INVALID_FOREIGN_INVOICED_PRICE = "Invalid Foreign Invoiced Price -";
    public static final String INVALID_VENDOR_NUMBER = "Invalid Vendor Number -";
    public static final String INVALID_VENDOR_ALIAS_NAME = "Invalid Vendor Alias Name -";
    public static final String INVALID_QTY = "Invalid Quantity -";
    public static final String INVALID_NO_OF_PARTS = "Invalid No Of Parts -";
    public static final String INVALID_RECEIVING_REQUIRED = "Invalid Receiving Required -";
    public static final String INVALID_USE_TAX_INDICATOR = "Invalid Use Tax Indicator -";
    public static final String INVALID_PREQ_POSITIVE_APPROVAL_REQ = "Invalid PREQ Positive Approval Required -";
    public static final String INVALID_PO_CONFIRMATION_INDICATOR = "Invalid PO Confirmation Indicator -";
    public static final String INVALID_ROUTE_TO_REQUESTOR = "Invalid route to requestor -";
    public static final String INVALID_DONOR_CODE = "Invalid Donor Code -";
    public static final String INVALID_REQUESTOR_NAME = "Invalid Requestor Name -";
    public static final String INVALID_ITEM_STATUS = "Invalid Item Status -";
    public static final String INVALID_DISCOUNT = "Invalid Discount -";
    public static final String INVALID_DISCOUNT_TYPE = "Invalid Discount Type -";
    public static final String INVALID_ACCOUNT_NUMBER = "Invalid Account Number -";
    public static final String INVALID_OBJECT_CODE = "Invalid Object Code -";
    public static final String INVALID_ITEM_CHART_CD = "Invalid Item Chart Code -";
    public static final String INVALID_FUND_CD = "Invalid Fund Code -";
    public static final String INVALID_FORMAT = "Invalid Format -";
    public static final String INVALID_VENDOR_INV_AMT = "Invalid Vendor Invoice Amount -";
    public static final String INVALID_INV_NMBR = "Invalid Invoice Number -";
    public static final String INVALID_INV_DT = "Invalid Invoice Date -";
    public static final String INV_FAILURE_REASON = "invoiceIngestFailureReason";
    public static final String REQUIRED_LIST_PRICE = "List Price";
    public static final String REQUIRED_VENDOR_NUMBER = "Vendor Number";
    public static final String REQUIRED_VENDOR_ALIAS_NAME = "vendor Alias Name";
    public static final String REQUIRED_QTY = "Quantity";
    public static final String REQUIRED_NO_OF_PARTS = "No Of Parts";
    public static final String REQUIRED_ACCOUNT_NUMBER = "Account Number";
    public static final String REQUIRED_OBJECT_CODE = "Object Code";
    public static final String REQUIRED_ITEM_CHART_CD = "Item Chart Code";
    public static final String REQUIRED_ITEM_TYPE = "Item Type";
    public static final String REQUIRED_COST_SOURCE = "Cost Source";
    public static final String REQUIRED_METHOD_OF_PO_TRANSMISSION = "Method Of PO Transmission";
    public static final String REQUIRED_BUILDING_CODE = "Building Code";
    public static final String REQUIRED_DELIVERY_BUILDING_ROOM_NUMBER = "Building Room Number";
    public static final String REQUIRED_DELIVERY_CAMPUS_CODE = "Delivery Campus Code";
    public static final String REQUIRED_ORG_CODE = "Org Code";
    public static final String REQUIRED_CHART_CODE = "Chart Code";
    public static final String REQUIRED_FUNDING_SOURCE = "Funding Source";
    public static final String REQUIRED_DEFAULT_LOCATION = "Default Location";
    public static final String REQUIRED_INVOICE_NUMBER = "Invoice Number";
    public static final String REQUIRED_INVOICE_DATE = "Invoice Date";
    public static final String REQUIRED_INVOICED_PRICE = "Invoiced Price";
    public static final String REQUIRED_INVOICED_FOREIGN_PRICE = "Invoiced Foreign Price";
    public static final String REQUIRED_VENDOR_INVOICE_AMOUNT = "Invoice Amount";
    public static final String REQUIRED_ITEM_DESCRIPTION = "Item Description";
    public static final String REQUIRED_CONTRACT_MGR_NM = "Contract Manager Name";
    public static final String REQUIRED_ASSIGN_TO_USER = "Assign to user";
    public static final String REQUIRED_ORDER_TYPE = "Order Type";
    public static final String REQUIRED_BUILDING_CD = "Building Code";
    public static final String REQUIRED_VENDOR_CHOICE = "Vendor Choice";
    public static final String REQUIRED_VENDOR_CUST_NBR = "Acquisition Unit's Vendor account / Vendor Info Customer # ";
    public static final String REQUIRED_PERCENT = "Percentage";
    public static final String REQUIRED_LOCATION_NM = "Location";
    public static final String REQUIRED_VENDOR_REF_NMBR = "Vendor Reference Number";
    public static final String REQUIRED_VENDOR_ITEM_IDENTIFIER = "Vendor Item Identifier";
    public static final String REQUIRED_DONOR_CD = "Donor Code";
    public static final String REQUIRED_REQUESTOR_NM = "Requestor Name";
    public static final String REQUIRED_ITEM_STATUS = "Item Status";
    public static final String REQUIRED_DISCOUNT = "Discount";
    public static final String REQUIRED_DISCOUNT_TYPE = "Discount Type";
    public static final String REQUIRED_FORMAT = "Format";
    public static final String REQUIRED_FUND_CODE = "Fund Code";
    public static final String NULL_VALUE_MESSAGE = "-  cannot be null or empty";
    public static final String NB_PRINT = "NB_PRINT";
    public static final String NB_ELECTRONIC = "NB_ELECTRONIC";
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String ITEM_TYP = "ITEM";
    public static final String PURCHASE_ORDER_TYPE_ID = "purchaseOrderTypeId";
    public static final String FORMAT_TYPE_ID = "formatTypeId";
    public static final String NOT_PAID = "Not Paid";

    public static final String REQUESTOR_PATRON_ID = "patronId";
    public static final String SYSTEM = "System";
    public static final String DATEFORMAT = "MM/dd/yyyy";
    public static final String TIMESTAMP = "MM/dd/yyyy hh:mm a";
    public static final String CREATE_BIB = "CREATE";
    public static final String UPDATE_BIB = "UPDATE";

    public static final String TEMPITEMTYPE = "TemporaryItemTypeCodeValue_search";
    public static final String PUBLISHERDISPLAY = "Publisher_display";
    public static final String STAFF_ONLY_COLOR = "#FF0000";
    public static final String NON_STAFF_ONLY_COLOR = "#000000";
    public static final String STAFF_ONLY_STYLE = "italic";
    public static final String NON_STAFF_ONLY_STYLE = "normal";
    public static final String INVOICE_IMPORT_INV_DESC = "INVOICE_IMPORT_INV_DESC";
    public static final String PO_ID = "poId";
    public static final String SERIAL_RECEIVING_RECORD_ID = "serialReceivingRecordId";
    public static final String SERIAL_RECEIVING_RECORD = "serialReceivingRecord";
    public static final String RECEIPT_PRINTER = "ReceiptPrinter";
    public static final String NORMAL_PRINTER = "NormalPrinter";
    public static final String ALL="All";
    public static final String FIRST="First";
    public static final String REQUEST_RECAL_DELIVERY = "1";
    public static final String REQUEST_RECAL_HOLD = "2";
    public static final String PICKUP_LOCATION = "pickUpLocation";

    public static final String AND_SEARCH_SCOPE = "AND";
    public static final String OR_SEARCH_SCOPE = "OR";
    public static final String PO_NONE = "NONE";
    public static final String RANGE_QUERY = "[* TO *]";
    public static final String HUNDRED = "100";
    public static final String ITEM_IN_LOAN = "error.item.in.loan.patron";
    public static final String ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC = "ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC";
    public static final String ORDER_RECORD_IMPORT_MARC_ONLY_PRINT = "ORDER_RECORD_IMPORT_MARC_ONLY_PRINT";
    public static final String ORDER_RECORD_IMPORT_MARC_ONLY_PRINT_ELECTRONIC = "ORDER_RECORD_IMPORT_MARC_ONLY_PRINT_ELECTRONIC";
    public static final String ORDER_RECORD_IMPORT_MARC_EDI = "ORDER_RECORD_IMPORT_MARC_EDI";
    public static final String ORDER_RECORD_IMPORT_MARC_EDI_ELECTRONIC = "ORDER_RECORD_IMPORT_MARC_EDI_ELECTRONIC";
    public static final String ORDER_RECORD_IMPORT_MARC_EDI_PRINT_ELECTRONIC = "ORDER_RECORD_IMPORT_MARC_EDI_PRINT_ELECTRONIC";
    public static final String BIB_IMP_RESP = "bibImportResponse";
    public static final String BIB_DATA_ONLY = "Bibliographic Data Only";
    public static final String BIB_INS = "Bibliographic, Holdings, and Item Data";
    public static final String BIB_EINS = "Bibliographic and EHoldings Data";
    public static final String BIB_INS_EINS = "Bibliographic,Holdings,Item and EHoldings Data";
    public static final String ORDER_IMPORT_VALIDATION_MSG = "In bib import profile, Data To Import value should be either 'Bibliographic, Holdings, and Item Data' or 'Bibliographic,Holdings,Item and EHoldings' to process Order Record Import.";
    public static final String CURR_ALPHA_CD = "currencyAlphaCode";

    public static final String PREFIX_FOR_DATA_FIELD = "mdf_";
    public static final String BIB_IMPORT_FAILURE_REASON = "The record is not processed due to content of the record might have the following special characters &,#,\",',<,>.";

    // For checking links while deleting records
    public static final String LINK = "link";
    public static final String REQUISITION = "REQUISITION";
    public static final String PURCHASE_ORDER = "PURCHASE ORDER";
    public static final String PAYMENT_REQUEST = "PAYMENT REQUEST";
    public static final String LINE_ITEM = "LINE ITEM";
    public static final String COPY = "COPY";
    public static final String REQUEST = "REQUEST";
    public static final String LOAN = "LOAN";
    public static final String SERIAL_RECEIVING = "SERIAL RECEIVING";
    public static final String DELETE_VIEW_PAGE = "DeleteViewPage";
    public static final String RENEW_INFO_INDICATOR = "RENEW_INFORMATION_INDICATOR_FOR_VUFIND_SERVICES";
    public static final String NUMBER_OF_ITEM_INFO = "NUMBER_OF_ITEM_INFO_IN_SINGLE_SOLR_CALL";
    public static final String  ExchangeRateDateFrom = "exchangeRateDateFrom";
    public static final String ExchangeRateDateTo = "exchangeRateDateTo";
    public static final String ExchangeRateDate = "exchangeRateDate";


    public static final String PLATFORM_STATUS_NAM_FIELD = "dataObject.platformStatusName";
    public static final String PLATFORM_STATUS_NAME = "platformStatusName";
    public static final String PLATFORM_ADMIN_URL_TYPE_NAME = "platformAdminUrlTypeName";
    public static final String PLATFORM_ADMIN_URL_TYPE_NAME_FIELD = "dataObject.platformAdminUrlTypeName";
    public static final String PLATFORM_NAME = "name";
    public static final String PLATFORM_NAME_VALUE = "Name";
    public static final String PLATFORM_GOKB_ID = "GOKb ID";
    public static final String  ON_HOLD_NOTICE_REQUEST_TYPE = "ON_HOLD_NOTICE_REQUEST_TYPE";
    public static final String PLATFORM_OLE_ID = "OLE ID";
    public static final String GOKB_ID = "gokbId";
    public static final String OLE_PLATFORM_ID = "olePlatformId";
    public static final String PLATFORM_ID = "platformId";
    public static final String PLATFORM_PROVIDER_NAME = "platformProviderName";
    public static final String PLATFORM_PROVIDER_NAME_VALUE = "Platform Provider";
    public static final String STATUS_NAME = "statusName";
    public static final String PLATFORM_STATUS = "Status";
    public static final String NEW_PLATFORM = "Platform";
    public static final String   ON_HOLD_NOTICE_ITEM_STATUS = "ON_HOLD_NOTICE_ITEM_STATUS";
    public static final String   REENCUMBER_RECURRING_ORDERS = "REENCUMBER_RECURRING_ORDERS";
    public static final String   RECUR_PMT_TYP_CD = "RECUR_PMT_TYP_CD";
    public static final String   REENCUMBER_FILE_DIRECTORY = "/rollover";
    public static final String   REENCUMBER_FILE_PATH = "/ReEncumberRecuring";
	
    public static final String   MULTIPLE = "Multiple";
    public static final String PO ="PO";
    public static final String INVOICE ="Invoice";
    public static final String FROM_DATE ="REENCUMBER_RECURRING_FROM_DATE";
    public static final String TO_DATE ="REENCUMBER_RECURRING_TO_DATE";
    public static final String FILE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String GL_UNIV_FIS_YR = "UNIV_FISCAL_YR";
    public static final String GL_CHART_CD = "FIN_COA_CD";
    public static final String GL_ACCOUNT_NBR = "ACCOUNT_NBR";
    public static final String GL_SUB_ACCT_NBR = "SUB_ACCT_NBR";
    public static final String GL_OBJ_CD = "FIN_OBJECT_CD";
    public static final String GL_SUB_OBJ_CD = "FIN_SUB_OBJ_CD";
    public static final String GL_BAL_TYP_CD = "FIN_BALANCE_TYP_CD";
    public static final String GL_OBJ_TYP_CD = "FIN_OBJ_TYP_CD";
    public static final String GL_UNIV_FISC_PERIOD_CD = "UNIV_FISCAL_PRD_CD";
    public static final String GL_FIN_DOC_TYP_CD = "FDOC_TYP_CD";
    public static final String GL_FIN_SYS_ORG_CD = "FS_ORIGIN_CD";
    public static final String GL_DOC_NBR = "FDOC_NBR";
    public static final String TRANS_LED_SEQ_NO = "TRN_ENTR_SEQ_NBR";
    public static final String GL_TRANS_LED_ENTRY_DESC = "TRN_LDGR_ENTR_DESC";
    public static final String GL_TRANS_LED_ENTRY_AMT = "TRN_LDGR_ENTR_AMT";
    public static final String GL_TRANS_DEB_CRE_CD = "TRN_DEBIT_CRDT_CD";
    public static final String GL_TRANS_DT = "TRANSACTION_DT";
    public static final String GL_TOTAL_INV_AMT = "AMT";
    public static final String DOC_NBR = "FDOC_REF_NBR";
    public static final String EXC_RATE = "OLE_EXCHANGE_RT";
    public static final String PROJECT_CODE = "PROJECT_CD";
    public static final String REF_DOC_TYP_CD = "FDOC_REF_TYP_CD";
    public static final String REF_ORG_CD = "FS_REF_ORIGIN_CD";
    public static final String ENCUM_UPDT_CD = "R";
    public static final String FDOC_REF_NBR = "FDOC_REF_NBR";
    public static final String DATE_SERVICE = "dateTimeService";
    public static final String RUN_DATE_SERVICE = "runDateService";
    public static final String PARAMETER_SERVICE = "parameterService";

    public static final String REQUEST_FULFILLED = "message.request.fulfilled";
    public static final String REQUEST_CANCELLED = "message.request.cancelled";
    public static final String REQUEST_EXPIRED = "message.request.expired";
    public static final String HOLD_REQUEST_EXPIRED = "message.request.hold.expired";




    public static String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if(parameter==null){
            parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
            parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        }
        return parameter!=null?parameter.getValue():null;
    }

    public static String getParameterForSelectModule(String name){
        ParameterKey parameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }

    public static final String DELIVER_ITEM__SEARCH_SERVICE = "oleDeliverItemSearchService";
    public static final String OLE_PLATFORM_DOC = "OLE_PLTFRM_DOC";
    public static final List<String> PLATFORM_RESULT_FIELDS = getSearchPlatformfields();
    public static final List<String> getSearchPlatformfields() {
        List<String> resultFields = new ArrayList<String>();
        resultFields.add(PLATFORM_NAME);
        resultFields.add(GOKB_ID);
        resultFields.add(OLE_PLATFORM_ID);
        resultFields.add(PLATFORM_PROVIDER_NAME);
        resultFields.add(STATUS_NAME);
        resultFields.add(DOC_NUM);
        return Collections.unmodifiableList(resultFields);
    }
    public static final String DUPLICATE = "error.duplicate";
    public static final String ERROR_DEFAULT_IND_DUPLICATE="error.duplicate.defaultIndicator";
    public static final String PLATFORM_SERVICE = "olePlatformService";


    public static final String CONFIRMATION_MESSAGE_INSTANCE = "<br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Create another PO for this instance? <br/><br/>";
    public static final String CONFIRMATION_MESSAGE_E_RESOURCE = "<br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Create another PO for this E-Resource? <br/><br/>";
    public static final String ERROR_MESSAGE_ERESOURCE = "EResource document doesn't exists for ";
    public static final String ERROR_MESSAGE_PLATFORM = "Platform document doesn't exists for ";
    public static final String ERROR_MESSAGE_MORE_THAN_ONE_ERESOURCE = "More than one EResource document exists";
    public static final String ERROR_MESSAGE_MORE_THAN_ONE_PLATFORM = "More than one Platform document exists";
    public static final String OLE_FUND_CODE_ACCOUNTING_LINE = "OleFundCode_AccountingLine";
    public static final String ERROR_NOTE_REQUIRED = "error.note.required";
    public static final String OLE_VENDOR_ACTIVE = "error.vendor.active";
    public static final String OLE_VENDOR_LINKED = "error.vendor.linked";

    public static final String GOKBID = "GOKbUID";
    public static final String TAG_035 = "035";
    public static final String TAG_245 = "245";
    public static final String TAG_500 = "500";
    public static final String TAG_246 = "246";
    public static final String TAG_022 = "022";
    public static final String TAG_010 = "010";
    public static final String CURRENCY_TYPE = "CURRENCY_TYPE";
    public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
    public static final String VENDOR_TYPE = "VENDOR_TYPE";
    public static final String OWNERSHIP_TYPE = "OWNERSHIP_TYPE";
    public static final String ADDRESS_TYPE = "ADDRESS_TYPE";
    public static final String ADDRESS = "ADDRESS";
    public static final String STATE = "STATE";
    public static final String CITY = "CITY";
    public static final String POSTAL_CODE = "POSTAL_CODE";
    public static final String COUNTRY = "COUNTRY";

    public static final class OleGokb {
        public static final int BATCH_SIZE = 50;
        public static final String PACKAGE_XPATH_EXP = "//package";
        public static final String TITLE_XPATH_EXP = "//title";
        public static final String PLATFORM_XPATH_EXP = "//platform";
        public static final String ORG_XPATH_EXP = "//org";
        public static final String RECORD_XPATH_EXP = "//record";
        public static final String HEADER_XPATH_EXP = "//header";
        public static final String RESUMPTION_TOKEN_EXP = "//resumptionToken";
        public static final String HEADER = "header";
        public static final String DATE_STAMP = "datestamp";
        public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String UTC_TIME_ZONE = "UTC";
        public static final String NUM_PKGS = "NUM_PKGS=";
        public static final String NUM_TIPPS = "NUM_TIPPS=";
        public static final String NUM_TITLES = "NUM_TITLES=";
        public static final String NUM_PLTFRMS = "NUM_PLTFRMS=";
        public static final String NUM_ORGS = "NUM_ORGS=";
        public static final String ID = "id";
        public static final String SCOPE = "scope";
        public static final String STATUS = "status";
        public static final String SERVICE = "service";
        public static final String BREAKABLE = "breakable";
        public static final String FIXED = "fixed";
        public static final String NAME = "name";
        public static final String VARIANT_NAMES = "variantNames";
        public static final String DATE_CREATED = "dateCreated";
        public static final String TIPPS = "TIPPs";
        public static final String TITLE = "title";
        public static final String PLATFORM = "platform";
        public static final String COVERAGE = "coverage";
        public static final String START_DATE = "startDate";
        public static final String START_VOLUME = "startVolume";
        public static final String START_ISSUE = "startIssue";
        public static final String END_DATE = "endDate";
        public static final String END_VOLUME = "endVolume";
        public static final String END_ISSUE = "endIssue";
        public static final String URL = "url";
        public static final String IMPRINT = "imprint";
        public static final String MEDIUM = "medium";
        public static final String IDENTIFIERS = "identifiers";
        public static final String ISSN = "issn";
        public static final String EISSN = "eissn";
        public static final String DOI = "doi";
        public static final String PROPRIETARY_ID = "proprietaryId";
        public static final String OCLC_NUM = "oclc";
        public static final String PUBLISHER = "publisher";
        public static final String AUTHENTICATION = "authentication";
        public static final String SOFTWARE = "software";
        public static final String ROLES = "roles";
        public static final String PIPE = "|";
    }

    public static final class PurapInvoiceHistory{
        public static final String PURAP_DOC_LINK = "accountsPayablePurchasingDocumentLinkIdentifier";
        public static final String POID = "purchaseOrderIdentifier";
        public static final String PO_ITM_ID = "poItemIdentifier";
        public static final String INVOICE_ID = "invoiceIdentifier";
    }
    public static final String ALRT_DOC_ALRT_FIELD_VAL = "AlertDocument-FieldValueMapping";
    public static final String INVALID_INTEGER = "error.invalid.integer.data";
    public static final String EMPTY_FIELDNAME = "error.empty.field.name";
    public static final String EMPTY_FIELDTYPE = "error.empty.field.type";
    public static final String EMPTY_FIELDVALUE = "error.empty.field.value";
    public static final String INVALID_BOOLEAN = "error.invalid.boolean.data";
    public static final String ALRT_DOC_TYPE_NAME_FIELD = "document-Typesdf";
    public static final String EMPTY_DOCTYPENAME = "error.empty.doctype.name";
    public static final String INVALID_DOCTYPENAME = "error.invalid.doctype.name";
    public static final String ALRT_DOCTYPENAME = "dataobject.documentTypeName";
    public static final String ALRT_DOCCLASSNAME = "dataobject.documentClassName";
    public static final String ALRTDOCTYP_NAME = "dataobject.alertDocumentTypeName";
    public static final String ALRTDOCTYPE_CLASS = "dataobject.alertDocumentClass";


    public static final String coverageStartDate = "coverageStartDate";
    public static final String coverageStartIssue = "coverageStartIssue";
    public static final String coverageStartVolume = "coverageStartVolume";
    public static final String coverageEndDate = "coverageEndDate";
    public static final String coverageEndIssue = "coverageEndIssue";
    public static final String coverageEndVolume = "coverageEndVolume";
    public static final String url = "url";
    public static final String gokbId = "gokbId";

    public static final String E_RESOURCE_NAME = "title";
    public static final String VARIANT_NAME = "VariantName";
    public static final String PUBLISHER = "Publisher";
    public static final String PACKAGE_SCOPE = "PackageScope";
    public static final String BREAKABLE = "Breakable";
    public static final String FIXED_TITLE_LIST = "FixedTitleList";
    public static final String PACKAGE_TYPE = "PackageType";
    public static final String E_RESOURCE_GOKBID = "eResourceGokbId";

    public static final String ERES_ACCESS_MAINTENANCE_ACTION_LINK = "oleEEResourceAccessMaintenance";
    public static final String OLE_MAINTENANCE_ACTION_LINK="oleMaintenance";
    public static final String ALERT_EVENT_NAME="alertEventName";
    public static final String ALERT_DOC_CLASS_NAME="alertDocumentClass";
    public static final String ALERT_DOC_TYPE_EXIST="alertDocumentTypeName";
    public static final String ALERT_DOC_MAINTENANCE_ACTION_LINK="alertMaintenance";
    public static final String ALERT_DOC_CONDITION="AlertDocument-AlertAndNotificationSection";
    public static final String ACTION_EVENT="error.action.event";


    public static final String NO_WORKFLOW="error.no.workflow";
    public static final String NO_DESCRIPTION="error.no.description";
    public static final String ACCESS_ACTIVATION_DESCRIPTION="Access Activation Record";
    public static final String INVALID_WORK_FLOW ="error.invalid.workflow";
    public static final String NEW_ALERT_DESCRIPTION = "New Alert Document";
    public static final String EDIT_ALERT_DESCRIPTION = "Edited Alert Document";
    public static final String COPY_ALERT_DESCRIPTION = "Saved Alert Document";

    public static final String PHRASE = "phrase";
	public static final String ARCHIVE_DATE = "archiveDate";
    public static final String MYSQL = "mysql";
    public static final String DB_VENDOR = "db.vendor";
    public static final String RENCUM_DATE_FORMAT = "yyyy-MM-dd";
    public static final String LOST_NOTICE_TO_DATE="LOST_NOTICE_TO_DATE";
    public static final String SIP_CIRC_STATUS = "OLE_ITEM_STATUS_WITH_SIP_CIRC_STATUS";
    public static final String NOTICE_THREAD_POOL_SIZE = "NOTICE_THREAD_POOL_SIZE";
    public static final String BULK_PO_CHANGE_THREAD_POOL_SIZE = "NOTICE_THREAD_POOL_SIZE";
    public static final String COURTESY_NOTICE_TO_DATE = "COURTESY_NOTICE_TO_DATE";
    public static final String OVERDUE_NOTICE_TO_DATE = "OVERDUE_NOTICE_TO_DATE";
    public static final String OLE_LOAN_DAO = "oleLoanDao";
    public static final String LOAN_WITH_NOTICES_DAO = "loanWithNoticesDAO";
    public static final String LOAN_HISTORY_DAO = "loanHistoryDAO";
    public static final String REQUEST_NOTICE_DAO="requestNoticeDAO";
    public static final String POBA_DIRECTORY = "poba";
    public static final String SUCCESSFULLEY_UPLOADED = "success.file.upload";

    public static final String N0_PROFILE_SELECTED = "error.no.profile";
    public static final String UPLOAD_FILE_AGAIN = "error.file.upload";
    public static final String SELECT_USER="error.select.user";
    public static final int DEFAULT_INV_ITM_LMT = 50;
    public static final class OLEPurchaseOrderBulkAmendment {
        public static final String CHART_CD = "chartOfAccountsCode";
        public static final String ACC_NO = "accountNumber";
        public static final String OBJ_CD = "financialObjectCode";
        public static final String DATE_FORMAT = "yyyy-MM-ddHH:mm:ss";
        public static final String PUR_AP_ID = "purapDocumentIdentifier";
        public static final String PUR_AP_CUR_IND = "purchaseOrderCurrentIndicator";
        public static final String FILE_NM = "POBA-Report.txt";
        public static final String ACC_LIN_PERC = "accountLinePercent";
        public static final String DOC_NO = "documentNumber";
        public static final String ITM_LIN_NO = "itemLineNumber";
        public static final String VENDOR_ITEM_NO = "vendorItemPoNumber";
        public static final String ITM_LOC = "itemLocation";
        public static final String ITM_LIST_PRC = "itemListPrice";
        public static final String ITM_QTY = "itemQuantity";
        public static final String ITM_NO_OF_PARTS = "itemNoOfParts";
        public static final String ITEM_PUB_VIEW_IND = "itemPublicViewIndicator";
        public static final String DO_NOT_CLAIM = "doNotClaim";
        public static final String ITEM_ROUTE_IND = "itemRouteToRequestorIndicator";
        public static final String ACC_LINE_INDEX = "accountingLineIndex";
        public static final String DEL_ST_CD = "deliveryStateCode";
        public static final String DEL_ROOM_NO = "deliveryBuildingRoomNumber";
        public static final String DEL_LINE1_ADDR = "deliveryBuildingLine1Address";
        public static final String DEL_LINE2_ADDR = "deliveryBuildingLine2Address";
        public static final String DEL_CAMPUS_CD = "deliveryCampusCode";
        public static final String DEL_COUNTRY_CD = "deliveryCountryCode";
        public static final String DEL_POSTAL_CD = "deliveryPostalCode";
        public static final String DEL_BUL_CD = "deliveryBuildingCode";
        public static final String DEL_TO_EMAIL_ADDR = "deliveryToEmailAddress";
        public static final String DEL_TO_NAME = "deliveryToName";
        public static final String DEL_TO_PHONE_NO = "deliveryToPhoneNumber";
        public static final String DEL_INST_NT = "deliveryInstructionText";
        public static final String OUTPUT_FILE_NAME = "_PurchaseOrderBulkAmendmentFile.csv";
        public static final String POBA = "poba";
        public static final String DOC_DESC = "POBA";
        public static final String SPEC_TIME = "specified time";
        public static final String IMMEDIATE = "immediate";
        public static final String FORMAT = "yyyy-MM-dd";
        public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String VENDOR_NAME = "vendorName";
        public static final String DOCUMENT_DESC = "documentDescription";
        public static final String ASSIGN_TO_PRCPL_NM = "assignedUserPrincipalName";
        public static final String RECUR_PAY_TYP_CD = "recurringPaymentTypeCode";
        public static final String PO_BEGIN_DT = "poBeginDate";
        public static final String PO_END_DATE = "poEndDate";
        public static final String ITM_COPY_NO = "itemCopyNumber";
        public static final String FORMAT_CD = "format";
        public static final String CATEORY_CD = "category";
        public static final String ITM_PRICE_SOURCE = "itemPriceSource";
        public static final String REQUEST_SOURCE = "requestSource";
        public static final String VENDOR_NUMBER = "vendorHeaderGeneratedIdentifier";
        public static final String VEN_DTL_ASSIGN_ID = "vendorDetailAssignedIdentifier";
    }

    public static final String RECEIPT_PRINTER_PAGE_SIZE = "B7";
    public static final String RECEIPT_PRINTER_FONT_SIZE = "13";
    public static final String REGULAR_PRINTER_PAGE_SIZE = "A4";
    public static final String REGULAR_PRINTER_FONT_SIZE = "10";
    public static final String RECALL_NOTICE = "RecallNotice";
    public static final String ONHOLD_NOTICE = "OnHoldNotice";
    public static final String ONHOLD_COURTESY_NOTICE = "OnHoldCourtesyNotice";
    public static final String REQUEST_EXPIRATION_NOTICE="RequestExpirationNotice";
    public static final String ONHOLD_EXPIRATION_NOTICE="OnHoldExpirationNotice";
    public static final String MISSING_PIECE_NOTICE ="Missing Piece Notice";
    public static final String MISSING_PIECE_NOTICE_TITLE ="Return With Missing Item Notice";
    public static final String MISSING_PIECE_NOTICE_BODY ="The following item(s) returned by you is missing one or more of its pieces.Please return the missing piece(s) to the library shown above or contact the library about this matter to avoid incurring any penalties.";
    public static final String MISSING_PIECE_NOTICE_CONFIG_NAME ="MissingPieceNoticeConfig";

    public static final String NEW_NOTICE_CONTENT_CONFIG_DOC="New Notice Content Configuration Document";
    public static final String COPY_NOTICE_CONTENT_CONFIG_DOC="Copied Notice Content Configuration Document";
    public static final String EDIT_NOTICE_CONTENT_CONFIG_DOC="Edited Notice Content Configuration Document";


    public static final String PATRON_INFORMATION = "Patron Information";
    public static final String PATRON_NAME = "Patron Name";
    public static final String NOTICE_ADDRESS = "Address";
    public static final String NOTICE_EMAIL = "Email";
    public static final String NOTICE_PHONE_NUMBER = "Phone #";
    public static final String TITLE_ITEM_INFORMATION = "Title/Item Information";
    public static final String CIRCULATION_LOCATION_LIBRARY_NAME = "Circulation Location/Library Name";
    public static final String CIRCULATION_REPLY_TO_EMAIL = "Circulation Reply-To Email";
    public static final String NOTICE_TITLE = "Title";
    public static final String NOTICE_AUTHOR = "Author";
    public static final String VOLUME_ISSUE_COPY = "Volume/Issue/Copy #";
    public static final String NOTICE_CALL_NUMBER = "Call #";
    public static final String NOTICE_CALL_NUMBER_PREFIX ="Call # Prefix";
    public static final String NOTICE_ITEM_BARCODE = "Item_Barcode";
    public static final String ORIGINAL_DUE_DATE = "Original Due Date";
    public static final String NEW_DUE_DATE = "New Due Date";
    public static final String ITEM_DUE_DATE = "Item Due Date";
    public static final String ITEM_WAS_DUE = "Item was due";
    public static final String HOLD_EXPIRATION_DATE = "Hold Expiration Date";
    public static final String NOTICE_RECALL_DUE_DATE = "Hold Expiration Date";
    public static final String LIBRARY_SHELVING_LOCATION ="Library shelving location";
    public static final String LIBRARY_LOCATION="Library Location";
    public static final String MISSING_ITEM_CHECK_IN_DATE = "Check In Date";
    public static final String MISSING_ITEM_NOTE ="Missing Piece Note";
    public static final String CLAIMS_SEARCH_COUNT = "Claims Search Count";

    public static final String SIMPLE_DATE_FORMAT_FOR_TIME_24H = "HH:mm";
    public static final String DEFAULT_DATE_FORMAT_24H = RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE +" "+ SIMPLE_DATE_FORMAT_FOR_TIME_24H;



    public static final String PATRON_NOTE_DEFAULT_OPERATOR ="Bulk Upload";
    public static final String METHOD_TO_CALL = "downloadAgreement,insertAgreementDocument";

    public static final String CHART_CD = "chartofaccountscode";
    public static final String ACC_NO = "ACCOUNT_NBR";
    public static final String SUB_ACC_NO = "sunAccountNumber";
    public static final String OBJECT_CD = "objectCode";


    public static final String MAX_NO_OF_THREAD_FOR_RENEW_SERVICE = "MAX_NO_OF_THREAD_FOR_RENEW_SERVICE";
    public static final String  RECALL_COURTESY_NOTICE = "RECALL_COURTESY_NOTICE";
    public static final String  RECALL_OVERDUE_NOTICE = "RECALL_OVERDUE_NOTICE";
    public static final String  RECALL_LOST_NOTICE = "RECALL_LOST_NOTICE";

    public static final String ADDRESS_SOURCE_CD = "oleAddressSourceCode";
    public static final String CREATE = "create";
    public static final String UPDATE = "update";

    public static final String NO_RULE_FOUND = "No rule found for given combination";
    public static final String NO_ASR_REQUEST = "error.no.asr.request";

    public static final String BIB_SEARCH_SCOPE_FIELD = "BIB_SEARCHSCOPE_FIELD";
    public static final String HOLDINGS_SEARCH_SCOPE_FIELD = "HOLDINGS_SEARCHSCOPE_FIELD";
    public static final String EHOLDINGS_SEARCH_SCOPE_FIELD = "EHOLDINGS_SEARCHSCOPE_FIELD";
    public static final String ITEM_SEARCH_SCOPE_FIELD = "ITEM_SEARCHSCOPE_FIELD";
    public static final String BIB_RENDER_PRINT = "BIB_RENDER_PRINT";
    public static final String ITEM_LOCATION_DISCLOSURE = "ITEM_LOCATION_DISCLOSURE";

    public static final String DEFAULT_TIME_FOR_DUE_DATE ="DEFAULT_TIME_FOR_DUE_DATE";

    public static final String HOLDINGS_DEFAULT_NOTE = "HOLDINGS_DEFAULT_NOTE_NONPUBLIC";
    public static final String ITEM_DEFAULT_NOTE = "ITEM_DEFAULT_NOTE_NONPUBLIC";
    public static final String HOLDINGS_SUPRESS_SHELVINGORDER = "HOLDINGS_SUPRESS_SHELVINGORDER";
    public static final String ITEM_SUPRESS_SHELVINGORDER = "ITEM_SUPRESS_SHELVINGORDER";

    public static final String HOLDINGS_CALL_NUMBER_TYPE ="HOLDINGS_CALL_NUMBER_TYPE";
    public static final String ITEM_CALL_NUMBER_TYPE ="ITEM_CALL_NUMBER_TYPE";
    public static final String E_HOLDINGS_CALL_NUMBER_TYPE ="EHOLDINGS_CALL_NUMBER_TYPE";

    public static final String DEFAULT_PAGE_SIZE_LOANED_ITEMS = "DEFAULT_PAGE_SIZE_LOANED_ITEMS";
    public static final String WHILE_CLAIMS_RETURN_SHOW_CANCEL_REQUEST_POPUP = "WHILE_CLAIMS_RETURN_SHOW_CANCEL_REQUEST_POPUP";
    public static final String DEFAULT_ITEM_TYPE_CODE = "DEFAULT_ITEM_TYPE_CODE";
    public static final String PATRON_NOTE_TYPES_TO_DISPLAY= "PATRON_NOTE_TYPES_TO_DISPLAY";

    public static final String NOTICE_CHRONOLOGY="Chronology";
    public static final String NOTICE_ENUMERATION="Enumeration";
    public static final String NOTICE_COPY_NUMBER="CopyNumber";

    public static final String ITEM_TYPE_CODE = "itemTypeCode";
    public static final String ITEM_TYP_CD_VALUE = "ITEM";
    public static final String ITEM_WILL_BE_HELD_UNTIL = "Item Will Be Held until";

    public static final String SEND_ONHOLD_NOTICE_WHILE_CHECKIN = "SEND_ONHOLD_NOTICE_WHILE_CHECKIN";
    public static final String SEND_MISSING_PIECE_NOTICE_WHILE_CHECKIN = "SEND_MISSING_PIECE_NOTICE_WHILE_CHECKIN";
    public static final String CHUNK_SIZE_FOR_NOTICE_CONTENT_REINDEX = "CHUNK_SIZE_FOR_NOTICE_CONTENT_REINDEX";

    public static final String LOAN_DOCUMENTS = "loanDocuments";
    public static final String DELIVER_NOTICES = "deliverNotices";
    public static final String SEND_CHECKOUT_RECEIPT_NOTICE = "SEND_CHECKOUT_RECEIPT_NOTICE";
    public static final String NOTICE_CONTENT_CONFIG_NAME = "noticeContentConfigName";
    public static final String FINE_CALC_WHILE_RENEW = "FINE_CALC_WHILE_RENEW";

    public static final String BLANKET_APPROVE = "BLANKET_APPROVAL";
    public static final String NOTIFICATION_TYP_CD = "K";
    public static final String INVOICE_DOC = "OLE_PRQS";
    public static final String PREQ_DOC = "OLE_PREQ";
    public static final String ERROR_REPORT_FILE_NM = "/ErrorReport";
    public static final String TEMPORARY_HISTORY_RECORD_DAYS = "TEMPORARY_HISTORY_RECORD_DAYS";
    public static final String NUMBER_OF_WORKING_DAYS = "numberOfWorkingDays";
    public static final String NUMBER_OF_WORKING_HOURS = "numberOfWorkingHours";
    public static final String MAIN_REC_REC_TYP = "Main";
    public static final String MESSAGE_UNSAVED_CHANGES = "message.unsaved.changes";


    public static final String MAX_NO_OF_THREAD_FOR_ORDER_IMPORT = "MAX_NO_OF_THREAD_FOR_ORDER_IMPORT";
    public static final String CHUNK_SIZE_FOR_ORDER_IMPORT = "CHUNK_SIZE_FOR_ORDER_IMPORT";
    public static final String NOT_ELIGIBLE_FOR_DEBIT = "error.not.eligible.transfer";
    public static final String REQUIRED_TRANSFER_AMT = "transfer.amt.required";
    public static final String REFUND_NOT_APPLICABLE = "error.refund.not.applicable";
    public static final String REFUND_AMT_NOT_APPLICABLE = "error.refund.amt.not.applicable";
    public static final String CANCEL_NOT_APPLICABLE = "error.not.eligible.cancel";
    public static final String REFUND_NOTICE = "Refund Notice";
    public static final String DEBT_TRANS_NOTELIGIBLE = "debit.transfer.not.eligible";
    public static final String PAY_FEE_PAR_TRANS = "PAY_FEE_PARTIALLY_TRANSFERED";
    public static final String PAY_TRANSFERRED = "Transferred";
    public static final String PAY_FULL_CRDT_ISSUED ="PAY_FULL_CRDT_ISSUED";
    public static final String PAY_PAR_CRDT_ISSUED ="PAY_PAR_CRDT_ISSUED";
    public static final String PAY_CRDT_FULLY_TRANSFERRED = "PAY_CRDT_FULLY_TRANSFERED";
    public static final String PAY_CRDT_PAR_TRANSFERRED = "PAY_CRDT_PAR_TRANSFERED";
    public static final String PAY_CRDT_FULLY_CANCELLED = "PAY_CRDT_FULLY_CANCELLED";
    public static final String PAY_CRDT_PAR_CANCELLED = "PAY_CRDT_PAR_CANCELLED";
    public static final String PAY_CRDT_CANCELLED = "Credit Cancelled";
    public static final String PAY_FULLY_REFUNDED ="PAY_FULLY_REFUNDED";
    public static final String PAY_PAR_REFUNDED ="PAY_PAR_REFUNDED";
    public static final String PAY_REFUNDED_APPLIED ="Refund Applied";
    public static final String PAY_FULLY_PAID_CRDT ="PAY_FULLY_PAID_CRDT";
    public static final String PAY_PAR_PAID_CRDT ="PAY_PAR_PAID_CRDT";
    public static final String PAY_REFUNDED_ISSUED ="Refund Issued";
    public static final String PAY_APPL_CRDT = "Applied Credit";
    public static final String RECALL_DELIVERY_REQUEST="Recall/Delivery Request";
    public static final String RECALL_HOLD_REQUEST="Recall/Hold Request";
    public static final String OVER_PAYMENT="refund.over.payment";
    public static final String ENTRR_REFUND_AMT="enter.refund.amount";
    public static final String NOTIFY_CLAIMS_RETURNED_TO_PATRON="NOTIFY_CLAIMS_RETURNED_TO_PATRON";
    public static final String CR_ITEMS_COUNT_TOWARD_LOANED_ITEMS_COUNT="CR_ITEMS_COUNT_TOWARD_LOANED_ITEMS_COUNT";
    public static final String ERROR_SELECT_ANY_ITEM="error.select.any.item";
    public static final String RECORD_UPDATED_SUCCESSFULLY="record.updated.successfully";
    public static final String CR_ITEM_SEARCH_COUNT_BEFORE_ITEM_BILLED="CR_ITEM_SEARCH_COUNT_BEFORE_ITEM_BILLED";
    public static final String ERROR_CIRC_DESK_REQUIRED = "error.circulation.desk.required";
    public static final String FORGIVE_LOST_FEES = "FORGIVE_LOST_FEES";
    public static final String ON_HOLD_EXP_NOTICE = "On Hold Expiration Notice";
    public static final String HOLD_COUR_NOT_TYP = "HOLDCOURTESY_NOTICE_TYPE";
    public static final String EMAIL_NOT_TYP = "email";
    public static final String ERROR_FROM_DATE = "error.from.date";
    public static final String ERROR_TO_DATE = "error.to.date";
    public static final String REPLACMENT_FEE="REPL_FEE";
    public static final String ITEM_BARCODE_OR_UUID_REQUIRED= "item.barcode.or.uuid.required";
    public static final String NO_RESULTS_FOUND = "no.result.found";
    public static final String PAYMENT_SUSPENDED = "PAY_SUSPENDED";
    public static final String PAY_NOT_ALLOWED = "error.pay.not.allowed";
    public static final String FORGIVE_PAY_NOT_ALLOWED = "error.forgive.pay.not.allowed";
    public static final String BILL_NOTE = "note";
    public static final String CLAIM_BILL_NOTE = "The fee was created as the end of a claim";
    public static final String CLAIMS_RETURN_FOUND_IN_LIB = "Claims Returned: [0]  found in library and checked in [1] at [2]";
    public static final String CLAIMS_RETURN_NOT_FOUND_IN_LIB = "Claims Returned: [0]  returned by patron and checked in [1] at [2]";
    public static final String BILL_SUSPENDED_NOTE = "The bill has been unsuspended as the result of the end of the claims process";
    public static final String ITEM_MISSING_IN_PATRON = "Claims Returned: [0] not found, waived and marked missing [1] at [2] ";
    public static final String ITEM_NOT_FOUND_IN_PATRON = "Claims Returned: [0] not found,  billed for replacement  [1] at [2] ";

    public static final String AGREEMENT_AUTH_USERS="AGREEMENT_AUTH_USERS";
    public static final String AGREEMENT_RECORD="agreementRecord";
    public static final String RECALL_NOTICE_SUBJECT = "Recall Notice";
    public static final String ONHOLD_NOTICE_SUBJECT = "On Hold Notice";
    public static final String REQUEST_EXPIRATION_NOTICE_SUBJECT = "Request Expiration Notice";
    public static final String NOTICE_HOLD_COURTESY_SUBJECT = "Hold Courtesy Notice";
    public static final String REQUEST_EXPIRATION_NOTICE_SUBJECT_LINE="Library Request Expired";
    public static final String RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE=" Missing Piece Returned";
    public static final String PICKUP_NOTICE_SUBJECT_LINE="Library Item Available for Pickup";
    public static final String ZERO_ENCUMBRANCE_CHECK = "ZERO_ENCUMBRANCE_CHECK";

}
