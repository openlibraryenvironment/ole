package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.eholdings.CreateEholdingsHandler;
import org.kuali.ole.dsng.rest.handler.eholdings.UpdateEholdingsHandler;
import org.kuali.ole.dsng.rest.handler.items.UpdateItemRecord;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.io.IOException;
import java.util.*;

/**
 * Created by SheikS on 12/31/2015.
 */
public class UpdateHoldingsProcessor {

    BibDAO bibDAO;

    HoldingDAO holdingDAO;

    ItemDAO itemDAO;

    BusinessObjectService businessObjectService;

    public void processHoldings(JSONObject requestJsonObject, Exchange exchange) {
        List<HoldingsRecord> holdingsRecordsToUpdate = new ArrayList<HoldingsRecord>();

        try {
            String overlayOps = requestJsonObject.getString("overlayOps");
            BibRecord bibRecord = (BibRecord) exchange.get("bib");
            List<HoldingsRecord> holdingsRecords = bibRecord.getHoldingsRecords();

            for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();
                exchange.add("holdings",holdingsRecord);
                if(StringUtils.equals(holdingsRecord.getHoldingsType(), PHoldings.PRINT)) {
                    UpdateHoldingsHandler updateHoldingsHandler = new UpdateHoldingsHandler();
                    if(updateHoldingsHandler.isInterested(overlayOps)) {
                        updateHoldingsHandler.setHoldingDAO(getHoldingDAO());
                        updateHoldingsHandler.setItemDAO(getItemDAO());
                        updateHoldingsHandler.setBusinessObjectService(getBusinessObjectService());
                        updateHoldingsHandler.process(requestJsonObject,exchange);
                    } else {
                        createHolding(requestJsonObject,exchange,overlayOps);
                    }
                } else {
                    UpdateHoldingsHandler updateHoldingsHandler = new UpdateEholdingsHandler();
                    if(updateHoldingsHandler.isInterested(overlayOps)) {
                        updateHoldingsHandler.setHoldingDAO(getHoldingDAO());
                        updateHoldingsHandler.setItemDAO(getItemDAO());
                        updateHoldingsHandler.setBusinessObjectService(getBusinessObjectService());
                        updateHoldingsHandler.process(requestJsonObject,exchange);
                    } else {
                        createEHolding(requestJsonObject,exchange,overlayOps);
                    }
                }
                exchange.remove("holdings");
            }

            List holdingRecordsToUpdate = (List) exchange.get("holdingRecordsToUpdate");
            if(CollectionUtils.isNotEmpty(holdingRecordsToUpdate)){
                getHoldingDAO().saveAll(holdingRecordsToUpdate);
            }
            Boolean isHoldingsMatched = (Boolean) exchange.get("holdingsMatchFound");
            Boolean isEHoldingsMatchFound = (Boolean) exchange.get("eholdingsMatchFound");
            if(null != isHoldingsMatched && isHoldingsMatched.equals(Boolean.TRUE)) {
                processItems(requestJsonObject, exchange, overlayOps);
            } else {
                createHolding(requestJsonObject, exchange, overlayOps);
            }

            if(null == isEHoldingsMatchFound || isHoldingsMatched == Boolean.FALSE) {
                createEHolding(requestJsonObject, exchange, overlayOps);
            }
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

    private void processItems(JSONObject requestJsonObject, Exchange exchange, String overlayOps) {
        UpdateItemRecord updateItemRecord = new UpdateItemRecord();
        if(updateItemRecord.isInterested(overlayOps)) {
            updateItemRecord.setHoldingDAO(getHoldingDAO());
            updateItemRecord.setItemDAO(getItemDAO());
            updateItemRecord.setBusinessObjectService(getBusinessObjectService());
            updateItemRecord.process(requestJsonObject,exchange);
        }
    }

    public BibDAO getBibDAO() {
        return bibDAO;
    }

    public void setBibDAO(BibDAO bibDAO) {
        this.bibDAO = bibDAO;
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
