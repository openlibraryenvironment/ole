package org.kuali.ole.oleng.rest.controller;

import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.BatchProfileRequestHandler;
import org.kuali.ole.oleng.service.BatchProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

/**
 * Created by SheikS on 12/17/2015.
 */
public class BatchProfileUtilController extends OleNgControllerBase {

    @Autowired
    private BatchProfileRequestHandler batchProfileRequestHandler;

    @RequestMapping(method = RequestMethod.GET, value = "/getCallNumberTypes", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getCallNumberTypes() {
        return batchProfileRequestHandler.prepareCallNumberType();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getLocations", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getLocations(@RequestParam("levelId") String levelId) {
        return batchProfileRequestHandler.prepareLocation(levelId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getBibStatus", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getBibStatus() {
        return batchProfileRequestHandler.prepareBibStatus();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getItemTypes", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getItemTypes() {
        return batchProfileRequestHandler.prepareItemTypes();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getDonorCodes", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getDonorCodes() {
        return batchProfileRequestHandler.prepareDonorCodes();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getItemStatus", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getItemStatus() {
        return batchProfileRequestHandler.prepareItemStatus();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getProfileNames", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getProfileNames(@RequestParam("batchType") String batchType) {
        return batchProfileRequestHandler.prepareProfileNames(batchType);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getGloballyProtectedFields", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getGloballyProtectedField() {
        return batchProfileRequestHandler.prepareGloballyProtectedField();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getOrderImportFieldValues", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getOrderImportFieldValues(@RequestParam("fieldName") String fieldName) {
        return batchProfileRequestHandler.prepareOrderImportFieldValues(fieldName);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getValuesForDropDown", produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String getValuesForDropDown(@RequestParam("dropDownType") String dropDownType) {
        return batchProfileRequestHandler.prepareValuesForDowndown(dropDownType);
    }
}
