package org.kuali.asr.processor;

import org.apache.log4j.Logger;
import org.kuali.asr.bo.*;
import org.kuali.asr.service.ASRService;
import org.kuali.asr.service.impl.ASRServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 12/24/13
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class act as controller for processing the request made to the exposed ASr Webservice
 */
@Path("/asr")
public class ASRRequestProcessor {
    private static final Logger LOG = Logger.getLogger(ASRRequestProcessor.class);
    private ASRService asrService ;

    public ASRService getAsrService() {
        if(asrService==null){
            asrService = new ASRServiceImpl();
        }
        return asrService;
    }

    public ASRRequestProcessor(){
        getAsrService();
    }



    @GET
    @Path("/lookupNewAsrItems/{operatorId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response lookUpNewASRItems(@PathParam("operatorId") String operatorId){
        Object object  = asrService.lookupNewASRItems(operatorId);
        if(object instanceof ASRItems){
            return  Response.status(200).entity(object).build();
        } else {
            ASRResponseBo asrResponseBo = (ASRResponseBo)object;
            return  Response.status(asrResponseBo.getStatusCode()).entity(object).build();
        }
    }

    @GET
    @Path("/lookupAsrRequests/{operatorId},{asrLocation}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response lookUpNewASRTypeRequests(@PathParam("operatorId") String operatorId,@PathParam("asrLocation") String asrLocation){
        Object object  = asrService.lookupASRTypeRequest(operatorId, asrLocation);
        if(object instanceof ASRRequests){
            return  Response.status(200).entity(object).build();
        } else {
            ASRResponseBo asrResponseBo = (ASRResponseBo)object;
            return  Response.status(asrResponseBo.getStatusCode()).entity(object).build();
        }
    }


    @GET
    @Path("/lookupAsrItemHolds/{operatorId},{itemBarcode}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response lookUpASRRequests(@PathParam("operatorId") String operatorId,@PathParam("itemBarcode") String itemBarcode){
        Object object =asrService.lookupAsrRequest(operatorId,itemBarcode);
        if(object instanceof LookupASRRequestResponseBO){
            LookupASRRequestResponseBO lookupASRRequestResponseBO = (LookupASRRequestResponseBO)object;
             return Response.status(lookupASRRequestResponseBO.getStatusCode()).entity(lookupASRRequestResponseBO).build();
        }else{
            ASRResponseBo asrResponseBo = (ASRResponseBo)object;
            return Response.status(asrResponseBo.getStatusCode()).entity(object).build();
        }
    }


    @DELETE
    @Path("/cancelHold/{holdId},{operatorId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response cancelHold(@PathParam("holdId") String holdId,@PathParam("operatorId") String operatorId){
        ASRResponseBo asrResponseBo = asrService.cancelASRRequest(holdId,operatorId);
        return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();

    }

    @DELETE
    @Path("/removeASRItem/{itemBarcode}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response removeASRItem(@PathParam("itemBarcode") String itemBarcode){
        ASRResponseBo asrResponseBo = asrService.removeASRItem(itemBarcode);
        return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
    }

    @POST
    @Path("/updateASRItemStatusAvailable")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateASRItemStatusToAvailable(UpdateASRItemRequestBo updateASRItemRequestBo){
        Object object = asrService.updateASRItemStatusToAvailable(updateASRItemRequestBo);
        if(object==null){
            return Response.status(200).entity(object).build();
        } else if(object instanceof ASRResponseBo) {
            ASRResponseBo asrResponseBo = (ASRResponseBo)object;
            return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
        }
        return Response.status(200).entity("").build();
    }


    @POST
    @Path("/updateASRItemStatusMissing")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateASRItemStatusToMissing(UpdateASRItemStatusBo updateASRItemStatusBo){
        Object object = asrService.updateASRItemStatusToMissing(updateASRItemStatusBo);
        if(object==null){
            return Response.status(200).entity(object).build();
        } else if(object instanceof ASRResponseBo) {
            ASRResponseBo asrResponseBo = (ASRResponseBo)object;
            return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
        }
        return Response.status(200).entity("").build();
    }

    @POST
    @Path("/updateASRItemStatusBeingRetrieved")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateASRItemStatus(UpdateASRItemStatusBo updateASRItemStatusBo){
        Object object= asrService.updateASRItemStatusToBeingRetrieved(updateASRItemStatusBo);
        if(object==null){
            return Response.status(200).entity(object).build();
        } else if(object instanceof ASRResponseBo) {
            ASRResponseBo asrResponseBo = (ASRResponseBo)object;
            return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
        }
        return Response.status(200).entity("").build();
    }

    @POST
    @Path("/placeASRRequest")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response placeRequestOnASRItem(PlaceASRItemRequestBo placeASRItemRequestBo){
        ASRResponseBo asrResponseBo = asrService.placeRequestOnASRItem(placeASRItemRequestBo);
        return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
    }


    @POST
    @Path("/updateASRRequest")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateASRRequestStatus(UpdateASRRequestStatusBo updateASRRequestStatusBo){
        Object object= asrService.updateASRRequestStatus(updateASRRequestStatusBo);
        if(object==null){
            return Response.status(200).entity(object).build();
        } else if(object instanceof ASRResponseBo) {
            ASRResponseBo asrResponseBo = (ASRResponseBo)object;
            return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
        }
        return Response.status(200).entity("").build();
    }


    @POST
    @Path("/addNewASRItem")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addNewASRItem(ASRItem asrItem){
        ASRResponseBo asrResponseBo = asrService.addNewASRItem(asrItem);
        return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
    }

    @POST
    @Path("/sendASRRequest")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response sendASRRequest(ASRRequestBo asrRequestBo){
        ASRResponseBo asrResponseBo = asrService.sendASRRequest(asrRequestBo);
        return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
    }

    @POST
    @Path("/receiveTransitOfASRItem")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response receiveASRItemTransit(ReceiveTransitRequestBo receiveTransitRequestBo){
        ASRResponseBo asrResponseBo = asrService.receiveASRItemTransit(receiveTransitRequestBo);
        return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
    }

    @POST
    @Path("/checkInASRItem")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response checkInASRItem(ASRCheckInBo asrCheckInBo){
        ASRResponseBo asrResponseBo = asrService.checkInASRItem(asrCheckInBo);
        return Response.status(asrResponseBo.getStatusCode()).entity(asrResponseBo).build();
    }


}
