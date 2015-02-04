package org.kuali.ole.ingest;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.pojo.edi.*;
import org.kuali.ole.select.bo.OleVendorAccountInfo;
import org.kuali.ole.service.OverlayRetrivalService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.util.*;

/**
 * OleTxRecordBuilder is used to build the transaction information into a pojo which are extracted from the ingested marc and edi file.
 */
public class OleTxRecordBuilder {
    private static OleTxRecordBuilder oleTxRecordBuilder;
    private OverlayRetrivalService overlayRetrivalService;

    /**
     * default constructor of OleTxRecordBuilder.
     */
    private OleTxRecordBuilder() {

    }

    /**
     *  Gets the instance of OleTxRecordBuilder.
     *  If OleTxRecordBuilder is null it returns new instance else it returns existing instance.
     * @return
     */
    public static OleTxRecordBuilder getInstance() {
        if (null == oleTxRecordBuilder) {
            oleTxRecordBuilder = new OleTxRecordBuilder();
        }
        return oleTxRecordBuilder;
    }

    /**
     * This method returns OleTxRecord.
     * This method build the OleTxRecord based on lineItemOrder,list of profileAttribute,ediOrder
     * @param lineItemOrder
     * @param profileAttributeBos
     * @param ediOrder
     * @return  oleTxRecord
     */
    public OleTxRecord build(LineItemOrder lineItemOrder, EDIOrder ediOrder) throws Exception {
        OleTxRecord oleTxRecord = new OleTxRecord();
        oleTxRecord.setListPrice(getListPrice(lineItemOrder));
        oleTxRecord.setQuantity(getQuantity(lineItemOrder));
        oleTxRecord.setVendorItemIdentifier(getVendorItemIdentifier(lineItemOrder));
        oleTxRecord.setVendorNumber(getVendorNumber(ediOrder));
        /*oleTxRecord.setChartCode(getChartCode(profileAttributeBos));
        oleTxRecord.setOrgCode(getAttributeValue(profileAttributeBos, OLEConstants.ORG_CODE));
        oleTxRecord.setReceivingRequired(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.RECV_REQUIRED)));
        oleTxRecord.setContractManager(getAttributeValue(profileAttributeBos, OLEConstants.CONTRACT_MANAGER));*/
        //oleTxRecord.setAssignToUser(getAttributeValue(profileAttributeBos, OLEConstants.ASSIGN_TO_USER));
        //oleTxRecord.setUseTaxIndicator(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.USE_TAXIND)));
        //oleTxRecord.setOrderType(getAttributeValue(profileAttributeBos, OLEConstants.ORDER_TYPE));
        /*oleTxRecord.setFundingSource(getAttributeValue(profileAttributeBos, OLEConstants.FUNDING_SOURCE));
        oleTxRecord.setPayReqPositiveApprovalReq(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.PAYREQ_POSITIVE_APPROVAL)));
        oleTxRecord.setPurchaseOrderConfirmationIndicator(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.PURCHASE_CONFIRMATION_INDICATOR)));
        oleTxRecord.setRequisitionSource(getAttributeValue(profileAttributeBos, OLEConstants.REQUISITION_SOURCE));
        oleTxRecord.setDeliveryCampusCode(getAttributeValue(profileAttributeBos, OLEConstants.DELIVERY_CAMPUS));
        oleTxRecord.setBuildingCode(getAttributeValue(profileAttributeBos, OLEConstants.BUILDING));*/
        //oleTxRecord.setVendorChoice(getAttributeValue(profileAttributeBos, OLEConstants.VENDOR_CHOICE));
        /*oleTxRecord.setItemType(getAttributeValue(profileAttributeBos, OLEConstants.ITEM_TYPE));
        oleTxRecord.setRouteToRequestor(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.ROUTE_RQUESTER)));
        oleTxRecord.setRouteToRequestor(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.PUBLIC_VIEW)));
        oleTxRecord.setMethodOfPOTransmission(getAttributeValue(profileAttributeBos, OLEConstants.PO_TRAMISSION_METHOD));
        oleTxRecord.setInternalPurchasingLimit(getAttributeValue(profileAttributeBos, OLEConstants.INTERNAL_PURCHASING_LIMIT));
        oleTxRecord.setCostSource(getAttributeValue(profileAttributeBos, OLEConstants.COST_SOURCE));
        oleTxRecord.setPercent(getAttributeValue(profileAttributeBos, OLEConstants.PERCENT));
        oleTxRecord.setDefaultLocation(getAttributeValue(profileAttributeBos, OLEConstants.DEFAULT_LOCATION));*/
        return oleTxRecord;
    }

