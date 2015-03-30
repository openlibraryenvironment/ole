package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEItemTypeLoaderService {

    public Object importItemTypes(String bodyContent, HttpContext context);

    public OLELoaderResponseBo updateItemTypeById(String itemTypeId, String bodyContent, HttpContext context);

    public Object exportItemTypeById(String itemTypeId);

    public Object exportItemTypeByCode(String shelvingSchemeCode);

    public List<OleInstanceItemType> exportAllItemTypes();
}
