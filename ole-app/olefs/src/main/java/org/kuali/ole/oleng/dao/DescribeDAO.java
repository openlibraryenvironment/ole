package org.kuali.ole.oleng.dao;

import org.kuali.ole.describe.bo.*;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AccessLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AuthenticationTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ReceiptStatusRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.StatisticalSearchRecord;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OleGloballyProtectedField;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

import java.util.List;

/**
 * Created by SheikS on 12/17/2015.
 */
public interface DescribeDAO {

    public List<OleShelvingScheme> fetchAllCallNumerTypes();
    public List<OleLocation> fetchAllLocations();
    public List<OleBibliographicRecordStatus> fetchAllBibStatus();
    public List<OleInstanceItemType> fetchAllItemType();
    public List<OLEDonor> fetchAllDonorCode();
    public List<OleItemAvailableStatus> fetchAllItemStatus();
    public List<BatchProcessProfile> fetchAllProfiles();
    public List<OleGloballyProtectedField> fetchAllGloballyProtectedFields();
    public List<AuthenticationTypeRecord> fetchAuthenticationTypeRecords();
    public List<ReceiptStatusRecord> fetchReceiptStatusRecords();
    public List<AccessLocation> fetchAccessLocations();
    public List<StatisticalSearchRecord> fetchStatisticalSearchCodes();
    public void deleteProfileById(Long profileId);
    public List<BatchProcessProfile> fetchProfileByNameAndType(String profileName, String type);
    public <T extends PersistableBusinessObject> T save(T bo);

}
