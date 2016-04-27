package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.OleNgBatchResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.ole.oleng.describe.processor.bibimport.MatchPointProcessor;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 12/22/15.
 */
public class BatchBibFileProcessorTest {

    @Mock
    private Record mockRecord;

    @Mock
    private BatchProcessProfile mockBatchProcesProfile;

    @Mock
    private BatchProfileDataMapping mockDataProfileMapping1;

    @Mock
    private BatchProfileDataMapping mockDataProfileMapping2;

    @Mock
    private BatchProfileDataMapping mockDataProfileMapping3;

    @Mock
    private BatchProfileDataMapping mockDataProfileMapping4;

    @Mock
    private BatchProfileMatchPoint mockMatchProfileMatchPoint1;

    @Mock
    private BatchProfileMatchPoint mockMatchProfileMatchPoint2;

    @Mock
    private BatchProfileMatchPoint mockMatchProfileMatchPoint3;

    @Mock
    private BatchProfileMatchPoint mockMatchProfileMatchPoint4;

    @Mock
    private MarcRecordUtil marcRecordUtil;

    @Mock
    BatchProcessTxObject mockBatchProcessTxObject;

    @Mock
    BatchFileProcessor mockBatchFileProcessor;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processRecords() throws Exception {
        List<Record> records = new ArrayList<>();
        records.add(mockRecord);

        String rawContent = new MarcRecordUtil().convertMarcRecordListToRawMarcContent(records);

        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        String reportDirectory = OleNGConstants.QUICK_LAUNCH + OleNGConstants.DATE_FORMAT.format(new Date());
        Map<Integer, RecordDetails> recordDetailsMap = new HashMap<>();
        for(int index = 0; index < records.size(); index++){
            RecordDetails recordDetails = new RecordDetails();
            recordDetails.setRecord(records.get(index + 1));
            recordDetailsMap.put(index, recordDetails);
        }

        JSONObject resposne = new JSONObject();
        resposne.put("status", "success");
        Mockito.when(mockBatchFileProcessor.processBatch(new File(""), OleNGConstants.MARC, "1","",new BatchJobDetails())).thenReturn(resposne);
        OleNgBatchResponse oleNgBatchResponse = batchBibFileProcessor.processRecords(recordDetailsMap,mockBatchProcessTxObject,mockBatchProcesProfile);
        String processedRecords = oleNgBatchResponse.toString();
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
        List overlayOps = batchBibFileProcessor.getOverlayOps(mockBatchProcesProfile);
        assertTrue(CollectionUtils.isNotEmpty(overlayOps));
        System.out.println(overlayOps);
    }

    @Test
    public void prepareDataMappingValuesWithOneMappingRow() throws Exception, JSONException {
        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();
        batchFileProcessor.setMarcRecordUtil(marcRecordUtil);
        ArrayList<BatchProfileDataMapping> profileDataMappings = new ArrayList<>();

        Mockito.when(mockDataProfileMapping1.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping1.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping1.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping1.getDataField()).thenReturn("050");
        Mockito.when(mockDataProfileMapping1.getSubField()).thenReturn("a");
        Mockito.when(mockDataProfileMapping1.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping1.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping1);


        Mockito.when(mockBatchProcesProfile.getBatchProfileDataMappingList()).thenReturn(profileDataMappings);

        Mockito.when(marcRecordUtil.getDataFieldValueWithIndicators(mockRecord, "050", null, null, "a")).thenReturn("123");

        List<JSONObject> bibDataMappings = batchFileProcessor.prepareDataMappings(Collections.singletonList(mockRecord), mockBatchProcesProfile, "holdings",  "pre marc transformation",false);
        assertTrue(CollectionUtils.isNotEmpty(bibDataMappings));

        assertEquals(bibDataMappings.get(0).get("Call Number").toString(), "[\"123\"]");

    }


    @Test
    public void prepareDataMappingValuesWithMultipleMappingRowsWithSamePriority() throws Exception, JSONException {
        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();
        batchFileProcessor.setMarcRecordUtil(marcRecordUtil);
        ArrayList<BatchProfileDataMapping> profileDataMappings = new ArrayList<>();

        Mockito.when(mockDataProfileMapping1.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping1.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping1.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping1.getDataField()).thenReturn("050");
        Mockito.when(mockDataProfileMapping1.getSubField()).thenReturn("a");
        Mockito.when(mockDataProfileMapping1.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping1.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping1);


        Mockito.when(mockDataProfileMapping2.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping2.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping2.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping2.getDataField()).thenReturn("050");
        Mockito.when(mockDataProfileMapping2.getSubField()).thenReturn("b");
        Mockito.when(mockDataProfileMapping2.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping2.getPriority()).thenReturn(1);



        profileDataMappings.add(mockDataProfileMapping2);

        Mockito.when(mockBatchProcesProfile.getBatchProfileDataMappingList()).thenReturn(profileDataMappings);

        Mockito.when(marcRecordUtil.getDataFieldValueWithIndicators(mockRecord, "050", null, null,  "a")).thenReturn("123");
        Mockito.when(marcRecordUtil.getDataFieldValueWithIndicators(mockRecord, "050", null, null,  "b")).thenReturn("213");

        List<JSONObject> bibDataMappings = batchFileProcessor.prepareDataMappings(Collections.singletonList(mockRecord), mockBatchProcesProfile, "holdings",  "pre marc transformation",false);
        assertTrue(CollectionUtils.isNotEmpty(bibDataMappings));
        assertEquals(bibDataMappings.get(0).get("Call Number").toString(), "[\"123 213\"]");


    }

