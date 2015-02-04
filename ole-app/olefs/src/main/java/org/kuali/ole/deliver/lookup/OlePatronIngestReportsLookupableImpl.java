package org.kuali.ole.deliver.lookup;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord;
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
 * OlePatronIngestReportsLookupableImpl makes validation  and populate the search criteria and return the search results
 */
public class OlePatronIngestReportsLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronIngestReportsLookupableImpl.class);


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
        List<OlePatronIngestSummaryRecord> finalSearchResult = new ArrayList<OlePatronIngestSummaryRecord>();
        List<OlePatronIngestSummaryRecord> olePatronIngestSummaryRecords = new ArrayList<OlePatronIngestSummaryRecord>();

        String dateField = searchCriteria.get(OLEConstants.OlePatronIngestLoadReport.CREATED_DATE);
        if (dateField != null && !dateField.equals("")) {
            searchCriteria.remove(OLEConstants.OlePatronIngestLoadReport.CREATED_DATE);
            String dateFieldOld = "";
            olePatronIngestSummaryRecords = (List<OlePatronIngestSummaryRecord>) super.getSearchResults(form, searchCriteria, unbounded);
            for (OlePatronIngestSummaryRecord olePatronIngestSummaryRecord : olePatronIngestSummaryRecords) {
                if (olePatronIngestSummaryRecord.getCreatedDate() != null) {
                    Timestamp dateOnly = olePatronIngestSummaryRecord.getCreatedDate();
                    SimpleDateFormat sdf = new SimpleDateFormat(OLEConstants.OlePatronIngestLoadReport.CREATED_DATE_FORMAT);
                    String datestr = sdf.format(dateOnly);
                    if (datestr.equals(dateField)) {
                        finalSearchResult.add(olePatronIngestSummaryRecord);
                    }
                }
            }
            searchCriteria.put(OLEConstants.OlePatronIngestLoadReport.CREATED_DATE, dateField);
        } else {
            olePatronIngestSummaryRecords = (List<OlePatronIngestSummaryRecord>) super.getSearchResults(form, searchCriteria, unbounded);
            finalSearchResult.addAll(olePatronIngestSummaryRecords);
        }
        searchResults = finalSearchResult;
        if (searchResults.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }

        sortSearchResults(form, searchResults);

        return searchResults;
    }

}
