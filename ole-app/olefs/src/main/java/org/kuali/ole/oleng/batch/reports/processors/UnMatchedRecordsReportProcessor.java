package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.utility.MarcRecordUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by angelind on 2/10/16.
 */
public class UnMatchedRecordsReportProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse) exchange.getIn().getBody();
        String reportDirectory = ConfigContext.getCurrentContextConfig().getProperty("camel.report.directory");
        String date = OleNGConstants.DATE_FORMAT.format(new Date());
        String directoryName = oleNGBibImportResponse.getDirectoryName();
        if(CollectionUtils.isNotEmpty(oleNGBibImportResponse.getUnmatchedRecords())) {
            String unmatchedMarcRawContent = new MarcRecordUtil().convertMarcRecordListToRawMarcContent(oleNGBibImportResponse.getUnmatchedRecords());
            FileUtils.write(new File(reportDirectory, directoryName + File.separator + oleNGBibImportResponse.getBibImportProfileName() + "-UnMatched-" + date + ".mrc"), unmatchedMarcRawContent);
        }
        exchange.getOut().setBody(oleNGBibImportResponse);
    }
}
