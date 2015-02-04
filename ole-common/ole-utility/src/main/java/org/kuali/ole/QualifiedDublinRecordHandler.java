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
package org.kuali.ole;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.DCValue;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.WorkBibDublinRecord;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.DCValueConverter;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.WorkBibDublinRecordConverter;

/**
 * Class to process Work Bib Dublin Records from and to XML.
 *
 * @author Rajesh Chowdary K
 */
public class QualifiedDublinRecordHandler {
    /**
     * Method to covert xml content to QualifiedDublinRecord.
     *
     * @param fileContent
     * @return
     */
    public WorkBibDublinRecord fromXML(String fileContent) {
        //throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        XStream xStream = new XStream();
        xStream.alias("dublin_core", WorkBibDublinRecord.class);
        xStream.alias("dcvalue", DCValue.class);
        xStream.addImplicitCollection(WorkBibDublinRecord.class, "dcValues", DCValue.class);
        xStream.registerConverter(new WorkBibDublinRecordConverter());
        xStream.registerConverter(new DCValueConverter());

        return (WorkBibDublinRecord) xStream.fromXML(fileContent);
    }

    /**
     * Method to covert QualifiedDublinRecord to XML Format.
     *
     * @param rec
     * @return
     */
    public String toXml(WorkBibDublinRecord rec) {
        XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("ddd", "_");
        XStream xStream = new XStream(new DomDriver("UTF-8", replacer));
        xStream.alias("dublin_core", WorkBibDublinRecord.class);
        xStream.alias("dcvalue", DCValue.class);
        xStream.addImplicitCollection(WorkBibDublinRecord.class, "dcValues", DCValue.class);
        xStream.registerConverter(new WorkBibDublinRecordConverter());
        xStream.registerConverter(new DCValueConverter());
        return xStream.toXML(rec);
    }

}
