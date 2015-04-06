package org.kuali.ole.loaders.describe.service;

import com.sun.jersey.api.core.HttpContext;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.describe.bo.OLELocationBo;
import org.kuali.ole.loaders.describe.bo.OLELocationLevelBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 2/4/15.
 */
public interface OLELocationLoaderHelperService {

    public boolean isLocationLevelExistById(String locationLevelId);

    public boolean isParentLocationLevelExist(String parentLocationLevelId);

    public boolean isParentLocationExist(String parentLocationId);

    public boolean isLocationExistByCode(String locationCode);

    public OleLocation createOleLocation(OLELocationBo oleLocationBo);

    public OLELoaderResponseBo updateOleLocation(OleLocation oleLocation, OLELocationBo oleLocationBo,HttpContext context);

    public OleLocation getLocationById(String locationId);

    public OleLocation getLocationByCode(String locationId);

    public List<OleLocation> getAllLocation();

    public Object formLocationExportResponse(Object object, String locationContext, String uri, boolean importContext);

    public Object formAllLocationExportResponse(HttpContext context, List<OleLocation> oleLocationList, String locationContext, String uri);

    public OleLocationLevel getLocationLevelById(String locationLevelId);

    public OleLocationLevel getLocationLevelByCode(String locationLevelCode);

    public List<OleLocationLevel> getAllLocationLevel();

    public Object formLocationLevelExportResponse(Object object, String locationContext, String uri,boolean addContext);

    public Object formAllLocationLevelExportResponse(HttpContext context, List<OleLocationLevel> oleLocationLevelList, String locationContext, String uri);

    public OLELoaderResponseBo updateOleLocationLevel(OleLocationLevel oleLocationLevel, OLELocationLevelBo oleLocationLevelBo,HttpContext context);


}
