package org.kuali.ole.constants;

import java.text.SimpleDateFormat;

/**
 * Created by SheikS on 1/6/2016.
 */
public class OleNGConstants {

    public static final class BatchProcess {
        public static final String ACCOUNT_NUMBER = "Account Number";
        public static final String VENDOR_INFO_CUSOTMER_NUMBER = "Acquisition Unit\'s Vendor account / Vendor Info Customer #";
        public static final String ASSIGN_TO_USER = "Assign To User";
        public static final String BUILDING_CODE = "Building Code";
        public static final String CAPTION = "Caption";
        public static final String CHART_CODE = "Chart Code";
        public static final String CONTRACT_MANAGER = "Contract Manager";
        public static final String COST_SOURCE = "Cost Source";
        public static final String DEFAULT_LOCATION = "Default Location";
        public static final String DELIVERY_BUILDING_ROOM_NUMBER = "Building Room Number";
        public static final String DELIVERY_CAMPUS_CODE = "Delivery Campus Code";
        public static final String DISCOUNT = "Discount";
        public static final String discountType = "Discount Type";
        public static final String FUNDING_CODE = "Fund Code";
        public static final String FUNDING_SOURCE = "Funding Source";
        public static final String ITEM_CHART_CODE = "Item Chart Code";
        public static final String ITEM_STATUS = "Item Status";
        public static final String LIST_PRICE = "List Price";
        public static final String CURRENCY_TYPE = "Currency Type";
        public static final String METHOD_OF_PO_TRANSMISSION = "Method Of PO Transmission";
        public static final String ITEM_NO_OF_PARTS = "No Of Parts";
        public static final String OBJECT_CODE = "Object Code";
        public static final String ORDER_TYPE = "Order Type";
        public static final String ORG_CODE = "Org Code";
        public static final String PREQ_POSITIVE_APPROVAL_REQ = "Pay Req Positive Approval Req";
        public static final String PERCENT = "Percent";
        public static final String PO_CONFIRMATION_INDICATOR = "Purchase Order Confirmation Indicator";
        public static final String QUANTITY = "Quantity";
        public static final String RECEIVING_REQUIRED = "Receiving Required";
        public static final String RECURRING_PAYMENT_BEGIN_DT = "Recurring Payment Begin Date";
        public static final String RECURRING_PAYMENT_END_DT = "Recurring Payment End Date";
        public static final String RECURRING_PAYMENT_TYP = "Recurring Payment Type";
        public static final String REQUEST_SRC = "Request Source";
        public static final String REQUESTOR_NAME = "Requestor Name";
        public static final String ROUTE_TO_REQUESTOR = "Route To Requestor";
        public static final String USE_TAX_INDICATOR = "Use Tax Indicator";
        public static final String VENDOR_ALIAS_NAME = "Vendor Alias Name";
        public static final String VENDOR_CHOICE = "Vendor Choice";
        public static final String VENDOR_NUMBER = "Vendor Number";
        public static final String vendorProfileCode = "Vendor Profile Code";
        public static final String vendorReferenceNumber = "Vendor Reference Number";
        public static final String volumeNumber = "Volume Number";
        public static final String LINE_ITEM_NOTE = "Line Item Note";

        public static final String REQUISITION_SOURCE = "requisitionSource";
        public static final String ITEM_PRICE_SOURCE = "itemPriceSource";
        public static final String SINGLE_COPY_NUMBER = "singleCopyNumber";


        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER = "Holdings Call Number";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE = "Holdings Call Number Type";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER = "Holdings Copy Number";
        public static final String DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_PREFIX = "Holdings Call Number Prefix";
        public static final String DESTINATION_FIELD_CALL_NUMBER = "Call Number";
        public static final String DESTINATION_FIELD_COPY_NUMBER = "Copy Number";
        public static final String DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY = "Donor Public Display";
        public static final String DESTINATION_FIELD_DONOR_NOTE = "Donor Note";
        public static final String DESTINATION_FIELD_DONOR_CODE = "Donor Code";
        public static final String RCPT_NOTE = "Receipt Note";
        public static final String RQST_NOTE = "Requestor Note";
        public static final String SELECTOR_NOTE = "Selector Note";
        public static final String SPL_PROCESS_NOTE = "Special Processing Instruction Note";
        public static final String VNDR_INSTR_NOTE = "Vendor Instructions Note";
        public static final String VENDOR_CUST_NBR = "vendorCustomerNumber";
        public static final String BOOK_PLATE = "Book Plate";
        public static final String EBOOK = "E-Book";
        public static final String EXCHANGE_RATE = "Exchange Rate";
        public static final String INVOICE_DATE = "Invoice Date";
        public static final String INVOICE_NUMBER = "Invoice Number";
        public static final String INVOICE_FOREIGN_PRICE = "Invoiced Foreign Price";
        public static final String INVOICE_PRICE = "Invoiced Price";
        public static final String ITEM_DESCRIPTION = "Item Description";
        public static final String REQUESTOR = "Requestor";
        public static final String VENDOR_ITEM_IDENTIFIER = "Vendor Item Identifier";
        public static final String PO_NUMBER = "Purchase Order Number";

