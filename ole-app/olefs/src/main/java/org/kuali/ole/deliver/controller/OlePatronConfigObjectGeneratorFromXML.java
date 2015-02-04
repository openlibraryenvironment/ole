package org.kuali.ole.deliver.controller;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.deliver.bo.OleConfigDocument;
import org.kuali.ole.deliver.bo.OlePatronConfig;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronConfigObjectGeneratorFromXML {
    /**
     * This method returns PatronConfig object from fileContent.
     * The xStream will convert the fileContent into PatronConfig Object.
     *
     * @param fileContent
     * @return PatronConfig object
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */

    public OlePatronConfig buildKrmsFromFileContent(String fileContent) throws URISyntaxException, IOException {
        XStream xStream = new XStream();
        xStream.alias("patronConfig", OlePatronConfig.class);
        xStream.alias("document", OleConfigDocument.class);
        Object object = xStream.fromXML(fileContent);
        return (OlePatronConfig) object;
    }
}
