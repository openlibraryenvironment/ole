package org.kuali.ole.docstore.engine.service.rest;

import org.kuali.ole.docstore.common.client.DocstoreRestClient;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.model.enums.DocType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by jayabharathreddy on 26/7/14.
 */
public class TestUpdateAndSearchCals implements Callable<Long> {

    private DocstoreRestClient restClient = new DocstoreRestClient();
    ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
    private static final Logger LOG = LoggerFactory.getLogger(TestUpdateAndSearchCals.class);

    private String itemId;
    private String enumeration;
    private static Long count = new Long(0);

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public TestUpdateAndSearchCals(String itemId, String enumeration) {
        this.itemId = itemId;
        this.enumeration = enumeration;
    }

    @Override
    public Long call() throws Exception {
        count++;
        testUpdateAndSearch(itemId, enumeration);
//        updateItem(itemId, enumeration);
        return count;
    }



    public void testUpdateAndSearch(String itemId, String enumeration) {
        updateItem(itemId, enumeration);
//        searchItem(itemId, enumeration);
    }

    private void searchItem(String itemId, String enumeration) {

        SearchParams searchParams = new SearchParams();
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "id", itemId), "AND"));
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "Enumeration_search", enumeration), "AND"));
        SearchResponse response = restClient.search(searchParams);
//        LOG.info("no. of item with id " + itemId + " and with enumeration " + enumeration + " is " + response.getSearchResults().size());
        System.out.println("no. of item with id " + itemId + " and with enumeration " + enumeration + " is " + response.getSearchResults().size());
    }

    private void updateItem(String itemId, String enumeration) {
        try {
            Item item = restClient.retrieveItem(itemId);
            org.kuali.ole.docstore.common.document.content.instance.Item oleItem = itemOlemlRecordProcessor.fromXML(item.getContent());
            oleItem.setEnumeration(enumeration);
            item.setContent(itemOlemlRecordProcessor.toXML(oleItem));
            long startTime = System.currentTimeMillis();
            restClient.updateItem(item);
            long endTime = System.currentTimeMillis();
//            LOG.info("Updating the item with id " + itemId + " with enumeration " + enumeration);
            System.out.println("Updating the item with id " + itemId + " with enumeration " + enumeration + " and time taken is  " + (endTime-startTime));
        }
        catch (Exception e) {
            LOG.info("item not found " + itemId);
        }
    }

    private void searchItem(String itemId) {
        SearchParams searchParams = new SearchParams();
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "id", itemId), "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), "ENUMERATION"));
        SearchResponse response = restClient.search(searchParams);
        if(response.getSearchResults() != null && response.getSearchResults().size() > 0 && response.getSearchResults().get(0).getSearchResultFields() != null && response.getSearchResults().get(0).getSearchResultFields().size() > 0 && response.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue() != null) {
            enumeration = response.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue();
        }
        System.out.println("item id " + itemId + " with enumeration " + enumeration);

    }

}
