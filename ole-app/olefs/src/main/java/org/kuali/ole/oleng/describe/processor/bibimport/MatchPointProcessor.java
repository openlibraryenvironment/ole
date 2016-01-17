package org.kuali.ole.oleng.describe.processor.bibimport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
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
                if (batchProfileMatchPoint.getDataType().equalsIgnoreCase(OleNGConstants.BIBLIOGRAPHIC)) {
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
                query =  OleNGConstants.CONTROL_FIELD_ + destDataField + ":\"" + matchPointValue + "\"";
            } else {
                String destSubField = batchProfileMatchPoint.getDestSubField();
                query =  OleNGConstants.MDF_ + destDataField + destSubField + ":" + "\"" + matchPointValue + "\"";
            }
        } else {
            query = OleNGConstants.CONTROL_FIELD_ + dataField + ":\"" + matchPointValue + "\"";
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
                        query = OleNGConstants.MDF_ + field + subField + ":" + "\"" + matchPointValue + "\"";
                    } else {
                        if(getMarcRecordUtil().isControlField(destDataField)){
                            query =  OleNGConstants.CONTROL_FIELD_ + destDataField + ":\"" + matchPointValue + "\"";
                        } else {
                            query = OleNGConstants.MDF_ + destDataField + destSubField + ":" + "\"" + matchPointValue + "\"";
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
        String query = OleNGConstants.BIB_QUERY_BEGIN + queryBuilder.toString() + "))";
        return query;
    }

    private void appendQuery(StringBuilder queryBuilder, String query) {
        if(queryBuilder.length() > 0) {
            queryBuilder.append(" OR ");
        }
        queryBuilder.append(query);
    }

    public JSONObject prepareMatchPointsForItem(Record marcRecord, BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONObject itemData = new JSONObject();
        JSONObject itemMatchPoints = prepareMatchPointsForDocType(marcRecord, batchProcessProfile.getBatchProfileMatchPointList(), OleNGConstants.ITEM);
        if (itemMatchPoints.length() > 0) {
            itemData.put(OleNGConstants.MATCH_POINT, itemMatchPoints);
        }

        return itemData;
    }


    public JSONObject prepareMatchPointsForHoldings(Record marcRecord, BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONObject holdingsData = new JSONObject();
        JSONObject holdingsMatchPoints = prepareMatchPointsForDocType(marcRecord,batchProcessProfile.getBatchProfileMatchPointList(), OleNGConstants.HOLDINGS);
        if (holdingsMatchPoints.length() > 0) {
            holdingsData.put(OleNGConstants.MATCH_POINT, holdingsMatchPoints);
        }
        return holdingsData;
    }

    public JSONObject prepareMatchPointsForEHoldings(Record marcRecord, BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONObject holdingsData = new JSONObject();
        JSONObject holdingsMatchPoints = prepareMatchPointsForDocType(marcRecord, batchProcessProfile.getBatchProfileMatchPointList(), OleNGConstants.EHOLDINGS);
        if (holdingsMatchPoints.length() > 0) {
            holdingsData.put(OleNGConstants.MATCH_POINT, holdingsMatchPoints);
        }
        return holdingsData;
    }



    private JSONObject prepareMatchPointsForDocType(Record marcRecord, List<BatchProfileMatchPoint> batchProfileMatchPoints, String docType) throws JSONException {
        JSONObject matchPoints = new JSONObject();
        if (CollectionUtils.isNotEmpty(batchProfileMatchPoints)) {
            for (Iterator<BatchProfileMatchPoint> iterator = batchProfileMatchPoints.iterator(); iterator.hasNext(); ) {
                BatchProfileMatchPoint batchProfileMatchPoint = iterator.next();
                if (batchProfileMatchPoint.getDataType().equalsIgnoreCase(docType)) {
                    String matchPoint = batchProfileMatchPoint.getMatchPointType();
                    String newValue;
                    if (matchPoints.has(matchPoint)) {
                        newValue = matchPoints.getString(matchPoint) + "," + getMatchPointValue(marcRecord,batchProfileMatchPoint);
                    } else {
                        newValue = getMatchPointValue(marcRecord, batchProfileMatchPoint);
                    }
                    matchPoints.put(matchPoint, newValue);
                }
            }
        }
        return matchPoints;
    }

    private String getMatchPointValue(Record marcRecord, BatchProfileMatchPoint batchProfileMatchPoint) {
        String matchPointValue = batchProfileMatchPoint.getMatchPointValue();
        String dataField = batchProfileMatchPoint.getDataField();
        String value = null;
        if (StringUtils.isNotBlank(matchPointValue)) {
            value =  matchPointValue;
        } else if(StringUtils.isNotBlank(dataField)) {
            if(getMarcRecordUtil().isControlField(dataField)) {
                value = getMarcRecordUtil().getControlFieldValue(marcRecord,dataField);
            } else {
                String subField = batchProfileMatchPoint.getSubField();
                if (StringUtils.isNotBlank(subField)) {
                    value = getMarcRecordUtil().getDataFieldValue(marcRecord,dataField,"$" + subField);
                }
            }
        }

        if(StringUtils.isBlank(value)) {
            value =  batchProfileMatchPoint.getConstant();
        }
        return value;
    }
}
