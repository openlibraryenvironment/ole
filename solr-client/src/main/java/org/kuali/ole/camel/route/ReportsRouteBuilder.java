package org.kuali.ole.camel.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.kuali.ole.Constants;
import org.kuali.ole.camel.processor.ReportProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sheiks on 14/02/17.
 */
@Component
public class ReportsRouteBuilder {
    Logger logger = LoggerFactory.getLogger(ReportsRouteBuilder.class);

    @Autowired
    public ReportsRouteBuilder(CamelContext camelContext, ReportProcessor reportProcessor) {
        try {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from(Constants.REPORT_Q)
                            .routeId(Constants.REPORT_ROUTE_ID)
                            .process(reportProcessor);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
