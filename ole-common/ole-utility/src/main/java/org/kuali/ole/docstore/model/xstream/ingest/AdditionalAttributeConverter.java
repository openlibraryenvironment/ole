package org.kuali.ole.docstore.model.xstream.ingest;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/13/12
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdditionalAttributeConverter implements Converter {


    @Override
    public void marshal(Object obj, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext context) {
        AdditionalAttributes additionalAttributes = (AdditionalAttributes) obj;
        if (additionalAttributes != null) {
            hierarchicalStreamWriter.startNode("additionalAttributes");
            Collection<String> attributeNames = additionalAttributes.getAttributeNames();
            if (attributeNames != null && attributeNames.size() > 0) {
                for (Iterator<String> iterator = attributeNames.iterator(); iterator.hasNext(); ) {
                    String name = iterator.next();
                    String value = additionalAttributes.getAttribute(name);
                    hierarchicalStreamWriter.startNode(name);
                    hierarchicalStreamWriter.setValue(value);
                    hierarchicalStreamWriter.endNode();
                }
            }
            hierarchicalStreamWriter.endNode();
        }

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext context) {
        AdditionalAttributes addAttributes = new AdditionalAttributes();
        while (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            String key = hierarchicalStreamReader.getNodeName();
            String value = hierarchicalStreamReader.getValue();
            addAttributes.setAttribute(key, value);
            hierarchicalStreamReader.moveUp();
        }
        return addAttributes;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(AdditionalAttributes.class);
    }


}
