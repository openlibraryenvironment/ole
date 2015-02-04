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
package org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an XML Record of category:Work, Type:Bibliographic, Format:DUBLIN.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibDublinRecord {

    private String schema = null;
    private List<DCValue> dcValues = new ArrayList<DCValue>();

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public List<DCValue> getDcValues() {
        return dcValues;
    }

    public void setDcValues(List<DCValue> dcValues) {
        this.dcValues = dcValues;
    }

    public void addDublinDCValue(DCValue dcValue) {
        this.dcValues.add(dcValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("schema: ");
        sb.append(schema);
        sb.append(";\n");
        for (DCValue dcValue : dcValues) {
            sb.append(dcValue);
            sb.append(";\n");
        }
        return sb.toString();
    }
}
