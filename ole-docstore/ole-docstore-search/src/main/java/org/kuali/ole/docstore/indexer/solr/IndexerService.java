package org.kuali.ole.docstore.indexer.solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/2/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IndexerService {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String UUID_FILE_NAME_SUFFIX = "_UUID_.xml";

    public static final String ID_FIELD_PREFIX = "id_disc_";
    public static final int BATCH_SIZE = 10000;
    public static final String BIBLIOGRAPHIC = "bibliographic";
    public static final String DOC_TYPE = "DocType";
    public static final String DOC_FORMAT = "DocFormat";
    public static final String HOLDINGS_IDENTIFIER = "holdingsIdentifier";
    public static final String ITEM_IDENTIFIER = "itemIdentifier";
    public static final String INSTANCE = "instance";


    /**
     * Deletes the documents (if existing) with the Ids specified in the given uuidList.
     *
     * @param docCategory
     * @param uuidList
     * @return SUCCESS or FAILURE (with error id if any)
     */
    public String deleteDocuments(String docCategory, List<String> uuidList)
            throws MalformedURLException, SolrServerException;

    /**
     * Deletes the document (if existing) with the given uuid.
     *
     * @param docCategory
     * @param uuid
     * @return SUCCESS or FAILURE (with error id if any)
     */
    public String deleteDocument(String docCategory, String uuid);

    //public String addDocuments(String docCategory, String docType, String docFormat, String docContent) throws Exception;

    /**
     * Indexes the documents from the xml files in the given fileDir.
     * Uses solrj API (SolrInputDocuments) instead of DIH (DataImportHandler).
     * The name of xml files should be of the form "{uuid}_UUID_.xml",
     * where {uuid} is the "id" field value for the document in the xml file.
     * Otherwise a random uuid will be assigned for the document.
     *
     * @param docCategory
     * @param docType
     * @param docFormat
     * @param dataDir
     * @return SUCCESS ("success-N" where N is num of documents processed); FAILURE (with error id if any).
     */
    public String indexDocumentsFromDirBySolrDoc(String docCategory, String docType, String docFormat, String dataDir);

    public String indexDocumentsFromStringBySolrDoc(String docCategory, String docType, String docFormat, String data)
            throws IOException;

    public String indexDocumentsFromFileBySolrDoc(String docCategory, String docType, String docFormat,
                                                  String filePath);

    public String indexDocumentsFromFiles(String docCategory, String docType, String docFormat, List<File> fileList);

    public String indexDocuments(List<RequestDocument> requestDocuments);

    public String indexDocuments(List<RequestDocument> requestDocuments, boolean commit);

    public String indexDocument(RequestDocument requestDocument);

    public String indexDocument(RequestDocument requestDocument, boolean commit);

    public String indexSolrDocuments(List<SolrInputDocument> solrDocs);

    public void commit() throws Exception;

    public void rollback() throws Exception;

    /**
     * Method to bulk Index Documents
     *
     * @param requestDocuments
     * @return
     */
    public String bulkIndexDocuments(List<RequestDocument> requestDocuments, boolean isCommit);

    public List<SolrDocument> getSolrDocumentBySolrId(String uniqueId);

//    public List<SolrDocument> getSolrDocumentBySolrId(String uniqueId,String docType);

    public List<SolrDocument> getSolrDocument(String fieldName, String fieldValue);

    public QueryResponse searchBibRecord(String docCat, String docType, String docFormat, String fieldName,
                                         String fieldValue, String fieldList);

    public void cleanupDiscoveryData() throws IOException, SolrServerException;

    public String bind(List<RequestDocument> requestDocument) throws Exception, IOException;

    public String bind(RequestDocument requestDocument) throws Exception;

    public String unbind(List<RequestDocument> requestDocuments) throws Exception;

    public void transferInstances(List<RequestDocument> requestDocuments) throws Exception;

    public void transferItems(List<RequestDocument> requestDocuments) throws Exception;

    public String delete(List<RequestDocument> requestDocuments) throws Exception;

    public String delete(RequestDocument requestDocument) throws SolrServerException, Exception;

    public String buildUuid();
}
