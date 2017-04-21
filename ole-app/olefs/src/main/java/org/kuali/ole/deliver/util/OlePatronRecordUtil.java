package org.kuali.ole.deliver.util;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronLostBarcode;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.drools.DroolsEngineLogger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 6/3/15.
 */
public class OlePatronRecordUtil extends CircUtilController {
    private static final Logger LOG = Logger.getLogger(OlePatronRecordUtil.class);
    private BusinessObjectService businessObjectService;

    public OlePatronDocument getPatronRecordByBarcode(String barcode) throws Exception {
        LOG.debug("Inside the getPatronBarcodeRecord method");
        StringBuffer values_StringBuffer = new StringBuffer();

        try {
            if (barcode.contains("*")){
                LOG.error(OLEConstants.PTRN_BARCD_NOT_EXT);
                throw new Exception(OLEConstants.PTRN_BARCD_NOT_EXT);
            }else{
                Map barMap = new HashMap();
                barMap.put(OLEConstants.OlePatron.BARCODE, barcode);
                List<OlePatronDocument> matching = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, barMap);

                if (matching != null && matching.size() > 0) {
                    OlePatronDocument olePatronDocument = matching.get(0);
                    if (GlobalVariables.getUserSession() != null) {
                        olePatronDocument.setPatronRecordURL(patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), olePatronDocument.getOlePatronId()));
                    }
                    return olePatronDocument;
                }else {
                    Map<String,String> lostBarCodeMap=new HashMap<>();
                    lostBarCodeMap.put(OLEConstants.OlePatron.PATRON_LOST_BARCODE_FLD, barcode);
                    List<OlePatronLostBarcode> lostBarcode = (List<OlePatronLostBarcode>) getBusinessObjectService().findMatching(OlePatronLostBarcode.class, lostBarCodeMap);
                    if(lostBarcode != null && lostBarcode.size() > 0){
                        LOG.error(OLEConstants.PTRN_LOST_BARCODE);
                        throw new Exception(OLEConstants.PTRN_LOST_BARCODE);
                    }else{
                        LOG.error(OLEConstants.PTRN_BARCD_NOT_EXT);
                        throw new Exception(OLEConstants.PTRN_BARCD_NOT_EXT);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() + e, e);
            values_StringBuffer.append(e.getMessage());
            throw new Exception(values_StringBuffer.toString());
        }

    }

    public String patronNameURL(String loginUser, String patronId) {
        boolean canEdit = canEdit(loginUser);
        String patronNameURL = "";
        if (canEdit) {
            patronNameURL = OLEConstants.ASSIGN_EDIT_PATRON_ID + patronId + OLEConstants.ASSIGN_PATRON_MAINTENANCE_EDIT;
        } else {
            patronNameURL = OLEConstants.ASSIGN_INQUIRY_PATRON_ID + patronId + OLEConstants.ASSIGN_PATRON_INQUIRY;
        }
        return patronNameURL;
    }

    public boolean canEdit(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.OlePatron.PATRON_NAMESPACE, OLEConstants.EDIT_PATRON);
    }

    public DroolsResponse fireRules(OlePatronDocument olePatronDocument, String[] expectedRules) {
        List facts = new ArrayList();
        facts.add(olePatronDocument);
        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(droolsResponse);

        fireRules(facts, expectedRules, "general-checks");

        return droolsResponse;
    }


    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


}
