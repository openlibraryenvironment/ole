package org.kuali.ole.ncip.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.ncip.bo.OLERenewItem;
import org.kuali.ole.ncip.bo.OLERenewItemList;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by pvsubrah on 6/23/15.
 */
public class RenewItemsService {

    private OlePatronRecordUtil olePatronRecordUtil;
    private BusinessObjectService businessObjectService;


    public OLERenewItemList renewItems(String patronBarcode, String operator, List<String> itemBarcodes) {

        OLERenewItemList oleRenewItemList = new OLERenewItemList();
        List<OLERenewItem> oleRenewItems = new ArrayList<>();
        OlePatronDocument olePatronDocument = lookupPatron(patronBarcode);
        ExecutorService renewItemsExecutorService;
        if (null != olePatronDocument) {
            ErrorMessage patronValidationErrors = getOlePatronRecordUtil().fireRules(olePatronDocument, null);

            if(null!=patronValidationErrors && StringUtils.isEmpty(patronValidationErrors.getErrorMessage())){
                renewItemsExecutorService = Executors.newFixedThreadPool(10);
                List<Future> futures = new ArrayList<>();

                for (Iterator<String> iterator = itemBarcodes.iterator(); iterator.hasNext(); ) {
                    String itemBarcode = iterator.next();
                    Future future = renewItemsExecutorService.submit(new RenewItemExecutorNonSIP2(olePatronDocument, operator, itemBarcode));
                    futures.add(future);
                }

                for (Iterator<Future> iterator = futures.iterator(); iterator.hasNext(); ) {
                    Future future = iterator.next();
                    try {
                        Object response = future.get();
                        oleRenewItems.add((OLERenewItem)response);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                oleRenewItems.add(generateErrorMessageObject(patronValidationErrors.getErrorMessage()));
                oleRenewItemList.setRenewItemList(oleRenewItems);
                return oleRenewItemList;
            }
        } else {
            oleRenewItems.add(generateErrorMessageObject(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO)));
            oleRenewItemList.setRenewItemList(oleRenewItems);
            return oleRenewItemList;
        }

       renewItemsExecutorService.shutdown();

        oleRenewItemList.setRenewItemList(oleRenewItems);
        return oleRenewItemList;

    }


    private OlePatronDocument lookupPatron(String patronBarcode) {
        Map<String, String> patronMap = new HashMap<>();
        patronMap.put(OLEConstants.BARCODE, patronBarcode);
        List<OlePatronDocument> patronDocuments = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);

        return CollectionUtils.isNotEmpty(patronDocuments) ? patronDocuments.get(0) : null;

    }


    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
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

    public OLERenewItem generateErrorMessageObject(String message){
        OLERenewItem oleRenewItem = new OLERenewItem();
        oleRenewItem.setMessage(message);
        return oleRenewItem;
    }

}
