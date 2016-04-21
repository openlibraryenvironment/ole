package org.kuali.ole.docstore.engine.service.index.solr;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.DocStoreConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.BibTrees;
import org.kuali.ole.docstore.common.document.content.instance.Location;
import org.kuali.ole.docstore.common.document.content.instance.LocationLevel;
import org.kuali.ole.docstore.common.exception.DocstoreIndexException;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreSolrIndexService implements DocumentIndexer, DocstoreConstants {

    private static final Logger LOG = LoggerFactory.getLogger(DocstoreSolrIndexService.class);
    public static final String ID_FIELD_PREFIX = "id_disc_";
    public static final String MAX_WARM_SEARCH = "Error opening new searcher. exceeded limit of maxWarmingSearchers";
    public static final int MAX_WARM_SEARCH_COUNT = 10;
    public static final int TIME_FOR_WARMING_SEARCHERS = 2000;
    private static long lastCommitTime = 0;
    private static int commitCounter = 1;
    private static long timeSinceLastCommit = 0;

    @Override
    public void create(Object object) {
        String result = null;
        List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();
        try {
            buildSolrInputDocument(object, solrInputDocuments);
            assignUUIDs(solrInputDocuments, null);
        } catch (Exception e1) {
            LOG.info("Exception :", e1);
            e1.printStackTrace();
            throw new DocstoreIndexException(e1.getMessage());
        }
        if ((null == solrInputDocuments) || (solrInputDocuments.isEmpty())) {
            throw new DocstoreIndexException("No valid documents found in input.");
        }
        indexSolrDocuments(solrInputDocuments, true);
    }


    @Override
    public void update(Object object) {
        List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();
        updateRecordInSolr(object, solrInputDocuments);
        if(solrInputDocuments.size() > 0) {
            indexSolrDocuments(solrInputDocuments, true);
        }
    }

    protected void updateRecordInSolr(Object object, List<SolrInputDocument> solrInputDocuments) {

    }

    @Override
    public void delete(String id) {
        try {
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            deleteRecordInSolr(server, id);
            server.commit();
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException();
        } catch (IOException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException();
        }
    }

    @Override
    public void deleteBatch(String id) {
        try {
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            deleteRecordInSolr(server, id);
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException();
        } catch (IOException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException();
        }
    }
    /**
     *  Deleting  documents and committing to solr at once
     * @param ids
     */

    private void delete(List<String> ids, SolrServer solrServer) throws IOException, SolrServerException {
        for (String id : ids) {
            deleteRecordInSolr(solrServer, id);
        }
    }

    /**
     * process documents and updating parent documents
     * @param id
     * @param solrInputDocuments
     */
    public void processDelete(String id, List<SolrInputDocument> solrInputDocuments) {
        try {
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            processRecord(server, id,solrInputDocuments);
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException();
        } catch (IOException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException();
        }
    }

    @Override
    public void transfer(List<String> sourceIds, String destinationId) {


        List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();

        modifySolrDocForSource(sourceIds, destinationId, solrInputDocuments);

        modifySolrDocForDestination(destinationId, sourceIds, solrInputDocuments);
        /*try {
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            UpdateResponse updateResponse = server.add(solrInputDocuments);
            server.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }*/
        indexSolrDocuments(solrInputDocuments, true);

    }

    @Override
    public void bind(String holdingsId, List<String> bibIds) throws SolrServerException, IOException {

    }

    @Override
    public void createTree(Object object) {


    }

    @Override
    public void createTrees(Object object) {

    }

    @Override
    public void processBibTrees(BibTrees bibTrees) {

    }


    protected void modifySolrDocForDestination(String destinationId, List<String> sourceIds, List<SolrInputDocument> solrInputDocumentListFinal) {
        //To change body of created methods use File | Settings | File Templates.
    }

    protected void modifySolrDocForSource(List<String> sourceIds, String destinationId, List<SolrInputDocument> solrInputDocumentListFinal) {
        //To change body of created methods use File | Settings | File Templates.
    }

    protected void deleteRecordInSolr(SolrServer solrServer, String id) throws IOException, SolrServerException {
        String query = "id:" + id;
        UpdateResponse updateResponse = solrServer.deleteByQuery(query);
    }

    /**
     *  Taking incoming documents and updating documents respectively
     * @param solrServer
     * @param id
     * @param solrInputDocuments
     * @throws IOException
     * @throws SolrServerException
     */
    protected void processRecord(SolrServer solrServer, String id , List<SolrInputDocument> solrInputDocuments) throws IOException, SolrServerException {
        String query = "id:" + id;
        UpdateResponse updateResponse = solrServer.deleteByQuery(query);
    }

    protected void buildSolrInputDocument(Object object, List<SolrInputDocument> solrInputDocuments) {

    }

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
                solrInputDocument.addField(ID, uuid);
                solrInputDocument.addField(UNIQUE_ID, uuid);
            } else {
                if (null != uuid) {
                    // Use the supplied UUID.
                    solrInputDocument.setField(ID, uuid);
                    solrInputDocument.setField(UNIQUE_ID, uuid);
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
                    SolrInputField uniqueIdField = solrInputDocument.getField(UNIQUE_ID);
                    if (null == uniqueIdField) {
                        solrInputDocument.addField(UNIQUE_ID, uuid);
                    } else {
                        if (uniqueIdField.getValue() == null) {
                            solrInputDocument.setField(UNIQUE_ID, uuid);
                        }
                    }
                }
            }
        }
    }

    protected void indexSolrDocuments(List<SolrInputDocument> solrDocs, boolean isCommit) {
        synchronized (this.getClass()) {
            SolrServer solr = null;
            try {
                solr = SolrServerManager.getInstance().getSolrServer();
                UpdateResponse response = solr.add(solrDocs);
                if (isCommit) {
                    try {
                        commitRecordsToSolr(solr);
                    } catch (HttpSolrServer.RemoteSolrException e) {
                        lastCommitTime = System.currentTimeMillis();
                        if (e.getMessage().startsWith(MAX_WARM_SEARCH)) {
                            LOG.warn(e.getMessage());
                        }
                        else {
                            throw e;
                        }
                    }
                }
            } catch (Exception e) {
                LOG.info("Exception :", e);
                rollback(solr);
                throw new DocstoreIndexException(e);
            }
        }
    }


    protected void indexAndDelete(List<SolrInputDocument> solrDocs, List<String> idsToDelete, boolean isCommit) {
        synchronized (this.getClass()) {
            SolrServer solr = null;
            try {
                solr = SolrServerManager.getInstance().getSolrServer();
                if (CollectionUtils.isNotEmpty(solrDocs)) {
                    UpdateResponse response = solr.add(solrDocs);
                }
                // deleting document which contains operation delete
                delete(idsToDelete, solr);
                if (isCommit) {
                    try {
                        commitRecordsToSolr(solr);
                    } catch (HttpSolrServer.RemoteSolrException e) {
                        lastCommitTime = System.currentTimeMillis();
                        if (e.getMessage().startsWith(MAX_WARM_SEARCH)) {
                            LOG.warn(e.getMessage());
                        }
                        else {
                            throw e;
                        }
                    }
                }
            } catch (Exception e) {
                LOG.info("Exception :", e);
                rollback(solr);
                throw new DocstoreIndexException(e);
            }
        }
    }


    public void commitRecordsToSolr(SolrServer solr) throws SolrServerException, IOException {

        if(commitCounter < MAX_WARM_SEARCH_COUNT) {
            solr.commit(false, false, false);
            //LOG.info("Time taken to commit with waitSearch = false  commitCounter < MAX_WARM_SEARCH_COUNT");
            commitCounter++;
        }
        else {

            timeSinceLastCommit = System.currentTimeMillis() - lastCommitTime;
            if(timeSinceLastCommit > TIME_FOR_WARMING_SEARCHERS) {
                solr.commit(false, false, false);
                commitCounter++;
//                commitCounter = 1;
                //LOG.info("Time taken to commit with waitSearch = false  timeSinceLastCommit > TIME_FOR_WARMING_SEARCHERS");

            }
            else {
                long startTime = System.currentTimeMillis();
                solr.commit(false, true, false);
                long endTime = System.currentTimeMillis();
                //LOG.info("Time taken to commit with waitSearch = true and time taken is " + (endTime - startTime));
                commitCounter = 1;
            }
        }
        lastCommitTime = System.currentTimeMillis();

    }

    protected void rollback(SolrServer solrServer) {
        try {
            solrServer.rollback();
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException(e.getMessage());
        } catch (IOException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException(e.getMessage());
        }
    }

    public List<SolrDocument> getSolrDocumentBySolrId(String uniqueId) {
        QueryResponse response = null;
        String result = null;
        try {
            String args = "(" + UNIQUE_ID + ":" + uniqueId + ")";
            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
            SolrQuery query = new SolrQuery();
            query.setQuery(args);
            response = solr.query(query);
        } catch (Exception e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException(e.getMessage());
        }
        return response.getResults();
    }

    public SolrInputDocument buildSolrInputDocFromSolrDoc(SolrDocument solrDocument) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        if (solrDocument != null) {
            Map<String, Collection<Object>> solrDocMap = solrDocument.getFieldValuesMap();
            if (solrDocMap != null && solrDocMap.size() > 0) {
                Set<String> resultField = solrDocMap.keySet();
                for (Iterator<String> iterator1 = resultField.iterator(); iterator1.hasNext(); ) {
                    String key = iterator1.next();
                    if (!key.equalsIgnoreCase(OleNGConstants._VERSION_)) {
                        Object value = solrDocMap.get(key);
                        solrInputDocument.addField(key, value);
                    }
                }
            }
        }
        return solrInputDocument;
    }

    public void buildSolrInputDocFromSolrDoc(SolrDocument solrDocument, SolrInputDocument solrInputDocument) {
        if (solrDocument != null) {
            Map<String, Collection<Object>> solrDocMap = solrDocument.getFieldValuesMap();
            if (solrDocMap != null && solrDocMap.size() > 0) {
                Set<String> resultField = solrDocMap.keySet();
                for (Iterator<String> iterator1 = resultField.iterator(); iterator1.hasNext(); ) {
                    String key = iterator1.next();
                    if (!key.equalsIgnoreCase(OleNGConstants._VERSION_)) {
                        Object value = solrDocMap.get(key);
                        solrInputDocument.addField(key, value);
                    }
                }
            }
        }
    }

    protected SolrDocument getSolrDocumentByUUID(String identifier) {
        SolrQuery query = new SolrQuery();
        SolrDocument solrDocument = null;
        SolrServer server = null;
        try {
            server = SolrServerManager.getInstance().getSolrServer();
            QueryResponse response = null;
            query.setQuery("id:" + identifier);
            response = server.query(query);
            solrDocument = response.getResults().get(0);
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException(e.getMessage());
        }
        return solrDocument;
    }

    protected SolrDocumentList getSolrDocumentByUUIDs(List<String> uuids) {
        String operand = "OR";
        SolrDocumentList solrDocumentList = null;
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (String uuid : uuids) {
            sb.append("(");
            sb.append(uuid);
            sb.append(")");
            sb.append(operand);
        }
        String queryString = sb.substring(0, sb.length() - operand.length()) + ")";
        SolrQuery query = new SolrQuery();
        SolrDocument solrDocument = null;
        try {
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            QueryResponse response = null;
            query.setQuery("id:" + queryString);
            response = server.query(query);
            solrDocumentList = response.getResults();
        } catch (SolrServerException e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException();
        }
        return solrDocumentList;
    }

    @Override
    public void bindAnalytics(String holdingsId, List<String> bibIds, String createOrBreak) throws SolrServerException, IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    /**
     * This method is used to index the bib info to holdings or items
     *
     * @param solrInputDocument - holdings or items solr input documents
     * @param sourceDocument -
     */
    protected void addBibInfoForHoldingsOrItems(SolrInputDocument solrInputDocument, SolrDocument sourceDocument) {

        solrInputDocument.addField(TITLE_SEARCH, sourceDocument.getFieldValues(TITLE_SEARCH));
        solrInputDocument.addField(TITLE_SORT, sourceDocument.getFieldValues(TITLE_SORT));
        solrInputDocument.addField(AUTHOR_SEARCH, sourceDocument.getFieldValues(AUTHOR_SEARCH));
        solrInputDocument.addField(PUBLICATIONPLACE_DISPLAY, sourceDocument.getFieldValues(PUBLICATIONPLACE_DISPLAY));
        solrInputDocument.addField(PUBLISHER_SEARCH, sourceDocument.getFieldValues(PUBLISHER_SEARCH));
        solrInputDocument.setField(PUBLISHER_SORT, sourceDocument.getFieldValue(PUBLISHER_SORT));
        solrInputDocument.addField(ISSN_SEARCH, sourceDocument.getFieldValues(ISSN_SEARCH));
        solrInputDocument.addField(ISBN_SEARCH, sourceDocument.getFieldValues(ISBN_SEARCH));
        solrInputDocument.addField(FORMAT_SEARCH, sourceDocument.getFieldValues(FORMAT_SEARCH));
        solrInputDocument.addField(LANGUAGE_SEARCH, sourceDocument.getFieldValues(LANGUAGE_SEARCH));
        solrInputDocument.addField(PUBLICATIONDATE_SEARCH, sourceDocument.getFieldValues(PUBLICATIONDATE_SEARCH));
        solrInputDocument.addField(MDF_035A, sourceDocument.getFieldValues(MDF_035A));

        solrInputDocument.addField(TITLE_DISPLAY, sourceDocument.getFieldValues(TITLE_DISPLAY));
        solrInputDocument.addField(AUTHOR_DISPLAY, sourceDocument.getFieldValues(AUTHOR_DISPLAY));
        solrInputDocument.addField(PUBLISHER_DISPLAY, sourceDocument.getFieldValues(PUBLISHER_DISPLAY));
        solrInputDocument.addField(ISSN_DISPLAY, sourceDocument.getFieldValues(ISSN_DISPLAY));
        solrInputDocument.addField(ISBN_DISPLAY, sourceDocument.getFieldValues(ISBN_DISPLAY));
        solrInputDocument.addField(FORMAT_DISPLAY, sourceDocument.getFieldValues(FORMAT_DISPLAY));
        solrInputDocument.addField(LANGUAGE_DISPLAY, sourceDocument.getFieldValues(LANGUAGE_DISPLAY));
        solrInputDocument.addField(PUBLICATIONDATE_DISPLAY, sourceDocument.getFieldValues(PUBLICATIONDATE_DISPLAY));

    }

    /**
     * This method is used to index the bib info to holdings or items
     *
     * @param solrInputDocument - holdings or items solr input documents
     * @param sourceDocument -
     */
    protected void setBibInfoForHoldingsOrItems(SolrInputDocument solrInputDocument, SolrDocument sourceDocument) {
        if(sourceDocument!=null){
            solrInputDocument.setField(TITLE_SEARCH, sourceDocument.getFieldValues(TITLE_SEARCH));
            solrInputDocument.setField(TITLE_SORT, sourceDocument.getFieldValues(TITLE_SORT));
            solrInputDocument.setField(AUTHOR_SEARCH, sourceDocument.getFieldValues(AUTHOR_SEARCH));
            solrInputDocument.setField(PUBLICATIONPLACE_DISPLAY, sourceDocument.getFieldValues(PUBLICATIONPLACE_DISPLAY));
            solrInputDocument.setField(PUBLISHER_SEARCH, sourceDocument.getFieldValues(PUBLISHER_SEARCH));
            solrInputDocument.setField(PUBLISHER_SORT, sourceDocument.getFieldValue(PUBLISHER_SORT));
            solrInputDocument.setField(ISSN_SEARCH, sourceDocument.getFieldValues(ISSN_SEARCH));
            solrInputDocument.setField(ISBN_SEARCH, sourceDocument.getFieldValues(ISBN_SEARCH));
            solrInputDocument.setField(FORMAT_SEARCH, sourceDocument.getFieldValues(FORMAT_SEARCH));
            solrInputDocument.setField(LANGUAGE_SEARCH, sourceDocument.getFieldValues(LANGUAGE_SEARCH));
            solrInputDocument.setField(PUBLICATIONDATE_SEARCH, sourceDocument.getFieldValues(PUBLICATIONDATE_SEARCH));
            solrInputDocument.setField(MDF_035A, sourceDocument.getFieldValues(MDF_035A));

            solrInputDocument.setField(TITLE_DISPLAY, sourceDocument.getFieldValues(TITLE_DISPLAY));
            solrInputDocument.setField(AUTHOR_DISPLAY, sourceDocument.getFieldValues(AUTHOR_DISPLAY));
            solrInputDocument.setField(PUBLISHER_DISPLAY, sourceDocument.getFieldValues(PUBLISHER_DISPLAY));
            solrInputDocument.setField(ISSN_DISPLAY, sourceDocument.getFieldValues(ISSN_DISPLAY));
            solrInputDocument.setField(ISBN_DISPLAY, sourceDocument.getFieldValues(ISBN_DISPLAY));
            solrInputDocument.setField(FORMAT_DISPLAY, sourceDocument.getFieldValues(FORMAT_DISPLAY));
            solrInputDocument.setField(LANGUAGE_DISPLAY, sourceDocument.getFieldValues(LANGUAGE_DISPLAY));
            solrInputDocument.setField(PUBLICATIONDATE_DISPLAY, sourceDocument.getFieldValues(PUBLICATIONDATE_DISPLAY));
        }
    }

    /**
     *
     * @param solrInputDocument
     * @param sourceInputDocument
     */

    protected void addBibInfoForHoldingsOrItems(SolrInputDocument solrInputDocument, SolrInputDocument sourceInputDocument) {

        solrInputDocument.addField(TITLE_SEARCH, sourceInputDocument.getFieldValues(TITLE_SEARCH));
        solrInputDocument.addField(TITLE_SORT, sourceInputDocument.getFieldValues(TITLE_SORT));
        solrInputDocument.addField(AUTHOR_SEARCH, sourceInputDocument.getFieldValues(AUTHOR_SEARCH));
        solrInputDocument.addField(PUBLISHER_SEARCH, sourceInputDocument.getFieldValues(PUBLISHER_SEARCH));
        solrInputDocument.addField(PUBLICATIONPLACE_DISPLAY, sourceInputDocument.getFieldValues(PUBLICATIONPLACE_DISPLAY));
        solrInputDocument.setField(PUBLISHER_SORT, sourceInputDocument.getFieldValue(PUBLISHER_SORT));
        solrInputDocument.addField(ISSN_SEARCH, sourceInputDocument.getFieldValues(ISSN_SEARCH));
        solrInputDocument.addField(ISBN_SEARCH, sourceInputDocument.getFieldValues(ISBN_SEARCH));
        solrInputDocument.addField(FORMAT_SEARCH, sourceInputDocument.getFieldValues(FORMAT_SEARCH));
        solrInputDocument.addField(LANGUAGE_SEARCH, sourceInputDocument.getFieldValues(LANGUAGE_SEARCH));
        solrInputDocument.addField(PUBLICATIONDATE_SEARCH, sourceInputDocument.getFieldValues(PUBLICATIONDATE_SEARCH));
        solrInputDocument.addField(MDF_035A, sourceInputDocument.getFieldValues(MDF_035A));

        solrInputDocument.addField(TITLE_DISPLAY, sourceInputDocument.getFieldValues(TITLE_DISPLAY));
        solrInputDocument.addField(AUTHOR_DISPLAY, sourceInputDocument.getFieldValues(AUTHOR_DISPLAY));
        solrInputDocument.addField(PUBLISHER_DISPLAY, sourceInputDocument.getFieldValues(PUBLISHER_DISPLAY));
        solrInputDocument.addField(ISSN_DISPLAY, sourceInputDocument.getFieldValues(ISSN_DISPLAY));
        solrInputDocument.addField(ISBN_DISPLAY, sourceInputDocument.getFieldValues(ISBN_DISPLAY));
        solrInputDocument.addField(FORMAT_DISPLAY, sourceInputDocument.getFieldValues(FORMAT_DISPLAY));
        solrInputDocument.addField(LANGUAGE_DISPLAY, sourceInputDocument.getFieldValues(LANGUAGE_DISPLAY));
        solrInputDocument.addField(PUBLICATIONDATE_DISPLAY, sourceInputDocument.getFieldValues(PUBLICATIONDATE_DISPLAY));

    }


    protected void addHoldingsInfoToItem(SolrInputDocument solrInputDocument, SolrInputDocument sourceInputDocument) {
        solrInputDocument.addField(HOLDINGS_LOCATION_SEARCH, sourceInputDocument.getFieldValue(LOCATION_LEVEL_SEARCH));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_SEARCH, sourceInputDocument.getFieldValue(CALL_NUMBER_SEARCH));
        solrInputDocument.addField(HOLDINGS_LOCATION_DISPLAY, sourceInputDocument.getFieldValue(LOCATION_LEVEL_DISPLAY));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_DISPLAY, sourceInputDocument.getFieldValue(CALL_NUMBER_DISPLAY));
        solrInputDocument.addField(HOLDINGS_COPYNUMBER_DISPLAY, sourceInputDocument.getFieldValue(COPY_NUMBER_DISPLAY));
        solrInputDocument.addField(HOLDINGS_COPYNUMBER_SEARCH, sourceInputDocument.getFieldValue(COPY_NUMBER_SEARCH));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_PREFIX_SEARCH, sourceInputDocument.getFieldValue(CALL_NUMBER_PREFIX_SEARCH));
        solrInputDocument.addField(HOLDINGS_CALLNUMBER_PREFIX_DISPLAY, sourceInputDocument.getFieldValue(CALL_NUMBER_PREFIX_DISPLAY));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_CODE_SEARCH, sourceInputDocument.getFieldValue(SHELVING_SCHEME_CODE_SEARCH));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_CODE_DISPLAY, sourceInputDocument.getFieldValue(SHELVING_SCHEME_CODE_DISPLAY));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_VALUE_SEARCH, sourceInputDocument.getFieldValue(SHELVING_SCHEME_VALUE_SEARCH));
        solrInputDocument.addField(HOLDINGS_SHELVING_SCHEME_VALUE_DISPLAY, sourceInputDocument.getFieldValue(SHELVING_SCHEME_VALUE_DISPLAY));
    }


    protected void removeFieldFromSolrInputDocument(SolrInputDocument holdingsSolrInputDoc) {
        holdingsSolrInputDoc.removeField(TITLE_SORT);
        holdingsSolrInputDoc.removeField(TITLE_SEARCH);
        holdingsSolrInputDoc.removeField(AUTHOR_SEARCH);
        holdingsSolrInputDoc.removeField(PUBLISHER_SEARCH);
        holdingsSolrInputDoc.removeField(ISSN_SEARCH);
        holdingsSolrInputDoc.removeField(ISBN_SEARCH);
        holdingsSolrInputDoc.removeField(FORMAT_SEARCH);
        holdingsSolrInputDoc.removeField(LANGUAGE_SEARCH);
        holdingsSolrInputDoc.removeField(PUBLICATIONDATE_SEARCH);

        holdingsSolrInputDoc.removeField(TITLE_DISPLAY);
        holdingsSolrInputDoc.removeField(AUTHOR_DISPLAY);
        holdingsSolrInputDoc.removeField(PUBLISHER_DISPLAY);
        holdingsSolrInputDoc.removeField(ISSN_DISPLAY);
        holdingsSolrInputDoc.removeField(ISBN_DISPLAY);
        holdingsSolrInputDoc.removeField(FORMAT_DISPLAY);
        holdingsSolrInputDoc.removeField(LANGUAGE_DISPLAY);
        holdingsSolrInputDoc.removeField(PUBLICATIONDATE_DISPLAY);

        holdingsSolrInputDoc.removeField(HOLDINGS_CALLNUMBER_DISPLAY);
        holdingsSolrInputDoc.removeField(HOLDINGS_CALLNUMBER_SEARCH);
        holdingsSolrInputDoc.removeField(HOLDINGS_LOCATION_DISPLAY);
        holdingsSolrInputDoc.removeField(HOLDINGS_LOCATION_SEARCH);

    }


    protected String buildSortableCallNumber(String callNumber, String codeValue) {
        String shelvingOrder = "";
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                shelvingOrder = callNumberObj.getShelfKey();
            }
        }
        return shelvingOrder;
    }


    protected void appendData(StringBuffer stringBuffer, String data) {
        if(StringUtils.isNotEmpty(data)) {
            stringBuffer.append(data);
            stringBuffer.append(" ");
        }
    }

    protected void buildLocationNameAndLocationLevel(Location location, StringBuffer locationName, StringBuffer locationLevel) {
        if(location != null && location.getLocationLevel() != null) {
            locationName = locationName.append(location.getLocationLevel().getName());
            locationLevel = locationLevel.append(location.getLocationLevel().getLevel());

            if (location.getLocationLevel().getLocationLevel() != null) {
                locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getName());
                locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLevel());

                if (location.getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                    locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getName());
                    locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                    if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                        locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                        locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                        if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                            locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                            locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());
                        }
                    }
                }
            }
        }
    }

    protected void buildLocationName(Location location, SolrInputDocument solrInputDocument ,StringBuffer loactionLevelStr) {
        if (location != null) {
            buildLocation(location.getLocationLevel(), solrInputDocument,loactionLevelStr);
        }
    }

    private void buildLocation(LocationLevel locationLevel, SolrInputDocument solrInputDocument,StringBuffer loactionLevelStr) {
        if (locationLevel != null) {
            String locationName = locationLevel.getName();
            if (LOCATION_LEVEL_INSTITUTION.equalsIgnoreCase(locationLevel.getLevel())) {
                solrInputDocument.addField(LEVEL1LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL1LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            } else if (LOCATION_LEVEL_CAMPUS.equalsIgnoreCase(locationLevel.getLevel())) {
                solrInputDocument.addField(LEVEL2LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL2LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            } else if (LOCATION_LEVEL_LIBRARY.equalsIgnoreCase(locationLevel.getLevel())) {
                solrInputDocument.addField(LEVEL3LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL3LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            } else if (LOCATION_LEVEL_COLLECTION.equalsIgnoreCase(locationLevel.getLevel())) {
                solrInputDocument.addField(LEVEL4LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL4LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            } else if (LOCATION_LEVEL_SHELVING.equalsIgnoreCase(locationLevel.getLevel()) || LOCATION_LEVEL_SHELVING_1.equalsIgnoreCase(locationLevel.getLevel())) {
                solrInputDocument.addField(LEVEL5LOCATION_DISPLAY, locationName);
                solrInputDocument.addField(LEVEL5LOCATION_SEARCH, locationName);
                appendData(loactionLevelStr,locationName.replace("-",""));
            }
            buildLocation(locationLevel.getLocationLevel(), solrInputDocument,loactionLevelStr);
        }
    }


    protected void addItemInfoToBib(SolrInputDocument solrInputDocument, SolrInputDocument solrBibDocument) {
        solrBibDocument.addField(ITEM_BARCODE_SEARCH, solrInputDocument.getFieldValue(ITEM_BARCODE_SEARCH));
        solrBibDocument.addField(ITEM_IDENTIFIER, solrInputDocument.getFieldValue(ITEM_IDENTIFIER));
    }

    protected void addHoldingsInfoToBib(SolrInputDocument solrInputDocument, SolrInputDocument solrBibInputDocument) {
        solrBibInputDocument.addField(URI_SEARCH, solrInputDocument.getFieldValue(URI_SEARCH));
    }

    protected void addHoldingsInfoToBib(SolrInputDocument solrInputDocument, SolrDocument solrBibDocument)  {
        solrBibDocument.addField(URI_SEARCH,solrInputDocument.getFieldValue(URI_SEARCH));
    }

    @Override
    public void unbindOne(List<String> holdingsIds, String bibId) throws SolrServerException, IOException {

    }

    @Override
    public void unbindAll(List<String> holdingsIds, String bibId) throws SolrServerException, IOException {

    }
}