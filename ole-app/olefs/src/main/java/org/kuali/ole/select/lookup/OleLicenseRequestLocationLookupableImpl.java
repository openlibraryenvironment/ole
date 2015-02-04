package org.kuali.ole.select.lookup;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleLicenseRequestLocation;
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
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLicenseRequestLocationLookupableImpl extends LookupableImpl {
    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        List<?> searchResults;
        List<OleLicenseRequestLocation> finalSearchResult = new ArrayList<OleLicenseRequestLocation>();
        List<OleLicenseRequestLocation> oleLicenseRequestLocation = new ArrayList<OleLicenseRequestLocation>();


        oleLicenseRequestLocation = (List<OleLicenseRequestLocation>) super.getSearchResults(form, searchCriteria, unbounded);
        finalSearchResult.addAll(oleLicenseRequestLocation);

        searchResults = finalSearchResult;

        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }
}
