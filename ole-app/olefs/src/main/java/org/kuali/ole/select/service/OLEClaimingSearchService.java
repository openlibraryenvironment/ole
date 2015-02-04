package org.kuali.ole.select.service;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEPOClaimHistory;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.form.OLEClaimingSearchForm;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 4/7/14
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEClaimingSearchService {
    private BusinessObjectService businessObjectService;
    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void getClaimResponse(OLEClaimingSearchForm oleClaimingSearchForm){
        if (!GlobalVariables.getMessageMap().hasMessages()) {
            oleClaimingSearchForm.setSuccessMsg("");
            oleClaimingSearchForm.setClaimErrorMessage("");
            String title = oleClaimingSearchForm.getTitle() != null ? oleClaimingSearchForm.getTitle() : "";
            if (title.contains("*")) {
                title = title.replace("*", "").trim();
            }
            List<OLEPOClaimHistory> claimHistoryList = (List<OLEPOClaimHistory>) KRADServiceLocator.getBusinessObjectService().findAll(OLEPOClaimHistory.class);
            List<OLEPOClaimHistory> claimHistoryFilterList = new ArrayList<>();
            for (OLEPOClaimHistory claimHistory : claimHistoryList) {
                OlePurchaseOrderItem olePurchaseOrderItem = claimHistory.getOlePurchaseOrderItem();
                OlePurchaseOrderDocument olePurchaseOrderDocument = olePurchaseOrderItem.getPurapDocument();
                claimHistory.setVendorName(olePurchaseOrderDocument.getVendorName());
                claimHistory.setTitle(olePurchaseOrderItem.getItemDescription());
                if (((oleClaimingSearchForm.getVendorName() != null && !oleClaimingSearchForm.getVendorName().trim().isEmpty() && claimHistory.getVendorName().contains(oleClaimingSearchForm.getVendorName()))
                        && (title == null || title.trim().isEmpty())
                        && (oleClaimingSearchForm.getClaimDate() == null))  // VendorName Check
                        || ((oleClaimingSearchForm.getVendorName() == null || oleClaimingSearchForm.getVendorName().trim().isEmpty())
                        && (title != null && !title.trim().isEmpty() && claimHistory.getTitle().contains(title))
                        && (oleClaimingSearchForm.getClaimDate() == null))  // Title Check
                        || ((oleClaimingSearchForm.getVendorName() == null || oleClaimingSearchForm.getVendorName().trim().isEmpty())
                        && (title == null || title.trim().isEmpty())
                        && (oleClaimingSearchForm.getClaimDate() != null && oleClaimingSearchForm.getClaimDate().equals(claimHistory.getClaimDate()))) // ClaimDate Check
                        || ((oleClaimingSearchForm.getVendorName() != null && !oleClaimingSearchForm.getVendorName().trim().isEmpty() && claimHistory.getVendorName().contains(oleClaimingSearchForm.getVendorName()))
                        && (title != null && !title.trim().isEmpty() && claimHistory.getTitle().contains(title))
                        && (oleClaimingSearchForm.getClaimDate() == null)) // VendorName and Title Check
                        || ((oleClaimingSearchForm.getVendorName() != null && !oleClaimingSearchForm.getVendorName().trim().isEmpty() && claimHistory.getVendorName().contains(oleClaimingSearchForm.getVendorName()))
                        && (title == null || title.trim().isEmpty())
                        && (oleClaimingSearchForm.getClaimDate() != null && oleClaimingSearchForm.getClaimDate().equals(claimHistory.getClaimDate()))) // VendorName and ClaimDate Check
                        || ((oleClaimingSearchForm.getVendorName() == null || oleClaimingSearchForm.getVendorName().trim().isEmpty())
                        && (title != null && !title.trim().isEmpty() && claimHistory.getTitle().contains(title))
                        && (oleClaimingSearchForm.getClaimDate() != null && oleClaimingSearchForm.getClaimDate().equals(claimHistory.getClaimDate())))  // Title and ClaimDate Check
                        || ((oleClaimingSearchForm.getVendorName() != null && !oleClaimingSearchForm.getVendorName().trim().isEmpty() && claimHistory.getVendorName().contains(oleClaimingSearchForm.getVendorName()))
                        && (title != null && !title.trim().isEmpty() && claimHistory.getTitle().contains(title))
                        && (oleClaimingSearchForm.getClaimDate() != null && oleClaimingSearchForm.getClaimDate().equals(claimHistory.getClaimDate()))) // VendorName and Title and ClaimDate Check
                        ) {
                    claimHistoryFilterList.add(claimHistory);
                }
            }
            if (((oleClaimingSearchForm.getVendorName() == null || oleClaimingSearchForm.getVendorName().trim().isEmpty())
                    && (title == null || title.trim().isEmpty())
                    && (oleClaimingSearchForm.getClaimDate() == null))) {
                oleClaimingSearchForm.setOleClaimingSearchRecordList(claimHistoryList);
            } else {
                oleClaimingSearchForm.setOleClaimingSearchRecordList(claimHistoryFilterList);
            }
            if (oleClaimingSearchForm.getOleClaimingSearchRecordList().size() == 0) {
                oleClaimingSearchForm.setClaimErrorMessage(OLEConstants.CLAIM_ERROR_MESSAGE);
                oleClaimingSearchForm.setOleClaimingSearchRecordList(null);
            }
        } else {
            oleClaimingSearchForm.setOleClaimingSearchRecordList(null);
        }

    }

    public void updateClaimNote(OLEClaimingSearchForm oleClaimingSearchForm ){
        if (oleClaimingSearchForm.getOleClaimingSearchRecordList() != null && oleClaimingSearchForm.getOleClaimingSearchRecordList().size() > 0) {
            KRADServiceLocator.getBusinessObjectService().save(oleClaimingSearchForm.getOleClaimingSearchRecordList());
        }
        oleClaimingSearchForm.setSuccessMsg("Updated Claim Response Information successfully");
    }

}
