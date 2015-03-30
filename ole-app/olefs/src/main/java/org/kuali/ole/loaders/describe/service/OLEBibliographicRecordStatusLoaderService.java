package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEBibliographicRecordStatusLoaderService {

    public Object importBibliographicRecordStatus(String bodyContent, HttpContext context);

    public OLELoaderResponseBo updateBibliographicRecordStatusById(String bibliographicRecordStatusId, String bodyContent, HttpContext context);

    public Object exportBibliographicRecordStatusById(String bibliographicRecordStatusId);

    public Object exportBibliographicRecordStatusByCode(String shelvingSchemeCode);

    public List<OleBibliographicRecordStatus> exportAllBibliographicRecordStatus();
}
