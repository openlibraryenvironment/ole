package org.kuali.ole.select.document;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/24/13
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceInvoices extends PersistableBusinessObjectBase {
    private String oleEResInvoiceId;
    private String oleERSIdentifier;
    private Integer oleInvoiceItemId;
    private String invoiceId;
    private Integer purchaseOrderId;
    private String invoiceNumber;
    private Date invoiceDate;
    private String vendorName;
    private String invoicedAmount;
    private Date paidDate;
    private String invoiceNote;
    private String fundCode;
    private Date checkClearedDate;
    private String checkNumber;
    private String invoiceStatus;
    private String holdingsId;
    private OLEEResourceRecordDocument oleERSDocument;
    private OLEEResourcePO oleEResPO;
    private String redirectUrl;

    public String getOleEResInvoiceId() {
        return oleEResInvoiceId;
    }

    public void setOleEResInvoiceId(String oleEResInvoiceId) {
        this.oleEResInvoiceId = oleEResInvoiceId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public Integer getOleInvoiceItemId() {
        return oleInvoiceItemId;
    }

    public void setOleInvoiceItemId(Integer oleInvoiceItemId) {
        this.oleInvoiceItemId = oleInvoiceItemId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(String invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public String getInvoiceNote() {
        return invoiceNote;
    }

    public void setInvoiceNote(String invoiceNote) {
        this.invoiceNote = invoiceNote;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public Date getCheckClearedDate() {
        return checkClearedDate;
    }

    public void setCheckClearedDate(Date checkClearedDate) {
        this.checkClearedDate = checkClearedDate;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public OLEEResourceRecordDocument getOleERSDocument() {
        return oleERSDocument;
    }

    public void setOleERSDocument(OLEEResourceRecordDocument oleERSDocument) {
        this.oleERSDocument = oleERSDocument;
    }

    public OLEEResourcePO getOleEResPO() {
        return oleEResPO;
    }

    public void setOleEResPO(OLEEResourcePO oleEResPO) {
        this.oleEResPO = oleEResPO;
    }

    public String getRedirectUrl() {
        String invoiceDocumentNumber = this.getInvoiceNumber();
        if (StringUtils.isNotBlank(invoiceDocumentNumber)) {
            redirectUrl = ConfigContext.getCurrentContextConfig().getProperty("kew.url") + OLEConstants.PO_LINE_ITEM_URL + invoiceDocumentNumber;
        }
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
