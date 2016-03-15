package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.FailureResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by angelind on 3/15/16.
 */
public class BibImportFailureReportProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse) object;
        List<FailureResponse> failureResponses = oleNGBibImportResponse.getFailureResponses();
        List<Record> marcRecords = new ArrayList<>();
        MarcRecordUtil marcRecordUtil = new MarcRecordUtil();
        if(CollectionUtils.isNotEmpty(failureResponses)) {
            Map<Integer,RecordDetails> recordsMap = oleNGBibImportResponse.getRecordsMap();
            if(null != recordsMap && recordsMap.size() > 0) {
                for(FailureResponse failureResponse : failureResponses) {
                    Integer index = failureResponse.getIndex();
                    if(null != index) {
                        RecordDetails recordDetails = recordsMap.get(index);
                        marcRecords.add(recordDetails.getRecord());
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(marcRecords)) {
                String failedMarcContent = marcRecordUtil.convertMarcRecordListToRawMarcContent(marcRecords);
                logMessage(directoryToWrite, "Bib-FailedMarcRecords","mrc", failedMarcContent);
            }
            String bibFailureMessage = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(failureResponses);
            logMessage(directoryToWrite, "Bib-FailureMessages","txt", bibFailureMessage);
        }
    }
}
