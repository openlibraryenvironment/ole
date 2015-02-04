package org.kuali.ole.web;

import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.process.LinkingInstanceNBibHandler;
import org.kuali.ole.logger.DocStoreLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/4/12
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkingBibNInstanceAfterBulkIngest
        extends HttpServlet {

    DocStoreLogger docStoreLogger = new DocStoreLogger(getClass().getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        outputMessage(resp, "Linking of Instsnce and Bib records started. Please check logs for further details");
        try {
            String action = req.getParameter("action");
            LinkingInstanceNBibHandler link = LinkingInstanceNBibHandler
                    .getInstance(DocCategory.WORK.getCode(), DocType.INSTANCE.getDescription(),
                            DocFormat.OLEML.getCode());
            if (!link.isRunning()) {
                link.startProcess();
            } else {
                outputMessage(resp, "Rebuild Indexes Process is already running");
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
