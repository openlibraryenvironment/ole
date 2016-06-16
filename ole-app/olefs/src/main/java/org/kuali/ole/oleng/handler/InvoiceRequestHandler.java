package org.kuali.ole.oleng.handler;

import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.service.OleNGInvoiceService;
import org.kuali.ole.utility.OleNgUtil;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SheikS on 12/18/2015.
 */
@Service("invoiceRequestHandler")
public class InvoiceRequestHandler extends OleNgUtil {

    @Autowired
    private OleNGInvoiceService oleNGInvoiceService;

    public String processInvoice(String requestBody) throws Exception {

        List<OleInvoiceRecord> oleInvoiceRecords = getObjectMapper().readValue(requestBody, new TypeReference<List<OleInvoiceRecord>>(){});
        OleInvoiceDocument oleInvoiceDocument = oleNGInvoiceService.createNewInvoiceDocument();
        Exchange exchange = new Exchange();
        oleNGInvoiceService.populateInvoiceDocWithOrderInformation(oleInvoiceDocument,oleInvoiceRecords, exchange);
        oleInvoiceDocument = oleNGInvoiceService.saveInvoiceDocument(oleInvoiceDocument);

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
