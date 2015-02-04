package org.kuali.ole.docstore.common.document.content.bib.marc.xstream;


import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;


/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 10/5/11
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubFieldConverter
        implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        SubField marcSubField = (SubField) o;
        hierarchicalStreamWriter.addAttribute("code", marcSubField.getCode());
        hierarchicalStreamWriter.setValue(marcSubField.getValue());

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        SubField marcSubField = new SubField();
        marcSubField.setCode(hierarchicalStreamReader.getAttribute("code"));
        marcSubField.setValue(hierarchicalStreamReader.getValue());
        return marcSubField;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(SubField.class);
    }
}
