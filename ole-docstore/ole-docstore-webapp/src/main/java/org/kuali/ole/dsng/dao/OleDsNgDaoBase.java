package org.kuali.ole.dsng.dao;

import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

/**
 * Created by SheikS on 12/7/2015.
 */
public class OleDsNgDAOBase {
    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if(null ==  businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
