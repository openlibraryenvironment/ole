package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.holdings.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by SheikS on 1/22/2016.
 */
public class CreateEHoldingsHandler extends Handler {

    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getListFromJSONArray(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if (op.equals("141") || op.equals("241")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.EHOLDINGS_FOR_CREATE);
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
                            JSONObject holdingsJSONObject = requestJsonObject.getJSONObject(OleNGConstants.EHOLDINGS);
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
                    addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_EHOLDINGS, e , 1);
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
                addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_EHOLDINGS, e , holdingsRecords.size());
            }
        }
    }


    private void setCommonValuesToHoldingsRecord(JSONObject requestJsonObject, HoldingsRecord holdingsRecord) {
        String createdDateString = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_DATE);
        Timestamp createdDate = getDateTimeStamp(createdDateString);
        String createdBy = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_BY);
        holdingsRecord.setCreatedBy(createdBy);
        holdingsRecord.setUpdatedBy(createdBy);
        holdingsRecord.setCreatedDate(createdDate);
        holdingsRecord.setUpdatedDate(createdDate);
        if(holdingsRecord.getStaffOnlyFlag()==null){
            holdingsRecord.setStaffOnlyFlag(false);
        }
        holdingsRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML);
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
            metaDataHandlers.add(new AccessLocationHandler());
            metaDataHandlers.add(new AccessPasswordHandler());
            metaDataHandlers.add(new AccessStatusHandler());
            metaDataHandlers.add(new AccessUserNameHandler());
            metaDataHandlers.add(new AccessPasswordHandler());
            metaDataHandlers.add(new AdminUrlHandler());
            metaDataHandlers.add(new AdminUserNameHandler());
            metaDataHandlers.add(new AuthenticationTypeHandler());
            metaDataHandlers.add(new CancellationDecisionDateHandler());
            metaDataHandlers.add(new CancellationEffectiveDateHandler());
            metaDataHandlers.add(new CancellationReasonHandler());
            metaDataHandlers.add(new CoverageStartDateHandler());
            metaDataHandlers.add(new CoverageStartIssueHandler());
            metaDataHandlers.add(new CoverageStartVolumeHandler());
            metaDataHandlers.add(new CoverageEndDateHandler());
            metaDataHandlers.add(new CoverageEndIssueHandler());
            metaDataHandlers.add(new CoverageEndVolumeHandler());
            metaDataHandlers.add(new CurrentSubscriptionEndDateHandler());
            metaDataHandlers.add(new CurrentSubscriptionStartDateHandler());
            metaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.eholdings.DonorCodeHandler());
            metaDataHandlers.add(new EResourceIdHandler());
            metaDataHandlers.add(new NoOfSumultaneousUserHander());
            metaDataHandlers.add(new InitialSubscriptionEndDateHandler());
            metaDataHandlers.add(new PerpetualAccessStartDateHandler());
            metaDataHandlers.add(new PerpetualAccessStartIssueHandler());
            metaDataHandlers.add(new PerpetualAccessStartVolumeHandler());
            metaDataHandlers.add(new PerpetualAccessEndDateHandler());
            metaDataHandlers.add(new PerpetualAccessEndIssueHandler());
            metaDataHandlers.add(new PerpetualAccessEndVolumeHandler());
            metaDataHandlers.add(new PersistentLinkHandler());
            metaDataHandlers.add(new PlatformHandler());
            metaDataHandlers.add(new ProxiedHandler());
            metaDataHandlers.add(new PublisherHandler());
            metaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.eholdings.StatisticalSearchCodeHandler());
            metaDataHandlers.add(new SubscriptionStatusHandler());
            metaDataHandlers.add(new UrlHandler());
            metaDataHandlers.add(new LinkTextHandler());
            metaDataHandlers.add(new ImprintHandler());
            metaDataHandlers.add(new NonPublicNoteHandler());
            metaDataHandlers.add(new PublicNoteHandler());
            metaDataHandlers.add(new NoOfSumultaneousUserHander());
            metaDataHandlers.add(new HoldingsStaffOnlyHandler());
            metaDataHandlers.add(new MaterialsSpecified());
            metaDataHandlers.add(new FirstIndicator());
            metaDataHandlers.add(new SecondIndicator());
            metaDataHandlers.add(new ShelvingOrderHandler());
        }
        return metaDataHandlers;
    }
}
