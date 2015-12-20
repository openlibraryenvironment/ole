package org.kuali.ole.oleng.describe.processor.bibimport;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.describe.bo.marc.structuralfields.ControlFields;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.kuali.ole.spring.batch.BatchUtil;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/20/2015.
 */
@Service("matchPointProcessor")
public class MatchPointProcessor extends BatchUtil {

    public void prepareSolrQueryMapForMatchPoint(Record marcRecord, Map queryMap, List<BatchProfileMatchPoint> batchProfileMatchPoints) {
        if (CollectionUtils.isNotEmpty(batchProfileMatchPoints)) {
            for (Iterator<BatchProfileMatchPoint> iterator = batchProfileMatchPoints.iterator(); iterator.hasNext(); ) {
                BatchProfileMatchPoint batchProfileMatchPoint = iterator.next();
                formSolrQueryMapForMatchPoint(marcRecord, batchProfileMatchPoint, queryMap);
            }
        }
    }

    private void formSolrQueryMapForMatchPoint(Record marcRecord, BatchProfileMatchPoint batchProfileMatchPoint,
                                               Map<Record, List<String>> queryMap) {
        if (null != batchProfileMatchPoint.getControlField() &&
                batchProfileMatchPoint.getControlField().equalsIgnoreCase(ControlFields.CONTROL_FIELD_001)) {
            processForControlField(marcRecord, queryMap);
        } else {
            processForDataField(marcRecord, batchProfileMatchPoint, queryMap);
        }

    }

    private void processForControlField(Record marcRecord, Map<Record, List<String>> queryMap) {
        String valueOf001 = getMarcRecordUtil().getControlFieldValue(marcRecord, "001");
        String query = "controlfield_001:" + "\"" + valueOf001 + "\"";  // Todo : need to verify that the value for the control field matching should come from profile or marc record
        addQueryToMap(queryMap, marcRecord, query);
    }

    private void processForDataField(Record marcRecord, BatchProfileMatchPoint batchProfileMatchPoint, Map<Record, List<String>> queryMap) {
        String field = batchProfileMatchPoint.getDataField();
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            String subField = batchProfileMatchPoint.getSubField();
            List<Subfield> subFields = dataField.getSubfields(subField);
            for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                Subfield subfield = subfieldIterator.next();
                String matchPointValue = subfield.getData();
                //Todo : Need to add ind1 and ind2 to the query.
                String query = "mdf_" + field + subField + ":" + "\"" + matchPointValue + "\"";
                addQueryToMap(queryMap, marcRecord, query);
            }
        }
    }

    private void addQueryToMap(Map<Record, List<String>> queryMap, Record marcRecord, String query) {
        List<String> queryList = new ArrayList<>();
        if (queryMap.containsKey(marcRecord)) {
            queryList = queryMap.get(marcRecord);
        }
        queryList.add(query);
        queryMap.put(marcRecord, queryList);
    }
}
