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
package org.kuali.ole.docstore.model.xstream.ingest;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/9/11
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class RequestDocumentConverter
        implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        AdditionalAttributeConverter addConverter = new AdditionalAttributeConverter();
        RequestDocument requestDocument = (RequestDocument) o;
        if (requestDocument.getId() != null) {
            hierarchicalStreamWriter.addAttribute("id", requestDocument.getId());
        }
        if (requestDocument.getCategory() != null) {
            hierarchicalStreamWriter.addAttribute("category", requestDocument.getCategory());
        }
        if (requestDocument.getType() != null) {
            hierarchicalStreamWriter.addAttribute("type", requestDocument.getType());
        }
        if (requestDocument.getFormat() != null) {
            hierarchicalStreamWriter.addAttribute("format", requestDocument.getFormat());
        }

        if (requestDocument.getContent() != null && requestDocument.getContent().getContent() != null) {
            hierarchicalStreamWriter.startNode("content");
            hierarchicalStreamWriter.setValue(requestDocument.getContent().getContent());
            hierarchicalStreamWriter.endNode();
        }

        //for License
        if (requestDocument.getUuid() != null) {
            hierarchicalStreamWriter.startNode("uuid");
            hierarchicalStreamWriter.setValue(requestDocument.getUuid());
            hierarchicalStreamWriter.endNode();
        }
        if (requestDocument.getDocumentName() != null) {
            hierarchicalStreamWriter.startNode("documentName");
            hierarchicalStreamWriter.setValue(requestDocument.getDocumentName());
            hierarchicalStreamWriter.endNode();
        }
        if (requestDocument.getDocumentTitle() != null) {
            hierarchicalStreamWriter.startNode("documentTitle");
            hierarchicalStreamWriter.setValue(requestDocument.getDocumentTitle());
            hierarchicalStreamWriter.endNode();
        }
        if (requestDocument.getDocumentMimeType() != null) {
            hierarchicalStreamWriter.startNode("documentMimeType");
            hierarchicalStreamWriter.setValue(requestDocument.getDocumentMimeType());
            hierarchicalStreamWriter.endNode();
        }

        //ends here...

        AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
        addConverter.marshal(additionalAttributes, hierarchicalStreamWriter, marshallingContext);

        List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
        if (linkedRequestDocuments != null && linkedRequestDocuments.size() > 0) {
            hierarchicalStreamWriter.startNode("linkedIngestDocuments");

            for (Iterator<RequestDocument> iterator = linkedRequestDocuments.iterator(); iterator.hasNext(); ) {
                hierarchicalStreamWriter.startNode("linkedIngestDocument");
                RequestDocument requestDoc = iterator.next();
                marshal(requestDoc, hierarchicalStreamWriter, marshallingContext);
                hierarchicalStreamWriter.endNode();
            }

            hierarchicalStreamWriter.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setId(hierarchicalStreamReader.getAttribute("id"));
        requestDocument.setCategory(hierarchicalStreamReader.getAttribute("category"));
        requestDocument.setType(hierarchicalStreamReader.getAttribute("type"));
        requestDocument.setFormat(hierarchicalStreamReader.getAttribute("format"));
        while (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();

            if (hierarchicalStreamReader.getNodeName().equals("uuid")) {
                requestDocument.setUuid(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("documentName")) {
                requestDocument.setDocumentName(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("documentTitle")) {
                requestDocument.setDocumentTitle(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("documentMimeType")) {
                requestDocument.setDocumentMimeType(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("content")) {
                setContentObject(hierarchicalStreamReader, requestDocument);
            } else if (hierarchicalStreamReader.getNodeName().equals("additionalAttributes")) {
                setAdditionalAttributesObject(hierarchicalStreamReader, requestDocument, unmarshallingContext);
            } else if (hierarchicalStreamReader.getNodeName().equals("linkedIngestDocuments")) {
                setLinkedRequestDocuments(hierarchicalStreamReader, requestDocument, unmarshallingContext);
            }
            hierarchicalStreamReader.moveUp();
        }
/*
         if (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            if (hierarchicalStreamReader.getNodeName().equals("additionalAttributes")) {
                setAdditionalAttributesObject(hierarchicalStreamReader, requestDocument);
            }
            if (hierarchicalStreamReader.getNodeName().equals("linkedIngestDocuments")) {
                setLinkedRequestDocuments(hierarchicalStreamReader, requestDocument, unmarshallingContext);
            }
            hierarchicalStreamReader.moveUp();
        }
        if (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            if (hierarchicalStreamReader.getNodeName().equals("linkedIngestDocuments")) {
                setLinkedRequestDocuments(hierarchicalStreamReader, requestDocument, unmarshallingContext);
            }
            hierarchicalStreamReader.moveUp();
        }
*/

        return requestDocument;
    }


    private void setLinkedRequestDocuments(HierarchicalStreamReader hierarchicalStreamReader,
                                           RequestDocument requestDocument, UnmarshallingContext unmarshallingContext) {
        while (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            requestDocument.addLinkedRequestDocument((RequestDocument) unmarshal(hierarchicalStreamReader, unmarshallingContext));
            hierarchicalStreamReader.moveUp();
        }

    }

    private void setAdditionalAttributesObject(HierarchicalStreamReader hierarchicalStreamReader,
                                               RequestDocument requestDocument,
                                               UnmarshallingContext unmarshallingContext) {
        AdditionalAttributeConverter addConverter = new AdditionalAttributeConverter();
        AdditionalAttributes additionalAttributes = (AdditionalAttributes) addConverter
                .unmarshal(hierarchicalStreamReader, unmarshallingContext);

        /*
                    if (hierarchicalStreamReader.getNodeName().equals("dateEntered")) {
                        additionalAttributes.setDateEntered(hierarchicalStreamReader.getValue());
                    }
                    else if (hierarchicalStreamReader.getNodeName().equals("lastUpdated")) {
                        additionalAttributes.setLastUpdated(hierarchicalStreamReader.getValue());
                    }
                    else if (hierarchicalStreamReader.getNodeName().equals("fastAddFlag")) {
                        additionalAttributes.setFastAddFlag(hierarchicalStreamReader.getValue());
                    }
                    else if (hierarchicalStreamReader.getNodeName().equals("supressFromPublic")) {
                        additionalAttributes.setSupressFromPublic(hierarchicalStreamReader.getValue());
                    }
                    else if (hierarchicalStreamReader.getNodeName().equals("harvestable")) {
                        additionalAttributes.setHarvestable(hierarchicalStreamReader.getValue());
                    }
                    else if (hierarchicalStreamReader.getNodeName().equals("status")) {
                        additionalAttributes.setStatus(hierarchicalStreamReader.getValue());
                    }
        */
        requestDocument.setAdditionalAttributes(additionalAttributes);
    }


    private void setContentObject(HierarchicalStreamReader hierarchicalStreamReader, RequestDocument requestDocument) {
        String value = hierarchicalStreamReader.getValue();
        if (isValidXml(value)) {
            Content content = new Content(value);
            requestDocument.setContent(content);
        }
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(RequestDocument.class);
    }

    public boolean isValidXml(String xmlContent) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlContent));
            builder.parse(is);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid XML content in RequestDocument. Try to replace (if any) '&' with '&amp;' and '<' with '&lt;'");
        }
    }
}
