package org.kuali.ole.docstore.engine.service.rest;

import org.kuali.ole.docstore.common.client.DocstoreRestClient;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.model.enums.DocType;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by jayabharathreddy on 26/7/14.
 */
public class TestUpdateAndSearch {

    private static int NTHREADS = 10;
    private DocstoreRestClient restClient = new DocstoreRestClient();

    public void testUpdateAndSearch() throws Exception {



        ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
        //create a list to hold the Future object associated with Callable
        List<Future<Long>> list = new ArrayList<Future<Long>>();
        //Create MyCallable instance
        int itemId = 7692010;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
//        Callable<Long> testSearch = new TestSearch("wio-" + (itemId));
//        Future<Long> searchFuture = executor.submit(testSearch);
//        list.add(searchFuture);
        for(int i=0; i< 50; i++){

            Callable<Long> worker = new TestUpdateAndSearchCals("wio-" + (itemId), ""+(i+17000));
//            Thread.sleep(3000);
            //submit Callable tasks to be executed by thread pool
            Future<Long> future = executor.submit(worker);
            //add Future to the list, we can get return value using Future
            list.add(future);
        }

        for(Future<Long> fut : list){
            try {
                //print the return value of Future, notice the output delay in console
                // because Future.get() waits for task to get completed
                System.out.println(new Date()+ "::"+fut.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        searchItem("wio-7692010");
        executor.shutdown();
    }


    private void searchItem(String itemId) {
        SearchParams searchParams = new SearchParams();
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.ITEM.getCode(), "id", itemId), "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), "ENUMERATION"));
        SearchResponse response = restClient.search(searchParams);
        if(response.getSearchResults() != null && response.getSearchResults().size() > 0 && response.getSearchResults().get(0).getSearchResultFields() != null && response.getSearchResults().get(0).getSearchResultFields().size() > 0 && response.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue() != null) {
            String enumeration = response.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue();
            System.out.println("item id " + itemId + " with enumeration " + enumeration);

        }

    }


}
