package org.kuali.ole.utility;

import org.apache.http.client.HttpResponseException;

import java.io.IOException;

public interface OleHttpRest {
    /**
     * Do a HTTP Get at some REST URL.
     *
     * @param restPath  the path starting with "rest/" of the URL where to do the GET
     * @return      response to the Get request, null if restPath not found (HTTP 404)
     * @exception IOException  on IO error
     * @exception HttpResponseException on non-2xx status codes
     */
    public String get(String restPath) throws IOException;

    /**
     * Do a HTTP Post at some REST URL.
     *
     * @param restPath  the path starting with "rest/" of the URL where to do the POST
     * @param content   request content
     * @param format    format of request and response content, eg. "json", "xml", "html", "text"
     * @return  response content
     * @exception IOException  on IO error
     * @exception HttpResponseException on non-2xx status codes
     */
    public String post(String restPath, String content, String format) throws IOException;

    /**
     * Do a HTTP Post in JSON format at some REST URL
     * @param restPath  the path starting with "rest/" of the URL where to do the POST
     * @param content   request content
     * @return  response content
     * @exception IOException  on IO error
     * @exception HttpResponseException on non-2xx status codes
     */
    public String json(String restPath, String content) throws IOException, HttpResponseException;
}
