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

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.DocStoreConstants;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.pojo.OleException;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertNotNull;
@Ignore
@Deprecated
public class CheckinManager_UT
        extends BaseTestCase {

    private static final Logger LOG = LoggerFactory
            .getLogger(CheckinManager_UT.class);
    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator
            .getIngestNIndexHandlerService();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateRecord() throws Exception {
        Request request = getRequestObject();
        Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        assertNotNull(response);
        List<RequestDocument> docStoreDocuments = request.getRequestDocuments();
        for (Iterator<RequestDocument> iterator = docStoreDocuments.iterator(); iterator.hasNext(); ) {
            RequestDocument docStoreDocument = iterator.next();
            if (docStoreDocument.getCategory().equals(DocCategory.WORK.getCode()) && docStoreDocument.getType()
                    .equals(DocType.BIB
                            .getDescription())) {
                CheckinManager checkinManager = new CheckinManager();
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionForRecord = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionForRecord);
                }
                docStoreDocument.setId(docStoreDocument.getUuid());
                checkinManager.updateContent(docStoreDocument);
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionAfterUpdate = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionAfterUpdate);
                }
            }
        }
    }

    private Request getRequestObject() throws URISyntaxException, IOException {
        URL resource = getClass().getResource("request.xml");
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        Request request = requestHandler.toObject(readFile(file));
        return request;
    }

    private String getCurrentVersionForRecord(String uuid) {
        String version = null;
        Session session = null;
        try {
            session = RepositoryManager.getRepositoryManager().getSession();
            VersionManager versionManager = session.getWorkspace().getVersionManager();
            Node nodeByUUID = new NodeHandler().getNodeByUUID(session, uuid);
            VersionHistory versionHistory = versionManager.getVersionHistory(nodeByUUID.getPath());
            VersionIterator allVersions = versionHistory.getAllVersions();
            while (allVersions.hasNext()) {
                version = allVersions.nextVersion().toString();
                LOG.info(version);
            }
        } catch (RepositoryException e) {
            LOG.info(e.getMessage());
        } catch (OleException e) {
            LOG.info(e.getMessage());
        } finally {
            try {
                RepositoryManager.getRepositoryManager().logout(session);
            } catch (OleException e) {
                LOG.info(e.getMessage());
            }
        }
        return version;
    }

    @Test
    public void testUpdateItemRecord() throws Exception {
        Request request = getRequestObject();
        Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        String itemUUID = response.getDocuments().get(0).getLinkedInstanceDocuments().get(2).getUuid();
        URL resource = getClass().getResource("update-item-request.xml");
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        request = requestHandler.toObject(readFile(file));
        List<RequestDocument> docStoreDocuments = request.getRequestDocuments();
        for (Iterator<RequestDocument> iterator = docStoreDocuments.iterator(); iterator.hasNext(); ) {
            RequestDocument docStoreDocument = iterator.next();
            docStoreDocument.setId(itemUUID);
            if (docStoreDocument.getCategory().equals(DocCategory.WORK.getCode()) && docStoreDocument.getType()
                    .equals(DocType.ITEM
                            .getDescription())) {
                CheckinManager checkinManager = new CheckinManager();
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionForRecord = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionForRecord);
                }
                docStoreDocument.setUuid(docStoreDocument.getId());
                checkinManager.updateContent(docStoreDocument);
                CheckoutManager checkOut = new CheckoutManager();
                String checkedOutContent = checkOut.checkOut(docStoreDocument.getId(), "mockUser", "checkout");
                Assert.assertNotNull(checkedOutContent);
                Assert.assertEquals(checkedOutContent, docStoreDocument.getContent().getContent());
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionAfterUpdate = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionAfterUpdate);
                }
            }
        }
    }

    @Test
    public void testUpdateSourceHoldingRecord() throws Exception {
        Request request = getRequestObject();
        Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        String srHolUUID = response.getDocuments().get(0).getLinkedInstanceDocuments().get(1).getUuid();
        URL resource = getClass().getResource("update-sourceHolding-request.xml");
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        request = requestHandler.toObject(readFile(file));
        List<RequestDocument> docStoreDocuments = request.getRequestDocuments();
        for (Iterator<RequestDocument> iterator = docStoreDocuments.iterator(); iterator.hasNext(); ) {
            RequestDocument docStoreDocument = iterator.next();
            docStoreDocument.setId(srHolUUID);
            if (docStoreDocument.getCategory().equals(DocCategory.WORK.getCode()) && docStoreDocument.getType()
                    .equals(DocType.SOURCEHOLDINGS
                            .getDescription())) {
                CheckinManager checkinManager = new CheckinManager();
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionForRecord = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionForRecord);
                }
                docStoreDocument.setUuid(docStoreDocument.getId());
                checkinManager.updateContent(docStoreDocument);
                CheckoutManager checkOut = new CheckoutManager();
                String checkedOutContent = checkOut.checkOut(docStoreDocument.getId(), "mockUser", "checkout");
                Assert.assertNotNull(checkedOutContent);
                Assert.assertEquals(checkedOutContent, docStoreDocument.getContent().getContent());
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionAfterUpdate = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionAfterUpdate);
                }
            }
        }
    }

    @Test
    public void testAddItemRecord() throws Exception {
        Request request = getRequestObject();
        Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        String instanceUUID = response.getDocuments().get(0).getLinkedDocuments().get(0).getUuid();
        URL resource = getClass().getResource("Add-newitem-instance-request.xml");
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        request = requestHandler.toObject(readFile(file));
        List<RequestDocument> docStoreDocuments = request.getRequestDocuments();
        for (Iterator<RequestDocument> iterator = docStoreDocuments.iterator(); iterator.hasNext(); ) {
            RequestDocument docStoreDocument = iterator.next();
            docStoreDocument.setId(instanceUUID);
            if (docStoreDocument.getCategory().equals(DocCategory.WORK.getCode()) && docStoreDocument.getType()
                    .equals(DocType.INSTANCE
                            .getDescription())) {
                CheckinManager checkinManager = new CheckinManager();
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionForRecord = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionForRecord);
                }
                docStoreDocument.setUuid(docStoreDocument.getId());
                checkinManager.updateContent(docStoreDocument);
                CheckoutManager checkOut = new CheckoutManager();
                for (RequestDocument addedItemRecords : docStoreDocument.getLinkedRequestDocuments()) {
                    String newItemUuid = addedItemRecords.getId();
                    String checkedOutContent = checkOut.checkOut(newItemUuid, "mockUser", "checkout");
                    Assert.assertNotNull(checkedOutContent);
                    Assert.assertEquals(checkedOutContent, addedItemRecords.getContent().getContent());
                }
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionAfterUpdate = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionAfterUpdate);
                }
            }
        }
    }

    @Test
    public void testUpdateHoldingRecord() throws Exception {
        Request request = getRequestObject();
        Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        String itemUUID = response.getDocuments().get(0).getLinkedInstanceDocuments().get(0).getUuid();
        URL resource = getClass().getResource("update-holding-request.xml");
        File file = new File(resource.toURI());
        RequestHandler requestHandler = new RequestHandler();
        request = requestHandler.toObject(readFile(file));
        List<RequestDocument> docStoreDocuments = request.getRequestDocuments();
        for (Iterator<RequestDocument> iterator = docStoreDocuments.iterator(); iterator.hasNext(); ) {
            RequestDocument docStoreDocument = iterator.next();
            docStoreDocument.setId(itemUUID);
            if (docStoreDocument.getCategory().equals(DocCategory.WORK.getCode()) && docStoreDocument.getType()
                    .equals(DocType.HOLDINGS
                            .getDescription())) {
                CheckinManager checkinManager = new CheckinManager();
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionForRecord = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionForRecord);
                }
                docStoreDocument.setUuid(docStoreDocument.getId());
                checkinManager.updateContent(docStoreDocument);
                CheckoutManager checkOut = new CheckoutManager();
                String checkedOutContent = checkOut.checkOut(docStoreDocument.getId(), "mockUser", "checkout");
                Assert.assertNotNull(checkedOutContent);
                Assert.assertEquals(checkedOutContent, docStoreDocument.getContent().getContent());
                if (DocStoreConstants.isVersioningEnabled) {
                    String currentVersionAfterUpdate = getCurrentVersionForRecord(docStoreDocument.getUuid());
                    assertNotNull(currentVersionAfterUpdate);
                }
            }
        }
    }

    @Test
    public void testCheckInManager() throws URISyntaxException, IOException, OleException, RepositoryException {
        CheckinManager checkinManager = (CheckinManager) BeanLocator.getBean("checkinManagerService");
        checkinManager.getDocStoreLogger();
        Session session = RepositoryManager.getRepositoryManager().getSession("CheckinManager", "updateRecordInDocStore");
        checkinManager.getVersionManager(session);
    }
}
