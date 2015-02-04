package org.kuali.ole.docstore.common.service;

import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.search.BrowseParams;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResultField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 6:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocstoreService {

    public void createBib(Bib bib);

    public void createHoldings(Holdings holdings);

    public void createItem(Item item);

    public void createHoldingsTree(HoldingsTree holdingsTree);

    public void createBibTree(BibTree bibTree);

    public Bib retrieveBib(String bibId);

    public List<Bib> retrieveBibs(List<String> bibIds);

    public Holdings retrieveHoldings(String holdingsId);

    public Item retrieveItem(String itemId);

    public List<Item> retrieveItems(List<String> itemIds);

    public HashMap<String,Item> retrieveItemMap(List<String> itemIds);

    public HoldingsTree retrieveHoldingsTree(String holdingsId);

    public BibTree retrieveBibTree(String bibId);

    public BibTrees retrieveBibTrees(List<String> bibIds);

    public void updateBib(Bib bib);

    public void updateBibs(List<Bib> bibs);

    public void updateHoldings(Holdings holdings);

    public void updateItem(Item item);

    public void deleteBib(String bibId);

    public void deleteHoldings(String holdingsId);

    public void deleteItem(String itemId);

    public SearchResponse search(SearchParams searchParams);

    public Bib findBib(Map<String, String> map);

    public BibTree findBibTree(Map<String, String> map);

    public Holdings findHoldings(Map<String, String> map);

    public HoldingsTree findHoldingsTree(Map<String, String> map);

    public Item findItem(Map<String, String> map);

    public void boundHoldingsWithBibs(String holdingsId, List<String> bibIds);

    public void transferHoldings(List<String> holdingsIds, String bibid);

    public void transferItems(List<String> itemIds, String bibid);

    public void createBibTrees(BibTrees bibTrees);

    public void deleteBibs(List<String> bibIds);

    public SearchResponse browseItems(BrowseParams browseParams);

    public SearchResponse browseHoldings(BrowseParams browseParams);

    public void setResultFieldsForHoldings(Holdings holdings, List<SearchResultField> searchResultFields);

    public void setResultFieldsForItem(Item itemDoc, List<SearchResultField> searchResultFields);

    public void setResultFieldsForBib(Bib bib, List<SearchResultField> searchResultFields);

    public void createLicense(License license);

    public void createLicenses(Licenses licenses);

    public License retrieveLicense(String licenseId);

    public Licenses retrieveLicenses(List<String> licenseIds);

    public void updateLicense(License license);

    public void updateLicenses(Licenses licenses);

    public void deleteLicense(String licenseId);

    public void createAnalyticsRelation(String seriesHoldingsId, List<String> itemIds);
    
    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds);

    public void bulkUpdateHoldings(Holdings holdings, List<String> holdingIds, String canUpdateStaffOnlyFlag);

    public void bulkUpdateItem(Item item, List<String> itemIds,String canUpdateStaffOnlyFlag);

    public Item retrieveItemByBarcode(String barcode);

    public BibTrees processBibTrees(BibTrees bibTrees);
}
