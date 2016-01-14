package org.kuali.ole.oleng.handler;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.service.OleNGRequisitionService;
import org.kuali.ole.oleng.util.OleNgUtil;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by SheikS on 12/18/2015.
 */
@Service("createReqAndPOServiceHandler")
public class CreateReqAndPOServiceHandler extends OleNgUtil implements CreateReqAndPOBaseServiceHandler {
    private OleNGRequisitionService oleNGRequisitionService;

    public String processOrder(OleOrderRecord oleOrderRecord) throws Exception {
        GlobalVariables.setUserSession(new UserSession("ole-quickstart"));
        OleRequisitionDocument requisitionDocument = getOleNGRequisitionService().createPurchaseOrderDocument(oleOrderRecord);
        JSONObject jsonObject = new JSONObject();
        if (null != requisitionDocument.getPurapDocumentIdentifier()) {
            jsonObject.put("status", "Success");
            jsonObject.put("requisitionId", requisitionDocument.getPurapDocumentIdentifier());
            return jsonObject.toString();
        }
        jsonObject.put("status", "failure");
        return jsonObject.toString();
    }

    public OleNGRequisitionService getOleNGRequisitionService() {
        return oleNGRequisitionService;
    }

    public void setOleNGRequisitionService(OleNGRequisitionService oleNGRequisitionService) {
        this.oleNGRequisitionService = oleNGRequisitionService;
    }
}
