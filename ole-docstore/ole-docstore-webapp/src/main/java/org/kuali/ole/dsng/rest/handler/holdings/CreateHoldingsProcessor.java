package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.eholdings.CreateEholdingsHandler;
import org.kuali.ole.dsng.rest.handler.eholdings.UpdateEholdingsHandler;
import org.kuali.ole.dsng.rest.handler.items.UpdateItemRecord;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by SheikS on 12/31/2015.
 */
public class CreateHoldingsProcessor {
    BibDAO bibDAO;

    HoldingDAO holdingDAO;

    ItemDAO itemDAO;

    BusinessObjectService businessObjectService;

    public void processHoldings(JSONObject requestJsonObject, Exchange exchange) {
        List<HoldingsRecord> holdingsRecordsToUpdate = new ArrayList<HoldingsRecord>();

        try {
            String overlayOps = requestJsonObject.getString("overlayOps");
            HoldingsRecord holdingsRecord = new HoldingsRecord();
            exchange.add("holdingsRecord", holdingsRecord);

            createHolding(requestJsonObject,exchange, overlayOps);

            createEHolding(requestJsonObject,exchange, overlayOps);

        } catch (JSONException e) {
            e.printStackTrace();
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

    private void createEHolding(JSONObject requestJsonObject, Exchange exchange, String overlayOps) {
        CreateHoldingsHandler createHoldingsHandler = new CreateEholdingsHandler();
        if(createHoldingsHandler.isInterested(overlayOps)) {
            createHoldingsHandler.setHoldingDAO(getHoldingDAO());
            createHoldingsHandler.setItemDAO(getItemDAO());
            createHoldingsHandler.setBusinessObjectService(getBusinessObjectService());
            createHoldingsHandler.process(requestJsonObject,exchange);
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
