package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.describe.bo.OLEStatisticalSearchingCodeBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEStatisticalSearchingCodeLoaderHelperService {

    public OleStatisticalSearchingCodes getStatisticalSearchingCodeById(String statisticalSearchingCodeId);

    public OleStatisticalSearchingCodes getStatisticalSearchingCodeByCode(String statisticalSearchingCodeCode);

    public List<OleStatisticalSearchingCodes> getAllStatisticalSearchingCode();

    public Object formStatisticalSearchingCodeExportResponse(Object object, String statisticalSearchingCodeContext, String uri, boolean addContext);

    public Object formAllStatisticalSearchingCodeExportResponse(HttpContext context, List<OleStatisticalSearchingCodes> oleStatisticalSearchingCodeList, String statisticalSearchingCodeContext, String uri);

    public OLELoaderResponseBo updateOleStatisticalSearchingCode(OleStatisticalSearchingCodes oleStatisticalSearchingCode, OLEStatisticalSearchingCodeBo oleStatisticalSearchingCodeBo, HttpContext context);
}
