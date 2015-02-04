package org.kuali.ole.deliver;

/**
 * Created with IntelliJ IDEA.
 * User: bala.km
 * Date: 11/26/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.smpp.SmppConfiguration;
import org.apache.camel.component.smpp.SmppConstants;
import org.apache.camel.component.smpp.SmppSubmitSmCommand;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.jsmpp.bean.*;
import org.jsmpp.session.SMPPSession;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.TimeZone;

import static junit.framework.Assert.assertTrue;


public class OleSMS_UT {

    private static TimeZone defaultTimeZone;

    @Mock
    private SMPPSession session;

    private SmppConfiguration config;
    private SmppSubmitSmCommand command;

    @BeforeClass
    public static void setUpBeforeClass() {
        defaultTimeZone = TimeZone.getDefault();

        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    @AfterClass
    public static void tearDownAfterClass() {
        if (defaultTimeZone != null) {
            TimeZone.setDefault(defaultTimeZone);
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        config = new SmppConfiguration();
        command = new SmppSubmitSmCommand(session, config);
    }

    @Test
    public void execute() throws Exception {
        Exchange exchange = new DefaultExchange(new DefaultCamelContext(), ExchangePattern.InOut);
        exchange.getIn().setHeader(SmppConstants.COMMAND, "SubmitSm");
        exchange.getIn().setHeader(SmppConstants.ID, "1");
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR_TON, TypeOfNumber.INTERNATIONAL.value());
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR_NPI, NumberingPlanIndicator.ISDN.value());
        exchange.getIn().setHeader(SmppConstants.SOURCE_ADDR, "1111111111");
        exchange.getIn().setHeader(SmppConstants.DEST_ADDR_TON, TypeOfNumber.INTERNATIONAL.value());
        exchange.getIn().setHeader(SmppConstants.DEST_ADDR_NPI, NumberingPlanIndicator.ISDN.value());
        exchange.getIn().setHeader(SmppConstants.DEST_ADDR, "2222222222");
        exchange.getIn().setHeader(SmppConstants.SCHEDULE_DELIVERY_TIME, new Date(1111111));
        exchange.getIn().setHeader(SmppConstants.VALIDITY_PERIOD, new Date(2222222));
        exchange.getIn().setHeader(SmppConstants.PROTOCOL_ID, (byte) 1);
        exchange.getIn().setHeader(SmppConstants.PRIORITY_FLAG, (byte) 2);
        exchange.getIn().setHeader(SmppConstants.REGISTERED_DELIVERY, new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS).value());
        exchange.getIn().setHeader(SmppConstants.REPLACE_IF_PRESENT_FLAG, ReplaceIfPresentFlag.REPLACE.value());
        exchange.getIn().setBody("short message body");

        try{
            command.execute(exchange);
        }catch(Exception e){
            assertTrue(false);
        }

    }

    @Test
    public void executeLongBody() throws Exception {

        Exchange exchange = new DefaultExchange(new DefaultCamelContext(), ExchangePattern.InOut);
        exchange.getIn().setHeader(SmppConstants.COMMAND, "SubmitSm");
        exchange.getIn().setHeader(SmppConstants.ID, "1");
        exchange.getIn().setBody("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");

        try{
            command.execute(exchange);
        }catch(Exception e){
            assertTrue(false);
        }

    }

}
