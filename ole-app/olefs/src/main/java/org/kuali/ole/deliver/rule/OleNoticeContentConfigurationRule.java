package org.kuali.ole.deliver.rule;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 10/27/15.
 */
public class OleNoticeContentConfigurationRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = (OleNoticeContentConfigurationBo) document.getNewMaintainableObject().getDataObject();
        isValid &= validateNoticeName(oleNoticeContentConfigurationBo);
        return isValid;
    }

    /**
     * This method validates duplicate Notice Name and return boolean value.
     *
     * @param oleNoticeContentConfigurationBo
     * @return boolean
     */
    private boolean validateNoticeName(OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        if (StringUtils.isNotBlank(oleNoticeContentConfigurationBo.getNoticeName())) {
            Map<String, String> criteria = new HashMap<>();
            criteria.put(OLEConstants.OleNoticeContentConfigurationRule.NOTICE_NAME, oleNoticeContentConfigurationBo.getNoticeName());
            List<OleNoticeContentConfigurationBo> noticeContentConfigurationBoList = (List<OleNoticeContentConfigurationBo>) getBoService().findMatching(OleNoticeContentConfigurationBo.class, criteria);
            if ((noticeContentConfigurationBoList.size() > 0)) {
                for (OleNoticeContentConfigurationBo noticeContentConfigurationBo : noticeContentConfigurationBoList) {
                    String noticeContentConfigurationId = noticeContentConfigurationBo.getOleNoticeContentConfigurationId();
                    if (null == oleNoticeContentConfigurationBo.getOleNoticeContentConfigurationId() || (!oleNoticeContentConfigurationBo.getOleNoticeContentConfigurationId().equalsIgnoreCase(noticeContentConfigurationId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleNoticeContentConfigurationRule.NOTICE_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
