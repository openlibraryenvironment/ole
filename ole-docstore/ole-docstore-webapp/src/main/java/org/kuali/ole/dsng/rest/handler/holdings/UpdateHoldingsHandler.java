package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.dao.BibValidationDao;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.util.HoldingsUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class UpdateHoldingsHandler extends Handler {
    protected List<HoldingsHandler> holdingMetaDataHandlers;

    public List<HoldingsHandler> getHoldingMetaDataHandlers() {
        if (null == holdingMetaDataHandlers) {
            holdingMetaDataHandlers = new ArrayList<HoldingsHandler>();
            holdingMetaDataHandlers.add(new HoldingsLocationHandler());
            holdingMetaDataHandlers.add(new CallNumberHandler());
            holdingMetaDataHandlers.add(new CallNumberTypeHandler());
            holdingMetaDataHandlers.add(new CallNumberPrefixHandler());
            holdingMetaDataHandlers.add(new CopyNumberHandler());
            holdingMetaDataHandlers.add(new AccessStatusHandler());
            holdingMetaDataHandlers.add(new SubscriptionStatusHandler());
            holdingMetaDataHandlers.add(new HoldingsStaffOnlyHandler());
            holdingMetaDataHandlers.add(new ShelvingOrderHandler());
        }
        return holdingMetaDataHandlers;
    }

    public void setHoldingMetaDataHandlers(List<HoldingsHandler> holdingMetaDataHandlers) {
        this.holdingMetaDataHandlers = holdingMetaDataHandlers;
    }

    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getListFromJSONArray(operation);
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

        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.HOLDINGS_FOR_UPDATE);
        Set<String> discardedBibForAdditionalOverlayOps = (Set<String>) exchange.get(OleNGConstants.DISCARDED_BIB_FOR_ADDITIONAL_OVERLAY_OPS);
        List<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        if (CollectionUtils.isNotEmpty(holdingsRecordAndDataMappings)) {
            String updatedBy = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_BY);
            String updatedDateString = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_DATE);
            Timestamp updatedDate = getDateTimeStamp(updatedDateString);

            for (Iterator<HoldingsRecordAndDataMapping> iterator = holdingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                try {
                    HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
                    HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                    String bibId = holdingsRecord.getBibId();
                    if (!isDiscardedByAdditionalOverlayOps(discardedBibForAdditionalOverlayOps, bibId)) {
                        holdingsRecord.setUpdatedDate(updatedDate);
                        holdingsRecord.setUpdatedBy(updatedBy);
                        if(holdingsRecord.getStaffOnlyFlag()==null){
                            holdingsRecord.setStaffOnlyFlag(false);
                        }
                        exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
                        JSONObject dataMappingByValue = holdingsRecordAndDataMapping.getDataMapping();
                        if (null != dataMappingByValue) {
                            processOverlay(exchange, holdingsRecord, dataMappingByValue);
                        }
                        HoldingsUtil.getInstance().processIfDeleteAllExistOpsFound(holdingsRecord, requestJsonObject, exchange);
                        holdingsRecords.add(holdingsRecord);
                    }else {
                        Set<String> discardedHoldingsIdsForAdditionalOps = (Set<String>) exchange.get(OleNGConstants.DISCARDED_HOLDINGS_FOR_ADDITIONAL_OVERLAY_OPS);
                        if(null == discardedHoldingsIdsForAdditionalOps) {
                            discardedHoldingsIdsForAdditionalOps = new HashSet<String>();
                        }
                        discardedHoldingsIdsForAdditionalOps.add(holdingsRecord.getHoldingsId());
                        exchange.add(OleNGConstants.DISCARDED_HOLDINGS_FOR_ADDITIONAL_OVERLAY_OPS, discardedHoldingsIdsForAdditionalOps);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_HOLDINGS, e , 1);
                }
            }
            exchange.remove(OleNGConstants.HOLDINGS_RECORD);
            try {
                getOleDsNGMemorizeService().getHoldingDAO().saveAll(holdingsRecords);
                for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                    HoldingsRecord holdingsRecord = iterator.next();
                    holdingsRecord.setOperationType(OleNGConstants.UPDATED);
                }
            } catch (Exception e) {
                e.printStackTrace();
                addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_HOLDINGS, e , 1);
            }
        }
    }

    private HoldingsRecord processOverlay(Exchange exchange, HoldingsRecord holdingsRecord, JSONObject dataMapping) throws JSONException, IOException {
        Map<String, Object> dataMappingsMap = new ObjectMapper().readValue(dataMapping.toString(), new TypeReference<Map<String, Object>>() {});
        for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
            String key1 = (String) iterator3.next();
            for (Iterator<HoldingsHandler> iterator4 = getHoldingMetaDataHandlers().iterator(); iterator4.hasNext(); ) {
                HoldingsHandler holdingsMetaDataHandlelr1 = iterator4.next();
                if (holdingsMetaDataHandlelr1.isInterested(key1)) {
                    holdingsMetaDataHandlelr1.setBusinessObjectService(getBusinessObjectService());
                    holdingsMetaDataHandlelr1.processDataMappings(dataMapping, exchange);
                }
            }
        }
        exchange.remove(OleNGConstants.MATCHED_HOLDINGS);
        return holdingsRecord;
    }


}
