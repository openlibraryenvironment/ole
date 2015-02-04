package org.kuali.ole.select.lookup;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleLicenseRequestType;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: asham
 * Date: 7/15/13
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLicenseRequestTypeLookupableImpl extends LookupableImpl {
    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        List<?> searchResults;
        List<OleLicenseRequestType> finalSearchResult = new ArrayList<OleLicenseRequestType>();
        List<OleLicenseRequestType> oleLicenseRequestType = new ArrayList<OleLicenseRequestType>();


        oleLicenseRequestType = (List<OleLicenseRequestType>) super.getSearchResults(form, searchCriteria, unbounded);
        finalSearchResult.addAll(oleLicenseRequestType);

        searchResults = finalSearchResult;

        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }
}
