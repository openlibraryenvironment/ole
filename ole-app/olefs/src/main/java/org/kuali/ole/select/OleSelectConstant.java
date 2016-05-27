/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select;

import org.kuali.rice.core.api.CoreApiServiceLocator;

import java.math.BigDecimal;
import java.util.*;

public class OleSelectConstant {

    public static final String CITATION = "CITATION";
    public static final String OPENURL = "OPENURL";
    public static final String FORM = "FORM";

    // added for jira - OLE-2203
    public static final String USD = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString("config.base.currency");
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filePath";
    public static final String XML_FILE_TYPE_EXTENSION = ".xml";
    public static final String XML_FILE_CONTENT = "xmlFileContent";
    public static final String IS_BIB_EDIT = "isBibEdit";
    public static final String BIBMARCXML_DIR = "ole.bibmarcxml.directory";

    public static final String SOAP_SUCCESS = "Document created successfully";
    public static final String SOAP_EXCEPTION = "Failure of document creation";

    public static final String STATUS = "true";

    public static final String SOAP_INVALID_OPENURL = "Failure of document creation: Invalid Open URL";
    public static final String SOAP_CITATION_PARSER_UNREACHABLE = "Failure of document creation: Unable to connect to parser";

    public static final String REQUESTOR_TYPE_STAFF = "STAFF";
    public static final String REQUESTOR_TYPE_BATCHINGEST = "BATCHINGEST";
    public static final String REQUESTOR_TYPE = "requestorType";
    public static final String REQUESTOR_TYPE_ID = "requestorTypeId";

    public static final String SYSTEM_USER_ROLE_NAME = "System User";

    public static final String DEFAULT_VALUE_SYSTEM = "System";
    public static final String DEFAULT_VALUE_ROLE = "Role";
    public static final String DEFAULT_VALUE_USER = "User";

    public static final String REQUEST_SRC_TYPE_BATCHINGEST = "BatchIngest";
    public static final String REQUEST_SRC_TYPE_STAFF = "Staff";
    public static final String REQUEST_SRC_TYPE_WEBFORM = "WebForm";

    public static final String REQUISITON_SRC_TYPE_AUTOINGEST = "AUTO";
    public static final String REQUISITON_SRC_TYPE_B2B = "B2B";
    public static final String REQUISITON_SRC_TYPE_LEGACY = "LGCY";
    public static final String REQUISITON_SRC_TYPE_MANUALINGEST = "MAN";
    public static final String REQUISITON_SRC_TYPE_DIRECTINPUT = "STAN";
    public static final String REQUISITON_SRC_TYPE_WEBFORM = "WEB";

    public static final String DOCSTORE_OPERATION_BATCHINGEST = "BATCHINGEST";
    public static final String DOCSTORE_OPERATION_INGEST = "ingest";
    public static final String DOCSTORE_OPERATION_STAFF = "STAFF";
    public static final String DOCSTORE_OPERATION_WEBFORM = "WEBFORM";

    /* Starts here **** Added for creation of request xml which is used to ingest the record in document store *****/
    public static final String DOCSTORE_CATEGORY_BIB = "bibliographic";
    public static final String DOCSTORE_CATEGORY_WORK = "work";
    public static final String DOCSTORE_TYPE_BIB = "bibliographic";
    public static final String DOCSTORE_TYPE_ITEM = "item";
    public static final String DOCSTORE_TYPE_INSTANCE = "instance";
    public static final String DOCSTORE_FORMAT_MARC = "marc";
    public static final String DOCSTORE_FORMAT_OLEML = "oleml";

    public static final String BIB_MARC_XMLSTRING = "bibMarcXMLString";
    public static final String ITEM_MARC_XMLSTRING = "itemMarcXMLString";
    public static final String INSTANCE_MARC_XMLSTRING = "instanceMarcXMLString";

    public static final String DOCSTORE_REQUEST_XMLSTRING = "docStoreRequestXMLString";

    public static final String CDATA_START_TAG = "<![CDATA[";
    public static final String CDATA_END_TAG = "]]>";
    /* Ends here **** Added for creation of request xml which is used to ingest the record in document store *****/

