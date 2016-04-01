package org.kuali.ole.gobi.response;

import org.junit.Test;

/**
 * Created by pvsubrah on 9/14/15.
 */
public class GobiResponseHandlerTest {
    @Test
    public void marshallForSuccessfulResponse() throws Exception {
        Response response = new Response();
        response.setPoLineNumber("123");
        response.setError(null);

        GobiResponseHandler gobiResponseHandler = new GobiResponseHandler();
        String xmlResponse = gobiResponseHandler.marshall(response);
        System.out.println(xmlResponse);

    }

    @Test
    public void marshallForFailureResponse() throws Exception {
        Response response = new Response();
        response.setPoLineNumber(null);
        ResponseError responseError = new ResponseError();
        responseError.setCode("FAIL");
        responseError.setMessage("PO Creation Failed!");
        response.setError(responseError);

        GobiResponseHandler gobiResponseHandler = new GobiResponseHandler();
        String xmlResponse = gobiResponseHandler.marshall(response);
        System.out.println(xmlResponse);

    }


}