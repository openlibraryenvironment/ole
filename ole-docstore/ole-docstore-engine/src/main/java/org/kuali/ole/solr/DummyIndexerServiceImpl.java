package org.kuali.ole.solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 3/14/12
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class DummyIndexerServiceImpl
        implements IndexerService {
    private static DummyIndexerServiceImpl genericIndexerServiceImpl = new DummyIndexerServiceImpl();

    private DummyIndexerServiceImpl() {

    }

    public static DummyIndexerServiceImpl getInstance() {
        System.out.println("in DummyIndexerServiceImpl");
        return genericIndexerServiceImpl;
    }

    @Override
    public String deleteDocuments(String s, List<String> strings) {
        return SUCCESS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String deleteDocument(String s, String s1) {
        return SUCCESS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexDocumentsFromFileBySolrDoc(String s, String s1, String s2, String s3) {
        return SUCCESS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexDocumentsFromDirBySolrDoc(String s, String s1, String s2, String s3) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexDocumentsFromStringBySolrDoc(String s, String s1, String s2, String s3) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexDocumentsFromFiles(String s, String s1, String s2, List<File> files) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<SolrDocument> getSolrDocumentBySolrId(String s) {
        return new ArrayList<SolrDocument>();  //To change body of implemented methods use File | Settings | File Templates.
    }

//    @Override
//    public List<SolrDocument> getSolrDocumentBySolrId(String uniqueId, String docType) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    @Override
    public List<SolrDocument> getSolrDocument(String s, String s1) {
        return new ArrayList<SolrDocument>();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public QueryResponse searchBibRecord(String s, String s1, String s2, String s3, String s4, String s5) {
        return new QueryResponse();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cleanupDiscoveryData() throws IOException, SolrServerException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String bind(List<RequestDocument> requestDocument) throws Exception, IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String bind(RequestDocument requestDocument) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String unbind(List<RequestDocument> requestDocuments) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void transferInstances(List<RequestDocument> requestDocuments) throws Exception {
        System.out.println("DummyIndexerServiceImpl : transferInstances ");
    }

    @Override
    public void transferItems(List<RequestDocument> requestDocuments) throws Exception {
        System.out.println("DummyIndexerServiceImpl : transferItems ");
    }

    @Override
    public String delete(List<RequestDocument> requestDocuments) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String delete(RequestDocument requestDocument) throws SolrServerException, Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String buildUuid() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String bulkIndexDocuments(List<RequestDocument> requestDocumentList, boolean isCommit) {
        return SUCCESS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexDocument(RequestDocument requestDocument) {
        return SUCCESS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexDocument(RequestDocument requestDocument, boolean commit) {
        return SUCCESS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexDocuments(List<RequestDocument> requestDocumentList) {
        return SUCCESS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexDocuments(List<RequestDocument> requestDocumentList, boolean commit) {
        return SUCCESS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexSolrDocuments(List<SolrInputDocument> solrDocs) {
        return SUCCESS;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void commit() throws Exception {
    }

    @Override
    public void rollback() throws Exception {
    }
}
