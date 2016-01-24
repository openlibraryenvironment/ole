package org.kuali.ole.dsng.util;

import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.constants.OleNGConstants;
import org.marc4j.marc.Record;
import org.marc4j.marc.VariableField;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pvsubrah on 1/24/16.
 */
public class DataMappingsUtilTest {

    @Mock
    private Record marcRecord;

    @Mock
    private JSONArray actionOps;

    @Mock
    private JSONObject actionOp;

    @Mock
    ArrayList<VariableField> variableFields;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void splitDataMappings() throws Exception, JSONException {

        Mockito.when(variableFields.size()).thenReturn(2);
        Mockito.when(marcRecord.getVariableFields("856")).thenReturn(variableFields);

        Mockito.when(actionOps.length()).thenReturn(1);
        Mockito.when(actionOps.get(0)).thenReturn(actionOp);
        Mockito.when(actionOp.getString(OleNGConstants.DOC_TYPE)).thenReturn(OleNGConstants.HOLDINGS);
        Mockito.when(actionOp.has(OleNGConstants.DATA_FIELD)).thenReturn(Boolean.TRUE);
        Mockito.when(actionOp.getString(OleNGConstants.DATA_FIELD)).thenReturn("856");
        Mockito.when(actionOp.getString(OleNGConstants.IND1)).thenReturn(null);
        Mockito.when(actionOp.getString(OleNGConstants.IND2)).thenReturn(null);
        Mockito.when(actionOp.getString(OleNGConstants.SUBFIELD)).thenReturn(null);


        Mockito.when(actionOp.getString("actionOpMappedField")).thenReturn("Link Text");

        JSONObject dataMappings = new JSONObject();
        dataMappings.put("Link Text", "View Film;View Film Part2");
        dataMappings.put("Call Number", "1231.123");


        DataMappingsUtil dataMappingsUtil = new DataMappingsUtil();

        List<JSONObject> splitDataMappings = dataMappingsUtil.splitDataMappings(marcRecord, dataMappings, actionOps);

        assertNotNull(splitDataMappings);

        assertTrue(!splitDataMappings.isEmpty());


    }

}