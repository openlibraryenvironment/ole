package org.kuali.ole.batch.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.batch.export.BatchProcessExportData;
import org.kuali.ole.batch.ingest.*;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 7/8/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchProcessFactory {
    private static final Logger LOG = Logger.getLogger(BatchProcessFactory.class);

    /**
     * Factory method to create and return the batch process based on the process type in the job
     *
     * @param processType
     * @return
     */
    public static OLEBatchProcess createProcess(String processType) {
        if (processType == null) {
            LOG.error("::Process Type is NULL Cannot create Batch process::");
            throw new RuntimeException("::Process Type is NULL Cannot create Batch process::");
        }
        if (processType.equalsIgnoreCase("Location Import")) {
            return new BatchProcessLocationIngest();
        } else if (processType.equals("Patron Import")) {
            return new BatchProcessPatronIngest();
        } else if (processType.equals("Order Record Import")) {
            return new BatchProcessOrderIngest();
        } else if (processType.equals("Batch Export")) {
            return new BatchProcessExportData();
        } else if (processType.equals("Invoice Import")) {
            return new BatchProcessInvoiceIngest();
        } else if (processType.equals("Batch Delete")) {
            return new BatchProcessDeleteData();
        } else if (processType.equals("Bib Import")) {
            return new BatchProcessBibImport();
        } else if (processType.equals("Claim Report")) {
            return new OLEBatchProcessClaimReport();
        } else if (processType.equals("Serial Record Import")) {
            return new OLEBatchProcessSerialRecordImport();
        } else if (processType.equals("GOKB Import")) {
            return new OLEBatchGOKBImport();
        } else if (processType.equals("Fund Record Import")) {
            return new BatchProcessFundCodeImport();
        } else {
            return null;
        }
    }
}
