package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.describe.bo.OLEItemAvailableStatusBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEItemAvailableStatusLoaderHelperService {

    public OleItemAvailableStatus getItemAvailableStatusById(String itemAvailableStatusId);

    public OleItemAvailableStatus getItemAvailableStatusByCode(String itemAvailableStatusCode);

    public List<OleItemAvailableStatus> getAllItemAvailableStatus();

    public Object formItemAvailableStatusExportResponse(Object object, String itemAvailableStatusContext, String uri, boolean addContext);

    public Object formAllItemAvailableStatusExportResponse(HttpContext context, List<OleItemAvailableStatus> oleItemAvailableStatusList, String itemAvailableStatusContext, String uri);

    public OLELoaderResponseBo updateOleItemAvailableStatus(OleItemAvailableStatus oleItemAvailableStatus, OLEItemAvailableStatusBo oleItemAvailableStatusBo, HttpContext context);
}
