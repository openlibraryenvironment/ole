package org.kuali.ole.spring.batch.processor;

import org.codehaus.jettison.json.JSONException;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.marc4j.marc.Record;

import java.util.List;

/**
 * Created by SheikS on 12/9/2015.
 */
public class BatchItemFileProcessor extends BatchFileProcessor {

    @Override
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException {
        return null;
    }
}
