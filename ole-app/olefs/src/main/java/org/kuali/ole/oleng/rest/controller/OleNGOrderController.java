package org.kuali.ole.oleng.rest.controller;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.oleng.handler.OrderRequestHandler;
import org.kuali.ole.oleng.service.impl.RequisitionServiceImpl;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
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
@RequestMapping("/order")
public class OleNGOrderController extends OleNgControllerBase {

    @RequestMapping(method = RequestMethod.POST, value = "/createOrder", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String createOrder(@RequestBody String requestBody) throws Exception {
        return new OrderRequestHandler().processOrder(requestBody);
    }

}
