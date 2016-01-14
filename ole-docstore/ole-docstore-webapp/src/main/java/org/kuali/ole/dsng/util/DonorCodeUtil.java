package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.OLEItemDonorRecord;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pvsubrah on 1/4/16.
 */
public class DonorCodeUtil extends BusinessObjectServiceHelperUtil {

    public OLEItemDonorRecord fetchDonorCodeByCode(String donorCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("donorCode", donorCode);
        List<OLEItemDonorRecord> matching = (List<OLEItemDonorRecord>) getBusinessObjectService().findMatching(OLEItemDonorRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    public OLEItemDonorRecord fetchDonorCodeByPublicDisplay(String publicDisplay) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("donorPublicDisplay", publicDisplay);
        List<OLEItemDonorRecord> matching = (List<OLEItemDonorRecord>) getBusinessObjectService().findMatching(OLEItemDonorRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }
}
