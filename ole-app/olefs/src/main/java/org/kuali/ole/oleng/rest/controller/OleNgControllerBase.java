package org.kuali.ole.oleng.rest.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.utility.JSONHelperUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

/**
 * Created by SheikS on 12/16/2015.
 */
public class OleNgControllerBase extends JSONHelperUtil {
    private BusinessObjectService businessObjectService;
    private ObjectMapper objectMapper;

    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public ObjectMapper getObjectMapper() {
        if(null == objectMapper) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
