package org.kuali.ole.dsng.rest.handler.holdings;

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
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.items.CreateItemHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class CreateHoldingsHandler extends Handler {

    protected List<HoldingsHandler> holdingMetaDataHandlers;

    public List<HoldingsHandler> getHoldingMetaDataHandlers() {
        if (null == holdingMetaDataHandlers) {
            holdingMetaDataHandlers = new ArrayList<HoldingsHandler>();
            holdingMetaDataHandlers.add(new HoldingsLocationHandler());
            holdingMetaDataHandlers.add(new CallNumberHandler());
            holdingMetaDataHandlers.add(new CallNumberTypeHandler());
            holdingMetaDataHandlers.add(new CallNumberPrefixHandler());
            holdingMetaDataHandlers.add(new CopyNumberHandler());
        }
        return holdingMetaDataHandlers;
    }

    public void setHoldingMetaDataHandlers(List<HoldingsHandler> holdingMetaDataHandlers) {
        this.holdingMetaDataHandlers = holdingMetaDataHandlers;
    }


    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getOperationsList(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if (op.equals("121") || op.equals("221")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        try {
            BibRecord bibRecord = (BibRecord) exchange.get("bib");
            JSONObject holdingJsonObject = getHoldingsJsonObject(requestJsonObject);
            HoldingsRecord holdingsRecord = new HoldingsRecord();
            exchange.add("holdingsRecord", holdingsRecord);

            JSONObject dataMappings = holdingJsonObject.getJSONObject("dataMapping");

            HashMap dataMappingsMap = new ObjectMapper().readValue(dataMappings.toString(), new TypeReference<Map<String, String>>() {
            });
            for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
                String key1 = (String) iterator3.next();
                for (Iterator<HoldingsHandler> iterator4 = getHoldingMetaDataHandlers().iterator(); iterator4.hasNext(); ) {
                    HoldingsHandler holdingsMetaDataHandlelr1 = iterator4.next();
                    if (holdingsMetaDataHandlelr1.isInterested(key1)) {
                        holdingsMetaDataHandlelr1.setBusinessObjectService(getBusinessObjectService());
                        holdingsMetaDataHandlelr1.processDataMappings(dataMappings, exchange);
                    }
                }
            }

            String createdDateString = getStringValueFromJsonObject(requestJsonObject, "updatedDate");
            Timestamp createdDate = getDateTimeStamp(createdDateString);
            String createdBy = getStringValueFromJsonObject(requestJsonObject,"updatedBy");
            holdingsRecord.setCreatedBy(createdBy);
            holdingsRecord.setCreatedDate(createdDate);
            holdingsRecord.setBibId(bibRecord.getBibId());

            setHoldingType(holdingsRecord);

            holdingsRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML);
            holdingsRecord.setBibRecords(Collections.singletonList(bibRecord));

            getHoldingDAO().save(holdingsRecord);

            createItem(requestJsonObject, exchange,holdingsRecord);

            List createdHoldingsDocuments = (List) exchange.get("holdingRecordsToCreate");
            if(null == createdHoldingsDocuments) {
                createdHoldingsDocuments = new ArrayList();
            }
            createdHoldingsDocuments.add(holdingsRecord);

            exchange.add("holdingRecordsToCreate", createdHoldingsDocuments);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getHoldingsJsonObject(JSONObject requestJsonObject) throws JSONException {
        return requestJsonObject.getJSONObject("holdings");
    }

    public void createItem(JSONObject requestJsonObject, Exchange exchange, HoldingsRecord holdingsRecord) {
        exchange.add("holdings",holdingsRecord);
        CreateItemHandler createItemHandler = new CreateItemHandler();
        createItemHandler.setItemDAO(getItemDAO());
        createItemHandler.setBusinessObjectService(getBusinessObjectService());
        createItemHandler.process(requestJsonObject,exchange);
        exchange.remove("holdings");
    }

    public void setHoldingType(HoldingsRecord holdingsRecord) {
        holdingsRecord.setHoldingsType(PHoldings.PRINT);
    }
}
