package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEShelvingSchemeLoaderService {

    public Object importShelvingSchemes(String bodyContent, HttpContext context);

    public OLELoaderResponseBo updateShelvingSchemeById(String shelvingSchemeId, String bodyContent, HttpContext context);

    public Object exportShelvingSchemeById(String shelvingSchemeId);

    public Object exportShelvingSchemeByCode(String shelvingSchemeCode);

    public List<OleShelvingScheme> exportAllShelvingSchemes();
}
