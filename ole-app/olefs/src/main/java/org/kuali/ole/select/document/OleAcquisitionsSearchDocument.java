/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License,  Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing,  software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,  either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.internal.compiler.flow.InsideSubRoutineFlowContext;
import org.joda.time.DateTime;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleAcquisitionSearchResult;
import org.kuali.ole.select.document.service.OleAcquisitionSearchService;
import org.kuali.ole.select.service.impl.BatchLoadServiceImpl;
import org.kuali.ole.select.service.impl.OleDocStoreSearchService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class is the document Class for Acquisition Search
 */
public class OleAcquisitionsSearchDocument extends TransactionalDocumentBase {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleAcquisitionsSearchDocument.class);

    /**
     * Stores the value of documentType.
     */
    private String documentType;

    /**
     * Stores the value of initiator of the document.
     */
    private String initiator;

    /**
     * Stores the value of title.
     */
    private String title;

    /**
     * Stores the value of author.
     */
    private String author;

    /**
     * Stores the value of publisher.
     */
    private String publisher;

    /**
     * Stores the value of isbn.
     */
    private String isbn;

    private String localIdentifier;

    /**
     * Stores the value of document Number.
     */
    private String docNumber;

    /**
     * Stores the value of requestorName.
     */
    private String requestorName;

    /**
     * Stores the value of internalRequestorId.
     */
    private String internalRequestorId;

    /**
     * Stores the value of externalRequestorId.
     */
    private String externalRequestorId;

    /**
     * Stores the value of vendorName.
     */
    private String vendorName;

    /**
     * Stores the value of Starting of creation Date.
     */
    private String dateFrom;

    /**
     * Stores the value of Creation Date Ending.
     */
    private String dateTo;

    /**
     * Stores the value of purapDocumentIdentifier.
     */
    private String purapDocumentIdentifier;

    /**
     * Stores the value of accountNumber.
     */
    private String accountNumber;

    /**
     * Stores the value of organizationCode.
     */
    private String organizationCode;

    /**
     * Stores the value of chartOfAccountsCode.
     */
    private String chartOfAccountsCode;

    /**
     * Stores the value of searchType.
     */
    private boolean searchType;

   /* private List<DocData> docDataList = new ArrayList<DocData>();

    public List<DocData> getDocDataList() {
        return docDataList;
    }

    public void setDocDataList(List<DocData> docDataList) {
        this.docDataList = docDataList;
    }*/
    private List<Bib> docDataList = new ArrayList<Bib>();

    private List<String> docTypeNames = new ArrayList<String>();


    private List<OleAcquisitionSearchResult> acqSearchResults = new ArrayList<OleAcquisitionSearchResult>();

    private transient OleAcquisitionSearchService oleAcquisitionSearchService;

    private transient DateTimeService dateTimeService;

    private transient OleDocStoreSearchService oleDocStoreSearchService;

    private transient ConfigurationService ConfigurationService;

    public OleAcquisitionSearchService getOleAcquisitionSearchService() {
        if (oleAcquisitionSearchService == null) {
            oleAcquisitionSearchService = SpringContext.getBean(OleAcquisitionSearchService.class);
        }
        return oleAcquisitionSearchService;
    }

    public DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            dateTimeService = SpringContext.getBean(DateTimeService.class);
        }
        return dateTimeService;
    }
    public List<Bib> getDocDataList() {
        return docDataList;
    }

    public void setDocDataList(List<Bib> docDataList) {
        this.docDataList = docDataList;
    }
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }
    public OleDocStoreSearchService getOleDocStoreSearchService() {
        if (oleDocStoreSearchService == null) {
            oleDocStoreSearchService = SpringContext.getBean(OleDocStoreSearchService.class);
        }
        return oleDocStoreSearchService;
    }

    public ConfigurationService getConfigurationService() {
        if (ConfigurationService == null) {
            ConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return ConfigurationService;
    }

    public OleAcquisitionsSearchDocument() {
        super();
        List<String> docTypes = new ArrayList<String>();
        docTypes.add(PurapConstants.REQUISITION_DOCUMENT_TYPE);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_CLOSE_DOCUMENT);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_RETRANSMIT_DOCUMENT);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PRINT_DOCUMENT);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PAYMENT_HOLD_DOCUMENT);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REMOVE_HOLD_DOCUMENT);
        docTypes.add(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_SPLIT_DOCUMENT);
        docTypes.add(OLEConstants.FinancialDocumentTypeCodes.LINE_ITEM_RECEIVING);
        docTypes.add(OLEConstants.FinancialDocumentTypeCodes.CORRECTION_RECEIVING);
        docTypes.add(PurapConstants.PurapDocTypeCodes.PAYMENT_REQUEST_DOCUMENT);
        docTypes.add(PurapConstants.PurapDocTypeCodes.INVOICE_DOCUMENT);
        this.docTypeNames = docTypes;
    }

    /**
     * Gets the Initiator attribute.
     *
     * @return Returns the Initiator.
     */
    public String getInitiator() {
        return initiator;
    }

    /**
     * Sets the Initiator attribute value.
     *
     * @param initiator
     */
    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    /**
     * Getter for property: title.<br/>
     * Stores the value of title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for property: title.<br/>
     * Stores the value of title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for property: author.<br/>
     * Stores the value of author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter for property: author.<br/>
     * Stores the value of author.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter for property: publisher.<br/>
     * Stores the value of publisher.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Setter for property: publisher.<br/>
     * Stores the value of publisher.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Getter for property: isxn.<br/>
     * Stores the value of isxn.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Setter for property: isbn.<br/>
     * Stores the value of isbn.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLocalIdentifier() {
        return localIdentifier;
    }

    public void setLocalIdentifier(String localIdentifier) {
        this.localIdentifier = localIdentifier;
    }

    /**
     * Getter for property: docNumber.<br/>
     * Stores the value of document Number.
     */
    public String getDocNumber() {
        return docNumber;
    }

    /**
     * Setter for property: docNumber.<br/>
     * Stores the value of document Number.
     */
    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * Getter for property: requestorName.<br/>
     * Stores the value of requestorName.
     */
    public String getRequestorName() {
        return requestorName;
    }

    /**
     * Setter for property: requestorName.<br/>
     * Stores the value of requestorName.
     */
    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    /**
     * Getter for property: vendorName.<br/>
     * Stores the value of vendorName.
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * Setter for property: vendorName.<br/>
     * Stores the value of vendorName.
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * Getter for property: dateFrom.<br/>
     * Stores the value of Starting of creation Date.
     */
    public String getDateFrom() {
        return dateFrom;
    }

    /**
     * Setter for property: dateFrom.<br/>
     * Stores the value of Starting of creation Date.
     */
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * Getter for property: dateTo.<br/>
     * Stores the value of Creation Date Ending.
     */
    public String getDateTo() {
        return dateTo;
    }

    /**
     * Setter for property: dateTo.<br/>
     * Stores the value of Creation Date Ending.
     */
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * Getter for property: purapDocumentIdentifier.<br/>
     * Stores the value of purapDocumentIdentifier.
     */
    public String getPurapDocumentIdentifier() {
        return purapDocumentIdentifier;
    }

    /**
     * Setter for property: purapDocumentIdentifier.<br/>
     * Stores the value of purapDocumentIdentifier.
     */
    public void setPurapDocumentIdentifier(String purapDocumentIdentifier) {
        this.purapDocumentIdentifier = purapDocumentIdentifier;
    }

    /**
     * Getter for property: accountNumber.<br/>
     * Stores the value of accountNumber.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Setter for property: accountNumber.<br/>
     * Stores the value of accountNumber.
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Getter for property: organizationCode.<br/>
     * Stores the value of organizationCode.
     */
    public String getOrganizationCode() {
        return organizationCode;
    }

    /**
     * Setter for property: organizationCode.<br/>
     * Stores the value of organizationCode.
     */
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    /**
     * Getter for property: chartOfAccountsCode.<br/>
     * Stores the value of chartOfAccountsCode.
     */
    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    /**
     * Setter for property: chartOfAccountsCode.<br/>
     * Stores the value of chartOfAccountsCode.
     */
    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }

    /**
     * Getter for property: searchType.<br/>
     * Stores the value of searchType.
     */
    public boolean getSearchType() {
        return searchType;
    }

    /**
     * Setter for property: searchType.<br/>
     * Stores the value of searchType.
     */
    public void setSearchType(boolean searchType) {
        this.searchType = searchType;
    }

    /**
     * Getter for property: internalRequestorId.<br/>
     * Stores the value of internalRequestorId.
     */
    public String getInternalRequestorId() {
        return internalRequestorId;
    }

    /**
     * Setter for property: internalRequestorId.<br/>
     * Stores the value of internalRequestorId.
     */
    public void setInternalRequestorId(String internalRequestorId) {
        this.internalRequestorId = internalRequestorId;
    }

    /**
     * Getter for property: initiator.<br/>
     * Stores the value of externalRequestorId.
     */
    public String getExternalRequestorId() {
        return externalRequestorId;
    }

    /**
     * Setter for property: initiator.<br/>
     * Stores the value of externalRequestorId.
     */
    public void setExternalRequestorId(String externalRequestorId) {
        this.externalRequestorId = externalRequestorId;
    }

    /**
     * Getter for property: documentType.<br/>
     * Stores the value of documentType.
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Setter for property: documentType.<br/>
     * Stores the value of documentType.
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * Getter for property: docDataList.<br/>
     * Stores the value of list of docData.
     */

    /**
     * @return map
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper ()
     */
    @SuppressWarnings("rawtypes")

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap map = new LinkedHashMap();
        return map;
    }

    /**
     * Getter for property: acqSearchResults.<br/>
     * Stores the value of list of oleAcquisitionSearchResult.
     */
    public List<OleAcquisitionSearchResult> getAcqSearchResults() {
        return acqSearchResults;
    }

    /**
     * Setter for property: acqSearchResults.<br/>
     * Stores the value of list of OleAcquisitionSearchResult.
     */
    public void setAcqSearchResults(List<OleAcquisitionSearchResult> acqSearchResults) {
        this.acqSearchResults = acqSearchResults;
    }

    /**
     * Depends on the document type this will check for Accounting details which are given
     * if that document doesn't contain correct accounting details this will return false
     *
     * @param docType
     * @return isValid(boolean)
     */
    private boolean checkForAccountingDetails(String docType) {
        LOG.debug("Inside checkForAccountingDetails of OleAcquisitionsSearchDocument");
        boolean isValid = true;
        if (StringUtils.isNotEmpty(docType)) {
            if (docType.equalsIgnoreCase(PurapConstants.PurapDocTypeCodes.PAYMENT_REQUEST_DOCUMENT)) {
                if (StringUtils.isNotEmpty(getRequestorName())
                        || StringUtils.isNotEmpty(getOrganizationCode())) {
                    isValid = false;
                }
            } else if (docType.equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.LINE_ITEM_RECEIVING)) {
                if (StringUtils.isNotEmpty(getAccountNumber())
                        || StringUtils.isNotEmpty(getChartOfAccountsCode())
                        || StringUtils.isNotEmpty(getOrganizationCode())
                        || StringUtils.isNotEmpty(getRequestorName())) {
                    isValid = false;
                }
            } else if (docType.equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.CORRECTION_RECEIVING)) {
                if (StringUtils.isNotEmpty(getAccountNumber())
                        || StringUtils.isNotEmpty(getChartOfAccountsCode())
                        || StringUtils.isNotEmpty(getOrganizationCode())
                        || StringUtils.isNotEmpty(getRequestorName())
                        || StringUtils.isNotEmpty(getVendorName())) {
                    isValid = false;
                }
            }
        }
        LOG.debug("Leaving checkForAccountingDetails of OleAcquisitionsSearchDocument");
        return isValid;
    }

    /**
     * This method populates a map search criteria that are entered
     *
     * @return Map containing Item fieldname and fieldvalues
     */
    public Map<String, List<String>> getSearchCriteria(Map<String, String> itemFields) throws Exception {
        LOG.debug("Inside populateRequisitionFields of OleAcquisitionsSearchDocument");
        Map<String, List<String>> searchCriteriaMap = new HashMap<String, List<String>>();
        DocumentEntry documentEntry = SpringContext.getBean(DataDictionaryService.class).getDataDictionary().getDocumentEntry(OleAcquisitionsSearchDocument.class.getName());

        Set attributeNames = documentEntry.getAttributeNames();
        for (Object attributeName : attributeNames) {
            String attr = (String) attributeName;
            if ((OleSelectConstant.AcquisitionsSearch.ACQ_PO_NUMBER.equals(attr)
                    || OleSelectConstant.AcquisitionsSearch.ACQ_ACCOUNT.equals(attr)
                    || OleSelectConstant.AcquisitionsSearch.ACQ_CHART.equals(attr)
                    || OleSelectConstant.AcquisitionsSearch.ACQ_VND_NAME.equals(attr)
                    || OleSelectConstant.AcquisitionsSearch.ACQ_ORG.equals(attr))) {
                String attrValue = (String) PropertyUtils.getProperty(this, attr);
                attr = itemFields.get(attr);
                if (StringUtils.isNotEmpty(attr) && StringUtils.isNotEmpty(attrValue)) {
                    searchCriteriaMap.put(attr, new ArrayList<String>(Arrays.asList(attrValue)));
                    /*else
                        searchCriteriaMap.put(attr, new String[]{""});*/
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Search Criteria Size in OleAcquisitionsSearchDocument.getSearchCriteria - " + searchCriteriaMap.size());
            }
        }

        LOG.debug("Leaving populateRequisitionFields of OleAcquisitionsSearchDocument");
        return searchCriteriaMap;
    }

    /**
     * This method populates a map bib search criteria that are entered
     *
     * @return bibSearchCriteriaMap
     */
   /* public Map<String, String> getBibSearchCriteria() throws Exception {
        Map<String, String> bibSearchCriteriaMap = new HashMap<String, String>();
        DocumentEntry documentEntry = SpringContext.getBean(DataDictionaryService.class).getDataDictionary().getDocumentEntry(OleAcquisitionsSearchDocument.class.getName());

        Set attributeNames = documentEntry.getAttributeNames();
        for (Object attributeName : attributeNames) {
            String attr = (String) attributeName;
            if ((OleSelectConstant.AcquisitionsSearch.ACQ_TITLE.equals(attr)
                    || OleSelectConstant.AcquisitionsSearch.ACQ_AUTHOR.equals(attr)
                    || OleSelectConstant.AcquisitionsSearch.ACQ_PUBLISHER.equals(attr)
                    || OleSelectConstant.AcquisitionsSearch.ACQ_ISBN.equals(attr)
                    || OleSelectConstant.AcquisitionsSearch.ACQ_LOCAL_ID.equals(attr))) {
                String attrValue = (String) PropertyUtils.getProperty(this, attr);
                attr = OleSelectConstant.AcquisitionsSearch.DOC_STORE_FIELDS.get(attr);
                if (StringUtils.isNotEmpty(attr) && StringUtils.isNotEmpty(attrValue)) {
                    bibSearchCriteriaMap.put(attr, attrValue);
                }
            }
        }
        return bibSearchCriteriaMap;
    }*/
    public SearchParams getBibSearchCriteria() throws Exception {
        DocumentEntry documentEntry = SpringContext.getBean(DataDictionaryService.class).getDataDictionary().getDocumentEntry(OleAcquisitionsSearchDocument.class.getName());
        SearchParams searchParams=new SearchParams();
        searchParams.setPageSize(Integer.parseInt("1000"));
        Set attributeNames = documentEntry.getAttributeNames();
        for (Object attributeName : attributeNames) {
            String attr = (String) attributeName;
            if ((Bib.TITLE.equals(attr.toUpperCase())
                    || Bib.AUTHOR.equals(attr.toUpperCase())
                    || Bib.PUBLISHER.equals(attr.toUpperCase())
                    || Bib.ISBN.equals(attr.toUpperCase())
                    || OleSelectConstant.AcquisitionsSearch.ACQ_LOCAL_ID.equals(attr))) {
                String attrValue = (String) PropertyUtils.getProperty(this, attr);
                attr = OleSelectConstant.AcquisitionsSearch.DOC_STORE_FIELDS.get(attr);
                if(attr.equalsIgnoreCase("localIdentifier")){
                    attr="LocalId_search";
                }
                else{
                    attr=attr.toUpperCase();
                }
                if (StringUtils.isNotEmpty(attr) && StringUtils.isNotEmpty(attrValue)) {
                    if (attrValue.contains("-")) {
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase",searchParams.buildSearchField(OleSelectConstant.DOCSTORE_TYPE_BIB,attr,attrValue),"AND"));
                    } else {
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND",searchParams.buildSearchField(OleSelectConstant.DOCSTORE_TYPE_BIB,attr,attrValue),"AND"));
                    }
                    searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(OleSelectConstant.DOCSTORE_TYPE_BIB,"id"));
                }
            }
        }
        LOG.debug("Inside getBibSearchCriteria" );
  return searchParams;
    }
    /**
     * This method populates a map for the search criteria's entered
     * and gets the final result
     *
     * @param resultRows
     * @return documentNumbers
     */
    public List<OleAcquisitionSearchResult> populateCriteriaAndSearch(String docTypeFullName, Map<String, List<String>> fixedParameters)
            throws WorkflowException, ParseException {
        LOG.debug("Inside populateCriteriaAndSearch of OleAcquisitionsSearchDocument");
        DocumentSearchCriteria.Builder docSearchCriteria = DocumentSearchCriteria.Builder.create();

        //fixedParameters.put("docTypeFullName", new String[]{docTypeFullName});
        docSearchCriteria.setDocumentTypeName(docTypeFullName);
        if (StringUtils.isNotEmpty(this.dateFrom)) {
            docSearchCriteria.setDateCreatedFrom(new DateTime(getDateTimeService().convertToDate(dateFrom)));
        } else {

            // Condition Added by Aditya for Jira OLE-2563
            if (!isAnyDocFilterValue() && !isAnyBibFilterValues()) {
                DateTimeService dateTimeService = getDateTimeService();
                DateTime currentDate = new DateTime(dateTimeService.getCurrentSqlDate().getTime());
                if (this.getDocumentType() == null) {
                    docSearchCriteria.setDateCreatedFrom(currentDate);

                }
            }
            //setDateFrom(currentDate);
        }
        if (StringUtils.isNotEmpty(dateTo)) {
            docSearchCriteria.setDateCreatedTo(new DateTime(getDateTimeService().convertToDate(dateTo)));
        }
        if (StringUtils.isNotEmpty(initiator)) {
            docSearchCriteria.setInitiatorPrincipalName(initiator);
        }
        if (StringUtils.isNotEmpty(docNumber)) {
            docSearchCriteria.setDocumentId(docNumber);
        }
        if (getSearchType()) {
            fixedParameters.put("displayType", Arrays.asList("document"));
        }

        List<OleAcquisitionSearchResult> resultRows = new ArrayList<OleAcquisitionSearchResult>();
        resultRows = getOleAcquisitionSearchService().performDocumentSearch(docSearchCriteria.build(), fixedParameters);

        LOG.debug("Leaving populateCriteriaAndSearch of OleAcquisitionsSearchDocument");
        return resultRows;
    }

    /**
     * This method checks whether any search criteria present or not
     *
     * @return isPresent
     */
    private boolean isAnyDocFilterValue() {
        boolean isPresent = false;
        if (StringUtils.isNotEmpty(getDocumentType())
                || StringUtils.isNotEmpty(getAccountNumber())
                || StringUtils.isNotEmpty(getChartOfAccountsCode())
                || StringUtils.isNotEmpty(getOrganizationCode())
                || StringUtils.isNotEmpty(getRequestorName())
                || StringUtils.isNotEmpty(getVendorName())
                || StringUtils.isNotEmpty(getDateFrom())
                || StringUtils.isNotEmpty(getDateTo())
                || StringUtils.isNotEmpty(getPurapDocumentIdentifier())
                || StringUtils.isNotEmpty(getDocNumber())
                || StringUtils.isNotEmpty(getInitiator())) {
            isPresent = true;
        }
        return isPresent;

    }

    /**
     * This method checks for Bibliographic search criteria's
     *
     * @return boolean
     */
    private boolean isAnyBibFilterValues() {
        boolean isValid = false;
        if (StringUtils.isNotEmpty(getTitle())
                || StringUtils.isNotEmpty(getAuthor())
                || StringUtils.isNotEmpty(getIsbn())
                || StringUtils.isNotEmpty(getLocalIdentifier())
                || StringUtils.isNotEmpty(getPublisher())) {
            isValid = true;
        }
        return isValid;

    }


    /**
     * checks for the existing documentTypes
     *
     * @return boolean
     */
    private boolean validateDocumentType() {
        if (docTypeNames.contains(documentType.toUpperCase())) {
            return true;
        }
        return false;
    }

    /**
     * checks the documentType and search criteria's and depending on that the search methods will be called
     * and a final Searched Results will be returned
     *
     * @return results (OleAcquisitionsSearchAttributes)
     */
    public List<OleAcquisitionSearchResult> searchResults() {
        LOG.debug("Inside SearchResults of OleAcquisitionsSearchDocument");
        List<OleAcquisitionSearchResult> resultRows = new ArrayList<OleAcquisitionSearchResult>();
        if (!isAnyDocFilterValue() && !isAnyBibFilterValues()) {
            DateTimeService dateTimeService = getDateTimeService();
            String currentDate = dateTimeService.toDateString(dateTimeService.getCurrentSqlDate());
            if (!(this.getDocumentType() != null && this.getDateFrom() == null)) {
                setDateFrom(currentDate);
            }
        }

        try {
            if (checkForAccountingDetails(getDocumentType())) {
                List<String> titleList;
                if (isAnyBibFilterValues()) {
                    //docDataList = getOleDocStoreSearchService().getDocResult(getBibSearchCriteria());
                    SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(getBibSearchCriteria());
                    List<SearchResult> searchResults=searchResponse.getSearchResults();
                    Map<String, List<String>> searchCriterias = new HashMap<String, List<String>>();
                    List<String> titleIds = getOleAcquisitionSearchService().getTitleIds(searchResults);
                    if (titleIds.size() > 0) {
                        if (StringUtils.isNotEmpty(getDocumentType()) && validateDocumentType()) {
                            if ((documentType.equalsIgnoreCase(PurapConstants.REQUISITION_DOCUMENT_TYPE))) {
                                searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID,
                                        titleIds);
                                searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.REQUISITION_FIELDS));
                                resultRows.addAll(populateCriteriaAndSearch(documentType.toUpperCase(), searchCriterias));
                            } else if ((documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT))
                                    | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT))
                                    | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT))
                                    | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_CLOSE_DOCUMENT))
                                    | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_RETRANSMIT_DOCUMENT))
                                    | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PRINT_DOCUMENT))
                                    | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT))
                                    | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PAYMENT_HOLD_DOCUMENT))
                                    | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REMOVE_HOLD_DOCUMENT))
                                    | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_SPLIT_DOCUMENT))) {
                                searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                                searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.PURCHASEORDER_FIELDS));
                                resultRows.addAll(populateCriteriaAndSearch(documentType.toUpperCase(), searchCriterias));
                            } else if (documentType.equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.LINE_ITEM_RECEIVING)) {
                                searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                                searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.LINE_ITEM_RECEIVING_FIELDS));
                                resultRows.addAll(populateCriteriaAndSearch(documentType.toUpperCase(), searchCriterias));
                            } else if (documentType.equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.CORRECTION_RECEIVING)) {
                                searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                                searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.CORRECTION_RECEIVING_FIELDS));
                                resultRows.addAll(populateCriteriaAndSearch(documentType.toUpperCase(), searchCriterias));
                            } else if (documentType.equalsIgnoreCase(PurapConstants.PurapDocTypeCodes.INVOICE_DOCUMENT)) {
                                searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                                searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.INVOICE_FIELDS));
                                resultRows.addAll(populateCriteriaAndSearch(documentType.toUpperCase(), searchCriterias));
                            } else {
                                searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                                searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.PAYMENT_FIELDS));
                                resultRows.addAll(populateCriteriaAndSearch(documentType.toUpperCase(), searchCriterias));
                            }
                        } else if (StringUtils.isEmpty(getDocumentType())) {
                            searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                            searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.REQUISITION_FIELDS));
                            resultRows.addAll(populateCriteriaAndSearch(PurapConstants.REQUISITION_DOCUMENT_TYPE, searchCriterias));

                            searchCriterias.clear();
                            searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                            searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.PURCHASEORDER_FIELDS));
                            resultRows.addAll(populateCriteriaAndSearch(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT,
                                    searchCriterias));
                            searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                            searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.INVOICE_FIELDS));
                            /*resultRows.addAll(populateCriteriaAndSearch(documentType.toUpperCase(), searchCriterias));*/
                            if (StringUtils.isEmpty(getRequestorName())) {
                                searchCriterias.clear();
                                searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                                searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.LINE_ITEM_RECEIVING_FIELDS));
                                resultRows.addAll(populateCriteriaAndSearch(OLEConstants.FinancialDocumentTypeCodes.LINE_ITEM_RECEIVING,
                                        searchCriterias));

                                searchCriterias.clear();
                                searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                                searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.CORRECTION_RECEIVING_FIELDS));
                                resultRows.addAll(populateCriteriaAndSearch(OLEConstants.FinancialDocumentTypeCodes.CORRECTION_RECEIVING,
                                        searchCriterias));

                                searchCriterias.clear();
                                searchCriterias.put(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID, titleIds);
                                searchCriterias.putAll(getSearchCriteria(OleSelectConstant.AcquisitionsSearch.PAYMENT_FIELDS));
                                resultRows.addAll(populateCriteriaAndSearch(PurapConstants.PurapDocTypeCodes.PAYMENT_REQUEST_DOCUMENT,
                                        searchCriterias));
                            }

                        } else {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.AcquisitionsSearch.REQUISITIONS,
                                    OLEKeyConstants.ERROR_NO_DOC_TYPE_FOUND, new String[]{});
                        }
                    }
                } else {
                    if (StringUtils.isNotEmpty(getDocumentType()) && validateDocumentType()) {
                        if ((documentType.equalsIgnoreCase(PurapConstants.REQUISITION_DOCUMENT_TYPE))) {
                            resultRows.addAll(populateCriteriaAndSearch(PurapConstants.REQUISITION_DOCUMENT_TYPE,
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.REQUISITION_FIELDS)));
                        } else if ((documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT))
                                | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_AMENDMENT_DOCUMENT))
                                | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REOPEN_DOCUMENT))
                                | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_CLOSE_DOCUMENT))
                                | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_RETRANSMIT_DOCUMENT))
                                | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PRINT_DOCUMENT))
                                | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_VOID_DOCUMENT))
                                | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_PAYMENT_HOLD_DOCUMENT))
                                | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_REMOVE_HOLD_DOCUMENT))
                                | (documentType.equalsIgnoreCase(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_SPLIT_DOCUMENT))) {
                            resultRows.addAll(populateCriteriaAndSearch(documentType.toUpperCase(),
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.PURCHASEORDER_FIELDS)));
                        } else if (documentType.equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.LINE_ITEM_RECEIVING)) {
                            resultRows.addAll(populateCriteriaAndSearch(OLEConstants.FinancialDocumentTypeCodes.LINE_ITEM_RECEIVING,
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.LINE_ITEM_RECEIVING_FIELDS)));
                        } else if (documentType.equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.CORRECTION_RECEIVING)) {
                            resultRows.addAll(populateCriteriaAndSearch(OLEConstants.FinancialDocumentTypeCodes.CORRECTION_RECEIVING,
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.CORRECTION_RECEIVING_FIELDS)));
                        } else if (documentType.equalsIgnoreCase(PurapConstants.PurapDocTypeCodes.PAYMENT_REQUEST_DOCUMENT)) {
                            resultRows.addAll(populateCriteriaAndSearch(PurapConstants.PurapDocTypeCodes.PAYMENT_REQUEST_DOCUMENT,
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.PAYMENT_FIELDS)));
                        }else if (documentType.equalsIgnoreCase(PurapConstants.PurapDocTypeCodes.INVOICE_DOCUMENT)) {
                            resultRows.addAll(populateCriteriaAndSearch(PurapConstants.PurapDocTypeCodes.INVOICE_DOCUMENT,
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.INVOICE_FIELDS)));
                        }

                    } else if (StringUtils.isEmpty(getDocumentType())) {
                        //searchCriteriaFlag=true;
                        resultRows.addAll(populateCriteriaAndSearch(PurapConstants.REQUISITION_DOCUMENT_TYPE,
                                getSearchCriteria(OleSelectConstant.AcquisitionsSearch.REQUISITION_FIELDS)));
                        resultRows.addAll(populateCriteriaAndSearch(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT,
                                getSearchCriteria(OleSelectConstant.AcquisitionsSearch.PURCHASEORDER_FIELDS)));
                        resultRows.addAll(populateCriteriaAndSearch(PurapConstants.PurapDocTypeCodes.INVOICE_DOCUMENT,
                                getSearchCriteria(OleSelectConstant.AcquisitionsSearch.INVOICE_FIELDS)));
                        if (StringUtils.isEmpty(getRequestorName())) {
                            resultRows.addAll(populateCriteriaAndSearch(OLEConstants.FinancialDocumentTypeCodes.LINE_ITEM_RECEIVING,
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.LINE_ITEM_RECEIVING_FIELDS)));
                            resultRows.addAll(populateCriteriaAndSearch(OLEConstants.FinancialDocumentTypeCodes.CORRECTION_RECEIVING,
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.CORRECTION_RECEIVING_FIELDS)));
                            resultRows.addAll(populateCriteriaAndSearch(PurapConstants.PurapDocTypeCodes.PAYMENT_REQUEST_DOCUMENT,
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.PAYMENT_FIELDS)));
                        }
                    } else {
                        GlobalVariables.getMessageMap().putError(OleSelectConstant.AcquisitionsSearch.REQUISITIONS,
                                OLEKeyConstants.ERROR_NO_DOC_TYPE_FOUND, new String[]{});
                    }

                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while attempting to Search the Document: " + ex);
            throw new RuntimeException(ex);
        }
        LOG.debug("Leaving SearchResults of OleAcquisitionsSearchDocument");

        if (getConfigurationService().getPropertyValueAsString("dateFrom").equalsIgnoreCase(this.getDateFrom())) {
            this.dateFrom = null;
        }

        return resultRows;
    }


    public List<OleAcquisitionSearchResult> listOfPOsSearchResults(String loadSumId) {
        LOG.debug("Inside SearchResults of OleAcquisitionsSearchDocument");
        List<OleAcquisitionSearchResult> resultRows = new ArrayList<OleAcquisitionSearchResult>();
        try {
            List allPOsList = SpringContext.getBean(BatchLoadServiceImpl.class).getPOList(loadSumId);
            if (checkForAccountingDetails(getDocumentType())) {
                if (StringUtils.isEmpty(getDocumentType())) {
                    if (allPOsList.size() > 0) {
                        for (int i = 0; i < allPOsList.size(); i++) {
                            this.docNumber = (String) allPOsList.get(i);
                            resultRows.addAll(populateCriteriaAndSearch(PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT,
                                    getSearchCriteria(OleSelectConstant.AcquisitionsSearch.PURCHASEORDER_FIELDS)));
                        }
                    } else {
                        GlobalVariables.getMessageMap().putError(OleSelectConstant.AcquisitionsSearch.REQUISITIONS,
                                OLEKeyConstants.ERROR_NO_DOC_TYPE_FOUND, new String[]{});
                    }
                } else {
                    GlobalVariables.getMessageMap().putError(OleSelectConstant.AcquisitionsSearch.REQUISITIONS,
                            OLEKeyConstants.ERROR_NO_DOC_TYPE_FOUND, new String[]{});
                }
            }
            this.docNumber = "";
        } catch (Exception ex) {
            LOG.error("Exception while attempting to Search the Document: " + ex);
            throw new RuntimeException(ex);
        }
        LOG.debug("Leaving SearchResults of OleAcquisitionsSearchDocument");

        return resultRows;
    }

    /**
     * This method tests Whether DateTO is Lesser than DateFrom and returns true if Lesser.
     *
     * @return boolean
     * @throws Exception
     */
    public boolean isToDateLesserThanFromDate() throws Exception {
        //for adding date
        if (this.getDocumentType() != null && this.getDateFrom() == null) {
            this.dateFrom = getConfigurationService().getPropertyValueAsString("dateFrom");
        }
        boolean isToDateLesserThanFromDate = false;
        if (this.dateFrom != null && this.dateTo != null) {
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date startDate = formatter.parse(this.dateFrom);
            Date endDate = formatter.parse(this.dateTo);
            if (startDate.compareTo(endDate) > 0) {
                isToDateLesserThanFromDate = true;
            }
        }
        return isToDateLesserThanFromDate;
    }

}
