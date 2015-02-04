package org.kuali.ole.docstore.model.xstream.ingest;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 2/28/12
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class IngestDocumentConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        RequestDocument requestDocument = (RequestDocument) o;
        hierarchicalStreamWriter.addAttribute("id", requestDocument.getId());
        hierarchicalStreamWriter.addAttribute("category", requestDocument.getCategory());
        hierarchicalStreamWriter.addAttribute("type", requestDocument.getType());
        hierarchicalStreamWriter.addAttribute("format", requestDocument.getFormat());
        hierarchicalStreamWriter.startNode("content");
        hierarchicalStreamWriter.setValue(requestDocument.getContent().getContent());
        hierarchicalStreamWriter.endNode();
        AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
        if (additionalAttributes != null) {
            hierarchicalStreamWriter.startNode("additionalAttributes");
            hierarchicalStreamWriter.startNode("dateEntered");
            if (additionalAttributes.getDateEntered() != null)
                hierarchicalStreamWriter.setValue(additionalAttributes.getDateEntered());
            hierarchicalStreamWriter.endNode();
            hierarchicalStreamWriter.startNode("lastUpdated");
            if (additionalAttributes.getLastUpdated() != null)
                hierarchicalStreamWriter.setValue(additionalAttributes.getLastUpdated());
            hierarchicalStreamWriter.endNode();
            hierarchicalStreamWriter.startNode("fastAddFlag");
            if (additionalAttributes.getFastAddFlag() != null)
                hierarchicalStreamWriter.setValue(additionalAttributes.getFastAddFlag());
            hierarchicalStreamWriter.endNode();
            hierarchicalStreamWriter.startNode("supressFromPublic");
            if (additionalAttributes.getSupressFromPublic() != null)
                hierarchicalStreamWriter.setValue(additionalAttributes.getSupressFromPublic());
            hierarchicalStreamWriter.endNode();
            hierarchicalStreamWriter.startNode("harvestable");
            if (additionalAttributes.getHarvestable() != null)
                hierarchicalStreamWriter.setValue(additionalAttributes.getHarvestable());
            hierarchicalStreamWriter.endNode();
            hierarchicalStreamWriter.startNode("status");
            if (additionalAttributes.getStatus() != null)
                hierarchicalStreamWriter.setValue(additionalAttributes.getStatus());
            hierarchicalStreamWriter.endNode();
            hierarchicalStreamWriter.endNode();
        }
        List<RequestDocument> linkedRequestDocuments = requestDocument.getLinkedRequestDocuments();
        if (linkedRequestDocuments != null && linkedRequestDocuments.size() > 0) {
            hierarchicalStreamWriter.startNode("linkedIngestDocuments");
            hierarchicalStreamWriter.startNode("linkedIngestDocument");
            for (Iterator<RequestDocument> iterator = linkedRequestDocuments.iterator(); iterator.hasNext(); ) {
                RequestDocument requestDoc = iterator.next();
                marshal(requestDoc, hierarchicalStreamWriter, marshallingContext);
            }
            hierarchicalStreamWriter.endNode();
            hierarchicalStreamWriter.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        RequestDocument requestDocument = new RequestDocument();
        requestDocument.setId(hierarchicalStreamReader.getAttribute("id"));
        requestDocument.setCategory(hierarchicalStreamReader.getAttribute("category"));
        requestDocument.setType(hierarchicalStreamReader.getAttribute("type"));
        requestDocument.setFormat(hierarchicalStreamReader.getAttribute("format"));
        hierarchicalStreamReader.moveDown();
        if (hierarchicalStreamReader.getNodeName().equals("content")) {
            String value = hierarchicalStreamReader.getValue();
            Content content = new Content(value);
            requestDocument.setContent(content);
        }
        hierarchicalStreamReader.moveUp();
        if (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            if (hierarchicalStreamReader.getNodeName().equals("additionalAttributes")) {
                AdditionalAttributes additionalAttributes = new AdditionalAttributes();
                while (hierarchicalStreamReader.hasMoreChildren()) {
                    hierarchicalStreamReader.moveDown();
                    if (hierarchicalStreamReader.getNodeName().equals("dateEntered")) {
                        additionalAttributes.setDateEntered(hierarchicalStreamReader.getValue());
                    } else if (hierarchicalStreamReader.getNodeName().equals("dateLastUpdated")) {
                        additionalAttributes.setLastUpdated(hierarchicalStreamReader.getValue());
                    } else if (hierarchicalStreamReader.getNodeName().equals("fastAddFlag")) {
                        additionalAttributes.setFastAddFlag(hierarchicalStreamReader.getValue());
                    } else if (hierarchicalStreamReader.getNodeName().equals("supressFromPublic")) {
                        additionalAttributes.setSupressFromPublic(hierarchicalStreamReader.getValue());
                    } else if (hierarchicalStreamReader.getNodeName().equals("harvestable")) {
                        additionalAttributes.setHarvestable(hierarchicalStreamReader.getValue());
                    } else if (hierarchicalStreamReader.getNodeName().equals("status")) {
                        additionalAttributes.setStatus(hierarchicalStreamReader.getValue());
                    }
                    hierarchicalStreamReader.moveUp();
                }
                requestDocument.setAdditionalAttributes(additionalAttributes);

            }
            hierarchicalStreamReader.moveUp();
        }
        if (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            if (hierarchicalStreamReader.getNodeName().equals("linkedIngestDocuments")) {
                while (hierarchicalStreamReader.hasMoreChildren()) {
                    hierarchicalStreamReader.moveDown();
                    requestDocument.addLinkedRequestDocument((RequestDocument) unmarshal(hierarchicalStreamReader,
                            unmarshallingContext));
                    hierarchicalStreamReader.moveUp();
                }
            }
            hierarchicalStreamReader.moveUp();
        }
        return requestDocument;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(RequestDocument.class);
    }
}
