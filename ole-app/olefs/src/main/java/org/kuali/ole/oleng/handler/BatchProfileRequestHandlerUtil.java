package org.kuali.ole.oleng.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.*;
import org.kuali.ole.oleng.OleNGConstants;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.service.BatchProfileService;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OleGloballyProtectedField;
import org.kuali.ole.spring.batch.BatchUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/17/2015.
 */
public class BatchProfileRequestHandlerUtil extends BatchUtil {

    @Autowired
    private BatchProfileService batchProfileService;

    public List<BatchProcessProfile> getBatchProcessProfiles(String profileName) {
        if(org.apache.commons.lang3.StringUtils.isNotBlank(profileName)){
            Map map = new HashedMap();
            map.put(OleNGConstants.BATCH_PROCESS_PROFILE_NAME,profileName);
            return (List<BatchProcessProfile>) getBusinessObjectService().findMatching(BatchProcessProfile.class, map);
        } else {
            return (List<BatchProcessProfile>) getBusinessObjectService().findAll(BatchProcessProfile.class);
        }
    }

    public BatchProcessProfile getBatchProcessProfileById(Long profileId) {
        Map map = new HashedMap();
        map.put(OleNGConstants.BATCH_PROCESS_PROFILE_ID,profileId);
        return getBusinessObjectService().findByPrimaryKey(BatchProcessProfile.class, map);
    }

    public String prepareCallNumberType(){
        List<OleShelvingScheme> allCallNumberTypes = batchProfileService.getAllCallNumberTypes();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(allCallNumberTypes)) {
                for (Iterator<OleShelvingScheme> iterator = allCallNumberTypes.iterator(); iterator.hasNext(); ) {
                    OleShelvingScheme oleShelvingScheme = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID,oleShelvingScheme.getShelvingSchemeId());
                    jsonObject.put(OleNGConstants.VALUE,oleShelvingScheme.getShelvingSchemeName());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareLocation(String locaionLevelId){
        List<OleLocation> allLocations = batchProfileService.getAllLocations();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(allLocations)) {
                for (Iterator<OleLocation> iterator = allLocations.iterator(); iterator.hasNext(); ) {
                    OleLocation oleLocation = iterator.next();
                    if (null != locaionLevelId && locaionLevelId.equalsIgnoreCase(oleLocation.getLevelId())) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(OleNGConstants.ID,oleLocation.getLocationId());
                        jsonObject.put(OleNGConstants.VALUE,oleLocation.getLocationCode());
                        jsonArray.put(jsonObject);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareBibStatus(){
        List<OleBibliographicRecordStatus> bibStatusList = batchProfileService.fetchAllBibStatus();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(bibStatusList)) {
                for (Iterator<OleBibliographicRecordStatus> iterator = bibStatusList.iterator(); iterator.hasNext(); ) {
                    OleBibliographicRecordStatus bibliographicRecordStatus = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID,bibliographicRecordStatus.getBibliographicRecordStatusId());
                    jsonObject.put(OleNGConstants.VALUE,bibliographicRecordStatus.getBibliographicRecordStatusName());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareItemTypes(){
        List<OleInstanceItemType> itemTypes = batchProfileService.fetchAllItemType();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(itemTypes)) {
                for (Iterator<OleInstanceItemType> iterator = itemTypes.iterator(); iterator.hasNext(); ) {
                    OleInstanceItemType itemType = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID,itemType.getInstanceItemTypeId());
                    jsonObject.put(OleNGConstants.VALUE,itemType.getInstanceItemTypeName());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareDonorCodes(){
        List<OLEDonor> oleDonors = batchProfileService.fetchAllDonorCode();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(oleDonors)) {
                for (Iterator<OLEDonor> iterator = oleDonors.iterator(); iterator.hasNext(); ) {
                    OLEDonor oleDonor = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID,oleDonor.getDonorId());
                    jsonObject.put(OleNGConstants.VALUE,oleDonor.getDonorCode());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareItemStatus(){
        List<OleItemAvailableStatus> itemStatusList = batchProfileService.fetchAllItemStatus();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(itemStatusList)) {
                for (Iterator<OleItemAvailableStatus> iterator = itemStatusList.iterator(); iterator.hasNext(); ) {
                    OleItemAvailableStatus availableStatus = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID,availableStatus.getItemAvailableStatusId());
                    jsonObject.put(OleNGConstants.VALUE,availableStatus.getItemAvailableStatusName());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareProfileNames(String batchType){
        List<BatchProcessProfile> allProfiles = batchProfileService.getAllProfiles();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(allProfiles)) {
                for (Iterator<BatchProcessProfile> iterator = allProfiles.iterator(); iterator.hasNext(); ) {
                    BatchProcessProfile batchProcessProfile = iterator.next();
                    if(StringUtils.equals(batchProcessProfile.getBatchProcessType(), batchType)){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(OleNGConstants.PROFILE_ID,batchProcessProfile.getBatchProcessProfileId());
                        jsonObject.put(OleNGConstants.PROFILE_NAME,batchProcessProfile.getBatchProcessProfileName());
                        jsonArray.put(jsonObject);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareGloballyProtectedField(){
        List<OleGloballyProtectedField> globallyProtectedFields = batchProfileService.getAllProtectedFields();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(globallyProtectedFields)) {
                for (Iterator<OleGloballyProtectedField> iterator = globallyProtectedFields.iterator(); iterator.hasNext(); ) {
                    OleGloballyProtectedField oleGloballyProtectedField = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.FIELD_OPERATION_TYPE,"Globally Protected Field");
                    jsonObject.put(OleNGConstants.DATA_FIELD,oleGloballyProtectedField.getTag());
                    jsonObject.put(OleNGConstants.IND1,oleGloballyProtectedField.getFirstIndicator());
                    jsonObject.put(OleNGConstants.IND2,oleGloballyProtectedField.getSecondIndicator());
                    jsonObject.put(OleNGConstants.SUBFIELD,oleGloballyProtectedField.getSubField());
                    jsonObject.put(OleNGConstants.IGNORE_GPF,false);
                    jsonObject.put(OleNGConstants.IS_ADDLINE,Boolean.TRUE);
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareOrderImportFieldValues(String fieldName) {
        Map<String, String> fieldValues = batchProfileService.fetchOrderImportFieldValues(fieldName);
        JSONArray jsonArray = new JSONArray();
        try {
            if (!fieldValues.isEmpty()) {
                for (Iterator iterator = fieldValues.entrySet().iterator(); iterator.hasNext(); ) {
                    Map.Entry<String, String> fieldValue = (Map.Entry<String, String>) iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.ID, fieldValue.getKey());
                    jsonObject.put(OleNGConstants.VALUE, fieldValue.getValue());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareValuesForDowndown(String dropDownType) {
        return batchProfileService.fetchValues(dropDownType).toString();
    }



    public BatchProfileService getBatchProfileService() {
        return batchProfileService;
    }
}
