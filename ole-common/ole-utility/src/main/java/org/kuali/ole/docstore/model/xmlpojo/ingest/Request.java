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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/7/11
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Request {

    public static Set<String> validOperationSet = getValidOperationSet();

    public enum Operation {
        ingest, bulkIngest, checkIn, checkOut, delete, deleteVerify, deleteWithLinkedDocs, bind, unbind, transfer,
        transferInstances, transferItems
    }

    private List<RequestDocument> requestDocuments = new ArrayList<RequestDocument>();
    private String user;
    private String operation;

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

    public List<RequestDocument> getRequestDocuments() {
        return requestDocuments;
    }

    public void setRequestDocuments(List<RequestDocument> requestDocuments) {
        this.requestDocuments = requestDocuments;
    }

    private static Set<String> getValidOperationSet() {
        validOperationSet = new HashSet<String>();
        validOperationSet.add(Request.Operation.ingest.toString());
        validOperationSet.add(Request.Operation.bulkIngest.toString());
        validOperationSet.add(Request.Operation.checkIn.toString());
        validOperationSet.add(Request.Operation.checkOut.toString());
        validOperationSet.add(Request.Operation.delete.toString());
        validOperationSet.add(Operation.deleteVerify.toString());
        validOperationSet.add(Request.Operation.deleteWithLinkedDocs.toString());
        validOperationSet.add(Request.Operation.bind.toString());
        validOperationSet.add(Request.Operation.unbind.toString());
        validOperationSet.add(Request.Operation.transfer.toString());
        validOperationSet.add(Request.Operation.transferInstances.toString());
        validOperationSet.add(Request.Operation.transferItems.toString());
        return validOperationSet;
    }
}
