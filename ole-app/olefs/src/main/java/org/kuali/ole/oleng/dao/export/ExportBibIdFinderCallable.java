package org.kuali.ole.oleng.dao.export;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.handler.BatchExportHandler;

import java.util.HashSet;
import java.util.List;
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

    public ExportBibIdFinderCallable(String query, int start, int chunkSize, BatchExportHandler batchExportHandler) {
        this.query = query;
        this.start = start;
        this.chunkSize = chunkSize;
        this.batchExportHandler = batchExportHandler;

    }

    @Override
    public Object call() throws Exception {
        Set<String> bibIdentifiers = new HashSet<>();
        SolrDocumentList solrDocumentList = batchExportHandler.getSolrRequestReponseHandler().getSolrDocumentList(query, start, chunkSize, OleNGConstants.BIB_IDENTIFIER);
        if (solrDocumentList.size() > 0) {
            for (SolrDocument solrDocument : solrDocumentList) {
                if (solrDocument.containsKey(OleNGConstants.BIB_IDENTIFIER)) {
                    List<String> bibIds = (List) solrDocument.getFieldValue(OleNGConstants.BIB_IDENTIFIER);
                    for (String bibId : bibIds) {
                        bibIdentifiers.add(DocumentUniqueIDPrefix.getDocumentId(bibId));
                    }
                }
            }
        }
        return bibIdentifiers;
    }
}
