package org.kuali.ole.select.bo;

import org.kuali.ole.coa.businessobject.OLECretePOAccountingLine;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sambasivam on 19/9/14.
 *
 * This class is used for displaying the results for create POs for eResource and instance.
 */
public class OLECreatePO extends PersistableBusinessObjectBase implements Cloneable {


    private String createPOId;
    private boolean selectFlag;
    private String poId;
    private String gokbId;
    private String title;
    private String isbnNIssn;
    private String price;
    private String vendorId;
    private String purposeId;
    private String orderTypeId;
    private String instanceId;
    private String instanceFlag;
    private String bibId;
    private String redirectUrl;
    private String oleERSIdentifier;
    private List<OLECretePOAccountingLine> accountingLines = new ArrayList<>();
    private List<OleFundCode> fundCodes = new ArrayList<>();

    public String getCreatePOId() {
        return createPOId;
    }

    public void setCreatePOId(String createPOId) {
        this.createPOId = createPOId;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getGokbId() {
        return gokbId;
    }

    public void setGokbId(String gokbId) {
        this.gokbId = gokbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbnNIssn() {
        return isbnNIssn;
    }

    public void setIsbnNIssn(String isbnNIssn) {
        this.isbnNIssn = isbnNIssn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }

    public String getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(String orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceFlag() {
        return instanceFlag;
    }

    public void setInstanceFlag(String instanceFlag) {
        this.instanceFlag = instanceFlag;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getRedirectUrl() {
        String purchaseOrderLineItemIdentifier = this.getPoId().toString();
        if (purchaseOrderLineItemIdentifier != null && !"".equals(purchaseOrderLineItemIdentifier)) {
            Map poNumber = new HashMap();
            poNumber.put(OLEConstants.PUR_DOC_IDENTIFIER, purchaseOrderLineItemIdentifier);
            List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = (List) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, poNumber);
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

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public List<OLECretePOAccountingLine> getAccountingLines() {
        return accountingLines;
    }

    public void setAccountingLines(List<OLECretePOAccountingLine> accountingLines) {
        this.accountingLines = accountingLines;
    }

    public List<OleFundCode> getFundCodes() {
        return fundCodes;
    }

    public void setFundCodes(List<OleFundCode> fundCodes) {
        this.fundCodes = fundCodes;
    }
}
