package org.kuali.ole.deliver.controller.checkin;

import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.controller.checkin.CheckinBaseController;
import org.kuali.ole.deliver.drools.CheckedInItem;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

import java.util.Date;

/**
 * Created by chenchulakshmig on 8/21/15.
 */
public class CheckInAPIController extends CheckinBaseController {

    @Override
    protected void setDataElements(OLEForm oleForm, ItemRecord itemRecord, OleLoanDocument loanDocument) {
        DroolsExchange droolsExchange = oleForm.getDroolsExchange();
        droolsExchange.addToContext("itemRecord", itemRecord);
        droolsExchange.addToContext("oleLoanDocument", loanDocument);
        if (loanDocument != null) {
            droolsExchange.addToContext("olePatronDocument", loanDocument.getOlePatron());
        }
    }

    @Override
    public CheckinForm getCheckinForm(OLEForm oleForm) {
        return null;
    }

    @Override
    public String getItemBarcode(OLEForm oleForm) {
        return (String) oleForm.getDroolsExchange().getContext().get("itemBarcode");
    }

    @Override
    public String getSelectedCirculationDesk(OLEForm oleForm) {
        return (String) oleForm.getDroolsExchange().getContext().get("selectedCirculationDesk");
    }

    @Override
    public Date getCustomDueDateMap(OLEForm oleForm) {
        return new Date();
    }

    @Override
    public String getCustomDueDateTime(OLEForm oleForm) {
        return null;
    }

    @Override
    public void setCheckedInItem(CheckedInItem checkedInItem, OLEForm oleForm) {

    }

    @Override
    public CheckedInItem getCheckedInItem(OLEForm oleForm) {
        CheckedInItem checkedInItem = (CheckedInItem) oleForm.getDroolsExchange().getFromContext("checkedInItem");
        if (null == checkedInItem) {
            return new CheckedInItem();
        }
        return checkedInItem;
    }

    @Override
    public void addCheckedInItemToCheckedInItemList(CheckedInItem checkedInItem, OLEForm oleForm) {
        oleForm.getDroolsExchange().addToContext("checkedInItem", checkedInItem);
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
    public String getMissingPieceMatchCheck(OLEForm oleForm) {
        return null;
    }

    @Override
    public void setNoOfPieces(OLEForm oleForm, String numberOfPieces) {

    }

    @Override
    public OleLoanDocument getOleLoanDocument(OLEForm oleForm) {
        return (OleLoanDocument) oleForm.getDroolsExchange().getFromContext("oleLoanDocument");
    }

    @Override
    public DroolsExchange getDroolsExchange(OLEForm oleForm) {
        return oleForm.getDroolsExchange();
    }

    @Override
    public String getOperatorId(OLEForm oleForm) {
        return (String) oleForm.getDroolsExchange().getContext().get("operatorId");
    }

    @Override
    public boolean isItemFoundInLibrary(OLEForm oleForm) {
        return false;
    }
}
