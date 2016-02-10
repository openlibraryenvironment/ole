package org.kuali.ole.utility;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.converter.MarcXMLConverter;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.marc.*;
import org.marc4j.marc.impl.ControlFieldImpl;
import org.marc4j.marc.impl.Verifier;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by SheikS on 12/11/2015.
 */
public class MarcRecordUtil {

    private MarcXMLConverter marcXMLConverter;

    public String getControlFieldValue(Record marcRecord, String field) {
        List<VariableField> variableFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = variableFields.iterator(); variableFieldIterator.hasNext(); ) {
            ControlFieldImpl controlField = (ControlFieldImpl) variableFieldIterator.next();
            return controlField.getData();
        }
        return null;
    }

    public void updateControlFieldValue(Record marcRecord, String field, String value) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            ControlFieldImpl controlField = (ControlFieldImpl) variableFieldIterator.next();
            controlField.setData(value);
        }
    }

    /*This method will get the field, subFields and value.  It will add variable field to record
    * Eg:
    *   field : 050
    *   subFields  : $a$b*/
    public void addDataField(Record marcRecord, String field, String subFields, String value) {
        MarcFactory factory = MarcFactory.newInstance();
        DataField dataField = factory.newDataField();
        dataField.setTag(field);
        dataField.setIndicator1(' ');
        dataField.setIndicator2(' ');

        StringTokenizer stringTokenizer = new StringTokenizer(subFields,"$");
        while(stringTokenizer.hasMoreTokens()) {
            String subField = stringTokenizer.nextToken();
            Subfield subfield = factory.newSubfield();
            subfield.setCode(subField.charAt(0));
            subfield.setData(value);
            dataField.addSubfield(subfield);
        }
        addVariableFieldToRecord(marcRecord, dataField);
    }

    /*This method will get the field, subFields and value.  It will add variable field to record
    * Eg:
    *   field : 050
    *   subFields  : $a$b*/
    public void addControlField(Record marcRecord, String field, String value) {
        MarcFactory factory = MarcFactory.newInstance();
        ControlField controlField = factory.newControlField();
        controlField.setTag(field);
        controlField.setData(value);
        addVariableFieldToRecord(marcRecord, controlField);
    }

    /*This method will get the field and tags and will return return the concadinated value
    * Eg:
    *   field : 050
    *   tags  : $a$b*/
    public String getDataFieldValue(Record marcRecord, String field, String tags) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        StringBuilder stringBuilder = new StringBuilder();
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            StringTokenizer stringTokenizer = new StringTokenizer(tags, "$");
            while(stringTokenizer.hasMoreTokens()) {
                String tag = stringTokenizer.nextToken();
                List <Subfield> subFields = dataField.getSubfields(tag);
                for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield subfield = subfieldIterator.next();
                    String data = subfield.getData();
                    appendMarcRecordValuesToStrinBuilder(stringBuilder,data);
                }
            }
        }
        return stringBuilder.toString();
    }



    /*This method will get the field and tags and will return return the concadinated value
    * Eg:
    *   field : 050
    *   tags  : ind1|ind2|$a$b*/
    public String getDataFieldValueWithIndicators(Record marcRecord, String field, String ind1, String ind2, String subField) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> multiDataFieldValues = getMultiDataFieldValues(marcRecord, field, ind1, ind2, subField);
        for (Iterator<String> iterator = multiDataFieldValues.iterator(); iterator.hasNext(); ) {
            String fieldValue = iterator.next();
            stringBuilder.append(fieldValue);
            if(iterator.hasNext()){
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }


    /*This method will get the field and tags and will return return the concadinated value
    * Eg:
    *   field : 050
    *   tags  : ind1|ind2|$a$b*/
    public List<String> getMultiDataFieldValues(Record marcRecord, String field, String ind1, String ind2, String subField) {
        List<String> values = new ArrayList<>();
        String indicator1 = (StringUtils.isNotBlank(ind1) ? String.valueOf(ind1.charAt(0)) : " ");
        String indicator2 = (StringUtils.isNotBlank(ind2) ? String.valueOf(ind2.charAt(0)) : " ");
        List<VariableField> dataFields = marcRecord.getVariableFields(field);

        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            if (doIndicatorsMatch(indicator1, indicator2, dataField)) {
                    List <Subfield> subFields = dataField.getSubfields(subField);
                    for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                        Subfield subfield = subfieldIterator.next();
                        String data = subfield.getData();
                        if (StringUtils.isNotBlank(data)) {
                            values.add(data);
                        }
                    }
            }
        }
        return values;
    }

    private boolean doIndicatorsMatch(String indicator1, String indicator2, DataField dataField) {
        boolean result = true;
        if(StringUtils.isNotBlank(indicator1)){
            result = dataField.getIndicator1() == indicator1.charAt(0);
        } else if (StringUtils.isNotBlank(indicator2)){
            result&= dataField.getIndicator2() == indicator2.charAt(0);
        }
        return result;
    }


    /*This method will get the field and tags and will return return the concadinated value
    * Eg:
    *   fieldAndTag : 050 $a$b*/
    public String getDataFieldValue(Record marcRecord, String fieldAndTag) {
        String[] fieldAndTagArray = fieldAndTag.split("[' ']");
        if(fieldAndTagArray.length > 1) {
            String field = fieldAndTagArray[0];
            String tags = fieldAndTagArray[1];
            return getDataFieldValue(marcRecord,field,tags);
        }
        return null;
    }

    public void updateDataFieldValue(Record marcRecord, String field, String tags,String value) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            StringTokenizer stringTokenizer = new StringTokenizer(tags, "$");
            while(stringTokenizer.hasMoreTokens()) {
                String tag = stringTokenizer.nextToken();
                List <Subfield> subFields = dataField.getSubfields(tag);
                for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield subfield = subfieldIterator.next();
                    subfield.setData(value);
                }
            }
        }
    }

    public void addVariableFieldToRecord(Record marcRecord, VariableField variableField) {
        marcRecord.addVariableField(variableField);
    }

    public void removeFieldFromRecord(Record marcRecord, String field) {
        List<VariableField> variableFields = marcRecord.getVariableFields(field);
        if (CollectionUtils.isNotEmpty(variableFields)) {
            for (Iterator<VariableField> iterator = variableFields.iterator(); iterator.hasNext(); ) {
                VariableField variableField = iterator.next();
                marcRecord.removeVariableField(variableField);
            }
        }
    }

    private void appendMarcRecordValuesToStrinBuilder(StringBuilder stringBuilder, String value) {
        if (StringUtils.isNotBlank(value)) {
            if(stringBuilder.length() > 0 ) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(value);
        }
    }


    public void replaceContentInControlField(Record marcRecord, String field, String replaceFrom, String replaceTo) {
        List<VariableField> variableFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = variableFields.iterator(); variableFieldIterator.hasNext(); ) {
            ControlFieldImpl controlField = (ControlFieldImpl) variableFieldIterator.next();
            String data = controlField.getData();
            data = data.replace(replaceFrom,replaceTo);
            controlField.setData(data);
        }
    }

    public void replaceContentInDataField(Record marcRecord, String field, String tags, String replaceFrom, String replaceTo) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            StringTokenizer stringTokenizer = new StringTokenizer(tags, "$");
            while(stringTokenizer.hasMoreTokens()) {
                String tag = stringTokenizer.nextToken();
                List <Subfield> subFields = dataField.getSubfields(tag);
                for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield subfield = subfieldIterator.next();
                    String data = subfield.getData();
                    data = data.replace(replaceFrom,replaceTo);
                    subfield.setData(data);
                }
            }

        }
    }

    public String convertMarcRecordListToRawMarcContent(List<Record> records) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MarcWriter writer = new MarcStreamWriter(byteArrayOutputStream);
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record record = iterator.next();
            writer.write(record);
        }
        writer.close();
        return byteArrayOutputStream.toString();
    }

    public boolean isControlField(String field) {
        return Verifier.isControlField(field);
    }

    public boolean hasField(Record marcRecord, String field) {
        List<VariableField> variableFields = marcRecord.getVariableFields(field);
        return (CollectionUtils.isNotEmpty(variableFields) ? true: false);
    }

    public List<Record> convertMarcXmlContentToMarcRecord(String marcRecord) {
        return getMarcXMLConverter().convertMarcXmlToRecord(marcRecord);
    }

    public String convertMarcRecordToMarcContent(Record marcRecord) {
        return getMarcXMLConverter().generateMARCXMLContent(marcRecord);
    }

    public MarcXMLConverter getMarcXMLConverter() {
        if(null == marcXMLConverter) {
            marcXMLConverter = new MarcXMLConverter();
        }
        return marcXMLConverter;
    }

    public Integer getNumOccurances(Record marcRecord, String dataField, String ind1, String ind2, String subField) {
        Integer numOccurances;

        List<VariableField> dataFields = marcRecord.getVariableFields(dataField);

        numOccurances = dataFields.size();

        return numOccurances;
    }
}
