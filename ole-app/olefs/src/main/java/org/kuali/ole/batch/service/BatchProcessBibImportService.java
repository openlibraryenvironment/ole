package org.kuali.ole.batch.service;

import org.kuali.ole.batch.bo.OLEBatchBibImportDataObjects;
import org.kuali.ole.batch.bo.OLEBatchBibImportStatistics;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.OrderBibMarcRecord;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 8/8/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BatchProcessBibImportService {

    public Bib performProcessBib(BibMarcRecord bibRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo, String staffOnly) throws Exception;

    public Bib findMatchingBibRecord(BibMarcRecord bibRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo, List<BibMarcRecord> failureRecordsList) throws Exception;

    public String preProcessMarc(String marcFileContent) throws Exception;

    public void process001(BibMarcRecord bibMarcRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo);

    public BibMarcRecord overlayFields(BibMarcRecord matchedRecord, BibMarcRecord inComingRecord, OLEBatchProcessProfileBo processProfile);

    List<BibMarcRecord> saveBatch(List<BibMarcRecord> bibMarcRecords,  OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, OLEBatchBibImportStatistics OleBatchbibImportStatistics);

    List<OrderBibMarcRecord> saveOderBatch(List<OrderBibMarcRecord> orderBibMarcRecords, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, OLEBatchBibImportStatistics bibImportStatistics);
}
