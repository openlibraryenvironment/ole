package org.kuali.ole.docstore.indexer.solr;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.discovery.solr.work.bib.WorkBibCommonFields;
import org.kuali.ole.docstore.discovery.solr.work.bib.dublin.WorkBibDublinDocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedDocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.bib.marc.WorkBibMarcDocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.instance.oleml.WorkInstanceOlemlDocBuilder;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.WorkBibDublinRecord;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.WorkBibDublinRecordProcessor;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedRecordProcessor;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordProcessor;
import org.kuali.ole.docstore.utility.BatchIngestStatistics;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/2/13
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDocumentIndexer implements IndexerService {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDocumentIndexer.class);
    public WorkBibMarcDocBuilder workBibMarcDocBuilder1 = new WorkBibMarcDocBuilder();
    public long commitRecCount = 0;


    public AbstractDocumentIndexer() {
        init();
    }

    protected void init() {
        LOG.debug("IndexerServiceImpl init ");
        //        docSearchUrl = PropertyUtil.getPropertyUtil().getProperty("docSearchURL");
        //        if ((null != docSearchUrl) && !docSearchUrl.endsWith("/")) {
        //            docSearchUrl = docSearchUrl + "/";
        //        }
    }

    public String deleteDocuments(String docCategory, List<String> uuidList)
            throws MalformedURLException, SolrServerException {
        String result = deleteDocumentsByUUIDList(uuidList, docCategory);
        return result;
    }

    public String deleteDocument(String docCategory, String uuid) {
        String result = deleteDocumentByUUID(uuid, docCategory);
        return result;
    }

    public String indexSolrDocuments(List<SolrInputDocument> solrDocs) {
        return indexSolrDocuments(solrDocs, true);
    }

    public String indexSolrDocuments(List<SolrInputDocument> solrDocs, boolean commit) {
        String result = null;
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            result = indexSolrDocuments(solrDocs, commit, false);
            timer.stop();
            LOG.info("Time taken for indexing " + solrDocs.size() + " Solr docs:" + timer.toString());
        } catch (Exception e) {
            result = buildFailureMsg(null, "Indexing failed. " + e.getMessage());
            LOG.error(result, e);
        }
        return result;
    }

    @Override
    public String indexDocumentsFromDirBySolrDoc(String docCategory, String docType, String docFormat, String dataDir) {
        String result = null;
        String xmlContent = "";
        // get the files from the dir.
        File srcDir = new File(dataDir);
        if ((null == srcDir) || !srcDir.isDirectory()) {
            result = buildFailureMsg(null, "Invalid data directory:" + dataDir);
            return result;
        }
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return (!name.startsWith(".") && (name.endsWith(".xml")));
            }
        };

        String[] srcFileNames = srcDir.list(filter);
        if ((null == srcFileNames) || (srcFileNames.length == 0)) {
            result = buildFailureMsg(null, "No data files found in data dir:" + dataDir);
            return result;
        }
        List<File> fileList = new ArrayList<File>(srcFileNames.length);
        for (int i = 0; i < srcFileNames.length; i++) {
            File srcFile = new File(dataDir + File.separator + srcFileNames[i]);
            fileList.add(srcFile);
        }
        return indexDocumentsFromFiles(docCategory, docType, docFormat, fileList);
    }

    @Override
    public String indexDocumentsFromStringBySolrDoc(String docCategory, String docType, String docFormat, String data)
            throws IOException {

        File file = File.createTempFile("marc.xml", ".tmp");
        FileUtils.writeStringToFile(file, data, "UTF-8");
        String filePath = file.getAbsolutePath();
        return indexDocumentsFromFileBySolrDoc(docCategory, docType, docFormat,
                filePath);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String indexDocumentsFromFileBySolrDoc(String docCategory, String docType, String docFormat,
                                                  String filePath) {
        List<File> fileList = new ArrayList<File>(0);
        fileList.add(new File(filePath));
        return indexDocumentsFromFiles(docCategory, docType, docFormat, fileList);
    }


    /**
     * Indexes the records (of the given docCategory, docType and docFormat) from the files in the given data directory.
     * <p>
     * This is a utility method to use Discovery separately from DocStore.
     * </p>
     *
     * @param docCategory category of the documents expected in the input files
     * @param docType     type of the documents expected in the input files
     * @param docFormat   format of the documents expected in the input files
     * @param fileList    list of files to be indexed
     * @return SUCCESS or FAILURE
     */
    @Override
    public String indexDocumentsFromFiles(String docCategory, String docType, String docFormat, List<File> fileList) {
        // TODO: Modify this method so that if dataDir is a file, it should be indexed.
        String result = null;
        String xmlContent = "";
        try {
            StopWatch indexingTimer = new StopWatch();
            StopWatch conversionTimer = new StopWatch();
            StopWatch fileIOTimer = new StopWatch();
            StopWatch totalTimer = new StopWatch();
            totalTimer.start();
            fileIOTimer.start();
            fileIOTimer.suspend();

            if ((null == fileList) || (fileList.size() == 0)) {
                result = buildFailureMsg(null, "No  files found in data dir:" + fileList);
                return result;
            }
            int numFiles = fileList.size();
            int numDocs = 0;
            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
            TransformerFactory tf = new com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            conversionTimer.start();
            conversionTimer.suspend();
            indexingTimer.start();
            indexingTimer.suspend();
            for (int i = 0; i < fileList.size(); i++) {
                File srcFile = fileList.get(i);
                LOG.info("Processing File: " + srcFile.getAbsolutePath());
                String srcFileName = srcFile.getName();

                // Get the id of the doc from the file name if Exists.
                String idFromFileName = null;
                List<String> idFromFileNameList = null;
                int suffixIndex = srcFileName.indexOf(UUID_FILE_NAME_SUFFIX);
                if (suffixIndex > 0) {
                    idFromFileName = srcFileName.substring(0, suffixIndex);
                    idFromFileNameList = new ArrayList<String>(1);
                    idFromFileNameList.add(idFromFileName);
                }

                int recordsProcessedInFile = 0;
                try {
                    XMLInputFactory xif = XMLInputFactory.newInstance();
                    XMLStreamReader xsr = xif.createXMLStreamReader(new FileReader(srcFile));
                    xsr.nextTag();
                    recordsProcessedInFile = 0;
                    List<SolrInputDocument> solrDocsToAdd = new ArrayList<SolrInputDocument>();
                    List<SolrInputDocument> solrDocs = null;
                    while (xsr.hasNext()) {
                        int eventType = xsr.next();
                        if (eventType == XMLStreamConstants.START_ELEMENT) {
                            if (DocFormat.MARC.isEqualTo(docFormat)) {
                                recordsProcessedInFile++;
                                LOG.debug("Processing Record(" + recordsProcessedInFile + ") of File: " + srcFileName);
                                fileIOTimer.resume();
                                StringWriter str = new StringWriter();
                                str.append("<collection>");
                                t.transform(new StAXSource(xsr), new StreamResult(str));
                                str.append("</collection>");
                                xmlContent = str.getBuffer().toString();
                                fileIOTimer.suspend();
                                conversionTimer.resume();
                                solrDocs = convertToSolrDocs(docCategory, docType, docFormat, xmlContent);
                                if ((null == solrDocs) || (solrDocs.size() == 0)) {
                                    continue;
                                }
                                if (idFromFileName == null) {
                                    assignUUIDs(solrDocs, null);
                                } else {
                                    assignUUIDs(solrDocs.subList(0, 1), idFromFileNameList);
                                }
                                conversionTimer.suspend();
                                numDocs += solrDocs.size();
                            } else if (DocFormat.DUBLIN_CORE.isEqualTo(docFormat)) {
                                // TODO: May be moved out of while loop?
                                conversionTimer.resume();
                                solrDocs = convertToSolrDocs(docCategory, docType, docFormat,
                                        FileUtils.readFileToString(srcFile, "UTF-8"));
                                assignUUIDs(solrDocs, null);
                                conversionTimer.suspend();
                                solrDocsToAdd.addAll(solrDocs);
                                numDocs += solrDocs.size();
                                break;
                            } else if (DocFormat.DUBLIN_UNQUALIFIED.isEqualTo(docFormat)) {
                                if (xsr.getName().getLocalPart().equalsIgnoreCase("record")) {
                                    conversionTimer.resume();
                                    solrDocs = new ArrayList<SolrInputDocument>();
                                    StringWriter str = new StringWriter();
                                    str.append("<OAI-PMH><ListRecords>");
                                    t.transform(new StAXSource(xsr), new StreamResult(str));
                                    str.append("</ListRecords></OAI-PMH>");
                                    str.close();
                                    xmlContent = str.getBuffer().toString();
                                    solrDocs = convertToSolrDocs(docCategory, docType, docFormat, xmlContent);
                                    str.flush();
                                    assignUUIDs(solrDocs, null);
                                    conversionTimer.suspend();
                                    numDocs += solrDocs.size();
                                }
                            } else {
                                throw new Exception("Unsupported Document Format: " + docFormat);
                            }
                        } else {
                            continue;
                        }

                        if (solrDocs != null) {
                            solrDocsToAdd.addAll(solrDocs);
                        }
                        if (solrDocsToAdd.size() < 500) {
                            // TODO: Handle the case when the size of the batch is too high. Do a check on the size.
                            continue;
                        }
                        indexingTimer.resume();
                        solr.add(solrDocsToAdd);
                        indexingTimer.suspend();
                        solrDocsToAdd.clear();
                        if (recordsProcessedInFile % 10000 == 0) {
                            totalTimer.split();
                            LOG.info("Records processed in file " + srcFileName + ":" + recordsProcessedInFile
                                    + "; Time elapsed:" + totalTimer.toSplitString());
                        }
                        if (idFromFileName != null || DocFormat.DUBLIN_CORE.isEqualTo(docFormat)) {
                            break;
                        }
                    }
                    if (solrDocsToAdd.size() > 0) {
                        indexingTimer.resume();
                        solr.add(solrDocsToAdd);
                        indexingTimer.suspend();
                        solrDocsToAdd.clear();
                    }
                } catch (Exception ex) {
                    String message = "Failure while processing file '" + srcFile.getAbsolutePath() + "' \nat Record: "
                            + recordsProcessedInFile + "\n" + xmlContent;
                    LOG.error(message , ex);
                    solr.rollback();
                    throw ex;
                }
                totalTimer.split();
                if (recordsProcessedInFile > 0) {
                    // Do not log this message if a file has only one record.
                    LOG.info("Records processed in file " + srcFileName + ":" + recordsProcessedInFile
                            + "; Time elapsed:" + totalTimer.toSplitString());
                }
            }
            // commit after all docs are added.
            if (numDocs > 0) {
                indexingTimer.resume();
                solr.commit();
                indexingTimer.suspend();
            }

            conversionTimer.stop();
            fileIOTimer.stop();
            indexingTimer.stop();
            totalTimer.stop();
            LOG.info("Num of files processed:" + numFiles + "; Num of documents processed:" + numDocs);
            LOG.info("Time taken for reading files:" + fileIOTimer.toString()
                    + "; Time taken for parsing and converting to Solr Docs:" + conversionTimer.toString());
            LOG.info(
                    "Time taken for indexing Solr docs:" + indexingTimer.toString() + "; Total time taken:" + totalTimer
                            .toString());
            result = SUCCESS + "-" + numDocs;
        } catch (Exception e) {
            result = buildFailureMsg(null, "Indexing failed. " + e.getMessage());
            LOG.error(result, e);
        }
        return result;
    }

    //    public String indexDocuments(List<RequestDocument> requestDocuments) {
    //        for (RequestDocument requestDocument : requestDocuments) {
    //            indexDocument(requestDocument);
    //        }
    //        return null;
    //    }

    public String indexDocument(RequestDocument requestDocument) {
        return indexDocument(requestDocument, true);
    }

    public String indexDocument(RequestDocument requestDocument, boolean commit) {
        List<RequestDocument> requestDocuments = null;
        if (requestDocument != null) {
            requestDocuments = new ArrayList<RequestDocument>(1);
            requestDocuments.add(requestDocument);
        }
        return indexDocuments(requestDocuments, commit);
    }

    @Override
    public String indexDocuments(List<RequestDocument> requestDocuments) {
        return indexDocuments(requestDocuments, true);
    }

    public String bulkIndexDocuments(List<RequestDocument> requestDocuments, boolean isCommit) {
        String result = "success";
        Map<String, SolrInputDocument> bibIdToDocMap = new HashMap<String, SolrInputDocument>();
        BatchIngestStatistics batchStatistics = BulkIngestStatistics.getInstance().getCurrentBatch();
        if (requestDocuments != null && requestDocuments.size() > 0) {
            StopWatch timer = new StopWatch();
            StopWatch buildSolrInputDocTimer = new StopWatch();
            StopWatch xmlToPojoTimer = new StopWatch();
            timer.start();
            buildSolrInputDocTimer.start();
            buildSolrInputDocTimer.suspend();
            xmlToPojoTimer.start();
            xmlToPojoTimer.suspend();

            List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();
            try {
                if (DocCategory.WORK.isEqualTo(requestDocuments.get(0).getCategory())) {
                    if (DocType.BIB.isEqualTo(requestDocuments.get(0).getType())) {
                        if (DocFormat.MARC.isEqualTo(requestDocuments.get(0).getFormat())) {
                            WorkBibMarcDocBuilder marcBuilder = new WorkBibMarcDocBuilder();
                            for (RequestDocument requestDocument : requestDocuments) {
                                marcBuilder.buildSolrInputDocument(requestDocument, solrInputDocuments,
                                        buildSolrInputDocTimer, xmlToPojoTimer);
                            }
                        } else if (DocFormat.DUBLIN_CORE.isEqualTo(requestDocuments.get(0).getFormat())) {
                            WorkBibDublinDocBuilder dublinBuilder = new WorkBibDublinDocBuilder();
                            for (RequestDocument requestDocument : requestDocuments) {
                                dublinBuilder.buildSolrInputDocument(requestDocument, solrInputDocuments);
                            }
                        } else if (DocFormat.DUBLIN_UNQUALIFIED.isEqualTo(requestDocuments.get(0).getFormat())) {
                            WorkBibDublinUnQualifiedDocBuilder dublinUnqBuilder
                                    = new WorkBibDublinUnQualifiedDocBuilder();
                            for (RequestDocument requestDocument : requestDocuments) {
                                dublinUnqBuilder.buildSolrInputDocument(requestDocument, solrInputDocuments);
                            }
                        }
                    } else if (DocType.INSTANCE.isEqualTo(requestDocuments.get(0).getType())) {
                        WorkInstanceOlemlDocBuilder oleMlDocBuilder = new WorkInstanceOlemlDocBuilder();
                        for (RequestDocument requestDocument : requestDocuments) {
                            if(requestDocument != null && requestDocument.getContent() != null && requestDocument.getContent().getContentObject() != null) {
                                InstanceCollection instanceCollection = (InstanceCollection) requestDocument.getContent().getContentObject();
                                if(instanceCollection.getInstance() != null && instanceCollection.getInstance().size() > 0) {
                                    Instance instance = instanceCollection.getInstance().get(0);
                                    for (String rId : instance.getResourceIdentifier()) {
                                        List<SolrDocument> docs = getSolrDocumentBySolrId(rId);
                                        for (SolrDocument solrDoc : docs) {
                                            SolrInputDocument bibSolrIDoc = ClientUtils.toSolrInputDocument(solrDoc);
                                            String bibId = bibSolrIDoc.getFieldValue(WorkBibCommonFields.UNIQUE_ID).toString();
                                            if (bibIdToDocMap.get(bibId) == null) {
                                                bibIdToDocMap.put(bibId, bibSolrIDoc);
                                            }
                                            bibIdToDocMap.get(bibId)
                                                    .addField("instanceIdentifier", instance.getInstanceIdentifier());
                                        }
                                    }
                                    oleMlDocBuilder.buildSolrInputDocuments(requestDocument, solrInputDocuments);
                                }
                            }
                        }
                    }
                }
                //                if (DocCategory.SECURITY.isEqualTo(requestDocuments.get(0).getCategory())) {
                //                    if (DocType.PATRON.isEqualTo(requestDocuments.get(0).getType())) {
                //                        if (DocFormat.OLEML.isEqualTo(requestDocuments.get(0).getFormat())) {
                //                            SecurityPatronOlemlDocBuilder patronBuilder = new SecurityPatronOlemlDocBuilder();
                //                            for (RequestDocument requestDocument : requestDocuments) {
                //                                patronBuilder.buildSolrInputDocument(requestDocument, solrInputDocuments);
                //                            }
                //                        }
                //                    }
                //                }
                assignUUIDs(solrInputDocuments, null);
                solrInputDocuments.addAll(bibIdToDocMap.values());
            } catch (Exception e1) {
                result = buildFailureMsg(null, "Bulk Indexing failed. " + e1.getMessage());
                LOG.error(result, e1);
                return result;
            }
            timer.stop();
            if (solrInputDocuments.isEmpty()) {
                result = buildFailureMsg(null, "No valid documents found in input.");
                return result;
            }
            int numDocs = solrInputDocuments.size();
            batchStatistics.setTimeToConvertXmlToPojo(xmlToPojoTimer.getTime());
            batchStatistics.setTimeToConvertToSolrInputDocs(buildSolrInputDocTimer.getTime());
            StopWatch indexingTimer = new StopWatch();
            indexingTimer.start();
            try {
                result = indexSolrDocuments(solrInputDocuments, isCommit, false, false, false);
                indexingTimer.stop();
                //                batchStatistics.setTimeToIndexSolrInputDocs(indexingTimer.toString());
            } catch (Exception e) {
                result = buildFailureMsg(null, "Indexing failed. " + e.getMessage());
                LOG.error(result, e);
            }
            LOG.debug("Time Consumptions...:\txmlToObj(" + numDocs + "):" + xmlToPojoTimer + "\tbuildSolrInputDoc("
                    + numDocs + "):" + buildSolrInputDocTimer + "\tTotal(" + numDocs + "):" + timer.toString()
                    + "\t indexingTime(" + solrInputDocuments.size() + "):" + indexingTimer.toString());
        }
        return result;
    }

    public List<SolrDocument> getSolrDocumentBySolrId(String uniqueId) {
        QueryResponse response = null;
        String result = null;
        try {
            String args = "(" + WorkBibCommonFields.UNIQUE_ID + ":" + uniqueId + ")";
            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
            SolrQuery query = new SolrQuery();
            query.setQuery(args);
            response = solr.query(query);
        } catch (Exception e) {
            result = buildFailureMsg();
            LOG.error(result, e);
        }
        return response.getResults();
    }

//    public List<SolrDocument> getSolrDocumentBySolrId(String uniqueId,String docType) {
//        QueryResponse response = null;
//        String result = null;
//        try {
//            String args = "(" + WorkBibCommonFields.ID + ":" + uniqueId +" AND DocType:"+docType+" )";
//            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
//            SolrQuery query = new SolrQuery();
//            query.setQuery(args);
//            response = solr.query(query);
//        }
//        catch (Exception e) {
//            result = buildFailureMsg();
//            LOG.error(result, e);
//        }
//        return response.getResults();
//    }


    public List<SolrDocument> getSolrDocument(String fieldName, String fieldValue) {
        QueryResponse response = null;
        String result = null;
        try {
            String args = "(" + fieldName + ":" + fieldValue + ")";
            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
            SolrQuery query = new SolrQuery();
            query.setQuery(args);
            response = solr.query(query);
        } catch (Exception e) {
            result = buildFailureMsg();
            LOG.error(result, e);
        }
        return response.getResults();
    }

    /**
     * Builds a UUID value with an identifiable prefix.
     * Used when a document does not have a UUID assigned by docstore.
     * Typical scenario is when discovery layer is used without docstore.
     *
     * @return
     */
    public String buildUuid() {
        String uuid = UUID.randomUUID().toString();
        uuid = ID_FIELD_PREFIX + uuid; // identifies the uuid generated by discovery module.
        return uuid;
    }

    /**
     * Assigns UUIDs for each document (that does not have an "id" field) in the given list.
     * Also makes sure "uniqueId" field is present. The UUIDs generated by this method start
     * with ID_FIELD_PREFIX for easy identification. Optionally takes a list
     * of UUIDs to be used to set/override the "id" field values of the documents.
     *
     * @param solrDocs
     * @param ids      List of id values (optional) to be used for the given documents.
     */
    protected void assignUUIDs(List<SolrInputDocument> solrDocs, List<String> ids) throws Exception {
        if ((null == solrDocs) || (solrDocs.size() == 0)) {
            return;
        }
        if ((null != ids) && (ids.size() < solrDocs.size())) {
            throw new Exception(
                    "Insufficient UUIDs(" + ids.size() + ") specified for documents(" + solrDocs.size() + ".");
        }
        for (int i = 0; i < solrDocs.size(); i++) {
            SolrInputDocument solrInputDocument = solrDocs.get(i);
            SolrInputField idField = solrInputDocument.getField("id");
            String uuid = null;
            if (null != ids) {
                // Get the supplied UUID.
                uuid = ids.get(i);
            }
            if (null == idField) {
                if (null == uuid) {
                    // Generate UUID.
                    uuid = UUID.randomUUID().toString();
                    uuid = ID_FIELD_PREFIX + uuid; // identifies the uuid generated by discovery module.
                }
                solrInputDocument.addField(WorkBibCommonFields.ID, uuid);
                solrInputDocument.addField(WorkBibCommonFields.UNIQUE_ID, uuid);
            } else {
                if (null != uuid) {
                    // Use the supplied UUID.
                    solrInputDocument.setField(WorkBibCommonFields.ID, uuid);
                    solrInputDocument.setField(WorkBibCommonFields.UNIQUE_ID, uuid);
                } else {
                    // Leave the existing id value and make sure uniqueId is set.
                    //                    uuid = (String) idField.getValue();
                    if (idField.getValue() instanceof List) {
                        List<String> uuidList = (List<String>) idField.getValue();
                        uuid = uuidList.get(0);
                    } else if (idField.getValue() instanceof String) {
                        uuid = (String) idField.getValue();
                    }
                    if (null == uuid) {
                        // Generate UUID.
                        uuid = UUID.randomUUID().toString();
                        uuid = ID_FIELD_PREFIX + uuid; // identifies the uuid generated by discovery module.
                        idField.setValue(uuid, 1.0f);
                    }
                    SolrInputField uniqueIdField = solrInputDocument.getField(WorkBibCommonFields.UNIQUE_ID);
                    if (null == uniqueIdField) {
                        solrInputDocument.addField(WorkBibCommonFields.UNIQUE_ID, uuid);
                    } else {
                        if (uniqueIdField.getValue() == null) {
                            solrInputDocument.setField(WorkBibCommonFields.UNIQUE_ID, uuid);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void commit() throws Exception {
        boolean waitFlush = false;
        boolean waitSearcher = false;
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        solr.commit(waitFlush, waitSearcher);
    }

    @Override
    public void rollback() throws Exception {
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        solr.rollback();
    }

    protected String indexSolrDocuments(List<SolrInputDocument> solrDocs, boolean isCommit, boolean optimize,
                                        boolean waitFlush, boolean waitSearcher) throws Exception {
        BulkIngestStatistics bulkLoadStatistics = BulkIngestStatistics.getInstance();
        BatchIngestStatistics batchStatistics = bulkLoadStatistics.getCurrentBatch();
        long commitSize = batchStatistics.getCommitSize();
        long recCount = batchStatistics.getRecCount();
        StopWatch indexSolrDocsTime = new StopWatch();
        StopWatch solrCommitTime = new StopWatch();
        indexSolrDocsTime.start();
        SolrServer solr = null;
        if ((null == solrDocs) || (solrDocs.isEmpty())) {
            return SUCCESS + "-0";
        }
        solr = SolrServerManager.getInstance().getSolrServer();
        if (solrDocs.size() > BATCH_SIZE) {
            int numSolrDocs = solrDocs.size();
            for (int fromIndex = 0; fromIndex < numSolrDocs; fromIndex += BATCH_SIZE) {
                int toIndex = fromIndex + BATCH_SIZE;
                if (toIndex > numSolrDocs) {
                    toIndex = numSolrDocs;
                }
                List batchSolrDocs = solrDocs.subList(fromIndex, toIndex);
                if ((null != batchSolrDocs) && (!batchSolrDocs.isEmpty())) {
                    LOG.info("Indexing records. fromIndex=" + fromIndex + ", toIndex=" + toIndex);
                    UpdateResponse response = solr.add(solrDocs);
                }
            }
        } else {
            LOG.debug("Indexing records. size=" + solrDocs.size());
            UpdateResponse response = solr.add(solrDocs);
        }
        indexSolrDocsTime.stop();
        commitRecCount = commitRecCount + recCount;
        if (commitRecCount == commitSize || bulkLoadStatistics.isLastBatch()) {
          //  isCommit = true;
        }
        if (isCommit) {
            solrCommitTime.start();
            LOG.info("Bulk ingest: Index commit started. Number of records being committed: " + bulkLoadStatistics
                    .getCommitRecCount());
            solr.commit(waitFlush, waitSearcher);
            commitRecCount = 0;
            solrCommitTime.stop();
        }
        if (optimize) {
            solr.optimize(waitFlush, waitSearcher);
        }


        LOG.debug("Time Consumptions...: Solr input docs of size ..." + solrDocs.size()
                + "\t time taken to index solr Input Docs" + indexSolrDocsTime + "solrcommit & Optimize"
                + solrCommitTime);
        batchStatistics.setTimeToIndexSolrInputDocs(indexSolrDocsTime.getTime());
        batchStatistics.setTimeToSolrCommit(solrCommitTime.getTime());
        return SUCCESS + "-" + solrDocs.size();
    }

    protected String indexSolrDocuments(List<SolrInputDocument> solrDocs, boolean commit, boolean optimize)
            throws Exception {
        String result = indexSolrDocuments(solrDocs, commit, optimize, true, true);
        return result;
    }

    protected List<SolrInputDocument> convertToSolrDocs(String docCategory, String docType, String docFormat,
                                                        String docContent) throws Exception {
        List<SolrInputDocument> solrDocs = null;
        if (DocCategory.WORK.isEqualTo(docCategory) && DocType.BIB.isEqualTo(docType) && DocFormat.MARC.isEqualTo(
                docFormat)) {
            try {
                WorkBibMarcRecordProcessor recordProcessor = new WorkBibMarcRecordProcessor();
                solrDocs = new WorkBibMarcDocBuilder()
                        .buildSolrInputDocuments(recordProcessor.fromXML(docContent).getRecords());
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new Exception("Exception while converting given XML Document: ", e);
            }
        } else if (DocCategory.WORK.isEqualTo(docCategory) && DocType.BIB.isEqualTo(docType) && DocFormat.DUBLIN_CORE
                .isEqualTo(
                        docFormat)) {
            WorkBibDublinRecordProcessor processor = new WorkBibDublinRecordProcessor();
            WorkBibDublinRecord record = processor.fromXML(docContent);
            solrDocs = new ArrayList<SolrInputDocument>();
            solrDocs.add(new WorkBibDublinDocBuilder().buildSolrInputDocument(record));
        } else if (DocCategory.WORK.isEqualTo(docCategory) && DocType.BIB.isEqualTo(docType) && DocFormat
                .DUBLIN_UNQUALIFIED.isEqualTo(docFormat)) {
            solrDocs = new WorkBibDublinUnQualifiedDocBuilder()
                    .buildSolrInputDocuments(new WorkBibDublinUnQualifiedRecordProcessor().fromXML(docContent));
        } else {
            throw new Exception("UnSupported Document Format: " + docCategory + ", " + docType + ", " + docFormat);
        }
        return solrDocs;
    }

    protected String deleteDocumentByUUID(String uuid, String category, boolean commit) {
        String result = SUCCESS;
        try {
            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
            solr.deleteById(uuid);
            if (commit) {
                solr.commit();
            }
        } catch (Exception e) {
            result = buildFailureMsg();
            LOG.error(result, e);
        }
        return result;
    }

    protected String deleteDocumentByUUID(String uuid, String category) {
        return deleteDocumentByUUID(uuid, category, true);
    }

    protected String deleteDocumentsByUUIDList(List<String> uuidList, String category, boolean commit) {
        String result = SUCCESS;
        try {
            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
            List<String> uuidList1 = new ArrayList<String>();
            if (uuidList.size() > 0) {
                for (String id : uuidList) {
                    if (id != null) {
                        uuidList1.add(id);
                    }
                }
            }
            if (uuidList1.size() > 0) {
                solr.deleteById(uuidList1);
            }
            if (commit) {
                solr.commit();
            }
        } catch (Exception e) {
            result = buildFailureMsg();
            LOG.error(result, e);
        }
        return result;
    }

    protected String deleteDocumentsByUUIDList(List<String> uuidsList, String category)
            throws SolrServerException, MalformedURLException {
        List<String> deleteUuidsList = new ArrayList<String>();
        List<String> holdingsIdentifierList = new ArrayList<String>();
        List<String> itemIdentifierList = new ArrayList<String>();
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        SolrQuery query = new SolrQuery();
        deleteUuidsList.addAll(uuidsList);
        for (int i = 0; i < uuidsList.size(); i++) {
            query.setQuery("id:" + uuidsList.get(i));
            QueryResponse response = solr.query(query);
            LOG.debug("query-->" + query);
            for (SolrDocument doc : response.getResults()) {
                LOG.debug("doc" + doc.toString());
                String docFormat = (String) doc.getFieldValue(DOC_FORMAT);
                String docType = (String) doc.getFieldValue(DOC_TYPE);
                if (docType.equalsIgnoreCase(BIBLIOGRAPHIC)) {
                } else if (docType.equalsIgnoreCase(INSTANCE)) {
                    if (doc.getFieldValue(ITEM_IDENTIFIER) instanceof List) {
                        itemIdentifierList = (List<String>) doc.getFieldValue(ITEM_IDENTIFIER);
                    } else {
                        itemIdentifierList.add((String) doc.getFieldValue(ITEM_IDENTIFIER));
                    }
                    if (doc.getFieldValue(HOLDINGS_IDENTIFIER) instanceof String) {
                        holdingsIdentifierList.add((String) doc.getFieldValue(HOLDINGS_IDENTIFIER));
                    } else {
                        holdingsIdentifierList = (List<String>) doc.getFieldValue(HOLDINGS_IDENTIFIER);
                    }
                    if (holdingsIdentifierList != null && holdingsIdentifierList.size() > 0) {
                        deleteUuidsList.addAll(holdingsIdentifierList);
                    }
                    if (itemIdentifierList != null && itemIdentifierList.size() > 0) {
                        deleteUuidsList.addAll(itemIdentifierList);

                    }
                }
            }
        }
        return deleteDocumentsByUUIDList(deleteUuidsList, category, true);
    }

    @Deprecated
    protected String buildDeleteQueryParamsForDeleteUrl(List<String> uuidList, boolean commit) {
        StringBuffer deleteQueryBuffer = new StringBuffer("");
        deleteQueryBuffer.append("stream.body=");
        deleteQueryBuffer.append("<delete>");
        for (int i = 0; i < uuidList.size(); i++) {
            deleteQueryBuffer.append("<query>");
            deleteQueryBuffer.append("id:");
            deleteQueryBuffer.append(uuidList.get(i));
            deleteQueryBuffer.append("</query>");
        }
        deleteQueryBuffer.append("</delete>");
        if (commit) {
            deleteQueryBuffer.append("&stream.body=<commit/>");
        }
        return deleteQueryBuffer.toString();

    }

    @Deprecated
    protected String buildDeleteQuery(String uuid, String category, boolean commit) {
        StringBuffer deleteQueryUrl = new StringBuffer("");
        if (commit) {
            deleteQueryUrl.append(SolrServerManager.getInstance().getSolrCoreURL());
            deleteQueryUrl.append("/update?stream.body=<delete><query>id:" + uuid
                    + "</query></delete>&stream.body=<commit/>");
        } else {
            deleteQueryUrl.append(SolrServerManager.getInstance().getSolrCoreURL());
            deleteQueryUrl.append("/update?stream.body=<delete><query>id:" + uuid + "</query></delete>");
        }
        return deleteQueryUrl.toString();
    }

    /**
     * @param inputURL
     * @throws Exception
     */
    @Deprecated
    protected void openConnection(URL inputURL) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) inputURL.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.connect();
        OutputStreamWriter streamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
        streamWriter.flush();
        // Get the response from inputURL
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String xmlResponse;
        while ((xmlResponse = bufferReader.readLine()) != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("XmlResponse->" + xmlResponse);
            }
        }
    }

    protected String getErrorID() {
        return String.valueOf(new Date().getTime());
    }

    protected String buildFailureMsg(String id, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(FAILURE).append("-ErrorID:");
        if (null != id) {
            sb.append(id);
        } else {
            sb.append(getErrorID());
        }
        if (null != msg) {
            sb.append("-ErrorMsg:").append(msg);
        }
        return sb.toString();
    }

    protected String buildFailureMsg() {
        return FAILURE + "-ErrorID:" + getErrorID();
    }

    public QueryResponse searchBibRecord(String docCat, String docType, String docFormat, String fieldName,
                                         String fieldValue, String fieldList) {
        QueryResponse response = null;
        String result = null;
        try {
            String identifier_args = "(" + fieldName + ":" + fieldValue + ")";
            String docCategory_args = "(DocCategory" + ":" + docCat + ")";
            String docType_args = "(DocType" + ":" + docType + ")";
            String docFormat_args = "(DocFormat" + ":" + docFormat + ")";
            String args = identifier_args + "AND" + docCategory_args + "AND" + docType_args + "AND" + docFormat_args;
            SolrServer solr = new HttpSolrServer(
                    ConfigContext.getCurrentContextConfig().getProperty("docSearchURL") + "bib");
            SolrQuery query = new SolrQuery();
            query.addField(fieldList);
            query.setQuery(args);
            response = solr.query(query);
        } catch (Exception e) {
            result = buildFailureMsg();
            LOG.error(result, e);
        }
        return response;
    }

    @Override
    public void cleanupDiscoveryData() throws IOException, SolrServerException {
        SolrServer server = null;
        try {
            server = SolrServerManager.getInstance().getSolrServer();
        } catch (SolrServerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        server.deleteByQuery("*:*");
        server.commit();

    }

    @Override
    public String bind(List<RequestDocument> requestDocuments) throws Exception {
        String result = null;
        for (RequestDocument requestDocument : requestDocuments) {
            result = bind(requestDocument);
        }
        return result;
    }

    @Override
    public String bind(RequestDocument requestDocument) throws Exception {
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
        updateInstanceDocument(requestDocument, solrInputDocumentList);
        updateBibDocument(requestDocument, solrInputDocumentList);
        LOG.info("solrInputDocumentList-->" + solrInputDocumentList);
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        UpdateResponse updateResponse = server.add(solrInputDocumentList);
        server.commit();
        return "success";
    }

    private void updateBibDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocumentList) throws Exception {
        List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
        for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
            SolrDocument bibSolrDocument = getSolrDocumentByUUID(linkedRequestDocument.getUuid(),
                    linkedRequestDocument.getType());
            List<String> instanceIdentifierList = new ArrayList<String>();
            Object instanceIdentifier = bibSolrDocument.getFieldValue("instanceIdentifier");
            if (instanceIdentifier instanceof List) {
                instanceIdentifierList = (List<String>) bibSolrDocument.getFieldValue("instanceIdentifier");

            } else if (instanceIdentifier instanceof String) {
                instanceIdentifierList.add((String) instanceIdentifier);
            }

            instanceIdentifierList.add(requestDocument.getUuid());
            bibSolrDocument.setField("instanceIdentifier", instanceIdentifierList);
            solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(bibSolrDocument));

        }
    }

    /**
     * @param requestDocument
     * @param solrInputDocumentList
     * @throws Exception
     * @throws java.io.IOException
     */
    private void updateInstanceDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocumentList) throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery(("id:" + requestDocument.getUuid() + " AND DocType:" + requestDocument.getType()));
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        LOG.debug("response.getResults()-->" + response.getResults());
        for (SolrDocument solrDocument : solrDocumentList) {
            List<String> bibIdentifierList = new ArrayList<String>();

            //Get the BibIdentifier list and update the bibIdentifier field with the linked doc uuid's.
            Object bibIdentifier = solrDocument.getFieldValue("bibIdentifier");
            if (bibIdentifier instanceof List) {
                bibIdentifierList = (List<String>) solrDocument.getFieldValue("bibIdentifier");

            } else if (bibIdentifier instanceof String) {
                bibIdentifierList.add((String) bibIdentifier);
            }
            LOG.info("bibIdentifierList-->" + bibIdentifierList);
            List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
            for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
                bibIdentifierList.add(linkedRequestDocument.getUuid());
            }
            solrDocument.setField("bibIdentifier", bibIdentifierList);
            solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(solrDocument));

            //Get the holdings identifier from the solrdoc and update bibIdentifier field in holding doc with the linked doc uuid's.
            Object holdingsIdentifier = solrDocument.getFieldValue("holdingsIdentifier");
            List<String> holdingIdentifierList = new ArrayList<String>();
            if (holdingsIdentifier instanceof List) {
                holdingIdentifierList = (List<String>) solrDocument.getFieldValue("holdingsIdentifier");

            } else if (holdingsIdentifier instanceof String) {
                holdingIdentifierList.add((String) holdingsIdentifier);

            }
            SolrDocument holdingSolrDocument = getSolrDocumentByUUID(holdingIdentifierList.get(0),
                    DocType.HOLDINGS.getCode());
            holdingSolrDocument.setField("bibIdentifier", bibIdentifierList);
            solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(holdingSolrDocument));

            //Get the item identifier from the solrdoc and update bibIdentifier field in item doc with the linked doc uuid's.
            Object itemIdentifier = solrDocument.getFieldValue("itemIdentifier");
            List<String> itemIdentifierList = new ArrayList<String>();
            if (itemIdentifier instanceof List) {
                itemIdentifierList = (List<String>) solrDocument.getFieldValue("itemIdentifier");

            } else if (itemIdentifier instanceof String) {
                itemIdentifierList.add((String) itemIdentifier);
            }

            for (String itemId : itemIdentifierList) {
                SolrDocument itemSolrDocument = getSolrDocumentByUUID(itemId, DocType.ITEM.getCode());
                itemSolrDocument.setField("bibIdentifier", bibIdentifierList);
                solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(itemSolrDocument));

            }


        }
    }


    @Override
    public String unbind(List<RequestDocument> requestDocuments) throws Exception {
        for (RequestDocument requestDocument : requestDocuments) {
            List<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
            unbindFromInstanceDocument(requestDocument, solrInputDocumentList);
            unbindFromBibDocument(requestDocument, solrInputDocumentList);
            LOG.info("solrInputDocumentList-->" + solrInputDocumentList);
            SolrQuery solrQuery = new SolrQuery();
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            UpdateResponse updateResponse = server.add(solrInputDocumentList);
            server.commit();

        }
        return "success";


    }

    @Override
    public void transferInstances(List<RequestDocument> requestDocuments) throws Exception {
        String destBibIdentifier = requestDocuments.get(requestDocuments.size() - 1).getUuid();
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
        // List<String> intanceIdentifierList=new ArrayList<String>();
        List<String> sourceBibUUIDList = new ArrayList<String>();
        List<String> instanceIdentifiersList = new ArrayList<String>();
        for (RequestDocument requestDocument : requestDocuments) {
            if (requestDocument.getType().equalsIgnoreCase("instance") && requestDocument.getFormat()
                    .equalsIgnoreCase("oleml")) {
                instanceIdentifiersList.add(requestDocument.getUuid());
            }
        }
        modifyInstanceSolrDocumentForTransferInstances(instanceIdentifiersList, sourceBibUUIDList, destBibIdentifier,
                solrInputDocumentList);
        LOG.debug("sourceBibUUIDList " + sourceBibUUIDList);
        LOG.debug("instanceIdentifiersList " + instanceIdentifiersList);
        LOG.debug("destBibIdentifier " + destBibIdentifier);

        modifySolrDocForSourceBibForTransferInstances(sourceBibUUIDList, instanceIdentifiersList,
                solrInputDocumentList);
        modifySolrDocForDestBibForTransferInstances(destBibIdentifier, instanceIdentifiersList, solrInputDocumentList);
        SolrQuery query = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        UpdateResponse updateResponse = server.add(solrInputDocumentList);
        //server.commit();
        LOG.debug("updateResponse " + updateResponse);
    }

    private void modifyInstanceSolrDocumentForTransferInstances(List<String> instanceIdentifiersList,
                                                                List<String> sourceBibUUIDList,
                                                                String destBibIdentifier,
                                                                List<SolrInputDocument> solrInputDocumentList)
            throws Exception {
        String sourceBibIdentifier = "";
        String holdingsIdentifier = "";
        SolrDocumentList solrDocumentList = getSolrDocumentByUUIDs(instanceIdentifiersList);
        for (SolrDocument instanceSolrDocument : solrDocumentList) {
            // String instanceIdentifier = requestDocument.getUuid();
            // intanceIdentifierList.add(instanceIdentifier);
            // SolrDocument instanceSolrDocument = getSolrDocumentByUUID(instanceIdentifier);
            sourceBibIdentifier = (String) instanceSolrDocument.getFieldValue("bibIdentifier");
            sourceBibUUIDList.add(sourceBibIdentifier);
            holdingsIdentifier = (String) instanceSolrDocument.getFieldValue("holdingsIdentifier");

            SolrDocument holdingSolrDocument = getSolrDocumentByUUID(holdingsIdentifier);

            holdingSolrDocument.setField("bibIdentifier", destBibIdentifier);
            solrInputDocumentList.add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(holdingSolrDocument));

            modifyItemSolrDocumentForTransferInstances(instanceSolrDocument, destBibIdentifier, solrInputDocumentList);
            instanceSolrDocument.setField("bibIdentifier", destBibIdentifier);
            solrInputDocumentList.add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(instanceSolrDocument));
        }
    }

    private void modifyItemSolrDocumentForTransferInstances(SolrDocument instanceSolrDocument, String destBibIdentifier,
                                                            List<SolrInputDocument> solrInputDocumentList)
            throws Exception {
        Object object = instanceSolrDocument.getFieldValue("itemIdentifier");
        //Bib can have more than one item identifier. If there is one item identifier solr will return item identifier string. If there are more than one item identifier solr returns
        //list of item identifiers
        if (object instanceof String) {
            String itemIdentifier = (String) object;
            SolrDocument itemSolrDocument = getSolrDocumentByUUID(itemIdentifier);
            itemSolrDocument.setField("bibIdentifier", destBibIdentifier);
            solrInputDocumentList.add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(itemSolrDocument));
        } else if (object instanceof List) {
            List<String> itemIdentifierList = (List<String>) object;
            SolrDocumentList solrDocumentList = getSolrDocumentByUUIDs(itemIdentifierList);
            for (SolrDocument itemSolrDocument : solrDocumentList) {
                itemSolrDocument.setField("bibIdentifier", destBibIdentifier);
                solrInputDocumentList.add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(itemSolrDocument));
            }
        }

    }

    private SolrDocumentList getSolrDocumentByUUIDs(List<String> uuids) throws Exception {
        String operand = "OR";
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (String uuid : uuids) {
            sb.append("(");
            sb.append(uuid);
            sb.append(")");
            sb.append(operand);
        }
        String queryString = sb.substring(0, sb.length() - operand.length()) + ")";
        LOG.debug("queryString " + queryString);
        SolrQuery query = new SolrQuery();
        SolrDocument solrDocument = null;
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        QueryResponse response = null;
        query.setQuery("id:" + queryString);
        response = server.query(query);
        LOG.debug("size " + response.getResults().size());
        SolrDocumentList solrDocumentList = response.getResults();
        return solrDocumentList;

    }

    private void modifySolrDocForDestBibForTransferInstances(String destBibIdentifier,
                                                             List<String> intanceIdentifierList,
                                                             List<SolrInputDocument> solrInputDocumentList)
            throws Exception {

        SolrDocument destBibSolrDocument = getSolrDocumentByUUID(destBibIdentifier);
        destBibSolrDocument.addField("instanceIdentifier", intanceIdentifierList);
        solrInputDocumentList.add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(destBibSolrDocument));
    }

    private void modifySolrDocForSourceBibForTransferInstances(List<String> sourceBibUUIDList,
                                                               List<String> instanceUUIDList,
                                                               List<SolrInputDocument> solrInputDocumentList)
            throws Exception {
        List bibIdentifierList;
        SolrDocumentList solrDocumentList = getSolrDocumentByUUIDs(sourceBibUUIDList);
        for (SolrDocument solrDocumentForSourceBib : solrDocumentList) {
            Object field = solrDocumentForSourceBib.getFieldValue("instanceIdentifier");
            if (field instanceof String) {
                String instanceIdentifier = (String) solrDocumentForSourceBib.getFieldValue("instanceIdentifier");
                solrDocumentForSourceBib.setField("instanceIdentifier", "");
            } else if (field instanceof List) {
                bibIdentifierList = (List) solrDocumentForSourceBib.getFieldValue("instanceIdentifier");
                for (String instanceIdenfier : instanceUUIDList) {
                    if (bibIdentifierList.contains(instanceIdenfier)) {
                        bibIdentifierList.remove(instanceIdenfier);
                    }
                }
                solrDocumentForSourceBib.setField("instanceIdentifier", bibIdentifierList);

            }
            solrInputDocumentList.add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(solrDocumentForSourceBib));

        }

    }

    @Override
    public void transferItems(List<RequestDocument> requestDocuments) throws Exception {
        LOG.debug("IndexerServiceImpl transferItems");
        List<SolrInputDocument> solrInputDocumentListFinal = new ArrayList<SolrInputDocument>();
        String destinationInstanceIdentifier = requestDocuments.get(requestDocuments.size() - 1).getUuid();
        List<String> itemUUIDList = new ArrayList<String>();
        List<String> sourceInstanceUUIDList = new ArrayList<String>();
        modifySolrDocForItem(requestDocuments, sourceInstanceUUIDList, itemUUIDList, destinationInstanceIdentifier,
                solrInputDocumentListFinal);
        LOG.debug("sourceInstanceUUIDList " + sourceInstanceUUIDList);
        LOG.debug("itemUUIDList " + itemUUIDList);
        modifySolrDocForDestInstance(destinationInstanceIdentifier, itemUUIDList, solrInputDocumentListFinal);
        modifySolrDocForSourceInstance(sourceInstanceUUIDList, itemUUIDList, solrInputDocumentListFinal);
        LOG.debug("solrInputDocumentListFinal size " + solrInputDocumentListFinal.size());
        SolrQuery query = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        UpdateResponse updateResponse = server.add(solrInputDocumentListFinal);
        // server.commit();
        LOG.debug("updateResponse " + updateResponse);
    }

    @Override
    public String delete(List<RequestDocument> requestDocuments) throws Exception {
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
        SolrQuery solrQuery = new SolrQuery();
        for (RequestDocument requestDocument : requestDocuments) {
            if (requestDocument.getCategory().equalsIgnoreCase("work") && requestDocument.getType().equalsIgnoreCase("bibliographic")
                    && requestDocument.getFormat().equalsIgnoreCase("marc")) {
                String query = "bibIdentifier:" + requestDocument.getUuid() + " OR " + "id:" + requestDocument.getUuid();
                UpdateResponse updateResponse = server.deleteByQuery(query);
                LOG.debug("updateResponse " + updateResponse);
            }
            if (requestDocument.getCategory().equalsIgnoreCase("work") && requestDocument.getType().equalsIgnoreCase("instance")
                    && requestDocument.getFormat().equalsIgnoreCase("oleml")) {
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
                    Object itemId = solrDocument.getFieldValue("itemIdentifier");
                    if (itemId != null) {
                        if (itemId instanceof List) {
                            List<String> itemList = (List<String>) solrDocument.getFieldValue("itemIdentifier");
                            deleteIdList.addAll(itemList);
                        } else if (itemId instanceof String) {
                            String itmId = (String) itemId;
                            deleteIdList.add(itmId);
                        }
                    }
                }
                deleteIdList.add(requestDocument.getUuid());
                deleteDocumentsByUUIDList(deleteIdList, "instance", true);
                query = "instanceIdentifier:" + requestDocument.getUuid() + "AND" + "(DocType:bibliographic)";
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
                            instanceIdentifier = "";
                            bibSolrDocument.setField("instanceIdentifier", instanceIdentifier);
                        }
                    }
                    solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(bibSolrDocument));
                }
                String result = indexSolrDocuments(solrInputDocumentList, true);
                if (!result.contains(SUCCESS)) {
                    return result;
                }
