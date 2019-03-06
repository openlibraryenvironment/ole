package org.kuali.ole.ncip.service.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.handler.RenewItemResponseHandler;
import org.kuali.asr.handler.ResponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleRenewalHistory;
import org.kuali.ole.deliver.util.*;
import org.kuali.ole.docstore.common.document.DocstoreDocument;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.bo.OLERenewItem;
import org.kuali.ole.bo.OLERenewItemList;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by pvsubrah on 6/23/15.
 */
public abstract class RenewItemsService {

    private static final Logger LOG = Logger.getLogger(RenewItemsService.class);

    private OlePatronRecordUtil olePatronRecordUtil;
    private BulkItemUpdateUtil bulkItemUpdateUtil;
    private RenewItemControllerUtil renewItemControllerUtil;

    private ResponseHandler responseHandler;
    private BusinessObjectService businessObjectService;
    private String patronBarcode;
    private String requestFormatType;
    protected String responseFormatType;
    protected String response;
    protected List<String> itemBarcodes;

    public String renewItems(Map renewParameters) {
        patronBarcode = (String) renewParameters.get("patronBarcode");
        itemBarcodes = (List<String>) renewParameters.get("itemBarcodes");
        requestFormatType = (String) renewParameters.get("requestFormatType");
        setResponseFormatType(renewParameters);

        DroolsResponse finalDroolResponse;
        OleStopWatch oleStopWatchForWholeProcess = new OleStopWatch();
        oleStopWatchForWholeProcess.start();
        OleStopWatch oleStopWatch = new OleStopWatch();

        OLERenewItemList oleRenewItemList = new OLERenewItemList();
        List<OLERenewItem> oleRenewItems = new ArrayList<>();
        OlePatronDocument olePatronDocument = lookupPatron(patronBarcode);
        if (null != olePatronDocument) {
            DroolsResponse droolsResponse = validatePatron(olePatronDocument);
            if (isPatronValid(droolsResponse)) {
                oleStopWatch.start();
                finalDroolResponse = processThreadsForLoanDocuments(olePatronDocument);
                oleStopWatch.end();
                System.out.println("Time taken for processing all thread : " + oleStopWatch.getTotalTime() + " ms");
            } else {
                oleRenewItems.add(generateErrorMessageObject(droolsResponse.retrieveErrorMessage()));
            oleRenewItemList.setRenewItemList(oleRenewItems);
            return prepareResponse(oleRenewItemList);
        }
        } else {
            oleRenewItems.add(generateErrorMessageObject(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO)));
            oleRenewItemList.setRenewItemList(oleRenewItems);
            return prepareResponse(oleRenewItemList);
        }

