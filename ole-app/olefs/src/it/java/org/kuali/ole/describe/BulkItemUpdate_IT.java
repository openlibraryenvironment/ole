package org.kuali.ole.describe;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.kuali.ole.OLERestBaseTestCase;
import org.kuali.ole.deliver.util.XMLFormatterUtil;
import org.kuali.ole.docstore.common.document.*;

import java.util.*;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by sheiksalahudeenm on 8/11/15.
 */
public class BulkItemUpdate_IT extends OLERestBaseTestCase {
    private String URL = DOCSTORE_APPLICATION_URL + "/documentrest/bib/process";

    @Test
    public void updateBulkItemToDocstoreTest(){

        Item documentItemObject = new Item();
        documentItemObject.setField(Item.DESTINATION_FIELD_ITEM_ITEM_BARCODE, "929");
        documentItemObject.setField(Item.DUE_DATE_TIME, "08/20/2015 02:36:00");
        documentItemObject.setId("wio-9470010");
        documentItemObject.serializeContent();

        Item documentItemObject2 = new Item();
        documentItemObject2.setField(org.kuali.ole.docstore.common.document.Item.DESTINATION_FIELD_ITEM_ITEM_BARCODE, "930");
        documentItemObject2.setField(Item.DUE_DATE_TIME, "08/20/2015 02:36:00");
        documentItemObject2.setId("wio-9470011");

        Item documentItemObject3 = new Item();
        documentItemObject3.setField(org.kuali.ole.docstore.common.document.Item.DESTINATION_FIELD_ITEM_ITEM_BARCODE, "930");
        documentItemObject3.setField(Item.DUE_DATE_TIME, "08/20/2015 02:36:00");
        documentItemObject3.setId("wio-947001111");

        List<Item> itemList = new ArrayList<>();
        itemList.add(documentItemObject);
        itemList.add(documentItemObject2);
        itemList.add(documentItemObject3);

        String response = updateItemsToDocstore(itemList);
        assertNotNull(response);
        BibTrees responseBibTrees = (BibTrees) BibTrees.deserialize(response);
        Map<String, Map> resultTypeMap = generateResultMapForBibTreeWithItemUuidAndResultType(responseBibTrees);
        for (Iterator<String> iterator = resultTypeMap.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Map map = resultTypeMap.get(key);
            for (Iterator itemIterator = map.keySet().iterator(); itemIterator.hasNext(); ) {
                String resultKey = (String) itemIterator.next();
                System.out.println(resultKey + "  :   " + map.get(resultKey));

            }
        }
    }

    private String updateItemsToDocstore(List<Item> itemList) {
        BibTrees bibTrees = new BibTrees();

        BibTree bibTree = new BibTree();
        Bib bib = new Bib();
        bibTree.setBib(bib);
        bib.setId("");

        HoldingsTree holdingsTree = new HoldingsTree();
        Holdings holdings = new Holdings();
        holdings.setId("");
        holdingsTree.setHoldings(holdings);

        for (Iterator<Item> iterator = itemList.iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            item.setOperation(DocstoreDocument.OperationType.UPDATE);
            item.serializeContent();
            holdingsTree.getItems().add(item);
        }

        bibTree.getHoldingsTrees().add(holdingsTree);

        bibTrees.getBibTrees().add(bibTree);
        String serialize = bibTrees.serialize(bibTrees);
        String response = sendPostRequest(URL, serialize,"xml");
        return response;
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
}
