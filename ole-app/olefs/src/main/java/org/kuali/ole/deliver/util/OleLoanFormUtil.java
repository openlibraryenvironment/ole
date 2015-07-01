package org.kuali.ole.deliver.util;

import org.kuali.ole.deliver.form.OleLoanForm;

/**
 * Created by pvsubrah on 6/3/15.
 */
public class OleLoanFormUtil {
    private static OleLoanFormUtil oleLoanFormUtil;

    private OleLoanFormUtil(){

    }

    public static OleLoanFormUtil getInstance() {
        if(null == oleLoanFormUtil){
            oleLoanFormUtil = new OleLoanFormUtil();
        }
        return oleLoanFormUtil;
    }


    public void resetOleLoanForm(OleLoanForm oleLoanForm) {
        oleLoanForm.setAddressVerified(false);
        oleLoanForm.setOverrideRenewItemFlag(false);
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setBorrowerType(null);
        oleLoanForm.setPatronBarcode(null);
        oleLoanForm.setPatronName(null);
        oleLoanForm.setProxyPatronId(null);
        oleLoanForm.setRealPatronBarcode(null);
        oleLoanForm.setPatronId(null);
        oleLoanForm.setRealPatronList(null);
        oleLoanForm.setLoanList(null);
        oleLoanForm.setDueDateMap(null);
        oleLoanForm.setExistingLoanList(null);
        oleLoanForm.setDueDateMap(null);
        oleLoanForm.setMessage(null);
        oleLoanForm.setSuccess(true);
        oleLoanForm.setChangeLocationFlag(false);
        //oleLoanForm.setItemStatusLost(false);
        oleLoanForm.setBlockLoan(false);
        oleLoanForm.setItemFocus(false);
        oleLoanForm.setSelfCheckOut(false);
        oleLoanForm.setCurrentPatronList(null);
        oleLoanForm.setPatronFocus(true);
        oleLoanForm.setBackGroundCheckIn(false);
        oleLoanForm.setRemoveMissingPieceFlag(false);
        oleLoanForm.setRecordDamagedItemNote(false);
        oleLoanForm.setRecordMissingPieceNote(false);
        oleLoanForm.setRecordCheckoutMissingPieceNote(false);
        oleLoanForm.setDisplayRecordNotePopup(false);
        oleLoanForm.setCheckoutRecordFlag(false);
        oleLoanForm.setSkipMissingPieceRecordPopup(false);
        oleLoanForm.setSkipDamagedRecordPopup(false);
        oleLoanForm.setDisplayMissingPieceNotePopup(false);
        oleLoanForm.setCheckoutMissingPieceRecordFlag(false);
        oleLoanForm.setDisplayDamagedRecordNotePopup(false);
        oleLoanForm.setCheckoutDamagedRecordFlag(false);
        oleLoanForm.setPatronbill(false);
        oleLoanForm.setSuccessMessage(null);
        oleLoanForm.setPopDateTimeInfo("");
    }

}
