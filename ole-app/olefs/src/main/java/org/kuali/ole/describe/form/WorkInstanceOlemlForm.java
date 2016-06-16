package org.kuali.ole.describe.form;

/**
 * Created with IntelliJ IDEA.
 * User: pp7788
 * Date: 12/11/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */

import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.describe.bo.InstanceRecordMetaData;
import org.kuali.ole.describe.bo.SourceEditorForUI;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InstanceEditorForm is the form class for Instance Editor
 */
public class WorkInstanceOlemlForm extends EditorForm {

    private InstanceRecordMetaData itemRecordMetaData = new InstanceRecordMetaData();
    private InstanceRecordMetaData holdingRecordMetaData = new InstanceRecordMetaData();
    private OleHoldings selectedHolding;
    private Instance instance;
    private Item selectedItem;
    private SourceEditorForUI selectedSourceHolding;
    private String oldItemStatus;
    private String redirectUrl;
    private String currentBarcode;
    private String proxyBarcode;
    private String lastBarcode;
    private BusinessObjectService businessObjectService;
    public WorkInstanceOlemlForm() {
        getSelectedHolding().getUri().add(new Uri());
        getSelectedHolding().getNote().add(new Note());
        getSelectedHolding().getExtentOfOwnership().add(new ExtentOfOwnership());
        getSelectedHolding().getExtentOfOwnership().get(0).getNote().add(new Note());
        getSelectedItem().getNote().add(new Note());
        getSelectedItem().getDonorInfo().add(new DonorInfo());
    }

    /**
     * Gets the instance of BusinessObjectService
     *
     * @return businessObjectService(BusinessObjectService)
     */
    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InstanceRecordMetaData getItemRecordMetaData() {
        return itemRecordMetaData;
    }

    public String getCurrentBarcode() {
        return currentBarcode;
    }

    public void setCurrentBarcode(String currentBarcode) {
        this.currentBarcode = currentBarcode;
    }

    public String getProxyBarcode() {
        return proxyBarcode;
    }

    public void setProxyBarcode(String proxyBarcode) {
        this.proxyBarcode = proxyBarcode;
    }

    public String getLastBarcode() {
        return lastBarcode;
    }

    public void setLastBarcode(String lastBarcode) {
        this.lastBarcode = lastBarcode;
    }

    public void setItemRecordMetaData(InstanceRecordMetaData itemRecordMetaData) {
        this.itemRecordMetaData = itemRecordMetaData;
    }

    public InstanceRecordMetaData getHoldingRecordMetaData() {
        return holdingRecordMetaData;
    }

    public void setHoldingRecordMetaData(InstanceRecordMetaData holdingRecordMetaData) {
        this.holdingRecordMetaData = holdingRecordMetaData;
    }

    public OleHoldings getSelectedHolding() {
        if (null == selectedHolding) {
            selectedHolding = new OleHoldings();
            selectedHolding.setCallNumber(new CallNumber());
        }
        return selectedHolding;
    }

    public void setSelectedHolding(OleHoldings selectedHolding) {
        this.selectedHolding = selectedHolding;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Item getSelectedItem() {
        if (null == selectedItem) {
            selectedItem = new Item();
            selectedItem.setCallNumber(new CallNumber());
        }
        return selectedItem;
    }

    public void setSelectedItem(Item selectedItem) {
        this.selectedItem = selectedItem;
    }

    public SourceEditorForUI getSelectedSourceHolding() {
        return selectedSourceHolding;
    }

    public void setSelectedSourceHolding(SourceEditorForUI selectedSourceHolding) {
        this.selectedSourceHolding = selectedSourceHolding;
    }

    public String getOldItemStatus() {
        return oldItemStatus;
    }

    public void setOldItemStatus(String oldItemStatus) {
        this.oldItemStatus = oldItemStatus;
    }

    public String getRedirectUrl() {
        String purchaseOrderLineItemIdentifier = this.getSelectedItem().getPurchaseOrderLineItemIdentifier();
        if (purchaseOrderLineItemIdentifier != null && !"".equals(purchaseOrderLineItemIdentifier)) {
            Map poNumber = new HashMap();
            poNumber.put(OLEConstants.PUR_DOC_IDENTIFIER, purchaseOrderLineItemIdentifier);
            List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = (List) getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, poNumber);
            for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocumentList) {
                if (olePurchaseOrderDocument != null) {
                    boolean validPO = olePurchaseOrderDocument!=null ? olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching() : false;
                    if (validPO){
                        redirectUrl = ConfigContext.getCurrentContextConfig().getProperty("kew.url") + OLEConstants.PO_LINE_ITEM_URL + olePurchaseOrderDocument.getDocumentNumber();
                    }
                }
            }
        }
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


}
