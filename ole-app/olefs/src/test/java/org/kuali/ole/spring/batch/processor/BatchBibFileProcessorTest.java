package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileAddOrOverlay;
import org.marc4j.marc.Record;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        batchProfileAddOrOverlay.setDataType("Bibliographic");
        batchProfileAddOrOverlay.setMatchOption("If Match Found");
        batchProfileAddOrOverlay.setOperation("overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay2 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay2.setDataType("Bibliographic");
        batchProfileAddOrOverlay2.setMatchOption("If Match Not Found");
        batchProfileAddOrOverlay2.setOperation("Add");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay2);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay3 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay3.setDataType("Holdings");
        batchProfileAddOrOverlay3.setMatchOption("If Match Found");
        batchProfileAddOrOverlay3.setOperation("overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay3);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay4 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay4.setDataType("Holdings");
        batchProfileAddOrOverlay4.setMatchOption("If Match Not Found");
        batchProfileAddOrOverlay4.setOperation("Add");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay4);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay5 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay5.setDataType("Item");
        batchProfileAddOrOverlay5.setMatchOption("If Match Found");
        batchProfileAddOrOverlay5.setOperation("overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay5);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay6 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay6.setDataType("Item");
        batchProfileAddOrOverlay6.setMatchOption("If Match Not Found");
        batchProfileAddOrOverlay6.setOperation("discard");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay6);

        Mockito.when(mockBatchProcesProfile.getBatchProfileAddOrOverlayList()).thenReturn(batchProfileAddOrOverlays);
        List overlayOps = batchBibFileProcessor.getOps(mockBatchProcesProfile);
        assertTrue(CollectionUtils.isNotEmpty(overlayOps));
        System.out.println(overlayOps);
    }

    @Test
    public void getOpsFilter() throws Exception, JSONException {
        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        ArrayList<BatchProfileAddOrOverlay> batchProfileAddOrOverlays = new ArrayList<>();

        BatchProfileAddOrOverlay batchProfileAddOrOverlay = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay.setDataType("Bibliographic");
        batchProfileAddOrOverlay.setMatchOption("If Match Found");
        batchProfileAddOrOverlay.setOperation("Overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay);

        BatchProfileAddOrOverlay batchProfileAddOrOverlayWithFilter1 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlayWithFilter1.setDataType("Bibliographic");
        batchProfileAddOrOverlayWithFilter1.setMatchOption("If Match Found");
        batchProfileAddOrOverlayWithFilter1.setOperation("Overlay");
        batchProfileAddOrOverlayWithFilter1.setAddOrOverlayField("Bib Status");
        batchProfileAddOrOverlayWithFilter1.setAddOrOverlayFieldOperation("Equals To");
        batchProfileAddOrOverlayWithFilter1.setAddOrOverlayFieldValue("Cataloging Complete");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlayWithFilter1);

        BatchProfileAddOrOverlay batchProfileAddOrOverlayWithFilter2 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlayWithFilter2.setDataType("Bibliographic");
        batchProfileAddOrOverlayWithFilter2.setMatchOption("If Match Found");
        batchProfileAddOrOverlayWithFilter2.setOperation("Overlay");
        batchProfileAddOrOverlayWithFilter2.setAddOrOverlayField("Staff Only");
        batchProfileAddOrOverlayWithFilter2.setAddOrOverlayFieldOperation("Equals To");
        batchProfileAddOrOverlayWithFilter2.setAddOrOverlayFieldValue("Y");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlayWithFilter2);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay2 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay2.setDataType("Bibliographic");
        batchProfileAddOrOverlay2.setMatchOption("If Match Not Found");
        batchProfileAddOrOverlay2.setOperation("Add");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay2);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay3 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay3.setDataType("Holdings");
        batchProfileAddOrOverlay3.setMatchOption("If Match Found");
        batchProfileAddOrOverlay3.setOperation("Overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay3);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay4 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay4.setDataType("Holdings");
        batchProfileAddOrOverlay4.setMatchOption("If Match Not Found");
        batchProfileAddOrOverlay4.setOperation("Add");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay4);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay5 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay5.setDataType("Item");
        batchProfileAddOrOverlay5.setMatchOption("If Match Found");
        batchProfileAddOrOverlay5.setOperation("Overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay5);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay6 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay6.setDataType("Item");
        batchProfileAddOrOverlay6.setMatchOption("If Match Not Found");
        batchProfileAddOrOverlay6.setOperation("Discard");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay6);

        Mockito.when(mockBatchProcesProfile.getBatchProfileAddOrOverlayList()).thenReturn(batchProfileAddOrOverlays);
        JSONArray opsFilter = batchBibFileProcessor.getOpsFilter(mockBatchProcesProfile);
        System.out.println(opsFilter.toString());

    }

}