package org.kuali.ole.oleng.service;

import org.kuali.ole.Exchange;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by SheikS on 1/6/2016.
 */
public interface OrderImportService {

    public OleOrderRecord prepareOleOrderRecord(RecordDetails recordDetails, BatchProcessProfile batchProcessProfile, Exchange exchange);
    public OleTxRecord processDataMapping(RecordDetails recordDetails, BatchProcessProfile batchProcessProfile, Exchange exchange);
    public void setOleNGMemorizeService(OleNGMemorizeService oleNGMemorizeService);
}
