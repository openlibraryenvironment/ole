package org.kuali.ole.oleng.handler;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.oleng.OleNGConstants;
import org.kuali.ole.oleng.service.InvoiceService;
import org.kuali.ole.oleng.service.impl.InvoiceServiceImpl;
import org.kuali.ole.oleng.util.OleNgUtil;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 12/18/2015.
 */
@Service("invoiceRequestHandler")
public class InvoiceRequestHandler extends OleNgUtil {

    @Autowired
    private InvoiceService invoiceService;

    public String processInvoice(String requestBody) throws Exception {

        GlobalVariables.setUserSession(new UserSession("ole-quickstart"));

        List<OleInvoiceRecord> oleInvoiceRecords = getObjectMapper().readValue(requestBody, new TypeReference<List<OleInvoiceRecord>>(){});
        OleInvoiceDocument oleInvoiceDocument = invoiceService.createInvoiceDocument(oleInvoiceRecords);
        oleInvoiceDocument = invoiceService.saveInvoiceDocument(oleInvoiceDocument);

        JSONObject jsonObject = new JSONObject();
        if(null != oleInvoiceDocument.getDocumentNumber()) {
            jsonObject.put(OleNGConstants.STATUS,OleNGConstants.SUCCESS);
            jsonObject.put(OleNGConstants.DOCUMENT_NUMBER,oleInvoiceDocument.getDocumentNumber());
            return jsonObject.toString();
        }
        jsonObject.put(OleNGConstants.STATUS, OleNGConstants.FAILURE);
        return jsonObject.toString();
    }
}