    public static final String ITEM_TYPE_CODE_ITEM = "ITEM";

    public static final String CURRENCY_TYPE_ID = "currencyTypeId";
    public static final String CURRENCY_TYPE_NAME = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString("config.base.currency");
    public static final String VENDOR_HEADER_GENERATED_ID = "vendorHeaderGeneratedIdentifier";
    public static final String EXCHANGE_RATE_DATE = "exchangeRateDate";
    public static final String DISCOUNT_TYPE_PERCENTAGE = "%";
    public static final String DISCOUNT_TYPE_DOLLOR = "#";
    public static final String TITLE_ID = "titleId";
    public static final String INSTANCE_ID = "instanceId";
    public static final String DOC_CATEGORY_TYPE = "docCategoryType";
    public static final String DOC_CATEGORY_TYPE_BIBLINKS = "BibligraphicLinks";
    public static final String DOC_CATEGORY_TYPE_ITEMLINKS = "bibIdentifier";
    public static final String DOC_CATEGORY_TYPE_BIB = "bibliographic";
    public static final String DOC_CATEGORY_TYPE_ITEM = "item";
    public static final String DOC_CATEGORY_TYPE_INSTANCE = "bibIdentifier";

    public static final String BATCHINGEST_REQUEST = "BATCHINGEST";
    public static final String STAFF_REQUEST = "Library Material";
    public static final String WEBFORM_REQUEST = "WebformRequest";

    public static final String ITEM_SRC_TYPE_PUBLISHER = "Publisher";

    public static final String DEFAULT_VALUE_USERID = "userId";

    public static final String DEFAULT_VALUE_FOR = "defaultValueFor";

    public static final String DEFAULT_VALUE_ROLE_ID = "roleId";

    public static final String VENDOR_TRANSMISSION_FORMAT_EDI = "EDI";

    public static final String VENDOR_TRANSMISSION_FORMAT_PDF = "PDF";

    public static final String METHOD_OF_PO_TRANSMISSION_NOPR = "NOPR";

    public static final String DEFAULT_TABLE_COLUMN_DOCUMENTTYPE = "documentType";
    public static final String CLOSED="Closed";


    // added for OLE-2144 start
    public static final class DocStoreDetails {
        public static final String TITLE_KEY = "title";
        public static final String ISBN_KEY = "isbn";
        public static final String AUTHOR_KEY = "author";
        public static final String PUBLISHER_KEY = "publisher";
        public static final String PUBLICATIONDATE_KEY = "publicationDate";
        public static final String ITEMLINKS_KEY = "bibIdentifier";
        public static final String LOCAL_ID = "localIdentifier";

        public static final String TITLE_VALUE = "Title_search";
        public static final String AUTHOR_VALUE = "Author_search";
        public static final String ISBN_VALUE = "ISBN_display";
        public static final String PUBLISHER_VALUE = "Publisher_search";
        public static final String PUBLICATIONDATE_VALUE = "YearOfPublication";
        public static final String ITEMLINKS_VALUE = "bibIdentifier";
        public static final String LOC_ID_VALUE = "LocalId_search";

        public static final Map<String, String> getDocstoreQueryKeys() {
            Map<String, String> docStoreQueryKeys = new HashMap<String, String>();
            docStoreQueryKeys.put(TITLE_KEY, TITLE_VALUE);
            docStoreQueryKeys.put(AUTHOR_KEY, AUTHOR_VALUE);
            docStoreQueryKeys.put(ISBN_KEY, ISBN_VALUE);
            docStoreQueryKeys.put(PUBLISHER_KEY, PUBLISHER_VALUE);
            docStoreQueryKeys.put(LOCAL_ID, LOC_ID_VALUE);
            docStoreQueryKeys.put(PUBLICATIONDATE_KEY, PUBLICATIONDATE_VALUE);
            docStoreQueryKeys.put(ITEMLINKS_KEY, ITEMLINKS_VALUE);
            return Collections.unmodifiableMap(docStoreQueryKeys);
        }

        public static final Map<String, String> DOCSTORE_QUERY_KEYS = getDocstoreQueryKeys();
    }

