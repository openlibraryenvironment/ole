package org.kuali.ole.deliver.rest;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.deliver.controller.ItemBarcodeUpdateHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.SchemaOutputResolver;
import java.util.Map;

/**
 * Created by hemalathas on 1/20/16.
 */
@Controller
@RequestMapping("/oledsdata")
public class OleDsDataUpdateRestController  {

    @RequestMapping(method = RequestMethod.POST, value = "/item/update/barcode", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public String updateItemBarcode(@RequestBody String body) throws Exception {
        String responseString = "";
        JSONObject jsonObject = new JSONObject(body);
        String oldBarcode = jsonObject.getString("oldBarcode");
        String newBarcode = jsonObject.getString("newBarcode");
        String itemId = jsonObject.getString("itemId");
        ItemBarcodeUpdateHandler itemBarcodeUpdateHandler = new ItemBarcodeUpdateHandler();
        itemBarcodeUpdateHandler.updateItemBarcode(oldBarcode,newBarcode,itemId);
        return responseString;
    }
}
