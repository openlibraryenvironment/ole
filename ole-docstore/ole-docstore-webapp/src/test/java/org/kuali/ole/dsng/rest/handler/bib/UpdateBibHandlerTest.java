package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.utility.MarcRecordUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.marc4j.marc.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 2/1/2016.
 */
public class UpdateBibHandlerTest {

    @Mock
    BusinessObjectService mockBusinessObjectService;

    @Mock
    SolrRequestReponseHandler mockSolrRequestReponseHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessIfDeleteAllExistOpsFound() throws JSONException {
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setHoldingsId("101");
        holdingsRecord.setHoldingsType(PHoldings.PRINT);
        ArrayList<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        ItemRecord itemRecord1 = new ItemRecord();
        itemRecord1.setItemId("10001");
        itemRecords.add(itemRecord1);

        ItemRecord itemRecord2 = new ItemRecord();
        itemRecord2.setItemId("10001");
        itemRecords.add(itemRecord2);

        ItemRecord itemRecord3 = new ItemRecord();
        itemRecord3.setItemId("10001");
        itemRecords.add(itemRecord3);

        holdingsRecord.setItemRecords(itemRecords);
        BibRecord bibRecord = new BibRecord();
        bibRecord.setBibId("1");
        ArrayList<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        holdingsRecords.add(holdingsRecord);
        bibRecord.setHoldingsRecords(holdingsRecords);

        JSONObject bibData = new JSONObject();
        JSONObject addedops = new JSONObject();
        addedops.put(OleNGConstants.ITEM, OleNGConstants.DELETE_ALL_EXISTING_AND_ADD);
        addedops.put(OleNGConstants.HOLDINGS, OleNGConstants.DELETE_ALL_EXISTING_AND_ADD);
        bibData.put(OleNGConstants.ADDED_OPS, addedops);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        updateBibHandler.setBusinessObjectService(mockBusinessObjectService);
        updateBibHandler.setSolrRequestReponseHandler(mockSolrRequestReponseHandler);
        updateBibHandler.processIfDeleteAllExistOpsFound(bibRecord,bibData, new Exchange());
        assertTrue(CollectionUtils.isEmpty(bibRecord.getHoldingsRecords()));
    }

    @Test
    public void testProcessIfDeleteAll() throws JSONException {
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setHoldingsId("101");
        holdingsRecord.setHoldingsType(PHoldings.PRINT);
        ArrayList<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        ItemRecord itemRecord1 = new ItemRecord();
        itemRecord1.setItemId("10001");
        itemRecords.add(itemRecord1);

        ItemRecord itemRecord2 = new ItemRecord();
        itemRecord2.setItemId("10001");
        itemRecords.add(itemRecord2);

        ItemRecord itemRecord3 = new ItemRecord();
        itemRecord3.setItemId("10001");
        itemRecords.add(itemRecord3);

        holdingsRecord.setItemRecords(itemRecords);
        BibRecord bibRecord = new BibRecord();
        bibRecord.setBibId("1");
        ArrayList<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        holdingsRecords.add(holdingsRecord);
        bibRecord.setHoldingsRecords(holdingsRecords);

        JSONObject bibData = new JSONObject();
        JSONObject addedops = new JSONObject();
        addedops.put(OleNGConstants.ITEM, OleNGConstants.DELETE_ALL);
        addedops.put(OleNGConstants.HOLDINGS, OleNGConstants.DELETE_ALL);
        bibData.put(OleNGConstants.ADDED_OPS, addedops);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        updateBibHandler.setBusinessObjectService(mockBusinessObjectService);
        updateBibHandler.setSolrRequestReponseHandler(mockSolrRequestReponseHandler);
        updateBibHandler.processIfDeleteAllExistOpsFound(bibRecord,bibData, new Exchange());
        assertTrue(CollectionUtils.isEmpty(bibRecord.getHoldingsRecords()));
    }

