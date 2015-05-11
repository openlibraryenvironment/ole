package org.kuali.ole.loaders.deliver.service;

import com.sun.jersey.api.core.HttpContext;
import org.codehaus.jettison.json.JSONArray;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.ingest.pojo.OlePatron;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEPatronLoaderHelperService {

    public OlePatronDocument getPatronById(String patronId);

    public OlePatronDocument getPatronByCode(String patronCode);

    public List<OlePatronDocument> getAllPatrons();

    public Object formPatronExportResponse(Object object, String patronContext, String uri, boolean addContext);

    public Object formAllPatronExportResponse(HttpContext context, List<OlePatronDocument> oleInstancePatronList, String patronContext, String uri);

    public List<OlePatron> formIngestOlePatrons(JSONArray patronJsonArray,Map<String,Integer> rejectedPatronBarcodeIndexMap, Map<String,Integer> selectedPatronBarcodeIndexMap);

    /*public OlePatron populateIngestOlePatronEmail(OLEPatronBo olePatronBo, OlePatron olePatron);

    public OlePatron populateIngestOlePatronName(OLEPatronBo olePatronBo, OlePatron olePatron);

    public OlePatron populateIngestOlePatronAddress(OLEPatronBo olePatronBo, OlePatron olePatron);

    public OlePatron populateIngestOlePatronPhone(OLEPatronBo olePatronBo, OlePatron olePatron);

    public OlePatron populateIngestOlePatronAffiliations(OLEPatronBo olePatronBo,  OlePatron olePatron);

    public OlePatron populateIngestOlePatronNotes(OLEPatronBo olePatronBo, OlePatron olePatron);*/

   // public OLELoaderResponseBo updateOlePatronDocument(OlePatronDocument oleInstancePatron, OLEPatronBo oleInstancePatronBo, HttpContext context);
}
