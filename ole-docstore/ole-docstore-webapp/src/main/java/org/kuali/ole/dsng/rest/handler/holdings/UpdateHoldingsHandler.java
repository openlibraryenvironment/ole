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
import java.util.*;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class UpdateHoldingsHandler extends Handler {
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
            if (op.equals("122") || op.equals("222")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        try {
            JSONObject holdingJsonObject = requestJsonObject.getJSONObject("holdings");
            if (holdingJsonObject.has("matchPoints")) {
                JSONObject matchPoints = holdingJsonObject.getJSONObject("matchPoints");
                HashMap map = new ObjectMapper().readValue(matchPoints.toString(), new TypeReference<Map<String, String>>() {
                });

                BibRecord bibRecord = (BibRecord) exchange.get("bib");
                List<HoldingsRecord> holdingsRecords = bibRecord.getHoldingsRecords();
                exchange.add("holdingRecords", holdingsRecords);
                for (Iterator iterator1 = map.keySet().iterator(); iterator1.hasNext(); ) {
                    String key = (String) iterator1.next();
                    for (Iterator<HoldingsHandler> iterator = getHoldingMetaDataHandlers().iterator(); iterator.hasNext(); ) {
                        Handler holdingsMetaDataHandlelr = iterator.next();

                        if (holdingsMetaDataHandlelr.isInterested(key)) {
                            holdingsMetaDataHandlelr.process(holdingJsonObject, exchange);
                        }
                    }
                }

                JSONObject dataMappings = holdingJsonObject.getJSONObject("dataMapping");

                HashMap dataMappingsMap = new ObjectMapper().readValue(dataMappings.toString(), new TypeReference<Map<String, String>>() {
                });
                for (Iterator iterator1 = dataMappingsMap.keySet().iterator(); iterator1.hasNext(); ) {
                    String key = (String) iterator1.next();
                    for (Iterator<HoldingsHandler> iterator = getHoldingMetaDataHandlers().iterator(); iterator.hasNext(); ) {
                        HoldingsHandler holdingsMetaDataHandlelr = iterator.next();
                        if (holdingsMetaDataHandlelr.isInterested(key)) {
                            holdingsMetaDataHandlelr.processDataMappings(dataMappings, exchange);
                        }
                    }
                }

            }

//            getHoldingDAO().saveAll(exchange.get("matchedHoldingRecords"));


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

    private Collection getCollletion(String values) {
        List queryValues = new ArrayList();

        StringTokenizer stringTokenizer = new StringTokenizer(values, ",");
        while (stringTokenizer.hasMoreTokens()) {
            queryValues.add(stringTokenizer.nextToken());
        }
        return queryValues;
    }
}
