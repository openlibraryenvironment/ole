package org.kuali.ole.utility;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemTypeRecord;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pvsubrah on 1/4/16.
 */
public class ItemUtil extends BusinessObjectServiceHelperUtil {
    public ItemStatusRecord fetchItemStatusByName(String itemStatusTypeName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", itemStatusTypeName);
        List<ItemStatusRecord> matching = (List<ItemStatusRecord>) getBusinessObjectService().findMatching(ItemStatusRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    public ItemTypeRecord fetchItemTypeByName(String itemTypeName) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", itemTypeName);
        List<ItemTypeRecord> matching = (List<ItemTypeRecord>) getBusinessObjectService().findMatching(ItemTypeRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }

    public ItemTypeRecord fetchItemTypeByCode(String itemTypeCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", itemTypeCode);
        List<ItemTypeRecord> matching = (List<ItemTypeRecord>) getBusinessObjectService().findMatching(ItemTypeRecord.class, map);
        if(CollectionUtils.isNotEmpty(matching)) {
            return matching.get(0);
        }
        return null;
    }
}
