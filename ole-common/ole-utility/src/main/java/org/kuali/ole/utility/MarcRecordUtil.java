package org.kuali.ole.utility;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.converter.MarcXMLConverter;
import org.marc4j.MarcSplitStreamWriter;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
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

    private static final Logger LOG = Logger.getLogger(MarcRecordUtil.class);

    private MarcXMLConverter marcXMLConverter;

    private MarcFactory marcFactory;

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
    public void addDataField(Record marcRecord, String field, String ind1, String ind2,String subField, String value) {
        char indicator1 = (StringUtils.isNotBlank(ind1) ? ind1.charAt(0) : ' ');
        char indicator2 = (StringUtils.isNotBlank(ind2) ? ind2.charAt(0) : ' ');
        MarcFactory factory = MarcFactory.newInstance();
        DataField dataField = factory.newDataField();
        dataField.setTag(field);
        dataField.setIndicator1(indicator1);
        dataField.setIndicator2(indicator2);

        if (StringUtils.isNotBlank(subField)) {
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
    public void addSubField(Record marcRecord, String field, String ind1, String ind2,String subField, String value) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        String indicator1 = (StringUtils.isNotBlank(ind1) ? String.valueOf(ind1.charAt(0)) : " ");
        String indicator2 = (StringUtils.isNotBlank(ind2) ? String.valueOf(ind2.charAt(0)) : " ");
        List<VariableField> matchedDataFields = getMatchedDataFields(indicator1, indicator2, null, null, dataFields);
        for (Iterator<VariableField> iterator = matchedDataFields.iterator(); iterator.hasNext(); ) {
            DataField dataField = (DataField) iterator.next();
            Subfield sf = getMarcFactory().newSubfield();
            sf.setCode(subField.charAt(0));
            sf.setData(value);
            dataField.addSubfield(sf);
        }
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
            while (stringTokenizer.hasMoreTokens()) {
                String tag = stringTokenizer.nextToken();
                List<Subfield> subFields = dataField.getSubfields(tag);
                for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield subfield = subfieldIterator.next();
                    String data = subfield.getData();
                    appendMarcRecordValuesToStrinBuilder(stringBuilder, data);
                }
            }
        }
        return stringBuilder.toString();
    }


    public String getDataFieldValueWithIndicators(Record marcRecord, String field, String ind1, String ind2, String subField) {

        List<String> multiDataFieldValues = getMultiDataFieldValues(marcRecord, field, ind1, ind2, subField);

        if(CollectionUtils.isNotEmpty(multiDataFieldValues)) {
            return multiDataFieldValues.get(0);
        }
        return "";
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
                List<Subfield> subFields = dataField.getSubfields(subField);
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
        if (StringUtils.isNotBlank(indicator1)) {
            result = dataField.getIndicator1() == indicator1.charAt(0);
        }
        if (StringUtils.isNotBlank(indicator2)) {
            result &= dataField.getIndicator2() == indicator2.charAt(0);
        }
        return result;
    }


    /*This method will get the field and tags and will return return the concadinated value
    * Eg:
    *   fieldAndTag : 050 $a$b*/
    public String getDataFieldValue(Record marcRecord, String fieldAndTag) {
        String[] fieldAndTagArray = fieldAndTag.split("[' ']");
        if (fieldAndTagArray.length > 1) {
            String field = fieldAndTagArray[0];
            String tags = fieldAndTagArray[1];
            return getDataFieldValue(marcRecord, field, tags);
        }
        return null;
    }

    public void updateDataFieldValue(Record marcRecord, String field, String ind1, String ind2, String subField, String value) {
        String indicator1 = (StringUtils.isNotBlank(ind1) ? String.valueOf(ind1.charAt(0)) : " ");
        String indicator2 = (StringUtils.isNotBlank(ind2) ? String.valueOf(ind2.charAt(0)) : " ");
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        List<VariableField> filterDataFields = getMatchedDataFields(indicator1, indicator2, subField, null, dataFields);

        for (Iterator<VariableField> variableFieldIterator = filterDataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            List<Subfield> subFields = dataField.getSubfields(subField);
            for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                Subfield subfield = subfieldIterator.next();
                subfield.setData(value);
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

    public void removeSubField(Record marcRecord, String field, String ind1, String ind2, String subfield) {
        String indicator1 = (StringUtils.isNotBlank(ind1) ? String.valueOf(ind1.charAt(0)) : " ");
        String indicator2 = (StringUtils.isNotBlank(ind2) ? String.valueOf(ind2.charAt(0)) : " ");
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        List<VariableField> filterDataFields = getMatchedDataFields(indicator1, indicator2, subfield, null, dataFields);
        if (CollectionUtils.isNotEmpty(filterDataFields)) {
            for (Iterator<VariableField> iterator = filterDataFields.iterator(); iterator.hasNext(); ) {
                DataField dataField = (DataField) iterator.next();
                List<Subfield> subfields = dataField.getSubfields(subfield);
                for (Iterator<Subfield> subfieldIterator = subfields.iterator(); subfieldIterator.hasNext(); ) {
                    Subfield sf = subfieldIterator.next();
                    dataField.removeSubfield(sf);
                }
            }
        }
    }

    private void appendMarcRecordValuesToStrinBuilder(StringBuilder stringBuilder, String value) {
        if (StringUtils.isNotBlank(value)) {
            if (stringBuilder.length() > 0) {
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
            data = data.replace(replaceFrom, replaceTo);
            controlField.setData(data);
        }
    }

    public void replaceContentInDataField(Record marcRecord, String field, String ind1, String ind2, String subField, String replaceFrom, String replaceTo) {
        String indicator1 = (StringUtils.isNotBlank(ind1) ? String.valueOf(ind1.charAt(0)) : " ");
        String indicator2 = (StringUtils.isNotBlank(ind2) ? String.valueOf(ind2.charAt(0)) : " ");
        List<VariableField> dataFields = marcRecord.getVariableFields(field);

        List<VariableField> matchedDataFields = getMatchedDataFields(indicator1, indicator2, subField, null, dataFields);
        for (Iterator<VariableField> iterator = matchedDataFields.iterator(); iterator.hasNext(); ) {
            DataField dataField = (DataField) iterator.next();
            List<Subfield> subFields = dataField.getSubfields(subField);
            for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                Subfield sf = subfieldIterator.next();
                String data = sf.getData();
                data = data.replace(replaceFrom, replaceTo);
                sf.setData(data);
            }
        }
    }

    public String convertMarcRecordListToRawMarcContent(List<Record> records) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MarcWriter writer = new MarcSplitStreamWriter(byteArrayOutputStream, OleNGConstants.UTF_8, 300000, "880");
        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record record = iterator.next();
            if (null != record) {
                try {
                    writer.write(record);
                } catch (Error e) {
                    LOG.error("Error with writing Marc record with 001 tag : " + getControlFieldValue(record,OleNGConstants.TAG_001));
                    e.printStackTrace();
                } catch (Exception e) {
                    LOG.error("Exception with writing Marc record with 001 tag : " + getControlFieldValue(record,OleNGConstants.TAG_001));
                    e.printStackTrace();
                }
            }
        }
        writer.close();
        return byteArrayOutputStream.toString();
    }

    public String convertMarcRecordListToMarcXmlContent(List<Record> records) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MarcWriter writer = new MarcXmlWriter(byteArrayOutputStream, OleNGConstants.UTF_8);
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
        return CollectionUtils.isNotEmpty(variableFields);
    }

    public List<Record> convertMarcXmlContentToMarcRecord(String marcRecord) {
        return getMarcXMLConverter().convertMarcXmlToRecord(marcRecord);
    }

    public String convertMarcRecordToMarcContent(Record marcRecord) {
        return getMarcXMLConverter().generateMARCXMLContent(marcRecord);
    }

    public MarcXMLConverter getMarcXMLConverter() {
        if (null == marcXMLConverter) {
            marcXMLConverter = new MarcXMLConverter();
        }
        return marcXMLConverter;
    }

    public Integer getNumOccurances(Record marcRecord, String dataField, String ind1, String ind2, String subField) {
        Integer numOccurances;

        List<VariableField> dataFields = marcRecord.getVariableFields(dataField);
        List<VariableField> matchedDataFields = getMatchedDataFields(ind1, ind2, subField, null, dataFields);
        numOccurances = matchedDataFields.size();

        return numOccurances;
    }

    /*This method will filter the datafield based on criteria.*/
    public List<VariableField> getMatchedDataFields(String ind1, String ind2, String subField, String value, List<VariableField> dataFields) {
        List<VariableField> filteredDataFields = new ArrayList<>();

        for (Iterator<VariableField> iterator = dataFields.iterator(); iterator.hasNext(); ) {
            boolean matchedDataField = true;
            DataField field = (DataField) iterator.next();
            if (StringUtils.isNotBlank(ind1)) {
                matchedDataField &= ind1.charAt(0) == field.getIndicator1();
            }
            if (StringUtils.isNotBlank(ind2)) {
                matchedDataField &= ind2.charAt(0) == field.getIndicator2();
            }

            char subFieldChar = (StringUtils.isNotBlank(subField) ? subField.charAt(0) : ' ');
            if (subFieldChar != ' ') {
                Subfield subfield = field.getSubfield(subFieldChar);
                matchedDataField &= null != subfield;
                if (matchedDataField && StringUtils.isNotBlank(value)) {
                    String data = subfield.getData();
                    matchedDataField &= data.equals(value);
                }
            }
            if (matchedDataField) {
                filteredDataFields.add(field);
            }
        }
        return filteredDataFields;
    }

    public MarcFactory getMarcFactory() {
        if(null == marcFactory) {
            marcFactory = MarcFactory.newInstance();
        }
        return marcFactory;
    }

    public void setMarcFactory(MarcFactory marcFactory) {
        this.marcFactory = marcFactory;
    }

    public void renameControlField(Record marcRecord, String sourceField, String destinationField) {
        List<VariableField> variableFields = marcRecord.getVariableFields(sourceField);
        for (Iterator<VariableField> variableFieldIterator = variableFields.iterator(); variableFieldIterator.hasNext(); ) {
            ControlFieldImpl controlField = (ControlFieldImpl) variableFieldIterator.next();
            controlField.setTag(destinationField);
        }
    }

    public void renameDataField(Record marcRecord, String sourceField, String destinationField) {
        List<VariableField> variableFields = marcRecord.getVariableFields(sourceField);
        for (Iterator<VariableField> variableFieldIterator = variableFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            dataField.setTag(destinationField);
        }
    }

    public void copyControlField(Record marcRecord, String sourceField, String destinationField) {
        List<VariableField> variableFields = marcRecord.getVariableFields(sourceField);
        for (Iterator<VariableField> variableFieldIterator = variableFields.iterator(); variableFieldIterator.hasNext(); ) {
            ControlFieldImpl controlField = (ControlFieldImpl) variableFieldIterator.next();
            ControlField newField = getMarcFactory().newControlField();
            newField.setTag(destinationField);
            newField.setData(controlField.getData());
            marcRecord.addVariableField(newField);
        }
    }

    public void copyDataField(Record marcRecord, String sourceField, String destinationField) {
        List<VariableField> variableFields = marcRecord.getVariableFields(sourceField);
        for (Iterator<VariableField> variableFieldIterator = variableFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            DataField newField = getMarcFactory().newDataField();
            newField.setTag(destinationField);
            newField.setIndicator1(dataField.getIndicator1());
            newField.setIndicator2(dataField.getIndicator2());
            for (Iterator<Subfield> iterator = dataField.getSubfields().iterator(); iterator.hasNext(); ) {
                Subfield subfield = iterator.next();
                newField.addSubfield(subfield);
                marcRecord.addVariableField(newField);
            }
        }
    }

}
