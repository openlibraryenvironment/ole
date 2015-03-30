package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.describe.bo.OLEShelvingSchemeBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEShelvingSchemeLoaderHelperService{

    public OleShelvingScheme getShelvingSchemeById(String shelvingSchemeId);

    public OleShelvingScheme getShelvingSchemeByCode(String shelvingSchemeCode);

    public List<OleShelvingScheme> getAllShelvingSchemes();

    public Object formOleShelvingSchemeExportResponse(Object object, String shelvingSchemeContext, String uri,boolean addContext);

    public Object formAllShelvingSchemeExportResponse(HttpContext context, List<OleShelvingScheme> oleShelvingSchemeList, String shelvingSchemeContext, String uri);

    public OLELoaderResponseBo updateOleShelvingScheme(OleShelvingScheme oleShelvingScheme, OLEShelvingSchemeBo oleShelvingSchemeBo,HttpContext context);
}
