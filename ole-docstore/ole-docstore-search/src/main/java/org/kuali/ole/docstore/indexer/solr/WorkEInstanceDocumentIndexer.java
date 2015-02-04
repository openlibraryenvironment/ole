package org.kuali.ole.docstore.indexer.solr;

import org.apache.commons.lang.time.StopWatch;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.discovery.solr.work.bib.marc.WorkBibMarcDocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.einstance.WorkEInstanceOlemlDocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.instance.oleml.WorkInstanceOlemlDocBuilder;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 7/18/13
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkEInstanceDocumentIndexer extends AbstractDocumentIndexer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static WorkEInstanceDocumentIndexer ourInstance = null;

    public static WorkEInstanceDocumentIndexer getInstance() {
        if (null == ourInstance) {
            ourInstance = new WorkEInstanceDocumentIndexer();
        }
        return ourInstance;
    }

    @Override
    public String indexDocuments(List<RequestDocument> requestDocuments, boolean commit) {

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
                    new WorkEInstanceOlemlDocBuilder()
                            .buildSolrInputDocument(requestDocument, solrInputDocuments);
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
//            batchStatistics.setTimeToConvertXmlToPojo(xmlToPojoTimer.getTime());
//            batchStatistics.setTimeToConvertToSolrInputDocs(buildSolrInputDocTime.getTime());
            logger.info("Conversion to Solr docs- Num:" + numDocs + ": Time taken:" + timer.toString());
            result = indexSolrDocuments(solrInputDocuments, commit);
        }
        return result;
    }

    @Override
    public String delete(RequestDocument requestDocument) throws Exception {
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
        SolrQuery solrQuery = new SolrQuery();
        String query = "id:" + requestDocument.getUuid();
        solrQuery.setQuery(query);
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        List<String> deleteIdList = new ArrayList<String>();
        for (SolrDocument solrDocument : solrDocumentList) {
            Object holId = solrDocument.getFieldValue("holdingsIdentifier");
            if (holId != null) {
                if (holId instanceof List) {
                    List<String> holIdList = (List<String>) solrDocument.getFieldValue("holdingsIdentifier");
                    deleteIdList.addAll(holIdList);
                } else if (holId instanceof String) {
                    String holdId = (String) holId;
                    deleteIdList.add(holdId);
                }
            }
        }
        deleteIdList.add(requestDocument.getUuid());
        deleteDocumentsByUUIDList(deleteIdList, "eInstance", true);
        query = "(instanceIdentifier:" + requestDocument.getUuid() + ") AND " + "(DocType:bibliographic)";
        solrQuery.setQuery(query);
        response = server.query(solrQuery);
        solrDocumentList = response.getResults();
        for (SolrDocument bibSolrDocument : solrDocumentList) {
            List<String> instanceIdentifierList = new ArrayList<String>();
            Object instanceIdentifier = bibSolrDocument.getFieldValue("instanceIdentifier");
            if (instanceIdentifier instanceof List) {
                instanceIdentifierList = (List<String>) bibSolrDocument.getFieldValue("instanceIdentifier");
                if (instanceIdentifierList.contains(requestDocument.getUuid())) {
                    instanceIdentifierList.remove(requestDocument.getUuid());
                    bibSolrDocument.setField("instanceIdentifier", instanceIdentifierList);
                }
            } else if (instanceIdentifier instanceof String) {
                String instId = (String) instanceIdentifier;
                if (instId.equalsIgnoreCase(requestDocument.getUuid())) {
                    bibSolrDocument.removeFields("instanceIdentifier");
                }
            }
            solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(bibSolrDocument));
        }
        String result = indexSolrDocuments(solrInputDocumentList, true);
        if (!result.contains(SUCCESS)) {
            return result;
        }
        return result;
    }
}
