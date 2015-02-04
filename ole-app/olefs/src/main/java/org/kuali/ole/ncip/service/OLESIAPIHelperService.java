package org.kuali.ole.ncip.service;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: suryar
 * Date: 20/9/13
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OLESIAPIHelperService {

    public HashMap<String,String> getAgencyPropertyMap(String namespaceCode, String componentCode, String parameterName, String agencyId, HashMap<String,String> agencyPropertyMap);

}
