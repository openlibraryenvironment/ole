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
package org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified;

/**
 * Represents an XML Record of category:Work, Type:Bibliographic, Format:DUBLIN_UNQUALIFIED
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibDublinUnQualifiedRecord {

    private String responseDate;
    private String request;
    private ListRecords ListRecords;

    /**
     * @return the ListRecords
     */
    public ListRecords getListRecords() {
        return ListRecords;
    }

    /**
     * @param ListRecords the ListRecords to set
     */
    public void setListRecords(ListRecords listRecords) {
        this.ListRecords = listRecords;
    }

    /**
     * @return the responseDate
     */
    public String getResponseDate() {
        return responseDate;
    }

    /**
     * @param responseDate the responseDate to set
     */
    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    /**
     * @return the request
     */
    public String getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public String toString() {

        return request + ", " + responseDate + ", " + ListRecords;
    }

}
