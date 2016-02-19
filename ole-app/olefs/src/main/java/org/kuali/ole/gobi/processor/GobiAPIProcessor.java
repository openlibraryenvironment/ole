package org.kuali.ole.gobi.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.ingest.BatchProcessBibImport;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.OrderBibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.dao.GobiDAO;
import org.kuali.ole.gobi.datobjects.CollectionType;
import org.kuali.ole.gobi.response.Response;
import org.kuali.ole.gobi.response.ResponseError;
import org.kuali.ole.gobi.service.impl.OleGobiOrderRecordServiceImpl;
import org.kuali.ole.ingest.IngestProcessor;
import org.kuali.ole.ingest.pojo.IngestRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.*;

public abstract class GobiAPIProcessor extends IngestProcessor {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(GobiAPIProcessor.class);

    private BatchProcessBibImport batchProcessBibImport;

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
        setMarcXMLContent(marcXMLContent);

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

    private Response getGobiResponse(Response gobiResponse, GobiDAO gobiDAO, String reqId, String documentStatus) {
        int count = 0;
        GobiResponseTimer gobiResponseTimer = new GobiResponseTimer();
        while (count < 5) {
            count = count + 1;
            gobiResponse = gobiResponseTimer.processReqResponseForPOCreation(reqId, gobiDAO, documentStatus);
            if (null != gobiResponse.getPoLineNumber() || (null != gobiResponse.getError() && gobiResponse.getError().getCode().equals("PO_FAIL"))) {
                break;
            }
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {

            }
        }
        return gobiResponse;
    }

    private boolean processBibRecords(OLEBatchProcessDefinitionDocument processDef, OLEBatchProcessJobDetailsBo job) {
        boolean isPreprocessDone = false;
        try {
            isPreprocessDone = marcProcess(processDef, job);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPreprocessDone;
    }


    private void prepareUserSession() {
        if (null == GlobalVariables.getUserSession() ||
                StringUtils.isBlank(GlobalVariables.getUserSession().getPrincipalName())) {

            //TODO: Need user info from System Parameter.
            Person person = KimApiServiceLocator.getPersonService().getPerson("olequickstart");
            String principalName = person.getPrincipalName();
            UserSession userSession = new UserSession(principalName);
            GlobalVariables.setUserSession(userSession);
        }
    }

    private void prepareIngestRecord(String marcXMLContent) {
        String agendaName = OLEConstants.PROFILE_AGENDA_NM;
        ;
        IngestRecord ingestRecord = new IngestRecord();
        ingestRecord.setOriginalMarcFileName("APIRequest");
        ingestRecord.setMarcFileContent(marcXMLContent);
        ingestRecord.setAgendaName(agendaName);
        ingestRecord.setAgendaDescription("GOBI API Process");
        ingestRecord.setByPassPreProcessing(true);
        setIngestRecord(ingestRecord);
    }

    private BatchProcessBibImport getBatchProcessBibImport() {
        if (null == batchProcessBibImport) {
            batchProcessBibImport = new BatchProcessBibImport();
        }
        return batchProcessBibImport;
    }

    public OLEBatchProcessProfileBo loadDefaultMapping(String profileName) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("batchProcessProfileName", profileName);
        List<OLEBatchProcessProfileBo> matchingProfileBos = (List<OLEBatchProcessProfileBo>) getBusinessObjectService().findMatching(OLEBatchProcessProfileBo.class, parameterMap);
        return CollectionUtils.isNotEmpty(matchingProfileBos) ? matchingProfileBos.get(0) : null;
    }

    protected List<OrderBibMarcRecord> extractOrderBibMarcRecords(CollectionType collection) {
        List<OrderBibMarcRecord> orderBibMarcRecords = new ArrayList<>();
        if (null != collection) {
            String collectionSerializedContent = collection.serialize(collection);
            BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
            BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(collectionSerializedContent);
            for (BibMarcRecord record : bibMarcRecords.getRecords()) {
                OrderBibMarcRecord orderBibMarcRecord = new OrderBibMarcRecord();
                orderBibMarcRecord.setBibMarcRecord(record);
                orderBibMarcRecords.add(orderBibMarcRecord);
            }
        }
        return orderBibMarcRecords;
    }

    public abstract boolean isInterested(GobiRequest gobiRequest);

    public abstract String getMarcXMLContent(GobiRequest gobiRequest);

    protected abstract OleGobiOrderRecordServiceImpl getOleOrderRecordService();

    protected abstract void linkToOrderOption();
}
