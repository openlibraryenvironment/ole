package org.kuali.ole.ncip.servlet;

import org.apache.log4j.Logger;
import org.kuali.ole.converter.OLECheckInItemConverter;
import org.kuali.ole.converter.OLELookupUserConverter;
import org.kuali.ole.converter.OLERenewItemConverter;
import org.kuali.ole.ncip.bo.OLECirculationErrorMessage;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.bo.OLENCIPErrorResponse;
import org.kuali.ole.ncip.converter.*;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.ncip.service.impl.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/16/13
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECirculationServlet extends HttpServlet {
    final Logger LOG = Logger.getLogger(OLECirculationServlet.class);


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OLECirculationService oleCirculationService=new OLECirculationServiceImpl();
        boolean doProcess=true;
        String responseString="";
        Map<String,String[]> parameterMap=new HashMap<String,String[]>();
        String outputFormat=OLENCIPConstants.XML_FORMAT;

        parameterMap=request.getParameterMap();

        if(parameterMap.containsKey(OLENCIPConstants.FORMAT)){
            if(parameterMap.get(OLENCIPConstants.FORMAT)[0].equalsIgnoreCase(OLENCIPConstants.XML_FORMAT) || parameterMap.get(OLENCIPConstants.FORMAT)[0].equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                outputFormat=parameterMap.get(OLENCIPConstants.FORMAT)[0];
            }
            else{
                doProcess=false;
            }
        }

        String service=parameterMap.get(OLENCIPConstants.OLE_CIRCULATION_SERVICE)[0];
        if(service!=null){
            if(doProcess){
                switch (service){
                    case OLENCIPConstants.PLACEREQUEST_SERVICE:
                        if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                                parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID) &&
                                parameterMap.containsKey(OLENCIPConstants.REQUEST_TYPE) && parameterMap.containsKey(OLENCIPConstants.ITEM_BARCODE) || parameterMap.containsKey(OLENCIPConstants.ITEM_ID)){
                            int size=5;
                            if(parameterMap.containsKey(OLENCIPConstants.FORMAT)){
                                size++;
                            }
                            if(parameterMap.containsKey(OLENCIPConstants.PICKUP_LOCATION))
                            {
                                size++;
                            }
                            if(parameterMap.containsKey(OLENCIPConstants.REQUEST_NOTE))
                            {
                                size++;
                            }
                            if(parameterMap.size()==size){
                                String pickupLocation=null;
                                if(parameterMap.containsKey(OLENCIPConstants.PICKUP_LOCATION)) {
                                    pickupLocation=parameterMap.get(OLENCIPConstants.PICKUP_LOCATION)[0];
                                }
                                String requestNote = null;
                                if(parameterMap.containsKey(OLENCIPConstants.REQUEST_NOTE)) {
                                    requestNote = parameterMap.get(OLENCIPConstants.REQUEST_NOTE)[0];
                                }
                                String itemId = null;
                                if(parameterMap.containsKey(OLENCIPConstants.ITEM_ID)) {
                                    itemId = parameterMap.get(OLENCIPConstants.ITEM_ID)[0];
                                }
                                String itemBarcode = null;
                                if(parameterMap.containsKey(OLENCIPConstants.ITEM_BARCODE)) {
                                    itemBarcode = parameterMap.get(OLENCIPConstants.ITEM_BARCODE)[0];
                                }
                                responseString=oleCirculationService.placeRequest(parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0],parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0],itemBarcode,itemId,parameterMap.get(OLENCIPConstants.REQUEST_TYPE)[0],pickupLocation,null,null,"Item Level",null, requestNote);
                                if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                    responseString=new OLEPlaceRequestConverter().generatePlaceRequestJson(responseString);
                                }
                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,OLENCIPConstants.INVALID_INPUT);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            }else{
                                responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501", OLENCIPConstants.PLACEREQUEST,outputFormat);
                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.PLACEREQUEST,outputFormat);
                        }
                        break;
                    case OLENCIPConstants.CANCELREQUEST_SERVICE:
                        if(parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID) &&
                                parameterMap.containsKey(OLENCIPConstants.REQUEST_ID)){
                            if(parameterMap.size()==3 || parameterMap.size()==4){
                               responseString=oleCirculationService.cancelRequests(parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0],parameterMap.get(OLENCIPConstants.REQUEST_ID)[0]);
                                if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                    responseString=new OLECancelRequestConverter().generateCancelRequestJson(responseString);
                                }
                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.REQUEST_ID,parameterMap.get(OLENCIPConstants.REQUEST_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,OLENCIPConstants.INVALID_INPUT);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            }else{
                                responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.CANCELREQUEST,outputFormat);
                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.CANCELREQUEST,outputFormat);
                        }
                        break;
                    case OLENCIPConstants.RENEWITEM_SERVICE:
                        if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                                parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID) &&
                                parameterMap.containsKey(OLENCIPConstants.ITEM_BARCODE)){
                            if(parameterMap.size()==4 || parameterMap.size()==5){

                                Map renewParameters = new HashMap();
                                renewParameters.put("patronBarcode", parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                renewParameters.put("operatorId", parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                List<String> items = new ArrayList<>();
                                items.add(parameterMap.get(OLENCIPConstants.ITEM_BARCODE)[0]);
                                renewParameters.put("itemBarcodes", items);
                                renewParameters.put("responseFormatType", outputFormat);
                                responseString = new VuFindRenewItemService().renewItems(renewParameters);

                                if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                    responseString=new OLERenewItemConverter().generateRenewItemJson(responseString);
                                }
                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,responseString);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            }else{
                                responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.RENEWITEM,outputFormat);
                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.RENEWITEM,outputFormat);
                        }
                        break;
                    case OLENCIPConstants.RENEWITEMLIST_SERVICE:
                        if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                                parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID) &&
                                parameterMap.containsKey(OLENCIPConstants.ITEM_BARCODE)){
                            if(parameterMap.size()==4 || parameterMap.size()==5){
                                Long startingTime = System.currentTimeMillis();

                                Map renewParameters = new HashMap();
                                renewParameters.put("patronBarcode", parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                renewParameters.put("operatorId", parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                String itemBarcodeList = parameterMap.get(OLENCIPConstants.ITEM_BARCODE)[0];
                                String[] itemBarcodeArray = itemBarcodeList.split(",");
                                List<String> renewalItemList = Arrays.asList(itemBarcodeArray);
                                renewParameters.put("itemBarcodes", renewalItemList);
                                renewParameters.put("responseFormatType", outputFormat);
                                responseString = new VuFindRenewItemService().renewItems(renewParameters);

                                Long endTimme = System.currentTimeMillis();
                                Long timeTakenForRenewAll = endTimme-startingTime;
                                LOG.info("The Time Taken for RenewAll : "+timeTakenForRenewAll);
                                if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                    responseString=new OLERenewItemConverter().generateRenewItemListJson(responseString);
                                }
                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,responseString);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            }else{
                                responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.RENEWITEM,outputFormat);
                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.RENEWITEM,outputFormat);
                        }
                        break;
                    case OLENCIPConstants.ACCEPTITEM_SERVICE:
                        if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                                parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID) &&
                                parameterMap.containsKey(OLENCIPConstants.ITEM_BARCODE) &&
                                parameterMap.containsKey(OLENCIPConstants.CALLNUMBER) &&
                                parameterMap.containsKey(OLENCIPConstants.TITLE) &&
                                parameterMap.containsKey(OLENCIPConstants.AUTHOR) &&
                                parameterMap.containsKey(OLENCIPConstants.ITEM_TYPE) &&
                                parameterMap.containsKey(OLENCIPConstants.ITEM_LOCATION) &&
                                parameterMap.containsKey(OLENCIPConstants.DATE_EXPIRES) &&
                                parameterMap.containsKey(OLENCIPConstants.REQUEST_TYPE)){
                            int size=11;
                            if(parameterMap.containsKey(OLENCIPConstants.FORMAT)){
                                size++;
                            }
                            if(parameterMap.containsKey(OLENCIPConstants.PICKUP_LOCATION))
                            {
                                size++;
                            }

                            if(parameterMap.size()==size){
                                String pickupLocation=null;
                                if(parameterMap.containsKey(OLENCIPConstants.PICKUP_LOCATION)) {
                                    pickupLocation=parameterMap.get(OLENCIPConstants.PICKUP_LOCATION)[0];
                                }
                                responseString=oleCirculationService.acceptItem(parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0],parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0],parameterMap.get(OLENCIPConstants.ITEM_BARCODE)[0],parameterMap.get(OLENCIPConstants.CALLNUMBER)[0],parameterMap.get(OLENCIPConstants.TITLE)[0],parameterMap.get(OLENCIPConstants.AUTHOR)[0],parameterMap.get(OLENCIPConstants.ITEM_TYPE)[0],parameterMap.get(OLENCIPConstants.ITEM_LOCATION)[0],parameterMap.get(OLENCIPConstants.DATE_EXPIRES)[0],parameterMap.get(OLENCIPConstants.REQUEST_TYPE)[0],pickupLocation);
                                if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                    responseString=new OLEAcceptItemConverter().generateAcceptItemJson(responseString);
                                }
                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,OLENCIPConstants.INVALID_INPUT);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            }else{
                                responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.ACCEPTITEM,outputFormat);
                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.ACCEPTITEM,outputFormat);
                        }
                        break;
                    case OLENCIPConstants.CHECKINITEM_SERVICE:
                        if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                                parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID) &&
                                parameterMap.containsKey(OLENCIPConstants.ITEM_BARCODE) &&
                                parameterMap.containsKey(OLENCIPConstants.DELETE_INDICATOR)){
                            if(parameterMap.size()==5 || parameterMap.size()==6){

                                Map checkinParameters = new HashMap();
                                checkinParameters.put("operatorId", parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                checkinParameters.put("itemBarcode", parameterMap.get(OLENCIPConstants.ITEM_BARCODE)[0]);
                                checkinParameters.put("responseFormatType", outputFormat);
                                checkinParameters.put("deleteIndicator", parameterMap.get(OLENCIPConstants.DELETE_INDICATOR)[0]);
                                responseString = new NonSip2CheckinItemServiceImplImpl().checkinItem(checkinParameters);

                                if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                    responseString=new OLECheckInItemConverter().generateCheckInItemJson(responseString);
                                }
                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,OLENCIPConstants.INVALID_INPUT);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            }else{
                                responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.CHECKINITEM,outputFormat);

                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.CHECKINITEM,outputFormat);
                        }
                        break;
                    case OLENCIPConstants.CHECKOUTITEM_SERVICE:
                        if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                                parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID) &&
                                parameterMap.containsKey(OLENCIPConstants.ITEM_BARCODE)){
                            if(parameterMap.size()==4 || parameterMap.size()==5){

                                Map checkoutParameters = new HashMap();
                                checkoutParameters.put("patronBarcode", parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                checkoutParameters.put("operatorId", parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                checkoutParameters.put("itemBarcode", parameterMap.get(OLENCIPConstants.ITEM_BARCODE)[0]);
                                checkoutParameters.put("responseFormatType", outputFormat);
                                responseString = new VuFindCheckoutItemServiceImplImpl().checkoutItem(checkoutParameters);

                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,OLENCIPConstants.INVALID_INPUT);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            }else{
                                responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.CHECKOUTITEM,outputFormat);
                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.CHECKOUTITEM,outputFormat);
                        }

                        break;
                    default:
                        LOG.info("Unknown Service Name: "+service+"   Parameter is missing");
                        responseString=getCirculationErrorMessage(service,OLENCIPConstants.UNKNOWN_SERVICE,"503",null,outputFormat);
                        response.sendError(405,"Method Not Supported");
                }
            }else{
                responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_FORMAT,"504",null,outputFormat);
            }
        }
        else{
            responseString=OLENCIPConstants.NULL_SERVICE;
        }

        LOG.info("Response :"+responseString);
        if(responseString==null){
            responseString=OLENCIPConstants.INVALID_DATA;
        }
        if(responseString!=null){
            responseString=responseString.replaceAll("errorMessage",OLENCIPConstants.MESSAGE);
            responseString=responseString.replaceAll("<br/>","");
        }
        responseString=responseString.replaceAll("errorMessage",OLENCIPConstants.MESSAGE);
        PrintWriter out=response.getWriter();
        response.setContentType(OLENCIPConstants.XML_CONTENT_TYPE);
        response.setCharacterEncoding(OLENCIPConstants.XML_CHAR_ENCODING);
        if(responseString.contains(OLENCIPConstants.INVALID_FORMAT)){
            response.setStatus(406);
        } else if(responseString.contains(OLENCIPConstants.PARAMETER_MISSING)){
            response.setStatus(422);
        }else{
            response.setStatus(200);
        }
        out.write(responseString);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OLECirculationService oleCirculationService=new OLECirculationServiceImpl();

        String responseString="";
        boolean doProcess=true;
        Map<String,String[]> parameterMap=new HashMap<String,String[]>();
        String outputFormat=OLENCIPConstants.XML_FORMAT;
        String service=null;

        parameterMap=request.getParameterMap();
        if(parameterMap.containsKey(OLENCIPConstants.FORMAT)){
            if(parameterMap.get(OLENCIPConstants.FORMAT)[0].equalsIgnoreCase(OLENCIPConstants.XML_FORMAT) || parameterMap.get(OLENCIPConstants.FORMAT)[0].equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                outputFormat=parameterMap.get(OLENCIPConstants.FORMAT)[0];
            }
            else{
                doProcess=false;
            }
        }
        if(parameterMap.containsKey(OLENCIPConstants.OLE_CIRCULATION_SERVICE)){
            service=parameterMap.get(OLENCIPConstants.OLE_CIRCULATION_SERVICE)[0];
        }

        if(service!=null){
            if(doProcess){
                if(service.equals(OLENCIPConstants.LOOKUPUSER_SERVICE)){
                    if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                            parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID)){
                        if(parameterMap.size()==3 || parameterMap.size()==4){

                            Map lookupUserParameters = new HashMap();
                            lookupUserParameters.put("patronBarcode", parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                            lookupUserParameters.put("operatorId", parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                            lookupUserParameters.put("responseFormatType", outputFormat);
                            responseString = new VuFindLookupUserServiceImpl().lookupUser(lookupUserParameters);

                            if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                responseString=new OLELookupUserConverter().generateLookupUserJson(responseString);
                            }
                            if(responseString==null){
                                OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                responseString=olencipErrorResponse.getErrorXml(service);
                            }

                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.LOOKUPUSER,outputFormat);
                        }


                    }else{
                        responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.LOOKUPUSER,outputFormat);
                    }
                }
                else if(service.equals(OLENCIPConstants.GETCHECKEDOUTITEM_SERVICE)){
                    if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                            parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID)){
                        if(parameterMap.size()==3 || parameterMap.size()==4){
                            try {
                                responseString=oleCirculationService.getCheckedOutItems(parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0],parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                    responseString=new OLECheckoutItemsConverter().generateGetCheckedOutItemsJson(responseString);
                                    System.out.println(responseString);
                                }
                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,OLENCIPConstants.INVALID_INPUT);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            } catch (Exception e) {
                                LOG.error(e,e);
                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.GETCHECKOUTITEMS,outputFormat);
                        }
                    }else{
                        responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.GETCHECKOUTITEMS,outputFormat);
                    }
                }
                else if(service.equals(OLENCIPConstants.FINE_SERVICE)){
                    if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                            parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID)){
                        if(parameterMap.size()==3 || parameterMap.size()==4){
                            try {
                                responseString=oleCirculationService.getFine(parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0], parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                    responseString=new OLEItemFineConverter().generateFineJson(responseString);
                                    System.out.println(responseString);
                                }
                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,OLENCIPConstants.INVALID_INPUT);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            } catch (Exception e) {
                                LOG.error(e,e);
                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.FINE,outputFormat);
                        }
                    }else{
                        responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.FINE,outputFormat);
                    }
                }
                else if(service.equals(OLENCIPConstants.HOLDS_SERVICE)){
                    if(parameterMap.containsKey(OLENCIPConstants.PATRON_BARCODE) &&
                            parameterMap.containsKey(OLENCIPConstants.OPERATOR_ID)){
                        if(parameterMap.size()==3 || parameterMap.size()==4){
                            try {
                                responseString=oleCirculationService.getHolds(parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0], parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
                                    responseString=new OLEHoldsConverter().generateHoldsJson(responseString);
                                    System.out.println(responseString);
                                }
                                if(responseString==null){
                                    OLENCIPErrorResponse olencipErrorResponse=new OLENCIPErrorResponse();
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.PATRON_BARCODE,parameterMap.get(OLENCIPConstants.PATRON_BARCODE)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.OPERATOR_ID,parameterMap.get(OLENCIPConstants.OPERATOR_ID)[0]);
                                    olencipErrorResponse.getErrorMap().put(OLENCIPConstants.MESSAGE,OLENCIPConstants.INVALID_INPUT);
                                    responseString=olencipErrorResponse.getErrorXml(service);
                                }
                            } catch (Exception e) {
                                LOG.error(e,e);
                            }
                        }else{
                            responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_PARAMETERS,"501",OLENCIPConstants.HOLDS,outputFormat);
                        }
                    }else{
                        responseString=getCirculationErrorMessage(service,OLENCIPConstants.PARAMETER_MISSING,"502",OLENCIPConstants.HOLDS,outputFormat);
                    }
                }
                else{
                    LOG.info("Unknown Service Name: "+service+"   Parameter is missing");
                    responseString=getCirculationErrorMessage(service,OLENCIPConstants.UNKNOWN_SERVICE,"503",null,outputFormat);
                    response.sendError(405,"Method Not Supported");
                }
            }
            else{
                responseString=getCirculationErrorMessage(service,OLENCIPConstants.INVALID_FORMAT,"504",null,outputFormat);
            }
        }
        else{
            responseString=OLENCIPConstants.NULL_SERVICE;
        }
            if(responseString!=null){
                responseString=responseString.replaceAll("errorMessage",OLENCIPConstants.MESSAGE);
                responseString=responseString.replaceAll("<br/>","");
            }
            PrintWriter out=response.getWriter();
        if(outputFormat.equalsIgnoreCase(OLENCIPConstants.XML_FORMAT)){
             response.setContentType(OLENCIPConstants.XML_CONTENT_TYPE);
              response.setCharacterEncoding(OLENCIPConstants.XML_CHAR_ENCODING);

        }
        if(responseString.contains(OLENCIPConstants.INVALID_FORMAT)){
            response.setStatus(406);
        } else if(responseString.contains(OLENCIPConstants.PARAMETER_MISSING)){
            response.setStatus(422);
        }else{
            response.setStatus(200);
        }


            out.write(responseString);

    }


    private String getCirculationErrorMessage(String service,String message,String code,String requiredParameters,String outputFormat){
        OLECirculationErrorMessage oleCirculationErrorMessage=new OLECirculationErrorMessage();
        OLECirculationErrorMessageConverter oleCirculationErrorMessageConverter=new OLECirculationErrorMessageConverter();
        oleCirculationErrorMessage.setMessage(message);
        oleCirculationErrorMessage.setCode(code);
        oleCirculationErrorMessage.setService(service);
        oleCirculationErrorMessage.setRequiredParameters(requiredParameters);
        String errorMessage="";
        errorMessage=oleCirculationErrorMessageConverter.generateCirculationErrorXml(oleCirculationErrorMessage);
        if(outputFormat.equalsIgnoreCase(OLENCIPConstants.JSON_FORMAT)){
            errorMessage=oleCirculationErrorMessageConverter.generateLookupUserJson(errorMessage);
        }
        return  errorMessage;
    }
}
