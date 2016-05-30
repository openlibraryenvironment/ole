package org.kuali.ole.oleng.batch.reports;

import org.kuali.ole.oleng.batch.reports.processors.BatchExportDeletedIdsFileProcessor;
import org.kuali.ole.oleng.batch.reports.processors.BatchExportMarcFileProcessor;
import org.kuali.ole.oleng.batch.reports.processors.BatchExportMarcXmlFileProcessor;
import org.kuali.ole.oleng.batch.reports.processors.OleNGReportProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeshbabuk on 4/26/16.
 */
public class BatchExportFileLogHandler extends ReportLogHandler {
    public static BatchExportFileLogHandler batchExportFileLogHandler;

    private BatchExportFileLogHandler() {
    }

    public static BatchExportFileLogHandler getInstance() {
        if (null == batchExportFileLogHandler) {
            batchExportFileLogHandler = new BatchExportFileLogHandler();
        }
        return batchExportFileLogHandler;
    }

    /**
     * The order of adding the processors matters.
     */
    public List<OleNGReportProcessor> getProcessors() {
        if (null == processors) {
            processors = new ArrayList<>();
            processors.add(new BatchExportMarcFileProcessor());
            processors.add(new BatchExportMarcXmlFileProcessor());
            processors.add(new BatchExportDeletedIdsFileProcessor());
        }
        return processors;
    }
}

