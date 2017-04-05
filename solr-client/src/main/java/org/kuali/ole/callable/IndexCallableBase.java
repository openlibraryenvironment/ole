package org.kuali.ole.callable;

import org.apache.camel.ProducerTemplate;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.callable.BibFullIndexCallable;
import org.kuali.ole.callable.BibRecordSetupCallable;
import org.kuali.ole.common.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.dao.OleMemorizeService;
import org.kuali.ole.model.jpa.BibRecord;
import org.kuali.ole.model.solr.RecordCountAndSolrDocumentMap;
import org.kuali.ole.response.IndexStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by sheiks on 27/03/17.
 */
public class IndexCallableBase {

    Logger logger = LoggerFactory.getLogger(BibFullIndexCallable.class);

    protected String coreName;
    protected String solrURL;
    protected BibMarcRecordProcessor bibMarcRecordProcessor;
    protected Map<String, String> fieldsToTags2IncludeMap = new HashMap<>();
    protected Map<String, String> fieldsToTags2ExcludeMap = new HashMap<>();
    protected ProducerTemplate producerTemplate;
    protected SolrTemplate solrTemplate;
    protected OleMemorizeService oleMemorizeService;
    protected IndexStatus indexStatus;

    public List<SolrInputDocument> processBibRecordToIndex(Iterator<BibRecord> iterator) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> futures = new ArrayList<>();
        while (iterator.hasNext()) {
            BibRecord bibRecord = iterator.next();
            Future submit = executorService.submit(new BibRecordSetupCallable(bibRecord,
                    bibMarcRecordProcessor,
                    fieldsToTags2IncludeMap, fieldsToTags2ExcludeMap,
                    producerTemplate, oleMemorizeService));
            futures.add(submit);
        }

        logger.info("Num futures to prepare Bib and Associated data : " + futures.size());

        int count= 0;
        List<SolrInputDocument> solrInputDocumentsToIndex = new ArrayList<>();
        for (Iterator<Future> futureIterator = futures.iterator(); futureIterator.hasNext(); ) {
            try {
                Future future = futureIterator.next();
                Object object = future.get();
                if(null != object) {
                    count++;
                    RecordCountAndSolrDocumentMap recordCountAndSolrDocumentMap = (RecordCountAndSolrDocumentMap) object;
                    Collection<SolrInputDocument> values = recordCountAndSolrDocumentMap.getSolrInputDocumentMap().values();
                    solrInputDocumentsToIndex.addAll(values);
                    indexStatus.addBibFetched(recordCountAndSolrDocumentMap.getNumberOfBibFetched());
                    indexStatus.addBibProcessed(recordCountAndSolrDocumentMap.getNumberOfBibProcessed());
                    indexStatus.addHoldingsFetched(recordCountAndSolrDocumentMap.getNumberOfHoldingsFetched());
                    indexStatus.addHoldingsProcessed(recordCountAndSolrDocumentMap.getNumberOfHoldingsProcessed());
                    indexStatus.addItemsFetched(recordCountAndSolrDocumentMap.getNumberOfItemsFetched());
                    indexStatus.addItemsProcessed(recordCountAndSolrDocumentMap.getNumberOfItemsProcessed());
                }
            } catch (Exception e) {
                logger.error("Exception : " + e.getMessage());
            }
        }

        executorService.shutdown();

        if (!CollectionUtils.isEmpty(solrInputDocumentsToIndex)) {
            SolrTemplate solrTemplate = new SolrTemplate(new HttpSolrClient(solrURL + File.separator + coreName));
            solrTemplate.setSolrCore(coreName);
            solrTemplate.saveDocuments(solrInputDocumentsToIndex);
            solrTemplate.commit();
        }
        return solrInputDocumentsToIndex;
    }

}
