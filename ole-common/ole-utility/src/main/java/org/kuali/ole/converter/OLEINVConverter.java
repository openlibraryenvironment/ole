package org.kuali.ole.converter;

import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.payload.StringResult;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/8/13
 * Time: 8:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEINVConverter {

    private Smooks smooks;

    public OLEINVConverter() {
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
            Locale.setDefault(defaultLocale);
            return javaResult.getResult();
        } finally {
            Locale.setDefault(defaultLocale);
            smooks.close();
        }
    }

    private String getConfigurationFileName() {
        return "inv-config.xml";
    }
}