    @Test
    public void testGetDataFieldBasedOnFieldOps() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1(' ');
        dataField2.setIndicator2(' ');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"");
        value1.put(OleNGConstants.IND2,"");
        value1.put(OleNGConstants.SUBFIELD,"d");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertNotNull(dataFieldBasedOnFieldOps);
        System.out.println(dataFieldBasedOnFieldOps);

    }

    @Test
    public void testGetDataFieldBasedOnFieldOpsWithMatchedValue() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1(' ');
        dataField2.setIndicator2(' ');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"");
        value1.put(OleNGConstants.IND2,"");
        value1.put(OleNGConstants.SUBFIELD,"d");
        value1.put(OleNGConstants.VALUE,"856d");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertNotNull(dataFieldBasedOnFieldOps);
        System.out.println(dataFieldBasedOnFieldOps);

    }

    @Test
    public void testGetDataFieldBasedOnFieldOpsWithUnMatchedValue() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1(' ');
        dataField2.setIndicator2(' ');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"");
        value1.put(OleNGConstants.IND2,"");
        value1.put(OleNGConstants.SUBFIELD,"d");
        value1.put(OleNGConstants.VALUE,"856dddd");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertTrue(CollectionUtils.isEmpty(dataFieldBasedOnFieldOps));
        System.out.println(dataFieldBasedOnFieldOps);

    }
    @Test
    public void testGetDataFieldBasedOnFieldOpsWithUnMatchedIndicator1() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1(' ');
        dataField2.setIndicator2(' ');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"4");
        value1.put(OleNGConstants.IND2,"");
        value1.put(OleNGConstants.SUBFIELD,"d");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertTrue(CollectionUtils.isEmpty(dataFieldBasedOnFieldOps));
        System.out.println(dataFieldBasedOnFieldOps);

    }

    @Test
    public void testGetDataFieldBasedOnFieldOpsWithMatchedIndicator1() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1('4');
        dataField2.setIndicator2(' ');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"4");
        value1.put(OleNGConstants.IND2,"");
        value1.put(OleNGConstants.SUBFIELD,"d");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertNotNull(dataFieldBasedOnFieldOps);
        System.out.println(dataFieldBasedOnFieldOps);
    }

    @Test
    public void testGetDataFieldBasedOnFieldOpsWithMatchedIndicator2() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1(' ');
        dataField2.setIndicator2('4');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"");
        value1.put(OleNGConstants.IND2,"4");
        value1.put(OleNGConstants.SUBFIELD,"d");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertNotNull(dataFieldBasedOnFieldOps);
        System.out.println(dataFieldBasedOnFieldOps);
    }

    @Test
    public void testGetDataFieldBasedOnFieldOpsWithUnMatchedIndicator2() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1(' ');
        dataField2.setIndicator2(' ');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"");
        value1.put(OleNGConstants.IND2,"4");
        value1.put(OleNGConstants.SUBFIELD,"d");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertTrue(CollectionUtils.isEmpty(dataFieldBasedOnFieldOps));
        System.out.println(dataFieldBasedOnFieldOps);
    }

    @Test
    public void testGetDataFieldBasedOnFieldOpsWithMatchedInd1Ind2SubFieldValue() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1('4');
        dataField2.setIndicator2('0');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"4");
        value1.put(OleNGConstants.IND2,"0");
        value1.put(OleNGConstants.SUBFIELD,"d");
        value1.put(OleNGConstants.VALUE,"856d");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertNotNull(dataFieldBasedOnFieldOps);
        System.out.println(dataFieldBasedOnFieldOps);
    }

    @Test
    public void testGetDataFieldBasedOnFieldOpsWithOnlyDataField() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1('4');
        dataField2.setIndicator2('0');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        DataField dataField3 = marcFactory.newDataField();
        dataField3.setTag("856");
        dataField3.setIndicator1('4');
        dataField3.setIndicator2('0');

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('c');
        subfield3.setData("856c");
        dataField3.addSubfield(subfield3);

        Subfield subfield4 = marcFactory.newSubfield();
        subfield4.setCode('d');
        subfield4.setData("856d");
        dataField3.addSubfield(subfield4);

        record.addVariableField(dataField3);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertNotNull(dataFieldBasedOnFieldOps);
        assertTrue(dataFieldBasedOnFieldOps.size() == 2);
        System.out.println(dataFieldBasedOnFieldOps);
    }
    @Test
    public void testGetDataFieldBasedOnFieldOpsWithDataFieldInd1() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1('4');
        dataField2.setIndicator2('0');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        DataField dataField3 = marcFactory.newDataField();
        dataField3.setTag("856");
        dataField3.setIndicator1('4');
        dataField3.setIndicator2('0');

        Subfield subfield3 = marcFactory.newSubfield();
        subfield3.setCode('c');
        subfield3.setData("856c");
        dataField3.addSubfield(subfield3);

        Subfield subfield4 = marcFactory.newSubfield();
        subfield4.setCode('d');
        subfield4.setData("856d");
        dataField3.addSubfield(subfield4);

        record.addVariableField(dataField3);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"4");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertNotNull(dataFieldBasedOnFieldOps);
        assertTrue(dataFieldBasedOnFieldOps.size() == 2);
        System.out.println(dataFieldBasedOnFieldOps);
    }
    @Test
    public void testGetDataFieldBasedOnFieldOpsWithDataFieldInd1Ind2() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1('4');
        dataField2.setIndicator2('0');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"4");
        value1.put(OleNGConstants.IND2,"0");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertNotNull(dataFieldBasedOnFieldOps);
        System.out.println(dataFieldBasedOnFieldOps);
    }

    @Test
    public void testGetDataFieldBasedOnFieldOpsWithDataFieldInd1Ind2SubField() throws JSONException {
        MarcFactory marcFactory = MarcFactory.newInstance();
        Record record = marcFactory.newRecord();

        DataField dataField = marcFactory.newDataField();
        dataField.setTag("035");
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        Subfield subfield = marcFactory.newSubfield();
        subfield.setCode('a');
        subfield.setData("35a");
        dataField.addSubfield(subfield);

        Subfield subfield2 = marcFactory.newSubfield();
        subfield2.setCode('b');
        subfield2.setData("35b");
        dataField.addSubfield(subfield2);

        record.addVariableField(dataField);

        DataField dataField2 = marcFactory.newDataField();
        dataField2.setTag("856");
        dataField2.setIndicator1('4');
        dataField2.setIndicator2('0');

        Subfield subfield5 = marcFactory.newSubfield();
        subfield5.setCode('c');
        subfield5.setData("856c");
        dataField2.addSubfield(subfield5);

        Subfield subfield6 = marcFactory.newSubfield();
        subfield6.setCode('d');
        subfield6.setData("856d");
        dataField2.addSubfield(subfield6);

        record.addVariableField(dataField2);

        JSONArray fieldOperation = new JSONArray();
        JSONObject value1 = new JSONObject();
        value1.put(OleNGConstants.DATA_FIELD,"856");
        value1.put(OleNGConstants.IND1,"4");
        value1.put(OleNGConstants.IND2,"0");
        value1.put(OleNGConstants.SUBFIELD,"d");
        fieldOperation.put(value1);

        String marcRecordToMarcContent = new MarcRecordUtil().convertMarcRecordToMarcContent(record);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        List<VariableField> dataFieldBasedOnFieldOps = updateBibHandler.getDataFieldBasedOnFieldOps(marcRecordToMarcContent, value1);
        assertNotNull(dataFieldBasedOnFieldOps);
        System.out.println(dataFieldBasedOnFieldOps);
    }

}