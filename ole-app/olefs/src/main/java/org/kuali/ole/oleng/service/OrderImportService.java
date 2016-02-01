package org.kuali.ole.oleng.service;

import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by SheikS on 1/6/2016.
 */
public interface OrderImportService {

    public OleTxRecord processDataMapping(String bibId, BatchProcessProfile batchProcessProfile);
}