    @Test
    public void prepareDataMappingValuesWithMultipleMappingRowsWithDifferentPriority() throws Exception, JSONException {
        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();
        batchFileProcessor.setMarcRecordUtil(marcRecordUtil);
        ArrayList<BatchProfileDataMapping> profileDataMappings = new ArrayList<>();

        Mockito.when(mockDataProfileMapping1.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping1.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping1.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping1.getDataField()).thenReturn("050");
        Mockito.when(mockDataProfileMapping1.getSubField()).thenReturn("a");
        Mockito.when(mockDataProfileMapping1.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping1.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping1);


        Mockito.when(mockDataProfileMapping2.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping2.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping2.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping2.getDataField()).thenReturn("050");
        Mockito.when(mockDataProfileMapping2.getSubField()).thenReturn("b");
        Mockito.when(mockDataProfileMapping2.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping2.getPriority()).thenReturn(2);



        profileDataMappings.add(mockDataProfileMapping2);

        Mockito.when(mockBatchProcesProfile.getBatchProfileDataMappingList()).thenReturn(profileDataMappings);

        Mockito.when(marcRecordUtil.getDataFieldValueWithIndicators(mockRecord, "050", null, null,  "a")).thenReturn("123");
        Mockito.when(marcRecordUtil.getDataFieldValueWithIndicators(mockRecord, "050", null, null,  "b")).thenReturn("213");

        List<JSONObject> bibDataMappings = batchFileProcessor.prepareDataMappings(Collections.singletonList(mockRecord), mockBatchProcesProfile, "holdings",  "pre marc transformation",false);
        assertTrue(CollectionUtils.isNotEmpty(bibDataMappings));
        assertEquals(bibDataMappings.get(0).get("Call Number").toString(), "[\"123\"]");
    }


 @Test
    public void prepareDataMappingValuesWithMultipleMappingRowsWithDifferentPriority1() throws Exception, JSONException {
        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();
        batchFileProcessor.setMarcRecordUtil(marcRecordUtil);
        ArrayList<BatchProfileDataMapping> profileDataMappings = new ArrayList<>();

        Mockito.when(mockDataProfileMapping1.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping1.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping1.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping1.getDataField()).thenReturn("050");
        Mockito.when(mockDataProfileMapping1.getSubField()).thenReturn("a");
        Mockito.when(mockDataProfileMapping1.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping1.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping1);


        Mockito.when(mockDataProfileMapping2.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping2.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping2.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping2.getDataField()).thenReturn("050");
        Mockito.when(mockDataProfileMapping2.getSubField()).thenReturn("b");
        Mockito.when(mockDataProfileMapping2.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping2.getPriority()).thenReturn(2);



        profileDataMappings.add(mockDataProfileMapping2);


        Mockito.when(mockDataProfileMapping3.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping3.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping3.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping3.getDataType()).thenReturn("constant");
        Mockito.when(mockDataProfileMapping3.getConstant()).thenReturn("12321");
        Mockito.when(mockDataProfileMapping3.getPriority()).thenReturn(1);


        profileDataMappings.add(mockDataProfileMapping3);

        Mockito.when(mockBatchProcesProfile.getBatchProfileDataMappingList()).thenReturn(profileDataMappings);

        Mockito.when(marcRecordUtil.getDataFieldValueWithIndicators(mockRecord, "050", null, null, "a")).thenReturn("123");
        Mockito.when(marcRecordUtil.getDataFieldValueWithIndicators(mockRecord, "050", null, null, "b")).thenReturn("213");

        List<JSONObject> bibDataMappings = batchFileProcessor.prepareDataMappings(Collections.singletonList(mockRecord), mockBatchProcesProfile, "holdings",  "pre marc transformation",false);
        assertTrue(CollectionUtils.isNotEmpty(bibDataMappings));
     assertEquals(bibDataMappings.get(0).get("Call Number").toString(), "[\"123 12321\"]");
    }

