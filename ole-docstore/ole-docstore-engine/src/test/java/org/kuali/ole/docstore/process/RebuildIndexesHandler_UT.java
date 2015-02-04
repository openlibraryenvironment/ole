package org.kuali.ole.docstore.process;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.OleDocStoreData;
import org.kuali.ole.RepositoryBrowser;
import org.kuali.ole.docstore.metrics.reindex.ReIndexingBatchStatus;
import org.kuali.ole.docstore.metrics.reindex.ReIndexingDocTypeStatus;
import org.kuali.ole.docstore.metrics.reindex.ReIndexingStatus;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.Assert.fail;

/**
 * Class to test RebuildIndexesHandler.
 *
 * @author Rajesh Chowdary K
 * @created May 2, 2012
 */
@Ignore
@Deprecated
public class RebuildIndexesHandler_UT extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(RebuildIndexesHandler_UT.class);

    @Before
    public void setUp() throws Exception {
        System.setProperty("OLE_DOCSTORE_USE_DISCOVERY", Boolean.TRUE.toString());
    }

    @Ignore
    public void testRebuildIndexesForMarc() {
        try {
            RebuildIndexesHandler reBuilder = RebuildIndexesHandler.getInstance();
            String category = DocCategory.WORK.getCode();
            String type = DocType.INSTANCE.getDescription();
            String format = DocFormat.MARC.getCode();
            reBuilder.startProcess(null, null, null);
            reBuilder.run();
            reBuilder.stopProcess();
            reBuilder.startProcess(category, type, format);
            reBuilder.run();
            reBuilder.stopProcess();
        } catch (Exception e) {
            LOG.info(e.getMessage(),e);
            fail();
        }
    }

    @Test
    public void testReIndexingBatchStatus() {
        ReIndexingBatchStatus reIndexingBatchStatus = new ReIndexingBatchStatus();
        reIndexingBatchStatus.setBatchEndTime(reIndexingBatchStatus.getBatchEndTime());
        reIndexingBatchStatus.setBatchIndexingTime(reIndexingBatchStatus.getBatchIndexingTime());
        reIndexingBatchStatus.setBatchLoadTime(reIndexingBatchStatus.getBatchLoadTime());
        reIndexingBatchStatus.setBatchStartTime(reIndexingBatchStatus.getBatchStartTime());
        reIndexingBatchStatus.setBatchTotalTime(reIndexingBatchStatus.getBatchTotalTime());
        reIndexingBatchStatus.setRecordsProcessed(reIndexingBatchStatus.getRecordsProcessed());
        reIndexingBatchStatus.setRecordsRemaining(reIndexingBatchStatus.getRecordsRemaining());
        reIndexingBatchStatus.setStatus(reIndexingBatchStatus.getStatus());
    }

    @Test
    public void testReIndexingStatus() {
        ReIndexingStatus reIndexingStatus = ReIndexingStatus.getInstance();
        reIndexingStatus.reset();
        reIndexingStatus.getDocTypeList();
        reIndexingStatus.startDocType(DocCategory.WORK.getCode(), DocType.BIB.getCode(), DocFormat.MARC.getCode());
        reIndexingStatus.getDocTypeList();
        ReIndexingDocTypeStatus reIndexingDocTypeStatus = new ReIndexingDocTypeStatus();
        List list = new ArrayList<ReIndexingDocTypeStatus>();
        list.add(reIndexingDocTypeStatus);
        ReIndexingBatchStatus reIndexingBatchStatus = new ReIndexingBatchStatus();
        List reIndexingBatchStatusList = new ArrayList<ReIndexingBatchStatus>();
        reIndexingBatchStatusList.add(reIndexingBatchStatus);
        reIndexingDocTypeStatus.setReIndBatStatusList(reIndexingBatchStatusList);
        reIndexingStatus.setReIndTypStatusList(list);
        LOG.info("ReIndexingStatsuList : " + reIndexingStatus.getReIndTypStatusList());
        reIndexingStatus.getJsonString();
        reIndexingStatus.toString();
    }

    @Test
    public void testReIndexingDocTypeStatus() {
        ReIndexingDocTypeStatus reIndexingDocTypeStatus = new ReIndexingDocTypeStatus();
        reIndexingDocTypeStatus.getDocCategory();
        reIndexingDocTypeStatus.setReIndBatStatusList(reIndexingDocTypeStatus.getReIndBatStatusList());
        reIndexingDocTypeStatus.getDocCategory();
        reIndexingDocTypeStatus.getDocFormat();
        reIndexingDocTypeStatus.getDocType();
        reIndexingDocTypeStatus.getStatus();
    }

}
