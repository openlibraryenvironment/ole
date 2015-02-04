package org.kuali.ole.select.lookup;


import org.hsqldb.lib.StringUtil;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OleLookupableImpl;
import org.kuali.ole.select.businessobject.OLEPurchaseOrderItemSearch;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.rice.kns.lookup.AbstractLookupableHelperServiceImpl;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.*;

public class OLEPurchaseOrderItemLookupableHelperServiceImpl extends OleLookupableImpl {

    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        Map<String, String> criteria = new HashMap<String, String>();
        List<OLEPurchaseOrderItemSearch> searchResults = new ArrayList<OLEPurchaseOrderItemSearch>();
        List<OlePurchaseOrderItem> purchaseOrderItems = new ArrayList<OlePurchaseOrderItem>();
        if(searchCriteria.get("vendorDetailAssignedIdentifier")!=null && !searchCriteria.get("vendorDetailAssignedIdentifier").isEmpty()){
            criteria.put("purchaseOrder.vendorDetailAssignedIdentifier",searchCriteria.get("vendorDetailAssignedIdentifier"));
        }
        if(searchCriteria.get("vendorHeaderGeneratedIdentifier")!=null && !searchCriteria.get("vendorHeaderGeneratedIdentifier").isEmpty()){
            criteria.put("purchaseOrder.vendorHeaderGeneratedIdentifier",searchCriteria.get("vendorHeaderGeneratedIdentifier"));
        }
        if (StringUtil.isEmpty(searchCriteria.get("purapDocumentIdentifier"))) {
            purchaseOrderItems = (List<OlePurchaseOrderItem>)getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, criteria);
        }
        else if(isNumber(searchCriteria.get("purapDocumentIdentifier"))){
            if(searchCriteria.get("purapDocumentIdentifier")!=null && !searchCriteria.get("purapDocumentIdentifier").isEmpty()){
                criteria.put("purchaseOrder.purapDocumentIdentifier",searchCriteria.get("purapDocumentIdentifier"));
            }
            purchaseOrderItems = (List<OlePurchaseOrderItem>)getBusinessObjectService().findMatching(OlePurchaseOrderItem.class,criteria);
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