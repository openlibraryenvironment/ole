package org.kuali.ole.docstore.engine.service.rest;

import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;
import org.kuali.ole.docstore.common.find.FindParams;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.service.BeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 12/18/13
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/holdings")
public class HoldingsRestController extends AbstractRestService {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingsRestController.class);
    private static String responseUrl = "documentrest/holdings/doc/";
    private static String responseTreeUrl = "documentrest/holdings/doc/tree/";
    private static String bindUrl = "bind";
    private static String unbindUrl = "unbind";
    private Logger logger = LoggerFactory.getLogger(HoldingsRestController.class);

    @Override
    @RequestMapping(value = "/doc/", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String createHoldings(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        Holdings holdings = new Holdings();
        try {
        holdings = (Holdings) holdings.deserialize(requestBody);
        if (holdings.getHoldingsType().equalsIgnoreCase("print")) {
            holdings = new PHoldings();
            holdings = (PHoldings) holdings.deserialize(requestBody);
        } else {
            holdings = new EHoldings();
            holdings = (EHoldings) holdings.deserialize(requestBody);
        }

            ds.createHoldings(holdings);
        } catch (DocstoreException e) {
            LOG.error("Exception Occurred in createHoldings() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        } catch (Exception exp) {
            LOG.error("Exception Occurred in createHoldings() :", exp);
        }
        return responseUrl + holdings.getId();
    }

    @Override
    @RequestMapping(value = "/doc", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String retrieveHoldings(@RequestParam("holdingsId") String holdingsId) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        Holdings holdings = null;
        try {
            holdings = ds.retrieveHoldings(holdingsId);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in retrieveHoldings() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        if (holdings.getHoldingsType().equalsIgnoreCase("print")) {
            holdings = ds.retrieveHoldings(holdingsId);
            Holdings pHoldings = new Holdings();
            return pHoldings.serialize(holdings);
        } else {
            holdings = ds.retrieveHoldings(holdingsId);
            Holdings eHoldings = new EHoldings();
            return eHoldings.serialize(holdings);
        }
    }

    @Override
    @RequestMapping(value = "/doc/", method = RequestMethod.PUT, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String updateHoldings(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        Holdings holdings = new Holdings();
        holdings = (Holdings) holdings.deserialize(requestBody);
        if (holdings.getHoldingsType().equalsIgnoreCase("print")) {
            holdings = new PHoldings();
            holdings = (PHoldings) holdings.deserialize(requestBody);
        } else {
            holdings = new EHoldings();
            holdings = (EHoldings) holdings.deserialize(requestBody);
        }
        try {
            ds.updateHoldings((holdings));
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in updateHoldings() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + holdings.getId();
    }

    @Override
    @RequestMapping(value = "/doc", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteHoldings(@RequestParam("holdingsId") String holdingsId) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        try {
            ds.deleteHoldings(holdingsId);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in deleteHoldings() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return "Success";
    }

    @Override
    @RequestMapping(value = "/doc/tree", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String createHoldingsTree(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree = (HoldingsTree) holdingsTree.deserialize(requestBody);
        try {
            ds.createHoldingsTree(holdingsTree);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in createHoldingsTree()  :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        StringBuilder itemIds = new StringBuilder();
        for (Item item : holdingsTree.getItems()) {
            if (holdingsTree.getItems().get(holdingsTree.getItems().size() - 1) == item) {
                itemIds.append(item.getId());
            } else {
                itemIds.append(item.getId() + "/");
            }
        }
        return responseTreeUrl + holdingsTree.getHoldings().getId() + "/" + itemIds.toString();

    }




    @Override
    @RequestMapping(value = "/doc/tree", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String retrieveHoldingsTree(@RequestParam("holdingsId") String holdingsId) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        HoldingsTree holdingsTree = null;
        try {
            holdingsTree = ds.retrieveHoldingsTree(holdingsId);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in retrieveHoldingsTree() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return holdingsTree.serialize(holdingsTree);
    }

    @Override
    @RequestMapping(value = "/tree", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String retrieveHoldingsTrees(@RequestParam("bibId") String[] bibIds) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        StringBuilder holdingsTrees = new StringBuilder("<holdingsTrees>");
        for (String bibId : bibIds) {
            try {
                BibTree bibTree = ds.retrieveBibTree(bibId);
                if (bibTree != null && bibTree.getHoldingsTrees() != null && bibTree.getHoldingsTrees().size() > 0) {
                    holdingsTrees.append("\n" + "<holdingsTree>").append("\n");
                    for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                        holdingsTrees.append(holdingsTree.getHoldings().getContent());

                        holdingsTrees.append("<items>").append("\n");
                        for (Item item : holdingsTree.getItems()) {
                            holdingsTrees.append(item.getContent()).append("\n");
                        }
                        holdingsTrees.append("</items>").append("\n");
                        holdingsTrees.append("</holdingsTree>").append("\n");
                    }
                }
            } catch (DocstoreException e) {
                LOG.error("Exception occurred in retrieveHoldingsTrees :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }
        }
        holdingsTrees.append("</holdingsTrees>");
        String result = holdingsTrees.toString();
        result = result.replaceAll("<shelvingOrder/>", "<shelvingOrder></shelvingOrder>");
        result = result.replaceAll("<uri/>", "<uri></uri>");
        result = result.replaceAll("<statisticalSearchingCode/>", "<statisticalSearchingCode></statisticalSearchingCode>");
        result = result.replaceAll("<location/>", "<location></location>");
        result = result.replaceAll("<donorInfo/>", "<donorInfo></donorInfo>");
        result = result.replaceAll("<highDensityStorage/>", "<highDensityStorage></highDensityStorage>");
        return result;
    }


    @Override
    @RequestMapping(value = "/doc/find", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String findHoldings(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        FindParams findParams = new FindParams();
        findParams = (FindParams) findParams.deserialize(requestBody);
        HashMap<String, String> hashMap = new HashMap();
        List<FindParams.Map.Entry> entries = findParams.getMap().getEntry();
        for (FindParams.Map.Entry entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        Holdings holdings = null;
        try {
            holdings = ds.findHoldings(hashMap);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in findHoldings() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return holdings.serialize(holdings);
    }

    @Override
    @RequestMapping(value = "/doc/tree/find", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String findHoldingsTree(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        FindParams findParams = new FindParams();
        findParams = (FindParams) findParams.deserialize(requestBody);
        HashMap<String, String> hashMap = new HashMap();
        List<FindParams.Map.Entry> entries = findParams.getMap().getEntry();
        for (FindParams.Map.Entry entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        HoldingsTree holdings = null;
        try {
            holdings = ds.findHoldingsTree(hashMap);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in findHoldingsTree() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return holdings.serialize(holdings);
    }

    @Override
    @RequestMapping(value = "/doc/{holdingsId}/bound", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String boundWithBibs(@PathVariable("holdingsId") String holdingsId, @RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        if (requestBody.contains("[")) {
            requestBody = requestBody.substring(1, requestBody.length() - 1);
        }
        String[] splitBibid = requestBody.split(",");
        List<String> bibIds = new ArrayList<>();
        for (String bibId : splitBibid) {
            bibIds.add(bibId);
        }
        try {
            ds.boundHoldingsWithBibs(holdingsId, bibIds);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in boundWithBibs() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + holdingsId + bindUrl;
    }

    @RequestMapping(value = "/doc/{seriesHoldingsId}/breakAnalytic", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String breakAnalyticsRelation(@PathVariable("seriesHoldingsId") String seriesHoldingsId, @RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        if (requestBody.contains("[")) {
            requestBody = requestBody.substring(1, requestBody.length() - 1);
        }
        String[] splitItemid = requestBody.split(",");
        List<String> itemIds = new ArrayList<>();
        for (String itemId : splitItemid) {
            itemIds.add(itemId);
        }
        try {
            ds.breakAnalyticsRelation(seriesHoldingsId, itemIds);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in breakAnalyticsRelation() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + seriesHoldingsId + unbindUrl;
    }

    @RequestMapping(value = "/doc/{seriesHoldingsId}/analytic", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String createAnalyticsRelation(@PathVariable("seriesHoldingsId") String seriesHoldingsId, @RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        if (requestBody.contains("[")) {
            requestBody = requestBody.substring(1, requestBody.length() - 1);
        }
        String[] splitItemid = requestBody.split(",");
        List<String> itemIds = new ArrayList<>();
        for (String itemId : splitItemid) {
            itemIds.add(itemId);
        }
        try {
            ds.createAnalyticsRelation(seriesHoldingsId, itemIds);
        } catch (DocstoreException e) {
            LOG.error("Exception occurrred in createAnalyticsRelation() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + seriesHoldingsId + bindUrl;
    }


    @RequestMapping(value = "/doc/{holdingsId}/transfer", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String transferItems(@PathVariable("holdingsId") String holdingsId,  @RequestParam("itemId") String[] itemIds) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        List<String> itemsIds = new ArrayList<>();
        for (String itemId : itemIds) {
            itemsIds.add(itemId);
        }
        try {
            ds.transferItems(itemsIds, holdingsId);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in transferItems() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return "Success";
    }

    @RequestMapping(value = "/doc/bulkUpdate", method = RequestMethod.PUT, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String bulkUpdateHoldings( @RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();

        String[] bulkUpdateRequest = requestBody.split("\n", 3);

        String holdingsId = bulkUpdateRequest[0];
        String canUpdateStaffOnlyFlag = bulkUpdateRequest[1];
        requestBody = bulkUpdateRequest[2];


        String[] holdingItemIds = holdingsId.split(",");
        List<String> holdingIds = new ArrayList<>();
        for (String itemId : holdingItemIds) {
            holdingIds.add(itemId);
        }
        Holdings holdings = new Holdings();
        holdings = (Holdings) holdings.deserialize(requestBody);
        if (holdings.getHoldingsType().equalsIgnoreCase("print")) {
            holdings = new PHoldings();
            holdings = (PHoldings) holdings.deserialize(requestBody);
        } else {
            holdings = new EHoldings();
            holdings = (EHoldings) holdings.deserialize(requestBody);
        }
        try {
            ds.bulkUpdateHoldings(holdings, holdingIds,canUpdateStaffOnlyFlag);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in bulkUpdateHoldings() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return "Success";
    }


}