    public static final String RECEIVING_QUEUE_SEARCH = "receivingQueuePo";
    public static final String FROM_DATE_CREATED = "fromDateCreated";
    public static final String TO_DATE_CREATED = "toDateCreated";
    public static final String PURCHASEORDER_STATUS_OPEN = "OPEN";
    public static final String ISBN = "isbn";
    public static final String NOTES_WARNING = "Notes warning:";
    public static final String NO_RECORDS_FOUND ="noRecordsfound";

    // added for OLE-2144 end

    //AcquisitionSearch
    public static final String PURCHASING_DOC_SEARCH = "Document Search";
    public static final String BIB_SEARCH = "Bib Search";

    public static class InvoiceSearch {
        public static final String PO_ID = "purchaseOrderIdentifier";
        public static final String PURAP_ID = "purapDocumentIdentifier";
        public static final String INV_NUMBER = "invoiceNumber";
        public static final String INV_ACCOUNT = "accountNumber";
        public static final String INV_CHART = "chartOfAccountsCode";
        public static final String INV_ORG = "organizationCode";
        public static final String INV_PAY_DATE="invoicePayDate";
        public static final String INV_TYP="invoiceType";
        public static final String INV_TYP_ID="invoiceTypeId";
        public static final String INV_DOC_NUM="documentNumber";
        public static final String INV_DATE="invoiceDate";
        public static final String INV_SUB_TYP="invoiceSubType";
        public static final String INV_SUB_TYP_ID="invoiceSubTypeId";
        public static final String INV_VND_NM="vendorName";
        public static final String INV_VND_NUM="vendorNumber";
        public static final String ITEM_TITLE_ID = "itemTitleId";
        public static final String PO_DOC_NUMS = "purchaseOrderDocumentNums";
        public static final String PO_DIS_INV_DT = "searchResultInvoiceDate";
        public static final String PO_DIS_INV_PAY_DT = "searchResultInvoicePayDate";
        public static final String ORG_DOC_NUMBER = "organizationDocumentNumber";

    }
    public static class AcquisitionsSearch {
        public static final String PO_ID = "purchaseOrderIdentifier";
        public static final String ITEM_TITLE_ID = "itemTitleId";
        public static final String ITEM_LOCAL_TITLE_ID = "localTitleId";
        public static final String DONOR_CODES = "donorSearchCodes";
        public static final String TITLE_ID = "titleId";
        public static final String searchType = "searchType";
        public static final String REQUISITIONS = "requisitions";
        public static final String CREATED_FROM = "dateFrom";
        public static final String CREATED_TO = "dateTo";
        public static final String ACQ_PO_NUMBER = "purapDocumentIdentifier";
        public static final String ACQ_ACCOUNT = "accountNumber";
        public static final String ACQ_CHART = "chartOfAccountsCode";
        public static final String ACQ_ORG = "organizationCode";
        public static final String INITIATOR = "initiator";
        public static final String REQUESTOR = "requestorName";
        public static final String ACQ_VND_NAME = "vendorName";
        public static final String ACQ_INT_REQID = "internalRequestorId";
        public static final String ACQ_EXT_REQID = "externalRequestorId";
        public static final String ACQ_TITLE = "title";
        public static final String ACQ_AUTHOR = "author";
        public static final String ACQ_PUBLISHER = "publisher";
        public static final String ACQ_ISBN = "isbn";
        public static final String ACQ_LOCAL_ID = "localIdentifier";
        public static final String ACQ_DOC_NUMBER = "documentNumber";
        public static final String APP_DOC_STATUS = "applicationDocumentStatus";
        public static final String APP_DOC_DESC = "documentDescription";
        public static final String APP_DOC_TYPE_CODE = "financialDocumentTypeCode";
        public static final String APP_DOC_NUM = "organizationDocumentNumber";

        public static final String ITM_EXT_REQID = "requestorId";

        public static final String DOCUMENT_TYPE_NAME = "docTypeFullName";
        public static final String documentType = "documentType";

