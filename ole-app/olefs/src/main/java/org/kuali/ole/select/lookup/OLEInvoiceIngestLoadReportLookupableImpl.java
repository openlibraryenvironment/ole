package org.kuali.ole.select.lookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.document.OLEInvoiceIngestLoadReport;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 3/5/14
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInvoiceIngestLoadReportLookupableImpl extends LookupableImpl{
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEInvoiceIngestLoadReportLookupableImpl.class);

    /**
     * This method will populate the search criteria and return the search results
     *
     * @param form
     * @param searchCriteria
     * @param unbounded
     * @return searchResults(list)
     */
    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        LOG.debug("Inside getSearchResults()");
        List<?> searchResults;
        List<OLEInvoiceIngestLoadReport> finalSearchResult = new ArrayList<OLEInvoiceIngestLoadReport>();
        List<OLEInvoiceIngestLoadReport> oleInvoiceIngestLoadDetails = new ArrayList<OLEInvoiceIngestLoadReport>();
        oleInvoiceIngestLoadDetails = (List<OLEInvoiceIngestLoadReport>) super.getSearchResults(form, searchCriteria, unbounded);
            finalSearchResult.addAll(oleInvoiceIngestLoadDetails);
        searchResults = finalSearchResult;
        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }
}
