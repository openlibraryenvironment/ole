package org.kuali.ole.converter;

import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.io.StreamUtils;
import org.milyn.payload.StringResult;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 2/29/12
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEDIConverter {
    private Smooks smooks;

    public OLEEDIConverter() {
        try {
            String configurationFileName = getConfigurationFileName();
            InputStream inputStream = getClass().getResourceAsStream(configurationFileName);
            smooks = new Smooks(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String convertToXML(String ediFile) throws IOException, SAXException, SmooksException {
        byte[] messageIn = ediFile.getBytes();
        ExecutionContext executionContext = smooks.createExecutionContext();
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(new Locale("en", "IE"));
        try {
            StringResult javaResult = new StringResult();
            smooks.filterSource(executionContext, new StreamSource(new ByteArrayInputStream(messageIn)), javaResult);
            return "<orders>" + javaResult.getResult() + "</orders>";
        } finally {
            Locale.setDefault(defaultLocale);
            smooks.close();
        }
    }

    private String getConfigurationFileName() {
        return "edi-config.xml";
    }
}
