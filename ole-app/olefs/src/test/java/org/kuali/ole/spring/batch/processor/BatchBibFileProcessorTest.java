package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileAddOrOverlay;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileFieldOperation;
import org.marc4j.marc.Record;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 12/22/15.
 */
public class BatchBibFileProcessorTest {

    @Mock
    private Record mockRecord;

    @Mock
    private BatchProcessProfile mockBatchProcesProfile;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processRecords() throws Exception, JSONException {
        List<Record> records = new ArrayList<>();
        records.add(mockRecord);

        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        String processedRecords = batchBibFileProcessor.processRecords(records, mockBatchProcesProfile);
        assertNotNull(processedRecords);
    }

    @Test
    public void getOverlayOps() throws Exception, JSONException {
        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        ArrayList<BatchProfileAddOrOverlay> batchProfileAddOrOverlays = new ArrayList<>();

        BatchProfileAddOrOverlay batchProfileAddOrOverlay = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay.setDataType("bibliographic");
        batchProfileAddOrOverlay.setMatchOption("do match");
        batchProfileAddOrOverlay.setOperation("overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay2 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay2.setDataType("bibliographic");
        batchProfileAddOrOverlay2.setMatchOption("do not match");
        batchProfileAddOrOverlay2.setOperation("add");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay2);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay3 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay3.setDataType("holdings");
        batchProfileAddOrOverlay3.setMatchOption("do match");
        batchProfileAddOrOverlay3.setOperation("overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay3);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay4 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay4.setDataType("holdings");
        batchProfileAddOrOverlay4.setMatchOption("do not match");
        batchProfileAddOrOverlay4.setOperation("add");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay4);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay5 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay5.setDataType("items");
        batchProfileAddOrOverlay5.setMatchOption("do match");
        batchProfileAddOrOverlay5.setOperation("overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay5);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay6 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay6.setDataType("items");
        batchProfileAddOrOverlay6.setMatchOption("do not match");
        batchProfileAddOrOverlay6.setOperation("discard");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay6);

        Mockito.when(mockBatchProcesProfile.getBatchProfileAddOrOverlayList()).thenReturn(batchProfileAddOrOverlays);
        List overlayOps = batchBibFileProcessor.getOverlayOps(mockBatchProcesProfile);
        assertTrue(CollectionUtils.isNotEmpty(overlayOps));
        System.out.println(overlayOps);
    }

}