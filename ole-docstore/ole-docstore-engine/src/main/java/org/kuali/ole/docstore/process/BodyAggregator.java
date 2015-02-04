/*
 * Copyright 2012 The Kuali Foundation.
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

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.kuali.ole.docstore.utility.FileIngestStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to Aggregate message bodies.
 *
 * @author Rajesh Chowdary K
 * @created Jun 11, 2012
 */
public class BodyAggregator
        implements AggregationStrategy {
    private Logger logger = LoggerFactory.getLogger(BodyAggregator.class);
    private static Integer start = 0;
    private BulkIngestStatistics bulkIngestStatistics = BulkIngestStatistics.getInstance();
    private FileIngestStatistics fileIngestStatistics = null;

    public BodyAggregator() {
    }

    @SuppressWarnings("unchecked")
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        if (start.equals(newExchange.getProperty("CamelSplitIndex"))) {
            fileIngestStatistics = bulkIngestStatistics.startFile();
            bulkIngestStatistics.setFirstBatch(true);
            bulkIngestStatistics.setFileIngestStatistics(fileIngestStatistics);
            logger.info("Bulk ingest: File started : " + newExchange.getProperty("CamelFileExchangeFile"));
        }

        if (oldExchange == null) {
            oldExchange = new DefaultExchange(newExchange);
            oldExchange.getIn().setHeaders(newExchange.getIn().getHeaders());
            List<Object> body = new ArrayList<Object>();
            oldExchange.getIn().setBody(body);
            oldExchange.getExchangeId();
        }
        oldExchange.getIn().getBody(List.class).add(newExchange.getIn().getBody());

        for (String key : newExchange.getProperties().keySet()) {
            oldExchange.setProperty(key, newExchange.getProperty(key));
        }

        return oldExchange;
    }

    public FileIngestStatistics getFileIngestStatistics() {
        return fileIngestStatistics;
    }

    public void setFileIngestStatistics(FileIngestStatistics fileIngestStatistics) {
        this.fileIngestStatistics = fileIngestStatistics;
    }
}