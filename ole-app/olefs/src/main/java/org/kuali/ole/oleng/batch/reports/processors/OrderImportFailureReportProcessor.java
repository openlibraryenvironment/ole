package org.kuali.ole.oleng.batch.reports.processors;

import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.response.OleNGOrderImportResponse;
import org.kuali.ole.docstore.common.response.OrderFailureResponse;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by angelind on 3/15/16.
 */
public class OrderImportFailureReportProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGOrderImportResponse oleNGOrderImportResponse = (OleNGOrderImportResponse) object;
        List<OrderFailureResponse> orderFailureResponses = oleNGOrderImportResponse.getOrderFailureResponses();
        List<Record> marcRecords = new ArrayList<>();
        MarcRecordUtil marcRecordUtil = new MarcRecordUtil();
        if(CollectionUtils.isNotEmpty(orderFailureResponses)) {
            Map<Integer, RecordDetails> recordsMap = oleNGOrderImportResponse.getRecordsMap();
            if(null != recordsMap && recordsMap.size() > 0) {
                for(OrderFailureResponse orderFailureResponse : orderFailureResponses) {
                    Integer index = orderFailureResponse.getIndex();
                    if(null != index) {
                        RecordDetails recordDetails = recordsMap.get(index);
                        Record record = recordDetails.getRecord();
                        if (null != record) {
                            marcRecords.add(record);
                        }
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(marcRecords)) {
                String failedMarcContent = marcRecordUtil.convertMarcRecordListToRawMarcContent(marcRecords);
                logMessage(directoryToWrite, "Order-FailedMarcRecords", "mrc", failedMarcContent);
            }
            String orderFailureMessage = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(orderFailureResponses);
            logMessage(directoryToWrite, "Order-FailureMessages", "txt", orderFailureMessage);
        }
    }
}
