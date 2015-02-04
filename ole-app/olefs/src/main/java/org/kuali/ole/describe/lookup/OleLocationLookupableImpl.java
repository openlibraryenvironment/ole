package org.kuali.ole.describe.lookup;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleLocation;
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
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLocationLookupableImpl extends LookupableImpl {
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        List<?> searchResults;
        List<OleLocation> finalSearchResult = new ArrayList<OleLocation>();
        List<OleLocation> oleLocation = new ArrayList<OleLocation>();


        oleLocation = (List<OleLocation>) super.getSearchResults(form, searchCriteria, unbounded);
        finalSearchResult.addAll(oleLocation);

        searchResults = finalSearchResult;

        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }
}
