package org.kuali.ole.ncip.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.service.OLESIAPIHelperService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: suryar
 * Date: 20/9/13
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESIAPIHelperServiceImpl implements OLESIAPIHelperService {

    private ParameterService parameterService;

    public HashMap<String, String> getAgencyPropertyMap(String namespaceCode, String componentCode, String parameterName, String agencyId, HashMap<String, String> agencyPropertyMap) {
        String paramValue = getParameterService().getParameterValueAsString(namespaceCode, componentCode, parameterName);
        String[] agencyRecordArray = paramValue.split(";");
        String[] agencyValueArray = null;
        HashMap<String, HashMap<String, String>> agencyRecordMap = new HashMap<String, HashMap<String, String>>();

        for (int agencyRecordCounter = 0; agencyRecordCounter < agencyRecordArray.length; agencyRecordCounter++) {
            HashMap<String, String> agencyValueMap = new HashMap<String, String>();
            agencyValueArray = agencyRecordArray[agencyRecordCounter].split(",");
            agencyValueMap.put(OLENCIPConstants.OPERATOR_ID, agencyValueArray[1]);
            agencyValueMap.put(OLENCIPConstants.ITEM_TYPE, agencyValueArray[2]);
            agencyValueMap.put(OLENCIPConstants.ITEM_LOCATION, agencyValueArray[3]);
            agencyValueMap.put(OLENCIPConstants.REQUEST_TYPE, agencyValueArray[4]);
            agencyRecordMap.put(agencyValueArray[0], agencyValueMap);
        }
        if (agencyRecordMap.containsKey(agencyId)) {
            HashMap<String, String> agencyValueMap = agencyRecordMap.get(agencyId);
            agencyPropertyMap.put(OLENCIPConstants.OPERATOR_ID, agencyValueMap.get(OLENCIPConstants.OPERATOR_ID));
            agencyPropertyMap.put(OLENCIPConstants.ITEM_TYPE, agencyValueMap.get(OLENCIPConstants.ITEM_TYPE));
            agencyPropertyMap.put(OLENCIPConstants.ITEM_LOCATION, agencyValueMap.get(OLENCIPConstants.ITEM_LOCATION));
            agencyPropertyMap.put(OLENCIPConstants.REQUEST_TYPE, agencyValueMap.get(OLENCIPConstants.REQUEST_TYPE));
        }
        return agencyPropertyMap;
    }

    public ParameterService getParameterService() {
        if (parameterService == null) {
            parameterService = SpringContext.getBean(ParameterService.class);
        }
        return parameterService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

}
