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
package org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

/**
 * Class to process Work Bib Dublin Records from and to XML.
 *
 * @author Rajesh Chowdary K
 */
public class BibDublinUnQualifiedRecordProcessor {
    /**
     * Method to covert xml content to BibDublinUnQualifiedRecordProcessor.
     *
     * @param fileContent
     * @return
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     * @throws javax.xml.xpath.XPathExpressionException
     */
    public BibDublinUnQualifiedRecord fromXML(String fileContent) {
//		fileContent=fileContent.replaceAll("&","&amp;");
//        fileContent=fileContent.replaceAll("< ","&lt; ");
        Object object = getXstream().fromXML(fileContent);
        return (BibDublinUnQualifiedRecord) object;
    }

    /**
     * Method to covert BibDublinUnQualifiedRecordProcessor to XML Format.
     *
     * @param rec
     * @return
     */
    public String toXml(BibDublinUnQualifiedRecord rec) {
        String xml = getXstream().toXML(rec);
        return xml;
    }

    private XStream getXstream() {
        XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("ddd", "_");
        XStream xStream = new XStream(new DomDriver("UTF-8", replacer));
        xStream.alias("OAI-PMH", BibDublinUnQualifiedRecord.class);
        xStream.alias("responseDate", String.class);
        xStream.alias("request", String.class);
        xStream.alias("ListRecords", ListRecords.class);
        xStream.alias("record", Record.class);
        xStream.alias("header", Header.class);
        xStream.alias("metadata", MetaData.class);
        xStream.alias("oai_dc:dc", OaiDcDoc.class);
        xStream.addImplicitCollection(ListRecords.class, "recordsList", Record.class);
        xStream.addImplicitCollection(MetaData.class, "oaiDcDocs", OaiDcDoc.class);
        xStream.registerConverter(new HeaderConverter());
        xStream.registerConverter(new OaiDcDocConverter());
        return xStream;
    }
}
