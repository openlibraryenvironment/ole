package org.kuali.ole.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.bo.explain.OleSRUExplainConfigDefaultTagField;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainConfigInfoDefaultConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        OleSRUExplainConfigDefaultTagField oleSRUExplainConfigDefaultTagField = (OleSRUExplainConfigDefaultTagField) o;
        hierarchicalStreamWriter.addAttribute("type", oleSRUExplainConfigDefaultTagField.getType());
        hierarchicalStreamWriter.setValue("" + oleSRUExplainConfigDefaultTagField.getValue());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        OleSRUExplainConfigDefaultTagField oleSRUExplainConfigDefaultTagField = new OleSRUExplainConfigDefaultTagField();
        oleSRUExplainConfigDefaultTagField.setType(hierarchicalStreamReader.getAttribute("type"));
        oleSRUExplainConfigDefaultTagField.setValue(Integer.parseInt(hierarchicalStreamReader.getValue()));
        return oleSRUExplainConfigDefaultTagField;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(OleSRUExplainConfigDefaultTagField.class);
    }
}
