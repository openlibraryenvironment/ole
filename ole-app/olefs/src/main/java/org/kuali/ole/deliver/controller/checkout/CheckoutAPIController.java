package org.kuali.ole.deliver.controller.checkout;

import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import java.sql.Timestamp;

/**
 * Created by pvsubrah on 8/19/15.
 */
public class CheckoutAPIController extends CheckoutBaseController {

    @Override
    protected void setDataElements(OLEForm oleForm, OleItemRecordForCirc oleItemRecordForCirc) {
        DroolsExchange droolsExchange = oleForm.getDroolsExchange();
        droolsExchange.addToContext("oleItemRecordForCirc", oleItemRecordForCirc);
    }

    @Override
    public CircForm getCircForm(OLEForm oleForm) {
        return null;
    }

    @Override
    public ItemRecord getItemRecord(OLEForm oleForm) {
        return (ItemRecord) oleForm.getDroolsExchange().getContext().get("itemRecord");
    }

    @Override
    public String getOperatorId(OLEForm oleForm) {
        return (String) oleForm.getDroolsExchange().getContext().get("operatorId");
    }

    @Override
    public void setItemRecord(OLEForm oleForm, ItemRecord itemRecord) {
        oleForm.getDroolsExchange().addToContext("itemRecord", itemRecord);
    }

    @Override
    public String getItemBarcode(OLEForm oleForm) {
        return (String) oleForm.getDroolsExchange().getContext().get("itemBarcode");
    }

    @Override
    public void setItemBarcode(OLEForm oleForm, String itemBarcode) {
        //UI logic and hence empty
    }

    @Override
    public OlePatronDocument getCurrentBorrower(OLEForm oleForm) {
        return (OlePatronDocument) oleForm.getDroolsExchange().getContext().get("currentBorrower");
    }

    @Override
    public void setItemValidationDone(boolean result, OLEForm oleForm) {
        //UI logic and hence empty
    }

    @Override
    public OleCirculationDesk getSelectedCirculationDesk(OLEForm oleForm) {
        return (OleCirculationDesk) oleForm.getDroolsExchange().getContext().get("selectedCirculationDesk");
    }

    @Override
    public void addLoanDocumentToCurrentSession(OleLoanDocument oleLoanDocument, OLEForm oleForm) {
        //UI logic and hence empty
    }

    @Override
    public boolean processCustomDueDateIfSet(OLEForm oleForm, OleLoanDocument oleLoanDocument) {
        return false;
    }

    @Override
    public void setDueDateTimeForItemRecord(OLEForm oleForm, Timestamp loanDueDate) {
        getItemRecord(oleForm).setDueDateTime(loanDueDate);
    }

    @Override
    public void addCurrentLoandDocumentToListofLoandedToPatron(OLEForm oleForm, OleLoanDocument oleLoanDocument) {
        oleForm.getDroolsExchange().addToContext("oleLoanDocument", oleLoanDocument);
    }

    @Override
    public void removeCurrentLoanDocumentFromCurrentSession(OLEForm oleForm, OleLoanDocument oleLoanDocument) {
        //UI logic and hence empty
    }

    @Override
    public void clearCurrentSessionList(OLEForm oleForm) {
        //UI logic and hence empty

    }

    @Override
    public String getCirculationLocationId(OLEForm oleForm) {
        return getSelectedCirculationDesk(oleForm).getCirculationDeskId();
    }

    @Override
    public boolean isRecordNoteForDamagedItem(OLEForm oleForm) {
        return false;
    }

    @Override
    public boolean isRecordNoteForClaimsReturn(OLEForm oleForm) {
        return false;
    }

    @Override
    public boolean isRecordNoteForMissingPiece(OLEForm oleForm) {
        return false;
    }

    @Override
    public String getMissingPieceMatchCheck(OLEForm oleForm){
        return null;
    }

}
