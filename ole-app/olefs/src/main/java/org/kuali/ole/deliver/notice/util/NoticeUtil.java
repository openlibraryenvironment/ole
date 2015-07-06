package org.kuali.ole.deliver.notice.util;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 7/2/15.
 */
public class NoticeUtil {
    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if(parameter==null){
            parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
            parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        }
        return parameter!=null?parameter.getValue():null;
    }


    public List<String> getList(String[] arrays) {
        List<String> resultList = new ArrayList<>();
        if (arrays != null && arrays.length > 0) {
            for (String arrayObj : arrays) {
                resultList.add(arrayObj);
            }
        }
        return resultList;
    }
    public Map<String, String> getMap(String[] arrays) {
        Map<String, String> resultMap = new HashMap<String, String>();
        if (arrays != null && arrays.length > 0) {
            for (String arrayObj : arrays) {
                resultMap.put(arrayObj, arrayObj);
            }
        }
        return resultMap;
    }
}
