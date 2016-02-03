package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        List<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        if (CollectionUtils.isNotEmpty(holdingsRecordAndDataMappings)) {
            for (Iterator<HoldingsRecordAndDataMapping> iterator = holdingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                JSONObject dataMapping = holdingsRecordAndDataMapping.getDataMapping();
                holdingsRecord.setBibId(holdingsRecord.getBibRecords().get(0).getBibId());
                exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
                try {
                    JSONObject holdingsJSONObject = requestJsonObject.getJSONObject(OleNGConstants.HOLDINGS);
                    exchange.add(OleNGConstants.DATAMAPPING, dataMapping);
                    processDataMappings(holdingsJSONObject, exchange);
                    setCommonValuesToHoldingsRecord(requestJsonObject, holdingsRecord);
                    holdingsRecords.add(holdingsRecord);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            exchange.remove(OleNGConstants.HOLDINGS_RECORD);
            exchange.remove(OleNGConstants.DATAMAPPING);
            getHoldingDAO().saveAll(holdingsRecords);
        }
    }


    private void setCommonValuesToHoldingsRecord(JSONObject requestJsonObject, HoldingsRecord holdingsRecord) {
        String createdDateString = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_DATE);
        Timestamp createdDate = getDateTimeStamp(createdDateString);
        String createdBy = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_BY);
        holdingsRecord.setCreatedBy(createdBy);
        holdingsRecord.setCreatedDate(createdDate);

        setHoldingType(holdingsRecord);

        holdingsRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML);
    }

    public void setHoldingType(HoldingsRecord holdingsRecord) {
        holdingsRecord.setHoldingsType(PHoldings.PRINT);
    }


    @Override
    public List<Handler> getMetaDataHandlers() {
        if (null == metaDataHandlers) {
            metaDataHandlers = new ArrayList<Handler>();
            metaDataHandlers.add(new HoldingsLocationHandler());
            metaDataHandlers.add(new CallNumberHandler());
            metaDataHandlers.add(new CallNumberTypeHandler());
            metaDataHandlers.add(new CallNumberPrefixHandler());
            metaDataHandlers.add(new CopyNumberHandler());
            metaDataHandlers.add(new AccessStatusHandler());
            metaDataHandlers.add(new SubscriptionStatusHandler());
            metaDataHandlers.add(new HoldingsStaffOnlyHandler());
        }
        return metaDataHandlers;
    }
}
