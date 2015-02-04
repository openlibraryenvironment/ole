package org.kuali.ole.select.document;

import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arjuns
 * Date: 6/24/13
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourcePO extends PersistableBusinessObjectBase {
    private String oleEResPOId;
    private String oleERSIdentifier;
    private Integer olePOItemId;
    private String redirectUrl;
    private String instanceId;
    private String title;
    private Integer paidAmountCurrentFY;
    private Integer paidAmountPreviousFY;
    private Integer paidAmountTwoYearsPreviousFY;
    private String purpose;
    private String poItemId;
    private String poStatus;
    private OLEEResourceRecordDocument oleERSDocument;

    private BusinessObjectService businessObjectService;
    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public String getPoItemId() {
        return poItemId;
    }

    public void setPoItemId(String poItemId) {
        this.poItemId = poItemId;
    }

    public String getOleEResPOId() {
        return oleEResPOId;
    }

    public void setOleEResPOId(String oleEResPOId) {
        this.oleEResPOId = oleEResPOId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOlePOItemId() {
        return olePOItemId;
    }

    public void setOlePOItemId(Integer olePOItemId) {
        this.olePOItemId = olePOItemId;
    }

    public String getRedirectUrl() {
        String purchaseOrderLineItemIdentifier = this.getOlePOItemId().toString();
        if (purchaseOrderLineItemIdentifier != null && !"".equals(purchaseOrderLineItemIdentifier)) {
            Map poNumber = new HashMap();
            poNumber.put(OLEConstants.PUR_DOC_IDENTIFIER, purchaseOrderLineItemIdentifier);
            List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = (List) getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, poNumber);
            for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocumentList) {
                if (olePurchaseOrderDocument != null) {
                    boolean validPO = olePurchaseOrderDocument != null ? olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching() : false;
                    if (validPO) {
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

    public Integer getPaidAmountCurrentFY() {
        return paidAmountCurrentFY;
    }

    public void setPaidAmountCurrentFY(Integer paidAmountCurrentFY) {
        this.paidAmountCurrentFY = paidAmountCurrentFY;
    }

    public Integer getPaidAmountPreviousFY() {
        return paidAmountPreviousFY;
    }

    public void setPaidAmountPreviousFY(Integer paidAmountPreviousFY) {
        this.paidAmountPreviousFY = paidAmountPreviousFY;
    }

    public Integer getPaidAmountTwoYearsPreviousFY() {
        return paidAmountTwoYearsPreviousFY;
    }

    public void setPaidAmountTwoYearsPreviousFY(Integer paidAmountTwoYearsPreviousFY) {
        this.paidAmountTwoYearsPreviousFY = paidAmountTwoYearsPreviousFY;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getPoStatus() {
        return poStatus;
    }

    public void setPoStatus(String poStatus) {
        this.poStatus = poStatus;
    }

    public OLEEResourceRecordDocument getOleERSDocument() {
        return oleERSDocument;
    }

    public void setOleERSDocument(OLEEResourceRecordDocument oleERSDocument) {
        this.oleERSDocument = oleERSDocument;
    }
}
