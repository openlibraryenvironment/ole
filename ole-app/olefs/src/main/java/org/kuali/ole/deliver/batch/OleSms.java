package org.kuali.ole.deliver.batch;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.smpp.SmppConfiguration;
import org.apache.camel.component.smpp.SmppConstants;
import org.apache.camel.component.smpp.SmppSubmitSmCommand;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.log4j.Logger;
import org.jsmpp.bean.*;
import org.jsmpp.session.SMPPSession;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/16/13
 * Time: 7:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSms {
    private SMPPSession session;

    private SmppConfiguration config = new SmppConfiguration();
    private static final Logger LOG = Logger.getLogger(OleSms.class);
    private SmppSubmitSmCommand command = new SmppSubmitSmCommand(session, config);


    public void sendSms(String from, String to, String message) {
        Exchange exchange = new DefaultExchange(new DefaultCamelContext(), ExchangePattern.InOut);
        exchange.getIn().setHeader(SmppConstants.COMMAND, "SubmitSm");
        exchange.getIn().setHeader(SmppConstants.ID, "1");
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR_TON, TypeOfNumber.INTERNATIONAL.value());
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR_NPI, NumberingPlanIndicator.ISDN.value());
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR, from);
        exchange.getIn().setHeader(SmppConstants.DEST_ADDR_TON, TypeOfNumber.INTERNATIONAL.value());
        exchange.getIn().setHeader(SmppConstants.DEST_ADDR_NPI, NumberingPlanIndicator.ISDN.value());
        exchange.getIn().setHeader(SmppConstants.DEST_ADDR, to);
        exchange.getIn().setHeader(SmppConstants.SCHEDULE_DELIVERY_TIME, new Date(1111111));
        exchange.getIn().setHeader(SmppConstants.VALIDITY_PERIOD, new Date(2222222));
        exchange.getIn().setHeader(SmppConstants.PROTOCOL_ID, (byte) 1);
        exchange.getIn().setHeader(SmppConstants.PRIORITY_FLAG, (byte) 2);
        exchange.getIn().setHeader(SmppConstants.REGISTERED_DELIVERY, new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS).value());
        exchange.getIn().setHeader(SmppConstants.REPLACE_IF_PRESENT_FLAG, ReplaceIfPresentFlag.REPLACE.value());
        exchange.getIn().setBody(message);

        try {
            command.execute(exchange);
        } catch (Exception e) {
            LOG.error("Exception while sending sms", e);
        }
    }

}
