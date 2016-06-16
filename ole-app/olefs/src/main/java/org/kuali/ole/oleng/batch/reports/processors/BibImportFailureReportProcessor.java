package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.BibFailureResponse;
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

    private MarcRecordUtil marcRecordUtil;

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse) object;
        List<BibFailureResponse> failureResponses = oleNGBibImportResponse.getFailureResponses();
        List<Record> marcRecords = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(failureResponses)) {
            Map<Integer,RecordDetails> recordsMap = oleNGBibImportResponse.getRecordsMap();
            if(null != recordsMap && recordsMap.size() > 0) {
                for(BibFailureResponse failureResponse : failureResponses) {
                    Integer index = failureResponse.getIndex();
                    if(null != index) {
                        RecordDetails recordDetails = recordsMap.get(index);
                        if (null != recordDetails) {
                            Record record = recordDetails.getRecord();
                            if (null != record) {
                                marcRecords.add(record);
                            }
                        }
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(marcRecords)) {
                String failedMarcContent = getMarcRecordUtil().convertMarcRecordListToRawMarcContent(marcRecords);
                writeMarcContent(directoryToWrite, failedMarcContent);
            }
            String bibFailureMessage = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(failureResponses);
            logMessage(directoryToWrite, "Bib-FailureMessages","txt", bibFailureMessage, false);
        }
    }

    public void writeMarcContent(String directoryToWrite, String failedMarcContent) throws Exception {
        logMessage(directoryToWrite, "Bib-FailedMarcRecords","mrc", failedMarcContent, true);
    }

    public MarcRecordUtil getMarcRecordUtil() {
        if(null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }
}
