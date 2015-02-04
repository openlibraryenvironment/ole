package org.kuali.ole.describe.bo;

import org.kuali.ole.BibliographicRecordHandler;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.describe.form.MarcEditorForm;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * MarcEditorFormDataHandler is the data handler class for Marc Editor
 */
public class WorkMarcEditorFormDataHandler {
    /**
     * This method will generate the responseXml,
     * This method builds the bibliographicRecord,And BibliographicRecordHandler will generate the responseXml.,
     *
     * @param uifFormBase
     * @return String
     */
    public String buildBibRecordForDocStore(UifFormBase uifFormBase) {
        MarcEditorForm marcEditorForm = (MarcEditorForm) uifFormBase;
        BibliographicRecord bibliographicRecord = new BibliographicRecord();
        bibliographicRecord.setLeader(marcEditorForm.getLeader());
        bibliographicRecord.setControlfields(buldBibliographicContrlFields(marcEditorForm.getControlFields()));
        bibliographicRecord.setDatafields(buildBibliographicDataFields(marcEditorForm.getDataFields()));
        String content = new BibliographicRecordHandler().generateXML(bibliographicRecord);
        return content;
    }

    /**
     * This method build the dataFields for bibliographicRecord and it will return List of marcEditorControlField ,
     * each marcEditorControlField sets the tag and value get from controlFields
     *
     * @param dataFields
     * @return List<DataField>
     */
    private List<DataField> buildBibliographicDataFields(List<MarcEditorDataField> dataFields) {
        String marcEditorDataFieldValue;
        List<DataField> marcDataFieldList = new ArrayList<DataField>();
        for (int i = 0; i < dataFields.size(); i++) {
            DataField marcDataField = new DataField();
            List<SubField> marcSubFieldList = new ArrayList<SubField>();
            marcEditorDataFieldValue = dataFields.get(i).getValue();
            StringTokenizer str = new StringTokenizer(marcEditorDataFieldValue, "|");
            while (str.hasMoreTokens()) {
                SubField marcSubField = new SubField();
                String dataFieldTokenizedVal = str.nextToken();
                String code = Character.toString(dataFieldTokenizedVal.charAt(0));
                marcSubField.setCode(code);
                String value = dataFieldTokenizedVal.substring(1, dataFieldTokenizedVal.length());
                marcSubField.setValue(value);
                marcSubFieldList.add(marcSubField);
            }
            marcDataField.setSubFields(marcSubFieldList);
            marcDataField.setTag(dataFields.get(i).getTag());
            marcDataField.setInd1(dataFields.get(i).getInd1());
            marcDataField.setInd2(dataFields.get(i).getInd2());
            marcDataFieldList.add(marcDataField);
        }
        return marcDataFieldList;
    }

    /**
     * This method build the controlFields for bibliographicRecord and it will return List of marcEditorControlField ,
     * each marcEditorControlField sets the tag and value get from controlFields
     *
     * @param controlFields
     * @return List<ControlField>
     */
    private List<ControlField> buldBibliographicContrlFields(List<MarcEditorControlField> controlFields) {
        List<ControlField> marcControlFieldList = new ArrayList<ControlField>();
        for (int i = 0; i < controlFields.size(); i++) {
            ControlField marcControlField = new ControlField();
            marcControlField.setTag(controlFields.get(i).getTag());
            marcControlField.setValue(controlFields.get(i).getValue());
            marcControlFieldList.add(marcControlField);
        }
        return marcControlFieldList;
    }

    /**
     * This method build the controlFields and it will return List of marcEditorControlField ,
     * each marcEditorControlField sets the tag and value get from controlFields
     *
     * @param controlFields
     * @return List<MarcEditorControlField>
     */
    public List<MarcEditorControlField> buildMarcEditorControlFields(List<ControlField> controlFields) {
        List<MarcEditorControlField> marcControlFieldList = new ArrayList<MarcEditorControlField>();
        for (int i = 0; i < controlFields.size(); i++) {
            MarcEditorControlField marcEditorControlField = new MarcEditorControlField();
            marcEditorControlField.setTag(controlFields.get(i).getTag());
            marcEditorControlField.setValue(controlFields.get(i).getValue());
            marcControlFieldList.add(marcEditorControlField);
        }
        return marcControlFieldList;
    }

    /**
     * This method build the dataFields and it will return List of marcEditorDataField ,
     * each marcEditorDataField sets the value, and each value has code and value.
     *
     * @param dataFields
     * @return List<MarcEditorDataField>
     */
    public List<MarcEditorDataField> buildMarcEditorDataFields(List<DataField> dataFields) {
        List<MarcEditorDataField> marcEditorDataFields = new ArrayList<MarcEditorDataField>();
        for (int i = 0; i < dataFields.size(); i++) {
            MarcEditorDataField marcEditorDataField = new MarcEditorDataField();
            marcEditorDataField.setTag(dataFields.get(i).getTag());
            marcEditorDataField.setInd1(dataFields.get(i).getInd1());
            marcEditorDataField.setInd2(dataFields.get(i).getInd2());
            List<SubField> subFields = dataFields.get(i).getSubFields();
            String subFieldVal = null;
            for (int j = 0; j < subFields.size(); j++) {
                String code = subFields.get(j).getCode();
                String value = subFields.get(j).getValue();
                if (subFieldVal != null)
                    subFieldVal = subFieldVal + "|" + code + value;
                else
                    subFieldVal = "|" + code + value;
            }
            marcEditorDataField.setValue(subFieldVal);
            marcEditorDataFields.add(marcEditorDataField);
        }
        return marcEditorDataFields;
    }

    /**
     * This method will get the uuid from the marcEditorForm,
     * uifFormBase has type casted in to MarcEditorForm and from the marcEditorForm we will get the controlFields,
     * iterating the controlFields will return the uuid if tag value equal 001
     *
     * @param uifFormBase
     * @return Returns uuid
     */
    public String getUUIDFromForm(UifFormBase uifFormBase) {
        MarcEditorForm form = (MarcEditorForm) uifFormBase;
        List<MarcEditorControlField> controlFields = form.getControlFields();
        for (Iterator<MarcEditorControlField> iterator = controlFields.iterator(); iterator.hasNext(); ) {
            MarcEditorControlField controlField = iterator.next();
            if (controlField.getTag().equals("001")) {
                return controlField.getValue();
            }
        }
        return null;
    }
}