    @Test
    public void prepareDataMappingValuesWithMultipleMappingRowsWithDifferentPriority2() throws Exception, JSONException {
        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();
        batchFileProcessor.setMarcRecordUtil(marcRecordUtil);
        ArrayList<BatchProfileDataMapping> profileDataMappings = new ArrayList<>();

        Mockito.when(mockDataProfileMapping1.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping1.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping1.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping1.getDataField()).thenReturn("050");
        Mockito.when(mockDataProfileMapping1.getSubField()).thenReturn("a");
        Mockito.when(mockDataProfileMapping1.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping1.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping1);


        Mockito.when(mockDataProfileMapping2.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping2.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping2.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping2.getDataField()).thenReturn("050");
        Mockito.when(mockDataProfileMapping2.getSubField()).thenReturn("b");
        Mockito.when(mockDataProfileMapping2.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping2.getPriority()).thenReturn(2);



        profileDataMappings.add(mockDataProfileMapping2);


        Mockito.when(mockDataProfileMapping3.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping3.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping3.getField()).thenReturn("Call Number");
        Mockito.when(mockDataProfileMapping3.getDataType()).thenReturn("constant");
        Mockito.when(mockDataProfileMapping3.getConstant()).thenReturn("12321");
        Mockito.when(mockDataProfileMapping3.getPriority()).thenReturn(1);


        profileDataMappings.add(mockDataProfileMapping3);

        Mockito.when(mockBatchProcesProfile.getBatchProfileDataMappingList()).thenReturn(profileDataMappings);

        Mockito.when(marcRecordUtil.getDataFieldValueWithIndicators(mockRecord, "050", null, null, "a")).thenReturn("213");

        List<JSONObject> bibDataMappings = batchFileProcessor.prepareDataMappings(Collections.singletonList(mockRecord), mockBatchProcesProfile, "holdings",  "pre marc transformation",false);
        assertTrue(CollectionUtils.isNotEmpty(bibDataMappings));
        assertEquals(bibDataMappings.get(0).get("Call Number").toString(), "[\"213 12321\"]");
    }

    @Test
    public void multiValueTestt() throws Exception, JSONException {
        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();

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
        subfield.setData("Value for 035-1 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("Value for 035-1 b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("035");
        dataField1.setIndicator1(' ');
        dataField1.setIndicator2(' ');

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('a');
        subfield3.setData("Value for 035-1 a");
        dataField1.addSubfield(subfield3);

        Subfield subfield4 = marcFactory.newSubfield();
        subfield4.setCode('b');
        subfield4.setData("Value for 035-1 b");
        dataField1.addSubfield(subfield4);

        record.addVariableField(dataField1);

        ArrayList<BatchProfileDataMapping> profileDataMappings = new ArrayList<>();

        Mockito.when(mockDataProfileMapping1.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping1.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping1.getField()).thenReturn("Call Number Type");
        Mockito.when(mockDataProfileMapping1.getDataField()).thenReturn("003");
        Mockito.when(mockDataProfileMapping1.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping1.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping1);


        Mockito.when(mockDataProfileMapping2.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping2.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping2.getField()).thenReturn("Public Note");
        Mockito.when(mockDataProfileMapping2.getDataField()).thenReturn("035");
        Mockito.when(mockDataProfileMapping2.getSubField()).thenReturn("b");
        Mockito.when(mockDataProfileMapping2.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping2.getPriority()).thenReturn(1);
        Mockito.when(mockDataProfileMapping2.isMultiValue()).thenReturn(true);



        profileDataMappings.add(mockDataProfileMapping2);


        Mockito.when(mockDataProfileMapping3.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping3.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping3.getField()).thenReturn("Copy Number");
        Mockito.when(mockDataProfileMapping3.getDataType()).thenReturn("constant");
        Mockito.when(mockDataProfileMapping3.getConstant()).thenReturn("12321");
        Mockito.when(mockDataProfileMapping3.getPriority()).thenReturn(1);


        profileDataMappings.add(mockDataProfileMapping3);


        Mockito.when(mockBatchProcesProfile.getBatchProfileDataMappingList()).thenReturn(profileDataMappings);

       // Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "b")).thenReturn("213");

        List<JSONObject> bibDataMappings = batchFileProcessor.prepareDataMappings(Collections.singletonList(record), mockBatchProcesProfile, "holdings",  "pre marc transformation",false);
        assertTrue(CollectionUtils.isNotEmpty(bibDataMappings));
        System.out.println(bibDataMappings);
    }
    @Test
    public void multiValueTest2() throws Exception, JSONException {
        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();

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
        subfield.setData("Value for 035-1 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("Value for 035-1 b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("035");
        dataField1.setIndicator1(' ');
        dataField1.setIndicator2(' ');

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('a');
        subfield3.setData("Value for 035-1 a");
        dataField1.addSubfield(subfield3);

        Subfield subfield4 = marcFactory.newSubfield();
        subfield4.setCode('b');
        subfield4.setData("Value for 035-1 b");
        dataField1.addSubfield(subfield4);

        record.addVariableField(dataField1);

        ArrayList<BatchProfileDataMapping> profileDataMappings = new ArrayList<>();

        Mockito.when(mockDataProfileMapping1.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping1.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping1.getField()).thenReturn("Call Number Type");
        Mockito.when(mockDataProfileMapping1.getDataField()).thenReturn("003");
        Mockito.when(mockDataProfileMapping1.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping1.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping1);


        Mockito.when(mockDataProfileMapping2.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping2.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping2.getField()).thenReturn("Public Note");
        Mockito.when(mockDataProfileMapping2.getDataField()).thenReturn("035");
        Mockito.when(mockDataProfileMapping2.getSubField()).thenReturn("b");
        Mockito.when(mockDataProfileMapping2.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping2.getPriority()).thenReturn(1);
        Mockito.when(mockDataProfileMapping2.isMultiValue()).thenReturn(true);

        profileDataMappings.add(mockDataProfileMapping2);

        Mockito.when(mockDataProfileMapping4.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping4.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping4.getField()).thenReturn("Public Note");
        Mockito.when(mockDataProfileMapping4.getDataField()).thenReturn("035");
        Mockito.when(mockDataProfileMapping4.getSubField()).thenReturn("a");
        Mockito.when(mockDataProfileMapping4.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping4.getPriority()).thenReturn(1);
        Mockito.when(mockDataProfileMapping4.isMultiValue()).thenReturn(true);
        profileDataMappings.add(mockDataProfileMapping4);


        Mockito.when(mockDataProfileMapping3.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping3.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping3.getField()).thenReturn("Copy Number");
        Mockito.when(mockDataProfileMapping3.getDataType()).thenReturn("constant");
        Mockito.when(mockDataProfileMapping3.getConstant()).thenReturn("12321");
        Mockito.when(mockDataProfileMapping3.getPriority()).thenReturn(1);


        profileDataMappings.add(mockDataProfileMapping3);


        Mockito.when(mockBatchProcesProfile.getBatchProfileDataMappingList()).thenReturn(profileDataMappings);

        Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "b")).thenReturn("213");

        List<JSONObject> bibDataMappings = batchFileProcessor.prepareDataMappings(Collections.singletonList(record), mockBatchProcesProfile, "holdings",  "pre marc transformation",false);
        assertTrue(CollectionUtils.isNotEmpty(bibDataMappings));
        System.out.println(bibDataMappings);
    }


