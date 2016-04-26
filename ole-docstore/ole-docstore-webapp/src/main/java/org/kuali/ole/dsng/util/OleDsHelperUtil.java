package org.kuali.ole.dsng.util;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.response.BibFailureResponse;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.dsng.indexer.BibIndexer;
import org.kuali.ole.dsng.indexer.HoldingIndexer;
import org.kuali.ole.dsng.indexer.ItemIndexer;
import org.kuali.ole.utility.JSONHelperUtil;
import org.kuali.ole.utility.MarcRecordUtil;

import java.util.List;

/**
 * Created by SheikS on 11/30/2015.
 */
public class OleDsHelperUtil extends BusinessObjectServiceHelperUtil implements DocstoreConstants {

    private ObjectMapper objectMapper;

    private BibIndexer bibIndexer;

    private HoldingIndexer holdingIndexer;

    private ItemIndexer itemIndexer;

    private MarcRecordUtil marcRecordUtil;

    private JSONHelperUtil jsonHelperUtil;


    private SolrRequestReponseHandler solrRequestReponseHandler;

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if(null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    public void setSolrRequestReponseHandler(SolrRequestReponseHandler solrRequestReponseHandler) {
        this.solrRequestReponseHandler = solrRequestReponseHandler;
    }

    public String getStringValueFromJsonObject(JSONObject jsonObject, String key) {
        return getJsonHelperUtil().getStringValueFromJsonObject(jsonObject,key);
    }

    public boolean getBooleanValueFromJsonObject(JSONObject jsonObject, String key) {
        return getJsonHelperUtil().getBooleanValueFromJsonObject(jsonObject,key);
    }

    public JSONArray getJSONArrayeFromJsonObject(JSONObject jsonObject, String key) {
        return getJsonHelperUtil().getJSONArrayeFromJsonObject(jsonObject,key);
    }

    public JSONObject getJSONObjectFromJSONObject(JSONObject jsonObject, String key) {
        return getJsonHelperUtil().getJSONObjectFromJSONObject(jsonObject,key);
    }

    public JSONObject getJSONObjectFromJsonArray(JSONArray jsonArray, int index) {
        return getJsonHelperUtil().getJSONObjectFromJsonArray(jsonArray,index);
    }

    public List<String> getListFromJSONArray(String operation){
        return getJsonHelperUtil().getListFromJSONArray(operation);

    }

    public void addFailureReportToExchange(JSONObject requestJsonObject, Exchange exchange, String type, Exception exception,
                                           Integer size) {
        String recordIndex = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.RECORD_INDEX);
        Integer index = null;
        if(null != recordIndex) {
            index = Integer.valueOf(recordIndex);
        }

        BibFailureResponse failureResponse = (BibFailureResponse) exchange.get(OleNGConstants.FAILURE_RESPONSE);
        failureResponse.setIndex(index);
        String message = exception.toString();
        failureResponse.setFailureMessage(message);

        String detailedMessage = getDetailedMessage(exception);
        failureResponse.setDetailedMessage(detailedMessage);

        int count = (null == size ? 0 : size);
        if(type.equalsIgnoreCase(OleNGConstants.NO_OF_FAILURE_HOLDINGS)){
            failureResponse.setNoOfFailureHoldings(failureResponse.getNoOfFailureHoldings() + count);
        } else if(type.equalsIgnoreCase(OleNGConstants.NO_OF_FAILURE_EHOLDINGS)){
            failureResponse.setNoOfFailureEHoldings(failureResponse.getNoOfFailureEHoldings() + count);
        } else if(type.equalsIgnoreCase(OleNGConstants.NO_OF_FAILURE_ITEM)){
            failureResponse.setNoOfFailureItems(failureResponse.getNoOfFailureItems() + count);
        }
    }

    public String getDetailedMessage(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        StringBuilder detailedMessage = new StringBuilder();
        String className = null;
        String methodName = null;
        int lineNumber = 0;
        if (null != stackTrace && stackTrace.length >= 2) {
            for(int index = 0; index < 2; index++) {
                StackTraceElement stackTraceElement = stackTrace[index];
                String fullClassName = stackTraceElement.getClassName();
                className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                methodName = stackTraceElement.getMethodName();
                lineNumber = stackTraceElement.getLineNumber();
                if(StringUtils.isNotBlank(detailedMessage.toString())){
                    detailedMessage.append("\n");
                }
                detailedMessage.append(className + "." + methodName + "():line#" + lineNumber);
            }
        }
        return detailedMessage.toString();
    }

    public ObjectMapper getObjectMapper() {
        if(null == objectMapper) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public HoldingIndexer getHoldingIndexer() {
        if(null == holdingIndexer) {
            holdingIndexer = new HoldingIndexer();
        }
        return holdingIndexer;
    }

    public void setHoldingIndexer(HoldingIndexer holdingIndexer) {
        this.holdingIndexer = holdingIndexer;
    }

    public BibIndexer getBibIndexer() {
        if(null == bibIndexer) {
            bibIndexer = new BibIndexer();

        }
        return bibIndexer;
    }

    public void setBibIndexer(BibIndexer bibIndexer) {
        this.bibIndexer = bibIndexer;
    }

    public ItemIndexer getItemIndexer() {
        if(null == itemIndexer) {
            itemIndexer = new ItemIndexer();
        }
        return itemIndexer;
    }

    public void setItemIndexer(ItemIndexer itemIndexer) {
        this.itemIndexer = itemIndexer;
    }



    public MarcRecordUtil getMarcRecordUtil() {
        if(null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }

    public void setMarcRecordUtil(MarcRecordUtil marcRecordUtil) {
        this.marcRecordUtil = marcRecordUtil;
    }

    public JSONHelperUtil getJsonHelperUtil() {
        if(null == jsonHelperUtil) {
            jsonHelperUtil = new JSONHelperUtil();
        }
        return jsonHelperUtil;
    }

    public void setJsonHelperUtil(JSONHelperUtil jsonHelperUtil) {
        this.jsonHelperUtil = jsonHelperUtil;
    }
}
