package org.kuali.ole.describe.bo;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.describe.bo.marc.structuralfields.ControlFields;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield006.ControlField006Text;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield007.ControlField007Text;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield008.ControlField008;
import org.kuali.ole.describe.form.MarcEditorForm;
import org.kuali.ole.describe.form.WorkBibMarcForm;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.*;

/**
 * MarcEditorFormDataHandler is the data handler class for Marc Editor
 */
public class MarcEditorFormDataHandler {
    /**
     * This method will generate the responseXml,
     * This method builds the bibliographicRecord,And BibliographicRecordHandler will generate the responseXml.,
     *
     * @param uifFormBase
     * @return String
     */
    public String buildBibRecordForDocStore(UifFormBase uifFormBase) {
        MarcEditorForm marcEditorForm = (MarcEditorForm) uifFormBase;
        BibMarcRecord bibMarcRecord = new BibMarcRecord();
        bibMarcRecord.setLeader(marcEditorForm.getLeader());
        bibMarcRecord.setControlFields(buildBibliographicContrlFields(marcEditorForm.getControlFields()));
        bibMarcRecord.setDataFields(buildBibliographicDataFields(marcEditorForm.getDataFields()));
        String content = new BibMarcRecordProcessor().generateXML(bibMarcRecord);
        return content;
    }

    /**
     * This method will generate the responseXml,
     * This method builds the bibliographicRecord,And BibliographicRecordHandler will generate the responseXml.,
     *
     * @param workBibMarcForm
     * @return String
     */
    public String buildBibRecordForDocStore(WorkBibMarcForm workBibMarcForm) {

        BibMarcRecord bibMarcRecord = new BibMarcRecord();
        bibMarcRecord.setLeader(workBibMarcForm.getLeader());
        bibMarcRecord.setControlFields(buildBibliographicContrlFields(workBibMarcForm.getControlFields()));
        bibMarcRecord.setDataFields(buildBibliographicDataFields(workBibMarcForm.getDataFields()));
        String content = new BibMarcRecordProcessor().generateXML(bibMarcRecord);

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
                marcSubField.setValue(value.trim());
                marcSubFieldList.add(marcSubField);
            }
            marcDataField.setSubFields(marcSubFieldList);
            marcDataField.setTag(dataFields.get(i).getTag());

            if (dataFields.get(i).getInd1().equals("") || dataFields.get(i).getInd1().equals("#") || dataFields.get(i).getInd1() == null) {
                marcDataField.setInd1(" ");
            } else {
                marcDataField.setInd1(dataFields.get(i).getInd1());
            }

