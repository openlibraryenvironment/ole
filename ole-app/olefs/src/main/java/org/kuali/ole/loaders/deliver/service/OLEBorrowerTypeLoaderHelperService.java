package org.kuali.ole.loaders.deliver.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.deliver.bo.OLEBorrowerTypeBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEBorrowerTypeLoaderHelperService {

    public OleBorrowerType getBorrowerTypeById(String borrowerTypeId);

    public OleBorrowerType getBorrowerTypeByCode(String borrowerTypeCode);

    public List<OleBorrowerType> getAllBorrowerTypes();

    public Object formOleBorrowerTypeExportResponse(Object object, String borrowerTypeContext, String uri, boolean addContext);

    public Object formAllBorrowerTypeExportResponse(HttpContext context, List<OleBorrowerType> oleInstanceBorrowerTypeList, String borrowerTypeContext, String uri);

    public OLELoaderResponseBo updateOleBorrowerType(OleBorrowerType oleInstanceBorrowerType, OLEBorrowerTypeBo oleInstanceBorrowerTypeBo, HttpContext context);
}
