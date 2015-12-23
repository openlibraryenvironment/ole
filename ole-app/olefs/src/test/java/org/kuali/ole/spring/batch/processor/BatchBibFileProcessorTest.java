package org.kuali.ole.spring.batch.processor;

import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.marc4j.marc.Record;
import org.mockito.Mock;
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

}