        public static final String CALL_NUMBER = "Call Number";
        public static final String CALL_NUMBER_PREFIX = "Call Number Prefix";
        public static final String CALL_NUMBER_TYPE = "Call Number Type";
        public static final String CHRONOLOGY = "Chronology";
        public static final String COPY_NUMBER = "Copy Number";
        public static final String DONOR_CODE = "Donor Code";
        public static final String DONOR_NOTE = "Donor Note";
        public static final String DONOR_PUBLIC_DISPLAY = "Donor Public Display";
        public static final String ENUMERATION = "Enumeration";
        public static final String ITEM_BARCODE = "Item Barcode";
        public static final String NO_OF_PIECES = "Number of Pieces";
        public static final String STAFF_ONLY = "Staff Only";
        public static final String ITEM_TYPE = "Item Type";
        public static final String STATISTICAL_CODE = "Statistical Code";
        public static final String VENDOR_LINE_ITEM_IDENTIFIER = "Vendor Line Item Identifier";
        public static final String ACCESS_STATUS = "Access Status";
        public static final String SUBSCRIPTION_STATUS = "Subscription Status";
        public static final String CREATE_REQ_PO = "Create Requisition and PO";
        public static final String CREATE_REQ_ONLY = "Create Requisition Only";
        public static final String CREATE_NEITHER_REQ_NOR_PO = "Create neither a Requisition nor a PO";


        public final static String LOCATION_LEVEL_1 = "Location Level1";
        public final static String LOCATION_LEVEL_2 = "Location Level2";
        public final static String LOCATION_LEVEL_3 = "Location Level3";
        public final static String LOCATION_LEVEL_4 = "Location Level4";
        public final static String LOCATION_LEVEL_5 = "Location Level5";

        public final static String HOLDINGS_LOCATION_LEVEL_1 = "Holdings Location Level1";
        public final static String HOLDINGS_LOCATION_LEVEL_2 = "Holdings Location Level2";
        public final static String HOLDINGS_LOCATION_LEVEL_3 = "Holdings Location Level3";
        public final static String HOLDINGS_LOCATION_LEVEL_4 = "Holdings Location Level4";
        public final static String HOLDINGS_LOCATION_LEVEL_5 = "Holdings Location Level5";

        public final static String ORDER_TYPE_HOLDINGS_AND_ITEM = "Holdings and Item";
        public final static String ORDER_TYPE_EHOLDINGS = "Eholdings";
    }

