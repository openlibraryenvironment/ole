/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.docstore.process;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

/**
 * Class to test BulkIngestDocStoreRequestBuilder.
 *
 * @author Rajesh Chowdary K
 * @version 0.8
 * @created Aug 6, 2012
 */
public class BulkIngestDocStoreRequestBuilder_UT {

    private static final Logger LOG = LoggerFactory.getLogger(BulkIngestDocStoreRequestBuilder_UT.class);
    Exchange exchange = new DefaultExchange(new DefaultCamelContext(), ExchangePattern.InOut);

    /**
     * Method to setUp
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Method to tearDown
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testForMarc() {
        System.out.println("Start:   ");
        CamelContext camelContext = DocStoreCamelContext.getInstance().getCamelContext();
        BulkIngestDocStoreRequestBuilder builder = new BulkIngestDocStoreRequestBuilder(
                "/opt/docstore/upload/ole-batchUpload/mrc", "rck", "batchIngest", camelContext,
                DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.MARC.getCode(),
                "/opt/docstore/upload/ole-batchUpload");
        System.out.println("Starting Context:   ");
        try {
            camelContext.addRoutes(builder);
            camelContext.start();
            Thread.currentThread().sleep(2000);
            System.out.println("Ended:   ");
            camelContext.stop();
        } catch (Exception e) {
            LOG.info("Exception :" + e);
            fail("Error in processing.." + e.getMessage());
        }
    }

    @Test
    public void testForDublinQ() {
        System.out.println("Start:   ");

        CamelContext camelContext = DocStoreCamelContext.getInstance().getCamelContext();
        BulkIngestDocStoreRequestBuilder builder = new BulkIngestDocStoreRequestBuilder(
                "/opt/docstore/upload/ole-batchUpload/dublin-q", "rck", "batchIngest", camelContext,
                DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.DUBLIN_CORE.getCode(),
                "/opt/docstore/upload/ole-batchUpload");
        System.out.println("Starting Context:   ");
        try {
            camelContext.addRoutes(builder);
            camelContext.start();
            Thread.currentThread().sleep(2000);
            System.out.println("Ended:   ");
            camelContext.stop();
        } catch (Exception e) {
            LOG.info("Exception :" + e);
            fail("Error in processing.." + e.getMessage());
        }
    }

    @Test
    public void testForDublinUnq() {
        System.out.println("Start:   ");

        CamelContext camelContext = DocStoreCamelContext.getInstance().getCamelContext();
        BulkIngestDocStoreRequestBuilder builder = new BulkIngestDocStoreRequestBuilder(
                "/opt/docstore/upload/ole-batchUpload/dublin-unq", "rck", "batchIngest", camelContext,
                DocCategory.WORK.getCode(), DocType.BIB.getDescription(), DocFormat.DUBLIN_UNQUALIFIED.getCode(),
                "/opt/docstore/upload/ole-batchUpload");
        System.out.println("Starting Context:   ");
        try {
            camelContext.addRoutes(builder);
            camelContext.start();
            Thread.currentThread().sleep(2000);
            System.out.println("Ended:   ");
            camelContext.stop();
        } catch (Exception e) {
            LOG.info("Exception :" + e);
            fail("Error in processing.." + e.getMessage());
        }
    }

    @Test
    public void testDocStoreCamelContext() throws Exception {
        DocStoreCamelContext docStoreCamelContext = DocStoreCamelContext.getInstance();
        docStoreCamelContext.isRunning();
        docStoreCamelContext.suspend();
        docStoreCamelContext.resume();
        docStoreCamelContext.stop();
    }
}
