package org.kuali.ole.dsng.rest.handler.items;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by SheikS on 12/26/2015.
 */
public class CreateItemHandler extends Handler {

    List<ItemHandler> itemMetaDataHandlers;

    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getOperationsList(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if (op.equals("131") || op.equals("231")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {

        try {
            HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get("holdings");
            if (null != holdingsRecord) {
                JSONObject holdingJsonObject = requestJsonObject.getJSONObject("items");
                ItemRecord itemRecord = new ItemRecord();
                exchange.add("itemRecord",itemRecord);

                JSONObject dataMappings = holdingJsonObject.getJSONObject("dataMapping");
                HashMap dataMappingsMap = new ObjectMapper().readValue(dataMappings.toString(), new TypeReference<Map<String, String>>() {
                });
                for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
                    String key1 = (String) iterator3.next();
                    for (Iterator<ItemHandler> iterator4 = getItemMetaDataHandlers().iterator(); iterator4.hasNext(); ) {
                        ItemHandler itemHandler = iterator4.next();
                        if (itemHandler.isInterested(key1)) {
                            itemHandler.setBusinessObjectService(getBusinessObjectService());
                            itemHandler.processDataMappings(dataMappings, exchange);
                        }
                    }
                }

                String updatedDateString = getStringValueFromJsonObject(requestJsonObject, "updatedDate");
                Timestamp updatedDate = getDateTimeStamp(updatedDateString);
                String updatedBy = getStringValueFromJsonObject(requestJsonObject,"updatedBy");
                itemRecord.setUpdatedBy(updatedBy);
                itemRecord.setUpdatedDate(updatedDate);
                itemRecord.setHoldingsId(holdingsRecord.getHoldingsId());
                itemRecord.setHoldingsRecord(holdingsRecord);
                itemRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML);

                getItemDAO().save(itemRecord);

                List createdItemDocuments = (List) exchange.get("itemRecordsToCreate");
                if(null == createdItemDocuments) {
                    createdItemDocuments = new ArrayList();
                }
                createdItemDocuments.add(itemRecord);

                exchange.add("itemRecordsToCreate", createdItemDocuments);
            }

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

    public List<ItemHandler> getItemMetaDataHandlers() {
        if(null == itemMetaDataHandlers){
            itemMetaDataHandlers = new ArrayList<ItemHandler>();
            itemMetaDataHandlers.add(new CallNumberHandler());
            itemMetaDataHandlers.add(new CallNumberPrefixHandler());
            itemMetaDataHandlers.add(new CallNumberTypeHandler());
            itemMetaDataHandlers.add(new ChronologyHandler());
            itemMetaDataHandlers.add(new CopyNumberHandler());
            itemMetaDataHandlers.add(new DonorCodeHandler());
            itemMetaDataHandlers.add(new DonorNoteHandler());
            itemMetaDataHandlers.add(new DonorPublicDisplayHandler());
            itemMetaDataHandlers.add(new EnumerationHandler());
            itemMetaDataHandlers.add(new ItemBarcodeHandler());
            itemMetaDataHandlers.add(new ItemStatusHandler());
            itemMetaDataHandlers.add(new ItemTypeHandler());
            itemMetaDataHandlers.add(new LocationHandler());
            itemMetaDataHandlers.add(new StatisticalSearchCodeHandler());
            itemMetaDataHandlers.add(new VendorLineItemIdHandler());
        }
        return itemMetaDataHandlers;
    }

    public void setItemMetaDataHandlers(List<ItemHandler> itemMetaDataHandlers) {
        this.itemMetaDataHandlers = itemMetaDataHandlers;
    }
}
