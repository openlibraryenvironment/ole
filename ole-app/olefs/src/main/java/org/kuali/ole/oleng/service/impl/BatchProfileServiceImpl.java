package org.kuali.ole.oleng.service.impl;

import org.kuali.ole.batch.bo.OLEBatchGloballyProtectedField;
import org.kuali.ole.describe.bo.*;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.service.BatchProfileService;
import org.kuali.ole.select.bo.OLEDonor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SheikS on 12/17/2015.
 */
@Service("batchProfileService")
public class BatchProfileServiceImpl implements BatchProfileService{

    @Autowired
    private DescribeDAO describeDAO;

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
    public List<OLEBatchGloballyProtectedField> getAllProtectedFields() {
        return describeDAO.fetchAllGloballyProtectedFields();
    }
}
