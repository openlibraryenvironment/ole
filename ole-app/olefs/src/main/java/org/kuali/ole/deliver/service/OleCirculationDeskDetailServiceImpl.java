package org.kuali.ole.deliver.service;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.ole.deliver.form.OleCirculationDeskDetailForm;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is the service class for performing save and search operations on Circulation Desk Mapping
 */
public class OleCirculationDeskDetailServiceImpl {


    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCirculationDeskDetailServiceImpl.class);
    private BusinessObjectService businessObjectService;

    /**
     * Gets the instance of BusinessObjectService
     *
     * @return businessObjectService(BusinessObjectService)
     */
    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }


    /**
     * This method is for populating the circulation details based on the available circulation desk
     *
     * @return oleCirculationDeskDetailList
     */
    public List<OleCirculationDeskDetail> populateCreateList() {
        List<OleCirculationDeskDetail> oleCirculationDeskDetailList = new ArrayList<OleCirculationDeskDetail>();
        List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) getBusinessObjectService().findAll(OleCirculationDesk.class);
        OleCirculationDeskDetail oleCirculationDeskDetail;
        for (int i = 0; i < oleCirculationDeskList.size(); i++) {
            if (oleCirculationDeskList.get(i).isActive()) {
                oleCirculationDeskDetail = new OleCirculationDeskDetail();
                oleCirculationDeskDetail.setOleCirculationDesk(oleCirculationDeskList.get(i));
                oleCirculationDeskDetail.setCirculationDeskId(oleCirculationDeskList.get(i).getCirculationDeskId());
                oleCirculationDeskDetailList.add(oleCirculationDeskDetail);
            }
        }
        return oleCirculationDeskDetailList;
    }

    /**
     * This method is for saving the circulation desk details for the operator
     *
     * @param oleCirculationDeskDetailForm
     * @return oleCirculationDeskDetailForm
     */
    public OleCirculationDeskDetailForm performSave(OleCirculationDeskDetailForm oleCirculationDeskDetailForm) {

        oleCirculationDeskDetailForm.setMessage(null);

        String operatorId = oleCirculationDeskDetailForm.getOperatorId();
        Map<String, String> opt = new HashMap<String, String>();
        opt.put("principalId", operatorId);
        List<PrincipalBo> matchedPrincipallList = (List<PrincipalBo>) getBusinessObjectService().findMatching(PrincipalBo.class, opt);

        if (matchedPrincipallList.size() == 0) {
            StringBuffer selectBuffer = new StringBuffer(OLEConstants.CRCL_DSK_INVALID_OPR);
            oleCirculationDeskDetailForm.setMessage(selectBuffer.toString());
            LOG.error(selectBuffer);
            return oleCirculationDeskDetailForm;
        } else {
            List<OleCirculationDeskDetail> oleCirculationDeskDetails = oleCirculationDeskDetailForm.getOleCirculationDetailsCreateList();
            int count = 0;
            for (int k = 0; k < oleCirculationDeskDetails.size(); k++) {
                if (oleCirculationDeskDetails.get(k).isDefaultLocation()) {
                    count++;
                }
            }
            if (count == 1 && !oleCirculationDeskDetailForm.getOperatorId().isEmpty()) {
                List<OleCirculationDeskDetail> newOleCirculationDeskDetails = new ArrayList<OleCirculationDeskDetail>();

                for (int i = 0; i < oleCirculationDeskDetails.size(); i++) {
                    if (!oleCirculationDeskDetails.get(i).isDefaultLocation() && !oleCirculationDeskDetails.get(i).isAllowedLocation()) {
                        getBusinessObjectService().delete(oleCirculationDeskDetails.get(i));
                    } else {
                        oleCirculationDeskDetails.get(i).setOperatorId(oleCirculationDeskDetailForm.getOperatorId());
                        newOleCirculationDeskDetails.add(oleCirculationDeskDetails.get(i));
                    }
                }

                getBusinessObjectService().save(newOleCirculationDeskDetails);
                //Success message set for circulation desk created
                StringBuffer newDeskDetailForm = new StringBuffer(OLEConstants.CRCL_DSK_SUCCESS);
                oleCirculationDeskDetailForm.setMessage(newDeskDetailForm.toString());
            } else if (oleCirculationDeskDetailForm.getOperatorId().isEmpty()) {
                StringBuffer selectBuffer = new StringBuffer(OLEConstants.CRCL_DSK_LOC_ERR);
                oleCirculationDeskDetailForm.setMessage(selectBuffer.toString());
                LOG.error(selectBuffer);
                return oleCirculationDeskDetailForm;
            } else {
                if (count == 0) {
                    StringBuffer defaultBuffer = new StringBuffer(OLEConstants.CRCL_DSK_NO_LOC_ERR);
                    oleCirculationDeskDetailForm.setMessage(defaultBuffer.toString());
                    LOG.error(defaultBuffer);
                    return oleCirculationDeskDetailForm;
                }
            }
            oleCirculationDeskDetailForm.setOleCirculationDetailsCreateList(populateCreateList());
            oleCirculationDeskDetailForm.setOperatorId(null);
            return oleCirculationDeskDetailForm;
        }
    }


    /**
     * This method is for searching the circulation details
     *
     * @param oleCirculationDeskDetailForm
     * @return oleCirculationDeskDetailForm
     */
    public OleCirculationDeskDetailForm performSearch(OleCirculationDeskDetailForm oleCirculationDeskDetailForm) {

        oleCirculationDeskDetailForm.setMessage(null);
        String operatorId = oleCirculationDeskDetailForm.getOperatorId();

        Map<String, String> opt = new HashMap<String, String>();
        opt.put("principalId", operatorId);
        List<PrincipalBo> matchedPrincipallList = (List<PrincipalBo>) getBusinessObjectService().findMatching(PrincipalBo.class, opt);
        if (matchedPrincipallList.size() == 0) {
            StringBuffer selectBuffer = new StringBuffer(OLEConstants.CRCL_DSK_INVALID_OPR);
            oleCirculationDeskDetailForm.setMessage(selectBuffer.toString());
            LOG.error(selectBuffer);
            return oleCirculationDeskDetailForm;
        }

        if (operatorId.isEmpty()) {
            StringBuffer selectBuffer = new StringBuffer(OLEConstants.CRCL_DSK_LOC_ERR);
            oleCirculationDeskDetailForm.setMessage(selectBuffer.toString());
            LOG.error(selectBuffer);
            return oleCirculationDeskDetailForm;
        } else {
            Map<String, String> detailMap = new HashMap<String, String>();
            detailMap.put("operatorId", oleCirculationDeskDetailForm.getOperatorId());
            List<OleCirculationDeskDetail> searchedCirculationDeskDetailList = (List<OleCirculationDeskDetail>) getBusinessObjectService().findMatching(OleCirculationDeskDetail.class, detailMap);
            List<OleCirculationDeskDetail> allCirculationDeskDetailList = populateCreateList();
            List<OleCirculationDeskDetail> mergedList = new ArrayList<OleCirculationDeskDetail>();
            mergedList.addAll(searchedCirculationDeskDetailList);
            for (int i = 0; i < searchedCirculationDeskDetailList.size(); i++) {
                for (int j = 0; j < allCirculationDeskDetailList.size(); j++) {
                    if (searchedCirculationDeskDetailList.get(i).getCirculationDeskId().equals(allCirculationDeskDetailList.get(j).getCirculationDeskId())) {
                        allCirculationDeskDetailList.remove(j);
                    }
                }
            }
            mergedList.addAll(allCirculationDeskDetailList);
            if (mergedList != null && mergedList.size() > 0) {
                for (int i = 0; i < mergedList.size(); i++) {
                    if (mergedList.get(i).getOleCirculationDesk() != null && !mergedList.get(i).getOleCirculationDesk().isActive()) {
                        mergedList.remove(i);
                    }
                }
            }
            oleCirculationDeskDetailForm.setOleCirculationDetailsCreateList(mergedList);
            return oleCirculationDeskDetailForm;
        }
    }

}

