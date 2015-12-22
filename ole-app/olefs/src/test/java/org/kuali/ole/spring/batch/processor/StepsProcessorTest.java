package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.i18n.Exception;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.loaders.common.FileUtils;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataTransformer;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.*;
import org.marc4j.marc.impl.RecordImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private MarcRecordUtil marcRecordUtil;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processSteps1() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();

        String rawMarcContent = FileUtils.readFileContent("org/kuali/ole/spring/batch/processor/InvYBP_Test_1207_2rec.mrc");
        List<Record> records = new MarcXMLConverter().convertRawMarchToMarc(rawMarcContent);
        assertTrue(CollectionUtils.isNotEmpty(records));
        Record mockMarcRecod = records.get(0);

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
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);


        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(mockMarcRecod,mockBatchProcessProfile);

        String valueOf001 = getMarcRecordUtil().getControlFieldValue(mockMarcRecod, "001");
        assertTrue(StringUtils.isBlank(valueOf001));

    }


    @Test
    public void processSteps2() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();

        String rawMarcContent = FileUtils.readFileContent("org/kuali/ole/spring/batch/processor/InvYBP_Test_1207_2rec.mrc");
        List<Record> records = new MarcXMLConverter().convertRawMarchToMarc(rawMarcContent);
        assertTrue(CollectionUtils.isNotEmpty(records));
        Record mockMarcRecod = records.get(0);


        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();
        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn("AddAndDelete");
        Mockito.when(mockBatchProfileDataTransformer1.getSourceField()).thenReturn("035 $a");
        Mockito.when(mockBatchProfileDataTransformer1.getDestinationField()).thenReturn("060 $a");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(3);


        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn("Prepend");
        Mockito.when(mockBatchProfileDataTransformer2.getDestinationField()).thenReturn("035 $a");
        Mockito.when(mockBatchProfileDataTransformer2.getSourceField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(4);



        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn("Remove");
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);


        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(mockMarcRecod,mockBatchProcessProfile);

        String valueOf060 = getMarcRecordUtil().getDataFieldValue(mockMarcRecod, "060 $a");
        assertTrue(StringUtils.isNotBlank(valueOf060));
    }

    @Test
    public void processSteps3() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();

        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();
        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("ocnValue for 035 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("ocmValue for 035 b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn("Remove");
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("035 $a$b");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf035$a= getMarcRecordUtil().getDataFieldValue(record, "035 $a");
        assertFalse(StringUtils.contains(valueOf035$a,"ocn"));

        String valueOf035$b= getMarcRecordUtil().getDataFieldValue(record, "035 $b");
        assertFalse(StringUtils.contains(valueOf035$b,"ocm"));
    }

    /*This test case of prepend test.*/
    @Test
    public void prependTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();

        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        ControlField controlField = marcFactory.newControlField();
        controlField.setTag("003");
        controlField.setData("OCLOC");

        record.addVariableField(controlField);

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("Value for 035 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("Value for 035 b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn("Prepend");
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer3.getDestinationField()).thenReturn("035 $a$b$c$d");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf035$a= getMarcRecordUtil().getDataFieldValue(record, "035 $a");
        assertTrue(StringUtils.equals(valueOf035$a,"(OCLOC)Value for 035 a"));
        System.out.println("035 $a : " + valueOf035$a);

        String valueOf035$b= getMarcRecordUtil().getDataFieldValue(record, "035 $b");
        assertTrue(StringUtils.equals(valueOf035$b,"(OCLOC)Value for 035 b"));
        System.out.println("035 $b : " + valueOf035$b);

    }


    public MarcRecordUtil getMarcRecordUtil() {
        if(null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }

}