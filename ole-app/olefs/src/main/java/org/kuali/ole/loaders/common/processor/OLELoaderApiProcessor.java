package org.kuali.ole.loaders.common.processor;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.ole.describe.bo.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.Collection;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.deliver.service.OLEBorrowerTypeLoaderHelperService;
import org.kuali.ole.loaders.deliver.service.OLEBorrowerTypeLoaderService;
import org.kuali.ole.loaders.describe.bo.*;
import org.kuali.ole.loaders.describe.service.*;
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
    OLELoaderService oleLoaderService;

    public OLELoaderService getOleLoaderService() {
        if(oleLoaderService == null){
            oleLoaderService = new OLELoaderServiceImpl();
        }
        return oleLoaderService;
    }

    public void setOleLoaderService(OLELoaderService oleLoaderService) {
        this.oleLoaderService = oleLoaderService;
    }

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
        return getOleLoaderService().formResponseForImport(object,OLELoaderConstants.OLELoaderContext.LOCATION);
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
            object = oleLocationLoaderHelperService.formLocationLevelExportResponse(object, OLELoaderConstants.OLELoaderContext.LOCATION_LEVEL, context.getRequest().getRequestUri().toASCIIString(),true);
            return  Response.status(200).entity(object).build();
        }else if(object instanceof  OLELoaderResponseBo){
            return Response.status(((OLELoaderResponseBo) object).getStatusCode()).entity(((OLELoaderResponseBo) object).getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        return null;
    }

    @POST
    @Path("/locationLevel")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response importLocationLevels(@Context HttpContext context, String bodyContent){
        LOG.info("Incoming request for Import Locations Level.");
        Object object = oleLocationLoaderService.importLocationLevels(bodyContent,context);
        return getOleLoaderService().formResponseForImport(object,OLELoaderConstants.OLELoaderContext.LOCATION_LEVEL);
    }

    @PUT
    @Path("/locationLevel/{locationLevelId}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response updateLocationLevel(@Context HttpContext context, @PathParam("locationLevelId") String locationLevelId, String body){
        LOG.info("Incoming request for update Location Level.");
        OLELoaderResponseBo object = oleLocationLoaderService.updateLocationLevelById(locationLevelId, body, context);
        if(object.getStatusCode() == 200){
            return  Response.status(object.getStatusCode()).entity(object.getDetails()).build();
        }else{
            return  Response.status(400).entity(object.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }

    }



    @GET
    @Path("/shelvingScheme/{pk}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportShelvingScheme(@Context HttpContext context, @PathParam("pk") String id){
        LOG.info("Incoming request for exportShelvingScheme.");
        Object object  = null;
        OLEShelvingSchemeLoaderService oleShelvingSchemeLoaderService = (OLEShelvingSchemeLoaderService) getOleLoaderService().getLoaderService("schelingScheme");
        OLEShelvingSchemeLoaderHelperService oleShelvingSchemeLoaderHelperService = (OLEShelvingSchemeLoaderHelperService) getOleLoaderService().getLoaderHelperService("shelvingScheme");
        if(id.matches("^([\\d]*)?$")){
            object = oleShelvingSchemeLoaderService.exportShelvingSchemeById(id);
        }else{
            object = oleShelvingSchemeLoaderService.exportShelvingSchemeById(id);
        }
        if(object instanceof OleShelvingScheme){
            object = oleShelvingSchemeLoaderHelperService.formOleShelvingSchemeExportResponse(object, OLELoaderConstants.OLELoaderContext.SHELVING_SCHEME, context.getRequest().getRequestUri().toASCIIString(), true);
            return  Response.status(200).entity(object).build();
        }else if(object instanceof  OLELoaderResponseBo){
            return Response.status(((OLELoaderResponseBo) object).getStatusCode()).entity(((OLELoaderResponseBo) object).getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        return null;
    }

    @GET
    @Path("/shelvingScheme")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportAllShelvingSchemes(@Context HttpContext context){
        LOG.info("Incoming request for exportAllShelvingSchemes.");
        OLEShelvingSchemeLoaderService oleShelvingSchemeLoaderService = (OLEShelvingSchemeLoaderService) getOleLoaderService().getLoaderService("schelingScheme");
        OLEShelvingSchemeLoaderHelperService oleShelvingSchemeLoaderHelperService = (OLEShelvingSchemeLoaderHelperService) getOleLoaderService().getLoaderHelperService("shelvingScheme");
        List<OleShelvingScheme> oleShelvingSchemeList = oleShelvingSchemeLoaderService.exportAllShelvingSchemes();
        if(CollectionUtils.isNotEmpty(oleShelvingSchemeList)){
            Object object = oleShelvingSchemeLoaderHelperService.formAllShelvingSchemeExportResponse(context, oleShelvingSchemeList, OLELoaderConstants.OLELoaderContext.SHELVING_SCHEME,
                    context.getRequest().getRequestUri().toASCIIString());
            return Response.status(200).entity(object).build();
        }
        return Response.status(500).entity(null).build();
    }

    @POST
    @Path("/shelvingScheme")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response importShelvingSchemes(@Context HttpContext context, String bodyContent){
        LOG.info("Incoming request for importShelvingSchemes.");
        OLEShelvingSchemeLoaderService oleShelvingSchemeLoaderService = (OLEShelvingSchemeLoaderService) getOleLoaderService().getLoaderService("schelingScheme");
        OLEShelvingSchemeLoaderHelperService oleShelvingSchemeLoaderHelperService = (OLEShelvingSchemeLoaderHelperService) getOleLoaderService().getLoaderHelperService("shelvingScheme");
        Object object = oleShelvingSchemeLoaderService.importShelvingSchemes(bodyContent, context);
        return getOleLoaderService().formResponseForImport(object,OLELoaderConstants.OLELoaderContext.SHELVING_SCHEME);
    }

    @PUT
    @Path("/shelvingScheme/{shelvingSchemeId}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response updateShelvingScheme(@Context HttpContext context, @PathParam("shelvingSchemeId") String shelvingSchemeId, String body){
        LOG.info("Incoming request for updateShelvingScheme.");
        OLEShelvingSchemeLoaderService oleShelvingSchemeLoaderService = (OLEShelvingSchemeLoaderService) getOleLoaderService().getLoaderService("schelingScheme");
        OLELoaderResponseBo object = oleShelvingSchemeLoaderService.updateShelvingSchemeById(shelvingSchemeId, body, context);
        if(object.getStatusCode() == 200){
            return  Response.status(object.getStatusCode()).entity(object.getDetails()).build();
        }else{
            return  Response.status(400).entity(object.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/itemType/{pk}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportItemType(@Context HttpContext context, @PathParam("pk") String id){
        LOG.info("Incoming request for exportItemType.");
        Object object  = null;
        OLEItemTypeLoaderService oleItemTypeLoaderService = (OLEItemTypeLoaderService) getOleLoaderService().getLoaderService("itemType");
        OLEItemTypeLoaderHelperService oleItemTypeLoaderHelperService = (OLEItemTypeLoaderHelperService) getOleLoaderService().getLoaderHelperService("itemType");
        if(id.matches("^([\\d]*)?$")){
            object = oleItemTypeLoaderService.exportItemTypeById(id);
        }else{
            object = oleItemTypeLoaderService.exportItemTypeByCode(id);
        }
        if(object instanceof OleInstanceItemType){
            object = oleItemTypeLoaderHelperService.formOleInstanceItemTypeExportResponse(object, OLELoaderConstants.OLELoaderContext.ITEM_TYPE, context.getRequest().getRequestUri().toASCIIString(), true);
            return  Response.status(200).entity(object).build();
        }else if(object instanceof  OLELoaderResponseBo){
            return Response.status(((OLELoaderResponseBo) object).getStatusCode()).entity(((OLELoaderResponseBo) object).getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        return null;
    }

    @GET
    @Path("/itemType")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportAllItemTypes(@Context HttpContext context){
        LOG.info("Incoming request for exportAllItemTypes.");
        OLEItemTypeLoaderService oleItemTypeLoaderService = (OLEItemTypeLoaderService) getOleLoaderService().getLoaderService("itemType");
        OLEItemTypeLoaderHelperService oleItemTypeLoaderHelperService = (OLEItemTypeLoaderHelperService) getOleLoaderService().getLoaderHelperService("itemType");
        List<OleInstanceItemType> oleInstanceItemTypes= oleItemTypeLoaderService.exportAllItemTypes();
        if(CollectionUtils.isNotEmpty(oleInstanceItemTypes)){
            Object object = oleItemTypeLoaderHelperService.formAllItemTypeExportResponse(context, oleInstanceItemTypes, OLELoaderConstants.OLELoaderContext.ITEM_TYPE,
                    context.getRequest().getRequestUri().toASCIIString());
            return Response.status(200).entity(object).build();
        }
        return Response.status(500).entity(null).build();
    }

    @POST
    @Path("/itemType")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response importItemType(@Context HttpContext context, String bodyContent){
        LOG.info("Incoming request for importItemType.");
        OLEItemTypeLoaderService oleItemTypeLoaderService = (OLEItemTypeLoaderService) getOleLoaderService().getLoaderService("itemType");
        Object object = oleItemTypeLoaderService.importItemTypes(bodyContent, context);
        return getOleLoaderService().formResponseForImport(object,OLELoaderConstants.OLELoaderContext.ITEM_TYPE);
    }

    @PUT
    @Path("/itemType/{itemTypeId}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response updateItemType(@Context HttpContext context, @PathParam("itemTypeId") String itemTypeId, String body){
        LOG.info("Incoming request for updateItemType.");
        OLEItemTypeLoaderService oleItemTypeLoaderService = (OLEItemTypeLoaderService) getOleLoaderService().getLoaderService("itemType");
        OLELoaderResponseBo object = oleItemTypeLoaderService.updateItemTypeById(itemTypeId, body, context);
        if(object.getStatusCode() == 200){
            return  Response.status(object.getStatusCode()).entity(object.getDetails()).build();
        }else{
            return  Response.status(400).entity(object.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/borrowerType/{pk}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportBorrowerType(@Context HttpContext context, @PathParam("pk") String id){
        LOG.info("Incoming request for exportBorrowerType.");
        Object object  = null;
        OLEBorrowerTypeLoaderService oleBorrowerTypeLoaderService = (OLEBorrowerTypeLoaderService) getOleLoaderService().getLoaderService("borrowerType");
        OLEBorrowerTypeLoaderHelperService oleBorrowerTypeLoaderHelperService = (OLEBorrowerTypeLoaderHelperService) getOleLoaderService().getLoaderHelperService("borrowerType");
        if(id.matches("^([\\d]*)?$")){
            object = oleBorrowerTypeLoaderService.exportBorrowerTypeById(id);
        }else{
            object = oleBorrowerTypeLoaderService.exportBorrowerTypeByCode(id);
        }
        if(object instanceof OleBorrowerType){
            object = oleBorrowerTypeLoaderHelperService.formOleBorrowerTypeExportResponse(object, OLELoaderConstants.OLELoaderContext.BORROWER_TYPE, context.getRequest().getRequestUri().toASCIIString(), true);
            return  Response.status(200).entity(object).build();
        }else if(object instanceof OLELoaderResponseBo){
            return Response.status(((OLELoaderResponseBo) object).getStatusCode()).entity(((OLELoaderResponseBo) object).getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        return null;
    }

    @GET
    @Path("/borrowerType")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportAllBorrowerTypes(@Context HttpContext context){
        LOG.info("Incoming request for exportAllBorrowerTypes.");
        OLEBorrowerTypeLoaderService oleBorrowerTypeLoaderService = (OLEBorrowerTypeLoaderService) getOleLoaderService().getLoaderService("borrowerType");
        OLEBorrowerTypeLoaderHelperService oleBorrowerTypeLoaderHelperService = (OLEBorrowerTypeLoaderHelperService) getOleLoaderService().getLoaderHelperService("borrowerType");
        List<OleBorrowerType> oleInstanceBorrowerTypes= oleBorrowerTypeLoaderService.exportAllBorrowerTypes();
        if(CollectionUtils.isNotEmpty(oleInstanceBorrowerTypes)){
            Object object = oleBorrowerTypeLoaderHelperService.formAllBorrowerTypeExportResponse(context, oleInstanceBorrowerTypes, OLELoaderConstants.OLELoaderContext.BORROWER_TYPE,
                    context.getRequest().getRequestUri().toASCIIString());
            return Response.status(200).entity(object).build();
        }
        return Response.status(500).entity(null).build();
    }

    @POST
    @Path("/borrowerType")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response importBorrowerType(@Context HttpContext context, String bodyContent){
        LOG.info("Incoming request for importBorrowerType.");
        OLEBorrowerTypeLoaderService oleBorrowerTypeLoaderService = (OLEBorrowerTypeLoaderService) getOleLoaderService().getLoaderService("borrowerType");
        Object object = oleBorrowerTypeLoaderService.importBorrowerTypes(bodyContent, context);
        return getOleLoaderService().formResponseForImport(object,OLELoaderConstants.OLELoaderContext.BORROWER_TYPE);
    }

    @PUT
    @Path("/borrowerType/{borrowerTypeId}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response updateBorrowerType(@Context HttpContext context, @PathParam("borrowerTypeId") String borrowerTypeId, String body){
        LOG.info("Incoming request for updateBorrowerType.");
        OLEBorrowerTypeLoaderService oleBorrowerTypeLoaderService = (OLEBorrowerTypeLoaderService) getOleLoaderService().getLoaderService("borrowerType");
        OLELoaderResponseBo object = oleBorrowerTypeLoaderService.updateBorrowerTypeById(borrowerTypeId, body, context);
        if(object.getStatusCode() == 200){
            return  Response.status(object.getStatusCode()).entity(object.getDetails()).build();
        }else{
            return  Response.status(400).entity(object.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/itemAvailableStatus/{pk}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportItemAvailableStatus(@Context HttpContext context, @PathParam("pk") String id){
        LOG.info("Incoming request for exportItemAvailableStatus.");
        Object object  = null;
        OLEItemAvailableStatusLoaderService oleItemAvailableStatusLoaderService = (OLEItemAvailableStatusLoaderService) getOleLoaderService().getLoaderService("itemAvailableStatus");
        OLEItemAvailableStatusLoaderHelperService oleItemAvailableStatusLoaderHelperService = (OLEItemAvailableStatusLoaderHelperService) getOleLoaderService().getLoaderHelperService("itemAvailableStatus");
        if(id.matches("^([\\d]*)?$")){
            object = oleItemAvailableStatusLoaderService.exportItemAvailableStatusById(id);
        }else{
            object = oleItemAvailableStatusLoaderService.exportItemAvailableStatusByCode(id);
        }
        if(object instanceof OleItemAvailableStatus){
            object = oleItemAvailableStatusLoaderHelperService.formItemAvailableStatusExportResponse(object, OLELoaderConstants.OLELoaderContext.BORROWER_TYPE, context.getRequest().getRequestUri().toASCIIString(), true);
            return  Response.status(200).entity(object).build();
        }else if(object instanceof OLELoaderResponseBo){
            return Response.status(((OLELoaderResponseBo) object).getStatusCode()).entity(((OLELoaderResponseBo) object).getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        return null;
    }

    @GET
    @Path("/itemAvailableStatus")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportAllItemAvailableStatus(@Context HttpContext context){
        LOG.info("Incoming request for exportAllItemAvailableStatus.");
        OLEItemAvailableStatusLoaderService oleItemAvailableStatusLoaderService = (OLEItemAvailableStatusLoaderService) getOleLoaderService().getLoaderService("itemAvailableStatus");
        OLEItemAvailableStatusLoaderHelperService oleItemAvailableStatusLoaderHelperService = (OLEItemAvailableStatusLoaderHelperService) getOleLoaderService().getLoaderHelperService("itemAvailableStatus");
        List<OleItemAvailableStatus> oleInstanceItemAvailableStatus= oleItemAvailableStatusLoaderService.exportAllItemAvailableStatus();
        if(CollectionUtils.isNotEmpty(oleInstanceItemAvailableStatus)){
            Object object = oleItemAvailableStatusLoaderHelperService.formAllItemAvailableStatusExportResponse(context, oleInstanceItemAvailableStatus, OLELoaderConstants.OLELoaderContext.BORROWER_TYPE,
                    context.getRequest().getRequestUri().toASCIIString());
            return Response.status(200).entity(object).build();
        }
        return Response.status(500).entity(null).build();
    }

    @POST
    @Path("/itemAvailableStatus")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response importItemAvailableStatus(@Context HttpContext context, String bodyContent){
        LOG.info("Incoming request for importItemAvailableStatus.");
        OLEItemAvailableStatusLoaderService oleItemAvailableStatusLoaderService = (OLEItemAvailableStatusLoaderService) getOleLoaderService().getLoaderService("itemAvailableStatus");
        Object object = oleItemAvailableStatusLoaderService.importItemAvailableStatus(bodyContent, context);
        return getOleLoaderService().formResponseForImport(object,OLELoaderConstants.OLELoaderContext.BORROWER_TYPE);
    }

    @PUT
    @Path("/itemAvailableStatus/{itemAvailableStatusId}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response updateItemAvailableStatus(@Context HttpContext context, @PathParam("itemAvailableStatusId") String itemAvailableStatusId, String body){
        LOG.info("Incoming request for updateItemAvailableStatus.");
        OLEItemAvailableStatusLoaderService oleItemAvailableStatusLoaderService = (OLEItemAvailableStatusLoaderService) getOleLoaderService().getLoaderService("itemAvailableStatus");
        OLELoaderResponseBo object = oleItemAvailableStatusLoaderService.updateItemAvailableStatusById(itemAvailableStatusId, body, context);
        if(object.getStatusCode() == 200){
            return  Response.status(object.getStatusCode()).entity(object.getDetails()).build();
        }else{
            return  Response.status(400).entity(object.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/statisticalSearchingCode/{pk}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportStatisticalSearchingCode(@Context HttpContext context, @PathParam("pk") String id){
        LOG.info("Incoming request for exportStatisticalSearchingCode.");
        Object object  = null;
        OLEStatisticalSearchingCodeLoaderService oleStatisticalSearchingCodeLoaderService = (OLEStatisticalSearchingCodeLoaderService) getOleLoaderService().getLoaderService("statisticalSearchingCode");
        OLEStatisticalSearchingCodeLoaderHelperService oleStatisticalSearchingCodeLoaderHelperService = (OLEStatisticalSearchingCodeLoaderHelperService) getOleLoaderService().getLoaderHelperService("statisticalSearchingCode");
        if(id.matches("^([\\d]*)?$")){
            object = oleStatisticalSearchingCodeLoaderService.exportStatisticalSearchingCodeById(id);
        }else{
            object = oleStatisticalSearchingCodeLoaderService.exportStatisticalSearchingCodeByCode(id);
        }
        if(object instanceof OleStatisticalSearchingCodes){
            object = oleStatisticalSearchingCodeLoaderHelperService.formStatisticalSearchingCodeExportResponse(object, OLELoaderConstants.OLELoaderContext.BORROWER_TYPE, context.getRequest().getRequestUri().toASCIIString(), true);
            return  Response.status(200).entity(object).build();
        }else if(object instanceof OLELoaderResponseBo){
            return Response.status(((OLELoaderResponseBo) object).getStatusCode()).entity(((OLELoaderResponseBo) object).getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        return null;
    }

    @GET
    @Path("/statisticalSearchingCode")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response exportAllStatisticalSearchingCode(@Context HttpContext context){
        LOG.info("Incoming request for exportAllStatisticalSearchingCode.");
        OLEStatisticalSearchingCodeLoaderService oleStatisticalSearchingCodeLoaderService = (OLEStatisticalSearchingCodeLoaderService) getOleLoaderService().getLoaderService("statisticalSearchingCode");
        OLEStatisticalSearchingCodeLoaderHelperService oleStatisticalSearchingCodeLoaderHelperService = (OLEStatisticalSearchingCodeLoaderHelperService) getOleLoaderService().getLoaderHelperService("statisticalSearchingCode");
        List<OleStatisticalSearchingCodes> oleInstanceStatisticalSearchingCode= oleStatisticalSearchingCodeLoaderService.exportAllStatisticalSearchingCode();
        if(CollectionUtils.isNotEmpty(oleInstanceStatisticalSearchingCode)){
            Object object = oleStatisticalSearchingCodeLoaderHelperService.formAllStatisticalSearchingCodeExportResponse(context, oleInstanceStatisticalSearchingCode, OLELoaderConstants.OLELoaderContext.BORROWER_TYPE,
                    context.getRequest().getRequestUri().toASCIIString());
            return Response.status(200).entity(object).build();
        }
        return Response.status(500).entity(null).build();
    }

    @POST
    @Path("/statisticalSearchingCode")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response importStatisticalSearchingCode(@Context HttpContext context, String bodyContent){
        LOG.info("Incoming request for importStatisticalSearchingCode.");
        OLEStatisticalSearchingCodeLoaderService oleStatisticalSearchingCodeLoaderService = (OLEStatisticalSearchingCodeLoaderService) getOleLoaderService().getLoaderService("statisticalSearchingCode");
        Object object = oleStatisticalSearchingCodeLoaderService.importStatisticalSearchingCode(bodyContent, context);
        return getOleLoaderService().formResponseForImport(object,OLELoaderConstants.OLELoaderContext.BORROWER_TYPE);
    }

    @PUT
    @Path("/statisticalSearchingCode/{statisticalSearchingCodeId}")
    @Produces({"application/ld+json", MediaType.APPLICATION_JSON})
    public Response updateStatisticalSearchingCode(@Context HttpContext context, @PathParam("statisticalSearchingCodeId") String statisticalSearchingCodeId, String body){
        LOG.info("Incoming request for updateStatisticalSearchingCode.");
        OLEStatisticalSearchingCodeLoaderService oleStatisticalSearchingCodeLoaderService = (OLEStatisticalSearchingCodeLoaderService) getOleLoaderService().getLoaderService("statisticalSearchingCode");
        OLELoaderResponseBo object = oleStatisticalSearchingCodeLoaderService.updateStatisticalSearchingCodeById(statisticalSearchingCodeId, body, context);
        if(object.getStatusCode() == 200){
            return  Response.status(object.getStatusCode()).entity(object.getDetails()).build();
        }else{
            return  Response.status(400).entity(object.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
    }



}
