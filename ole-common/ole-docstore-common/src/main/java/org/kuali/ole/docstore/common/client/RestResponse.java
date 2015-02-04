package org.kuali.ole.docstore.common.client;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.lang.String;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 1/3/14
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class RestResponse {
    private HttpResponse response;
    private String responseBody;
    private String contentType;

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


}
