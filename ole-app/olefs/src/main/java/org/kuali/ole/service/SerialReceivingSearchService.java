package org.kuali.ole.service;

import org.kuali.ole.describe.form.SerialsReceivingRecordForm;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.pojo.OLESerialReceivingRecord;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 4/3/14
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SerialReceivingSearchService {
    public SearchResponse holdingSearch(int startIndex, int searchLimit,String sortingOrder);


    public Set<String> getInstanceIdList(String poId);

    public SearchResponse searchDataFromDocstore(int startIndex, int searchLimit, Set<String> bibIds, HashMap<String,String> criteriaMap,String sortingOrder);

    public OLESerialReceivingRecord getSerialRecord(SearchResult searchResult);

    public List<OLESerialReceivingRecord> getOleSerialReceivingList(List<OLESerialReceivingDocument> oleSerialReceivingDocuments);

    public List<OLESerialReceivingDocument> getOleSerialReceivingDocuments(SerialsReceivingRecordForm serialsReceivingRecordForm);

    public List<OLESerialReceivingRecord> getOleSerialReceivingListFromHoldings(SearchResult searchResult);
}
