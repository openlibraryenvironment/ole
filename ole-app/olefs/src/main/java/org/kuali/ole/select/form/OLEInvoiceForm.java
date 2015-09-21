package org.kuali.ole.select.form;

import java.util.HashMap;
import java.util.Map;

import org.kuali.ole.select.document.OleInvoiceDocument;
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
    private Boolean filterAdditionalCharges;

    private String pageNumber;
    private String selectRowDetails;
    private String urlBase;
    private boolean showSelectedRowDetails;
    private boolean successFlag;
    private boolean blanketApproveSuccessFlag;

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

	public boolean isFilterAdditionalCharges() {
		if (filterAdditionalCharges == null) {
			OleInvoiceDocument doc = (OleInvoiceDocument) getDocument();
			filterAdditionalCharges = doc == null ? true : !doc.isAdditionalChargesFlag();
		}
		return filterAdditionalCharges;
	}
	
	public void setFilterAdditionalCharges(boolean filterAdditionalCharges) {
		this.filterAdditionalCharges = filterAdditionalCharges;
	}

	public boolean isFilterRequisition() {
		Map<String, Object> ext = getExtensionData();
		for (String k : ext.keySet())
			if (k != null && k.startsWith("requisition"))
				return false;
		return true;
	}
	
	public boolean showRequisition(String id) {
		return Boolean.TRUE.equals(getExtensionData().get("requisition" + id));
	}

	public void setShowRequisitionFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.put("requisition" + id, Boolean.TRUE);
		}
	}
	
	public void setHideRequisitionFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.remove("requisition" + id);
		}
	}
	
	public boolean isFilterLineItemReceiving() {
		Map<String, Object> ext = getExtensionData();
		for (String k : ext.keySet())
			if (k != null && k.startsWith("LineItemReceiving"))
				return false;
		return true;
	}
	
	public boolean showLineItemReceiving(String id) {
		return Boolean.TRUE.equals(getExtensionData().get("LineItemReceiving" + id));
	}

	public void setShowLineItemReceivingFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.put("LineItemReceiving" + id, Boolean.TRUE);
		}
	}
	
	public void setHideLineItemReceivingFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.remove("LineItemReceiving" + id);
		}
	}
	
	public boolean isFilterCorrection() {
		Map<String, Object> ext = getExtensionData();
		for (String k : ext.keySet())
			if (k != null && k.startsWith("Correction"))
				return false;
		return true;
	}
	
	public boolean showCorrection(String id) {
		return Boolean.TRUE.equals(getExtensionData().get("Correction" + id));
	}

	public void setShowCorrectionFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.put("Correction" + id, Boolean.TRUE);
		}
	}
	
	public void setHideCorrectionFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.remove("Correction" + id);
		}
	}
	
	public boolean isFilterPaymentRequest() {
		Map<String, Object> ext = getExtensionData();
		for (String k : ext.keySet())
			if (k != null && k.startsWith("PaymentRequest"))
				return false;
		return true;
	}
	
	public boolean showPaymentRequest(String id) {
		return Boolean.TRUE.equals(getExtensionData().get("PaymentRequest" + id));
	}

	public void setShowPaymentRequestFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.put("PaymentRequest" + id, Boolean.TRUE);
		}
	}
	
	public void setHidePaymentRequestFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.remove("PaymentRequest" + id);
		}
	}
	
	public boolean isFilterCreditMemo() {
		Map<String, Object> ext = getExtensionData();
		for (String k : ext.keySet())
			if (k != null && k.startsWith("CreditMemo"))
				return false;
		return true;
	}
	
	public boolean showCreditMemo(String id) {
		return Boolean.TRUE.equals(getExtensionData().get("CreditMemo" + id));
	}

	public void setShowCreditMemoFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.put("CreditMemo" + id, Boolean.TRUE);
		}
	}
	
	public void setHideCreditMemoFor(String id) {
		Map<String, Object> ext = getExtensionData();
		synchronized (ext) {
			ext.remove("CreditMemo" + id);
		}
	}
	
	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getSelectRowDetails() {
		return selectRowDetails;
	}

	public void setSelectRowDetails(String selectRowDetails) {
		this.selectRowDetails = selectRowDetails;
	}

	public boolean isShowSelectedRowDetails() {
		return showSelectedRowDetails;
	}

	public void setShowSelectedRowDetails(boolean showSelectedRowDetails) {
		this.showSelectedRowDetails = showSelectedRowDetails;
	}

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public boolean isSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.successFlag = successFlag;
    }

    public boolean isBlanketApproveSuccessFlag() {
        return blanketApproveSuccessFlag;
    }

    public void setBlanketApproveSuccessFlag(boolean blanketApproveSuccessFlag) {
        this.blanketApproveSuccessFlag = blanketApproveSuccessFlag;
    }
}

