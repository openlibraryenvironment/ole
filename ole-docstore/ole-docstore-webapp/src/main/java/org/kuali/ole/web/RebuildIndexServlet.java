package org.kuali.ole.web;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.engine.service.storage.rdbms.dao.CallNumberMigrationDao;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.engine.service.storage.rdbms.dao.RebuildIndexDao;
import org.kuali.ole.docstore.metrics.reindex.ReIndexingStatus;
import org.kuali.ole.docstore.process.RebuildIndexesHandler;
import org.kuali.ole.logger.DocStoreLogger;
import org.kuali.rice.core.api.config.property.ConfigContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "reindex";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reindexResult ="";
        try {

            String docCategory = DocCategory.WORK.getCode();
            String docType = DocType.BIB.getCode();
            String docFormat = DocFormat.MARC.getCode();
            String action = req.getParameter("action");
            String batchSizeReq = req.getParameter("batchSize");
            String startIndexReq = req.getParameter("startIndex");
            String endIndexReq = req.getParameter("endIndex");
            String updateDate = null;
            if (req.getParameter("updateDate") != null) {
                updateDate = req.getParameter("updateDate").trim();
            }
            int batchSize = 0;
            int startIndex = 0;
            int endIndex = 0;
            if (StringUtils.isNotEmpty(batchSizeReq)) {
                batchSize = Integer.parseInt(batchSizeReq);
            }
            if (StringUtils.isNotEmpty(startIndexReq)) {
                startIndex = Integer.parseInt(startIndexReq);
            }
            if (StringUtils.isNotEmpty(endIndexReq)) {
                endIndex = Integer.parseInt(endIndexReq);
            }
            if (startIndex > endIndex) {
                String result = "Please provide valid input " + startIndex + " > " + endIndex;
                outputMessage(resp, result);
            }
            RebuildIndexesHandler rebuildIndexesHandler = RebuildIndexesHandler.getInstance();

            if (action.equalsIgnoreCase("start")) {
                String result = rebuildIndexesHandler.startProcess(docCategory, docType, docFormat, batchSize, startIndex, endIndex, updateDate);
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
            } else if (action.equalsIgnoreCase("shelfKey")) {
                CallNumberMigrationDao callNumberMigration = (CallNumberMigrationDao) org.kuali.ole.dsng.service.SpringContext.getBean("callNumberMigrationDao");
                callNumberMigration.init();
            } else if (action.equalsIgnoreCase("fromFile")) {
                reindexResult =uploadFile(req,rebuildIndexesHandler);
            }
             else {
                String result = "Invalid action :" + action;
                outputMessage(resp, result);
            }
        } catch (Exception e) {
            docStoreLogger.log("Error during rebuilding of the indexes from documentstore", e);
        }
        if (ServletFileUpload.isMultipartContent(req)) {
            req.setAttribute("reindexResult",reindexResult);
            req.getRequestDispatcher("/admin.jsp").forward(req, resp);
        }else{
            req.setAttribute("reindexResult","");
        }
    }

    private void outputMessage(HttpServletResponse resp, String s) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println(s);
        out.flush();
        out.close();
    }



    public String uploadFile(HttpServletRequest req, RebuildIndexesHandler rebuildIndexesHandler) throws Exception {
        String filePath =null;
        if (ServletFileUpload.isMultipartContent(req)) {
            String uploadPath = ConfigContext.getCurrentContextConfig().getProperty("staging.directory")
                    + File.separator + UPLOAD_DIRECTORY;

            // creates the directory if it does not exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            List<FileItem> formItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // saves the file on disk
                        item.write(storeFile);
                    }
                }
            }
        }

        RebuildIndexDao rebuildIndexDao = (RebuildIndexDao) org.kuali.ole.dsng.service.SpringContext.getBean("rebuildIndexDao");
        return rebuildIndexesHandler.reindexFromFile(filePath,rebuildIndexDao);
    }
}
