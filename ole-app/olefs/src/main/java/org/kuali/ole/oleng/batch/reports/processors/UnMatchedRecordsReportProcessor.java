package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.utility.MarcRecordUtil;

/**
 * Created by angelind on 2/10/16.
 */
public class UnMatchedRecordsReportProcessor extends OleNGReportProcessor {

    public void process(Object object, String directoryToWrite) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse)object;
        if(CollectionUtils.isNotEmpty(oleNGBibImportResponse.getUnmatchedRecords())) {
            String unmatchedMarcRawContent = new MarcRecordUtil().convertMarcRecordListToRawMarcContent(oleNGBibImportResponse.getUnmatchedRecords());
            logMessage(directoryToWrite,  oleNGBibImportResponse.getBibImportProfileName() + "-UnMatched","mrc", unmatchedMarcRawContent, true);
        }
    }
}