    public OleTxRecord build(List<ProfileAttributeBo> profileAttributeBos) throws Exception {
        OleTxRecord oleTxRecord = new OleTxRecord();
/*        oleTxRecord.setListPrice(getListPrice(lineItemOrder));
        oleTxRecord.setQuantity(getQuantity(lineItemOrder));
        oleTxRecord.setVendorItemIdentifier(getVendorItemIdentifier(lineItemOrder));
        oleTxRecord.setVendorNumber(getVendorNumber(ediOrder));       */
        /*oleTxRecord.setChartCode(getChartCode(profileAttributeBos));
        oleTxRecord.setOrgCode(getAttributeValue(profileAttributeBos, OLEConstants.ORG_CODE));
        oleTxRecord.setReceivingRequired(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.RECV_REQUIRED)));
        oleTxRecord.setContractManager(getAttributeValue(profileAttributeBos, OLEConstants.CONTRACT_MANAGER));*/
        //oleTxRecord.setAssignToUser(getAttributeValue(profileAttributeBos, OLEConstants.ASSIGN_TO_USER));
        //oleTxRecord.setUseTaxIndicator(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.USE_TAXIND)));
        //oleTxRecord.setOrderType(getAttributeValue(profileAttributeBos, OLEConstants.ORDER_TYPE));
        /*oleTxRecord.setFundingSource(getAttributeValue(profileAttributeBos, OLEConstants.FUNDING_SOURCE));
        oleTxRecord.setPayReqPositiveApprovalReq(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.PAYREQ_POSITIVE_APPROVAL)));
        oleTxRecord.setPurchaseOrderConfirmationIndicator(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.PURCHASE_CONFIRMATION_INDICATOR)));
        oleTxRecord.setRequisitionSource(getAttributeValue(profileAttributeBos, OLEConstants.REQUISITION_SOURCE));
        oleTxRecord.setDeliveryCampusCode(getAttributeValue(profileAttributeBos, OLEConstants.DELIVERY_CAMPUS));
        oleTxRecord.setBuildingCode(getAttributeValue(profileAttributeBos, OLEConstants.BUILDING));*/
        //oleTxRecord.setVendorChoice(getAttributeValue(profileAttributeBos, OLEConstants.VENDOR_CHOICE));
        /*oleTxRecord.setItemType(getAttributeValue(profileAttributeBos, OLEConstants.ITEM_TYPE));
        oleTxRecord.setRouteToRequestor(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.ROUTE_RQUESTER)));
        oleTxRecord.setRouteToRequestor(Boolean.parseBoolean(getAttributeValue(profileAttributeBos, OLEConstants.PUBLIC_VIEW)));
        oleTxRecord.setMethodOfPOTransmission(getAttributeValue(profileAttributeBos, OLEConstants.PO_TRAMISSION_METHOD));
        oleTxRecord.setInternalPurchasingLimit(getAttributeValue(profileAttributeBos, OLEConstants.INTERNAL_PURCHASING_LIMIT));
        oleTxRecord.setCostSource(getAttributeValue(profileAttributeBos, OLEConstants.COST_SOURCE));
        oleTxRecord.setPercent(getAttributeValue(profileAttributeBos, OLEConstants.PERCENT));
        oleTxRecord.setDefaultLocation(getAttributeValue(profileAttributeBos, OLEConstants.DEFAULT_LOCATION));*/
        return oleTxRecord;
    }


    /**
     *  This method returns chartCode from the List of profileAttributeBos.
     * @param profileAttributeBos
     * @return   AttributeValue
     */
    private String getChartCode(List<ProfileAttributeBo> profileAttributeBos) {
        return getAttributeValue(profileAttributeBos, OLEConstants.OLE_CHART_CODE);
    }

    /**
     *  This method gets the vendorNumber from ediOrder.
     * @param ediOrder
     * @return
     */
    private String getVendorNumber(EDIOrder ediOrder) {
        return ediOrder.getMessage().getSupplierPartyQualifier().getSupplierInformation().getSupplierCodeIdentification();
    }

    /**
     * This method returns fundCode as AccountInfo, based on List of  supplierReferenceInformation got from lineItemOrder.
     * @param lineItemOrder
     * @return AccountInfo
     */
    public Map<String, String> getAccountInfo(LineItemOrder lineItemOrder) throws Exception {
        List<SupplierReferenceInformation> supplierReferenceInformationList = lineItemOrder.getSupplierReferenceInformation();
        if (!supplierReferenceInformationList.isEmpty()) {
            SupplierReferenceInformation supplierReferenceInformation = supplierReferenceInformationList.get(0);
            List<SupplierLineItemReference> supplierLineItemReferenceList = supplierReferenceInformation.getSupplierLineItemReference();
            if (!supplierLineItemReferenceList.isEmpty()) {
                SupplierLineItemReference supplierLineItemReference = supplierLineItemReferenceList.get(0);
                if (supplierLineItemReference.getSuppliersOrderLine().equals("BFN")) {
                    return translateRefNumberToOLEAccountInfo(supplierLineItemReference.getVendorReferenceNumber());
                }
            }
        }
        return null;
    }



