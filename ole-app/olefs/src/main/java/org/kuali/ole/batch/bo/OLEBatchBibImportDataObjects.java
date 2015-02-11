package org.kuali.ole.batch.bo;


import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.ingest.BatchProcessBibImport;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.OrderBibMarcRecord;
import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.docstore.common.document.ids.HoldingsId;
import org.apache.log4j.Logger;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 7/2/14
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchBibImportDataObjects {
    private static final Logger LOG = Logger.getLogger(OLEBatchBibImportDataObjects.class);
    private BibTrees bibTrees;

    public BibTrees getBibTrees() {
        if (bibTrees == null) {
            bibTrees = new BibTrees();
        }
        return bibTrees;
    }

    public void setBibTreesObj(BibTrees bibTrees) {
        this.bibTrees = bibTrees;
    }


    public List<OrderBibMarcRecord> getResponseOrderRecord(List<OrderBibMarcRecord> orderBibMarcRecords) {
        int createBibCount = 0;
        int updateBibCount = 0;
        int createHoldingsCount = 0;
        int updateHoldingsCount = 0;
        for (int i = 0; i < getBibTrees().getBibTrees().size(); i++) {
            BibTree bibTree = getBibTrees().getBibTrees().get(i);
            OrderBibMarcRecord orderBibMarcRecord = orderBibMarcRecords.get(i);
            if (bibTree.getBib().getResult().equals(DocstoreDocument.ResultType.FAILURE)) {
                orderBibMarcRecord.setFailureReason(bibTree.getBib().getMessage());
                continue;
            } else {
                BibId bibId = new BibId();
                bibId.setId(bibTree.getBib().getId());
                orderBibMarcRecord.setBibId(bibId);
            }
            if(bibTree.getBib().getOperation().name().equalsIgnoreCase(OLEConstants.CREATE_BIB)){
                createBibCount++;
            }
            else if(bibTree.getBib().getOperation().name().equalsIgnoreCase(OLEConstants.UPDATE_BIB)){
                updateBibCount++;
            }
            for (int j = 0; j < bibTree.getHoldingsTrees().size(); j++) {

                HoldingsTree holdingsTree = bibTree.getHoldingsTrees().get(j);
                HoldingsId holdingsId = new HoldingsId();
                if (holdingsTree.getHoldings().getResult().equals(DocstoreDocument.ResultType.FAILURE)) {
                    orderBibMarcRecord.setFailureReason(holdingsTree.getHoldings().getMessage());
                    continue;
                } else {
                    holdingsId.setId(holdingsTree.getHoldings().getId());
                    orderBibMarcRecord.getBibId().getHoldingsIds().add(holdingsId);
                }
                if(holdingsTree.getHoldings().getOperation().name().equalsIgnoreCase(OLEConstants.CREATE_BIB)){
                    createHoldingsCount++;
                }
                else if(holdingsTree.getHoldings().getOperation().name().equalsIgnoreCase(OLEConstants.UPDATE_BIB)){
                    updateHoldingsCount++;
                }
                for (Item item : holdingsTree.getItems()) {
                    if (item.getResult().equals(DocstoreDocument.ResultType.FAILURE)) {
                        orderBibMarcRecord.setFailureReason(item.getMessage());
                        continue;
                    } else {
                        holdingsId.getItems().add(item.getId());

                    }
                }
            }
            orderBibMarcRecord.setCreateBibCount(createBibCount);
            orderBibMarcRecord.setUpdateBibCount(updateBibCount);
            orderBibMarcRecord.setCreateHoldingsCount(createHoldingsCount);
            orderBibMarcRecord.setUpdateHoldingsCount(updateHoldingsCount);
        }

        return orderBibMarcRecords;
    }

    public List<OrderBibMarcRecord> processBibImport(List<BibMarcRecord> records,BatchProcessBibImport batchProcessBibImport)throws Exception{
        List<OrderBibMarcRecord> orderBibMarcRecords = new ArrayList<>();
        for(BibMarcRecord record:records){
            OrderBibMarcRecord orderBibMarcRecord = new OrderBibMarcRecord();
            orderBibMarcRecord.setBibMarcRecord(record);
            orderBibMarcRecords.add(orderBibMarcRecord);
        }
        try {
            orderBibMarcRecords= batchProcessBibImport.processBatchOrder(orderBibMarcRecords);
        } catch (Exception e) {
            LOG.error("Exception when calling  bib import: ", e);
        }
        return orderBibMarcRecords;
    }
}

