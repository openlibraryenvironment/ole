package org.kuali.ole.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.bo.explain.OleSRUExplainIndexMapName;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainIndexMapNameConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        OleSRUExplainIndexMapName oleSRUExplainIndexMapName = (OleSRUExplainIndexMapName) o;
        hierarchicalStreamWriter.addAttribute("set", oleSRUExplainIndexMapName.getSet());
        hierarchicalStreamWriter.setValue(oleSRUExplainIndexMapName.getValue());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        OleSRUExplainIndexMapName oleSRUExplainIndexMapName = new OleSRUExplainIndexMapName();
        oleSRUExplainIndexMapName.setSet(hierarchicalStreamReader.getAttribute("set"));
        oleSRUExplainIndexMapName.setValue(hierarchicalStreamReader.getValue());
        return oleSRUExplainIndexMapName;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(OleSRUExplainIndexMapName.class);
    }
}
