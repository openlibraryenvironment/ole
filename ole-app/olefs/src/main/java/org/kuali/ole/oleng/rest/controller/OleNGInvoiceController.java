package org.kuali.ole.oleng.rest.controller;

import org.kuali.ole.oleng.handler.InvoiceRequestHandler;
import org.kuali.ole.oleng.handler.OrderRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;

/**
 * Created by SheikS on 12/18/2015.
 */
@Controller
@RequestMapping("/invoice")
public class OleNGInvoiceController {

    @Autowired
    private InvoiceRequestHandler invoiceRequestHandler;

    @RequestMapping(method = RequestMethod.POST, value = "/createInvoice", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String createInvoice(@RequestBody String requestBody) throws Exception {
        return invoiceRequestHandler.processInvoice(requestBody);
    }
}
