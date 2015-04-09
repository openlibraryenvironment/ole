package org.kuali.ole.loaders.deliver.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public interface OLEPatronLoaderService {

    /*public Object importPatrons(String bodyContent, HttpContext context);

    public OLELoaderResponseBo updatePatronById(String patronId, String bodyContent, HttpContext context);*/

    public Object exportPatronById(String patronId);

    public Object exportPatronByCode(String shelvingSchemeCode);

    public List<OlePatronDocument> exportAllPatrons();
}
