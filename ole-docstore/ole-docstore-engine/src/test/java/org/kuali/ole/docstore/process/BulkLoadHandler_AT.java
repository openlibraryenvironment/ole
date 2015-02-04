package org.kuali.ole.docstore.process;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 3/13/12
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class BulkLoadHandler_AT {
    private String jobId;

    private static final Logger LOG = LoggerFactory.getLogger(BulkLoadHandler_AT.class);

    /**
     * Method to setUp
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        DocStoreCamelContext.getInstance();
    }

    /**
     * Method to tearDown
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i < 5; i++) {
            Thread.sleep(5000);
            System.out.println("Status: " + BulkLoadHandler.getInstance().getStatus());
        }
        DocStoreCamelContext.getInstance().stop();
        Thread.sleep(5000);
        System.out.println("Status: " + BulkLoadHandler.getInstance().getStatus());

    }

    /**
     * Test method for {@link org.kuali.ole.docstore.process.DocStoreCamelContext#addBulkIngestNIndexProcess(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testBulkIngestAndIndex() {
        URL url = ClassLoader.getSystemClassLoader().getResource("org/kuali/ole/bulkIngest/");
        try {
            jobId = url.getFile();
            BulkLoadHandler.getInstance()
                    .loadBulk(jobId, ProcessParameters.BULK_DEFAULT_USER, ProcessParameters.BULK_DEFUALT_ACTION);
        } catch (Exception e) {
            LOG.info(e.getMessage());
            fail("Bulk Ingest Failed...");

        }
    }

    @Test
    public final void testBulkIngestAndIndexMetric() {
        URL url = ClassLoader.getSystemClassLoader().getResource("org/kuali/ole/bulkIngest/bulkIngestNIndexMetrics");
        try {
            jobId = url.getFile();
            BulkLoadHandler.getInstance()
                    .loadBulk(jobId, ProcessParameters.BULK_DEFAULT_USER, ProcessParameters.BULK_DEFUALT_ACTION);
        } catch (Exception e) {
            LOG.info(e.getMessage());
            //fail("Bulk Ingest Failed...");

        }
    }


}
