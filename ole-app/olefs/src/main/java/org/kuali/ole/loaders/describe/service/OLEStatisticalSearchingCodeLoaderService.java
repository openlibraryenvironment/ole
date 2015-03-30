package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEStatisticalSearchingCodeLoaderService {

    public Object importStatisticalSearchingCode(String bodyContent, HttpContext context);

    public OLELoaderResponseBo updateStatisticalSearchingCodeById(String statisticalSearchingCodeId, String bodyContent, HttpContext context);

    public Object exportStatisticalSearchingCodeById(String statisticalSearchingCodeId);

    public Object exportStatisticalSearchingCodeByCode(String shelvingSchemeCode);

    public List<OleStatisticalSearchingCodes> exportAllStatisticalSearchingCode();
}
