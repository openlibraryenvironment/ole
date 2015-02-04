package org.kuali.ole.docstore.common.document.content.instance.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.common.document.content.instance.Uri;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/22/12
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class UriConverter implements Converter {

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Uri uri = (Uri) source;
        if (uri.getResolvable() != null) {
            writer.addAttribute("resolvable", uri.getResolvable());
        }
        if (uri.getValue() != null) {
            writer.setValue(uri.getValue());
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Uri uri = new Uri();
        uri.setResolvable(reader.getAttribute("resolvable"));
        uri.setValue(reader.getValue());
        return uri;
    }

    @Override
    public boolean canConvert(Class uriClass) {
        return uriClass.equals(Uri.class);
    }
}