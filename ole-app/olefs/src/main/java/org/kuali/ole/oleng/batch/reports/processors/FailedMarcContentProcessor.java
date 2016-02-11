package org.kuali.ole.oleng.batch.reports.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.FileUtils;
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
        String projectHome = ConfigContext.getCurrentContextConfig().getProperty("project.home");
        String date = new SimpleDateFormat("yyyy-MMM-dd-hh-mm-ss-a").format(new Date());
        FileUtils.write(new File(projectHome, "reports/failureReports/" + batchProcessFailureResponse.getBatchProcessProfileName() + "-FailedInputData-" + date + ".mrc"), failedRawMarcContent);
        exchange.getOut().setBody(batchProcessFailureResponse);
    }
}
