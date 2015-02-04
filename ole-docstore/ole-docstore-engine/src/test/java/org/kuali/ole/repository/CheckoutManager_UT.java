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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xstream.ingest.RequestHandler;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.IngestNIndexHandlerService;
import org.kuali.ole.logger.DocStoreLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/14/11
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class CheckoutManager_UT
        extends BaseTestCase {

    private IngestNIndexHandlerService ingestNIndexHandlerService = BeanLocator
            .getIngestNIndexHandlerService();
    private static final Logger LOG = LoggerFactory
            .getLogger(CheckoutManager_UT.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testCheckOut() throws Exception {
        URL resource = getClass().getResource("request.xml");
        File file = new File(resource.toURI());
        String inputFile = readFile(file);
        Request request = new RequestHandler().toObject(inputFile);
        Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        assertNotNull(response);
        List<RequestDocument> docStoreDocuments = request.getRequestDocuments();

        CheckoutManager checkoutManager = new CheckoutManager();
        DocStoreLogger docStoreLogger = checkoutManager.getDocStoreLogger();
        docStoreLogger.log("checkOutManager");
        for (Iterator<RequestDocument> iterator = docStoreDocuments.iterator(); iterator.hasNext(); ) {
            RequestDocument next = iterator.next();
            String checkedOutContent = checkoutManager.checkOut(next.getUuid(), "mockUser", "checkout");
            docStoreLogger.log("checkOut content:" + checkedOutContent);
            assertNotNull(checkedOutContent);
        }
    }

    @Test
    public void testCheckOutForInstance() throws Exception {

        URL resource = getClass().getResource("/org/kuali/ole/repository/requestInstance.xml");
        File file = new File(resource.toURI());
        String inputFile = readFile(file);
        Request request = new RequestHandler().toObject(inputFile);
        Response response = ingestNIndexHandlerService.ingestNIndexRequestDocuments(request);
        assertNotNull(response);
        List<RequestDocument> docStoreDocuments = request.getRequestDocuments();

        CheckoutManager checkoutManager = new CheckoutManager();
        for (Iterator<RequestDocument> iterator = docStoreDocuments.iterator(); iterator.hasNext(); ) {
            RequestDocument next = iterator.next();
            String checkedOutContent = checkoutManager.checkOut(next.getUuid(), "mockUser", "checkout");
            assertNotNull(checkedOutContent);
            System.out.println(checkedOutContent);


        }
    }

}
