package org.kuali.ole.describe.bo;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.kuali.ole.UnQualifiedDublinRecordHandler;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.DCValue;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.WorkBibDublinRecord;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.*;
import org.kuali.ole.pojo.dublin.unqualified.UnQualifiedDublinRecord;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * DublinEditorFormDataHandler is the data handler class for Dublin Editor
 */
public class WorkDublinEditorFormDataHandler {
    /**
     * This method returns response Xml from the docStore for Duplin Record, This method will ingest new record to docstore if uuid is null, else it will update existing record.
     * the toXml method in UnQualifiedDublinRecordHandler will convert the unQualifiedDublinRecord in to responseXml.
     *
     * @param uifFormBase
     * @param uuid
     * @return content
     */
    public String buildDublinRecordForDocStore(UifFormBase uifFormBase, String uuid) {

        String content = "";
        EditorForm dublinEditorForm = (EditorForm) uifFormBase;
        if (dublinEditorForm.getDocFormat().equals(DocFormat.DUBLIN_UNQUALIFIED.getCode())) {
            UnQualifiedDublinRecord unQualifiedDublinRecord = new UnQualifiedDublinRecord();
            if (uuid != null && !uuid.trim().equals("")) {
                unQualifiedDublinRecord.setListRecords(buildDublinDCValuesForEdit(dublinEditorForm));
            } else {
                unQualifiedDublinRecord.setListRecords(buildDublinDCValues(dublinEditorForm.getDublinFieldList()));
            }
            dublinEditorForm.setTitle(buildDublinUnqTitleField(dublinEditorForm, unQualifiedDublinRecord));
            content = new UnQualifiedDublinRecordHandler().toXml(unQualifiedDublinRecord);
        } else if (dublinEditorForm.getDocFormat().equals(DocFormat.DUBLIN_CORE.getCode())) {
            //System.out.println("Enter Dublin Save");
            Log.info("Enter Dublin Save");
            //TODO: code for building content.
            //            WorkBibDublinRecord workBibDublinRecord = new WorkBibDublinRecord();
            //            List<DCValue> dcValues = new ArrayList<DCValue>();
            //            DCValue dcValue = new DCValue();
            //            dcValue.setQualifier("contributor");
            //            dcValue.setElement("contributor");
            //            dcValue.setValue("contributor");
            //            dcValues.add(dcValue);
            //            workBibDublinRecord.setDcValues(dcValues);
            //            content = new QualifiedDublinRecordHandler().toXml(workBibDublinRecord);
        }
        return content;
    }

    /**
     * This method returns List of DublinDcValue records,
     * In this method each record contains a metadata with oaiDcDocList,
     * each oaiDcDocList contains list of oaiDcDoc with tagName and value.
     *
     * @param dublinFields
     * @return listRecords
     */
    private ListRecords buildDublinDCValues(List<WorkDublinEditorField> dublinFields) {
        ListRecords listRecords = new ListRecords();
        Record record = new Record();
        MetaData metaData = new MetaData();
        OaiDcDoc oaiDcDoc = new OaiDcDoc();
        List<OaiDcDoc> oaiDcDocList = new ArrayList<OaiDcDoc>();
        for (int i = 0; i < dublinFields.size(); i++) {
            if (!dublinFields.get(i).getElement().equalsIgnoreCase("default")) {
                oaiDcDoc.put(dublinFields.get(i).getElement(), dublinFields.get(i).getValue());
            }
        }
        oaiDcDocList.add(oaiDcDoc);
        metaData.setOaiDcDocs(oaiDcDocList);
        record.setMetadata(metaData);
        listRecords.addRecord(record);
        return listRecords;
    }

    private ListRecords buildDublinDCValues1(List<WorkDublinEditorField> dublinFields) {
        ListRecords listRecords = new ListRecords();
        Record record = new Record();
        MetaData metaData = new MetaData();
        OaiDcDoc oaiDcDoc = new OaiDcDoc();
        List<OaiDcDoc> oaiDcDocList = new ArrayList<OaiDcDoc>();
        for (int i = 0; i < dublinFields.size(); i++) {
            if (!dublinFields.get(i).getElement().equalsIgnoreCase("default")) {
                oaiDcDoc.put(dublinFields.get(i).getElement(), dublinFields.get(i).getValue());
            }
        }
        oaiDcDocList.add(oaiDcDoc);
        metaData.setOaiDcDocs(oaiDcDocList);
        record.setMetadata(metaData);
        listRecords.addRecord(record);
        return listRecords;
    }

