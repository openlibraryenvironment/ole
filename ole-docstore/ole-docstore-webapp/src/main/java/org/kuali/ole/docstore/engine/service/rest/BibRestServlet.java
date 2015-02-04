package org.kuali.ole.docstore.engine.service.rest;

import com.google.common.io.CharStreams;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.OrderBibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.docstore.common.document.ids.BibIds;
import org.kuali.ole.docstore.common.document.ids.HoldingsId;
import org.kuali.ole.docstore.common.exception.*;
import org.kuali.ole.docstore.common.find.FindParams;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.service.BeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 3/2/14
 * Time: 7:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibRestServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(BibRestServlet.class);
    private static String responseUrl = "documentrest/bib/doc/";
    private static String responseTreeUrl = "documentrest/bib/doc/tree/";
    public static BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/xml;charset=UTF-8");
        String result = "";
        try {
            if (req.getPathInfo() != null && req.getPathInfo().startsWith("/reload")) {
                DocumentSearchConfig.getDocumentSearchConfig();
                DocumentSearchConfig.reloadDocumentConfig();
            } else if (req.getPathInfo() != null && req.getPathInfo().contains("doc")) {
                if (req.getPathInfo().startsWith("/doc/trees")) {
                    result = retrieveBibTrees(req);
                } else if (req.getPathInfo().startsWith("/doc/tree")) {
                    result = retrieveBibTree(req);
                } else {
                    result = retrieveBib(req);
                }
            } else {
                result = retrieveBibContent(req);
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
            if (req.getPathInfo().startsWith("/tree/doc/find")) {
                result = findBibTree(req);
            } else if (req.getPathInfo().startsWith("/doc/find")) {
                result = findBib(req);
            } else if (req.getPathInfo().startsWith("/doc/tree")) {
                result = createBibTree(req);
            } else if (req.getPathInfo().contains("/transfer")) {
                result = transferHoldings(req);
            } else if (req.getPathInfo().contains("/process")) {
                result = processBibTrees(req);
            } else {
                result = createBib(req);
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
            if (req.getPathInfo().startsWith("/doc/trees")) {

            } else {
                result = updateBib(req);
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
            result = deleteBibs(req);
            out.print(result);
        } catch (DocstoreException de) {
            LOG.error("Exception :", de);
            out.print(DocstoreExceptionProcessor.toXml(de));
        } catch (Exception e) {
            LOG.error("Exception :", e);
            out.print(e);
        }
    }

    private String deleteBib(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String bibId = getIdFromPathInfo(req.getPathInfo());
        ds.deleteBib(bibId);
        return "Success";
    }

    private String deleteBibs(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        Map params = req.getParameterMap();
        String[] bibIds = (String[]) params.get("bibId");
        List<String> bibIdList = new ArrayList<String>();
        for (String bibId : bibIds) {
            bibIdList.add(bibId);
        }
            ds.deleteBibs(bibIdList);
        return "Success";
    }

    private String getIdFromPathInfo(String pathInfo) {
        String id = "";
        if (StringUtils.isNotEmpty(pathInfo)) {
            int length = pathInfo.length();
            if (pathInfo.endsWith("/")) {
                pathInfo = pathInfo.substring(0, length - 1);
            }
            id = pathInfo.substring(pathInfo.lastIndexOf("/") + 1);
        }
        return id;
    }

    private String findBibTree(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        BibTree bibTree = new BibTree();
        FindParams findParams = new FindParams();
        findParams = (FindParams) findParams.deserialize(requestBody);
        HashMap<String, String> hashMap = new HashMap();
        List<FindParams.Map.Entry> entries = findParams.getMap().getEntry();
        for (FindParams.Map.Entry entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        bibTree = ds.findBibTree(hashMap);
        return bibTree.serialize(bibTree);

    }

    private String findBib(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        Bib bib = new BibMarc();
        FindParams findParams = new FindParams();
        findParams = (FindParams) findParams.deserialize(requestBody);
        HashMap<String, String> hashMap = new HashMap();
        List<FindParams.Map.Entry> entries = findParams.getMap().getEntry();
        for (FindParams.Map.Entry entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        bib = ds.findBib(hashMap);
        return (bib.serialize(bib));
    }

    private String createBib(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        Bib bib = new BibMarc();
        Bib bibObj = (Bib) bib.deserialize(requestBody);
        ds.createBib(bibObj);
        String responseStr = responseUrl + bibObj.getId();
        return responseStr;
    }

    private String updateBib(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());

        Bib bib = new BibMarc();
        Bib bibObj = (Bib) bib.deserialize(requestBody);
        ds.updateBib(bibObj);
        return responseUrl + bibObj.getId();
    }


    private String retrieveBib(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        Map params = req.getParameterMap();
        String[] bibIds = (String[]) params.get("bibId");
        List<String> bibIdList = new ArrayList<String>();

        for (String bibId : bibIds) {
            bibIdList.add(bibId);
        }
        List<Bib> bibs = null;
        bibs = ds.retrieveBibs(bibIdList);
        if (bibs.size() == 1) {
            Bib bib = new Bib();
            return bib.serialize(bibs.get(0));
        }
        Bibs bibsObj = new Bibs();
        bibsObj.getBibs().addAll(bibs);
        return Bibs.serialize(bibsObj);
    }

    private String retrieveBibContent(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        StringBuffer contentBuffer = new StringBuffer();
        BibMarcRecords bibMarcRecords = new BibMarcRecords();
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        List<String> bibIdList = new ArrayList<String>();
        Map params = req.getParameterMap();
        String[] queryVariables = (String[]) params.get("bibId");
        if (queryVariables.length > 0) {
            for (String bibId : queryVariables) {
                bibIdList.add(bibId);
            }
        }
        if (bibIdList != null && bibIdList.size() > 1) {
            List<Bib> bibs = ds.retrieveBibs(bibIdList);
            for (Bib bibRecord : bibs) {
                bibMarcRecords.getRecords().addAll(bibMarcRecordProcessor.fromXML(bibRecord.getContent()).getRecords());
            }
            if (bibMarcRecords != null) {
                contentBuffer.append(bibMarcRecordProcessor.generateXML(bibMarcRecords.getRecords()));
            }
        } else if (bibIdList != null && bibIdList.size() == 1) {
            Bib bib = ds.retrieveBib(bibIdList.get(0));
            bibMarcRecords = bibMarcRecordProcessor.fromXML(bib.getContent());
            contentBuffer.append(bibMarcRecordProcessor.generateXML(bibMarcRecords.getRecords()));
        }
        return contentBuffer.toString();
    }

    private String retrieveBibTree(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String id = req.getParameter("bibId");
        BibTree bibTree = ds.retrieveBibTree(id);
        return bibTree.serialize(bibTree);
    }

    private String createBibTree(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        BibTree bibTree = new BibTree();
            bibTree = (BibTree) bibTree.deserialize(requestBody);
            ds.createBibTree(bibTree);
        return responseTreeUrl + bibTree.getBib().getId();
    }


    private String processBibTrees(HttpServletRequest req) throws IOException {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String requestBody = CharStreams.toString(req.getReader());
        BibTrees bibTrees = new BibTrees();
        bibTrees = (BibTrees) bibTrees.deserialize(requestBody);
        bibTrees = ds.processBibTrees(bibTrees);
        return bibTrees.serialize(bibTrees);
    }

    private String transferHoldings(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        //String[] transferStr = req.getPathInfo().split("transfer");
        String bibId = req.getPathInfo().split("/")[2];
        Map params = req.getParameterMap();
        String[] splitHoldingIds = (String[]) params.get("holdingsId");
        List<String> holdingsIds = new ArrayList<String>();
        for (String holdingsId : splitHoldingIds) {
            holdingsIds.add(holdingsId);
        }
        ds.transferHoldings(holdingsIds, bibId);
        return "Success";
    }

    private String retrieveBibTrees(HttpServletRequest req) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        List<String> bibIdList = new ArrayList<String>();
        Map params = req.getParameterMap();
        String[] bibIds = (String[]) params.get("bibId");
        for (String bibId : bibIds) {
            bibIdList.add(bibId);
        }
        return BibTrees.serialize(ds.retrieveBibTrees(bibIdList));
    }

}
