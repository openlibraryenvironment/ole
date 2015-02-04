package org.kuali.ole.docstore.model.xstream.work.bib.marc;


import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;


/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 10/6/11
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class ControlFieldConverter
        implements Converter {
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        //System.out.println("ControlFieldConverter marshal");
        ControlField marcControlField = (ControlField) source;
        writer.addAttribute("tag", marcControlField.getTag());
        writer.setValue(marcControlField.getValue());

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        ControlField marcControlField = new ControlField();
        marcControlField.setTag(reader.getAttribute("tag"));
        marcControlField.setValue((reader.getValue()));
        return marcControlField;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(ControlField.class);
    }
}
