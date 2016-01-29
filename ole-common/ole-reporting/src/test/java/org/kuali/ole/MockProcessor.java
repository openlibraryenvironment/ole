package org.kuali.ole;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * Created by pvsubrah on 1/27/16.
 */
class MockProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        String messageBody = (String) in.getBody();
        System.out.println("Message body to be processed:" + messageBody);
    }
}