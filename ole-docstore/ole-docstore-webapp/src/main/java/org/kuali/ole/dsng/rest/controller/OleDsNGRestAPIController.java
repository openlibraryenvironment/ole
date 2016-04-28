package org.kuali.ole.dsng.rest.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.dsng.rest.processor.OleDsNgRestAPIProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;

/**
 * Created by SheikS on 11/25/2015.
 */
public class OleDsNGRestAPIController {

    private ObjectMapper objectMapper;

    @Autowired
    private OleDsNgRestAPIProcessor oleDsNgRestAPIProcessor;

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.CREATE_BIB, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String createBib(@RequestBody String body) throws Exception {
        String savedBibJsonObject = getOleDsNgRestAPIProcessor().createBib(body);
        return savedBibJsonObject;
    }

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.UPDATE_BIB, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String updateBib(@RequestBody String body) throws Exception {
        String savedBibJsonObject = getOleDsNgRestAPIProcessor().updateBib(body);
        return savedBibJsonObject;
    }

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.CREATE_HOLDINGS, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String createHolding(@RequestBody String body) throws Exception {
        String savedBibJsonObject = getOleDsNgRestAPIProcessor().createHolding(body);
        return savedBibJsonObject;
    }

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.UPDATE_HOLDINGS, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String updateHolding(@RequestBody String body) throws Exception {
        String savedBibJsonObject = getOleDsNgRestAPIProcessor().updateHolding(body);
        return savedBibJsonObject;
    }

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.CREATE_ITEM, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String createItem(@RequestBody String body) throws Exception {
        String savedBibJsonObject = getOleDsNgRestAPIProcessor().createItem(body);
        return savedBibJsonObject;
    }

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.UPDATE_ITEM, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public String updateItem(@RequestBody String body) throws Exception {
        String savedBibJsonObject = getOleDsNgRestAPIProcessor().updateItem(body);
        return savedBibJsonObject;
    }

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.PROCESS_DELETE_BIBS, produces = {MediaType.APPLICATION_JSON + ";charset=UTF-8"})
    @ResponseBody
    public ResponseEntity processDeleteBibs(@RequestBody String body) throws Exception {
        String deleteResponseObject = getOleDsNgRestAPIProcessor().processDeleteBibs(body);
        return new ResponseEntity<String>(deleteResponseObject, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.PROCESS_BIB_HOLDINGS_ITEMS, produces = {MediaType.APPLICATION_JSON + ";charset=UTF-8"})
    @ResponseBody
    public ResponseEntity processBibHoldingsItems(@RequestBody String body) throws Exception {
        String overlayResponseObject = getOleDsNgRestAPIProcessor().processBibAndHoldingsAndItems(body);
        return new ResponseEntity<String>(overlayResponseObject, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.CREATE_DUMMY_HOLDINGS, produces = {MediaType.APPLICATION_JSON + ";charset=UTF-8"})
    @ResponseBody
    public ResponseEntity createDummyHoldings(@RequestBody String body) throws Exception {
        String holdingsContent = getOleDsNgRestAPIProcessor().createDummyHoldings(body);
        return new ResponseEntity<String>(holdingsContent, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = OleNGConstants.CREATE_DUMMY_ITEM, produces = {MediaType.APPLICATION_JSON + ";charset=UTF-8"})
    @ResponseBody
    public ResponseEntity createDummyItem(@RequestBody String body) throws Exception {
        String itemContent = getOleDsNgRestAPIProcessor().createDummyItem(body);
        return new ResponseEntity<String>(itemContent, HttpStatus.CREATED);
    }

    public OleDsNgRestAPIProcessor getOleDsNgRestAPIProcessor() {
        return oleDsNgRestAPIProcessor;
    }

    public void setOleDsNgRestAPIProcessor(OleDsNgRestAPIProcessor oleDsNgRestAPIProcessor) {
        this.oleDsNgRestAPIProcessor = oleDsNgRestAPIProcessor;
    }

    public ObjectMapper getObjectMapper() {
        if(null == objectMapper) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
