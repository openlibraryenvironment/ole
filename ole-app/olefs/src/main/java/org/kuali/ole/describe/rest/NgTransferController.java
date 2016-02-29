package org.kuali.ole.describe.rest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.SearchResultDisplayFields;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by srirams on 7/1/16.
 */
@Controller
@RequestMapping("/ngTransferController")
public class NgTransferController {

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/transfer", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String transfer(@RequestBody String requestBody) throws JSONException {

        JSONObject requestObject = null;
        try {
            requestObject = new JSONObject(requestBody);
            String sourceDocType = (String) requestObject.get("sourceDocType");
            String selectedSourceId = (String) requestObject.get("sourceId");
            String selectedDestinationId = (String) requestObject.get("destinationId");
            List selectedSourceIds = new ArrayList<String>(1);
            selectedSourceIds.add(selectedSourceId);
            if(sourceDocType.contains(DocstoreConstants.DOC_TYPE_HOLDING_VALUE)){
                String message = checkHoldingsTransferable(selectedSourceId);
                if(message.length()>0){
                    requestObject.put("message", message);
                    return requestObject.toString();
                }
                getDocstoreClientLocator().getDocstoreClient().transferHoldings(selectedSourceIds , selectedDestinationId);
            }else if(sourceDocType.contains(DocstoreConstants.DOC_TYPE_ITEM_VALUE)){
                String message = checkItemTransferable(selectedSourceId);
                if(message.length()>0){
                    requestObject.put("message", message);
                    return requestObject.toString();
                }
                getDocstoreClientLocator().getDocstoreClient().transferItems(selectedSourceIds, selectedDestinationId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestObject.put("message", DocstoreConstants.TRANSFER_SUCCESS_MESSAGE);
        return requestObject.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/url", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String url() throws JSONException {

        JSONObject object = new JSONObject();;
        object.put("docstoreUrl", ConfigContext.getCurrentContextConfig().getProperty("ole.docstore.url.base"));
        return object.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buildResults", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String buildResults(@RequestParam("sourceDocType") String sourceDocType) throws JSONException {

        JSONObject requestObject = new JSONObject();
        DocumentSearchConfig documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig().reloadDocumentConfig();
        SearchResultDisplayFields searchResultDisplayFields = new SearchResultDisplayFields();
        searchResultDisplayFields.buildSearchResultDisplayFields(documentSearchConfig.getDocTypeConfigs(),sourceDocType);
        //requestObject.put
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(searchResultDisplayFields);
            System.out.print(json);
            return json.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestObject.toString();
    }

    private String checkHoldingsTransferable(String holdingsId) throws Exception {
        StringBuffer message = new StringBuffer();
        HoldingsTree holdingsTree = getDocstoreClientLocator().getDocstoreClient().retrieveHoldingsTree(holdingsId);
        if(holdingsTree!=null && holdingsTree.getHoldings().isBoundWithBib()){
            message.append(DocstoreConstants.TRANSFER_BOUND_WITH_ERROR_MESSAGE);
            return message.toString();
        }else if(holdingsTree!=null && holdingsTree.getHoldings().isSeries()){
            message.append(DocstoreConstants.TRANSFER_HOLDINGS_ANALYTIC_ERROR_MESSAGE);
            return message.toString();
        }else if(holdingsTree!=null && !holdingsTree.getHoldings().isSeries() && holdingsTree.getItems()!=null){
            Iterator<Item> iterator = holdingsTree.getItems().iterator();
            while(iterator.hasNext()){
                if(iterator.next().isAnalytic()){
                    message.append(DocstoreConstants.TRANSFER_HOLDINGS_ITEM_ANALYTIC_ERROR_MESSAGE);
                    break;
                }
            }
        }
        return message.toString();
    }

    private String checkItemTransferable(String itemId) throws Exception {
        StringBuffer message = new StringBuffer();
        Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemId);
        if(item.isAnalytic()){
            message.append(DocstoreConstants.TRANSFER_ITEM_ANALYTIC_ERROR_MESSAGE);
        }
        return message.toString();
    }


}