//                return result;
            }
            if (requestDocument.getCategory().equalsIgnoreCase("work") && requestDocument.getType().equalsIgnoreCase("item")
                    && requestDocument.getFormat().equalsIgnoreCase("oleml")) {
                String query = "id:" + requestDocument.getUuid();
                UpdateResponse updateResponse = server.deleteByQuery(query);
                query = "itemIdentifier:" + requestDocument.getUuid();
                solrQuery.setQuery(query);
                QueryResponse response = server.query(solrQuery);
                List<SolrDocument> solrDocumentList = response.getResults();
                for (SolrDocument instanceSolrDocument : solrDocumentList) {
                    List<String> itemIdentifierList = new ArrayList<String>();
                    Object itemIdentifier = instanceSolrDocument.getFieldValue("itemIdentifier");
                    if (itemIdentifier instanceof List) {
                        itemIdentifierList = (List<String>) instanceSolrDocument.getFieldValue("itemIdentifier");
                        if (itemIdentifierList.contains(requestDocument.getUuid())) {
                            itemIdentifierList.remove(requestDocument.getUuid());
                            instanceSolrDocument.setField("itemIdentifier", itemIdentifierList);
                        }
                    } else if (itemIdentifier instanceof String) {
                        String itemId = (String) itemIdentifier;
                        if (itemId.equalsIgnoreCase(requestDocument.getUuid())) {
                            itemIdentifier = "";
                            instanceSolrDocument.setField("itemIdentifier", itemIdentifier);
                        }
                    }
                    solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(instanceSolrDocument));
                }
                String result = indexSolrDocuments(solrInputDocumentList, true);
                if (!result.contains(SUCCESS)) {
                    return result;
                }
