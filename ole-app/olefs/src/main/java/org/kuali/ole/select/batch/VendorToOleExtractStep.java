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

import org.kuali.ole.select.batch.service.VendorToOleExtractService;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.ole.sys.batch.BatchInputFileType;
import org.kuali.ole.sys.batch.service.BatchInputFileService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendorToOleExtractStep extends AbstractStep {


    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(VendorToOleExtractStep.class);

    private BatchInputFileService batchInputFileService;
    private BatchInputFileType marcInputFileType;
    private VendorToOleExtractService vendorToOleExtractService;

    public VendorToOleExtractStep() {
        super();
    }

    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {

        boolean processSuccess = true;
        List<String> fileNamesToLoad = batchInputFileService.listInputFileNamesWithDoneFile(marcInputFileType);
        if (fileNamesToLoad != null && fileNamesToLoad.size() > 0) {
            List<String> processedFiles = new ArrayList();
            for (String inputFileName : fileNamesToLoad) {
                //processSuccess = vendorToOleExtractService.loadVendorToOleEtl().loadVendorToOleEtl(inputFileName);
                processSuccess = vendorToOleExtractService.loadVendorToOleEtl();
                if (processSuccess) {
                    processedFiles.add(inputFileName);
                }
            }
        } else
            processSuccess = vendorToOleExtractService.loadVendorToOleEtl();
        return processSuccess;

    }

    public boolean execute() throws InterruptedException {
        try {
            return execute(null, null);
        } catch (InterruptedException e) {
            LOG.error("Exception occured executing step", e);
            throw e;
        } catch (RuntimeException e) {
            LOG.error("Exception occured executing step", e);
            throw e;
        }
    }

    public BatchInputFileService getBatchInputFileService() {
        return batchInputFileService;
    }

    public void setBatchInputFileService(BatchInputFileService batchInputFileService) {
        this.batchInputFileService = batchInputFileService;
    }

    public BatchInputFileType getMarcInputFileType() {
        return marcInputFileType;
    }

    public void setMarcInputFileType(BatchInputFileType marcInputFileType) {
        this.marcInputFileType = marcInputFileType;
    }

    public VendorToOleExtractService getVendorToOleExtractService() {
        return vendorToOleExtractService;
    }

    public void setVendorToOleExtractService(VendorToOleExtractService vendorToOleExtractService) {
        this.vendorToOleExtractService = vendorToOleExtractService;
    }

}
