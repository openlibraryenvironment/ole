package org.kuali.ole.docstore.engine.service.rest;

import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;
import org.kuali.ole.docstore.common.search.SearchParams;
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

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 12/27/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/search")
public class SearchRestController extends AbstractRestService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchRestController.class);

    @Override
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/xml", produces = "application/xml")
    @ResponseBody
    public String search(@RequestBody String requestBody) {
        DocstoreService ds = BeanLocator.getDocstoreService();
        SearchParams searchParams = new SearchParams();
        searchParams = (SearchParams) searchParams.deserialize(requestBody);
        SearchResponse searchResponse = null;
        try {
            searchResponse = ds.search(searchParams);
        } catch (DocstoreException e) {
            LOG.error("SearchRestController Exception : ", e);
            return DocstoreExceptionProcessor.toXml(e);
        }
        return searchResponse.serialize(searchResponse);
    }

}
