package org.kuali.ole.loaders.common.processor;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.common.document.content.bib.marc.Collection;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.describe.bo.*;
import org.kuali.ole.loaders.describe.service.OLELocationLoaderHelperService;
import org.kuali.ole.loaders.describe.service.OLELocationLoaderService;
import org.kuali.ole.loaders.describe.service.impl.OLELocationLoaderHelperServiceImpl;
import org.kuali.ole.loaders.describe.service.impl.OLELocationLoaderServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 2/17/15.
 */
@Path("/")
public class OLELoaderApiProcessor {

    private static final Logger LOG = Logger.getLogger(OLELoaderApiProcessor.class);
    OLELocationLoaderService oleLocationLoaderService = new OLELocationLoaderServiceImpl();
    OLELocationLoaderHelperService oleLocationLoaderHelperService = new OLELocationLoaderHelperServiceImpl();
    OLELoaderService oleLoaderService = new OLELoaderServiceImpl();

    @GET
    @Path("/location/{pk}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportLocation(@Context HttpContext context, @PathParam("pk") String id){
        Object object  = null;
        if(id.matches("^([\\d]*)?$")){
            object = oleLocationLoaderService.exportLocationById(id);
        }else{
            object = oleLocationLoaderService.exportLocationByCode(id);
        }
        if(object instanceof OleLocation){
            object = oleLocationLoaderHelperService.formLocationExportResponse(object,OLELoaderConstants.OLELoaderContext.LOCATION,context.getRequest().getRequestUri().toASCIIString(),true);
            return  Response.status(200).entity(object).build();
        }else if(object instanceof  OLELoaderResponseBo){
            return Response.status(((OLELoaderResponseBo) object).getStatusCode()).entity(((OLELoaderResponseBo) object).getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        return null;
    }

    @GET
    @Path("/location")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportAllLocations(@Context HttpContext context){
        List<OleLocation> oleLocationList = oleLocationLoaderService.exportAllLocations(context);
        if(CollectionUtils.isNotEmpty(oleLocationList)){
            Object object = oleLocationLoaderHelperService.formAllLocationExportResponse(context, oleLocationList, OLELoaderConstants.OLELoaderContext.LOCATION,
                    context.getRequest().getRequestUri().toASCIIString());
            return Response.status(200).entity(object).build();
        }
        return Response.status(500).entity(null).build();
    }

    @POST
    @Path("/location")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response importLocations(@Context HttpContext context, String bodyContent){
        LOG.info("Incoming request for Import Locations.");
        Object object = oleLocationLoaderService.importLocations(bodyContent,context);
        if(object instanceof OLELoaderImportResponseBo){
            if(CollectionUtils.isNotEmpty(((OLELoaderImportResponseBo) object).getOleRejectedBos())){
                String rejectedBosIndexs = "";
                int count = 1;
                for(Integer index : ((OLELoaderImportResponseBo) object).getOleRejectedBos()){
                    if(count == 1){
                        rejectedBosIndexs = String.valueOf(index);
                        count++;
                    }else{
                        rejectedBosIndexs += ","+String.valueOf(index);
                    }
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("@context",OLELoaderConstants.OLELoaderContext.LOCATION);
                    jsonObject.put("items",((OLELoaderImportResponseBo) object).getOleCreatedBos());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(CollectionUtils.isNotEmpty(((OLELoaderImportResponseBo) object).getOleRejectedBos()) && CollectionUtils.isEmpty(((OLELoaderImportResponseBo) object).getOleCreatedBos())){
                    return Response.status(400).entity("").build();
                }else{
                    return Response.status(200).entity(jsonObject).header("X-OLE-Rejected", rejectedBosIndexs).build();
                }
            }else{
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("@context",OLELoaderConstants.OLELoaderContext.LOCATION);
                    jsonObject.put("items",((OLELoaderImportResponseBo) object).getOleCreatedBos());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Response.status(201).entity(jsonObject).build();
            }
        }
        return null;
    }

    @PUT
    @Path("/location/{locationId}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response updateLocation(@Context HttpContext context, @PathParam("locationId") String locationId, String body){
        LOG.info("Incoming request for update Location.");
        OLELoaderResponseBo object = oleLocationLoaderService.updateLocationById(locationId, body, context);
        if(object.getStatusCode() == 200){
            return  Response.status(object.getStatusCode()).entity(object.getDetails()).build();
        }else{
            return  Response.status(400).entity(object.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }

    }

    @GET
    @Path("/locationLevel/{pk}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportLocationLevel(@Context HttpContext context, @PathParam("pk") String id){
        Object object  = null;
        if(id.matches("^([\\d]*)?$")){
            object = oleLocationLoaderService.exportLocationLevelById(id);
        }else{
            object = oleLocationLoaderService.exportLocationLevelByCode(id);
        }
        if(object instanceof OleLocationLevel){
            object = oleLocationLoaderHelperService.formLocationLevelExportResponse(object, OLELoaderConstants.OLELoaderContext.LOCATION_LEVEL, context.getRequest().getRequestUri().toASCIIString());
            return  Response.status(200).entity(object).build();
        }else if(object instanceof  OLELoaderResponseBo){
            return Response.status(((OLELoaderResponseBo) object).getStatusCode()).entity(((OLELoaderResponseBo) object).getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        return null;
    }


}
