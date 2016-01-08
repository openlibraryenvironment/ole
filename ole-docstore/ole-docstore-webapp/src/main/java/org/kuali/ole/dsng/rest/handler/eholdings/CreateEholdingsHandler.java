package org.kuali.ole.dsng.rest.handler.eholdings;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class CreateEholdingsHandler extends CreateHoldingsHandler {
    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getOperationsList(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if (op.equals("141") || op.equals("241")) {
                return true;
            }
        }
        return false;
    }

    @Override
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
            holdingMetaDataHandlers.add(new ImprintHandler());
            holdingMetaDataHandlers.add(new NonPublicNoteHandler());
            holdingMetaDataHandlers.add(new PublicNoteHandler());
            holdingMetaDataHandlers.add(new NoOfSumultaneousUserHander());
        }
        return holdingMetaDataHandlers;
    }

    @Override
    public JSONObject getHoldingsJsonObject(JSONObject requestJsonObject) throws JSONException {
        return requestJsonObject.getJSONObject("eholdings");
    }

    @Override
    public void setHoldingType(HoldingsRecord holdingsRecord) {
        holdingsRecord.setHoldingsType(EHoldings.ELECTRONIC);
    }

    @Override
    public void createItem(JSONObject requestJsonObject, Exchange exchange, HoldingsRecord holdingsRecord) {

    }
}
