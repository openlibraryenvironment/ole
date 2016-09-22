package org.kuali.ole.oleng.dao.export;

import org.kuali.ole.oleng.handler.BatchExportHandler;
import org.kuali.ole.oleng.util.BatchExportUtil;

import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by sheiks on 16/09/16.
 */
public class ExportBibIdFinderCallable implements Callable {

    private String query;
    private int start;
    private int chunkSize;
    private BatchExportHandler batchExportHandler;
    private BatchExportUtil batchExportUtil;

    public ExportBibIdFinderCallable(String query, int start, int chunkSize, BatchExportHandler batchExportHandler) {
        this.query = query;
        this.start = start;
        this.chunkSize = chunkSize;
        this.batchExportHandler = batchExportHandler;

    }

    @Override
    public Object call() throws Exception {
        Set<String> bibIdentifiers = getBatchExportUtil().getBibIdentifiersForQuery(query, start, chunkSize);
        return bibIdentifiers;
    }

    public BatchExportUtil getBatchExportUtil() {
        if(null == batchExportUtil) {
            batchExportUtil = new BatchExportUtil();
        }
        return batchExportUtil;
    }

    public void setBatchExportUtil(BatchExportUtil batchExportUtil) {
        this.batchExportUtil = batchExportUtil;
    }
}
