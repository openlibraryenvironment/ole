/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.web;

import org.kuali.ole.docstore.process.BulkIngestNIndexProcessor;
import org.kuali.ole.docstore.process.BulkIngestNIndexRouteBuilder;
import org.kuali.ole.docstore.process.BulkLoadHandler;
import org.kuali.ole.docstore.process.batch.BulkProcessRequest;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.service.BulkIngestProcessHandlerService;
import org.kuali.ole.docstore.utility.BulkIngestStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 5/18/11
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class BulkIngestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(BulkIngestServlet.class);
    /* private BulkIngestProcessHandlerService bIService        = (BulkIngestProcessHandlerService) BeanLocator
                                                                      .getBean("bulkIngestProcessHandlerService");
     private              BulkLoadHandler                 bulkLoadHandler              = null;
     private              BulkIngestNIndexRouteBuilder    bulkIngestNIndexRouteBuilder = null;
     private              BulkIngestNIndexProcessor       bulkIngestNIndexProcessor    = null;
     private              BulkIngestStatistics            bulkLoadStatistics           = null;*/
    private BulkIngestStatistics bulkLoadStatistics = BulkIngestStatistics.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            /*if ("Start".equals(req.getParameter("action"))) {
                if ("DocStore Request".equals(req.getParameter("bulkIngestDataFormat"))) {
                    if (bulkLoadHandler != null) {
                        DocStoreCamelContext.getInstance().resume();
                        outputMessage(resp, "Ingestion has started. Please check logs for further details");
                    }
                    else {
                        bIService.startBulkIngestForDocStoreRequestFormat(req.getParameter("bulkIngestFolder1"));
                        bulkLoadHandler = bIService.getLoadHandler();
                        bulkIngestNIndexRouteBuilder = bulkLoadHandler.getBulkRoute();
                        bulkIngestNIndexProcessor = bulkIngestNIndexRouteBuilder.getBulkIngestNIndexProcessor();
                        bulkLoadStatistics = bulkIngestNIndexProcessor.getBulkLoadStatistics();
                        DocStoreCamelContext.getInstance().resume();
                    }
                }
                else if ("Standard Doc Format".equals(req.getParameter("bulkIngestDataFormat"))) {
                    String folder = req.getParameter("bulkIngestFolder");
                    if (folder != null && folder.trim().length() != 0) {
                        bIService.startBulkIngestForStandardXMLFormat(req.getParameter("bulkIngestFolder"),
                                                                      req.getParameter("bulkIngestDocCategory"),
                                                                      req.getParameter("bulkIngestDocType"),
                                                                      req.getParameter("bulkIngestDocFormat"),req.getParameter("bulkIngestFolder1"));
                    }
                    else {
                        outputMessage(resp, "'Folder' field should not be empty.");
                        return;
                    }
                }
                outputMessage(resp, "Ingestion has started. Please check logs for further details");
            }
            else if ("statistics".equals(req.getParameter("action"))) {
                outputMessage(resp, bulkLoadStatistics.getJsonString());
            }
            else if ("Stop".equals(req.getParameter("action"))) {
                outputMessage(resp, "Ingestion process has been stopped.");
                DocStoreCamelContext.getInstance().suspend();
            }
            else if ("Clear Status".equals(req.getParameter("action"))) {
                bulkLoadStatistics.clearBulkIngestStatistics();
            }*/
            BulkProcessRequest bulkProcessRequest = new BulkProcessRequest();
            bulkProcessRequest.setUser(req.getParameter("user"));
            bulkProcessRequest.setOperation(BulkProcessRequest.BulkProcessOperation.INGEST);
            bulkProcessRequest.setDataFolder(req.getParameter("bulkIngestFolder"));
            bulkProcessRequest.setDocCategory(req.getParameter("bulkIngestDocCategory"));
            bulkProcessRequest.setDocType(req.getParameter("bulkIngestDocType"));
            bulkProcessRequest.setDocFormat(req.getParameter("bulkIngestDocFormat"));
            bulkProcessRequest.setBulkIngestFolder(req.getParameter("bulkIngestFolder1"));
            if ("Start".equals(req.getParameter("action"))) {
                bulkProcessRequest.setAction(BulkProcessRequest.BulkProcessAction.START);
                outputMessage(resp, "Ingestion has started. Please check logs for further details");
                if ("DocStore Request".equals(req.getParameter("bulkIngestDataFormat"))) {
                    bulkProcessRequest.setDataFormat(BulkProcessRequest.BulkIngestDataFormat.DOCSTORE);
                } else if ("Standard Doc Format".equals(req.getParameter("bulkIngestDataFormat"))) {
                    bulkProcessRequest.setDataFormat(BulkProcessRequest.BulkIngestDataFormat.STANDARD);
                }
                outputMessage(resp, "Ingestion has started. Please check logs for further details");
            } else if ("statistics".equals(req.getParameter("action"))) {
                bulkProcessRequest.setAction(BulkProcessRequest.BulkProcessAction.STATUS);
                outputMessage(resp, bulkLoadStatistics.getJsonString());
            } else if ("Stop".equals(req.getParameter("action"))) {
                bulkProcessRequest.setAction(BulkProcessRequest.BulkProcessAction.STOP);
                outputMessage(resp, "Ingestion process has been stopped.");
            } else if ("Clear Status".equals(req.getParameter("action"))) {
                bulkProcessRequest.setAction(BulkProcessRequest.BulkProcessAction.CLEAR);
            }
            BeanLocator.getDocumentServiceImpl().bulkProcess(bulkProcessRequest);
        } catch (Exception e) {
            LOG.error("Bulk Ingest STARTUP Failed: ", e);
            outputMessage(resp, "Problem in loading Bulk Ingest!\ncause:\n" + e.getMessage()
                    + "\nPlease refer to Application log for further details!");
        }
    }


    private void outputMessage(HttpServletResponse resp, String s) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println(s);
        out.flush();
        out.close();
    }
}
