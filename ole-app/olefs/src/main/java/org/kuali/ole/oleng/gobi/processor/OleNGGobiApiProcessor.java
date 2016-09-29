package org.kuali.ole.oleng.gobi.processor;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.dao.GobiDAO;
import org.kuali.ole.gobi.response.Response;
import org.kuali.ole.gobi.response.ResponseError;
import org.kuali.ole.gobi.service.impl.OleGobiOrderRecordServiceImpl;
import org.kuali.ole.sys.context.SpringContext;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 8/3/2016.
 */
public abstract class OleNGGobiApiProcessor {

    public Response process(GobiRequest gobiRequest) {
        OleGobiOrderRecordServiceImpl oleOrderRecordService = getOleOrderRecordService();
        oleOrderRecordService.setOrder(gobiRequest.getPurchaseOrder());

        setOleOrderRecordService(oleOrderRecordService);

        Response gobiResponse = new Response();

        OLEBatchProcessProfileBo oleBatchProcessProfileBo = loadDefaultMapping(gobiRequest.getProfileIdForDefaultMapping());

        OLEBatchProcessDefinitionDocument processDef = new OLEBatchProcessDefinitionDocument();
        //TODO: Review where to get this from.
        processDef.setUser("gobi-api");
        processDef.setOleBatchProcessProfileBo(oleBatchProcessProfileBo);

        OLEBatchProcessJobDetailsBo job = new OLEBatchProcessJobDetailsBo();
        job.setBatchProfileName(oleBatchProcessProfileBo.getBatchProcessProfileName());

        String marcXMLContent = getMarcXMLContent(gobiRequest);
        String updatedMarcXMLContent = updateMarcXMLContentWithYBPKey(marcXMLContent, gobiRequest);
        setMarcXMLContent(updatedMarcXMLContent);

        prepareIngestRecord(marcXMLContent);

        prepareUserSession();

        boolean bibImportSuccess = processBibRecords(processDef, job);

        if (bibImportSuccess) {

            linkToOrderOption();

            //Create REQ/PO
            postProcess(job);
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setMessage("Unable to create Bib record successfully.");
            responseError.setCode("FAILED");
            gobiResponse.setError(responseError);
            return gobiResponse;
        }


        List reqList = job.getOrderImportHelperBo().getReqList();

        if (!CollectionUtils.isEmpty(reqList)) {
            GobiDAO gobiDAO = (GobiDAO) SpringContext.getService("gobiDAO");
            for (Iterator iterator = reqList.iterator(); iterator.hasNext(); ) {
                String reqId = String.valueOf(iterator.next());
                String documentStatus = gobiDAO.getDocumentStatus(reqId);
                gobiResponse = getGobiResponse(gobiResponse, gobiDAO, reqId, documentStatus);
            }
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setMessage("PO Creation failed. Please consult the institution for any errors on their side.");
            responseError.setCode("FAILED");
            gobiResponse.setError(responseError);
        }
        return gobiResponse;
    }

    public abstract boolean isInterested(GobiRequest gobiRequest);

    public abstract String getMarcXMLContent(GobiRequest gobiRequest);

    protected abstract OleGobiOrderRecordServiceImpl getOleOrderRecordService();

    protected abstract void linkToOrderOption();
}
