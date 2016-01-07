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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    BatchProfileDataTransformer mockBatchProfileDataTransformer4;

    @Mock
    BatchProfileDataTransformer mockBatchProfileDataTransformer5;


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
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn("add");
        Mockito.when(mockBatchProfileDataTransformer1.getDestinationField()).thenReturn("035 $a");
        Mockito.when(mockBatchProfileDataTransformer1.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(3);


        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn("prepend");
        Mockito.when(mockBatchProfileDataTransformer2.getDestinationField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer2.getSourceField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);



        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn("delete");
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);


        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn("delete");
        Mockito.when(mockBatchProfileDataTransformer4.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(4);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer5);
        Mockito.when(mockBatchProfileDataTransformer5.getOperation()).thenReturn("delete");
        Mockito.when(mockBatchProfileDataTransformer5.getSourceField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer5.getStep()).thenReturn(5);


        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(mockMarcRecod,mockBatchProcessProfile);

        String valueOf001 = getMarcRecordUtil().getControlFieldValue(mockMarcRecod, "001");
        assertTrue(StringUtils.isBlank(valueOf001));
        System.out.println("001 : " + valueOf001);

        String valueOf003 = getMarcRecordUtil().getControlFieldValue(mockMarcRecod, "003");
        assertTrue(StringUtils.isBlank(valueOf003));
        System.out.println("003 : " + valueOf003);



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
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn("add");
        Mockito.when(mockBatchProfileDataTransformer1.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer1.getDestinationField()).thenReturn("060 $a");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(3);


        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn("Prepend");
        Mockito.when(mockBatchProfileDataTransformer2.getDestinationField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer2.getSourceField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);



        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn("delete");
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn("delete");
        Mockito.when(mockBatchProfileDataTransformer4.getSourceField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(4);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer5);
        Mockito.when(mockBatchProfileDataTransformer5.getOperation()).thenReturn("delete");
        Mockito.when(mockBatchProfileDataTransformer5.getSourceField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer5.getStep()).thenReturn(5);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(mockMarcRecod,mockBatchProcessProfile);

        String valueOf001 = getMarcRecordUtil().getControlFieldValue(mockMarcRecod, "001");
        assertTrue(StringUtils.isBlank(valueOf001));
        System.out.println("001 : " + valueOf001);

        String valueOf003 = getMarcRecordUtil().getControlFieldValue(mockMarcRecod, "003");
        assertTrue(StringUtils.isBlank(valueOf003));
        System.out.println("003 : " + valueOf003);

        String valueOf060 = getMarcRecordUtil().getDataFieldValue(mockMarcRecod, "060 $a");
        assertTrue(StringUtils.isNotBlank(valueOf060));
        System.out.println("060 : " + valueOf060);
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
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn("delete");
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("035 $a$b");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf035$a= getMarcRecordUtil().getDataFieldValue(record, "035 $a");
        assertFalse(StringUtils.contains(valueOf035$a,"ocn"));
        System.out.println("035 $a : " + valueOf035$a);

        String valueOf035$b= getMarcRecordUtil().getDataFieldValue(record, "035 $b");
        assertFalse(StringUtils.contains(valueOf035$b,"ocm"));
        System.out.println("035 $b : " + valueOf035$b);
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

    /*This test case of prepend test.*/
    @Test
    public void deleteTest() throws Exception, IOException {
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
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn("delete");
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn("delete");
        Mockito.when(mockBatchProfileDataTransformer4.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer4.getSourceField()).thenReturn("035 $a$b");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(1);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf035$a$b= getMarcRecordUtil().getDataFieldValue(record, "035 $a$b");
        assertTrue(StringUtils.isBlank(valueOf035$a$b));

        String valueOf003= getMarcRecordUtil().getDataFieldValue(record, "003");
        assertTrue(StringUtils.isBlank(valueOf003));

    }

    /*This test case of prepend test.*/
    @Test
    public void moveTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();

        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        ControlField controlField = marcFactory.newControlField();
        controlField.setTag("003");
        controlField.setData("OCLOC");

        record.addVariableField(controlField);

        ControlField controlField1 = marcFactory.newControlField();
        controlField1.setTag("004");
        controlField1.setData("123456878");

        record.addVariableField(controlField1);

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

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("095");
        dataField1.setIndicator1(' ');
        dataField1.setIndicator2(' ');

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('a');
        subfield3.setData("Value for 95 a");
        dataField1.addSubfield(subfield3);

        Subfield subfield4 = marcFactory.newSubfield();
        subfield4.setCode('b');
        subfield4.setData("Value for 95 b");
        dataField1.addSubfield(subfield4);

        record.addVariableField(dataField1);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn("move");
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer3.getSourceField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer3.getDestinationField()).thenReturn("050 $a$b$c");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn("move");
        Mockito.when(mockBatchProfileDataTransformer4.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer4.getSourceField()).thenReturn("004");
        Mockito.when(mockBatchProfileDataTransformer4.getDestinationField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(2);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer5);
        Mockito.when(mockBatchProfileDataTransformer5.getOperation()).thenReturn("move");
        Mockito.when(mockBatchProfileDataTransformer5.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer5.getSourceField()).thenReturn("035 $a$b");
        Mockito.when(mockBatchProfileDataTransformer5.getDestinationField()).thenReturn("008");
        Mockito.when(mockBatchProfileDataTransformer5.getStep()).thenReturn(3);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn("move");
        Mockito.when(mockBatchProfileDataTransformer1.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer1.getSourceField()).thenReturn("095 $a$b");
        Mockito.when(mockBatchProfileDataTransformer1.getDestinationField()).thenReturn("100 $a$b");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(4);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf050$a$b$c= getMarcRecordUtil().getDataFieldValue(record, "050 $a$b$c");
        assertTrue(StringUtils.equals(valueOf050$a$b$c,"OCLOC OCLOC OCLOC"));
        System.out.println("050 $a$b$c : " + valueOf050$a$b$c);

        String valueOf100$a$b$c= getMarcRecordUtil().getDataFieldValue(record, "100 $a$b");
        assertTrue(StringUtils.equals(valueOf100$a$b$c,"Value for 95 a Value for 95 b Value for 95 a Value for 95 b"));
        System.out.println("100 $a$b : " + valueOf100$a$b$c);

        String valueOf001= getMarcRecordUtil().getControlFieldValue(record, "001");
        assertTrue(StringUtils.equals(valueOf001,"123456878"));
        System.out.println("001 : " + valueOf001);

        String valueOf008= getMarcRecordUtil().getControlFieldValue(record, "008");
        assertTrue(StringUtils.equals(valueOf008,"Value for 035 a Value for 035 b"));
        System.out.println("008 : " + valueOf100$a$b$c);

        String valueOf003= getMarcRecordUtil().getControlFieldValue(record, "003");
        assertTrue(StringUtils.isBlank(valueOf003));

        String valueOf004= getMarcRecordUtil().getDataFieldValue(record, "004");
        assertTrue(StringUtils.isBlank(valueOf004));

        String valueOf35$a$b= getMarcRecordUtil().getControlFieldValue(record, "095 $a$b");
        assertTrue(StringUtils.isBlank(valueOf35$a$b));

        String valueOf65$a$b= getMarcRecordUtil().getDataFieldValue(record, "004");
        assertTrue(StringUtils.isBlank(valueOf65$a$b));

    }


    public MarcRecordUtil getMarcRecordUtil() {
        if(null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }

}