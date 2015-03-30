package org.kuali.ole.loaders.deliver.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEBorrowerTypeLoaderService {

    public Object importBorrowerTypes(String bodyContent, HttpContext context);

    public OLELoaderResponseBo updateBorrowerTypeById(String borrowerTypeId, String bodyContent, HttpContext context);

    public Object exportBorrowerTypeById(String borrowerTypeId);

    public Object exportBorrowerTypeByCode(String shelvingSchemeCode);

    public List<OleBorrowerType> exportAllBorrowerTypes();
}
