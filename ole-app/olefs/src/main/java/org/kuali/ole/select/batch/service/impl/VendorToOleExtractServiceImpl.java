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
package org.kuali.ole.select.batch.service.impl;

import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.select.batch.service.VendorToOleExtractService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;

import java.io.File;

public class VendorToOleExtractServiceImpl implements VendorToOleExtractService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(VendorToOleExtractServiceImpl.class);
    protected OlePurapService olePurapService;

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public boolean loadVendorToOleEtl() {

        try {
            ConfigurationService kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
            String directory = kualiConfigurationService.getPropertyValueAsString(OLEConstants.STAGING_DIRECTORY_KEY);
            String sourcePath = directory + getOlePurapService().getParameter(OLEConstants.SOURCE_FOLDER);
            String logPath = directory + getOlePurapService().getParameter(OLEConstants.LOG_FOLDER);
            String destinationPath = directory + getOlePurapService().getParameter(OLEConstants.DESTINATION_FOLDER);
            String backupFolder = directory + getOlePurapService().getParameter(OLEConstants.BACKUP_FOLDER);
            String filePath = getClass().getClassLoader().getResource("KualiETLConfig.xml").toString();
            if (LOG.isDebugEnabled()) {
                LOG.debug("-------filePath -------------------" + filePath);
                LOG.debug("-----------------ETL Configuration Paths---------------");
                LOG.debug("-----sourcePath------" + sourcePath);
                LOG.debug("-----logPath------" + logPath);
                LOG.debug("-----destinationPath------" + destinationPath);
                LOG.debug("-----backUpFolderPath------" + backupFolder);
            }
            File file = new File(sourcePath);

            int position = filePath.indexOf("/");
            String newFilePath = "";
            if (directory.contains("local")) {
                newFilePath = filePath.substring(position + 1);
            } else {
                newFilePath = "/" + filePath.substring(position + 1);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("-------newFilePath -------------------" + newFilePath);
            }
            String[] args = {"--context_param COMMAND_LINE_CONFIG_FILE_NAME=" + newFilePath, "--context_param COMMAND_LINE_SOUCE_FILE_PATH=" + sourcePath, "--context_param COMMAND_LINE_LOG_FILE_PATH=" + logPath, "--context_param COMMAND_LINE_DESTINATION_FILE_PATH=" + destinationPath, "--context_param COMMAND_LINE_BACKUP_FOLDER_PATH=" + backupFolder};
            LOG.debug("-------ETL Job Started-------------");
            // kuali.kualimainjob_0_1.KualiMainJob kualiMainJob = new kuali.kualimainjob_0_1.KualiMainJob();
            // kualiMainJob.runJobInTOS(args);
            LOG.debug("-------ETL Job Completed-----------");
        } catch (Exception e) {
            LOG.error("VendorToOleExtractServiceImpl.loadVendorToOleEtl():", e);
            throw new RuntimeException(e);
        }

        return true;
    }

}
