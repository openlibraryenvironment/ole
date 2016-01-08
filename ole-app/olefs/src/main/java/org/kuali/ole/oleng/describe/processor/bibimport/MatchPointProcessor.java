package org.kuali.ole.oleng.describe.processor.bibimport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

    public String prepareSolrQueryMapForMatchPoint(Record marcRecord, List<BatchProfileMatchPoint> batchProfileMatchPoints) {
        List<String> queryList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(batchProfileMatchPoints)) {
            for (Iterator<BatchProfileMatchPoint> iterator = batchProfileMatchPoints.iterator(); iterator.hasNext(); ) {
                BatchProfileMatchPoint batchProfileMatchPoint = iterator.next();
                if (batchProfileMatchPoint.getDataType().equalsIgnoreCase("bibliographic")) {
                    String query = formSolrQueryMapForMatchPoint(marcRecord, batchProfileMatchPoint);
                    if(StringUtils.isNotBlank(query)){
                        queryList.add(query) ;
                    }
                }
            }
        }
        return prepareSolrQueryFromQueryList(queryList);
    }

    private String formSolrQueryMapForMatchPoint(Record marcRecord, BatchProfileMatchPoint batchProfileMatchPoint) {
        String dataField = batchProfileMatchPoint.getDataField();
        if (null != batchProfileMatchPoint.getDataField() && getMarcRecordUtil().isControlField(dataField)) {
            return processForControlField(marcRecord,batchProfileMatchPoint);
        } else {
            return processForDataField(marcRecord, batchProfileMatchPoint);
        }

    }

    private String processForControlField(Record marcRecord,BatchProfileMatchPoint  batchProfileMatchPoint) {
        String dataField = batchProfileMatchPoint.getDataField();
        String destDataField = batchProfileMatchPoint.getDestDataField();
        String matchPointValue = getMarcRecordUtil().getControlFieldValue(marcRecord, dataField);
        String query = null;
        if(StringUtils.isNotBlank(destDataField)) {
            if(getMarcRecordUtil().isControlField(destDataField)) {
                query =  "controlfield_" + destDataField + ":\"" + matchPointValue + "\"";
            } else {
                String destSubField = batchProfileMatchPoint.getDestSubField();
                query =  "mdf_" + destDataField + destSubField + ":" + "\"" + matchPointValue + "\"";
            }
        } else {
            query = "controlfield_" + dataField + ":\"" + matchPointValue + "\"";
        }
        return query;
    }

    private String processForDataField(Record marcRecord, BatchProfileMatchPoint batchProfileMatchPoint) {
        String field = batchProfileMatchPoint.getDataField();
        String subField = batchProfileMatchPoint.getSubField();
        String destDataField = batchProfileMatchPoint.getDestDataField();
        String destSubField = batchProfileMatchPoint.getDestSubField();

        String query = null;

        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            List<Subfield> subFields = dataField.getSubfields(subField);
            for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                Subfield subfield = subfieldIterator.next();
                String matchPointValue = subfield.getData();
                if (StringUtils.isNotBlank(matchPointValue)) {
                    //Todo : Need to add ind1 and ind2 to the query.
                    if(StringUtils.isBlank(destDataField)) {
                        query = "mdf_" + field + subField + ":" + "\"" + matchPointValue + "\"";
                    } else {
                        if(getMarcRecordUtil().isControlField(destDataField)){
                            query =  "controlfield_" + destDataField + ":\"" + matchPointValue + "\"";
                        } else {
                            query = "mdf_" + destDataField + destSubField + ":" + "\"" + matchPointValue + "\"";
                        }
                    }
                }
            }
        }
        return query;
    }

    private String prepareSolrQueryFromQueryList(List<String> queryList) {
        StringBuilder queryBuilder = new StringBuilder();
        for (Iterator<String> iterator = queryList.iterator(); iterator.hasNext(); ) {
            String query = iterator.next();
            appendQuery(queryBuilder,query);
        }
        String query = "((DocType:bibliographic) AND (" + queryBuilder.toString() + "))";
        return query;
    }

    private void appendQuery(StringBuilder queryBuilder, String query) {
        if(queryBuilder.length() > 0) {
            queryBuilder.append(" OR ");
        }
        queryBuilder.append(query);
    }
}
