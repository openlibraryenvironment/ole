package org.kuali.ole.docstore.engine.service.index;


import org.kuali.ole.docstore.common.document.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocstoreIndexService {

    public void createBib(Bib bib);

    public void createHoldings(Holdings holdings);

    public void createItem(Item item);

    public void createHoldingsTree(HoldingsTree holdingsTree);

    public void createBibTree(BibTree bibTree);

    public void updateBib(Bib bib);

    public void updateBibs(List<Bib> bibs);

    public void updateHoldings(Holdings holdings);

    public void updateItem(Item item);

    public void deleteBib(String bibId);

    public void deleteHoldings(String holdingsId);

    public void deleteItem(String itemId);

    public void boundHoldingsWithBibs(String holdingsId, List<String> bibIds);

    public void transferHoldings(List<String> holdingsIds, String bibId);

    public void transferItems(List<String> itemIds, String holdingsId);

    public void createBibTrees(BibTrees bibTrees);

    public void deleteBibs(List<String> bibIds);

    public void createLicense(License license);

    public void createLicenses(Licenses licenses);

    public void updateLicense(License license);

    public void updateLicenses(Licenses licenses);

    public void deleteLicense(String licenseId);

    public void createAnalyticsRelation(String seriesHoldingsId, List<String> itemIds);

    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds);

    public void processBibTrees(BibTrees bibTrees);
}