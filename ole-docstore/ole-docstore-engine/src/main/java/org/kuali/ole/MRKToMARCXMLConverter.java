package org.kuali.ole;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.*;
import org.marc4j.marc.impl.Verifier;

import java.io.*;


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

        }

        writer.write(record);
        writer.close();
        return new String(byteArrayOutputStream.toByteArray());

    }

    public Record addLineToRecord(String inline, Record record) {
        String tag = "";

        tag = inline.substring(1, 4);

        if (tag.equals("LDR")) {
            Leader leader = marcFactory.newLeader(inline.substring(6, 30));
            record.setLeader(leader);
        } else if (Verifier.isControlField(tag)) {
            ControlField cf = marcFactory.newControlField(tag, inline.substring(6));
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

        tag = inline.substring(1, 4);

        if (inline.substring(6, 7).equals("\""))
            ind1 = ' ';
        else
            ind1 = inline.charAt(6);

        if (inline.substring(7, 8).equals("\""))
            ind2 = ' ';
        else
            ind2 = inline.charAt(7);

        DataField df = marcFactory.newDataField(tag, ind1, ind2);

        String[] tagSplit = inline.substring(8).split("\\$");
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
}
