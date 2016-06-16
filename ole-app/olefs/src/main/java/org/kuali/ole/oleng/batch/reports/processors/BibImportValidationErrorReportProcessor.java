package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 4/19/16.
 */
public class BibImportValidationErrorReportProcessor extends OleNGReportProcessor {

    @Override
    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse) object;
        List<Record> records = new ArrayList<>();
        MarcRecordUtil marcRecordUtil = new MarcRecordUtil();
        List<BibResponse> bibResponses = oleNGBibImportResponse.getBibResponses();
        if (CollectionUtils.isNotEmpty(bibResponses)) {
            Map<Integer, RecordDetails> recordsMap = oleNGBibImportResponse.getRecordsMap();
            List<ErrorMessage> errorMessages = new ArrayList<>();
            for (BibResponse bibResponse : bibResponses) {
                List<String> validationErrorMessages = bibResponse.getValidationErrorMessages();
                if (CollectionUtils.isNotEmpty(validationErrorMessages)) {
                    Integer recordIndex = bibResponse.getRecordIndex();
                    RecordDetails recordDetails = recordsMap.get(recordIndex);
                    if (null != recordDetails && recordDetails.getRecord() != null) {
                        records.add(recordDetails.getRecord());
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setIndex(recordIndex);
                        errorMessage.setValidationErrorMessages(validationErrorMessages);
                        errorMessages.add(errorMessage);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(records)) {
                String failedMarcContent = marcRecordUtil.convertMarcRecordListToRawMarcContent(records);
                logMessage(directoryToWrite, "Bib-ValidationErrorMarcRecords", "mrc", failedMarcContent, true);
            }
            if (CollectionUtils.isNotEmpty(errorMessages)) {
                String bibFailureMessage = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(errorMessages);
                logMessage(directoryToWrite, "Bib-ValidationErrorMessages", "txt",bibFailureMessage, false);
            }
        }

    }

    class ErrorMessage{
        private int index;
        private List<String> validationErrorMessages;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public List<String> getValidationErrorMessages() {
            return validationErrorMessages;
        }

        public void setValidationErrorMessages(List<String> validationErrorMessages) {
            this.validationErrorMessages = validationErrorMessages;
        }
    }
}
