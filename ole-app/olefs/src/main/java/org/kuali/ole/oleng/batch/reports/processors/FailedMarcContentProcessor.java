package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.FileUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by angelind on 2/11/16.
 */
public class FailedMarcContentProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        BatchProcessFailureResponse batchProcessFailureResponse = (BatchProcessFailureResponse) exchange.getIn().getBody();
        String failedRawMarcContent = batchProcessFailureResponse.getFailedRawMarcContent();
        String reportDirectory = ConfigContext.getCurrentContextConfig().getProperty("camel.report.directory");
        String date = OleNGConstants.DATE_FORMAT.format(new Date());
        String directoryName = batchProcessFailureResponse.getDirectoryName();
        FileUtils.write(new File(reportDirectory, directoryName + File.separator + batchProcessFailureResponse.getBatchProcessProfileName() + "-FailedInputData-" + date + ".mrc"), failedRawMarcContent);
        exchange.getOut().setBody(batchProcessFailureResponse);
    }
}
