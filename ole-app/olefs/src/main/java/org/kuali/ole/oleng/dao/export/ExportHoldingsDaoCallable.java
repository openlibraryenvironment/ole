package org.kuali.ole.oleng.dao.export;

import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.response.OleNGBatchExportResponse;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.BatchExportHandler;
import org.kuali.ole.oleng.helper.ExportEholdingsMappingHelper;
import org.kuali.ole.oleng.helper.ExportHoldingsMappingHelper;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by sheiks on 31/08/16.
 */
public class ExportHoldingsDaoCallable implements Callable {

    private HoldingsTree holdingsTree;
    private BatchProcessProfile batchProcessProfile;
    private OleNGBatchExportResponse oleNGBatchExportResponse;
    private BatchExportHandler batchExportHandler;
    private String bibId;
    private BatchProcessTxObject batchProcessTxObject;

    public ExportHoldingsDaoCallable(HoldingsTree holdingsTree, String bibId, BatchProcessTxObject batchProcessTxObject, BatchProcessProfile batchProcessProfile, OleNGBatchExportResponse oleNGBatchExportResponse, BatchExportHandler batchExportHandler) {
        this.holdingsTree = holdingsTree;
        this.batchProcessProfile = batchProcessProfile;
        this.oleNGBatchExportResponse = oleNGBatchExportResponse;
        this.batchExportHandler = batchExportHandler;
        this.bibId = bibId;
        this.batchProcessTxObject=batchProcessTxObject;
    }

    @Override
    public Object call() throws Exception {
        List<DataField> dataFields = new ArrayList<>();
        try {
            if (holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase(OleNGConstants.PRINT)) {
                dataFields = new ExportHoldingsMappingHelper().generateDataFieldForHolding(holdingsTree, batchProcessProfile);
            } else {
                dataFields = new ExportEholdingsMappingHelper().generateDataFieldForEHolding(holdingsTree, batchProcessProfile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            batchExportHandler.addBatchExportFailureResponseToExchange(e, bibId, batchProcessTxObject.getExchangeObjectForBatchExport());
        }
        return dataFields;
    }
}
