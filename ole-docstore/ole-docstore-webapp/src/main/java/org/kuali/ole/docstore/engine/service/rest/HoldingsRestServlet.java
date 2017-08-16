package org.kuali.ole.docstore.engine.service.rest;

import com.google.common.io.CharStreams;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;
import org.kuali.ole.docstore.common.find.FindParams;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.service.BeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 6/16/14
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class HoldingsRestServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingsRestServlet.class);
    private static String responseUrl = "documentrest/holdings/doc/";
    private static String responseTreeUrl = "documentrest/holdings/doc/tree/";
    private static String bindUrl = "bind";
    private static String unbindUrl = "unbind";
    private Logger logger = LoggerFactory.getLogger(HoldingsRestController.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/xml;charset=UTF-8");
        String result = "";
        try {
            if (req.getPathInfo() != null && req.getPathInfo().contains("doc")) {
                if (req.getPathInfo().startsWith("/doc/trees")) {
                    result = retrieveHoldingsDocTrees(req);
                } else if (req.getPathInfo().startsWith("/doc/tree")) {
                    result = retrieveHoldingsTree(req);
                } else if (req.getPathInfo().startsWith("/doc")) {
                    result = retrieveHoldings(req);
                }
            } else {
                if (req.getPathInfo().startsWith("/tree")) {
                    result = retrieveHoldingsTrees(req);
                }
            }
            out.print(result);
        } catch (DocstoreException de) {
            LOG.error("Docstore Exception :", de);
            out.print(DocstoreExceptionProcessor.toXml(de));
        } catch (Exception e) {
            LOG.error("Exception :", e);
            out.print(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/xml;charset=UTF-8");
        String result = "";
        try {
            if (req.getPathInfo().startsWith("/doc/tree/find")) {
                result = findHoldingsTree(req);
            } else if (req.getPathInfo().startsWith("/doc/find")) {
                result = findHoldings(req);
            } else if (req.getPathInfo().contains("/bound")) {
                String holdingsId = req.getPathInfo().split("/")[2];
                boundWithBibs(holdingsId, req);
            }else if (req.getPathInfo().contains("/unbind/one")) {
                String bibId = req.getPathInfo().split("/")[2];
                unbindWithOneBib(bibId, req);
            }else if (req.getPathInfo().contains("/unbind/all")) {
                String bibId = req.getPathInfo().split("/")[2];
                unbindWithAllBibs(bibId, req);
            } else if (req.getPathInfo().contains("/analytic")) {
                String holdingsId = req.getPathInfo().split("/")[2];
                createAnalyticsRelation(holdingsId, req);
            } else if (req.getPathInfo().contains("/breakAnalytic")) {
                String holdingsId = req.getPathInfo().split("/")[2];
                breakAnalyticsRelation(holdingsId, req);
            } else if (req.getPathInfo().contains("/transfer")) {
                String holdingsId = req.getPathInfo().split("/")[2];
                result = transferItems(holdingsId, req);
            }  else if (req.getPathInfo().startsWith("/doc/tree")) {
                result = createHoldingsTree(req);
            } else if (req.getPathInfo().startsWith("/doc")) {
                result = createHoldings(req);
            }
            out.print(result);
        } catch (DocstoreException de) {
            LOG.error("Exception :", de);
            out.print(DocstoreExceptionProcessor.toXml(de));
        } catch (Exception e) {
            LOG.error("Exception :", e);
            out.print(e);
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/xml;charset=UTF-8");
        String result = "";
        try {
            if (req.getPathInfo().contains("/bulkUpdate")) {
                result = bulkUpdateHoldings(req);
            } else if (req.getPathInfo().startsWith("/doc")) {
                result = updateHoldings(req);
            }
            out.print(result);
        } catch (DocstoreException de) {
            LOG.error("Exception :", de);
            out.print(DocstoreExceptionProcessor.toXml(de));
        } catch (Exception e) {
            LOG.error("Exception :", e);
            out.print(e);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/xml;charset=UTF-8");
        String result = "";
        try {
            result = deleteHoldings(req);
            out.print(result);
        } catch (DocstoreException de) {
            LOG.error("Exception :", de);
            out.print(DocstoreExceptionProcessor.toXml(de));
        } catch (Exception e) {
            LOG.error("Exception :", e);
            out.print(e);
        }
    }

    /**
     * Creating Holdings
     *
     * @param req
     * @return
     * @throws IOException
     */
    public String createHoldings(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
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


    /**
     * Retrieve Holdings
     *
     * @param req
     * @return
     */
    public String retrieveHoldings(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String holdingsId = req.getParameter(("holdingsId"));
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

    /**
     * Update Holdings
     *
     * @param req
     * @return
     * @throws IOException
     */

    public String updateHoldings(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
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


    /**
     * Delete Holdings
     *
     * @param req
     * @return
     */
    public String deleteHoldings(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String holdingsId = req.getParameter(("holdingsId"));
        try {
            ds.deleteHoldings(holdingsId);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in deleteHoldings() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return "Success";
    }


    /**
     * Create Holdings doc Tree
     *
     * @param req
     * @return
     * @throws IOException
     */
    public String createHoldingsTree(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
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


    /**
     * Retrieve Holdings Tree
     *
     * @param req
     * @return
     * @throws IOException
     */
    public String retrieveHoldingsTree(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String holdingsId = req.getParameter(("holdingsId"));

        HoldingsTree holdingsTree = null;
        try {
            holdingsTree = ds.retrieveHoldingsTree(holdingsId);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in retrieveHoldingsTree() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return holdingsTree.serialize(holdingsTree);
    }

    /**
     * Retrieve Holdings Trees
     *
     * @param req
     * @return
     * @throws IOException
     */

    public String retrieveHoldingsTrees(HttpServletRequest req) throws IOException {
        Map params = req.getParameterMap();
        String[] bibIds = (String[]) params.get("bibId");
        DocstoreService ds = BeanLocator.getDocstoreService();
        StringBuilder holdingsTrees = new StringBuilder("<holdingsTrees>");
        for (String bibId : bibIds) {
            try {
                BibTree bibTree = ds.retrieveBibTree(bibId);
                if (bibTree != null && bibTree.getHoldingsTrees() != null && bibTree.getHoldingsTrees().size() > 0) {
                    for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                        holdingsTrees.append("\n" + "<holdingsTree>").append("\n");
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
                LOG.info("Exception occurred in retrieveHoldingsTrees :" + e.getErrorCode() + " , " + e.getErrorParams());
                holdingsTrees.append("<noBib><id>"+bibId+"</id></noBib>");
             }
        }
        holdingsTrees.append("</holdingsTrees>");
        return holdingsTrees.toString();
    }

    /**
     * Retrieve Holdings Doc Trees
     *
     * @param req
     * @return
     * @throws IOException
     */

    public String retrieveHoldingsDocTrees(HttpServletRequest req) throws IOException {
        Map params = req.getParameterMap();
        String[] bibIds = (String[]) params.get("bibId");
        DocstoreService ds = BeanLocator.getDocstoreService();
        HoldingsTrees holdingsTrees = new HoldingsTrees();
        for (String bibId : bibIds) {
            try {
                BibTree bibTree = ds.retrieveBibTree(bibId);
                if (bibTree != null && bibTree.getHoldingsTrees() != null && bibTree.getHoldingsTrees().size() > 0) {
                 holdingsTrees.getHoldingsTrees().addAll(bibTree.getHoldingsTrees());
                }
            } catch (DocstoreException e) {
                LOG.error("Exception occurred in retrieveHoldingsDocTrees :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }
        }
        return HoldingsTrees.serialize(holdingsTrees);
    }


    /**
     * @param req
     * @return
     */
    public String findHoldings(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
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

    /**
     * @param req
     * @return
     * @throws IOException
     */
    public String findHoldingsTree(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
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

    /**
     * @param holdingsId
     * @param req
     * @return
     * @throws IOException
     */
    public String boundWithBibs(String holdingsId, HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        if (requestBody.contains("[")) {
            requestBody = requestBody.substring(1, requestBody.length() - 1);
        }
        String[] splitBibid = requestBody.split(",");
        List<String> bibIds = new ArrayList<String>();
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

    /**
     * @param seriesHoldingsId
     * @param req
     * @return
     * @throws IOException
     */
    public String breakAnalyticsRelation(String seriesHoldingsId, HttpServletRequest req) throws IOException {
        String requestBody = CharStreams.toString(req.getReader());
        DocstoreService ds = BeanLocator.getDocstoreService();
        if (requestBody.contains("[")) {
            requestBody = requestBody.substring(1, requestBody.length() - 1);
        }
        String[] splitItemid = requestBody.split(",");
        List<String> itemIds = new ArrayList<String>();
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

    /**
     * @param seriesHoldingsId
     * @param req
     * @return
     * @throws IOException
     */
    public String createAnalyticsRelation(String seriesHoldingsId, HttpServletRequest req) throws IOException {
        String requestBody = CharStreams.toString(req.getReader());
        DocstoreService ds = BeanLocator.getDocstoreService();
        if (requestBody.contains("[")) {
            requestBody = requestBody.substring(1, requestBody.length() - 1);
        }
        String[] splitItemid = requestBody.split(",");
        List<String> itemIds = new ArrayList<String>();
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


    /**
     * @param holdingsId
     * @param req
     * @return
     * @throws IOException
     */
    public String transferItems(String holdingsId, HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        Map params = req.getParameterMap();
        String[] itemIds = (String[]) params.get("itemId");
        List<String> itemsIds = new ArrayList<String>();
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

    /**
     * @param req
     * @return
     * @throws IOException
     */
    public String bulkUpdateHoldings(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        String[] bulkUpdateRequest = requestBody.split("\n", 3);

        String holdingsId = bulkUpdateRequest[0];
        String canUpdateStaffOnlyFlag = bulkUpdateRequest[1];
        requestBody = bulkUpdateRequest[2];


        String[] holdingItemIds = holdingsId.split(",");
        List<String> holdingIds = new ArrayList<String>();
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
            ds.bulkUpdateHoldings(holdings, holdingIds, canUpdateStaffOnlyFlag);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in bulkUpdateHoldings() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return "Success";
    }

    public String unbindWithOneBib(String bibId, HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        if (requestBody.contains("[")) {
            requestBody = requestBody.substring(1, requestBody.length() - 1);
        }
        String[] splitHoldingsid = requestBody.split(",");
        List<String> holdingsIds = new ArrayList<String>();
        for (String holdingsId : splitHoldingsid) {
            holdingsIds.add(holdingsId);
        }
        try {
            ds.unbindWithOneBib(holdingsIds, bibId);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in unbindWithOneBib() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + holdingsIds + unbindUrl;
    }

    public String unbindWithAllBibs(String bibId, HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        if (requestBody.contains("[")) {
            requestBody = requestBody.substring(1, requestBody.length() - 1);
        }
        String[] splitHoldingsid = requestBody.split(",");
        List<String> holdingsIds = new ArrayList<String>();
        for (String holdingsId : splitHoldingsid) {
            holdingsIds.add(holdingsId);
        }
        try {
            ds.unbindWithAllBibs(holdingsIds, bibId);
        } catch (DocstoreException e) {
            LOG.error("Exception occurred in unbindWithAllBibs() :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + holdingsIds + unbindUrl;
    }


}
