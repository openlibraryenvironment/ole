package org.kuali.ole.oleng.service;

import org.codehaus.jettison.json.JSONArray;
import org.kuali.ole.batch.bo.OLEBatchProcessFilterCriteriaBo;
import org.kuali.ole.describe.bo.*;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OleGloballyProtectedField;

import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/17/2015.
 */
public interface BatchProfileService {

    public List<OleShelvingScheme> getAllCallNumberTypes();
    public List<OleLocation> getAllLocations();
    public List<OleBibliographicRecordStatus> fetchAllBibStatus();
    public List<OleInstanceItemType> fetchAllItemType();
    public List<OLEDonor> fetchAllDonorCode();
    public List<OleItemAvailableStatus> fetchAllItemStatus();
    public List<BatchProcessProfile> getAllProfiles();
    public List<OleGloballyProtectedField> getAllProtectedFields();
    public Map<String, String> fetchOrderImportFieldValues(String fieldName);
    public JSONArray fetchValues(String type);
    public void deleteProfileById(Long profileId);
    public BatchProcessProfile saveProfile(BatchProcessProfile batchProcessProfile);
    public List<BatchProcessJob> getAllBatchProcessJobs();
    public List<BatchJobDetails> getAllBatchJobs();
    public List<OLEBatchProcessFilterCriteriaBo> getAllBatchProcessFilterCriterias();

}
