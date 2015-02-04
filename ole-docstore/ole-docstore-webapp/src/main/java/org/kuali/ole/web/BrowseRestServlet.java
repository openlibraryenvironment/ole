package org.kuali.ole.web;

import com.google.common.io.CharStreams;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;
import org.kuali.ole.docstore.common.search.BrowseParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
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


/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 3/2/14
 * Time: 7:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrowseRestServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(BrowseRestServlet.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/xml;charset=UTF-8");
        String result = "";
        try {
            if (req.getPathInfo().startsWith("/holdings")) {
                result = browseHoldings(req);
            } else if (req.getPathInfo().startsWith("/item")) {
                result = browseItem(req);
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

    private String browseHoldings(HttpServletRequest req) throws Exception{
        String requestBody = CharStreams.toString(req.getReader());
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

    private String browseItem(HttpServletRequest req) throws Exception {
        String requestBody = CharStreams.toString(req.getReader());
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



}