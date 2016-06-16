package org.kuali.ole.dsng.dao;

import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SheikS on 12/14/2015.
 */
public class OleDsNGDAOBase {
    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if(null ==  businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    protected Map getHoldingsMap(String holdingsId) {
        Map map = new HashMap();
        map.put(OleNGConstants.HOLDINGS_ID, DocumentUniqueIDPrefix.getDocumentId(holdingsId));
        return map;
    }
}
