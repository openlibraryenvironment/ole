package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEItemAvailableStatusLoaderService {

    public Object importItemAvailableStatus(String bodyContent, HttpContext context);

    public OLELoaderResponseBo updateItemAvailableStatusById(String itemAvailableStatusId, String bodyContent, HttpContext context);

    public Object exportItemAvailableStatusById(String itemAvailableStatusId);

    public Object exportItemAvailableStatusByCode(String shelvingSchemeCode);

    public List<OleItemAvailableStatus> exportAllItemAvailableStatus();
}
