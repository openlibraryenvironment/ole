package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.InvoiceFailureResponse;
import org.kuali.ole.docstore.common.response.OleNGInvoiceImportResponse;
import org.kuali.ole.docstore.common.response.OrderFailureResponse;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by angelind on 3/16/16.
 */
public class InvoiceImportFailureReportProcessor extends OleNGReportProcessor {

    private MarcRecordUtil marcRecordUtil;

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGInvoiceImportResponse oleNGInvoiceImportResponse = (OleNGInvoiceImportResponse) object;
        List<InvoiceFailureResponse> invoiceFailureResponses = oleNGInvoiceImportResponse.getInvoiceFailureResponses();
        List<Record> marcRecords = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(invoiceFailureResponses)) {
            String fileExtension = oleNGInvoiceImportResponse.getFileExtension();
            if (StringUtils.isNotBlank(fileExtension) && fileExtension.equalsIgnoreCase(OleNGConstants.MARC)) {
                Map<Integer, RecordDetails> recordsMap = oleNGInvoiceImportResponse.getRecordsMap();
                if(null != recordsMap && recordsMap.size() > 0) {
                    for(InvoiceFailureResponse failureResponse : invoiceFailureResponses) {
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
            }
            String invoiceFailureMessage = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(invoiceFailureResponses);
            logMessage(directoryToWrite, "Invoice-FailureMessages", "txt", invoiceFailureMessage, false);
        }
    }

    public void writeMarcContent(String directoryToWrite, String failedMarcContent) throws Exception {
        logMessage(directoryToWrite, "Invoice-FailedMarcRecords", "mrc", failedMarcContent, true);
    }

    public MarcRecordUtil getMarcRecordUtil() {
        if(null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }
}
