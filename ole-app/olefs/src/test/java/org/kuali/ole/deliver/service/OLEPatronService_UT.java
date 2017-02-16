package org.kuali.ole.deliver.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.deliver.util.XMLFormatterUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sheiksalahudeenm on 5/5/15.
 */
public class OLEPatronService_UT {

    private static final Logger LOG = Logger.getLogger(OLEPatronService_UT.class);
    private static String APPLICATION_URL  = "http://dev.oleproject.org";

    @Test
    public void createPatronTest(){
        try {
            String requestContent  =readFileContent("org/kuali/ole/deliver/patron/createPatronWsdlXml");
            String resposneString = OLESOAPService.sendSoapRequest(APPLICATION_URL + "/remoting/olePatronService",requestContent);
            System.out.println("Response : \n" +formatContentForPretty(resposneString));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void createPatronWithAddressTest(){
        try {
            String requestContent  =readFileContent("org/kuali/ole/deliver/patron/createPatron.xml");
            String resposneString = OLESOAPService.sendSoapRequest(APPLICATION_URL + "/remoting/olePatronService",requestContent);
            System.out.println("Response : \n" + XMLFormatterUtil.formatContentForPretty(resposneString));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String readFileContent(String path) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader(getFilePath(path)));
        String line=null;
        String fullContent = "";
        while ((line=br.readLine())!=null)
        {
            fullContent += line;
        }
        return fullContent;
    }

    public String getFilePath(String classpathRelativePath)  {
        try {
            Resource rsrc = new ClassPathResource(classpathRelativePath);
            return rsrc.getFile().getAbsolutePath();
        } catch(Exception e){
            LOG.error("Error : while accessing file "+e);
        }
        return null;
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
