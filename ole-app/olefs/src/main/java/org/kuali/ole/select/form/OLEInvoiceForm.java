package org.kuali.ole.select.form;

import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;

/**
 * Created with IntelliJ IDEA.
 * User: anithaa
 * Date: 7/8/13
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInvoiceForm extends TransactionalDocumentFormBase {

    private String sfcFailRouteMsg;
    private String sfcFailApproveMsg;
    private String itemLimit;

    private String validationMessage;
    private String validationFlag;
    private String subscriptionValidationMessage;
    private String blanketApproveSubscriptionDateValidationFlag;
    private String subscriptionDateValidationMessage;
    private String subscriptionDateValidationFlag;

    private String duplicationMessage;
    private String duplicationApproveMessage;
    
    private boolean filterProcessItems = true;

    public String getSubscriptionDateValidationFlag() {
        return subscriptionDateValidationFlag;
    }

    public void setSubscriptionDateValidationFlag(String subscriptionDateValidationFlag) {
        this.subscriptionDateValidationFlag = subscriptionDateValidationFlag;
    }

    private String amountExceedsMessage;
    private String amountExceedsMesgForBlankApp;

    public String getSubscriptionDateValidationMessage() {
        return subscriptionDateValidationMessage;
    }

    public void setSubscriptionDateValidationMessage(String subscriptionDateValidationMessage) {
        this.subscriptionDateValidationMessage = subscriptionDateValidationMessage;
    }

    public String getSubscriptionValidationMessage() {
        return subscriptionValidationMessage;
    }

    public void setSubscriptionValidationMessage(String subscriptionValidationMessage) {
        this.subscriptionValidationMessage = subscriptionValidationMessage;
    }

    public String getBlanketApproveSubscriptionDateValidationFlag() {
        return blanketApproveSubscriptionDateValidationFlag;
    }

    public void setBlanketApproveSubscriptionDateValidationFlag(String blanketApproveSubscriptionDateValidationFlag) {
        this.blanketApproveSubscriptionDateValidationFlag = blanketApproveSubscriptionDateValidationFlag;
    }

    public String getAmountExceedsMesgForBlankApp() {
        return amountExceedsMesgForBlankApp;
    }

    public void setAmountExceedsMesgForBlankApp(String amountExceedsMesgForBlankApp) {
        this.amountExceedsMesgForBlankApp = amountExceedsMesgForBlankApp;
    }

    public String getAmountExceedsMessage() {
        return amountExceedsMessage;
    }

    public void setAmountExceedsMessage(String amountExceedsMessage) {
        this.amountExceedsMessage = amountExceedsMessage;
    }

    public OLEInvoiceForm() {
        super();
    }

    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_PRQS";
    }

    public String getSfcFailRouteMsg() {
        return sfcFailRouteMsg;
    }

    public void setSfcFailRouteMsg(String sfcFailRouteMsg) {
        this.sfcFailRouteMsg = sfcFailRouteMsg;
    }

    public String getSfcFailApproveMsg() {
        return sfcFailApproveMsg;
    }

    public void setSfcFailApproveMsg(String sfcFailApproveMsg) {
        this.sfcFailApproveMsg = sfcFailApproveMsg;
    }

    public String getItemLimit() {
        return itemLimit;
    }

    public void setItemLimit(String itemLimit) {
        this.itemLimit = itemLimit;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getValidationFlag() {
        return validationFlag;
    }

    public void setValidationFlag(String validationFlag) {
        this.validationFlag = validationFlag;
    }

    /**
     * This method gets the duplicationMessage
     * @return duplicationMessage
     */
    public String getDuplicationMessage() {
        return duplicationMessage;
    }

    /**
     * This method sets the duplicationMessage
     * @param duplicationMessage
     */
    public void setDuplicationMessage(String duplicationMessage) {
        this.duplicationMessage = duplicationMessage;
    }

    public String getDuplicationApproveMessage() {
        return duplicationApproveMessage;
    }

    public void setDuplicationApproveMessage(String duplicationApproveMessage) {
        this.duplicationApproveMessage = duplicationApproveMessage;
    }

	public boolean isFilterProcessItems() {
		return filterProcessItems;
	}

	public void setFilterProcessItems(boolean filterProcessItems) {
		this.filterProcessItems = filterProcessItems;
	}

}

