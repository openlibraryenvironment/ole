package org.kuali.ole.select.lookup;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.document.OLEInvoicePurchaseOrderSearch;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This is the Lookupable Impl class for the PurchaseOrder Documents
 */
public class OLEInvoicePurchaseOrderSearchLookupableImpl extends LookupableImpl {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEInvoicePurchaseOrderSearchLookupableImpl.class);

    private String poId;
    private String title;
    private String author;
    private String vendorNumber;
    private String isbn;
    private String documentNumber;
    private String searchResultPoId;
    private String searchResultPostingYear;
    private String searchResultTotalAmount;
    private Date poDateFrom;
    private Date poDateTo;
    private static transient BusinessObjectService businessObjectService;
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public boolean isNumber(String poNum) {
        boolean flag = true;
        if (poNum != null && !(poNum.isEmpty())) {
            if (StringUtils.isNumeric(poNum)) {
            } else {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {

        LOG.debug("Inside performSearch()");
        List<OLEInvoicePurchaseOrderSearch> finalInvoicePurchaseOrderList = new ArrayList<>();
        poId = searchCriteria.get(org.kuali.ole.OLEConstants.PURAP_DOC_IDENTIFIER) != null ? searchCriteria.get(org.kuali.ole.OLEConstants.PURAP_DOC_IDENTIFIER) : "";
        boolean validPoId = validatePoId();
        boolean validatePOFromAndToDates = validatePOFromAndToDates(searchCriteria.get("poDateFrom"), searchCriteria.get("poDateTo"));
        if (validPoId && validatePOFromAndToDates) {
            title = searchCriteria.get(org.kuali.ole.OLEConstants.TITLE) != null ? searchCriteria.get(org.kuali.ole.OLEConstants.TITLE) : "";
            author = searchCriteria.get(org.kuali.ole.OLEConstants.AUTHOR) != null ? searchCriteria.get(org.kuali.ole.OLEConstants.AUTHOR) : "";
            isbn = searchCriteria.get(org.kuali.ole.OLEConstants.ISBN) != null ? searchCriteria.get(org.kuali.ole.OLEConstants.ISBN) : "";
            vendorNumber = searchCriteria.get(org.kuali.ole.OLEConstants.VENDOR_NAME) != null ? searchCriteria.get(org.kuali.ole.OLEConstants.VENDOR_NAME) : "";
            documentNumber = searchCriteria.get(org.kuali.ole.OLEConstants.DOC_NUM) != null ? searchCriteria.get(org.kuali.ole.OLEConstants.DOC_NUM) : "";
            searchUsingSearchCriteria(finalInvoicePurchaseOrderList, vendorNumber, documentNumber, poId, title, author, isbn);
            if (GlobalVariables.getMessageMap().getWarningCount() > 0) {
                GlobalVariables.getMessageMap().getWarningMessages().clear();
            }
            if (finalInvoicePurchaseOrderList.size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, org.kuali.ole.OLEConstants.NO_RECORD_FOUND);
            }
        }
        return finalInvoicePurchaseOrderList;
    }

    private boolean validatePoId() {
        if (StringUtils.isNotBlank(poId)) {
            String[] poIds = poId.split(",");
            for (String poId : poIds) {
                if (!StringUtils.isNumeric(poId)) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,
                            OleSelectConstant.ERROR_PO_INVALID_FORMAT,
                    new String[]{org.kuali.ole.OLEConstants.PURCHASE_ORDER_NUM});
            return false;
        }
    }
        }
        return true;
    }

    private boolean validatePOFromAndToDates(String fromDate, String toDate) {
        boolean isValid = true;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        if (StringUtils.isNotBlank(fromDate)) {
            try {
                poDateFrom = sdf.parse(fromDate);
            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_INVALID_DATE, new String[]{"PO Date From"});
                isValid = false;
            }
        }
        if (StringUtils.isNotBlank(toDate)) {
            try {
                poDateTo = sdf.parse(toDate);
            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_INVALID_DATE, new String[]{"PO Date to"});
                isValid = false;
            }
        }
        if (poDateFrom !=null && poDateTo!=null){
            if (poDateFrom.compareTo(poDateTo) > 0){
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEKeyConstants.PUR_ORD_DATE_TO_NOT_LESSER_THAN_PUR_ORD_DATE_FROM, new String[]{});
                isValid = false;
            }
        }
        return isValid;
    }

    public void searchUsingSearchCriteria(List<OLEInvoicePurchaseOrderSearch> finalInvoicePurchaseOrderList, String vendorNumber, String documentNumber, String poId, String title, String author, String isbn) {
        DocumentSearchCriteria.Builder docSearchCriteria = DocumentSearchCriteria.Builder.create();
        docSearchCriteria.setDocumentTypeName(PurapConstants.PurapDocTypeCodes.PO_DOCUMENT);
        Map<String, List<String>> fixedParameters = new HashMap<>();
        if (!poId.isEmpty())
            fixedParameters.put(org.kuali.ole.OLEConstants.PURAP_DOC_IDENTIFIER, Arrays.asList(poId.split(",")));
        if (!vendorNumber.isEmpty())
            fixedParameters.put(org.kuali.ole.OLEConstants.OLEBatchProcess.VENDOR_NUMBER, Arrays.asList(vendorNumber));
        Map<String, List<String>> attributes = new HashMap<String, List<String>>();
        if (docSearchCriteria != null) {
            if (!fixedParameters.isEmpty()) {
                for (String propertyField : fixedParameters.keySet()) {
                    if (fixedParameters.get(propertyField) != null) {
                        attributes.put(propertyField, fixedParameters.get(propertyField));
                    }
                }
            }
        }
        docSearchCriteria.setDocumentAttributeValues(attributes);
        docSearchCriteria.setDocumentId(documentNumber);
        if (poDateFrom != null) {
            docSearchCriteria.setDateCreatedFrom(new DateTime(poDateFrom));
        }
        if (poDateTo != null) {
            docSearchCriteria.setDateCreatedTo(new DateTime(poDateTo));
        }
        docSearchCriteria.setApplicationDocumentStatus(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN);
        //docSearchCriteria.setDocumentStatuses(documentStatuses);
        DocumentSearchCriteria docSearchCriteriaDTO = docSearchCriteria.build();
        DocumentSearchResults components = null;
        components = KEWServiceLocator.getDocumentSearchService().lookupDocuments(GlobalVariables.getUserSession().getPrincipalId(), docSearchCriteriaDTO);
        List<DocumentSearchResult> docSearchResults = components.getSearchResults();
        if (docSearchResults!=null && !docSearchResults.isEmpty()) {
            for (DocumentSearchResult searchResult : docSearchResults) {
                List<DocumentAttribute> documentAttributeLists = searchResult.getDocumentAttributeByName(org.kuali.ole.OLEConstants.ITEM_TITLE_ID);
                Set distinctDocsearchResult = new HashSet();
                for (DocumentAttribute distinctDocsearchResultList : documentAttributeLists) {
                    distinctDocsearchResult.add(distinctDocsearchResultList);
                }
                List<DocumentAttribute> documentAttributeList = new ArrayList();
                documentAttributeList.addAll(distinctDocsearchResult);
                for (DocumentAttribute itemTitleId : documentAttributeList) {
                    String bibId = ((String) itemTitleId.getValue());
                    if (!title.isEmpty() || !author.isEmpty() || !isbn.isEmpty()) {
                        searchFromDocstore(finalInvoicePurchaseOrderList, searchResult, bibId, title, author, isbn);
                    } else {
                        getOlePurchaseOrderSearchList(finalInvoicePurchaseOrderList, searchResult, bibId, "", "", "");
                    }
                }
            }
        }
    }


    public void searchFromDocstore(List<OLEInvoicePurchaseOrderSearch> finalInvoicePurchaseOrderList, DocumentSearchResult search_Result, String bibId, String title, String author, String isbn) {
        String title_display = "";
        String author_display = "";
        String isbn_display = "";
        SearchResponse search_Response = new SearchResponse();
        SearchParams search_Params = new SearchParams();
        if (!title.isEmpty()) {
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition(org.kuali.ole.OLEConstants.AND_SEARCH_SCOPE, search_Params.buildSearchField(DocType.ITEM.getCode(), Bib.TITLE, title), org.kuali.ole.OLEConstants.AND_SEARCH_SCOPE));
        }
        if (!author.isEmpty()) {
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition(org.kuali.ole.OLEConstants.AND_SEARCH_SCOPE, search_Params.buildSearchField(DocType.ITEM.getCode(), Bib.AUTHOR, author), org.kuali.ole.OLEConstants.AND_SEARCH_SCOPE));
        }
        if (!isbn.isEmpty()) {
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition(org.kuali.ole.OLEConstants.AND_SEARCH_SCOPE, search_Params.buildSearchField(DocType.ITEM.getCode(), Bib.ISBN, isbn), org.kuali.ole.OLEConstants.AND_SEARCH_SCOPE));
        }
        search_Params.getSearchConditions().add(search_Params.buildSearchCondition(org.kuali.ole.OLEConstants.PO_NONE, search_Params.buildSearchField(DocType.ITEM.getCode(), org.kuali.ole.OLEConstants.PURCHASE_ORDER_LINE_ID_SEARCH, org.kuali.ole.OLEConstants.RANGE_QUERY), org.kuali.ole.OLEConstants.AND_SEARCH_SCOPE));
        search_Params.getSearchConditions().add(search_Params.buildSearchCondition(org.kuali.ole.OLEConstants.AND_SEARCH_SCOPE, search_Params.buildSearchField(DocType.ITEM.getCode(), org.kuali.ole.OLEConstants.BIB_SEARCH, bibId), org.kuali.ole.OLEConstants.AND_SEARCH_SCOPE));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.TITLE));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.AUTHOR));
        search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), Bib.ISBN));
        try {
            search_Response = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
            if (search_Response != null) {
                if (search_Response.getSearchResults() != null && search_Response.getSearchResults().size() > 0) {
                    for (SearchResult searchResult : search_Response.getSearchResults()) {
                        if (searchResult.getSearchResultFields() != null && searchResult.getSearchResultFields().size() > 0) {
                            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                if (searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(Bib.TITLE) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                                    title_display = searchResultField.getFieldValue();
                                }
                                if (searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(Bib.AUTHOR) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                                    author_display = searchResultField.getFieldValue();
                                }
                                if (searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(Bib.ISBN) && searchResultField.getFieldValue() != null && !searchResultField.getFieldValue().isEmpty()) {
                                    isbn_display = searchResultField.getFieldValue();
                                }
                            }
                        }
                    }
                    getOlePurchaseOrderSearchList(finalInvoicePurchaseOrderList, search_Result, bibId, title_display, author_display, isbn_display);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<OLEInvoicePurchaseOrderSearch> getOlePurchaseOrderSearchList(List<OLEInvoicePurchaseOrderSearch> finalInvoicePurchaseOrderList, DocumentSearchResult searchResult, String bibId, String title, String author, String isbn) {

        try {
            OLEInvoicePurchaseOrderSearch oleInvoicePurchaseOrderSearch = new OLEInvoicePurchaseOrderSearch();
            BibTree bibTree = new BibTree();
            if ((bibId != null && bibId != "") && title.isEmpty() && author.isEmpty() && isbn.isEmpty()) {
                bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId);
                oleInvoicePurchaseOrderSearch.setTitle(bibTree.getBib().getTitle());

                oleInvoicePurchaseOrderSearch.setAuthor(bibTree.getBib().getAuthor());
                oleInvoicePurchaseOrderSearch.setIsbn(bibTree.getBib().getIsbn());
            } else {
                if (!title.isEmpty() && title != null) {
                    oleInvoicePurchaseOrderSearch.setTitle(title);
                }
                if (!author.isEmpty() && author != null) {
                    oleInvoicePurchaseOrderSearch.setAuthor(author);
                }
                if (!isbn.isEmpty() && isbn != null) {
                    oleInvoicePurchaseOrderSearch.setIsbn(isbn);
                }
            }
            Map<String, String> searchParam = new HashMap<>();
            searchParam.put(org.kuali.ole.OLEConstants.DOC_NUM, searchResult.getDocument().getDocumentId());
            searchResultPoId = searchResult.getDocumentAttributeByName(org.kuali.ole.OLEConstants.PURAP_DOC_IDENTIFIER).get(0).getValue().toString();
            if (searchResultPoId != null && (!searchResultPoId.isEmpty())) {
                oleInvoicePurchaseOrderSearch.setPurapDocumentIdentifier(searchResultPoId);
            }
            oleInvoicePurchaseOrderSearch.setDocumentNumber(searchResult.getDocument().getDocumentId());
            searchResultPostingYear = searchResult.getDocumentAttributeByName(org.kuali.ole.OLEConstants.POSTING_YEAR).get(0).getValue().toString();
            if (searchResultPostingYear != null && (!searchResultPostingYear.isEmpty())) {
                oleInvoicePurchaseOrderSearch.setPostingYear(new Integer(searchResultPostingYear));
            }
            searchResultTotalAmount = searchResult.getDocumentAttributeByName(org.kuali.ole.OLEConstants.TOTAL_DOLLAR_AMOUNT).get(0).getValue().toString();
            if (searchResultTotalAmount != null && (!searchResultTotalAmount.isEmpty())) {
                oleInvoicePurchaseOrderSearch.setAmount(new KualiDecimal(searchResultTotalAmount));
            }
            finalInvoicePurchaseOrderList.add(oleInvoicePurchaseOrderSearch);
        } catch (Exception e) {
            LOG.error("Exception while getting PurchaseOrderSearchList ------> " + e);
            e.printStackTrace();
        }
        return finalInvoicePurchaseOrderList;
    }


    @Override
    public Map<String, String> performClear(LookupForm form, Map<String, String> searchCriteria) {
        super.performClear(form, searchCriteria);
        Map<String, String> clearedSearchCriteria = new HashMap<String, String>();
        for (Map.Entry<String, String> searchKeyValue : searchCriteria.entrySet()) {
            String searchPropertyName = searchKeyValue.getKey();
            if (searchPropertyName.equals(org.kuali.ole.OLEConstants.VENDOR_NAME)) {
                clearedSearchCriteria.put(searchPropertyName, searchKeyValue.getValue());
            }
        }
        return clearedSearchCriteria;
    }


}