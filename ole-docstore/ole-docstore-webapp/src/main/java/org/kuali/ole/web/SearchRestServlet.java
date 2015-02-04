package org.kuali.ole.web;

import com.google.common.io.CharStreams;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreExceptionProcessor;
import org.kuali.ole.docstore.common.search.SearchParams;
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
public class SearchRestServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(SearchRestServlet.class);


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requestBody = CharStreams.toString(req.getReader());
        DocstoreService ds = BeanLocator.getDocstoreService();
        SearchParams searchParams = new SearchParams();
        searchParams = (SearchParams) searchParams.deserialize(requestBody);
        SearchResponse searchResponse = null;
        try {
            searchResponse = ds.search(searchParams);
        } catch (DocstoreException e) {
            LOG.error("Exception : ", e);
            out.print(DocstoreExceptionProcessor.toXml(e));
        }
        out.print(searchResponse.serialize(searchResponse));

    }
}