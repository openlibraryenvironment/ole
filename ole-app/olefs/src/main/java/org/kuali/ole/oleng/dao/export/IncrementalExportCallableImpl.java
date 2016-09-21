package org.kuali.ole.oleng.dao.export;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.handler.BatchExportHandler;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiks on 20/09/16.
 */
public class IncrementalExportCallableImpl extends ExportCallable {

    public IncrementalExportCallableImpl(Map<String, Map<String, String>> commomFields, JdbcTemplate jdbcTemplate,
                                         List<String> bibIds, int fileNumber, BatchExportHandler batchExportHandler,
                                         BatchProcessTxObject batchProcessTxObject) {
        super(commomFields, jdbcTemplate, fileNumber, batchExportHandler, batchProcessTxObject);
        this.bibIds = new ArrayList<>(bibIds);
        this.bibIdsString = StringUtils.join(bibIds, ',');
    }

    @Override
    public Object call() throws Exception {
        return processRecords();
    }
}

