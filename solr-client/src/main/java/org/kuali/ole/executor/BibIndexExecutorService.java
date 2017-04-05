package org.kuali.ole.executor;

import org.apache.camel.ProducerTemplate;
import org.kuali.ole.common.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.dao.OleMemorizeService;
import org.kuali.ole.repo.BibRecordRepository;
import org.kuali.ole.service.SolrAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.solr.core.SolrTemplate;

/**
 * Created by sheiks on 27/03/17.
 */
public class BibIndexExecutorService {
    Logger logger = LoggerFactory.getLogger(BibIndexExecutorService.class);
    @Value("${solr.url}")
    String solrUrl;

    @Value("${solr.parent.core}")
    String solrCore;

    @Value("${solr.server.protocol}")
    String solrServerProtocol;

    @Autowired
    SolrAdmin solrAdmin;

    @Autowired
    BibRecordRepository bibRecordRepository;

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    SolrTemplate solrTemplate;

    @Autowired
    OleMemorizeService oleMemorizeService;

    private BibMarcRecordProcessor bibMarcRecordProcessor;

    public BibMarcRecordProcessor getBibMarcRecordProcessor() {
        if(null == bibMarcRecordProcessor) {
            bibMarcRecordProcessor = new BibMarcRecordProcessor();
        }
        return bibMarcRecordProcessor;
    }

    public void setBibMarcRecordProcessor(BibMarcRecordProcessor bibMarcRecordProcessor) {
        this.bibMarcRecordProcessor = bibMarcRecordProcessor;
    }
}
