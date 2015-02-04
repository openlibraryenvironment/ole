package org.kuali.ole.docstore.process;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/22/12
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class BulkIngestNIndexRouteBuilder_UT extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(BulkIngestNIndexRouteBuilder_UT.class);
    Exchange exchange = new DefaultExchange(new DefaultCamelContext(), ExchangePattern.InOut);

    @Test
    public void testBodyAggregator() {
        BodyAggregator bodyAggregator = new BodyAggregator();
        bodyAggregator.setFileIngestStatistics(bodyAggregator.getFileIngestStatistics());
        exchange.setProperty("CamelSplitIndex", new Integer("0"));
        bodyAggregator.aggregate(null, exchange);
    }

    @Test
    public void testBulkIngestNIndexProcessor() throws Exception {
        BulkIngestNIndexProcessor bulkIngestNIndexProcessor = new BulkIngestNIndexProcessor("user", "action");
        bulkIngestNIndexProcessor.setBulkLoadStatistics(bulkIngestNIndexProcessor.getBulkLoadStatistics());
        bulkIngestNIndexProcessor.setBulkProcessRequest(bulkIngestNIndexProcessor.getBulkProcessRequest());
        //bulkIngestNIndexProcessor.process(exchange);
    }

    @Test
    public void testSplitPredicate() {
        SplitPredicate splitPredicate = new SplitPredicate(new Integer("1000"));
        exchange.setProperty("CamelFileExchangeFile", new String("CamelFileExchangeFile"));
        exchange.setProperty("CamelSplitComplete", new Boolean("true"));
        exchange.setProperty("CamelAggregatedSize", new Integer("000"));
        splitPredicate.matches(exchange);
        exchange.setProperty("CamelFileExchangeFile", new String("CamelFileExchangeFile"));
        exchange.setProperty("CamelSplitComplete", new String("CamelSplitComplete"));
        exchange.setProperty("CamelAggregatedSize", new Integer("1000"));
        splitPredicate.matches(exchange);
        exchange.setProperty("CamelFileExchangeFile", new String("CamelFileExchangeFile"));
        exchange.setProperty("CamelSplitComplete", new String("CamelSplitComplete"));
        exchange.setProperty("CamelAggregatedSize", new Integer("000"));
        splitPredicate.matches(exchange);
    }

}
