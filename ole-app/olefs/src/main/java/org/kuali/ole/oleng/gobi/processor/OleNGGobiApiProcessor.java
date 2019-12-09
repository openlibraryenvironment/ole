package org.kuali.ole.oleng.gobi.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jfree.util.Log;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.OleNgBatchResponse;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.dao.GobiDAO;
import org.kuali.ole.gobi.processor.GobiResponseTimer;
import org.kuali.ole.gobi.response.Response;
import org.kuali.ole.gobi.response.ResponseError;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.gobi.service.impl.OleNgGobiOrderImportServiceImpl;
import org.kuali.ole.oleng.util.OleNGPOHelperUtil;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.spring.batch.processor.BatchOrderImportProcessor;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.MarcReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.impl.DataFieldImpl;
import org.marc4j.marc.impl.SubfieldImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by SheikS on 8/3/2016.
 */
public abstract class OleNGGobiApiProcessor extends BatchOrderImportProcessor{


    private OleNGPOHelperUtil oleNGPOHelperUtil;


    public Response process(GobiRequest gobiRequest) {

        Response gobiResponse = new Response();

        OleNgGobiOrderImportServiceImpl oleNgGobiOrderImportService = getOleOrderRecordService();

        oleNgGobiOrderImportService.setOrder(gobiRequest.getPurchaseOrder());

        OleNGPOHelperUtil oleNGPOHelperUtil = getOleNGPOHelperUtil();
        oleNGPOHelperUtil.setOleOrderImportService(oleNgGobiOrderImportService);
        setOleNGPOHelperUtil(oleNGPOHelperUtil);

        BatchProcessProfile batchProcessProfile = fetchBatchProcessProfile(gobiRequest.getProfileIdForDefaultMapping());

        if(null == batchProcessProfile) {
            ResponseError responseError = getResponseError("Batch process profile is null");
            gobiResponse.setError(responseError);
            return gobiResponse;
        }
        UserSession actualUserSession = GlobalVariables.getUserSession();
        prepareUserSession();

        String marcXMLContent = getMarcXMLContent(gobiRequest);
        String updatedMarcXMLContent = updateMarcXMLContentWithYBPKey(marcXMLContent, gobiRequest);
        List<Record> records = getMarcRecordUtil().getMarcXMLConverter().convertMarcXmlToRecord(updatedMarcXMLContent);
        Map<Integer, RecordDetails> recordDetailsMap = getRecordDetailsMap(records);

        BatchJobDetails batchJobDetails = new BatchJobDetails();
        batchJobDetails.setProfileName(batchProcessProfile.getBatchProcessProfileName());

        String reportDirectory = OleNGConstants.GOBI_API + OleNGConstants.DATE_FORMAT.format(new Date());

        BatchProcessTxObject batchProcessTxObject = getBatchProcessTxObject(batchProcessProfile, reportDirectory, batchJobDetails);
        List<String> requisionIds = new ArrayList<>();
        try {
            OleNgBatchResponse oleNgBatchResponse = processRecords(recordDetailsMap, batchProcessTxObject, batchProcessProfile);
            String response = oleNgBatchResponse.getResponse();
            JSONObject responseObject = new JSONObject(response);
            requisionIds = getRequisionIds(responseObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!CollectionUtils.isEmpty(requisionIds)) {
            GobiDAO gobiDAO = (GobiDAO) SpringContext.getService("gobiDAO");
            for (Iterator iterator = requisionIds.iterator(); iterator.hasNext(); ) {
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
        if(actualUserSession != null) {
            GlobalVariables.setUserSession(actualUserSession);
        }
        return gobiResponse;
    }

    private List<String> getRequisionIds(JSONObject responseObject) {
        BatchUtil batchUtil = getOleNGPOHelperUtil().getBatchUtil();
        JSONArray requisitionIdArray = batchUtil.getJSONArrayeFromJsonObject(responseObject, "requisitionIds");
        List<String> requisitionIds = batchUtil.getListFromJSONArray(requisitionIdArray.toString());
        return requisitionIds;
    }

    private ResponseError getResponseError(String message) {
        ResponseError responseError = new ResponseError();
        responseError.setMessage(message);
        responseError.setCode("FAILED");
        return responseError;
    }

    private BatchProcessTxObject getBatchProcessTxObject(BatchProcessProfile batchProcessProfile, String reportDirectory, BatchJobDetails batchJobDetails) {
        BatchProcessTxObject batchProcessTxObject = new BatchProcessTxObject();
        batchProcessTxObject.setBatchProcessProfile(batchProcessProfile);
        batchProcessTxObject.setBatchFileProcessor(this);
        batchProcessTxObject.setBatchJobDetails(batchJobDetails);
        batchProcessTxObject.setReportDirectoryName(reportDirectory);
        batchProcessTxObject.getOleStopWatch().start();
        return batchProcessTxObject;
    }

    public abstract boolean isInterested(GobiRequest gobiRequest);

    public abstract String getMarcXMLContent(GobiRequest gobiRequest);

    protected abstract OleNgGobiOrderImportServiceImpl getOleOrderRecordService();

    protected abstract void linkToOrderOption();

    public BatchProcessProfile fetchBatchProcessProfile(String profileName) {
        BatchProcessProfile batchProcessProfile = null;
        Map parameterMap = new HashedMap();
        parameterMap.put("batchProcessProfileName", profileName);
        List<BatchProcessProfile> matching = (List<BatchProcessProfile>) getBusinessObjectService().findMatching(BatchProcessProfile.class, parameterMap);
        if(CollectionUtils.isNotEmpty(matching)){
            try {
                batchProcessProfile = matching.get(0);
                getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                batchProcessProfile = getObjectMapper().readValue(IOUtils.toString(batchProcessProfile.getContent()), BatchProcessProfile.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return batchProcessProfile;
    }

    private void prepareUserSession() {
        //TODO: Need user info from System Parameter.
        Person person = KimApiServiceLocator.getPersonService().getPerson(ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT, "GOBI_API_USER"));
        if(person==null){
            person = KimApiServiceLocator.getPersonService().getPerson("gobiapi");
        }
        String principalName = person.getPrincipalName();
        UserSession userSession = new UserSession(principalName);
        GlobalVariables.setUserSession(userSession);
    }

    private String updateMarcXMLContentWithYBPKey(String marcXMLContent, GobiRequest gobiRequest) {
        String updatedMarcXMLContent;
        BigInteger ybpOrderKey = null;

        if (null != gobiRequest.getPurchaseOrder().getOrder().getListedElectronicMonograph()) {
            ybpOrderKey = gobiRequest.getPurchaseOrder().getOrder().getListedElectronicMonograph().getOrderDetail().getYBPOrderKey();

        } else if (null != gobiRequest.getPurchaseOrder().getOrder().getUnlistedElectronicMonograph()) {
            ybpOrderKey = gobiRequest.getPurchaseOrder().getOrder().getUnlistedElectronicMonograph().getOrderDetail().getYBPOrderKey();

        } else if (null != gobiRequest.getPurchaseOrder().getOrder().getListedElectronicSerial()) {
            ybpOrderKey = gobiRequest.getPurchaseOrder().getOrder().getListedElectronicSerial().getOrderDetail().getYBPOrderKey();

        } else if (null != gobiRequest.getPurchaseOrder().getOrder().getListedPrintSerial()) {
            ybpOrderKey = gobiRequest.getPurchaseOrder().getOrder().getListedPrintSerial().getOrderDetail().getYBPOrderKey();

        } else if (null != gobiRequest.getPurchaseOrder().getOrder().getUnlistedPrintSerial()) {
            ybpOrderKey = gobiRequest.getPurchaseOrder().getOrder().getUnlistedPrintSerial().getOrderDetail().getYBPOrderKey();

        } else if (null != gobiRequest.getPurchaseOrder().getOrder().getListedPrintMonograph()) {
            ybpOrderKey = gobiRequest.getPurchaseOrder().getOrder().getListedPrintMonograph().getOrderDetail().getYBPOrderKey();

        } else if (null != gobiRequest.getPurchaseOrder().getOrder().getUnlistedPrintMonograph()) {
            ybpOrderKey = gobiRequest.getPurchaseOrder().getOrder().getUnlistedPrintMonograph().getOrderDetail().getYBPOrderKey();
        }

        MarcReader reader = new MarcXmlReader(IOUtils.toInputStream(marcXMLContent));
        Record record = null;
        while (reader.hasNext()) {
            record = reader.next();
            DataField dataField = (DataField) record.getVariableField("980");
            if (null != dataField) {
                record.getDataFields().remove(dataField);
            }
            dataField = new DataFieldImpl();
            dataField.setTag("980");
            dataField.setIndicator1(' ');
            dataField.setIndicator2(' ');

            Subfield sf = new SubfieldImpl();
            sf.setCode('a');
            sf.setData(String.valueOf(ybpOrderKey));

            dataField.addSubfield(sf);

            record.getDataFields().add(dataField);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MarcWriter marcWriter = new MarcXmlWriter(byteArrayOutputStream);
        marcWriter.write(record);
        marcWriter.close();
        updatedMarcXMLContent = byteArrayOutputStream.toString();

        return updatedMarcXMLContent;
    }

    private Map<Integer, RecordDetails> getRecordDetailsMap(List<Record> records) {
        Map<Integer, RecordDetails> recordDetailsMap = new HashMap<>();
        int recordIndex = 1;
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record record = iterator.next();
            RecordDetails recordDetails = new RecordDetails();
            recordDetails.setRecord(record);
            recordDetailsMap.put(recordIndex, recordDetails);
            recordIndex++;
        }
        return recordDetailsMap;
    }

    public OleNGPOHelperUtil getOleNGPOHelperUtil() {
        if(null == oleNGPOHelperUtil) {
            oleNGPOHelperUtil = new OleNGPOHelperUtil();
        }
        return oleNGPOHelperUtil;
    }

    public void setOleNGPOHelperUtil(OleNGPOHelperUtil oleNGPOHelperUtil) {
        this.oleNGPOHelperUtil = oleNGPOHelperUtil;
    }

    private Response getGobiResponse(Response gobiResponse, GobiDAO gobiDAO, String reqId, String documentStatus) {
        String gobiResponseThreadTimeCount = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants.DESCRIBE_COMPONENT, "GOBI_RESPONSE_THREAD_TIME_COUNT");

        int timerCount = StringUtils.isNotBlank(gobiResponseThreadTimeCount) ? Integer.valueOf(gobiResponseThreadTimeCount) : 15;

        int count = 0;
        GobiResponseTimer gobiResponseTimer = new GobiResponseTimer();
        while (count < timerCount) {
            count = count + 1;
            gobiResponse = gobiResponseTimer.processReqResponseForPOCreation(reqId, gobiDAO, documentStatus);
            if (null != gobiResponse.getPoLineNumber() || (null != gobiResponse.getError() && gobiResponse.getError().getCode().equals("PO_FAIL"))) {
                return gobiResponse;
            }
            try {
                Thread.sleep(5 * 1000);
                Log.info("Waiting 5 seconds to get response on PO creation status!");
            } catch (InterruptedException e) {

            }
        }
        gobiResponse.setPoLineNumber("000");
        return gobiResponse;
    }
}
