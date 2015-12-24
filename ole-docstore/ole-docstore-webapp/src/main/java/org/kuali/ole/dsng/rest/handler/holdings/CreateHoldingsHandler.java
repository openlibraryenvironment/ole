package org.kuali.ole.dsng.rest.handler.holdings;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class CreateHoldingsHandler extends Handler {

    List<HoldingsHandler> holdingMetaDataHandlers;

    public List<HoldingsHandler> getHoldingMetaDataHandlers() {
        if (null == holdingMetaDataHandlers) {
            holdingMetaDataHandlers = new ArrayList<HoldingsHandler>();
            holdingMetaDataHandlers.add(new LocationHandler());
            holdingMetaDataHandlers.add(new CallNumberHandler());
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
        List<HoldingsRecord> holdingsRecordsToCreate = new ArrayList<HoldingsRecord>();
        try {
            BibRecord bibRecord = (BibRecord) exchange.get("bib");
            JSONObject holdingJsonObject = requestJsonObject.getJSONObject("holdings");
            HoldingsRecord holdingsRecord = new HoldingsRecord();
            exchange.add("holdingsRecord", holdingsRecord);

            if (null != exchange.get("matchedHoldings")) {
                JSONObject dataMappings = holdingJsonObject.getJSONObject("dataMapping");

                HashMap dataMappingsMap = new ObjectMapper().readValue(dataMappings.toString(), new TypeReference<Map<String, String>>() {
                });
                for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
                    String key1 = (String) iterator3.next();
                    for (Iterator<HoldingsHandler> iterator4 = getHoldingMetaDataHandlers().iterator(); iterator4.hasNext(); ) {
                        HoldingsHandler holdingsMetaDataHandlelr1 = iterator4.next();
                        if (holdingsMetaDataHandlelr1.isInterested(key1)) {
                            holdingsMetaDataHandlelr1.processDataMappings(dataMappings, exchange);
                        }
                    }
                }

                String updatedDateString = getStringValueFromJsonObject(requestJsonObject, "updatedDate");
                Timestamp updatedDate = getDateTimeStamp(updatedDateString);
                String updatedBy = getStringValueFromJsonObject(requestJsonObject,"updatedBy");
                holdingsRecord.setUpdatedBy(updatedBy);
                holdingsRecord.setUpdatedDate(updatedDate);
                holdingsRecord.setBibId(bibRecord.getBibId());
                holdingsRecord.setBibRecords(Collections.singletonList(bibRecord));
                holdingsRecordsToCreate.add(holdingsRecord);
            }
            getHoldingDAO().saveAll(holdingsRecordsToCreate);
            exchange.add("holdingRecordsToCreate", holdingsRecordsToCreate);

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
}
