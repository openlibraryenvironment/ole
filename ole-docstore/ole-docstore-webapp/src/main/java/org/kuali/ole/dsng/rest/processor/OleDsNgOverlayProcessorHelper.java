package org.kuali.ole.dsng.rest.processor;

import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.bib.CreateBibHandler;
import org.kuali.ole.dsng.rest.handler.bib.DiscardBibHandler;
import org.kuali.ole.dsng.rest.handler.bib.UpdateBibHandler;
import org.kuali.ole.dsng.rest.handler.eholdings.*;
import org.kuali.ole.dsng.rest.handler.eholdings.PublicNoteHandler;
import org.kuali.ole.dsng.rest.handler.holdings.*;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberPrefixHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberTypeHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CopyNumberHandler;
import org.kuali.ole.dsng.rest.handler.items.*;
import org.kuali.ole.dsng.rest.handler.items.DonorCodeHandler;
import org.kuali.ole.dsng.rest.handler.items.StatisticalSearchCodeHandler;
import org.kuali.ole.dsng.util.OleDsHelperUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by pvsubrah on 1/30/16.
 */
public class OleDsNgOverlayProcessorHelper extends OleDsHelperUtil {


    private CopyOnWriteArrayList<Handler> bibHandlers;
    private CopyOnWriteArrayList<Handler> holdingHandlers;
    private CopyOnWriteArrayList<Handler> eholdingHandlers;
    private CopyOnWriteArrayList<Handler> itemHandlers;
    private CopyOnWriteArrayList<HoldingsHandler> holdingMetaDataHandlers;
    private CopyOnWriteArrayList<HoldingsHandler> eholdingMetaDataHandlers;
    private CopyOnWriteArrayList<ItemHandler> itemMetaDataHandlers;



    public CopyOnWriteArrayList<Handler> getBibHandlers() {
        if (null == bibHandlers) {
            bibHandlers = new CopyOnWriteArrayList<Handler>();
            bibHandlers.add(new CreateBibHandler());
            bibHandlers.add(new UpdateBibHandler());
            bibHandlers.add(new DiscardBibHandler());
        }
        return bibHandlers;
    }

    public CopyOnWriteArrayList<HoldingsHandler> getHoldingMetaDataHandlers() {
        if (null == holdingMetaDataHandlers) {
            holdingMetaDataHandlers = new CopyOnWriteArrayList<HoldingsHandler>();
            holdingMetaDataHandlers.add(new HoldingsIdHandler());
            holdingMetaDataHandlers.add(new HoldingsLocationHandler());
            holdingMetaDataHandlers.add(new CallNumberHandler());
            holdingMetaDataHandlers.add(new CallNumberTypeHandler());
            holdingMetaDataHandlers.add(new CallNumberPrefixHandler());
            holdingMetaDataHandlers.add(new CopyNumberHandler());
        }
        return holdingMetaDataHandlers;
    }

    public CopyOnWriteArrayList<ItemHandler> getItemMetaDataHandlers() {
        if (null == itemMetaDataHandlers) {
            itemMetaDataHandlers = new CopyOnWriteArrayList<ItemHandler>();
            itemMetaDataHandlers.add(new ItemIdHandler());
            itemMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberHandler());
            itemMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberPrefixHandler());
            itemMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberTypeHandler());
            itemMetaDataHandlers.add(new ChronologyHandler());
            itemMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CopyNumberHandler());
            itemMetaDataHandlers.add(new DonorCodeHandler());
            itemMetaDataHandlers.add(new EnumerationHandler());
            itemMetaDataHandlers.add(new ItemHoldingLocationHandler());
            itemMetaDataHandlers.add(new ItemBarcodeHandler());
            itemMetaDataHandlers.add(new ItemStatusHandler());
            itemMetaDataHandlers.add(new ItemTypeHandler());
            itemMetaDataHandlers.add(new NoOfPiecesHandler());
            itemMetaDataHandlers.add(new ItemLocationHandler());
            itemMetaDataHandlers.add(new StatisticalSearchCodeHandler());
            itemMetaDataHandlers.add(new VendorLineItemIdHandler());
        }
        return itemMetaDataHandlers;
    }

    public CopyOnWriteArrayList<HoldingsHandler> getEholdingMetaDataHandlers() {

        if (null == eholdingMetaDataHandlers) {
            eholdingMetaDataHandlers = new CopyOnWriteArrayList<HoldingsHandler>();
            eholdingMetaDataHandlers.add(new HoldingsIdHandler());
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
            eholdingMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.eholdings.DonorCodeHandler());
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
            eholdingMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.eholdings.StatisticalSearchCodeHandler());
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

    public void setBibHandlers(CopyOnWriteArrayList<Handler> bibHandlers) {
        this.bibHandlers = bibHandlers;
    }

    public CopyOnWriteArrayList<Handler> getHoldingHandlers() {
        if (null == holdingHandlers) {
            holdingHandlers = new CopyOnWriteArrayList<Handler>();
            holdingHandlers.add(new CreateHoldingsHanlder());
            holdingHandlers.add(new UpdateHoldingsHandler());
            holdingHandlers.add(new DiscardHoldingsHandler());
        }
        return holdingHandlers;
    }

    public CopyOnWriteArrayList<Handler> getEHoldingHandlers() {
        if (null == eholdingHandlers) {
            eholdingHandlers = new CopyOnWriteArrayList<Handler>();
            eholdingHandlers.add(new CreateEHoldingsHandler());
            eholdingHandlers.add(new UpdateEholdingsHandler());
        }
        return eholdingHandlers;
    }

    public void setHoldingHandlers(CopyOnWriteArrayList<Handler> holdingHandlers) {
        this.holdingHandlers = holdingHandlers;
    }

    public CopyOnWriteArrayList<Handler> getItemHandlers() {
        if (null == itemHandlers) {
            itemHandlers = new CopyOnWriteArrayList<Handler>();
            itemHandlers.add(new CreateItemHandler());
            itemHandlers.add(new UpdateItemHandler());
        }
        return itemHandlers;
    }

    public void setItemHandlers(CopyOnWriteArrayList<Handler> itemHandlers) {
        this.itemHandlers = itemHandlers;
    }

}
