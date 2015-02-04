package org.kuali.ole.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.bo.explain.OleSRUExplainConfigDefaultTagField;
import org.kuali.ole.bo.explain.OleSRUExplainConfigSupportTagField;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainConfigInfoSupportConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        OleSRUExplainConfigSupportTagField oleSRUExplainConfigSupportTagField = (OleSRUExplainConfigSupportTagField) o;
        hierarchicalStreamWriter.addAttribute("type", oleSRUExplainConfigSupportTagField.getType());
        hierarchicalStreamWriter.setValue(oleSRUExplainConfigSupportTagField.getValue());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        OleSRUExplainConfigSupportTagField oleSRUExplainConfigSupportTagField = new OleSRUExplainConfigSupportTagField();
        oleSRUExplainConfigSupportTagField.setType(hierarchicalStreamReader.getAttribute("type"));
        oleSRUExplainConfigSupportTagField.setValue(hierarchicalStreamReader.getValue());
        return oleSRUExplainConfigSupportTagField;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(OleSRUExplainConfigSupportTagField.class);
    }
}