//                return result;
            }
        }
//        server.commit();
        return "success";
    }


    @Override
    public String delete(RequestDocument requestDocument) throws Exception {
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
        SolrQuery solrQuery = new SolrQuery();
        Date date = new Date();
        if (requestDocument.getCategory().equalsIgnoreCase("work") && requestDocument.getType()
                .equalsIgnoreCase("bibliographic")
                && requestDocument.getFormat().equalsIgnoreCase("marc")) {
            String query = "(bibIdentifier:" + requestDocument.getUuid() + ") OR (DocType:" + requestDocument.getType()
                    + " AND id:" + requestDocument.getUuid() + ")";
            LOG.info("query-->" + query);
            UpdateResponse updateResponse = server.deleteByQuery(query);
            LOG.info("updateResponse " + updateResponse);
            // save deleted  Bibliographic with Bibliographic_delete
            String newId = requestDocument.getUuid() + "_d";
            SolrInputDocument solrInputDocument = new SolrInputDocument();
            solrInputDocument.setField("DocType", "bibliographic_delete");
            solrInputDocument.setField("dateUpdated", date);
            solrInputDocument.setField("uniqueId", newId);
            solrInputDocument.setField("id", newId);
            solrInputDocument.setField("LocalId_display", DocumentLocalId.getDocumentIdDisplay(requestDocument.getUuid()));
            UpdateResponse updateResponseForBib = server.add(solrInputDocument);
            LOG.debug("updateResponse " + updateResponseForBib);
        }
        if (requestDocument.getCategory().equalsIgnoreCase("work") && requestDocument.getType()
                .equalsIgnoreCase("bibliographic")
                && requestDocument.getFormat().equalsIgnoreCase("dublinunq")) {
            String query = "(bibIdentifier:" + requestDocument.getUuid() + ") OR (DocType:" + requestDocument.getType()
                    + " AND id:" + requestDocument.getUuid() + ")";
            LOG.info("query-->" + query);
            UpdateResponse updateResponse = server.deleteByQuery(query);
            LOG.info("updateResponse " + updateResponse);
        }

        if (requestDocument.getCategory().equalsIgnoreCase("work") && requestDocument.getType()
                .equalsIgnoreCase("instance")
                && requestDocument.getFormat().equalsIgnoreCase("oleml")) {
            String query = "(instanceIdentifier:" + requestDocument.getUuid() + " AND DocFormat:" + requestDocument
                    .getFormat() + ")";
            LOG.info("query-->" + query);
            UpdateResponse updateResponse = server.deleteByQuery(query);
            query = "(id:" + requestDocument.getUuid() + " AND DocType:" + requestDocument.getType() + ")";
            server.deleteByQuery(query);
            query = "instanceIdentifier:" + requestDocument.getUuid() + " AND " + "(DocType:bibliographic)";
            solrQuery.setQuery(query);
            QueryResponse response = server.query(solrQuery);
            List<SolrDocument> solrDocumentList = response.getResults();
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
                bibSolrDocument.setField("dateUpdated", date);
                solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(bibSolrDocument));
            }
            String result = indexSolrDocuments(solrInputDocumentList, true);
            if (!result.contains(SUCCESS)) {
                return result;
            }
        }
        if (requestDocument.getCategory().equalsIgnoreCase("work") && requestDocument.getType().equalsIgnoreCase("item")
                && requestDocument.getFormat().equalsIgnoreCase("oleml")) {
            String query = "id:" + requestDocument.getUuid();
            UpdateResponse updateResponse = server.deleteByQuery(query);
            query = "itemIdentifier:" + requestDocument.getUuid();
            solrQuery.setQuery(query);
            QueryResponse response = server.query(solrQuery);
            List<SolrDocument> solrDocumentList = response.getResults();
            for (SolrDocument instanceSolrDocument : solrDocumentList) {
                List<String> itemIdentifierList = new ArrayList<String>();
                Object itemIdentifier = instanceSolrDocument.getFieldValue("itemIdentifier");
                if (itemIdentifier instanceof List) {
                    itemIdentifierList = (List<String>) instanceSolrDocument.getFieldValue("itemIdentifier");
                    if (itemIdentifierList.contains(requestDocument.getUuid())) {
                        itemIdentifierList.remove(requestDocument.getUuid());
                        instanceSolrDocument.setField("itemIdentifier", itemIdentifierList);
                    }
                } else if (itemIdentifier instanceof String) {
                    String itemId = (String) itemIdentifier;
                    if (itemId.equalsIgnoreCase(requestDocument.getUuid())) {
                        itemIdentifier = "";
                        instanceSolrDocument.setField("itemIdentifier", itemIdentifier);
                    }
                }
                solrInputDocumentList
                        .add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(instanceSolrDocument));
            }
            query = "id:" + requestDocument.getUuid();
            solrQuery.setQuery(query);
            response = server.query(solrQuery);
            List<SolrDocument> solrDocuments = response.getResults();
            String bibId = "";
            if(solrDocuments != null && solrDocuments.size() > 0) {
                SolrDocument itemDoc = solrDocuments.get(0);
                Object bibIdentifier = itemDoc.getFieldValue("bibIdentifier");
                if (bibIdentifier instanceof List) {
                    List<String> bibIds = (List) bibIdentifier;
                    bibId = bibIds.get(0);
                }
                else {
                    bibId = (String) bibIdentifier;
                }
            }
            if(StringUtils.isNotEmpty(bibId)) {
                query = "id:" + bibId;
                solrQuery.setQuery(query);
                response = server.query(solrQuery);
                List<SolrDocument> bibSolrDocumentList = response.getResults();
                for(SolrDocument bibSolrDocument : bibSolrDocumentList) {
                    bibSolrDocument.setField("dateUpdated", date);
                    solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(bibSolrDocument));
                }
            }
            String result = indexSolrDocuments(solrInputDocumentList, true);
            if (!result.contains(SUCCESS)) {
                return result;
            }
            //                return result;

        }
        return "success";

    }

    private void modifySolrDocForSourceInstance(List<String> sourceInstanceUUIDList, List<String> itemUUIDList,
                                                List<SolrInputDocument> solrInputDocumentListFinal) throws Exception {

        List itemIdentifierList = new ArrayList();
        for (String sourceInstanceIdentifier : sourceInstanceUUIDList) {
            SolrDocument solrDocumentForSourceInstance = getSolrDocumentByUUID(sourceInstanceIdentifier);
            Object field = solrDocumentForSourceInstance.getFieldValue("itemIdentifier");
            if (field instanceof String) {
                String instanceIdentifier = (String) solrDocumentForSourceInstance.getFieldValue("itemIdentifier");
                itemIdentifierList.add(instanceIdentifier);
            } else if (field instanceof List) {
                itemIdentifierList = (List) solrDocumentForSourceInstance.getFieldValue("itemIdentifier");
            }
            for (String itemIdenfier : itemUUIDList) {
                if (itemIdentifierList.contains(itemIdenfier)) {
                    itemIdentifierList.remove(itemIdenfier);
                }
            }
            solrDocumentForSourceInstance.setField("itemIdentifier", itemIdentifierList);
            String holdingsIdentifier = (String) solrDocumentForSourceInstance.getFieldValue("holdingsIdentifier");
            SolrDocument solrDocumentForHoldingsIdentifier = getSolrDocumentByUUID(holdingsIdentifier);
            solrDocumentForHoldingsIdentifier.setField("itemIdentifier", itemIdentifierList);
            solrInputDocumentListFinal
                    .add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(solrDocumentForSourceInstance));
            solrInputDocumentListFinal
                    .add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(solrDocumentForHoldingsIdentifier));
        }

    }

    private void modifySolrDocForDestInstance(String destInstanceIdentifier, List<String> itemUUIDList,
                                              List<SolrInputDocument> solrInputDocumentListFinal) throws Exception {

        SolrDocument solrDocumentForDestinationInstance = getSolrDocumentByUUID(destInstanceIdentifier);
        solrDocumentForDestinationInstance.addField("itemIdentifier", itemUUIDList);
        String holdingsIdentifier = (String) solrDocumentForDestinationInstance.getFieldValue("holdingsIdentifier");
        SolrDocument solrDocumentForHoldingsIdentifier = getSolrDocumentByUUID(holdingsIdentifier);
        solrDocumentForHoldingsIdentifier.addField("itemIdentifier", itemUUIDList);
        solrInputDocumentListFinal
                .add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(solrDocumentForDestinationInstance));
        solrInputDocumentListFinal
                .add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(solrDocumentForHoldingsIdentifier));

    }

    private void modifySolrDocForItem(List<RequestDocument> requestDocuments, List<String> sourceInstanceUUIDList,
                                      List<String> itemUUIDList, String destInstanceIdentifier,
                                      List<SolrInputDocument> solrInputDocumentListFinal) throws Exception {
        // List<SolrInputDocument> solrInputDocumentList = new ArrayList<SolrInputDocument>();
        SolrDocument solrDocumentForDestinationInstance = getSolrDocumentByUUID(destInstanceIdentifier);

        for (RequestDocument requestDocument : requestDocuments) {
            if (requestDocument.getType().equalsIgnoreCase("item") && requestDocument.getFormat()
                    .equalsIgnoreCase("oleml")) {
                String itemUUID = requestDocument.getUuid();
                SolrDocument solrDocumentForItem = getSolrDocumentByUUID(itemUUID);
                String sourceInstanceUUID = (String) solrDocumentForItem.getFieldValue("instanceIdentifier");
                sourceInstanceUUIDList.add(sourceInstanceUUID);
                solrDocumentForItem.setField("holdingsIdentifier",
                        solrDocumentForDestinationInstance.getFieldValue("holdingsIdentifier"));
                solrDocumentForItem
                        .setField("bibIdentifier", solrDocumentForDestinationInstance.getFieldValue("bibIdentifier"));
                solrDocumentForItem
                        .setField("instanceIdentifier", solrDocumentForDestinationInstance.getFieldValue("id"));
                itemUUIDList.add(itemUUID);
                solrInputDocumentListFinal
                        .add(workBibMarcDocBuilder1.buildSolrInputDocFromSolrDoc(solrDocumentForItem));
            }
        }
    }

    private void unbindFromInstanceDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocumentList) throws Exception {

        SolrQuery solrQuery = new SolrQuery();
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        solrQuery.setQuery("id:" + requestDocument.getUuid());
        QueryResponse response = server.query(solrQuery);
        List<SolrDocument> solrDocumentList = response.getResults();
        LOG.debug("response.getResults()-->" + response.getResults());
        for (SolrDocument solrDocument : solrDocumentList) {
            List<String> bibIdentifierList = new ArrayList<String>();

            //Get the BibIdentifier list and update the bibIdentifier field with the linked doc uuid's.
            Object bibIdentifier = solrDocument.getFieldValue("bibIdentifier");
            if (bibIdentifier instanceof List) {
                bibIdentifierList = (List<String>) solrDocument.getFieldValue("bibIdentifier");

            } else if (bibIdentifier instanceof String) {
                bibIdentifierList.add((String) bibIdentifier);
            }
            LOG.info("bibIdentifierList before remove-->" + bibIdentifierList);
            List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
            for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
                if (bibIdentifierList.contains(linkedRequestDocument.getUuid())) {
                    bibIdentifierList.remove(linkedRequestDocument.getUuid());
                }

            }
            LOG.info("bibIdentifierList after remove-->" + bibIdentifierList);
            solrDocument.setField("bibIdentifier", bibIdentifierList);
            solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(solrDocument));

            //Get the holdings identifier from the solrdoc and update bibIdentifier field in holding doc with the linked doc uuid's.
            Object holdingsIdentifier = solrDocument.getFieldValue("holdingsIdentifier");
            List<String> holdingIdentifierList = new ArrayList<String>();
            if (holdingsIdentifier instanceof List) {
                holdingIdentifierList = (List<String>) solrDocument.getFieldValue("holdingsIdentifier");

            } else if (holdingsIdentifier instanceof String) {
                holdingIdentifierList.add((String) holdingsIdentifier);

            }

            SolrDocument holdingSolrDocument = getSolrDocumentByUUID(holdingIdentifierList.get(0));
            holdingSolrDocument.setField("bibIdentifier", bibIdentifierList);
            solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(holdingSolrDocument));

            //Get the item identifier from the solrdoc and update bibIdentifier field in item doc with the linked doc uuid's.
            Object itemIdentifier = solrDocument.getFieldValue("itemIdentifier");
            List<String> itemIdentifierList = new ArrayList<String>();
            if (itemIdentifier instanceof List) {
                itemIdentifierList = (List<String>) solrDocument.getFieldValue("itemIdentifier");

            } else if (itemIdentifier instanceof String) {
                itemIdentifierList.add((String) itemIdentifier);
            }

            for (String itemId : itemIdentifierList) {
                SolrDocument itemSolrDocument = getSolrDocumentByUUID(itemId);
                itemSolrDocument.setField("bibIdentifier", bibIdentifierList);
                solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(itemSolrDocument));

            }


        }


    }

    private void unbindFromBibDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocumentList) throws Exception {
        List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
        for (RequestDocument linkedRequestDocument : linkedRequestDocuments) {
            SolrDocument bibSolrDocument = getSolrDocumentByUUID(linkedRequestDocument.getUuid());
            List<String> instanceIdentifierList = new ArrayList<String>();
            Object instanceIdentifier = bibSolrDocument.getFieldValue("instanceIdentifier");
            if (instanceIdentifier instanceof List) {
                instanceIdentifierList = (List<String>) bibSolrDocument.getFieldValue("instanceIdentifier");

            } else if (instanceIdentifier instanceof String) {
                instanceIdentifierList.add((String) instanceIdentifier);
            }
            LOG.info("instanceIdentifierList before remove-->" + instanceIdentifierList);
            if (instanceIdentifierList.contains(requestDocument.getUuid())) {
                instanceIdentifierList.remove(requestDocument.getUuid());
            }
            LOG.info("instanceIdentifierList after remove-->" + instanceIdentifierList);
            bibSolrDocument.setField("instanceIdentifier", instanceIdentifierList);
            solrInputDocumentList.add(new WorkBibMarcDocBuilder().buildSolrInputDocFromSolrDoc(bibSolrDocument));

        }

    }


    private SolrDocument getSolrDocumentByUUID(String identifier) throws Exception {
        SolrQuery query = new SolrQuery();
        SolrDocument solrDocument = null;
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        QueryResponse response = null;
        query.setQuery("id:" + identifier);
        response = server.query(query);
        solrDocument = response.getResults().get(0);
        return solrDocument;
    }

    private SolrDocument getSolrDocumentByUUID(String identifier, String docType) throws Exception {
        SolrQuery query = new SolrQuery();
        SolrDocument solrDocument = null;
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        QueryResponse response = null;
        query.setQuery(("id:" + identifier + " AND " + "DocType:" + docType));
        response = server.query(query);
        solrDocument = response.getResults().get(0);
        return solrDocument;
    }
}
