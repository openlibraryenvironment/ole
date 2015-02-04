package org.kuali.ole;


import org.junit.Test;
import org.kuali.ole.service.OleValidateInputRequestService;
import org.kuali.ole.serviceimpl.OleValidateInputRequestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleValidateInputRequestService_UT  extends BaseTestCase{

    private static Logger LOG = LoggerFactory.getLogger(OleValidateInputRequestService_UT.class);
    private OleValidateInputRequestService oleValidateInputRequestService=new OleValidateInputRequestServiceImpl();

    @Test
    public void testInputRequestValidation() throws Exception{

        String searchRetireveOperationFlag=oleValidateInputRequestService.inputRequestValidation(getReqParameters());
        String explainOperationFlag=oleValidateInputRequestService.inputRequestValidation(getReqParameters());
        assertNotNull(searchRetireveOperationFlag);
        assertNotNull(explainOperationFlag);
    }

    public Map getReqParameters() {

        HashMap reqParamMap=new HashMap();
        reqParamMap.put(OleSRUConstants.OperationType,OleSRUConstants.SEARCH_RETRIEVE);
        reqParamMap.put(OleSRUConstants.VERSION,"1.1");
        reqParamMap.put(OleSRUConstants.QUERY,"title=jon");
        reqParamMap.put(OleSRUConstants.START_RECORD,"1");
        reqParamMap.put(OleSRUConstants.MAXIMUM_RECORDS,"10");
        reqParamMap.put(OleSRUConstants.RECORD_PACKING,"xml");
        reqParamMap.put(OleSRUConstants.RECORD_SCHEMA,"recordSchema");

        return reqParamMap;

    }


}
