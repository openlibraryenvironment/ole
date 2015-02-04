package org.kuali.ole.repository;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author PJ7789
 */
@Ignore
@Deprecated
public class DeleteManage_AT {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteManage_AT.class);

    @Test
    public void deleteLinkedDocs() throws Exception {
        Session session = null;

        File file = new File(getClass().getResource("/org/kuali/ole/repository/request.xml").toURI());
        String input = FileUtils.readFileToString(file);
        RequestHandler rh = new RequestHandler();
        Request request = rh.toObject(input);
        //            IngestNIndexHandlerService ingestNIndexHandlerService = new IngestNIndexHandlerService();
        //            Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        //            for (ResponseDocument resDoc : response.getDocuments()) {
        //                bibIds.add(resDoc.getUuid());
        //                for (ResponseDocument linkedDoc : resDoc.getLinkedDocuments()) {
        //                    instanceIds.add(linkedDoc.getUuid());
        //                }
        //            }
        //


        List<RequestDocument> docStoreDocuments = null;
        List<String> uuidsList = new ArrayList<String>();
        String status = null;
        List<String> categoryList = new ArrayList<String>();
        List<String> typeList = new ArrayList<String>();
        List<String> formatList = new ArrayList<String>();
        String category = null;
        String type = null;
        String format = null;
        session = RepositoryManager.getRepositoryManager().getSession();
        try {
            for (Iterator<RequestDocument> iterator = docStoreDocuments.iterator(); iterator.hasNext(); ) {
                RequestDocument document = iterator.next();
                category = document.getCategory();
                type = document.getType();
                format = document.getFormat();
                LOG.info("UUIDs...." + document.getUuid() + "category...." + category);
                String uuid = document.getUuid();
                uuidsList.add(uuid);
                categoryList.add(category);
                typeList.add(type);
                formatList.add(format);
            }
            String deleteUuidXml = deleteuuidsXml(uuidsList, categoryList, typeList, formatList);
            //status = new DeleteManager().deleteLinkedDocs(deleteUuidXml);
            LOG.info("status" + status);
            checkUuidInSolr(uuidsList, category);
            checkUuidInDocStore(uuidsList, session);
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            status = "Failure";
            LOG.info("status" + status);
        } finally {
            RepositoryManager.getRepositoryManager().logout(session);
        }
    }

    private String deleteuuidsXml(List<String> uuidsList, List<String> categoryList, List<String> typeList,
                                  List<String> formatList) {

        StringBuilder sb = new StringBuilder();
        sb.append("<request>\n");
        sb.append("	<user>").append("ole-khuntley").append("</user>\n");
        sb.append("	<operation>").append("batchDeleteWithLinks").append("</operation>\n");
        sb.append("<requestDocuments>\n");
        for (int i = 0; i < uuidsList.size(); i++) {
            sb.append("<ingestdocument id=\"" + uuidsList.get(i) + "\" category=\"" + categoryList.get(i) + "\" type=\""
                    + typeList.get(i) + "\" format=\"" + formatList.get(i) + "\">" + "</ingestdocument>\n");
        }
        sb.append("	</requestDocuments>\n");
        sb.append("	</request>");
        LOG.info("sb.tostring" + sb.toString());

        return sb.toString();
    }

    private void checkUuidInSolr(List<String> uuidsList, String category) throws Exception {
        if (category.equalsIgnoreCase("bibliographic")) {
            category = "bib";
        }
        SolrServer solr = new HttpSolrServer(
                ConfigContext.getCurrentContextConfig().getProperty("docSearchURL") + "/" + category);
        SolrQuery query = new SolrQuery();
        for (int i = 0; i < uuidsList.size(); i++) {
            query.setQuery("uniqueId:" + uuidsList.get(i));
            QueryResponse response = solr.query(query);
            LOG.info("query..." + query);
            SolrDocumentList doc = response.getResults();
            LOG.info("NumFound..." + doc.getNumFound());
            assertEquals(0, (int) (doc.getNumFound()));
        }
    }

    private void checkUuidInDocStore(List<String> uuidsList, Session session) throws Exception {
        for (int i = 0; i < uuidsList.size(); i++) {
            Node deleteNode = new NodeHandler().getNodeByUUID(session, uuidsList.get(i));
            LOG.info("deleteNodes..." + deleteNode);
            assertNull(deleteNode);
        }
    }
}