        List<Item> itemList = new ArrayList<>();
        Map<String, Object> finalDroolResponseContext = finalDroolResponse.getDroolsExchange().getContext();
        for (Iterator<String> iterator = finalDroolResponseContext.keySet().iterator(); iterator.hasNext(); ) {
            String itemUuid = iterator.next();
            DroolsResponse droolsResponse = (DroolsResponse) finalDroolResponseContext.get(itemUuid);
            if(StringUtils.isNotBlank(droolsResponse.getSucessMessage())){
                OleLoanDocument currentLoanDocument = (OleLoanDocument) droolsResponse.getDroolsExchange().getContext().get(itemUuid);
                Item itemForUpdate = getRenewItemControllerUtil().getItemForUpdate(getRenewItemControllerUtil().getUpdateParameters(currentLoanDocument));
                if (null != itemForUpdate) {
                    itemList.add(itemForUpdate);
                }
            }
        }
        oleStopWatch.start();
        processBulkUpdate(itemList, finalDroolResponse);
        oleStopWatch.end();
        System.out.println("Time taken for bulk update (Doctore and Db) : " + oleStopWatch.getTotalTime() + " ms");
        List<OLERenewItem> oleRenewItemsForSuccessfulRenew = prepareRenewItemsForRenewedItems(finalDroolResponse);
        oleRenewItemList.setRenewItemList(oleRenewItemsForSuccessfulRenew);
        oleRenewItemList.getRenewItemList().addAll(oleRenewItems);
        oleStopWatchForWholeProcess.end();
        System.out.println("Time taken to complete the renew process  is : " + oleStopWatchForWholeProcess.getTotalTime() + "ms for " + itemBarcodes.size() + " items");
        return prepareResponse(oleRenewItemList);

    }

    private void setResponseFormatType(Map checkoutParameters) {
        responseFormatType = (String) checkoutParameters.get("responseFormatType");
        if (responseFormatType == null) {
            responseFormatType = "xml";
        }
        responseFormatType = responseFormatType.toUpperCase();
    }


    private DroolsResponse processThreadsForLoanDocuments(OlePatronDocument olePatronDocument) {
        DroolsResponse finalDroolResponse = new DroolsResponse();
        ExecutorService renewItemsExecutorService;
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("renew-item-thread-%d").build();
        int numberOfThreadForNew = getRenewItemControllerUtil().getMaximumNumberOfThreadForRenewService();
        renewItemsExecutorService = Executors.newFixedThreadPool(numberOfThreadForNew, threadFactory);
        List<Future> futures = new ArrayList<>();

        for (Iterator<String> iterator = itemBarcodes.iterator(); iterator.hasNext(); ) {
            String itemBarcode = iterator.next();
            Future future = renewItemsExecutorService.submit(new RenewItemExecutor(olePatronDocument, itemBarcode));
            futures.add(future);
        }
        for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
            Future future = iterator.next();
            try {
                DroolsResponse response = (DroolsResponse) future.get();
                Map<String, Object> keyMap = response.getDroolsExchange().getContext();
                if (keyMap.size() > 0) {
                    String itemUuid = keyMap.keySet().iterator().next();
                    finalDroolResponse.getDroolsExchange().addToContext(itemUuid, response);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        renewItemsExecutorService.shutdown();

        return finalDroolResponse;
    }

    private boolean isPatronValid(DroolsResponse droolsResponse) {
        return null != droolsResponse && StringUtils.isEmpty(droolsResponse.retrieveErrorMessage());
    }

    public abstract DroolsResponse validatePatron(OlePatronDocument olePatronDocument);

    private List<OLERenewItem> prepareRenewItemsForRenewedItems(DroolsResponse finalDroolResponse) {
        List<OLERenewItem> oleRenewItems = new ArrayList<>();
        Map<String, Object> context = finalDroolResponse.getDroolsExchange().getContext();
        if (context.size() > 0) {
            for (Iterator<String> iterator = context.keySet().iterator(); iterator.hasNext(); ) {
                String itemUuid = iterator.next();
                DroolsResponse droolsResponse = (org.kuali.ole.deliver.util.DroolsResponse) context.get(itemUuid);
                OLERenewItem oleRenewItem = null;
                if(StringUtils.isNotBlank(droolsResponse.getSucessMessage())){
                    oleRenewItem = getRenewItemControllerUtil().getRenewItemForSuccessLoanDocument((OleLoanDocument) droolsResponse.getDroolsExchange().getContext().get(itemUuid));
                }else{
                    Object individualObject = droolsResponse.getDroolsExchange().getContext().get(itemUuid);
                    oleRenewItem = getRenewItemControllerUtil().getRenewItemForFailureLoanDocument((OleLoanDocument) individualObject);
                }
                oleRenewItems.add(oleRenewItem);
            }
        }
        return oleRenewItems;
    }


    public void processBulkUpdate(List<Item> itemList, DroolsResponse finalDroolResponse) {
        if (CollectionUtils.isNotEmpty(itemList)) {
            Map<String, Map> docstoreResultMap = bulkUpdateForRenewItems(itemList);
            List<OleLoanDocument> loanDocumentsToBeUpdatedInDb = new ArrayList<>();
            List<OleRenewalHistory> oleRenewalHistoryList = new ArrayList<>();

            for (Iterator<String> iterator = docstoreResultMap.keySet().iterator(); iterator.hasNext(); ) {
                String itemUuid = iterator.next();
                Map result = docstoreResultMap.get(itemUuid);
                DocstoreDocument.ResultType resultType = (DocstoreDocument.ResultType) result.get("result");
                if (resultType.equals(DocstoreDocument.ResultType.SUCCESS)) {
                    DroolsResponse individualDroolResponse = (DroolsResponse) finalDroolResponse.getDroolsExchange().getContext().get(itemUuid);
                    OleLoanDocument oleLoanDocument = (OleLoanDocument) individualDroolResponse.getDroolsExchange().getContext().get(itemUuid);
                    oleRenewalHistoryList.add(updateRenewalHistoryRecord(oleLoanDocument));
                    loanDocumentsToBeUpdatedInDb.add(oleLoanDocument);
                } else {
                    DroolsResponse droolsResponse = (DroolsResponse) finalDroolResponse.getDroolsExchange().getContext().get(itemUuid);
                    droolsResponse.addErrorMessage("Solr update for the item during renew was not successful.");
                    LOG.error("Solr update for during renew was not successful for item : " + result.get("barcode"));
                }
            }

            try {
                OleStopWatch oleStopWatch = new OleStopWatch();
                oleStopWatch.start();
                getBusinessObjectService().save(loanDocumentsToBeUpdatedInDb);
                getBusinessObjectService().save(oleRenewalHistoryList);
                oleStopWatch.end();
                System.out.println("Time taken for bulk update to DB : " + oleStopWatch.getTotalTime() + " ms");
            } catch (Exception e) {
                //TODO: rollback in SOLR.
            }
        }
    }

    private OleRenewalHistory updateRenewalHistoryRecord(OleLoanDocument oleLoanDocument) {
        OleRenewalHistory oleRenewalHistory = new OleRenewalHistory();
        oleRenewalHistory.setItemBarcode(oleLoanDocument.getItemId());
        oleRenewalHistory.setItemId(oleLoanDocument.getItemUuid());
        oleRenewalHistory.setLoanId(oleLoanDocument.getLoanId());
        oleRenewalHistory.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
        oleRenewalHistory.setPatronBarcode(null != oleLoanDocument.getOlePatron() ? oleLoanDocument.getOlePatron().getBarcode() : oleLoanDocument.getPatronBarcode());
        oleRenewalHistory.setRenewalDueDate(oleLoanDocument.getLoanDueDate());
        oleRenewalHistory.setRenewedDate(new Timestamp(System.currentTimeMillis()));
        return oleRenewalHistory;
    }

    public Map<String, Map> bulkUpdateForRenewItems(List<Item> itemList) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        Map<String, Map> docstoreResultMap = getBulkItemUpdateUtil().updateItemsToDocstore(itemList);
        oleStopWatch.end();
        System.out.println("Time taken to batch update item loan due dates in solr : " + oleStopWatch.getTotalTime() + " ms");
        return docstoreResultMap;
    }


    private OlePatronDocument lookupPatron(String patronBarcode) {
        Map<String, String> patronMap = new HashMap<>();
        patronMap.put(OLEConstants.BARCODE, patronBarcode);
        List<OlePatronDocument> patronDocuments = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
        return CollectionUtils.isNotEmpty(patronDocuments) ? patronDocuments.get(0) : null;
    }


    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = (BusinessObjectService) SpringContext.getService("oleBusinessObjectService");
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


    private OlePatronRecordUtil getOlePatronRecordUtil() {
        if (null == olePatronRecordUtil) {
            olePatronRecordUtil = new OlePatronRecordUtil();
        }
        return olePatronRecordUtil;
    }

    public void setOlePatronRecordUtil(OlePatronRecordUtil olePatronRecordUtil) {
        this.olePatronRecordUtil = olePatronRecordUtil;
    }

    public OLERenewItem generateErrorMessageObject(String message) {
        OLERenewItem oleRenewItem = new OLERenewItem();
        oleRenewItem.setMessage(message);
        return oleRenewItem;
    }

    protected ResponseHandler getResponseHandler() {
        if (null == responseHandler) {
            responseHandler = new RenewItemResponseHandler();
        }
        return responseHandler;
    }

    public BulkItemUpdateUtil getBulkItemUpdateUtil() {
        if (null == bulkItemUpdateUtil) {
            bulkItemUpdateUtil = new BulkItemUpdateUtil();
        }
        return bulkItemUpdateUtil;
    }

    public void setBulkItemUpdateUtil(BulkItemUpdateUtil bulkItemUpdateUtil) {
        this.bulkItemUpdateUtil = bulkItemUpdateUtil;
    }

    public RenewItemControllerUtil getRenewItemControllerUtil() {
        if (null == renewItemControllerUtil) {
            renewItemControllerUtil = new RenewItemControllerUtil();
        }
        return renewItemControllerUtil;
    }

    public void setRenewItemControllerUtil(RenewItemControllerUtil renewItemControllerUtil) {
        this.renewItemControllerUtil = renewItemControllerUtil;
    }

    public abstract String prepareResponse(OLERenewItemList oleRenewItemList);

}
