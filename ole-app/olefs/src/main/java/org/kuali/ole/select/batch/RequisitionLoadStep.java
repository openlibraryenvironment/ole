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
package org.kuali.ole.select.batch;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.batch.service.RequisitionLoadTransactionsService;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.ole.sys.batch.BatchInputFileType;
import org.kuali.ole.sys.batch.service.BatchInputFileService;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This step will call a service method to load the vendor  xml file into the transaction table. Validates the data before the
 * load.
 */
public class RequisitionLoadStep extends AbstractStep {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RequisitionLoadStep.class);

    private BatchInputFileService batchInputFileService;
    private RequisitionLoadTransactionsService requisitionLoadTransactionsService;
    private BatchInputFileType requisitionInputFileType;

    /**
     * Controls the procurement card process.
     */
    public boolean execute(String jobName, Date jobRunDate) {

        List<String> fileNamesToLoad = batchInputFileService.listInputFileNamesWithDoneFile(requisitionInputFileType);
        boolean processSuccess = true;
        List<String> processedFiles = new ArrayList();
        for (String inputFileName : fileNamesToLoad) {
            processSuccess = requisitionLoadTransactionsService.loadRequisitionFile(inputFileName);
            if (processSuccess) {
                processedFiles.add(inputFileName);
            }
        }

        removeDoneFiles(processedFiles);

        return processSuccess;
    }

    /**
     * Clears out associated .done files for the processed data files.
     */
    private void removeDoneFiles(List<String> dataFileNames) {

        for (String dataFileName : dataFileNames) {
            File doneFile = new File(StringUtils.substringBeforeLast(dataFileName, ".") + ".done");
            if (doneFile.exists()) {
                doneFile.delete();
            }
        }

    }

    public BatchInputFileService getBatchInputFileService() {
        return batchInputFileService;
    }

    public void setBatchInputFileService(BatchInputFileService batchInputFileService) {
        this.batchInputFileService = batchInputFileService;
    }

    public RequisitionLoadTransactionsService getRequisitionLoadTransactionsService() {
        return requisitionLoadTransactionsService;
    }

    public void setRequisitionLoadTransactionsService(RequisitionLoadTransactionsService requisitionLoadTransactionsService) {
        this.requisitionLoadTransactionsService = requisitionLoadTransactionsService;
    }

    public BatchInputFileType getRequisitionInputFileType() {
        return requisitionInputFileType;
    }

    public void setRequisitionInputFileType(BatchInputFileType requisitionInputFileType) {
        this.requisitionInputFileType = requisitionInputFileType;
    }

}
