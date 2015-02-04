package org.kuali.ole.docstore.engine.service.rest;

import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;
import org.kuali.ole.docstore.common.search.BrowseParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.service.BeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 1/6/14
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/browse")

public class BrowseRestController extends AbstractRestService {

    private static final Logger LOG = LoggerFactory.getLogger(BrowseRestController.class);

    @Override
    @RequestMapping(value = "/items", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String browseItems(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        BrowseParams browseParams = new BrowseParams();
        browseParams = (BrowseParams) browseParams.deserialize(requestBody);
        SearchResponse searchResponse = null;
        try {
            searchResponse = ds.browseItems(browseParams);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return searchResponse.serialize(searchResponse);
    }

    @Override
    @RequestMapping(value = "/holdings", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String browseHoldings(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        BrowseParams browseParams = new BrowseParams();
        browseParams = (BrowseParams) browseParams.deserialize(requestBody);
        SearchResponse searchResponse = null;
        try {
            searchResponse = ds.browseHoldings(browseParams);
        } catch (DocstoreException e) {
            LOG.info("Exception :", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return searchResponse.serialize(searchResponse);
    }
}