    public static final String TAG_001 = "001";
    public static final String TAG_003 = "003";
    public static final String UPDATED_BY = "updatedBy";
    public static final String UPDATED_DATE = "updatedDate";
    public static final String BIB_STATUS_UPDATED = "BIB_STATUS_UPDATED";
    public static final String UNMODIFIED_CONTENT = "unmodifiedContent";
    public static final String MODIFIED_CONTENT = "modifiedContent";
    public static final String OPS = "ops";
    public static final String MATCHPOINT_FOR_DATAMAPPING = "matchpointForDataMapping";
    public static final String ADDED_OPS = "addedOps";
    public static final String ADDITIONAL_OVERLAY_OPS = "additionalOverlayOps";
    public static final String ACTION_OPS = "actionOps";
    public static final String FIELD_OPS = "fieldOps";
    public static final String BIB_DATAMAPPINGS = "bibDataMappings";
    public static final String HOLDINGS_DATAMAPPINGS = "holdingsDataMappings";
    public static final String EHOLDINGS_DATAMAPPINGS = "eHoldingsDataMappings";
    public static final String ITEM_DATAMAPPINGS = "itemDataMappings";
    public static final String DATAMAPPING = "dataMapping";
    public static final String BIBLIOGRAPHIC = "bibliographic";
    public static final String HOLDINGS = "holdings";
    public static final String HOLDINGS_FOR_CREATE = "holdingsForCreate";
    public static final String HOLDINGS_FOR_UPDATE = "holdingsForUpdate";
    public static final String EHOLDINGS = "eholdings";
    public static final String EHOLDINGS_FOR_CREATE = "eholdingsForCreate";
    public static final String EHOLDINGS_FOR_UPDATE = "eholdingsForUpdate";
    public static final String ITEM = "item";
    public static final String ITEMS_FOR_CREATE = "itemsForCreate";
    public static final String ITEMS_FOR_UPDATE = "itemsForUpdate";
    public static final String BIB_MARC = "Bib Marc";
    public static final String CONSTANT = "Constant";
    public static final String PRE_MARC_TRANSFORMATION = "Pre Marc Transformation";
    public static final String POST_MARC_TRANSFORMATION = "Post Marc Transformation";
    public static final String ADD = "Add";
    public static final String OVERLAY = "Overlay";
    public static final String DELETE_ALL = "Delete All";
    public static final String DISCARD = "Discard";
    public static final String IF_MATCH_FOUND = "If Match Found";
    public static final String IF_NOT_MATCH_FOUND = "If Match Not Found";
    public static final String CREATE_MULTIPLE_DELETE_ALL_EXISTING = "Create multiple, Delete all existing and add";
    public static final String CREATE_MULTIPLE_KEEP_ALL_EXISTING = "Create multiple, Keep all existing and add";
    public static final String DELETE_ALL_EXISTING_AND_ADD = "Delete all existing and add";
    public static final String KEEP_ALL_EXISTING_AND_ADD = "Keep all existing and add";
    public static final String CREATE_MULTIPLE = "Create multiple";
    public static final String OVERLAY_MULTIPLE = "Overlay multiple";
    public static final String MATCH_POINT = "matchPoints";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";
    public static final String FOUR = "4";
    public static final String FIVE = "5";
    public static final String MDF_ = "mdf_";
    public static final String CONTROL_FIELD_ = "controlfield_";
    public static final String BIB_QUERY_BEGIN = "((DocType:bibliographic) AND (";
    public static final String ADD_FIELD = "Add Field";
    public static final String ADD_SUBFIELD = "Add Subfield";
    public static final String DELETE_FIELD = "Delete Field";
    public static final String DELETE_SUBFIELD = "Delete Subfield";
    public static final String REMOVE_VALUE = "Remove Value";
    public static final String COPY_PASTE = "Copy and Paste";
    public static final String CUT_PASTE = "Cut and Paste";
    public static final String PREPEND_WITH_PREFIX = "Prepend with Prefix";
    public static final String REPLACE = "Replace";
    public static final String SPACE_SPLIT = "[' ']";
    public static final String TRANSACTION_MANAGER = "transactionManager";
    public static final String PROFILE_NAME = "profileName";
    public static final String PROFILE_ID = "profileId";
    public static final String PROFILE_TYPE = "profileType";
    public static final String FILE_NAME = "fileName";
    public static final String CONTENT = "content";
    public static final String ACTION = "action";
    public static final String COPY = "copy";
    public static final String EDIT = "edit";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "stauts";
    public static final String ID = "id";
    public static final String WHERE = "where";
    public static final String CONDITION = "condition";
    public static final String VALUE = "value";
    public static final String FIELD_OPERATION_TYPE = "fieldOperationType";
    public static final String DATA_FIELD = "dataField";
    public static final String IND1 = "ind1";
    public static final String IND2 = "ind2";
    public static final String SUBFIELD = "subField";
    public static final String LINKFIELD = "linkField";
    public static final String IGNORE_GPF = "ignoreGPF";
    public static final String IS_ADDLINE = "isAddLine";
    public static final String DOC_TYPE = "docType";
    public static final String BIB_STATUS = "Bib Status";
    public static final String EQUAL_TO = "Equals To";
    public static final String NOT_EQUAL_TO = "Not Equals To";

    public static final String JOB_ID = "jobId";
    public static final String BATCH_PROCESS_ID = "batchProcessId";
    public static final String PROCESS_NAME = "batchProcessName";
    public static final String PROCESS_TYPE = "batchProcessType";
    public static final String JOB_TYPE = "jobType";
    public static final String CRON_EXPRESSION = "cronExpression";
    public static final String JOB_DETAIL_ID = "jobDetailId";
    public static final String JOB_NAME = "jobName";
    public static final String CREATED_BY = "createdBy";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String PER_COMPLETED = "perCompleted";
    public static final String TIME_SPENT = "timeSpent";
    public static final String TOTAL_RECORDS = "totalRecords";
    public static final String TOTAL_RECORDS_PROCESSED = "totalRecordsProcessed";
    public static final String TOTAL_FAILURE_RECORDS = "totalFailureRecords";
    public static final String JOB_STATUS = "status";
    public static final String CREATED_ON = "createdOn";
    public static final String NEXT_RUN_TIME = "nextRunTime";
    public static final String EXECUTION_COUNT = "executionCount";
    public static final String LAST_EXECUTION_STATUS = "lastExecutionStatus";
    public static final String SCHEDULE_OPTION = "scheduleOption";
    public static final String SCHEDULE_TYPE = "scheduleType";
    public static final String SCHEDULE_DATE = "scheduleDate";
    public static final String SCHEDULE_TIME = "scheduleTime";
    public static final String WEEK_DAY = "weekDay";
    public static final String MONTH_DAY = "monthDay";
    public static final String MONTH_FREQUENCY = "monthFrequency";
    public static final String SCHEDULE = "schedule";
    public static final String COMPLETED = "COMPLETED";
    public static final String FAILED = "FAILED";

