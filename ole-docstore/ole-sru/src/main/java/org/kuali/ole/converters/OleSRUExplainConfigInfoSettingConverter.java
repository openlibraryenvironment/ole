package org.kuali.ole.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.bo.explain.OleSRUExplainConfigDefaultTagField;
import org.kuali.ole.bo.explain.OleSRUExplainConfigSettingTagField;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainConfigInfoSettingConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        OleSRUExplainConfigSettingTagField oleSRUExplainConfigSettingTagField = (OleSRUExplainConfigSettingTagField) o;
        hierarchicalStreamWriter.addAttribute("type", oleSRUExplainConfigSettingTagField.getType());
        hierarchicalStreamWriter.setValue("" + oleSRUExplainConfigSettingTagField.getValue());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        OleSRUExplainConfigSettingTagField oleSRUExplainConfigSettingTagField = new OleSRUExplainConfigSettingTagField();
        oleSRUExplainConfigSettingTagField.setType(hierarchicalStreamReader.getAttribute("type"));
        oleSRUExplainConfigSettingTagField.setValue(Integer.parseInt(hierarchicalStreamReader.getValue()));
        return oleSRUExplainConfigSettingTagField;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(OleSRUExplainConfigSettingTagField.class);
    }
}
