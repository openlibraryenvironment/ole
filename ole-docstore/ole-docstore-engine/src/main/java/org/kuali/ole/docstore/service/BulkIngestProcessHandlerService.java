/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.docstore.service;

import org.apache.camel.CamelContext;
import org.kuali.ole.docstore.process.BulkIngestDocStoreRequestBuilder;
import org.kuali.ole.docstore.process.BulkLoadHandler;
import org.kuali.ole.docstore.process.DocStoreCamelContext;
import org.kuali.ole.docstore.process.ProcessParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Class BulkIngest Process Handler Service.
 *
 * @author Rajesh Chowdary K
 * @version 0.8
 * @created Aug 8, 2012
 */
public class BulkIngestProcessHandlerService {

    private Logger logger = LoggerFactory.getLogger(BulkIngestProcessHandlerService.class);
    private static Set<String> processMonitor = new HashSet<String>();
    private BulkLoadHandler loadHandler = BulkLoadHandler.getInstance();

    /**
     * Method to start Bulk Ingest Process for DocStore Request Format.
     *
     * @throws Exception
     */
    public void startBulkIngestForDocStoreRequestFormat(String folder) throws Exception {
        loadHandler.loadBulk(folder, ProcessParameters.BULK_DEFAULT_USER, ProcessParameters.BULK_DEFUALT_ACTION);
    }

    /**
     * Method to start Bulk Ingest Process For Standard XML Format.
     *
     * @param folder
     * @param category
     * @param type
     * @param format
     * @throws Exception
     */
    public void startBulkIngestForStandardXMLFormat(String folder, String category, String type, String format,
                                                    String bulkIngestUploadDir) throws Exception {
        String cmd = category + "/" + type + "/" + format + "@" + folder;
        try {
            logger.info("Bulk Ingest Process for " + cmd + " \t\t: STARTING...");
            if (processMonitor.contains(cmd)) {
                throw new Exception("Process Already Running : " + cmd);
            }
            CamelContext camelContext = DocStoreCamelContext.getInstance().getCamelContext();
            BulkIngestDocStoreRequestBuilder builder = new BulkIngestDocStoreRequestBuilder(folder,
                    ProcessParameters.BULK_DEFAULT_USER,
                    ProcessParameters.BULK_DEFUALT_ACTION,
                    camelContext, category,
                    type, format,
                    bulkIngestUploadDir);
            camelContext.addRoutes(builder);
            camelContext.start();
            processMonitor.add(cmd);
            logger.info("Bulk Ingest Process for " + cmd + " \t\t: STARTED");
        } catch (Exception e) {
            logger.error("Unable to Start Bulk Ingest Process for " + cmd + ".\n Cuase: " + e.getMessage(), e);
            throw new Exception("Unable to Start Bulk Ingest Process for " + cmd + ".\n Cuase: " + e.getMessage(), e);
        }

    }

    public BulkLoadHandler getLoadHandler() {
        return loadHandler;
    }

    public void setLoadHandler(BulkLoadHandler loadHandler) {
        this.loadHandler = loadHandler;
    }
}
