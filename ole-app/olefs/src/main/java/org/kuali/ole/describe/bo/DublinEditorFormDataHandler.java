package org.kuali.ole.describe.bo;

import org.kuali.ole.UnQualifiedDublinRecordHandler;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.*;
import org.kuali.ole.describe.form.DublinEditorForm;
import org.kuali.ole.pojo.dublin.unqualified.UnQualifiedDublinRecord;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * DublinEditorFormDataHandler is the data handler class for Dublin Editor
 */
public class DublinEditorFormDataHandler {
    /**
     * This method returns response Xml from the docStore for Duplin Record, This method will ingest new record to docstore if uuid is null, else it will update existing record.
     * the toXml method in UnQualifiedDublinRecordHandler will convert the unQualifiedDublinRecord in to responseXml.
     *
     * @param uifFormBase
     * @param uuid
     * @return content
     */
    public String buildDublinRecordForDocStore(UifFormBase uifFormBase, String uuid) {
        DublinEditorForm dublinEditorForm = (DublinEditorForm) uifFormBase;
        UnQualifiedDublinRecord unQualifiedDublinRecord = new UnQualifiedDublinRecord();
        if (uuid != null && !uuid.trim().equals("")) {
            unQualifiedDublinRecord.setListRecords(buildDublinDCValuesForEdit(dublinEditorForm));
        } else {
            unQualifiedDublinRecord.setListRecords(buildDublinDCValues(dublinEditorForm.getDublinFieldList()));
        }
        String content = new UnQualifiedDublinRecordHandler().toXml(unQualifiedDublinRecord);
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
    private ListRecords buildDublinDCValues(List<DublinEditorField> dublinFields) {
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

    public List<DublinEditorField> buildDublinEditorFields(UnQualifiedDublinRecord unQualifiedDublinRecord) {
        ListRecords listRecords = unQualifiedDublinRecord.getListRecords();
        List<Tag> tagList = listRecords.getRecords().get(0).getMetadata().getOaiDcDocs().get(0).getAllTags();
        List<DublinEditorField> dublinEditorFieldList = new ArrayList<DublinEditorField>();
        for (int i = 0; i < tagList.size(); i++) {
            DublinEditorField dublinEditorField = new DublinEditorField();
            dublinEditorField.setElement(tagList.get(i).getName());
            dublinEditorField.setValue(tagList.get(i).getValue());
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
    private ListRecords buildDublinDCValuesForEdit(DublinEditorForm dublinEditorForm) {
        ListRecords listRecords = new ListRecords();
        Record record = new Record();
        MetaData metaData = new MetaData();
        OaiDcDoc oaiDcDoc = new OaiDcDoc();
        List<OaiDcDoc> oaiDcDocList = new ArrayList<OaiDcDoc>();
        List<DublinEditorField> newElementList = dublinEditorForm.getDublinFieldList();
        List<DublinEditorField> existingElementList = dublinEditorForm.getExistingDublinFieldList();
        for (int i = 0; i < newElementList.size(); i++) {
            if (!newElementList.get(i).getElement().equalsIgnoreCase("default")) {
                oaiDcDoc.put(newElementList.get(i).getElement(), newElementList.get(i).getValue());
            }
        }
        for (int i = 0; i < existingElementList.size(); i++) {
            if (!existingElementList.get(i).getElement().equalsIgnoreCase("default")) {
                oaiDcDoc.put(existingElementList.get(i).getElement(), existingElementList.get(i).getValue());
            }
        }
        oaiDcDocList.add(oaiDcDoc);
        metaData.setOaiDcDocs(oaiDcDocList);
        record.setMetadata(metaData);
        listRecords.addRecord(record);
        return listRecords;
    }

}
