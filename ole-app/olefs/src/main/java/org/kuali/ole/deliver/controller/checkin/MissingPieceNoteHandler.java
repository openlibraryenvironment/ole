package org.kuali.ole.deliver.controller.checkin;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronNoteType;
import org.kuali.ole.deliver.bo.OlePatronNotes;
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
        String note = getPatronNoteToRecord(OLEConstants.MISSING_PIECE_ITEM_CHECKED_IN_FLAG, missingPieceRecordInfo, itemRecord);
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

    private String getPatronNoteToRecord(String systemParameter, Map<String, Object> missingPieceRecordInfo, ItemRecord itemRecord) {
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

    public void updateMissingPieceRecord(ItemRecord itemRecord, Map<String, Object> missingPieceRecordInfo) {
        int newMissingPieceCount = 0;
        int oldMissingPieceCount = 0;
        Timestamp customDateTime = null;
        Date customDate = (Date) missingPieceRecordInfo.get("customDate");
        String customTime = (String) missingPieceRecordInfo.get("customTime");
        String missingPieceCount = (String) missingPieceRecordInfo.get("missingPieceCount");
        String missingPieceNote = (String) missingPieceRecordInfo.get("missingPieceNote");
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
        MissingPieceItemRecord missingPieceItemRecord = new MissingPieceItemRecord();
        missingPieceItemRecord.setMissingPieceFlagNote(missingPieceNote);
        missingPieceItemRecord.setMissingPieceCount(missingPieceCount);
        missingPieceItemRecord.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
        missingPieceItemRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(itemRecord.getItemId()));
        missingPieceItemRecord.setMissingPieceDate(customDateTime);
        itemRecord.getMissingPieceItemRecordList().add(missingPieceItemRecord);
    }

    public DateTimeService getDateTimeService() {
        return (DateTimeService) SpringContext.getService("dateTimeService");
    }
}