    @Test
    public void nonMultiValueWithMultipleFields() throws Exception, JSONException {
        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();

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
        subfield.setData("Value for 035-1 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("Value for 035-1 b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("035");
        dataField1.setIndicator1(' ');
        dataField1.setIndicator2(' ');

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('a');
        subfield3.setData("Value for 035-1 a");
        dataField1.addSubfield(subfield3);

        Subfield subfield4 = marcFactory.newSubfield();
        subfield4.setCode('b');
        subfield4.setData("Value for 035-1 b");
        dataField1.addSubfield(subfield4);

        record.addVariableField(dataField1);

        ArrayList<BatchProfileDataMapping> profileDataMappings = new ArrayList<>();

        Mockito.when(mockDataProfileMapping1.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping1.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping1.getField()).thenReturn("Call Number Type");
        Mockito.when(mockDataProfileMapping1.getDataField()).thenReturn("003");
        Mockito.when(mockDataProfileMapping1.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping1.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping1);


        Mockito.when(mockDataProfileMapping2.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping2.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping2.getField()).thenReturn("Public Note");
        Mockito.when(mockDataProfileMapping2.getDataField()).thenReturn("035");
        Mockito.when(mockDataProfileMapping2.getSubField()).thenReturn("b");
        Mockito.when(mockDataProfileMapping2.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping2.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping2);

        Mockito.when(mockDataProfileMapping4.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping4.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping4.getField()).thenReturn("Public Note");
        Mockito.when(mockDataProfileMapping4.getDataField()).thenReturn("035");
        Mockito.when(mockDataProfileMapping4.getSubField()).thenReturn("a");
        Mockito.when(mockDataProfileMapping4.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping4.getPriority()).thenReturn(1);
        profileDataMappings.add(mockDataProfileMapping4);


        Mockito.when(mockDataProfileMapping3.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping3.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping3.getField()).thenReturn("Copy Number");
        Mockito.when(mockDataProfileMapping3.getDataType()).thenReturn("constant");
        Mockito.when(mockDataProfileMapping3.getConstant()).thenReturn("12321");
        Mockito.when(mockDataProfileMapping3.getPriority()).thenReturn(1);


        profileDataMappings.add(mockDataProfileMapping3);


        Mockito.when(mockBatchProcesProfile.getBatchProfileDataMappingList()).thenReturn(profileDataMappings);

        List<JSONObject> bibDataMappings = batchFileProcessor.prepareDataMappings(Collections.singletonList(record), mockBatchProcesProfile, "holdings",  "pre marc transformation",false);
        assertTrue(CollectionUtils.isNotEmpty(bibDataMappings));
        System.out.println(bibDataMappings);
    }

    @Test
    public void testAddMatchPointToDataMapping() throws Exception, JSONException {
        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();

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
        subfield.setData("Value for 035-1 a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("Value for 035-1 b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("035");
        dataField1.setIndicator1(' ');
        dataField1.setIndicator2(' ');

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('a');
        subfield3.setData("Value for 035-1 a");
        dataField1.addSubfield(subfield3);

        Subfield subfield4 = marcFactory.newSubfield();
        subfield4.setCode('b');
        subfield4.setData("Value for 035-1 b");
        dataField1.addSubfield(subfield4);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1(' ');
        dataField2.setIndicator2(' ');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('y');
        subfield5.setData("Link Text 1");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('3');
        subfield6.setData("Link Text 2");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        ArrayList<BatchProfileDataMapping> profileDataMappings = new ArrayList<>();

        Mockito.when(mockDataProfileMapping1.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping1.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping1.getField()).thenReturn("Call Number Type");
        Mockito.when(mockDataProfileMapping1.getDataField()).thenReturn("003");
        Mockito.when(mockDataProfileMapping1.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping1.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping1);


        Mockito.when(mockDataProfileMapping2.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping2.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping2.getField()).thenReturn("Public Note");
        Mockito.when(mockDataProfileMapping2.getDataField()).thenReturn("035");
        Mockito.when(mockDataProfileMapping2.getSubField()).thenReturn("b");
        Mockito.when(mockDataProfileMapping2.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping2.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping2);

        Mockito.when(mockDataProfileMapping4.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping4.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping4.getField()).thenReturn("Public Note");
        Mockito.when(mockDataProfileMapping4.getDataField()).thenReturn("035");
        Mockito.when(mockDataProfileMapping4.getSubField()).thenReturn("a");
        Mockito.when(mockDataProfileMapping4.getDataType()).thenReturn("bib marc");
        Mockito.when(mockDataProfileMapping4.getPriority()).thenReturn(1);
        profileDataMappings.add(mockDataProfileMapping4);


        Mockito.when(mockDataProfileMapping3.getTransferOption()).thenReturn("Pre Marc Transformation");
        Mockito.when(mockDataProfileMapping3.getDestination()).thenReturn("holdings");
        Mockito.when(mockDataProfileMapping3.getField()).thenReturn("Copy Number");
        Mockito.when(mockDataProfileMapping3.getDataType()).thenReturn("constant");
        Mockito.when(mockDataProfileMapping3.getConstant()).thenReturn("12321");
        Mockito.when(mockDataProfileMapping3.getPriority()).thenReturn(1);

        profileDataMappings.add(mockDataProfileMapping3);


        ArrayList<BatchProfileMatchPoint> batchProfileMatchPoints = new ArrayList<>();
        batchProfileMatchPoints.add(mockMatchProfileMatchPoint1);
        Mockito.when(mockMatchProfileMatchPoint1.getDataType()).thenReturn("Holdings");
        Mockito.when(mockMatchProfileMatchPoint1.getMatchPointType()).thenReturn("Link Text");
        Mockito.when(mockMatchProfileMatchPoint1.getDataField()).thenReturn("856");
        Mockito.when(mockMatchProfileMatchPoint1.getSubField()).thenReturn("y");

        batchProfileMatchPoints.add(mockMatchProfileMatchPoint2);
        Mockito.when(mockMatchProfileMatchPoint2.getDataType()).thenReturn("Holdings");
        Mockito.when(mockMatchProfileMatchPoint2.getMatchPointType()).thenReturn("Link Text");
        Mockito.when(mockMatchProfileMatchPoint2.getDataField()).thenReturn("856");
        Mockito.when(mockMatchProfileMatchPoint2.getSubField()).thenReturn("3");

        Mockito.when(mockBatchProcesProfile.getBatchProfileDataMappingList()).thenReturn(profileDataMappings);
        Mockito.when(mockBatchProcesProfile.getBatchProfileMatchPointList()).thenReturn(batchProfileMatchPoints);

        batchFileProcessor.setMatchPointProcessor(new MatchPointProcessor());
        List<JSONObject> bibDataMappings = batchFileProcessor.prepareDataMappings(Collections.singletonList(record), mockBatchProcesProfile, "holdings",  "pre marc transformation",true);
        assertTrue(CollectionUtils.isNotEmpty(bibDataMappings));
        System.out.println(bibDataMappings);
    }

    /*This test case for prepare matchpoint.*/
    @Test
    public void prepareMatchPoint() throws Exception, IOException, JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("025");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("Value for 025 a");
        dataField.addSubfield(subfield);

        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("050");
        dataField1.setIndicator1(' ');
        dataField1.setIndicator2(' ');

        Subfield subfield1 = marcFactory.newSubfield();
        subfield1.setCode('a');
        subfield1.setData("Value for 050 a");
        dataField1.addSubfield(subfield1);

        record.addVariableField(dataField1);

        ArrayList<BatchProfileMatchPoint> batchProfileMatchPoints = new ArrayList<>();

        batchProfileMatchPoints.add(mockMatchProfileMatchPoint1);
        Mockito.when(mockMatchProfileMatchPoint1.getDataType()).thenReturn("Holdings");
        Mockito.when(mockMatchProfileMatchPoint1.getMatchPointType()).thenReturn("Call Number");
        Mockito.when(mockMatchProfileMatchPoint1.getMatchPointValue()).thenReturn("CallNumber-123456");
        Mockito.when(mockMatchProfileMatchPoint1.getDataField()).thenReturn("025");
        Mockito.when(mockMatchProfileMatchPoint1.getSubField()).thenReturn("a");
        Mockito.when(mockMatchProfileMatchPoint1.getConstant()).thenReturn("Constant Value 1");

        batchProfileMatchPoints.add(mockMatchProfileMatchPoint2);
        Mockito.when(mockMatchProfileMatchPoint2.getDataType()).thenReturn("Holdings");
        Mockito.when(mockMatchProfileMatchPoint2.getMatchPointType()).thenReturn("Copy Number");
        Mockito.when(mockMatchProfileMatchPoint2.getDataField()).thenReturn("050");
        Mockito.when(mockMatchProfileMatchPoint2.getSubField()).thenReturn("a");
        Mockito.when(mockMatchProfileMatchPoint2.getConstant()).thenReturn("Constant Value 2");

        batchProfileMatchPoints.add(mockMatchProfileMatchPoint3);
        Mockito.when(mockMatchProfileMatchPoint3.getDataType()).thenReturn("Holdings");
        Mockito.when(mockMatchProfileMatchPoint3.getMatchPointType()).thenReturn("Location Level 1");
        Mockito.when(mockMatchProfileMatchPoint3.getConstant()).thenReturn("Constant Value 3");

        batchProfileMatchPoints.add(mockMatchProfileMatchPoint4);
        Mockito.when(mockMatchProfileMatchPoint4.getDataType()).thenReturn("Holdings");
        Mockito.when(mockMatchProfileMatchPoint4.getMatchPointType()).thenReturn("Copy Number Type");
        Mockito.when(mockMatchProfileMatchPoint4.getDataField()).thenReturn("090");
        Mockito.when(mockMatchProfileMatchPoint4.getSubField()).thenReturn("a");
        Mockito.when(mockMatchProfileMatchPoint4.getConstant()).thenReturn("Constant Value 4");

        Mockito.when(mockBatchProcesProfile.getBatchProfileMatchPointList()).thenReturn(batchProfileMatchPoints);

        MatchPointProcessor matchPointProcessor = new MatchPointProcessor();
        JSONObject jsonObject = matchPointProcessor.prepareMatchPointsForHoldings(record, mockBatchProcesProfile);
        assertNotNull(jsonObject);
        System.out.println(jsonObject.toString());

    }

    @Test
    public void getDataFieldValueWithIndicatorTest() {
        MarcRecordUtil marcRecordUtil = new MarcRecordUtil();
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("025");
        dataField.setIndicator1('1');
        dataField.setIndicator2('2');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("Value for 025 a");
        dataField.addSubfield(subfield);

        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("035");
        dataField1.setIndicator1('1');
        dataField1.setIndicator2('2');

        Subfield subfield1 = marcFactory.newSubfield();
        subfield1.setCode('a');
        subfield1.setData("Value for 035 a");
        dataField1.addSubfield(subfield1);

        record.addVariableField(dataField1);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("050");
        dataField2.setIndicator1('1');
        dataField2.setIndicator2('2');

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('a');
        subfield2.setData("Value for 050 a");
        dataField2.addSubfield(subfield2);
        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('b');
        subfield3.setData("Value for 050 b");
        dataField2.addSubfield(subfield3);

        record.addVariableField(dataField2);

        String valueOf025$a = marcRecordUtil.getDataFieldValueWithIndicators(record, "025", "1", "2", "a");
        assertEquals(valueOf025$a, "Value for 025 a");
        System.out.println(valueOf025$a);

        String valueOf050$b = marcRecordUtil.getDataFieldValueWithIndicators(record, "050", "1", "2", "b");
        assertEquals(valueOf050$b, "Value for 050 b");
        System.out.println(valueOf050$b);
    }

    @Test
    public void getDataFieldValueWithIndicatorAndMultipleSubFieldTest() {
        MarcRecordUtil marcRecordUtil = new MarcRecordUtil();
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("035");
        dataField1.setIndicator1('1');
        dataField1.setIndicator2('2');

        Subfield subfield1 = marcFactory.newSubfield();
        subfield1.setCode('a');
        subfield1.setData("035 a-1");
        dataField1.addSubfield(subfield1);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('a');
        subfield2.setData("035 a-2");
        dataField1.addSubfield(subfield2);

        record.addVariableField(dataField1);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("035");
        dataField2.setIndicator1('1');
        dataField2.setIndicator2('2');

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('a');
        subfield3.setData("035 a-3");
        dataField2.addSubfield(subfield3);
        Subfield subfield4 = marcFactory.newSubfield();
        subfield4.setCode('b');
        subfield4.setData("035 b-1");
        dataField2.addSubfield(subfield4);

        record.addVariableField(dataField2);

        String valueOf035$a = marcRecordUtil.getDataFieldValueWithIndicators(record, "035", "1", "2", "a" );
        assertEquals(valueOf035$a,"035 a-1");
        System.out.println(valueOf035$a);
    }

    @Test
    public void testSplitRecordsByTagField() {

        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("856");
        dataField.setIndicator1('1');
        dataField.setIndicator2('2');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("Value for 856 a-1");
        dataField.addSubfield(subfield);

        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("856");
        dataField1.setIndicator1('1');
        dataField1.setIndicator2('2');

        Subfield subfield1 = marcFactory.newSubfield();
        subfield1.setCode('a');
        subfield1.setData("Value for 856 a-2");
        dataField1.addSubfield(subfield1);

        record.addVariableField(dataField1);

        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        MarcDataField marcDataField = new MarcDataField();
        marcDataField.setDataField("856");
        List<Record> records = batchBibFileProcessor.splitRecordByMultiValue(record, marcDataField);
        assertTrue(CollectionUtils.isNotEmpty(records));
        assertTrue(records.size() == 2);
    }


    @Test
    public void testSplitRecordsByTagFieldAndIndicators() {

        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("856");
        dataField.setIndicator1('1');
        dataField.setIndicator2('2');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('u');
        subfield.setData("http://www.someurl.com");
        dataField.addSubfield(subfield);


        Subfield subfield1 = marcFactory.newSubfield();
        subfield1.setCode('y');
        subfield1.setData("link text1");
        dataField.addSubfield(subfield1);



        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("856");
        dataField1.setIndicator1(' ');
        dataField1.setIndicator2('2');

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('u');
        subfield2.setData("http://www.someurl1.com");
        dataField1.addSubfield(subfield2);

        record.addVariableField(dataField1);

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('y');
        subfield3.setData("link text2");
        dataField1.addSubfield(subfield3);

        record.addVariableField(dataField1);

        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        MarcDataField marcDataField = new MarcDataField();
        marcDataField.setDataField("856");
        marcDataField.setInd1("1");
        marcDataField.setSubField("u");
        List<Record> records = batchBibFileProcessor.splitRecordByMultiValue(record, marcDataField);
        assertTrue(CollectionUtils.isNotEmpty(records));
        assertTrue(records.size() == 1);
    }


    @Test
    public void testSplitRecordsByTagFieldAndIndicators1() {

        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("856");
        dataField.setIndicator1('1');
        dataField.setIndicator2('2');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('u');
        subfield.setData("http://www.someurl.com");
        dataField.addSubfield(subfield);


        Subfield subfield1 = marcFactory.newSubfield();
        subfield1.setCode('y');
        subfield1.setData("link text1");
        dataField.addSubfield(subfield1);



        record.addVariableField(dataField);

        DataField dataField1 = marcFactory.newDataField();
        dataField1.setTag("856");
        dataField1.setIndicator1(' ');
        dataField1.setIndicator2('2');

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('u');
        subfield2.setData("http://www.someurl1.com");
        dataField1.addSubfield(subfield2);

        record.addVariableField(dataField1);

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('y');
        subfield3.setData("link text2");
        dataField1.addSubfield(subfield3);

        record.addVariableField(dataField1);

        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        MarcDataField marcDataField = new MarcDataField();
        marcDataField.setDataField("856");
        marcDataField.setSubField("u");
        List<Record> records = batchBibFileProcessor.splitRecordByMultiValue(record, marcDataField);
        assertTrue(CollectionUtils.isNotEmpty(records));
        assertTrue(records.size() == 3);
    }


    @Test
    public void testActionOps() {

        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        ArrayList<BatchProfileAddOrOverlay> batchProfileAddOrOverlays = new ArrayList<>();
        BatchProfileAddOrOverlay batchProfileAddOrOverlay = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay.setDataType(OleNGConstants.HOLDINGS);
        batchProfileAddOrOverlay.setMatchOption(OleNGConstants.IF_NOT_MATCH_FOUND);
        batchProfileAddOrOverlay.setOperation(OleNGConstants.ADD);
        batchProfileAddOrOverlay.setAddOperation(OleNGConstants.CREATE_MULTIPLE);
        batchProfileAddOrOverlay.setDataField("856");
        batchProfileAddOrOverlay.setInd1("0");
        batchProfileAddOrOverlay.setInd2("4");
        batchProfileAddOrOverlay.setSubField("y");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay2 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay2.setDataType(OleNGConstants.ITEM);
        batchProfileAddOrOverlay2.setMatchOption(OleNGConstants.IF_NOT_MATCH_FOUND);
        batchProfileAddOrOverlay2.setOperation(OleNGConstants.ADD);
        batchProfileAddOrOverlay2.setAddOperation(OleNGConstants.CREATE_MULTIPLE_DELETE_ALL_EXISTING);
        batchProfileAddOrOverlay2.setDataField("876");
        batchProfileAddOrOverlay2.setSubField("y");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay2);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay3 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay3.setDataType(OleNGConstants.EHOLDINGS);
        batchProfileAddOrOverlay3.setMatchOption(OleNGConstants.IF_NOT_MATCH_FOUND);
        batchProfileAddOrOverlay3.setOperation(OleNGConstants.ADD);
        batchProfileAddOrOverlay3.setAddOperation(OleNGConstants.CREATE_MULTIPLE_KEEP_ALL_EXISTING);
        batchProfileAddOrOverlay3.setDataField("856");
        batchProfileAddOrOverlay3.setInd1("0");
        batchProfileAddOrOverlay3.setInd2("4");
        batchProfileAddOrOverlay3.setSubField("u");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay3);

