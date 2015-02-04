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
package org.kuali.ole.docstore.service;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Class to DocumentIndexer_UT.
 *
 * @author Rajesh Chowdary K
 * @created Feb 23, 2012
 */
public class DocumentIndexer_UT
        extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentIndexer_UT.class);
    private DocumentIndexer documentIndexer = new DocumentIndexer();
    private List<RequestDocument> reqDocuments = new ArrayList<RequestDocument>();
    private Request req;
    private Session session;

    /**
     * Method to setUp
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        URL resource = getClass().getResource("/org/kuali/ole/repository/request.xml");
        File file = new File(resource.toURI());
        String fileContent = FileUtils.readFileToString(file);
        RequestHandler rh = new RequestHandler();
        req = rh.toObject(fileContent);
        reqDocuments = req.getRequestDocuments();
        session = RepositoryManager.getRepositoryManager().getSession(req.getUser(), req.getOperation());
        DocumentIngester ingester = new DocumentIngester();
        for (RequestDocument doc : reqDocuments) {
            ingester.ingestBibNLinkedInstanceRequestDocuments(doc, session);
        }
    }

    /**
     * Method to tearDown
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        if (session != null) {
            session.logout();
        }
    }

    /**
     * /**
     * Test method for {@link org.kuali.ole.docstore.service.DocumentIndexer#indexDocuments(java.util.List)}.
     */

    public final void testIndexDocuments() {
        try {
            documentIndexer.indexDocuments(reqDocuments);
        } catch (Exception e) {
            LOG.info(e.getMessage(),e);
            fail("Failed in indexing : " + e);
        }

    }

    /**
     * Method to testIndexDocumentsForBulk
     */
    public final void testIndexDocumentsForBulk() {
        try {
            documentIndexer.indexDocumentsForBulk(reqDocuments, true);
        } catch (Exception e) {
            LOG.info(e.getMessage(),e);
            fail("Failed in indexing : " + e);
        }
    }

    /**
     * Test method for {@link org.kuali.ole.docstore.service.DocumentIndexer#indexDocument(org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument)}.
     */

    public final void testIndexDocument() {
        try {
            documentIndexer.indexDocuments(reqDocuments);
        } catch (Exception e) {
            LOG.info(e.getMessage(),e);
            fail("Failed in indexing : " + e);
        }
    }

    /**
     * Test method for {@link org.kuali.ole.docstore.service.DocumentIndexer#rollbackIndexedData(java.util.List)}.
     */

    public final void testRollbackIndexedData() {
        try {
            documentIndexer.indexDocuments(reqDocuments);
        } catch (Exception e) {
            try {
                documentIndexer.rollbackIndexedData(reqDocuments);
            } catch (Exception ex) {
                LOG.info(e.getMessage() , e);
                fail("Failed in rolling back of indexing : " + ex);
            }
        }

    }

}
