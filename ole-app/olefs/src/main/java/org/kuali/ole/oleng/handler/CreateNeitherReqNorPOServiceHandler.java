package org.kuali.ole.oleng.handler;

import org.kuali.ole.Exchange;
import org.kuali.ole.oleng.service.OleNGMemorizeService;
import org.kuali.ole.oleng.service.impl.OleNGMemorizeServiceImpl;
import org.kuali.ole.pojo.OleOrderRecord;

import java.util.List;

/**
 * Created by SheikS on 2/16/2016.
 */
public class CreateNeitherReqNorPOServiceHandler implements CreateReqAndPOBaseServiceHandler {

    private OleNGMemorizeService oleNGMemorizeService;

    public Integer processOrder(List<OleOrderRecord> oleOrderRecords, Exchange exchange) throws Exception {
        return null;
    }

    public OleNGMemorizeService getOleNGMemorizeService() {
        if(null == oleNGMemorizeService) {
            oleNGMemorizeService = new OleNGMemorizeServiceImpl();
        }
        return oleNGMemorizeService;
    }

    public void setOleNGMemorizeService(OleNGMemorizeService oleNGMemorizeService) {
        this.oleNGMemorizeService = oleNGMemorizeService;
    }
}
