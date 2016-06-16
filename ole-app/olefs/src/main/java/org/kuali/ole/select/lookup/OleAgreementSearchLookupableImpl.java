package org.kuali.ole.select.lookup;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleFeeType;
import org.kuali.ole.select.bo.OleAgreementRecord;
import org.kuali.ole.service.OleAgreementSearchService;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * OleAgreementSearchLookupableImpl is the view helper service class for Agreement Search
 */
public class OleAgreementSearchLookupableImpl extends LookupableImpl {

   /* OleAgreementSearchService agreementSearchService = new OleAgreementSearchService();

    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {

        Collection<?> displayList;

        LookupUtils.preprocessDateFields(searchCriteria);
        displayList = agreementSearchService.getAgreementInformation(searchCriteria);


        for (Object object : displayList) {
            if (isResultReturnable(object)) {
                form.setAtLeastOneRowReturnable(true);
            }
        }
        if (displayList.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        return displayList;
    }*/


    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {

        List<?> searchResults;
        List<OleAgreementRecord> finalSearchResult = new ArrayList<OleAgreementRecord>();
        List<OleAgreementRecord> oleAgreementRecords = new ArrayList<OleAgreementRecord>();
        oleAgreementRecords = (List<OleAgreementRecord>) super.getSearchResults(form, searchCriteria, unbounded);
        finalSearchResult.addAll(oleAgreementRecords);

        searchResults = finalSearchResult;
        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }
}
