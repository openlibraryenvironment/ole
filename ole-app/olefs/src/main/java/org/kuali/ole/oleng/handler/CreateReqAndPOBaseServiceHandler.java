package org.kuali.ole.oleng.handler;

import org.kuali.ole.Exchange;
import org.kuali.ole.oleng.service.OleNGMemorizeService;
import org.kuali.ole.pojo.OleOrderRecord;

import java.util.List;

/**
 * Created by pvsubrah on 1/14/16.
 */
public interface CreateReqAndPOBaseServiceHandler {
    public Integer processOrder(List<OleOrderRecord> oleOrderRecords, Exchange exchange) throws Exception;
    public void setOleNGMemorizeService(OleNGMemorizeService oleNGMemorizeService);
    public OleNGMemorizeService getOleNGMemorizeService();
}
