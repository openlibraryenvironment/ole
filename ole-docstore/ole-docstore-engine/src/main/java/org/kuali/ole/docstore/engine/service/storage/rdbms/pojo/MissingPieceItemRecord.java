package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import java.sql.Timestamp;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by hemalathas on 3/25/15.
 */
public class MissingPieceItemRecord extends PersistableBusinessObjectBase {

    private String itemId;
    private String missingPieceItemId;
    private Timestamp missingPieceDate;
    private String missingPieceCount;
    private String missingPieceFlagNote;
    private String operatorId;
    private String patronBarcode;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Timestamp getMissingPieceDate() {
        return missingPieceDate;
    }

    public void setMissingPieceDate(Timestamp missingPieceDate) {
        this.missingPieceDate = missingPieceDate;
    }

    public String getMissingPieceItemId() {
        return missingPieceItemId;
    }

    public void setMissingPieceItemId(String missingPieceItemId) {
        this.missingPieceItemId = missingPieceItemId;
    }

    public String getMissingPieceCount() {
        return missingPieceCount;
    }

    public void setMissingPieceCount(String missingPieceCount) {
        this.missingPieceCount = missingPieceCount;
    }

    public String getMissingPieceFlagNote() {
        return missingPieceFlagNote;
    }

    public void setMissingPieceFlagNote(String missingPieceFlagNote) {
        this.missingPieceFlagNote = missingPieceFlagNote;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }
}
