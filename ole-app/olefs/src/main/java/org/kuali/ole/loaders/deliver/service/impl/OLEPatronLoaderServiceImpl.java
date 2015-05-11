package org.kuali.ole.loaders.deliver.service.impl;

import com.sun.jersey.api.core.HttpContext;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.SortedArraySet;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.ingest.pojo.OlePatron;
import org.kuali.ole.loaders.common.bo.OLELoaderImportResponseBo;
import org.kuali.ole.loaders.common.bo.OLELoaderResponseBo;
import org.kuali.ole.loaders.common.constants.OLELoaderConstants;
import org.kuali.ole.loaders.common.service.OLELoaderService;
import org.kuali.ole.loaders.common.service.impl.OLELoaderServiceImpl;
import org.kuali.ole.loaders.deliver.service.OLEPatronLoaderHelperService;
import org.kuali.ole.loaders.deliver.service.OLEPatronLoaderService;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by sheiksalahudeenm on 27/3/15.
 */
public class OLEPatronLoaderServiceImpl implements OLEPatronLoaderService {

    private static final Logger LOG = Logger.getLogger(OLEPatronLoaderServiceImpl.class);
    private OLEPatronLoaderHelperService olePatronLoaderHelperService;
    private OLELoaderService oleLoaderService;
    private BusinessObjectService businessObjectService;
    private OlePatronConverterService olePatronConverterService;

    public OLEPatronLoaderHelperService getPatronDocumentLoaderHelperService() {
        if(olePatronLoaderHelperService == null){
            olePatronLoaderHelperService = new OLEPatronLoaderHelperServiceImpl();
        }
        return olePatronLoaderHelperService;
    }

    public void setPatronDocumentLoaderHelperService(OLEPatronLoaderHelperService olePatronLoaderHelperService) {
        this.olePatronLoaderHelperService = olePatronLoaderHelperService;
    }

    public OLELoaderService getOleLoaderService() {
        if(oleLoaderService == null ){
            oleLoaderService = new OLELoaderServiceImpl();
        }
        return oleLoaderService;
    }

    public void setOleLoaderService(OLELoaderService oleLoaderService) {
        this.oleLoaderService = oleLoaderService;
    }

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public OlePatronConverterService getOlePatronConverterService() {
        if(olePatronConverterService == null){
            olePatronConverterService = (OlePatronConverterService) SpringContext.getService("olePatronConverterService");
        }
        return olePatronConverterService;
    }

    public void setOlePatronConverterService(OlePatronConverterService olePatronConverterService) {
        this.olePatronConverterService = olePatronConverterService;
    }

