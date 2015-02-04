package org.kuali.ole.select.lookup;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleLicenseRequestStatus;
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
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLicenseRequestStatusLookupableImpl extends LookupableImpl {
    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        List<?> searchResults;
        List<OleLicenseRequestStatus> finalSearchResult = new ArrayList<OleLicenseRequestStatus>();
        List<OleLicenseRequestStatus> oleLicenseRequestStatus = new ArrayList<OleLicenseRequestStatus>();


        oleLicenseRequestStatus = (List<OleLicenseRequestStatus>) super.getSearchResults(form, searchCriteria, unbounded);
        finalSearchResult.addAll(oleLicenseRequestStatus);

        searchResults = finalSearchResult;

        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }
}
