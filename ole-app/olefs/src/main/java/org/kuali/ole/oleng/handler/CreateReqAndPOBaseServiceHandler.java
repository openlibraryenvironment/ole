package org.kuali.ole.oleng.handler;

import org.kuali.ole.pojo.OleOrderRecord;

import java.util.List;

/**
 * Created by pvsubrah on 1/14/16.
 */
public interface CreateReqAndPOBaseServiceHandler {
    public Integer processOrder(List<OleOrderRecord> oleOrderRecords) throws Exception;
}
