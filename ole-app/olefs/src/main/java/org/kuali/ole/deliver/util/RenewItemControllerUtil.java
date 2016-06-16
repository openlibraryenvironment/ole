package org.kuali.ole.deliver.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.bo.OLERenewItem;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by sheiksalahudeenm on 8/13/15.
 */
public class RenewItemControllerUtil extends CircUtilController{

    private static final Logger LOG = Logger.getLogger(RenewItemControllerUtil.class);

    public Integer incrementRenewalCount(OleLoanDocument oleLoanDocument) {
        Integer numRenewals = 0;
        String numberOfRenewalsPreviously = oleLoanDocument.getNumberOfRenewals();
        if (StringUtils.isNotBlank(numberOfRenewalsPreviously)) {
            numRenewals = new Integer(numberOfRenewalsPreviously) + 1;
        }
        return numRenewals;
    }

    public Map getUpdateParameters(OleLoanDocument currentLoanDocument) {
        HashMap parameterValues = new HashMap();
        parameterValues.put("itemUUID", currentLoanDocument.getItemUuid());
        parameterValues.put("loanDueDate", currentLoanDocument.getLoanDueDate());
        parameterValues.put("numRenewals", currentLoanDocument.getNumberOfRenewals());
        if (StringUtils.isNotBlank(currentLoanDocument.getItemStatus()) && currentLoanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST)) {
            parameterValues.put("itemStatus", OLEConstants.ITEM_STATUS_CHECKEDOUT);
        }
        return parameterValues;
    }

    public OLERenewItem getRenewItemForSuccessLoanDocument(OleLoanDocument oleLoanDocument) {
        OLERenewItem oleRenewItem = new OLERenewItem();
        String itemBarcode = oleLoanDocument.getItemId();
        oleRenewItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RENEW_SUCCESS) + " - Item Barcode(" + itemBarcode + ")");
        oleRenewItem.setSuccess(true);
        oleRenewItem.setItemBarcode(itemBarcode);
        oleRenewItem.setPastDueDate(oleLoanDocument.getPastDueDate().toString());
        oleRenewItem.setNewDueDate(oleLoanDocument.getLoanDueDate().toString());
        oleRenewItem.setRenewalCount(oleLoanDocument.getNumberOfRenewals());
        oleRenewItem.setPatronBarcode(oleLoanDocument.getOlePatron().getBarcode());
        oleRenewItem.setTitleIdentifier(oleLoanDocument.getTitle());
        oleRenewItem.setItemProperties(oleLoanDocument.getAuthor() != null ? "Author=" + oleLoanDocument.getAuthor() : null);
        oleRenewItem.setMediaType(oleLoanDocument.getItemType());
        return oleRenewItem;
    }
    public OLERenewItem getRenewItemForFailureLoanDocument(OleLoanDocument oleLoanDocument) {
        OLERenewItem oleRenewItem = new OLERenewItem();
        String itemBarcode = oleLoanDocument.getItemId();
        oleRenewItem.setMessage(oleLoanDocument.getErrorMessage());
        oleRenewItem.setItemBarcode(itemBarcode);
        Date pastDueDate = oleLoanDocument.getPastDueDate();
        oleRenewItem.setPastDueDate(pastDueDate != null ? pastDueDate.toString() : null);
        Timestamp loanDueDate = oleLoanDocument.getLoanDueDate();
        oleRenewItem.setNewDueDate(loanDueDate != null ? loanDueDate.toString() : null);
        oleRenewItem.setRenewalCount(oleLoanDocument.getNumberOfRenewals());
        oleRenewItem.setPatronBarcode(oleLoanDocument.getOlePatron() != null ? oleLoanDocument.getOlePatron().getBarcode() : null);
        oleRenewItem.setTitleIdentifier(oleLoanDocument.getTitle());
        oleRenewItem.setItemProperties(oleLoanDocument.getAuthor() != null ? "Author=" + oleLoanDocument.getAuthor() : null);
        oleRenewItem.setMediaType(oleLoanDocument.getItemType());
        return oleRenewItem;
    }

    public int getMaximumNumberOfThreadForRenewService() {
        String maxNumberOfThreadFromParameter = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.MAX_NO_OF_THREAD_FOR_RENEW_SERVICE);
        int maxNumberOfThread = 10;
        if(StringUtils.isNotBlank(maxNumberOfThreadFromParameter)){
            try{
                int maxNumberOfThreadFromParameterInterger = Integer.parseInt(maxNumberOfThreadFromParameter);
                if(maxNumberOfThreadFromParameterInterger > 0){
                    maxNumberOfThread = maxNumberOfThreadFromParameterInterger;
                }else{
                    LOG.info("Invalid max number of thread for renew service from system parameter. So taking the default max number of thread : " + maxNumberOfThread);
                }
            }catch(Exception exception){
                LOG.info("Invalid max number of thread for renew service from system parameter. So taking the default max number of thread : " + maxNumberOfThread);
            }
        }else{
            LOG.info("Invalid max number of thread for renew service from system parameter. So taking the default max number of thread : " + maxNumberOfThread);
        }
        return maxNumberOfThread;
    }

}
