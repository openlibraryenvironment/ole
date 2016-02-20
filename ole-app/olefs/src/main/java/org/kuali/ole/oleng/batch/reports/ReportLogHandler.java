package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.kuali.ole.DynamicRouteBuilder;
import org.kuali.ole.OleCamelContext;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by SheikS on 2/18/2016.
 */
public abstract class ReportLogHandler {
    protected ProducerTemplate sedaProducer;
    protected List<Processor> processors;
    protected String endpointFrom;
    protected String endpointToDirectory;
    protected DynamicRouteBuilder dynamicRouteBuilder;

    public abstract List<Processor> getProcessors();

    protected void initiateHander(String directoryName, String profileName, String reportType) {
        String fileName = reportType + OleNGConstants.TIMESTAMP_FOR_CAMEL + ".txt";
        endpointToDirectory = getReportDirectoryPath();
        endpointFrom = "seda:" + reportType + "ResponseQ_" + directoryName + "_" + profileName;
        String endPoint2 = "file:"+ endpointToDirectory + File.separator + directoryName + "?fileName=" + fileName;
        try {
            dynamicRouteBuilder = new DynamicRouteBuilder(OleCamelContext.getInstance().getContext(), endpointFrom, endPoint2, getProcessors());
            OleCamelContext.getInstance().getContext().addRoutes(dynamicRouteBuilder);
            if (null == sedaProducer) {
                sedaProducer = OleCamelContext.getInstance().createProducerTemplate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getReportDirectoryPath() {
        return ConfigContext.getCurrentContextConfig().getProperty("camel.report.directory");
    }

    public void logMessage(Object response) {
        sedaProducer.sendBody(endpointFrom, response);
        try {
            OleCamelContext.getInstance().getContext().stopRoute(endpointFrom);
            OleCamelContext.getInstance().getContext().removeRoute(endpointFrom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
