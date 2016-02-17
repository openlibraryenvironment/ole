package org.kuali.ole.describe.rest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.sys.context.SpringContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
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
                getDocstoreClientLocator().getDocstoreClient().transferHoldings(selectedSourceIds , selectedDestinationId);
            }else if(sourceDocType.contains(DocstoreConstants.DOC_TYPE_ITEM_VALUE)){
                getDocstoreClientLocator().getDocstoreClient().transferItems(selectedSourceIds, selectedDestinationId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DocstoreException e) {
            e.printStackTrace();
            if(e.getErrorMessage().equalsIgnoreCase(DocstoreResources.HOLDINGS_BIND_WITH_MULTIPLE_BIB)){
                requestObject.put("message", DocstoreConstants.TRANSFER_BOUND_WITH_ERROR_MESSAGE);
                return requestObject.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestObject.put("message", DocstoreConstants.TRANSFER_SUCCESS_MESSAGE);
        requestObject.toString();
        return requestObject.toString();
    }
}
