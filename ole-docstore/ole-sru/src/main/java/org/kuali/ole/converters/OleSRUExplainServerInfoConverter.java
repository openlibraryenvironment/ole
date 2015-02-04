package org.kuali.ole.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.bo.explain.OleSRUExplainServerInfo;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainServerInfoConverter implements Converter {


    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
                        MarshallingContext marshallingContext) {
        OleSRUExplainServerInfo oleSRUExplainServerInfo = (OleSRUExplainServerInfo) o;
        hierarchicalStreamWriter.addAttribute("protocol", oleSRUExplainServerInfo.getProtocol() == null ? "" : oleSRUExplainServerInfo.getProtocol());
        hierarchicalStreamWriter.addAttribute("version", oleSRUExplainServerInfo.getVersion() == null ? "" : oleSRUExplainServerInfo.getVersion());
        hierarchicalStreamWriter.addAttribute("transport", oleSRUExplainServerInfo.getTransport() == null ? "" : oleSRUExplainServerInfo.getTransport());
        hierarchicalStreamWriter.addAttribute("method", oleSRUExplainServerInfo.getMethod() == null ? "" : oleSRUExplainServerInfo.getMethod());
        hierarchicalStreamWriter.setValue(oleSRUExplainServerInfo.getValue());
        hierarchicalStreamWriter.startNode("zr:host");
        hierarchicalStreamWriter.setValue(oleSRUExplainServerInfo.getHost());
        hierarchicalStreamWriter.endNode();
        hierarchicalStreamWriter.startNode("zr:port");
        hierarchicalStreamWriter.setValue(oleSRUExplainServerInfo.getPort());
        hierarchicalStreamWriter.endNode();
        hierarchicalStreamWriter.startNode("zr:database");
        hierarchicalStreamWriter.setValue(oleSRUExplainServerInfo.getDatabase());
        hierarchicalStreamWriter.endNode();

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader,
                            UnmarshallingContext unmarshallingContext) {
        // System.out.println("datafield unmarshal");
        OleSRUExplainServerInfo oleSRUExplainServerInfo = new OleSRUExplainServerInfo();
        oleSRUExplainServerInfo.setProtocol(hierarchicalStreamReader.getAttribute("protocol"));
        oleSRUExplainServerInfo.setVersion(hierarchicalStreamReader.getAttribute("version"));
        oleSRUExplainServerInfo.setTransport(hierarchicalStreamReader.getAttribute("transport"));
        oleSRUExplainServerInfo.setMethod(hierarchicalStreamReader.getAttribute("method"));
        oleSRUExplainServerInfo.setValue(hierarchicalStreamReader.getValue());
        while (hierarchicalStreamReader.hasMoreChildren()) {
            hierarchicalStreamReader.moveDown();
            if ("zr:host".equals(hierarchicalStreamReader.getNodeName()))
                oleSRUExplainServerInfo.setHost(hierarchicalStreamReader.getValue());
            else if ("zr:port".equals(hierarchicalStreamReader.getNodeName()))
                oleSRUExplainServerInfo.setPort(hierarchicalStreamReader.getValue());
            else if ("zr:database".equals(hierarchicalStreamReader.getNodeName()))
                oleSRUExplainServerInfo.setDatabase(hierarchicalStreamReader.getValue());
            hierarchicalStreamReader.moveUp();
        }
        return oleSRUExplainServerInfo;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(OleSRUExplainServerInfo.class);
    }
}
