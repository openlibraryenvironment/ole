package org.kuali.ole.docstore.engine.service;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.*;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.engine.service.index.DocstoreIndexService;
import org.kuali.ole.docstore.engine.service.index.DocstoreIndexServiceImpl;
import org.kuali.ole.docstore.engine.service.index.solr.BibMarcIndexer;
import org.kuali.ole.docstore.engine.service.search.DocstoreSearchService;
import org.kuali.ole.docstore.engine.service.search.DocstoreSolrSearchService;
import org.kuali.ole.docstore.engine.service.storage.DocstoreRDBMSStorageService;
import org.kuali.ole.docstore.engine.service.storage.DocstoreStorageService;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.impl.parameter.ParameterServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreServiceImpl implements DocstoreService {
    private static final Logger LOG = LoggerFactory.getLogger(DocstoreServiceImpl.class);
    private DocstoreStorageService docstoreStorageService = null;
    private DocstoreSearchService docstoreSearchService = null;
    private DocstoreIndexService docstoreIndexService = null;
    protected ParameterServiceImpl parameterService = new ParameterServiceImpl();

    public String getParameter() {
        ParameterKey parameterKey = ParameterKey.create(OLE, OLE_DESC, DESCRIBE, PROCESS_SOLR_IND);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }

    @Override
    public void createBib(Bib bib) {
        try {
            getDocstoreStorageService().createBib(bib);
        } catch (Exception e) {
            LOG.error("Exception occurred while creating Bib", e);
            throw e;
        }
        try {
            getDocstoreIndexService().createBib(bib);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing created Bib", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void createHoldings(Holdings holdings) {
        try {
            getDocstoreStorageService().createHoldings(holdings);
        } catch (Exception e) {
            LOG.error("Exception occurred while creating Holdings or EHoldings", e);
            throw e;
        }
        try {
            getDocstoreIndexService().createHoldings(holdings);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing created Holdings or EHoldings", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void createItem(Item item) {
        try {
            getDocstoreStorageService().createItem(item);
        } catch (Exception e) {
            LOG.error("Exception occurred while creating item", e);
            throw e;
        }
        try {
            getDocstoreIndexService().createItem(item);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing created item", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void createHoldingsTree(HoldingsTree holdingsTree) {
        try {
            getDocstoreStorageService().createHoldingsTree(holdingsTree);
        } catch (Exception e) {
            LOG.error("Exception occurred while creating Holdings tree", e);
            throw e;
        }
        try {
            getDocstoreIndexService().createHoldingsTree(holdingsTree);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing created Holdings tree", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void createBibTree(BibTree bibTree) {
        try {
            getDocstoreStorageService().createBibTree(bibTree);
        } catch (Exception e) {
            LOG.error("Exception occurred while creating Bib tree", e);
            throw e;
        }
        try {
            getDocstoreIndexService().createBibTree(bibTree);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing created Bib tree", e);
            docstoreStorageService.rollback();
            throw e;
        }


    }

    @Override
    public Bib retrieveBib(String bibId) {
        Bib bib = getDocstoreStorageService().retrieveBib(bibId);
        return bib;
    }

    @Override
    public List<Bib> retrieveBibs(List<String> bibIds) {
        List<Bib> bibs = getDocstoreStorageService().retrieveBibs(bibIds);
        return bibs;
    }

    @Override
    public List<Item> retrieveItems(List<String> itemIds) {
        List<Item> items = getDocstoreStorageService().retrieveItems(itemIds);
        return items;
    }

    @Override
    public HashMap<String, Item> retrieveItemMap(List<String> itemIds) {
        HashMap<String, Item> items = getDocstoreStorageService().retrieveItemMap(itemIds);
        return items;
    }

    @Override
    public Holdings retrieveHoldings(String holdingsId) {
        Holdings holdings = getDocstoreStorageService().retrieveHoldings(holdingsId);

        //Holdings holdings = getHoldingsRecord();
        //getDocstoreStorageService().retrieveHoldings(holdingsId);
//        Holdings holdings = new PHoldingsOleml();
        holdings.setId(holdingsId);
        holdings.setCategory(DocCategory.WORK.getCode());
        holdings.setType(DocType.HOLDINGS.getCode());
        holdings.setFormat(DocFormat.OLEML.getCode());
        return holdings;
    }

    @Override
    public Item retrieveItem(String itemId) {
        Item item = getDocstoreStorageService().retrieveItem(itemId);
        item.setId(itemId);
        item.setCategory(DocCategory.WORK.getCode());
        item.setType(DocType.ITEM.getCode());
        item.setFormat(DocFormat.OLEML.getCode());
        return item;
    }

    @Override
    public HoldingsTree retrieveHoldingsTree(String holdingsId) {
        HoldingsTree holdingsTree = getDocstoreStorageService().retrieveHoldingsTree(holdingsId);
        return holdingsTree;
    }

    @Override
    public BibTree retrieveBibTree(String bibId) {
        BibTree bibTree = getDocstoreStorageService().retrieveBibTree(bibId);
        return bibTree;
    }

    @Override
    public BibTrees retrieveBibTrees(List<String> bibIds) {
        BibTrees bibTrees = new BibTrees();
        bibTrees.getBibTrees().addAll(getDocstoreStorageService().retrieveBibTrees(bibIds));
        return bibTrees;
    }

    @Override
    public void updateBib(Bib bib) {
        try {
            getDocstoreStorageService().updateBib(bib);
        } catch (Exception e) {
            LOG.error("Exception occurred while updating Bib ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().updateBib(bib);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing updated Bib ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void updateBibs(List<Bib> bibs) {
        try {
            getDocstoreStorageService().updateBibs(bibs);
        } catch (Exception e) {
            LOG.error("Exception occurred while updating list of Bib ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().updateBibs(bibs);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing updated  list of Bib ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void updateHoldings(Holdings holdings) {
        try {
            getDocstoreStorageService().updateHoldings(holdings);
        } catch (Exception e) {
            LOG.error("Exception occurred while updating Holdings ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().updateHoldings(holdings);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing updated Holdings ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void updateItem(Item item) {
        try {
            getDocstoreStorageService().updateItem(item);
        } catch (Exception e) {
            LOG.error("Exception occurred while updating item ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().updateItem(item);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing updated item ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void deleteBib(String bibId) {
        if (!DocumentUniqueIDPrefix.hasPrefix(bibId)) {
            bibId = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, bibId);
        }
        try {
            getDocstoreStorageService().deleteBib(bibId);
        } catch (Exception e) {
            LOG.error("Exception occurred while deleting Bib ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().deleteBib(bibId);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing deleted Bib ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void deleteHoldings(String holdingsId) {
        if (!DocumentUniqueIDPrefix.hasPrefix(holdingsId)) {
            holdingsId = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, holdingsId);
        }
        try {
            Holdings holding = retrieveHoldings(holdingsId);
            getDocstoreStorageService().deleteHoldings(holdingsId);
            getDocstoreStorageService().saveDeletedHolding(holding);
        } catch (Exception e) {
            LOG.error("Exception occurred while deleting Holdings ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().deleteHoldings(holdingsId);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing deleted Holdings ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void deleteItem(String itemId) {
        if (!DocumentUniqueIDPrefix.hasPrefix(itemId)) {
            itemId = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML, itemId);
        }
        try {
            Item item = retrieveItem(itemId);
            getDocstoreStorageService().deleteItem(itemId);
            getDocstoreStorageService().saveDeletedItem(item);
        } catch (Exception e) {
            LOG.error("Exception occurred while deleting item ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().deleteItem(itemId);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing deleted item ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public SearchResponse search(SearchParams searchParams) {
        SearchResponse searchResponse = null;
        try {
            DocstoreSearchService searchService = getDocstoreSearchService();
            searchResponse = searchService.search(searchParams);
        } catch (Exception e) {
            LOG.error("Exception occurred in Search Response service ", e);
            throw e;
        }
        return searchResponse;
    }

    public void setResultFieldsForHoldings(Holdings holdings, List<SearchResultField> searchResultFields) {
        OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(holdings.getContent());
        try {
            for (SearchResultField searchResultField : searchResultFields) {
                if (searchResultField.getDocType().equalsIgnoreCase("Holdings")) {
                    if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.CALL_NUMBER)) {
                        holdings.setCallNumber(oleHoldings.getCallNumber().getNumber());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.CALLNUMBER_PREFIX)) {
                        holdings.setCallNumberPrefix(oleHoldings.getCallNumber().getPrefix());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.CALLNUMBER_TYPE)) {
                        holdings.setCallNumberType(oleHoldings.getCallNumber().getType());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.COPY_NUMBER)) {
                        holdings.setCopyNumber(oleHoldings.getCopyNumber());
                    } else if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.LOCATION_NAME)) {
                        holdings.setLocationName(oleHoldings.getLocation().getLocationLevel().getName());
//                } else if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.CREATED_BY)) {
//                    holdings.setCreatedBy(oleHoldings.getCreatedBy());
//                } else if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.UPDATED_BY)) {
//                    holdings.setUpdatedBy(oleHoldings.getUpdatedBy());
//                } else if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.DATE_ENTERED)) {
//                    holdings.setCreatedOn(oleHoldings.getCreatedDate().toString());
//                } else if (searchResultField.getFieldName().equalsIgnoreCase(Holdings.DATE_UPDATED)) {
//                    holdings.setUpdatedBy(oleHoldings.getUpdatedDate().toString());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in setting the result fields for Holdings ", e);
            throw e;
        }
    }

    public void setResultFieldsForItem(Item itemDoc, List<SearchResultField> searchResultFields) {
        org.kuali.ole.docstore.common.document.content.instance.Item item = new ItemOlemlRecordProcessor().fromXML(itemDoc.getContent());
        for (SearchResultField searchResultField : searchResultFields) {
            if (searchResultField.getDocType().equalsIgnoreCase("Item")) {
                if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.ITEM_STATUS)) {
                    itemDoc.setItemStatus(item.getItemStatus().getCodeValue());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.CALL_NUMBER)) {
                    itemDoc.setCallNumber(item.getCallNumber().getNumber());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.CALL_NUMBER_TYPE)) {
                    itemDoc.setCallNumber(item.getCallNumber().getShelvingScheme().getCodeValue());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.CALL_NUMBER_PREFIX)) {
                    itemDoc.setCallNumberPrefix(item.getCallNumber().getPrefix());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.LOCATION)) {
                    itemDoc.setLocation(item.getLocation().toString());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.SHELVING_ORDER)) {
                    itemDoc.setShelvingOrder(item.getCallNumber().getShelvingOrder().getCodeValue());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.ITEM_BARCODE)) {
                    itemDoc.setBarcode(item.getAccessInformation().getBarcode());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.COPY_NUMBER)) {
                    itemDoc.setCopyNumber(item.getCopyNumber());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.ENUMERATION)) {
                    itemDoc.setEnumeration(item.getEnumeration());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.CHRONOLOGY)) {
                    itemDoc.setChronology(item.getChronology());
                } else if (searchResultField.getFieldName().equalsIgnoreCase(itemDoc.ITEM_TYPE)) {
                    itemDoc.setItemType(item.getItemType().getCodeValue());
                }
            }
        }
    }

    @Override
    public void setResultFieldsForBib(Bib bib, List<SearchResultField> searchResultFields) {
        for (SearchResultField searchResultField : searchResultFields) {
            if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode())) {
                if (searchResultField.getFieldName().equalsIgnoreCase("Title")) {
                    bib.setTitle(searchResultField.getFieldValue());
                }
                if (searchResultField.getFieldName().equalsIgnoreCase("Format")) {
                    bib.setFormat(searchResultField.getFieldValue());
                }
                if (searchResultField.getFieldName().equalsIgnoreCase("Id")) {
                    bib.setId(searchResultField.getFieldValue());
                }
                if (searchResultField.getFieldName().equalsIgnoreCase("LocalId")) {
                    bib.setLocalId(searchResultField.getFieldValue());
                }
                if (searchResultField.getFieldName().equalsIgnoreCase("Author")) {
                    bib.setAuthor(searchResultField.getFieldValue());
                }
                if (searchResultField.getFieldName().equalsIgnoreCase("PublicationDate")) {
                    bib.setPublicationDate(searchResultField.getFieldValue());
                }
            }
        }
    }

    @Override
    public void createLicense(License license) {
        try {
            getDocstoreStorageService().createLicense(license);
        } catch (Exception e) {
            LOG.error("Exception occurred while creating license for docstore ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().createLicense(license);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing created license for docstore ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public Bib findBib(Map<String, String> map) {
        String id = null;
        try {
            id = getDocstoreSearchService().findBib(map);
            if (id != null) {
                return retrieveBib(id);
            }
        } catch (Exception e) {
            LOG.error("Exception occurred while retrieving a bib for the id:" + id, e);
            throw e;
        }
        return null;
    }

    @Override
    public BibTree findBibTree(Map<String, String> map) {
        String id = null;
        try {
            id = getDocstoreSearchService().findBib(map);
            if (id != null) {
                return retrieveBibTree(id);
            }
        } catch (Exception e) {
            LOG.error("Exception occurred while retrieving bib tree for the id:" + id, e);
            throw e;
        }
        return null;
    }

    @Override
    public Holdings findHoldings(Map<String, String> map) {
        String id = null;
        try {
            id = getDocstoreSearchService().findBib(map);
            if (id != null) {
                return retrieveHoldings(id);
            }
        } catch (Exception e) {
            LOG.error("Exception occurred while retrieving Holdings for the id:" + id, e);
            throw e;
        }
        return null;
    }

    @Override
    public HoldingsTree findHoldingsTree(Map<String, String> map) {
        String id = null;
        try {
            id = getDocstoreSearchService().findBib(map);
            if (id != null) {
                return retrieveHoldingsTree(id);
            }
        } catch (Exception e) {
            LOG.error("Exception occurred while retrieving Holdings tree for the id:" + id, e);
            throw e;
        }
        return null;
    }

    @Override
    public Item findItem(Map<String, String> map) {
        String id = null;
        try {
            id = getDocstoreSearchService().findBib(map);
            if (id != null) {
                return retrieveItem(id);
            }
        } catch (Exception e) {
            LOG.error("Exception occurred while retrieving item for the id:" + id, e);
            throw e;
        }
        return null;
    }

    @Override
    public void boundHoldingsWithBibs(String holdingsId, List<String> bibIds) {
        try {
            getDocstoreStorageService().boundHoldingsWithBibs(holdingsId, bibIds);
        } catch (Exception e) {
            LOG.error("Exception occurred in bound holdings with bibs ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().boundHoldingsWithBibs(holdingsId, bibIds);
        } catch (Exception e) {
            docstoreStorageService.rollback();
            LOG.error("Exception occurred while indexing the bounded holdings with bibs ", e);
            throw e;
        }

    }

    @Override
    public void transferHoldings(List<String> holdingsIds, String bibId) {

        //  holdingsIds = new ArrayList<>();
        /*bibId = "wbm-10000006";
        holdingsIds.add("wno-11");*/
        try {
            getDocstoreStorageService().transferHoldings(holdingsIds, bibId);
        } catch (Exception e) {
            LOG.error("Exception occurred while transferring Holdings ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().transferHoldings(holdingsIds, bibId);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing a transferred Holdings ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void transferItems(List<String> itemIds, String holdingsId) {
        try {
            getDocstoreStorageService().transferItems(itemIds, holdingsId);
        } catch (Exception e) {
            LOG.error("Exception occurred while transferring items ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().transferItems(itemIds, holdingsId);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing a transferred items ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void createBibTrees(BibTrees bibTrees) {
        try {
            getDocstoreStorageService().createBibTrees(bibTrees);
        } catch (Exception e) {
            LOG.error("Exception occurred while creating bib trees ", e);
        }
        try {
            getDocstoreIndexService().createBibTrees(bibTrees);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing created bib trees ", e);
            docstoreStorageService.rollback();
            throw new DocstoreIndexException();
        }
    }

    @Override
    public void deleteBibs(List<String> bibIds) {
        List<Bib> bibs = new ArrayList<>();
        for(String bibId:bibIds){
            Bib bib = retrieveBib(bibId);
            bibs.add(bib);
        }
        getDocstoreStorageService().deleteBibs(bibIds);
        try {
            getDocstoreStorageService().saveDeletedBibs(bibs);
        } catch (Exception e) {
            LOG.error("Exception occurred while saving deleted bib records ", e);
        }
        try {
            getDocstoreIndexService().deleteBibs(bibIds);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing bib records after deletion", e);
            docstoreStorageService.rollback();
        }
    }

    @Override
    public SearchResponse browseItems(BrowseParams browseParams) {
        try {
            SearchResponse searchResponse = getDocstoreSearchService().search(browseParams);
//                    List<String> itemIds = getDocstoreSearchService().callNumberBrowse(browseParams);
//                    List<Item> items = new ArrayList<>();
//                    for (String itemId : itemIds) {
//                        Item item = retrieveItem(itemId);
//                        if (item != null) {
//                            items.add(item);
//                            setResultFieldsForItem(item, browseParams.getSearchResultFields());
//                        }
//                    }
            return searchResponse;

        } catch (Exception e) {
            LOG.error("Exception occurred getting the search response for browse items ", e);
            throw e;
        }

    }

    @Override
    public SearchResponse browseHoldings(BrowseParams browseParams) {
        try {
            SearchResponse searchResponse = getDocstoreSearchService().search(browseParams);
//                    List<String> holdingIds = new ArrayList<>();
//                    for (SearchResult searchResult : searchResponse.getSearchResults()) {
//                        for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
//                            if (searchResultField.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
//                                if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
//                                    holdingIds.add(searchResultField.getFieldValue());
//                                }
//                            }
//                        }
//                    }
//                    List<Holdings> holdingsList = new ArrayList<>();
//                    for (String holdingId : holdingIds) {
//                        Holdings holdings = retrieveHoldings(holdingId);
//                        if (holdings != null) {
//                            holdingsList.add(holdings);
//                            setResultFieldsForHoldings(holdings, browseParams.getSearchResultFields());
//                        }
//                    }
            return searchResponse;
        } catch (Exception e) {
            LOG.error("Exception occurred getting the search response for browse Holdings ", e);
            throw e;
        }
    }

    private DocstoreStorageService getDocstoreStorageService() {
        if (docstoreStorageService == null) {
            docstoreStorageService = new DocstoreRDBMSStorageService();
        }
        return docstoreStorageService;
    }

    private DocstoreSearchService getDocstoreSearchService() {
        if (docstoreSearchService == null) {
            docstoreSearchService = new DocstoreSolrSearchService();
        }

        return docstoreSearchService;
    }

    private DocstoreIndexService getDocstoreIndexService() {
        if (docstoreIndexService == null) {
            docstoreIndexService = new DocstoreIndexServiceImpl();
        }
        return docstoreIndexService;
    }

    @Override
    public void createLicenses(Licenses licenses) {
        try {
            getDocstoreStorageService().createLicenses(licenses);
        } catch (Exception e) {
            LOG.error("Exception occurred while creating license for docstore ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().createLicenses(licenses);
        } catch (Exception e) {
            LOG.error("Exception occurred while doing indexing the created license  ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public License retrieveLicense(String licenseId) {
        License license = null;
        try {
            license = (License) getDocstoreStorageService().retrieveLicense(licenseId);
        } catch (Exception e) {
            LOG.error("Exception occurred while retrieving a created license , id : " + licenseId, e);
            throw e;
        }
        return license;
    }

    @Override
    public Licenses retrieveLicenses(List<String> licenseIds) {
        Licenses licenses = null;
        try {
            licenses = getDocstoreStorageService().retrieveLicenses(licenseIds);
        } catch (Exception e) {
            LOG.error("Exception occurred while retrieving a created licenses , ids : " + licenseIds, e);
            throw e;
        }
        return licenses;
    }

    @Override
    public void updateLicense(License license) {
        try {
            getDocstoreStorageService().updateLicense(license);
        } catch (Exception e) {
            LOG.error("Exception occurred while updating license ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().updateLicense(license);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing updated license ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void updateLicenses(Licenses licenses) {
        try {
            getDocstoreStorageService().updateLicenses(licenses);
        } catch (Exception e) {
            LOG.error("Exception occurred while updating licenses ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().updateLicenses(licenses);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing updated licenses ", e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void deleteLicense(String licenseId) {
//        if (!DocumentUniqueIDPrefix.hasPrefix(licenseId)) {
//            licenseId = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_LICENSE_ONIXPL, licenseId);
//        }
        try {
            getDocstoreStorageService().deleteLicense(licenseId);
        } catch (Exception e) {
            LOG.error("Exception occurred while deleting license for id : " + licenseId, e);
            throw e;
        }
        try {
            getDocstoreIndexService().deleteLicense(licenseId);
        } catch (Exception e) {
            LOG.error("Exception occurred while indexing the license for id : " + licenseId, e);
            docstoreStorageService.rollback();
            throw e;
        }
    }

    @Override
    public void createAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        try {
            getDocstoreStorageService().createAnalyticsRelation(seriesHoldingsId, itemIds);
        } catch (Exception e) {
            LOG.error("Exception occurred while creating analytical relation ", e);
            throw e;
        }

        try {
            getDocstoreIndexService().createAnalyticsRelation(seriesHoldingsId, itemIds);
        } catch (Exception e) {
            docstoreStorageService.rollback();
            LOG.error("Exception occurred while indexing created analytical relation ", e);
            throw e;
        }
    }

    @Override
    public void bulkUpdateHoldings(Holdings holdings, List<String> holdingIds, String canUpdateStaffOnlyFlag) {
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        List<String> bibIds = new ArrayList<>();
        try {
            for (String holdingId : holdingIds) {
                Holdings existingHoldings = (Holdings) getDocstoreStorageService().retrieveHoldings(holdingId);
                if (existingHoldings != null) {
                    OleHoldings existingOleHoldings = holdingOlemlRecordProcessor.fromXML(existingHoldings.getContent());
                    if (oleHoldings != null && oleHoldings.getLocation() != null &&
                            oleHoldings.getLocation().getLocationLevel() != null &&
                            oleHoldings.getLocation().getLocationLevel().getName() != null &&
                            !oleHoldings.getLocation().getLocationLevel().getName().isEmpty()) {
//                    if (existingOleHoldings.getLocation() != null && existingOleHoldings.getLocation().getLocationLevel() != null) {
//                        existingOleHoldings.getLocation().getLocationLevel().setName(oleHoldings.getLocation().getLocationLevel().getName());
//                    } else {
                        existingOleHoldings.setLocation(oleHoldings.getLocation());
//                    }

                    }
                    if (canUpdateStaffOnlyFlag.equalsIgnoreCase("true")) {
                        existingHoldings.setStaffOnly(oleHoldings.isStaffOnlyFlag());
                    }
                    if (oleHoldings.getCallNumber() != null) {
                        if (existingOleHoldings.getCallNumber() != null) {
                            if (StringUtils.isNotBlank(oleHoldings.getCallNumber().getPrefix())) {
                                existingOleHoldings.getCallNumber().setPrefix(oleHoldings.getCallNumber().getPrefix());
                            }
                            if (StringUtils.isNotBlank(oleHoldings.getCallNumber().getNumber())) {
                                existingOleHoldings.getCallNumber().setNumber(oleHoldings.getCallNumber().getNumber());
                            }
                            if (oleHoldings.getCallNumber().getShelvingScheme() != null &&
                                    StringUtils.isNotBlank(oleHoldings.getCallNumber().getShelvingScheme().getCodeValue())) {
                                if(existingOleHoldings.getCallNumber().getShelvingScheme()!=null) {
                                    existingOleHoldings.getCallNumber().getShelvingScheme().setCodeValue(oleHoldings.getCallNumber().getShelvingScheme().getCodeValue());
                                } else{
                                    existingOleHoldings.getCallNumber().setShelvingScheme(oleHoldings.getCallNumber().getShelvingScheme());
                                }
                            }
                            if (oleHoldings.getCallNumber().getShelvingOrder() != null &&
                                    StringUtils.isNotBlank(oleHoldings.getCallNumber().getShelvingOrder().getFullValue())) {
                                existingOleHoldings.getCallNumber().getShelvingOrder().setFullValue(oleHoldings.getCallNumber().getShelvingOrder().getFullValue());
                            }
                        } else {
                            existingOleHoldings.setCallNumber(oleHoldings.getCallNumber());
                        }
                    }
                    if (oleHoldings.getNote() != null && oleHoldings.getNote().size() > 0) {
                        List<Note> holdingNotes = existingOleHoldings.getNote();
                        if (holdingNotes != null && holdingNotes.size() > 0) {
                            for (Note note : oleHoldings.getNote()) {
                                if (note.getType() != null && note.getValue() != null && !note.getValue().isEmpty()) {
                                    holdingNotes.add(note);
                                }
                            }
                            existingOleHoldings.setNote(holdingNotes);
                        } else {

                            for (Note note : oleHoldings.getNote()) {
                                if (note.getType() != null && note.getValue() != null && !note.getValue().isEmpty()) {
                                    holdingNotes.add(note);
                                }
                            }
                            if (holdingNotes != null && holdingNotes.size() > 0)
                                existingOleHoldings.setNote(holdingNotes);

                        }
                    }

                    if (holdings instanceof PHoldings) {
                        setPHoldingInformation(oleHoldings, existingOleHoldings);
                    } else {
                        setEHoldingInformation(oleHoldings, existingOleHoldings);
                    }
                    if (canUpdateStaffOnlyFlag.equalsIgnoreCase("true")) {
                        existingHoldings.setStaffOnly(holdings.isStaffOnly());
                    }
                    existingHoldings.setUpdatedBy(holdings.getUpdatedBy());
                    existingHoldings.setUpdatedOn(holdings.getUpdatedOn());
                    existingHoldings.setLastUpdated(holdings.getLastUpdated());
                    existingHoldings.setCategory(holdings.getCategory());
                    existingHoldings.setType(holdings.getType());
                    existingHoldings.setFormat(holdings.getFormat());
                    existingHoldings.setContent(holdingOlemlRecordProcessor.toXML(existingOleHoldings));
                    try {
                        updateHoldings(existingHoldings);
                    } catch (Exception e){
                        bibIds.add(existingOleHoldings.getBibIdentifier());
                    }
                } else {
                    DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.HOLDING_ID_NOT_FOUND, DocstoreResources.HOLDING_ID_NOT_FOUND);
                    docstoreException.addErrorParams("holdingsId", holdingId);
                    throw docstoreException;
                }

            }
            if(bibIds.size()>0){
                BibMarcIndexer bibMarcIndexer = new BibMarcIndexer();
                DocstoreRDBMSStorageService rdbmsStorageService = new DocstoreRDBMSStorageService();
                for(String bibId : bibIds) {
                    bibMarcIndexer.createTree(rdbmsStorageService.retrieveBibTree(bibId));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred while doing bulk update of Holdings  ", e);
            throw e;
        }
    }

    @Override
    public void bulkUpdateItem(Item item, List<String> itemIds, String canUpdateStaffOnlyFlag) {
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
        List<String> bibIds = new ArrayList<>();
        try {
            for (String itemId : itemIds) {
                Item existingItem = (Item) getDocstoreStorageService().retrieveItem(itemId);
                if (existingItem != null) {
                    org.kuali.ole.docstore.common.document.content.instance.Item existingItemContent = itemOlemlRecordProcessor.fromXML(existingItem.getContent());
                    if (itemContent != null && itemContent.getLocation() != null &&
                            itemContent.getLocation().getLocationLevel() != null &&
                            itemContent.getLocation().getLocationLevel().getName() != null &&
                            !itemContent.getLocation().getLocationLevel().getName().isEmpty()) {
                        if (existingItemContent.getLocation() != null && existingItemContent.getLocation().getLocationLevel() != null) {
                            existingItemContent.setLocation(itemContent.getLocation());
                        } else {
                            existingItemContent.setLocation(itemContent.getLocation());
                        }

                    }
                    if (canUpdateStaffOnlyFlag.equalsIgnoreCase("true")) {
                        existingItemContent.setStaffOnlyFlag(item.isStaffOnly());
                    }
                    if (itemContent.getCallNumber() != null) {
                        if (existingItemContent.getCallNumber() != null) {
                            if (StringUtils.isNotBlank(itemContent.getCallNumber().getPrefix())) {
                                existingItemContent.getCallNumber().setPrefix(itemContent.getCallNumber().getPrefix());
                            }
                            if (StringUtils.isNotBlank(itemContent.getCallNumber().getNumber())) {
                                existingItemContent.getCallNumber().setNumber(itemContent.getCallNumber().getNumber());
                                if (itemContent.getCallNumber().getShelvingScheme() != null &&
                                        StringUtils.isNotBlank(itemContent.getCallNumber().getShelvingScheme().getCodeValue()) && !itemContent.getCallNumber().getShelvingScheme().getCodeValue().equalsIgnoreCase("NOINFO")) {
                                    existingItemContent.getCallNumber().setShelvingScheme(itemContent.getCallNumber().getShelvingScheme());
                                }
                            }
                            if (itemContent.getCallNumber().getShelvingOrder() != null &&
                                    StringUtils.isNotBlank(itemContent.getCallNumber().getShelvingOrder().getFullValue())) {
                                existingItemContent.getCallNumber().setShelvingOrder(itemContent.getCallNumber().getShelvingOrder());
                            }
                        } else {
                            existingItemContent.setCallNumber(itemContent.getCallNumber());
                        }
                    }
                    if (StringUtils.isNotBlank(itemContent.getEnumeration())) {
                        existingItemContent.setEnumeration(itemContent.getEnumeration());
                    }
                    if (itemContent.getAccessInformation() != null) {
                        if (itemContent.getAccessInformation().getBarcode() != null && !itemContent.getAccessInformation().getBarcode().isEmpty()) {
                            if (existingItemContent.getAccessInformation() != null && existingItemContent.getAccessInformation().getBarcode() != null) {
                                existingItemContent.getAccessInformation().setBarcode(itemContent.getAccessInformation().getBarcode());
                            } else {
                                existingItemContent.setAccessInformation(itemContent.getAccessInformation());
                            }
                        }
                        if (itemContent.getAccessInformation().getUri() != null &&
                                itemContent.getAccessInformation().getUri().getValue() != null && !itemContent.getAccessInformation().getUri().getValue().isEmpty()) {
                            existingItemContent.setAccessInformation(itemContent.getAccessInformation());
                        }
                    }
                    if (itemContent.getChronology() != null && !itemContent.getChronology().isEmpty()) {
                        existingItemContent.setChronology(itemContent.getChronology());
                    }
                    if (itemContent.getBarcodeARSL() != null && !itemContent.getBarcodeARSL().isEmpty()) {
                        existingItemContent.setBarcodeARSL(itemContent.getBarcodeARSL());
                    }
                    if (itemContent.getCopyNumber() != null && !itemContent.getCopyNumber().isEmpty()) {
                        existingItemContent.setCopyNumber(itemContent.getCopyNumber());
                    }
                    if (itemContent.getFormerIdentifier() != null && itemContent.getFormerIdentifier().size() > 0 &&
                            itemContent.getFormerIdentifier().get(0) != null && itemContent.getFormerIdentifier().get(0).getIdentifier() != null
                            && itemContent.getFormerIdentifier().get(0).getIdentifier().getIdentifierValue() != null) {
                        existingItemContent.setFormerIdentifier(itemContent.getFormerIdentifier());
                    }
                    if (itemContent.getStatisticalSearchingCode() != null && itemContent.getStatisticalSearchingCode().size() > 0 &&
                            itemContent.getStatisticalSearchingCode().get(0).getCodeValue() != null) {
                        existingItemContent.setStatisticalSearchingCode(itemContent.getStatisticalSearchingCode());
                    }
                    if (itemContent.getItemType() != null && itemContent.getItemType().getCodeValue() != null && !itemContent.getItemType().getCodeValue().isEmpty()) {
                        existingItemContent.setItemType(itemContent.getItemType());
                    }
                    if (itemContent.getTemporaryItemType() != null && itemContent.getTemporaryItemType().getCodeValue() != null &&
                            !itemContent.getTemporaryItemType().getCodeValue().isEmpty()) {
                        existingItemContent.setTemporaryItemType(itemContent.getTemporaryItemType());
                    }
                    if (itemContent.getNumberOfPieces() != null && !itemContent.getNumberOfPieces().isEmpty()) {
                        existingItemContent.setNumberOfPieces(itemContent.getNumberOfPieces());
                    }
                    if (itemContent.getPrice() != null) {
                        existingItemContent.setPrice(itemContent.getPrice());
                    }
                    if (itemContent.getDonorInfo() != null && itemContent.getDonorInfo().size() > 0) {
                        List<DonorInfo> donorInfos = existingItemContent.getDonorInfo();
                        if (donorInfos != null && donorInfos.size() > 0) {
                            for (DonorInfo donorInfo : itemContent.getDonorInfo()) {
                                donorInfos.add(donorInfo);
                            }
                            existingItemContent.setDonorInfo(donorInfos);
                        } else {

                            for (DonorInfo donorInfo : itemContent.getDonorInfo()) {
                                donorInfos.add(donorInfo);
                            }
                            if (donorInfos != null && donorInfos.size() > 0)
                                existingItemContent.setDonorInfo(donorInfos);

                        }
                    }
                    if(itemContent.getItemClaimsReturnedRecords() != null && itemContent.getItemClaimsReturnedRecords().size() > 0){
                        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords = existingItemContent.getItemClaimsReturnedRecords();
                        if(itemClaimsReturnedRecords != null && itemClaimsReturnedRecords.size() > 0){
                            for(ItemClaimsReturnedRecord itemClaimsReturnedRecord : itemContent.getItemClaimsReturnedRecords()){
                                itemClaimsReturnedRecords.add(itemClaimsReturnedRecord);
                            }
                            existingItemContent.setItemClaimsReturnedRecords(itemClaimsReturnedRecords);
                        } else {
                            for(ItemClaimsReturnedRecord itemClaimsReturnedRecord : itemContent.getItemClaimsReturnedRecords()){
                                itemClaimsReturnedRecords.add(itemClaimsReturnedRecord);
                            }
                            if(itemClaimsReturnedRecords != null && itemClaimsReturnedRecords.size() > 0)
                                existingItemContent.setItemClaimsReturnedRecords(itemClaimsReturnedRecords);
                        }
                    }
                    if(itemContent.getItemDamagedRecords() != null && itemContent.getItemDamagedRecords().size() > 0){
                        List<ItemDamagedRecord> itemDamagedRecords = existingItemContent.getItemDamagedRecords();
                        if(itemDamagedRecords != null && itemDamagedRecords.size() > 0){
                            for(ItemDamagedRecord itemDamagedRecord : itemContent.getItemDamagedRecords()){
                                itemDamagedRecords.add(itemDamagedRecord);
                            }
                            existingItemContent.setItemDamagedRecords(itemDamagedRecords);
                        } else {
                            for(ItemDamagedRecord itemDamagedRecord : itemContent.getItemDamagedRecords()){
                                itemDamagedRecords.add(itemDamagedRecord);
                            }
                            if(itemDamagedRecords != null && itemDamagedRecords.size() > 0)
                                existingItemContent.setItemDamagedRecords(itemDamagedRecords);
                        }
                    }
                    if(itemContent.getMissingPieceItemRecordList() != null && itemContent.getMissingPieceItemRecordList().size() > 0){
                        List<MissingPieceItemRecord> missingPieceItemRecords = existingItemContent.getMissingPieceItemRecordList();
                        if(missingPieceItemRecords != null && missingPieceItemRecords.size()>0){
                            for(MissingPieceItemRecord missingPieceItemRecord : itemContent.getMissingPieceItemRecordList()){
                                missingPieceItemRecords.add(missingPieceItemRecord);
                            }
                            existingItemContent.setMissingPieceItemRecordList(missingPieceItemRecords);
                        } else {
                            for(MissingPieceItemRecord missingPieceItemRecord : itemContent.getMissingPieceItemRecordList()){
                                missingPieceItemRecords.add(missingPieceItemRecord);
                            }
                            if(missingPieceItemRecords != null && missingPieceItemRecords.size()>0){
                                existingItemContent.setMissingPieceItemRecordList(missingPieceItemRecords);
                            }
                        }
                    }


                    if (itemContent.getCheckinNote() != null && !itemContent.getCheckinNote().isEmpty()) {
                        existingItemContent.setCheckinNote(itemContent.getCheckinNote());
                    }
                    if (itemContent.isFastAddFlag()) {
                        existingItemContent.setFastAddFlag(itemContent.isFastAddFlag());
                    }
                    if (itemContent.isClaimsReturnedFlag()) {
                        existingItemContent.setClaimsReturnedFlag(itemContent.isClaimsReturnedFlag());
                        if (itemContent.getClaimsReturnedFlagCreateDate() != null) {
                            existingItemContent.setClaimsReturnedFlagCreateDate(itemContent.getClaimsReturnedFlagCreateDate());
                        }
                        if (itemContent.getClaimsReturnedNote() != null) {
                            existingItemContent.setClaimsReturnedNote(itemContent.getClaimsReturnedNote());
                        }
                    }
                    if (itemContent.isItemDamagedStatus()) {
                        existingItemContent.setItemDamagedStatus(itemContent.isItemDamagedStatus());
                        if (itemContent.getDamagedItemNote() != null) {
                            existingItemContent.setDamagedItemNote(itemContent.getDamagedItemNote());
                        }
                    }
                    if (itemContent.isMissingPieceFlag()) {
                        existingItemContent.setMissingPieceFlag(itemContent.isMissingPieceFlag());
                        if (itemContent.getMissingPiecesCount() != null) {
                            existingItemContent.setMissingPiecesCount(itemContent.getMissingPiecesCount());
                        }
                        if (itemContent.getMissingPieceFlagNote() != null) {
                            existingItemContent.setMissingPieceFlagNote(itemContent.getMissingPieceFlagNote());
                        }
                    }
                    if (itemContent.getNote() != null && itemContent.getNote().size() > 0) {
                        List<Note> notes = existingItemContent.getNote();
                        if (notes != null) {
                            for (Note note : itemContent.getNote()) {
                                if (note.getType() != null && note.getValue() != null && !note.getValue().isEmpty()) {
                                    notes.add(note);
                                }
                                existingItemContent.setNote(notes);
                            }
                        } else {

                            for (Note note : itemContent.getNote()) {
                                if (note.getType() != null && note.getValue() != null && !note.getValue().isEmpty()) {
                                    notes.add(note);
                                }
                            }
                            if (notes != null && notes.size() > 0)
                                existingItemContent.setNote(notes);

                        }
                    }
                    if(StringUtils.isNotBlank(item.getUpdatedBy())){
                        existingItem.setUpdatedBy(item.getUpdatedBy());
                    }
                    if (itemContent.getHighDensityStorage() != null &&
                            itemContent.getHighDensityStorage().getRow() != null) {
                        existingItemContent.setHighDensityStorage(itemContent.getHighDensityStorage());
                    }
                    if (itemContent.getItemStatus() != null) {
                        if (StringUtils.isNotBlank(itemContent.getItemStatus().getCodeValue()) || StringUtils.isNotBlank(itemContent.getItemStatus().getFullValue())) {
                            existingItemContent.setItemStatus(itemContent.getItemStatus());
                            existingItemContent.setItemStatusEffectiveDate(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));
                        }
                    }
                    if (canUpdateStaffOnlyFlag.equalsIgnoreCase("true")) {
                        existingItem.setStaffOnly(item.isStaffOnly());
                    }
                    existingItem.setCategory(item.getCategory());
                    existingItem.setType(item.getType());
                    existingItem.setFormat(item.getFormat());
                    existingItem.setContent(itemOlemlRecordProcessor.toXML(existingItemContent));
                    try {
                        updateItem(existingItem);
                    } catch(Exception ex){
                        bibIds.add(DocumentUniqueIDPrefix.getDocumentId(existingItem.getHolding().getBib().getId()));
                    }

                } else {
                    DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ITEM_ID_NOT_FOUND, DocstoreResources.ITEM_ID_NOT_FOUND);
                    docstoreException.addErrorParams("itemId", itemId);
                    throw docstoreException;
                }
            }
            if(bibIds.size()>0){
                BibMarcIndexer bibMarcIndexer = new BibMarcIndexer();
                DocstoreRDBMSStorageService rdbmsStorageService = new DocstoreRDBMSStorageService();
                for(String bibId : bibIds) {
                    bibMarcIndexer.createTree(rdbmsStorageService.retrieveBibTree(bibId));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred while doing bulk update for item ", e);
            throw e;
        }
    }

    @Override
    public Item retrieveItemByBarcode(String barcode) {
        Item item = getDocstoreStorageService().retrieveItemByBarcode(barcode);
        return item;
    }

    private void setPHoldingInformation(OleHoldings oleHoldings, OleHoldings existingOleHoldings) {
        if ((oleHoldings.getCopyNumber()!=null) && (StringUtils.isNotBlank(oleHoldings.getCopyNumber()))) {
            existingOleHoldings.setCopyNumber(oleHoldings.getCopyNumber());
        }
        if (oleHoldings.getExtentOfOwnership() != null && oleHoldings.getExtentOfOwnership().size() > 0) {
            List<ExtentOfOwnership> extentOfOwnerships = existingOleHoldings.getExtentOfOwnership();
            if (extentOfOwnerships != null && extentOfOwnerships.size() > 0) {
                for (ExtentOfOwnership extentOfOwnership : oleHoldings.getExtentOfOwnership()) {
                    if (extentOfOwnership.getType() != null && extentOfOwnership.getNote() != null && extentOfOwnership.getTextualHoldings() != null) {

                        extentOfOwnerships.add(extentOfOwnership);
                    }
                }
                existingOleHoldings.setExtentOfOwnership(extentOfOwnerships);
            } else {

                for (ExtentOfOwnership extentOfOwnership : oleHoldings.getExtentOfOwnership()) {
                    if (extentOfOwnership.getType() != null && extentOfOwnership.getNote() != null && extentOfOwnership.getTextualHoldings() != null) {
                        extentOfOwnerships.add(extentOfOwnership);
                    }
                }
                if (extentOfOwnerships != null && extentOfOwnerships.size() > 0)
                    existingOleHoldings.setExtentOfOwnership(extentOfOwnerships);

            }
        }
        if (oleHoldings.getReceiptStatus() != null) {
            existingOleHoldings.setReceiptStatus(oleHoldings.getReceiptStatus());
        }

        if (oleHoldings.getUri() != null && oleHoldings.getUri().size() > 0) {
            List<Uri> uriList = existingOleHoldings.getUri();
            if (uriList != null && uriList.size() > 0) {
                for (Uri uri : oleHoldings.getUri()) {
                    if (uri.getValue() != null && !uri.getValue().isEmpty()) {

                        uriList.add(uri);
                    }
                }
                existingOleHoldings.setUri(uriList);
            } else {
                for (Uri uri : oleHoldings.getUri()) {
                    if (uri.getValue() != null && !uri.getValue().isEmpty()) {
                        uriList.add(uri);
                    }
                }
                if (uriList != null && uriList.size() > 0)
                    existingOleHoldings.setUri(uriList);
            }
        }
    }

    private void setEHoldingInformation(OleHoldings oleHoldings, OleHoldings existingOleHoldings) {

        if (oleHoldings.getAccessStatus() != null) {
            existingOleHoldings.setAccessStatus(oleHoldings.getAccessStatus());
        }
        if (oleHoldings.getPlatform() != null) {
            if (existingOleHoldings.getPlatform() != null) {
                if (oleHoldings.getPlatform().getPlatformName() != null && !oleHoldings.getPlatform().getPlatformName().isEmpty()) {
                    existingOleHoldings.getPlatform().setPlatformName(oleHoldings.getPlatform().getPlatformName());
                }
                if (oleHoldings.getPlatform().getAdminUrl() != null && !oleHoldings.getPlatform().getAdminUrl().isEmpty()) {
                    existingOleHoldings.getPlatform().setAdminUrl(oleHoldings.getPlatform().getAdminUrl());
                }
                if (oleHoldings.getPlatform().getAdminPassword() != null && !oleHoldings.getPlatform().getAdminPassword().isEmpty()) {
                    existingOleHoldings.getPlatform().setAdminPassword(oleHoldings.getPlatform().getAdminPassword());
                }
                if (oleHoldings.getPlatform().getAdminUserName() != null && !oleHoldings.getPlatform().getAdminUserName().isEmpty()) {
                    existingOleHoldings.getPlatform().setAdminUserName(oleHoldings.getPlatform().getAdminUserName());
                }
            } else {
                existingOleHoldings.setPlatform(oleHoldings.getPlatform());
            }
        }
        if (oleHoldings.getStatusDate() != null && !oleHoldings.getStatusDate().isEmpty()) {
            existingOleHoldings.setStatusDate(oleHoldings.getStatusDate());
        }
        if (oleHoldings.getPublisher() != null && !oleHoldings.getPublisher().isEmpty()) {
            existingOleHoldings.setPublisher(oleHoldings.getPublisher());
        }
        /*if (oleHoldings.isStaffOnlyFlag()) {

        }*/
        if (oleHoldings.getImprint() != null && !oleHoldings.getImprint().isEmpty()) {
            existingOleHoldings.setImprint(oleHoldings.getImprint());
        }
        if (oleHoldings.getStatisticalSearchingCode() != null && oleHoldings.getStatisticalSearchingCode().getCodeValue() != null &&
                !oleHoldings.getStatisticalSearchingCode().getCodeValue().isEmpty()) {
            if (existingOleHoldings.getStatisticalSearchingCode() != null) {
                existingOleHoldings.getStatisticalSearchingCode().setCodeValue(oleHoldings.getStatisticalSearchingCode().getCodeValue());
            } else {
                existingOleHoldings.setStatisticalSearchingCode(oleHoldings.getStatisticalSearchingCode());
            }
        }
        if (oleHoldings.getExtentOfOwnership() != null &&
                oleHoldings.getExtentOfOwnership().size() > 0) {
            List<ExtentOfOwnership> existingExtendOfOwnerShip = existingOleHoldings.getExtentOfOwnership();
            if (existingExtendOfOwnerShip != null && existingExtendOfOwnerShip.size() > 0) {
                for (ExtentOfOwnership extendOwnership : oleHoldings.getExtentOfOwnership()) {
                    List<Coverage> extendCoverages = new ArrayList<>();
                    if ((extendOwnership.getCoverages() != null && extendOwnership.getCoverages().getCoverage() != null &&
                            extendOwnership.getCoverages().getCoverage().size() > 0) ||
                            (extendOwnership.getPerpetualAccesses() != null && extendOwnership.getPerpetualAccesses().getPerpetualAccess() != null &&
                                    extendOwnership.getPerpetualAccesses().getPerpetualAccess().size() > 0)) {
                        existingExtendOfOwnerShip.add(extendOwnership);
                    }
                }
                existingOleHoldings.setExtentOfOwnership(existingExtendOfOwnerShip);
            } else {
                existingOleHoldings.setExtentOfOwnership(oleHoldings.getExtentOfOwnership());
            }
        }
        if (oleHoldings.getSubscriptionStatus() != null && !oleHoldings.getSubscriptionStatus().isEmpty()) {
            existingOleHoldings.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
        }
        if (oleHoldings.getDonorInfo() != null && oleHoldings.getDonorInfo().size() > 0) {
            List<DonorInfo> donorInfos = existingOleHoldings.getDonorInfo();
            if (donorInfos != null && donorInfos.size() > 0) {
                for (DonorInfo donorInfo : oleHoldings.getDonorInfo()) {
                    donorInfos.add(donorInfo);
                }
                existingOleHoldings.setDonorInfo(donorInfos);
            } else {

                for (DonorInfo donorInfo : oleHoldings.getDonorInfo()) {
                    donorInfos.add(donorInfo);
                }
                if (donorInfos != null && donorInfos.size() > 0)
                    existingOleHoldings.setDonorInfo(donorInfos);
            }

        }

        if (oleHoldings.getHoldingsAccessInformation() != null &&
                ((oleHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser() != null && !oleHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser().isEmpty()) ||
                        (oleHoldings.getHoldingsAccessInformation().getAccessUsername() != null && !oleHoldings.getHoldingsAccessInformation().getAccessUsername().isEmpty()) ||
                        (oleHoldings.getHoldingsAccessInformation().getAccessPassword() != null && !oleHoldings.getHoldingsAccessInformation().getAccessPassword().isEmpty()) ||
                        (oleHoldings.getHoldingsAccessInformation().getAuthenticationType() != null && !oleHoldings.getHoldingsAccessInformation().getAuthenticationType().isEmpty()) ||
                        (oleHoldings.getHoldingsAccessInformation().getProxiedResource() != null && !oleHoldings.getHoldingsAccessInformation().getProxiedResource().isEmpty()) ||
                        (oleHoldings.getHoldingsAccessInformation().getMaterialsSpecified() != null && !oleHoldings.getHoldingsAccessInformation().getMaterialsSpecified().isEmpty()) ||
                        (oleHoldings.getHoldingsAccessInformation().getFirstIndicator() != null && !oleHoldings.getHoldingsAccessInformation().getFirstIndicator().isEmpty()) ||
                        (oleHoldings.getHoldingsAccessInformation().getSecondIndicator() != null && !oleHoldings.getHoldingsAccessInformation().getSecondIndicator().isEmpty()) ||
                        (oleHoldings.getHoldingsAccessInformation().getAccessLocation() != null && !oleHoldings.getHoldingsAccessInformation().getAccessLocation().isEmpty()))) {
            existingOleHoldings.setHoldingsAccessInformation(oleHoldings.getHoldingsAccessInformation());
        }
        if (oleHoldings.getLocalPersistentLink() != null && !oleHoldings.getLocalPersistentLink().isEmpty()) {
            existingOleHoldings.setLocalPersistentLink(oleHoldings.getLocalPersistentLink());
        }
        if (oleHoldings.getLink() != null && oleHoldings.getLink().size() > 0) {
            List<Link> linkList = existingOleHoldings.getLink();
            for (Link link : oleHoldings.getLink()) {
                linkList.add(link);
            }
            if (linkList != null && linkList.size() > 0)
                existingOleHoldings.setLink(linkList);
        }
        if (oleHoldings.isInterLibraryLoanAllowed()) {
            existingOleHoldings.setInterLibraryLoanAllowed(oleHoldings.isInterLibraryLoanAllowed());
        }

    }

    @Override
    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        try {
            getDocstoreStorageService().breakAnalyticsRelation(seriesHoldingsId, itemIds);
        } catch (Exception e) {
            LOG.error("Exception occurred breaking analytical relation ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().breakAnalyticsRelation(seriesHoldingsId, itemIds);
        } catch (Exception e) {
            docstoreStorageService.rollback();
            LOG.error("Exception occurred indexing of break analytical relation ", e);
            throw e;
        }
    }

    /**
     * This method is used for batch updates to bibs, holdings and items.
     * If any operation fails, it is recorded at the document level, and next document is processed.
     * Ideally no exception should be thrown by this method.
     * @param bibTrees
     * @return
     */
    @Override
    public BibTrees processBibTrees(BibTrees bibTrees) {
        try {
            getDocstoreStorageService().processBibTrees(bibTrees);
            if(!"false".equalsIgnoreCase(getParameter())){
                getDocstoreIndexService().processBibTrees(bibTrees);
            }
        } catch (Exception e) {
            LOG.error("Exception occurred while processing bib trees ", e);
			 for(BibTree bibTree:bibTrees.getBibTrees()){
                Bib bib = bibTree.getBib();
                if(bib.getResult().equals(Holdings.ResultType.FAILURE)){
                    throw new DocstoreException("Exception while processing BIB "+bib.getMessage());
                }
                for(HoldingsTree holdingsTree:bibTree.getHoldingsTrees()){
                        Holdings holdings = holdingsTree.getHoldings();
                    if(holdings.getResult().equals(Holdings.ResultType.FAILURE)){
                        throw new DocstoreException("Exception while processing Holdings  :"+holdings.getMessage());
                    }
                    for(Item item:holdingsTree.getItems()){
                        if(item.getResult().equals(Holdings.ResultType.FAILURE)){
                            throw new DocstoreException("Exception while processing Item  :"+item.getMessage());
                        }
                    }
                }
            }
            throw e;
        }
        return bibTrees;
    }

    @Override
    public void unbindWithOneBib(List<String> holdingsIds, String bibId) {
        try {
            getDocstoreStorageService().unbindWithOneBib(holdingsIds, bibId);
        } catch (Exception e) {
            LOG.error("Exception occurred in unbinding holdings with bibs ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().unbindWithOneBib(holdingsIds, bibId);
        } catch (Exception e) {
            docstoreStorageService.rollback();
            LOG.error("Exception occurred while indexing the unbinded holdings with bibs ", e);
            throw e;
        }

    }

    @Override
    public void unbindWithAllBibs(List<String> holdingsIds, String bibId) {
        try {
            getDocstoreStorageService().unbindWithAllBibs(holdingsIds, bibId);
        } catch (Exception e) {
            LOG.error("Exception occurred in unbinding holdings with bibs ", e);
            throw e;
        }
        try {
            getDocstoreIndexService().unbindWithAllBibs(holdingsIds, bibId);
        } catch (Exception e) {
            docstoreStorageService.rollback();
            LOG.error("Exception occurred while indexing the unbinded holdings with bibs ", e);
            throw e;
        }
    }
	
	/**
     * This method is used for batch updates to bibs, holdings and items.
     * If any operation fails, it is recorded at the document level, and next document is processed.
     * Ideally no exception should be thrown by this method.
     * @param bibTrees
     * @return
     */
    @Override
    public BibTrees processBibTreesForBatch(BibTrees bibTrees) {
        try {
            getDocstoreStorageService().processBibTreesForBatch(bibTrees);
            getDocstoreIndexService().processBibTrees(bibTrees);

        } catch (Exception e) {
            LOG.error("Exception occurred while processing bib trees ", e);
			 for(BibTree bibTree:bibTrees.getBibTrees()){
                Bib bib = bibTree.getBib();
                if(bib.getResult().equals(Holdings.ResultType.FAILURE)){
                    throw new DocstoreException("Exception while processing BIB "+bib.getMessage());
                }
                for(HoldingsTree holdingsTree:bibTree.getHoldingsTrees()){
                        Holdings holdings = holdingsTree.getHoldings();
                    if(holdings.getResult().equals(Holdings.ResultType.FAILURE)){
                        throw new DocstoreException("Exception while processing Holdings  :"+holdings.getMessage());
                    }
                    for(Item item:holdingsTree.getItems()){
                        if(item.getResult().equals(Holdings.ResultType.FAILURE)){
                            throw new DocstoreException("Exception while processing Item  :"+item.getMessage());
                        }
                    }
                }
            }
            throw e;
        }
        return bibTrees;
    }

}
