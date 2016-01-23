package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.eholdings.*;
import org.kuali.ole.dsng.rest.handler.items.UpdateItemHandler;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.io.IOException;
import java.util.*;

/**
 * Created by SheikS on 12/31/2015.
 */
public class UpdateHoldingsProcessor {

    BibDAO bibDAO;

    HoldingDAO holdingDAO;

    ItemDAO itemDAO;

    BusinessObjectService businessObjectService;

    protected List<HoldingsHandler> holdingMetaDataHandlers;


    public void processHoldings(JSONObject requestJsonObject, Exchange exchange) {
        List<HoldingsRecord> holdingsToUpdate = new ArrayList<HoldingsRecord>();
        try {
            String ops = requestJsonObject.getString(OleNGConstants.OPS);
            BibRecord bibRecord = (BibRecord) exchange.get(OleNGConstants.BIB);
            List<HoldingsRecord> holdingsRecords = bibRecord.getHoldingsRecords();

            JSONObject holdingJsonObject = requestJsonObject.getJSONObject(OleNGConstants.HOLDINGS);

            if (holdingJsonObject.has(OleNGConstants.MATCH_POINT)) {
                JSONObject matchPoints = holdingJsonObject.getJSONObject(OleNGConstants.MATCH_POINT);
                HashMap map = new ObjectMapper().readValue(matchPoints.toString(), new TypeReference<Map<String, String>>() {
                });

                for (Iterator iterator1 = map.keySet().iterator(); iterator1.hasNext(); ) {
                    String key = (String) iterator1.next();
                    for (Iterator<HoldingsHandler> iterator2 = getHoldingMetaDataHandlers().iterator(); iterator2.hasNext(); ) {
                        Handler holdingsMetaDataHandlelr = iterator2.next();
                        if (holdingsMetaDataHandlelr.isInterested(key)) {
                            holdingsMetaDataHandlelr.process(matchPoints, exchange);
                            HoldingsRecord holdings = (HoldingsRecord) exchange.get(OleNGConstants.MATCHED_HOLDINGS);

                        }
                    }
                }
            }



            for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();
                exchange.add(OleNGConstants.HOLDINGS, holdingsRecord);
                if (StringUtils.equals(holdingsRecord.getHoldingsType(), PHoldings.PRINT)) {
                    UpdateHoldingsHandler updateHoldingsHandler = new UpdateHoldingsHandler();
                    if (updateHoldingsHandler.isInterested(ops)) {
                        updateHoldingsHandler.setHoldingDAO(getHoldingDAO());
                        updateHoldingsHandler.setItemDAO(getItemDAO());
                        updateHoldingsHandler.setBusinessObjectService(getBusinessObjectService());
                        updateHoldingsHandler.process(requestJsonObject, exchange);
                    } else {
                        createHolding(requestJsonObject, exchange, ops);
                    }
                } else {
                    UpdateHoldingsHandler updateHoldingsHandler = new UpdateEholdingsHandler();
                    if (updateHoldingsHandler.isInterested(ops)) {
                        updateHoldingsHandler.setHoldingDAO(getHoldingDAO());
                        updateHoldingsHandler.setItemDAO(getItemDAO());
                        updateHoldingsHandler.setBusinessObjectService(getBusinessObjectService());
                        updateHoldingsHandler.process(requestJsonObject, exchange);
                    } else {
                        createEHolding(requestJsonObject, exchange, ops);
                    }
                }
                exchange.remove(OleNGConstants.HOLDINGS);
            }

            List holdingRecordsToUpdate = (List) exchange.get(OleNGConstants.HOLDINGS_UPDATED);
            getHoldingDAO().saveAll(holdingRecordsToUpdate);

            Boolean isHoldingsMatched = (Boolean) exchange.get(OleNGConstants.HOLDINGS_MATCH_FOUND);
            Boolean isEHoldingsMatchFound = (Boolean) exchange.get(OleNGConstants.EHOLDINGS_MATCH_FOUND);

            if (null != isHoldingsMatched && isHoldingsMatched.equals(Boolean.TRUE)) {
                processItems(requestJsonObject, exchange, ops);
            } else {
                createHolding(requestJsonObject, exchange, ops);
            }

            if (null == isEHoldingsMatchFound || isHoldingsMatched == Boolean.FALSE) {
                createEHolding(requestJsonObject, exchange, ops);
            }

            exchange.remove(OleNGConstants.HOLDINGS_UPDATED);
            exchange.remove(OleNGConstants.HOLDINGS_MATCH_FOUND);

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

    private void createHolding(JSONObject requestJsonObject, Exchange exchange, String ops) {
        CreateHoldingsHandler createHoldingsHandler = new CreateHoldingsHandler();
        if (createHoldingsHandler.isInterested(ops)) {
            createHoldingsHandler.setHoldingDAO(getHoldingDAO());
            createHoldingsHandler.setItemDAO(getItemDAO());
            createHoldingsHandler.setBusinessObjectService(getBusinessObjectService());
            createHoldingsHandler.process(requestJsonObject, exchange);
        }
    }

    private void createEHolding(JSONObject requestJsonObject, Exchange exchange, String ops) {
        CreateHoldingsHandler createHoldingsHandler = new CreateEholdingsHandler();
        if (createHoldingsHandler.isInterested(ops)) {
            createHoldingsHandler.setHoldingDAO(getHoldingDAO());
            createHoldingsHandler.setItemDAO(getItemDAO());
            createHoldingsHandler.setBusinessObjectService(getBusinessObjectService());
            createHoldingsHandler.process(requestJsonObject, exchange);
        }
    }

    private void processItems(JSONObject requestJsonObject, Exchange exchange, String ops) {
        UpdateItemHandler updateItemHandler = new UpdateItemHandler();
        if (updateItemHandler.isInterested(ops)) {
            updateItemHandler.setHoldingDAO(getHoldingDAO());
            updateItemHandler.setItemDAO(getItemDAO());
            updateItemHandler.setBusinessObjectService(getBusinessObjectService());
            updateItemHandler.process(requestJsonObject, exchange);
        }
    }

    public BibDAO getBibDAO() {
        return bibDAO;
    }

    public void setBibDAO(BibDAO bibDAO) {
        this.bibDAO = bibDAO;
    }

    public HoldingDAO getHoldingDAO() {
        return holdingDAO;
    }

    public void setHoldingDAO(HoldingDAO holdingDAO) {
        this.holdingDAO = holdingDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


    public List<HoldingsHandler> getHoldingMetaDataHandlers() {
        if (null == holdingMetaDataHandlers) {
            holdingMetaDataHandlers = new ArrayList<HoldingsHandler>();
            holdingMetaDataHandlers.add(new HoldingsLocationHandler());
            holdingMetaDataHandlers.add(new CallNumberHandler());
            holdingMetaDataHandlers.add(new CallNumberTypeHandler());
            holdingMetaDataHandlers.add(new CallNumberPrefixHandler());
            holdingMetaDataHandlers.add(new CopyNumberHandler());
            holdingMetaDataHandlers.add(new AccessLocationHandler());
            holdingMetaDataHandlers.add(new AccessPasswordHandler());
            holdingMetaDataHandlers.add(new AccessStatusHandler());
            holdingMetaDataHandlers.add(new AccessUserNameHandler());
            holdingMetaDataHandlers.add(new AccessPasswordHandler());
            holdingMetaDataHandlers.add(new AdminUrlHandler());
            holdingMetaDataHandlers.add(new AdminUserNameHandler());
            holdingMetaDataHandlers.add(new AuthenticationTypeHandler());
            holdingMetaDataHandlers.add(new CancellationDecisionDateHandler());
            holdingMetaDataHandlers.add(new CancellationEffectiveDateHandler());
            holdingMetaDataHandlers.add(new CancellationReasonHandler());
            holdingMetaDataHandlers.add(new CoverageStartDateHandler());
            holdingMetaDataHandlers.add(new CoverageStartIssueHandler());
            holdingMetaDataHandlers.add(new CoverageStartVolumeHandler());
            holdingMetaDataHandlers.add(new CoverageEndDateHandler());
            holdingMetaDataHandlers.add(new CoverageEndIssueHandler());
            holdingMetaDataHandlers.add(new CoverageEndVolumeHandler());
            holdingMetaDataHandlers.add(new CurrentSubscriptionEndDateHandler());
            holdingMetaDataHandlers.add(new CurrentSubscriptionStartDateHandler());
            holdingMetaDataHandlers.add(new DonorCodeHandler());
            holdingMetaDataHandlers.add(new DonorNoteHandler());
            holdingMetaDataHandlers.add(new DonorPublicDisplayHandler());
            holdingMetaDataHandlers.add(new EResourceIdHandler());
            holdingMetaDataHandlers.add(new NoOfSumultaneousUserHander());
            holdingMetaDataHandlers.add(new InitialSubscriptionEndDateHandler());
            holdingMetaDataHandlers.add(new PerpetualAccessStartDateHandler());
            holdingMetaDataHandlers.add(new PerpetualAccessStartIssueHandler());
            holdingMetaDataHandlers.add(new PerpetualAccessStartVolumeHandler());
            holdingMetaDataHandlers.add(new PerpetualAccessEndDateHandler());
            holdingMetaDataHandlers.add(new PerpetualAccessEndIssueHandler());
            holdingMetaDataHandlers.add(new PerpetualAccessEndVolumeHandler());
            holdingMetaDataHandlers.add(new PersistentLinkHandler());
            holdingMetaDataHandlers.add(new PlatformHandler());
            holdingMetaDataHandlers.add(new ProxiedHandler());
            holdingMetaDataHandlers.add(new PublisherHandler());
            holdingMetaDataHandlers.add(new StatisticalSearchCodeHandler());
            holdingMetaDataHandlers.add(new SubscriptionStatusHandler());
            holdingMetaDataHandlers.add(new UrlHandler());
            holdingMetaDataHandlers.add(new LinkTextHandler());
            holdingMetaDataHandlers.add(new ImprintHandler());
            holdingMetaDataHandlers.add(new NonPublicNoteHandler());
            holdingMetaDataHandlers.add(new PublicNoteHandler());
            holdingMetaDataHandlers.add(new NoOfSumultaneousUserHander());
        }
        return holdingMetaDataHandlers;

    }
}
