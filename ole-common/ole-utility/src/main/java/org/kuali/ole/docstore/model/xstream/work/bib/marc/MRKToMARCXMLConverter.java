package org.kuali.ole.docstore.model.xstream.work.bib.marc;

import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.*;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.*;

import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.impl.Verifier;

import java.io.*;
import java.net.URL;


/**
 * Created with IntelliJ IDEA.
 * User: jpillan
 * Date: 2/7/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */

public class MRKToMARCXMLConverter {
    MarcFactory marcFactory = MarcFactory.newInstance();

    public String convert(String content) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MarcWriter writer = new MarcXmlWriter(byteArrayOutputStream, true);


        Record record;
        String inline;

        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        record = marcFactory.newRecord();

        try {
            while ((inline = br.readLine()) != null) {

                if (inline.equals("") || inline.startsWith(" ")) {
                    writer.write(record);
                    record = marcFactory.newRecord();
                } else {
                    record = addLineToRecord(inline, record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        writer.write(record);
        writer.close();
        return new String(byteArrayOutputStream.toByteArray());

    }

    public Record addLineToRecord(String inline, Record record) {
        String tag = "";
        tag = inline.substring(0, 3);
        if (tag.equals("LDR") || tag.equalsIgnoreCase("Lea")) {
            Leader leader = marcFactory.newLeader(inline.substring(7, inline.length()));
            record.setLeader(leader);
        } else if (Verifier.isControlField(tag)) {
            ControlField cf = marcFactory.newControlField(tag, inline.substring(4));
            record.addVariableField(cf);
        } else {
            record.addVariableField(processDatafield(inline));
        }
        return record;
    }

    private DataField processDatafield(String inline) {
        String tag;
        char ind1;
        char ind2;
        tag = inline.substring(0, 3);
        if (inline.substring(4, 5).equals("\""))
            ind1 = ' ';
        else
            ind1 = inline.charAt(4);

        if (inline.substring(5, 6).equals("\""))
            ind2 = ' ';
        else
            ind2 = inline.charAt(5);

        DataField df = marcFactory.newDataField(tag, ind1, ind2);

        String[] tagSplit = inline.substring(7).split("\\$");
        Subfield sf = null;
        for (String text : tagSplit) {
            if (!text.equals("") && !text.equals(" ")) {
                char subfield = text.charAt(0);
                String subfieldText = text.substring(1);
                sf = marcFactory.newSubfield(subfield, subfieldText);
                df.addSubfield(sf);
            }
        }
        return df;
    }

    public String ConvertMarcXMLToMRK(String marcXML) throws Exception {

        System.out.println("marcXML " + marcXML);
        WorkBibMarcRecordProcessor workBibMarcRecordProcessor = new WorkBibMarcRecordProcessor();
        StringBuffer sb = new StringBuffer();
        WorkBibMarcRecords marcRecords = workBibMarcRecordProcessor.fromXML(marcXML);
        System.out.println("size " + marcRecords.getRecords().size());
        WorkBibMarcRecord workBibMarcRecord = marcRecords.getRecords().get(0);

        sb.append("Leader " + workBibMarcRecord.getLeader());
        sb.append("\n");
        for (org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField controlField : workBibMarcRecord
                .getControlFields()) {
            sb.append(controlField.getTag());
            sb.append(" " + controlField.getValue());
            sb.append("\n");
        }
        for (org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField dataField : workBibMarcRecord
                .getDataFields()) {
            sb.append(dataField.getTag());
            sb.append(" ");
            sb.append(dataField.getInd1());
            sb.append(dataField.getInd2());
            //sb.append("\n");
            for (SubField subField : dataField.getSubFields()) {
                sb.append("$");
                sb.append(subField.getCode());
                sb.append(subField.getValue());
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
