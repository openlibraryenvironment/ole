package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.describe.bo.OLEItemTypeBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEItemTypeLoaderHelperService {

    public OleInstanceItemType getItemTypeById(String itemTypeId);

    public OleInstanceItemType getItemTypeByCode(String itemTypeCode);

    public List<OleInstanceItemType> getAllItemTypes();

    public Object formOleInstanceItemTypeExportResponse(Object object, String itemTypeContext, String uri, boolean addContext);

    public Object formAllItemTypeExportResponse(HttpContext context, List<OleInstanceItemType> oleInstanceItemTypeList, String itemTypeContext, String uri);

    public OLELoaderResponseBo updateOleInstanceItemType(OleInstanceItemType oleInstanceItemType, OLEItemTypeBo oleInstanceItemTypeBo, HttpContext context);
}
