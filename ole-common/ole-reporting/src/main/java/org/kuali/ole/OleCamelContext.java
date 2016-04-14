package org.kuali.ole;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.List;

/**
 * Created by pvsubrah on 1/10/15.
 */
public class OleCamelContext {
    public static OleCamelContext oleCamelContext;
    private CamelContext context;

    private OleCamelContext() {
        context = new DefaultCamelContext();
        try {
            context.start();
        } catch (Exception e) {
            System.out.println("Camel Context not initialized");
            e.printStackTrace();
        }
    }

    public static OleCamelContext getInstance() {
        if (null == oleCamelContext) {
            oleCamelContext = new OleCamelContext();
        }
        return oleCamelContext;
    }

    public void shutDown() throws Exception {
        getContext().stop();
    }

    public void start() throws Exception {
        getContext().start();
    }

    public CamelContext getContext() {
        return context;
    }

    public void addRoutes(RouteBuilder routeBuilder) throws Exception {
        getContext().addRoutes(routeBuilder);
    }

    public ProducerTemplate createProducerTemplate() {
        return getContext().createProducerTemplate();
    }

    public void addDynamicRoute(String endPoint1, String endPoint2, List<Processor> processors) throws Exception {
        addRoutes(new DynamicRouteBuilder(context, endPoint1, endPoint2, processors));
    }

}
