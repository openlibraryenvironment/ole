package org.kuali.ole.oleng.dao.impl;

import org.kuali.ole.describe.bo.*;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OleGloballyProtectedField;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by SheikS on 12/17/2015.
 */
@Repository("DescribeDAO")
@Scope("prototype")
public class DescribeDAOImpl implements DescribeDAO {

    private BusinessObjectService businessObjectService;

    public List<OleShelvingScheme> fetchAllCallNumerTypes(){
        return (List<OleShelvingScheme>) KRADServiceLocator.getBusinessObjectService().findAll(OleShelvingScheme.class);
    }

    public List<OleLocation> fetchAllLocations() {
        return (List<OleLocation>) getBusinessObjectService().findAll(OleLocation.class);
    }

    public List<OleBibliographicRecordStatus> fetchAllBibStatus() {
        return (List<OleBibliographicRecordStatus>) KRADServiceLocator.getBusinessObjectService().findAll(OleBibliographicRecordStatus.class);
    }

    @Override
    public List<OleInstanceItemType> fetchAllItemType() {
        return (List<OleInstanceItemType>) getBusinessObjectService().findAll(OleInstanceItemType.class);
    }

    @Override
    public List<OLEDonor> fetchAllDonorCode() {
        return (List<OLEDonor>) getBusinessObjectService().findAll(OLEDonor.class);
    }

    @Override
    public List<OleItemAvailableStatus> fetchAllItemStatus() {
        return (List<OleItemAvailableStatus>) getBusinessObjectService().findAll(OleItemAvailableStatus.class);
    }

    @Override
    public List<BatchProcessProfile> fetchAllProfiles() {
        return (List<BatchProcessProfile>) getBusinessObjectService().findAll(BatchProcessProfile.class);
    }

    @Override
    public List<OleGloballyProtectedField> fetchAllGloballyProtectedFields() {
        return (List<OleGloballyProtectedField>) getBusinessObjectService().findAll(OleGloballyProtectedField.class);
    }

    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
