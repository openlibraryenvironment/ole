package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pvsubrah on 6/4/15.
 */
public class CheckoutUIController extends CheckoutBaseController {
    private static final Logger LOG = Logger.getLogger(CheckoutUIController.class);

    @Override
    protected void setDataElements(OLEForm oleForm, OleItemRecordForCirc oleItemRecordForCirc) {
        CircForm circForm = getCircForm(oleForm);
        circForm.getDroolsExchange().addToContext("oleItemRecordForCirc", oleItemRecordForCirc);
        circForm.getDroolsExchange().addToContext("circForm", circForm);
    }

    @Override
    public CircForm getCircForm(OLEForm oleForm){
        return (CircForm) oleForm;
    }

    @Override
    public ItemRecord getItemRecord(OLEForm oleForm) {
        return getCircForm(oleForm).getItemRecord();
    }

    @Override
    public String getOperatorId(OLEForm oleForm) {
        return getLoginUserId();
    }

    @Override
    public void setItemRecord(OLEForm oleForm, ItemRecord itemRecord) {
        getCircForm(oleForm).setItemRecord(itemRecord);
    }

    @Override
    public String getItemBarcode(OLEForm oleForm) {
        return getCircForm(oleForm).getItemBarcode();
    }

    @Override
    public void setItemBarcode(OLEForm oleForm, String itemBarcode) {
        getCircForm(oleForm).setItemBarcode(itemBarcode);
    }

    @Override
    public OlePatronDocument getCurrentBorrower(OLEForm oleForm) {
        return getCircForm(oleForm).getPatronDocument();
    }

    @Override
    public void setItemValidationDone(boolean result, OLEForm oleForm) {
        getCircForm(oleForm).setItemValidationDone(result);
    }

    @Override
    public OleCirculationDesk getSelectedCirculationDesk(OLEForm oleForm) {
        CircForm circForm =(CircForm)oleForm;
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(circForm.getSelectedCirculationDesk());
        return oleCirculationDesk;
    }

    @Override
    public void addLoanDocumentToCurrentSession(OleLoanDocument oleLoanDocument, OLEForm oleForm) {
        getCircForm(oleForm).getLoanDocumentListForCurrentSession().add(0,oleLoanDocument);
    }

    @Override
    public boolean processCustomDueDateIfSet(OLEForm oleForm, OleLoanDocument oleLoanDocument) {
        if (null != getCircForm(oleForm).getCustomDueDateMap()) {
            try {
                processCustomDueDate(oleForm, oleLoanDocument);
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }

    private void processCustomDueDate(OLEForm oleForm, OleLoanDocument oleLoanDocument) throws Exception {
        boolean timeFlag = false;
        Timestamp timestamp;
        Pattern pattern;
        Matcher matcher;
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);

        String customDueDateTime = getCircForm(oleForm).getCustomDueDateTime();
        Date customDueDateMap = getCircForm(oleForm).getCustomDueDateMap();

        if (StringUtils.isNotBlank(customDueDateTime)) {
            String[] str = customDueDateTime.split(":");
            pattern = Pattern.compile(OLEConstants.TIME_24_HR_PATTERN);
            matcher = pattern.matcher(customDueDateTime);
            timeFlag = matcher.matches();
            if (timeFlag) {
                if (str != null && str.length <= 2) {
                    getCircForm(oleForm).setCustomDueDateTime(customDueDateTime + OLEConstants.CHECK_OUT_DUE_TIME_MS);
                }
                timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(customDueDateMap).concat(" ").concat(getCircForm(oleForm).getCustomDueDateTime()));
            } else {
                getCircForm(oleForm).setCustomDueDateTimeMessage(OLEConstants.DUE_DATE_TIME_FORMAT_MESSAGE);
                throw new Exception();
            }
        } else if (fmt.format(customDueDateMap).compareTo(fmt.format(new Date())) == 0) {
            timestamp = new Timestamp(new Date().getTime());
        } else {
            String closingTime = getDefaultClosingTime(oleLoanDocument,customDueDateMap);
            if(StringUtils.isNotBlank(closingTime)) {
                timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(customDueDateMap).concat(" ").concat(closingTime).concat(":00"));
            }else{
                timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(customDueDateMap).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
            }
        }
        oleLoanDocument.setLoanDueDate(timestamp);
        oleLoanDocument.setCirculationPolicyId(OLEConstants.NO_CIRC_POLICY_FOUND);
    }

    @Override
    public void setDueDateTimeForItemRecord(OLEForm oleForm, Timestamp loanDueDate) {
        getItemRecord(oleForm).setDueDateTime(loanDueDate);
    }

    @Override
    public void addCurrentLoandDocumentToListofLoandedToPatron(OLEForm oleForm, OleLoanDocument oleLoanDocument) {
        getCircForm(oleForm).getPatronDocument().getOleLoanDocuments().add(oleLoanDocument);
    }

    @Override
    public void removeCurrentLoanDocumentFromCurrentSession(OLEForm oleForm, OleLoanDocument oleLoanDocument) {
        getCircForm(oleForm).getLoanDocumentListForCurrentSession().remove(oleLoanDocument);
    }

    @Override
    public void clearCurrentSessionList(OLEForm oleForm) {
        getCircForm(oleForm).getLoanDocumentListForCurrentSession().clear();
    }

    @Override
    public String getCirculationLocationId(OLEForm oleForm) {
        return getCircForm(oleForm).getSelectedCirculationDesk();
    }

    @Override
    public boolean isRecordNoteForDamagedItem(OLEForm oleForm) {
        return getCircForm(oleForm).isRecordNoteForDamagedItem();
    }

    @Override
    public boolean isRecordNoteForClaimsReturn(OLEForm oleForm) {
        return getCircForm(oleForm).isRecordNoteForClaimsReturn();
    }

    @Override
    public boolean isRecordNoteForMissingPiece(OLEForm oleForm) {
        return getCircForm(oleForm).isRecordNoteForMissingPiece();
    }

    @Override
    public String getMissingPieceMatchCheck(OLEForm oleForm) {
        return getCircForm(oleForm).getMissingPieceMatchCheck();
    }

}
