package org.kuali.ole.loaders.common.service;

import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;

import javax.ws.rs.core.Response;

/**
 * Created by sheiksalahudeenm on 2/16/15.
 */
public interface OLELoaderService {

    public OLELoaderResponseBo generateResponse(String code, String message, int statusCode);

    public OLELoaderResponseBo generateResponse(String code, String message, int statusCode,String details);

    public Response returnLoaderAPIResponse(String context, Object object);

}
