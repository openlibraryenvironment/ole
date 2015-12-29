package org.kuali.ole.oleng.service.impl;

import org.kuali.ole.describe.bo.*;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.dao.SelectDAO;
import org.kuali.ole.oleng.service.BatchProfileService;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OleGloballyProtectedField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/17/2015.
 */
@Service("batchProfileService")
public class BatchProfileServiceImpl implements BatchProfileService{

    @Autowired
    private DescribeDAO describeDAO;

    @Autowired
    private SelectDAO selectDAO;

    @Override
    public List<OleShelvingScheme> getAllCallNumberTypes() {
        return describeDAO.fetchAllCallNumerTypes();
    }

    @Override
    public List<OleLocation> getAllLocations() {
        return describeDAO.fetchAllLocations();
    }

    @Override
    public List<OleBibliographicRecordStatus> fetchAllBibStatus() {
        return describeDAO.fetchAllBibStatus();
    }

    @Override
    public List<OleInstanceItemType> fetchAllItemType() {
        return describeDAO.fetchAllItemType();
    }

    @Override
    public List<OLEDonor> fetchAllDonorCode() {
        return describeDAO.fetchAllDonorCode();
    }

    @Override
    public List<OleItemAvailableStatus> fetchAllItemStatus() {
        return describeDAO.fetchAllItemStatus();
    }

    @Override
    public List<BatchProcessProfile> getAllProfiles() {
        return describeDAO.fetchAllProfiles();
    }

    @Override
    public List<OleGloballyProtectedField> getAllProtectedFields() {
        return describeDAO.fetchAllGloballyProtectedFields();
    }

    @Override
    public Map<String, String> fetchOrderImportFieldValues(String fieldName) {
        return selectDAO.fetchOrderImportFieldValues(fieldName);
    }

    @Override
    public void deleteProfileById(Long profileId) {
        describeDAO.deleteProfileById(profileId);
    }
}
