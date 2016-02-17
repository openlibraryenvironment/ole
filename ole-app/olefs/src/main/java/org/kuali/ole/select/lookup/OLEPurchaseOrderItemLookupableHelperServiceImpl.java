package org.kuali.ole.select.lookup;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OleLookupableImpl;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.businessobject.OLEPurchaseOrderItemSearch;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OLEPurchaseOrderItemLookupableHelperServiceImpl extends OleLookupableImpl {

    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        Map<String, String> criteria = new HashMap<String, String>();
        List<OLEPurchaseOrderItemSearch> searchResults = new ArrayList<OLEPurchaseOrderItemSearch>();
        List<OlePurchaseOrderItem> purchaseOrderItems = new ArrayList<OlePurchaseOrderItem>();

        if (StringUtils.isNotBlank(searchCriteria.get("vendorDetailAssignedIdentifier"))) {
            criteria.put("vendorDetailAssignedIdentifier", searchCriteria.get("vendorDetailAssignedIdentifier"));
        }
        if (StringUtils.isNotBlank(searchCriteria.get("vendorHeaderGeneratedIdentifier"))) {
            criteria.put("vendorHeaderGeneratedIdentifier", searchCriteria.get("vendorHeaderGeneratedIdentifier"));
        }
        if (StringUtils.isNotBlank(searchCriteria.get("purapDocumentIdentifier")) && isNumber(searchCriteria.get("purapDocumentIdentifier"))) {
            criteria.put("purapDocumentIdentifier", searchCriteria.get("purapDocumentIdentifier"));
        }

        List<OlePurchaseOrderDocument> purchaseOrderDocuments = (List<OlePurchaseOrderDocument>) getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, criteria);
        if (CollectionUtils.isNotEmpty(purchaseOrderDocuments)) {
            for (OlePurchaseOrderDocument olePurchaseOrderDocument : purchaseOrderDocuments) {
                if (olePurchaseOrderDocument.getApplicationDocumentStatus().equals(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN)) {
                    List<OlePurchaseOrderItem> olePurchaseOrderItems = olePurchaseOrderDocument.getItemsActiveOnly();
                    purchaseOrderItems.addAll(olePurchaseOrderItems);
                }
            }
        }

        OLEPurchaseOrderItemSearch poItemSearch;
        for (OlePurchaseOrderItem poItem : purchaseOrderItems) {
            if (poItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                if(poItem.getItemTitleId()!=null){
                    poItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(poItem.getItemTitleId()));
                }
                poItemSearch = new OLEPurchaseOrderItemSearch();
                poItemSearch.setPurchaseOrderItemID(poItem.getItemIdentifier());
                poItemSearch.setPurapDocumentIdentifier(poItem.getPurapDocument() != null &&
                        poItem.getPurapDocument().getPurapDocumentIdentifier() != null ? poItem.getPurapDocument().getPurapDocumentIdentifier().toString() : "");
                poItemSearch.setTempItemIdentifier(poItem.getItemIdentifier());
                poItemSearch.setItemDescription(poItem.getItemDescription());
                poItemSearch.setLocalId(poItem.getItemTitleId());
                poItemSearch.setAccountsPayableLinkIdentifier(poItem.getPurapDocument() != null &&
                        poItem.getPurapDocument().getAccountsPayablePurchasingDocumentLinkIdentifier() != null ?
                        poItem.getPurapDocument().getAccountsPayablePurchasingDocumentLinkIdentifier() : Integer.valueOf("0"));
                poItemSearch.setPoItemPrice(poItem.getExtendedPrice() != null ? poItem.getExtendedPrice().toString() : "0.00");
                poItemSearch.setItemLineNumber(poItem.getItemLineNumber());
                poItemSearch.setDocFormat(poItem.getDocFormat());
                searchResults.add(poItemSearch);
            }
        }
        return searchResults;
    }
    public static boolean isNumber(String actionInterval) {
        String actStr = actionInterval;
        for (int i = 0; i < actStr.length(); i++) {
            if (!Character.isDigit(actStr.charAt(i)))
                return false;
        }
        return true;
    }

    @Override
    public Map<String, String> performClear(LookupForm form, Map<String, String> searchCriteria) {
        super.performClear(form,searchCriteria);
        // Map<String, InputField> criteriaFieldMap = new HashMap<String, InputField>();
        Map<String, String> clearedSearchCriteria = new HashMap<String, String>();
        for (Map.Entry<String, String> searchKeyValue : searchCriteria.entrySet()) {
            String searchPropertyName = searchKeyValue.getKey();
            if(searchPropertyName.equals("vendorDetailAssignedIdentifier") ||
                    searchPropertyName.equals("vendorHeaderGeneratedIdentifier") ||
                searchPropertyName.equals("vendorName")) {
                clearedSearchCriteria.put(searchPropertyName,searchKeyValue.getValue());
            }
        }
        return clearedSearchCriteria;
    }
}