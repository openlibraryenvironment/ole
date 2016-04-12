package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.utility.MarcRecordUtil;

/**
 * Created by pvsubrah on 2/9/16.
 */
public class MatchedRecordsReportProcessor extends OleNGReportProcessor {

    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse)object;
        if(CollectionUtils.isNotEmpty(oleNGBibImportResponse.getMatchedRecords())) {
            String matchedMarcRawContent = new MarcRecordUtil().convertMarcRecordListToRawMarcContent(oleNGBibImportResponse.getMatchedRecords());
            logMessage(directoryToWrite, oleNGBibImportResponse.getBibImportProfileName() + "-Matched","mrc", matchedMarcRawContent, true);
        }
    }
}
