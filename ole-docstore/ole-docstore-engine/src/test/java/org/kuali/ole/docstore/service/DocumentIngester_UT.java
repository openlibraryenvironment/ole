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
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordProcessor;
import org.kuali.ole.repository.NodeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Class to DocumentIngester_UT.
 *
 * @author Rajesh Chowdary K
 * @created Feb 23, 2012
 */
@Ignore
@Deprecated
public class DocumentIngester_UT
        extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentIngester_UT.class);


    private DocumentIngester documentIngester = new DocumentIngester();
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
     * Test method for {@link org.kuali.ole.docstore.service.DocumentIngester#//ingestBibRequestDocument(org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument, javax.jcr.Session)}.
     */
    @Test
    public final void testIngestBibRequestDocument() {
        try {
            documentIngester.ingestBibDocument(reqDocuments.get(0), session, null);
            LOG.info("UUID OF Ingested Record: " + reqDocuments.get(0).getUuid());
            if (reqDocuments.get(0).getUuid() == null) {
                fail("Not Ingested.");
            }
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            fail("Failed :  Test : ingestBibRequestDocument(): " + e);
        }

    }

    /**
     * Test method for {@link org.kuali.ole.docstore.service.DocumentIngester#ingestBibNLinkedInstanceRequestDocuments(org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument, javax.jcr.Session)}.
     */

    public final void testIngestBibNLinkedInstanceRequestDocuments() {
        try {
            RequestDocument bibDoc = reqDocuments.get(0);
            List<String> ingestIDs = documentIngester.ingestBibNLinkedInstanceRequestDocuments(bibDoc, session);
            session.save();
            NodeHandler nh = new NodeHandler();
            Node bibNode = nh.getNodeByUUID(session, bibDoc.getUuid());
            if (bibNode == null) {
                fail("Docuement Ingest Failed.");
            }
            String bibIdentifier = bibNode.getIdentifier();
            WorkBibMarcRecordProcessor marcProcessor = new WorkBibMarcRecordProcessor();
            WorkBibMarcRecord marcRec = marcProcessor.fromXML(bibDoc.getContent().getContent()).getRecords().get(0);
            boolean isBibFound = false;
            for (ControlField cf : marcRec.getControlFields()) {
                if (cf.getTag().equals("001")) {
                    if (cf.getValue().equals(bibIdentifier)) {
                        isBibFound = true;
                    }
                }
            }
            // if (!isBibFound)
            // fail("Bib Identifier Not found in Marc as CF 001");

            PropertyIterator props = bibNode.getProperties("instanceIdentifier");
            while (props.hasNext()) {
                String instanceIdentifier = ((Property) props.next()).getString();
                LOG.info("Instance Identifier: " + instanceIdentifier);
                Node instanceNode = nh.getNodeByUUID(session, instanceIdentifier);
                LOG.info("Instance Node: " + instanceNode);
                boolean isBibIdentifierFound = false;
                PropertyIterator instanceNodeProps = instanceNode.getProperties("bibIdentifier");
                LOG.info("bibIdentifier : Props: " + instanceIdentifier);
                while (instanceNodeProps.hasNext()) {
                    if (((Property) instanceNodeProps.next()).getString().equals(bibDoc.getUuid())) {
                        isBibIdentifierFound = true;
                    }
                }

                if (!isBibIdentifierFound) {
                    fail("No Matching Bib Identifier Found in Instance Node.");
                }

                int holdingsNodesCount = 0;
                int itemNodesCount = 0;
                NodeIterator children = instanceNode.getNodes();
                while (children.hasNext()) {
                    Node child = (Node) children.next();
                    PropertyIterator childProps = child.getProperties();
                    LOG.info("Child Node Props: " + childProps);

                    while (childProps.hasNext()) {
                        Property prop = (Property) childProps.next();
                        LOG.info("Prop: " + prop);
                    }

                }

            }

            /*
             * Verify the following:
             * 4. Verify bib info
             * a. Find bib node.
             * b. Get its uuid as bibIdentifier.
             * c. Get its content. Verify that 001 field in xml content is = bibIdentifier.
             * d. Get instanceIdentifier property.
             * e. Use it to get instanceNode.
             * 5. Verify instance info.
             * a. Get bibIdentifier property of instanceNode.
             * b. Verify it is same as the one in 4.b
             * c. Get children of instanceNode.
             * d. Verify that there is one holdings node and two itemNodes.
             * e. Get the xml content of holdings node. Verify that it has holdingsIdentifier=uuid of holdings node.
             * f. Get the xml contents of item nodes. Verify that they have itemIdentifier=uuid of item node.
             */
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            fail("Failed :  Test : ingestBibNLinkedInstanceRequestDocuments(): " + e);
        }
    }

    /**
     * Test method for {@link org.kuali.ole.docstore.service.DocumentIngester#//ingestInstaceRequestDocument(org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument, javax.jcr.Session)}.
     */

    @Test
    public final void testIngestInstaceRequestDocument() {
        try {
            RequestDocument instanceReqDocument = reqDocuments.get(0).getLinkedRequestDocuments().get(0);
            List<String> ingestedDocs = documentIngester
                    .ingestInstaceRequestDocumentForBulk(instanceReqDocument, session, null);
            for (String ingestedDoc : ingestedDocs) {
                LOG.info("Instance Document UUID: " + ingestedDoc);
                if (ingestedDoc == null) {
                    fail("Instance Document Not Ingested.");
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage() , e);
            fail("Failed :  Test : ingestInstaceRequestDocument(): " + e);
        }
    }

    /**
     * Test method for {@link org.kuali.ole.docstore.service.DocumentIngester#rollbackDocStoreIngestedData(javax.jcr.Session, java.util.List)}.
     */

    @Test
    public final void testRollbackDocStoreIngestedData() {
        List<String> uuids = new ArrayList<String>();
        try {
            RequestDocument instanceReqDocument = reqDocuments.get(0).getLinkedRequestDocuments().get(0);
            uuids = documentIngester.ingestInstaceRequestDocumentForBulk(instanceReqDocument, session, null);
            throw new Exception("Test Throw");
        } catch (Exception e) {
            try {
                documentIngester.rollbackDocStoreIngestedData(session, reqDocuments);
            } catch (Exception ex) {
                fail("Failed :  Test : rollbackDocStoreIngestedData(): " + ex);
            }
        }
    }

}
