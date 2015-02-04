package org.kuali.ole.batch.ingest;


import org.kuali.ole.batch.bo.OLEBatchBibImportDataObjects;
import org.kuali.ole.batch.helper.BatchBibImportHelper;
import org.kuali.ole.batch.helper.BatchGOKbHelperService;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;

import java.util.List;

/**
 * Created by sambasivam on 10/21/14.
 */
public class OLEBatchGOKBImport extends BatchProcessBibImport {


    protected BatchBibImportHelper getBatchBibImportHelper() {
        if(batchBibImportHelper == null) {
            batchBibImportHelper = new BatchGOKbHelperService();
        }
        return batchBibImportHelper;
    }

    protected void createBatchMismatchFile(String misMatchMarcRecords, String recordName) throws Exception {

    }
}
