package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by pvsubrah on 1/22/16.
 */
public class CreateHoldingsHanlder extends Handler {
    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getListFromJSONArray(operation);
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
        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.HOLDINGS_FOR_CREATE);
        Set<String> discardedBibForAdditionalOverlayOps = (Set<String>) exchange.get(OleNGConstants.DISCARDED_BIB_FOR_ADDITIONAL_OVERLAY_OPS);
        List<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        if (CollectionUtils.isNotEmpty(holdingsRecordAndDataMappings)) {
            for (Iterator<HoldingsRecordAndDataMapping> iterator = holdingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                try {
                    HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
                    HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                    String bibId = holdingsRecord.getBibRecords().get(0).getBibId();
                    if (StringUtils.isNotBlank(bibId)) {
                        if (!isDiscardedByAdditionalOverlayOps(discardedBibForAdditionalOverlayOps, bibId)) {
                            JSONObject dataMapping = holdingsRecordAndDataMapping.getDataMapping();
                            holdingsRecord.setBibId(bibId);
                            exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
                            JSONObject holdingsJSONObject = requestJsonObject.getJSONObject(OleNGConstants.HOLDINGS);
                            if (null != dataMapping) {
                                exchange.add(OleNGConstants.DATAMAPPING, dataMapping);
                                processDataMappings(holdingsJSONObject, exchange);
                            }
                            setCommonValuesToHoldingsRecord(requestJsonObject, holdingsRecord);
                            holdingsRecords.add(holdingsRecord);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_HOLDINGS, e, 1);
                }

            }
            exchange.remove(OleNGConstants.HOLDINGS_RECORD);
            exchange.remove(OleNGConstants.DATAMAPPING);
            try {
                getOleDsNGMemorizeService().getHoldingDAO().saveAll(holdingsRecords);
                for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                    HoldingsRecord holdingsRecord = iterator.next();
                    holdingsRecord.setOperationType(OleNGConstants.CREATED);
                }
            } catch (Exception e) {
                e.printStackTrace();
                addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_HOLDINGS, e, holdingsRecords.size());
            }
        }
    }


    private void setCommonValuesToHoldingsRecord(JSONObject requestJsonObject, HoldingsRecord holdingsRecord) {
        String createdDateString = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_DATE);
        Timestamp createdDate = getDateTimeStamp(createdDateString);
        String createdBy = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_BY);
        holdingsRecord.setCreatedBy(createdBy);
        holdingsRecord.setCreatedDate(createdDate);
        holdingsRecord.setUpdatedBy(createdBy);
        holdingsRecord.setUpdatedDate(createdDate);
        setHoldingType(holdingsRecord);
        if(holdingsRecord.getStaffOnlyFlag()==null){
            holdingsRecord.setStaffOnlyFlag(false);
        }
        holdingsRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML);
    }

    public void setHoldingType(HoldingsRecord holdingsRecord) {
        holdingsRecord.setHoldingsType(PHoldings.PRINT);
    }


    @Override
    public CopyOnWriteArrayList<Handler> getMetaDataHandlers() {
        if (null == metaDataHandlers) {
            metaDataHandlers = new CopyOnWriteArrayList<Handler>();
            metaDataHandlers.add(new HoldingsLocationHandler());
            metaDataHandlers.add(new CallNumberHandler());
            metaDataHandlers.add(new CallNumberTypeHandler());
            metaDataHandlers.add(new CallNumberPrefixHandler());
            metaDataHandlers.add(new CopyNumberHandler());
            metaDataHandlers.add(new AccessStatusHandler());
            metaDataHandlers.add(new SubscriptionStatusHandler());
            metaDataHandlers.add(new HoldingsStaffOnlyHandler());
            metaDataHandlers.add(new ShelvingOrderHandler());
        }
        return metaDataHandlers;
    }
}
