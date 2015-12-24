package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
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
        List<HoldingsRecord> holdingsRecordsToUpdate = new ArrayList<HoldingsRecord>();
        try {
            BibRecord bibRecord = (BibRecord) exchange.get("bib");
            List<HoldingsRecord> holdingsRecords = bibRecord.getHoldingsRecords();

            for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {

                JSONObject holdingJsonObject = requestJsonObject.getJSONObject("holdings");
                if (holdingJsonObject.has("matchPoints")) {
                    JSONObject matchPoints = holdingJsonObject.getJSONObject("matchPoints");
                    HashMap map = new ObjectMapper().readValue(matchPoints.toString(), new TypeReference<Map<String, String>>() {
                    });

                    HoldingsRecord holdingsRecord = iterator.next();
                    exchange.add("holdingsRecord", holdingsRecord);

                    matchPointsLoop:
                    for (Iterator iterator1 = map.keySet().iterator(); iterator1.hasNext(); ) {
                        String key = (String) iterator1.next();
                        for (Iterator<HoldingsHandler> iterator2 = getHoldingMetaDataHandlers().iterator(); iterator2.hasNext(); ) {
                            Handler holdingsMetaDataHandlelr = iterator2.next();
                            if (holdingsMetaDataHandlelr.isInterested(key)) {
                                holdingsMetaDataHandlelr.process(matchPoints, exchange);
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
                                    holdingsRecordsToUpdate.add(holdingsRecord);
                                    exchange.remove("matchedHoldings");
                                    break matchPointsLoop;
                                }
                            }
                        }
                    }
                }
            }

            if(CollectionUtils.isNotEmpty(holdingsRecordsToUpdate)) {
                getHoldingDAO().saveAll(holdingsRecordsToUpdate);
                Map<String, SolrInputDocument> solrInputDocumentMap = (Map<String, SolrInputDocument>) exchange.get("solrInputDocumentMap");
                if(null == solrInputDocumentMap) {
                    solrInputDocumentMap = new HashMap<String,SolrInputDocument>();
                }
                for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                    HoldingsRecord holdingsRecord = iterator.next();
                    solrInputDocumentMap = getHoldingIndexer().getInputDocumentForHoldings(holdingsRecord, solrInputDocumentMap);
                }
                exchange.add("solrInputDocumentMap",solrInputDocumentMap);
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

    private Collection getCollletion(String values) {
        List queryValues = new ArrayList();

        StringTokenizer stringTokenizer = new StringTokenizer(values, ",");
        while (stringTokenizer.hasMoreTokens()) {
            queryValues.add(stringTokenizer.nextToken());
        }
        return queryValues;
    }
}
