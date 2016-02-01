package org.kuali.ole.dsng.util;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 1/24/16.
 */
public class DataMappingsUtil {
    public List<JSONObject> splitDataMappings(Record marcRecord, JSONObject dataMappings, JSONArray actionOps) {
        ArrayList<JSONObject> splitMappings = new ArrayList<JSONObject>();
        String mappedField;
        Integer numOccurances;
        for (int i = 0; i < actionOps.length(); i++) {
            try {
                JSONObject actionOp = (JSONObject) actionOps.get(i);
                String docType = actionOp.getString(OleNGConstants.DOC_TYPE);
                if (docType.equalsIgnoreCase(OleNGConstants.EHOLDINGS) || docType.equalsIgnoreCase(OleNGConstants.HOLDINGS) || docType.equalsIgnoreCase(OleNGConstants.ITEM)) {
                    String dataField = actionOp.has(OleNGConstants.DATA_FIELD) ? actionOp.getString(OleNGConstants.DATA_FIELD) : null;
                    String ind1 = actionOp.has(OleNGConstants.IND1) ? actionOp.getString(OleNGConstants.IND1) : null;
                    String ind2 = actionOp.has(OleNGConstants.IND2) ? actionOp.getString(OleNGConstants.IND2) : null;
                    String subField = actionOp.has(OleNGConstants.SUBFIELD) ? actionOp.getString(OleNGConstants.SUBFIELD) : null;

                    numOccurances = new MarcRecordUtil().getNumOccurances(marcRecord, dataField, ind1, ind2, subField);

                    if (numOccurances > 1) {
                        mappedField = actionOp.getString("actionOpMappedField");
                        String mappedValue = dataMappings.getString(mappedField);
                        dataMappings.remove(mappedField);
                        StringTokenizer stringTokenizer = new StringTokenizer(mappedValue, ";");
                        while (stringTokenizer.hasMoreTokens()){
                            JSONObject dataMapping = new JSONObject();
                            for (Iterator iterator = dataMappings.keys(); iterator.hasNext(); ) {
                                String fieldName = (String) iterator.next();
                                String fieldValue = dataMappings.getString(fieldName);
                                dataMapping.put(fieldName, fieldValue);
                            }
                            dataMapping.put(mappedField, stringTokenizer.nextToken());
                            splitMappings.add(dataMapping);
                        }
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return splitMappings;
    }
}
