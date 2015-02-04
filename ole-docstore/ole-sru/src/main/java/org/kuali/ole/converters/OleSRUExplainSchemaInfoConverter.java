package org.kuali.ole.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.bo.explain.OleSRUExplainSchema;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainSchemaInfoConverter implements Converter {


    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        OleSRUExplainSchema oleSRUExplainSchema = (OleSRUExplainSchema) o;
        hierarchicalStreamWriter.addAttribute("name", oleSRUExplainSchema.getName());
        hierarchicalStreamWriter.addAttribute("identifier", oleSRUExplainSchema.getIdentifier());
        hierarchicalStreamWriter.setValue(oleSRUExplainSchema.getValue());
        hierarchicalStreamWriter.startNode("zr:title");
        hierarchicalStreamWriter.setValue(oleSRUExplainSchema.getTitle());
        hierarchicalStreamWriter.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        OleSRUExplainSchema oleSRUExplainSchema = new OleSRUExplainSchema();
        oleSRUExplainSchema.setName(hierarchicalStreamReader.getAttribute("name"));
        oleSRUExplainSchema.setIdentifier(hierarchicalStreamReader.getAttribute("identifier"));
        oleSRUExplainSchema.setValue(hierarchicalStreamReader.getValue());
        while (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            if ("zr:title".equals(hierarchicalStreamReader.getNodeName()))
                oleSRUExplainSchema.setTitle(hierarchicalStreamReader.getValue());
            hierarchicalStreamReader.moveUp();
        }
        return oleSRUExplainSchema;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(OleSRUExplainSchema.class);
    }
}
