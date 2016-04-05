package org.kuali.ole.spring.batch.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.model.AggregateDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.SplitDefinition;
import org.kuali.ole.constants.OleNGConstants;

/**
 * Created by SheikS on 4/4/2016.
 */
public class DynamicMarcStreamRouteBuilder extends RouteBuilder {
    private final String from;
    private FileEndpoint fEPoint = null;
    private final int BULK_INGEST_POLL_INTERVAL = 1500;
    private int chunkSize = 100;
    private MarcStreamingProcesssor marcStreamingProcesssor;

    public DynamicMarcStreamRouteBuilder(CamelContext context, String from, int chunkSize) {
        super(context);
        this.from = from;
        this.chunkSize = chunkSize;
    }

    @Override
    public void configure() throws Exception {
        System.out.println("Loading Bulk Ingest Process: @" + from);
        fEPoint = endpoint(
                "file:" + from + "?delete=true&idempotent=true&delay=" + BULK_INGEST_POLL_INTERVAL,
                FileEndpoint.class);
        RouteDefinition route = from(fEPoint);
        route.setId(from);
        SplitDefinition split = route.split().tokenize(OleNGConstants.MARC_SPLIT);
        split.streaming();
        AggregateDefinition aggregator = split.aggregate(constant(true), new MarcStreamingBodyAggregator());
        aggregator.completionPredicate(new MarcStreamingSplitPredicate(chunkSize));
        aggregator.process(getMarcStreamingProcesssor());
    }

    public MarcStreamingProcesssor getMarcStreamingProcesssor() {
        if(null == marcStreamingProcesssor) {
            marcStreamingProcesssor = new MarcStreamingProcesssor();
        }
        return marcStreamingProcesssor;
    }
}
