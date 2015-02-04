package org.kuali.ole.service;

import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.docstore.common.document.HoldingsTrees;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.pojo.edi.EDIOrder;
import org.kuali.rice.krms.api.engine.EngineResults;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 2/28/13
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OleOrderRecordService {
    public OleOrderRecord fetchOleOrderRecordForMarcEdi(String bibId, EDIOrder ediOrder, BibMarcRecord bibMarcRecord,int recordPosition, OLEBatchProcessJobDetailsBo job) throws Exception;
    public OleOrderRecord fetchOleOrderRecordForMarc(String bibId, BibMarcRecord bibMarcRecord,int recordPosition, OLEBatchProcessJobDetailsBo job) throws Exception;
    public boolean validateDefaultLocation(String defaultLocation);
    public boolean validateVendorNumber(String vendorNumber);
    public boolean validateDestinationFieldValues(String destinationFieldValue);
    public boolean validateForPercentage(String percentage);
    public boolean validateForNumber(String fieldValue);
    public boolean checkRequestorName(String requestorName);
    public boolean validateItemStatus(String itemStatus);
    public List<OleTxRecord> getQuantityItemPartsLocation(List<BibMarcRecord> bibMarcRecords, OLEBatchProcessJobDetailsBo job);
}
