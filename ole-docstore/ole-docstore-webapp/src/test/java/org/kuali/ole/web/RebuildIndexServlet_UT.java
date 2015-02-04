package org.kuali.ole.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.metrics.reindex.ReIndexingStatus;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.process.RebuildIndexesHandler;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/17/12
 * Time: 1:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class RebuildIndexServlet_UT {
    /**
     * Method to setUp
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Method to tearDown
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testStartRebuildIndex() throws Exception {
        RebuildIndexesHandler rebuildIndexesHandler = RebuildIndexesHandler.getInstance();
        String result = rebuildIndexesHandler
                .startProcess(DocCategory.WORK.getDescription(), DocType.BIB.getDescription(),
                              DocFormat.MARC.getDescription());

        System.out.println(result);
    }

    @Test
    public void testShowRebuildIndexStatus() throws Exception {
        RebuildIndexesHandler rebuildIndexesHandler = RebuildIndexesHandler.getInstance();
        rebuildIndexesHandler
                .startProcess(DocCategory.WORK.getDescription(), DocType.BIB.getDescription(),
                              DocFormat.MARC.getDescription());

        ReIndexingStatus reIndexingStatus = ReIndexingStatus.getInstance();

        String jsonString = reIndexingStatus.getJsonString();
        System.out.println("json String:" + jsonString);
    }

    @Test
    public void testClearRebuildIndexStatus() throws Exception {
        RebuildIndexesHandler rebuildIndexesHandler = RebuildIndexesHandler.getInstance();
        String result = rebuildIndexesHandler
                .startProcess(DocCategory.WORK.getDescription(), DocType.BIB.getDescription(),
                              DocFormat.MARC.getDescription());

        ReIndexingStatus reIndexingStatus = ReIndexingStatus.getInstance();
        reIndexingStatus.reset();
        String jsonString = reIndexingStatus.getJsonString();
        System.out.println("json String:" + jsonString);
    }


    @Test
    public void testStopRebuildIndexStatus() throws Exception {
        RebuildIndexesHandler rebuildIndexesHandler = RebuildIndexesHandler.getInstance();
        rebuildIndexesHandler.startProcess(DocCategory.WORK.getDescription(), DocType.BIB.getDescription(),
                                           DocFormat.MARC.getDescription());
        String result = rebuildIndexesHandler.stopProcess();
        System.out.println(result);


    }
}
