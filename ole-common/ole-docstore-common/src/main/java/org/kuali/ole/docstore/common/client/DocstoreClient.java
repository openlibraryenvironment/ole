package org.kuali.ole.docstore.common.client;

import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.search.BrowseParams;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Venkatasrinath
 * Date: 12/16/13
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocstoreClient {

    public void createBib(Bib bib);

    public void createHoldings(Holdings holdings);

    public void createItem(Item item);

    public void createHoldingsTree(HoldingsTree holdingsTree);


    public void createBibTree(BibTree bibTree);

    public Bib retrieveBib(String bibId);

    public Holdings retrieveHoldings(String holdingsId);

    public Item retrieveItem(String itemId);

    public HoldingsTree retrieveHoldingsTree(String holdingsId);

    public BibTree retrieveBibTree(String bibId);

    public BibTrees retrieveBibTrees(List<String> bibIds);

    public Bib updateBib(Bib bib);



    public Holdings updateHoldings(Holdings holdings);


    public Item updateItem(Item item);



    public void deleteBib(String bibId);

    public void deleteHoldings(String holdingsId);

    public void deleteItem(String itemId);

    public void deleteItems(List<String> itemIds);

    public SearchResponse search(SearchParams searchParams);

    public Bib findBib(Map<String, String> map);

    public BibTree findBibTree(Map<String, String> map);

    public Holdings findHoldings(Map<String, String> map);

    public HoldingsTree findHoldingsTree(Map<String, String> map);

    public Item findItem(Map<String, String> map);

    public SearchResponse browseItems(BrowseParams browseParams);

    public SearchResponse browseHoldings(BrowseParams browseParams);

    public void boundWithBibs(String holdingsId, List<String> bibIds);

    public void transferHoldings(List<String> holdingsIds, String bibId);

    public void transferItems(List<String> itemIds, String bibId);

    public void deleteBibs(List<String> bibIds);

    public List<Bib> retrieveBibs(List<String> bibIds);

    public List<Item> retrieveItems(List<String> itemIds);

    public HashMap<String,Item> retrieveItemMap(List<String> itemIds);

    public void createLicenses(Licenses licenses);

    public License retrieveLicense(String id);

    public Licenses retrieveLicenses(List<String> ids);

    public void updateLicense(License license);

    public void updateLicenses(Licenses licenses);

    public void deleteLicense(String id);

    public void createAnalyticsRelation(String seriesHoldingsId, List<String> itemIds);
    
    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds);

    public void bulkUpdateHoldings(Holdings holdings, List<String> holdingIds,String canUpdateStaffOnlyFlag);

    public void bulkUpdateItem(Item item, List<String> itemIds,String canUpdateStaffOnlyFlag);

    public BibMarcRecords retrieveBibContent(List<String> bibIds);

    public String patchItem(String requestBody);

    public String updateItemByBarcode(String barcode, String requestBody);

    public HoldingsTrees retrieveHoldingsTrees(List<String> bibIds);

    public HoldingsTrees retrieveHoldingsDocTrees(List<String> bibIds);

    public Item retrieveItemByBarcode(String barcode);

    public void reloadConfiguration();

    public BibTrees processBibTrees(BibTrees bibTrees);
}
