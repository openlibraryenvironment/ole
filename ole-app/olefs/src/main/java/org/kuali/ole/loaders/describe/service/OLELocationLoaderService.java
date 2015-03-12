package org.kuali.ole.loaders.describe.service;


import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.describe.bo.OLELocationBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 2/4/15.
 */
public interface OLELocationLoaderService {

    public Object importLocations(String bodyContent, HttpContext context);

    public OLELoaderResponseBo updateLocationById(String locationId, String bodyContent, HttpContext context);

    public Object exportLocationById(String locationId);

    public Object exportLocationByCode(String locationCode);

    public List<OleLocation> exportAllLocations(HttpContext context);

    public boolean validateLocationBo(OLELocationBo oleLocationBo);

    public Object exportLocationLevelById(String locationLevelId);

    public Object exportLocationLevelByCode(String locationLevelCode);



}
