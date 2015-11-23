package org.kuali.ole.spring.batch.processor;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileConstantsBo;
import org.kuali.ole.spring.batch.handlers.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 11/19/2015.
 */
public class BatchProfileProcessorTest {

    @Mock
    OLEBatchProcessProfileBo mochOleBatchProcessProfileBo;

    @Mock
    BibStatusHandler mockBibStatusHandler;

    @Mock
    ConstantsAndDefaultValueHandler mockConstantsAndDefaultValueHandler;

    @Mock
    DataMappingHandler mockDataMappingHandler;

    @Mock
    DeleteFieldHandler mockDeleteFieldHandler;

    @Mock
    GloballyProtectedFieldHandler mockGloballyProtectedFieldHandler;

    @Mock
    ImportBibliographicRecordHandler mockImportBibliographicRecordHandler;

    @Mock
    MatchingOverlaysHandler mockMatchingOverlaysHandler;

    @Mock
    ProfileProtectedFieldHandler mockProfileProtectedFieldHandler;

    @Mock
    RenameFieldHandler mockRenameFieldHandler;

    @Mock
    StaffOnlyHandler mockStaffOnlyHandler;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    /*
    * This test case for testing the handler's isInterested and process method called properly or not*/
    @Test
    public void testProcessProfile() throws Exception {
        BatchProfileProcessor batchProfileProcessor = new BatchProfileProcessor();


        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBos = new ArrayList<>();
        OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo = new OLEBatchProcessProfileConstantsBo();
        oleBatchProcessProfileConstantsBo.setAttributeName("Attribute Name");
        oleBatchProcessProfileConstantsBo.setAttributeValue("Attribute Value");
        oleBatchProcessProfileConstantsBos.add(oleBatchProcessProfileConstantsBo);
        Mockito.when(mochOleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList())
                .thenReturn(oleBatchProcessProfileConstantsBos);

        Mockito.when(mockBibStatusHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();
        Mockito.when(mockStaffOnlyHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();
        Mockito.when(mockMatchingOverlaysHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();
        Mockito.when(mockImportBibliographicRecordHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();
        Mockito.when(mockConstantsAndDefaultValueHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();
        Mockito.when(mockDataMappingHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();
        Mockito.when(mockDeleteFieldHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();
        Mockito.when(mockGloballyProtectedFieldHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();
        Mockito.when(mockProfileProtectedFieldHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();
        Mockito.when(mockRenameFieldHandler.isInterested(mochOleBatchProcessProfileBo)).thenCallRealMethod();

        List<BatchProcessProfileHandler> batchProcessProfileHandlers = new ArrayList<>();
        batchProcessProfileHandlers.add(mockBibStatusHandler);
        batchProcessProfileHandlers.add(mockConstantsAndDefaultValueHandler);
        batchProcessProfileHandlers.add(mockDataMappingHandler);
        batchProcessProfileHandlers.add(mockDeleteFieldHandler);
        batchProcessProfileHandlers.add(mockGloballyProtectedFieldHandler);
        batchProcessProfileHandlers.add(mockImportBibliographicRecordHandler);
        batchProcessProfileHandlers.add(mockMatchingOverlaysHandler);
        batchProcessProfileHandlers.add(mockProfileProtectedFieldHandler);
        batchProcessProfileHandlers.add(mockRenameFieldHandler);
        batchProcessProfileHandlers.add(mockStaffOnlyHandler);
        batchProfileProcessor.setBatchProcessProfileHandlers(batchProcessProfileHandlers);

        batchProfileProcessor.processProfile(mochOleBatchProcessProfileBo);

        Mockito.verify(mockBibStatusHandler, Mockito.times(1)).process();
        Mockito.verify(mockImportBibliographicRecordHandler, Mockito.times(1)).process();
        Mockito.verify(mockMatchingOverlaysHandler, Mockito.times(1)).process();
        Mockito.verify(mockStaffOnlyHandler, Mockito.times(1)).process();
        Mockito.verify(mockConstantsAndDefaultValueHandler, Mockito.times(1)).process();
        Mockito.verify(mockGloballyProtectedFieldHandler, Mockito.times(0)).process();
    }
}