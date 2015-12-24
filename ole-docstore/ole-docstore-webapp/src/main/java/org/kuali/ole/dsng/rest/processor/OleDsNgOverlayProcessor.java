package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.dao.OleNGPlatformAwareDao;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.bib.CreateBibHandler;
import org.kuali.ole.dsng.rest.handler.bib.UpdateBibHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberPrefixHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberTypeHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CopyNumberHandler;
import org.kuali.ole.dsng.rest.handler.holdings.*;
import org.kuali.ole.dsng.rest.handler.holdings.LocationHandler;
import org.kuali.ole.dsng.rest.handler.items.*;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgOverlayProcessor extends OleDsHelperUtil implements DocstoreConstants {
    @Autowired
    BibDAO bibDAO;

    @Autowired
    HoldingDAO holdingDAO;

    @Autowired
    ItemDAO itemDAO;

    private List<ItemOverlayHandler> itemOverlayMatchPointHandlers;

    private List<Handler> bibHandlers;
    private List<Handler> holdingHandlers;
    private List<Handler> itemHandlers;

    public List<Handler> getBibHandlers() {
        if (null == bibHandlers) {
            bibHandlers = new ArrayList<Handler>();
            bibHandlers.add(new CreateBibHandler());
            bibHandlers.add(new UpdateBibHandler());
            bibHandlers.add(new CreateHoldingsHandler());
            bibHandlers.add(new UpdateHoldingsHandler());
        }
        return bibHandlers;
    }

    public void setBibHandlers(List<Handler> bibHandlers) {
        this.bibHandlers = bibHandlers;
    }

    public List<Handler> getHoldingHandlers() {
        if (null == holdingHandlers) {
            holdingHandlers = new ArrayList<Handler>();
            holdingHandlers.add(new CreateHoldingsHandler());
            holdingHandlers.add(new UpdateHoldingsHandler());
        }
        return holdingHandlers;
    }

    public void setHoldingHandlers(List<Handler> holdingHandlers) {
        this.holdingHandlers = holdingHandlers;
    }

    public List<Handler> getItemHandlers() {
        return itemHandlers;
    }

    public void setItemHandlers(List<Handler> itemHandlers) {
        this.itemHandlers = itemHandlers;
    }

    public String processBibAndHoldingsAndItems(String jsonBody) {
        JSONArray responseJsonArray = null;
        Map<String,SolrInputDocument> solrInputDocumentMap = new HashedMap();
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            responseJsonArray = new JSONArray();
            for (int index = 0; index < requestJsonArray.length(); index++) {
                JSONObject requestJsonObject = requestJsonArray.getJSONObject(index);
                Exchange exchange = new Exchange();

                String overlayOps = requestJsonObject.getString("overlayOps");

                processBib(requestJsonObject, exchange, overlayOps);

                BibRecord bibRecord = (BibRecord) exchange.get("bib");

                processHoldings(requestJsonObject, exchange, overlayOps);

                if (null != bibRecord) {
                    solrInputDocumentMap = getBibIndexer().getInputDocumentForBib(bibRecord, solrInputDocumentMap);
                }
            }


            List<SolrInputDocument> solrInputDocuments = getBibIndexer().getSolrInputDocumentListFromMap(solrInputDocumentMap);
            getBibIndexer().commitDocumentToSolr(solrInputDocuments);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJsonArray.toString();
    }

    private void processHoldings(JSONObject requestJsonObject, Exchange exchange, String overlayOps) {
        for (Iterator<Handler> iterator = getHoldingHandlers().iterator(); iterator.hasNext(); ) {
            Handler holdingHandler = iterator.next();
            if(holdingHandler.isInterested(overlayOps)){
                holdingHandler.setHoldingDAO(holdingDAO);
                holdingHandler.process(requestJsonObject, exchange);
            }
        }
    }

    private void processBib(JSONObject requestJsonObject, Exchange exchange, String overlayOps) {
        for (Iterator<Handler> iterator = getBibHandlers().iterator(); iterator.hasNext(); ) {
            Handler handler = iterator.next();
            if(handler.isInterested(overlayOps)){
                handler.setBibDAO(bibDAO);
                handler.setBusinessObjectService(getBusinessObjectService());
                handler.process(requestJsonObject, exchange);
            }
        }
    }

    public String getStringValueFromJsonObject(JSONObject jsonObject, String key) {
        String returnValue = null;
        try {
            returnValue = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public List<ItemOverlayHandler> getItemOverlayMatchPointHandlers() {
        if(CollectionUtils.isEmpty(itemOverlayMatchPointHandlers)) {
            itemOverlayMatchPointHandlers = new ArrayList<ItemOverlayHandler>();
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberHandler());
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberPrefixHandler());
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberTypeHandler());
            itemOverlayMatchPointHandlers.add(new ChronologyHandler());
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CopyNumberHandler());
            itemOverlayMatchPointHandlers.add(new DonorCodeHandler());
            itemOverlayMatchPointHandlers.add(new DonorNoteHandler());
            itemOverlayMatchPointHandlers.add(new DonorPublicDisplayHandler());
            itemOverlayMatchPointHandlers.add(new EnumerationHandler());
            itemOverlayMatchPointHandlers.add(new ItemBarcodeHandler());
            itemOverlayMatchPointHandlers.add(new ItemStatusHandler());
            itemOverlayMatchPointHandlers.add(new ItemTypeHandler());
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.LocationHandler());
            itemOverlayMatchPointHandlers.add(new StatisticalSearchCodeHandler());
            itemOverlayMatchPointHandlers.add(new VendorLineItemIdHandler());
        }
        return itemOverlayMatchPointHandlers;
    }
}
