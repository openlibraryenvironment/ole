package org.kuali.ole.deliver.notice.valuesFinder;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 10/6/15.
 */
public class OleNoticeItemFieldLabelMappingKeyValuesFinder extends KeyValuesBase {


    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        String noticeType = null;
        if(GlobalVariables.getUserSession().getObjectMap()!=null){
          noticeType = (String)GlobalVariables.getUserSession().getObjectMap().get("noticeType");
        }

        keyValues.add(new ConcreteKeyValue(OLEConstants.TITLE_ITEM_INFORMATION,OLEConstants.TITLE_ITEM_INFORMATION));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_TITLE,OLEConstants.NOTICE_TITLE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_AUTHOR,OLEConstants.NOTICE_AUTHOR));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_COPY_NUMBER,OLEConstants.NOTICE_COPY_NUMBER));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_ENUMERATION,OLEConstants.NOTICE_ENUMERATION));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_CHRONOLOGY,OLEConstants.NOTICE_CHRONOLOGY));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_CALL_NUMBER,OLEConstants.NOTICE_CALL_NUMBER));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_CALL_NUMBER_PREFIX,OLEConstants.NOTICE_CALL_NUMBER_PREFIX));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_ITEM_BARCODE,OLEConstants.NOTICE_ITEM_BARCODE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ITEM_TYPE,OLEConstants.OLEBatchProcess.ITEM_TYPE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.LIBRARY_LOCATION,OLEConstants.LIBRARY_LOCATION));
        keyValues.add(new ConcreteKeyValue(OLEConstants.LIBRARY_SHELVING_LOCATION,OLEConstants.LIBRARY_SHELVING_LOCATION));
        if(StringUtils.isNotEmpty(noticeType) && (noticeType.equals(OLEConstants.RECALL_NOTICE) || noticeType.equals(OLEConstants.ONHOLD_NOTICE) || noticeType.equals(OLEConstants.ONHOLD_EXPIRATION_NOTICE) || noticeType.equals(OLEConstants.REQUEST_EXPIRATION_NOTICE) || noticeType.equals(OLEConstants.ONHOLD_COURTESY_NOTICE))) {
            keyValues.add(new ConcreteKeyValue(OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME,OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME));
            keyValues.add(new ConcreteKeyValue(OLEConstants.CIRCULATION_REPLY_TO_EMAIL,OLEConstants.CIRCULATION_REPLY_TO_EMAIL));
         }
        if(StringUtils.isNotEmpty(noticeType) && noticeType.equals(OLEConstants.RECALL_NOTICE)){
        keyValues.add(new ConcreteKeyValue(OLEConstants.ORIGINAL_DUE_DATE,OLEConstants.ORIGINAL_DUE_DATE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NEW_DUE_DATE,OLEConstants.NEW_DUE_DATE));
        }
        if(StringUtils.isNotEmpty(noticeType) && (noticeType.equals(OLEConstants.ONHOLD_NOTICE) || noticeType.equals(OLEConstants.ONHOLD_EXPIRATION_NOTICE) || noticeType.equals(OLEConstants.ONHOLD_COURTESY_NOTICE))){
            keyValues.add(new ConcreteKeyValue(OLEConstants.ITEM_WILL_BE_HELD_UNTIL,OLEConstants.ITEM_WILL_BE_HELD_UNTIL));
        }

        if(StringUtils.isNotEmpty(noticeType) && (noticeType.equals(OLEConstants.OVERDUE_NOTICE) || noticeType.equals(OLEConstants.COURTESY_NOTICE))){
        keyValues.add(new ConcreteKeyValue(OLEConstants.ITEM_DUE_DATE,OLEConstants.ITEM_DUE_DATE));
        }
        if(StringUtils.isNotEmpty(noticeType) && noticeType.equals(OLEConstants.REQUEST_EXPIRATION_NOTICE)){
        keyValues.add(new ConcreteKeyValue(OLEConstants.HOLD_EXPIRATION_DATE,OLEConstants.HOLD_EXPIRATION_DATE));
        }
        if(StringUtils.isNotEmpty(noticeType) && noticeType.equals(OLEConstants.MISSING_PIECE_NOTICE)){
            keyValues.add(new ConcreteKeyValue(OLEConstants.MISSING_ITEM_CHECK_IN_DATE,OLEConstants.MISSING_ITEM_CHECK_IN_DATE));
            keyValues.add(new ConcreteKeyValue(OLEConstants.MISSING_ITEM_NOTE,OLEConstants.MISSING_ITEM_NOTE));
        }
        if(StringUtils.isNotEmpty(noticeType) && (noticeType.equals(OLEConstants.NOTICE_LOST)
                                              || noticeType.equals(OLEConstants.LOST_ITEM_PROCESSING_FEE_NOTICE)
                                              || noticeType.equals(OLEConstants.OVERDUE_FINE_NOTICE))){
            keyValues.add(new ConcreteKeyValue("Bill Number","Bill Number"));
            keyValues.add(new ConcreteKeyValue(OLEConstants.FEE_TYPE,OLEConstants.FEE_TYPE));
            keyValues.add(new ConcreteKeyValue(OLEConstants.FEE_AMT,OLEConstants.FEE_AMT));
        }

        if(StringUtils.isNotEmpty(noticeType) && (noticeType.equals(OLEConstants.LOST_ITEM_PROCESSING_FEE_NOTICE)
                                              ||  noticeType.equals(OLEConstants.OVERDUE_FINE_NOTICE))){
            keyValues.add(new ConcreteKeyValue(OLEConstants.FINE_ITEM_DUE_DATE,OLEConstants.FINE_ITEM_DUE_DATE));
        }


        if(StringUtils.isNotEmpty(noticeType) && (noticeType.equals(OLEConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE)
                                              || noticeType.equals(OLEConstants.CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE)
                                              || noticeType.equals(OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NOTICE)
                                              || noticeType.equals(OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE)
                                              || noticeType.equals("Claims Returned Not Found Fines Owed Notice"))){
            keyValues.add(new ConcreteKeyValue(OLEConstants.CLAIMS_SEARCH_COUNT,OLEConstants.CLAIMS_SEARCH_COUNT));
        }
        return keyValues;
    }
}
