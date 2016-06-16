package org.kuali.ole.dsng.rest.handler.bib;

import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.dsng.rest.handler.AdditionalOverlayOpsHandler;

import java.util.List;

/**
 * Created by SheikS on 3/21/2016.
 */
public class BibStatusOverlayOpsHandler extends AdditionalOverlayOpsHandler{
    @Override
    public Boolean isInterested(String type) {
        return OleNGConstants.BIB_STATUS.equalsIgnoreCase(type);
    }

    @Override
    public boolean isValid(String condition, List<String> values, Object object) {
        boolean isValid = true;
        BibRecord bibRecord = (BibRecord) object;
        if(OleNGConstants.EQUAL_TO.equalsIgnoreCase(condition)) {
            if(!values.contains(bibRecord.getStatus())) {
                isValid = false;
            }
        } else if(OleNGConstants.NOT_EQUAL_TO.equalsIgnoreCase(condition)) {
            if(values.contains(bibRecord.getStatus())) {
                isValid = false;
            }
        }
        return isValid;

    }
}
