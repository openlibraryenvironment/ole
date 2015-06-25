package org.kuali.ole.docstore.common.document.factory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.Bib;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pvsubrah on 5/1/15.
 */
public class JAXBContextFactory_UT {

    @Test
    public void getInstance() throws Exception {
        JAXBContextFactory instance = JAXBContextFactory.getInstance();
        assertNotNull(instance);
    }


    @Test
    public void compareInitTimes() throws Exception {
        Long startTime = System.currentTimeMillis();
        JAXBContext.newInstance(Bib.class).createMarshaller();
        Long endTime = System.currentTimeMillis();
        long l = endTime - startTime;
        System.out.println("Time taken to init new Instance: " + l);

        Long startTime1 = System.currentTimeMillis();
        JAXBContextFactory.getInstance().getMarshaller(Bib.class);
        Long endTime1 = System.currentTimeMillis();
        long l1 = endTime1 - startTime1;
        System.out.println("Time taken to init new Instance using Factory: " + l1);
        assertTrue(l1 < l);
        System.out.println(l1 +" ms vs " + l + " ms");
    }

    @Before
    public void setUp() {
        try {
            JAXBContextFactory.getInstance().getUnMarshaller(Bib.class);
            JAXBContextFactory.getInstance().getMarshaller(Bib.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void marshallAndUnMarShall() throws Exception {
        String content = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            content = FileUtils.readFileToString(file);
        } catch (Exception e) {
            System.err.println(e);
        }
        Long startTime1 = System.currentTimeMillis();
        Bib bib = new Bib();
        try {
            Unmarshaller unmarshaller = JAXBContextFactory.getInstance().getUnMarshaller(Bib.class);
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            bib = unmarshaller.unmarshal(new StreamSource(input), Bib.class).getValue();

        } catch (Exception e) {
            e.printStackTrace();
        }


        StringWriter sw = new StringWriter();
        JAXBContextFactory.getInstance().getMarshaller(Bib.class).marshal(bib, sw);
        Long endTime1 = System.currentTimeMillis();
        long l1 = endTime1 - startTime1;
        System.out.println("Time taken  with out synchronized: " + l1);

        String input ="";
        Long startTime = System.currentTimeMillis();
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            System.err.println(e);
        }
        Bib bib1 = new Bib();
        bib1 = (Bib) bib1.deserialize(input);
        String serializeXml = bib1.serialize(bib1);
        Long endTime = System.currentTimeMillis();
        long l = endTime - startTime;
        System.out.println("Time taken  with  synchronized: " + l);


    }





    public static String prettyPrint(final String xml) {
        if (StringUtils.isBlank(xml)) {
            throw new RuntimeException("xml was null or blank in prettyPrint()");
        }

        final StringWriter sw;

        try {
            final OutputFormat format = OutputFormat.createPrettyPrint();
            final org.dom4j.Document document = DocumentHelper.parseText(xml);
            sw = new StringWriter();
            final XMLWriter writer = new XMLWriter(sw, format);
            writer.write(document);
        } catch (Exception e) {
            throw new RuntimeException("Error pretty printing xml:\n" + xml, e);
        }
        String[] xmlArray = StringUtils.split(sw.toString(), '\n');
        Object[] xmlContent = ArrayUtils.subarray(xmlArray, 1, xmlArray.length);
        StringBuilder xmlStr = new StringBuilder();
        for (Object object : xmlContent) {
            xmlStr.append(object.toString());
            xmlStr.append('\n');
        }
        return xmlStr.toString();
    }


}