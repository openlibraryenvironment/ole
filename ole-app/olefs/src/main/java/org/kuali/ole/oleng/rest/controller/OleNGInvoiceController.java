package org.kuali.ole.oleng.rest.controller;

import org.kuali.ole.oleng.handler.InvoiceRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;

/**
 * Created by SheikS on 12/18/2015.
 */
public class OleNGInvoiceController extends OleNGOrderController {

    @Autowired
    private InvoiceRequestHandler invoiceRequestHandler;

    @RequestMapping(method = RequestMethod.POST, value = "/invoice/createInvoice", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String createInvoice(@RequestBody String requestBody) throws Exception {
        return invoiceRequestHandler.processInvoice(requestBody);
    }
}
