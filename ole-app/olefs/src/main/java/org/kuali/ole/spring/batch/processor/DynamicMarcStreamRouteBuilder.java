package org.kuali.ole.spring.batch.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.model.AggregateDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.rice.krad.util.GlobalVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by SheikS on 4/4/2016.
 */
public class DynamicMarcStreamRouteBuilder extends RouteBuilder {
    private final String from;
    private FileEndpoint fEPoint = null;
    private final int BULK_INGEST_POLL_INTERVAL = 1500;
    private int chunkSize = 100;
    private MarcStreamingProcesssor marcStreamingProcesssor;
    private BatchProcessTxObject batchProcessTxObject;
    private RoutesDefinition routeCollection = new OleNGBatchCamelRoutesDefinition();
    private static Logger log = LoggerFactory.getLogger(DynamicMarcStreamRouteBuilder.class);
    public DynamicMarcStreamRouteBuilder(CamelContext context, String from, int chunkSize, BatchProcessTxObject batchProcessTxObject) {
        super(context);
        this.from = from;
        this.chunkSize = chunkSize;
        this.batchProcessTxObject = batchProcessTxObject;
    }

    @Override
    public void configure() throws Exception {
        log.info("Loading Bulk Ingest Process: @" + from);
        fEPoint = endpoint(
                "file:" + from + "?delete=true&idempotent=true&delay=" + BULK_INGEST_POLL_INTERVAL,
                FileEndpoint.class);
        RouteDefinition route = from(fEPoint);
        route.setId(from);
        OleNGBatchCamelSplitDefinition split = (OleNGBatchCamelSplitDefinition) route.split().tokenize(OleNGConstants.MARC_SPLIT);
        split.streaming();
        split.setObjectForCustomProcess(batchProcessTxObject);
        AggregateDefinition aggregator = split.aggregate(constant(true), new MarcStreamingBodyAggregator());
        aggregator.completionPredicate(new MarcStreamingSplitPredicate(chunkSize));
        aggregator.process(getMarcStreamingProcesssor());
    }

    public MarcStreamingProcesssor getMarcStreamingProcesssor() {
        if(null == marcStreamingProcesssor) {
            marcStreamingProcesssor = new MarcStreamingProcesssor(batchProcessTxObject, GlobalVariables.getUserSession());
        }
        return marcStreamingProcesssor;
    }

    @Override
    public RoutesDefinition getRouteCollection() {
        return routeCollection;
    }
}
