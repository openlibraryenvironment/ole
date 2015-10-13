package org.kuali.ole.deliver.util;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.util.BatchBibImportUtil;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.sys.context.SpringContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 8/12/15.
 */
public class BulkItemUpdateUtil {
    private DocstoreClientLocator docstoreClientLocator;

    public Map<String, Map> updateItemsToDocstore(List<Item> itemList) {
        org.kuali.ole.docstore.common.document.BibTrees bibTrees = new org.kuali.ole.docstore.common.document.BibTrees();

        org.kuali.ole.docstore.common.document.BibTree bibTree = new org.kuali.ole.docstore.common.document.BibTree();
        org.kuali.ole.docstore.common.document.Bib bib = new org.kuali.ole.docstore.common.document.Bib();
        bibTree.setBib(bib);
        bib.setId("");

        org.kuali.ole.docstore.common.document.HoldingsTree holdingsTree = new org.kuali.ole.docstore.common.document.HoldingsTree();
        org.kuali.ole.docstore.common.document.Holdings holdings = new org.kuali.ole.docstore.common.document.Holdings();
        holdings.setId("");
        holdingsTree.setHoldings(holdings);

        List<String> locationLevel = BatchBibImportUtil.getLocationLevel();

        for (Iterator<Item> iterator = itemList.iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            if(CollectionUtils.isNotEmpty(locationLevel)) {
                populateLocationLevels(item,locationLevel);
            }
            item.buildLocationLevels((org.kuali.ole.docstore.common.document.content.instance.Item) item.getContentObject());
            item.setOperation(org.kuali.ole.docstore.common.document.DocstoreDocument.OperationType.UPDATE);
            item.serializeContent();
            holdingsTree.getItems().add(item);
        }

        bibTree.getHoldingsTrees().add(holdingsTree);

        bibTrees.getBibTrees().add(bibTree);
        BibTrees responseBibTree = null;
        try {
            responseBibTree = getDocstoreClientLocator().getDocstoreClient().processBibTrees(bibTrees);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Map> statusMap = generateResultMapForBibTreeWithItemUuidAndResultType(responseBibTree);
        return statusMap;
    }

    private void populateLocationLevels(Item item, List<String> locationLevel) {
        item.setLevel1Location(locationLevel.get(0));
        item.setLevel2Location(locationLevel.get(1));
        item.setLevel3Location(locationLevel.get(2));
        item.setLevel4Location(locationLevel.get(3));
        item.setLevel5Location(locationLevel.get(4));
    }

    private Map<String, Map> generateResultMapForBibTreeWithItemUuidAndResultType(BibTrees bibTrees) {
        Map<String, Map> resultTypeMap = new HashMap<>();
        if(null != bibTrees){
            List<BibTree> bibTreeList = bibTrees.getBibTrees();
            if(CollectionUtils.isNotEmpty(bibTreeList)){
                for (Iterator<BibTree> bibTreeIterator = bibTreeList.iterator(); bibTreeIterator.hasNext(); ) {
                    BibTree bibTree = bibTreeIterator.next();
                    List<HoldingsTree> holdingsTrees = bibTree.getHoldingsTrees();
                    if(CollectionUtils.isNotEmpty(holdingsTrees)){
                        for (Iterator<HoldingsTree> holdingsTreeIterator = holdingsTrees.iterator(); holdingsTreeIterator.hasNext(); ) {
                            HoldingsTree holdingsTree = holdingsTreeIterator.next();
                            List<Item> items = holdingsTree.getItems();
                            if(CollectionUtils.isNotEmpty(items)){
                                for (Iterator<Item> itemIterator = items.iterator(); itemIterator.hasNext(); ) {
                                    Item item = itemIterator.next();
                                    Map statusMap = new HashMap();
                                    statusMap.put("result", item.getResult());
                                    statusMap.put("message", item.getMessage());
                                    statusMap.put("barcode", item.getBarcode());
                                    resultTypeMap.put(item.getId(),statusMap);
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultTypeMap;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if(null == docstoreClientLocator){
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
        this.docstoreClientLocator = docstoreClientLocator;
    }
}
