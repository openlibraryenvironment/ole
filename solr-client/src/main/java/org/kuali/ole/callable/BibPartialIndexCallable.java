package org.kuali.ole.callable;

import org.apache.camel.ProducerTemplate;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.common.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.dao.OleMemorizeService;
import org.kuali.ole.model.jpa.BibRecord;
import org.kuali.ole.repo.BibRecordRepository;
import org.kuali.ole.response.PartialIndexStatus;
import org.kuali.ole.util.HelperUtil;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.util.StopWatch;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by sheiks on 27/03/17.
 */
public class BibPartialIndexCallable extends IndexCallableBase implements Callable {

    private List<Integer> bibIds;

    public BibPartialIndexCallable(String solrURL, String coreName, List<Integer> bibIds,
                                   BibMarcRecordProcessor bibMarcRecordProcessor,
                                   Map<String, String> fieldsToTags2IncludeMap,
                                   Map<String, String> fieldsToTags2ExcludeMap,
                                   ProducerTemplate producerTemplate,
                                   SolrTemplate solrTemplate,
                                   OleMemorizeService oleMemorizeService) {
        this.bibIds = bibIds;
        this.coreName = coreName;
        this.solrURL = solrURL;
        this.bibMarcRecordProcessor = bibMarcRecordProcessor;
        this.fieldsToTags2IncludeMap.putAll(fieldsToTags2IncludeMap);
        this.fieldsToTags2ExcludeMap.putAll(fieldsToTags2ExcludeMap);
        this.producerTemplate = producerTemplate;
        this.solrTemplate = solrTemplate;
        this.oleMemorizeService = oleMemorizeService;
        this.indexStatus = PartialIndexStatus.getInstance();
    }

    @Override
    public Object call() throws Exception {
        StopWatch mainStopWatch = new StopWatch();
        mainStopWatch.start();
        BibRecordRepository bibRecordRepository = (BibRecordRepository) HelperUtil.getRepository(BibRecordRepository.class);
        List<BibRecord> bibRecords = bibRecordRepository.findAll(bibIds);

        Iterator<BibRecord> iterator = bibRecords.iterator();

        List<SolrInputDocument> solrInputDocumentsToIndex = processBibRecordToIndex(iterator);

        String message = "Total solr input document  = " + solrInputDocumentsToIndex.size();

        mainStopWatch.stop();
        logger.info(message + " time taken for process : " + mainStopWatch.getTotalTimeSeconds() + " Secs");

        return solrInputDocumentsToIndex.size();
    }
}