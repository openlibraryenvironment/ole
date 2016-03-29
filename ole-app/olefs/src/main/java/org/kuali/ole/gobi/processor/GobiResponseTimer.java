package org.kuali.ole.gobi.processor;

import org.kuali.ole.gobi.dao.GobiDAO;
import org.kuali.ole.gobi.response.Response;
import org.kuali.ole.gobi.response.ResponseError;

/**
 * Created by pvsubrah on 9/8/15.
 */
public class GobiResponseTimer {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(GobiResponseTimer.class);

    public Response processReqResponseForPOCreation(String reqId, GobiDAO gobiDAO, String documentStatus) {
        Response gobiResponse = new Response();
        documentStatus = gobiDAO.getDocumentStatus(reqId);
        if (documentStatus.equals("F")) {
            String poId = gobiDAO.getPOId(reqId);
            if (null != poId) {
                LOG.info("Purchase Order Created for GOBI with PO ID: " + poId);
            }
            gobiResponse.setPoLineNumber(poId);
        } else if (documentStatus.equals("E")) {
            ResponseError responseError = new ResponseError();
            responseError.setMessage("PO Creation Failed.");
            responseError.setCode("PO_FAIL");
            gobiResponse.setError(responseError);
            LOG.error("Purchase Order record creation failed!");
        }
        return gobiResponse;
    }
}
