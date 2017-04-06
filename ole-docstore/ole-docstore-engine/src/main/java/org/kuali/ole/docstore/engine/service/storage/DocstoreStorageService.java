package org.kuali.ole.docstore.engine.service.storage;

import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocstoreStorageService {

    public void createBib(Bib bib);

    public void createHoldings(Holdings holdings);

    public void createItem(Item item);

    public void createHoldingsTree(HoldingsTree holdingsTree);

    public void createBibTree(BibTree bibTree);

    public Bib retrieveBib(String bibId);

    public List<Bib> retrieveBibs(List<String> bibIds);

    public List<Item> retrieveItems(List<String> itemIds);

    public HashMap<String,Item> retrieveItemMap(List<String> itemIds);

    public Holdings retrieveHoldings(String holdingsId);

    public Item retrieveItem(String itemId);

    public HoldingsTree retrieveHoldingsTree(String holdingsId);

    public BibTree retrieveBibTree(String bibId);

    public List<BibTree> retrieveBibTrees(List<String> bibIds);

    public void updateBib(Bib bib);

    public void updateBibs(List<Bib> bibs);

    public void updateHoldings(Holdings holdings);

    public void updateItem(Item item);

    public void deleteBib(String bibId);

    public void deleteHoldings(String holdingsId);

    public void deleteItem(String itemId);

    public void rollback();

    public void boundHoldingsWithBibs(String holdingsId, List<String> bibIds);

    public void transferHoldings(List<String> holdingsIds, String bibId);

    public void transferItems(List<String> itemIds, String holdingsId);

    public void createBibTrees(BibTrees bibTrees);

    public void deleteBibs(List<String> bibIds);

    public void validateInput(Object object);

    public void createLicense(License license);

    public void createLicenses(Licenses licenses);

    public Object retrieveLicense(String licenseId);

    public Licenses retrieveLicenses(List<String> licenseIds);

    public void updateLicense(License license);

    public void updateLicenses(Licenses licenses);

    public void deleteLicense(String licenseId);

    public void createAnalyticsRelation(String seriesHoldingsId, List<String> itemIds);

    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds);

    public Item retrieveItemByBarcode(String barcode);

    public void processBibTrees(BibTrees bibTrees);
	
	public void processBibTreesForBatch(BibTrees bibTrees);

    public void unbindWithOneBib(List<String> holdingsIds, String bibId);

    public void unbindWithAllBibs(List<String> holdingsIds, String bibId);

    public void saveDeletedBibs(List<Bib> bibs) throws Exception;

    public void saveDeletedHolding(Holdings holdings);

    public void saveDeletedItem(Item item);
}
