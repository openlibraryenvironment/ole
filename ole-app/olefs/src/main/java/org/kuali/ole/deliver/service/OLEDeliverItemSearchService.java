package org.kuali.ole.deliver.service;

import org.kuali.ole.deliver.bo.OLESingleItemResultDisplayRow;

import java.util.Map;

/**
 * Created by chenchulakshmig on 1/28/15.
 */
public interface OLEDeliverItemSearchService {

    public void setDeliverRequestInfo(Map itemIdMap, OLESingleItemResultDisplayRow singleItemResultDisplayRow);

    public void setOutstandingFineInfo(Map itemIdMap, OLESingleItemResultDisplayRow singleItemResultDisplayRow);

    public void setNoteInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow);

    public void setBorrowerInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow);

    public void setAdditionalCopiesInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow);

    public void setRequestHistoryInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow);

    public void setInTransitHistoryInfo(OLESingleItemResultDisplayRow singleItemResultDisplayRow);

}
