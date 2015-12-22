package org.kuali.ole.spring.batch.processor;

import org.apache.cxf.common.i18n.Exception;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataTransformer;
import org.marc4j.marc.Record;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 12/21/15.
 */
public class StepsProcessorTest {

    @Mock
    BatchProcessProfile mockBatchProcessProfile;

    @Mock
    BatchProfileDataTransformer mockBatchProfileDataTransformer1;

    @Mock
    BatchProfileDataTransformer mockBatchProfileDataTransformer2;

    @Mock
    BatchProfileDataTransformer mockBatchProfileDataTransformer3;

    @Mock
    Record mockMarcRecod;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processSteps() throws Exception {
        StepsProcessor stepsProcessor = new StepsProcessor();

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();
        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn("AddAndDelete");
        Mockito.when(mockBatchProfileDataTransformer1.getDestinationField()).thenReturn("035 $a");
        Mockito.when(mockBatchProfileDataTransformer1.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(2);


        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn("Prepend");
        Mockito.when(mockBatchProfileDataTransformer2.getDestinationField()).thenReturn("035 $a");
        Mockito.when(mockBatchProfileDataTransformer2.getSourceField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(3);



        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn("Remove");
        Mockito.when(mockBatchProfileDataTransformer3.getDestinationField()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);


        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(mockMarcRecod,mockBatchProcessProfile);

    }

}