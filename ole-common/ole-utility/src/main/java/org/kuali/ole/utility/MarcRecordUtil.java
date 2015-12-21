package org.kuali.ole.utility;

import org.apache.commons.collections.CollectionUtils;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;
import org.marc4j.marc.impl.ControlFieldImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by SheikS on 12/11/2015.
 */
public class MarcRecordUtil {

    public String getControlFieldValue(Record marcRecord, String field) {
        List<VariableField> variableFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = variableFields.iterator(); variableFieldIterator.hasNext(); ) {
            ControlFieldImpl controlField = (ControlFieldImpl) variableFieldIterator.next();
            return controlField.getData();
        }
        return null;
    }

    public void updateControlField(Record marcRecord, String field,String value) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            ControlFieldImpl controlField = (ControlFieldImpl) variableFieldIterator.next();
            controlField.setData(value);
        }
    }

    /*This method will get the field and tags and will return return the concadinated value
    * Eg:
    *   field : 050
    *   tags  : $a$b*/
    public String getContentFromMarcRecord(Record marcRecord, String field, String tags) {
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
    *   fieldAndTag : 050 $a$b*/
    public String getContentFromMarcRecord(Record marcRecord, String fieldAndTag) {
        String[] fieldAndTagArray = fieldAndTag.split("[' ']");
        if(fieldAndTagArray.length > 1) {
            String field = fieldAndTagArray[0];
            String tags = fieldAndTagArray[1];
            return getContentFromMarcRecord(marcRecord,field,tags);
        }
        return null;
    }

    public void updateDataFieldValue(Record marcRecord, String field, String tag,String value) {
        List<VariableField> dataFields = marcRecord.getVariableFields(field);
        for (Iterator<VariableField> variableFieldIterator = dataFields.iterator(); variableFieldIterator.hasNext(); ) {
            DataField dataField = (DataField) variableFieldIterator.next();
            List <Subfield> subFields = dataField.getSubfields(tag);
            for (Iterator<Subfield> subfieldIterator = subFields.iterator(); subfieldIterator.hasNext(); ) {
                Subfield subfield = subfieldIterator.next();
                subfield.setData(value);
            }
        }
    }

    public void addVariableFieldToRecord(Record marcRecord, VariableField variableField) {
        marcRecord.addVariableField(variableField);
    }

    public void deleteFieldInRecord(Record marcRecord, String field) {
        List<VariableField> variableFields = marcRecord.getVariableFields(field);
        if (CollectionUtils.isNotEmpty(variableFields)) {
            for (Iterator<VariableField> iterator = variableFields.iterator(); iterator.hasNext(); ) {
                VariableField variableField = iterator.next();
                marcRecord.removeVariableField(variableField);
            }
        }
    }


    private void appendMarcRecordValuesToStrinBuilder(StringBuilder stringBuilder, String location) {
        if(stringBuilder.length() > 0 ) {
            stringBuilder.append(" ");
        }
        stringBuilder.append(location);
    }

}
