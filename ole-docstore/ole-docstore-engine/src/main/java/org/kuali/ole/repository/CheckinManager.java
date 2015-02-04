/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.repository;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.DocStoreConstants;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.SourceHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.SourceHoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.kuali.ole.docstore.service.DocumentIndexer;
import org.kuali.ole.docstore.service.ServiceLocator;
import org.kuali.ole.logger.DocStoreLogger;
import org.kuali.ole.pojo.OleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Iterator;

import static org.kuali.ole.docstore.process.ProcessParameters.FILE_ITEM;

public class CheckinManager {

    DocStoreLogger docStoreLogger = new DocStoreLogger(this.getClass().getName());
    private static final Logger LOG = LoggerFactory.getLogger(CheckinManager.class);

    public String updateContent(RequestDocument requestDocument) throws OleException {
        //        List<String> ingestDocs = new ArrayList<String>();
        //        List<String> uuids = new ArrayList<String>();
        //        StringBuilder sb = new StringBuilder();
        //        String uuid = null;
        String content = null;
        String latestVersion = null;
        String checkInFail = "Check in failed. ";
        content = requestDocument.getContent().getContent();
        if (requestDocument.getUuid() == null || requestDocument.getUuid().trim().length() == 0) {
            requestDocument.setUuid(requestDocument.getId());
        }
        //        uuid = requestDocument.getUuid();
        //        ingestDocs.add(sb.toString());
        //        uuids.add(uuid);
        if (DocType.INSTANCE.getCode().equalsIgnoreCase(requestDocument.getType()) && content == null
                && requestDocument.getId() != null) {
            for (RequestDocument linkedItemDocument : requestDocument.getLinkedRequestDocuments()) {
                if (DocType.ITEM.getDescription().equalsIgnoreCase(linkedItemDocument.getType())
                        && linkedItemDocument.getContent().getContent() != null) {
                    ingestNIndexItemRecForInstance(requestDocument);
                }
            }
        }
        if (!DocType.SOURCEHOLDINGS.getDescription().equalsIgnoreCase(requestDocument.getType())) {
            String result = ServiceLocator.getIndexerService().indexDocument(requestDocument);
            if (!result.startsWith("success")) {
                throw new OleException(checkInFail + result);
            }
        }
        Session session = null;
        try {
            latestVersion = updateRecordInDocStore(requestDocument);
        } catch (Exception e) {
            docStoreLogger
                    .log("Document was updated in indexer but not in docStore, trying to rollback the changes from indexer",
                            e);

            RequestDocument prevRequestDoc = null;
            session = RepositoryManager.getRepositoryManager().getSession("CheckinManager", "checkIn");
            Node nodeByUUID = getNodeByUUID(session, requestDocument.getId());
            try {
                DocumentIndexer documentIndexer = new DocumentIndexer();
                prevRequestDoc = new RequestDocument();
                prevRequestDoc = requestDocument;
                String prevContent = nodeByUUID.getNode("jcr:content").getProperty("jcr:data").getValue().getString();
                prevRequestDoc.getContent().setContent(prevContent);
                documentIndexer.indexDocument(prevRequestDoc);
            } catch (Exception ex) {
                String failMsg = checkInFail + "Unable to Roll back the changes in indexer.  Record UUID "
                        + requestDocument.getId() + "got updated in indexer, but not in docstore ";
                docStoreLogger.log(failMsg, ex);
                throw new OleException(failMsg, ex);

            }
            docStoreLogger.log(checkInFail, e);
            throw new OleException(checkInFail, e);
        } finally {
            RepositoryManager.getRepositoryManager().logout(session);
        }


        return latestVersion;
    }

    private void ingestNIndexItemRecForInstance(RequestDocument reqDoc) throws OleException {
        Session session = null;
        try {
            session = RepositoryManager.getRepositoryManager()
                    .getSession("CheckinManager", "ingestNIndexItemRecForInstance");
            Node nodeByUUID = getNodeByUUID(session, reqDoc.getId());
            Node holdingsNode = nodeByUUID.getNode(ProcessParameters.NODE_HOLDINGS);
            NodeHandler nodeHandler = new NodeHandler();

            for (RequestDocument linkedItemDocument : reqDoc.getLinkedRequestDocuments()) {
                ItemOlemlRecordProcessor recordProcessor = new ItemOlemlRecordProcessor();
                Item item = recordProcessor.fromXML(linkedItemDocument.getContent().getContent());
                linkedItemDocument.getContent().setContentObject(item);
                String uuid = (nodeHandler.initFileNode(linkedItemDocument, FILE_ITEM, holdingsNode, session))
                        .getIdentifier();
                linkedItemDocument.setId(uuid);
            }
            session.save();
        } catch (Exception e) {
            docStoreLogger.log(e.getMessage());
            throw new OleException(e.getMessage(), e);
        } finally {
            RepositoryManager.getRepositoryManager().logout(session);
        }
    }