        Mockito.when(mockBatchProcesProfile.getBatchProfileAddOrOverlayList()).thenReturn(batchProfileAddOrOverlays);
        List overlayOps = batchBibFileProcessor.getActionOps(mockBatchProcesProfile);
        assertTrue(CollectionUtils.isNotEmpty(overlayOps));
        System.out.println(overlayOps);


    }

    @Test
    public void testAddedOps() {

        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        ArrayList<BatchProfileAddOrOverlay> batchProfileAddOrOverlays = new ArrayList<>();
        BatchProfileAddOrOverlay batchProfileAddOrOverlay = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay.setDataType(OleNGConstants.HOLDINGS);
        batchProfileAddOrOverlay.setMatchOption(OleNGConstants.IF_NOT_MATCH_FOUND);
        batchProfileAddOrOverlay.setOperation(OleNGConstants.ADD);
        batchProfileAddOrOverlay.setAddOperation(OleNGConstants.CREATE_MULTIPLE);
        batchProfileAddOrOverlay.setDataField("856");
        batchProfileAddOrOverlay.setInd1("0");
        batchProfileAddOrOverlay.setInd2("4");
        batchProfileAddOrOverlay.setSubField("y");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay2 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay2.setDataType(OleNGConstants.ITEM);
        batchProfileAddOrOverlay2.setMatchOption(OleNGConstants.IF_NOT_MATCH_FOUND);
        batchProfileAddOrOverlay2.setOperation(OleNGConstants.ADD);
        batchProfileAddOrOverlay2.setAddOperation(OleNGConstants.CREATE_MULTIPLE_DELETE_ALL_EXISTING);
        batchProfileAddOrOverlay2.setDataField("876");
        batchProfileAddOrOverlay2.setSubField("y");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay2);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay3 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay3.setDataType(OleNGConstants.EHOLDINGS);
        batchProfileAddOrOverlay3.setMatchOption(OleNGConstants.IF_NOT_MATCH_FOUND);
        batchProfileAddOrOverlay3.setOperation(OleNGConstants.ADD);
        batchProfileAddOrOverlay3.setAddOperation(OleNGConstants.CREATE_MULTIPLE_KEEP_ALL_EXISTING);
        batchProfileAddOrOverlay3.setDataField("856");
        batchProfileAddOrOverlay3.setInd1("0");
        batchProfileAddOrOverlay3.setInd2("4");
        batchProfileAddOrOverlay3.setSubField("u");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay3);

        Mockito.when(mockBatchProcesProfile.getBatchProfileAddOrOverlayList()).thenReturn(batchProfileAddOrOverlays);
        JSONObject addedOps = batchBibFileProcessor.getAddedOps(mockBatchProcesProfile);
        assertNotNull(addedOps);
        System.out.println(addedOps);


    }


}