package org.kuali.ole.docstore.model.xstream.ingest;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 3/7/12
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResponseDocumentConverter
        implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        ResponseDocument responseDocument = (ResponseDocument) o;
        if (responseDocument.getId() != null) {
            hierarchicalStreamWriter.addAttribute("id", responseDocument.getId());
        }
        if (responseDocument.getCategory() != null) {
            hierarchicalStreamWriter.addAttribute("category", responseDocument.getCategory());
        }
        if (responseDocument.getType() != null) {
            hierarchicalStreamWriter.addAttribute("type", responseDocument.getType());
        }
        if (responseDocument.getFormat() != null) {
            hierarchicalStreamWriter.addAttribute("format", responseDocument.getFormat());
        }
        if (responseDocument.getStatus() != null) {
            hierarchicalStreamWriter.startNode("status");
            hierarchicalStreamWriter.setValue(responseDocument.getStatus());
            hierarchicalStreamWriter.endNode();
        }
        if (responseDocument.getStatus() != null) {
            hierarchicalStreamWriter.startNode("statusMessage");
            hierarchicalStreamWriter.setValue(responseDocument.getStatusMessage());
            hierarchicalStreamWriter.endNode();
        }

        if (responseDocument.getUuid() != null) {
            hierarchicalStreamWriter.startNode("uuid");
            hierarchicalStreamWriter.setValue(responseDocument.getUuid());
            hierarchicalStreamWriter.endNode();
        }
        if (responseDocument.getContent() != null) {
            hierarchicalStreamWriter.startNode("content");
            hierarchicalStreamWriter.setValue(responseDocument.getContent().getContent());
            hierarchicalStreamWriter.endNode();
        }

        if (responseDocument.getDocumentName() != null) {
            hierarchicalStreamWriter.startNode("documentName");
            hierarchicalStreamWriter.setValue(responseDocument.getDocumentName());
            hierarchicalStreamWriter.endNode();
        }
        if (responseDocument.getDocumentTitle() != null) {
            hierarchicalStreamWriter.startNode("documentTitle");
            hierarchicalStreamWriter.setValue(responseDocument.getDocumentTitle());
            hierarchicalStreamWriter.endNode();
        }
        if (responseDocument.getDocumentMimeType() != null) {
            hierarchicalStreamWriter.startNode("documentMimeType");
            hierarchicalStreamWriter.setValue(responseDocument.getDocumentMimeType());
            hierarchicalStreamWriter.endNode();
        }
        if (responseDocument.getVersion() != null) {
            hierarchicalStreamWriter.startNode("version");
            hierarchicalStreamWriter.setValue(responseDocument.getVersion());
            hierarchicalStreamWriter.endNode();
        }


        if (responseDocument.getAdditionalAttributes() != null) {
            AdditionalAttributeConverter addConverter = new AdditionalAttributeConverter();
            AdditionalAttributes additionalAttributes = responseDocument.getAdditionalAttributes();
            addConverter.marshal(additionalAttributes, hierarchicalStreamWriter, marshallingContext);
        }

        List<ResponseDocument> linkedResponseDocuments = responseDocument.getLinkedDocuments();
        if (linkedResponseDocuments != null && linkedResponseDocuments.size() > 0) {
            hierarchicalStreamWriter.startNode("linkedDocuments");
            for (Iterator<ResponseDocument> iterator = linkedResponseDocuments.iterator(); iterator.hasNext(); ) {
                hierarchicalStreamWriter.startNode("linkedDocument");
                ResponseDocument respDoc = iterator.next();
                marshal(respDoc, hierarchicalStreamWriter, marshallingContext);
                List<ResponseDocument> instanceResposeDocuments = responseDocument.getLinkedInstanceDocuments();
                if (instanceResposeDocuments != null && instanceResposeDocuments.size() > 0) {
                    hierarchicalStreamWriter.startNode("linkedDocuments");
                    for (Iterator<ResponseDocument> instanceIterator = instanceResposeDocuments
                            .iterator(); instanceIterator.hasNext(); ) {
                        hierarchicalStreamWriter.startNode("linkedDocument");
                        ResponseDocument instanceDoc = instanceIterator.next();
                        marshalInstance(instanceDoc, hierarchicalStreamWriter, marshallingContext);
                        hierarchicalStreamWriter.endNode();
                    }
                    hierarchicalStreamWriter.endNode();
                }
                hierarchicalStreamWriter.endNode();
            }
            hierarchicalStreamWriter.endNode();
        }
    }

    private void setAdditionalAttributesObject(HierarchicalStreamReader hierarchicalStreamReader,
                                               ResponseDocument responseDocument,
                                               UnmarshallingContext unmarshallingContext) {
        AdditionalAttributeConverter addConverter = new AdditionalAttributeConverter();
        AdditionalAttributes additionalAttributes = (AdditionalAttributes) addConverter
                .unmarshal(hierarchicalStreamReader, unmarshallingContext);
        responseDocument.setAdditionalAttributes(additionalAttributes);
    }

    private void marshalInstance(ResponseDocument instanceDoc, HierarchicalStreamWriter hierarchicalStreamWriter,
                                 MarshallingContext marshallingContext) {
        ResponseDocument responseDocument = (ResponseDocument) instanceDoc;
        hierarchicalStreamWriter.addAttribute("id", responseDocument.getId());
        hierarchicalStreamWriter.addAttribute("category", responseDocument.getCategory());
        hierarchicalStreamWriter.addAttribute("type", responseDocument.getType());
        hierarchicalStreamWriter.addAttribute("format", responseDocument.getFormat());
        hierarchicalStreamWriter.startNode("content");
        hierarchicalStreamWriter.setValue(responseDocument.getContent().getContent());
        hierarchicalStreamWriter.endNode();
        hierarchicalStreamWriter.startNode("uuid");
        hierarchicalStreamWriter.setValue(responseDocument.getUuid());
        hierarchicalStreamWriter.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {

        ResponseDocument responseDocument = new ResponseDocument();
        responseDocument.setId(hierarchicalStreamReader.getAttribute("id"));
        responseDocument.setCategory(hierarchicalStreamReader.getAttribute("category"));
        responseDocument.setType(hierarchicalStreamReader.getAttribute("type"));
        responseDocument.setFormat(hierarchicalStreamReader.getAttribute("format"));


        while (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            if (hierarchicalStreamReader.getNodeName().equals("status")) {
                responseDocument.setStatus(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("statusMessage")) {
                responseDocument.setStatusMessage(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("uuid")) {
                responseDocument.setUuid(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("content")) {
                String value = hierarchicalStreamReader.getValue();
                Content content = new Content(value);
                responseDocument.setContent(content);
            } else if (hierarchicalStreamReader.getNodeName().equals("documentName")) {
                responseDocument.setDocumentName(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("documentTitle")) {
                responseDocument.setDocumentTitle(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("documentMimeType")) {
                responseDocument.setDocumentMimeType(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("additionalAttributes")) {
                setAdditionalAttributesObject(hierarchicalStreamReader, responseDocument, unmarshallingContext);
            } else if (hierarchicalStreamReader.getNodeName().equals("version")) {
                responseDocument.setVersion(hierarchicalStreamReader.getValue());
            } else if (hierarchicalStreamReader.getNodeName().equals("linkedDocuments")) {
                while (hierarchicalStreamReader.hasMoreChildren()) {
                    hierarchicalStreamReader.moveDown();
                    responseDocument.addLinkedDocument(
                            (ResponseDocument) unmarshal(hierarchicalStreamReader, unmarshallingContext));
                    hierarchicalStreamReader.moveUp();
                }
            }
            hierarchicalStreamReader.moveUp();
        }
        /*


                hierarchicalStreamReader.moveDown();
                if (hierarchicalStreamReader.getNodeName().equals("uuid")) {
                    String value = hierarchicalStreamReader.getValue();
                    responseDocument.setUuid(value);
                }

                if (hierarchicalStreamReader.getNodeName().equals("content")) {
                    String value = hierarchicalStreamReader.getValue();
                    Content content = new Content(value);
                    responseDocument.setContent(content);
                }

                 hierarchicalStreamReader.moveUp();
                if(hierarchicalStreamReader.getNodeName().equals("documentName"))
                {
                   responseDocument.setDocumentName( hierarchicalStreamReader.getValue());
                }
                 if(hierarchicalStreamReader.getNodeName().equals("documentTitle"))
                {
                   responseDocument.setDocumentTitle( hierarchicalStreamReader.getValue());
                }
                 if(hierarchicalStreamReader.getNodeName().equals("documentMimeType"))
                {
                   responseDocument.setDocumentMimeType( hierarchicalStreamReader.getValue());
                }
                hierarchicalStreamReader.moveDown();

                hierarchicalStreamReader.moveUp();
                if (hierarchicalStreamReader.hasMoreChildren()) {
                    hierarchicalStreamReader.moveDown();
                    if (hierarchicalStreamReader.getNodeName().equals("linkedDocuments")) {
                        while (hierarchicalStreamReader.hasMoreChildren()) {
                            hierarchicalStreamReader.moveDown();
                            responseDocument.addLinkedDocument(
                                    (ResponseDocument) unmarshal(hierarchicalStreamReader, unmarshallingContext));
                            hierarchicalStreamReader.moveUp();
                        }
                    }
                    hierarchicalStreamReader.moveUp();
                }
        */
        return responseDocument;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(ResponseDocument.class);
    }
}
