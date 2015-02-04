package org.kuali.ole.web;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.engine.service.index.solr.BibConstants;
import org.kuali.ole.docstore.metrics.reindex.ReIndexingStatus;
import org.kuali.ole.docstore.process.RebuildIndexesHandler;
import org.kuali.ole.logger.DocStoreLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 11/12/11
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class RebuildIndexServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    DocStoreLogger docStoreLogger = new DocStoreLogger(getClass().getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String docCategory = DocCategory.WORK.getCode();
            String docType = DocType.BIB.getCode();
            String docFormat = DocFormat.MARC.getCode();
            String action = req.getParameter("action");
            String batchSizeReq = req.getParameter("batchSize");
            int batchSize = 0;
            if(StringUtils.isNotEmpty(batchSizeReq)) {
                batchSize = Integer.parseInt(batchSizeReq);
            }
            RebuildIndexesHandler rebuildIndexesHandler = RebuildIndexesHandler.getInstance();

            if (action.equalsIgnoreCase("start")) {
                String result = rebuildIndexesHandler.startProcess(docCategory, docType, docFormat, batchSize);
                outputMessage(resp, result);
            } else if (action.equalsIgnoreCase("stop")) {
                String result = rebuildIndexesHandler.stopProcess();
                outputMessage(resp, result);
            } else if (action.equalsIgnoreCase("status")) {
//                ReIndexingStatus reIndexingStatus = ReIndexingStatus.getInstance();
//                String result = reIndexingStatus.getJsonString();
                String result = rebuildIndexesHandler.showStatus();
                outputMessage(resp, result);
            } else if (action.equalsIgnoreCase("bibStatus")) {
                String result = rebuildIndexesHandler.showBibStatus();
                outputMessage(resp, result);
            } else if (action.equalsIgnoreCase("Clear Status")) {
                ReIndexingStatus reIndexingStatus = ReIndexingStatus.getInstance();
                reIndexingStatus.reset();
            } else if (action.equalsIgnoreCase("store")) {
                String result = rebuildIndexesHandler.storeBibInfo(batchSize);
                outputMessage(resp, result);
            } else {
                String result = "Invalid action :" + action;
                outputMessage(resp, result);
            }
        } catch (Exception e) {
            docStoreLogger.log("Error during rebuilding of the indexes from documentstore", e);
        }
    }

    private void outputMessage(HttpServletResponse resp, String s) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println(s);
        out.flush();
        out.close();
    }
}