            if (dataFields.get(i).getInd2().equals("") || dataFields.get(i).getInd2().equals("#") || dataFields.get(i).getInd2() == null) {
                marcDataField.setInd2(" ");
            } else {
                marcDataField.setInd2(dataFields.get(i).getInd2());
            }

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
    private List<ControlField> buildBibliographicContrlFields(List<MarcEditorControlField> controlFields) {
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
    public List<MarcEditorControlField> buildMarcEditorControlFields(WorkBibMarcForm workBibMarcForm,
                                                                     List<ControlField> controlFields) {
        List<MarcEditorControlField> marcControlFieldList = new ArrayList<MarcEditorControlField>();
        ControlFields marcControlFields = new ControlFields();

        ControlField008 controlField008 = new ControlField008();
        List<ControlField006Text> controlFields006List = new ArrayList<ControlField006Text>();
        List<ControlField007Text> controlFields007List = new ArrayList<ControlField007Text>();

        for (int i = 0; i < controlFields.size(); i++) {
            MarcEditorControlField marcEditorControlField = new MarcEditorControlField();
            marcEditorControlField.setTag(controlFields.get(i).getTag());
            marcEditorControlField.setValue(controlFields.get(i).getValue());
            if (marcEditorControlField.getTag().equals(ControlFields.CONTROL_FIELD_001)) {
                if (marcEditorControlField.getValue() != null && marcEditorControlField.getValue().length() > 0) {
                    marcControlFields.setLocalId(DocumentLocalId.getDocumentIdDisplay(marcEditorControlField.getValue()));
                }
                marcControlFields.setControlField001(marcEditorControlField.getValue());
            } else if (marcEditorControlField.getTag().equals(ControlFields.CONTROL_FIELD_003)) {
                marcControlFields.setControlField003(marcEditorControlField.getValue());
            } else if (marcEditorControlField.getTag().equals(ControlFields.CONTROL_FIELD_005)) {
                marcControlFields.setControlField005(marcEditorControlField.getValue());
            } else if (marcEditorControlField.getTag().equals(ControlFields.CONTROL_FIELD_006)) {
                ControlField006Text controlField006Text = new ControlField006Text();
                controlField006Text.setRawText(marcEditorControlField.getValue());
                controlFields006List.add(controlField006Text);
                marcControlFields.setControlFields006List(controlFields006List);
            } else if (marcEditorControlField.getTag().equals(ControlFields.CONTROL_FIELD_007)) {
                ControlField007Text controlField007Text = new ControlField007Text();
                controlField007Text.setRawText(marcEditorControlField.getValue());
                controlFields007List.add(controlField007Text);
                marcControlFields.setControlFields007List(controlFields007List);
            } else if (marcEditorControlField.getTag().equals(ControlFields.CONTROL_FIELD_008)) {
                controlField008.setRawText(marcEditorControlField.getValue());
                controlField008.setDate008(controlField008.getRawText().substring(0,6));
                marcControlFields.setControlField008(controlField008);
            }
            workBibMarcForm.setMarcControlFields(marcControlFields);
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
            if (dataFields.get(i).getInd1() == null || dataFields.get(i).getInd1().equals(" ") || dataFields.get(i).getInd1().equals("")) {
                marcEditorDataField.setInd1("#");
            } else {
                marcEditorDataField.setInd1(dataFields.get(i).getInd1());
            }
            if (dataFields.get(i).getInd2() == null || dataFields.get(i).getInd2().equals(" ") || dataFields.get(i).getInd2().equals("")) {
                marcEditorDataField.setInd2("#");
            } else {
                marcEditorDataField.setInd2(dataFields.get(i).getInd2());
            }
            List<SubField> subFields = dataFields.get(i).getSubFields();
            String subFieldVal = null;
            for (int j = 0; j < subFields.size(); j++) {
                String code = subFields.get(j).getCode();
                String value = subFields.get(j).getValue();
                String modifiedVal = value.replace("\"", "&quot;");
                // modified code for jira - 4989
                /*if (modifiedVal != null) {
                    modifiedVal = modifiedVal.replace(" ", "&nbsp;");
                }*/

                if (subFieldVal != null) {
                    subFieldVal = subFieldVal + "|" + code + " "+modifiedVal+" ";
                } else {
                    subFieldVal = "|" + code + " "+modifiedVal+" ";
                }
            }
            marcEditorDataField.setValue(subFieldVal);
            marcEditorDataFields.add(marcEditorDataField);
        }
        return marcEditorDataFields;
    }

    /**
     * This method will get the title and Author fields from the marc data fields.
     *
     * @param dataFields
     * @return
     */
    public String buildMarcEditorTitleField(List<DataField> dataFields) {
        StringBuilder titleBuilder = new StringBuilder();
        String title = null;
        String author = null;
        for (DataField dataField : dataFields) {
            if (dataField.getTag().equalsIgnoreCase("245")) {
                List<SubField> subFields = dataField.getSubFields();
                for (SubField subField : subFields) {
                    if (subField.getCode().equalsIgnoreCase("a"))
                        if (subField.getValue() != null)
                            title = subField.getValue();
                }

            }

            if ((dataField.getTag().equalsIgnoreCase("100")) || (dataField.getTag().equalsIgnoreCase("110"))) {
                List<SubField> subFields = dataField.getSubFields();
                for (SubField subField : subFields) {
                    if (subField.getCode().equalsIgnoreCase("a")) {
                        if (subField.getValue() != null) {
                            author = subField.getValue();
                        }

                    }
                }

            }
        }
        if (!StringUtils.isBlank(title)) {
            titleBuilder.append(title);

        }
        titleBuilder.append(" / ");
        if (!StringUtils.isBlank(author)) {
            titleBuilder.append(author);
        }
        return titleBuilder.toString();
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

    /**
     * This method will get the uuid from the marcEditorForm,
     * uifFormBase has type casted in to MarcEditorForm and from the marcEditorForm we will get the controlFields,
     * iterating the controlFields will return the uuid if tag value equal 001
     *
     * @param workBibMarcForm
     * @return Returns uuid
     */
    public String getUUIDFromForm(WorkBibMarcForm workBibMarcForm) {
        List<MarcEditorControlField> controlFields = workBibMarcForm.getControlFields();
        for (Iterator<MarcEditorControlField> iterator = controlFields.iterator(); iterator.hasNext(); ) {
            MarcEditorControlField controlField = iterator.next();
            if (controlField.getTag().equals("001")) {
                return controlField.getValue();
            }
        }
        return null;
    }

}
