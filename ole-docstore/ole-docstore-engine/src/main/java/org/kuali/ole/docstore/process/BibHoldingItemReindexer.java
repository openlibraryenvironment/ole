package org.kuali.ole.docstore.process;

import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.docstore.common.document.BibTrees;
import org.kuali.ole.docstore.common.util.BatchBibTreeDBUtil;
import org.kuali.ole.docstore.common.util.ReindexBatchStatistics;
import org.kuali.ole.docstore.engine.service.index.solr.BibMarcIndexer;
import org.kuali.ole.docstore.engine.service.index.solr.HoldingsOlemlIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 5/30/14
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibHoldingItemReindexer {

//    int batchSize = 5000;
    private String filePath =  System.getProperty("solr.solr.home");
    private static BibHoldingItemReindexer bibHoldingItemReindexer = null;
    private static BibMarcIndexer bibMarcIndexer = null;
    private ReindexBatchStatistics totalBatchStatistics = new ReindexBatchStatistics();
    private static final Logger LOG = LoggerFactory.getLogger(BibHoldingItemReindexer.class);
    private ReindexBatchStatistics currentBatchStatistics = new ReindexBatchStatistics();
    BatchBibTreeDBUtil bibTreeUtil = new BatchBibTreeDBUtil();


    public void index(int batchSize) throws Exception {

        totalBatchStatistics.setStartTime(new Date());

        BibMarcIndexer bibMarcIndexer = BibMarcIndexer.getInstance();

        int count = 1;

        bibTreeUtil.init();

        BibTrees bibTrees = bibTreeUtil.fetchNextBatch(batchSize, totalBatchStatistics);

        while (bibTrees.getBibTrees().size() > 0) {
            org.springframework.util.StopWatch stopWatch = new org.springframework.util.StopWatch();
            try {
                stopWatch.start();
                bibMarcIndexer.createTrees(bibTrees, totalBatchStatistics);
                stopWatch.stop();
                totalBatchStatistics.addIndexTime(stopWatch.getTotalTimeMillis());

                if(count * batchSize % 20000 == 0) {
                    currentBatchStatistics.getBatchStatus(totalBatchStatistics);
                    BatchBibTreeDBUtil.writeStatusToFile(filePath,  RebuildIndexesHandler.STATUS_FILE_NAME, currentBatchStatistics.toString());
                    currentBatchStatistics.setData(totalBatchStatistics);

                }

            } catch (Exception e) {
                BatchBibTreeDBUtil.writeStatusToFile(filePath, RebuildIndexesHandler.EXCEPION_FILE_NAME, "batch " + count + " failed due to *************\n" + e, bibTrees.getBibTrees().get(0).getBib().getId(),bibTrees.getBibTrees().get(bibTrees.getBibTrees().size()-1).getBib().getId());
                LOG.error("Exception while reindexing  : ", e);
            }
            count++;
            bibTrees = bibTreeUtil.fetchNextBatch(batchSize, totalBatchStatistics);
        }

        totalBatchStatistics.setEndTime(new Date());
        BatchBibTreeDBUtil.writeStatusToFile(filePath,  RebuildIndexesHandler.STATUS_FILE_NAME, totalBatchStatistics.toString());


        bibTreeUtil.fetchBibHoldings();
        bibTreeUtil.fetchHoldingItems();

        Thread.sleep(3000);

        fetchHoldings();
        fetchItems();
        bibTreeUtil.closeConnections();
    }


    public String showStats() {

        return totalBatchStatistics.toString();
    }

    public static BibHoldingItemReindexer getInstance() {
        if(bibHoldingItemReindexer == null) {
            bibHoldingItemReindexer = new BibHoldingItemReindexer();
        }
        return bibHoldingItemReindexer;
    }

    public void fetchHoldings() throws SQLException, IOException, SolrServerException {
        boolean cursor = true;
        Map<String, List> map = bibTreeUtil.fetchBibHolding(cursor);
        while (map != null) {
            for (Map.Entry<String, List> stringListEntry : map.entrySet()) {
                    String key = stringListEntry.getKey();
                    List value = stringListEntry.getValue();
                    BibMarcIndexer.getInstance().bind(key, value);
            }
            cursor = false;
            map = bibTreeUtil.fetchBibHolding(cursor);

        }
    }
    public void fetchItems() throws SQLException, IOException, SolrServerException {
        boolean cursor = true;
        Map<String, List> map = bibTreeUtil.fetchHoldingItem(cursor);
        while (map != null) {
            for (Map.Entry<String, List> stringListEntry : map.entrySet()) {
                    String key = stringListEntry.getKey();
                    List value = stringListEntry.getValue();
                    BibMarcIndexer.getInstance().bindAnalytics(key, value, "CREATE");
            }
            cursor = false;
            map = bibTreeUtil.fetchHoldingItem(cursor);

        }
    }

}
