package org.kuali.ole.docstore.engine.service.rest;

import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;
import org.kuali.ole.docstore.common.find.FindParams;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.service.BeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 12/17/13
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/bibs1")

public class BibsRestController extends AbstractRestService {

    private static final Logger LOG = LoggerFactory.getLogger(BibsRestController.class);
    private static String responseUrl = "documentrest/bibs/";
    private static String responseTreeUrl = "documentrest/bibs/tree/";
    private Logger logger = LoggerFactory.getLogger(BibsRestController.class);

    @Override
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/xml;charset=UTF-8", produces = "application/text")
    @ResponseBody
    public String createBib(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        Bib bib = new BibMarc();
        Bib bibObj = (Bib) bib.deserialize(requestBody.replaceAll("\n", ""));
        try {
            ds.createBib(bibObj);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + bibObj.getId();
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String updateBib(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        Bib bib = new BibMarc();
        Bib bibObj = (Bib) bib.deserialize(requestBody);
        try {
            ds.updateBib(bibObj);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseUrl + bibObj.getId();
    }



    @Override
    @RequestMapping(value = "/{bibId}", method = RequestMethod.GET, produces = "application/xml;charset=UTF-8")
    @ResponseBody
    public String retrieveBib(@PathVariable("bibId") String bibId) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        if (bibId.contains("bibIds")) {
            String[] splitString = bibId.split("=");
            String[] bibIds = splitString[1].split(",");
            List<String> bibIdList = new ArrayList<>();
            for (String id : bibIds) {
                bibIdList.add(id);
            }
            List<Bib> bibs = null;
            try {
                bibs = ds.retrieveBibs(bibIdList);
            } catch (DocstoreException e) {
                LOG.info("Exception :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }
            Bibs bibsObj = new Bibs();
            bibsObj.getBibs().addAll(bibs);
            return Bibs.serialize(bibsObj);
        } else {
            Bib bib = null;
            try {
                bib = ds.retrieveBib(bibId);
            } catch (DocstoreException e) {
                LOG.info("Exception :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }
            return bib.serialize(bib);
        }
    }

    @Override
    @RequestMapping(value = "/{bibId}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteBib(@PathVariable("bibId") String bibId) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        if (bibId.contains("bibIds")) {
            String[] splitString = bibId.split("=");
            String[] bibIds = splitString[1].split(",");
            List<String> bibIdList = new ArrayList<>();
            for (String id : bibIds) {
                bibIdList.add(id);
            }
            try {
                ds.deleteBibs(bibIdList);
            } catch (DocstoreException e) {
                LOG.info("Exception :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }

        } else {
            try {
                ds.deleteBib(bibId);
            } catch (DocstoreException e) {
                LOG.info("Exception :", e);
                return DocstoreExceptionProcessor.toXml(e);
            }

        }
        return "Success";
    }

    @Override
    @RequestMapping(value = "/tree", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String createBibTree(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        BibTree bibTree = new BibTree();
        bibTree = (BibTree) bibTree.deserialize(requestBody);
        try {
            ds.createBibTree(bibTree);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return responseTreeUrl + bibTree.getBib().getId();
    }



    @Override
    @RequestMapping(value = "/tree/{bibId}", method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public String retrieveBibTree(@PathVariable("bibId") String bibId) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        BibTree bibTree = null;
        try {
            bibTree = ds.retrieveBibTree(bibId);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return bibTree.serialize(bibTree);
    }

    @Override
    @RequestMapping(value = "/find", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String findBibs(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        FindParams findParams = new FindParams();
        findParams = (FindParams) findParams.deserialize(requestBody);
        HashMap<String, String> hashMap = new HashMap();
        List<FindParams.Map.Entry> entries = findParams.getMap().getEntry();
        for (FindParams.Map.Entry entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        Bib bib = null;
        try {
            bib = ds.findBib(hashMap);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }

        return bib.serialize(bib);
    }

    @Override
    @RequestMapping(value = "/tree/find", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String findBibTree(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        FindParams findParams = new FindParams();
        findParams = (FindParams) findParams.deserialize(requestBody);
        HashMap<String, String> hashMap = new HashMap();
        List<FindParams.Map.Entry> entries = findParams.getMap().getEntry();
        for (FindParams.Map.Entry entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        BibTree bibTree = null;
        try {
            bibTree = ds.findBibTree(hashMap);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return bibTree.serialize(bibTree);
    }

    @Override
    @RequestMapping(value = "{bibId}/transfer/{holdingIds}", method = RequestMethod.POST, consumes = "application/xml", produces = "application/text")
    @ResponseBody
    public String transferHoldings(@PathVariable("bibId") String bibId, @PathVariable("holdingIds") String holdingIds) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        String[] splitHoldingIds = holdingIds.split(",");
        List<String> holdingsIds = new ArrayList<>();
        for (String holdingsId : splitHoldingIds) {
            holdingsIds.add(holdingsId);
        }
        try {
            ds.transferHoldings(holdingsIds, bibId);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return "Success";
    }


}
