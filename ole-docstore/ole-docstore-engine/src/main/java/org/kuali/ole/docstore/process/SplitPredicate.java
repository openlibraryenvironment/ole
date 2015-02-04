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
import org.apache.camel.Predicate;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to predict split utility for processing.
 *
 * @author Rajesh Chowdary K
 * @created Jun 11, 2012
 */
public class SplitPredicate
        implements Predicate {
    private Logger logger = LoggerFactory.getLogger(SplitPredicate.class);
    private Integer batchSize = 1000;
    private BulkIngestStatistics bulkLoadStatistics = BulkIngestStatistics.getInstance();

    public SplitPredicate(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public boolean matches(Exchange exchange) {

        if (exchange.getProperty("CamelSplitComplete") != null && Boolean.TRUE
                .equals(exchange.getProperty("CamelSplitComplete"))) {
            logger.info("Processing End Of File: " + exchange.getProperty("CamelFileExchangeFile"));
            bulkLoadStatistics.setLastBatch(true);
            return true;
        }

        if (exchange.getProperty("CamelAggregatedSize").equals(batchSize)) {
            return true;
        }

        return false;
    }
}
