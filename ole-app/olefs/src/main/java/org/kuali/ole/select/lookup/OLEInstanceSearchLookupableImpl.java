package org.kuali.ole.select.lookup;

import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.lookup.OleItemSearchLookupableImpl;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.select.bo.OLEInstanceSearch;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 7/4/13
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInstanceSearchLookupableImpl extends LookupableImpl {


    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleItemSearchLookupableImpl.class);
    private DocstoreClientLocator docstoreClientLocator;
    private DocstoreUtil docstoreUtil;

    public DocstoreUtil getDocstoreUtil() {
        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);
        }
        return docstoreUtil;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    private String title;
    private String author;
    private String publisher;
    private String localId;

    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        LOG.debug("Inside performSearch()");
        boolean searchFlag = true;
        for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
            if (!entry.getValue().trim().equals("")) {
                searchFlag = false;
                break;
            }
        }
        if (searchFlag) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.INSTANCE_BLANK_SEARCH_ERROR_MSG);
            return new ArrayList<Object>();
        }
        List<OLEInstanceSearch> oleInstanceSearchList = new ArrayList<>();
        title = searchCriteria.get("title") != null ? searchCriteria.get("title") : "";
        author = searchCriteria.get("author") != null ? searchCriteria.get("author") : "";
        publisher = searchCriteria.get("publisher") != null ? searchCriteria.get("publisher") : "";
        localId = searchCriteria.get("localId") != null ? searchCriteria.get("localId") : "";
        if (!title.isEmpty() || !author.isEmpty() || !publisher.isEmpty() || !localId.isEmpty()) {
            SearchParams searchParams = new SearchParams();
            SearchResponse searchResponse = new SearchResponse();
            searchParams.setPageSize(25);
            try {
                if (!title.isEmpty() || !author.isEmpty() || !publisher.isEmpty()) {
                    if (!title.isEmpty()) {
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(), Bib.TITLE, title), "AND"));
                    }
                    if (!author.isEmpty()) {
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(), Bib.AUTHOR, author), "AND"));
                    }
                    if (!publisher.isEmpty()) {
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(), Bib.PUBLISHER, publisher), "AND"));
                    }
                    if (!localId.isEmpty() && (!localId.contains(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML))) {
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(), "holdingsIdentifier", DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, localId)), "AND"));
                    }
                    searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id"));
                    searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                    if (searchResponse.getSearchResults() != null && searchResponse.getSearchResults().size() > 0) {
                        for (SearchResult searchResult : searchResponse.getSearchResults()) {
                            String fieldName = searchResult.getSearchResultFields().get(0).getFieldName() != null ? searchResult.getSearchResultFields().get(0).getFieldName() : "";
                            String fieldValue = searchResult.getSearchResultFields().get(0).getFieldValue() != null ? searchResult.getSearchResultFields().get(0).getFieldValue() : "";
                            if (!fieldName.isEmpty() && fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty()) {
                                oleInstanceSearchList.addAll(getHoldingsBibInfo(fieldValue, localId));
                            }
                        }
                    }
                } else if (!localId.isEmpty() && !localId.contains(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML)) {
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.HOLDINGS.getCode(), OLEConstants.LOCALID_SEARCH, localId), ""));
                    oleInstanceSearchList.addAll(getOleInstanceList(searchParams));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (oleInstanceSearchList.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        return oleInstanceSearchList;
    }


    public List<OLEInstanceSearch> getHoldingsBibInfo(String bibId, String localId) {
        List<OLEInstanceSearch> oleInstanceSearches = new ArrayList<>();
        try {
            BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId);
            for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                if (holdingsTree.getHoldings().getId().equalsIgnoreCase(DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, localId)) || localId.isEmpty()) {
                    OLEInstanceSearch oleInstanceSearch = new OLEInstanceSearch();
                    HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
                    OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
                    oleInstanceSearch.setBibId(bibTree.getBib().getId());
                    oleInstanceSearch.setTitle(bibTree.getBib().getTitle());
                    oleInstanceSearch.setAuthor(bibTree.getBib().getAuthor());
                    oleInstanceSearch.setIssn(bibTree.getBib().getIssn());
                    oleInstanceSearch.setPublisher(bibTree.getBib().getPublisher());
                    oleInstanceSearch.setLocalId(DocumentUniqueIDPrefix.getDocumentId(bibTree.getBib().getId()));
                    oleInstanceSearch.setInstanceId(holdingsTree.getHoldings().getId());
                    oleInstanceSearch.setInstanceLocalId(DocumentUniqueIDPrefix.getDocumentId(holdingsTree.getHoldings().getId()));
                    oleInstanceSearch.setCallNumber(oleHoldings.getCallNumber()!=null? oleHoldings.getCallNumber().getNumber() : null);
                    oleInstanceSearch.setCopyNumber(oleHoldings.getCopyNumber());
                    StringBuffer locationLevel = new StringBuffer("");
                    oleInstanceSearch.setLocation(getDocstoreUtil().getLocation(oleHoldings.getLocation(), locationLevel));
                    oleInstanceSearches.add(oleInstanceSearch);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return oleInstanceSearches;
    }

    public List<OLEInstanceSearch> getOleInstanceList(SearchParams searchParams) {
        SearchResponse searchResponse = new SearchResponse();
        List<OLEInstanceSearch> oleInstanceSearchList = new ArrayList<>();
        Bib bib = new BibMarc();
        org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), "id"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), Holdings.CALL_NUMBER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), Holdings.COPY_NUMBER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), Holdings.LOCATION_NAME));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), OLEConstants.LOCALID_SEARCH));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), bib.TITLE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), bib.AUTHOR));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), bib.PUBLISHER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), bib.ISSN));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), OLEConstants.LOCALID_SEARCH));
        try {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            if (searchResponse.getSearchResults() != null && searchResponse.getSearchResults().size() > 0) {
                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    OLEInstanceSearch oleInstanceSearch = new OLEInstanceSearch();
                    if (searchResult.getSearchResultFields().get(0).getFieldName().equalsIgnoreCase("id") && searchResult.getSearchResultFields().get(0).getFieldValue() != null && !searchResult.getSearchResultFields().get(0).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(0).getDocType().equalsIgnoreCase("holdings")) {
                        oleInstanceSearch.setInstanceId(searchResult.getSearchResultFields().get(0).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(1).getFieldName().equalsIgnoreCase(Holdings.CALL_NUMBER) && searchResult.getSearchResultFields().get(1).getFieldValue() != null && !searchResult.getSearchResultFields().get(1).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(1).getDocType().equalsIgnoreCase("holdings")) {
                        oleInstanceSearch.setCallNumber(searchResult.getSearchResultFields().get(1).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(2).getFieldName().equalsIgnoreCase(Holdings.COPY_NUMBER) && searchResult.getSearchResultFields().get(2).getFieldValue() != null && !searchResult.getSearchResultFields().get(2).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(2).getDocType().equalsIgnoreCase("holdings")) {
                        oleInstanceSearch.setCopyNumber(searchResult.getSearchResultFields().get(2).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(3).getFieldName().equalsIgnoreCase(Holdings.LOCATION_NAME) && searchResult.getSearchResultFields().get(3).getFieldValue() != null && !searchResult.getSearchResultFields().get(3).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(3).getDocType().equalsIgnoreCase("holdings")) {
                        oleInstanceSearch.setLocation(searchResult.getSearchResultFields().get(3).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(4).getFieldName().equalsIgnoreCase(OLEConstants.LOCALID_SEARCH) && searchResult.getSearchResultFields().get(4).getFieldValue() != null && !searchResult.getSearchResultFields().get(4).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(4).getDocType().equalsIgnoreCase("holdings")) {
                        oleInstanceSearch.setInstanceLocalId(searchResult.getSearchResultFields().get(4).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(5).getFieldName().equalsIgnoreCase("id") && searchResult.getSearchResultFields().get(5).getFieldValue() != null && !searchResult.getSearchResultFields().get(5).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(5).getDocType().equalsIgnoreCase("bibliographic")) {
                        oleInstanceSearch.setBibId(searchResult.getSearchResultFields().get(5).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(6).getFieldName().equalsIgnoreCase(Bib.TITLE) && searchResult.getSearchResultFields().get(6).getFieldValue() != null && !searchResult.getSearchResultFields().get(6).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(6).getDocType().equalsIgnoreCase("bibliographic")) {
                        oleInstanceSearch.setTitle(searchResult.getSearchResultFields().get(6).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(7).getFieldName().equalsIgnoreCase(Bib.AUTHOR) && searchResult.getSearchResultFields().get(7).getFieldValue() != null && !searchResult.getSearchResultFields().get(7).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(7).getDocType().equalsIgnoreCase("bibliographic")) {
                        oleInstanceSearch.setAuthor(searchResult.getSearchResultFields().get(7).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(8).getFieldName().equalsIgnoreCase(Bib.PUBLISHER) && searchResult.getSearchResultFields().get(8).getFieldValue() != null && !searchResult.getSearchResultFields().get(8).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(8).getDocType().equalsIgnoreCase("bibliographic")) {
                        oleInstanceSearch.setPublisher(searchResult.getSearchResultFields().get(8).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(9).getFieldName().equalsIgnoreCase(Bib.ISSN) && searchResult.getSearchResultFields().get(9).getFieldValue() != null && !searchResult.getSearchResultFields().get(9).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(9).getDocType().equalsIgnoreCase("bibliographic")) {
                        oleInstanceSearch.setIssn(searchResult.getSearchResultFields().get(9).getFieldValue());
                    }
                    if (searchResult.getSearchResultFields().get(10).getFieldName().equalsIgnoreCase(OLEConstants.LOCALID_SEARCH) && searchResult.getSearchResultFields().get(10).getFieldValue() != null && !searchResult.getSearchResultFields().get(10).getFieldValue().isEmpty() && searchResult.getSearchResultFields().get(10).getDocType().equalsIgnoreCase("bibliographic")) {
                        oleInstanceSearch.setLocalId(searchResult.getSearchResultFields().get(10).getFieldValue());
                    }
                    oleInstanceSearchList.add(oleInstanceSearch);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return oleInstanceSearchList;

    }

}
