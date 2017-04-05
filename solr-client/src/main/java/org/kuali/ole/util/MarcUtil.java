package org.kuali.ole.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.Constants;
import org.marc4j.MarcReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.marc4j.marc.impl.ControlFieldImpl;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sheiks on 05/12/16.
 */
public class MarcUtil extends Constants{

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
        if(!marcXml.contains(UTF_8_XML_TAG)) {
            marcXml = UTF_8_XML_TAG + marcXml;
            InputStreamReader inputStreamReader = new InputStreamReader(IOUtils.toInputStream(marcXml));
            String currentEncoding = inputStreamReader.getEncoding();
            try {
                String data = new String(marcXml.getBytes() , currentEncoding);
                byte[] destinationBytes = data.getBytes(currentEncoding);
                marcXml = new String(destinationBytes, UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return marcXml;
    }

    public String getDataFieldValueStartsWith(Record record, String dataFieldStartTag) {
        StringBuffer fieldValue = new StringBuffer();
        if (record != null) {
            List<VariableField> variableFields = record.getVariableFields();
            if (!CollectionUtils.isEmpty(variableFields)) {
                for (VariableField variableField : variableFields) {
                    if (variableField != null && StringUtils.isNotBlank(variableField.getTag()) && variableField.getTag().startsWith(dataFieldStartTag)) {
                        DataField dataField = (DataField) variableField;
                        List<Subfield> subfields = dataField.getSubfields();
                        for (Subfield subfield : subfields) {
                            if (subfield != null && StringUtils.isNotBlank(subfield.getData())) {
                                fieldValue.append(subfield.getData());
                                fieldValue.append(" ");
                            }
                        }
                    }
                }
            }
        }
        return fieldValue.toString().trim();
    }

    public String getDataFieldValueStartsWith(Record record, String dataFieldStartTag, List<Character> subFieldTags) {
        StringBuffer fieldValue = new StringBuffer();
        if (record != null) {
            List<VariableField> variableFields = record.getVariableFields();
            if (!CollectionUtils.isEmpty(variableFields)) {
                Subfield subfield;
                for (VariableField variableField : variableFields) {
                    if (variableField != null && StringUtils.isNotBlank(variableField.getTag()) && variableField.getTag().startsWith(dataFieldStartTag)) {
                        DataField dataField = (DataField) variableField;
                        for (Character subFieldTag : subFieldTags){
                            subfield = dataField.getSubfield(subFieldTag);
                            if (subfield != null) {
                                fieldValue.append(subfield.getData());
                                fieldValue.append(" ");
                            }
                        }
                    }
                }
            }
        }
        return fieldValue.toString().trim();
    }

    public List<String> getListOfDataFieldValuesStartsWith(Record record, String dataFieldStartTag, List<Character> subFieldTags) {
        List<String> fieldValues = new ArrayList<>();
        if (record != null) {
            List<VariableField> variableFields = record.getVariableFields();
            if (!CollectionUtils.isEmpty(variableFields)) {
                Subfield subfield;
                for (VariableField variableField : variableFields) {
                    if (variableField != null && StringUtils.isNotBlank(variableField.getTag()) && variableField.getTag().startsWith(dataFieldStartTag)) {
                        DataField dataField = (DataField) variableField;
                        for (Character subFieldTag : subFieldTags){
                            subfield = dataField.getSubfield(subFieldTag);
                            if (subfield != null) {
                                String data = subfield.getData();
                                if (StringUtils.isNotBlank(data)){
                                    fieldValues.add(data);
                                }
                            }
                        }
                    }
                }
            }
        }
        return fieldValues;
    }

    public String getDataFieldValue(Record marcRecord, String field, String ind1, String ind2, String subField) {
        List<String> strings = resolveValue(marcRecord, field, ind1, ind2, subField);
        return CollectionUtils.isEmpty(strings)? "" : strings.get(0);
    }

    public List<String> getMultiDataFieldValues(Record marcRecord, String field, String ind1, String ind2, String subField) {
        return resolveValue(marcRecord, field, ind1, ind2, subField);
    }

    private List<String> resolveValue(Record marcRecord, String field, String ind1, String ind2, String subField) {
        List<String> values = new ArrayList<>();
        String indicator1 = (StringUtils.isNotBlank(ind1) ? String.valueOf(ind1.charAt(0)) : " ");
        String indicator2 = (StringUtils.isNotBlank(ind2) ? String.valueOf(ind2.charAt(0)) : " ");
        List<VariableField> dataFields = marcRecord.getVariableFields(field);

        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            if(dataField!=null){
                if (doIndicatorsMatch(indicator1, indicator2, dataField)) {
                    List<Subfield> subFields = dataField.getSubfields(subField);
                    for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                        Subfield subfield = subfieldIterator.next();
                        if (subField!=null){
                            String data = subfield.getData();
                            if (StringUtils.isNotBlank(data)) {
                                values.add(data);
                            }
                        }
                    }
                }
            }
        }
        return values;
    }

