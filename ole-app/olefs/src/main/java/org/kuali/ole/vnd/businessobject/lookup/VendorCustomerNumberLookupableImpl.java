package org.kuali.ole.vnd.businessobject.lookup;

import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.kns.web.ui.ResultRow;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 3/6/14
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class VendorCustomerNumberLookupableImpl extends LookupableImpl {

    @Override
    public Collection<?> performSearch(org.kuali.rice.krad.web.form.LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        List searchResults = (List) getLookupService().findCollectionBySearchHelper(getDataObjectClass(), searchCriteria, bounded);
        if(searchResults.size() == 0){
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, org.kuali.ole.OLEConstants.NO_RECORD_FOUND);
        }
        return searchResults;
    }

    @Override
    public Map<String, String> performClear(org.kuali.rice.krad.web.form.LookupForm form, Map<String, String> searchCriteria) {
        String vendorHeaderGeneratedIdentifier=searchCriteria.get(OLEConstants.VENDOR_HEADER_IDENTIFIER);
        String vendorDetailAssignedIdentifier=searchCriteria.get(OLEConstants.VENDOR_DETAIL_IDENTIFIER);
        super.performClear(form, searchCriteria);    //To change body of overridden methods use File | Settings | File Templates.
        Map<String, String> clearedSearchCriteria = new HashMap<String, String>();
        for(Map.Entry<String,String> map:searchCriteria.entrySet()){
            if(map.getKey() != null && map.getKey().equalsIgnoreCase(OLEConstants.VENDOR_HEADER_IDENTIFIER)){
                clearedSearchCriteria.put(OLEConstants.VENDOR_HEADER_IDENTIFIER,vendorHeaderGeneratedIdentifier);
            }
            if(map.getKey() != null && map.getKey().equalsIgnoreCase(OLEConstants.VENDOR_DETAIL_IDENTIFIER)){
                clearedSearchCriteria.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER,vendorDetailAssignedIdentifier);
            }
        }
        return clearedSearchCriteria;
    }
}
