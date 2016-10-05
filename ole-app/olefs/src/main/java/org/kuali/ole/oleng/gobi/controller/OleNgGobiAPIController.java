package org.kuali.ole.oleng.gobi.controller;

import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.processor.*;
import org.kuali.ole.gobi.request.GobiRequestHandler;
import org.kuali.ole.gobi.request.GobiRequestValidator;
import org.kuali.ole.gobi.response.GobiResponseHandler;
import org.kuali.ole.gobi.response.Response;
import org.kuali.ole.gobi.service.GobiAPIService;
import org.kuali.ole.oleng.gobi.processor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sheiks on 05/10/16.
 */
@Controller
@RequestMapping("/order")
public class OleNgGobiAPIController {
    @Autowired
    private GobiAPIService gobiAPIService;
    List<OleNGGobiApiProcessor> gobiAPIProcessors;
    private GobiRequestHandler gobiRequestHandler;
    private GobiResponseHandler gobiResponseHandler;

    @Autowired
    private OleNGListedPrintSerialRecordProcessor oleNGListedPrintSerialRecordProcessor;
    @Autowired
    private OleNGUnListedPrintSerialRecordProcessor oleNGUnListedPrintSerialRecordProcessor;
    @Autowired
    private OleNGListedElectronicSerialRecordProcessor oleNGListedElectronicSerialRecordProcessor;
    @Autowired
    private OleNGUnlistedPrintMonographRecordProcessor oleNGUnlistedPrintMonographRecordProcessor;
    @Autowired
    private OleNGListedPrintMonographRecordProcessor oleNGListedPrintMonographRecordProcessor;
    @Autowired
    private OleNGListedElectronicMonographRecordProcessor oleNGListedElectronicMonographRecordProcessor;
    @Autowired
    private OleNGUnListedElectronicMonographRecordProcessor oleNGUnListedElectronicMonographRecordProcessor;


    @RequestMapping(method = RequestMethod.POST, value = "/createOrder", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public ResponseEntity<String> createOrder(@RequestBody String body) {
        Response gobiResponse = null;
        boolean validRequest = validateIncomingRequest(body);

        if (validRequest) {
            GobiRequest gobiRequest = getGobiRequestHandler().unmarshal(body);
            String baseAccount = gobiRequest.getPurchaseOrder().getCustomerDetail().getBaseAccount();
            gobiRequest.setProfileIdForDefaultMapping(baseAccount);
            for (Iterator<OleNGGobiApiProcessor> iterator = getGobiAPIProcessors().iterator(); iterator.hasNext(); ) {
                OleNGGobiApiProcessor gobiAPIProcessor = iterator.next();
                if (gobiAPIProcessor.isInterested(gobiRequest)) {
                    gobiResponse = gobiAPIProcessor.process(gobiRequest);
                }
            }
        } else {
            //TODO: Prepare failure response
        }
        String marshaledResponse = getGobiResponseHandler().marshall(gobiResponse);
        ResponseEntity<String> stringResponseEntity;
        if (null== gobiResponse.getError()) {
            stringResponseEntity = new ResponseEntity<>(marshaledResponse, HttpStatus.OK);
        } else {
            stringResponseEntity = new ResponseEntity<>(marshaledResponse, HttpStatus.BAD_REQUEST);
        }
        return stringResponseEntity;
    }

    private GobiResponseHandler getGobiResponseHandler() {
        if (null == gobiResponseHandler) {
            gobiResponseHandler = new GobiResponseHandler();
        }

        return gobiResponseHandler;
    }

    private boolean validateIncomingRequest(String body) {
        return new GobiRequestValidator().validate(body);
    }

    private GobiRequestHandler getGobiRequestHandler() {
        if (null == gobiRequestHandler) {
            gobiRequestHandler = new GobiRequestHandler();
        }
        return gobiRequestHandler;
    }

    public GobiAPIService getGobiAPIService() {
        return gobiAPIService;
    }

    public void setGobiAPIService(GobiAPIService gobiAPIService) {
        this.gobiAPIService = gobiAPIService;
    }

    public List<OleNGGobiApiProcessor> getGobiAPIProcessors() {
        gobiAPIProcessors = new ArrayList<>();
        gobiAPIProcessors.add(oleNGListedPrintSerialRecordProcessor);
        gobiAPIProcessors.add(oleNGUnListedPrintSerialRecordProcessor);
        gobiAPIProcessors.add(oleNGListedElectronicSerialRecordProcessor);
        gobiAPIProcessors.add(oleNGUnlistedPrintMonographRecordProcessor);
        gobiAPIProcessors.add(oleNGListedPrintMonographRecordProcessor);
        gobiAPIProcessors.add(oleNGListedElectronicMonographRecordProcessor);
        gobiAPIProcessors.add(oleNGUnListedElectronicMonographRecordProcessor);
        return gobiAPIProcessors;
    }

}