    private boolean doIndicatorsMatch(String indicator1, String indicator2, DataField dataField) {
        boolean result = true;
        if (StringUtils.isNotBlank(indicator1)) {
            result = dataField.getIndicator1() == indicator1.charAt(0);
        }
        if (StringUtils.isNotBlank(indicator2)) {
            result &= dataField.getIndicator2() == indicator2.charAt(0);
        }
        return result;
    }

    public String getControlFieldValue(Record marcRecord, String field) {
        List<VariableField> variableFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = variableFields.iterator(); variableFieldIterator.hasNext(); ) {
            ControlFieldImpl controlField = (ControlFieldImpl) variableFieldIterator.next();
            if (controlField!=null) {
                return controlField.getData();
            }
        }
        return null;
    }

    public Integer getSecondIndicatorForDataField(Record marcRecord, String field) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        if (!CollectionUtils.isEmpty(dataFields)) {
            DataField dataField = (DataField) dataFields.get(0);
            char dataFieldIndicator2 = dataField.getIndicator2();
            if (Character.isDigit(dataFieldIndicator2)) {
                return Character.getNumericValue(dataFieldIndicator2);
            }
        }
        return 0;
    }

    public List<Record> readMarcXml(String marcXmlString) {
        List<Record> recordList = new ArrayList<>();
        InputStream in = new ByteArrayInputStream(marcXmlString.getBytes());
        MarcReader reader = new MarcXmlReader(in);
        while (reader.hasNext()) {
            Record record = reader.next();
            recordList.add(record);
        }
        return recordList;
    }

    public String getDataFieldValue(Record record, String field, char subField) {
        DataField dataField = getDataField(record, field);
        if (dataField != null) {
            Subfield subfield = dataField.getSubfield(subField);
            if (subfield != null) {
                return subfield.getData();
            }
        }
        return null;
    }

    private String getDataFieldValue(DataField dataField, char subField) {
        Subfield subfield = dataField.getSubfield(subField);
        if (subfield != null) {
            return subfield.getData();
        }
        return null;
    }

    public boolean isSubFieldExists(Record record, String field) {
        DataField dataField = getDataField(record, field);
        if (dataField != null) {
            List<Subfield> subfields = dataField.getSubfields();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(subfields)) {
                for (Subfield subfield : subfields) {
                    String data = subfield.getData();
                    if (StringUtils.isNotBlank(data)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public DataField getDataField(Record record, String field) {
        VariableField variableField = record.getVariableField(field);
        if (variableField != null) {
            DataField dataField = (DataField) variableField;
            if (dataField != null) {
                return dataField;
            }
        }
        return null;
    }

    public Character getInd1(Record record, String field, char subField) {
        DataField dataField = getDataField(record, field);
        if (dataField != null) {
            Subfield subfield = dataField.getSubfield(subField);
            if (subfield != null) {
                return dataField.getIndicator1();
            }
        }
        return null;
    }

    public String writeMarcXml(Record record) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MarcWriter marcWriter = new MarcXmlWriter(byteArrayOutputStream, true);
        marcWriter.write(record);
        marcWriter.close();
        String content = new String(byteArrayOutputStream.toByteArray());
        content = content.replaceAll("marcxml:", "");
        return content;
    }

}