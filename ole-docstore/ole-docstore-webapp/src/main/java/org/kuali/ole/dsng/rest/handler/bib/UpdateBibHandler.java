package org.kuali.ole.dsng.rest.handler.bib;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.holdings.CreateHoldingsHandler;
import org.kuali.ole.dsng.rest.handler.holdings.UpdateHoldingsHandler;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class UpdateBibHandler extends Handler {
    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getOperationsList(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if(op.equals("112") || op.equals("212")){
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        try {
            String overlayOps = requestJsonObject.getString("overlayOps");
            String newBibContent = requestJsonObject.getString("modifiedContent");
            String updatedBy = requestJsonObject.getString("updatedBy");
            String updatedDateString = (String) requestJsonObject.get("updatedDate");

            if (requestJsonObject.has("id")) {
                String bibId = requestJsonObject.getString("id");
                BibRecord bibRecord = getBibDAO().retrieveBibById(bibId);
                bibRecord.setStatusUpdatedBy(updatedBy);
                bibRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC);

                Timestamp updatedDate = getDateTimeStamp(updatedDateString);

                bibRecord.setStatusUpdatedDate(updatedDate);

                String newContent = replaceBibIdTo001Tag(newBibContent, bibId);
                bibRecord.setContent(newContent);

                BibRecord updatedBibRecord = getBibDAO().save(bibRecord);
                exchange.add("bib", updatedBibRecord);

                processHoldings(requestJsonObject,exchange,overlayOps);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void processHoldings(JSONObject requestJsonObject, Exchange exchange,String overlayOps) {
        UpdateHoldingsHandler updateHoldingsHandler = new UpdateHoldingsHandler();
        if(updateHoldingsHandler.isInterested(overlayOps)) {
            updateHoldingsHandler.setHoldingDAO(getHoldingDAO());
            updateHoldingsHandler.setItemDAO(getItemDAO());
            updateHoldingsHandler.setBusinessObjectService(getBusinessObjectService());
            updateHoldingsHandler.process(requestJsonObject,exchange);
        } else {
            createHolding(requestJsonObject,exchange,overlayOps);
        }
    }

    private void createHolding(JSONObject requestJsonObject, Exchange exchange, String overlayOps) {
        CreateHoldingsHandler createHoldingsHandler = new CreateHoldingsHandler();
        if(createHoldingsHandler.isInterested(overlayOps)) {
            createHoldingsHandler.setHoldingDAO(getHoldingDAO());
            createHoldingsHandler.setItemDAO(getItemDAO());
            createHoldingsHandler.setBusinessObjectService(getBusinessObjectService());
            createHoldingsHandler.process(requestJsonObject,exchange);
        }
    }
}
