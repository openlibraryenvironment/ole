package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OleLoanFastAdd;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pvsubrah on 6/18/15.
 */
public class CircForm extends UifFormBase {
    /*
    Need loggedInUser is the principalId used for checking for permissions etc.
    overridingPrincipalName is the operator who has overriding privs. Needed for the loan.
     */
    private String loggedInUser = null;
    private String overridingPrincipalName;

    /*
    selectedCirculationDesk selected circ desk.
    previouslySelectedCirculationDesk is to maintain the state
    in case we need to revert back to the previously selected desk.
     */
    private String selectedCirculationDesk;
    private String previouslySelectedCirculationDesk;

    private ErrorMessage errorMessage = new ErrorMessage();

    private boolean proxyCheckDone;
    private boolean itemValidationDone;

    /*
    patronBarcode: Current Borrower
     */
    private String patronBarcode;
    private OlePatronDocument patronDocument;


    private String itemBarcode;
    private ItemRecord itemRecord;

    private boolean proceedWithCheckout;
    private boolean showExistingLoan;


    private OleLoanFastAdd oleLoanFastAdd;

    private Date customDueDateMap;
    private String customDueDateTime;
    private String customDueDateTimeMessage;

    private List<OleLoanDocument> loanDocumentListForCurrentSession;
    private List<OleLoanDocument> existingLoanList;

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public String getSelectedCirculationDesk() {
        return selectedCirculationDesk;
    }

    public void setSelectedCirculationDesk(String selectedCirculationDesk) {
        this.selectedCirculationDesk = selectedCirculationDesk;
    }

    public String getPreviouslySelectedCirculationDesk() {
        return previouslySelectedCirculationDesk;
    }

    public void setPreviouslySelectedCirculationDesk(String previouslySelectedCirculationDesk) {
        this.previouslySelectedCirculationDesk = previouslySelectedCirculationDesk;
    }

    public void reset() {
        this.previouslySelectedCirculationDesk = this.selectedCirculationDesk;
        this.patronBarcode = null;
        this.itemBarcode = null;
        this.itemRecord = null;
        this.showExistingLoan = false;
        this.proxyCheckDone = false;
        this.itemValidationDone = false;
        if (null != getExistingLoanList()) {
            this.getExistingLoanList().clear();
        }
        if (null != getLoanDocumentListForCurrentSession()) {
            this.getLoanDocumentListForCurrentSession().clear();
        }
        this.patronDocument = new OlePatronDocument();
        this.proceedWithCheckout = false;
    }

    public OlePatronDocument getPatronDocument() {
        return patronDocument;
    }

    public void setPatronDocument(OlePatronDocument patronDocument) {
        this.patronDocument = patronDocument;
        if (null != patronDocument) {
            this.patronBarcode = patronDocument.getBarcode();
        }
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOverridingPrincipalName() {
        return overridingPrincipalName;
    }

    public void setOverridingPrincipalName(String overridingPrincipalName) {
        this.overridingPrincipalName = overridingPrincipalName;
    }

    public boolean isProceedWithCheckout() {
        return proceedWithCheckout;
    }

    public void setProceedWithCheckout(boolean proceedWithCheckout) {
        this.proceedWithCheckout = proceedWithCheckout;
    }

    public boolean isProxyCheckDone() {
        return proxyCheckDone;
    }

    public boolean isItemValidationDone() {
        return itemValidationDone;
    }

    public void setItemValidationDone(boolean itemValidationDone) {
        this.itemValidationDone = itemValidationDone;
    }

    public void setProxyCheckDone(boolean proxyCheckDone) {
        this.proxyCheckDone = proxyCheckDone;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public boolean isShowExistingLoan() {
        return showExistingLoan;
    }

    public void setShowExistingLoan(boolean showExistingLoan) {
        this.showExistingLoan = showExistingLoan;
    }

    public List<OleLoanDocument> getLoanDocumentListForCurrentSession() {
        if (null == loanDocumentListForCurrentSession) {
            loanDocumentListForCurrentSession = new ArrayList<>();
        }
        return loanDocumentListForCurrentSession;
    }

    public void setLoanDocumentListForCurrentSession(List<OleLoanDocument> loanDocumentListForCurrentSession) {
        this.loanDocumentListForCurrentSession = loanDocumentListForCurrentSession;
    }

    public void setItemRecord(ItemRecord itemRecord) {
        this.itemRecord = itemRecord;
    }

    public ItemRecord getItemRecord() {
        return itemRecord;
    }

    public String getCustomDueDateTimeMessage() {
        return customDueDateTimeMessage;
    }

    public void setCustomDueDateTimeMessage(String customDueDateTimeMessage) {
        this.customDueDateTimeMessage = customDueDateTimeMessage;
    }

    public Date getCustomDueDateMap() {
        return customDueDateMap;
    }

    public void setCustomDueDateMap(Date customDueDateMap) {
        this.customDueDateMap = customDueDateMap;
    }

    public String getCustomDueDateTime() {
        return customDueDateTime;
    }

    public void setCustomDueDateTime(String customDueDateTime) {
        this.customDueDateTime = customDueDateTime;
    }

    public void setExistingLoanList(List<OleLoanDocument> existingLoanList) {
        this.existingLoanList = existingLoanList;
    }

    public List<OleLoanDocument> getExistingLoanList() {
        return existingLoanList;
    }

    public OleLoanFastAdd getOleLoanFastAdd() {
        return oleLoanFastAdd;
    }

    public void setOleLoanFastAdd(OleLoanFastAdd oleLoanFastAdd) {
        this.oleLoanFastAdd = oleLoanFastAdd;
    }
}
