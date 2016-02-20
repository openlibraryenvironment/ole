package org.kuali.ole;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 2/9/16.
 */
public class DynamicRouteBuilder extends RouteBuilder {
    private final String from;
    private final String to;
    private final List<Processor> processorList;

    public DynamicRouteBuilder(CamelContext context, String from, String to, List<Processor> processorList) {
        super(context);
        this.from = from;
        this.to = to;
        this.processorList = processorList;
    }

    @Override
    public void configure() throws Exception {
        RouteDefinition routeDefinition = from(from).routeId(from);

        for (Iterator<Processor> iterator = processorList.iterator(); iterator.hasNext(); ) {
            Processor processor = iterator.next();
            routeDefinition.process(processor);
        }

        routeDefinition.to(to);
    }
}
