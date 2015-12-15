package org.kuali.ole.oleng.handler;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.batch.profile.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeshbabuk on 12/10/15.
 */
public class BatchProfileRequestHandler {

    public BatchProcessProfile convertJsonToProfile(JSONObject jsonObject) throws JSONException {
        BatchProcessProfile batchProcessProfile = new BatchProcessProfile();
        List<BatchProfileMatchPoint> batchProfileMatchPointList = new ArrayList<>();
        List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = new ArrayList<>();
        List<BatchProfileFieldOperation> batchProfileFieldOperationList = new ArrayList<>();
        List<BatchProfileDataMapping> batchProfileDataMappingList = new ArrayList<>();
        List<BatchProfileDataTransformer> batchProfileDataTransformerList = new ArrayList<>();

        JSONObject mainSectionJsonObj = jsonObject.getJSONObject("mainSection");
        JSONArray matchPointsJsonArray = jsonObject.getJSONArray("matchPoints");
        JSONArray addOrOverlayJsonArray = jsonObject.getJSONArray("addOrOverlay");
        JSONArray fieldOperationsJsonArray = jsonObject.getJSONArray("fieldOperations");
        JSONArray dataMappingsJsonArray = jsonObject.getJSONArray("dataMappings");
        JSONArray dataTransformationsJsonArray = jsonObject.getJSONArray("dataTransformations");

        for (int i = 1, size = matchPointsJsonArray.length(); i < size; i++) {
            BatchProfileMatchPoint batchProfileMatchPoint = new BatchProfileMatchPoint();
            JSONObject matchPointJsonObj = matchPointsJsonArray.getJSONObject(i);
            batchProfileMatchPoint.setDataType(matchPointJsonObj.get("matchPointDocType").toString());
            batchProfileMatchPoint.setMatchPoint(matchPointJsonObj.get("matchPointValue").toString());
            if (StringUtils.isNotBlank(batchProfileMatchPoint.getMatchPoint())) {
                batchProfileMatchPointList.add(batchProfileMatchPoint);
            }
        }

        for (int i = 1, size = addOrOverlayJsonArray.length(); i < size; i++) {
            BatchProfileAddOrOverlay batchProfileAddOrOverlay = new BatchProfileAddOrOverlay();
            JSONObject addOrOverlayJsonObj = addOrOverlayJsonArray.getJSONObject(i);
            batchProfileAddOrOverlay.setMatchOption(addOrOverlayJsonObj.get("matchOption").toString());
            batchProfileAddOrOverlay.setDataType(addOrOverlayJsonObj.get("addOrOverlayDocType").toString());
            batchProfileAddOrOverlay.setOperation(addOrOverlayJsonObj.get("operation").toString());
            batchProfileAddOrOverlay.setBibStatus(addOrOverlayJsonObj.get("bibStatus").toString());
            batchProfileAddOrOverlay.setAddOperation(addOrOverlayJsonObj.get("addOperation").toString());
            batchProfileAddOrOverlay.setAddItems(Boolean.valueOf(addOrOverlayJsonObj.get("addItems").toString()));
            batchProfileAddOrOverlayList.add(batchProfileAddOrOverlay);
        }

        for (int i = 1, size = fieldOperationsJsonArray.length(); i < size; i++) {
            BatchProfileFieldOperation batchProfileFieldOperation = new BatchProfileFieldOperation();
            JSONObject fieldOperationJsonObj = fieldOperationsJsonArray.getJSONObject(i);
            batchProfileFieldOperation.setFieldOperationType(fieldOperationJsonObj.get("fieldOperationType").toString());
            batchProfileFieldOperation.setDataField(fieldOperationJsonObj.get("dataField").toString());
            batchProfileFieldOperation.setInd1(fieldOperationJsonObj.get("ind1").toString());
            batchProfileFieldOperation.setInd2(fieldOperationJsonObj.get("ind2").toString());
            batchProfileFieldOperation.setSubField(fieldOperationJsonObj.get("subField").toString());
            batchProfileFieldOperationList.add(batchProfileFieldOperation);
        }

        for (int i = 1, size = dataMappingsJsonArray.length(); i < size; i++) {
            BatchProfileDataMapping batchProfileDataMapping = new BatchProfileDataMapping();
            JSONObject dataMappingJsonObj = dataMappingsJsonArray.getJSONObject(i);
            batchProfileDataMapping.setDataType(dataMappingJsonObj.get("dataMappingDocType").toString());
            batchProfileDataMapping.setDataField(dataMappingJsonObj.get("dataField").toString());
            batchProfileDataMapping.setInd1(dataMappingJsonObj.get("ind1").toString());
            batchProfileDataMapping.setInd2(dataMappingJsonObj.get("ind2").toString());
            batchProfileDataMapping.setSubField(dataMappingJsonObj.get("subField").toString());
            batchProfileDataMapping.setDestination(dataMappingJsonObj.get("destination").toString());
            batchProfileDataMapping.setField(dataMappingJsonObj.get("field").toString());
            batchProfileDataMappingList.add(batchProfileDataMapping);
        }

        for (int i = 1, size = dataTransformationsJsonArray.length(); i < size; i++) {
            BatchProfileDataTransformer batchProfileDataTransformer = new BatchProfileDataTransformer();
            JSONObject dataTransformerJsonObj = dataTransformationsJsonArray.getJSONObject(i);
            batchProfileDataTransformer.setDataType(dataTransformerJsonObj.get("dataTransformationDocType").toString());
            batchProfileDataTransformer.setDataField(dataTransformerJsonObj.get("dataField").toString());
            batchProfileDataTransformer.setInd1(dataTransformerJsonObj.get("ind1").toString());
            batchProfileDataTransformer.setInd2(dataTransformerJsonObj.get("ind2").toString());
            batchProfileDataTransformer.setSubField(dataTransformerJsonObj.get("subField").toString());
            batchProfileDataTransformer.setTransformer(dataTransformerJsonObj.get("transformer").toString());
            batchProfileDataTransformer.setExpression(dataTransformerJsonObj.get("expression").toString());
            batchProfileDataTransformerList.add(batchProfileDataTransformer);
        }

        if (mainSectionJsonObj.has("profileName")) {
            batchProcessProfile.setBatchProcessProfileName(mainSectionJsonObj.get("profileName").toString());
        }
        batchProcessProfile.setBatchProfileMatchPointList(batchProfileMatchPointList);
        batchProcessProfile.setBatchProfileAddOrOverlayList(batchProfileAddOrOverlayList);
        batchProcessProfile.setBatchProfileFieldOperationList(batchProfileFieldOperationList);
        batchProcessProfile.setBatchProfileDataMappingList(batchProfileDataMappingList);
        batchProcessProfile.setBatchProfileDataTransformerList(batchProfileDataTransformerList);
        return batchProcessProfile;
    }
}
