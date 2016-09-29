package org.kuali.ole.gobi.controller;

import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.processor.*;
import org.kuali.ole.gobi.request.GobiRequestHandler;
import org.kuali.ole.gobi.request.GobiRequestValidator;
import org.kuali.ole.gobi.response.GobiResponseHandler;
import org.kuali.ole.gobi.response.Response;
import org.kuali.ole.gobi.service.GobiAPIService;
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

@Controller
@RequestMapping("/order")
public class GobiAPIController {

    @Autowired
    private GobiAPIService gobiAPIService;
    List<GobiAPIProcessor> gobiAPIProcessors;
    private GobiRequestHandler gobiRequestHandler;
    private GobiResponseHandler gobiResponseHandler;


    @RequestMapping(method = RequestMethod.POST, value = "/createOrder", produces = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ResponseBody
    public ResponseEntity<String> createOrder(@RequestBody String body) {
        Response gobiResponse = null;
        boolean validRequest = validateIncomingRequest(body);

        if (validRequest) {
            GobiRequest gobiRequest = getGobiRequestHandler().unmarshal(body);
            String baseAccount = gobiRequest.getPurchaseOrder().getCustomerDetail().getBaseAccount();
            gobiRequest.setProfileIdForDefaultMapping(baseAccount);
            for (Iterator<GobiAPIProcessor> iterator = getGobiAPIProcessors().iterator(); iterator.hasNext(); ) {
                GobiAPIProcessor gobiAPIProcessor = iterator.next();
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

    public List<GobiAPIProcessor> getGobiAPIProcessors() {
        gobiAPIProcessors = new ArrayList<>();
        gobiAPIProcessors.add(new ListedPrintSerialRecordProcessor());
        gobiAPIProcessors.add(new UnListedPrintSerialRecordProcessor());
        gobiAPIProcessors.add(new ListedElectronicSerialRecordProcessor());
        gobiAPIProcessors.add(new UnlistedPrintMonographRecordProcessor());
        gobiAPIProcessors.add(new ListedPrintMonographRecordProcessor());
        gobiAPIProcessors.add(new ListedElectronicMonographRecordProcessor());
        gobiAPIProcessors.add(new UnListedElectronicMonographRecordProcessor());
        return gobiAPIProcessors;
    }

}
