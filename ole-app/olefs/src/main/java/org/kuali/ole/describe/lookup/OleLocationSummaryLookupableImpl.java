package org.kuali.ole.describe.lookup;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleLocationIngestSummaryRecord;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: SG7940
 * Date: 4/18/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleLocationSummaryLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLocationSummaryLookupableImpl.class);

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
        List<OleLocationIngestSummaryRecord> finalSearchResult = new ArrayList<OleLocationIngestSummaryRecord>();
        List<OleLocationIngestSummaryRecord> oleLocationIngestSummaryRecords = new ArrayList<OleLocationIngestSummaryRecord>();
        String dateField = searchCriteria.get(OLEConstants.OleLocationIngestSummaryRecord.DATE);
        if (dateField != null && !dateField.equals("")) {
            searchCriteria.remove(OLEConstants.OleLocationIngestSummaryRecord.DATE);
            oleLocationIngestSummaryRecords = (List<OleLocationIngestSummaryRecord>) super.getSearchResults(form, searchCriteria, unbounded);
            for (OleLocationIngestSummaryRecord oleLocationIngestSummaryRecord : oleLocationIngestSummaryRecords) {
                if (oleLocationIngestSummaryRecord.getDate() != null) {
                    Timestamp dateOnly = oleLocationIngestSummaryRecord.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.OleLocationIngestSummaryRecord.DATE_FORMAT);
                    String datestr = sdf.format(dateOnly);
                    if (datestr.equals(dateField)) {
                        finalSearchResult.add(oleLocationIngestSummaryRecord);
                    }
                }
            }
            searchCriteria.put(OLEConstants.OleLocationIngestSummaryRecord.DATE, dateField);
        } else {
            oleLocationIngestSummaryRecords = (List<OleLocationIngestSummaryRecord>) super.getSearchResults(form, searchCriteria, unbounded);
            finalSearchResult.addAll(oleLocationIngestSummaryRecords);
        }
        searchResults = finalSearchResult;
        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        sortSearchResults(form, searchResults);
        return searchResults;
    }
}
