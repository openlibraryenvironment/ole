/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.docstore.process;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.apache.camel.model.AggregateDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.SplitDefinition;
import org.apache.camel.model.ThreadsDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kuali.ole.docstore.process.ProcessParameters.*;

/**
 * Class BulkIngestNIndexRouteBuilder.
 *
 * @author Rajesh Chowdary K
 * @created Mar 15, 2012
 */
public class BulkIngestNIndexRouteBuilder
        extends RouteBuilder {

    private static Logger log = LoggerFactory.getLogger(BulkIngestNIndexProcessor.class);
    private String folder = null;
    private String user = null;
    private String action = null;
    private FileEndpoint fEPoint = null;
    private BulkIngestNIndexProcessor bulkIngestNIndexProcessor = null;

    /**
     * Constructor.
     *
     * @param folder
     * @param user
     * @param action
     * @param context
     */
    public BulkIngestNIndexRouteBuilder(String folder, String user, String action, CamelContext context) {
        super(context);
        this.folder = folder;
        this.user = user;
        this.action = action;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        log.debug("Loading Bulk Ingest Process: @" + folder);
        fEPoint = endpoint(
                "file:" + folder + "?noop=false&sortBy=file:name&move=.done&delay=" + BULK_INGEST_POLL_INTERVAL,
                FileEndpoint.class);
        fEPoint.setFilter(new BulkIngestFileFilter());
        RouteDefinition route = from(fEPoint);
        route.setId(folder);
        SplitDefinition split = route.split().tokenizeXML("ingestDocument");
        split.streaming();
        AggregateDefinition aggregator = split.aggregate(constant(true), new BodyAggregator());
        aggregator.setParallelProcessing(BULK_PROCESSOR_MULTI_THREADED);
        aggregator.completionPredicate(new SplitPredicate(BULK_PROCESSOR_SPLIT_SIZE));
        ThreadsDefinition threads = aggregator.threads(BULK_PROCESSOR_THREADS_MIN, BULK_PROCESSOR_THREADS_MAX);
        bulkIngestNIndexProcessor = new BulkIngestNIndexProcessor(user, action);
        threads.process(bulkIngestNIndexProcessor);
        threads.setThreadName("bulkIngest");
        route.setErrorHandlerBuilder(DocStoreCamelContext.getInstance().getErrorHandler());
        log.info("Loaded Bulk Ingest Process: @" + folder);
    }

    protected FileEndpoint getFileEndPoint() {
        return fEPoint;
    }

    public class BulkIngestFileFilter
            implements GenericFileFilter {

        @Override
        public boolean accept(GenericFile file) {
            return file.getFileName().toLowerCase().endsWith(".xml");
        }

    }

    public BulkIngestNIndexProcessor getBulkIngestNIndexProcessor() {
        return bulkIngestNIndexProcessor;
    }

    public void setBulkIngestNIndexProcessor(BulkIngestNIndexProcessor bulkIngestNIndexProcessor) {
        this.bulkIngestNIndexProcessor = bulkIngestNIndexProcessor;
    }
}
