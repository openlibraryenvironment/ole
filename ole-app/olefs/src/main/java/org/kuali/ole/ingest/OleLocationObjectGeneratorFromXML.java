package org.kuali.ole.ingest;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.ingest.pojo.OleLocationGroup;
import org.kuali.ole.ingest.pojo.OleLocationIngest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * OleLocationObjectGeneratorFromXML is used as a converter which converts xml string to
 * OleLocationGroup object and OleLocationGroup object to xml string
 */
public class OleLocationObjectGeneratorFromXML {
    /**
     *  This method returns OleLocationGroup.
     *  This method build the LocationGroup based on file content.
     * @param fileContent
     * @return OleLocationGroup
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
   public OleLocationGroup buildLocationFromFileContent(String fileContent) throws URISyntaxException, IOException {

        XStream xStream = new XStream();
        xStream.alias("locationGroup", OleLocationGroup.class);
        xStream.alias("location", OleLocationIngest.class);
        xStream.addImplicitCollection(OleLocationGroup.class,"locationGroup");
        Object object =  xStream.fromXML(fileContent);
        return (OleLocationGroup)object;
   }

    /**
     *   This method  returns xml.
     *   This method  convert the oleLocationIngestList into xml.
     * @param oleLocationIngestList
     * @return  stringBuffer
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    public String toXML(List<OleLocationIngest> oleLocationIngestList) throws URISyntaxException, IOException {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<locationGroup xmlns=\"http://ole.kuali.org/standards/ole-location\">");
        stringBuffer.append("\n");
        for(OleLocationIngest oleLocationIngest : oleLocationIngestList){
            XStream xStream = new XStream();
            xStream.alias("location", OleLocationIngest.class);
            xStream.addImplicitCollection(OleLocationGroup.class,"locationGroup");
            String xml = xStream.toXML(oleLocationIngest);
            stringBuffer.append(xml);
            stringBuffer.append("\n");
        }
        stringBuffer.append("</locationGroup>");
        return stringBuffer.toString();
    }
}
