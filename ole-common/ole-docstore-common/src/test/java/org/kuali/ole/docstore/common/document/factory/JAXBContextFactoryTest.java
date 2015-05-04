package org.kuali.ole.docstore.common.document.factory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.util.ParseXml;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pvsubrah on 5/1/15.
 */
public class JAXBContextFactoryTest {

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

    @Test
    public void marshallAndUnMarShall() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 1000; i++) {
            Runnable worker = new JAXBContextThread("" + i);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }



    public void deserializeAndSerialize(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
           System.err.println(e);
        }
        Bib bib = new Bib();
        bib = (Bib) bib.deserialize(input);
        assertNotNull(bib);

        String serializeXml = bib.serialize(bib);
        serializeXml = ParseXml.formatXml(serializeXml);
        prettyPrint(serializeXml);
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