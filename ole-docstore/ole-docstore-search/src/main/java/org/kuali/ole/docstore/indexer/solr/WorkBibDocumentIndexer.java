package org.kuali.ole.docstore.indexer.solr;

import org.apache.commons.lang.time.StopWatch;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.discovery.solr.work.bib.dublin.WorkBibDublinDocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedDocBuilder;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/2/13
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibDocumentIndexer extends AbstractDocumentIndexer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static WorkBibDocumentIndexer ourInstance = null;

    public static WorkBibDocumentIndexer getInstance() {
        if (null == ourInstance) {
            ourInstance = new WorkBibDocumentIndexer();
        }
        return ourInstance;
    }

    @Override
    public String indexDocuments(List<RequestDocument> requestDocuments, boolean commit) {
        BatchIngestStatistics batchStatistics = BulkIngestStatistics.getInstance().getCurrentBatch();

        String result = null;
        StopWatch timer = new StopWatch();
        StopWatch xmlToObjTime = new StopWatch();
        xmlToObjTime.start();
        xmlToObjTime.suspend();
        timer.start();
        List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();
        if (requestDocuments != null && requestDocuments.size() > 0) {
            StopWatch buildSolrInputDocTime = new StopWatch();
            StopWatch xmlToPojoTimer = new StopWatch();
            buildSolrInputDocTime.start();
            buildSolrInputDocTime.suspend();
            xmlToPojoTimer.start();
            xmlToPojoTimer.suspend();
            try {
                for (RequestDocument requestDocument : requestDocuments) {

                    if (DocFormat.DUBLIN_CORE.isEqualTo(requestDocument.getFormat())) {
                        new WorkBibDublinDocBuilder().buildSolrInputDocument(requestDocument, solrInputDocuments);
                    } else if (DocFormat.DUBLIN_UNQUALIFIED.isEqualTo(requestDocument.getFormat())) {
                        new WorkBibDublinUnQualifiedDocBuilder()
                                .buildSolrInputDocument(requestDocument, solrInputDocuments);

                    }
                    assignUUIDs(solrInputDocuments, null);
                }
            } catch (Exception e1) {
                result = buildFailureMsg(null, "Indexing failed. " + e1.getMessage());
                logger.error(result, e1);
            }
            timer.stop();
            if ((null == solrInputDocuments) || (solrInputDocuments.isEmpty())) {
                result = buildFailureMsg(null, "No valid documents found in input.");
                return result;
            }
            int numDocs = solrInputDocuments.size();
            batchStatistics.setTimeToConvertXmlToPojo(xmlToPojoTimer.getTime());
            batchStatistics.setTimeToConvertToSolrInputDocs(buildSolrInputDocTime.getTime());
            logger.info("Conversion to Solr docs- Num:" + numDocs + ": Time taken:" + timer.toString());
            result = indexSolrDocuments(solrInputDocuments, commit);
        }
        return result;
    }
}