    private String updateRecordInDocStore(RequestDocument reqDoc) throws OleException, RepositoryException {
        Session session = RepositoryManager.getRepositoryManager()
                .getSession("CheckinManager", "updateRecordInDocStore");
        String charset = "UTF-8";
        byte[] documentBytes = null;
        String currentVersion = null;

        try {
            if (reqDoc.getDocumentName() != null && reqDoc.getDocumentName().trim().length() != 0) {
                documentBytes = FileUtils.readFileToByteArray(new File(reqDoc.getDocumentName()));
            } else if (reqDoc.getContent().getContent() != null) {
                setIdentifierValueInContent(reqDoc);
                documentBytes = reqDoc.getContent().getContent().getBytes(charset);
            }
        } catch (Exception e) {
            getDocStoreLogger().log("Failed to convert input string to byte[] with charset " + charset, e);
            throw new OleException(e.getMessage());
        }
        RequestDocument linkReqInfo = new RequestDocument();
        if (reqDoc.getLinkedRequestDocuments() != null && reqDoc.getLinkedRequestDocuments().size() > 0) {
            for (Iterator<RequestDocument> linkIterator = reqDoc.getLinkedRequestDocuments().iterator(); linkIterator
                    .hasNext(); ) {
                linkReqInfo = linkIterator.next();
            }
        }
        Node nodeByUUID = session.getNodeByIdentifier(reqDoc.getUuid());
        try {
            Binary binary = null;
            if (documentBytes != null) {
                binary = session.getValueFactory().createBinary(new ByteArrayInputStream(documentBytes));
                nodeByUUID.getNode("jcr:content").setProperty("jcr:data", binary);
            }
            if (linkReqInfo != null && linkReqInfo.getId() != null && linkReqInfo.getType().equalsIgnoreCase(
                    DocType.INSTANCE.getCode())) {
                nodeByUUID.setProperty("instanceIdentifier", linkReqInfo.getId());
            }
            Calendar lastModified = Calendar.getInstance();
            lastModified.setTimeInMillis(lastModified.getTimeInMillis());
            if (!reqDoc.getType().equalsIgnoreCase(DocType.INSTANCE.getCode())) {
                nodeByUUID.getNode("jcr:content").setProperty("jcr:lastModified", lastModified);
            }
            session.save();
            if (DocStoreConstants.isVersioningEnabled || DocType.LICENSE.isEqualTo(reqDoc.getType())) {
                VersionManager versionManager = getVersionManager(session);
                versionManager.checkpoint(nodeByUUID.getPath());
                VersionHistory versionHistory = versionManager.getVersionHistory(nodeByUUID.getPath());
                VersionIterator allVersions = versionHistory.getAllVersions();
                while (allVersions.hasNext()) {
                    currentVersion = allVersions.nextVersion().getName();
                }
                getDocStoreLogger()
                        .log("Version updated for UUID:" + reqDoc.getUuid() + "  ====  version:" + currentVersion);
            }
        } catch (Exception e) {
            docStoreLogger.log(e.getMessage());
            throw new OleException(e.getMessage(), e);
        } finally {
            RepositoryManager.getRepositoryManager().logout(session);
        }
        return currentVersion;
    }

    private void setIdentifierValueInContent(RequestDocument reqDoc) {
        if (reqDoc.getType().equalsIgnoreCase(DocType.ITEM.getDescription())) {
            ItemOlemlRecordProcessor recordProcessor = new ItemOlemlRecordProcessor();
            Item item = recordProcessor.fromXML(reqDoc.getContent().getContent());
            item.setItemIdentifier(reqDoc.getId());
            reqDoc.getContent().setContent(recordProcessor.toXML(item));
        }
        if (reqDoc.getType().equalsIgnoreCase(DocType.HOLDINGS.getDescription())) {
            HoldingOlemlRecordProcessor recordProcessor = new HoldingOlemlRecordProcessor();
            OleHoldings holdings = recordProcessor.fromXML(reqDoc.getContent().getContent());
            holdings.setHoldingsIdentifier(reqDoc.getId());
            reqDoc.getContent().setContent(recordProcessor.toXML(holdings));
        }
        if (reqDoc.getType().equalsIgnoreCase(DocType.SOURCEHOLDINGS.getDescription())) {
            SourceHoldingOlemlRecordProcessor recordProcessor = new SourceHoldingOlemlRecordProcessor();
            SourceHoldings sourceHoldings = recordProcessor.fromXML(reqDoc.getContent().getContent());
            sourceHoldings.setHoldingsIdentifier(reqDoc.getId());
            reqDoc.getContent().setContent(recordProcessor.toXML(sourceHoldings));
        }
    }

    private Node getNodeByUUID(Session newSession, String uuid) throws OleException {
        return new NodeHandler().getNodeByUUID(newSession, uuid);
    }

    public DocStoreLogger getDocStoreLogger() {
        return docStoreLogger;
    }

    public VersionManager getVersionManager(Session session) throws OleException, RepositoryException {
        return session.getWorkspace().getVersionManager();
    }
}
