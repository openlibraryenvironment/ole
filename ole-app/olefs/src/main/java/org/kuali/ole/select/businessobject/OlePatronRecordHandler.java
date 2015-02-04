/*
 * Copyright 2013 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.businessobject;

import com.thoughtworks.xstream.XStream;

public class OlePatronRecordHandler {

    public OlePatronDocuments retrievePatronFromXML(String xml) {
        XStream xStream = new XStream();
        xStream.alias("olePatronDocuments", OlePatronDocuments.class);
        xStream.alias("olePatronDocument", OLERequestorPatronDocument.class);
        xStream.alias("olePatronId", String.class);
        xStream.alias("barcode", String.class);
        xStream.alias("borrowerType", String.class);
        xStream.alias("firstName", String.class);
        xStream.alias("lastName", String.class);
        xStream.alias("activeIndicator", Boolean.class);
        xStream.addImplicitCollection(OlePatronDocuments.class, "olePatronDocuments");
        return (OlePatronDocuments) xStream.fromXML(xml);
    }
}
