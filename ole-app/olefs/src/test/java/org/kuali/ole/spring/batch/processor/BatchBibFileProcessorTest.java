package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.ole.oleng.describe.processor.bibimport.MatchPointProcessor;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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

    @Mock
    private BatchProfileDataMapping mockDataProfileMapping1;

    @Mock
    private BatchProfileDataMapping mockDataProfileMapping2;

    @Mock
    private BatchProfileDataMapping mockDataProfileMapping3;

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

        Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "a")).thenReturn("123");

        JSONObject bibDataMappings = batchFileProcessor.prepareDataMappings(mockRecord, mockBatchProcesProfile, "holdings", "pre marc transformation");
        assertNotNull(bibDataMappings);

        assertEquals(bibDataMappings.get("Call Number"), "123");

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

        Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "a")).thenReturn("123");
        Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "b")).thenReturn("213");

        JSONObject bibDataMappings = batchFileProcessor.prepareDataMappings(mockRecord, mockBatchProcesProfile, "holdings", "pre marc transformation");
        assertNotNull(bibDataMappings);
        assertEquals(bibDataMappings.get("Call Number"), "123 213");


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

        Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "a")).thenReturn("123");
        Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "b")).thenReturn("213");

        JSONObject bibDataMappings = batchFileProcessor.prepareDataMappings(mockRecord, mockBatchProcesProfile, "holdings", "pre marc transformation");
        assertNotNull(bibDataMappings);
        assertEquals(bibDataMappings.get("Call Number"), "123");
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

        Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "a")).thenReturn("123");
        Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "b")).thenReturn("213");

        JSONObject bibDataMappings = batchFileProcessor.prepareDataMappings(mockRecord, mockBatchProcesProfile, "holdings", "pre marc transformation");
        assertNotNull(bibDataMappings);
        assertEquals(bibDataMappings.get("Call Number"), "123 12321");
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

        Mockito.when(marcRecordUtil.getDataFieldValue(mockRecord, "050", "b")).thenReturn("213");

        JSONObject bibDataMappings = batchFileProcessor.prepareDataMappings(mockRecord, mockBatchProcesProfile, "holdings", "pre marc transformation");
        assertNotNull(bibDataMappings);
        assertEquals(bibDataMappings.get("Call Number"), "12321");
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


}