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
package org.kuali.ole;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.repopojo.FolderNode;
import org.kuali.ole.docstore.model.repopojo.RepositoryData;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
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
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 5/23/11
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class RepositoryBrowser_UT extends BaseTestCase {

    private static final Logger LOG = LoggerFactory
            .getLogger(RepositoryBrowser_UT.class);
    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator
            .getIngestNIndexHandlerService();

    @Before
    public void setUp() throws Exception {
        //  super.setUp();
        // MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBrowseRepository() throws Exception {
        RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
        List<OleDocStoreData> oleDocStoreDatas = repositoryBrowser.browseDataSetup();
        for (Iterator<OleDocStoreData> iterator = oleDocStoreDatas.iterator(); iterator.hasNext(); ) {
            OleDocStoreData oleDocStoreData = iterator.next();
            LOG.info(oleDocStoreData.getCategory());
            Map<String, List<String>> typeFormatMap = oleDocStoreData.getTypeFormatMap();
            Set<String> keys = typeFormatMap.keySet();
            for (Iterator<String> stringIterator = keys.iterator(); stringIterator.hasNext(); ) {
                String docType = stringIterator.next();
                LOG.info(docType + ": " + typeFormatMap.get(docType));
            }
        }
    }

    @Test
    public void testGetRandomUUIDs() throws Exception {
        URL url = this.getClass().getResource("repository/request.xml");
        File file = new File(url.toURI());

        String docStoreDocuments = ingestNIndexHandlerService.ingestNIndexRequestDocuments(readFile(file));
        // System.out.println("testGetRandomUUIDs docStoreDocuments length "+docStoreDocuments);
        assertTrue(!docStoreDocuments.isEmpty());
        RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
        List<String> uuiDs = repositoryBrowser
                .getUUIDs(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.MARC.getCode(),
                        1);
        System.out.println("testGetRandomUUIDs uuiDs length " + uuiDs.size());
        if (uuiDs.size() > 0) {
            Assert.assertNotNull(uuiDs);
        }
        uuiDs = repositoryBrowser.getUUIDs(DocCategory.WORK.getCode(), DocType.LICENSE.getDescription(),
                DocFormat.ONIXPL.getCode(), 1);
        System.out.println("uuiDs LICENSE" + uuiDs);
        if (uuiDs.size() > 0) {
            Assert.assertNotNull(uuiDs);
        }
        uuiDs = repositoryBrowser
                .getUUIDs(DocCategory.WORK.getCode(), DocType.ITEM.getDescription(), DocFormat.OLEML.getCode(),
                        1);
        System.out.println("uuiDs ITEM" + uuiDs.size());
        Assert.assertNotNull(uuiDs);
        uuiDs = repositoryBrowser.getUUIDs(DocCategory.WORK.getCode(), DocType.HOLDINGS.getDescription(),
                DocFormat.OLEML.getCode(), 1);
        Assert.assertNotNull(uuiDs);
        System.out.println("uuiDs HOLDING" + uuiDs.size());
        for (Iterator<String> iterator = uuiDs.iterator(); iterator.hasNext(); ) {
            String uuid = iterator.next();
            LOG.info(uuid);
        }
    }

    public void testGetUUIDs() {
        try {
            URL url = this.getClass().getResource("repository/request.xml");
            File file = new File(url.toURI());

            String docStoreDocuments = ingestNIndexHandlerService.ingestNIndexRequestDocuments(readFile(file));
            //  System.out.println("testGetRandomUUIDs docStoreDocuments length "+docStoreDocuments);
            assertTrue(!docStoreDocuments.isEmpty());
            RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
            List<String> uuiDs = repositoryBrowser
                    .getUUIDs(DocCategory.WORK.getCode(), DocType.BIB.getDescription(),
                            DocFormat.MARC.getCode(), 1, 2);

            System.out.println("testGetRandomUUIDs uuiDs length " + uuiDs.size());
            assertTrue(!uuiDs.isEmpty());
            uuiDs = repositoryBrowser.getUUIDs(DocCategory.WORK.getCode(), DocType.LICENSE.getDescription(),
                    DocFormat.ONIXPL.getCode(), 1, 1);

            System.out.println("uuiDs LICENSE" + uuiDs);
            //assertTrue(!uuiDs.isEmpty());
            uuiDs = repositoryBrowser.getUUIDs(DocCategory.WORK.getCode(), DocType.ITEM.getDescription(),
                    DocFormat.OLEML.getCode(), 1, 1);
            System.out.println("uuiDs ITEM" + uuiDs);
            //   assertTrue(!uuiDs.isEmpty());
            uuiDs = repositoryBrowser.getUUIDs(DocCategory.WORK.getCode(), DocType.HOLDINGS.getDescription(),
                    DocFormat.OLEML.getCode(), 1, 1);
            System.out.println("uuiDs ITEM" + uuiDs);
            for (Iterator<String> iterator = uuiDs.iterator(); iterator.hasNext(); ) {
                String uuid = iterator.next();
                LOG.info(uuid);
            }
        } catch (Exception e) {
            LOG.info("Exception : " + e);
        }
    }

    @Test
    public void testRepositoryNodeCount() throws Exception {
        // MarcIngester marcIngester = new MarcIngester();
        // marcIngester.setSolrIndexer(mockSolrIndexer);
        // URL url = this.getClass().getResource("sample.xml");
        // File file = new File(url.toURI());
        // List<RequestDocument> docStoreDocuments = marcIngester.ingestDocument(readFile(file));
        // assertTrue(!docStoreDocuments.isEmpty());

        RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
        List<OleDocStoreData> oleDocStoreDataList = repositoryBrowser.getFilesCount();
        assertNotNull(oleDocStoreDataList);
        for (Iterator<OleDocStoreData> iterator = oleDocStoreDataList.iterator(); iterator.hasNext(); ) {
            OleDocStoreData oleDocStoreData = iterator.next();
            Map<String, Map<String, Long>> typeFormatMapWithNodeCount = oleDocStoreData.getTypeFormatMapWithNodeCount();
            for (Iterator<String> oleDocStoreDataIterator = typeFormatMapWithNodeCount.keySet()
                    .iterator(); oleDocStoreDataIterator
                         .hasNext(); ) {
                String key = oleDocStoreDataIterator.next();
                LOG.info(key + ": " + typeFormatMapWithNodeCount.get(key));
            }
        }
    }

    @Test
    public void repoDump() throws RepositoryException, OleException {
        RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
        String dump = repositoryBrowser.getRepositoryDump();
        LOG.info("dump" + dump);
    }

    @Test
    public void getDetails() throws Exception {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession("mockUser", "test");
        System.out.println("getName " + session.getWorkspace().getName());
    }

    @Ignore
    @Test
    public void getRepositoryDumpRange() throws Exception {
        RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
        String category = DocCategory.WORK.getCode();
        String type = DocType.BIB.getDescription();
        String format = DocFormat.MARC.getCode();
        int fromIndex = 1;
        int Count = 1;
        cleanRepository(category, type);
        String beforedump = repositoryBrowser.getRepositoryDump();
        LOG.info("beforedump" + beforedump);
        loadBulkMarcRecords(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.MARC.getCode(),
                "/org/kuali/ole/bulkhandler/OLE-Bib-bulkIngest-IU-Set1-split.xml");
        String dump = repositoryBrowser.getRepositoryDump();
        LOG.info("dump" + dump);
        String dumpRange = repositoryBrowser.getRepositoryRangeDump(category, type, format, fromIndex, Count);
        LOG.info("dumpRange" + dumpRange);
    }

    public void cleanRepository(String category, String type) throws OleException, RepositoryException {
        RepositoryManager repositoryManager = RepositoryManager.getRepositoryManager();
        Session session = repositoryManager.getSession("mockUser", "test");
        Node rootNode = session.getRootNode();
        Node catNode = rootNode.getNode(category);
        Node typeNode = catNode.getNode(type);
        for (Iterator<Node> typeiterator = typeNode.getNodes(); typeiterator.hasNext(); ) {
            Node formatNode = typeiterator.next();
            if (!formatNode.getName().equals("jcr:system")) {
                formatNode.remove();
            }
        }
        session.save();
        repositoryManager.logout(session);
    }

    private void loadBulkMarcRecords(String category, String type, String format, String inputXml) throws Exception {
        File file = new File(getClass().getResource("/org/kuali/ole/repository/request.xml").toURI());
        String input = FileUtils.readFileToString(file);
        RequestHandler rh = new RequestHandler();
        Request request = rh.toObject(input);
        Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);

        // BulkLoadHandler bulkLoadHandler = new BulkLoadHandler();
        // assertNotNull(bulkLoadHandler);
        // List<String> recordUUIDs = bulkLoadHandler.loadBulk(category,
        // type,
        // format,
        // new File(getClass().getResource(inputXml).toURI()),
        // "testUser", "testLoadBulk");
        // assertNotNull(recordUUIDs);
        // Assert.assertTrue(!recordUUIDs.isEmpty());
        // for (Iterator<String> iterator = recordUUIDs.iterator(); iterator.hasNext(); ) {
        // String recordUUID = iterator.next();
        // LOG.info("recordUUID"+recordUUID);
        // }
    }

    @Test
    public void loadRepository() throws OleException, RepositoryException {
        RepositoryManager oleRepositoryManager = RepositoryManager.getRepositoryManager();
        RepositoryData repositoryData = new RepositoryData();
        RepositoryBrowser repositoryBrowser = new RepositoryBrowser();
        FolderNode folderNode = repositoryBrowser
                .computeNodeCount(oleRepositoryManager.getSession().getRootNode(), repositoryData);
        String repoDump = repositoryData.getRepositoryDump(repositoryData);
        LOG.info("repoDump" + repoDump);
        repositoryBrowser.browseRepositoryContent();
        repositoryBrowser.generateNodeCount();
    }

}