        public static final Map<String, String> getRequisitionFieldNames() {
            Map<String, String> requisitionFields = new HashMap<String, String>();
            requisitionFields.put(ACQ_PO_NUMBER, ACQ_PO_NUMBER);
            requisitionFields.put(ACQ_VND_NAME, ACQ_VND_NAME);
            requisitionFields.put(ACQ_INT_REQID, ACQ_INT_REQID);
            requisitionFields.put(ACQ_EXT_REQID, ITM_EXT_REQID);
            requisitionFields.put(ACQ_ACCOUNT, ACQ_ACCOUNT);
            requisitionFields.put(ACQ_CHART, ACQ_CHART);
            requisitionFields.put(ACQ_ORG, ACQ_ORG);
            return Collections.unmodifiableMap(requisitionFields);
        }

        public static final Map<String, String> getPurchaseOrderNames() {
            Map<String, String> purchaseOrderFields = new HashMap<String, String>();
            purchaseOrderFields.put(ACQ_PO_NUMBER, ACQ_PO_NUMBER);
            purchaseOrderFields.put(ACQ_VND_NAME, ACQ_VND_NAME);
            purchaseOrderFields.put(ACQ_INT_REQID, ACQ_INT_REQID);
            purchaseOrderFields.put(ACQ_EXT_REQID, ITM_EXT_REQID);
            purchaseOrderFields.put(ACQ_ACCOUNT, ACQ_ACCOUNT);
            purchaseOrderFields.put(ACQ_CHART, ACQ_CHART);
            purchaseOrderFields.put(ACQ_ORG, ACQ_ORG);
            return Collections.unmodifiableMap(purchaseOrderFields);
        }

        public static final Map<String, String> getLineItemReceivingNames() {
            Map<String, String> lineItemReceivingFields = new HashMap<String, String>();
            lineItemReceivingFields.put(ACQ_PO_NUMBER, PO_ID);
            lineItemReceivingFields.put(ACQ_VND_NAME, ACQ_VND_NAME);
            return Collections.unmodifiableMap(lineItemReceivingFields);
        }

        public static final Map<String, String> getCorrectionReceivingNames() {
            Map<String, String> correctionReceivingFields = new HashMap<String, String>();
            correctionReceivingFields.put(ACQ_PO_NUMBER, PO_ID);
            return Collections.unmodifiableMap(correctionReceivingFields);
        }

        public static final Map<String, String> getPaymentRequestNames() {
            Map<String, String> paymentRequestFields = new HashMap<String, String>();
            paymentRequestFields.put(ACQ_PO_NUMBER, ACQ_PO_NUMBER);
            paymentRequestFields.put(ACQ_VND_NAME, ACQ_VND_NAME);
            paymentRequestFields.put(ACQ_ACCOUNT, ACQ_ACCOUNT);
            paymentRequestFields.put(ACQ_CHART, ACQ_CHART);
            paymentRequestFields.put(ACQ_ORG, ACQ_ORG);
            return Collections.unmodifiableMap(paymentRequestFields);
        }

        public static final Map<String, String> getDocStoreDetails() {
            Map<String, String> docStoreFields = new HashMap<String, String>();
            docStoreFields.put(ACQ_TITLE, ACQ_TITLE);
            docStoreFields.put(ACQ_AUTHOR, ACQ_AUTHOR);
            docStoreFields.put(ACQ_PUBLISHER, ACQ_PUBLISHER);
            docStoreFields.put(ACQ_LOCAL_ID, ACQ_LOCAL_ID);
            docStoreFields.put(ACQ_ISBN, ACQ_ISBN);
            return Collections.unmodifiableMap(docStoreFields);
        }

        public static final List<String> getResultNames() {
            List<String> resultFields = new ArrayList<String>();
            resultFields.add(APP_DOC_NUM);
            resultFields.add(APP_DOC_TYPE_CODE);
            resultFields.add(APP_DOC_DESC);
            //resultFields.add(APP_DOC_STATUS);
            resultFields.add(ACQ_PO_NUMBER);
            resultFields.add(ITEM_TITLE_ID);
            resultFields.add(PO_ID);
            return Collections.unmodifiableList(resultFields);
        }
        public static final Map<String, String> getInvoiceNames() {
            Map<String, String> invoiceFields = new HashMap<String, String>();
            invoiceFields.put(ACQ_VND_NAME, ACQ_VND_NAME);
            invoiceFields.put(ACQ_PO_NUMBER, ACQ_PO_NUMBER);
            invoiceFields.put(ACQ_VND_NAME, ACQ_VND_NAME);
            invoiceFields.put(ACQ_ACCOUNT, ACQ_ACCOUNT);
            invoiceFields.put(ACQ_CHART, ACQ_CHART);
            invoiceFields.put(ACQ_ORG, ACQ_ORG);
            return Collections.unmodifiableMap(invoiceFields);
        }

