package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.utility.MarcRecordUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pvsubrah on 2/9/16.
 */
public class MatchedRecordsReportProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = (OleNGBibImportResponse) exchange.getIn().getBody();
        String projectHome = ConfigContext.getCurrentContextConfig().getProperty("project.home");
        String date = new SimpleDateFormat("yyyy-MMM-dd-hh-mm-ss-a").format(new Date());
        if(CollectionUtils.isNotEmpty(oleNGBibImportResponse.getMatchedRecords())) {
            String matchedMarcRawContent = new MarcRecordUtil().convertMarcRecordListToRawMarcContent(oleNGBibImportResponse.getMatchedRecords());
            FileUtils.write(new File(projectHome, "reports/" + oleNGBibImportResponse.getBibImportProfileName() + "-Matched-" + date + ".mrc"), matchedMarcRawContent);
        }
        exchange.getOut().setBody(oleNGBibImportResponse);
    }
}
