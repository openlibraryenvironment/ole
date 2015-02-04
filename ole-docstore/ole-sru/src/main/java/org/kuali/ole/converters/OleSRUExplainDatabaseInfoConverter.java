package org.kuali.ole.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.bo.explain.OleSRUExplainDatabaseTitle;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainDatabaseInfoConverter implements Converter {


    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        OleSRUExplainDatabaseTitle oleSRUExplainDatabaseTitle = (OleSRUExplainDatabaseTitle) o;
        hierarchicalStreamWriter.addAttribute("lang", oleSRUExplainDatabaseTitle.getLang());
        hierarchicalStreamWriter.addAttribute("primary", oleSRUExplainDatabaseTitle.getPrimary());
        hierarchicalStreamWriter.setValue(oleSRUExplainDatabaseTitle.getValue());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        // System.out.println("datafield unmarshal");
        OleSRUExplainDatabaseTitle oleSRUExplainDatabaseTitle = new OleSRUExplainDatabaseTitle();
        oleSRUExplainDatabaseTitle.setLang(hierarchicalStreamReader.getAttribute("lang"));
        oleSRUExplainDatabaseTitle.setPrimary(hierarchicalStreamReader.getAttribute("primary"));
        oleSRUExplainDatabaseTitle.setValue(hierarchicalStreamReader.getValue());
        return oleSRUExplainDatabaseTitle;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(OleSRUExplainDatabaseTitle.class);
    }
}