        public static final Map<String, String> REQUISITION_FIELDS = getRequisitionFieldNames();
        public static final Map<String, String> PURCHASEORDER_FIELDS = getPurchaseOrderNames();
        public static final Map<String, String> LINE_ITEM_RECEIVING_FIELDS = getLineItemReceivingNames();
        public static final Map<String, String> CORRECTION_RECEIVING_FIELDS = getCorrectionReceivingNames();
        public static final Map<String, String> PAYMENT_FIELDS = getPaymentRequestNames();
        public static final Map<String, String> DOC_STORE_FIELDS = getDocStoreDetails();
        public static final List<String> RESULT_FIELDS = getResultNames();
        public static final Map<String, String> INVOICE_FIELDS = getInvoiceNames();

    }

    //Invoice Section Ids
    public static final String PROCESS_ITEM_SECTION_ID = "OLEInvoiceView-processItems";
    public static final String PO_ITEM_SECTION_ID = "OleInvoiceView-POItems";
    public static final String INVOICE_INFO_SECTION_ID = "OLEInvoiceView-invoiceInfo";
    public static final String INVOICE_ITEM_SECTION_ID = "OleInvoiceView-invoiceItems";
    public static final String INVOICE_ADDITIONAL_ITEM_SECTION_ID="OLEInvoiceView-ProcessItem-AdditionalCharges";
    public static final String INVOICE_ADDITIONAL_CHARGE_SECTION_ID="OLEInvoiceView-AdditionalCharges-additionalItems";
    public static final String INVOICE_ITEMS_FUNDCODE="OLEInvoiceCurrentItemAccountingLine-VerticalBoxSection";
    public static final String ERROR_INVOICE_ITEMS_FUNDCODE_REQUIRED="error.invoice.item.fundcode.required";
    public static final String ERROR_INVOICE_ITEMS_FUNDCODE_INVALID="error.invoice.item.fundcode.invalid";


    //Added for Payment Request Validation
    public static final String FOREIGN_VENDOR_INVOICE_AMOUNT = "foreignVendorInvoiceAmount";
    public static final String FOREIGN_VENDOR_INVOICE_AMOUNT_STRING = "Foreign Vendor Invoice Amount";
    public static final String ERROR_ITEM_QUANTITY_REQUIRED = "error.item.qty.required";
    public static final String ERROR_INVALID_ACC_NO = "error.inv.accno";
    public static final String ERROR_REQUIRED = "error.item.description.required";
    public static final String ERROR_NO_OF_PARTS_REQUIRED = "error.item.noOfParts.required";
    public static final String ERROR_ATLEAST_ONE_ITEM_QTY_REQUIRED = "error.atleast.one.item.qty.required";
    public static final String ERROR_SUBSCIPTION_FROM_DATE_REQUIRED = "error.subscription.from.date.required";
    public static final String ERROR_SUBSCIPTION_TO_DATE_REQUIRED = "error.subscription.to.date.required";
    public static final String ERROR_SUBSCIPTION_FROM_DATE_GREATER_THAN_TO_DATE = "error.subscription.from.date.greater.than.to.date";
    public static final String ERROR_PO_INVALID_FORMAT = "error.po.invalid.format";

    //Added for Requestor Phone Number Validation
    public static final String REQUESTOR_PERSON_PHONE_NUMBER = "requestorPersonPhoneNumber";
    public static final String ERROR_REQUESTOR_PHONE_NUMBER = "error.requestor.phone.number";
    public static final String PURCHASE_ORDER_PERSON_PHONE_NUMBER = "requestorPersonPhoneNumber";

    //Added for Requestor Notes
    public static final String REQUESTOR_NOTES_PRE_ORDER_SERVICE = "Requestor Note";

