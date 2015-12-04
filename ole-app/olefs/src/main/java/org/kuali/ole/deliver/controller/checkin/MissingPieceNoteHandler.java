package org.kuali.ole.deliver.controller.checkin;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by angelind on 8/19/15.
 */
public class MissingPieceNoteHandler {

    public void savePatronNoteForMissingPiece(Map<String, Object> missingPieceRecordInfo, OlePatronDocument olePatronDocument, ItemRecord itemRecord) {
        String note = getPatronNoteToRecord(missingPieceRecordInfo);
        Map map = new HashMap();
        if(olePatronDocument != null) {
            OlePatronNotes olePatronNotes = new OlePatronNotes();
            olePatronNotes.setPatronNoteText(note);
            map.clear();
            map.put("patronNoteTypeCode", "GENERAL");
            OlePatronNoteType olePatronNoteType = (OlePatronNoteType) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronNoteType.class, map);
            if(olePatronNoteType != null) {
                olePatronNotes.setPatronNoteTypeId(olePatronNoteType.getPatronNoteTypeId());
                olePatronNotes.setOlePatronId(olePatronDocument.getOlePatronId());
                olePatronNotes.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                olePatronNotes.setNoteCreatedOrUpdatedDate(new Timestamp(new Date().getTime()));
                if(CollectionUtils.isNotEmpty(olePatronDocument.getNotes())) {
                    olePatronDocument.getNotes().add(olePatronNotes);
                } else {
                    List<OlePatronNotes> olePatronNotesList = new ArrayList<>();
                    olePatronNotesList.add(olePatronNotes);
                    olePatronDocument.setNotes(olePatronNotesList);
                }
                KRADServiceLocator.getBusinessObjectService().save(olePatronDocument.getNotes());
            }
        }
    }

    private String getPatronNoteToRecord(Map<String, Object> missingPieceRecordInfo) {
        String systemParameter = (String) missingPieceRecordInfo.get("noteParameter");
        String note = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, systemParameter);
        SimpleDateFormat dateFormat = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE + " " + RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
        String itemBarcode = (String) missingPieceRecordInfo.get("itemBarcode");
        Date customDate = (Date) missingPieceRecordInfo.get("customDate");
        String customTime = (String) missingPieceRecordInfo.get("customTime");
        String selectedCirculationDesk = (String) missingPieceRecordInfo.get("selectedCirculationDesk");
        String missingPieceCount = (String) missingPieceRecordInfo.get("missingPieceCount");
        if(StringUtils.isNotBlank(itemBarcode)) {
            note = note.replace("[0]", itemBarcode);
        }
        if(customDate != null) {
            try {
                Timestamp customDateTime = new CircUtilController().processDateAndTimeForAlterDueDate(customDate, customTime);
                note = note.replace("[1]",dateFormat.format(customDateTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            note = note.replace("[1]",dateFormat.format(getDateTimeService().getCurrentDate()));
        }
        if(StringUtils.isNotBlank(selectedCirculationDesk)) {
            OleCirculationDesk oleCirculationDesk = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleCirculationDesk.class, selectedCirculationDesk);
            if(oleCirculationDesk != null) {
                note = note.replace("[2]", oleCirculationDesk.getCirculationDeskCode());
            }
        }
        int noOfMissingPiece = 0;
        if (StringUtils.isNotBlank(missingPieceCount)) {
            noOfMissingPiece = Integer.parseInt(missingPieceCount);
        }
        note = note.replace("[3]", noOfMissingPiece +"");
        return note;
    }

    public void updateMissingPieceRecord(ItemRecord itemRecord, Map<String, Object> missingPieceRecordInfo, OleLoanDocument oleLoanDocument) {
        int newMissingPieceCount = 0;
        int oldMissingPieceCount = 0;
        Timestamp customDateTime = null;
        Date customDate = (Date) missingPieceRecordInfo.get("customDate");
        String customTime = (String) missingPieceRecordInfo.get("customTime");
        String missingPieceCount = (String) missingPieceRecordInfo.get("missingPieceCount");
        String missingPieceNote = (String) missingPieceRecordInfo.get("missingPieceNote");
        String olePatronId = (String) missingPieceRecordInfo.get("olePatronId");
        String olePatronBarcode = (String) missingPieceRecordInfo.get("olePatronBarcode");
        itemRecord.setMissingPieceFlag(true);
        if(StringUtils.isNotBlank(missingPieceCount)) {
            newMissingPieceCount = Integer.parseInt(missingPieceCount);
        }
        if(StringUtils.isNotBlank(itemRecord.getMissingPiecesCount())) {
            oldMissingPieceCount = Integer.parseInt(itemRecord.getMissingPiecesCount());
        }
        itemRecord.setMissingPiecesCount(oldMissingPieceCount + newMissingPieceCount + "");
        if(customDate != null) {
            try {
                customDateTime = new CircUtilController().processDateAndTimeForAlterDueDate(customDate, customTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            customDateTime = new Timestamp(new Date().getTime());
        }
        itemRecord.setMissingPieceEffectiveDate(customDateTime);
        if(StringUtils.isNotBlank(missingPieceNote)) {
            itemRecord.setMissingPieceFlagNote(missingPieceNote);
        }
        createMissingPieceItemRecord(itemRecord, olePatronId, olePatronBarcode);
        if(null != oleLoanDocument) {
            populateMissingPieceInfoInLoanDocument(itemRecord, oleLoanDocument);
        }
    }

    private void populateMissingPieceInfoInLoanDocument(ItemRecord itemRecord, OleLoanDocument oleLoanDocument) {
        oleLoanDocument.setMissingPieceFlag(itemRecord.isMissingPieceFlag());
        oleLoanDocument.setMissingPiecesCount(itemRecord.getMissingPiecesCount());
        oleLoanDocument.setMissingPieceItemDate(itemRecord.getMissingPieceEffectiveDate());
        oleLoanDocument.setMissingPieceNote(itemRecord.getMissingPieceFlagNote());
    }

    private void createMissingPieceItemRecord(ItemRecord itemRecord, String olePatronId, String olePatronBarcode) {
        MissingPieceItemRecord missingPieceItemRecord = new MissingPieceItemRecord();
        missingPieceItemRecord.setMissingPieceFlagNote(itemRecord.getMissingPieceFlagNote());
        missingPieceItemRecord.setMissingPieceCount(itemRecord.getMissingPiecesCount());
        missingPieceItemRecord.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
        missingPieceItemRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(itemRecord.getItemId()));
        missingPieceItemRecord.setMissingPieceDate(itemRecord.getMissingPieceEffectiveDate());
        missingPieceItemRecord.setPatronId(olePatronId);
        missingPieceItemRecord.setPatronBarcode(olePatronBarcode);
        itemRecord.getMissingPieceItemRecordList().add(missingPieceItemRecord);
    }

    public DateTimeService getDateTimeService() {
        return (DateTimeService) SpringContext.getService("dateTimeService");
    }
}