    @Override
    public Object importPatrons(String bodyContent, HttpContext context) {
        LOG.info("Inside importPatrons method.");
        OLELoaderImportResponseBo oleLoaderImportResponseBo = new OLELoaderImportResponseBo();
        Set<Integer> rejectPatronList = new SortedArraySet<>();
        List<JSONObject> createdPatronObject = new ArrayList<JSONObject>();
        JSONObject requestJsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        List<OlePatron> failureList = new ArrayList<>();
        List<OlePatronDocument> createdList = new ArrayList<>();
        Map<String,Integer> rejectedPatronBarcodeAndIndexMap = new HashMap<>();
        Map<String,Integer> selectedPatronBarcodeAndIndexMap = new HashMap<>();
        if(requestJsonObject != null) {
            if (requestJsonObject.has("items")) {
                String items = getOleLoaderService().getStringValueFromJsonObject(requestJsonObject, "items");
                if (StringUtils.isNotBlank(items)) {
                    JSONArray patronJsonArray = getOleLoaderService().getJsonArrayFromString(items);
                    List<OlePatron> olePatronList = getPatronDocumentLoaderHelperService().formIngestOlePatrons(patronJsonArray,rejectedPatronBarcodeAndIndexMap,selectedPatronBarcodeAndIndexMap);
                    createdList = getOlePatronConverterService().createOlePatronDocument(olePatronList,failureList);
                    for(OlePatronDocument olePatronDocument : createdList){
                        createdPatronObject.add((JSONObject)getPatronDocumentLoaderHelperService().formPatronExportResponse(olePatronDocument,OLELoaderConstants.OLELoaderContext.PATRON, context.getRequest().getRequestUri().toASCIIString(), false));
                    }
                    for(OlePatron olePatron : failureList){
                        rejectPatronList.add(selectedPatronBarcodeAndIndexMap.get(olePatron.getBarcode()));
                    }
                    for (Iterator<String> patronBarcodeIndex= rejectedPatronBarcodeAndIndexMap.keySet().iterator(); patronBarcodeIndex.hasNext(); ) {
                        String barcode =  patronBarcodeIndex.next();
                        rejectPatronList.add(rejectedPatronBarcodeAndIndexMap.get(barcode));
                    }
                }else{
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
                }

            }
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
        oleLoaderImportResponseBo.setOleRejectedBos(new ArrayList<Integer>(rejectPatronList));
        oleLoaderImportResponseBo.setOleCreatedBos(createdPatronObject);
        return oleLoaderImportResponseBo;
    }

   /* @Override
    public OLELoaderResponseBo updatePatronById(String patronId, String bodyContent, HttpContext context) {
        LOG.info("Inside updatePatronById method.");
        OLEPatronBo olePatronBo = new OLEPatronBo();
        JSONObject jsonObject = getOleLoaderService().getJsonObjectFromString(bodyContent);
        boolean validObject = false;
        if (jsonObject != null) {
            if (jsonObject.has("name")) {
                String name = getOleLoaderService().getStringValueFromJsonObject(jsonObject, "name");
                if (StringUtils.isNotBlank(name)) {
                    olePatronBo.setPatronName(name);
                    validObject = true;
                }
            }

            if(jsonObject.has("description")){
                String description = getOleLoaderService().getStringValueFromJsonObject(jsonObject,"description");
                if(StringUtils.isNotBlank(description)){
                    olePatronBo.setPatronDescription(description);
                    validObject = true;
                }
            }

            if(jsonObject.has("active")){
                try{
                    boolean active = Boolean.parseBoolean(getOleLoaderService().getStringValueFromJsonObject(jsonObject, "active"));
                    olePatronBo.setActive(active);
                    validObject = true;
                }catch(Exception e){
                    e.printStackTrace();
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.INVALID_BOOLEAN, OLELoaderConstants.OLEloaderStatus.INVALID_BOOLEAN);
                }

            }

            if (olePatronBo != null && validObject) {
                OlePatronDocument olePatron = getPatronDocumentLoaderHelperService().getPatronById(patronId);
                if (olePatron != null) {
                    return getPatronDocumentLoaderHelperService().updateOlePatronDocument(olePatron,olePatronBo,context);
                } else {
                    return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.PATRON_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.PATRON_NOT_EXIST);
                }
            } else {
                return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
            }
        }else {
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.BAD_REQUEST, OLELoaderConstants.OLEloaderStatus.BAD_REQUEST);
        }
    }*/

    @Override
    public Object exportPatronById(String patronId) {
        LOG.info("Inside exportPatronById method.");
        OlePatronDocument olePatron = getPatronDocumentLoaderHelperService().getPatronById(patronId);
        if(olePatron != null){
            return olePatron;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.PATRON_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.PATRON_NOT_EXIST);
        }
    }

    @Override
    public Object exportPatronByCode(String patronCode) {
        LOG.info("Inside exportPatronByCode method.");
        OlePatronDocument olePatron = getPatronDocumentLoaderHelperService().getPatronByCode(patronCode);
        if(olePatron != null){
            return olePatron;
        }else{
            return getOleLoaderService().generateResponse(OLELoaderConstants.OLEloaderMessage.PATRON_NOT_EXIST, OLELoaderConstants.OLEloaderStatus.PATRON_NOT_EXIST);
        }
    }

    @Override
    public List<OlePatronDocument> exportAllPatrons() {
        LOG.info("Inside exportAllPatrons method.");
        List<OlePatronDocument> olePatrons = getPatronDocumentLoaderHelperService().getAllPatrons();
        return olePatrons;
    }
}
