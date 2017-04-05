package org.kuali.ole.callable;

import org.apache.camel.ProducerTemplate;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.common.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.dao.OleMemorizeService;
import org.kuali.ole.model.jpa.BibRecord;
import org.kuali.ole.repo.BibRecordRepository;
import org.kuali.ole.response.FullIndexStatus;
import org.kuali.ole.util.HelperUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by sheiks on 27/10/16.
 */
public class BibFullIndexCallable extends IndexCallableBase implements Callable {
    private final int pageNum;

    private final int docsPerPage;

    public BibFullIndexCallable(String solrURL, String coreName, int pageNum, int docsPerPage,
                                BibMarcRecordProcessor bibMarcRecordProcessor,
                                Map<String, String> fieldsToTags2IncludeMap,
                                Map<String, String> fieldsToTags2ExcludeMap,
                                ProducerTemplate producerTemplate,
                                SolrTemplate solrTemplate,
                                OleMemorizeService oleMemorizeService) {
        this.pageNum = pageNum;
        this.docsPerPage = docsPerPage;
        this.coreName = coreName;
        this.solrURL = solrURL;
        this.bibMarcRecordProcessor = bibMarcRecordProcessor;
        this.fieldsToTags2IncludeMap.putAll(fieldsToTags2IncludeMap);
        this.fieldsToTags2ExcludeMap.putAll(fieldsToTags2ExcludeMap);
        this.producerTemplate = producerTemplate;
        this.solrTemplate = solrTemplate;
        this.oleMemorizeService = oleMemorizeService;
        this.indexStatus = FullIndexStatus.getInstance();
    }

    @Override
    public Object call() throws Exception {
        StopWatch mainStopWatch = new StopWatch();
        mainStopWatch.start();
        Page<BibRecord> pageableBibRecords = HelperUtil.getRepository(BibRecordRepository.class).findAll(new PageRequest(pageNum, docsPerPage));
        mainStopWatch.stop();
        logger.info("Page Num : " + pageNum + "    Num Bibs Fetched : " + pageableBibRecords.getNumberOfElements() + "   and time taken to fetch : " + mainStopWatch.getTotalTimeSeconds() + " Secs");

        StopWatch processStopWatch = new StopWatch();
        processStopWatch.start();

        Iterator<BibRecord> iterator = pageableBibRecords.iterator();

        List<SolrInputDocument> solrInputDocumentsToIndex = processBibRecordToIndex(iterator);

        String message = "Total solr input document for page : " + pageNum + "  docPerPage " + docsPerPage + "  = " + solrInputDocumentsToIndex.size();

        processStopWatch.stop();
        logger.info(message + "    time taken for process : " + processStopWatch.getTotalTimeSeconds() + " Secs");

        return solrInputDocumentsToIndex.size();
    }
}
