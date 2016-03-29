package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.i18n.Exception;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.constants.OleNGConstants;
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
import java.util.Iterator;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
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
    BatchProfileDataTransformer mockBatchProfileDataTransformer4;

    @Mock
    BatchProfileDataTransformer mockBatchProfileDataTransformer5;


    private MarcRecordUtil marcRecordUtil;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addFieldTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();


        Record record = getMarcRecordUtil().getMarcFactory().newRecord();

        ControlField controlField = getMarcRecordUtil().getMarcFactory().newControlField();
        controlField.setTag("003");
        controlField.setData("OCLOC");
        record.addVariableField(controlField);

        DataField dataField1 = getMarcRecordUtil().getMarcFactory().newDataField();
        dataField1.setTag("852");
        dataField1.setIndicator1('1');
        dataField1.setIndicator2('4');

        Subfield subfield2 = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield2.setCode('a');
        subfield2.setData("852 a");
        dataField1.addSubfield(subfield2);
        record.addVariableField(dataField1);


        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.ADD_FIELD);
        Mockito.when(mockBatchProfileDataTransformer1.getConstant()).thenReturn("New Field Value");
        Mockito.when(mockBatchProfileDataTransformer1.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer1.getInd1()).thenReturn("0");
        Mockito.when(mockBatchProfileDataTransformer1.getInd2()).thenReturn("4");
        Mockito.when(mockBatchProfileDataTransformer1.getSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer1.getDestDataField()).thenReturn("070");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd1()).thenReturn("0");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd2()).thenReturn("4");
        Mockito.when(mockBatchProfileDataTransformer1.getDestSubField()).thenReturn("z");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn(OleNGConstants.ADD_FIELD);
        Mockito.when(mockBatchProfileDataTransformer2.getDataField()).thenReturn("852");
        Mockito.when(mockBatchProfileDataTransformer2.getInd1()).thenReturn("1");
        Mockito.when(mockBatchProfileDataTransformer2.getInd2()).thenReturn("4");
        Mockito.when(mockBatchProfileDataTransformer2.getSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer2.getDestDataField()).thenReturn("050");
        Mockito.when(mockBatchProfileDataTransformer2.getDestInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer2.getDestInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer2.getDestSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.ADD_FIELD);
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer3.getDestDataField()).thenReturn("080");
        Mockito.when(mockBatchProfileDataTransformer3.getDestInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer3.getDestInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer3.getDestSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(3);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn(OleNGConstants.ADD_FIELD);
        Mockito.when(mockBatchProfileDataTransformer4.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer4.getDestDataField()).thenReturn("006");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(4);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf070$z= getMarcRecordUtil().getDataFieldValueWithIndicators(record,"070", "0", "4", "z");
        assertEquals(valueOf070$z,"New Field Value");
        System.out.println("070 04 $a : " + valueOf070$z);

        String valueOf050$a= getMarcRecordUtil().getDataFieldValueWithIndicators(record,"050", " ", " ", "a");
        assertEquals(valueOf050$a,"852 a");
        System.out.println("050 $a : " + valueOf050$a);

        String valueOf080$a= getMarcRecordUtil().getDataFieldValueWithIndicators(record,"080", " ", " ", "a");
        assertEquals(valueOf080$a,"OCLOC");
        System.out.println("080 $a : " + valueOf080$a);

        String valueOf006= getMarcRecordUtil().getControlFieldValue(record,"006");
        assertEquals(valueOf006,"OCLOC");
        System.out.println("006 : " + valueOf006);
    }

    @Test
    public void addSubFieldTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();
        Record record = getMarcRecordUtil().getMarcFactory().newRecord();

        ControlField controlField = getMarcRecordUtil().getMarcFactory().newControlField();
        controlField.setTag("003");
        controlField.setData("OCLOC");
        record.addVariableField(controlField);

        DataField dataField = getMarcRecordUtil().getMarcFactory().newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield.setCode('a');
        subfield.setData("035 a");
        dataField.addSubfield(subfield);
        record.addVariableField(dataField);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.ADD_SUBFIELD);
        Mockito.when(mockBatchProfileDataTransformer1.getConstant()).thenReturn("New Sub Field Value");
        Mockito.when(mockBatchProfileDataTransformer1.getDestDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer1.getDestSubField()).thenReturn("z");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn(OleNGConstants.ADD_SUBFIELD);
        Mockito.when(mockBatchProfileDataTransformer2.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getDestDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer2.getDestInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer2.getDestInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer2.getDestSubField()).thenReturn("h");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.ADD_SUBFIELD);
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer3.getInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer3.getInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer3.getSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer3.getDestDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer3.getDestInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer3.getDestInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer3.getDestSubField()).thenReturn("g");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(3);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf035$z= getMarcRecordUtil().getDataFieldValueWithIndicators(record,"035", " ", " ", "z");
        assertEquals(valueOf035$z,"New Sub Field Value");
        System.out.println("035  $z : " + valueOf035$z);

        String valueOf035$h= getMarcRecordUtil().getDataFieldValueWithIndicators(record,"035", " ", " ", "h");
        assertEquals(valueOf035$h,"OCLOC");
        System.out.println("035  $h : " + valueOf035$h);

        String valueOf035$g= getMarcRecordUtil().getDataFieldValueWithIndicators(record,"035", " ", " ", "g");
        assertEquals(valueOf035$g,"035 a");
        System.out.println("035  $g : " + valueOf035$g);
    }

    @Test
    public void copyAndPasteTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();
        Record record = getMarcRecordUtil().getMarcFactory().newRecord();
        DataField dataField = getMarcRecordUtil().getMarcFactory().newDataField();
        dataField.setTag("035");
        dataField.setIndicator1('1');
        dataField.setIndicator2('2');

        Subfield subfield = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield.setCode('a');
        subfield.setData("035 a");
        dataField.addSubfield(subfield);
        record.addVariableField(dataField);


        ControlField controlField = getMarcRecordUtil().getMarcFactory().newControlField();
        controlField.setTag("003");
        controlField.setData("OCLOC");
        record.addVariableField(controlField);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.COPY_PASTE);
        Mockito.when(mockBatchProfileDataTransformer1.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer1.getDestDataField()).thenReturn("070");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn(OleNGConstants.COPY_PASTE);
        Mockito.when(mockBatchProfileDataTransformer2.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getDestDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf070$z= getMarcRecordUtil().getDataFieldValueWithIndicators(record,"070", "1", "2", "a");
        assertEquals(valueOf070$z,"035 a");
        System.out.println("070 12 $z : " + valueOf070$z);

        String valueOf001= getMarcRecordUtil().getControlFieldValue(record,"001");
        assertEquals(valueOf001,"OCLOC");
        System.out.println("001 : " + valueOf001);
    }

    @Test
    public void cutAndPasteTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();
        Record record = getMarcRecordUtil().getMarcFactory().newRecord();
        DataField dataField = getMarcRecordUtil().getMarcFactory().newDataField();
        dataField.setTag("035");
        dataField.setIndicator1('1');
        dataField.setIndicator2('2');

        Subfield subfield = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield.setCode('a');
        subfield.setData("035 a");
        dataField.addSubfield(subfield);
        record.addVariableField(dataField);


        ControlField controlField = getMarcRecordUtil().getMarcFactory().newControlField();
        controlField.setTag("003");
        controlField.setData("OCLOC");
        record.addVariableField(controlField);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.CUT_PASTE);
        Mockito.when(mockBatchProfileDataTransformer1.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer1.getDestDataField()).thenReturn("070");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn(OleNGConstants.CUT_PASTE);
        Mockito.when(mockBatchProfileDataTransformer2.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getDestDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf070$z= getMarcRecordUtil().getDataFieldValueWithIndicators(record,"070", "1", "2", "a");
        assertEquals(valueOf070$z,"035 a");
        System.out.println("070 12 $z : " + valueOf070$z);

        String valueOf001= getMarcRecordUtil().getControlFieldValue(record,"001");
        assertEquals(valueOf001,"OCLOC");
        System.out.println("001 : " + valueOf001);

        VariableField field035 = record.getVariableField("035");
        assertNull(field035);
        VariableField field003 = record.getVariableField("003");
        assertNull(field003);
    }

    @Test
    public void deleteFieldTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();
        Record record = getMarcRecordUtil().getMarcFactory().newRecord();
        DataField dataField = getMarcRecordUtil().getMarcFactory().newDataField();
        dataField.setTag("035");
        dataField.setIndicator1('1');
        dataField.setIndicator2('2');

        Subfield subfield = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield.setCode('a');
        subfield.setData("035 a");
        dataField.addSubfield(subfield);
        record.addVariableField(dataField);


        ControlField controlField = getMarcRecordUtil().getMarcFactory().newControlField();
        controlField.setTag("003");
        controlField.setData("OCLOC");
        record.addVariableField(controlField);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.DELETE_FIELD);
        Mockito.when(mockBatchProfileDataTransformer1.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn(OleNGConstants.DELETE_FIELD);
        Mockito.when(mockBatchProfileDataTransformer2.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        VariableField field035 = record.getVariableField("035");
        assertNull(field035);
        VariableField field003 = record.getVariableField("003");
        assertNull(field003);
    }

    @Test
    public void deleteSubFieldTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();
        Record record = getMarcRecordUtil().getMarcFactory().newRecord();
        DataField dataField = getMarcRecordUtil().getMarcFactory().newDataField();
        dataField.setTag("035");
        dataField.setIndicator1('1');
        dataField.setIndicator2('2');

        Subfield subfield = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield.setCode('a');
        subfield.setData("035 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield2.setCode('b');
        subfield2.setData("035 b");
        dataField.addSubfield(subfield2);
        record.addVariableField(dataField);

        DataField dataField2 = getMarcRecordUtil().getMarcFactory().newDataField();
        dataField2.setTag("035");
        dataField2.setIndicator1('1');
        dataField2.setIndicator2('2');

        Subfield subfield3 = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield3.setCode('z');
        subfield3.setData("035 z");
        dataField2.addSubfield(subfield3);

        Subfield subfield4 = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield4.setCode('b');
        subfield4.setData("035 b");
        dataField2.addSubfield(subfield4);
        record.addVariableField(dataField2);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.DELETE_SUBFIELD);
        Mockito.when(mockBatchProfileDataTransformer1.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer1.getInd1()).thenReturn("1");
        Mockito.when(mockBatchProfileDataTransformer1.getInd2()).thenReturn("2");
        Mockito.when(mockBatchProfileDataTransformer1.getSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(1);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        List<VariableField> dataFields = record.getVariableFields("035");
        assertTrue(CollectionUtils.isNotEmpty(dataFields));
        for (Iterator<VariableField> iterator = dataFields.iterator(); iterator.hasNext(); ) {
            DataField df = (DataField) iterator.next();
            Subfield sf = df.getSubfield('a');
            assertNull(sf);
        }
    }

    @Test
    public void RemoveValueTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();
        Record record = getMarcRecordUtil().getMarcFactory().newRecord();
        DataField dataField = getMarcRecordUtil().getMarcFactory().newDataField();
        dataField.setTag("035");
        dataField.setIndicator1('1');
        dataField.setIndicator2('2');

        Subfield subfield = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield.setCode('a');
        subfield.setData("035 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = getMarcRecordUtil().getMarcFactory().newSubfield();
        subfield2.setCode('b');
        subfield2.setData("035 b");
        dataField.addSubfield(subfield2);
        record.addVariableField(dataField);

        ControlField controlField = getMarcRecordUtil().getMarcFactory().newControlField();
        controlField.setTag("001");
        controlField.setData("ocnocm123456789");
        record.addVariableField(controlField);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer1.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer1.getInd1()).thenReturn("1");
        Mockito.when(mockBatchProfileDataTransformer1.getInd2()).thenReturn("2");
        Mockito.when(mockBatchProfileDataTransformer1.getSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer1.getConstant()).thenReturn("035 ");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer2.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer2.getConstant()).thenReturn("ocn,ocm");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(1);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        List<VariableField> dataFields = record.getVariableFields("035");
        assertTrue(CollectionUtils.isNotEmpty(dataFields));
        for (Iterator<VariableField> iterator = dataFields.iterator(); iterator.hasNext(); ) {
            DataField df = (DataField) iterator.next();
            Subfield sf = df.getSubfield('a');
            assertFalse(sf.getData().contains("035 "));
        }

        ControlField variableField = (ControlField) record.getVariableField("001");
        assertEquals(variableField.getData(), "123456789");
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


        ControlField controlField1 = getMarcRecordUtil().getMarcFactory().newControlField();
        controlField1.setTag("001");
        controlField1.setData("123456");
        record.addVariableField(controlField1);

        record.addVariableField(controlField1);

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1('1');
        dataField.setIndicator2('4');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("035 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("035 b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("050");
        dataField1.setIndicator1('1');
        dataField1.setIndicator2('4');

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('b');
        subfield3.setData("050 b");
        dataField1.addSubfield(subfield3);
        record.addVariableField(dataField1);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.PREPEND_WITH_PREFIX);
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer3.getDestDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer3.getDestInd1()).thenReturn("1");
        Mockito.when(mockBatchProfileDataTransformer3.getDestInd2()).thenReturn("4");
        Mockito.when(mockBatchProfileDataTransformer3.getDestSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn(OleNGConstants.PREPEND_WITH_PREFIX);
        Mockito.when(mockBatchProfileDataTransformer4.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer4.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer4.getDestDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(2);


        batchProfileDataTransformers.add(mockBatchProfileDataTransformer5);
        Mockito.when(mockBatchProfileDataTransformer5.getOperation()).thenReturn(OleNGConstants.PREPEND_WITH_PREFIX);
        Mockito.when(mockBatchProfileDataTransformer5.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer5.getDataField()).thenReturn("050");
        Mockito.when(mockBatchProfileDataTransformer5.getInd1()).thenReturn("1");
        Mockito.when(mockBatchProfileDataTransformer5.getInd2()).thenReturn("4");
        Mockito.when(mockBatchProfileDataTransformer5.getSubField()).thenReturn("b");
        Mockito.when(mockBatchProfileDataTransformer5.getDestDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer5.getStep()).thenReturn(3);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf035$a= getMarcRecordUtil().getDataFieldValueWithIndicators(record, "035" ,"1", "4", "a");
        assertTrue(StringUtils.equals(valueOf035$a,"(OCLOC)035 a"));
        System.out.println("035 $a : " + valueOf035$a);

        String valueOf035$b= getMarcRecordUtil().getDataFieldValueWithIndicators(record, "035" ,"1", "4", "b");
        assertTrue(StringUtils.equals(valueOf035$b,"035 b"));
        System.out.println("035 $b : " + valueOf035$b);

        String valueOf001= getMarcRecordUtil().getControlFieldValue(record,"001");
        assertEquals(valueOf001,"(OCLOC)123456");
        System.out.println("001 : " + valueOf001);

        String valueOf003= getMarcRecordUtil().getControlFieldValue(record,"003");
        assertEquals(valueOf003,"(050 b)OCLOC");
        System.out.println("003 : " + valueOf003);

    }

    @Test
    public void replaceTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();

        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        ControlField controlField = marcFactory.newControlField();
        controlField.setTag("003");
        controlField.setData("(OCLOC)123456");

        record.addVariableField(controlField);

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1('1');
        dataField.setIndicator2('4');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("035 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("035 b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.REPLACE);
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("OLE");
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn(OleNGConstants.REPLACE);
        Mockito.when(mockBatchProfileDataTransformer4.getConstant()).thenReturn("035 b-Modified");
        Mockito.when(mockBatchProfileDataTransformer4.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer4.getInd1()).thenReturn("1");
        Mockito.when(mockBatchProfileDataTransformer4.getInd2()).thenReturn("4");
        Mockito.when(mockBatchProfileDataTransformer4.getSubField()).thenReturn("b");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(2);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf003= getMarcRecordUtil().getControlFieldValue(record, "003");
        assertTrue(StringUtils.equals(valueOf003, "OLE"));
        System.out.println("033 : " + valueOf003);

        String valueOf035$a= getMarcRecordUtil().getDataFieldValueWithIndicators(record, "035", "1", "4", "a");
        assertTrue(StringUtils.equals(valueOf035$a,"035 a"));
        System.out.println("035 $a : " + valueOf035$a);

        String valueOf035$b= getMarcRecordUtil().getDataFieldValueWithIndicators(record, "035", "1", "4", "b");
        assertTrue(StringUtils.equals(valueOf035$b,"035 b-Modified"));
        System.out.println("035 $b : " + valueOf035$b);
    }




    @Test
    public void processSteps1() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();

        String rawMarcContent = FileUtils.readFileContent("org/kuali/ole/spring/batch/processor/InvYBP_Test_1207_2rec.mrc");
        List<Record> records = new MarcXMLConverter().convertRawMarchToMarc(rawMarcContent);
        assertTrue(CollectionUtils.isNotEmpty(records));
        Record mockMarcRecod = records.get(0);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();
        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn(OleNGConstants.PREPEND_WITH_PREFIX);
        Mockito.when(mockBatchProfileDataTransformer2.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getDestDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.ADD_FIELD);
        Mockito.when(mockBatchProfileDataTransformer1.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer1.getDestDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd1()).thenReturn("1");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd2()).thenReturn("4");
        Mockito.when(mockBatchProfileDataTransformer1.getDestSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(3);


        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn(OleNGConstants.REPLACE);
        Mockito.when(mockBatchProfileDataTransformer4.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer4.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(4);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer5);
        Mockito.when(mockBatchProfileDataTransformer5.getOperation()).thenReturn(OleNGConstants.REPLACE);
        Mockito.when(mockBatchProfileDataTransformer5.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer5.getConstant()).thenReturn("");
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
        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn(OleNGConstants.PREPEND_WITH_PREFIX);
        Mockito.when(mockBatchProfileDataTransformer2.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getDestDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.ADD_FIELD);
        Mockito.when(mockBatchProfileDataTransformer1.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer1.getDestDataField()).thenReturn("060");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd1()).thenReturn("1");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd2()).thenReturn("4");
        Mockito.when(mockBatchProfileDataTransformer1.getDestSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(3);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn(OleNGConstants.REPLACE);
        Mockito.when(mockBatchProfileDataTransformer4.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(4);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer5);
        Mockito.when(mockBatchProfileDataTransformer5.getOperation()).thenReturn(OleNGConstants.REPLACE);
        Mockito.when(mockBatchProfileDataTransformer5.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer5.getStep()).thenReturn(5);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(mockMarcRecod,mockBatchProcessProfile);

        String valueOf001 = getMarcRecordUtil().getControlFieldValue(mockMarcRecod, "001");
        assertTrue(StringUtils.isBlank(valueOf001));
        System.out.println("001 : " + valueOf001);

        String valueOf003 = getMarcRecordUtil().getControlFieldValue(mockMarcRecod, "003");
        assertTrue(StringUtils.isBlank(valueOf003));
        System.out.println("003 : " + valueOf003);

        String valueOf060 = getMarcRecordUtil().getDataFieldValueWithIndicators(mockMarcRecod, "060","1", "4", "a");
        assertTrue(StringUtils.isNotBlank(valueOf060));
        System.out.println("060 : " + valueOf060);
    }

    @Test
    public void processWithNewSubField() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();

        String rawMarcContent = FileUtils.readFileContent("org/kuali/ole/spring/batch/processor/InvYBP_Test_1207_2rec.mrc");
        List<Record> records = new MarcXMLConverter().convertRawMarchToMarc(rawMarcContent);
        assertTrue(CollectionUtils.isNotEmpty(records));
        Record mockMarcRecod = records.get(0);

        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();
        batchProfileDataTransformers.add(mockBatchProfileDataTransformer3);
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer2);
        Mockito.when(mockBatchProfileDataTransformer2.getOperation()).thenReturn(OleNGConstants.PREPEND_WITH_PREFIX);
        Mockito.when(mockBatchProfileDataTransformer2.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer2.getDestDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer2.getStep()).thenReturn(2);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer1);
        Mockito.when(mockBatchProfileDataTransformer1.getOperation()).thenReturn(OleNGConstants.ADD_SUBFIELD);
        Mockito.when(mockBatchProfileDataTransformer1.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer1.getDestDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer1.getDestInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer1.getDestSubField()).thenReturn("z");
        Mockito.when(mockBatchProfileDataTransformer1.getStep()).thenReturn(3);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn(OleNGConstants.REPLACE);
        Mockito.when(mockBatchProfileDataTransformer4.getDataField()).thenReturn("001");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(4);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer5);
        Mockito.when(mockBatchProfileDataTransformer5.getOperation()).thenReturn(OleNGConstants.REPLACE);
        Mockito.when(mockBatchProfileDataTransformer5.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer5.getStep()).thenReturn(5);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(mockMarcRecod,mockBatchProcessProfile);

        String valueOf001 = getMarcRecordUtil().getControlFieldValue(mockMarcRecod, "001");
        assertTrue(StringUtils.isBlank(valueOf001));
        System.out.println("001 : " + valueOf001);

        String valueOf003 = getMarcRecordUtil().getControlFieldValue(mockMarcRecod, "003");
        assertTrue(StringUtils.isBlank(valueOf003));
        System.out.println("003 : " + valueOf003);

        List<VariableField> dataFields035 = mockMarcRecod.getVariableFields("035");
        assertTrue(CollectionUtils.isNotEmpty(dataFields035) && dataFields035.size() == 1);
        DataField dataField035 = (DataField) mockMarcRecod.getVariableField("035");
        List<Subfield> subfields = dataField035.getSubfields('a');
        assertTrue(CollectionUtils.isNotEmpty(subfields) && subfields.size() == 1);
        List<Subfield> subfields$z = dataField035.getSubfields('z');
        assertTrue(CollectionUtils.isNotEmpty(subfields$z) && subfields$z.size() == 1);


        String valueOf035 = getMarcRecordUtil().getDataFieldValueWithIndicators(mockMarcRecod, "035"," ", " ", "z");
        assertTrue(StringUtils.isNotBlank(valueOf035));
        System.out.println("035 : " + valueOf035);
    }

    @Test
    public void removeValueTest() throws Exception, IOException {
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
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer3.getInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer3.getInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer3.getSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer4.getConstant()).thenReturn("ocm,ocn");
        Mockito.when(mockBatchProfileDataTransformer4.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer4.getInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer4.getInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer4.getSubField()).thenReturn("b");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(1);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf035$a= getMarcRecordUtil().getDataFieldValue(record, "035 $a");
        assertFalse(StringUtils.contains(valueOf035$a,"ocn"));
        System.out.println("035 $a : " + valueOf035$a);

        String valueOf035$b= getMarcRecordUtil().getDataFieldValue(record, "035 $b");
        assertFalse(StringUtils.contains(valueOf035$b,"ocm"));
        System.out.println("035 $b : " + valueOf035$b);
    }



    @Test
    public void deleteFieldTest2() throws Exception, IOException {
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
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.DELETE_FIELD);
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);

        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn(OleNGConstants.DELETE_FIELD);
        Mockito.when(mockBatchProfileDataTransformer4.getConstant()).thenReturn("");
        Mockito.when(mockBatchProfileDataTransformer4.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(1);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        ControlField controlField003 = (ControlField) record.getVariableField("003");
        assertNull(controlField003);

        DataField dataField1 = (DataField) record.getVariableField("035");
        assertNull(dataField1);
    }

    @Test
    public void deleteValueTest() throws Exception, IOException {
        StepsProcessor stepsProcessor = new StepsProcessor();

        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        ControlField controlField = marcFactory.newControlField();
        controlField.setTag("003");
        controlField.setData("(OCLOC)003 Value");

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
        Mockito.when(mockBatchProfileDataTransformer3.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer3.getConstant()).thenReturn("(OCLOC)");
        Mockito.when(mockBatchProfileDataTransformer3.getDataField()).thenReturn("003");
        Mockito.when(mockBatchProfileDataTransformer3.getStep()).thenReturn(1);



        batchProfileDataTransformers.add(mockBatchProfileDataTransformer4);
        Mockito.when(mockBatchProfileDataTransformer4.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer4.getConstant()).thenReturn("Value for ");
        Mockito.when(mockBatchProfileDataTransformer4.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer4.getInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer4.getInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer4.getSubField()).thenReturn("a");
        Mockito.when(mockBatchProfileDataTransformer4.getStep()).thenReturn(2);


        batchProfileDataTransformers.add(mockBatchProfileDataTransformer5);
        Mockito.when(mockBatchProfileDataTransformer5.getOperation()).thenReturn(OleNGConstants.REMOVE_VALUE);
        Mockito.when(mockBatchProfileDataTransformer5.getConstant()).thenReturn("Value for ");
        Mockito.when(mockBatchProfileDataTransformer5.getDataField()).thenReturn("035");
        Mockito.when(mockBatchProfileDataTransformer5.getInd1()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer5.getInd2()).thenReturn(" ");
        Mockito.when(mockBatchProfileDataTransformer5.getSubField()).thenReturn("b");
        Mockito.when(mockBatchProfileDataTransformer5.getStep()).thenReturn(3);

        Mockito.when(mockBatchProcessProfile.getBatchProfileDataTransformerList()).thenReturn(batchProfileDataTransformers);

        stepsProcessor.processSteps(record,mockBatchProcessProfile);

        String valueOf003= getMarcRecordUtil().getControlFieldValue(record, "003");
        assertTrue(StringUtils.equals(valueOf003, "003 Value"));

        String valueOf035$a= getMarcRecordUtil().getDataFieldValue(record, "035 $a");
        assertTrue(StringUtils.equals(valueOf035$a,"035 a"));
        System.out.println("035 $a : " + valueOf035$a);

        String valueOf035$b= getMarcRecordUtil().getDataFieldValue(record, "035 $b");
        assertTrue(StringUtils.equals(valueOf035$b,"035 b"));
        System.out.println("035 $b : " + valueOf035$b);
    }

    public MarcRecordUtil getMarcRecordUtil() {
        if(null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }
}