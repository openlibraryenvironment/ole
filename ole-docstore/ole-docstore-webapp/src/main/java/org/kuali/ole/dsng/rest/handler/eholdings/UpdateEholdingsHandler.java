package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.holdings.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/31/2015.
 */
public class UpdateEholdingsHandler extends Handler {
    protected List<HoldingsHandler> eholdingMetaDataHandlers;


    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getListFromJSONArray(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if (op.equals("142") || op.equals("242")) {
                return true;
            }
        }
        return false;
    }

    public List<HoldingsHandler> getEholdingMetaDataHandlers() {
        if (null == eholdingMetaDataHandlers) {
            eholdingMetaDataHandlers = new ArrayList<HoldingsHandler>();
            eholdingMetaDataHandlers.add(new HoldingsLocationHandler());
            eholdingMetaDataHandlers.add(new CallNumberHandler());
            eholdingMetaDataHandlers.add(new CallNumberTypeHandler());
            eholdingMetaDataHandlers.add(new CallNumberPrefixHandler());
            eholdingMetaDataHandlers.add(new CopyNumberHandler());
            eholdingMetaDataHandlers.add(new AccessLocationHandler());
            eholdingMetaDataHandlers.add(new AccessPasswordHandler());
            eholdingMetaDataHandlers.add(new AccessStatusHandler());
            eholdingMetaDataHandlers.add(new AccessUserNameHandler());
            eholdingMetaDataHandlers.add(new AccessPasswordHandler());
            eholdingMetaDataHandlers.add(new AdminUrlHandler());
            eholdingMetaDataHandlers.add(new AdminUserNameHandler());
            eholdingMetaDataHandlers.add(new AuthenticationTypeHandler());
            eholdingMetaDataHandlers.add(new CancellationDecisionDateHandler());
            eholdingMetaDataHandlers.add(new CancellationEffectiveDateHandler());
            eholdingMetaDataHandlers.add(new CancellationReasonHandler());
            eholdingMetaDataHandlers.add(new CoverageStartDateHandler());
            eholdingMetaDataHandlers.add(new CoverageStartIssueHandler());
            eholdingMetaDataHandlers.add(new CoverageStartVolumeHandler());
            eholdingMetaDataHandlers.add(new CoverageEndDateHandler());
            eholdingMetaDataHandlers.add(new CoverageEndIssueHandler());
            eholdingMetaDataHandlers.add(new CoverageEndVolumeHandler());
            eholdingMetaDataHandlers.add(new CurrentSubscriptionEndDateHandler());
            eholdingMetaDataHandlers.add(new CurrentSubscriptionStartDateHandler());
            eholdingMetaDataHandlers.add(new DonorCodeHandler());
            eholdingMetaDataHandlers.add(new DonorNoteHandler());
            eholdingMetaDataHandlers.add(new DonorPublicDisplayHandler());
            eholdingMetaDataHandlers.add(new EResourceIdHandler());
            eholdingMetaDataHandlers.add(new NoOfSumultaneousUserHander());
            eholdingMetaDataHandlers.add(new InitialSubscriptionEndDateHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessStartDateHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessStartIssueHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessStartVolumeHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessEndDateHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessEndIssueHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessEndVolumeHandler());
            eholdingMetaDataHandlers.add(new PersistentLinkHandler());
            eholdingMetaDataHandlers.add(new PlatformHandler());
            eholdingMetaDataHandlers.add(new ProxiedHandler());
            eholdingMetaDataHandlers.add(new PublisherHandler());
            eholdingMetaDataHandlers.add(new StatisticalSearchCodeHandler());
            eholdingMetaDataHandlers.add(new SubscriptionStatusHandler());
            eholdingMetaDataHandlers.add(new UrlHandler());
            eholdingMetaDataHandlers.add(new LinkTextHandler());
            eholdingMetaDataHandlers.add(new ImprintHandler());
            eholdingMetaDataHandlers.add(new NonPublicNoteHandler());
            eholdingMetaDataHandlers.add(new PublicNoteHandler());
            eholdingMetaDataHandlers.add(new NoOfSumultaneousUserHander());
        }
        return eholdingMetaDataHandlers;

    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.EHOLDINGS_FOR_UPDATE);
        List<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        if (CollectionUtils.isNotEmpty(holdingsRecordAndDataMappings)) {
            try {
                String updatedBy = requestJsonObject.getString(OleNGConstants.UPDATED_BY);
                String updatedDateString = (String) requestJsonObject.get(OleNGConstants.UPDATED_DATE);
                Timestamp updatedDate = getDateTimeStamp(updatedDateString);

                for (Iterator<HoldingsRecordAndDataMapping> iterator = holdingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                    HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
                    HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                    holdingsRecord.setUpdatedDate(updatedDate);
                    holdingsRecord.setUpdatedBy(updatedBy);
                    exchange.add(OleNGConstants.HOLDINGS_RECORD,holdingsRecord);
                    JSONObject dataMappingByValue = holdingsRecordAndDataMapping.getDataMapping();
                    if(null != dataMappingByValue) {
                        processOverlay(exchange, holdingsRecord, dataMappingByValue);
                    }
                    holdingsRecords.add(holdingsRecord);
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
            exchange.remove(OleNGConstants.HOLDINGS_RECORD);
            getHoldingDAO().saveAll(holdingsRecords);
        }
    }

    private HoldingsRecord processOverlay(Exchange exchange, HoldingsRecord holdingsRecord, JSONObject dataMapping) throws JSONException, IOException {
        Map<String, Object> dataMappingsMap = new ObjectMapper().readValue(dataMapping.toString(), new TypeReference<Map<String, Object>>() {});
        for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
            String key1 = (String) iterator3.next();
            for (Iterator<HoldingsHandler> iterator4 = getEholdingMetaDataHandlers().iterator(); iterator4.hasNext(); ) {
                HoldingsHandler holdingsMetaDataHandlelr1 = iterator4.next();
                if (holdingsMetaDataHandlelr1.isInterested(key1)) {
                    holdingsMetaDataHandlelr1.setBusinessObjectService(getBusinessObjectService());
                    holdingsMetaDataHandlelr1.processDataMappings(dataMapping, exchange);
                }
            }
        }
        exchange.remove(OleNGConstants.MATCHED_HOLDINGS);
        return  holdingsRecord;
    }

}