    // accounting line percent jira OLE-2112.
    public static final Integer ACCOUNTINGLINE_PERCENT_HUNDRED = 100;
    // changes for jira OLE-2354
    public static final Integer ONE = 1;
    public static final Integer ZERO = 0;

    public static final String OLE_DOCSEARCH_URL = "ole.docsearch.url";

    public static final String OLE_DOCSTOREAPP_URL = "ole.docstoreapp.url";

    public static final String OLE_DOCSTORE_URL = "ole.docstore.url";

    public static final String DOCUMENT = "document";

    public static final String FAILURE = "failure";

    public static final String DOCSTORE_URL_PING = "docStoreUrlPing";

    public static final String ACCOUNT_TEMPORARY_RESTRICTED_CODE = "T";

    public static final String ACCOUNT_RESTRICTED_CODE = "R";

    public static final String RESTRICTED = "restricted";

    // added for jira OLE-2853
    public static final String ITEMTITLEID = "itemTitleId";

    //Added for jira OLE-
    public static final String OLE_PERSON = "PERSON";
    public static final String OLE_SYSTEM = "SYSTEM";
    public static final int PERSON_ENTITY_TYP_CODE_SIZE = 4;

    //Added for Jira OLE-3060
    public static final String RICE2_URL = "ole.rice2.url.portal";

    public static final String VENDOR_DEPOSIT_ACCOUNT = "vendorDepositAccount";
    public static final String SUBFUND_GROUP_CODE = "subFundGroupCode";
    public static final String VENDOR_NAME = "vendorName";
    public static final String INCLUDE = "Include";
    public static final String EXCLUDE = "Exclude";

    // Added for jira OLE-3498
    public static final String SUB_FUND_GRP_CD = "SUB_FUND_GRP_CD";
    public static final String FUND_GRP_CD = "FUND_GRP_CD";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final String PERCENT = "accountLinePercent";
    public static final String CHART_OF_ACCOUNTS_CODE = "chartOfAccountsCode";


    //Added for the jira OLE-3508
    public static final String ERROR_DI_ACCOUNTING_TOTAL = "errors.di.accounting.total";

    //Added for the jira OLE-3509
    public static final String ERROR_UNIT_PRICE_REQUIRED = "error.unit.price.required";

    public static final String REQUISITION = "Requisition";

    public static final String PURCHASE_ORDER = "Purchase Order";

    public static final String PAYMENT_REQUEST = "Payment Request";
    public static final BigDecimal MAX_PERCENT = new BigDecimal(100);
    public static final BigDecimal ZERO_PERCENT = new BigDecimal(0);
    public static final String DUPLICATE_INVOICE = "One or more potential duplicate invoices have been identified: " ;
    public static final String QUES_FOR_DUPLICATE_INVOICE_FOR_SAVE = "Do you want to save this INV anyway?";
    public static final String QUES_FOR_DUPLICATE_INVOICE_FOR_APPROVE = "Do you want to approve this INV anyway?";
    public static final String QUES_FOR_DUPLICATE_INVOICE_FOR_SUBMIT = "Do you want to submit this INV anyway?";
    public static final String BUDGET_RECORDING_LEVEL_CODE = "O";
    public static final String FILE_UPLOAD_SECTION = "FiscalYearRolloverFileUpload";
    public static final String FISCAL_YR_ROLL_VIEW = "OLEFiscalYearRolloverView";
    public static final String POBA_VIEW = "OLEPurchaseOrderBatchView";
    public static final String PRORATE_OPTION_REQ = "error.prorate.option.required";

    public static class EncumberReportConstant{
        public static final String DONORCODE="donorCode";
        public static final String FROM_DATE="fromDate";
        public static final String TO_DATE="toDate";
        public static final String DONORID="DONORID";
        public static final String DONOR_CODE="DONOR_CODE";
        public static final String PONUM ="PONUM";
        public static final String DOCID ="DOCID";
        public static final String DONOR_NOTE="DONOR_NOTE";
        public static final String DONOR_AMT="DONOR_AMT";
        public static final String ENCUMBERAMT="ENCUMBERAMT";
        public static final String EXPENSEDAMT="EXPENSEDAMT";
    }
}