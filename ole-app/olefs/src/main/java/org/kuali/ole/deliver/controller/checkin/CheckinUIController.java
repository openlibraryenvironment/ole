package org.kuali.ole.deliver.controller.checkin;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.drools.CheckedInItem;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.util.GlobalVariables;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenchulakshmig on 8/21/15.
 */
public class CheckinUIController extends CheckinBaseController {

    @Override
    protected void setDataElements(OLEForm oleForm, ItemRecord itemRecord, OleLoanDocument loanDocument) {
        CheckinForm checkinForm = getCheckinForm(oleForm);
        checkinForm.getDroolsExchange().addToContext("itemRecord", itemRecord);
        checkinForm.getDroolsExchange().addToContext("oleLoanDocument", loanDocument);
        if (loanDocument != null) {
            checkinForm.getDroolsExchange().addToContext("olePatronDocument", loanDocument.getOlePatron());
        }
    }

    public CheckinForm getCheckinForm(OLEForm oleForm) {
        return (CheckinForm) oleForm;
    }

    @Override
    public String getItemBarcode(OLEForm oleForm) {
        return getCheckinForm(oleForm).getItemBarcode();
    }

    @Override
    public String getSelectedCirculationDesk(OLEForm oleForm) {
        return getCheckinForm(oleForm).getSelectedCirculationDesk();
    }

    @Override
    public Date getCustomDueDateMap(OLEForm oleForm) {
        return getCheckinForm(oleForm).getCustomDueDateMap();
    }

    @Override
    public String getCustomDueDateTime(OLEForm oleForm) {
        String checkinTime="";
        if(StringUtils.isNotBlank(getCheckinForm(oleForm).getCustomDueDateTime())) {
            checkinTime = getCheckinForm(oleForm).getCustomDueDateTime();
        }else {
            checkinTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        }
        return checkinTime;
    }

    @Override
    public void setCheckedInItem(CheckedInItem checkedInItem, OLEForm oleForm) {
        getCheckinForm(oleForm).setCheckedInItem(checkedInItem);
    }

    @Override
    public CheckedInItem getCheckedInItem(OLEForm oleForm) {
        return getCheckinForm(oleForm).getCheckedInItem();
    }

    @Override
    public void addCheckedInItemToCheckedInItemList(CheckedInItem checkedInItem, OLEForm oleForm) {
        getCheckinForm(oleForm).getCheckedInItemList().add(0, checkedInItem);
    }

    @Override
    public boolean isRecordNoteForDamagedItem(OLEForm oleForm) {
        return getCheckinForm(oleForm).isRecordNoteForDamagedItem();
    }

    @Override
    public boolean isRecordNoteForClaimsReturn(OLEForm oleForm) {
        return getCheckinForm(oleForm).isRecordNoteForClaimsReturn();
    }

    @Override
    public boolean isRecordNoteForMissingPiece(OLEForm oleForm) {
        return getCheckinForm(oleForm).isRecordNoteForMissingPiece();
    }

    @Override
    public String getMissingPieceMatchCheck(OLEForm oleForm) {
        return getCheckinForm(oleForm).getMissingPieceMatchCheck();
    }

    @Override
    public void setNoOfPieces(OLEForm oleForm, String numberOfPieces) {
        getCheckinForm(oleForm).setNoOfPieces(numberOfPieces);
    }

    @Override
    public OleLoanDocument getOleLoanDocument(OLEForm oleForm) {
        return (OleLoanDocument) oleForm.getDroolsExchange().getContext().get("oleLoanDocument");
    }

    @Override
    public DroolsExchange getDroolsExchange(OLEForm oleForm) {
        return getCheckinForm(oleForm).getDroolsExchange();
    }

    @Override
    public String getOperatorId(OLEForm oleForm) {
        return GlobalVariables.getUserSession().getPrincipalId();
    }

    @Override
    public boolean isItemFoundInLibrary(OLEForm oleForm) {
        return getCheckinForm(oleForm).isItemFoundInLibrary();
    }
}
