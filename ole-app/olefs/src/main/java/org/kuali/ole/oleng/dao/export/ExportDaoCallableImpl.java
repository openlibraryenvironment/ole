package org.kuali.ole.oleng.dao.export;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.handler.BatchExportHandler;
import org.kuali.ole.oleng.util.BatchExportUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by rajeshbabuk on 4/25/16.
 */
public class ExportDaoCallableImpl extends ExportCallable{

    private String query;
    private int start;
    private int chunkSize;
    private BatchExportUtil batchExportUtil;

    public ExportDaoCallableImpl(Map<String, Map<String, String>> commomFields,
                                 JdbcTemplate jdbcTemplate, String query, int start, int chunkSize, int fileNumber,
                                 BatchExportHandler batchExportHandler, BatchProcessTxObject batchProcessTxObject) {
        super(commomFields, jdbcTemplate, fileNumber, batchExportHandler, batchProcessTxObject);
        this.query = query;
        this.start = start;
        this.chunkSize = chunkSize;
    }

    @Override
    public Object call() throws Exception {
        Set<String> bibIdentifiers = getBatchExportUtil().getBibIdentifiersForQuery(query, start, chunkSize);
        this.bibIdsString = StringUtils.join(bibIdentifiers, ',');
        bibIds = new ArrayList<>(bibIdentifiers);
        return processRecords();
    }


    public BatchExportUtil getBatchExportUtil() {
        if(null == batchExportUtil) {
            batchExportUtil = new BatchExportUtil();
        }
        return batchExportUtil;
    }
}
