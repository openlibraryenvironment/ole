package org.kuali.ole.deliver.notice.solr;

import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sheiksalahudeenm on 10/20/15.
 */
public class OleNoticeContentReIndexer_IT extends OLETestCaseBase {

    @Test
    public void testReindexNoticeContent() throws Exception {
        UpdateResponse updateResponse = new OleNoticeContentReIndexer().reindexNoticeContent();
        assertNotNull(updateResponse);
        System.out.println(updateResponse.getElapsedTime());
    }

    @Test
    public void chunkSizeTest() throws Exception {
        List<String> stringList = new ArrayList<>();
        for(int index = 0 ; index <= 1000 ; index++){
            stringList.add("String : " + index);
        }

        int startingIndex = 0;
        int chunkSize = 100;
        int endIndex = chunkSize;
        while(startingIndex <= stringList.size()){
            if(endIndex > stringList.size()){
                endIndex = stringList.size();
            }
            System.out.print("Staring Index : " + startingIndex + "    EndIndex : " + endIndex);
            List<String> subList = stringList.subList(startingIndex, endIndex);
            System.out.print(" SubList Size : " + subList.size());
            System.out.println("\n");
            startingIndex = startingIndex + chunkSize;
            endIndex = endIndex + chunkSize;
        }
    }
}