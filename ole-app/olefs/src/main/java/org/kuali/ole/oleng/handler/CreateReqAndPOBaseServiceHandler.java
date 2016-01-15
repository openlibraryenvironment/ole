package org.kuali.ole.oleng.handler;

import org.kuali.ole.pojo.OleOrderRecord;

/**
 * Created by pvsubrah on 1/14/16.
 */
public interface CreateReqAndPOBaseServiceHandler {
    public Integer processOrder(OleOrderRecord oleOrderRecord) throws Exception;
}
