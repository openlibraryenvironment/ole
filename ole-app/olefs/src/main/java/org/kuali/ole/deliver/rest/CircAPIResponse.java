package org.kuali.ole.deliver.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by pvsubrah on 6/25/15.
 */

@XmlRootElement(name="response")
class CircAPIResponse {

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}