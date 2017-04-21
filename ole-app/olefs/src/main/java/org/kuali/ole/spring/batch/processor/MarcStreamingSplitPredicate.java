package org.kuali.ole.spring.batch.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by SheikS on 4/4/2016.
 */
public class MarcStreamingSplitPredicate implements Predicate {
    private Logger logger = LoggerFactory.getLogger(MarcStreamingSplitPredicate.class);
    private Integer batchSize = 1000;

    public MarcStreamingSplitPredicate(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public boolean matches(Exchange exchange) {

        Object camelSplitComplete = exchange.getProperty("CamelSplitComplete");
        if (camelSplitComplete != null && Boolean.TRUE
                .equals(camelSplitComplete)) {
            logger.info("Processing End Of File: " + exchange.getProperty("CamelFileExchangeFile"));
            return true;
        }
        if (exchange.getProperty("CamelAggregatedSize").equals(batchSize)) {
            return true;
        }

        return false;
    }
}