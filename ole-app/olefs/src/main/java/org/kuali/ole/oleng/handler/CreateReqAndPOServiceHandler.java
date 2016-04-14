package org.kuali.ole.oleng.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.instance.DonorInfo;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemTypeRecord;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.service.OleNGRequisitionService;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.spring.batch.processor.BatchBibFileProcessor;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.MarcRecordUtil;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.marc.Record;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by SheikS on 12/18/2015.
 */
@Service("createReqAndPOServiceHandler")
public class CreateReqAndPOServiceHandler extends BatchUtil implements CreateReqAndPOBaseServiceHandler {
    private OleNGRequisitionService oleNGRequisitionService;
    private DataCarrierService dataCarrierService;

    public Integer processOrder(List<OleOrderRecord> oleOrderRecords, Exchange exchange) throws Exception {
        GlobalVariables.setUserSession(new UserSession("ole-quickstart"));
        OleRequisitionDocument requisitionDocument = getOleNGRequisitionService().createPurchaseOrderDocument(oleOrderRecords,exchange);
        List items = requisitionDocument.getItems();
        if(CollectionUtils.isNotEmpty(items)) {
            for (Iterator iterator = items.iterator(); iterator.hasNext(); ) {
                OleRequisitionItem oleRequisitionItem = (OleRequisitionItem) iterator.next();
                String itemTypeCode = oleRequisitionItem.getItemTypeCode();
                try {
                    if(StringUtils.isNotBlank(itemTypeCode) && itemTypeCode.equalsIgnoreCase(OLEConstants.ITEM)) {
                        String bibUUID = oleRequisitionItem.getBibUUID();
                        BibRecord bibRecord = getBusinessObjectService().findBySinglePrimaryKey(BibRecord.class, DocumentUniqueIDPrefix.getDocumentId(bibUUID));
                        List<Record> records = new MarcRecordUtil().convertMarcXmlContentToMarcRecord(bibRecord.getContent());
                        String bibProfileName = oleOrderRecords.get(0).getBibImportProfileName();
                        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
                        if(StringUtils.isNotBlank(bibProfileName)) {
                            BatchProcessProfile processProfile = fetchBatchProcessProfile(bibProfileName);
                            if(oleRequisitionItem.getLinkToOrderOption().equalsIgnoreCase(org.kuali.ole.OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT)) {
                                List<JSONObject> preTransformForHoldings = batchBibFileProcessor.prepareDataMappings(records, processProfile,
                                        OleNGConstants.HOLDINGS, OleNGConstants.PRE_MARC_TRANSFORMATION, false);
                                List<JSONObject> postTransformForHoldings = batchBibFileProcessor.prepareDataMappings(records, processProfile,
                                        OleNGConstants.HOLDINGS, OleNGConstants.POST_MARC_TRANSFORMATION, false);

                                List<JSONObject> dataMappingForHoldings = batchBibFileProcessor.buildOneObjectForList(preTransformForHoldings, postTransformForHoldings);

                                List<JSONObject> preTransformForItem = batchBibFileProcessor.prepareDataMappings(records, processProfile,
                                        OleNGConstants.ITEM, OleNGConstants.PRE_MARC_TRANSFORMATION, false);
                                List<JSONObject> postTransformForItem = batchBibFileProcessor.prepareDataMappings(records, processProfile,
                                        OleNGConstants.ITEM, OleNGConstants.POST_MARC_TRANSFORMATION, false);

                                List<JSONObject> dataMappingForItem = batchBibFileProcessor.buildOneObjectForList(preTransformForItem, postTransformForItem);

                                Holdings dummyHoldingsFromDataMapping = createDummyHoldingsFromDataMapping(dataMappingForHoldings);
                                Item dummyItemFromDataMapping = createDummyItemFromDataMapping(dataMappingForItem);

                                getDataCarrierService().addData("reqItemId:" + oleRequisitionItem.getItemIdentifier()+":holdings",dummyHoldingsFromDataMapping);
                                getDataCarrierService().addData("reqItemId:" + oleRequisitionItem.getItemIdentifier()+":item",dummyItemFromDataMapping);
                            } else if(oleRequisitionItem.getLinkToOrderOption().equalsIgnoreCase(org.kuali.ole.OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC)) {
                                List<JSONObject> preTransformForHoldings = batchBibFileProcessor.prepareDataMappings(records, processProfile,
                                        OleNGConstants.EHOLDINGS, OleNGConstants.PRE_MARC_TRANSFORMATION, false);
                                List<JSONObject> postTransformForHoldings = batchBibFileProcessor.prepareDataMappings(records, processProfile,
                                        OleNGConstants.EHOLDINGS, OleNGConstants.POST_MARC_TRANSFORMATION, false);

                                List<JSONObject> dataMappingForEHoldings = batchBibFileProcessor.buildOneObjectForList(preTransformForHoldings, postTransformForHoldings);

                                Holdings dummyHoldingsFromDataMapping = createDummyEHoldingsFromDataMapping(dataMappingForEHoldings);
                                getDataCarrierService().addData("reqItemId:" + oleRequisitionItem.getItemIdentifier()+":holdings",dummyHoldingsFromDataMapping);
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    addOrderFaiureResponseToExchange(e, null, exchange);
                }

            }
        }
        return requisitionDocument.getPurapDocumentIdentifier();
    }

    public OleNGRequisitionService getOleNGRequisitionService() {
        return oleNGRequisitionService;
    }

    public void setOleNGRequisitionService(OleNGRequisitionService oleNGRequisitionService) {
        this.oleNGRequisitionService = oleNGRequisitionService;
    }

    private Holdings createDummyHoldingsFromDataMapping(List<JSONObject> dataMappingForHoldings) {
        JSONObject request = new JSONObject();
        try {
            if(CollectionUtils.isNotEmpty(dataMappingForHoldings)) {
                request.put(OleNGConstants.DATAMAPPING, dataMappingForHoldings.get(0));
            }
            request.put(OleNGConstants.HOLDINGS_TYPE, PHoldings.PRINT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String response = getOleDsNgRestClient().postData(OleNGConstants.CREATE_DUMMY_HOLDINGS, request, OleDsNgRestClient.Format.JSON);

        Holdings holdings = null;
        try {
            JSONObject responseObject = new JSONObject(response);
            String content = getStringValueFromJsonObject(responseObject, OleNGConstants.CONTENT);
            holdings = (PHoldings) new PHoldings().deserialize(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(null == holdings) {
            holdings = new PHoldings();
        }
        OleHoldings oleHoldings = holdings.getContentObject();
        holdings.setContentObject(oleHoldings);
        holdings.serializeContent();
        return holdings;
    }

    private Item createDummyItemFromDataMapping(List<JSONObject> dataMappingForItem) {
        JSONObject request = new JSONObject();
        try {
            if(CollectionUtils.isNotEmpty(dataMappingForItem)) {
                request.put(OleNGConstants.DATAMAPPING, dataMappingForItem.get(0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String response = getOleDsNgRestClient().postData(OleNGConstants.CREATE_DUMMY_ITEM, request, OleDsNgRestClient.Format.JSON);

        ItemOleml itemOleml = new ItemOleml();
        Item item = null;
        try {
            JSONObject responseObject = new JSONObject(response);
            String content = getStringValueFromJsonObject(responseObject, OleNGConstants.CONTENT);
            item = (Item) itemOleml.deserialize(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(null == item) {
            item = new Item();
        }
        return item;
    }

    private Holdings createDummyEHoldingsFromDataMapping(List<JSONObject> dataMappingForHoldings) {
        JSONObject request = new JSONObject();
        try {
            if(CollectionUtils.isNotEmpty(dataMappingForHoldings)) {
                request.put(OleNGConstants.DATAMAPPING, dataMappingForHoldings.get(0));
            }
            request.put(OleNGConstants.HOLDINGS_TYPE, EHoldings.ELECTRONIC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String response = getOleDsNgRestClient().postData(OleNGConstants.CREATE_DUMMY_HOLDINGS, request, OleDsNgRestClient.Format.JSON);

        EHoldings eHoldings = null;
        try {
            JSONObject responseObject = new JSONObject(response);
            String content = getStringValueFromJsonObject(responseObject, OleNGConstants.CONTENT);
            eHoldings = (EHoldings) new EHoldings().deserialize(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(null == eHoldings) {
            eHoldings = new EHoldings();
        }
        OleHoldings oleHoldings = eHoldings.getContentObject();
        oleHoldings.setDonorInfo(new ArrayList<DonorInfo>());
        eHoldings.setContentObject(oleHoldings);
        eHoldings.serializeContent();
        return eHoldings;

    }

    private List<String> getListOfValue(JSONObject itemMappings, String type) {
        JSONArray jsonArrayFromJsonObject = getJSONArrayFromJsonObject(itemMappings, type);
        return getListFromJSONArray(jsonArrayFromJsonObject.toString());
    }



    public BatchProcessProfile fetchBatchProcessProfile(String profileName) {
        BatchProcessProfile batchProcessProfile = null;

        Map parameterMap = new HashMap();
        parameterMap.put("batchProcessProfileName",profileName);
        parameterMap.put("batchProcessType","Bib Import");
        List<BatchProcessProfile> matching = (List<BatchProcessProfile>) KRADServiceLocator.getBusinessObjectService().findMatching(BatchProcessProfile.class, parameterMap);
        if(CollectionUtils.isNotEmpty(matching)){
            try {
                batchProcessProfile = matching.get(0);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                batchProcessProfile = objectMapper.readValue(IOUtils.toString(batchProcessProfile.getContent()), BatchProcessProfile.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return batchProcessProfile;
    }

    public List<String> getListFromJSONArray(String jsonArrayString){
        List ops = new ArrayList();
        try {
            ops = new ObjectMapper().readValue(jsonArrayString, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ops;

    }

    public JSONArray getJSONArrayFromJsonObject(JSONObject jsonObject, String key) {
        JSONArray returnValue = null;
        try {
            if(jsonObject.has(key)){
                returnValue = jsonObject.getJSONArray(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public DataCarrierService getDataCarrierService() {
        if(dataCarrierService == null){
            dataCarrierService = SpringContext.getBean(DataCarrierService.class);
        }
        return dataCarrierService;
    }

    public CallNumberTypeRecord fetchCallNumberTypeRecordByName(String callNumberTypeName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", callNumberTypeName);
        List<CallNumberTypeRecord> matching = (List<CallNumberTypeRecord>) getBusinessObjectService().findMatching(CallNumberTypeRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    public ItemTypeRecord fetchItemTypeByName(String itemTypeName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", itemTypeName);
        List<ItemTypeRecord> matching = (List<ItemTypeRecord>) getBusinessObjectService().findMatching(ItemTypeRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    public ItemStatusRecord fetchItemStatusByName(String itemStatusTypeName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", itemStatusTypeName);
        List<ItemStatusRecord> matching = (List<ItemStatusRecord>) getBusinessObjectService().findMatching(ItemStatusRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }
}
