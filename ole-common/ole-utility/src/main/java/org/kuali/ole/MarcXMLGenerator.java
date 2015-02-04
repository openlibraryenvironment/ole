package org.kuali.ole;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/2/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarcXMLGenerator {
    /**
     * @param inputFile
     * @return converted marc xml File Name
     * @throws java.io.FileNotFoundException This method takes in a raw marc file and converts into a marc xml file.
     */
    public String convertRawMarcToXML(File inputFile) throws FileNotFoundException {
        InputStream input = new FileInputStream(inputFile);
        String fileName = inputFile.getName().replace(".mrc", ".xml");
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
        MarcReader reader = new MarcStreamReader(input);
        MarcWriter writer = new MarcXmlWriter(fileOutputStream, true);

        while (reader.hasNext()) {
            Record record = reader.next();
            writer.write(record);
        }
        writer.close();
        return fileName;
    }


    /**
     * @param content
     * @return converted marc xml
     */
    public String convert(String content) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        MarcReader reader = new MarcStreamReader(byteArrayInputStream);
        MarcWriter writer = new MarcXmlWriter(byteArrayOutputStream, true);

        while (reader.hasNext()) {
            Record record = reader.next();
            writer.write(record);
        }
        writer.close();
        return new String(byteArrayOutputStream.toByteArray());
    }
}
