package org.kuali.ole.select.lookup;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLERoleBo;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hemalathas on 12/17/14.
 */
public class OLERoleLookupableImpl  extends LookupableImpl {

    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {

        List<?> searchResults;
        List<OLERoleBo> finalSearchResult = new ArrayList<OLERoleBo>();
        List<OLERoleBo> oleRoleBo = new ArrayList<OLERoleBo>();
        oleRoleBo = (List<OLERoleBo>) super.getSearchResults(form, searchCriteria, unbounded);
        finalSearchResult.addAll(oleRoleBo);

        searchResults = finalSearchResult;
        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }




}
