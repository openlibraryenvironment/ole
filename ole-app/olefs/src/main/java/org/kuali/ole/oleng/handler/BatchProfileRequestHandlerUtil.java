package org.kuali.ole.oleng.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.batch.bo.OLEBatchProcessFilterCriteriaBo;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.describe.bo.*;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.service.BatchProfileService;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OleGloballyProtectedField;
import org.kuali.ole.spring.batch.BatchUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by SheikS on 12/17/2015.
 */
public class BatchProfileRequestHandlerUtil extends BatchUtil {

    @Autowired
    private BatchProfileService batchProfileService;

    public List<BatchProcessProfile> getBatchProcessProfiles(Map searchCriteriaMap) {
        if(null != searchCriteriaMap && searchCriteriaMap.size() > 0){
            return (List<BatchProcessProfile>) getBusinessObjectService().findMatching(BatchProcessProfile.class, searchCriteriaMap);
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
                    if (null != locaionLevelId && (locaionLevelId.equalsIgnoreCase(oleLocation.getLevelId())) && oleLocation.isActive()) {
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
                    jsonObject.put(OleNGConstants.VALUE,itemType.getInstanceItemTypeCode());
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
        String excludeItemStatus = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                .SELECT_NMSPC, OLEConstants.SELECT_CMPNT, OLEConstants.EXCLUDE_ITEM_STATUS);
        List<String> excludeItemStatusList = StringUtils.isNotBlank(excludeItemStatus) ? Arrays.asList(excludeItemStatus.split(",")) : new ArrayList<String>();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(itemStatusList)) {
                for (Iterator<OleItemAvailableStatus> iterator = itemStatusList.iterator(); iterator.hasNext(); ) {
                    OleItemAvailableStatus availableStatus = iterator.next();
                    if (availableStatus!=null && !excludeItemStatusList.contains(availableStatus.getItemAvailableStatusCode())){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(OleNGConstants.ID,availableStatus.getItemAvailableStatusId());
                        jsonObject.put(OleNGConstants.VALUE,availableStatus.getItemAvailableStatusName());
                        jsonArray.put(jsonObject);
                    }
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
                        if (OleNGConstants.BATCH_EXPORT.equalsIgnoreCase(batchProcessProfile.getBatchProcessType())) {
                            batchProcessProfile = getObjectMapper().readValue(batchProcessProfile.getContent(), BatchProcessProfile.class);
                        }
                        jsonObject.put(OleNGConstants.UPLOAD_EXPORT_INPUT_FILE, batchProcessProfile.isUploadExportInputFile());
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

    public String prepareBatchProcessJobs() {
        List<BatchProcessJob> batchProcessJobs = batchProfileService.getAllBatchProcessJobs();
        JSONArray jsonArray = new JSONArray();
        try {
            if (CollectionUtils.isNotEmpty(batchProcessJobs)) {
                for (Iterator<BatchProcessJob> iterator = batchProcessJobs.iterator(); iterator.hasNext(); ) {
                    BatchProcessJob batchProcessJob = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.JOB_ID, batchProcessJob.getJobId());
                    jsonObject.put(OleNGConstants.JOB_NAME, batchProcessJob.getJobName());
                    jsonObject.put(OleNGConstants.PROFILE_TYPE, batchProcessJob.getProfileType());
                    jsonObject.put(OleNGConstants.PROFILE_NAME, batchProcessJob.getBatchProfileName());
                    jsonObject.put(OleNGConstants.CREATED_BY, batchProcessJob.getCreatedBy());
                    jsonObject.put(OleNGConstants.CREATED_ON, batchProcessJob.getCreatedOn());
                    jsonObject.put(OleNGConstants.NEXT_RUN_TIME, batchProcessJob.getNextRunTime());
                    jsonObject.put(OleNGConstants.JOB_TYPE, batchProcessJob.getJobType());
                    jsonObject.put(OleNGConstants.CRON_EXPRESSION, batchProcessJob.getCronExpression());
                    if (OleNGConstants.BATCH_EXPORT.equalsIgnoreCase(batchProcessJob.getProfileType())) {
                        if (null != batchProcessJob.getBatchProcessProfile()) {
                            BatchProcessProfile profile = getObjectMapper().readValue(batchProcessJob.getBatchProcessProfile().getContent(), BatchProcessProfile.class);
                            batchProcessJob.setBatchProcessProfile(profile);
                            //batchProcessJob.setUploadExportInputFile(profile.isUploadExportInputFile());
                        }
                    }
                    jsonObject.put(OleNGConstants.UPLOAD_EXPORT_INPUT_FILE, batchProcessJob.isUploadExportInputFile());
                    List<BatchJobDetails> batchJobDetailsList = batchProcessJob.getBatchJobDetailsList();
                    sortExecutions(batchJobDetailsList);
                    jsonObject.put(OleNGConstants.EXECUTION_COUNT, batchJobDetailsList.size());
                    if (CollectionUtils.isNotEmpty(batchJobDetailsList)) {
                        jsonObject.put(OleNGConstants.LAST_EXECUTION_STATUS, batchJobDetailsList.get(batchJobDetailsList.size() - 1).getStatus());
                    }
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareBatchJobs() {
        List<BatchJobDetails> batchJobs = batchProfileService.getAllBatchJobs();
        JSONArray jsonArray = new JSONArray();
        try {
            if (CollectionUtils.isNotEmpty(batchJobs)) {
                for (Iterator<BatchJobDetails> iterator = batchJobs.iterator(); iterator.hasNext(); ) {
                    BatchJobDetails batchJob = iterator.next();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(OleNGConstants.JOB_DETAIL_ID, batchJob.getJobDetailId());
                    jsonObject.put(OleNGConstants.JOB_NAME, batchJob.getJobName());
                    jsonObject.put(OleNGConstants.JOB_ID, batchJob.getJobId());
                    jsonObject.put(OleNGConstants.PROFILE_TYPE, batchJob.getProfileType());
                    jsonObject.put(OleNGConstants.PROFILE_NAME, batchJob.getProfileName());
                    jsonObject.put(OleNGConstants.FILE_NAME, batchJob.getFileName());
                    jsonObject.put(OleNGConstants.CREATED_BY, batchJob.getCreatedBy());
                    jsonObject.put(OleNGConstants.START_TIME, batchJob.getStartTime());
                    jsonObject.put(OleNGConstants.END_TIME, batchJob.getEndTime());
                    jsonObject.put(OleNGConstants.PER_COMPLETED, batchJob.getPerCompleted());
                    jsonObject.put(OleNGConstants.TIME_SPENT, batchJob.getTimeSpent());
                    jsonObject.put(OleNGConstants.TOTAL_RECORDS, batchJob.getTotalRecords());
                    jsonObject.put(OleNGConstants.TOTAL_RECORDS_PROCESSED, batchJob.getTotalRecordsProcessed());
                    jsonObject.put(OleNGConstants.TOTAL_FAILURE_RECORDS, batchJob.getTotalFailureRecords());
                    jsonObject.put(OleNGConstants.JOB_STATUS, batchJob.getStatus());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public String prepareFilterNames() {
        List<OLEBatchProcessFilterCriteriaBo> oleBatchProcessFilterCriteriaBos = batchProfileService.getAllBatchProcessFilterCriterias();
        Map<String, String> filterNames = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        try {
            if(CollectionUtils.isNotEmpty(oleBatchProcessFilterCriteriaBos)) {
                for(OLEBatchProcessFilterCriteriaBo oleBatchProcessFilterCriteriaBo : oleBatchProcessFilterCriteriaBos) {
                    filterNames.put(oleBatchProcessFilterCriteriaBo.getFieldDisplayName(), oleBatchProcessFilterCriteriaBo.getFieldDisplayName());
                }
            }
            if (!filterNames.isEmpty()) {
                for (Iterator iterator = filterNames.entrySet().iterator(); iterator.hasNext(); ) {
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

    public void sortExecutions(List<BatchJobDetails> batchJobDetailses) {
        if (CollectionUtils.isNotEmpty(batchJobDetailses)) {
            Collections.sort(batchJobDetailses, new Comparator<BatchJobDetails>() {
                public int compare(BatchJobDetails details1, BatchJobDetails details2) {
                    long priorityForDataMapping1 = details1.getJobDetailId();
                    long priorityForDataMapping2 = details2.getJobDetailId();
                    return new Long(priorityForDataMapping1).compareTo(new Long(priorityForDataMapping2));
                }
            });
        }
    }

    public String canShowLocalDataMapping() {
        ParameterValueResolver instance = ParameterValueResolver.getInstance();
        Boolean showDataMapping = instance.getParameterAsBoolean(OleNGConstants.APPL_ID_OLE,
                OleNGConstants.DESC_NMSPC, OleNGConstants.DESCRIBE_COMPONENT, OleNGConstants.SHOW_LOCAL_DATA_MAPPING_SECTION);
        JSONObject jsonObject = new JSONObject();
        addElementToJsonObject(jsonObject, "canShow", showDataMapping);
        return jsonObject.toString();
    }

    private void addElementToJsonObject(JSONObject jsonObject, String key, Object value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public BatchProfileService getBatchProfileService() {
        return batchProfileService;
    }
}
