package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.describe.bo.OLEBibliographicRecordStatusBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEBibliographicRecordStatusLoaderHelperService {

    public OleBibliographicRecordStatus getBibliographicRecordStatusById(String bibliographicRecordStatusId);

    public OleBibliographicRecordStatus getBibliographicRecordStatusByCode(String bibliographicRecordStatusCode);

    public List<OleBibliographicRecordStatus> getAllBibliographicRecordStatus();

    public Object formBibliographicRecordStatusExportResponse(Object object, String bibliographicRecordStatusContext, String uri, boolean addContext);

    public Object formAllBibliographicRecordStatusExportResponse(HttpContext context, List<OleBibliographicRecordStatus> oleBibliographicRecordStatusList, String bibliographicRecordStatusContext, String uri);

    public OLELoaderResponseBo updateOleBibliographicRecordStatus(OleBibliographicRecordStatus oleBibliographicRecordStatus, OLEBibliographicRecordStatusBo oleBibliographicRecordStatusBo, HttpContext context);
}
