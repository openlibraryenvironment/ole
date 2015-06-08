package org.kuali.ole.ncip;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.Assert.assertTrue;

/**
 * Created by angelind on 5/13/15.
 */
public class OLENCIPService_IT extends OLETestCaseBase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testLookupUser(){

        String APPLICATION_URL = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
        String requestContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<NCIPMessage version=\"http://www.niso.org/schemas/ncip/v2_0/ncip_v2_0.xsd\"\n" +
                "   xmlns=\"http://www.niso.org/2008/ncip\">\n" +
                "  <LookupUser>\n" +
                "    <InitiationHeader>\n" +
                "      <FromAgencyId>\n" +
                "        <AgencyId>LEHI</AgencyId>\n" +
                "      </FromAgencyId>\n" +
                "      <ToAgencyId>\n" +
                "        <AgencyId>Lehigh University</AgencyId>\n" +
                "      </ToAgencyId>\n" +
                "    </InitiationHeader> \n" +
                "    <UserId>\n" +
                "      <AgencyId>LEHI</AgencyId>\n" +
                "      <UserIdentifierValue>6010570003043558</UserIdentifierValue>\n" +
                "    </UserId>\n" +
                "    <UserElementType>Block Or Trap</UserElementType>\n" +
                "    <UserElementType>Name Information</UserElementType>\n" +
                "    <UserElementType>User Address Information</UserElementType>\n" +
                "    <UserElementType>User Privilege</UserElementType>\n" +
                "    <UserElementType>User Id</UserElementType>\n" +
                "  </LookupUser>\n" +
                "</NCIPMessage>";
        String response  = sendRequest(APPLICATION_URL + "/OLENCIPResponder" , requestContent);
        System.out.println("Response Content : "  + formatContentForPretty(response));
        if(response.contains("<ElectronicAddressType>electronic mail address</ElectronicAddressType>")){
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }


    public String sendRequest(String url, String requestContent){
        String responseString = "";
        try {
            URL oURL = new URL(url);
            HttpURLConnection con = (HttpURLConnection) oURL.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setDoOutput(true);
            OutputStream reqStream = con.getOutputStream();
            reqStream.write(requestContent.getBytes());
            InputStream resStream = con.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(resStream));
            String line = null;
            StringBuilder responseContentBuilder = new StringBuilder();
            while((line = in.readLine()) != null) {
                responseContentBuilder.append(line);
            }
            responseString = responseContentBuilder.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseString;
    }

    private String formatContentForPretty(String content){
        try {
            final Document document = parseXmlFile(content);
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
