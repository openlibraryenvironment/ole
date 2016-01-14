package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AccessLocation;

import java.util.HashMap;
import java.util.List;

/**
 * Created by SheikS on 1/6/2016.
 */
public class AccessLocationUtil extends BusinessObjectServiceHelperUtil {

    public AccessLocation fetchAccessLocationByCode(String accessLocationCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", accessLocationCode);
        List<AccessLocation> matching = (List<AccessLocation>) getBusinessObjectService().findMatching(AccessLocation.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }
}
