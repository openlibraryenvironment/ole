package org.kuali.ole.docstore.engine.client;

import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.search.BrowseParams;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.service.BeanLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Venkatasrinath
 * Date: 12/16/13
 * Time: 7:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreLocalClient implements DocstoreClient {

    private static DocstoreService ds = BeanLocator.getDocstoreService();

    @Override
    public void createBib(Bib bib) {
        ds.createBib(bib);
    }

    @Override
    public void createHoldings(Holdings holdings) {
        ds.createHoldings(holdings);
    }

    @Override
    public void createItem(Item item) {
        ds.createItem(item);
    }

    @Override
    public void createHoldingsTree(HoldingsTree holdingsTree) {
        ds.createHoldingsTree(holdingsTree);
    }



    @Override
    public void createBibTree(BibTree bibTree) {
        ds.createBibTree(bibTree);
    }

    @Override
    public Bib retrieveBib(String bibId) {
        return ds.retrieveBib(bibId);
    }

    @Override
    public Holdings retrieveHoldings(String holdingsId) {
        return ds.retrieveHoldings(holdingsId);
    }

    @Override
    public Item retrieveItem(String itemId) {
        return ds.retrieveItem(itemId);
    }

    @Override
    public HoldingsTree retrieveHoldingsTree(String retrieveHoldingsTree) {
        return ds.retrieveHoldingsTree(retrieveHoldingsTree);
    }

    @Override
    public BibTree retrieveBibTree(String bibId) {
        return ds.retrieveBibTree(bibId);
    }

    @Override
    public BibTrees retrieveBibTrees(List<String> bibIds) {
        return ds.retrieveBibTrees(bibIds);
    }

    @Override
    public Bib updateBib(Bib bib) {
        ds.updateBib(bib);
        return bib;
    }


    @Override
    public Holdings updateHoldings(Holdings holdings) {
        ds.updateHoldings(holdings);
        return holdings;
    }


    @Override
    public Item updateItem(Item item) {
        ds.updateItem(item);
        return item;
    }


    @Override
    public void deleteBib(String bibId) {
        ds.deleteBib(bibId);
    }

    @Override
    public void deleteHoldings(String holdingsId) {
        ds.deleteHoldings(holdingsId);
    }

    @Override
    public void deleteItem(String itemId) {
        ds.deleteItem(itemId);
    }

    @Override
    public void deleteItems(List<String> itemIds) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SearchResponse search(SearchParams searchParams) {
        return ds.search(searchParams);
    }

    @Override
    public Bib findBib(Map<String, String> map) {
        return ds.findBib(map);
    }

    @Override
    public BibTree findBibTree(Map<String, String> map) {
        return ds.findBibTree(map);
    }

    @Override
    public Holdings findHoldings(Map<String, String> map) {
        return ds.findHoldings(map);
    }

    @Override
    public HoldingsTree findHoldingsTree(Map<String, String> map) {
        return ds.findHoldingsTree(map);
    }

    @Override
    public Item findItem(Map<String, String> map) {
        return ds.findItem(map);
    }

    @Override
    public SearchResponse browseItems(BrowseParams browseParams) {
        return ds.browseItems(browseParams);
    }

    @Override
    public SearchResponse browseHoldings(BrowseParams browseParams) {
        return ds.browseHoldings(browseParams);
    }

    @Override
    public void boundWithBibs(String holdingsId, List<String> bibIds) {
        ds.boundHoldingsWithBibs(holdingsId, bibIds);
    }

    @Override
    public void transferHoldings(List<String> holdingsIds, String bibId) {
        ds.transferHoldings(holdingsIds, bibId);
    }

    @Override
    public void transferItems(List<String> itemIds, String bibId) {
        ds.transferItems(itemIds, bibId);
    }

    @Override
    public void deleteBibs(List<String> bibIds) {
        ds.deleteBibs(bibIds);
    }



    @Override
    public List<Bib> retrieveBibs(List<String> bibIds) {
        return ds.retrieveBibs(bibIds);
    }

    @Override
    public List<Item> retrieveItems(List<String> itemIds) {
        return ds.retrieveItems(itemIds);
    }

    @Override
    public HashMap<String, Item> retrieveItemMap(List<String> itemIds) {
        return ds.retrieveItemMap(itemIds);
    }

    @Override
    public void createLicenses(Licenses licenses) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public License retrieveLicense(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Licenses retrieveLicenses(List<String> ids) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateLicense(License license) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateLicenses(Licenses licenses) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteLicense(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        ds.createAnalyticsRelation(seriesHoldingsId, itemIds);
    }
    
    @Override
    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        ds.breakAnalyticsRelation(seriesHoldingsId, itemIds);
    }

    @Override
    public void bulkUpdateHoldings(Holdings holdings, List<String> holdingIds,String canUpdateStaffOnlyFlag) {
        ds.bulkUpdateHoldings(holdings,holdingIds,canUpdateStaffOnlyFlag);
    }

    @Override
    public void bulkUpdateItem(Item item, List<String> itemIds,String canUpdateStaffOnlyFlag) {
        ds.bulkUpdateItem(item, itemIds,canUpdateStaffOnlyFlag);
    }

    @Override
    public BibMarcRecords retrieveBibContent(List<String> bibIds) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String patchItem(String requestBody) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String updateItemByBarcode(String barcode, String requestBody) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HoldingsTrees retrieveHoldingsTrees(List<String> bibIds) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HoldingsTrees retrieveHoldingsDocTrees(List<String> bibIds) {
        return null;
    }

    @Override
    public Item retrieveItemByBarcode(String barcode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reloadConfiguration() {
        DocumentSearchConfig.reloadDocumentConfig();
    }

    @Override
    public BibTrees processBibTrees(BibTrees bibTrees) {
        return ds.processBibTrees(bibTrees);
    }

}
