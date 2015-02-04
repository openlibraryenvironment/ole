package org.kuali.ole.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.bo.explain.OleSRUExplainIndexSet;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainIndexInfoConverter implements Converter {


    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        OleSRUExplainIndexSet oleSRUExplainIndexSet = (OleSRUExplainIndexSet) o;
        hierarchicalStreamWriter.addAttribute("name", oleSRUExplainIndexSet.getName());
        hierarchicalStreamWriter.addAttribute("identifier", oleSRUExplainIndexSet.getIdentifier());
        hierarchicalStreamWriter.setValue(oleSRUExplainIndexSet.getValue());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        OleSRUExplainIndexSet oleSRUExplainIndexSet = new OleSRUExplainIndexSet();
        oleSRUExplainIndexSet.setName(hierarchicalStreamReader.getAttribute("name"));
        oleSRUExplainIndexSet.setIdentifier(hierarchicalStreamReader.getAttribute("identifier"));
        oleSRUExplainIndexSet.setValue(hierarchicalStreamReader.getValue());
        return oleSRUExplainIndexSet;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(OleSRUExplainIndexSet.class);
    }
}
