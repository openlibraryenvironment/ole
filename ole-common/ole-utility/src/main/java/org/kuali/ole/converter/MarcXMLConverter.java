package org.kuali.ole.converter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.*;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.marc4j.*;
import org.marc4j.marc.Record;

import java.io.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/2/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarcXMLConverter {
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

    public void generateMarcBean(BibliographicRecord orderRecord) {

    }


    public List<Record> convertRawMarchToMarc(String rawMarc) {
        List<Record> records = new ArrayList<>();
            MarcReader reader = new MarcStreamReader(IOUtils.toInputStream(rawMarc));
            while (reader.hasNext()) {
                Record record = reader.next();
                records.add(record);

            }

        return records;
    }

    public Record getNextRecord(MarcReader reader) {
        while (reader.hasNext()) {
            Record record = reader.next();
            return record;
        }
        return null;
    }

    public List<Record> convertMarcXmlToRecord(String marcXml) {
        marcXml = convertToUTF8(marcXml);
        List<Record> records = new ArrayList<>();
        MarcReader reader = new MarcXmlReader(IOUtils.toInputStream(marcXml));
        while (reader.hasNext()) {
            Record record = reader.next();
            records.add(record);
        }

        return records;
    }

    private String convertToUTF8(String marcXml) {
        if(!marcXml.contains(OleNGConstants.UTF_8_XML_TAG)) {
            marcXml = OleNGConstants.UTF_8_XML_TAG + marcXml;
            InputStreamReader inputStreamReader = new InputStreamReader(IOUtils.toInputStream(marcXml));
            String currentEncoding = inputStreamReader.getEncoding();
            try {
                String data = new String(marcXml.getBytes() , currentEncoding);
                byte[] destinationBytes = data.getBytes(currentEncoding);
                marcXml = new String(destinationBytes, OleNGConstants.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return marcXml;
    }

    public String convertMarcRecordToRawMarcContent(Record record) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MarcWriter writer = new MarcStreamWriter(byteArrayOutputStream);
        writer.write(record);
        writer.close();
        return byteArrayOutputStream.toString();
    }


    public String generateMARCXMLContent(Record marcRecord){
        org.apache.commons.io.output.ByteArrayOutputStream out = new org.apache.commons.io.output.ByteArrayOutputStream();
        MarcWriter writer = new MarcXmlWriter(out);
        writer.write(marcRecord);
        writer.close();
        return new String(out.toByteArray());
    }


}
