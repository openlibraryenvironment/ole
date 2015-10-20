package org.kuali.ole.deliver.notice.solr;

import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;

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
}