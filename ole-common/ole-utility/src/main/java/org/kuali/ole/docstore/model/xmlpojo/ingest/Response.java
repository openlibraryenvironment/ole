/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.docstore.model.xmlpojo.ingest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/14/11
 * Time: 12:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class Response {

    public static final String FAILURE = "Failure";

    private List<ResponseDocument> documents = new ArrayList<ResponseDocument>();
    private List<LinkInformation> linkInformation;
    private String user;
    private String operation;
    private String status;
    private String message;
    private String statusMessage;


    public List<ResponseDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ResponseDocument> documents) {
        this.documents = documents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<LinkInformation> getLinkInformation() {
        return linkInformation;
    }

    public void setLinkInformation(List<LinkInformation> linkInformation) {
        this.linkInformation = linkInformation;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
