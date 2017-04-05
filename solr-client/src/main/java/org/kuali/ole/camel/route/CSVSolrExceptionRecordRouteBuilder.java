package org.kuali.ole.camel.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.kuali.ole.Constants;
import org.kuali.ole.report.SolrExceptionReportCSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by sheiks on 14/02/17.
 */
@Component
public class CSVSolrExceptionRecordRouteBuilder {

    @Autowired
    public CSVSolrExceptionRecordRouteBuilder(CamelContext context, @Value("${solr.report.directory}") String matchingReportsDirectory) {
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from(Constants.CSV_SOLR_EXCEPTION_REPORT_Q)
                            .routeId(Constants.CSV_SOLR_EXCEPTION_REPORT_ROUTE_ID)
                            .marshal().bindy(BindyType.Csv, SolrExceptionReportCSVRecord.class)
                            .to("file:" + matchingReportsDirectory + File.separator + "?fileName=${in.header.fileName}-${date:now:ddMMMyyyy_hh-mm-ss-a}.csv")
                            .onCompletion().log("File has been created successfully.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

