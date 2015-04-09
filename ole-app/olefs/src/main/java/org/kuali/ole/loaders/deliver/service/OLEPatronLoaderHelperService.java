package org.kuali.ole.loaders.deliver.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEPatronLoaderHelperService {

    public OlePatronDocument getPatronById(String patronId);

    public OlePatronDocument getPatronByCode(String patronCode);

    public List<OlePatronDocument> getAllPatrons();

    public Object formPatronExportResponse(Object object, String patronContext, String uri, boolean addContext);

    public Object formAllPatronExportResponse(HttpContext context, List<OlePatronDocument> oleInstancePatronList, String patronContext, String uri);

   // public OLELoaderResponseBo updateOlePatronDocument(OlePatronDocument oleInstancePatron, OLEPatronBo oleInstancePatronBo, HttpContext context);
}
