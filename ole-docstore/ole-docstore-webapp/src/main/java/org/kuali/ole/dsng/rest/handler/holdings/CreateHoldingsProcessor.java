package org.kuali.ole.dsng.rest.handler.holdings;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.eholdings.CreateEholdingsHandler;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class CreateHoldingsProcessor {
    BibDAO bibDAO;

    HoldingDAO holdingDAO;

    ItemDAO itemDAO;

    BusinessObjectService businessObjectService;

    List<Handler> handlers;

    public List<Handler> getHandlers() {
        if (null == handlers) {
            handlers = new ArrayList<Handler>();
            handlers.add(new CreateHoldingsHandler());
            handlers.add(new CreateEholdingsHandler());
        }
        return handlers;
    }

    public void processHoldings(JSONObject requestJsonObject, Exchange exchange) {
        try {
            String overlayOps = requestJsonObject.getString("overlayOps");
            HoldingsRecord holdingsRecord = new HoldingsRecord();
            exchange.add("holdingsRecord", holdingsRecord);

            createHoldingsOrEHoldigs(requestJsonObject,exchange, overlayOps);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createHoldingsOrEHoldigs(JSONObject requestJsonObject, Exchange exchange, String overlayOps) {
        for (Iterator<Handler> iterator = getHandlers().iterator(); iterator.hasNext(); ) {
            Handler handler = iterator.next();
            if(handler.isInterested(overlayOps)) {
                handler.setHoldingDAO(getHoldingDAO());
                handler.setItemDAO(getItemDAO());
                handler.setBusinessObjectService(getBusinessObjectService());
                handler.process(requestJsonObject,exchange);
            }

        }

    }

    public HoldingDAO getHoldingDAO() {
        return holdingDAO;
    }

    public void setHoldingDAO(HoldingDAO holdingDAO) {
        this.holdingDAO = holdingDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