    /**
     * This method returns fundCode as AccountInfo, based on List of  supplierReferenceInformation got from lineItemOrder.
     * @param vendorReferenceNumber
     * @return AccountInfo
     */
    public Map<String, String> getAccountInfo(String vendorReferenceNumber) throws Exception {
            return translateRefNumberToOLEAccountInfo(vendorReferenceNumber);
    }

    /**
     *  This method maps the vendorReferenceNumber and returns the  fundCode.
     * @param vendorReferenceNumber
     * @return  fundCodes
     */
    private Map<String, String> translateRefNumberToOLEAccountInfo(String vendorReferenceNumber)throws Exception{
        //HashMap<String, String> fundCodes = new OLEAccountInfoExtractor().buildAccountInfoMap(vendorReferenceNumber);
        HashMap<String, String> fundCodes = getVendorAccountObjectDetails(vendorReferenceNumber);
        return fundCodes;
    }

    private HashMap<String,String> getVendorAccountObjectDetails(String vendorReferenceNumber)throws Exception{
        HashMap<String,String>  criteriaMap=new HashMap<String,String>();
        criteriaMap.put("vendorRefNumber",vendorReferenceNumber);
        OleVendorAccountInfo oleVendorAccountInfo = getOverlayRetrivalService().getAccountObjectForVendorRefNo(criteriaMap);
        if(oleVendorAccountInfo==null)
            return null;
        HashMap<String,String> accountObjectMap=new HashMap<String, String>();
        accountObjectMap.put(oleVendorAccountInfo.getAccountNumber(), oleVendorAccountInfo.getObjectCode());
        return accountObjectMap;

    }

    /**
     *  This method returns vendorItemReference number from the List of buyerReferenceInformation got from lineItemOrder.
     * @param lineItemOrder
     * @return vendorItemReference
     */
    public String getVendorItemIdentifier(LineItemOrder lineItemOrder) {
        List<BuyerReferenceInformation> buyerReferenceInformationList = lineItemOrder.getBuyerReferenceInformation();
        if (buyerReferenceInformationList.size() > 0) {
            BuyerReferenceInformation buyerReferenceInformation = buyerReferenceInformationList.get(0);
            List<BuyerLineItemReference> buyerLineItemReferenceList = buyerReferenceInformation.getBuyerLineItemReference();
            if (buyerLineItemReferenceList.size() > 0) {
                BuyerLineItemReference buyerLineItemReferenceRef = buyerLineItemReferenceList.get(0);
                String buyersOrderLine = buyerLineItemReferenceRef.getBuyersOrderLine();
                String vendorItemReference = buyerLineItemReferenceRef.getOrderLineNumber();
                if (buyersOrderLine.equals("SLI")) {
                    return vendorItemReference;
                }
            }
        }
        return null;
    }

    /**
     * This method returns the Quantity from the list of QuantityInformation got from lineItemOrder.
     * If there are no QuantityInformation then it return null.
     * @param lineItemOrder
     * @return Quantity
     */
    private String getQuantity(LineItemOrder lineItemOrder) {
        List<QuantityInformation> quantityInformation = lineItemOrder.getQuantityInformation();
        if (quantityInformation.size() > 0) {
            List<Qunatity> qunatity = quantityInformation.get(0).getQunatity();
            if (qunatity.size() > 0) {
                return qunatity.get(0).getQuantity();
            }
        }
        return null;
    }

    /**
     *  This method returns ListPrice from the List of PriceInformation got from lineItemOrder.
     *  If there are no PriceInformation then it return null.
     * @param lineItemOrder
     * @return  Price
     */
    private String getListPrice(LineItemOrder lineItemOrder) {
        List<PriceInformation> priceInformation = lineItemOrder.getPriceInformation();
        if (priceInformation.size() > 0) {
            List<ItemPrice> itemPrice = priceInformation.get(0).getItemPrice();
            if (itemPrice.size() > 0) {
                return itemPrice.get(0).getPrice();
            }
        }
        return null;
    }

    /**
     * This method returns AttributeValue from List of profileAttribute with matching attributeName.
     * If there are no profile attributes then it return null.
     * @param profileAttributes
     * @param attributeName
     * @return  attributeValue
     */
    private String getAttributeValue(List<ProfileAttributeBo> profileAttributes, String attributeName) {
        for (Iterator<ProfileAttributeBo> iterator = profileAttributes.iterator(); iterator.hasNext(); ) {
            ProfileAttributeBo attribute = iterator.next();
            if (attribute.getAttributeName().equals(attributeName)) {
                return attribute.getAttributeValue();
            }
        }
        return null;
    }

    public OverlayRetrivalService getOverlayRetrivalService() {
        if(overlayRetrivalService == null){
            overlayRetrivalService = GlobalResourceLoader.getService(OLEConstants.OVERLAY_RETRIVAL_SERVICE);
        }
        return overlayRetrivalService;
    }

    public void setOverlayRetrivalService(OverlayRetrivalService overlayRetrivalService) {
        this.overlayRetrivalService = overlayRetrivalService;
    }

}
