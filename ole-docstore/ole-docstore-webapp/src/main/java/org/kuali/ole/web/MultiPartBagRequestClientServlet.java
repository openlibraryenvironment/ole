package org.kuali.ole.web;

import org.kuali.ole.docstore.OleException;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.service.MultiPartBagRequestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet MultiPartBagRequestClient to all requests in a given folder for Handling Multi Part Zip Bag contents.
 *
 * @author Rajesh Chowdary K
 * @created Jun 5, 2012
 */
public class MultiPartBagRequestClientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(MultiPartBagRequestClientServlet.class);
    private ResponseHandler rh = new ResponseHandler();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestFolderPath = request.getParameter("requestFolderPath");
        String restUrl = request.getParameter("restUrl");
        PrintWriter out = response.getWriter();
        logger.debug("restUrl-->" + restUrl);
        logger.debug("requestFolderPath-->" + requestFolderPath);
        out.println("<responses>");
        try {
            List<Response> responses = new MultiPartBagRequestClient().runMultiPartRequestsAtLocation(requestFolderPath, restUrl);
            response.setContentType("text/xml");
            for (Response resp : responses)
                out.println(rh.toXML(resp));
            out.println("</responses>");
        } catch (OleException e) {
            out.print(e.getMessage());
            logger.error(e.getMessage());
        } catch (Exception e) {
            out.print(e.getMessage());
            logger.error(e.getMessage(), e);
        }
    }

}
