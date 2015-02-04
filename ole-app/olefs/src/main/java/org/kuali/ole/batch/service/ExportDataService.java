package org.kuali.ole.batch.service;

import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.docstore.common.search.SearchResult;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 7/5/13
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExportDataService {
    Object[] getExportDataBySolr(List<SearchResult> solrDocumentList, OLEBatchProcessProfileBo profile) throws Exception;
}

