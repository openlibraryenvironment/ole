package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AuthenticationTypeRecord;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pvsubrah on 1/4/16.
 */
public class AuthenticationTypeUtil extends BusinessObjectServiceHelperUtil {
    public AuthenticationTypeRecord fetchAuthenticationTypeRecordByCode(String authenticationType) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", authenticationType);
        List<AuthenticationTypeRecord> authenticationTypeRecords = (List<AuthenticationTypeRecord>) getBusinessObjectService().findMatching(AuthenticationTypeRecord.class, map);
        if(CollectionUtils.isNotEmpty(authenticationTypeRecords)) {
            return authenticationTypeRecords.get(0);
        }
        return null;
    }
}
