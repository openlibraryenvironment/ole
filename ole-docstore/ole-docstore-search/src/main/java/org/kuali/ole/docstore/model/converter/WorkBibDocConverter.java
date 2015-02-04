package org.kuali.ole.docstore.model.converter;

import org.kuali.ole.docstore.discovery.solr.work.bib.WorkBibCommonFields;
import org.kuali.ole.docstore.discovery.solr.work.bib.marc.WorkBibMarcDocBuilder;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 27/11/12
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibDocConverter
        implements WorkBibCommonFields {

    private static final Logger LOG = LoggerFactory.getLogger(WorkBibDocConverter.class);

    public WorkBibDocument convert(WorkBibMarcRecord marcRecord) {
        WorkBibDocument workBibDocument = new WorkBibDocument();
        WorkBibMarcDocBuilder workBibMarcDocBuilder = new WorkBibMarcDocBuilder();

        String field = AUTHOR_DISPLAY;
        Object fieldValue = workBibMarcDocBuilder.buildFieldValue(field, marcRecord);
        if (fieldValue instanceof List) {
            List<String> fieldValueList = (List<String>) fieldValue;
            workBibDocument.setAuthors(fieldValueList);
        } else if (fieldValue instanceof String) {
            String fieldVal = (String) fieldValue;
            workBibDocument.setAuthor(fieldVal);
        }

        field = TITLE_DISPLAY;
        fieldValue = workBibMarcDocBuilder.buildFieldValue(field, marcRecord);
        if (fieldValue instanceof List) {
            List<String> fieldValueList = (List<String>) fieldValue;
            workBibDocument.setTitles(fieldValueList);
        } else if (fieldValue instanceof String) {
            String fieldVal = (String) fieldValue;
            workBibDocument.setTitle(fieldVal);
        }

        field = PUBLICATIONDATE_DISPLAY;
        fieldValue = workBibMarcDocBuilder.buildFieldValue(field, marcRecord);
        if (fieldValue instanceof List) {
            List<String> fieldValueList = (List<String>) fieldValue;
            workBibDocument.setPublicationDates(fieldValueList);
        } else if (fieldValue instanceof String) {
            String fieldVal = (String) fieldValue;
            workBibDocument.setPublicationDate(fieldVal);
        }

        field = PUBLISHER_DISPLAY;
        fieldValue = workBibMarcDocBuilder.buildFieldValue(field, marcRecord);
        if (fieldValue instanceof List) {
            List<String> fieldValueList = (List<String>) fieldValue;
            workBibDocument.setPublishers(fieldValueList);
        } else if (fieldValue instanceof String) {
            String fieldVal = (String) fieldValue;
            workBibDocument.setPublisher(fieldVal);
        }

        field = EDITION_DISPLAY;
        fieldValue = workBibMarcDocBuilder.buildFieldValue(field, marcRecord);
        if (fieldValue instanceof List) {
            List<String> fieldValueList = (List<String>) fieldValue;
            workBibDocument.setEditions(fieldValueList);
        } else if (fieldValue instanceof String) {
            String fieldVal = (String) fieldValue;
            workBibDocument.setEdition(fieldVal);
        }

        return workBibDocument;
    }

    public List<WorkBibDocument> convert(WorkBibMarcRecords marcRecordList) {
        List<WorkBibDocument> workBibDocumentList = new ArrayList<WorkBibDocument>();
        for (WorkBibMarcRecord marcRecord : marcRecordList.getRecords()) {
            workBibDocumentList.add(convert(marcRecord));
        }
        return workBibDocumentList;
    }


}