    public static final String BATCH_PROCESS_PROFILE_NAME = "batchProcessProfileName";
    public static final String BATCH_PROCESS_PROFILE_TYPE = "batchProcessType";
    public static final String BATCH_PROCESS_PROFILE_ID = "batchProcessProfileId";

    public static final String SUCCESS = "Success";
    public static final String FAILURE = "failure";

    public static final String DOCUMENT_NUMBER = "documentNumber";
    public static final String REQUISITION_ID = "requisitionId";
    public static final String VENDOR_HEADER_GENERATOR_ID = "vendorHeaderGeneratedIdentifier";
    public static final String VENDOR_DETAIL_GENERATOR_ID = "vendorDetailAssignedIdentifier";
    public static final String INVOICE_DOCUMENT = "invoiceDocument";
    public static final String PO_DOCUMENT = "purchaseOrderDocument";

    public static final String OLENG = "/oledsng";
    public static final String CREATE_BIB = "/createBib";
    public static final String UPDATE_BIB = "/updateBib";
    public static final String CREATE_HOLDINGS = "/createHolding";
    public static final String UPDATE_HOLDINGS = "/updateHolding";
    public static final String CREATE_ITEM = "/createItem";
    public static final String UPDATE_ITEM = "/updateItem";
    public static final String PROCESS_BIB_HOLDINGS_ITEMS = "/processBibHoldingsItems";
    public static final String RETRIEVE_BIB_BY_ID = "/retrieveBibById";

    public static final String CREATE_DUMMY_HOLDINGS = "/createDummyHoldings";
    public static final String CREATE_DUMMY_ITEM = "/createDummyItem";
    public static final String HOLDINGS_TYPE = "holdingsType";

    public static final String BIB = "bib";
    public static final String BIB_RECORD = "bibRecord";
    public static final String HOLDINGS_RECORD = "holdingsRecord";
    public static final String ITEM_RECORD = "itemRecord";
    public static final String MATCHED_HOLDINGS = "matchedHoldings";
    public static final String MATCHED_ITEM = "matchedItem";
    public static final String MATCHED_VALUE = "matchedValue";
    public static final String BIB_UPDATED = "bibUpdated";
    public static final String HOLDINGS_CREATED = "holdingRecordsToCreate";
    public static final String HOLDINGS_UPDATED = "holdingRecordsToUpdate";
    public static final String ITEMS_CREATED = "itemRecordsToCreate";
    public static final String ITEMS_UPDATED = "itemRecordsToUpdate";
    public static final String PUBLIC = "public";
    public static final String NON_PUBLIC = "nonPublic";
    public static final String HOLDINGS_MATCH_FOUND = "holdingsMatchFound";
    public static final String EHOLDINGS_MATCH_FOUND = "eholdingsMatchFound";

    public final static String CREATED = "Created";
    public final static String UPDATED = "Updated";
    public final static String DISCARDED = "Discarded";
    public final static String RECORD_INDEX = "recordIndex";
    public final static String MARC = "mrc";
    public final static String EDI = "edi";
    public final static String INV = "inv";
    public final static String BIB_IMPORT = "Bib Import";

    public static final String FORWARD_SLASH = "/";

    public static final String TIMESTAMP_FOR_CAMEL = "-${date:now:yyyy-MMM-dd-hh-mm-ss-a}";
    public static final String QUICK_LAUNCH = "QuickLaunch-";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MMM-dd-hh-mm-ss-a");
    public static final String UTF_8 = "UTF-8";
    public static final String UTF_8_XML_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public static final String COMMON_IDENTIFIER_SEARCH = "common_identifier_search";
    public static final String FAILURE_RESPONSE = "failureResponse";
    public static final String NO_OF_FAILURE_HOLDINGS = "noOfFailureHoldings";
    public static final String NO_OF_FAILURE_ITEM = "noOfFailureItems";
    public static final String NO_OF_FAILURE_EHOLDINGS = "noOfFailureEHoldings";

    public static final String MARC_SPLIT = "\u001E\u001D";

}