    /**
     * This method returns List of  DublinEditorField,
     * each dublinEditorField object sets the tagName and value got from unQualifiedDublinRecord.
     *
     * @param unQualifiedDublinRecord
     * @return dublinEditorFieldList
     */

    public List<WorkDublinEditorField> buildDublinUnqEditorFields(EditorForm editorForm, UnQualifiedDublinRecord unQualifiedDublinRecord) {
        ListRecords listRecords = unQualifiedDublinRecord.getListRecords();
        List<Tag> tagList = listRecords.getRecords().get(0).getMetadata().getOaiDcDocs().get(0).getAllTags();
        //   List<WorkDublinEditorField> dublinEditorFieldList = editorForm.getDublinFieldList();
        List<WorkDublinEditorField> dublinEditorFieldList = new ArrayList<WorkDublinEditorField>();
        for (int i = 0; i < tagList.size(); i++) {
            WorkDublinEditorField dublinEditorField = new WorkDublinEditorField();
            dublinEditorField.setElement(tagList.get(i).getName());
            dublinEditorField.setValue(tagList.get(i).getValue());
            dublinEditorFieldList.add(dublinEditorField);
        }

        return dublinEditorFieldList;
    }

    public String buildDublinUnqTitleField(EditorForm editorForm, UnQualifiedDublinRecord unQualifiedDublinRecord) {

        String title = "";
        String author = "";
        ListRecords listRecords = unQualifiedDublinRecord.getListRecords();
        List<Tag> tagList = listRecords.getRecords().get(0).getMetadata().getOaiDcDocs().get(0).getAllTags();
        List<WorkDublinEditorField> dublinEditorFieldList = new ArrayList<WorkDublinEditorField>();
        for (Tag tag : tagList) {
            if (tag.getName().contains("title")) {
                if (!(StringUtils.isBlank(tag.getValue()))) {
                    title = tag.getValue();
                }
            }

            if (tag.getName().contains("creator")) {
                if (!StringUtils.isBlank(tag.getValue())) {
                    author = tag.getValue();
                }
            }
        }
        return title + " / " + author;

    }

    public List<WorkDublinEditorField> buildDublinEditorFields(EditorForm editorForm, WorkBibDublinRecord workBibDublinRecord) {
        List<DCValue> listRecords = workBibDublinRecord.getDcValues();
        List<WorkDublinEditorField> dublinEditorFieldList = editorForm.getDublinFieldList();
        for (int i = 0; i < listRecords.size(); i++) {
            WorkDublinEditorField dublinEditorField = new WorkDublinEditorField();
            dublinEditorField.setElement(listRecords.get(i).getElement());
            dublinEditorField.setValue(listRecords.get(i).getValue());
            dublinEditorFieldList.add(dublinEditorField);
        }
        return dublinEditorFieldList;
    }

    /**
     * This method returns updated ListRecords to be set in UnQualifiedDublinRecord  to update the existing docStore records.
     * In this method each record contains updated metadata with oaiDcDocList,
     * each oaiDcDocList contains updated list of oaiDcDoc with tagName and value.
     *
     * @param dublinEditorForm
     * @return listRecords
     */
    private ListRecords buildDublinDCValuesForEdit(EditorForm dublinEditorForm) {
        ListRecords listRecords = new ListRecords();
        String element = "";
        String value = "";
        Record record = new Record();
        List<String> elementList = new ArrayList<String>();
        MetaData metaData = new MetaData();
        OaiDcDoc oaiDcDoc = new OaiDcDoc();
        List<OaiDcDoc> oaiDcDocList = new ArrayList<OaiDcDoc>();
        List<WorkDublinEditorField> newElementList = dublinEditorForm.getDublinFieldList();
        List<WorkDublinEditorField> existingElementList = dublinEditorForm.getExistingDublinFieldList();
        for (int i = 0; i < newElementList.size(); i++) {
            if (!newElementList.get(i).getElement().equalsIgnoreCase("default")) {
                element = newElementList.get(i).getElement();
                value = newElementList.get(i).getValue();
                if (!elementList.contains(element)) {
                    elementList.add(element);
                    oaiDcDoc.put(element, value);
                }

            }
        }
//        for (int i = 0; i < existingElementList.size(); i++) {
//            if (!existingElementList.get(i).getElement().equalsIgnoreCase("default")) {
//                oaiDcDoc.put(existingElementList.get(i).getElement(), existingElementList.get(i).getValue());
//            }
//        }
        oaiDcDocList.add(oaiDcDoc);
        metaData.setOaiDcDocs(oaiDcDocList);
        record.setMetadata(metaData);
        listRecords.addRecord(record);
        return listRecords;
    }

}
