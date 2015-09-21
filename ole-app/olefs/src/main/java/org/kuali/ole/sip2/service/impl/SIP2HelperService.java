package org.kuali.ole.sip2.service.impl;

import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.OleItemLevelBillPayment;
import org.kuali.ole.deliver.bo.OlePaymentStatus;
import org.kuali.ole.ncip.bo.OLEItemFine;
import org.kuali.ole.sip2.service.NettyServer;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public interface SIP2HelperService {

    public NettyServer startOLESip2Server(StringBuffer responseString, NettyServer olesip2Server);

    public void stopOLESip2Server(StringBuffer responseString, NettyServer olesip2Server);

    public void startOLESip2Server();

    public void stopOLESip2Server();

}
