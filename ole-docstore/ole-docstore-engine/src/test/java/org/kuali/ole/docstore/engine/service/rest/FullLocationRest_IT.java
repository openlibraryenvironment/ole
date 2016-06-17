package org.kuali.ole.docstore.engine.service.rest;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class FullLocationRest_IT {

    @Test
    public void testRestApi() {

        // Make sure the referenced bib exists
        HttpUriRequest request = new HttpGet(
                "http://localhost:8080/oledocstore/documentrest/holdings/tree?bibId=wbm-10000006");
        try {
            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            String responseAsString = EntityUtils.toString(response.getEntity());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(responseAsString)));
            Node locationNode = document.getElementsByTagName("location").item(0);
            if (locationNode != null) {
                Node fullNameNode = document.getElementsByTagName("fullName").item(0);
                Assert.assertNotNull(fullNameNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}