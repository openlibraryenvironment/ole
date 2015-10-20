package org.kuali.ole.deliver.notice.solr;

import com.google.common.collect.Lists;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by sheiksalahudeenm on 10/20/15.
 */
public class OleNoticeContentReIndexer_IT {

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
        int chunkSize = 100;

        List<List<String>> partition = Lists.partition(stringList, chunkSize);
        for (Iterator<List<String>> iterator = partition.iterator(); iterator.hasNext(); ) {
            List<String> subList = iterator.next();
            System.out.println(subList.size());
        }
    }
}